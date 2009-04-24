package de.ama.framework.util;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 18.04.2009
 * Time: 23:31:37
 * To change this template use File | Settings | File Templates.
 */
public class Util {

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
        if (gesamt == null) return gesamt;
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

    public static String normalizeFileName(String in){
        String ret = replaceSubString(in," ","_");
        return ret;
    }
}
