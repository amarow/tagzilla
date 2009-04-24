package de.ama.framework.servlet;

import de.ama.framework.util.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


public class DownloadServlet extends HttpServlet {
    static final int BUFFER_SIZE = 16384;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file = new File(request.getParameter("path"));
        prepareResponseFor(response, file);
        streamFileTo(response, file);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }


    private void streamFileTo(HttpServletResponse response, File file)  throws IOException {
        OutputStream os = response.getOutputStream();
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        while ((bytesRead = fis.read(buffer)) > 0) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
        fis.close();
    }

    private void prepareResponseFor(HttpServletResponse response, File file) {
        StringBuilder type = new StringBuilder("attachment; filename=");
        type.append(Util.normalizeFileName(file.getName()));
        response.setContentLength((int) file.length());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", type.toString());
    }
}