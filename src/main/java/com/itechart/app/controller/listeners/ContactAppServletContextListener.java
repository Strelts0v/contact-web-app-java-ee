package com.itechart.app.controller.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * perform actions before ServletContext is initialized and destroyed
 */
public class ContactAppServletContextListener implements ServletContextListener{

    private static final String ROOT_PATH_VARIABLE_NAME = "rootPath";

    /**
     * sets rootPath property to implement system independent
     * and path-relative destination of file with logs
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        System.setProperty(ROOT_PATH_VARIABLE_NAME, context.getRealPath("/"));
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}