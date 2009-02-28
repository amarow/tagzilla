package server.services;

import de.ama.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * User: x
 * Date: 19.05.2008
 */
public class Environment {
    private static Map beanDictionary = new HashMap();
    private static Map singletons = new HashMap();

    /**
     * Common Umgebung "hochfahren"
     */
    public static void initCommon(){
        singletons.put(PersistentService.NAME   ,getBean("server.services.impl.PersistentServiceImpl"));
        singletons.put(UserService.NAME         ,getBean("server.services.impl.UserServiceImpl"));
        singletons.put(ActionService.NAME       ,getBean("server.services.impl.ActionServiceImpl"));

        beanDictionary.put(XmlService.NAME          ,"server.services.impl.XmlServiceImpl");

//        Endpoint.publish("http://localhost:8085/services", getBean(ActionService.NAME));
        System.out.println("WebService ActionService up and running");
    }
    /**
     * Produktionsumgebung "hochfahren"
     */
    public static void initProduction(){
        if(!beanDictionary.isEmpty()) return ;
        initCommon();
    }

    /**
     * Testumgebung "hochfahren"
     * Hier können andere Services instantiiert werden die für Tests geeigneter sind (Mocks, etc)
     * In der TEstclasse muss eben initTest anstatt initProduction()  gerufen werden.
     */
    public static void initTest(){
        if(!beanDictionary.isEmpty()) return ;
        initCommon();
    }

    public static PersistentService getPersistentService(){
        return (PersistentService) getSingleton(PersistentService.NAME);
    }

    public static UserService getUserService(){
        return (UserService) getSingleton(UserService.NAME);
    }

    public static ActionService getActionService() {
        return (ActionService) getSingleton(ActionService.NAME);
    }

    public static XmlService getXmlService() {
        return (XmlService) getBean(XmlService.NAME);
    }

    public static Object getSingleton(String name) {
        return singletons.get(name);
    }

    public static Object getBean(String name) {
        Object singleton = singletons.get(name);
        if(singleton != null){
           return singleton;
        }

        Object ret = null;
        try {
            Class c = getBeanClass(name);
            ret = Util.createObject(c);
        } catch (Exception e) {
            throw new IllegalArgumentException("can not create bean for name ["+ name+"]",e);
        }
        if(ret==null){
            throw new IllegalArgumentException("can not create bean for name ["+ name+"]");
        }
        return ret;
    }

    public static Class getBeanClass(String name) {
        String className = (String) beanDictionary.get(name);

        if(className==null && !name.contains(".")){
            className="server.beans."+Util.firstCharToUpper(name);
        }

        if(className==null ){
            className=name;
        }

        Class ret = null;
        try {
            ret = Util.createClass(className);
        } catch (Exception e) {
            throw new IllegalArgumentException("can not create bean-class for name ["+ name+"]",e);
        }
        if(ret==null){
            throw new IllegalArgumentException("can not create bean-class for name ["+ name+"]");
        }
        return ret;
    }


}
