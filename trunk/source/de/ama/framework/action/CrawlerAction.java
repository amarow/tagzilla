package de.ama.framework.action;

import de.ama.tagzilla.data.CrawlerData;
import de.ama.server.services.Environment;

import java.util.List;


public class CrawlerAction extends ActionScriptAction {


    public CrawlerData crawler;

    public void execute() {

        try {

            if (message.equals("start")) {
                    Environment.getCrawlerService().startCrawler(crawler);
            } else if (message.equals("stop")) {
                    Environment.getCrawlerService().stopCrawler(crawler);
            } else if (message.equals("delete")) {
                    Environment.getCrawlerService().stopCrawler(crawler);
                    Environment.getCrawlerService().deleteCrawler(crawler);
            }

            commit();

        } finally {
            List list = Environment.getCrawlerService().getAllCrawlers();
            data = mapBosToDataTable(list, new CrawlerData());
            message = Environment.getCrawlerService().infoCrawler(crawler);
        }


    }


}