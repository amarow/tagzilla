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
    private String oidString;

    public Selection() {
    }

    public Selection(Data data) {
        setData(data);
    }

    public String getOidString() {
        return oidString;
    }

    public void setData(Data data) {
        this.oidString = data.getOidString();
    }
}
