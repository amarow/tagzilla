package de.ama.server.actions;

import de.ama.server.services.Environment;
import de.ama.util.StringDivider;

import java.io.File;


public class CrawlerAction extends ServerAction {

    public void execute() {
        String tmp = (String) getData();
        StringDivider sd = new StringDivider(tmp,".");
        if(sd.ok()){
            if(sd.pre().equals("start")){
                String path = sd.post();
                File file = new File(path);
                if(file.exists() && file.isDirectory()){
                    Environment.getCrawlerService().startCrawler(path,5000);
                }
            }
            if(sd.pre().equals("stop")){
                String path = sd.post();
                File file = new File(path);
                if(file.exists() && file.isDirectory()){
                    Environment.getCrawlerService().stopCrawler(path);
                }
            }
        }
    }

    public boolean needsUser() {
        return false;
    }
}