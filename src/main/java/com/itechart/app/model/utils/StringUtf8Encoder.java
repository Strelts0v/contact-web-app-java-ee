package com.itechart.app.model.utils;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class StringUtf8Encoder {

    private static final Logger logger = LoggerFactory.getLogger(StringUtf8Encoder.class);

    public static String getString(FileItem item){
        String value = null;
        try {
            value = item.getString(StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException uee){
            logger.error(uee.getMessage());
        }
        return value;
    }
}
