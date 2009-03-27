package de.ama.server.blazeds;

import flex.messaging.io.BeanProxy;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 27.03.2009
 * Time: 13:44:12
 * To change this template use File | Settings | File Templates.
 */
public class PersistentBeanProxy extends BeanProxy {
    @Override
    public Object createInstance(String className) {
        System.out.println("PersistentBeanProxy.createInstance ----------> " + className);
        return super.createInstance(className);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
