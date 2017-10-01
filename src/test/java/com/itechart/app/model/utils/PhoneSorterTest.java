package com.itechart.app.model.utils;

import com.itechart.app.model.entities.Phone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PhoneSorterTest {

    private List<Phone> phoneList;

    {
        final int phoneListLength = 10;
        phoneList = new ArrayList<>(phoneListLength);
        for (int i = 0; i < phoneListLength; i++) {
            if (i % 2 == 0) {
                phoneList.add(new Phone());
            } else {
                Phone phone = new Phone();
                phone.setPhoneId(1);
                phoneList.add(phone);
            }
        }
    }

    private PhoneSorter phoneSorter;

    @Before
    public void initAttachmentSorter(){
        phoneSorter = new PhoneSorter(phoneList);
    }

    @Test
    public void getOldAttachmentsShouldReturnExpectedAttachmentCount() throws Exception {
        final int expectedAttachmentCount = 5;
        final String errorMessage =
                "Expected and actual count of returned old attachment objects are different";
        Assert.assertEquals(errorMessage, expectedAttachmentCount,
                phoneSorter.getOldPhones().size());
    }

    @Test
    public void  getNewAttachmentsShouldReturnExpectedAttachmentCount() throws Exception {
        final int expectedAttachmentCount = 5;
        final String errorMessage =
                "Expected and actual count of returned new attachment objects are different";
        Assert.assertEquals(errorMessage, expectedAttachmentCount,
                phoneSorter.getNewPhones().size());
    }
}