/*
 * DataMapperDefault.java
 *
 * Created on 31. August 2003, 21:42
 */

package de.ama.framework.data;


import de.ama.util.Time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

    public void writeDataToBo(Object bo, Data rootData, String[] keys) throws MappingException {
        int size = keys.length;
        DataBinding binding = DataBinding.getBinding(rootData);
        for (int i = 0; i < size; i++) {
            MethodBinding mb = binding.getMethodeBinding(keys[i], bo, rootData);
            Object dataValue = mb.getDataValue(rootData);
            if (dataValue == null) {
                if (mb.getDataFieldType() == Data.class) {
                    mb.setBoValue(bo, null);
                }
                continue;
            }
            if (dataValue.getClass() == Date.class) {
                if (mb.hasBoSetter()) {
                    if (mb.getBoSetterType() == Date.class) {
                        mb.setBoValue(bo, dataValue);
                    } else if (mb.getBoSetterType() == String.class) {
                        mb.setBoValue(bo, de.ama.util.Util.asDBString((Date) dataValue));
                    }
                }
            } else if (dataValue.getClass() == Time.class) {
                if (mb.hasBoSetter()) {
                    if (mb.getBoSetterType() == Time.class) {
                        mb.setBoValue(bo, dataValue);
                    } else if (mb.getBoSetterType() == String.class) {
                        mb.setBoValue(bo, de.ama.util.Util.asDBString((Time) dataValue));
                    }
                }
            } else if (dataValue.getClass() == DataTable.class) {
                if (mb.hasBoSetter()) {
                    DataTable dataTable = (DataTable) dataValue;
                    Collection bos = (Collection) mb.getBoValue(bo);
                    if (bos == null) {
                        bos = new ArrayList();
                    }
                    writeObjects(bos, dataTable);
                    // container ist an sich ein eigenständiges Object, wir setzen es aber zurück ins bo,
                    // damit wir dort noch Verknüpfungsarbeiten leisten können.
                    mb.setBoValue(bo, bos);
                }
            } else if (dataValue instanceof Data) {
                Data d = (Data) dataValue;
                writeChildBo(bo, d, mb);
            } else {
                mb.setBoValue(bo, dataValue);
            }
        }
    }

    public void readDataFromBo(Object bo, Data rootData, String[] keys) throws MappingException {

        DataBinding binding = DataBinding.getBinding(rootData);
        for (int i = 0; i < keys.length; i++) {
            MethodBinding mb = binding.getMethodeBinding(keys[i], bo, rootData);
            Object dataValue = mb.getDataValue(rootData);

            Object boValue = mb.getBoValue(bo);

            if (boValue != null) {
                if (mb.getDataFieldType() == Date.class) {
                    if (boValue instanceof Date) {
                        mb.setDataValue(rootData, boValue);
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
                    DataTable dataTable = (DataTable) dataValue;
                    if (dataTable == null) {
                        throw new MappingException("DataTables müssen im DataObjekt initialisiert werden Field: " + mb.getKey());
                    }
                    readObjects(dataTable, (Collection) boValue, false);
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


