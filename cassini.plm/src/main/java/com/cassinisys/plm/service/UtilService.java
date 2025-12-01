package com.cassinisys.plm.service;

import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Nageshreddy on 29-09-2020.
 */
@Component
public class UtilService {

    @Autowired
    private FileRepository fileRepository;
    @Value("classpath:mime.types.txt")
    private Resource reportsResource;
    private MimetypesFileTypeMap mimeTypes = null;

    @Autowired
    private SessionWrapper sessionWrapper;

    public String getParentPath(PLMFile file, String path, Integer id) {
        if (file.getParentFile() != null) {
            path = File.separator + file.getId() + path;
            path = visitParentFolder(id, file.getParentFile(), path);
        } else {
            path = File.separator + id + File.separator + file.getId() + path;
        }
        return path;
    }

    public String visitParentFolder(Integer itemId, Integer fileId, String path) {
        PLMFile file = fileRepository.findOne(fileId);
        path = getParentPath(file, path, itemId);
        return path;
    }

    public void removeFileIfExist(List<PLMFile> files, String dir) {
        files.forEach(file -> {
            String filePath = dir + File.separator + file.getId();
            File fDir = new File(filePath);
            FileUtils.deleteQuietly(fDir);
            fileRepository.delete(file.getId());
        });
    }

    private String getMimeType(String fileName) {
        try {
            if (this.mimeTypes == null) {
                this.mimeTypes = new MimetypesFileTypeMap(this.reportsResource.getInputStream());
            }

            return this.mimeTypes.getContentType(fileName);
        } catch (IOException var3) {
            return null;
        }
    }

    public HttpServletResponse generatePdfWaterMark(String filePath, String user, String date, HttpServletResponse response) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(filePath);
        PdfStamper stamper = new PdfStamper(reader, response.getOutputStream());
        int n = reader.getNumberOfPages();
        Font font = new Font(Font.FontFamily.HELVETICA, 70);
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 40);
        Phrase phrase = new Phrase(user, font);
        Phrase datePhrase = new Phrase(date, dateFont);
        // transparency652701
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.3f);
        // properties
        PdfContentByte over;
        Rectangle pagesize;
        float x, y;
        // loop over every page (in case more than one page)
        for (int i = 1; i <= n; i++) {
            pagesize = reader.getPageSizeWithRotation(i);
            x = (pagesize.getLeft() + pagesize.getRight()) / 2;
            y = (pagesize.getTop() + pagesize.getBottom()) / 2;
            over = stamper.getOverContent(i);
            over.saveState();
            over.setGState(gs1);
            // add text for user and date
            ColumnText.showTextAligned(over, Element.ALIGN_CENTER, phrase, x, y, 45f); // 45f means rotate the watermark 45 degrees anticlockwise
            ColumnText.showTextAligned(over, Element.ALIGN_CENTER, datePhrase, x, y - 70, 45f);
            over.restoreState();
        }

        stamper.close();
        reader.close();
        return response;

    }

    @Transactional
    public void writeFileContentToResponse(HttpServletResponse response, String fileName, File file, String user, String date) throws DocumentException {
        response.setContentType(this.getMimeType(fileName));
        try {
            String e = URLDecoder.decode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + e);
        } catch (UnsupportedEncodingException var6) {
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        }
        try {
            response = generatePdfWaterMark(file.getPath(), user, date, response);
            ServletOutputStream e1 = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

}
