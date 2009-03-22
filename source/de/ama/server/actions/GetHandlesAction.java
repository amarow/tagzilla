package de.ama.server.actions;

import de.ama.server.services.Environment;
import de.ama.util.Util;


public class GetHandlesAction extends ServerAction {
    public String path;
    public String tag;

    public void execute() {

        if(!Util.isEmpty(tag)) {
            data = Environment.getCrawlerService().getAllHandlesByTag(tag);
        }

        if(!Util.isEmpty(path)) {
            data = Environment.getCrawlerService().getAllHandlesByPath(path);
        }
    }

    public boolean needsUser() {
        return false;
    }
}