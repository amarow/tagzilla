package de.ama.server.actions;

import de.ama.db.Query;
import de.ama.server.bom.Desk;
import de.ama.server.services.Environment;


public class DeskAction extends ServerAction {
    public String deskName;


    public void execute() {

        if (data == null) {  // load
            Desk d = (Desk) Environment.getPersistentService().getObject(new Query(Desk.class, "name", Query.EQ, deskName), true);
            data = d;
        } else {           // save
            Desk d = (Desk) data;
            Environment.getPersistentService().makePersistent(d);
            commit();
        }

    }

    public boolean needsUser() {
        return false;
    }
}