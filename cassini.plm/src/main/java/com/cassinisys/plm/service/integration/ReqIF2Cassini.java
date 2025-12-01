package com.cassinisys.plm.service.integration;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.integration.reqif.model.*;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.rm.SpecificationType;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.service.rm.RequirementsService;
import com.cassinisys.plm.service.rm.SpecificationsService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ReqIF2Cassini {
    @Value("classpath:integration/reqif/ReqIFParser.groovy")
    private Resource parserSource;

    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private SpecElementRepository specElementRepository;
    @Autowired
    private SpecSectionRepository specSectionRepository;
    @Autowired
    private SpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private RequirementTypeRepository requirementTypeRepository;
    @Autowired
    private RmObjectTypeAttributeRepository rmObjectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private SpecRequirementRepository specRequirementRepository;
    @Autowired
    private SpecificationsService specificationsService;
    @Autowired
    private RequirementsService requirementsService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private RmObjectAttributeRepository rmObjectAttributeRepository;
    @Autowired
    private ReqIFService reqIFService;
    @Autowired
    private PersonService personService;
    @Autowired
    private MessageSource messageSource;

    private boolean isImportFromArchive = false;
    private SystemMapping systemMapping = null;
    private List<ReqIFArchiveFile> reqifArchiveFiles = new ArrayList<>();
    private Map<String, ReqIFArchiveFile> archiveFilesMap = new HashMap<>();

    public com.cassinisys.plm.model.rm.Specification importReqIf(Integer specId, InputStream inputStream) {
        systemMapping = reqIFService.getReqIFMapping().getSystemByName("Doors");
        com.cassinisys.plm.model.rm.Specification specification = null;
        try {
            ScriptEngineManager factory = new ScriptEngineManager();
            GroovyScriptEngineImpl engine = (GroovyScriptEngineImpl) factory.getEngineByName("groovy");
            engine.put("_reqIfInputStream", inputStream);
            engine.put("_scriptDir", parserSource.getFile().getParentFile());
            ReqIF reqIF = (ReqIF) engine.eval(new FileReader(parserSource.getFile()));
            specification = importReqifIntoSpec(specId, reqIF);
        } catch (IOException | ScriptException e) {
            throw new CassiniException(e.getMessage());
        }
        return specification;
    }

    public void clearCache() {
        isImportFromArchive = false;
        reqifArchiveFiles.clear();
        archiveFilesMap.clear();
    }

    public com.cassinisys.plm.model.rm.Specification importReqifArchive(Integer specId, InputStream inputStream) {
        com.cassinisys.plm.model.rm.Specification specification = null;
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        final ZipInputStream zis = new ZipInputStream(bis);
        try {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                ReqIFArchiveFile archiveFile = new ReqIFArchiveFile();
                archiveFile.setFileName(name);
                archiveFile.setBytes(IOUtils.toByteArray(zis));
                reqifArchiveFiles.add(archiveFile);
                archiveFilesMap.put(name, archiveFile);
            }
            zis.close();
            if (reqifArchiveFiles.size() == 0) {
                throw new CassiniException(messageSource.getMessage("no_files_in_archive_file", null, "There are no files in the reqif archive file", LocaleContextHolder.getLocale()));
            }
            for (ReqIFArchiveFile reqifArchiveFile : reqifArchiveFiles) {
                String fileName = reqifArchiveFile.getFileName();
                if (fileName.toLowerCase().endsWith(".reqif")) {
                    isImportFromArchive = true;
                    ByteArrayInputStream bstream = new ByteArrayInputStream(reqifArchiveFile.getBytes());
                    specification = importReqIf(specId, bstream);
                    break;
                }
            }

        } catch (Exception e) {
            throw new CassiniException(messageSource.getMessage("error_reading_reqif_file" + " : " + e.getMessage(), null, "Error reading reqif archive file. REASON", LocaleContextHolder.getLocale()));
        }
        return specification;
    }

    private com.cassinisys.plm.model.rm.Specification importReqifIntoSpec(Integer specId, ReqIF reqIF) {
        com.cassinisys.plm.model.rm.Specification specification = null;
        List<com.cassinisys.plm.integration.reqif.model.Specification> reqIfSpecifications = reqIF.getContent().getSpecifications();
        if (reqIfSpecifications.size() == 0) {
            throw new CassiniException(messageSource.getMessage("there_is_no_specification_file", null, "There are no specifications in this file", LocaleContextHolder.getLocale()));
        }
        if (reqIfSpecifications.size() > 1) {
            throw new CassiniException(messageSource.getMessage("more_than_one_file_cannot_proceed", null, "There are more than one specifications in the reqif file. Import cannot proceed.", LocaleContextHolder.getLocale()));
        }
        com.cassinisys.plm.integration.reqif.model.Specification spec = reqIfSpecifications.get(0);
        com.cassinisys.plm.model.rm.Specification cassiniSpecification = specificationRepository.findOne(specId);
        if (cassiniSpecification != null) {
            cassiniSpecification.setName(spec.getLongName());
            cassiniSpecification.setDescription(spec.getDescription());
            cassiniSpecification = specificationRepository.save(cassiniSpecification);
            addSpecAttributes(spec, cassiniSpecification);
            addSpecFiles(spec, cassiniSpecification);
            createSpecHierarchy(spec.getChildren(), null, cassiniSpecification);
            specification = cassiniSpecification;
        }
        return specification;
    }

    private void addSpecFiles(com.cassinisys.plm.integration.reqif.model.Specification reqifSpec,
                              com.cassinisys.plm.model.rm.Specification cassiniSpecification) {
        if (isImportFromArchive) {
            AttributeValue attachmentsValue = reqifSpec.getAttributeValueByAttributeName("CASSINI-FILE-ATTACHMENTS");
            if (attachmentsValue != null && attachmentsValue instanceof AttributeValueXhtml) {
                AttributeValueXhtml filesAtt = (AttributeValueXhtml) attachmentsValue;
                uploadFiles(cassiniSpecification.getId(), filesAtt);
            }
        }
    }

    private void addReqFiles(com.cassinisys.plm.integration.reqif.model.SpecObject specObject,
                             com.cassinisys.plm.model.rm.Requirement cassiniRequirement) {
        if (isImportFromArchive) {
            AttributeValue attachmentsValue = specObject.getAttributeValueByAttributeName("CASSINI-FILE-ATTACHMENTS");
            if (attachmentsValue != null && attachmentsValue instanceof AttributeValueXhtml) {
                AttributeValueXhtml filesAtt = (AttributeValueXhtml) attachmentsValue;
                uploadFiles(cassiniRequirement.getId(), filesAtt);
            }
        }
    }

    private void uploadFiles(Integer id, AttributeValueXhtml filesAtt) {
        try {
            List<String> fileNames = filesAtt.getFileNames();
            if (fileNames.size() > 0) {
                Map<String, MultipartFile> filesMap = new HashMap<>();
                for (String fileName : fileNames) {
                    ReqIFArchiveFile archiveFile = archiveFilesMap.get(fileName);
                    if (archiveFile != null) {
                        String name = FilenameUtils.getName(fileName);
                        ByteArrayInputStream bstream = new ByteArrayInputStream(archiveFile.getBytes());
                        //MultipartFile multipartFile = new MockMultipartFile(name, name, reqIFService.getMimeType(name), bstream);
                        FileItem fileItem = new DiskFileItemFactory().createItem("file",
                                reqIFService.getMimeType(name), false, name);
                        IOUtils.copy(bstream, fileItem.getOutputStream());
                        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
                        filesMap.put(name, multipartFile);
                    }
                }
                specificationsService.uploadRmObjectFiles(id, filesMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSpecAttributes(com.cassinisys.plm.integration.reqif.model.Specification reqifSpec,
                                   com.cassinisys.plm.model.rm.Specification cassiniSpec) {
        SpecificationType reqType = specificationTypeRepository.findByName(systemMapping.getDefaultSpecType());
        if (reqType != null) {
            List<RmObjectTypeAttribute> attributes = specificationsService.getAttributes(reqType.getId(), true);//rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(reqType.getId());
            for (RmObjectTypeAttribute attribute : attributes) {
                AttributeMapping attributeMapping = systemMapping.findByCassiniName(attribute.getName());
                String systemName = attribute.getName();
                if (attributeMapping != null && attributeMapping.getSystemName() != null &&
                        !attributeMapping.getSystemName().trim().isEmpty()) {
                    systemName = attributeMapping.getSystemName().trim();
                }
                AttributeValue attributeValue = reqifSpec.getAttributeValueByAttributeName(systemName);
                if (attributeValue != null) {
                    setCassiniAttributeValue(attributeValue, cassiniSpec.getId(), attribute);
                }
            }
        }
    }

    private void setCassiniAttributeValue(AttributeValue reqifAttributeValue, Integer objectId, RmObjectTypeAttribute attribute) {
        RmObjectAttribute rmObjectAttribute = null;
        ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(objectId, attribute.getId());
        if (objectAttribute == null) {
            rmObjectAttribute = new RmObjectAttribute();
            rmObjectAttribute.setId(new ObjectAttributeId(objectId, attribute.getId()));
        }
        if (rmObjectAttribute != null) {
            if (attribute.getDataType() == DataType.TEXT) {
                rmObjectAttribute.setStringValue(((AttributeValueString) reqifAttributeValue).getValue());
            } else if (attribute.getDataType() == DataType.LONGTEXT) {
                rmObjectAttribute.setLongTextValue(((AttributeValueString) reqifAttributeValue).getValue());
            } else if (attribute.getDataType() == DataType.INTEGER) {
                rmObjectAttribute.setIntegerValue(((AttributeValueInteger) reqifAttributeValue).getValue());
            } else if (attribute.getDataType() == DataType.DOUBLE) {
                rmObjectAttribute.setDoubleValue((double) ((AttributeValueReal) reqifAttributeValue).getValue());
            } else if (attribute.getDataType() == DataType.DATE) {
                rmObjectAttribute.setDateValue(((AttributeValueDate) reqifAttributeValue).getValue());
            } else if (attribute.getDataType() == DataType.BOOLEAN) {
                rmObjectAttribute.setBooleanValue(((AttributeValueBoolean) reqifAttributeValue).getValue());
            } else if (attribute.getDataType() == DataType.RICHTEXT) {
                ((AttributeValueXhtml) reqifAttributeValue).updateImages(archiveFilesMap);
                rmObjectAttribute.setRichTextValue(((AttributeValueXhtml) reqifAttributeValue).getValue());
            } else if (attribute.getDataType() == DataType.LIST) {
                if (attribute.isListMultiple()) {
                    String s = ((AttributeValueString) reqifAttributeValue).getValue();
                    String[] arr = s.split(";");
                    rmObjectAttribute.setMListValue(arr);
                } else {
                    rmObjectAttribute.setListValue(((AttributeValueString) reqifAttributeValue).getValue());
                }
            } else if (attribute.getDataType() == DataType.OBJECT) {
                rmObjectAttribute.setRefValue(((AttributeValueInteger) reqifAttributeValue).getValue());
            }
            rmObjectAttributeRepository.save(rmObjectAttribute);
        }
    }

    private void createSpecHierarchy(List<SpecHierarchy> children,
                                     SpecElement parentElem,
                                     com.cassinisys.plm.model.rm.Specification cassiniSpecification) {
        for (SpecHierarchy child : children) {
            SpecObject specObject = child.getObject();
            if (specObject != null) {
                Integer cassiniId = specObject.getCassiniId();
                if (specObject.getType().getLongName().equalsIgnoreCase("Requirement Section")) {
                    SpecSection section = null;
                    if (cassiniId == null || specSectionRepository.findBySpecificationAndId(cassiniSpecification.getId(), cassiniId) == null) {
                        section = new SpecSection();
                        section.setSpecification(cassiniSpecification.getId());
                        if (parentElem != null) {
                            section.setParent(parentElem.getId());
                        }
                        String name = specObject.getLongName();
                        AttributeValueString aString = (AttributeValueString) specObject.getAttributeValueByAttributeName("ReqIF.ChapterName");
                        if (aString != null && !aString.getValue().trim().isEmpty()) {
                            name = aString.getValue();
                        }
                        section.setName(name);
                        String description = specObject.getDescription();
                        aString = (AttributeValueString) specObject.getAttributeValueByAttributeName("ReqIF.Text");
                        if (aString != null && !aString.getValue().trim().isEmpty()) {
                            description = aString.getValue();
                        }
                        section.setDescription(description);
                        section = specificationsService.createSpecSection(section);
                    } else {
                        section = specSectionRepository.findBySpecificationAndId(cassiniSpecification.getId(), cassiniId);
                        String name = specObject.getLongName();
                        AttributeValueString aString = (AttributeValueString) specObject.getAttributeValueByAttributeName("ReqIF.ChapterName");
                        if (aString != null && !aString.getValue().trim().isEmpty()) {
                            name = aString.getValue();
                        }
                        section.setName(name);
                        String description = specObject.getDescription();
                        aString = (AttributeValueString) specObject.getAttributeValueByAttributeName("ReqIF.Text");
                        if (aString != null) {
                            description = aString.getValue();
                        }
                        section.setDescription(description);
                        section = specSectionRepository.save(section);
                    }
                    createSpecHierarchy(child.getChildren(), section, cassiniSpecification);
                } else if (specObject.getType().getLongName().equalsIgnoreCase("Requirement")) {
                    Requirement cassiniRequirement = null;
                    if (cassiniId == null || requirementRepository.findBySpecificationAndId(cassiniSpecification.getId(), cassiniId) == null) {
                        AttributeValueString tyepAttribute = (AttributeValueString) specObject.getAttributeValueByAttributeName("Cassini.Type");
                        RequirementType reqType = null;
                        if (tyepAttribute != null) {
                            reqType = requirementTypeRepository.findByName(tyepAttribute.getValue());
                        }
                        if (reqType == null) {
                            reqType = requirementTypeRepository.findByName(systemMapping.getDefaultReqType());
                        }
                        if (reqType != null && reqType.getNumberSource() != null) {
                            cassiniRequirement = new Requirement();
                            cassiniRequirement.setType(reqType);
                            cassiniRequirement.setSpecification(cassiniSpecification.getId());
                            cassiniRequirement.setObjectNumber(autoNumberService.getNextNumber(reqType.getNumberSource().getId()));
                            String name = specObject.getLongName();
                            AttributeValueString aString = (AttributeValueString) specObject.getAttributeValueByAttributeName("ReqIF.Name");
                            if (aString != null && !aString.getValue().trim().isEmpty()) {
                                name = aString.getValue();
                            }
                            cassiniRequirement.setName(name);
                            String description = specObject.getDescription();
                            AttributeValueXhtml xString = (AttributeValueXhtml) specObject.getAttributeValueByAttributeName("ReqIF.Text");
                            if (xString != null && !xString.getValue().trim().isEmpty()) {
                                xString.updateImages(archiveFilesMap);
                                description = xString.getValue();
                            }
                            cassiniRequirement.setDescription(description);
                            AttributeValueInteger assignedToAtt = (AttributeValueInteger) specObject.getAttributeValueByAttributeName("Cassini.AssignedTo");
                            if (assignedToAtt != null) {
                                Integer assignedTo = assignedToAtt.getValue();
                                if (assignedTo != null && assignedTo != 0) {
                                    cassiniRequirement.setAssignedTo(personService.get(assignedTo));
                                }
                            }
                            AttributeValueDate finishDateAtt = (AttributeValueDate) specObject.getAttributeValueByAttributeName("Cassini.PlannedFinishDate");
                            if (finishDateAtt != null) {
                                Date dt = finishDateAtt.getValue();
                                cassiniRequirement.setPlannedFinishDate(dt);
                            }
                            cassiniRequirement = requirementRepository.save(cassiniRequirement);
                            SpecRequirement specRequirement = new SpecRequirement();
                            specRequirement.setSpecification(cassiniSpecification.getId());
                            specRequirement.setRequirement(cassiniRequirement);
                            if (parentElem != null) {
                                specRequirement.setParent(parentElem.getId());
                            }
                            specificationsService.createSpecRequirement(specRequirement);
                        }
                    } else {
                        cassiniRequirement = requirementRepository.findBySpecificationAndId(cassiniSpecification.getId(), cassiniId);
                        String name = specObject.getLongName();
                        AttributeValueString aString = (AttributeValueString) specObject.getAttributeValueByAttributeName("ReqIF.Name");
                        if (aString != null) {
                            name = aString.getValue();
                        }
                        cassiniRequirement.setName(name);
                        String description = specObject.getDescription();
                        AttributeValueXhtml xString = (AttributeValueXhtml) specObject.getAttributeValueByAttributeName("ReqIF.Text");
                        if (xString != null) {
                            description = xString.getValue();
                        }
                        cassiniRequirement.setDescription(description);
                        AttributeValueInteger assignedToAtt = (AttributeValueInteger) specObject.getAttributeValueByAttributeName("Cassini.AssignedTo");
                        if (assignedToAtt != null) {
                            Integer assignedTo = assignedToAtt.getValue();
                            if (assignedTo != null && assignedTo != 0) {
                                cassiniRequirement.setAssignedTo(personService.get(assignedTo));
                            }
                        }
                        AttributeValueDate finishDateAtt = (AttributeValueDate) specObject.getAttributeValueByAttributeName("Cassini.PlannedFinishDate");
                        if (finishDateAtt != null) {
                            Date dt = finishDateAtt.getValue();
                            cassiniRequirement.setPlannedFinishDate(dt);
                        }
                        cassiniRequirement = requirementRepository.save(cassiniRequirement);
                    }
                    addReqAttributes(specObject, cassiniRequirement);
                    addReqFiles(specObject, cassiniRequirement);
                }
            }
        }
    }

    private void addReqAttributes(SpecObject specObject,
                                  Requirement requirement) {
        AttributeValueString tyepAttribute = (AttributeValueString) specObject.getAttributeValueByAttributeName("Cassini.Type");
        RequirementType reqType = null;
        if (tyepAttribute != null) {
            reqType = requirementTypeRepository.findByName(tyepAttribute.getValue());
        }
        if (reqType == null) {
            reqType = requirementTypeRepository.findByName(systemMapping.getDefaultReqType());
        }
        if (reqType != null) {
            List<RmObjectTypeAttribute> attributes = requirementsService.getAttributes(reqType.getId(), true); //rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(reqType.getId());
            for (RmObjectTypeAttribute attribute : attributes) {
                AttributeMapping attributeMapping = systemMapping.findByCassiniName(attribute.getName());
                String systemName = attribute.getName();
                if (attributeMapping != null && attributeMapping.getSystemName() != null &&
                        !attributeMapping.getSystemName().trim().isEmpty()) {
                    systemName = attributeMapping.getSystemName().trim();
                }
                AttributeValue attributeValue = specObject.getAttributeValueByAttributeName(systemName);
                if (attributeValue != null) {
                    setCassiniAttributeValue(attributeValue, requirement.getId(), attribute);
                }
            }
        }
    }

    public class ReqIFArchiveFile {
        private String fileName;
        private InputStream inputStream;
        private byte[] bytes;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }
    }

}
