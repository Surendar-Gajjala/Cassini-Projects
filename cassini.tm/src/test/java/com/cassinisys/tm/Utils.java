package com.cassinisys.tm;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by reddy on 9/15/16.
 */
public class Utils {

    @Test
    public void testPassword() throws Exception {
        String hashed = BCrypt.hashpw("bzarri", BCrypt.gensalt());
        System.out.println(hashed);
    }

}
