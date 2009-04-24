package de.ama.tagzilla.data;

import de.ama.db.PersistentMarker;


/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 28.02.2009
 * Time: 14:53:26
 * To change this template use File | Settings | File Templates.
 */
public class DeskHandle implements PersistentMarker {

    private String path;
    private long lastmodified ;
    private long size;
    private String lastUser;
    private long userId;
    private  int    x;
    private  int    y;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(long lastmodified) {
        this.lastmodified = lastmodified;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }



    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}