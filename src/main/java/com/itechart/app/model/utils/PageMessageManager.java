package com.itechart.app.model.utils;

import java.util.ResourceBundle;

/**
 * Not-instantiated class that gets database configuration properties
 * from resource bundle messages.properties
 */
public class PageMessageManager {

    /** object for extracting messages from resource bundle messages.properties */
    private static final ResourceBundle messageBundle
            = ResourceBundle.getBundle("messages");

    /**
     * private constructor of class, which implement non-instantiation
     */
    private PageMessageManager(){}

    /**
     * gets message content from resource bundle
     * @param key - key for extracting message content
     * @return message content
     */
    public static String getMessage(final String key) {
        return messageBundle.getString(key);
    }
}