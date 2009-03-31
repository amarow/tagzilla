/*
 * Data.java
 *
 * Created on 24. Januar 2003, 01:30
 */

package de.ama.framework.data;

import de.ama.db.Query;
import de.ama.util.Environment;
import de.ama.util.Log;
import de.ama.util.StringDivider;
import de.ama.util.Util;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Andreas Marochow
 * Data-Objecte transportiern Daten vom RmiServiceIfc zum Client und wieder zurück.
 */
public abstract class Data implements Serializable {
    private int version = 0;
    private String oidString;

    private transient ArrayList queryDescriptions;


    /**
     * Werden zur Zeit für generische Editoren (SearchPanel) benötigt. Hat kein Einfluss auf Editoren.
     * @return
     */
    public String[] getUpperCaseFields() {
        return null;
    }
    public void preMain() {
        if (Environment.isServer()) {
              DataDictionary.registerData(this);
            }
    }

    public static boolean isDataClass(Class c) {
        return Data.class.isAssignableFrom(c);
    }

    /**
     * Hook zum festlegen der Spalte, nach der defaultmäßig sortiert werden soll.
     * Die Sortierung findet sofort nachdem die DataTable aus der Db geladen wurde statt.
     *
     * @return der MethodKey ohne session/set  !!Die Richtung kann hier ebenfalls angegeben werden.
     *         z.B. startdate.ascending wird ".ascending" nicht gefunden, so wird descending sortiert.
     */
    public String getDefaultSortKey() {
        return null;
    }

    public static Data createEmptyData(Class clazz) {
        //System.out.println("CREATE EMPTY DATA MIT "+clazz.getName());
        Data data = null;
        Exception exception = null;
        if (clazz != null) {
            try {
                data = (Data) clazz.newInstance();
            } catch (Exception e) {
                data = null;
                exception = e;
                exception.printStackTrace();
            }
        }
        if (data == null) {
            String msg = "could not create Data for Class " + (clazz == null ? "null" : clazz.getName());
            Util.showException(msg, exception);
        }
        return data;
    }

    ////////////////////////// abstract //////////////////////////////


    public String getBoClassName(){
        return Util.removeTrailingStrings(getClass().getName(),"Data");
    }

    /**
     * Soll ein spezieller Mapper benutzt werden so kann diese Methode im Data-Object
     * überschrieben werden.
     *
     * @return , ein spezieller Mapper.
     */
    public DataMapper getMapper() {
        return new DataMapperDefault();
    }

    ////////////////////////// Hooks vor und nach dem Mapping  //////////////////////////////

    public void preReadDataFromBo(Object obj)  {
    }

    public void postReadDataFromBo(Object obj) {
    }

    public void preWriteDataToBo(Object obj) throws MappingException {
    }

    public void postWriteDataToBo(Object obj) throws MappingException {
    }

    /**
     * Diese Methode wird auf jeden fall nach dem Mappen aufgerufen. Wenn sie überschrieben wird,
     * sollte hierin möglichst keine Exception mehr geworfen werden.
     *
     * @param bo
     */
    public void finalyWriteDataToBo(Object bo) {
    }


    /**
     * Hook zum löschen von Werten nach einer tiefen Copy.
     *
     */
    public void postDeepCopy()  {

    }
    
    /**
     * Dies Methode muss überschrieben werden um QueryDescriptions anzupassen.
     * - Es können Typ-Differenzen zwischen BO und Data angegeben werden.
     * - Es können Path-Querys angegeben werden.
     *
     * Wer nur Angaben zu bestimmten methodKeys machen will, aber sonst die Default QueryDescriptions, die für
     * die Einfachen Datentypen erzeugtwerden können mitbenutzen will schreibt
     *  super.createQueryDescriptions() zu Beginn der Methode
     *
     * Beispiel :
     *
     *  public void createQueryDescriptions() {
     *     super.createQueryDescriptions();
     *     addQueryDescription("shipmentCountry", "shipmentCountry{de.logas.basics.LandNeuImpl}->isoAlphaCode", String.class);
     *     addQueryDescription("wayOfTransportLangtext", "wayOfTransport{de.logas.basics.TransportWegNeuImpl}->code", String.class);
     *  }
     *
     *   ersetzt nur die QueryDefinitions für die MethodKeys : "shipmentCountry" und  "wayOfTransportLangtext"
     */
    public void createQueryDescriptions() {
        createDefaultQueryDescriptions();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public String getType() {
        return this.getClass().getName();
    }

    /**
     * Erzeugt ein Leeres Bo. Bo's müssen hierfür einen No-Arg-Konstruktor haben. In dieser
     * Methode wird die Information die im Hook getBoClassName() angegeben wird ausgewertet.
     *
     * @return  ein leeres BO.
     */
    public Object createEmptyBo() {
        String cName = getBoClassName();
        StringDivider sd = new StringDivider(cName, "|");
        if (sd.ok()) {
            cName = sd.post();
        }
        
        try {
            Class c = Class.forName(cName);
            Object obj = c.newInstance();
            return obj;
        } catch (Exception e) {
            Log.write("Could not create Bo with name " + cName, e);
            return null;
        }
    }
    
    /**
     * Beschreibt alle Felder die zwischen Bo und Data gemappet werden
     *
     * @return ein String-Array das
     */
    public String[] getFieldKeys() {
        List allFields = getAllFields(getClass());
        List fieldNames = new ArrayList();
        for (int i = 0; i < allFields.size(); i++) {
            String fieldName = ((Field) allFields.get(i)).getName();
            fieldNames.add(fieldName);
        }
        return (String[]) fieldNames.toArray(new String[0] );
    }

    public String[] getFieldKeysWithoutBoSetter() {
        return new String[0];
    }

    public String[] getFieldKeysWithoutBoGetter() {
        return new String[0];
    }
    
    private boolean isNoGetterField(String key){
        String[] noGetterFields=getFieldKeysWithoutBoGetter();
        for (int i = 0; i < noGetterFields.length; i++) {
            String noGetterField = noGetterFields[i];
            if(key.equals(noGetterField)){
                return true;
            }
        }
        return false;
    }
    
    public Class[] getFieldTypes() throws Exception {
        String[] keys = this.getFieldKeys();
        return getTypes(keys);
    }
    
    public Class[] getTypes(String[] keys) {
        Class[] types = new Class[keys.length];
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                try {
                    Field field = this.getClass().getField(keys[i]);
                    types[i] = field.getType();
                } catch (Exception e) {
                    Util.showException("Could not find Field <" + keys[i] + "> in Data [" + getClass().getName() + "] ", e);
                }
            }
        }
        return types;
    }
    
    /**
     * Sollte überschrieben werden wenn die Auflister-Tabelle nicht alle Field-Keys(Spalten) des
     * Data-Objects zeigen soll, oder die Reihenfolge verändert werden soll.
     *
     * @return ein String-Array mit den gewünschten FieldKeys(Spalten) für den Auflister.
     */
    public String[] getTableColKeys() {
        return getFieldKeys();
    }
    
    /**
     * Sollte überschrieben werden, wenn die Spaltenköpfe abweichende Bezeichner haben sollen.
     *
     * @param methodKey , der umzuschlüsselnde Wert
     * @return Spaltenkopfnamne, welcher an der Oberfläche noch durch den Translations-Mechanismus umgesetzt wird.
     */
    
    public String getTableColumnName(String methodKey) {
        return methodKey;
    }
    
    /**
     * Sollte überschrieben werden, wenn die Spalten vom Standard abweichende Cell-Renderer haben sollen.
     *
     * @param methodKey , der SpaltenSchlüssel
     * @return der für diese Spalte gewünschte CellRenderer
     */
    
    public TableCellRenderer getTableCellRenderer(String methodKey) {
        return null;
    }
    /**
     * Sollte überschrieben werden, wenn die Spalten vom Standard abweichende Cell-Editoren haben sollen.
     *
     * @param methodKey , der SpaltenSchlüssel
     * @return der für diese Spalte gewünschte Cell-Editor
     */
    
    public TableCellEditor getTableCellEditor(String methodKey) {
        return null;
    }
    
    
    /**
     * Liefert alle Types der TableCollumns in der Reihenfolge wie sie in getTableColKeys definiert
     * wurden als Class[].
     *
     * @return Class[] mit den Spalten-Typen(Klassen).
     * @throws MappingException
     */
    public Class[] getColumnTypes() throws MappingException {
        String[] keys = this.getTableColKeys();
        try {
            return getTypes(keys);
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }
    
    
    /**
     * Sollte überschrieben werden wenn die LookUp-Tabelle sich von der Auflister-Tabelle
     * unterscheiden soll (meistens weniger Spalten). Der erste columnKey ist dann auch der
     * Default-Search-Key für die LookUps.
     *
     * @return ein String-Array mit den gewünschten FieldKeys(Spalten) für die LookUp-Dialoge.
     */
    public String[] getMiniTableColKeys() {
        return getTableColKeys();
    }
    
    /**
     * Liefert nur die collumnKeys, die für die in getGuiRepresentation und getGuiRepresentationLong benötigt
     * werden, durch diese Eingrenzung kann der Mapping-Vorgang für große DatenMmengen möglicherweise noch
     * beschleunigt werden, weil eben nur das nötigste gemappt wird.
     *
     * @return ein String-Array mit den gewünschten FieldKeys(Spalten) für die Gui-Representation.
     */
    
    public String[] getGuiRepTableColKeys() {
        return getMiniTableColKeys();
    }
    
    
    public ArrayList getQueryDescriptions() {
        if (queryDescriptions == null) {
            queryDescriptions = new ArrayList();
            createQueryDescriptions();
        }
        return queryDescriptions;
    }
    
    public void createDefaultQueryDescriptions() {
        String[] colKeys = getTableColKeys();
        for (int i = 0; i < colKeys.length; i++) {
            String colKey = colKeys[i];
            addQueryDescription(colKey);
        }

        colKeys = getMiniTableColKeys();
        for (int i = 0; i < colKeys.length; i++) {
            String colKey = colKeys[i];
            addQueryDescription(colKey);
        }

        addQueryDescription(getSearchColumnKey());
    }
    
    /**
     * Fügt eine Default QueryDescription hinzu.
     * Achtung ! Der Vorgang kann abgelehnt werden, wenn der Typ nicht ermittelbar ist.
     * z.B. bey DataReferenze oder DataProxy oder Data.
     * @param methodKey
     */
    public void addQueryDescription(String methodKey) {
        Class fieldType = getFieldType(methodKey);
        if (fieldType == String.class ||
                fieldType == Integer.class || fieldType == int.class ||
                fieldType == Double.class || fieldType == double.class ||
                fieldType == Boolean .class || fieldType == boolean.class ||
                fieldType == Long.class || fieldType == long.class ||
                fieldType == Date.class
                ) {
            addQueryDescription(new QueryDescription(methodKey, methodKey, fieldType));
        }
    }

    public void addQueryDescription(String methodKey, String path, Class type) {
        addQueryDescription(new QueryDescription(methodKey, path, type, ""));
    }

    public void addQueryDescription(String methodKey, String path, Class type, String startCondition) {
        addQueryDescription(new QueryDescription(methodKey, path, type, startCondition));
    }

    public void addQueryDescription(String methodKey, String startCondition) {
        addQueryDescription(new QueryDescription(methodKey, methodKey, getFieldType(methodKey), startCondition));
    }

    public void addQueryDescription(QueryDescription qd) {
        if (getQueryDescrition(qd.getMethodKey()) == null) {
            getQueryDescriptions().add(qd);
        }
    }


    public void removeQueryDescription(String methodKey) {
        QueryDescription qd = getQueryDescrition(methodKey);
        if (qd != null) {
            getQueryDescriptions().remove(qd);
        }
    }

    public QueryDescription getQueryDescrition(String colKey) {
        List qd = getQueryDescriptions();
        for (int i = 0; i < qd.size(); i++) {
            QueryDescription queryDescrition = (QueryDescription) qd.get(i);
            if (queryDescrition.getMethodKey().equals(colKey)) {
                return queryDescrition;
            }
        }
        return null;
    }


    public boolean isQueryCollumn(String key) {
        return (getQueryDescrition(key) != null);
    }

//    /**
//     * Diese Methode liefert einen Query-String für LookUps. Die DEfaultImplementierung
//     * Hängt wenn nicht schon vorhanden noch einen '*' an.
//     *
//     * @param searchColumnKey
//     * @param input
//     * @return  ein String in dieser Form "searchColumnKey:value";
//     */
//    public String prepareLookUpQuery(String searchColumnKey, String input) {
//        if (searchColumnKey == null || searchColumnKey.trim().length() == 0) {
//            searchColumnKey = getSearchColumnKey();
//        }
//
//        String condition="";
//        if(input!=null && input.length()>0){
//           condition = searchColumnKey + ":" + input;
//        }
//
//        return condition;
//    }

    public String getSearchColumnKey() {
        return getMiniTableColKeys()[0];
    }

    public Class getFieldType(String fieldName) {
        Class[] types = getTypes(new String[]{fieldName});
        if (types.length > 0) {
            return types[0];
        }
        throw new IllegalArgumentException("can not find field with name " + fieldName + " -> can not give Type of Field");

    }

    public Object getValue(String key) throws MappingException {
        StringDivider sd = new StringDivider(key, ".", true);
        if (sd.ok()) {
            Data data = (Data) getValue(sd.pre());
            return data.getValue(sd.post());
        }

        try {
            Field field = getClass().getField(key);
            return field.get(this);
        } catch (Exception e) {
            throw new MappingException("field=" + key, e);
        }

    }

    public void setValue(String key, Object val) throws MappingException {
        StringDivider sd = new StringDivider(key, ".", true);
        if (sd.ok()) {
            Data data = (Data) getValue(sd.pre());
            //System.out.println("go deeper to ---> "+data.getClass().getName());
            data.setValue(sd.post(), val);
            return;
        }

        try {
            Field field = getClass().getField(key);
            field.set(this, val);

        } catch (Exception e) {
            throw new MappingException("field=" + key, e);
        }
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public java.lang.String getOidString() {
        return oidString;
    }

    public void setOidString(java.lang.String oidString) {
        this.oidString = oidString;
    }

    public boolean isNew() {
        return (oidString == null);
    }

    public String asString() {
        StringBuffer sb = new StringBuffer(getClass().getName() + "  " + oidString + ":" + version + " \r\n ");
        List allFields = getAllFields(getClass());
        for (int i = 0; i < allFields.size(); i++) {
            Field f = (Field) allFields.get(i);
            try {
                Object val = f.get(this);
                if (val instanceof Data) {
                    sb.append("DATA:\r\n");
                    Data data = (Data) val;
                    sb.append(data.asString());
                } else if (val instanceof List) {
                    List dataCol = (List) val;
                    sb.append("TABLE:\r\n");
                    for (int j = 0; j < dataCol.size(); j++) {
                        Object o = dataCol.get(j);
                        if (o instanceof Data) {
                            Data data = (Data) o;
                            sb.append(data.asString());
                        } else {
                            sb.append(o);
                        }
                    }

                } else {
                   sb.append(f.getName() + "=" + val + " \r\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static Field findField(Class c, String fieldName) {
        List list = getAllFields(c);
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Field f = (Field) iter.next();
            if (f.getName().equals(fieldName)) {
                return f;
            }
        }
        return null;
    }

    public static List getAllFields(Class c) {
        List fieldList = new ArrayList();
        Field[] fields = c.getFields();
        for (int i = 0; i < fields.length; i++) {
            int m = fields[i].getModifiers();
            if (!Modifier.isStatic(m) && !Modifier.isTransient(m) && Modifier.isPublic(m) ) {
                fieldList.add(fields[i]);
            }
        }
        return fieldList;
    }

    public void setGuiRepresentation(String rep) {
        String[] guiRepKeys = getGuiRepTableColKeys();
        if (guiRepKeys.length == 1) {
            String methodKey = guiRepKeys[0];
            try {
                setValue(methodKey, rep);
            } catch (MappingException e) {
                Log.write("setGuiRepresentation() failed please override to fit value type", e);
            }
        }
    }


    public Object getQueryValue(Class valueClass, String arg, String methodKey) throws MappingException {
        Object value = null;
        try {
            if (arg != null) {
                if (valueClass == Integer.class || valueClass == int.class) {
                    value = new Integer(arg);
                } else if (valueClass == Float.class || valueClass == float.class) {
                    value = new Float(arg);
                } else if (valueClass == Long.class || valueClass == long.class) {
                    value = new Long(arg);
                } else if (valueClass == Double.class || valueClass == double.class) {
                    value = new Double(arg);
                } else if (valueClass == Boolean.class || valueClass == boolean.class) {
                    value = new Boolean(arg);
                } else if (valueClass == Date.class ) {
                    Date date = new Date(arg);
                    value = Util.asDBString(date);
                } else {
                    value = arg;
                }
            }
        } catch (Exception e) {
            throw new MappingException("wrong query argument see [" + arg + "]");
        }
        return value;
    }

    public String[] getQueryGuiValues(String methodKey) {
        return null;
    }

    /**
     * Hook, mit dem bestimmt werden kann ob eine Lookup-Table für diese Data-Klasse erlaubt ist. Wenn hier mit false
     * geantwortet wird, werden immer die aktuellen Daten vom RmiServiceIfc geholt (für volatile Bewegungsdaten geeignet).
     *
     * @return
     */
    public boolean isCashable() {
        return true;
    }

    private transient boolean recursionPreventer = false;

    public final Query  buildThermQuery(String part) throws MappingException {
        // Beispiel therm = "countryOfOrigin:IN";
        Query  query = null;
        StringDivider sd = new StringDivider(part, ":");
        if (!recursionPreventer && sd.ok()) {
            recursionPreventer = true;
            query = buildExtendedThermQuery(sd.pre(), sd.post());
        }
        recursionPreventer = false;
        return query;
    }

    /**
     * Hook mit dem ein ganzer Query-Ausdruck für eine Spalte erzeugt werden kann. Z.B. Erweiterung auf unsichtbare Attribute.
     *
     * @return Raus kommt die komplette Querydefinition für diese Spalte
     */
    public Query  buildExtendedThermQuery(String methodKey, String value)  throws MappingException {
        return null;
    }

    public String asCsvLine() {
        StringBuffer sb = new StringBuffer();
        List fields = getAllFields(getClass());
        for (int i = 0; i < fields.size(); i++) {
            Field f = (Field) fields.get(i);
            if (f.getType().isPrimitive()) {
                try {
                    sb.append(f.get(this));
                    sb.append(',');
                } catch (IllegalAccessException e) {
                    sb.append(e.getMessage());
                }
            }
        }
        return sb.toString() + Util.CRLF;
    }

    public String getXmlName(){
        return Util.getUnqualifiedClassName(getClass());
    }

    public String asXMLString(String rootName) throws IllegalAccessException {
         return asXMLString(rootName, false);
    }

    public  static int level = 0;
    public String asXMLString(String rootName, boolean printFormat) throws IllegalAccessException {
        level++;
        String indent="";
        for(int x=0;x<Data.level;x++)
            indent +="  ";

        StringBuffer sb = new StringBuffer();
        sb.append(indent+"<").append(rootName).append(">"+Util.CRLF);
        List allFields = getAllFields(getClass());
        for (int i = 0; i < allFields.size(); i++) {
            Field f = (Field) allFields.get(i);
                Object val = f.get(this);
                if (val instanceof Data) {
                    Data data = (Data) val;
                    sb.append(data.asXMLString(f.getName()));
                } else if (val instanceof List) {
                    List dataCol = (List) val;
                    for (int j = 0; j < dataCol.size(); j++) {
                        Object o = dataCol.get(j);
                        if (o instanceof Data) {
                            Data data = (Data) o;
                            sb.append(data.asXMLString(f.getName(),printFormat));
                        } else {
                            sb.append(Util.asXMLString(f.getName(),o));
                        }
                    }
                } else {
                    sb.append(indent+"   "+Util.asXMLString(f.getName(),val,printFormat)+Util.CRLF);
                }
        }
        sb.append(indent+"   "+Util.asXMLString("oid",getOidString(),printFormat)+Util.CRLF);
        sb.append(indent+"   "+Util.asXMLString("version",""+getVersion(),printFormat)+Util.CRLF);
        sb.append(indent+"</").append(rootName).append(">"+Util.CRLF);
        level--;
        return sb.toString();
    }

    /**
     * Fügt zwei String-Arrays zusammen. null als Argument wird wie ein leeres Array interpretiert.
     *
     * @return ein Array, das alle Elemente von array1 und array2 enthält.
     */
    public String[] concat(String[] array1, String[] array2) {
        return Util.concat(array1, array2);
        }

    public void setStringValue(String attrib, String userInput) {
        System.out.println("Data.setStringValue attr="+attrib+" val="+userInput);
    }

}
