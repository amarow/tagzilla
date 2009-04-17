package de.ama.framework.action;

import de.ama.server.services.Environment;
import de.ama.util.StringDivider;


public class NewUserAction extends ActionScriptAction {

    public void execute() {
        String tmp = (String) data;
        StringDivider sd = new StringDivider(tmp,"{del}");
        if(sd.ok()){
            userId = Environment.getUserService().newUser(sd.pre() ,sd.post());
            commit();
        }

    }

    public boolean needsUser() {
        return false;
    }
}
