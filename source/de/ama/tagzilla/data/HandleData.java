package de.ama.tagzilla.data;

import de.ama.framework.data.Data;
import de.ama.util.Util;


/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 28.02.2009
 * Time: 14:53:26
 * To change this template use File | Settings | File Templates.
 */
public class HandleData extends Data {
    public String path;
    public String tags;
    public long size;

    public String getGuiRepresentation() {
        return Util.saveToString(path);
    }
}