package com.cassinisys.platform.service.utils;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.dto.ColumnData;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    public ColumnData readColumnData(InputStream is) {
        try {
            InputStream bis = new BufferedInputStream(is);
            XSSFWorkbook workBook = new XSSFWorkbook(bis);
            int numberOfSheets = workBook.getNumberOfSheets();
            if(numberOfSheets > 0) {
                XSSFSheet sheet = workBook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    XSSFRow row = (XSSFRow) rowIterator.next();
                    ColumnData columnData = new ColumnData();
                    List<String> list = columnData.getNames();

                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        XSSFCell cell = (XSSFCell) cellIterator.next();
                        list.add(cell.toString());
                    }

                    return columnData;
                }
            }
        } catch (IOException e) {
            throw new CassiniException("Error reading excel file. REASON: " + e.getMessage());
        }

        return null;
    }


    public TableData readData(InputStream is) {
        TableData tableData = new TableData();

        try {
            InputStream bis = new BufferedInputStream(is);
            XSSFWorkbook workBook = new XSSFWorkbook(bis);
            int numberOfSheets = workBook.getNumberOfSheets();
            if(numberOfSheets > 0) {
                XSSFSheet sheet = workBook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                int rowIndex = 1;
                while (rowIterator.hasNext()) {
                    XSSFRow row = (XSSFRow) rowIterator.next();
                    if(rowIndex == 1) {
                        List<String> list = tableData.getColumns().getNames();
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            XSSFCell cell = (XSSFCell) cellIterator.next();
                            list.add(cell.toString());
                        }
                    }
                    else {
                        RowData rowData = new RowData();
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            XSSFCell cell = (XSSFCell) cellIterator.next();
                            rowData.put(tableData.getColumns().getNames().get(cell.getColumnIndex()), cell.toString());
                        }
                        tableData.getRows().add(rowData);
                    }

                    rowIndex++;
                }
            }
        } catch (IOException e) {
            throw new CassiniException("Error reading excel file. REASON: " + e.getMessage());
        }

        return tableData;
    }

    public TableData writeData(TableData tableData, OutputStream os) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        List<String> columns = tableData.getColumns().getNames();
        for(int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        Map<Integer, Boolean> autoColumns = new HashMap<>();

        for(RowData rowData : tableData.getRows()) {
            Row row = sheet.createRow(rowNum++);
            for(int i=0; i<columns.size(); i++) {
                String value = rowData.get(columns.get(i));

                if(value != null && value.length() > 500) {

                }

                Cell cell = row.createCell(i);

                CellStyle style = workbook.createCellStyle(); //Create new style
                style.setWrapText(true); //Set wordwrap
                cell.setCellStyle(style); //Apply style to cell
                cell.setCellValue(value);
            }
        }

        try {
            workbook.write(os);
        } catch (IOException e) {
            throw new CassiniException("Error creating excel file. REASON: " + e.getMessage());
        }

        return tableData;
    }

}
