package de.ama.tagzilla.data;

import de.ama.framework.data.Data;
import de.ama.framework.data.DataTable;
import de.ama.util.Util;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 27.03.2009
 * Time: 20:35:08
 * To change this template use File | Settings | File Templates.
 */
public class DeskData extends Data {
    public DataTable objects = new DataTable();
    public String name;
    public int sliderPos;

    public String getGuiRepresentation() {
       return Util.saveToString(name);
    }
}
