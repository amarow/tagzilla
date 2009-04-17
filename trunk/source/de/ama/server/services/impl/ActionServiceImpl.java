package de.ama.server.services.impl;

/**
 * User: x
 * Date: 16.05.2008
 */

import de.ama.framework.action.ActionScriptAction;
import de.ama.server.services.ActionService;
import de.ama.server.services.Environment;
import com.caucho.hessian.server.HessianServlet;


public class ActionServiceImpl extends HessianServlet implements ActionService {


    public ActionScriptAction execute(ActionScriptAction a) {
        System.out.println("ActionServiceImpl.remote_execute " + a.getName());

        try {
            Environment.getPersistentService().join(a.getCatalog());
            ActionScriptAction.setCurrent(a);

            if (a.needsUser()) {
                a.setUser(Environment.getUserService().getActiveUser(a.userId));
            }

            a.execute();

            System.out.println("ActionServiceImpl.execute OK " + a.getName());
            return a;

        } catch (Exception e) {
            e.printStackTrace();
            a.detailErrorMessage = e.getMessage();
            return a;
        } finally {
            Environment.getPersistentService().rollback();
            Environment.getPersistentService().leave();
            ActionScriptAction.setCurrent(null);
        }

    }

}