package de.ama.framework.action;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 02.04.2009
 * Time: 16:35:57
 * To change this template use File | Settings | File Templates.
 */
public class FileAction extends ActionScriptAction {
    static final int BUFFER_SIZE = 1024*5;
    public String fileName;


    @Override
    public void execute() throws Exception {
        if(data==null){
            readFile();
        } else {
            writeFile();
        }
    }

    private void writeFile() {
        System.out.println("FileAction.execute writing file:"+fileName);

        File file = new File(fileName);

        int l = (int) file.length();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write((byte[]) data);
            fos.close();
            data = null;
        } catch (IOException e) {
            detailErrorMessage = e.getMessage();
        }

        file = new File(fileName);
        System.out.println("FileAction.execute wrote "+file.length()+" bytes");
    }


    private void readFile() throws IOException {
        System.out.println("FileAction.execute reading file:"+fileName);

        File file = new File(fileName);
        int l = (int) file.length();
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[l];
        int bytesRead = fis.read(buffer);
        fis.close();
        data=buffer;

        System.out.println("FileAction.execute read "+bytesRead+" bytes");
    }


}
