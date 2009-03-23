package de.ama.server.services;

import de.ama.server.bom.Crawler;
import de.ama.db.OidIterator;

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

    void startCrawler(Crawler c);

    void stopAllCrawlers();

    void stopCrawler(Crawler c);

    void deleteCrawler(Crawler c);

    String infoCrawler(Crawler c);

    List getAllCrawlers();
}
