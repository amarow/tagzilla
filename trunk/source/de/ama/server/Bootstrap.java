
package de.ama.server;

import de.ama.server.services.Environment;
import de.ama.util.Util;

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
        System.out.println("*************************** stop ***************************");
        Environment.getPersistentService().stop();
        Environment.getCrawlerService().stop();
        System.out.println("TAGZILA-CrawlerServiceImpl endet OK.");
        System.out.println("*************************************************************");
    }

}