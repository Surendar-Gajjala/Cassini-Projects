package com.cassinisys.plm.service.integration;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.plm.integration.reqif.model.*;
import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.RmObjectFile;
import com.cassinisys.plm.model.rm.Specification;
import com.cassinisys.plm.repo.rm.RequirementRepository;
import com.cassinisys.plm.repo.rm.RmObjectFileRepository;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.rm.SpecificationsService;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ReqIFService {
    public static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    @Autowired
    private SpecificationsService specificationsService;

    @Autowired
    private Cassini2ReqIF cassini2ReqIF;

    @Autowired
    private ReqIF2Cassini reqIF2Cassini;

    @Autowired
    private RmObjectFileRepository fileRepository;

    @Autowired
    private ItemFileService itemFileService;

    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private MessageSource messageSource;

    @Value("classpath:mime.types.txt")
    private Resource mimeTypesResourc;

    private MimetypesFileTypeMap mimeTypes = null;

    @Value("classpath:integration/reqif/mapping.json")
    private Resource mappingFile;
    private ReqIFMapping reqIFMapping;

    public static Date fromStringToDate(String sDate) {
        if (sDate == null || sDate.trim().isEmpty()) return null;
        try {
            DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_FORMAT);
            DateTime dateTime = dtf.parseDateTime(sDate);
            return dateTime.toDate();
        } catch (Exception e) {
            return null;
        }
    }

    public static String fromDateToString(Date date) {
        if (date == null) return "";
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
            DateTime dt = new DateTime(date);
            return fmt.print(dt);
        } catch (Exception e) {
            return "";
        }
    }

    public void convertToReqIF(Integer specId, HttpServletResponse httpResponse) {
        Specification specification = specificationsService.findById(specId);
        String fileName = specification.getName() + ".reqifz";
        httpResponse.setContentType("application/zip");
        httpResponse.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        com.cassinisys.plm.integration.reqif.model.Specification.specName = specification.getName();
        ReqIF reqIF = cassini2ReqIF.toReqIF(specification);
        try {
            OutputStream out = httpResponse.getOutputStream();
            ZipOutputStream zout = new ZipOutputStream(out);
            List<RmObjectFile> files = fileRepository.findByObjectAndLatestTrueOrderByModifiedDateDesc(specId);
            String xhtml = writeFilesToZip(specification, reqIF, files, zout);
            if (!xhtml.isEmpty()) {
                com.cassinisys.plm.integration.reqif.model.Specification specObject =
                        reqIF.getContent().getSpecificationByIntegerAttribute("Cassini ID", specId);
                if (specObject != null) {
                    AttributeDefinition def = specObject.getType().getSpecAttributeByName("CASSINI-FILE-ATTACHMENTS");
                    if (def != null) {
                        AttributeValueXhtml xhtmlAtt = new AttributeValueXhtml(def);
                        xhtmlAtt.setValue(xhtml);
                        specObject.getValues().add(xhtmlAtt);
                    }
                }
            }
            List<Requirement> requirements = requirementRepository.findBySpecificationAndLatestTrue(specId);
            for (Requirement requirement : requirements) {
                files = fileRepository.findByObjectAndLatestTrueOrderByModifiedDateDesc(requirement.getId());
                xhtml = writeFilesToZip(specification, reqIF, files, zout);
                if (!xhtml.isEmpty()) {
                    SpecObject specObject =
                            reqIF.getContent().getSpecObjectByIntegerAttribute("Cassini ID", requirement.getId());
                    if (specObject != null) {
                        AttributeDefinition def = specObject.getType().getSpecAttributeByName("CASSINI-FILE-ATTACHMENTS");
                        if (def != null) {
                            AttributeValueXhtml xhtmlAtt = new AttributeValueXhtml(def);
                            xhtmlAtt.setValue(xhtml);
                            specObject.getValues().add(xhtmlAtt);
                        }
                    }
                }
            }
            String reqifString = reqIF.toXmlString();
            zout.putNextEntry(new ZipEntry(specification.getName() + ".reqif"));
            byte[] bytes = reqifString.getBytes();
            zout.write(bytes, 0, bytes.length);
            zout.closeEntry();
            writeOleObjectsToZip(specification, reqIF, zout);
            zout.close();
            out.flush();
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }
    }

    private void writeOleObjectsToZip(Specification specification, ReqIF reqIF, ZipOutputStream zout) {
        List<ImageObject> imageObjects = AttributeValueXhtml.getImageObjects();
        try {
            for (ImageObject imageObject : imageObjects) {
                String fileName = specification.getName() + "/" + imageObject.getOleFileName();
                zout.putNextEntry(new ZipEntry(fileName));
                String imgData = imageObject.getImageData();
                String base64 = imgData.split(",")[1];
                byte[] imageBytes = Base64.decodeBase64(base64);
                zout.write(imageBytes, 0, imageBytes.length);
                zout.closeEntry();
                fileName = specification.getName() + "/" + imageObject.getImageFileName();
                zout.putNextEntry(new ZipEntry(fileName));
                imageBytes = Base64.decodeBase64(base64);
                zout.write(imageBytes, 0, imageBytes.length);
                zout.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageObjects.clear();
    }

    private String writeFilesToZip(Specification specification, ReqIF reqIF, List<RmObjectFile> files, ZipOutputStream zout) {
        String xhtml = "";
        for (RmObjectFile file : files) {
            File fileObject = itemFileService.getObjectFileOnDisk(file.getObject(), file.getId());
            if (fileObject != null) {
                try {
                    String fileName = specification.getName() + "/" + file.getName();
                    zout.putNextEntry(new ZipEntry(fileName));
                    FileInputStream fis = new FileInputStream(fileObject);
                    byte[] fileContent = Files.readAllBytes(fileObject.toPath());
                    zout.write(fileContent, 0, fileContent.length);
                    zout.closeEntry();
                    fis.close();
                    String mimeType = getMimeType(fileName);
                    String s = "<reqif-xhtml:object type=\"{0}\" data=\"{1}\">{2}</reqif-xhtml:object>";
                    xhtml += MessageFormat.format(s, mimeType, fileName, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xhtml;
    }

    public Specification convertToCassini(Integer specId, MultipartHttpServletRequest request) {
        Specification specification = null;
        List<MultipartFile> multipartFiles = new ArrayList<>(request.getFileMap().values());
        MultipartFile multipartFile = multipartFiles.get(0);
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = multipartFile.getName();
        }
        //MultipartFile mFile = new MockMultipartFile("file",
        //       file.getName(), "text/plain", IOUtils.toByteArray(input));
        if (fileName != null) {
            try {
                fileName = fileName.toLowerCase();
                reqIF2Cassini.clearCache();
                if (fileName.endsWith(".reqif")) {
                    specification = reqIF2Cassini.importReqIf(specId, multipartFile.getInputStream());
                } else if (fileName.endsWith(".reqifz")) {
                    specification = reqIF2Cassini.importReqifArchive(specId, multipartFile.getInputStream());
                } else {
                    throw new CassiniException(messageSource.getMessage("unknown_file_format_reqif_supported", null, "Unknown file format. Only .reqif or .reqifz files are supported.", LocaleContextHolder.getLocale()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return specification;
    }

    public String getMimeType(String fileName) {
        try {
            if (mimeTypes == null) {
                mimeTypes = new MimetypesFileTypeMap(mimeTypesResourc.getInputStream());
            }
            return mimeTypes.getContentType(fileName);
        } catch (IOException e) {
            return null;
        }
    }

    public ReqIFMapping getReqIFMapping() {
        if (reqIFMapping == null) {
            loadMappingFile();
        }
        return reqIFMapping;
    }

    private void loadMappingFile() {
        try {
            reqIFMapping = ReqIFMapping.parse(mappingFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
