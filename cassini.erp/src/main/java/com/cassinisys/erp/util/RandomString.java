package com.cassinisys.erp.util;

/**
 * Created by reddy on 9/7/15.
 */
public class RandomString {
    private static RandomString INSTANCE = new RandomString();

    private static final String ALPHA =
            "abcdefghijklmnopqrstuvwxyz";

    private static final String ALPHA_NUM =
            "0123456789abcdefghijklmnopqrstuvwxyz";

    private static final String ALPHA_NUM_SPECIALCHARS =
            "0123456789abcdefghijklmnopqrstuvwxyz%$#@&*";

    private final int DEFAULT_LEN = 10;

    private RandomString() {

    }

    public static RandomString get() {
        return INSTANCE;
    }

    public String getAlpha() {
        return getAlpha(DEFAULT_LEN);
    }

    public String getAlphaNumeric() {
        return getAlphaNumeric(DEFAULT_LEN);
    }

    public String getAlpha(int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i=0;  i<len;  i++) {
            int ndx = (int)(Math.random()*ALPHA.length());
            sb.append(ALPHA.charAt(ndx));
        }
        return sb.toString();
    }

    public String getAlphaNumeric(int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i=0;  i<len;  i++) {
            int ndx = (int)(Math.random()*ALPHA_NUM.length());
            sb.append(ALPHA_NUM.charAt(ndx));
        }
        return sb.toString();
    }

    public String getSpecialChars(int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i=0;  i<len;  i++) {
            int ndx = (int)(Math.random()*ALPHA_NUM_SPECIALCHARS.length());
            sb.append(ALPHA_NUM_SPECIALCHARS.charAt(ndx));
        }
        return sb.toString();
    }
}
