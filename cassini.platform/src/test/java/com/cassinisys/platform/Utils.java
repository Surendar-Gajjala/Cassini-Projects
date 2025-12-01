package com.cassinisys.platform;

import com.cassinisys.platform.util.DecryptSerializer;
import com.cassinisys.platform.util.EncryptDeserializer;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by reddy on 9/15/16.
 */
public class Utils {

    EncryptDeserializer encryptDeserializer = new EncryptDeserializer();
    DecryptSerializer decryptSerializer = new DecryptSerializer();

    @Test
    public void testPassword() throws Exception {
        String hashed = BCrypt.hashpw("2ktc16a", BCrypt.gensalt());
        System.out.println(hashed);
    }

    @Test
    public void passwordTest() throws Exception {
        System.out.print(BCrypt.checkpw("3r9smmo", "$2a$10$vFpXrVAgat.7.JjuHFxwtekZ9aQkaLxNOmngzlj/Ly7mz7gSHowaO"));
    }

    @Test
    public void encryptTest() throws Exception {
        String val = encryptDeserializer.encrypt("3r9smmo");
        System.out.println(val);
    }

    @Test
    public void decryptTest() throws Exception{
        String val = decryptSerializer.decrypt("PdC0r5QTVvrbdJI5C187oA==");
        System.out.print(val);
    }
}
