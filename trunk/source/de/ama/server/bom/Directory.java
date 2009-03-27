package de.ama.server.bom;

import de.ama.db.PersistentMarker;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 10.03.2009
 * Time: 21:16:40
 * To change this template use File | Settings | File Templates.
 */
public class Directory implements PersistentMarker{
    public String path;
    public long lastmodified ;
    public long size;
    public long userId;
    public long pause;

    public String getPath() {
        return Handle.fromDbString(path);
    }

    public void setPath(String path) {
        this.path = Handle.toDBString(path);
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

    public void setPause(long pause) {
        this.pause = pause;
    }

    public long getPause() {
        return pause;
    }
}
