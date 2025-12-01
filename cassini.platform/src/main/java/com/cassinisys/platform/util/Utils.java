package com.cassinisys.platform.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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

    public static String OTP() {
        return Utils.OTP(6);
    }

    public static String OTP(int len) {
        // Using numeric values
        String numbers = "0123456789";

        // Using random method
        SecureRandom random = new SecureRandom();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    public static Object cloneObject(Object obj, Class clz) {
        try {
            String json = new ObjectMapper().writeValueAsString(obj);
            obj = new ObjectMapper().readValue(json, clz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static void removeDirIfExist(File fDir){
        if (fDir.exists()) {
            try {
                System.gc();
                Thread.sleep(1000L);
                FileUtils.deleteQuietly(fDir);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyDataUsingStream(String oldFileDir, FileInputStream instream, FileOutputStream outstream, String dir){
        try {
            instream = null;
            outstream = null;
            File infile = new File(oldFileDir);
            File outfile = new File(dir);
            instream = new FileInputStream(infile);
            outstream = new FileOutputStream(outfile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = instream.read(buffer)) > 0) {
                outstream.write(buffer, 0, length);
            }
            instream.close();
            outstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
