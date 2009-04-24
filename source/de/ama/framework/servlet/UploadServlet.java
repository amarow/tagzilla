package de.ama.framework.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class UploadServlet extends HttpServlet {
    static final int BUFFER_SIZE = 16384;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String serverPath = request.getParameter("serverPath");
        String fileName = request.getParameter("fileName");

        File file = new File(serverPath);
        BufferedInputStream is = new BufferedInputStream(request.getInputStream());
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        byte[] buff = new byte[BUFFER_SIZE];
        int len;
        while (0 < (len = is.read(buff))) {
            os.write(buff, 0, len);
        }
        is.close();
        os.flush();
        os.close();

        response.setStatus(HttpServletResponse.SC_OK);
    }



}