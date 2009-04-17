package de.ama.framework.servlet;

import com.caucho.hessian.server.HessianServlet;
import de.ama.framework.data.Data;

public class HelloWorldImpl extends HessianServlet {

    public Data dataTest(Data d){
        System.out.println("HelloWorldImpl.dataTest "+d.getType());
        return d;
    }


    public String xxxhello(String who) {
        System.out.println("HelloWorldImpl.xxxxhello");
        return "Hello, " + who + "!";
    }
}
