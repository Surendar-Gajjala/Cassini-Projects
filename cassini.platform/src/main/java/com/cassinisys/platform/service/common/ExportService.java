package com.cassinisys.platform.service.common;

import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.ExportRow;
import com.cassinisys.platform.model.common.ExportRowDetail;
import com.cassinisys.platform.model.common.Header;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import freemarker.template.Configuration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import com.cassinisys.platform.model.common.Header;

/**
 * Created by lakshmi on 08-10-2017.
 */
@Service
public class ExportService {

    public static Map fileMap = new HashMap();

    @Autowired
    Configuration fmConfiguration;

    @Transactional
    public String getContentFromTemplate(String templatePath, Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            fmConfiguration.setClassForTemplateLoading(this.getClass(), "/templates/");
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate(templatePath), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Transactional
    public String exportFile(String fileType, Export export,
                             HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt = new Date();
        String fName = export.getFileName() + "_" + dateFormat.format(dt);
        if (fileType != null && fileType.equalsIgnoreCase("excel")) {
            fileId = export.getFileName() + ".xls";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("application/vnd.ms-excel");
            Workbook workbook = new HSSFWorkbook();

            try {
                buildExcelDocument(export, workbook, response);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    workbook.write(baos);
                } finally {

                }
                is = new ByteArrayInputStream(baos.toByteArray());

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (fileType != null && fileType.equalsIgnoreCase("pdf")) {

            fileId = export.getFileName() + ".pdf";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("application/pdf");
            try {

                Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
                PdfWriter writer = PdfWriter.getInstance(document, baos);
                writer.setViewerPreferences(getViewerPreferences());
                document.open();

                document.add(new Paragraph(export.getFileName() + " :" + LocalDate.now()));
                buildPdfDocument(export, document, response);
                document.close();
                response.setContentLength(baos.toByteArray().length);
                is = new ByteArrayInputStream(baos.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (fileType != null && fileType.equalsIgnoreCase("csv")) {
            fileId = export.getFileName() + ".csv";

            response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
            response.setContentType("text/csv");
            ByteArrayOutputStream baos = null;
            try {
                baos = writeCsvStream(export);

                response.setContentLength(baos.toByteArray().length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            is = new ByteArrayInputStream(baos.toByteArray());

        } else if (fileType != null && fileType.equalsIgnoreCase("html")) {


            fileId = export.getFileName() + ".html";


            try {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + export.getFileName() + "\"");
                response.setContentType("text/html");

                String htmlResponse = getExportHtml(export);
                response.setContentLength(htmlResponse.getBytes().length);

                is = new ByteArrayInputStream(htmlResponse.getBytes());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fileMap.put(fileId, is);
        return fileId;
    }

    /**
     * Return the viewer preferences for the PDF file.
     * <p>By default returns {@code AllowPrinting} and
     * {@code PageLayoutSinglePage}, but can be subclassed.
     * The subclass can either have fixed preferences or retrieve
     * them from bean properties defined on the View.
     *
     * @return an int containing the bits information against PdfWriter definitions
     */
    @Transactional
    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }

    @Transactional
    protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request) throws DocumentException {
        writer.setViewerPreferences(getViewerPreferences());
    }

    @Transactional
    public void downloadExportFile(String fileId, HttpServletResponse response) {
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileId, "UTF-8"));
        } catch (UnsupportedEncodingException var6) {
            response.setHeader("Content-disposition", "attachment; filename=" + fileId);
        }

        try {
            ServletOutputStream e = response.getOutputStream();
            org.apache.commons.io.IOUtils.copy((InputStream) fileMap.get(fileId), e);
            e.flush();

        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    @Transactional
    public String getExportHtml(Export export) {
        Map<String, Object> model = new HashMap<>();
        List<ExportRow> exportRows = export.getExportRows();
        for (ExportRow exportRow : exportRows) {
            List<ExportRowDetail> exportRowDetails = exportRow.getExportRowDetails();
            for (ExportRowDetail exportRowDetail : exportRowDetails) {
                String value = exportRowDetail.getColumnValue();
                if (value == null) {
                    exportRowDetail.setColumnValue(" ");
                }
            }
        }
        model.put("exportHeaders", export.getHeaders());
        model.put("exportData", export.getExportRows());
        String templatePath = "tablesDataExport/tablesDataExport.html";
        String exportHtmlData = getContentFromTemplate(templatePath, model);
        return exportHtmlData;

    }

    @Transactional
    public void buildExcelDocument(Export export,
                                   Workbook workbook,
                                   HttpServletResponse response) throws Exception {

        HSSFSheet sheet = (HSSFSheet) workbook.createSheet(export.getFileName());
        sheet.setDefaultColumnWidth(30);
        PrintSetup ps = sheet.getPrintSetup();
        sheet.setAutobreaks(true);

        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);

        // create style for header cells
        Font font = workbook.createFont();
        font.setFontName("Arial");
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        int i = 0;
        Row header = sheet.createRow(0);
        for (String headerTxt : export.getHeaders()) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headerTxt);
            cell.setCellStyle(style);
            i++;
        }
        // create header row

        int rowCount = 1;
        CellStyle style1 = workbook.createCellStyle();
        style1.setWrapText(true);
        CellStyle style2 = workbook.createCellStyle();
        style1.setWrapText(true);
        style1.setVerticalAlignment(style1.VERTICAL_TOP);
        style2.setAlignment(style2.ALIGN_CENTER);
        style2.setVerticalAlignment(style2.VERTICAL_TOP);
        for (ExportRow exportRow : export.getExportRows()) {
            Row userRow = sheet.createRow(rowCount++);
            int j = 0;
            for (ExportRowDetail exportRowDetail : exportRow.getExportRowDetails()) {
                if (exportRowDetail.getColumnType() != null) {
                    Cell cell = userRow.createCell(j);
                    int columnIndex = cell.getColumnIndex();
                    if (exportRowDetail.getColumnValue() != null && exportRowDetail.getColumnValue().matches("[0-9]+")) {
                        cell.setCellValue(exportRowDetail.getColumnValue());
                        cell.setCellStyle(style2);
                        sheet.autoSizeColumn(columnIndex);
                    } else if (exportRowDetail.getColumnType().equalsIgnoreCase("String")) {
                        int length1 = sheet.getColumnWidth(columnIndex)/300;
                        int length2 = exportRowDetail.getColumnValue() != null ? exportRowDetail.getColumnValue().length() : 0;
                        if (length2 < length1) {
                            length2 = length1;
                        }
                        if (length2 < 15) {
                            sheet.setColumnWidth(columnIndex, 300 * 15);
                        } else if (length2 < 50) {
                            sheet.setColumnWidth(columnIndex, 300 * length2);
                        } else {
                            sheet.setColumnWidth(columnIndex, 300 * 50);
                        }
                        cell.setCellValue(exportRowDetail.getColumnValue());
                        cell.setCellStyle(style1);
                    } else {
                        cell.setCellValue(exportRowDetail.getColumnValue());
                        cell.setCellStyle(style2);
                        sheet.autoSizeColumn(columnIndex);
                    }
                } else {
                    userRow.createCell(j).setCellValue("");
                    userRow.getCell(j).setCellStyle(style1);
                }
                j++;
            }
        }
    }

    @Transactional
    public void buildPdfDocument(Export export,
                                 Document document,
                                 HttpServletResponse response) throws Exception {
        // change the file name
        PdfPTable table = new PdfPTable(export.getHeaders().size());
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(10);

        // define font for table header row
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setColor(BaseColor.WHITE);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);

        for (String headerTxt : export.getHeaders()) {
            cell.setPhrase(new Phrase(headerTxt, font));
            table.addCell(cell);
        }
        for (ExportRow exportRow : export.getExportRows()) {
            for (ExportRowDetail exportRowDetail : exportRow.getExportRowDetails()) {
                table.addCell(exportRowDetail.getColumnValue());
            }
        }
        document.add(table);
    }

    @Transactional
    public ByteArrayOutputStream writeCsvStream(Export export) throws IOException {
        List<List<String>> listList = new ArrayList<List<String>>();
        List<String> lst = null;
        listList.add(export.getHeaders());
        for (ExportRow exportRow : export.getExportRows()) {
            lst = new ArrayList<>();
            for (ExportRowDetail exportRowDetail : exportRow.getExportRowDetails()) {
                lst.add(exportRowDetail.getColumnValue());
            }
            listList.add(lst);
        }

        final ByteArrayOutputStream riskCategoryCsvStream = new ByteArrayOutputStream();
        Writer out = new BufferedWriter(new OutputStreamWriter(riskCategoryCsvStream));
        CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.EXCEL);
        csvPrinter.printRecords(listList);
        csvPrinter.flush();
        csvPrinter.close();
        return riskCategoryCsvStream;

    }

    public void createExportObject(List reportDTOs, List<String> columns, Export export) {
        List<ExportRow> exportRows = new ArrayList();
        for (Object row : reportDTOs) {
            ExportRow exportRow = new ExportRow();
            List<ExportRowDetail> rowDetails = new ArrayList();
            Class ftClass = row.getClass();

            Field[] fields = ftClass.getDeclaredFields();
            Map<String, Field> fieldMap = new HashMap<>();
            for (Field field : fields) {
                Header header = field.getDeclaredAnnotation(Header.class);
                if (header != null) {
                    fieldMap.put(header.value(), field);
                }
            }

            for (int i = 0; i < columns.size(); i++) {
                ExportRowDetail rowDetail1 = new ExportRowDetail();
                String column = columns.get(i);
                rowDetail1.setColumnName(column);
                String column2 = column.replaceAll("\\s", "");
                String column1 = column2.substring(0, 1).toLowerCase() + column2.substring(1);
                Field f1 = ReflectionUtils.findField(ftClass, column1);

                if (f1 != null) {
                    assignValue(f1, row, rowDetail1);
                } else if (fieldMap.containsKey(column)) {
                    f1 = fieldMap.get(column);
                    assignValue(f1, row, rowDetail1);
                } else {
                    System.err.println(column + " property or Header Value doesn't exist in " + ftClass);
                    rowDetail1.setColumnValue("");
                }
                rowDetails.add(rowDetail1);
            }
            exportRow.setExportRowDetails(rowDetails);
            exportRows.add(exportRow);
        }
        export.setExportRows(exportRows);
    }

    private void assignValue(Field f1, Object row, ExportRowDetail rowDetail1) {
        try {
            ReflectionUtils.makeAccessible(f1);
            if (f1.get(row) != null) {
                if (!f1.get(row).toString().trim().equals(0)) {
                    Class aClass = f1.getType();
                    if (aClass.toString().contains("String") || aClass.toString().contains("Character")) {
                        rowDetail1.setColumnType("String");
                        rowDetail1.setColumnValue(f1.get(row).toString());
                    } else {
                        rowDetail1.setColumnType("Numeric");
                        rowDetail1.setColumnValue(f1.get(row).toString());
                    }
                } else {
                    rowDetail1.setColumnValue("");
                }
            } else {
                rowDetail1.setColumnValue("");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
