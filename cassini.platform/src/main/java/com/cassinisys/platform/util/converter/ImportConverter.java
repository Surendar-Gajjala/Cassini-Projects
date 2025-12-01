package com.cassinisys.platform.util.converter;

import com.cassinisys.platform.exceptions.CassiniException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Nageshreddy on 02-11-2018.
 */

@Component
public class ImportConverter {

    @Autowired
    private MessageSource messageSource;

    @Transactional
    public File trimAndConvertMultipartFileToFile(MultipartFile file) {

        if (file.getOriginalFilename().trim().endsWith(".xlsx")) {
            DataFormatter df = new DataFormatter();
            try {
                File fileName = new File("temp.xlsx");
                fileName.setWritable(true);
                fileName.setReadable(true);
                fileName.setExecutable(true);
                FileOutputStream fos = new FileOutputStream(fileName);
                XSSFWorkbook newBook = new XSSFWorkbook();
                XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                int totalSheets = workbook.getNumberOfSheets();
                for (int k = 0; k < totalSheets; k++) {
                    Sheet worksheet = workbook.getSheetAt(k);
                    XSSFSheet sheet = newBook.createSheet(worksheet.getSheetName());
                    int rows = worksheet.getLastRowNum();
                    for (int i = 0; i <= rows; i++) {
                        Row row = worksheet.getRow(i);
                        if (row != null && !isRowEmpty(row)) {
                            Row cRow = sheet.createRow(i);
                            int cols = row.getLastCellNum();
                            for (int j = 0; j < cols; j++) {
                                Cell cCell = cRow.createCell(j);
                                String s = df.formatCellValue(row.getCell(j));
                                if (s != null) {
                                    s = org.springframework.util.StringUtils.trimWhitespace(s);
                                    cCell.setCellValue(s);
                                } else {
                                    cCell.setCellValue("");
                                }
                            }
                        }
                    }
                }
                newBook.write(fos);
                fos.flush();
                fos.close();
                return fileName;
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        } else if (file.getOriginalFilename().trim().endsWith(".xls")) {
            DataFormatter df = new DataFormatter();
            try {
                File fileName = new File("temp.xlsx");
                fileName.setWritable(true);
                fileName.setReadable(true);
                fileName.setExecutable(true);
                FileOutputStream fos = new FileOutputStream(fileName);
                XSSFWorkbook newBook = new XSSFWorkbook();
                HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
                int totalSheets = workbook.getNumberOfSheets();
                for (int k = 0; k < totalSheets; k++) {
                    Sheet worksheet = workbook.getSheetAt(k);
                    XSSFSheet sheet = newBook.createSheet(worksheet.getSheetName());
                    int rows = worksheet.getLastRowNum();
                    for (int i = 0; i <= rows; i++) {
                        Row row = worksheet.getRow(i);
                        if (row != null && !isRowEmpty(row)) {
                            Row cRow = sheet.createRow(i);
                            int cols = row.getLastCellNum();
                            for (int j = 0; j < cols; j++) {
                                Cell cCell = cRow.createCell(j);
                                String s = df.formatCellValue(row.getCell(j));
                                if (s != null) {
                                    s = org.springframework.util.StringUtils.trimWhitespace(s);
                                    cCell.setCellValue(s);
                                } else {
                                    cCell.setCellValue("");
                                }
                            }
                        }
                    }
                }
                newBook.write(fos);
                fos.flush();
                fos.close();
                return fileName;
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        } /*else if (file.getOriginalFilename().trim().endsWith(".csv")) {
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("new sheet");
            File fileName = new File("temp.xlsx");
            String[] nextLine;
            try {
                InputStream is = file.getInputStream();
                CSVReader reader = new CSVReader(new InputStreamReader(is));
                int rowNum = 0;
                while ((nextLine = reader.readNext()) != null) {
                    Row currentRow = sheet.createRow(rowNum++);
                    for (int i = 0; i < nextLine.length; i++) {
                        if (NumberUtils.isDigits(nextLine[i])) {
                            currentRow.createCell(i).setCellValue(Integer.parseInt(nextLine[i]));
                        } else if (NumberUtils.isNumber(nextLine[i])) {
                            currentRow.createCell(i).setCellValue(Double.parseDouble(nextLine[i]));
                        } else {
                            currentRow.createCell(i).setCellValue(nextLine[i]);
                        }
                    }
                }
                FileOutputStream fileOut = new FileOutputStream(fileName);
                wb.write(fileOut);
                fileOut.flush();
                fileOut.close();
                return fileName;
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }*/ else {
            throw new CassiniException(messageSource.getMessage("upload_valid_file_format",
                    null, "Please upload valid file format", LocaleContextHolder.getLocale()));
        }
//        return null;
    }

    public static boolean isRowEmpty(Row row) {
        int i = 0;
        DataFormatter df = new DataFormatter();
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && df.formatCellValue(cell).equals("")) {
                i++;
            }
        }

        if (i == row.getLastCellNum()) {
            return true;
        }
        return false;
    }

    @Transactional
    public static File convertMultipartToFile(MultipartFile multipartFile) {
        try {
            File file = new File(multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            return file;
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }

        return null;
    }

}
