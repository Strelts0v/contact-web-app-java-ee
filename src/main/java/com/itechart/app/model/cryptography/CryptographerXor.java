package com.itechart.app.model.cryptography;

/**
 * implements Cryptographer interface according XOR crypt algorithm
 */
public class CryptographerXor implements Cryptographer {

    /** property - crypto key */
    private static final String CRYPTO_KEY = "qwerty";

    @Override
    public String encrypt(String str) {
        if(str != null) {
            byte[] key = CRYPTO_KEY.getBytes();
            byte[] text = str.getBytes();
            byte[] result = new byte[str.length()];
            for (int i = 0; i < text.length; i++) {
                result[i] = (byte) (text[i] ^ key[i % key.length]);
            }
            return new String(result);
        }
        return null;
    }

    @Override
    public String decrypt(String str) {
        return encrypt(str);
    }
}
