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
    public static long QUERY_LIMIT = 200 ;

    private String path;
    private String tags;
    public String lastUser;
    public long lastmodified ;
    public long size;
    public long userId;
    public  int    x;
    public  int    y;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPath() {
        return fromDbString(path);
    }

    public void setPath(String path) {
        this.path = toDBString(path);
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

    public static String toDBString(String path){
        path=replaceSubString(path,"\\","/");
        path=replaceSubString(path,"ä", "&au");
        path=replaceSubString(path,"Ä", "&AU");
        path=replaceSubString(path,"ö", "&ou");
        path=replaceSubString(path,"Ö", "&OU");
        path=replaceSubString(path,"ü", "&uu");
        path=replaceSubString(path,"Ü", "&UU");
        path=replaceSubString(path,"ß", "&sz");
        return path;
    }

    public static String fromDbString(String path){
        path=replaceSubString(path,"&au","ä" );
        path=replaceSubString(path,"&AU","Ä" );
        path=replaceSubString(path,"&ou","ö" );
        path=replaceSubString(path,"&OU","Ö" );
        path=replaceSubString(path,"&uu","ü" );
        path=replaceSubString(path,"&UU","Ü" );
        path=replaceSubString(path,"&sz","ß" );
        return path;
    }

    public static String replaceSubString(String gesamt, String oldString, String newString) {
        String result = "";
        if (gesamt == null)    return gesamt;
        if (newString == null) newString = "";
        if (oldString == null) oldString = "";
        if (oldString.equals("")) return gesamt;
        if (oldString.equals(newString)) return gesamt;

        int indexOldStr = gesamt.indexOf(oldString);
        int oldStringLength = oldString.length();
        while (indexOldStr > -1) {
            result += gesamt.substring(0, indexOldStr) + newString;
            gesamt = gesamt.substring(indexOldStr + oldStringLength);
            indexOldStr = gesamt.indexOf(oldString);
        }
        result += gesamt;
        return result;
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
