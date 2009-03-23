package de.ama.server.bom;

import de.ama.db.PersistentMarker;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 28.02.2009
 * Time: 14:53:26
 * To change this template use File | Settings | File Templates.
 */
public class Handle implements PersistentMarker {

    public static String DELIM ="|";
    public static long QUERY_LIMIT = 1000 ;

    private String path;
    private String tags;
    public String lastUser;
    public long lastmodified ;
    public long size;
    public long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void addTag(String tag){
        if(tag.contains(DELIM)){
            throw new IllegalArgumentException("The '"+ DELIM +"' is not permited in tags");
        }

        if(tags==null) tags=DELIM;

        if(tags.contains(DELIM +tag+ DELIM)) return;

        tags += tag+ DELIM;
    }
}
