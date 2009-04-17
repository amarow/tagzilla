package de.ama.server.services.impl;

import de.ama.db.Query;
import de.ama.framework.action.ActionScriptAction;
import de.ama.tagzilla.data.User;
import de.ama.server.services.Environment;
import de.ama.server.services.UserService;
import de.ama.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserServiceImpl implements UserService {
    Map<Long , User> activeUsers = new HashMap<Long , User>() ;

    public Long  newUser(String name, String pwd) {

        if(Util.isEmpty(name)){
            ActionScriptAction.getCurrent().setMessage("bitte gebe einen Namen an");
            return new Long(-1);
        }

        if(Util.isEmpty(pwd)){
            ActionScriptAction.getCurrent().setMessage("bitte gebe einen Passwort ein");
            return new Long(-1);
        }

        long count = Environment.getPersistentService().getObjectCount(new Query(User.class, "name", Query.EQ, name).
                                                                   and(new Query(User.class, "pwd", Query.EQ, pwd)));
        if (count == 0) {

            long id = Environment.getPersistentService().getNextNumber(USER_ID_SEQUENZE);

            User user = new User(name, pwd, id);
            Environment.getPersistentService().makePersistent(user);
            return user.getId();
        }

        ActionScriptAction.getCurrent().setMessage("Es gibt schon einen User mit der Kombination User/Passwort. Bitte wählen Sie ein anderes Passwort ");
        return null;
    }

    public void removeUser(User user) {
        Environment.getPersistentService().delete(Environment.getPersistentService().getOidString(user));
    }

    public User getActiveUser(Long id) {
        User user = activeUsers.get(id);
        if(user==null) {
            throw new RuntimeException("no active user / session dead") ;
        }
        return user;
    }

    public boolean logout(User user){
        return (activeUsers.remove(user.getId())!=null);
    }

    public Long login(String name, String pwd) {

        if(Util.isEmpty(name)){
            ActionScriptAction.getCurrent().setMessage("bitte gebe einen Namen an");
            return new Long(-1);
        }
        if(Util.isEmpty(pwd)){
//            throw new RuntimeException("bitte geben Sie ein Passwort ein") ;
            ActionScriptAction.getCurrent().setMessage("bitte gebe einen Passwort ein");
            return new Long(-1);
        }

        List users = Environment.getPersistentService().getObjects(new Query(User.class, "name", Query.EQ, name).
                                                                    and(new Query(User.class, "pwd", Query.EQ, pwd)));
        if (users.size() == 0) {
            ActionScriptAction.getCurrent().setMessage("Es gibt keinen User mit der Kombination User/Passwort.");
            return new Long(-1);
        }

        User user = (User) users.get(0);

        activeUsers.put(user.getId(),user);

        return user.getId();
    }
}
