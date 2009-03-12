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

    public void execute(){
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

    public String saveBean(Object bean){
        markGraphDirty((Persistent) bean);
        return Environment.getPersistentService().makePersistent(bean);
    }


    public void markGraphDirty(Persistent p) {
       p.setDirty(true);
       p.setLoaded(true);
//       Class c = p.getClass();
//        try {
//            Method method = c.getMethod("setDirty",new Class[]{Boolean.class});
//            if(method!=null){
//                method.invoke()
//            }
//        } catch (NoSuchMethodException e) {
//        }
    }

    public void deleteBean(String oid){
        Environment.getPersistentService().delete(oid);
    }

    public Object createBean(String type){
        return Environment.getBean(type);
    }

    public Object copyBean(Object bean){
        XmlService m = (XmlService) Environment.getBean(XmlService.NAME);
        String t = m.toXmlString(bean);
//        node.eraseOids();
        return m.toObject(t);
    }


    public Object loadBean(String oid){
        return Environment.getPersistentService().getObject(oid);
    }

    public List queryBeans(){
//        String type = qd.getNode("/type").getText();
//        Class clazz = Environment.getBeanClass(type);
//        List statements = qd.getNodes("/statement");
//        Query q = new Query(clazz);
//        for (int i = 0; i < statements.size(); i++) {
//            ActionData statement = (ActionData) statements.get(i);
//            q.and(new Query(clazz, statement.getAttribute("path"), statement.getAttribute("op"), statement.getAttribute("value")));
//        }
//
//        XmlMapper m = new XmlMapper();
//        List objects = Environment.getPersistentService().getObjects(q);
//        return m.fromObject(objects);
        return new ArrayList();
    }

    public void addError(String message) {
    }

    public void addMessage(String message) {
    }


    public boolean needsUser() {
        return true;
    }

    public String getName() {
        return Util.getUnqualifiedClassName(getClass());
    }
}
