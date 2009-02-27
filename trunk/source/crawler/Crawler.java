package crawler;

import java.io.File;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: x
 * Date: 23.02.2009
 * Time: 21:26:39
 * To change this template use File | Settings | File Templates.
 */
public abstract class Crawler implements Runnable{
    private String path;
    private HashMap allfiles = new HashMap();

    private FileFilter filter;
    private long pause = 5000;
    private boolean running=true;
    private boolean adjusting=true;
    private long scannedDirsCount=0;
    private long scannedFilesCount=0;


    public Crawler(String path) {
        this(path, "", 10000);
    }

    public Crawler(String path, String filter, long pause) {
        this.path = path;
        this.filter = new FileFilter(filter);
        this.pause = pause;

        File root = new File(path);
        if(!root.isDirectory()) {
            throw new IllegalArgumentException("root file is not a directory, can not scan file " + root.getPath());
        }
    }


    public void setRunning(boolean running) {
        this.running = running;
    }

    private void walkDirs(File in) {
        if(in.isDirectory()){
            File files[] = in.listFiles();
            if(files!=null) {
                for (int i = 0; i < files.length; i++) {
                     walkDirs(files[i]);
                }
            }

            String key = in.getPath();
            Long current = (Long) allfiles.get(key);
            if (current == null || (current.longValue() != in.lastModified())) {
                allfiles.put(key, new Long(in.lastModified()));
                checkDir(in);
            }
            scannedDirsCount++;
        }
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public final void checkDir( File f) {
        File files[] = f.listFiles(filter);
        if(files!=null){
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if(file.isFile()){
                    scannedFilesCount++;
                    if(scannedFilesCount%1000==0){
                        sleep(1);
                    }
                    String key = file.getPath();
                    Long current = (Long) allfiles.get(key);
                    if (current == null) {
                        allfiles.put(key, new Long(file.lastModified()));
                        if(!adjusting){
                            onChange(files[i], "add");
                        }
                    } else if (current.longValue() != file.lastModified()) {
                        allfiles.put(key, new Long(file.lastModified()));
                        if(!adjusting){
                            onChange(files[i], "modify");
                        }
                    }
                }
            }
        }
    }


    public final void run() {

        while(running) {
            adjusting = allfiles.isEmpty();
            scannedDirsCount=0;
            scannedFilesCount=0;
            long start = System.currentTimeMillis();
            File root = new File(path);
            walkDirs(root);
            long stop = System.currentTimeMillis();

            long consum = stop - start;
            System.out.println("checked " +scannedDirsCount+" directories and "+ scannedFilesCount+" of ("+allfiles.size() +") files in "+ consum +" millis.");
            System.out.println("waiting "+pause+" millis");
            sleep(pause);
        }

    }


    protected abstract void onChange(File file, String action);
}

class FileFilter implements java.io.FileFilter {
    private String filter;

    public FileFilter() {
        this.filter = "";
    }

    public FileFilter(String filter) {
        this.filter = filter;
    }

    public boolean accept(File file) {
        if ("".equals(filter)) {
            return true;
        }
        return (file.getName().endsWith(filter));
    }
}