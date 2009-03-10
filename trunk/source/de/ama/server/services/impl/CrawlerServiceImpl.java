package de.ama.server.services.impl;

import de.ama.server.crawler.Crawler;
import de.ama.server.services.CrawlerService;
import de.ama.server.services.Environment;
import de.ama.server.bom.Directory;
import de.ama.server.bom.Handle;
import de.ama.db.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CrawlerServiceImpl implements CrawlerService {

    private List<Crawler> crawlers = new ArrayList<Crawler>();

    public static void main(String args[]) {
        new CrawlerServiceImpl(args);
    }

    public CrawlerServiceImpl() {
    }

    public CrawlerServiceImpl(String args[]) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            startCrawler(arg, 5000);
        }
    }



    private Crawler findCrawler(String path) {
        for (Iterator<Crawler> iterator = crawlers.iterator(); iterator.hasNext();) {
            Crawler c = iterator.next();
            if (c.getRootPath().equals(path)) {
                return c;
            }
        }
        return null;
    }



    public void startCrawler(String path, int pause) {
        Crawler crawler = findCrawler(path);
        if (crawler == null) {

            Directory dir = (Directory) Environment.getPersistentService().getObject(new Query(Directory.class, "path", Query.EQ, path), false);
            if (dir == null) {
                dir = new Directory();
                dir.setPath(path);
                dir.setPause(pause);
                Environment.getPersistentService().makePersistent(dir);
                Environment.getPersistentService().commit();
            }

            crawler = new Crawler(path, pause);
            System.out.println("starting crawler " + path);
            Thread thread = new Thread(crawler);
            thread.start();
            crawlers.add(crawler);

        } else {
            if (crawler.isRunning()) {
                System.out.println("crawler " + path + " allready started");
            } else {
                System.out.println("starting crawler " + path);
                Thread thread = new Thread(crawler);
                thread.start();
            }
        }
    }

    public void stopCrawler(String path) {
        Crawler crawler = findCrawler(path);
        if (crawler != null && crawler.isRunning()) {
            System.out.println("stopping crawler " + crawler.getRootPath());
            crawler.setRunning(false);
            crawlers.remove(crawler);
        } else {
            System.out.println("crawler " + path + " not found or allready stopped");
        }
    }


    public void stopAllCrawlers() {
        for (Iterator<Crawler> iterator = crawlers.iterator(); iterator.hasNext();) {
            Crawler c = iterator.next();
            System.out.println("stopping crawler " + c.getRootPath());
            c.setRunning(false);
            iterator.remove();
        }
    }

    public void deleteCrawler(String path) {
        Environment.getPersistentService().delete(new Query(Directory.class, "path", Query.EQ, path));
        Environment.getPersistentService().delete(new Query(Handle.class, "path", Query.LIKE, path));
        Environment.getPersistentService().commit();
    }

    
    public List getAllCrawlers() {
        List list = Environment.getPersistentService().getObjects(new Query(Directory.class));
        for (int i = 0; i < list.size(); i++) {
            Directory directory = (Directory) list.get(i);
            if(findCrawler(directory.getPath())==null) {
               Crawler c = new Crawler(directory.getPath(),directory.getPause());
               crawlers.add(c);
            }
        }
        return crawlers;
    }


}