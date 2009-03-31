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
    private String type;
    private String oidString;

    public String getType() {
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

    public boolean hasType(String dataClass) {
        return dataClass==getType();
    }
}
