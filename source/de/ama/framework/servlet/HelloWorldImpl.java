package de.ama.framework.servlet;

import com.caucho.hessian.server.HessianServlet;

public class HelloWorldImpl extends HessianServlet  {

    @Override
    public void setHomeAPI(Class api) {
        super.setHomeAPI(api);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public HelloWorldImpl() {
        System.out.println("HelloWorldImpl.HelloWorldImpl AAAAAAAAAAAAAAAAAAA");
    }

    public String hello(String who)
  {
    return "Hello, " + who + "!";
  }
}
