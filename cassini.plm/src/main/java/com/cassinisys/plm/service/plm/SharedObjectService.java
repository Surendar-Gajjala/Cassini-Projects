package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.SharedTypeService;
import com.cassinisys.platform.service.core.SharedTypeSystem;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.filtering.SharedObjectCriteria;
import com.cassinisys.plm.filtering.SharedObjectPredicateBuilder;
import com.cassinisys.plm.model.dto.*;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.pgc.PGCDeclaration;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.pgc.PGCDeclarationRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.service.pm.ProjectService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by GSR on 20-10-2017.
 */
@Service
@Transactional
public class SharedObjectService implements CrudService<PLMSharedObject, Integer>, SharedTypeSystem {

    @Autowired
    private SharedObjectRepository sharedObjectRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private EMailTemplateConfigRepository eMailTemplateConfigRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private PGCDeclarationRepository declarationRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SharedTypeService sharedTypeService;
    @Autowired
    private SharedObjectPredicateBuilder sharedObjectPredicateBuilder;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private PLMActivityRepository plmActivityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProjectDeliveravbleRepository projectDeliveravbleRepository;
    @Autowired
    private ActivityDeliverableRepository activityDeliverableRepository;
    @Autowired
    private TaskDeliverableRepository taskDeliverableRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private PLMDocumentRepository documentRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private PLMDocumentRepository plmDocumentRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramProjectRepository programProjectRepository;


    @PostConstruct
    public void InitSharedObjectService() {
        sharedTypeService.registerTypeSystem("sharedObject", new SharedObjectService());
    }

    @Override
    public PLMSharedObject create(PLMSharedObject plmSharedObject) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.ITEM.toString())) {
            PLMItem item = itemRepository.findOne(plmSharedObject.getObjectId());
            List<PLMItem> items = new ArrayList();
            items.add(item);
            objectIds.add(item.getId());
            List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
            for (Integer person : sharedToObjects) {
                List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                        plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
                List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                        .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
                if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                    sharedObject = sharedObjects1.get(0);
                    sharedObject.setPermission(plmSharedObject.getPermission());
                    sharedObject = sharedObjectRepository.save(sharedObject);
                } else if (sharedObjects.size() == 0) {
                    sharedObject = new PLMSharedObject();
                    sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                    sharedObject.setSharedTo(person);
                    sharedObject.setObjectId(plmSharedObject.getObjectId());
                    sharedObject.setPermission(plmSharedObject.getPermission());
                    sharedObject.setShareType(plmSharedObject.getShareType());
                    sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                    sharedObject = sharedObjectRepository.save(sharedObject);
                }
            }
            if (sharedObject.getShareType() != null) {
                if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                    sendNotificationToMultiplePerson(objectIds, plmSharedObject, PLMObjectType.ITEM, ShareType.PERSON);
                } else {
                    sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.ITEM, ShareType.GROUP);
                }
            }

        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.MANUFACTURER.toString())) {
            sharedObject = createMfrObject(plmSharedObject);
        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.MANUFACTURERPART.toString())) {
            sharedObject = createMfrPartObject(plmSharedObject);
        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.MFRSUPPLIER.toString())) {
            sharedObject = createSupplierObject(plmSharedObject);
        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.CUSTOMOBJECT.toString())) {
            sharedObject = createCustomObject(plmSharedObject);
        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.DOCUMENT.toString())) {
            sharedObject = createFolderObject(plmSharedObject);
        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.PROJECT.toString())) {
            sharedObject = createProjectObjects(plmSharedObject, PLMObjectType.PROJECT);
        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.PROJECTACTIVITY.toString())) {
            sharedObject = createProjectObjects(plmSharedObject, PLMObjectType.PROJECTACTIVITY);
        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.PROJECTTASK.toString())) {
            sharedObject = createProjectObjects(plmSharedObject, PLMObjectType.PROJECTTASK);
        } else if (plmSharedObject.getSharedObjectType().toString().equals(PLMObjectType.PROGRAM.toString())) {
            sharedObject = createProgramObject(plmSharedObject);
        }
        return sharedObject;
    }

    @Override
    public PLMSharedObject update(PLMSharedObject plmSharedObject) {
        checkNotNull(plmSharedObject);
        checkNotNull(plmSharedObject.getId());
        return sharedObjectRepository.save(plmSharedObject);
    }

    @Override
    public void delete(Integer id) {
        PLMSharedObject object = sharedObjectRepository.findOne(id);
        sharedObjectRepository.delete(object);
    }

    @Override
    public PLMSharedObject get(Integer id) {
        return sharedObjectRepository.findOne(id);
    }

    @Override
    public List<PLMSharedObject> getSharedToList(Integer integer) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        sharedObjectRepository = webApplicationContext.getBean(SharedObjectRepository.class);
        List<PLMSharedObject> sharedObjects = sharedObjectRepository.findBySharedTo(integer);
        return sharedObjects;
    }

    public List<Person> getSharedToPersons() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = sharedObjectRepository.getSharedToIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }


    @Override
    public List<PLMSharedObject> getAll() {
        return sharedObjectRepository.findAll();
    }

    private String getCss(String host) {
        String css = "";
        String[] urls = new String[]{host + "/app/assets/bower_components/bootstrap/dist/css/bootstrap.min.css",
                host + "/app/assets/bower_components/cassini-platform/template/css/bootstrap-override.css",
                host + "/app/assets/bower_components/cassini-platform/template/css/style.default.css",
                host + "/app/assets/bower_components/cassini-platform/template/css/style.katniss.css"};
        try {
            String[] e = urls;
            int var5 = urls.length;
            for (int var6 = 0; var6 < var5; ++var6) {
                String url = e[var6];
                css = css + IOUtils.toString(new URL(url), (Charset) null);
                css = css + "\n\n";
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }
        return css;
    }

    private void sendNotificationToMultiplePerson(List<Integer> objectIds, PLMSharedObject object,
                                                  PLMObjectType objectType, ShareType shareType) {
        String sharedPerson = "";
        if (sessionWrapper != null && sessionWrapper.getSession() != null) {
            sharedPerson = sessionWrapper.getSession().getLogin().getPerson().getFullName();
        }
        String documentName = "";
        String fileNames = "";
        List<CassiniObject> objects = null;
        if (objectType.name().equals("ITEM")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.ITEM.toString()));
        } else if (objectType.name().equals("MANUFACTURER")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.MANUFACTURER.toString()));
        } else if (objectType.name().equals("MANUFACTURERPART")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.MANUFACTURERPART.toString()));
        } else if (objectType.name().equals("MFRSUPPLIER")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.MFRSUPPLIER.toString()));
        } else if (objectType.name().equals("CUSTOMOBJECT")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.CUSTOMOBJECT.toString()));
        } else if (objectType.name().equals("DOCUMENT")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.DOCUMENT.toString()));
            List<PLMDocument> documents = documentRepository.findByIdIn(objectIds);
            for (PLMDocument document : documents) {
                List<PLMDocument> filesList = plmDocumentRepository.findByParentFileAndFileTypeAndLatestTrueOrderByModifiedDateDesc(document.getId(), "FILE");
                if (documentName.equals("")) {
                    documentName = document.getName();
                } else {
                    documentName = documentName + ", " + document.getName();
                }

                for (PLMDocument plmDocument : filesList) {
                    if (fileNames.equals("")) {
                        fileNames = plmDocument.getName();
                    } else {
                        fileNames = fileNames + ", " + plmDocument.getName();
                    }
                }
            }
        } else if (objectType.name().equals("PROJECT")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.PROJECT.toString()));
        } else if (objectType.name().equals("PROJECTACTIVITY")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.PROJECTACTIVITY.toString()));
        } else if (objectType.name().equals("PROJECTTASK")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.PROJECTTASK.toString()));
        } else if (objectType.name().equals("PROGRAM")) {
            objects = objectRepository.findByIdInAndObjectType(objectIds,
                    ObjectType.valueOf(PLMObjectType.PROGRAM.toString()));
        }
        List<Person> toPersons = new ArrayList<>();
        if (shareType.name().equals("PERSON")) {
            object.getSharedToObjects().forEach(personId -> {
                Person person = personRepository.findOne(personId);
                if (isValidMailAddress(person.getEmail())) {
                    toPersons.add(person);
                }
            });
        } else if (shareType.name().equals("GROUP")) {
            PersonGroup group = personGroupRepository.findOne(object.getSharedTo());
            Set<GroupMember> groupMembers = group.getGroupMember();
            for (GroupMember groupMember : groupMembers) {
                if (isValidMailAddress(groupMember.getPerson().getEmail())) {
                    toPersons.add(groupMember.getPerson());
                }
            }
        }
        String[] recipientAddress = new String[toPersons.size()];
        String email = "";
        String sharedToPerson = "";
        if (toPersons.size() > 0) {
            for (int i = 0; i < toPersons.size(); i++) {
                Person person = toPersons.get(i);
                if (email == "") {
                    email = person.getEmail();
                } else {
                    email = email + "," + person.getEmail();
                }
                if (sharedToPerson == "") {
                    sharedToPerson = person.getFullName();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            URL companyLogo = null;
            String templatePath = null;
           /* EmailTemplateConfiguration emailTemplateConfiguration = eMailTemplateConfigRepository
                    .findByTemplateName("shareobjectmail.html");*/
            Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.LOGO");
            if (preference != null) {
                if (preference.getCustomLogo() != null) {
                    URL url1 = ItemService.class
                            .getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                    File file = new File(url1.getPath());
                    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                        outputStream.write(preference.getCustomLogo());
                        /*
                         * model.put("companyLogo",
                         * ItemService.class.getClassLoader().getResource("templates/email/share/" +
                         * "dummy_logo.png"));
                         */
                        companyLogo = ItemService.class.getClassLoader()
                                .getResource("templates/email/share/" + "dummy_logo.png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
           /* if (emailTemplateConfiguration != null) {
                if (emailTemplateConfiguration.getTemplateSourceCode() != null
                        && emailTemplateConfiguration.getTemplateSourceCode() != "") {
                    byte[] data = DatatypeConverter
                            .parseBase64Binary(emailTemplateConfiguration.getTemplateSourceCode());
                    URL url1 = ItemService.class
                            .getClassLoader().getResource("templates/email/share/" + "customTemplate.html");
                    File file = new File(url1.getPath());
                    try {
                        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                        Writer writer = new OutputStreamWriter(outputStream);
                        writer.write(emailTemplateConfiguration.getTemplateSourceCode());
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    templatePath = "email/share/customTemplate.html";
                } else {
                    templatePath = "email/share/shareobjectmail.html";
                }
            } else {
                templatePath = "email/share/shareobjectmail.html";
            }*/
            templatePath = "email/share/shareobjectmail.html";
            final String templatePaths = templatePath;
            final URL companyLogos = companyLogo;
            final List<CassiniObject> objectList = objects;
            final String sharedPersonName = sharedPerson;
            final String sharedToPersonName = sharedToPerson;
            final String documentNameFinal = documentName;
            final String fileNamesFinal = fileNames;
            new Thread(() -> {
                HashMap model = new HashMap();
                model.put("host", host);
                model.put("cssIncludes", this.getCss(host));
                model.put("permission", object.getPermission());
                model.put("companyLogo", companyLogos);
                model.put("items", objectList);
                model.put("isCustomObject", false);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                if (objectType.name().equals("ITEM")) {
                    model.put("isItem", true);
                    model.put("isMfr", false);
                    model.put("isPart", false);
                    model.put("isProject", false);
                    model.put("isProgram", false);
                    model.put("isSupplier", false);
                    model.put("isFolder", false);
                    String mailSubject = setMailSubject(objectType, objectIds);
                    mail.setMailSubject(mailSubject);
                } else if (objectType.name().equals("MANUFACTURER")) {
                    model.put("isItem", false);
                    model.put("isMfr", true);
                    model.put("isPart", false);
                    model.put("isProject", false);
                    model.put("isProgram", false);
                    model.put("isSupplier", false);
                    model.put("isFolder", false);
                    String mailSubject = setMailSubject(objectType, objectIds);
                    mail.setMailSubject(mailSubject);
                } else if (objectType.name().equals("MANUFACTURERPART")) {
                    model.put("isItem", false);
                    model.put("isMfr", false);
                    model.put("isPart", true);
                    model.put("isProject", false);
                    model.put("isProgram", false);
                    model.put("isSupplier", false);
                    model.put("isFolder", false);
                    String mailSubject = setMailSubject(objectType, objectIds);
                    mail.setMailSubject(mailSubject);
                } else if (objectType.name().equals("MFRSUPPLIER")) {
                    model.put("isItem", false);
                    model.put("isMfr", false);
                    model.put("isPart", false);
                    model.put("isSupplier", true);
                    model.put("isProject", false);
                    model.put("isProgram", false);
                    model.put("isFolder", false);
                    String mailSubject = setMailSubject(objectType, objectIds);
                    mail.setMailSubject(mailSubject);
                } else if (objectType.name().equals("CUSTOMOBJECT")) {
                    model.put("isItem", false);
                    model.put("isMfr", false);
                    model.put("isPart", false);
                    model.put("isSupplier", false);
                    model.put("isProject", false);
                    model.put("isProgram", false);
                    model.put("isFolder", false);
                    model.put("isCustomObject", true);
                    String mailSubject = setMailSubject(objectType, objectIds);
                    mail.setMailSubject(mailSubject);
                } else if (objectType.name().equals("DOCUMENT")) {
                    model.put("isItem", false);
                    model.put("isMfr", false);
                    model.put("isPart", false);
                    model.put("isSupplier", false);
                    model.put("isFolder", true);
                    model.put("isProject", false);
                    model.put("isProgram", false);
                    model.put("sharedPerson", sharedPersonName);
                    model.put("documentName", documentNameFinal);
                    model.put("fileNames", fileNamesFinal);
                    if (recipientAddress.length == 1) {
                        model.put("sharedToPerson", sharedToPersonName);
                    } else {
                        model.put("sharedToPerson", "All");
                    }
                    String mailSubject = setMailSubject(objectType, objectIds);
                    mail.setMailSubject(mailSubject);
                } else if (objectType.name().equals("PROJECT") || objectType.name().equals("PROJECTACTIVITY")
                        || objectType.name().equals("PROJECTTASK")) {
                    model.put("isItem", false);
                    model.put("isMfr", false);
                    model.put("isPart", false);
                    model.put("isProject", true);
                    model.put("isProgram", false);
                    model.put("isSupplier", false);
                    model.put("isFolder", false);
                    model.put("sharedBy", personRepository.findOne(object.getSharedBy()).getFullName());
                    String mailSubject = setMailSubject(objectType, objectIds);
                    mail.setMailSubject(mailSubject);
                } else if (objectType.name().equals("PROGRAM")) {
                    model.put("isItem", false);
                    model.put("isMfr", false);
                    model.put("isPart", false);
                    model.put("isProject", false);
                    model.put("isProgram", true);
                    model.put("isSupplier", false);
                    model.put("isFolder", false);
                    model.put("sharedBy", personRepository.findOne(object.getSharedBy()).getFullName());
                    String mailSubject = setMailSubject(objectType, objectIds);
                    mail.setMailSubject(mailSubject);
                }
                mail.setModel(model);
                mail.setTemplatePath(templatePaths);
                mailService.sendEmail(mail);
            }).start();

        }
    }

    private String setMailSubject(PLMObjectType objectType, List<Integer> objectIds) {
        String mailSubject = null;
        List<String> itemName = new ArrayList<>();
        List<String> itemNumber = new ArrayList<>();
        if (objectType.name().equals("ITEM")) {
            List<PLMItem> items = itemRepository.findByIdIn(objectIds);
            items.forEach(i -> {
                itemName.add(i.getItemName());
                itemNumber.add(i.getItemNumber());
            });
            mailSubject = "CassiniPLM - Item Object Shared" + itemNumber + " - " + itemName;
        } else if (objectType.name().equals("MANUFACTURER")) {
            List<PLMManufacturer> manufacturers = manufacturerRepository.findByIdIn(objectIds);
            manufacturers.forEach(i -> {
                itemName.add(i.getName());
            });
            mailSubject = "CassiniPLM - Manufacturer Object Shared" + itemName;
        } else if (objectType.name().equals("MANUFACTURERPART")) {
            List<PLMManufacturerPart> parts = manufacturerPartRepository.findByIdIn(objectIds);
            parts.forEach(i -> {
                itemNumber.add(i.getPartNumber());
                itemName.add(i.getPartName());
            });
            mailSubject = "CassiniPLM - Manufacturer Part Object Shared" + itemNumber + " - " + itemName;
        } else if (objectType.name().equals("PROJECT")) {
            List<PLMProject> parts = projectRepository.findByIdIn(objectIds);
            parts.forEach(i -> {
                itemNumber.add(i.getName());
            });
            mailSubject = "CassiniPLM - Project Object Shared" + itemNumber;
        } else if (objectType.name().equals("MFRSUPPLIER")) {
            List<PLMSupplier> suppliers = supplierRepository.findByIdIn(objectIds);
            suppliers.forEach(i -> {
                itemNumber.add(i.getName());
            });
            mailSubject = "CassiniPLM - Project Object Shared" + itemNumber;
        } else if (objectType.name().equals("CUSTOMOBJECT")) {
            List<CustomObject> suppliers = customObjectRepository.findByIdIn(objectIds);
            suppliers.forEach(i -> {
                itemNumber.add(i.getName());
            });
            mailSubject = "CassiniPLM - Project Object Shared" + itemNumber;
        } else if (objectType.name().equals("DOCUMENT")) {
            List<PLMDocument> parts = documentRepository.findByIdIn(objectIds);
            parts.forEach(i -> {
                itemNumber.add(i.getName());
            });
            mailSubject = "Documents Shared Notification";
        } else if (objectType.name().equals("PROJECTACTIVITY")) {
            List<PLMActivity> parts = plmActivityRepository.findByIdIn(objectIds);
            parts.forEach(i -> {
                itemNumber.add(i.getName());
            });
            mailSubject = "CassiniPLM - Project Activity Object Shared" + itemNumber;
        } else if (objectType.name().equals("PROJECTTASK")) {
            List<PLMTask> parts = taskRepository.findByIdIn(objectIds);
            parts.forEach(i -> {
                itemNumber.add(i.getName());
            });
            mailSubject = "CassiniPLM - Project Task Object Shared" + itemNumber;
        } else if (objectType.name().equals("PROGRAM")) {
            List<PLMProgram> parts = programRepository.findByIdIn(objectIds);
            parts.forEach(i -> {
                itemNumber.add(i.getName());
            });
            mailSubject = "CassiniPLM - Program Task Object Shared" + itemNumber;
        }
        return mailSubject;
    }

    private Boolean isValidMailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public List<PLMSharedObject> saveMultipleSharedObjects(List<PLMItem> plmItems, PLMSharedObject sharedObject) {
        String items = null;
        PLMItemType itemType = null;
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        List<Integer> objectIds = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        for (PLMItem plmItem : plmItems) {
            itemType = plmItem.getItemType();
            if (items == null) {
                items = plmItem.getItemNumber() + "[" + plmItem.getItemName() + "]";
            } else {
                items = items + " ," + plmItem.getItemNumber() + "[" + plmItem.getItemName() + "]";
            }
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository
                        .findByObjectIdAndSharedTo(plmItem.getId(), person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(plmItem.getId());
                    object.setSharedObjectType(ObjectType.valueOf(plmItem.getObjectType().toString()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                }
            }
            if (object.isAdded())
                newItems.add(plmItem.getItemName());
            else
                existingItems.add(plmItem.getItemName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);
            objectIds.add(plmItem.getId());
        }
        /*
         * if (sharedObjects.size() > 0) {
         * if (sharedObject.getShareType().equals(ShareType.PERSON)) {
         * sendEmailToPerson(plmItems, sharedObject);
         * } else {
         * sendItemsShareEmailToGroup(plmItems, sharedObject);
         * }
         * 
         * }
         */
        if (sharedObjects.size() > 0) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.ITEM, ShareType.PERSON);
            } else {
                if (object.isAdded())
                    sendNotificationToMultiplePerson(objectIds, object, PLMObjectType.ITEM, ShareType.GROUP);
            }
        }
        return sharedObjects;
    }

    public List<PLMSharedObject> getSharedObjectsByPersonAndItem(Integer objectId, Integer sharedTo) {
        return sharedObjectRepository.findByObjectIdAndSharedTo(objectId, sharedTo);
    }

    public List<PLMSharedObject> getSharedObjectsByItemsAndPerson(Integer[] objectIds, Integer[] personId) {
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        Integer sharedTo = personId[0];
        for (Integer objectId : objectIds) {
            List<PLMSharedObject> sharedObject = sharedObjectRepository.findByObjectIdAndSharedTo(objectId, sharedTo);
            if (sharedObject != null) {
                sharedObjects.addAll(sharedObject);
            }
        }
        return sharedObjects;
    }

    public List<PLMSharedObject> getSharedObjectByItem(Integer objectId) {
        List<PLMSharedObject> plmSharedObjects = sharedObjectRepository.findByObjectId(objectId);
        for (PLMSharedObject sharedObject : plmSharedObjects) {
            sharedObject.setUserId(sharedObject.getSharedTo());
        }
        return plmSharedObjects;
    }

    public List<PLMSharedObject> getSharedProjectsBySharedTo(List<Integer> personIds) {
        List<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("PROJECT"), personIds);
        return sharedObjects;
    }

    public List<PLMSharedObject> getSharedProgramsBySharedTo(List<Integer> personIds) {
        List<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("PROGRAM"), personIds);
        return sharedObjects;
    }

    public Page<SharedItemObjectDto> getSharedItemsBySharedPerson(Integer sharedBy, Pageable pageable) {
        List<SharedItemObjectDto> sharedItemObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf("ITEM"), sharedBy, pageable);
        sharedItemObjectDtos = getItemsShareObjects(sharedObjects, sharedItemObjectDtos);
        return new PageImpl<SharedItemObjectDto>(sharedItemObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedItemObjectDto> getItemsBySharedToPerson(Integer sharedTo, Pageable pageable) {
        List<SharedItemObjectDto> sharedItemObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf("ITEM"), sharedTo, pageable);
        sharedItemObjectDtos = getItemsShareObjects(sharedObjects, sharedItemObjectDtos);
        return new PageImpl<SharedItemObjectDto>(sharedItemObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedItemObjectDto> getItemsByExternalGroup(Integer group, Pageable pageable) {
        List<SharedItemObjectDto> sharedItemObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf("ITEM"), group, pageable);
        sharedItemObjectDtos = getItemsShareObjects(sharedObjects, sharedItemObjectDtos);
        return new PageImpl<SharedItemObjectDto>(sharedItemObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedItemObjectDto> getSharedItemsBySharedByAndSharedToPerson(Integer sharedBy, Integer sharedTo,
                                                                               Pageable pageable) {
        List<SharedItemObjectDto> sharedItemObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedByAndSharedTo(ObjectType.valueOf("ITEM"), sharedBy, sharedTo, pageable);
        sharedItemObjectDtos = getItemsShareObjects(sharedObjects, sharedItemObjectDtos);
        return new PageImpl<SharedItemObjectDto>(sharedItemObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedItemObjectDto> getSharedItemsBySharedToPerson(List<Integer> sharedTo, Pageable pageable) {
        List<SharedItemObjectDto> sharedItemObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("ITEM"), sharedTo, pageable);
        sharedItemObjectDtos = getItemsShareObjects(sharedObjects, sharedItemObjectDtos);
        return new PageImpl<SharedItemObjectDto>(sharedItemObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<SharedItemObjectDto> findAllSharedObjects(Pageable pageable) {
        List<SharedItemObjectDto> sharedItemObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf("ITEM"),
                pageable);
        sharedItemObjectDtos = getItemsShareObjects(sharedObjects, sharedItemObjectDtos);
        return new PageImpl<SharedItemObjectDto>(sharedItemObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public List<PLMSharedObject> saveMultipleMfrSharedObjects(List<PLMManufacturer> manufacturers,
                                                              PLMSharedObject sharedObject) {
        String mfrs = null;
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        List<Integer> objectIds = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        for (PLMManufacturer plmManufacturer : manufacturers) {
            if (mfrs == null) {
                mfrs = plmManufacturer.getName();
            } else {
                mfrs = mfrs + " ," + plmManufacturer.getName();
            }
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository
                        .findByObjectIdAndSharedTo(plmManufacturer.getId(), person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(plmManufacturer.getId());
                    object.setSharedObjectType(ObjectType.valueOf(plmManufacturer.getObjectType().toString()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                }
            }
            if (object.isAdded())
                newItems.add(plmManufacturer.getName());
            else
                existingItems.add(plmManufacturer.getName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);
            objectIds.add(plmManufacturer.getId());
        }
        if (sharedObjects.size() > 0) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.MANUFACTURER, ShareType.PERSON);
            } else {
                if (object.isAdded())
                    sendNotificationToMultiplePerson(objectIds, object, PLMObjectType.MANUFACTURER, ShareType.GROUP);
            }
        }
        return sharedObjects;
    }

    public List<PLMSharedObject> saveMultipleMfrPartsSharedObjects(List<PLMManufacturerPart> parts,
                                                                   PLMSharedObject sharedObject) {
        String mfrParts = null;
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        List<Integer> objectIds = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        for (PLMManufacturerPart part : parts) {
            if (mfrParts == null) {
                mfrParts = part.getPartNumber() + "[" + part.getPartName() + "]";
            } else {
                mfrParts = mfrParts + " ," + part.getPartName() + "[" + part.getPartName() + "]";
            }
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository.findByObjectIdAndSharedTo(part.getId(),
                        person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(part.getId());
                    object.setSharedObjectType(ObjectType.valueOf(part.getObjectType().toString()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                }
            }
            if (object.isAdded())
                newItems.add(part.getPartName());
            else
                existingItems.add(part.getPartName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);
            objectIds.add(part.getId());
        }
        if (sharedObjects.size() > 0) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.MANUFACTURERPART,
                        ShareType.PERSON);
            } else {
                if (object.isAdded())
                    sendNotificationToMultiplePerson(objectIds, object, PLMObjectType.MANUFACTURERPART,
                            ShareType.GROUP);
            }
        }
        return sharedObjects;
    }

    public List<PLMSharedObject> saveMultipleSuppliersSharedObjects(List<PLMSupplier> parts,
                                                                    PLMSharedObject sharedObject) {
        String suppliers = null;
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        List<Integer> objectIds = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        for (PLMSupplier part : parts) {
            if (suppliers == null) {
                suppliers = "[" + part.getName() + "]";
            } else {
                suppliers = suppliers + " ," + "[" + part.getName() + "]";
            }
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository.findByObjectIdAndSharedTo(part.getId(),
                        person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(part.getId());
                    object.setSharedObjectType(ObjectType.valueOf(part.getObjectType().toString()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                }
            }
            if (object.isAdded())
                newItems.add(part.getName());
            else
                existingItems.add(part.getName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);
            objectIds.add(part.getId());
        }
        if (sharedObjects.size() > 0) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.MFRSUPPLIER,
                        ShareType.PERSON);
            } else {
                if (object.isAdded())
                    sendNotificationToMultiplePerson(objectIds, object, PLMObjectType.MFRSUPPLIER,
                            ShareType.GROUP);
            }
        }
        return sharedObjects;
    }


    public List<PLMSharedObject> saveMultipleCustomObjectsSharedObjects(List<CustomObject> customObjects,
                                                                        PLMSharedObject sharedObject) {
        String suppliers = null;
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        List<Integer> objectIds = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        for (CustomObject part : customObjects) {
            if (suppliers == null) {
                suppliers = "[" + part.getName() + "]";
            } else {
                suppliers = suppliers + " ," + "[" + part.getName() + "]";
            }
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository.findByObjectIdAndSharedTo(part.getId(),
                        person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(part.getId());
                    object.setSharedObjectType(ObjectType.valueOf(part.getObjectType().toString()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                }
            }
            if (object.isAdded())
                newItems.add(part.getName());
            else
                existingItems.add(part.getName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);
            objectIds.add(part.getId());
        }
        if (sharedObjects.size() > 0) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.CUSTOMOBJECT,
                        ShareType.PERSON);
            } else {
                if (object.isAdded())
                    sendNotificationToMultiplePerson(objectIds, object, PLMObjectType.CUSTOMOBJECT,
                            ShareType.GROUP);
            }
        }
        return sharedObjects;
    }

    public List<PLMSharedObject> saveMultipleFoldersSharedObjects(List<PLMDocument> documents,
                                                                  PLMSharedObject sharedObject) {
        String folders = null;
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        List<Integer> objectIds = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        for (PLMDocument part : documents) {
            if (folders == null) {
                folders = "[" + part.getName() + "]";
            } else {
                folders = folders + " ," + "[" + part.getName() + "]";
            }
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository.findByObjectIdAndSharedTo(part.getId(),
                        person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(part.getId());
                    object.setSharedObjectType(ObjectType.valueOf(part.getObjectType().toString()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                }
            }
            if (object.isAdded())
                newItems.add(part.getName());
            else
                existingItems.add(part.getName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);
            objectIds.add(part.getId());
        }
        if (sharedObjects.size() > 0) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.DOCUMENT,
                        ShareType.PERSON);
            } else {
                if (object.isAdded())
                    sendNotificationToMultiplePerson(objectIds, object, PLMObjectType.DOCUMENT,
                            ShareType.GROUP);
            }
        }
        return sharedObjects;
    }

    public List<PLMSharedObject> saveMultipleProjectSharedObjects(
            List<SharedMultipleProjectObjectsDto> sharedMultipleProjectObjectsDtos, PLMSharedObject sharedObject) {
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        List<Integer> activityIds = new ArrayList<>();
        List<Integer> taskIds = new ArrayList<>();
        for (SharedMultipleProjectObjectsDto projectObjectsDto : sharedMultipleProjectObjectsDtos) {
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository
                        .findByObjectIdAndSharedTo(projectObjectsDto.getId(), person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(projectObjectsDto.getId());
                    object.setSharedObjectType(ObjectType.valueOf(projectObjectsDto.getObjectType()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                } else {
                    for (PLMSharedObject plmSharedObject : plmSharedObjects) {
                        if (!sharedObject.getPermission().equals(plmSharedObject.getPermission())) {
                            plmSharedObject.setPermission(sharedObject.getPermission());
                            sharedObjectRepository.save(plmSharedObject);
                        }
                    }
                }
            }
            if (object.isAdded())
                newItems.add(projectObjectsDto.getName());
            else
                existingItems.add(projectObjectsDto.getName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);
            if (projectObjectsDto.getObjectType().equals(PLMObjectType.PROJECTACTIVITY.name()))
                activityIds.add(projectObjectsDto.getId());
            if (projectObjectsDto.getObjectType().equals(PLMObjectType.PROJECTTASK.name()))
                taskIds.add(projectObjectsDto.getId());
        }
        if (sharedObjects.size() > 0) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                if (activityIds.size() > 0)
                    sendNotificationToMultiplePerson(activityIds, sharedObject, PLMObjectType.PROJECTACTIVITY,
                            ShareType.PERSON);
                if (taskIds.size() > 0)
                    sendNotificationToMultiplePerson(taskIds, sharedObject, PLMObjectType.PROJECTTASK,
                            ShareType.PERSON);
            } else {
                if (object.isAdded())
                    if (activityIds.size() > 0)
                        sendNotificationToMultiplePerson(activityIds, object, PLMObjectType.PROJECTACTIVITY,
                                ShareType.GROUP);
                if (taskIds.size() > 0)
                    sendNotificationToMultiplePerson(taskIds, object, PLMObjectType.PROJECTTASK, ShareType.GROUP);
            }
        }
        return sharedObjects;
    }

    public List<PLMSharedObject> saveMultipleProgramSharedObjects(
            List<SharedMultipleProjectObjectsDto> sharedMultipleProjectObjectsDtos, PLMSharedObject sharedObject) {
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        List<Integer> activityIds = new ArrayList<>();
        List<Integer> taskIds = new ArrayList<>();
        for (SharedMultipleProjectObjectsDto projectObjectsDto : sharedMultipleProjectObjectsDtos) {
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository
                        .findByObjectIdAndSharedTo(projectObjectsDto.getId(), person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(projectObjectsDto.getId());
                    object.setSharedObjectType(ObjectType.valueOf(projectObjectsDto.getObjectType()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                } else {
                    for (PLMSharedObject plmSharedObject : plmSharedObjects) {
                        if (!sharedObject.getPermission().equals(plmSharedObject.getPermission())) {
                            plmSharedObject.setPermission(sharedObject.getPermission());
                            sharedObjectRepository.save(plmSharedObject);
                        }
                    }
                }
            }
            if (object.isAdded())
                newItems.add(projectObjectsDto.getName());
            else
                existingItems.add(projectObjectsDto.getName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);

        }

        return sharedObjects;
    }

    @Transactional
    private PLMSharedObject createMfrObject(PLMSharedObject plmSharedObject) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        PLMManufacturer manufacturer = manufacturerRepository.findOne(plmSharedObject.getObjectId());
        List<PLMManufacturer> manufacturers = new ArrayList();
        manufacturers.add(manufacturer);
        objectIds.add(manufacturer.getId());
        List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
        for (Integer person : sharedToObjects) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                    plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
            List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                    .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
            if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                sharedObject = sharedObjects1.get(0);
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject = sharedObjectRepository.save(sharedObject);
            } else if (sharedObjects.size() == 0) {
                sharedObject = new PLMSharedObject();
                sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                sharedObject.setSharedTo(person);
                sharedObject.setObjectId(plmSharedObject.getObjectId());
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject.setShareType(plmSharedObject.getShareType());
                sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                sharedObject = sharedObjectRepository.save(sharedObject);
            }
        }
        if (sharedObject.getShareType() != null) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, plmSharedObject, PLMObjectType.MANUFACTURER,
                        ShareType.PERSON);
            } else {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.MANUFACTURER, ShareType.GROUP);
            }
        }
        return sharedObject;
    }

    @Transactional
    private PLMSharedObject createProgramObject(PLMSharedObject plmSharedObject) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        PLMProgram part = programRepository.findOne(plmSharedObject.getObjectId());
        List<PLMProgram> manufacturerParts = new ArrayList();
        manufacturerParts.add(part);
        objectIds.add(part.getId());
        List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
        for (Integer person : sharedToObjects) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                    plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
            List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                    .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
            if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                sharedObject = sharedObjects1.get(0);
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject = sharedObjectRepository.save(sharedObject);
            } else if (sharedObjects.size() == 0) {
                sharedObject = new PLMSharedObject();
                sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                sharedObject.setSharedTo(person);
                sharedObject.setObjectId(plmSharedObject.getObjectId());
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject.setShareType(plmSharedObject.getShareType());
                sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                sharedObject = sharedObjectRepository.save(sharedObject);
            }
        }
        if (sharedObject.getShareType() != null) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, plmSharedObject, PLMObjectType.PROGRAM,
                        ShareType.PERSON);
            } else {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.PROGRAM,
                        ShareType.GROUP);
            }
        }
        return sharedObject;
    }

    @Transactional
    private PLMSharedObject createMfrPartObject(PLMSharedObject plmSharedObject) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        PLMManufacturerPart part = manufacturerPartRepository.findOne(plmSharedObject.getObjectId());
        List<PLMManufacturerPart> manufacturerParts = new ArrayList();
        manufacturerParts.add(part);
        objectIds.add(part.getId());
        List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
        for (Integer person : sharedToObjects) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                    plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
            List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                    .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
            if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                sharedObject = sharedObjects1.get(0);
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject = sharedObjectRepository.save(sharedObject);
            } else if (sharedObjects.size() == 0) {
                sharedObject = new PLMSharedObject();
                sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                sharedObject.setSharedTo(person);
                sharedObject.setObjectId(plmSharedObject.getObjectId());
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject.setShareType(plmSharedObject.getShareType());
                sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                sharedObject = sharedObjectRepository.save(sharedObject);
            }
        }
        if (sharedObject.getShareType() != null) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, plmSharedObject, PLMObjectType.MANUFACTURERPART,
                        ShareType.PERSON);
            } else {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.MANUFACTURERPART,
                        ShareType.GROUP);
            }
        }
        return sharedObject;
    }

    @Transactional
    private PLMSharedObject createSupplierObject(PLMSharedObject plmSharedObject) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        PLMSupplier supplier = supplierRepository.findOne(plmSharedObject.getObjectId());
        List<PLMSupplier> suppliers = new ArrayList();
        suppliers.add(supplier);
        objectIds.add(supplier.getId());
        List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
        for (Integer person : sharedToObjects) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                    plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
            List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                    .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
            if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                sharedObject = sharedObjects1.get(0);
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject = sharedObjectRepository.save(sharedObject);
            } else if (sharedObjects.size() == 0) {
                sharedObject = new PLMSharedObject();
                sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                sharedObject.setSharedTo(person);
                sharedObject.setObjectId(plmSharedObject.getObjectId());
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject.setShareType(plmSharedObject.getShareType());
                sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                sharedObject = sharedObjectRepository.save(sharedObject);
            }
        }
        if (sharedObject.getShareType() != null) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, plmSharedObject, PLMObjectType.MFRSUPPLIER,
                        ShareType.PERSON);
            } else {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.MFRSUPPLIER,
                        ShareType.GROUP);
            }
        }
        return sharedObject;
    }


    @Transactional
    private PLMSharedObject createCustomObject(PLMSharedObject plmSharedObject) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        CustomObject customObject = customObjectRepository.findOne(plmSharedObject.getObjectId());
        List<CustomObject> customObjects = new ArrayList();
        customObjects.add(customObject);
        objectIds.add(customObject.getId());
        List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
        for (Integer person : sharedToObjects) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                    plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
            List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                    .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
            if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                sharedObject = sharedObjects1.get(0);
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject = sharedObjectRepository.save(sharedObject);
            } else if (sharedObjects.size() == 0) {
                sharedObject = new PLMSharedObject();
                sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                sharedObject.setSharedTo(person);
                sharedObject.setObjectId(plmSharedObject.getObjectId());
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject.setShareType(plmSharedObject.getShareType());
                sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                sharedObject = sharedObjectRepository.save(sharedObject);
            }
        }
        if (sharedObject.getShareType() != null) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, plmSharedObject, PLMObjectType.CUSTOMOBJECT,
                        ShareType.PERSON);
            } else {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.CUSTOMOBJECT,
                        ShareType.GROUP);
            }
        }
        return sharedObject;
    }


    @Transactional
    private PLMSharedObject createProjectObjects(PLMSharedObject plmSharedObject, PLMObjectType objectType) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        if (objectType.equals(PLMObjectType.PROJECT)) {
            PLMProject project = projectRepository.findOne(plmSharedObject.getObjectId());
            List<PLMProject> projects = new ArrayList();
            projects.add(project);
            objectIds.add(project.getId());
            // OverrideProjectPermission(project, plmSharedObject);
        } else if (objectType.equals(PLMObjectType.PROJECTACTIVITY)) {
            PLMActivity part = plmActivityRepository.findOne(plmSharedObject.getObjectId());
            List<PLMActivity> projects = new ArrayList();
            projects.add(part);
            objectIds.add(part.getId());
        } else if (objectType.equals(PLMObjectType.PROJECTTASK)) {
            PLMTask part = taskRepository.findOne(plmSharedObject.getObjectId());
            List<PLMTask> projects = new ArrayList();
            projects.add(part);
            objectIds.add(part.getId());
        }
        List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
        for (Integer person : sharedToObjects) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                    plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
            List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                    .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
            if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                sharedObject = sharedObjects1.get(0);
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject = sharedObjectRepository.save(sharedObject);
            } else if (sharedObjects.size() == 0) {
                sharedObject = new PLMSharedObject();
                sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                sharedObject.setSharedTo(person);
                sharedObject.setObjectId(plmSharedObject.getObjectId());
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject.setShareType(plmSharedObject.getShareType());
                sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                sharedObject = sharedObjectRepository.save(sharedObject);
            }
        }
        if (sharedObject.getShareType() != null) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, plmSharedObject, objectType, ShareType.PERSON);
            } else {
                sendNotificationToMultiplePerson(objectIds, sharedObject, objectType, ShareType.GROUP);
            }
        }
        return sharedObject;
    }

    @Transactional
    public PLMSharedObject createDeclarationObject(PLMSharedObject plmSharedObject) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        PGCDeclaration declaration = declarationRepository.findOne(plmSharedObject.getObjectId());
        List<PGCDeclaration> declarations = new ArrayList();
        declarations.add(declaration);
        objectIds.add(declaration.getId());
        List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
        for (Integer person : sharedToObjects) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                    plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
            List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                    .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
            if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                sharedObject = sharedObjects1.get(0);
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject = sharedObjectRepository.save(sharedObject);
            } else if (sharedObjects.size() == 0) {
                sharedObject = new PLMSharedObject();
                sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                sharedObject.setSharedTo(person);
                sharedObject.setObjectId(plmSharedObject.getObjectId());
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject.setShareType(plmSharedObject.getShareType());
                sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                sharedObject = sharedObjectRepository.save(sharedObject);
            }
        }
        /*
         * if (sharedObject.getShareType() != null) {
         * if (sharedObject.getShareType().equals(ShareType.PERSON)) {
         * sendNotificationToMultiplePerson(objectIds, plmSharedObject,
         * PLMObjectType.MANUFACTURERPART, ShareType.PERSON);
         * } else {
         * sendNotificationToMultiplePerson(objectIds, sharedObject,
         * PLMObjectType.MANUFACTURERPART, ShareType.GROUP);
         * }
         * }
         */
        return sharedObject;
    }

    @Transactional
    public Page<SharedMfrPartObjectDto> getSharedMfrsByPerson(SharedObjectCriteria objectCriteria, Pageable pageable) {

        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);

        List<SharedMfrPartObjectDto> sharedMfrObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findAll(predicate, pageable);

        sharedMfrObjectDtos = getMfrShareObjects(sharedObjects, sharedMfrObjectDtos, PLMObjectType.MANUFACTURER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedMfrObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    @Transactional
    public Page<SharedMfrPartObjectDto> getAllSharedMfrObjects(Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedMfrObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = null;
        sharedObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf("MANUFACTURER"), pageable);
        sharedMfrObjectDtos = getMfrShareObjects(sharedObjects, sharedMfrObjectDtos, PLMObjectType.MANUFACTURER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedMfrObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    @Transactional
    public Page<SharedMfrPartObjectDto> getAllSharedProgramsObjects(Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedMfrObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = null;
        sharedObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf("PROGRAM"), pageable);
        sharedMfrObjectDtos = getMfrShareObjects(sharedObjects, sharedMfrObjectDtos, PLMObjectType.PROGRAM);
        return new PageImpl<SharedMfrPartObjectDto>(sharedMfrObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    /* Shared Items */
    public Page<SharedItemObjectDto> getSharedItemsBySharedTo(SharedObjectCriteria objectCriteria, Pageable pageable) {
        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
        List<SharedItemObjectDto> sharedItemObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findAll(predicate, pageable);
        sharedItemObjectDtos = getItemsShareObjects(sharedObjects, sharedItemObjectDtos);
        return new PageImpl<SharedItemObjectDto>(sharedItemObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    private List<SharedItemObjectDto> getItemsShareObjects(Page<PLMSharedObject> sharedObjects,
                                                           List<SharedItemObjectDto> sharedPartObjectDtos) {
        for (PLMSharedObject sharedObject : sharedObjects.getContent()) {
            SharedItemObjectDto sharedMfrPartObjectDto = new SharedItemObjectDto();
            PLMItem item = itemRepository.findOne(sharedObject.getObjectId());
            PLMItemRevision revision = itemRevisionRepository.findOne(item.getLatestRevision());
            sharedMfrPartObjectDto.setId(item.getId());
            sharedMfrPartObjectDto.setName(item.getItemName());
            sharedMfrPartObjectDto.setNumber(item.getItemNumber());
            sharedMfrPartObjectDto.setDescription(item.getDescription());
            sharedMfrPartObjectDto.setType(item.getItemType().getName());
            sharedMfrPartObjectDto.setObjectType(item.getObjectType());
            sharedMfrPartObjectDto.setRevision(revision.getRevision());
            sharedMfrPartObjectDto.setLifeCyclePhase(revision.getLifeCyclePhase());
            sharedMfrPartObjectDto.setItemClass(item.getItemType().getItemClass());
            sharedMfrPartObjectDto.setPermission(sharedObject.getPermission());
            sharedMfrPartObjectDto.setShareType(sharedObject.getShareType());
            sharedMfrPartObjectDto.setReleaseDate(revision.getReleasedDate());
            sharedMfrPartObjectDto.setLatestRevision(item.getLatestRevision());
            sharedMfrPartObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
            if (sharedObject.getShareType().name().equals("PERSON")) {
                sharedMfrPartObjectDto.setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
            } else {
                sharedMfrPartObjectDto.setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
            }
            sharedMfrPartObjectDto.setSharedOn(sharedObject.getCreatedDate());
            sharedPartObjectDtos.add(sharedMfrPartObjectDto);
        }
        return sharedPartObjectDtos;

    }

    @Transactional
    public Page<SharedMfrPartObjectDto> getSharedMfrPartsByPerson(SharedObjectCriteria objectCriteria,
                                                                  Pageable pageable) {
        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedPartObjects = sharedObjectRepository.findAll(predicate, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedPartObjects, sharedPartObjectDtos,
                PLMObjectType.MANUFACTURERPART);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable,
                sharedPartObjects.getTotalElements());
    }

    @Transactional
    public Page<SharedMfrPartObjectDto> getSharedSuppliersByPerson(SharedObjectCriteria objectCriteria,
                                                                   Pageable pageable) {
        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
        List<SharedMfrPartObjectDto> sharedSupplierObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedSupplierObjects = sharedObjectRepository.findAll(predicate, pageable);
        sharedSupplierObjectDtos = getMfrShareObjects(sharedSupplierObjects, sharedSupplierObjectDtos,
                PLMObjectType.MFRSUPPLIER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedSupplierObjectDtos, pageable,
                sharedSupplierObjects.getTotalElements());
    }


    @Transactional
    public Page<SharedMfrPartObjectDto> getSharedCustomObjectsByPerson(SharedObjectCriteria objectCriteria,
                                                                       Pageable pageable) {
        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
        List<SharedMfrPartObjectDto> sharedSupplierObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedSupplierObjects = sharedObjectRepository.findAll(predicate, pageable);
        sharedSupplierObjectDtos = getMfrShareObjects(sharedSupplierObjects, sharedSupplierObjectDtos,
                PLMObjectType.CUSTOMOBJECT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedSupplierObjectDtos, pageable,
                sharedSupplierObjects.getTotalElements());
    }


    @Transactional
    public Page<SharedMfrPartObjectDto> getAllSharedMfrPartObjects(Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedPartObjects = null;
        sharedPartObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf("MANUFACTURERPART"),
                pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedPartObjects, sharedPartObjectDtos,
                PLMObjectType.MANUFACTURERPART);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable,
                sharedPartObjects.getTotalElements());
    }


    @Transactional
    public Page<SharedMfrPartObjectDto> getAllSharedSupplierObjects(Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedPartObjects = null;
        sharedPartObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf("MFRSUPPLIER"),
                pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedPartObjects, sharedPartObjectDtos,
                PLMObjectType.MFRSUPPLIER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable,
                sharedPartObjects.getTotalElements());
    }


    @Transactional
    public Page<SharedMfrPartObjectDto> getAllSharedCustomObjects(Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedPartObjects = null;
        sharedPartObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf("CUSTOMOBJECT"),
                pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedPartObjects, sharedPartObjectDtos,
                PLMObjectType.CUSTOMOBJECT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable,
                sharedPartObjects.getTotalElements());
    }

    @Transactional
    public Page<SharedFolderObjectDto> getAllSharedFolderObjects(Pageable pageable) {
        List<SharedFolderObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedPartObjects = null;
        sharedPartObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf("DOCUMENT"),
                pageable);
        sharedPartObjectDtos = getFolderShareObjects(sharedPartObjects, sharedPartObjectDtos,
                PLMObjectType.DOCUMENT);
        return new PageImpl<SharedFolderObjectDto>(sharedPartObjectDtos, pageable,
                sharedPartObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedMfrsBySharedPerson(Integer sharedBy, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf("MANUFACTURER"), sharedBy, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MANUFACTURER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedMfrsBySharedByAndSharedToPerson(Integer sharedBy, Integer sharedTo,
                                                                                 Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findBySharedObjectTypeAndSharedByAndSharedTo(
                ObjectType.valueOf("MANUFACTURER"), sharedBy, sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MANUFACTURER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedMfrPartsBySharedPerson(Integer sharedBy, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf("MANUFACTURERPART"), sharedBy, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MANUFACTURERPART);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getMfrPartsBySharedTo(Integer sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf("MANUFACTURERPART"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MANUFACTURERPART);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedMfrPartsBySharedByAndSharedToPerson(Integer sharedBy, Integer sharedTo,
                                                                                     Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findBySharedObjectTypeAndSharedByAndSharedTo(
                ObjectType.valueOf("MANUFACTURERPART"), sharedBy, sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MANUFACTURERPART);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedMfrPartsBySharedToPerson(List<Integer> sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("MANUFACTURERPART"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MANUFACTURERPART);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    /*
     * Share Suppliers Filter
     */

    public Page<SharedMfrPartObjectDto> getSharedSuppliersBySharedPerson(Integer sharedBy, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf("MFRSUPPLIER"), sharedBy, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MFRSUPPLIER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSuppliersBySharedTo(Integer sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf("MFRSUPPLIER"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MFRSUPPLIER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedSuppliersBySharedByAndSharedToPerson(Integer sharedBy,
                                                                                      Integer sharedTo,
                                                                                      Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findBySharedObjectTypeAndSharedByAndSharedTo(
                ObjectType.valueOf("MFRSUPPLIER"), sharedBy, sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MFRSUPPLIER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSuppliersBySharedToPerson(List<Integer> sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("MFRSUPPLIER"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MFRSUPPLIER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }


     /*
     * Share Custom Object Filter
     */

    public Page<SharedMfrPartObjectDto> getSharedCustomObjectsBySharedPerson(Integer sharedBy, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf("CUSTOMOBJECT"), sharedBy, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.CUSTOMOBJECT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getCustomObjectsBySharedTo(Integer sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf("CUSTOMOBJECT"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.CUSTOMOBJECT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedCustomObjectsBySharedByAndSharedToPerson(Integer sharedBy,
                                                                                          Integer sharedTo,
                                                                                          Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findBySharedObjectTypeAndSharedByAndSharedTo(
                ObjectType.valueOf("CUSTOMOBJECT"), sharedBy, sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.CUSTOMOBJECT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getCustomObjectsBySharedToPerson(List<Integer> sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("CUSTOMOBJECT"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.CUSTOMOBJECT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    /*
     * Share Folder Filter
     */

    public Page<SharedMfrPartObjectDto> getSharedFoldersBySharedPerson(Integer sharedBy, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf("DOCUMENT"), sharedBy, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.DOCUMENT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getFoldersBySharedTo(Integer sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf("DOCUMENT"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.DOCUMENT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedFoldersBySharedByAndSharedToPerson(Integer sharedBy, Integer sharedTo,
                                                                                    Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findBySharedObjectTypeAndSharedByAndSharedTo(
                ObjectType.valueOf("DOCUMENT"), sharedBy, sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.DOCUMENT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getFoldersBySharedToPerson(List<Integer> sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("DOCUMENT"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.DOCUMENT);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedMfrsBySharedToPerson(List<Integer> sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("MANUFACTURER"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.MANUFACTURER);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    private List<SharedMfrPartObjectDto> getMfrShareObjects(Page<PLMSharedObject> sharedObjects,
                                                            List<SharedMfrPartObjectDto> sharedPartObjectDtos, PLMObjectType objectType) {
        for (PLMSharedObject sharedObject : sharedObjects.getContent()) {
            SharedMfrPartObjectDto sharedMfrPartObjectDto = new SharedMfrPartObjectDto();
            if (objectType.name().equals("MANUFACTURER")) {
                PLMManufacturer manufacturer = manufacturerRepository.findOne(sharedObject.getObjectId());
                sharedMfrPartObjectDto.setId(manufacturer.getId());
                sharedMfrPartObjectDto.setName(manufacturer.getName());
                sharedMfrPartObjectDto.setDescription(manufacturer.getDescription());
                sharedMfrPartObjectDto.setType(manufacturer.getMfrType().getName());
                sharedMfrPartObjectDto.setLifeCyclePhase(manufacturer.getLifeCyclePhase());
                sharedMfrPartObjectDto.setPermission(sharedObject.getPermission());
                sharedMfrPartObjectDto.setShareType(sharedObject.getShareType());
                sharedMfrPartObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                if (sharedObject.getShareType().name().equals("PERSON")) {
                    sharedMfrPartObjectDto
                            .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                } else {
                    sharedMfrPartObjectDto
                            .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                }
                sharedMfrPartObjectDto.setSharedOn(sharedObject.getCreatedDate());
                sharedPartObjectDtos.add(sharedMfrPartObjectDto);
            } else if (objectType.name().equals("PROGRAM")) {
                PLMProgram manufacturer = programRepository.findOne(sharedObject.getObjectId());
                sharedMfrPartObjectDto.setId(manufacturer.getId());
                sharedMfrPartObjectDto.setName(manufacturer.getName());
                sharedMfrPartObjectDto.setType(manufacturer.getType().getName());
                sharedMfrPartObjectDto.setManager(personRepository.findOne(manufacturer.getProgramManager()).getFullName());
                sharedMfrPartObjectDto.setDescription(manufacturer.getDescription());
                sharedMfrPartObjectDto.setPermission(sharedObject.getPermission());
                sharedMfrPartObjectDto.setShareType(sharedObject.getShareType());
                sharedMfrPartObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                if (sharedObject.getShareType().name().equals("PERSON")) {
                    sharedMfrPartObjectDto
                            .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                } else {
                    sharedMfrPartObjectDto
                            .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                }
                sharedMfrPartObjectDto.setSharedOn(sharedObject.getCreatedDate());
                sharedPartObjectDtos.add(sharedMfrPartObjectDto);
            } else if (objectType.name().equals("MANUFACTURERPART")) {
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(sharedObject.getObjectId());
                sharedMfrPartObjectDto.setId(manufacturerPart.getId());
                sharedMfrPartObjectDto.setName(manufacturerPart.getPartName());
                PLMManufacturer manufacturer = manufacturerRepository.findOne(manufacturerPart.getManufacturer());
                sharedMfrPartObjectDto.setMfrId(manufacturer.getId());
                sharedMfrPartObjectDto.setMfr(manufacturer.getName());
                sharedMfrPartObjectDto.setNumber(manufacturerPart.getPartNumber());
                sharedMfrPartObjectDto.setDescription(manufacturerPart.getDescription());
                sharedMfrPartObjectDto.setType(manufacturerPart.getMfrPartType().getName());
                sharedMfrPartObjectDto.setLifeCyclePhase(manufacturerPart.getLifeCyclePhase());
                sharedMfrPartObjectDto.setPermission(sharedObject.getPermission());
                sharedMfrPartObjectDto.setShareType(sharedObject.getShareType());
                sharedMfrPartObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                if (sharedObject.getShareType().name().equals("PERSON")) {
                    sharedMfrPartObjectDto
                            .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                } else {
                    sharedMfrPartObjectDto
                            .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                }
                sharedMfrPartObjectDto.setSharedOn(sharedObject.getCreatedDate());
                sharedPartObjectDtos.add(sharedMfrPartObjectDto);
            } else if (objectType.name().equals("MFRSUPPLIER")) {
                PLMSupplier supplier = supplierRepository.findOne(sharedObject.getObjectId());
                sharedMfrPartObjectDto.setId(supplier.getId());
                sharedMfrPartObjectDto.setName(supplier.getName());
                sharedMfrPartObjectDto.setDescription(supplier.getDescription());
                sharedMfrPartObjectDto.setType(supplier.getSupplierType().getName());
                sharedMfrPartObjectDto.setLifeCyclePhase(supplier.getLifeCyclePhase());
                sharedMfrPartObjectDto.setPermission(sharedObject.getPermission());
                sharedMfrPartObjectDto.setShareType(sharedObject.getShareType());
                sharedMfrPartObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                if (sharedObject.getShareType().name().equals("PERSON")) {
                    sharedMfrPartObjectDto
                            .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                } else {
                    sharedMfrPartObjectDto
                            .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                }
                sharedMfrPartObjectDto.setSharedOn(sharedObject.getCreatedDate());
                sharedPartObjectDtos.add(sharedMfrPartObjectDto);
            } else if (objectType.name().equals("CUSTOMOBJECT")) {
                CustomObject customObject = customObjectRepository.findOne(sharedObject.getObjectId());
                sharedMfrPartObjectDto.setId(customObject.getId());
                sharedMfrPartObjectDto.setName(customObject.getName());
                sharedMfrPartObjectDto.setDescription(customObject.getDescription());
                sharedMfrPartObjectDto.setType(customObject.getType().getName());
                sharedMfrPartObjectDto.setPermission(sharedObject.getPermission());
                sharedMfrPartObjectDto.setShareType(sharedObject.getShareType());
                sharedMfrPartObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                if (sharedObject.getShareType().name().equals("PERSON")) {
                    sharedMfrPartObjectDto
                            .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                } else {
                    sharedMfrPartObjectDto
                            .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                }
                sharedMfrPartObjectDto.setSharedOn(sharedObject.getCreatedDate());
                sharedPartObjectDtos.add(sharedMfrPartObjectDto);
            } else if (objectType.name().equals("PGCDECLARATION")) {
                PGCDeclaration declaration = declarationRepository.findOne(sharedObject.getObjectId());
                sharedMfrPartObjectDto.setId(declaration.getId());
                sharedMfrPartObjectDto.setName(declaration.getName());
                sharedMfrPartObjectDto.setNumber(declaration.getNumber());
                sharedMfrPartObjectDto.setDescription(declaration.getDescription());
                sharedMfrPartObjectDto.setSupplier(supplierRepository.findOne(declaration.getSupplier()).getName());
                sharedMfrPartObjectDto.setType(declaration.getType().getName());
                sharedMfrPartObjectDto.setPermission(sharedObject.getPermission());
                sharedMfrPartObjectDto.setShareType(sharedObject.getShareType());
                sharedMfrPartObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                if (sharedObject.getShareType().name().equals("PERSON")) {
                    sharedMfrPartObjectDto
                            .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                } else {
                    sharedMfrPartObjectDto
                            .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                }
                sharedMfrPartObjectDto.setStatus(declaration.getStatus().toString());
                sharedMfrPartObjectDto.setSharedOn(sharedObject.getCreatedDate());
                sharedPartObjectDtos.add(sharedMfrPartObjectDto);
            }
        }
        return sharedPartObjectDtos;

    }

    @Transactional
    public Page<SharedMfrPartObjectDto> getSharedDeclarationsByPerson(SharedObjectCriteria objectCriteria,
                                                                      Pageable pageable) {
        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedPartObjects = sharedObjectRepository.findAll(predicate, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedPartObjects, sharedPartObjectDtos,
                PLMObjectType.PGCDECLARATION);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable,
                sharedPartObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedDeclarationsBySharedToPerson(List<Integer> sharedTo,
                                                                              Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf("PGCDECLARATION"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.PGCDECLARATION);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedDeclarationsBySharedByAndSharedToPerson(Integer sharedBy,
                                                                                         Integer sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository.findBySharedObjectTypeAndSharedByAndSharedTo(
                ObjectType.valueOf("PGCDECLARATION"), sharedBy, sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.PGCDECLARATION);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedDeclarationsBySharedPerson(Integer sharedBy, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf("PGCDECLARATION"), sharedBy, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.PGCDECLARATION);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedMfrPartObjectDto> getSharedDeclarationsBySharedTo(Integer sharedTo, Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf("PGCDECLARATION"), sharedTo, pageable);
        sharedPartObjectDtos = getMfrShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.PGCDECLARATION);
        return new PageImpl<SharedMfrPartObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    @Transactional
    public Page<SharedMfrPartObjectDto> getAllSharedDeclarationObjects(Pageable pageable) {
        List<SharedMfrPartObjectDto> sharedMfrObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = null;
        sharedObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf("PGCDECLARATION"), pageable);
        sharedMfrObjectDtos = getMfrShareObjects(sharedObjects, sharedMfrObjectDtos, PLMObjectType.PGCDECLARATION);
        return new PageImpl<SharedMfrPartObjectDto>(sharedMfrObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<SharedProjectObjectDto> getSharedProjectsByPerson(SharedObjectCriteria objectCriteria,
                                                                  Pageable pageable, PLMObjectType objectType) {
        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
        List<SharedProjectObjectDto> sharedProjectObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedPartObjects = sharedObjectRepository.findAll(predicate, pageable);
        sharedProjectObjectDtos = getProjectShareObjects(sharedPartObjects, sharedProjectObjectDtos, objectType);
        return new PageImpl<SharedProjectObjectDto>(sharedProjectObjectDtos, pageable,
                sharedPartObjects.getTotalElements());
    }

    @Transactional
    public Page<SharedProjectObjectDto> getSharedProgramsByPerson(SharedObjectCriteria objectCriteria,
                                                                  Pageable pageable, PLMObjectType objectType) {
        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
        List<SharedProjectObjectDto> sharedProjectObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedPartObjects = sharedObjectRepository.findAll(predicate, pageable);
        sharedProjectObjectDtos = getProjectShareObjects(sharedPartObjects, sharedProjectObjectDtos, objectType);
        return new PageImpl<SharedProjectObjectDto>(sharedProjectObjectDtos, pageable,
                sharedPartObjects.getTotalElements());
    }

    @Transactional(readOnly = true)
    private List<SharedProjectObjectDto> getProjectShareObjects(Page<PLMSharedObject> sharedObjects,
                                                                List<SharedProjectObjectDto> sharedProjectObjectDtos, PLMObjectType objectType) {
        for (PLMSharedObject sharedObject : sharedObjects.getContent()) {
            SharedProjectObjectDto sharedProjectObjectDto = new SharedProjectObjectDto();
            if (objectType.name().equals("PROJECT")) {
                PLMProject project = projectRepository.findOne(sharedObject.getObjectId());
                if (project != null) {
                    project = projectService.getProjectPercentComplete(project.getId());
                    sharedProjectObjectDto.setId(project.getId());
                    sharedProjectObjectDto.setName(project.getName());
                    sharedProjectObjectDto.setDescription(project.getDescription());
                    sharedProjectObjectDto.setPlannedStartDate(project.getPlannedStartDate());
                    sharedProjectObjectDto.setPlannedFinishDate(project.getPlannedFinishDate());
                    sharedProjectObjectDto.setActualStartDate(project.getActualStartDate());
                    sharedProjectObjectDto.setActualFinishDate(project.getActualFinishDate());
                    sharedProjectObjectDto.setMakeConversationPrivate(project.getMakeConversationPrivate());
                    sharedProjectObjectDto.setPercentComplete(project.getPercentComplete());
                    if (project.getProjectManager() != null) {
                        Person person = personRepository.findOne(project.getProjectManager());
                        sharedProjectObjectDto.setManagerFirstName(person.getFirstName());
                        sharedProjectObjectDto.setManagerLastName(person.getLastName());
                        sharedProjectObjectDto.setManagerFullName(person.getFullName());
                        sharedProjectObjectDto.setProjectManager(project.getProjectManager());
                        sharedProjectObjectDto.setHasManagerImage(person.isHasImage());
                    }
                    Integer loginPersonId = null;
                    if (sessionWrapper != null && sessionWrapper.getSession() != null) {
                        loginPersonId = sessionWrapper.getSession().getLogin().getPerson().getId();
                    }
                    Boolean personExistInTeam = false;
                    List<Integer> projectMemberIds = projectMemberRepository.getProjectMemberIds(project.getId());
                    if (projectMemberIds.size() > 0) {
                        List<Person> persons = personRepository.findByIdIn(projectMemberIds);
                        for (Person person : persons) {
                            if (project.getMakeConversationPrivate()) {
                                if (loginPersonId != null && loginPersonId.equals(person.getId())) {
                                    personExistInTeam = true;
                                }
                            }
                            ProjectMemberDto projectMemberDto = new ProjectMemberDto();
                            projectMemberDto.setId(person.getId());
                            projectMemberDto.setFirstName(person.getFirstName());
                            projectMemberDto.setLastName(person.getLastName());
                            projectMemberDto.setFullName(person.getFullName());
                            projectMemberDto.setHasImage(person.isHasImage());
                            sharedProjectObjectDto.getProjectMembers().add(projectMemberDto);
                        }
                    }
                    if (!project.getMakeConversationPrivate()) {
                        sharedProjectObjectDto.setShowConversationCount(true);
                    } else if (personExistInTeam
                            || (loginPersonId != null && project.getProjectManager().equals(loginPersonId))) {
                        sharedProjectObjectDto.setShowConversationCount(true);
                    }
                    sharedProjectObjectDto.setTasks(taskRepository.getProjectTaskCount(project.getId()));
                    sharedProjectObjectDto.setComments(commentRepository.findAllByObjectId(project.getId()).size());
                    List<Integer> deliverableItems = projectDeliveravbleRepository
                            .getProjectDeliverableItem(project.getId());
                    deliverableItems.addAll(activityDeliverableRepository.getActivityDeliverableItems(project.getId()));
                    deliverableItems.addAll(taskDeliverableRepository.getTaskDeliverableItems(project.getId()));
                    List<Integer> items = new ArrayList<Integer>(new HashSet<Integer>(deliverableItems));
                    sharedProjectObjectDto.setDeliverables(items.size());
                    if (sharedObject.getShareType().name().equals("PERSON")) {
                        sharedProjectObjectDto
                                .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                    } else {
                        sharedProjectObjectDto
                                .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                    }
                    sharedProjectObjectDto.setPermission(sharedObject.getPermission());
                    sharedProjectObjectDto.setShareType(sharedObject.getShareType());
                    sharedProjectObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                    sharedProjectObjectDto.setSharedOn(sharedObject.getCreatedDate());
                    sharedProjectObjectDtos.add(sharedProjectObjectDto);
                }
            } else if (objectType.name().equals("PROJECTACTIVITY")) {
                PLMActivity activity = plmActivityRepository.findOne(sharedObject.getObjectId());
                if (activity != null) {
                    PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
                    sharedProjectObjectDto.setId(activity.getId());
                    sharedProjectObjectDto.setName(activity.getName());
                    sharedProjectObjectDto.setDescription(activity.getDescription());
                    sharedProjectObjectDto.setPlannedStartDate(activity.getPlannedStartDate());
                    sharedProjectObjectDto.setPlannedFinishDate(activity.getPlannedFinishDate());
                    sharedProjectObjectDto.setActualStartDate(activity.getActualStartDate());
                    sharedProjectObjectDto.setActualFinishDate(activity.getActualFinishDate());
                    sharedProjectObjectDto.setAssignedTo(activity.getAssignedTo());
                    sharedProjectObjectDto.setPercentComplete(activity.getPercentComplete());
                    sharedProjectObjectDto.setPhaseName(wbsElement.getName());
                    sharedProjectObjectDto.setProjectName(wbsElement.getProject().getName());
                    if (sharedObject.getShareType().name().equals("PERSON")) {
                        sharedProjectObjectDto
                                .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                    } else {
                        sharedProjectObjectDto
                                .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                    }
                    sharedProjectObjectDto.setPermission(sharedObject.getPermission());
                    sharedProjectObjectDto.setShareType(sharedObject.getShareType());
                    sharedProjectObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                    sharedProjectObjectDto.setSharedOn(sharedObject.getCreatedDate());
                    sharedProjectObjectDtos.add(sharedProjectObjectDto);
                }
            } else if (objectType.name().equals("PROJECTTASK")) {
                PLMTask task = taskRepository.findOne(sharedObject.getObjectId());
                if (task != null) {
                    PLMActivity activity = plmActivityRepository.findOne(task.getActivity());
                    PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
                    sharedProjectObjectDto.setId(task.getId());
                    sharedProjectObjectDto.setName(task.getName());
                    sharedProjectObjectDto.setDescription(task.getDescription());
                    sharedProjectObjectDto.setPlannedStartDate(task.getPlannedStartDate());
                    sharedProjectObjectDto.setPlannedFinishDate(task.getPlannedFinishDate());
                    sharedProjectObjectDto.setActualStartDate(task.getActualStartDate());
                    sharedProjectObjectDto.setActualFinishDate(task.getActualFinishDate());
                    sharedProjectObjectDto.setAssignedTo(task.getAssignedTo());
                    sharedProjectObjectDto.setActivity(task.getActivity());
                    sharedProjectObjectDto.setProject(wbsElement.getProject().getId());
                    sharedProjectObjectDto.setActivityName(activity.getName());
                    sharedProjectObjectDto.setProjectName(wbsElement.getProject().getName());
                    sharedProjectObjectDto.setPercentComplete(task.getPercentComplete());
                    sharedProjectObjectDto.setPhaseName(wbsElement.getName());
                    if (sharedObject.getShareType().name().equals("PERSON")) {
                        sharedProjectObjectDto
                                .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                    } else {
                        sharedProjectObjectDto
                                .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                    }
                    sharedProjectObjectDto.setPermission(sharedObject.getPermission());
                    sharedProjectObjectDto.setShareType(sharedObject.getShareType());
                    sharedProjectObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                    sharedProjectObjectDto.setSharedOn(sharedObject.getCreatedDate());
                    sharedProjectObjectDtos.add(sharedProjectObjectDto);
                }
            } else if (objectType.name().equals("PROGRAM")) {
                PLMProgram program = programRepository.findOne(sharedObject.getObjectId());
                if (program != null) {
                    sharedProjectObjectDto.setId(program.getId());
                    sharedProjectObjectDto.setName(program.getName());
                    sharedProjectObjectDto.setType(program.getType().getName());
                    sharedProjectObjectDto.setDescription(program.getDescription());
                    sharedProjectObjectDto.setManager(personRepository.findOne(program.getProgramManager()).getFullName());
                    if (sharedObject.getShareType().name().equals("PERSON")) {
                        sharedProjectObjectDto
                                .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                    } else {
                        sharedProjectObjectDto
                                .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                    }
                    sharedProjectObjectDto.setPermission(sharedObject.getPermission());
                    sharedProjectObjectDto.setShareType(sharedObject.getShareType());
                    sharedProjectObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                    sharedProjectObjectDto.setSharedOn(sharedObject.getCreatedDate());
                    sharedProjectObjectDtos.add(sharedProjectObjectDto);
                }
            }
        }
        return sharedProjectObjectDtos;

    }


    public List<PLMSharedObject> getByObjectId(Integer objectId) {
        return sharedObjectRepository.findByObjectId(objectId);
    }

    public PersonSharedObjectCounts getPersonSharedCounts(Integer personId){
        PersonSharedObjectCounts personSharedObjectCounts = new PersonSharedObjectCounts();
        personSharedObjectCounts.setProject(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.PROJECT.toString()), personId));
        personSharedObjectCounts.setProgram(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.PROGRAM.toString()), personId));
        personSharedObjectCounts.setActivity(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.PROJECTACTIVITY.toString()), personId));
        personSharedObjectCounts.setTask(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.PROJECTTASK.toString()), personId));
        personSharedObjectCounts.setItem(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.ITEM.toString()), personId));
        personSharedObjectCounts.setMfrPart(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.MANUFACTURERPART.toString()), personId));
        personSharedObjectCounts.setSupplier(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.MFRSUPPLIER.toString()), personId));
        personSharedObjectCounts.setCustomObject(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.CUSTOMOBJECT.toString()), personId));
        personSharedObjectCounts.setMfr(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.MANUFACTURER.toString()), personId));
        personSharedObjectCounts.setFolder(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.DOCUMENT.toString()), personId));
        personSharedObjectCounts.setSharedFolders(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf("FILE"), personId));
        personSharedObjectCounts.setDeclaration(sharedObjectRepository.getCountByShareTypeAndSharedTo(ObjectType.valueOf(PLMObjectType.PGCDECLARATION.toString()), personId));
    
        return personSharedObjectCounts ;


    }


    public SharedObjectCounts getSharedCounts(String type) {
        SharedObjectCounts sharedObjectCounts = new SharedObjectCounts();
        SharedObjectCriteria objectCriteria = new SharedObjectCriteria();
        if (type.equals("external")) {
            objectCriteria.setSharedObjectType(PLMObjectType.PROJECT.toString());
            Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setProject(sharedObjectRepository.count(predicate));
            objectCriteria.setSharedObjectType(PLMObjectType.PROJECTACTIVITY.toString());
            Predicate predicate1 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setActivity(sharedObjectRepository.count(predicate1));
            objectCriteria.setSharedObjectType(PLMObjectType.PROJECTTASK.toString());
            Predicate predicate2 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setTask(sharedObjectRepository.count(predicate2));
            objectCriteria.setSharedObjectType(PLMObjectType.ITEM.toString());
            Predicate predicate3 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setItem(sharedObjectRepository.count(predicate3));
            objectCriteria.setSharedObjectType(PLMObjectType.MANUFACTURERPART.toString());
            Predicate predicate4 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setMfrPart(sharedObjectRepository.count(predicate4));
            objectCriteria.setSharedObjectType(PLMObjectType.MFRSUPPLIER.toString());
            Predicate predicate7 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setSupplier(sharedObjectRepository.count(predicate7));
            objectCriteria.setSharedObjectType(PLMObjectType.CUSTOMOBJECT.toString());
            Predicate predicate20 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setCustomObject(sharedObjectRepository.count(predicate20));
            objectCriteria.setSharedObjectType(PLMObjectType.DOCUMENT.toString());
            Predicate predicate8 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setFolder(sharedObjectRepository.count(predicate8));
            objectCriteria.setSharedObjectType(PLMObjectType.PGCDECLARATION.toString());
            Predicate predicate5 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setDeclaration(sharedObjectRepository.count(predicate5));
            objectCriteria.setSharedObjectType(PLMObjectType.MANUFACTURER.toString());
            Predicate predicate6 = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
            sharedObjectCounts.setMfr(sharedObjectRepository.count(predicate6));
            sharedObjectCounts.setSharedFolders(sharedObjectRepository.findByObjectTypeAndSharedTo(ObjectType.valueOf("FILE"), sessionWrapper.getSession().getLogin().getPerson().getId()));
        } else {
            sharedObjectCounts.setItem(
                    sharedObjectRepository.getObjectTypeCount(ObjectType.valueOf(PLMObjectType.ITEM.toString())));
            sharedObjectCounts.setDeclaration(sharedObjectRepository
                    .getObjectTypeCount(ObjectType.valueOf(PLMObjectType.PGCDECLARATION.toString())));
            sharedObjectCounts.setMfrPart(sharedObjectRepository
                    .getObjectTypeCount(ObjectType.valueOf(PLMObjectType.MANUFACTURERPART.toString())));
            sharedObjectCounts.setSupplier(sharedObjectRepository
                    .getObjectTypeCount(ObjectType.valueOf(PLMObjectType.MFRSUPPLIER.toString())));
            sharedObjectCounts.setCustomObject(sharedObjectRepository
                    .getObjectTypeCount(ObjectType.valueOf(PLMObjectType.CUSTOMOBJECT.toString())));
            sharedObjectCounts.setFolder(sharedObjectRepository
                    .getObjectTypeCount(ObjectType.valueOf(PLMObjectType.DOCUMENT.toString())));
            sharedObjectCounts.setMfr(sharedObjectRepository
                    .getObjectTypeCount(ObjectType.valueOf(PLMObjectType.MANUFACTURER.toString())));
            sharedObjectCounts.setProject(
                    sharedObjectRepository.getObjectTypeCount(ObjectType.valueOf(PLMObjectType.PROJECT.toString())));
            sharedObjectCounts.setActivity(sharedObjectRepository
                    .getObjectTypeCount(ObjectType.valueOf(PLMObjectType.PROJECTACTIVITY.toString())));
            sharedObjectCounts.setTask(sharedObjectRepository
                    .getObjectTypeCount(ObjectType.valueOf(PLMObjectType.PROJECTTASK.toString())));
        }
        return sharedObjectCounts;
    }

    public void OverrideProjectPermission(PLMProject project, PLMSharedObject sharedObject) {
        List<SharedMultipleProjectObjectsDto> sharedMultipleProjectObjectsDtos = new ArrayList<>();
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(project);
        if (wbsElements.size() > 0) {
            for (PLMWbsElement wbsElement : wbsElements) {
                List<PLMActivity> activities = activityRepository.findByWbs(wbsElement.getId());
                if (activities.size() > 0) {
                    for (PLMActivity activity : activities) {
                        SharedMultipleProjectObjectsDto sharedMultipleProjectObjectsDto = new SharedMultipleProjectObjectsDto();
                        sharedMultipleProjectObjectsDto.setObjectType(PLMObjectType.PROJECTACTIVITY.name());
                        sharedMultipleProjectObjectsDto.setId(activity.getId());
                        sharedMultipleProjectObjectsDto.setName(activity.getName());
                        sharedMultipleProjectObjectsDtos.add(sharedMultipleProjectObjectsDto);
                        List<PLMTask> tasks = taskRepository.findByActivity(activity.getId());
                        if (tasks.size() > 0) {
                            for (PLMTask task : tasks) {
                                SharedMultipleProjectObjectsDto sharedMultipleProjectObjectsDto1 = new SharedMultipleProjectObjectsDto();
                                sharedMultipleProjectObjectsDto1.setObjectType(PLMObjectType.PROJECTTASK.name());
                                sharedMultipleProjectObjectsDto1.setId(task.getId());
                                sharedMultipleProjectObjectsDto1.setName(task.getName());
                                sharedMultipleProjectObjectsDtos.add(sharedMultipleProjectObjectsDto1);
                            }
                        }
                    }
                }
            }
        }
        saveMultipleProjectSharedObjects(sharedMultipleProjectObjectsDtos, sharedObject);
    }

    public Page<SharedProjectObjectDto> getAllSharedProjectObjects(Pageable pageable, String objectType) {
        List<SharedProjectObjectDto> sharedProjectObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = null;
        sharedObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf(objectType), pageable);
        sharedProjectObjectDtos = getProjectShareObjects(sharedObjects, sharedProjectObjectDtos,
                PLMObjectType.valueOf(objectType));
        return new PageImpl<SharedProjectObjectDto>(sharedProjectObjectDtos, pageable,
                sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getAllSharedProgramObjects(Pageable pageable, String objectType) {
        List<SharedProjectObjectDto> sharedProjectObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = null;
        sharedObjects = sharedObjectRepository.findBySharedObjectType(ObjectType.valueOf(objectType), pageable);
        sharedProjectObjectDtos = getProjectShareObjects(sharedObjects, sharedProjectObjectDtos,
                PLMObjectType.valueOf(objectType));
        return new PageImpl<SharedProjectObjectDto>(sharedProjectObjectDtos, pageable,
                sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getProjectObjectsBySharedTo(Integer sharedTo, Pageable pageable,
                                                                    String objectType) {
        List<SharedProjectObjectDto> sharedProjectObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf(objectType), sharedTo, pageable);
        sharedProjectObjectDtos = getProjectShareObjects(sharedObjects, sharedProjectObjectDtos,
                PLMObjectType.valueOf(objectType));
        return new PageImpl<SharedProjectObjectDto>(sharedProjectObjectDtos, pageable,
                sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getProgramObjectsBySharedTo(Integer sharedTo, Pageable pageable,
                                                                    String objectType) {
        List<SharedProjectObjectDto> sharedProjectObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedTo(ObjectType.valueOf(objectType), sharedTo, pageable);
        sharedProjectObjectDtos = getProjectShareObjects(sharedObjects, sharedProjectObjectDtos,
                PLMObjectType.valueOf(objectType));
        return new PageImpl<SharedProjectObjectDto>(sharedProjectObjectDtos, pageable,
                sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getProjectObjectsBySharedBy(Integer sharedBy, Pageable pageable,
                                                                    String objectType) {
        List<SharedProjectObjectDto> sharedProjectObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf(objectType), sharedBy, pageable);
        sharedProjectObjectDtos = getProjectShareObjects(sharedObjects, sharedProjectObjectDtos,
                PLMObjectType.valueOf(objectType));
        return new PageImpl<SharedProjectObjectDto>(sharedProjectObjectDtos, pageable,
                sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getProgramObjectsBySharedBy(Integer sharedBy, Pageable pageable,
                                                                    String objectType) {
        List<SharedProjectObjectDto> sharedProjectObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedBy(ObjectType.valueOf(objectType), sharedBy, pageable);
        sharedProjectObjectDtos = getProjectShareObjects(sharedObjects, sharedProjectObjectDtos,
                PLMObjectType.valueOf(objectType));
        return new PageImpl<SharedProjectObjectDto>(sharedProjectObjectDtos, pageable,
                sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getSharedProjectObjectBySharedPerson(Integer sharedBy, Integer sharedTo,
                                                                             Pageable pageable, String type) {
        List<SharedProjectObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedByAndSharedTo(ObjectType.valueOf(type), sharedBy, sharedTo, pageable);
        sharedPartObjectDtos = getProjectShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.valueOf(type));
        return new PageImpl<SharedProjectObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getSharedProgramObjectBySharedPerson(Integer sharedBy, Integer sharedTo,
                                                                             Pageable pageable, String type) {
        List<SharedProjectObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedByAndSharedTo(ObjectType.valueOf(type), sharedBy, sharedTo, pageable);
        sharedPartObjectDtos = getProjectShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.valueOf(type));
        return new PageImpl<SharedProjectObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getSharedProjectObjectssBySharedToPerson(List<Integer> sharedTo,
                                                                                 Pageable pageable, String type) {
        List<SharedProjectObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf(type), sharedTo, pageable);
        sharedPartObjectDtos = getProjectShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.valueOf(type));
        return new PageImpl<SharedProjectObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }

    public Page<SharedProjectObjectDto> getSharedProgramObjectssBySharedToPerson(List<Integer> sharedTo,
                                                                                 Pageable pageable, String type) {
        List<SharedProjectObjectDto> sharedPartObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedObjects = sharedObjectRepository
                .findBySharedObjectTypeAndSharedToIn(ObjectType.valueOf(type), sharedTo, pageable);
        sharedPartObjectDtos = getProjectShareObjects(sharedObjects, sharedPartObjectDtos, PLMObjectType.valueOf(type));
        return new PageImpl<SharedProjectObjectDto>(sharedPartObjectDtos, pageable, sharedObjects.getTotalElements());
    }
    


    /*
    *
    * Folder Sharing
    * */


    @Transactional
    private PLMSharedObject createFolderObject(PLMSharedObject plmSharedObject) {
        PLMSharedObject sharedObject = new PLMSharedObject();
        List<Integer> objectIds = new ArrayList<>();
        PLMDocument folder = documentRepository.findOne(plmSharedObject.getObjectId());
        List<PLMDocument> folders = new ArrayList();
        folders.add(folder);
        objectIds.add(folder.getId());
        List<Integer> sharedToObjects = plmSharedObject.getSharedToObjects();
        for (Integer person : sharedToObjects) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.findByObjectIdAndSharedToAndPermission(
                    plmSharedObject.getObjectId(), person, plmSharedObject.getPermission());
            List<PLMSharedObject> sharedObjects1 = sharedObjectRepository
                    .findByObjectIdAndSharedTo(plmSharedObject.getObjectId(), person);
            if (sharedObjects.size() == 0 && sharedObjects1.size() > 0) {
                sharedObject = sharedObjects1.get(0);
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject = sharedObjectRepository.save(sharedObject);
            } else if (sharedObjects.size() == 0) {
                sharedObject = new PLMSharedObject();
                sharedObject.setSharedBy(plmSharedObject.getSharedBy());
                sharedObject.setSharedTo(person);
                sharedObject.setObjectId(plmSharedObject.getObjectId());
                sharedObject.setPermission(plmSharedObject.getPermission());
                sharedObject.setShareType(plmSharedObject.getShareType());
                sharedObject.setSharedObjectType(plmSharedObject.getSharedObjectType());
                sharedObject = sharedObjectRepository.save(sharedObject);
            }
        }
        if (sharedObject.getShareType() != null) {
            if (sharedObject.getShareType().equals(ShareType.PERSON)) {
                sendNotificationToMultiplePerson(objectIds, plmSharedObject, PLMObjectType.DOCUMENT,
                        ShareType.PERSON);
            } else {
                sendNotificationToMultiplePerson(objectIds, sharedObject, PLMObjectType.DOCUMENT,
                        ShareType.GROUP);
            }
        }
        return sharedObject;
    }


    @Transactional
    public Page<SharedFolderObjectDto> getSharedFoldersByPerson(SharedObjectCriteria objectCriteria,
                                                                Pageable pageable) {
        Predicate predicate = sharedObjectPredicateBuilder.build(objectCriteria, QPLMSharedObject.pLMSharedObject);
        List<SharedFolderObjectDto> sharedSupplierObjectDtos = new ArrayList<>();
        Page<PLMSharedObject> sharedSupplierObjects = sharedObjectRepository.findAll(predicate, pageable);
        sharedSupplierObjectDtos = getFolderShareObjects(sharedSupplierObjects, sharedSupplierObjectDtos,
                PLMObjectType.DOCUMENT);
        return new PageImpl<SharedFolderObjectDto>(sharedSupplierObjectDtos, pageable,
                sharedSupplierObjects.getTotalElements());
    }


    private List<SharedFolderObjectDto> getFolderShareObjects(Page<PLMSharedObject> sharedObjects,
                                                              List<SharedFolderObjectDto> sharedPartObjectDtos, PLMObjectType objectType) {
        for (PLMSharedObject sharedObject : sharedObjects.getContent()) {
            SharedFolderObjectDto sharedMfrPartObjectDto = new SharedFolderObjectDto();
            if (objectType.name().equals("DOCUMENT")) {
                PLMDocument folder = documentRepository.findOne(sharedObject.getObjectId());
                sharedMfrPartObjectDto.setId(sharedObject.getId());
                sharedMfrPartObjectDto.getFileDto().setId(folder.getId());
                sharedMfrPartObjectDto.getFileDto().setModifiedDate(folder.getModifiedDate());
                sharedMfrPartObjectDto.getFileDto().setName(folder.getName());
                sharedMfrPartObjectDto.getFileDto().setFileNo(folder.getFileNo());
                sharedMfrPartObjectDto.getFileDto().setRevision(folder.getRevision());
                sharedMfrPartObjectDto.getFileDto().setParentObject(objectType);
                sharedMfrPartObjectDto.getFileDto().setLifeCyclePhase(folder.getLifeCyclePhase());
                sharedMfrPartObjectDto.getFileDto().setObjectType(PLMObjectType.valueOf(folder.getObjectType().toString()));
                sharedMfrPartObjectDto.getFileDto().setDescription(folder.getDescription());
                sharedMfrPartObjectDto.getFileDto().setFileNo(folder.getFileNo());
                sharedMfrPartObjectDto.getFileDto().setFileType(folder.getFileType());
                sharedMfrPartObjectDto.getFileDto().setParentFile(folder.getParentFile());
                sharedMfrPartObjectDto.getFileDto().setSize(folder.getSize());
                sharedMfrPartObjectDto.getFileDto().setLatest(folder.getLatest());
                sharedMfrPartObjectDto.getFileDto().setParentObject(objectType);
                sharedMfrPartObjectDto.getFileDto().setParentFile(folder.getParentFile());
                sharedMfrPartObjectDto.getFileDto().setVersion(folder.getVersion());
                sharedMfrPartObjectDto.setPermission(sharedObject.getPermission());
                sharedMfrPartObjectDto.setShareType(sharedObject.getShareType());


                sharedMfrPartObjectDto.setSharedBy(personRepository.findOne(sharedObject.getSharedBy()).getFullName());
                if (sharedObject.getShareType().name().equals("PERSON")) {
                    sharedMfrPartObjectDto
                            .setSharedTo(personRepository.findOne(sharedObject.getSharedTo()).getFullName());
                } else {
                    sharedMfrPartObjectDto
                            .setSharedTo(personGroupRepository.findOne(sharedObject.getSharedTo()).getName());
                }
                sharedMfrPartObjectDto.setSharedOn(sharedObject.getCreatedDate());
                sharedPartObjectDtos.add(sharedMfrPartObjectDto);
            }

        }
        return sharedPartObjectDtos;

    }


    @Transactional(readOnly = true)
    public List<SharedFolderObjectDto> getFolderChildren(Integer folderId) {
        List<SharedFolderObjectDto> folderObjectDtos = new ArrayList<SharedFolderObjectDto>();
        List<SharedFolderObjectDto> folders = new ArrayList<>();
        List<SharedFolderObjectDto> files = new ArrayList<>();
        List<Integer> foldersList = fileRepository.getByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(folderId, "FOLDER");
        List<Integer> filesList = fileRepository.getByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(folderId, "FILE");
        if (foldersList.size() > 0) {
            folders = convertFilesIdsToDtoList(foldersList);
        }
        if (filesList.size() > 0) {
            files = convertFilesIdsToDtoList(filesList);
        }
        Collections.sort(files, new Comparator<SharedFolderObjectDto>() {
            public int compare(final SharedFolderObjectDto object1, final SharedFolderObjectDto object2) {
                return object2.getFileDto().getModifiedDate().compareTo(object1.getFileDto().getModifiedDate());
            }
        });

        folderObjectDtos.addAll(folders);
        folderObjectDtos.addAll(files);

        return folderObjectDtos;
    }


    private List<SharedFolderObjectDto> convertFilesIdsToDtoList(List<Integer> fileIds) {
        List<SharedFolderObjectDto> sharedFolderObjectDtoList = new ArrayList<>();
        List<PLMDocument> files = documentRepository.findByIdIn(fileIds);
        files.forEach(plmFile -> {
            SharedFolderObjectDto sharedMfrPartObjectDto = new SharedFolderObjectDto();
            sharedMfrPartObjectDto.setId(plmFile.getId());
            sharedMfrPartObjectDto.getFileDto().setModifiedDate(plmFile.getModifiedDate());
            sharedMfrPartObjectDto.getFileDto().setId(plmFile.getId());
            sharedMfrPartObjectDto.getFileDto().setFileNo(plmFile.getFileNo());
            sharedMfrPartObjectDto.getFileDto().setName(plmFile.getName());
            sharedMfrPartObjectDto.getFileDto().setDescription(plmFile.getDescription());
            sharedMfrPartObjectDto.getFileDto().setRevision(plmFile.getRevision());
            sharedMfrPartObjectDto.getFileDto().setDescription(plmFile.getDescription());
            sharedMfrPartObjectDto.getFileDto().setLifeCyclePhase(plmFile.getLifeCyclePhase());
            sharedMfrPartObjectDto.getFileDto().setFileNo(plmFile.getFileNo());
            sharedMfrPartObjectDto.getFileDto().setFileType(plmFile.getFileType());
            sharedMfrPartObjectDto.getFileDto().setParentFile(plmFile.getParentFile());
            sharedMfrPartObjectDto.getFileDto().setSize(plmFile.getSize());
            sharedMfrPartObjectDto.getFileDto().setLatest(plmFile.getLatest());
            sharedMfrPartObjectDto.getFileDto().setParentFile(plmFile.getParentFile());
            sharedMfrPartObjectDto.getFileDto().setVersion(plmFile.getVersion());
            sharedMfrPartObjectDto.setPermission(null);
            sharedMfrPartObjectDto.setShareType(null);
            sharedMfrPartObjectDto
                    .setSharedTo("");
            sharedMfrPartObjectDto.setSharedOn(null);
            sharedFolderObjectDtoList.add(sharedMfrPartObjectDto);
        });
        return sharedFolderObjectDtoList;
    }



    /*
    *
    * Files and Folders Sharing by individual objects
    *
    *
    * */


    public List<PLMSharedObject> saveMultipleSharedFileObjects(
            List<FileDto> plmFiles, PLMSharedObject sharedObject) {
        List<PLMSharedObject> sharedObjects = new ArrayList<>();
        PLMSharedObject object = new PLMSharedObject();
        List<String> newItems = new ArrayList<>();
        List<String> existingItems = new ArrayList<>();
        for (FileDto plmFile : plmFiles) {
            for (Integer person : sharedObject.getSharedToObjects()) {
                List<PLMSharedObject> plmSharedObjects = sharedObjectRepository
                        .findByObjectIdAndSharedTo(plmFile.getId(), person);
                if (plmSharedObjects.size() == 0) {
                    object = new PLMSharedObject();
                    object.setObjectId(plmFile.getId());
                    object.setSharedObjectType(ObjectType.valueOf(plmFile.getObjectType().name()));
                    object.setSharedTo(person);
                    object.setShareType(sharedObject.getShareType());
                    object.setParentObjectId(sharedObject.getParentObjectId());
                    object.setParentObjectType(sharedObject.getParentObjectType());
                    object.setPermission(sharedObject.getPermission());
                    object.setSharedBy(sharedObject.getSharedBy());
                    object = sharedObjectRepository.save(object);
                    object.setAdded(true);
                } else {
                    throw new CassiniException(plmFile.getName() + " : already shared");
                }
            }
            if (object.isAdded())
                newItems.add(plmFile.getName());
            else
                existingItems.add(plmFile.getName());
            object.setNewItems(newItems);
            object.setExistingItems(existingItems);
            sharedObjects.add(object);

        }

        return sharedObjects;
    }


    @Transactional
    public List<FileDto> getExternalSharedFoldersByPerson(Integer personId) {
        List<FileDto> fileDtos = new LinkedList<>();
        List<PLMSharedObject> sharedObjects = sharedObjectRepository.getObjectIdsByObjectTypeAndSharedTo(ObjectType.valueOf("FILE"), personId);
        Map<Integer, PLMSharedObject> shareObjectMap = new LinkedHashMap();
        List<Integer> objectIds = new LinkedList<>();
        sharedObjects.forEach(sharedObject -> {
                    objectIds.add(sharedObject.getObjectId());
                    shareObjectMap.put(sharedObject.getObjectId(), sharedObject);
                }
        );

        if (objectIds.size() > 0) {
            List<PLMFile> plmFiles = fileRepository.findByIdInAndFileType(objectIds, "FOLDER");
            plmFiles.forEach(plmFile -> {
                        PLMSharedObject shareObject = shareObjectMap.get(plmFile.getId());
                        FileDto fileDto = new FileDto();
                        fileDto.setId(plmFile.getId());
                        fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().toString()));
                        fileDto.setName(plmFile.getName());
                        if (shareObject.getParentObjectType().toString().equals("PROGRAM")) {
                            PLMProgram plmProgram = programRepository.findOne(shareObject.getParentObjectId());
                            fileDto.setType(PLMObjectType.PROGRAM);
                            fileDto.setParentObjectName(plmProgram.getName());
                            fileDto.setParentObjectId(plmProgram.getId());
                        } else if (shareObject.getParentObjectType().toString().equals("PROJECT")) {
                            PLMProject plmProgramProject = projectRepository.findOne(shareObject.getParentObjectId());
                            fileDto.setType(PLMObjectType.PROJECT);
                            fileDto.setParentObjectName(plmProgramProject.getName());
                            fileDto.setParentObjectId(plmProgramProject.getId());
                        } else if (shareObject.getParentObjectType().toString().equals("PROJECTACTIVITY")) {
                            PLMActivity plmActivity = activityRepository.findOne(shareObject.getParentObjectId());
                            fileDto.setType(PLMObjectType.PROJECTACTIVITY);
                            fileDto.setParentObjectName(plmActivity.getName());
                            fileDto.setParentObjectId(plmActivity.getId());
                        } else if (shareObject.getParentObjectType().toString().equals("PROJECTTASK")) {
                            PLMTask plmTask = taskRepository.findOne(shareObject.getParentObjectId());
                            fileDto.setType(PLMObjectType.PROJECTTASK);
                            fileDto.setParentObjectName(plmTask.getName());
                            fileDto.setParentObjectId(plmTask.getId());
                        }
                        visitFolderChildren(fileDto);
                        fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().name()));
                        fileDto.setParentFile(plmFile.getParentFile());
                        fileDto.setFileType(plmFile.getFileType());
                        fileDtos.add(fileDto);
                    }
            );
        }


        List<PLMSharedObject> sharedObjectsForDocuments = sharedObjectRepository.getObjectIdsByObjectTypeAndSharedTo(ObjectType.valueOf("DOCUMENT"), personId);
        List<Integer> objectIdsForDocuments = new LinkedList<>();
        sharedObjectsForDocuments.forEach(sharedObject -> {
                    objectIdsForDocuments.add(sharedObject.getObjectId());
                }
        );

        if (objectIdsForDocuments.size() > 0) {
            List<PLMDocument> plmDocuments = documentRepository.findByIdInAndFileType(objectIdsForDocuments, "FOLDER");
            plmDocuments.forEach(plmDocument -> {
                        FileDto fileDto = new FileDto();
                        fileDto.setId(plmDocument.getId());
                        fileDto.setName(plmDocument.getName());
                        fileDto.setObjectType(PLMObjectType.valueOf(plmDocument.getObjectType().toString()));
                        fileDto.setObjectType(PLMObjectType.valueOf(plmDocument.getObjectType().name()));
                        fileDto.setParentFile(plmDocument.getParentFile());
                        fileDto.setFileType(plmDocument.getFileType());
                        visitDocumentFolderChildren(fileDto);
                        fileDtos.add(fileDto);
                    }
            );
        }


        return fileDtos;
    }


    private void visitDocumentFolderChildren(FileDto fileDto) {
        List<PLMDocument> plmDocuments = documentRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(fileDto.getId(), "FOLDER");
        plmDocuments.forEach(document -> {
            FileDto fileDtoChildren = new FileDto();
            fileDtoChildren.setId(document.getId());
            fileDtoChildren.setName(document.getName());
            fileDtoChildren.setObjectType(PLMObjectType.valueOf(document.getObjectType().toString()));
            fileDto.getChildren().add(fileDtoChildren);
            visitDocumentFolderChildren(fileDtoChildren);
        });
        fileDto.setCount(documentRepository.getChildrenCountByParentAndFileType(fileDto.getId(), "FILE"));
    }


    private void visitFolderChildren(FileDto fileDto) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(fileDto.getId(), "FOLDER");
        plmFiles.forEach(document -> {
            FileDto fileDtoChildren = new FileDto();
            fileDtoChildren.setId(document.getId());
            fileDtoChildren.setName(document.getName());
            fileDtoChildren.setParentObjectName(fileDto.getParentObjectName());
            fileDtoChildren.setType(fileDto.getType());
            fileDtoChildren.setParentObjectId(fileDto.getParentObjectId());
            fileDtoChildren.setObjectType(PLMObjectType.valueOf(document.getObjectType().toString()));
            fileDto.getChildren().add(fileDtoChildren);
            visitFolderChildren(fileDtoChildren);
        });
        fileDto.setCount(fileRepository.getChildCountByFileType(fileDto.getId(), "FILE"));
    }


    /*
    * Shared root files
    *
    * */


    private void generateFileDto(FileDto fileDto, PLMFile plmFile) {
        fileDto.setSize(plmFile.getSize());
        fileDto.setDescription(plmFile.getDescription());
        fileDto.setVersion(plmFile.getVersion());
        fileDto.setCreatedDate(plmFile.getCreatedDate());
        fileDto.setModifiedDate(plmFile.getModifiedDate());
        Person createPerson = personRepository.findOne(plmFile.getCreatedBy());
        Person modifiedPerson = personRepository.findOne(plmFile.getModifiedBy());
        fileDto.setModifiedByName(modifiedPerson.getFullName());
        fileDto.setCreatedByName(createPerson.getFullName());

    }


    @Transactional
    public List<FileDto> getExternalSharedRootFilesByPerson(Integer personId) {
        List<FileDto> fileDtos = new LinkedList<>();
        List<PLMSharedObject> sharedObjects = sharedObjectRepository.getObjectIdsByObjectTypeAndSharedTo(ObjectType.valueOf("FILE"), personId);
        Map<Integer, PLMSharedObject> shareObjectMap = new LinkedHashMap();
        List<Integer> objectIds = new LinkedList<>();
        sharedObjects.forEach(sharedObject -> {
                    objectIds.add(sharedObject.getObjectId());
                    shareObjectMap.put(sharedObject.getObjectId(), sharedObject);
                }
        );

        if (objectIds.size() > 0) {
            List<PLMFile> plmFiles = fileRepository.findByIdInAndFileType(objectIds, "FILE");
            plmFiles.forEach(plmFile -> {
                        PLMSharedObject shareObject = shareObjectMap.get(plmFile.getId());
                        FileDto fileDto = new FileDto();
                        fileDto.setId(plmFile.getId());
                        fileDto.setName(plmFile.getName());
                        fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().toString()));
                        if (shareObject.getParentObjectType().toString().equals("PROGRAM")) {
                            if (shareObject.getParentObjectId() != null) {
                                PLMProgram plmProgram = programRepository.findOne(shareObject.getParentObjectId());
                                if (plmProgram != null) {
                                    fileDto.setType(PLMObjectType.PROGRAM);
                                    fileDto.setParentObjectName(plmProgram.getName());
                                    fileDto.setParentObjectId(plmProgram.getId());
                                    generateFileDto(fileDto, plmFile);
                                }
                            }
                        } else if (shareObject.getParentObjectType().toString().equals("PROJECT")) {
                            if (shareObject.getParentObjectId() != null) {
                                PLMProject plmProgramProject = projectRepository.findOne(shareObject.getParentObjectId());
                                if (plmProgramProject != null) {
                                    fileDto.setType(PLMObjectType.PROJECT);
                                    fileDto.setParentObjectName(plmProgramProject.getName());
                                    fileDto.setParentObjectId(plmProgramProject.getId());
                                    generateFileDto(fileDto, plmFile);
                                }
                            }
                        } else if (shareObject.getParentObjectType().toString().equals("PROJECTACTIVITY")) {
                            if (shareObject.getParentObjectId() != null) {
                                PLMActivity plmActivity = activityRepository.findOne(shareObject.getParentObjectId());
                                if (plmActivity != null) {
                                    fileDto.setType(PLMObjectType.PROJECTACTIVITY);
                                    fileDto.setParentObjectName(plmActivity.getName());
                                    fileDto.setParentObjectId(plmActivity.getId());
                                    generateFileDto(fileDto, plmFile);
                                }
                            }
                        } else if (shareObject.getParentObjectType().toString().equals("PROJECTTASK")) {
                            if (shareObject.getParentObjectId() != null) {
                                PLMTask plmTask = taskRepository.findOne(shareObject.getParentObjectId());
                                if (plmTask != null) {
                                    fileDto.setType(PLMObjectType.PROJECTTASK);
                                    fileDto.setParentObjectName(plmTask.getName());
                                    fileDto.setParentObjectId(plmTask.getId());
                                    generateFileDto(fileDto, plmFile);
                                }
                            }
                        }
                        fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().name()));
                        fileDto.setFileType(plmFile.getFileType());
                        fileDtos.add(fileDto);
                    }
            );
        }

        return fileDtos;
    }

/*
*
* selected folder shared files
*
* */

    @Transactional
    public List<FileDto> getExternalSharedFolderFiles(Integer folderId, Integer personId, String objectType) {
        List<FileDto> fileDtos = new LinkedList<>();
        if (objectType.equals("FILE")) {
            List<PLMSharedObject> sharedObjects = sharedObjectRepository.getObjectIdsByObjectTypeAndSharedTo(ObjectType.valueOf("FILE"), personId);
            Map<Integer, PLMSharedObject> shareObjectMap = new LinkedHashMap();
            List<Integer> objectIds = new LinkedList<>();
            sharedObjects.forEach(sharedObject -> {
                        objectIds.add(sharedObject.getObjectId());
                        shareObjectMap.put(sharedObject.getObjectId(), sharedObject);
                    }
            );

            if (objectIds.size() > 0) {
                List<PLMFile> plmFiles = fileRepository.findByIdInAndFileTypeAndParentFile(objectIds, "FILE", folderId);
                plmFiles.forEach(plmFile -> {
                            PLMSharedObject shareObject = shareObjectMap.get(plmFile.getId());
                            FileDto fileDto = new FileDto();
                            fileDto.setId(plmFile.getId());
                            fileDto.setName(plmFile.getName());
                            if (shareObject.getParentObjectType().toString().equals("PROGRAM")) {
                                PLMProgram plmProgram = programRepository.findOne(shareObject.getParentObjectId());
                                fileDto.setType(PLMObjectType.PROGRAM);
                                fileDto.setParentObjectName(plmProgram.getName());
                                fileDto.setParentObjectId(plmProgram.getId());
                                generateFileDto(fileDto, plmFile);

                            } else if (shareObject.getParentObjectType().toString().equals("PROJECT")) {
                                PLMProject plmProgramProject = projectRepository.findOne(shareObject.getParentObjectId());
                                fileDto.setType(PLMObjectType.PROJECT);
                                fileDto.setParentObjectName(plmProgramProject.getName());
                                fileDto.setParentObjectId(plmProgramProject.getId());
                                generateFileDto(fileDto, plmFile);
                            } else if (shareObject.getParentObjectType().toString().equals("PROJECTACTIVITY")) {
                                PLMActivity plmActivity = activityRepository.findOne(shareObject.getParentObjectId());
                                fileDto.setType(PLMObjectType.PROJECTACTIVITY);
                                fileDto.setParentObjectName(plmActivity.getName());
                                fileDto.setParentObjectId(plmActivity.getId());
                                generateFileDto(fileDto, plmFile);
                            } else if (shareObject.getParentObjectType().toString().equals("PROJECTTASK")) {
                                PLMTask plmTask = taskRepository.findOne(shareObject.getParentObjectId());
                                fileDto.setType(PLMObjectType.PROJECTTASK);
                                fileDto.setParentObjectName(plmTask.getName());
                                fileDto.setParentObjectId(plmTask.getId());
                                generateFileDto(fileDto, plmFile);
                            }
                            fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().name()));
                            fileDto.setParentFile(plmFile.getParentFile());
                            fileDto.setFileType(plmFile.getFileType());
                            fileDtos.add(fileDto);
                        }
                );
            }
        } else {


            List<PLMDocument> plmDocuments = documentRepository.getFilesByParentAndFileTypeOrderByModifiedDateDesc(folderId, "FILE");
            plmDocuments.forEach(plmFile -> {
                        FileDto fileDto = new FileDto();
                        fileDto.setId(plmFile.getId());
                        fileDto.setName(plmFile.getName());
                        fileDto.setSize(plmFile.getSize());
                        fileDto.setDescription(plmFile.getDescription());
                        fileDto.setVersion(plmFile.getVersion());
                        fileDto.setRevision(plmFile.getRevision());
                        fileDto.setLifeCyclePhase(plmFile.getLifeCyclePhase());
                        fileDto.setCreatedDate(plmFile.getCreatedDate());
                        fileDto.setModifiedDate(plmFile.getModifiedDate());
                        fileDto.setParentObjectId(plmFile.getParentFile());
                        fileDto.setParentFile(plmFile.getParentFile());
                        fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().name()));
                        fileDto.setType(PLMObjectType.valueOf(plmFile.getObjectType().name()));
                        Person createPerson = personRepository.findOne(plmFile.getCreatedBy());
                        Person modifiedPerson = personRepository.findOne(plmFile.getModifiedBy());
                        fileDto.setModifiedByName(modifiedPerson.getFullName());
                        fileDto.setCreatedByName(createPerson.getFullName());
                        fileDto.setFileType(plmFile.getFileType());
                        fileDtos.add(fileDto);
                    }
            );
        }


        return fileDtos;
    }


}
