package de.ama.server.actions;

import de.ama.server.services.Environment;
import de.ama.util.StringDivider;


public class LoginAction extends ServerAction {

    public void execute() {
        String tmp = (String) data;
        StringDivider sd = new StringDivider(tmp,"{del}");
        if(sd.ok()){
           userId = Environment.getUserService().login(sd.pre() ,sd.post());
        }
    }

    public boolean needsUser() {
        return false;
    }
}
