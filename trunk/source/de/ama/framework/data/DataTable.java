/*
 * TableModel.java
 *
 * Created on 18. Januar 2003, 01:48
 */

package de.ama.framework.data;

import de.ama.util.Util;

import javax.swing.table.AbstractTableModel;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Andreas Marochow
 * DataTable ist ein TableModel zum anzeigen in JTables, und gleichzeitig auch das Transportmedium
 * für Data-Listen. In der letzten (unsichtbaren) Column einer DataTable sind die DataProxy's verborgen.
 * Wenn auf dem Client daten verändert oder angelegt werden, hängen an diesen DataProxy's echte Data-Objekte, die
 * auch wieder in die Bo's zurückgemappt werden. Gegebenefalls werden auch neue Bo's erzeugt und gespeichert.
 * Mann kann eine DataTable auch das Löschen aus der Datenbank übertragen, indem mann DataTable.setIsDeletingBos(true) sagt.
 * Ist dieser Parameter auf false (Default) werden die Bo's nur aus dem entspr. Container entfernt.
 */
public class DataTable extends AbstractTableModel implements Externalizable {
    //private List deletedProxys = new ArrayList();
    //private List newProxys = new ArrayList();
    private List rows = new ArrayList();
    private List heads = new ArrayList();
    private Class type;
    private String containerId;
    private boolean isDeletingBos;
    private String sortKey;
    private String name;     // wichtig für die GuiREpresentation, wird auf Client mit Lang-translate übersetzt.
    private boolean containsReferences;  // enthällt nur references
    private boolean containsHeavyProxys; // datas an den Proxys werden immer mitgestreamt.
    private String delimiter="{delim}";  // falls auf einen String im Bo gemapped werden soll, ist dies der Delimiter


    static final long serialVersionUID = -1L;

    ////////////////////// Factory Methods //////////////////////////////////////////

    /**
     * Wenn die DataTable nur DataReferences enthalten soll, ist die FactoryMethode zu verwenden.
     * Stellt sicher das die enthaltenen Objecte nicht kopiert werden sondern nur Kompositionnen
     * darstellen (z.B. zu Stammdaten hin)
     *
     * @param dataClass , der Typ der Table.
     * @return eine leere DataTable
     */
    public static DataTable createReferenceTableFromDataClass(Class dataClass) {
        DataTable dt = createFromDataClass(dataClass, false);
        dt.setContainsReferences(true);
        return dt;
    }

    public boolean containsReferences() {
        return containsReferences;
    }

    /**
     * Wenn die DataTable nur heavy Proxys enthalten soll, ist die FactoryMethode zu verwenden.
     *
     * @param dataClass , der Typ der Table.
     * @return eine leere DataTable
     */
    public static DataTable createHeavyTableFromDataClass(Class dataClass) {
        DataTable dt = createFromDataClass(dataClass, false);
        dt.setContainsHeavyProxys(true);
        return dt;
    }

    public boolean containsHeavyProxys() {
        return containsHeavyProxys;
    }


    /**
     * Factory-Methode zum erzeugen einer leeren DataTable.
     *
     * @param dataClass , der Typ der Table.
     * @return eine leere DataTable
     */
    public static DataTable createFromDataClass(Class dataClass) {
        return createFromDataClass(dataClass, false);
    }
    /**
     * Factory-Methode zum erzeugen einer leeren DataTable.
     *
     * @param dataClass , der Typ der Table.
     * @return eine leere DataTable
     */
    public static DataTable createFromDataClass(Class dataClass, String delim) {
        DataTable dt= createFromDataClass(dataClass, false);
        dt.setDelimitter(delim);
        return dt;
    }

    public void setDelimitter(String delim) {
        this.delimiter=delim;
    }

    /**
     * Factory-Methode zum erzeugen einer leeren DataTable.
     *
     * @param mini Ist zu benutzen, wenn nur eine schmale LookupTable gebraucht wird.
     * @param dataClass , der Typ der Table.
     * @return eine leere DataTable
     */
    public static DataTable createFromDataClass(Class dataClass, boolean mini) {
        Data data = Data.createEmptyData(dataClass);
        return createFromData(data, mini);
    }

    /**
     * Factory-Methode zum erzeugen einer leeren DataTable.
     *
     * @param mini Ist zu benutzen, wenn nur eine schmale LookupTable gebraucht wird.
     * @param data , der Typ der Table.
     * @return eine leere DataTable
     */
    public static DataTable createFromData(Data data, boolean mini) {
        String[] colKeys = mini ? data.getMiniTableColKeys() : data.getTableColKeys();
        Class[] types = data.getTypes(colKeys);
        DataTable table = new DataTable(data.getClass());

        if (colKeys != null) {
            for (int i = 0; i < colKeys.length; i++) {
                table.addColumn(new DataTableHead(types[i], false, colKeys[i], data.isQueryCollumn(colKeys[i])));
            }
        }
        table.setSortKey(data.getDefaultSortKey());
        return table;
    }

    public static DataTable createFromBoList(Class typ, Collection boList, boolean lookupList) throws MappingException{
        Data data = Data.createEmptyData(typ);
        DataMapper mapper = data.getMapper();
        return mapper.createFromBoList(data, boList, lookupList);
    }


    ////////////////////// Factory Methods //////////////////////////////////////////

    /**
     * Wenn die DataTable nur DataReferences enthalten soll, ist hier true zu setzen.
     * Stellt sicher das die enthaltenen Objecte nicht kopiert werden sondern nur Kompositionnen
     * darstellen (z.B. zu Stammdaten hin)
     */
    public void setContainsReferences(boolean containsReferences) {
        this.containsReferences = containsReferences;
    }


    public void setContainsHeavyProxys(boolean containsHeavyProxys) {
        this.containsHeavyProxys = containsHeavyProxys;
    }

    public int findColumnByKey(String columnKey) {
        for (int i = 0; i < heads.size(); i++) {
            DataTableHead head = (DataTableHead) heads.get(i);
            if (head.methodKey.equals(columnKey)) {
                return i;
            }
        }
        return -1;
    }

    private DataTable(Class type) {
        this.type = type;
        this.name = Util.getUnqualifiedClassName(type);
    }

    /**
     *  Returns false.  This is the default implementation for all cells.
     *
     *  @param  rowIndex  the row being queried
     *  @param  columnIndex the column being queried
     *  @return false
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return ((DataTableHead) heads.get(columnIndex)).editable;
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount() {
        return heads.size();
    }


    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex the column whose value is to be queried
     * @return	the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        List row = (List) rows.get(rowIndex);
        Object val = row.get(columnIndex);
        //        if (val.getClass() == Date.class) {
        //            val=Util.asString((Date)val);
        //        }
        return val;
    }

    public Object getValueAt(DataProxy proxy, String methodKey) {
        List row = findRow(proxy);
        int col = findColumnByKey(methodKey);
        return row.get(col);
    }

    public void setValueAt(Object o, int rowIndex, int columnIndex) {
        List row = (List) rows.get(rowIndex);
        row.set(columnIndex, o);
        try {
            Data data = getDataProxy(rowIndex).getData(false);
            data.setValue(getColumnKey(columnIndex), o);
            fillRow(row, data);
        } catch (MappingException e) {
            Util.showException("can not directUpdate Data-Object", e);
        }

        //  Dies passiert m. E alles schon in fillRow.
//        if (getColumnName(columnIndex).equals(getSortKey())) {
//            sort();
//        } else {
//            fireTableRowsUpdated(rowIndex, rowIndex);
//        }
    }


    public String getColumnName(int i) {
        return ((DataTableHead) heads.get(i)).getCaption();
    }


//    public String setColumnName(int i, String str) {
//        return ((DataTableHead) heads.session(i)).caption = str;
//    }

    public void setColumnEditable(int i, boolean editable) {
        ((DataTableHead) heads.get(i)).editable = editable;
    }

    public String getColumnKey(int i) {
        return ((DataTableHead) heads.get(i)).methodKey;
    }

    public Class getColumnClass(int i) {
        //return String.class;
        return ((DataTableHead) heads.get(i)).type;
    }

    public int getColumnWidth(int i) {
        return ((DataTableHead) heads.get(i)).width;
    }

    public void setColumnWidth(int col, int width) {
        ((DataTableHead) heads.get(col)).width = width;
    }

    public boolean isColumnQueryAble(int i) {
        return ((DataTableHead) heads.get(i)).queryAble;
    }

    public void setColumnQueryAble(int i, boolean how) {
        ((DataTableHead) heads.get(i)).queryAble = how;
        if (how == false) {
            ((DataTableHead) heads.get(i)).queryString = "";
        }
    }

    public Object getRow(int index) {
        return (List) rows.get(index);
    }

    public void addColumn(DataTableHead head) {
        heads.add(head);
    }

    public void clear() {
        heads.clear();
        removeAllRows();
    }

    public void flush() {
        removeAllRows();
        //deletedProxys.clear();
        //newProxys.clear();
    }


    ///////////////////////////////  removing Rows //////////////////////////////////////
    public void removeRows(int[] ixs) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = ixs.length - 1; i >= 0; i--) {
            int index = ixs[i];
            if (index > max) max = index;
            if (index < min) min = index;
            DataProxy dp = getDataProxy(index);
//            if (!dp.hasNewData()) {
//                deletedProxys.add(dp);
//            }
            rows.remove(index);
        }
        fireTableRowsDeleted(min, max);
    }

    public void removeRow(List row) {
        int index = rows.indexOf(row);
        removeRow(index);
    }


    public void removeRow(DataProxy dp) {
        List row = findRow(dp);
        if (row != null) {
            removeRow(row);
        }
    }

    public void removeRow(Data data) {
        if (data == null) return;
        removeRow(data.getDataProxy());
    }

    public void removeRow(int index) {
        if (index >= 0 && index < rows.size()) {
            DataProxy dp = getDataProxy(index);
//            if (!dp.hasNewData()) {
//                deletedProxys.add(dp);
//            } else {
//                //newProxys.remove(dp);
//            }
            rows.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    public void removeAllRows() {
        if (getRowCount() > 0) {
            int i = getRowCount() - 1;
            rows.clear();
            fireTableRowsDeleted(0, i);
        }
    }

    ////////////////////////////////  adding Rows //////////////////////////////////////////

    public List newRow() {
        List row = new ArrayList();
        addRow(row);
        return row;
    }

    public void addRow(List row) {
        rows.add(row);
        int index = rows.size() - 1;

        fireTableRowsInserted(index, index);
    }

    public void addRow(Data data) {
        if (data == null) return;
        List row = new ArrayList();
        fillRow(row, data);
//        if (data.isNew()) {
//            newProxys.add(data.getDataProxy());
//        }
        addRow(row);
    }


    public boolean updateRow(Data data) {
        if (data == null) return false;
        List row = findRow(data.getDataProxy());
        if (row != null) {
            int index = rows.indexOf(row);
            fillRow(row, data);
            return true;
        } else {
            return false;
        }
    }

    public List findRow(DataProxy proxy) {
        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            List row = (List) iter.next();
            DataProxy dp = (DataProxy) row.get(getColumnCount());
            if (proxy.equals(dp)) {
                return row;
            }
        }
        return null;
    }

    public int getRowIndex(List row) {
        return rows.indexOf(row);
    }

    public DataProxy getDataProxy(int rowIndex) {
        List row = (List) getRow(rowIndex);
        return (DataProxy) row.get(getColumnCount());
    }

    public List getDataProxys() {
        List proxys = new ArrayList();
        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            List row = (List) iter.next();
            DataProxy dp = (DataProxy) row.get(getColumnCount());
            proxys.add(dp);
        }
        return proxys;
    }

    public void replaceRow(DataProxy dp, Data newData) {
        List row = findRow(dp);
        if (row != null) {
            fillRow(row, newData);
//            if(dp.hasReference()){
//               deletedProxys.add(dp);
//            }
        }
    }


    public void fillRow(List row, Data data) {
        Data  oldData = null;
        if(row.size()>0){
           DataProxy dp =  (DataProxy) row.get(getColumnCount());
           if(dp!=null && dp.hasData()){
              oldData = dp.getData(false);
           }
        }
        row.clear();
        for (int i = 0; i < heads.size(); i++) {
            DataTableHead h = (DataTableHead) heads.get(i);
            Object val = null;
            try {
                val = data.getValue(h.methodKey);
            } catch (MappingException e) {
                val = " ? " + h.methodKey;
            }
            if (val != null) {
//                if (val instanceof Data) {
//                    val = ((Data) val).getGuiRepresentation();
//                } else if (val instanceof DataProxy) {
//                    val = ((DataProxy) val).getGuiRepresentation();
//                }
                row.add(val);
            } else {
                row.add("");   // wichtig damit col index stimmt für Proxy in letzter Spalte !!!
            }
        }
        DataProxy dp = data.getDataProxy();
        dp.makeUnique();
        row.add(dp);
    }

    public List getRows() {
        return rows;
    }

    public List getCols() {
        return heads;
    }

    public String[] getColKeys() {
        String[] colKeys = new String[heads.size()];
        for (int i = 0; i < heads.size(); i++) {
            DataTableHead h = (DataTableHead) heads.get(i);
            colKeys[i] = h.methodKey;
        }
        return colKeys;
    }

    public void append(DataTable table) {
        if (getColumnCount() == 0) {
            heads = table.getCols();
        } else if (table.getColumnCount() != getColumnCount()) {
            throw new IllegalArgumentException("Error in append:DataTable columnSizes wrong " + getColumnCount() + "<>" + table.getColumnCount());
        }
        rows.addAll(table.getRows());
    }


    private boolean hasSortKey() {
        return sortKey != null;
    }

    public String toString() {
        return getGuiRepresentation();
    }

    public String getGuiRepresentation() {
        String ret = "";
        for (int i = 0; i < rows.size(); i++) {
            if(ret.length()>0) ret+= ",";
            ret+=getDataProxy(i).getGuiRepresentation();
            if(i>10) break;
        }
        return ret;
    }

    public String asString() {
        int cellwidth = 20;
        String line = "\n\r|";
        StringBuffer buf = new StringBuffer("DataTable rows=" + getRowCount() + " cols=" + getColumnCount() + " " + type.getName() + ":"
                + getContainerId() + "\r\n|");
        Iterator col = heads.iterator();
        while (col.hasNext()) {
            DataTableHead o = (DataTableHead) col.next();
            buf.append(Util.formatString(o.getCaption(), cellwidth));
            buf.append("|");
            line += Util.formatString("-------------------------------------------------------------------------------------", cellwidth);
            line += "+";
        }
        buf.append(Util.formatString("DataProxy", cellwidth) + "|");
        line += Util.formatString("-------------------------------------------------------------------------------------", 80);
        line += "|\r\n|";
        buf.append(line);
        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            List row = (List) iter.next();
            col = row.iterator();
            int count = 0;
            while (col.hasNext()) {
                Object o = col.next();
                buf.append(Util.formatString(o.toString(), (count == heads.size()) ? 80 : cellwidth));
                buf.append("|");
                count++;
            }
            buf.append(line);
        }
        buf.setLength(buf.length() - 1);
        return buf.toString();
    }

    public boolean isContainerTable() {
        return (containerId != null && containerId.length() > 0);
    }


//    public java.util.List getDeletedProxys() {
//        return deletedProxys;
//    }

//    public java.util.List getNewProxys() {
//        return newProxys;
//    }


    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }


    public void setContainerId(String parentOidString, String methodKey) {
        containerId = parentOidString + "(method)" + methodKey;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }


    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getContainerId() {
        return containerId;
    }

    public boolean isDeletingBos() {
        return isDeletingBos;
    }

    public void setIsDeletingBos(boolean isDeletingBos) {
        this.isDeletingBos = isDeletingBos;
    }

    public int findRowWithValue(Object value) {
       return findRowWithValue(value,true);
    }

    public int findRowWithValue(Object value, boolean exact) {
        for (int col = 0; col < getColumnCount(); col++) {
            for (int row = 0; row < getRowCount(); row++) {
                if(exact){
                if (getValueAt(row, col).equals(value)) {
                    return row;
                }
                } else {
                    if (getValueAt(row, col).toString().indexOf(value.toString())>=0) {
                        return row;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * Ist nur für Externalizable nötig
     */
    public DataTable() {
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        //deletedProxys = (List) in.readObject();
        //newProxys = (List)in.readObject();
        rows = (List) in.readObject();
        heads = (List) in.readObject();
        type = (Class) in.readObject();
        containerId = (String) in.readObject();
        isDeletingBos = in.readBoolean();
        sortKey = (String) in.readObject();
        name = (String) in.readObject();
        delimiter = (String) in.readObject();
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        //out.writeObject(deletedProxys);
        //out.writeObject(newProxys);
        out.writeObject(rows);
        out.writeObject(heads);
        out.writeObject(type);
        out.writeObject(containerId);
        out.writeBoolean(isDeletingBos);
        out.writeObject(sortKey);
        out.writeObject(name);
        out.writeObject(delimiter);
    }

    public void prepareDeepCopy() throws Exception {
        // je nachdem ob wir Referenzen oder Proxys enthalten wird
        // hier nachgeladen für die Kopie, oder weggeworfen.
        List proxys = getDataProxys();
        for (int i = 0; i < proxys.size(); i++) {
            DataProxy proxy = (DataProxy) proxys.get(i);
            if (containsReferences || proxy.isReference()) {
                proxy.unload();
            } else {
                proxy.getData(false);
            }
        }
    }



    public String convertToDelimitterSeperatedString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < getRowCount(); i++) {
            StringData sd = (StringData) getDataProxy(i).getData(false);
            if (i > 0) {
                sb.append(delimiter);
            }
            sb.append(sd.value);
        }
//        System.out.println("DataTable.convertToDelimitterSeperatedString String= ["+sb+"]\"");
        return sb.toString();
    }

    public void readDelimitterSeperatedString(String str) {
//        System.out.println("DataTable.readDelimitterSeperatedString String= ["+str+"]");
        List l = Util.asList(str, delimiter);
        flush();
        for (int i = 0; i < l.size(); i++) {
            addRow(new StringData(l.get(i)));
        }
    }

    public int size() {
        return getRowCount();
    }


    public String getColumnQueryString(int col) {

        try {
            return ((DataTableHead) heads.get(col)).queryString.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void setColumnQueryString(int col, String queryString) {
        ((DataTableHead) heads.get(col)).queryString = queryString;
    }

    public String getQueryString() {
        String ret = "";
        for (int i = 0; i < heads.size(); i++) {
            DataTableHead head = (DataTableHead) heads.get(i);
            if (head.queryString != null) {
                head.queryString = head.queryString.trim();
                if (head.queryString.length() > 0) {
                    if (ret.length() > 1) ret += "|";
                    ret += head.methodKey + ":" + Util.replaceSubString(head.queryString, ",", "," + head.methodKey + ":");
                }
            }
        }
        return ret;
    }

    public String asCsvTable() {
        StringBuffer sb = new StringBuffer();
        List proxys = getDataProxys();
        for (int i = 0; i < proxys.size(); i++) {
            DataProxy proxy = (DataProxy) proxys.get(i);
            Data d = proxy.getData(false);
            sb.append(d.asCsvLine());
        }
        return sb.toString();
    }

    public String asXMLString(boolean printFormat) throws IllegalAccessException {
        Data.level++;
        StringBuffer sb = new StringBuffer();
        String indent="";
        for(int x=0;x<Data.level;x++)
            indent +="  ";

        sb.append(indent+"<"+Util.saveToString(name)+"Table>"+Util.CRLF);
        sb.append(indent+"<tableMeta>"+Util.CRLF);
        sb.append(indent+Util.asXMLString("colCount",new Integer(getColumnCount()),false)+Util.CRLF);
        sb.append(indent+Util.asXMLString("rowCount",new Integer(getRowCount()),false)+Util.CRLF);
        sb.append(indent+Util.asXMLString("full",(getRowCount()>0?"true":"false"),false)+Util.CRLF);
        sb.append(indent+"</tableMeta>"+Util.CRLF);

        sb.append(indent+"<tableHeader>"+Util.CRLF);
        for(int col = 0; col < getColumnCount(); col++){
           sb.append(indent+"   "+Util.asXMLString("element",getColumnKey(col)+"|"+getColumnName(col),false)+Util.CRLF);
        }
        sb.append(indent+"</tableHeader>").append(Util.CRLF);

        List proxys = getDataProxys();
        for (int i = 0; i < proxys.size(); i++) {
            DataProxy proxy = (DataProxy) proxys.get(i);
            Data d = proxy.getData(printFormat);
            sb.append( d.asXMLString(d.getXmlName(),printFormat) );
        }
        sb.append(indent+"</"+Util.saveToString(name)+"Table>").append(Util.CRLF);
        Data.level--;
        return sb.toString();
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }


}
