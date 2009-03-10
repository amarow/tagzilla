
package de.ama.server;

import de.ama.server.services.Environment;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * User: x
 * Date: 22.05.2008
 */
public class Bootstrap implements ServletContextListener {


    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("*************************** start ****************************");
        Environment.initProduction();
        System.out.println("TAGZILA-CrawlerServiceImpl up and running OK.");

        System.out.println("**************************************************************");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("*************************** stopAllCrawlers ***************************");
        Environment.getPersistentService().stop();
        Environment.getCrawlerService().stopAllCrawlers();
        System.out.println("TAGZILA-CrawlerServiceImpl endet OK.");
        System.out.println("*************************************************************");
    }

}