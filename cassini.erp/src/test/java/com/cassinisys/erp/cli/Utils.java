package com.cassinisys.erp.cli;

import com.cassinisys.erp.config.api.APIKeyGenerator;
import com.cassinisys.erp.util.RandomString;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by reddy on 8/26/15.
 */
public class Utils {

    @Test
    public void testString() {
        String s = "orderedDate:asc";
        int index = s.indexOf(':');
        String prop = s.substring(0, index);
        String order = s.substring(index+1);
        System.out.println(prop + " " + order);
    }

    @Test
    public void testApiKeyGenerator() throws Exception {
        for(int i=0; i<5; i++) {
            System.out.println(APIKeyGenerator.get().generate());
        }
    }

    @Test
    public void testRandomString() throws Exception {
        for(int i=0; i<100; i++) {
            String s = RandomString.get().getSpecialChars(8);
            System.out.println(s);
        }
    }

    @Test
    public void testPassword() throws Exception {
        String pwd = "o6tiu9lz";
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
        System.out.println("Hashed: " + hashed);

        String newPwd = "o6tiu9lz";
        if(BCrypt.checkpw(newPwd, hashed)) {
            System.out.println("Password is correct");
        }
        else {
            System.out.println("Password is incorrect");
        }

    }
}
