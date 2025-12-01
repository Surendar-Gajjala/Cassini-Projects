package com.cassinisys.platform.util;

import com.cassinisys.platform.exceptions.CassiniException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;

/**
 * Created by Nageshreddy on 14-03-2019.
 */
@Component
public class EncryptDeserializer extends JsonDeserializer<String> {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    @Override
    public String deserialize(JsonParser jsonparser,
                              DeserializationContext deserializationcontext) throws IOException {
        String str = jsonparser.getText();
        return encrypt(str);
    }

    public String encrypt(String value) {
        try {
            Key key = new SecretKeySpec(EncryptDeserializer.KEY.getBytes(), EncryptDeserializer.ALGORITHM);
            Cipher cipher = Cipher.getInstance(EncryptDeserializer.ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
            String encryptedValue64 = new BASE64Encoder().encode(encryptedByteValue);
            return encryptedValue64;
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }
    }

}