package com.cassinisys.erp.config.api;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 9/2/15.
 */
public final class APIKeyGenerator {
    private static APIKeyGenerator INSTANCE = null;
    private KeyGenerator keyGen = null;
    private List<String> cache = new ArrayList<>();

    private APIKeyGenerator() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
    }

    public static APIKeyGenerator get() {
        if(INSTANCE == null) {
            INSTANCE = new APIKeyGenerator();
        }

        return INSTANCE;
    }

    public String generate() {
        SecretKey secretKey = keyGen.generateKey();
        byte[] encoded = secretKey.getEncoded();

        String key = DatatypeConverter.printHexBinary(encoded).toLowerCase();
        cache.add(key);

        return key;
    }

    public boolean validateKey(String key) {
        return cache.contains(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
