package de.ama.server.services.impl;

/**
 * User: x
 * Date: 16.05.2008
 */

import de.ama.server.actions.ActionScriptAction;
import de.ama.server.actions.ServerAction;
import de.ama.server.services.ActionService;
import de.ama.server.services.Environment;


public class ActionServiceImpl implements ActionService {


    private ServerAction createAction(String name) {
        return (ServerAction) Environment.getBean("server.actions." + name);
    }

    public ActionScriptAction execute(ActionScriptAction asa) {
        System.out.println("ActionServiceImpl.remote_execute " + asa.serverActionName);

        asa.detailErrorMessage = null;
        asa.message = null;

        try {
            Environment.getPersistentService().join(asa.getCatalog());

            ServerAction a = createAction(asa.getServerActionName());
            if (a == null) {
                throw new RuntimeException("cant find ServerAction for [" + asa.serverActionName + "]");
            }
            ServerAction.setCurrent(a);

            if (a.needsUser()) {
                a.setUser(Environment.getUserService().getActiveUser(asa.userId));
            }

            a.setData(asa.data);
            a.execute();
            asa.data = a.getData();

            System.out.println("ActionServiceImpl.execute OK " + asa.serverActionName);
            return asa;

        } catch (Exception e) {
            e.printStackTrace();
            asa.detailErrorMessage = e.getMessage();
            return asa;
        } finally {
            Environment.getPersistentService().rollback();
            Environment.getPersistentService().leave();
            ServerAction.setCurrent(null);
        }

    }

}