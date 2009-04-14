package de.ama.server.actions;

import de.ama.server.services.Environment;


public class LogoutAction extends ServerAction {

    public boolean success;

    public void execute() {
        success = Environment.getUserService().logout(getUser());
    }

}

