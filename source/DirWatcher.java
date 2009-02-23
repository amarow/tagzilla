import java.util.*;
import java.io.*;

public abstract class DirWatcher extends TimerTask {
    private String path;
    private HashMap allfiles = new HashMap();
    private DirFilterWatcher dfw;

    public DirWatcher(String path) {
        this(path, "");
    }

    public DirWatcher(String path, String filter) {
        this.path = path;
        dfw = new DirFilterWatcher(filter);

        List dirs = collectDirs(new File(path), new ArrayList());
    }

    public void collectAllFiles(File f) {
        if(f.isDirectory()){
            File files[] = f.listFiles(dfw);
            for (int i = 0; i < files.length; i++) {
               if(files[i].isDirectory()) {
                   collectAllFiles(files[i]);
               } else {
                   allfiles.put(files[i], new Long(files[i].lastModified()));
               }
            }
        }
    }

    public List collectDirs(File f, List ret) {
        if(f.isDirectory()){
            File files[] = f.listFiles(dfw);
            for (int i = 0; i < files.length; i++) {
               if(files[i].isDirectory()) {
                   ret.add(files[i]);
                   collectDirs(files[i], ret);
               }
            }
        }
        return ret;
    }

    public final void run() {
        System.out.println("scaning " +allfiles.size()+" files");

        List dirs = collectDirs(new File(path), new ArrayList());
        for (int i = 0; i < dirs.size(); i++) {
            File dir = (File) dirs.get(i);
            checkDir(dir);
        }
    }

    public final void checkDir( File f) {
        HashSet checkedFiles = new HashSet();
        File filesArray[] = f.listFiles(dfw);
        System.out.println("scan allfiles in " + f.getName() +" (" +allfiles.size()+")");

        // scan the allfiles and check for modification/addition
        for (int i = 0; i < filesArray.length; i++) {
            Long current = (Long) allfiles.get(filesArray[i]);
            checkedFiles.add(filesArray[i]);
            if (current == null) {
                // new file
                allfiles.put(filesArray[i], new Long(filesArray[i].lastModified()));
                onChange(filesArray[i], "add");
            } else if (current.longValue() != filesArray[i].lastModified()) {
                // modified file
                allfiles.put(filesArray[i], new Long(filesArray[i].lastModified()));
                onChange(filesArray[i], "modify");
            }
        }

        // now check for deleted allfiles
        Set ref = ((HashMap) allfiles.clone()).keySet();
        ref.removeAll((Set) checkedFiles);
        Iterator it = ref.iterator();
        while (it.hasNext()) {
            File deletedFile = (File) it.next();
            allfiles.remove(deletedFile);
            onChange(deletedFile, "delete");
        }
    }

    protected abstract void onChange(File file, String action);
}

class DirFilterWatcher implements FileFilter {
    private String filter;

    public DirFilterWatcher() {
        this.filter = "";
    }

    public DirFilterWatcher(String filter) {
        this.filter = filter;
    }

    public boolean accept(File file) {
        if ("".equals(filter)) {
            return true;
        }
        return (file.getName().endsWith(filter));
    }
}
