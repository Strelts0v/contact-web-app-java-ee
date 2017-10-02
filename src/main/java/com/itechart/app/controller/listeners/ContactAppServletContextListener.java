package com.itechart.app.controller.listeners;

import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.jobs.JobScheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * perform actions before ServletContext is initialized and destroyed
 */
public class ContactAppServletContextListener implements ServletContextListener{

    private static final String ROOT_PATH_VARIABLE_NAME = "rootPath";

    public void contextInitialized(ServletContextEvent event) {
        // launches job scheduler
        JobScheduler scheduler = JobScheduler.getInstance();
        scheduler.initializeJobScheduler();
        AppLogger.info("Job scheduler was launched");
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}