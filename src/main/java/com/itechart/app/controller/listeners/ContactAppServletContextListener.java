package com.itechart.app.controller.listeners;

import com.itechart.app.model.jobs.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * perform actions before ServletContext is initialized and destroyed
 */
public class ContactAppServletContextListener implements ServletContextListener{

    private final Logger logger = LoggerFactory.getLogger(ContactAppServletContextListener.class);

    public void contextInitialized(ServletContextEvent event) {
        // launches job scheduler
        JobScheduler scheduler = JobScheduler.getInstance();
        scheduler.initializeJobScheduler();
        logger.info("Job scheduler was launched");
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}