package de.ama.server.crawler;

import de.ama.server.crawler.Crawler;

import java.io.*;

public class Server {

    public static void main(String args[]) {
        Crawler c = new Crawler("d:/", "", 5000) {
            protected void onChange(File file, String action) {
                System.out.println("File " + file.getAbsolutePath() + " action: " + action);
            }
        };
        Thread t = new Thread(c);
        t.start();
    }
}