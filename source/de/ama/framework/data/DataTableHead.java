package de.ama.framework.data;


import java.io.Serializable;

/**
 * User: Andreas Marochow
 * Date: 06.07.2003
 * Der Kopf einer DataTable.
 */

public class DataTableHead implements Serializable {
    static final long serialVersionUID = -1L;

    public Class type;
    public boolean editable;
    public String methodKey;
    public String queryString;
    public boolean queryAble;
    public int width ;


    public DataTableHead(Class type, boolean editable, String methodKey, boolean queryAble) {
        this.type = convertPrimitive(type);
        this.editable = false;
        this.methodKey = methodKey;
        this.queryAble = queryAble;
        width = 80;
    }

    public String getCaption(){
        return methodKey;
    }

    private Class convertPrimitive(Class type) {
        if (type.isPrimitive()) {
            if (type == int.class) {
                return Integer.class;
            } else if (type == boolean.class) {
                return Boolean.class;
            } else if (type == float.class) {
                return Float.class;
            } else if (type == double.class) {
                return Double.class;
            } else if (type == long.class) {
                return Long.class;
            }
            return String.class;
        }else{
            return type;
        }
    }
}

