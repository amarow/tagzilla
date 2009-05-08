package de.ama.framework.action;

import de.ama.server.services.Environment;


public class LoginAction extends ActionScriptAction {

    public String _user;
    public String _pwd;


    public void execute() {
        userId = Environment.getUserService().login(_user, _pwd);
        if (userId < 0) {
            Environment.getUserService().newUser(_user, _pwd);
            commit();
        }

    }

    public boolean needsUser() {
        return false;
    }
}
