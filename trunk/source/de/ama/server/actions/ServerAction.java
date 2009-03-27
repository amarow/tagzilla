package de.ama.server.actions;

import de.ama.db.Persistent;
import de.ama.server.bom.User;
import de.ama.server.services.Environment;
import de.ama.server.services.XmlService;
import de.ama.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * User: x
 * Date: 28.04.2008
 *

 *
 */
public class ServerAction {

    // ************** ActionScript Members *********************

    public Long   userId;
    public String catalog;

    public Object data;
    public String message;
    public String detailErrorMessage;

    // *********************************************************

    private static ThreadLocal currentActionHolder;
    private User    user;

    public ServerAction() {
    }


    public static void setCurrent(ServerAction a) {
        if (currentActionHolder == null) {
            currentActionHolder = new ThreadLocal();
        }
        currentActionHolder.set(a);
    }

    public static ServerAction getCurrent() {
        return (ServerAction)currentActionHolder.get();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void execute() throws Exception {
    }

    public String getCatalog(){
        return "tagzilla";
    }

    public void commit(){
        Environment.getPersistentService().commit();
    }

    public void rollback(){
        Environment.getPersistentService().rollback();
    }



    public boolean needsUser() {
        return true;
    }

    public String getName() {
        return Util.getUnqualifiedClassName(getClass());
    }


    public void setMessage(String m) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
