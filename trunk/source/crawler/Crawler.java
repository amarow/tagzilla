package crawler;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 23.02.2009
 * Time: 21:26:39
 * To change this template use File | Settings | File Templates.
 */
public abstract class Crawler implements Runnable {
    private String rootPath;
    private HashMap storeA = new HashMap();
    private HashMap storeB = new HashMap();
    private HashMap allfilesA = storeA;
    private HashMap allfilesB = storeB;

    private long pause = 5000;
    private boolean running = true;
    private boolean adjusting = true;
    private long scannedFilesCount = 0;


    public Crawler(String path) {
        this(path, "", 10000);
    }

    public Crawler(String path, String filter, long pause) {
        this.rootPath = path;
        this.pause = pause;

        File root = new File(path);
        if (!root.isDirectory()) {
            throw new IllegalArgumentException("root file is not a directory, can not scan file " + root.getPath());
        }
    }


    public void setRunning(boolean running) {
        this.running = running;
    }

    private void walkDirs(File in) {
        if (in.isDirectory()) {
            File files[] = in.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if(file.isDirectory()){
                        walkDirs(file);
                    } else {
                        scannedFilesCount++;
                        if(scannedFilesCount%1500==0){
                            sleep(500);
                        }
                        String key = file.getPath();
                        Long current = (Long) allfilesA.remove(key);

                        if (current == null) {
                            allfilesB.put(key, new Long(file.lastModified()));
                            if (!adjusting) {
                                onChange(file, "added");
                            }
                        } else {
                            if (current.longValue() != file.lastModified()) {
                                allfilesB.put(key, new Long(file.lastModified()));
                                if (!adjusting) {
                                    onChange(file, "modified");
                                }
                            } else {
                                allfilesB.put(key, current);
                            }
                        }
                    }

                }
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public final void run() {

        while (running) {
            adjusting = allfilesA.isEmpty();
            scannedFilesCount = 0;
            long start = System.currentTimeMillis();
            File root = new File(rootPath);
            walkDirs(root);
            long stop = System.currentTimeMillis();

            Set set = allfilesA.keySet();
            for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                onChange(new File(key), "deleted");
            }
            allfilesA.clear();
            flip();

            long consum = stop - start;
            System.out.println("checked " + scannedFilesCount + " of (" + allfilesA.size() + ") files in " + consum + " millis.");
            System.out.println("waiting " + pause + " millis");
            sleep(pause);
        }

        System.out.println("By By");

    }

    private void flip() {
        if (allfilesA == storeA) {
            allfilesA = storeB;
            allfilesB = storeA;
        } else {
            allfilesA = storeA;
            allfilesB = storeB;
        }
    }

    public void stop(){
        running = false;
    }

    protected abstract void onChange(File file, String action);
}

