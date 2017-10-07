package com.itechart.app.model.email;

import com.itechart.app.model.enums.EmailTemplateEnum;
import com.itechart.app.model.exceptions.EmailTemplateEngineException;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class EmailTemplateEngine {

    private final Logger logger = LoggerFactory.getLogger(EmailTemplateEngine.class);

    public String generateTemplate(EmailTemplateEnum emailTemplate,
                                  Map<String, String> emailParamsMap)
            throws EmailTemplateEngineException {

        TemplateConfiguration cfg = TemplateConfiguration.getInstance();
        Writer out;
        try {
            Template template = cfg.getTemplate(emailTemplate.getEmailTemplateFileName());
            out = new StringWriter();
            template.process(emailParamsMap, out);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new EmailTemplateEngineException("Error during generating email template");
        }
        return out.toString();
    }
}
