package de.ama.server.services;

import de.ama.tagzilla.data.CrawlerData;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 06.03.2009
 * Time: 11:59:06
 * To change this template use File | Settings | File Templates.
 */
public interface CrawlerService {
    String NAME = "CrawlerService";

    void startCrawler(CrawlerData c);

    void stopAllCrawlers();

    void stopCrawler(CrawlerData c);

    void deleteCrawler(CrawlerData c);

    String infoCrawler(CrawlerData c);

    List getAllCrawlers();
}
