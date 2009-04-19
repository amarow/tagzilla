package de.ama.tagzilla.data;

import de.ama.db.PersistentMarker;
import de.ama.framework.util.Util;


/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 28.02.2009
 * Time: 14:53:26
 * To change this template use File | Settings | File Templates.
 */
public class Handle implements PersistentMarker {

    public static String DELIM ="|";
    public static long QUERY_LIMIT = 200 ;

    private String path;
    private String tags;
    private long lastmodified ;
    private long size;

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

    
    public String getPath() {
        return Util.fromDbString(path);
    }

    public void setPath(String path) {
        this.path = Util.toDBString(path);
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
