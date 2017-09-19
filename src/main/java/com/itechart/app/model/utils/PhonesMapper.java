package com.itechart.app.model.utils;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.entities.Phone;

import java.util.List;

public class PhonesMapper {

    private static final String PHONES_PARAM = "phones";

    public static List<Phone> mapPhones(RequestContent requestContent){
        String[] phonesStr = requestContent.getParameterValues(PHONES_PARAM);
        return null;
    }
}
