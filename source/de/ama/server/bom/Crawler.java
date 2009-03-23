package de.ama.server.bom;

import de.ama.db.DB;
import de.ama.db.Query;
import de.ama.server.bom.Handle;
import de.ama.server.bom.Directory;
import de.ama.server.services.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 23.02.2009
 * Time: 21:26:39
 * To change this template use File | Settings | File Templates.
 */
public class Crawler implements Runnable {
    public  String rootPath;
    private HashMap storeA = new HashMap();
    private HashMap storeB = new HashMap();
    private HashMap allfilesA = storeA;
    private HashMap allfilesB = storeB;

    public  long pause = 5000;
    public  boolean running = false;
    private boolean adjusting = true;
    public  long scannedFilesCount = 0;
    private long commitCount = 0;

    public Crawler() {
    }

    public Crawler(String path, long pause) {
        this.rootPath = path;
        this.pause = pause;

        File root = new File(path);
        if (!root.isDirectory()) {
            throw new IllegalArgumentException("root file is not a directory, can not scan file " + root.getPath());
        }
    }

    public Crawler(Directory dir) {
        rootPath=dir.getPath();
        pause=dir.getPause();

        List l = getAllHandlesByPath(rootPath);
        for (int i = 0; i < l.size(); i++) {
            Handle handle = (Handle) l.get(i);
            storeA.put(handle.getPath(),handle.getLastmodified());
        }
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
                            onAddFile(file);
                        } else {
                            if (current.longValue() != file.lastModified()) {
                                allfilesB.put(key, new Long(file.lastModified()));
                                onRemoveFile(file);
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
        running = true;

        DB.joinCatalog("tagzilla");

        while (running) {
            try {
                adjusting = allfilesA.isEmpty();
                scannedFilesCount = 0;
                long start = System.currentTimeMillis();
                File root = new File(rootPath);
                walkDirs(root);
                long stop = System.currentTimeMillis();

                Set set = allfilesA.keySet();
                for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                    String key = (String) iterator.next();
                    onRemoveFile(new File(key));
                }
                allfilesA.clear();
                flip();

                double consum = (stop - start)/1000.0;
                System.out.println(rootPath +" checked " + scannedFilesCount +" of " + allfilesA.size() +" files in " + consum +" sec");
                DB.session().commit();

            } catch (Exception e) {
                DB.session().rollback();
            }

            sleep(pause);
        }

        DB.leaveCatalog();
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

    public String getRootPath() {
        return rootPath;
    }

    public String getrootPath() {
        return rootPath;
    }

    protected void onAddFile(File file){
        System.out.println("ADD    :"+ file.getPath());

        Handle h = new Handle();
        h.setPath(file.getPath());
        h.setLastmodified(file.lastModified());
        h.setSize(file.length());

        Environment.getPersistentService().makePersistent(h);
        commitBunch();
    };

    protected void onUpdateFile(File file){
        System.out.println("UPDATE :"+ file.getPath());

    };

    protected void onRemoveFile(File file){
        System.out.println("REMOVE :"+ file.getPath());
    };

    protected void commitBunch(){
        if(commitCount%100==0){
            Environment.getPersistentService().commit();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public long getScannedFilesCount() {
        return scannedFilesCount;
    }

    public boolean isAdjusting() {
        return adjusting;
    }

    public long getPause() {
        return pause;
    }

    public List getAllHandlesByPath(String path) {
        return Environment.getPersistentService().getObjects(new Query(Handle.class, "path", Query.LIKE, path+"*"));
    }


}

