package de.ama.server;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.handler.AbstractHandler;
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

    private Handler createHandler(){
        Handler handler = new AbstractHandler() {
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
                    throws IOException, ServletException {
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("<h1>Tagzilla Server</h1>");
                ((Request) request).setHandled(true);
            }
        };
        return handler;
    }

    private void start(){


        try {
            Environment.initProduction();

            Server server = new Server(8080);
//            server.setHandler(createHandler());
            Context root = new Context(server,"/tagzilla",Context.SESSIONS);
            root.addServlet(new ServletHolder(new DownloadServlet()),"/download/*");
            root.addServlet(new ServletHolder(new ActionServiceImpl()),"/action/*");
            root.addServlet(new ServletHolder(new HelloWorldImpl()),"/hello/*");

            server.start();
            System.out.println("**********************************************");
            System.out.println("* OK up and running");
            System.out.println("**********************************************");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}

