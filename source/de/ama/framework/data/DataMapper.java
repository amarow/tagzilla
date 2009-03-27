/*
 * DataMapperDefault.java
 *
 * Created on 31. August 2003, 21:42
 */

package de.ama.framework.data;

import de.ama.db.*;
import de.ama.util.StrTokenizer;
import de.ama.util.StringDivider;
import de.ama.util.Util;

import java.lang.reflect.Method;
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
//    private static ThreadLocal cashHolder = new ThreadLocal();
//
//    public Map getCache() {
//        if(cashHolder.session()==null){
//           cashHolder.set(new HashMap());
//        }
//        return (Map) cashHolder.session();
//    }
//
//    public Data getCachedData(Data data, Object boValue) {
//        String key = data.getClass().getName()+"-"+UmgebungsObjekt.current().getOID(boValue).toString();
//        Data d =  (Data) getCache().session(key);
//        if(d!=null){
//            System.out.println("<<< getCacheData = " + key);
//        }
//        return d;
//    }
//
//    public void cacheData(Data data) {
//        String key = data.getClass().getName()+"-"+data.getOidString();
//        System.out.println(">>> cacheData = " + key);
//        getCache().put(key,data);
//    }


    ////////////////////////////// abstracts ///////////////////////////////////////
    // In Mapper-Derivaten müssen diese Methoden überschrieben werden.

    public abstract void writeDataToBo(Object bo, Data rootData, String[] keys) throws MappingException;

    public abstract void readDataFromBo(Object bo, Data rootData, String[] keys) throws MappingException;

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

//        if (EnvironmentBase.getServerPropertyAsBoolean("Mapping", "checkVersionDeep", false)) {
//            checkVersion(bo, rootData);
//        }

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


    public Data readDataFromBo(Object bo, Data rootData, int type) throws MappingException {
//        Data previousReadData = getCachedData(rootData,bo);
//        if(previousReadData!=null){
//            return previousReadData;
//        }
//            System.out.println("DataMapper.readDataFromBo "+ Util.getUnqualifiedClassName(rootData.getClass()));
        readOidAndVersion(bo, rootData);
//        cacheData(rootData);



        // Hook für den Entwickler des Data-Objekts
        rootData.preReadDataFromBo(bo);

        String[] keys = getMappingKeys(rootData, type);
        readDataFromBo(bo, rootData, keys);

        // Hook für den Entwickler des Data-Objekts
        rootData.postReadDataFromBo(bo);


        //   Debug.debug("read Object : " + bo.getClass().getName() + " oid=" + rootData.getOidString());

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
        DataTable table = DataTable.createFromData(data, mini);
        if (boList != null) {
            readObjects(boList, table, mini);
        }
        return table;
    }

    public void writeObjects(Object obj, DataTable table) throws MappingException {
        if (obj == null) {
            throw new MappingException("Try to write DataTable into Bo, but there is no Collection to write to !!!!" +
                    "\r\n Collections in Bo's should be initialized " +
                    "\r\n Table-Class = " + table.getType().getName());
        }

        List proxys = table.getDataProxys();
        Collection container = null;
        //System.out.println("DataMapper.writeObjects :" + obj.getClass() + " table=" + table.getGuiRepresentation());
        // Leider kann in der Signatur keine Collection übergeben werden, weil
        if (obj instanceof Collection) {
            container = (Collection) obj;
        } else if (obj instanceof Iterator) {
            return;    // Iterator sind nur zum lesen gedacht.
        } else {
            throw new MappingException("writeObjects with wrong collection type :" + obj.getClass().getName());
        }

        // Wir merken uns die bisherige Collection.
        List  oldContainer = new ArrayList();
        for (Iterator iterator = container.iterator(); iterator.hasNext();) {
             oldContainer.add(iterator.next());            
        }

        // Wir entleren die Collection und bauen sie neu auf.
        container.clear();

        for (int i = 0; i < proxys.size(); i++) {
            DataProxy dp = (DataProxy) proxys.get(i);
            Object element = null;
            // Falls es Daten gibt, sollen diese auch gemappt werden.
            if (dp.hasData()) {
                Data data = dp.getData(false);
                if (dp.hasNewData()) {
                    // bei ganz neuen Daten muß auch ein passendes Bo erzeugt werden.
                    element = data.createEmptyBo();
                } else {
                    // sonst nehnmen wir das alte BO.
                    element = DB.session().getObject(data.getOidString());
                }
                data.getMapper().writeDataToBo(element, data);
            } else if (dp.hasReference()) {
                // wenn es keine Daten gibt, gibt es vieleicht eine Referenz umzuhängen ?
                element = DB.session().getObject(dp.getOidString());
            }

            container.add(element);
            oldContainer.remove(element);
        }

        if (table.isDeletingBos()) {
            DB.session().deleteObjects(oldContainer);
//            Util.printList("FROM DB DELETED OBJECTS :", oldContainer);
        }


    }


    public void readObjects(Object obj, DataTable table, boolean mini) throws MappingException {
        Collection container = null;
        // Leider kann in der Signatur keine Collection übergeben werden, weil
        if (obj instanceof Collection) {
            container = (Collection) obj;
        } else {
            throw new MappingException("readObjects with wrong collection type :" + obj.getClass().getName());
        }

        String sortKey = table.getSortKey();
        table.setSortKey(null);   // disable Sorting.

        for (Iterator iterator = container.iterator(); iterator.hasNext();) {
            Object bo = iterator.next();
            if (bo == null) {
                continue;
            }

            Data data = DataDictionary.getDataForBo(bo, table.getType());

            int mappingType = mini ? MINI_TABLE : BIG_TABLE;
            if (table.containsHeavyProxys()) {
                mappingType = FULL_OBJECT;
            }

            DataProxy dp = null;
            try {
                data = data.getMapper().readDataFromBo(bo, data, mappingType);
            } catch (Exception e) {
                dp = data.getDataProxy();
                dp.setErrorText(Util.getAllExceptionInfos(e));
            }

            dp = data.getDataProxy();
            List row = table.newRow();
            table.fillRow(row, data);


            if (!table.containsHeavyProxys()) {
                dp.unload();  // wir wollen ja nur den Proxy in der Tabelle haben !
            }
        }

    }

    ////////////////////////////////////// für UpdateTableCommands //////////////////////////////////

//    /*
//     * Voraussetzung ist hier das die ContainerId  oidString des Cotainers und methodenName
//     * des ParenObjects in der DataTAble gespeichert werden.
//     */
//
//    public void readContainerObjects(String containerId, DataTable table, boolean mini) throws MappingException, Hinweis {
//        List container = getContainer(containerId);
//        readObjects(container, table, mini);
//    }

    /**
     * Einen Container aus einem beliebigen Bo herausholen containerId enthält oidString und methoden Key
     */
    public List getContainer(String containerId) throws MappingException {
        StringDivider sd = new StringDivider(containerId, "(method)");
        if (sd.ok()) {
            Object obj = null;
            try {
                obj = DB.session().getObject(sd.pre());
            } catch (DBException de) {
                throw new MappingException("can not retreive Container from BO containerId=[" + containerId + "], Bo not found !!!");
            }


            if (obj != null) {
                try {
                    Class c = obj.getClass();
                    //          System.out.println("Class"+c.getName());
                    String key = Util.getMethodeName("get", sd.post());
                    //        System.out.println("key="+key);
                    Method getter = c.getMethod(key, (Class)null);
                    //      System.out.println("getter"+getter);
                    return (List) getter.invoke(obj, (Class)null);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new MappingException("can not retreive Container from BO containerId=[" + containerId + "]", e);
                }
            }
        } else {
            throw new MappingException("can not retreive Container from wrong containerId = [" + containerId + "]");
        }
        return null;
    }


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
            Object o = (Object) objs.get(i);
            boList.add(o);
            count++;
        }

        return (int) objs.size();
    }

}
