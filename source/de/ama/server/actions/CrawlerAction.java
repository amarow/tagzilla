package de.ama.server.actions;

import de.ama.server.services.Environment;
import de.ama.server.crawler.Crawler;
import de.ama.util.StringDivider;

import java.io.File;
import java.util.List;
import java.util.ArrayList;


public class CrawlerAction extends ServerAction {


    public Crawler crawler;

    public void execute() {

        if (message.equals("start")) {
                Environment.getCrawlerService().startCrawler(crawler);
        } else if (message.equals("stop")) {
                Environment.getCrawlerService().stopCrawler(crawler);
        } else if (message.equals("delete")) {
                Environment.getCrawlerService().stopCrawler(crawler);
                Environment.getCrawlerService().deleteCrawler(crawler);
        } else if (message.equals("delete")) {
                Environment.getCrawlerService().stopCrawler(crawler);
                Environment.getCrawlerService().deleteCrawler(crawler);
        }

        data = Environment.getCrawlerService().getAllCrawlers();
        message = Environment.getCrawlerService().infoCrawler(crawler);
    }

    public boolean needsUser() {
        return false;
    }
}