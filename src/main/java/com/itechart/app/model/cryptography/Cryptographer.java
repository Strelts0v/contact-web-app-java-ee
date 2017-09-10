package com.itechart.app.model.cryptography;

/**
 * specifies contract for cryptography algorithm
 */
public interface Cryptographer {

    /**
     * encrypts input string
     * @param str - input string
     * @return encrypt string value
     */
    String encrypt(String str);

    /**
     * decrypts input string
     * @param str - input string
     * @return decrypt string value
     */
    String decrypt(String str);
}