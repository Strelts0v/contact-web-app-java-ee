package com.itechart.app.controller.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestRequest {

    private final Logger logger = LoggerFactory.getLogger(RestRequest.class);

    private String actionFromRestUrl;

    private Integer resourceId;

    /** list of all patterns according REST api */
    static private List<Pattern> patterns;

    // initialize all patterns according REST API
    static {
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("/create_contact"));
        patterns.add(Pattern.compile("/delete_contact"));
        patterns.add(Pattern.compile("/get_contact/([0-9]*)"));
        patterns.add(Pattern.compile("/get_contacts"));
        patterns.add(Pattern.compile("/search_contact"));
        patterns.add(Pattern.compile("/send_email_to_contacts"));
        patterns.add(Pattern.compile("/update_contact"));
        patterns.add(Pattern.compile("/find_nationalities"));
    }


    public RestRequest(final String pathInfo) throws ServletException {
        Matcher matcher;
        for (Pattern pattern: patterns) {
            matcher = pattern.matcher(pathInfo);
            if(matcher.find()){
                // save as action everything expect / and id if it exist
                actionFromRestUrl = matcher.group(0).substring(1);

                // means that there is also id for resource
                if(actionFromRestUrl.contains("/")){
                    int indexOfSlash = actionFromRestUrl.indexOf("/");
                    resourceId = Integer.parseInt(actionFromRestUrl.substring(indexOfSlash + 1));
                    actionFromRestUrl = actionFromRestUrl.substring(0, indexOfSlash);
                }
                // after finding correspond url pattern RestRequest object is ready to use
                return;
            }
        }
        logger.info("Initializing of RestRequest was successful");
        throw new ServletException("Invalid URI");
    }

    public boolean hasResourceId(){
        return resourceId == null ? false : true;
    }

    public String getActionFromRestUrl() {
        return actionFromRestUrl;
    }

    public Integer getResourceId() {
        return resourceId;
    }
}
