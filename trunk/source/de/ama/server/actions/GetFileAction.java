package de.ama.server.actions;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 02.04.2009
 * Time: 16:35:57
 * To change this template use File | Settings | File Templates.
 */
public class GetFileAction extends ServerAction {
    static final int BUFFER_SIZE = 1024*5;
    public String fileName;


    @Override
    public void execute() throws Exception {
        System.out.println("GetFileAction.execute reading file:"+fileName);

        File file = new File(fileName);
        int l = (int) file.length();
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[l];
        int bytesRead = fis.read(buffer);
        fis.close();
        data=buffer;
        
        System.out.println("GetFileAction.execute read "+bytesRead+" bytes");
    }


}
