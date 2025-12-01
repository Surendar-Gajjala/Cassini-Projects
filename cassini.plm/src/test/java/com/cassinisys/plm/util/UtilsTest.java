package com.cassinisys.plm.util;

import com.cassinisys.platform.util.RandomString;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

public class UtilsTest {

    @Test
    public void generatePassword() throws Exception {
        String password = "cassini";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println(password + " : " + hash);
    }

}
