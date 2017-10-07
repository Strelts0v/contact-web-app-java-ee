package com.itechart.app.model.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class TemplateConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(TemplateConfiguration.class);

    private static volatile TemplateConfiguration INSTANCE;

    private Configuration configuration;

    private TemplateConfiguration() throws IOException{
        configuration = new Configuration(Configuration.VERSION_2_3_23);

        try {
            URL templateFolderUri = getClass().getResource("/email/templates");
            configuration.setDirectoryForTemplateLoading(new File(new URI(templateFolderUri.toString())));
        } catch (URISyntaxException use){
            logger.error(use.getMessage());
            throw new IOException(use);
        }

        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        configuration.setLogTemplateExceptions(false);
    }

    public static TemplateConfiguration getInstance() {
        if (INSTANCE == null) {
            synchronized (TemplateConfiguration.class){
                if (INSTANCE == null) {
                    try {
                        INSTANCE = new TemplateConfiguration();
                    } catch (IOException ioe){
                        logger.error(ioe.getMessage());
                    }
                }
            }
        }
        return INSTANCE;
    }

    public Template getTemplate(String templateFileName) throws IOException{
        return configuration.getTemplate(templateFileName);
    }
}