/*
 * DataMapperDefault.java
 *
 * Created on 31. August 2003, 21:42
 */

package de.ama.framework.data;


import de.ama.db.DB;
import de.ama.db.PersistentList;
import de.ama.util.Time;

import java.util.Date;
import java.util.List;

/**
 * @author ama
 *         Dieser Mapper übernimmt das generische mappen aus Business-Objekten in Data-Objekte und zurück
 */
public class DataMapperDefault extends DataMapper {

    public Class getBoClass() {
        return this.getClass();  // dies ist natürlich keine BoClass, aber im DataDictionary soll dieser Mapper nicht
        // gefunden werden, es wird immer auf diesen DataMapperDefault ausgewichen.
    }

    /**
     * Generisches Mapping über Reflection, sollte eigentlich für alle Bo's funktionieren.
     * Wenn mann aber mal ganz etwas seltsames in ein Data-Object hieven muß, kann ein spezieller
     * DataMapperDefault geschrieben werden. Spezielle Mapper werden vom Data-Objekt geliefert.
     *
     * @param bo       , das Business-Objetc aus dem gelesen (in das geschrieben ) wird
     * @param rootData
     * @throws MappingException
     * @throws MappingException
     */

    public void writeDataToBo(Object bo, Data rootData, String[] keys) throws MappingException{
        int size = keys.length;
        DataBinding binding = DataBinding.getBinding(rootData);
        for (int i = 0; i < size; i++) {
            MethodBinding mb = binding.getMethodeBinding(keys[i], bo, rootData);
            Object val = mb.getDataValue(rootData);
            if (val == null) {
                if (mb.getDataFieldType() == Data.class) {
                    mb.setBoValue(bo, null);
                }
                continue;
            }
            if (val.getClass() == Date.class) {
                if (mb.hasBoSetter()) {
                    if (mb.getBoSetterType() == Date.class) {
                        mb.setBoValue(bo,val);
                    }  else if (mb.getBoSetterType() == String.class) {
                        mb.setBoValue(bo, de.ama.util.Util.asDBString((Date) val));
                    }
                }
            } else if (val.getClass() == Time.class) {
                if (mb.hasBoSetter()) {
                    if (mb.getBoSetterType() == Time.class) {
                        mb.setBoValue(bo,val);
                    }  else if (mb.getBoSetterType() == String.class) {
                        mb.setBoValue(bo, de.ama.util.Util.asDBString((Time) val));
                    }
                }
            } else if (val.getClass() == DataTable.class) {
                if (mb.hasBoSetter()) {
                DataTable dt = (DataTable) val;
                Object container = mb.getBoValue(bo);
                    if (mb.getBoGetterType() == String.class) {
                        mb.setBoValue(bo, dt.convertToDelimitterSeperatedString());
                    continue;
                }
                if (container == null && dt != null) {
                    // Falls die Collection noch gar niocht existiert, hier versuchen eine ins
                    // Bo einzutragen.
                    if (List.class.isAssignableFrom(mb.getBoSetterType())) {
                        container = new PersistentList(mb.getBoSetterType());
                        mb.setBoValue(bo, container);
                    }
                }
                writeObjects(container, dt);
                // container ist an sich ein eigenständiges Object, wir setzen es aber zurück ins bo,
                // damit wir dort noch Verknüpfungsarbeiten leisten können.
                mb.setBoValue(bo, container);
                }
            } else if (val.getClass() == DataProxy.class) {
                DataProxy dp = (DataProxy) val;
                if (dp.hasData()) {
                    // Proxy ist geladen somit sind die Daten potentiell verändert.
                    Data d = dp.getData(false);
                    writeChildBo(bo, d, mb);
                } else {
                    // Proxy stellt nur eine Refferenz dar
                    if (dp.hasReference()) {
                        Object childBo = DB.session().getObject(dp.getOidString());
                        mb.setBoValue(bo, childBo);
                    } else {
                        mb.setBoValue(bo, null);
                    }
                }
            } else if (val.getClass() == DataReference.class) {
                DataReference ref = (DataReference) val;
                if (ref.hasReference()) {
                    Object childBo = DB.session().getObject(ref.getOidString());
                    mb.setBoValue(bo, childBo);
                } else {
                    mb.setBoValue(bo, null);
                }
            } else if (val instanceof Data) {
                Data d = (Data) val;
                writeChildBo(bo, d, mb);
            } else {
                mb.setBoValue(bo, val);
            }
        }
    }

    public void readDataFromBo(Object bo, Data rootData, String[] keys) throws MappingException{
        //System.out.println("bo = " + bo);
        //System.out.println("readDataFromBo ::::::::::::: rootData = " + rootData);

        DataBinding binding = DataBinding.getBinding(rootData);
        for (int i = 0; i < keys.length; i++) {
            MethodBinding mb = binding.getMethodeBinding(keys[i], bo, rootData);
            Object dataValue = mb.getDataValue(rootData);

            Object boValue = mb.getBoValue(bo);

            if (boValue != null) {
                if (mb.getDataFieldType() == Date.class) {
                    if (boValue instanceof Date) {
                        mb.setDataValue(rootData,boValue);
                    } else if (boValue instanceof String) {
                        Date date = de.ama.util.Util.fromDBString((String) boValue);
                        mb.setDataValue(rootData, date);
                    }
                } else if (mb.getDataFieldType() == Time.class) {
                    if (boValue instanceof Time) {
                        mb.setDataValue(rootData, boValue);
                    } else if (boValue instanceof String) {
                        Time time = de.ama.util.Util.timeFromDBString((String) boValue);
                        mb.setDataValue(rootData, time);
                    }
                } else if (dataValue instanceof DataTable) {
                    DataTable dt = (DataTable) mb.getDataValue(rootData);
                    if (dt == null) {
                        throw new MappingException("DataTables müssen im DataObjekt initialisiert werden Field: " + mb.getKey());
                    }
                    if (mb.getBoGetterType()==String.class) {
                        dt.readDelimitterSeperatedString((String) boValue);
                        continue;
                    }

                    dt.setContainerId(rootData.getOidString(), mb.getKey()); // fürs directUpdate einer tabelle
                    readObjects(boValue, dt, false);
                } else if (dataValue instanceof DataProxy) {
                    DataProxy dp = (DataProxy) mb.getDataValue(rootData);
                    if (dp == null) {
                        throw new MappingException("DataProxys oder DataReferences müssen im DataObjekt initialisiert werden Field: " + mb.getKey());
                    }
                    //dp.setOidString(UmgebungsObjekt.current().getOID(boValue).toString());
                    Data data = dp.createData();
                    data = readDataFromBo(boValue, data, REFERENCE);
                    dp = data.getDataProxy();
                    dp.unload();
                    if (mb.getDataFieldType() == DataReference.class) {
                        mb.setDataValue(rootData, dp.toReference());
                    } else {
                        mb.setDataValue(rootData, dp);
                    }
                    //                            System.out.println("DataProxy gesetzt für "+keys[i]);
                } else if (dataValue instanceof Data) {
                    Data data = (Data) mb.getDataValue(rootData);
                    if (data == null) {
                        throw new MappingException("Data's müssen im DataObjekt initialisiert werden Field: " + mb.getKey());
                    }
                    data = readDataFromBo(boValue, data, FULL_OBJECT);
                    mb.setDataValue(rootData, data);
                    //                            System.out.println("Data gesetzt für "+keys[i]);
                } else {   // primitives
                    mb.setDataValue(rootData, boValue);
                    //                            System.out.println("Value gesetzt für "+keys[i]);
                }
            }
        }

    }

}


