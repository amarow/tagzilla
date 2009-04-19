package de.ama.tagzilla.data;

import de.ama.db.PersistentMarker;


/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 28.02.2009
 * Time: 14:53:26
 * To change this template use File | Settings | File Templates.
 */
public class DeskHandle extends Handle {

    private String lastUser;
    private long userId;
    private  int    x;
    private  int    y;

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