package com.cassinisys.erp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by reddy on 10/10/15.
 */
public class Utils {
    public static <E> List<E> makeList(Iterable<E> iterable) {
        List<E> list = new ArrayList<E>();
        for (E item : iterable) {
            list.add(item);
        }
        return list;
    }

    public static List<String> extractPhoneNumbers(String text) {
        List<String> phoneNumbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            phoneNumbers.add(matcher.group());
        }

        return phoneNumbers;
    }
}
