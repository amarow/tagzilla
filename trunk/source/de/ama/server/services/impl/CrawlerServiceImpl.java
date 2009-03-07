package de.ama.server.services.impl;

import de.ama.server.crawler.Crawler;
import de.ama.server.services.CrawlerService;
import de.ama.util.Util;

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
            startCrawler(arg,5000);
        }
        showThreads();
    }

    public void showThreads() {
        System.out.println("*****************************************************************************");
        for (int i = 0; i < crawlers.size(); i++) {
            Crawler  c = crawlers.get(i);
            System.out.println("crawler "+ Util.formatString(c.getRootPath(),20));
        }
        System.out.println("*****************************************************************************");
    }

    public void startCrawler(String path, int pause){
        Crawler crawler = findCrawler(path);
        if(crawler==null){
            crawler = new Crawler(path, pause);
            System.out.println("starting crawler "+ path);
            Thread thread = new Thread(crawler);
            thread.start();
            crawlers.add(crawler);
        } else {
            System.out.println("crawler "+ path+ " allready started");
        }
    }

    public void stopCrawler(String path){
        Crawler crawler = findCrawler(path);
        if(crawler!=null){
            System.out.println("stopping crawler "+ crawler.getRootPath());
            crawler.setRunning(false);
            crawlers.remove(crawler);
        } else {
            System.out.println("crawler "+ path + " not found");
        }
    }

    private Crawler findCrawler(String path){
        for (Iterator<Crawler> iterator = crawlers.iterator(); iterator.hasNext();) {
            Crawler c = iterator.next();
            if(c.getRootPath().equals(path)){
                return c;
            }
        }
        return null;
    }

    public void stop(){
        for (Iterator<Crawler> iterator = crawlers.iterator(); iterator.hasNext();) {
            Crawler c = iterator.next();
            System.out.println("stopping crawler "+ c.getRootPath() );
            c.setRunning(false);
            iterator.remove();
        }
    }


}