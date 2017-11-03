package com.itechart.app.model.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonConverterTest {

    private JsonConverter converter = new JsonConverter();

    @Test
    public void toJsonShouldReturnCorrespondString() throws Exception {
        List<String> nationalities = new ArrayList<>(2);
        nationalities.add("Belarus");
        nationalities.add("Russian");

        String resultJsonData = converter.toJson(nationalities);
        String expectedJsonData = "[\"Belarus\",\"Russian\"]";
        String errorMessage = "Expected and result json nationalities data are different";

        Assert.assertEquals(errorMessage, expectedJsonData, resultJsonData);
    }

    @Test
    public void EmptyListToJsonShouldReturnEmptyString() throws Exception {
        List<String> nationalities = new ArrayList<>();

        String resultJsonData = converter.toJson(nationalities);
        String expectedJsonData = "";
        String errorMessage = "Expected and result json nationalities data are different";

        Assert.assertEquals(errorMessage, expectedJsonData, resultJsonData);
    }

    @Test
    public void NullListToJsonShouldReturnEmptyString() throws Exception {
        List<String> nationalities = null;

        String resultJsonData = converter.toJson(nationalities);
        String expectedJsonData = "";
        String errorMessage = "Expected and result json nationalities data are different";

        Assert.assertEquals(errorMessage, expectedJsonData, resultJsonData);
    }
}