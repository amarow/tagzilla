package de.ama.server.services;

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

    void startCrawler(String path, int pause);

    void stopAllCrawlers();

    void stopCrawler(String path);

    void deleteCrawler(String path);

    List getAllCrawlers();

}
