package de.ama.server.actions;

import de.ama.db.Query;
import de.ama.server.bom.Handle;
import de.ama.server.bom.HandleData;
import de.ama.server.services.Environment;
import de.ama.util.Util;

import java.util.List;


public class GetHandlesAction extends ServerAction {
    public String path;
    public String tag;

    public void execute() {

        if(!Util.isEmpty(tag)) {
            Query q = new Query(Handle.class, "tags", Query.LIKE, "*"+Handle.DELIM + tag + Handle.DELIM+"*");
            if(Environment.getPersistentService().getObjectCount(q)>Handle.QUERY_LIMIT){
                message = "query was limited to "+Handle.QUERY_LIMIT+" entries";
            }
            List objects = Environment.getPersistentService().getObjects(q.limit(Handle.QUERY_LIMIT));
            data = mapBosToDataTable(objects, new HandleData());
            System.out.println("found "+ data + " handles");
            return;
        }

        if(!Util.isEmpty(path)) {
            Query q = new Query(Handle.class, "path", Query.LIKE, Handle.toDBString(path) + "*");
            if(Environment.getPersistentService().getObjectCount(q)>Handle.QUERY_LIMIT){
                message = "query was limited to "+Handle.QUERY_LIMIT+" entries";
            }
            List objects = Environment.getPersistentService().getObjects(q.limit(Handle.QUERY_LIMIT));
            data = mapBosToDataTable(objects, new HandleData());
            System.out.println("found "+ data + " handles");
            return;
        }

        data = null;
    }

    public boolean needsUser() {
        return false;
    }
}