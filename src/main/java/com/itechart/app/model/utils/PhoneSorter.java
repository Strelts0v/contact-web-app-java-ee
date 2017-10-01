package com.itechart.app.model.utils;

import com.itechart.app.model.entities.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneSorter {

    private List<Phone> oldPhones;
    private List<Phone> newPhones;

    public PhoneSorter(List<Phone> phoneList){
        oldPhones = new ArrayList<>();
        newPhones = new ArrayList<>();
        for(Phone phone : phoneList){
            if(phone.getPhoneId() == 0){
                newPhones.add(phone);
            } else {
                oldPhones.add(phone);
            }
        }
    }

    public List<Phone> getOldPhones(){
        return oldPhones;
    }

    public List<Phone> getNewPhones(){
        return newPhones;
    }
}
