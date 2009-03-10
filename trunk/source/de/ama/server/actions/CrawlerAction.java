package de.ama.server.actions;

import de.ama.server.services.Environment;
import de.ama.server.crawler.Crawler;
import de.ama.util.StringDivider;

import java.io.File;
import java.util.List;
import java.util.ArrayList;


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
            } else if(sd.pre().equals("stop")){
                String path = sd.post();
                File file = new File(path);
                if(file.exists() && file.isDirectory()){
                    Environment.getCrawlerService().stopCrawler(path);
                }
            } else if(sd.pre().equals("delete")){
                String path = sd.post();
                File file = new File(path);
                if(file.exists() && file.isDirectory()){
                    Environment.getCrawlerService().stopCrawler(path);
                    Environment.getCrawlerService().deleteCrawler(path);
                }
            }
        }

        List list = Environment.getCrawlerService().getAllCrawlers();
        setData(list);
    }

    public boolean needsUser() {
        return false;
    }
}