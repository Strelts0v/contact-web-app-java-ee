package com.itechart.app.model.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itechart.app.model.entities.Phone;
import org.apache.commons.fileupload.FileItem;

import java.lang.reflect.Type;
import java.util.List;

public class PhoneParser {

    public static List<Phone> parsePhones(FileItem item, int contactId) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Phone>>(){}.getType();

        // here method getString() from FileItem should return JSON string
        List<Phone> phones = gson.fromJson(item.getString(), type);

        // set contact id to each phone object
        for(Phone phone : phones){
            phone.setContactId(contactId);
        }

        return phones;
    }

    public static List<Phone> parsePhones(FileItem item) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Phone>>(){}.getType();

        // here method getString() from FileItem should return JSON string
        List<Phone> phones = gson.fromJson(item.getString(), type);

        return phones;
    }
}
