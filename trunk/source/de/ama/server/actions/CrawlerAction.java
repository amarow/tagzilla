package de.ama.server.actions;

import de.ama.server.services.Environment;
import de.ama.server.bom.Crawler;


public class CrawlerAction extends ServerAction {


    public Crawler crawler;

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
            data = Environment.getCrawlerService().getAllCrawlers();
            message = Environment.getCrawlerService().infoCrawler(crawler);
        }


    }

    public boolean needsUser() {
        return false;
    }
}