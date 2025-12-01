package com.cassinisys.plm.generate;

import com.cassinisys.plm.generate.pojo.BaseClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Nageshreddy on 25-11-2021.
 */
public class GenerateUtils {

    public static int assembleId = 1;
    public static int partId = 1;
    public static int specId = 1;
    public static int substanceId = 1;
    public static int plantId = 1;
    public static int assyId = 1;
    public static int assetId = 1;
    public static int wcId = 1;
    public static int mcId = 1;
    public static int eqId = 1;
    public static int insId = 1;
    public static int manId = 1;
    public static int tlId = 1;
    public static int mtId = 1;
    public static int jfId = 1;
    public static int shiftId = 1;
    public static int opId = 1;
    public static int mtrId = 1;
    public static int sppId = 1;
    public static int meterId = 1;

    public static Object[] getColumns(String val) {
        try {
            JSONParser parser = new JSONParser();
            File resource = new ClassPathResource("importColumnNames.json").getFile();
            Object obj = parser.parse(new FileReader(resource));
            JSONObject jsonObject = (JSONObject) obj;
            Map<String, List<String>> stringListMap = new ObjectMapper().readValue(jsonObject.toString(), Map.class);
            List<String> strings = stringListMap.get(val);
            return strings.toArray();
        } catch (Exception e) {

        }
        return null;
    }

    public static String getRandomPhone() {
        return String.format("%03d%03d%04d",
                (int) Math.floor(999 * Math.random()),
                (int) Math.floor(999 * Math.random()),
                (int) Math.floor(9999 * Math.random()));
    }

    public static String generateRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "
                + "0123456789 "
                + "-_/ "
                + "abcdefghijklmnopqrstuvxyz ";
        StringBuilder s = new StringBuilder(n);
        int y;
        for (y = 0; y < n; y++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            s.append(AlphaNumericString
                    .charAt(index));
        }
        return s.toString();
    }

    public static String generateRandomTypeString(String str, int n) {
        String AlphaNumericString = "AB"
                + "12";
        StringBuilder s = new StringBuilder(n);
        int y;
        for (y = 0; y < n; y++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            s.append(AlphaNumericString
                    .charAt(index));
        }
        return str + "-" + s.toString();
    }

    public static String generateRandomNameString(String str, int n) {
        String AlphaNumericString = "AB"
                + "12";
        StringBuilder s = new StringBuilder(n);
        int y;
        for (y = 0; y < n; y++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            s.append(AlphaNumericString
                    .charAt(index));
        }
        return str + s.toString();
    }

    public static void writeInExcel(List<BaseClass> excelObjects, String excelFile, Object[] columns, Class class1) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet
                    = workbook.createSheet("Sheet-1");
            XSSFRow row;
            Map<Integer, Object[]> stringMap
                    = new TreeMap<Integer, Object[]>();
            Integer i = 1;
            stringMap.put(i, columns);
            i = 2;

            Field[] fields = class1.getDeclaredFields();
            for (int k = 0; k < fields.length; k++) fields[k].setAccessible(true);

            for (BaseClass eo : excelObjects) {
                List<Object> objs = new ArrayList<>();
                for (int k = 0; k < fields.length; k++) objs.add(fields[k].get(eo));
                stringMap.put(i, objs.toArray());
                i = i + 1;
            }

            Set<Integer> keyid = stringMap.keySet();
            int rowid = 0;

            // writing the data into the sheets...
            for (Integer key : keyid) {
                row = spreadsheet.createRow(rowid++);
                Object[] objectArr = stringMap.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue((String) obj);
                }
            }

            FileOutputStream out = new FileOutputStream(
                    new File(excelFile));

            workbook.write(out);
            out.close();
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public static int getRandomIntegerBetweenRange(double min, double max) {
        int x = (int) Math.round((Math.random() * ((max - min) + 1)) + min);
        return x;
    }

    public static String getRandomIntegerBetweenRangeToString(double min, double max) {
        int x = (int) Math.round((Math.random() * ((max - min) + 1)) + min);
        return convertString(x);
    }


    public static String getRandomIntegerBetweenRangeInString(double min, double max) {
        int x = (int) Math.round((Math.random() * ((max - min) + 1)) + min);
        Integer val = (Integer) x;
        return val.toString();
    }

    public static String getItemNumber(int qty) {
        String prefix = "";
        String next = "";
        String padwith = "0";
        int numbers = 5;

        int n;
        if (qty == 1) {
            n = assembleId;
            prefix = "ASSY-";
        } else {
            n = partId;
            prefix = "PART-";
        }
        if (prefix != null && !prefix.trim().isEmpty()) {
            next += prefix;
        }


        String s = "" + n;
        if ((numbers - s.length()) >= 0) {
            next += StringUtils.repeat(padwith, (numbers - s.length()));
            next += n;
        }

        if (qty == 1) assembleId++;
        else partId++;

        return next;
    }

    public static String getRandomPincode() {
        return String.format("%03d%03d",
                (int) Math.floor(999 * Math.random()),
                (int) Math.floor(999 * Math.random()));
    }


    public static String generateSpecNumber(String prex) {
        String prefix = prex;
        String next = "";
        String padwith = "0";
        int numbers = 5;
        int n = 0;
        if (prex.equals("ST-")) {
            n = substanceId;
//            n = getRandomIntegerBetweenRange(1 , 24000);
        } else if (prex.equals("SPEC-")) {
            n = getRandomIntegerBetweenRange(1, 50);
        } else if (prex.equals("ASSY-")) {
            n = assyId;
        } else if (prex.equals("PT-")) {
            n = plantId;
        } else if (prex.equals("WC-")) {
            n = wcId;
        } else if (prex.equals("MC-")) {
            n = mcId;
        } else if (prex.equals("EQ-")) {
            n = eqId;
        } else if (prex.equals("INS-")) {
            n = insId;
        } else if (prex.equals("MAN-")) {
            n = manId;
        } else if (prex.equals("TL-")) {
            n = tlId;
        } else if (prex.equals("MT-")) {
            n = mtId;
        } else if (prex.equals("JF-")) {
            n = jfId;
        } else if (prex.equals("SHFT-")) {
            n = shiftId;
        } else if (prex.equals("OP-")) {
            n = opId;
        } else if (prex.equals("MTR-")) {
            n = mtrId;
        } else if (prex.equals("SPP-")) {
            n = sppId;
        } else if (prex.equals("Meter")) {
            n = meterId;
        } else {
            n = assetId;
        }
        if (prefix != null && !prefix.trim().isEmpty()) {
            next += prefix;
        }
        String s = "" + n;
        if ((numbers - s.length()) >= 0) {
            next += StringUtils.repeat(padwith, (numbers - s.length()));
            next += n;
        }
        if (prex.equals("ST-")) {
            substanceId++;
        } else if (prex.equals("SPEC-")) {
            specId++;
        } else if (prex.equals("ASSY-")) {
            assyId++;
        } else if (prex.equals("PT-")) {
            plantId++;
        } else if (prex.equals("WC-")) {
            wcId++;
        } else if (prex.equals("MC-")) {
            mcId++;
        } else if (prex.equals("EQ-")) {
            eqId++;
        } else if (prex.equals("INS-")) {
            insId++;
        } else if (prex.equals("MAN-")) {
            manId++;
        } else if (prex.equals("TL-")) {
            tlId++;
        } else if (prex.equals("MT-")) {
            mtId++;
        } else if (prex.equals("JF-")) {
            jfId++;
        } else if (prex.equals("SHFT-")) {
            shiftId++;
        } else if (prex.equals("OP-")) {
            opId++;
        } else if (prex.equals("MTR-")) {
            mtrId++;
        } else if (prex.equals("SPP-")) {
            sppId++;
        } else if (prex.equals("Meter")) {
            meterId++;
        } else {
            assetId++;
        }
        return next;
    }

    public static String getItemNumber(int val, int i, int level) {
        String prefix = "";
        String next = "";
        String padwith = "0";
        int numbers = 5;

        int n;
        if (i < level) {
            n = assembleId;
            prefix = "ASSY-";
        } else if (val == 1) {
            n = assembleId;
            if (val < 1561) prefix = "ASSY-";
            else prefix = "PART-";
        } else {
            n = partId;
            if (val < 23439) prefix = "PART-";
            else prefix = "ASSY-";
        }
        if (prefix != null && !prefix.trim().isEmpty()) {
            next += prefix;
        }

        String s = "" + n;
        if ((numbers - s.length()) >= 0) {
            next += StringUtils.repeat(padwith, (numbers - s.length()));
            next += n;
        }

        if (i < level || val == 1) assembleId++;
        else partId++;

        return next;
    }

    public static String getType(int val, int i, int level) {
        String type = "";

        int n;
        if (i < level) {
            type = "Assembly";
        } else if (val == 1) {
            if (val < 1561) type = "Assembly";
            else type = "Part";
        } else {
            if (val < 23439) type = "Part";
            else type = "Assembly";
        }
        return type;
    }

    public static String getTypePath(List<String> types) {
        Integer val = getRandomIntegerBetweenRange(0, 2);
        return types.get(val);
    }

    public static String getTypePath(int number) {
        String[] types = new String[]{"Preffered", "ALTERNATE", "Preffered"};
        Integer val = getRandomIntegerBetweenRange(0, number);
        return types[val];
    }

    public static String getJigOrFixture(int number) {
        List<String> strings = new ArrayList<>();
        strings.add("Jig");
        strings.add("Fixture");
        Integer val = getRandomIntegerBetweenRange(0, number - 1);
        return strings.get(val);
    }

    public static String getMeterValue() {
        List<String> strings = new ArrayList<>();
        strings.add("Yes");
        strings.add("No");
        Integer val = getRandomIntegerBetweenRange(0, 0);
        return strings.get(val);
    }

        public static String generateMROMeter(String str){
        String val = getRandomIntegerBetweenRangeInString(1, 50);
        return str+val;
    }

    public static String convertString(int i) {
        Integer val = (Integer) i;
        return val.toString();
    }

    public static String generateCASString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "
                + "0123456789";
        StringBuilder s = new StringBuilder(n);
        int y;
        for (y = 0; y < n; y++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            s.append(AlphaNumericString
                    .charAt(index));
        }
        return s.toString();
    }
}
