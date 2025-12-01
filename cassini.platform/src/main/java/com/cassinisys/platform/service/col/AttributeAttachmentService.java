package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author reddy
 */
@Service
public class AttributeAttachmentService implements CrudService<AttributeAttachment, Integer> {

    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Override
    @Transactional
    public AttributeAttachment create(AttributeAttachment attributeAttachment) {
        return attributeAttachmentRepository.save(attributeAttachment);
    }

    @Transactional
    public List<AttributeAttachment> addAttributeMultipleAttachments(Integer objectId, Integer attributeDef, ObjectType objectType, List<MultipartFile> files) {
        checkNotNull(objectId);
        checkNotNull(objectType);
        CassiniObject object = objectRepository.findByIdAndObjectType(objectId,
                objectType);
        if (object == null) {
            throw new ResourceNotFoundException();
        }
        List<AttributeAttachment> attachments = new ArrayList<AttributeAttachment>();
        Login login = sessionWrapper.getSession().getLogin();
        try {
            for (MultipartFile file : files) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                AttributeAttachment attributeAttachment = new AttributeAttachment();
                attributeAttachment.setAttributeDef(attributeDef);
                attributeAttachment.setAddedBy(login.getPerson().getId());
                attributeAttachment.setAddedOn(new Date());
                attributeAttachment.setName(name);

                int index = name.lastIndexOf('.');
                if (index != -1) {
                    String ext = name.substring(index);
                    ext = ext.toLowerCase();
                    attributeAttachment.setExtension(ext);
                }

                attributeAttachment.setSize(new Long(file.getSize()).intValue());
                attributeAttachment.setObjectId(objectId);
                attributeAttachment.setObjectType(objectType);
                attributeAttachment = attributeAttachmentRepository.save(attributeAttachment);
                attachments.add(attributeAttachment);

                saveAttachmentToDisk(attributeAttachment, file);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return attachments;
    }


    @Transactional
    public List<AttributeAttachment> addAttributeAttachments(Integer objectId, Integer attributeDef, ObjectType objectType, Map<String, MultipartFile> fileMap) {
        checkNotNull(objectId);
        checkNotNull(objectType);
        checkNotNull(fileMap);
        CassiniObject object = objectRepository.findByIdAndObjectType(objectId,
                objectType);
        if (object == null) {
            throw new ResourceNotFoundException();
        }
        List<AttributeAttachment> attachments = new ArrayList<AttributeAttachment>();
        Login login = sessionWrapper.getSession().getLogin();
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                AttributeAttachment attributeAttachment = new AttributeAttachment();
                attributeAttachment.setAttributeDef(attributeDef);
                attributeAttachment.setAddedBy(login.getPerson().getId());
                attributeAttachment.setAddedOn(new Date());
                attributeAttachment.setName(name);

                int index = name.lastIndexOf('.');
                if (index != -1) {
                    String ext = name.substring(index);
                    ext = ext.toLowerCase();
                    attributeAttachment.setExtension(ext);
                }

                attributeAttachment.setSize(new Long(file.getSize()).intValue());
                attributeAttachment.setObjectId(objectId);
                attributeAttachment.setObjectType(objectType);
                attributeAttachment = attributeAttachmentRepository.save(attributeAttachment);
                attachments.add(attributeAttachment);

                saveAttachmentToDisk(attributeAttachment, file);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return attachments;
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

    private File getAttachmentFile(Integer objectId, Attachment attachment) {
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + "attachments" + File.separator + objectId;
        File attachmentsFolder = new File(path);
        if (!attachmentsFolder.exists()) {
            attachmentsFolder.mkdirs();
        }
        return new File(attachmentsFolder, Integer.toString(attachment.getId()));
    }

    @Override
    @Transactional
    public AttributeAttachment update(AttributeAttachment attributeAttachment) {
        return attributeAttachmentRepository.save(attributeAttachment);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        attributeAttachmentRepository.delete(id);
    }

    @Transactional
    public void deleteAttachment(Integer id, Integer objectId) {

        AttributeAttachment attributeAttachment = attributeAttachmentRepository.findOne(id);

        ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(objectId, attributeAttachment.getAttributeDef());

        List<Integer> attributeAttachmentIds = new ArrayList<>();
        if (objectAttribute != null) {
            if (objectAttribute.getAttachmentValues().length > 0) {

                for (Integer integer : objectAttribute.getAttachmentValues()) {
                    if (!integer.equals(id)) {
                        attributeAttachmentIds.add(integer);
                    }
                }

                if (attributeAttachmentIds.size() > 0) {
                    objectAttribute.setAttachmentValues(attributeAttachmentIds.stream().filter(Objects::nonNull).toArray((Integer[]::new)));
                } else {
                    objectAttribute.setAttachmentValues(attributeAttachmentIds.stream().filter(Objects::isNull).toArray((Integer[]::new)));
                }
            }
        }

        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + "attachments" + File.separator + objectId + File.separator + id;

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        attributeAttachmentRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AttributeAttachment get(Integer id) {
        return attributeAttachmentRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttributeAttachment> getAll() {
        return attributeAttachmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AttributeAttachment> getAttributeAttachmentsByObjectIdAndAttributeId(Integer objectId, Integer attributeDef) {
        return attributeAttachmentRepository.findByObjectIdAndAttributeDef(objectId, attributeDef);
    }

    @Transactional(readOnly = true)
    public List<AttributeAttachment> getAttributeAttachmentsByObjectId(Integer objectId) {
        return attributeAttachmentRepository.findByObjectId(objectId);
    }

    @Transactional(readOnly = true)
    public List<AttributeAttachment> getMultipleAttributeAttachments(List<Integer> attachmentIds) {
        return attributeAttachmentRepository.findByIdIn(attachmentIds);
    }
}
