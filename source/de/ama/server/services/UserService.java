package server.services;

import server.beans.User;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 21.08.2008
 * Time: 18:08:37
 * To change this template use File | Settings | File Templates.
 */
public interface UserService {
    public static String NAME = "UserService";
    public static String USER_ID_SEQUENZE = "user_id";

    public User getActiveUser(String id);

    public String newUser(String name, String pwd);
    public String login(String name, String pwd);
    public void removeUser(User user);


}
