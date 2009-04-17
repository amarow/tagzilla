package de.ama.framework.action;

import de.ama.db.Query;
import de.ama.framework.data.Data;
import de.ama.tagzilla.data.Desk;
import de.ama.tagzilla.data.DeskData;
import de.ama.server.services.Environment;


public class DeskAction extends ActionScriptAction {
    public boolean save;


    public void execute() {

        if (save) {           // save
            data = mapDataToBo((Data) data, true);
        } else {  // load
            DeskData dd = (DeskData) data;
            Desk d = (Desk) Environment.getPersistentService().getObject(new Query(Desk.class, "name", Query.EQ, dd.name), true);
            mapBoToData(d, (Data) data);
        }

    }

}