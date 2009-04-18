package de.ama.tagzilla.actions;

import de.ama.tagzilla.data.CrawlerData;
import de.ama.server.services.Environment;
import de.ama.framework.action.ActionScriptAction;

import java.util.List;


public class CrawlerAction extends ActionScriptAction {


    public CrawlerData crawler;
    public String code;

    public void execute() {

        try {

            if (code.equals("start")) {
                    Environment.getCrawlerService().startCrawler(crawler);
            } else if (code.equals("stop")) {
                    Environment.getCrawlerService().stopCrawler(crawler);
            } else if (code.equals("delete")) {
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