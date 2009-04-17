/*
 * DataMapperDefault.java
 *
 * Created on 31. August 2003, 21:42
 */

package de.ama.framework.data;

import de.ama.db.DB;
import de.ama.db.OidIterator;
import de.ama.db.Persistent;
import de.ama.db.Query;
import de.ama.util.StrTokenizer;
import de.ama.util.StringDivider;
import de.ama.util.Util;

import java.util.*;

/**
 * @author ama
 *         Dieser Mapper übernimmt das generische mappen aus Business-Objekten in Data-Objekte und zurück.
 *         Spezielle Mapper werden in der  entspr. Data-Klasse vereinbart, indem diese den Mapper über getMapper() herausgibt.
 */

public abstract class DataMapper {

    public final static int REFERENCE = 0;
    public final static int MINI_TABLE = 1;
    public final static int BIG_TABLE = 2;
    public final static int FULL_OBJECT = 3;
    public final static int XML = 4;

    ////////////////////////////// abstracts ///////////////////////////////////////
    // In Mapper-Derivaten müssen diese Methoden überschrieben werden.

    public abstract void writeDataToBo(Object bo, Data rootData, String[] keys) ;

    public abstract void readDataFromBo(Object bo, Data rootData, String[] keys) ;

    ////////////////////////////// abstracts ///////////////////////////////////////

    public void checkVersion(Object bo, Data rootData) throws MappingException {
        if (bo instanceof Persistent) {
            // Optimistic Locking Kontrolle .
            int oldVersion = ((Persistent) bo).getVersion();
            if (!rootData.isNew() && rootData.getVersion() < oldVersion) {
                String msg = "Version MISMATCH dataVersion=" + rootData.getVersion() + " objectVersion=" + oldVersion + " for "
                        + bo.getClass().getName() + " oid=" + ((Persistent) bo).getOidString();
                System.out.println("**************************************************");
                System.out.println("  OPTIMISTIC LOCKING EXCEPTION");
                System.out.println("  " + msg);
                System.out.println("**************************************************");
                throw new MappingException(msg, MappingException.OPTIMISTIC_LOCKING_INVALID);

            }
        }
    }

    public void writeDataToBo(Object bo, Data rootData) throws MappingException {

        try {
            // Hook für den Entwickler des Data-Objekts
            rootData.preWriteDataToBo(bo);

            String[] keys = getMappingKeys(rootData, FULL_OBJECT);
            writeDataToBo(bo, rootData, keys);

            // Hook für den Entwickler des Data-Objekts
            rootData.postWriteDataToBo(bo);

        } finally {
            // Hook für den Entwickler des Data-Objekts
            rootData.finalyWriteDataToBo(bo);
        }

    }


    public Data readDataFromBo(Object bo, Data rootData, int type)  {
        readOidAndVersion(bo, rootData);

        // Hook für den Entwickler des Data-Objekts
        rootData.preReadDataFromBo(bo);

        String[] keys = getMappingKeys(rootData, type);
        readDataFromBo(bo, rootData, keys);

        // Hook für den Entwickler des Data-Objekts
        rootData.postReadDataFromBo(bo);

        return rootData;
    }

    /**
     * ausgelagert weil hier geprüft wird ob ein neues Bo angelegt werden muss, oder nur upgedatet wird.
     *
     * @param bo
     * @param data
     * @param mb
     * @throws MappingException
     */

    public void writeChildBo(Object bo, Data data, MethodBinding mb) throws MappingException {
        Object childBo = null;
        if (!data.isNew()) {
            childBo = DB.session().getObject(data.getOidString());
        }

        if (childBo == null) {
            childBo = data.createEmptyBo();
        }

        data.getMapper().writeDataToBo(childBo, data);
        mb.setBoValue(bo, childBo);
    }

    public void readOidAndVersion(Object bo, Data rootData) {
        if (bo instanceof Persistent) {
            Persistent p = (Persistent) bo;
            rootData.setOidString(p.getOidString());
            rootData.setVersion(p.getVersion());
        }
    }

    public String[] getMappingKeys(Data rootData, int type) {
        String[] keys;
        if (type == REFERENCE) {
            keys = rootData.getGuiRepTableColKeys();
        } else if (type == MINI_TABLE) {
            keys = rootData.getMiniTableColKeys();
        } else if (type == BIG_TABLE) {
            keys = rootData.getTableColKeys();
        } else {
            keys = rootData.getFieldKeys();
        }
        return keys;
    }


    public DataTable createFromBoList(Data data, Collection boList, boolean mini) throws MappingException {
        DataTable table = new DataTable();
        if (boList != null) {
            readObjects( table, boList, mini);
        }
        return table;
    }

    public void writeObjects(Collection bos, DataTable table) throws MappingException {
        if (bos == null) {
            throw new MappingException("Try to write DataTable into Bo, but there is no Collection to write to !!!!" +
                    "\r\n Collections in Bo's should be initialized ");
        }

        // Wir merken uns die bisherige Collection.
        List  oldContainer = new ArrayList(bos);

        // Wir entleren die Collection und bauen sie neu auf.
        bos.clear();

        for (int i = 0; i < table.size(); i++) {
            Object o = table.get(i);
            if (o instanceof Data) {
                Data data = (Data) o;
                if(data.isNew()){
                    // bei ganz neuen Daten muß auch ein passendes Bo erzeugt werden.
                    o = data.createEmptyBo();
                } else {
                    // sonst nehnmen wir das alte BO.
                    o = DB.session().getObject(data.getOidString());
                }
                data.getMapper().writeDataToBo(o, data);
            }

            bos.add(o);
            oldContainer.remove(o);
        }

        if(table.deleting ){
           DB.session().deleteObjects(oldContainer);
        }

    }


    public void readObjects(DataTable dataCol, Collection objCol, boolean mini) throws MappingException {

        for (Iterator iterator = objCol.iterator(); iterator.hasNext();) {
            Object bo = iterator.next();
            if (bo == null) { continue; }

            Data data = DataDictionary.getDataForBo(bo,null);

            int mappingType = mini ? MINI_TABLE : BIG_TABLE;
            data = data.getMapper().readDataFromBo(bo, data, mappingType);

            dataCol.add(data);
        }

    }

    ////////////////////////////////////// für UpdateTableCommands //////////////////////////////////

    public static Query buildQuery(Data data, String condition) throws MappingException, ClassNotFoundException {

        Class target = Class.forName(data.getBoClassName());
        Query query = new Query(target);

        if (condition == null || condition.trim().length() < 1) {
            return query;   // leere Query ================>
        }

        StrTokenizer st = new StrTokenizer(condition, "|");

        // Wir sammeln erst mal in einer HashMap auf, um dann später die Query-Reihenfolge
        // aus Data.getQueryColKeys() einzuhalten. Dies ist wichtig da, die Performance stark von
        // Query-Reihenfolge abhängen kann.
        HashMap queryMap = new HashMap();
        while (st.hasMoreTokens()) {
            String part = st.nextToken();
            StringDivider sd = new StringDivider(part, ":");
            if (sd.ok()) {
                queryMap.put(sd.pre(), part);
            }
        }

        // Jetzt entsprechend data.getQueryColKeys() umsortieren.
        List qds = data.getQueryDescriptions();
        if (qds == null || qds.size() <= 0) {
            throw new MappingException("No QueryDescription defined in [" + data.getClass().getName() + "] Can not perform a Query !");
        }
        for (int i = 0; i < qds.size(); i++) {
            QueryDescription definition = (QueryDescription) qds.get(i);
            String part = (String) queryMap.get(definition.getMethodKey());
            if (part != null) {
                queryMap.remove(definition.getMethodKey());
                query.and(buildPartQuery(data, part));
            }
        }

        // Die restlichen Parts (Klauseln) die im Data nicht aufgeführt sind (createQueryDescriptions) noch anfügen
        for (Iterator iterator = queryMap.values().iterator(); iterator.hasNext();) {
            String part = (String) iterator.next();
            query.and(buildPartQuery(data, part));

        }

        return query;
    }

    public static Query buildPartQuery(Data data, String part) throws MappingException, ClassNotFoundException {

        Class target = Class.forName(data.getBoClassName());
        StrTokenizer st = new StrTokenizer(part, ",");
        Query query = new Query(target);
        while (st.hasMoreElements()) {
            String therm = (String) st.nextElement();
            query.or(buildThermQuery(data, therm));
        }

        return query;
    }

    /**
     * Eine einzelne QueryDefinition formulieren.
     * Formate :  "attribute:value..value"   -> attribute im Interval
     * "attribute:< value"        -> attribute größer value
     * "attribute:> value"        -> attribute kleiner value
     * "attribute:value"          -> attribute gleich value
     * Beispiele :
     *
     * @param data
     * @param therm
     * @return
     */
    public static Query buildThermQuery(Data data, String therm) throws MappingException, ClassNotFoundException {
        Class target = Class.forName(data.getBoClassName());

        if (data.buildThermQuery(therm) != null) {
            return data.buildThermQuery(therm);
        }

        Query query = new Query(target);
        StringDivider sd = new StringDivider(therm, ":");
        if (sd.ok()) {
            String operator = Query.EQ;
            String arg = null, arg2 = null;
            if (sd.post().indexOf("<=") >= 0) {           // Kleiner gleich
                arg = Util.replaceFirstSubString(sd.post(), "<=", "").trim();
                operator = Query.LE;
            } else if (sd.post().indexOf('<') >= 0) {      // Kleiner
                arg = Util.replaceFirstSubString(sd.post(), "<", "").trim();
                operator = Query.LT;
            } else if (sd.post().indexOf(">=") >= 0) {     // Größer gleich
                arg = Util.replaceFirstSubString(sd.post(), ">=", "").trim();
                operator = Query.GE;
            } else if (sd.post().indexOf('>') >= 0) {      // Größer
                arg = Util.replaceFirstSubString(sd.post(), ">", "").trim();
                operator = Query.GT;
            } else if (sd.post().indexOf("..") >= 0 /*|| sd.post().indexOf("-") >= 0*/) {         // Interval
                StringDivider sd2 = new StringDivider(sd.post(), "..");
                if (sd2.ok()) {
                    arg = sd2.pre();
                    arg2 = sd2.post();
                }
                /*else {
                    sd2 = new StringDivider(sd.post(), "-");
                    if (sd2.ok()) {
                        arg = sd2.pre();
                        arg2 = sd2.post();
                    }
                } */
            } else {
                arg = sd.post();
            }

            String methodKey = sd.pre().trim();
            QueryDescription qd = data.getQueryDescrition(methodKey);
            if (qd == null) {
                qd = new QueryDescription(methodKey, methodKey, data.getFieldType(methodKey));
            }

            String path = qd.getPath();
            Class type = qd.getAttributeType();

            Object value = data.getQueryValue(type, arg.trim(), methodKey);

            Object value2 = null;
            if (arg2 != null) {
                value2 = data.getQueryValue(type, arg2, methodKey);
            }

            if (value != null && value2 == null) {
                if (value instanceof String) {
                    // kein Komma && keine Liste
                    boolean oderListe = ((String) value).indexOf(',') != -1;
                    boolean undListe = ((String) value).indexOf('|') != -1;
                    if (oderListe || undListe) {
                        // der Value enthält wieder eine Liste -> Rekursion
                        query = buildQuery(data, (String) value);
                    } else {
                        query = new Query(target, path, operator, value);
                    }
                } else {
                    query = new Query(target, path, operator, value);
                }
            }

            if (value != null && value2 != null) {
                query = new Query(target, path, Query.GE, value);
                query.and(new Query(target, path, Query.LT, value2));
            }
        }

        return query;
    }

    public int testQuerySize(Data data, String condition) throws ClassNotFoundException, MappingException {
        Query query = buildQuery(data, condition);
        return DB.session().getObjectCount(query);
    }

    public int fillBoList(List boList, Data data, String condition, long wantedSize, long max) throws ClassNotFoundException, MappingException {
        Query query = buildQuery(data, condition);

        OidIterator objs = DB.session().getObjects(query);

        // Begrenzung ausschalten.
        if (wantedSize == 0 && !condition.endsWith("**") && objs.size() > max) {
            return -1;
        }
        int count = 0;
        for (int i = 0; i < objs.size(); i++) {
            if (wantedSize > 0 && count == wantedSize) {
                break;
            }
            Object o = objs.get(i);
            boList.add(o);
            count++;
        }

        return (int) objs.size();
    }

}
