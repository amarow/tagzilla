package de.ama.server;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.xml.XmlConfiguration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

import de.ama.framework.servlet.DownloadServlet;
import de.ama.framework.servlet.HelloWorldImpl;
import de.ama.server.services.Environment;
import de.ama.server.services.impl.ActionServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 16.04.2009
 * Time: 09:36:31
 * To change this template use File | Settings | File Templates.
 */
public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();
        starter.start();
    }

    private void start() {


        try {

            // DB Connection ....
            Environment.initProduction();

            Server server = new Server(8080);

            // the Files from the webroot are served with a standard ResourceHandler
            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setResourceBase(Environment.getHomeDir().getAbsolutePath());

            // Servlet Contexts are here
            Context servletContexts = new Context(server, "/tagzilla", Context.SESSIONS);
            servletContexts.addServlet(new ServletHolder(new DownloadServlet()), "/download/*");
            servletContexts.addServlet(new ServletHolder(new ActionServiceImpl()), "/action/*");
            servletContexts.addServlet(new ServletHolder(new HelloWorldImpl()), "/hello/*");
//            servletContexts.setClassLoader(Thread.currentThread().getContextClassLoader());

            // put it all together
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resource_handler, servletContexts, new DefaultHandler()});
            server.setHandler(handlers);
            
            // and go
            server.start();
            server.join();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}

