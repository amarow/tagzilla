package de.ama.framework.action;

import de.ama.framework.data.Data;
import de.ama.server.services.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 30.04.2009
 * Time: 18:30:43
 * To change this template use File | Settings | File Templates.
 */
public class DataAction extends ActionScriptAction{

    @Override
    public void execute() throws Exception {

        if (data!=null) {           // save
            data = mapDataToBo((Data) data, true);
        } else {  // load
            Object o = Environment.getPersistentService().getObject(selectionModel.getSingleSelection().getOidString());
            mapBoToData(o, (Data) data);
        }
    }
}
