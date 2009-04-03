package de.ama.server.bom;

import de.ama.framework.data.Data;
import de.ama.util.Util;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 26.03.2009
 * Time: 14:53:38
 * To change this template use File | Settings | File Templates.
 */
public class TagData extends Data {
    public String path;
    public String tag;
    public int weight;
    public int x;
    public int y;
    public int bgcolor;

    public String getGuiRepresentation() {
        return Util.saveToString(tag);
    }
}