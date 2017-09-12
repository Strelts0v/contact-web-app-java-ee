package com.itechart.app.model.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.MissingResourceException;

public class PageConfigurationManagerTest {

    @Test
    public void getPageNameWithValidKeyTest() throws Exception {
        final String pageKey = "path.page.jsp.index";
        final String expectedPagePath = "/views/jsp/index";
        final String errorMessage = "Expected equal expected and actual page path";

        final String resultPagePath = PageConfigurationManager.getPageName(pageKey);
        Assert.assertEquals(errorMessage, expectedPagePath, resultPagePath);
    }

    @Test(expected = MissingResourceException.class)
    public void getPageNameWithInvalidKeyTest() throws Exception {
        final String pageKey = "invalid";
        PageConfigurationManager.getPageName(pageKey);
    }
}