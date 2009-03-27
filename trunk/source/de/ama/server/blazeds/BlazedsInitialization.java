package de.ama.server.blazeds;

import flex.messaging.io.PropertyProxyRegistry;
import de.ama.db.PersistentMarker;


public class BlazedsInitialization {

    public BlazedsInitialization() {
        PropertyProxyRegistry.getRegistry().register(PersistentMarker.class, new PersistentBeanProxy());
    }

}
