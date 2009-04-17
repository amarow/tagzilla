package de.ama.framework.servlet;

import com.caucho.hessian.server.HessianServlet;

public class HelloWorldImpl extends HessianServlet  {


    public HelloWorldImpl() {
        System.out.println("HelloWorldImpl.HelloWorldImpl AAAAAAAAAAAAAAAAAAA");
    }

    public String hello(String who)
  {
    return "Hello, " + who + "!";
  }
}
