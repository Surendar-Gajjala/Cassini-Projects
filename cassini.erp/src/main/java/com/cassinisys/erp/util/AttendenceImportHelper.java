package com.cassinisys.erp.util;

import com.cassinisys.erp.model.hrm.AttendanceStatus;
import com.cassinisys.erp.model.hrm.ERPEmployeeAttendance;
import jxl.*;
import jxl.read.biff.BiffException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lakshmi on 1/22/2016.
 */
@Component
public class AttendenceImportHelper {


    public List<ERPEmployeeAttendance> attnds = new ArrayList<ERPEmployeeAttendance>();

    public List<String> dates= new ArrayList<String>();

    public List<ERPEmployeeAttendance> empMonthAttendsLst = null;

    public static final String DATEFORMAT = "dd/MM/yyyy, HH:mm:ss";
    public static final String DATEFORMATT = "dd-MMM-yyyy";
    public static final String DATEFORMAT_ONLY = "dd/MM/yyyy";


    public List<ERPEmployeeAttendance> init(InputStream inputStream) {
        FileInputStream fs = null;
        try {

            contentReading(inputStream);

            if (attnds.size() > 0) {
                System.out.println("Size of Objs is :" + attnds.size());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attnds;
    }

    // Returns the Headings used inside the excel sheet
    public void getHeadingFromXlsFile(Sheet sheet) {
        int columnCount = sheet.getColumns();
        for (int i = 0; i < columnCount; i++) {
            System.out.println(sheet.getCell(i, 0).getContents());
        }
    }

    public void contentReading(InputStream fileInputStream) {
        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        Cell rowData[] = null;
        int rowCount = '0';
        int columnCount = '0';
        DateCell dc = null;
        int totalSheet = 0;

        String empId = "";
        String inTime = "";
        String outTime = "";

        boolean tsinTime = false;
        boolean tsoutTime = false;

        ERPEmployeeAttendance attndDTO = null;

        try {
            ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = Workbook.getWorkbook(fileInputStream, ws);

            totalSheet = workbook.getNumberOfSheets();
            if (totalSheet > 0) {
                System.out.println("Total Sheet Found:" + totalSheet);
                for (int j = 0; j < totalSheet; j++) {
                    System.out.println("Sheet Name:"
                            + workbook.getSheet(j).getName());
                }
            }
            s = workbook.getSheet(0);
            rowCount = s.getRows();
            columnCount =s.getColumns();

            String month ="";
            String year ="";
            String startDay="";
            String endDay="";
            String beginDate="";
            String endDate="";
            boolean startDateFinish=false;
            boolean endDateFinish=false;

            // Get Individual Row
            rowData = s.getRow(1);
            for(int m=0;m<rowData.length;m++){
                if (rowData[m].getContents().length() != 0) {

                    if (rowData[m].getContents().length()>5 && !startDateFinish) {

                        String startDateVal=rowData[m].getContents();
                        String[] startDatee=startDateVal.split(" ");
                        month=startDatee[0];
                        startDay=startDatee[1];
                        year=startDatee[2];
                        beginDate=startDay+"/"+month+"/"+year;
                        System.out.println("Start Date is :"+beginDate);
                        startDateFinish=true;
                        continue;

                    }

                    if (rowData[m].getContents().length()>5 && !endDateFinish) {

                        String endDateVal=rowData[m].getContents();
                        String[] endDatee=endDateVal.split(" ");

                        endDay=endDatee[1];
                        endDate=endDay+"/"+month+"/"+year;
                        System.out.println("End Date is:"+endDate);

                        endDateFinish=true;
                    }

                    if(endDateFinish && startDateFinish)
                        break;
                }


            }
            rowCount=0;
            columnCount=0;
            rowData=null;
            int daysCount=0;
            s = workbook.getSheet(0);
            rowCount = s.getRows();
            columnCount =s.getColumns();

            for (int ii = 0; ii < rowCount; ii++) {
                // Get Individual Row
                rowData = s.getRow(ii);
                if (rowData[0].getContents().length() != 0
                        && !rowData[0].getContents().startsWith(
                        "Monthly Status Report")
                        && !rowData[0].getContents().startsWith("Company")
                        && !rowData[0].getContents().startsWith(
                        "Department") && rowData[0].getContents().startsWith(
                        "Days")) {

                    if (rowData[0].getContents().equalsIgnoreCase(
                            "Days")) {
                        for (int j = 1; j < columnCount; j++) {
                            if(rowData[j].getContents().length()>0){
                                String dd[]=rowData[j].getContents().split(" ");
                                dates.add(dd[0]);
                                daysCount++;
                            }else{
                                dates.add("--");
                            }
                        }
                        break;
                    }


                }
            }
            System.out.println(" Total Number Of Days in Report is:"+daysCount);

            for (int ts = 0; ts < totalSheet; ts++) {
                // Getting Default Sheet i.e. 0
                s = workbook.getSheet(ts);
                System.out.println("Total Rows inside Sheet:" + s.getRows());
                rowCount = s.getRows();

                // Total Total No Of Columns in Sheet
                System.out.println("Total Column inside Sheet:"
                        + s.getColumns());
                columnCount = s.getColumns();

                // Reading Individual Row Content
                for (int i = 0; i < rowCount; i++) {
                    // Get Individual Row
                    rowData = s.getRow(i);
                    if (rowData[0].getContents().length() != 0
                            && rowData[0].getContents().startsWith(
                            "Emp. Code :") || rowData[0].getContents().startsWith("InTime")
                            || rowData[0].getContents().startsWith("OutTime")
                            ) {


                        if(rowData[0].getContents().startsWith("Emp. Code :")){
                            for(int e=1;e<dates.size();e++){

                                if(rowData[e].getContents().length()>0) {
                                    empId = rowData[e].getContents();
                                    break;
                                }

                            }
                        }

                        if (rowData[0].getContents().equalsIgnoreCase("InTime")) {

                            empMonthAttendsLst= new ArrayList<ERPEmployeeAttendance>();
                            for(int in=1;in<=dates.size();in++){

                                int val = in - 1;
                                String isVal=dates.get(val);
                                if(!(isVal!=null && isVal.equalsIgnoreCase("--"))) {

                                    inTime = rowData[in].getContents();

                                    attndDTO = new ERPEmployeeAttendance();
                                    attndDTO.setEmpNumber(empId);

                                    beginDate = dates.get(val) + "-" + month + "-" + year;
                                    if (inTime != null && inTime.length() > 0) {
                                        attndDTO.setInTime(getDate(beginDate, inTime));
                                        attndDTO.setStatus(AttendanceStatus.P);
                                    }
                                    attndDTO.setDate(getOnlyDate(beginDate));

                                    empMonthAttendsLst.add(attndDTO);
                                }
                            }
                            tsinTime = true;

                        }

                        if (rowData[0].getContents().equalsIgnoreCase("OutTime")) {

                            int oo=0;
                            for(int out=1;out<=dates.size();out++){

                                int val = out - 1;
                                String isVal=dates.get(val);
                                if(!(isVal!=null && isVal.equalsIgnoreCase("--"))) {

                                    outTime = rowData[out].getContents();

                                    attndDTO = empMonthAttendsLst.get(oo);
                                    oo++;
                                    attndDTO.setEmpNumber(empId);

                                    beginDate = dates.get(val) + "-" + month + "-" + year;
                                    if (outTime != null && outTime.length() > 0) {
                                        attndDTO.setOutTime(getDate(beginDate, outTime));
                                    }

                                }

                            }
                            tsoutTime = true;


                        }

                        if (tsinTime && tsoutTime) {


                            for(int a=0;a<empMonthAttendsLst.size();a++){
                                attnds.add(empMonthAttendsLst.get(a));
                            }
                            empMonthAttendsLst=null;
                            tsinTime = false;
                            tsoutTime = false;
                        }
                    }




                }
            }
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    public static Date getDate(String strDate, String time) {
        Date date = null;

        int hrs = 0;
        int min = 0;
        if (time != null && time.length() > 0) {
            String[] times = time.split(":");
            hrs = Integer.parseInt(times[0]);
            min = Integer.parseInt(times[1]);
        }

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat(DATEFORMATT).parse(strDate));

            int mnth=cal.get(Calendar.MONTH);
            mnth=mnth+1;

            String fullDate=cal.get(Calendar.DAY_OF_MONTH)+"/"+mnth+"/"+cal.get(Calendar.YEAR);


            Calendar calOnly=Calendar.getInstance();
            calOnly.setTime(new SimpleDateFormat(DATEFORMAT_ONLY).parse(fullDate));
            calOnly.set(Calendar.HOUR, hrs);
            calOnly.set(Calendar.MINUTE, min);

            date=calOnly.getTime();


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

    public static Date getOnlyDate(String strDate) {
        Date date = null;


        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat(DATEFORMATT).parse(strDate));
            date = cal.getTime();

            int mnth=cal.get(Calendar.MONTH);
            mnth=mnth+1;

            String dateOnly=cal.get(Calendar.DAY_OF_MONTH)+"/"+mnth+"/"+cal.get(Calendar.YEAR);

            Calendar calOnly=Calendar.getInstance();
            calOnly.setTime(new SimpleDateFormat(DATEFORMAT_ONLY).parse(dateOnly));
            date=calOnly.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

}