package de.ama.server.bom;

import de.ama.db.PersistentMarker;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 26.03.2009
 * Time: 14:53:38
 * To change this template use File | Settings | File Templates.
 */
public class Tag implements PersistentMarker {
    public  String path;
    public  String tag;
    public  int    weight;
    public  int    x;
    public  int    y;

}
