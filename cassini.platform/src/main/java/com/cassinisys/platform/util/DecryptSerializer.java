package com.cassinisys.platform.util;

import com.cassinisys.platform.exceptions.CassiniException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;

/**
 * Created by Nageshreddy on 14-03-2019.
 */
@Component
public class DecryptSerializer extends JsonSerializer<String> {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider arg2) throws
            IOException {
        String str = decrypt(value);
        gen.writeString(str);
    }

    public String decrypt(String value) {
        try {
            Key key = new SecretKeySpec(DecryptSerializer.KEY.getBytes(), DecryptSerializer.ALGORITHM);
            Cipher cipher = Cipher.getInstance(DecryptSerializer.ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedValue64 = new BASE64Decoder().decodeBuffer(value);
            byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
            String decryptedValue = new String(decryptedByteValue, "utf-8");
            return decryptedValue;
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }
    }

}