package de.ama.server.blazeds;

import de.ama.framework.data.DataDictionary;
import de.ama.server.bom.CrawlerData;
import de.ama.server.bom.DeskData;
import de.ama.server.bom.HandleData;
import de.ama.server.bom.TagData;


public class BlazedsInitialization {

    public BlazedsInitialization() {
        DataDictionary.registerData(new CrawlerData());
        DataDictionary.registerData(new HandleData());
        DataDictionary.registerData(new TagData());
        DataDictionary.registerData(new DeskData());
    }

}
