package com.itechart.app.model.dao.utils;

public class SearchTemplateEngine {

    private SearchTemplateEngine(){}

    public static String generateSearchTemplate(String searchValue){
        String searchTemplate;
        if(searchValue == null) {
            searchTemplate = "%";
        }
        else if(searchValue.equals("")){
            searchTemplate = "%";
        } else {
            searchTemplate = "%" + searchValue + "%";
        }
        return searchTemplate;
    }
}
