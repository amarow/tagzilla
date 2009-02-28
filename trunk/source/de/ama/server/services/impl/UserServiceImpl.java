package server.services.impl;

import de.ama.db.Query;
import de.ama.util.Util;
import server.actions.ServerAction;
import server.beans.User;
import server.services.Environment;
import server.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserServiceImpl implements UserService {
    Map<String , User> activeUsers = new HashMap<String , User>() ;

    public String  newUser(String name, String pwd) {

        if(Util.isEmpty(name)){
            ServerAction.getCurrent().addError("bitte gebe einen Namen an");
            return "";
        }

        if(Util.isEmpty(pwd)){
            ServerAction.getCurrent().addError("bitte gebe einen Passwort ein");
            return "";
        }

        long count = Environment.getPersistentService().getObjectCount(new Query(User.class, "name", Query.EQ, name).
                                                                   and(new Query(User.class, "pwd", Query.EQ, pwd)));
        if (count == 0) {

            long id = Environment.getPersistentService().getNextNumber(USER_ID_SEQUENZE);

            User user = new User(name, pwd, name + "_" + id);
            Environment.getPersistentService().makePersistent(user);
            return user.getId();
        }

        ServerAction.getCurrent().addError("Es gibt schon einen User mit der Kombination User/Passwort. Bitte wählen Sie ein anderes Passwort ");
        return null;
    }

    public void removeUser(User user) {
        Environment.getPersistentService().delete(Environment.getPersistentService().getOidString(user));
    }

    public User getActiveUser(String id) {
        User user = activeUsers.get(id);
        if(user==null) {
            throw new RuntimeException("no active user / session dead") ;
        }
        return user;
    }

    public String login(String name, String pwd) {

        if(Util.isEmpty(name)){
            ServerAction.getCurrent().addError("bitte gebe einen Namen an");
            return "";
        }
        if(Util.isEmpty(pwd)){
//            throw new RuntimeException("bitte geben Sie ein Passwort ein") ;
            ServerAction.getCurrent().addError("bitte gebe einen Passwort ein");
            return "";
        }

        List users = Environment.getPersistentService().getObjects(new Query(User.class, "name", Query.EQ, name).
                                                                    and(new Query(User.class, "pwd", Query.EQ, pwd)));
        if (users.size() == 0) {
            ServerAction.getCurrent().addError("Es gibt keinen User mit der Kombination User/Passwort.");
            return "";
        }

        User user = (User) users.get(0);

        activeUsers.put(user.getId(),user);

        return user.getId();
    }
}
