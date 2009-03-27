/*
 * Selection.java
 *
 * Created on 1. Februar 2003, 23:15
 */

package de.ama.framework.data;

import java.io.Serializable;


/**
 * @author Andreas Marochow
 */
public class Selection implements Serializable {
    static final long serialVersionUID = -1L;
    private Class type;
    private String oidString;

    public Class getType() {
        return type;
    }

    public Selection(Data data) {
        setData(data);
    }

    public String getOidString() {
        return oidString;
    }

    public void setData(Data data) {
        this.type = data.getType();
        this.oidString = data.getOidString();
    }

    public boolean hasType(Class dataClass) {
        return dataClass.isAssignableFrom(getType());
    }
}
