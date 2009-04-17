package de.ama.server.blazeds;

import de.ama.framework.data.DataDictionary;
import de.ama.tagzilla.data.CrawlerData;
import de.ama.tagzilla.data.DeskData;
import de.ama.tagzilla.data.HandleData;
import de.ama.tagzilla.data.TagData;


public class BlazedsInitialization {

    public BlazedsInitialization() {
        DataDictionary.registerData(new CrawlerData());
        DataDictionary.registerData(new HandleData());
        DataDictionary.registerData(new TagData());
        DataDictionary.registerData(new DeskData());
    }

}
