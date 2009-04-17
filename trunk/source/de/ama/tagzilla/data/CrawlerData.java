package de.ama.tagzilla.data;

import de.ama.framework.data.Data;
import de.ama.util.Util;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 23.02.2009
 * Time: 21:26:39
 * To change this template use File | Settings | File Templates.
 */
public class CrawlerData extends Data {
    public  String rootPath;
    public  long pause = 5000;
    public  boolean running = false;
    public  long scannedFilesCount = 0;

    public String getGuiRepresentation() {
        return Util.saveToString(rootPath);
    }

}