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
            startCrawler(new Crawler(arg, 5000));
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



    public void startCrawler(Crawler c) {
        Crawler crawler = findCrawler(c.rootPath);
        if (crawler == null) {

            Directory dir = (Directory) Environment.getPersistentService().getObject(new Query(Directory.class, "path", Query.EQ, c.rootPath), false);
            if (dir == null) {
                dir = new Directory();
                dir.setPath(c.rootPath);
                dir.setPause(c.pause);
                Environment.getPersistentService().makePersistent(dir);
                Environment.getPersistentService().commit();
            }

            System.out.println("starting crawler " + c.rootPath);
            Thread thread = new Thread(crawler);
            thread.start();
            crawlers.add(crawler);

        } else {
            if (crawler.isRunning()) {
                System.out.println("crawler " + c.rootPath + " allready started");
            } else {
                System.out.println("starting crawler " + c.rootPath);
                Thread thread = new Thread(crawler);
                thread.start();
            }
        }
    }

    public void stopCrawler(Crawler c) {
        Crawler crawler = findCrawler(c.rootPath);
        if (crawler != null && crawler.isRunning()) {
            System.out.println("stopping crawler " + crawler.getRootPath());
            crawler.setRunning(false);
            crawlers.remove(crawler);
        } else {
            System.out.println("crawler " + c.rootPath + " not found or allready stopped");
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

    public void deleteCrawler(Crawler c) {
        Crawler crawler = findCrawler(c.rootPath);
        Environment.getPersistentService().delete(new Query(Directory.class, "path", Query.EQ, c.rootPath+"*"));
        Environment.getPersistentService().delete(new Query(Handle.class, "path", Query.LIKE, c.rootPath+"*"));
        Environment.getPersistentService().commit();
        crawlers.remove(crawler);
    }

    public String infoCrawler(Crawler c) {
        long dirs = Environment.getPersistentService().getObjectCount(new Query(Directory.class, "path", Query.EQ, c.rootPath + "*"));
        long handles= Environment.getPersistentService().getObjectCount(new Query(Handle.class, "path", Query.LIKE, c.rootPath+"*"));
        return "dirs="+dirs+" handles="+handles;
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