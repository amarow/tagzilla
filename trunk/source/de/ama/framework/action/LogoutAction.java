package de.ama.framework.action;

import de.ama.server.services.Environment;


public class LogoutAction extends ActionScriptAction {

    public boolean success;

    public void execute() {
        success = Environment.getUserService().logout(getUser());
    }

}

