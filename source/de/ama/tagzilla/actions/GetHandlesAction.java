package de.ama.tagzilla.actions;

import de.ama.db.Query;
import de.ama.tagzilla.data.Handle;
import de.ama.tagzilla.data.HandleData;
import de.ama.server.services.Environment;
import de.ama.util.Util;
import de.ama.framework.action.ActionScriptAction;

import java.util.List;


public class GetHandlesAction extends ActionScriptAction {
    public String path;
    public String tag;
    public long count;

    public void execute() {

        if(!Util.isEmpty(tag)) {
            Query q = new Query(Handle.class, "tags", Query.LIKE, "*"+Handle.DELIM + tag + Handle.DELIM+"*");
            count = Environment.getPersistentService().getObjectCount(q);
            if(count>Handle.QUERY_LIMIT){
                message = "query was limited to "+Handle.QUERY_LIMIT+" entries";
            }
            List objects = Environment.getPersistentService().getObjects(q.limit(Handle.QUERY_LIMIT));
            data = mapBosToDataTable(objects, new HandleData());
            System.out.println("found "+ data + " handles");
            return;
        }

        if(!Util.isEmpty(path)) {
            Query q = new Query(Handle.class, "path", Query.LIKE, de.ama.framework.util.Util.toDBString(path));
            count = Environment.getPersistentService().getObjectCount(q);
            if(count >Handle.QUERY_LIMIT){
                message = "query was limited to "+Handle.QUERY_LIMIT+" entries";
            }
            List objects = Environment.getPersistentService().getObjects(q.limit(Handle.QUERY_LIMIT));
            data = mapBosToDataTable(objects, new HandleData());
            System.out.println("found "+ data + " handles");
            return;
        }

        data = null;
    }

}