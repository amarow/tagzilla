package de.ama.framework.data;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 07.09.2003
 * Time: 22:08:23
 * Diese hashmap stellt die Verbindung zwischen Bo und DataObject her.
 * Die Datas registrieren sich selbst über den premain-Mechanismus.
 */
public class DataDictionary {

    public static Map dataDictionary = new HashMap();    // BoClassName -->  DataClass

    public static Class getDataClassForBo(Object bo) {
        return getDataClassForBoClass(bo.getClass());
    }

    public static Class getDataClassForBoClass(Class boClass) {
        Class possibleDerivate = boClass;
        Class dataClass = null;

        while (dataClass == null && boClass!=null) {
            String key = getDictionaryClassName(boClass);
            dataClass = (Class) dataDictionary.get(key);
            if (dataClass == null) {
                boClass = boClass.getSuperclass();
            }
    }

        if(boClass!=possibleDerivate){
           dataDictionary.put(getDictionaryClassName(possibleDerivate),dataClass); 
        }

        return dataClass;
    }

    private static String getDictionaryClassName(Class boClass) {
        String key = boClass.getName().replace('$', '.');
        return key;
    }

    public static Class getDataClassForBo(Object bo, Class baseClass) {
        return (Class) dataDictionary.get(bo.getClass().getName() + "|" + baseClass.getName());
    }

    public static Data getDataForBo(Object bo, Class baseType) throws MappingException {
        Class c = getDataClassForBo(bo);
        Data data = null;
        if (c != null && c != Double.class) {
            data = Data.createEmptyData(c);
        }

        // Im Sonderfall ausweichen.
        if (data == null) {
            data = Data.createEmptyData(getDataClassForBo(bo, baseType));
        }

        if (data == null) {
            System.out.println("***********************************************************");
            System.out.println("******         NO DATA-OBJECT FOR BO          *************");
            System.out.println("******        " + bo == null ? "bo=null" : bo.getClass().getName());
            MappingException he = new MappingException("cann not create Data for bo " + bo == null ? "bo=null" : bo.getClass().getName());
            throw he;
        }
        return data;
    }

    public static void registerData(Data data) {
        Class containedClass = (Class) dataDictionary.get(data.getBoClassName());
        if (containedClass != null) {
            System.out.println("***********************************************************");
            System.out.println("******         DOUBLE DATA-OBJECT             *************");
            System.out.println("******        " + data.getClass().getName());
            dataDictionary.put(data.getBoClassName(), Double.class);        // Marker für doppelte BO-Einträge
            System.out.println("******     removed : " + data.getBoClassName());
            String key = data.getBoClassName() + "|" + containedClass.getName();
            System.out.println("******     registered instead : " + key + " --> " + containedClass.getName());
            dataDictionary.put(key, containedClass);
            key = data.getBoClassName() + "|" + data.getClass().getName();
            dataDictionary.put(key, data.getClass());
            System.out.println("******     registered instead : " + key + " --> " + data.getClass().getName());
        } else {
            dataDictionary.put(data.getBoClassName(), data.getClass());
        }
    }

    public static List getDataClassNames() {
        //Properties props = UmgebungsObjekt.getCurrentDatenbanken();
        List ret = new ArrayList();
        for (Iterator iter = dataDictionary.values().iterator(); iter.hasNext();) {
            Class c = (Class) iter.next();
            ret.add(c.getName());

        }
        return ret;
    }
}
