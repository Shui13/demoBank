package com.sample.bankAccount;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Application {

    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);

        ServletContextHandler ctx =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        ctx.setContextPath("/");
        server.setHandler(ctx);

        ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
        serHol.setInitOrder(1);
        serHol.setInitParameter("jersey.config.server.provider.packages",
                "com.sample.bankAccount");
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
