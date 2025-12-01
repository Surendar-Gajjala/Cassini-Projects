package com.cassinisys.erp.util;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by reddy on 15/03/16.
 */
public class PhoneNumbersTest {

    @Test
    public void testPhoneNumbers() throws Exception {
        String str = "This is a 9701030235,9701028175";
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
