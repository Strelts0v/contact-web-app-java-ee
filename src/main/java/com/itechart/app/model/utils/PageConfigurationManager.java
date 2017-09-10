package com.itechart.app.model.utils;

import java.util.ResourceBundle;

/**
 * Not-instantiated class that gets database configuration properties
 * from resource bundle pages.properties
 */
public class PageConfigurationManager {

    /** object for extracting names of pages from resource bundle pages.properties */
    private static final ResourceBundle pageBundle
            = ResourceBundle.getBundle("pages");

    /**
     * private constructor of class, which implement non-instantiation
     */
    private PageConfigurationManager(){}

    /**
     * gets name page from resource bundle
     * @param key - key for extracting page
     * @return path to page
     */
    public static String getPageName(final String key) {
        return pageBundle.getString(key);
    }
}
