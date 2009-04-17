/*
 * PreMainInitializer.java
 *
 * Created on 10. Juli 2003, 18:21
 */

package de.ama.framework.util;

import de.ama.util.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author  Andreas Marochow
 * Diese Klasse untersucht ein oder mehrere Zip-Archive und das classes Verzeichnis, um darin spezielle Klassen zu finden, die
 * sich selbst initialisieren oder irgendwo registieren wollen.
 * Zu diesm Zweck überschreibt der Anwendungsentwickler die Methode preMain() (möglicherweise in einer BasisKlasse. )
 *
 *     Beispiel : Materialien
 *         public abstract class BasicMaterial implements BasicMaterialIfc, BearbeitbarIfc {
 *
 *          public void preMain(){
 *              MaterialFactory.session().registerMaterial(this);
 *          }
 *           ...........
 *
 *  Ab jetzt können einfach neue Materialien erfunden werden. Diese registrieren sich selbstätig
 *  in ihrer Materialfactory. Die preMain()-Methode konnte in der BasisKlasse implementiert werden.
 *
 * Der Vorteil ist das nicht mehr in zentralen Klassen, die eigentlich nichts mit den fachlichen
 * Zusammenhängen zu tun haben, irgendwelche Registrierungen vorgenommen werden müssen. Die Erfahrung hat
 * gezeigt das genau diese Einträge in Zentralen Klassen oft vergessen werden, oder nicht eingecheckt
 * werden.
 *
 * Neue Klassen_Namens-Pattern können hier in der initForServer() oder initForClient() Methode registriert werden.
 *
 *
 */
public class PreMainInitializer {
    
    /**
     * Die zentralen Initialisierungsmethoden für den Client oder RmiServiceIfc.
     * Hier können neue NamensPattern aufgenommen werden.
     */
    public static void initForClient(){
        PreMainInitializer preMainInitializer = new PreMainInitializer();

        // Klassen mit den folgenden Pattern im Namen werden geladen. Es wird versucht eine Methode
        // namens preMain() auf ihnen aufzurufen.
        preMainInitializer.addKeyWord("Data");

        // Die Zip-archives in der gleichenREihenfolge wie im ClassPath angeben.
        preMainInitializer.addAllFilesIn(Environment.getClassDir());

        preMainInitializer.execute();
    }
    
    public static void initForServer(){
        try{
            PreMainInitializer preMainInitializer = new PreMainInitializer();
            
            // Klassen mit den folgenden Pattern im Namen werden geladen. Es wird versucht eine Methode
            // namens preMain() auf ihnen aufzurufen.
            preMainInitializer.addKeyWord("Data");


            // Die Zip-archives in der gleichen Reihenfolge wie im ClassPath angeben.
            preMainInitializer.addAllFilesIn(Environment.getClassDir());

            preMainInitializer.execute();
            
            
        }
        catch(Throwable e){
            e.printStackTrace();
        }
    }
    
    
    ////////////////////////////// Implementierung ///////////////////////////
    ////////////////////////////// Implementierung ///////////////////////////
    ////////////////////////////// Implementierung ///////////////////////////

    private List keyWords = new ArrayList();
    private List zipFileNames = new ArrayList();
    private Set relevantZipEntries = new TreeSet();
    
    public void execute(){
        
        long startTime = System.currentTimeMillis();
        
        
        for(int i=0;i<zipFileNames.size();i++){
            String zipFileName=(String)zipFileNames.get(i);
            if(parseZip(zipFileName)){
                debug(zipFileName+" to initialized preMain OK.");
            }else{
                debug("No "+zipFileName+" to initialize preMain.");
            }
        }
        
        initEntries();
        String str = "preMain Time="+(System.currentTimeMillis()-startTime);
        debug(str);
    }
    
    private boolean parseZip(String zipFileName){
        try {
            File zipFile=new File(zipFileName);
            if(!zipFile.exists()){
                return false;
            }
            ZipFile zf = new ZipFile(zipFile);
            // Enumerate each entry
            for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
                // Get the entry name
                String zipEntryName = ((ZipEntry)entries.nextElement()).getName();
                for(int i=0;i<keyWords.size();i++){
                    String keyWord=(String)keyWords.get(i);
                    if(zipEntryName.indexOf(keyWord)>=0){
                        //if(!relevantZipEntries.contains(zipEntryName))
                        relevantZipEntries.add(zipEntryName);
                    }
                }
            }
        } catch (IOException e) {
            debug( e.getMessage() );
        }
        return true;
    }
    
    
    private void initEntries(){
        debug("relevantZipEntries.size()="+relevantZipEntries.size());
        int noPreMainClassCount=0;
        int preMainClassCount=0;
        try {
            Iterator iter = relevantZipEntries.iterator();
            while (iter.hasNext()){
                String zipEntry=(String)iter.next();
                String className = zipEntry.replace('/','.').substring(0,zipEntry.length()-6);
                try {
                    Class c = Class.forName(className);
                    Method preMain=c.getMethod("preMain",null);
                    if(preMain!=null){
                       // System.out.println("Try to preInit :"+className);
                        Object o = c.newInstance();
                        preMain.invoke(o, null);
                        preMainClassCount++;
                    }
                } catch (Throwable e) {
                    noPreMainClassCount++;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        debug("preMainClassCount="+preMainClassCount+"  noPreMainClassCount="+noPreMainClassCount);
    }
    
    
    public void addKeyWord(String str){
        keyWords.add(str);
    }
    
    public void addZipFile(String str){
        zipFileNames.add(str);
    }
    
    
    
    public void addAllFilesIn(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i=0; i<children.length; i++) {
                addAllFilesIn(new File(file, children[i]));
            }
        } else {
            int len = Environment.getClassDir().getAbsolutePath().length();
            String fName=file.getAbsolutePath();
            String dirEntry = fName.substring(len,fName.length());
            dirEntry = dirEntry.replace('\\','/');
            if (dirEntry.startsWith("/")) {
                dirEntry = dirEntry.substring(1);
            }
            
            for(int i=0;i<keyWords.size();i++){
                String keyWord=(String)keyWords.get(i);
                if(dirEntry.indexOf(keyWord)>=0){
                    relevantZipEntries.add(dirEntry);
                }
            }
        }
    }
    
    
    private void debug(String msg){
       System.out.println(msg);
    }
}
