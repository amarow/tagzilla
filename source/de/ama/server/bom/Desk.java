package de.ama.server.bom;

import de.ama.db.PersistentMarker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 26.03.2009
 * Time: 14:53:38
 * To change this template use File | Settings | File Templates.
 */
public class Desk implements PersistentMarker {
    public List objects = new ArrayList();
    public String name;
}