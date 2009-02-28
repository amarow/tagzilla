package de.ama.server.actions;

import de.ama.util.Util;


public class ActionScriptAction {

    public String serverActionName;
    public String userId;
    public String catalog;

    public Object data;
    public String message;
    public String detailErrorMessage;



    


    public String getServerActionName() {
        return Util.saveToString(serverActionName,"ServerAction");
    }

    public String getCatalog() {
        return Util.saveToString(catalog,"tagzilla");
    }
}
