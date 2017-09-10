package com.itechart.app.model.cryptography;

import org.junit.Assert;
import org.junit.Test;

public class CryptographerXorTest {

    private final Cryptographer cryptographer = new CryptographerXor();

    @Test
    public void encryptAndDecryptWithShortStrTest() throws Exception {
        final String testStr = "44447777";
        final String encryptStr = cryptographer.encrypt(testStr);
        final String errorMessage = "Values of original and decrypt strings are different";

        Assert.assertEquals(errorMessage, testStr, cryptographer.decrypt(encryptStr));
    }

    @Test
    public void encryptAndDecryptWithEmptyStrTest() throws Exception {
        final String testStr = "";
        final String encryptStr = cryptographer.encrypt(testStr);
        final String errorMessage = "Values of original and decrypt strings are different";

        Assert.assertEquals(errorMessage, testStr, cryptographer.decrypt(encryptStr));
    }

    @Test
    public void encryptAndDecryptWithNullStrTest() throws Exception {
        final String testStr = null;
        final String encryptStr = cryptographer.encrypt(testStr);
        final String errorMessage = "Values of original and decrypt strings are different";

        Assert.assertEquals(errorMessage, testStr, cryptographer.decrypt(encryptStr));
    }

    @Test
    public void encryptAndDecryptWithComplicatedStrTest() throws Exception {
        final String testStr = "hello hello hello hello hello 4444 7777!!! Test Test";
        final String encryptStr = cryptographer.encrypt(testStr);
        final String errorMessage = "Values of original and decrypt strings are different";

        Assert.assertEquals(errorMessage, testStr, cryptographer.decrypt(encryptStr));
    }
}