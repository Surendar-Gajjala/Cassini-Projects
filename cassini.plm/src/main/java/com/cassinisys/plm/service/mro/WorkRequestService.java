package com.cassinisys.plm.service.mro;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.Media;
import com.cassinisys.platform.model.col.MediaType;
import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.WorkRequestEvents;
import com.cassinisys.plm.filtering.WorkRequestCriteria;
import com.cassinisys.plm.filtering.WorkRequestPredicateBuilder;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.WorkflowDefinitionMasterRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.plm.ItemService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Service
public class WorkRequestService implements CrudService<MROWorkRequest, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MROObjectTypeRepository mroObjectTypeRepository;
    @Autowired
    private MROWorkRequestRepository workRequestRepository;
    @Autowired
    private MROWorkRequestTypeRepository workRequestTypeRepository;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private WorkRequestPredicateBuilder workRequestPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private WorkflowDefinitionMasterRepository workflowDefinitionMasterRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private MROWorkOrderRepository mroWorkOrderRepository;
    @Autowired
    private AutoNumberService autoNumberService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#workRequest,'create')")
    public MROWorkRequest create(MROWorkRequest workRequest) {
        List<MROObjectAttribute> mroObjectAttributes = workRequest.getMroObjectAttributes();
        MROWorkRequest existWorkRequest = workRequestRepository.findByNumber(workRequest.getNumber());
        MROWorkRequest existWorkRequestName = workRequestRepository.findByName(workRequest.getName());
        if (existWorkRequest != null) {
            String message = messageSource.getMessage("workRequest_number_already_exists", null, "{0} WorkRequest Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existWorkRequest.getNumber());
            throw new CassiniException(result);
        }
        if (existWorkRequestName != null) {
            String message = messageSource.getMessage("workRequest_name_already_exists", null, "{0} WorkRequest Name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existWorkRequestName.getName());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(workRequest.getType().getAutoNumberSource().getId(), workRequest.getNumber());
        workRequest = workRequestRepository.save(workRequest);
        for (MROObjectAttribute attribute : mroObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(workRequest.getId(), attribute.getId().getAttributeDef()));
                mroObjectAttributeRepository.save(attribute);
            }
        }
        applicationEventPublisher.publishEvent(new WorkRequestEvents.WorkRequestCreatedEvent(workRequest));
        sendEmailToMaintenanceManager(workRequest);
        return workRequest;
    }

    private void sendEmailToMaintenanceManager(MROWorkRequest workRequest) {
        MROAsset asset = assetRepository.findOne(workRequest.getAsset());
        Person requestor = personRepository.findOne(workRequest.getRequestor());
        Person createdBy = personRepository.findOne(workRequest.getCreatedBy());
        List<Person> persons = new ArrayList<>();
        PersonGroup personGroup = personGroupRepository.findByNameEqualsIgnoreCaseAndParentIsNull("Maintenance Manager");
        if (personGroup != null) {
            List<GroupMember> groupMembers = groupMemberRepository.findByPersonGroup(personGroup);
            groupMembers.forEach(groupMember -> {
                Login login = loginRepository.findByPersonId(groupMember.getPerson().getId());
                if (login.getIsActive()) {
                    persons.add(groupMember.getPerson());
                }
            });
        }

        if (persons.size() > 0) {
            String[] recipientAddress = new String[persons.size()];
            String email = "";
            if (persons.size() > 0) {
                for (int i = 0; i < persons.size(); i++) {
                    Person person = persons.get(i);
                    if (email == "") {
                        email = person.getEmail();
                    } else {
                        email = email + "," + person.getEmail();
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
                Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.LOGO");
                if (preference != null) {
                    if (preference.getCustomLogo() != null) {
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                        File file = new File(url1.getPath());
                        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                            outputStream.write(preference.getCustomLogo());
                            companyLogo = ItemService.class.getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String createdDate = df.format(workRequest.getCreatedDate());
                final URL companyLogos = companyLogo;
                new Thread(() -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("host", host);
                    model.put("workRequest", workRequest);
                    model.put("asset", asset);
                    model.put("companyLogo", companyLogos);
                    model.put("persons", persons.size());
                    model.put("personName", persons.get(0).getFullName());
                    model.put("requestor", requestor.getFullName());
                    model.put("createdBy", createdBy.getFullName());
                    model.put("createdDate", createdDate);
                    Mail mail = new Mail();
                    mail.setMailToList(recipientAddress);
                    mail.setMailSubject("New Work Request");
                    mail.setTemplatePath("email/workRequestNotification.html");
                    mail.setModel(model);
                    mailService.sendEmail(mail);
                }).start();
            }
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#workRequest.id ,'edit')")
    public MROWorkRequest update(MROWorkRequest workRequest) {
        MROWorkRequest existWorkRequestName = workRequestRepository.findByName(workRequest.getName());
        if (existWorkRequestName != null) {
            String message = messageSource.getMessage("workRequest_name_already_exists", null, "{0} WorkRequest Name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existWorkRequestName.getName());
            throw new CassiniException(result);
        }
        MROWorkRequest oldWorkRequest = JsonUtils.cloneEntity(workRequestRepository.findOne(workRequest.getId()), MROWorkRequest.class);
        workRequest = workRequestRepository.save(workRequest);
        applicationEventPublisher.publishEvent(new WorkRequestEvents.WorkRequestBasicInfoUpdatedEvent(oldWorkRequest, workRequest));
        return workRequest;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        List<MROWorkOrder> workOrders = mroWorkOrderRepository.findByRequest(id);
        if (workOrders.size() > 0) {
            throw new CassiniException(messageSource.getMessage("work_request_already_in_use", null, "Work request already in use", LocaleContextHolder.getLocale()));
        } else {
            workRequestRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROWorkRequest get(Integer id) {
        MROWorkRequest workRequest = workRequestRepository.findOne(id);
        if (workRequest != null) {

            Integer[] objectIds = workRequest.getAttachments();
            List<Integer> attachmentIds = new ArrayList<>();
            if (objectIds.length > 0) {
                for (int i = 0; i < objectIds.length; i++) {
                    attachmentIds.add(objectIds[i]);
                }
            }
            workRequest.setAssetName(assetRepository.findOne(workRequest.getAsset()).getName());
            workRequest.getImages().addAll(mediaRepository.findByIdIn(attachmentIds));
            workRequest.getAttachmentList().addAll(attachmentRepository.findByIdIn(attachmentIds));
        }
        return workRequest;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkRequest> getAll() {
        return workRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkRequest> getAllPendingRequests() {
        List<Integer> workRequests = mroWorkOrderRepository.getWorkRequestIdsFromWorkOrder();
        if (workRequests.size() > 0) {
            return workRequestRepository.findByIdNotIn(workRequests);
        } else {
            return workRequestRepository.findAll();
        }
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkRequest> findMultiple(List<Integer> ids) {
        return workRequestRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkOrder> getWorkRequestWorkOrders(Integer id) {
        return mroWorkOrderRepository.findByRequest(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<WorkRequestDto> getAllWorkRequests(Pageable pageable, WorkRequestCriteria workRequestCriteria) {
        Predicate predicate = workRequestPredicateBuilder.build(workRequestCriteria, QMROWorkRequest.mROWorkRequest);
        Page<MROWorkRequest> workRequests = workRequestRepository.findAll(predicate, pageable);

        List<WorkRequestDto> workRequestDtos = new LinkedList<>();
        workRequests.getContent().forEach(workRequest -> {
            WorkRequestDto workRequestDto = new WorkRequestDto();
            workRequestDto.setId(workRequest.getId());
            workRequestDto.setName(workRequest.getName());
            workRequestDto.setNumber(workRequest.getNumber());
            workRequestDto.setType(workRequest.getType().getName());
            workRequestDto.setObjectType(workRequest.getObjectType().name());
            workRequestDto.setSubType(workRequest.getType().getName());
            workRequestDto.setDescription(workRequest.getDescription());
            workRequestDto.setPriority(workRequest.getPriority());
            workRequestDto.setStatus(workRequest.getStatus());
            workRequestDto.setAsset(assetRepository.findOne(workRequest.getAsset()).getName());
            workRequestDto.setRequestor(personRepository.findOne(workRequest.getRequestor()).getFullName());
            workRequestDto.setCreatedBy(personRepository.findOne(workRequest.getCreatedBy()).getFullName());
            workRequestDto.setCreatedDate(workRequest.getCreatedDate());
            workRequestDto.setModifiedBy(personRepository.findOne(workRequest.getModifiedBy()).getFullName());
            workRequestDto.setModifiedDate(workRequest.getModifiedDate());
            workRequestDtos.add(workRequestDto);
        });

        return new PageImpl<WorkRequestDto>(workRequestDtos, pageable, workRequests.getTotalElements());
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MROWorkRequest> getObjectsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MROWorkRequestType type = workRequestTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return workRequestRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MROWorkRequestType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MROWorkRequestType> children = workRequestTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MROWorkRequestType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    public MROWorkRequest uploadWorkRequestMediaFiles(Integer objectId, MultipartHttpServletRequest request) throws Exception {
        Map<String, MultipartFile> map = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(map.values());
        MROWorkRequest workRequest = workRequestRepository.findOne(objectId);
        List<Integer> integers = new ArrayList<>();
        if (files.size() > 0) {
            for (MultipartFile file : files) {

                String[] fileType = file.getContentType().split("/");
                if (fileType[0].equals("image") || fileType[0].equals("video")) {
                    String fname = file.getOriginalFilename();
                    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                    try {
                        byte[] picData = file.getBytes();

                        Media media = new Media();
                        media.setFileName(fname);
                        media.setData(picData);
                        media.setExtension(extension);
                        media.setObjectId(objectId);

                        if (fileType[0].equals("image")) {
                            media.setMediaType(MediaType.IMAGE);
                        } else {
                            media.setMediaType(MediaType.VIDEO);
                        }

                        media = mediaRepository.save(media);
                        integers.add(media.getId());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String fname = file.getOriginalFilename();
                    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                    Attachment attachment = new Attachment();
                    attachment.setAddedBy(workRequest.getCreatedBy());
                    attachment.setAddedOn(new Date());
                    attachment.setName(fname);

                    int index = fname.lastIndexOf('.');
                    if (index != -1) {
                        String ext = fname.substring(index);
                        ext = ext.toLowerCase();
                        attachment.setExtension(ext);
                    }

                    attachment.setSize(new Long(file.getSize()).intValue());
                    attachment.setObjectId(objectId);
                    attachment.setObjectType(ObjectType.ATTACHMENT);

                    attachment = attachmentRepository.save(attachment);
                    integers.add(attachment.getId());
                    saveAttachmentToDisk(attachment, file);
                }
            }
            /*Integer[] intArray = new Integer[integers.size()];*/
          /*  intArray = integers.toArray(intArray);
            workRequest.setAttachments(intArray);*/


            Integer[] attachmentIds = workRequest.getAttachments();
            List<Integer> workRequestAttachIds = new ArrayList<>();
            if (attachmentIds.length > 0) {
                for (int i = 0; i < attachmentIds.length; i++) {
                    workRequestAttachIds.add(attachmentIds[i]);
                }
            }

            for (Integer attachId : integers) {
                if (workRequestAttachIds.indexOf(attachId) == -1) {
                    workRequestAttachIds.add(attachId);
                }
            }
            Integer[] intArray = new Integer[workRequestAttachIds.size()];
            intArray = workRequestAttachIds.toArray(intArray);
            workRequest.setAttachments(intArray);


            workRequestRepository.save(workRequest);
        }
        return workRequest;
    }

    private void saveAttachmentToDisk(Attachment attachment,
                                      MultipartFile multipartFile) {
        try {
            File file = getAttachmentFile(attachment.getObjectId(), attachment);
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(
                    file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    public File getAttachmentFile(Integer objectId, Attachment attachment) {
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + "attachments" + File.separator + objectId;
        File attachmentsFolder = new File(path);
        if (!attachmentsFolder.exists()) {
            attachmentsFolder.mkdirs();
        }
        return new File(attachmentsFolder, Integer.toString(attachment.getId()));
    }
}
