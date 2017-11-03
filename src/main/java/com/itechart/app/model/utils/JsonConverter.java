package com.itechart.app.model.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonConverter {

    public String toJson(List<String> nationalities){
        if(nationalities == null || nationalities.size() == 0){
            return "";
        }
        Type listType = new TypeToken<List<String>>() {}.getType();

        Gson gson = new Gson();
        String json = gson.toJson(nationalities, listType);
        return json;
    }
}
