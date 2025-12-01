package com.cassinisys.plm.controller.exim;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.plm.service.exim.exporter.Exporter;
import com.cassinisys.plm.service.exim.importer.Importer;
import com.cassinisys.plm.service.exim.importer.ItemImporter;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/plm/exim")
@Api(tags = "PLM.EXIM", description = "Export Import Related")
public class ImportExportController extends BaseController {
    @Autowired
    private Exporter exporter;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemImporter itemImporter;

    @RequestMapping(value = "/export/getfile", method = RequestMethod.GET, produces = "application/zip")
    public void getExportedFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = "cassini-plm-export.zip";
            response.setContentType("application/zip");
            response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
            OutputStream out = response.getOutputStream();
            byte[] bytes = (byte[]) request.getSession().getAttribute("exportFile");
            if (bytes != null) {
                IOUtils.copy(new ByteArrayInputStream(bytes), out);
                request.getSession().setAttribute("exportFile", null);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public boolean exportData(@RequestParam List<String> objects, HttpServletRequest request) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ZipOutputStream zout = new ZipOutputStream(out);
            exporter.exportData(zout, null, objects.toArray(new String[0]));
            zout.close();
            out.flush();
            byte[] bytes = out.toByteArray();
            request.getSession().setAttribute("exportFile", bytes);
        } catch (IOException e) {
            throw new CassiniException(messageSource.getMessage("error_import_reason" + e.getMessage(),
                    null, "Error importing data. REASON" + e.getMessage(), LocaleContextHolder.getLocale()));
        }
        return true;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public boolean importData(MultipartHttpServletRequest request) {
        Map<String, MultipartFile> filesMap = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(filesMap.values());
        if (files.size() > 1) {
            throw new CassiniException(messageSource.getMessage("only_one_file_supported_to_import",
                    null, "Only one file allowed to import. Multiple files not supported.", LocaleContextHolder.getLocale()));
        }
        MultipartFile file = files.get(0);
        String name = file.getOriginalFilename();
        try {
            if (name.toLowerCase().endsWith(".zip")) {
                BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
                final ZipInputStream zis = new ZipInputStream(bis);
                importer.importData(zis);
                zis.close();
            } else {
                throw new CassiniException(messageSource.getMessage("only_zip_files_allowed",
                        null, "Only .zip files are allowed to import. Please upload another file.", LocaleContextHolder.getLocale()));
            }
        } catch (IOException e) {
            throw new CassiniException(messageSource.getMessage("error_import_reason" + e.getMessage(),
                    null, "Error importing data. REASON" + e.getMessage(), LocaleContextHolder.getLocale()));
        }
        return true;
    }

    @RequestMapping(value = "/get/headers", method = RequestMethod.POST)
    public List<String> getHeadersFromFile(MultipartHttpServletRequest request) throws Exception {
        return itemImporter.getHeadersFromExcel(request.getFileMap());
    }

    @RequestMapping(value = "/send/headers/{objecttype}", method = RequestMethod.POST)
    public void sendHeaders(@PathVariable("objecttype") String objectType, @RequestBody Map<String, String> headersDTOs) {
        importer.saveHeadersMap(headersDTOs, objectType);
    }

      /*
    * Download Object templates
    * */

    @RequestMapping(value = "/download/{objecttype}/excel", method = RequestMethod.GET)
    public void downloadObjectTemplate(HttpServletResponse response, @PathVariable("objecttype") String objectType) {
        importer.downloadObjectTemplate(response, objectType);
    }


    @RequestMapping(value = "/import/{objecttype}/file", method = RequestMethod.POST)
    public void importIndividualObjectFile(@PathVariable("objecttype") String objectType, MultipartHttpServletRequest request) throws Exception {
        importer.importIndividualObjectFile(objectType, request.getFileMap());
    }

}