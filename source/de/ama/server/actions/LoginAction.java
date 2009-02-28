package de.ama.server.actions;

import de.ama.server.services.Environment;
import de.ama.util.StringDivider;


public class LoginAction extends ServerAction {

    public void execute() {
        String tmp = (String) getData();
        StringDivider sd = new StringDivider(tmp,"{del}");
        if(sd.ok()){
            String userId = Environment.getUserService().login(sd.pre() ,sd.post());
            setData(userId);
        }
    }

    public boolean needsUser() {
        return false;
    }
}
