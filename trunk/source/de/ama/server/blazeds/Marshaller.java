package de.ama.server.blazeds;

import de.ama.db.DB;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 26.03.2009
 * Time: 18:03:29
 * To change this template use File | Settings | File Templates.
 */
public class Marshaller implements flex.messaging.io.TypeMarshaller{
    public Object createInstance(Object o, Class aClass) {
        System.out.println("Marshaller.createInstance"+o.getClass().getName());
        return o;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object convert(Object o, Class aClass) {
        System.out.println("Marshaller.convert"+o.getClass().getName());

        DB.joinCatalog("tagzilla");
        return o;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
