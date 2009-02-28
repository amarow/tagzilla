package de.ama.server.bom;

import de.ama.db.PersistentMarker;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 28.02.2009
 * Time: 14:53:26
 * To change this template use File | Settings | File Templates.
 */
public class User implements PersistentMarker {
    private String name;
    private String pwd;
    private String id;

    public User(String name, String pwd, String id) {
        this.name = name;
        this.pwd = pwd;
        this.id = id;
    }

    public String getId() {
        return id;
    }
}