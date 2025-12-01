package com.cassinisys.platform.service.col;

import com.cassinisys.platform.config.AppProperties;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class AttachmentService implements
        PageableService<Attachment, Integer> {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;


    @Transactional
    public Attachment multiplePartFilesPost(Integer objectId, ObjectType objectType, MultipartFile file) {
        checkNotNull(objectId);
        checkNotNull(objectType);
        checkNotNull(file);
        Attachment attachment = new Attachment();
        CassiniObject object = objectRepository.findByIdAndObjectType(objectId,
                objectType);
        if (object == null) {
            throw new ResourceNotFoundException();
        }
        Login login = sessionWrapper.getSession().getLogin();
        try {
            String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
            attachment.setAddedBy(login.getPerson().getId());
            attachment.setAddedOn(new Date());
            attachment.setName(name);

            int index = name.lastIndexOf('.');
            if (index != -1) {
                String ext = name.substring(index);
                ext = ext.toLowerCase();
                attachment.setExtension(ext);
            }

            attachment.setSize(new Long(file.getSize()).intValue());
            attachment.setObjectId(objectId);
            attachment.setObjectType(objectType);
            if (objectType == ObjectType.valueOf("TESTRESULT") || objectType == ObjectType.valueOf("ITEM")) {
                Attachment attachment1 = attachmentRepository.findByObjectIdAndName(objectId, attachment.getName());
                if (attachment1 != null) {
                    attachmentRepository.delete(attachment1.getId());
                }
            }
            attachment = attachmentRepository.save(attachment);
            saveAttachmentToDisk(attachment, file);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return attachment;
    }


    @Transactional
    public List<Attachment> addAttachments(Integer objectId, ObjectType objectType, Map<String, MultipartFile> fileMap) {
        checkNotNull(objectId);
        checkNotNull(objectType);
        checkNotNull(fileMap);
        CassiniObject object = objectRepository.findByIdAndObjectType(objectId,
                objectType);
        if (object == null) {
            throw new ResourceNotFoundException();
        }
        List<Attachment> attachments = new ArrayList<Attachment>();
        Login login = sessionWrapper.getSession().getLogin();
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                Attachment attachment = new Attachment();
                attachment.setAddedBy(login.getPerson().getId());
                attachment.setAddedOn(new Date());
                attachment.setName(name);

                int index = name.lastIndexOf('.');
                if (index != -1) {
                    String ext = name.substring(index);
                    ext = ext.toLowerCase();
                    attachment.setExtension(ext);
                }

                attachment.setSize(new Long(file.getSize()).intValue());
                attachment.setObjectId(objectId);
                attachment.setObjectType(objectType);
                if (objectType == ObjectType.valueOf("TESTRESULT") || objectType == ObjectType.valueOf("ITEM") || objectType == ObjectType.valueOf("INWARDITEMBATCHNUMBER") || objectType == ObjectType.valueOf("IMITEMLOT")) {
                    Attachment attachment1 = attachmentRepository.findByObjectIdAndName(objectId, attachment.getName());
                    if (attachment1 != null) {
                        attachmentRepository.delete(attachment1.getId());
                    }
                }
                attachment = attachmentRepository.save(attachment);
                attachments.add(attachment);

                saveAttachmentToDisk(attachment, file);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return attachments;
    }

    @Transactional(readOnly = true)
    public void downloadAttachment(Integer id, HttpServletResponse response) {
        Attachment attachment = attachmentRepository.findOne(id);
        File file = getAttachmentFile(attachment.getObjectId(), attachment);
        if (file.exists()) {
            fileDownloadService.writeFileContentToResponse(response, attachment.getName(), file);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public void previewFile(Integer id, HttpServletResponse response) throws Exception {
        Attachment attachment = attachmentRepository.findOne(id);
        String fileName = attachment.getName();
        File file = getAttachmentFile(attachment.getObjectId(), attachment);
        if (file != null) {
            try {
                String e = URLDecoder.decode(fileName, "UTF-8");
                response.setHeader("Content-disposition", "inline; filename=" + e);
            } catch (UnsupportedEncodingException var6) {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            ServletOutputStream e1 = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        } else {
            throw new ResourceNotFoundException();
        }
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

    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        Attachment attachment = attachmentRepository.findOne(id);
        if (attachment == null) {
            throw new ResourceNotFoundException();
        }
        attachmentRepository.delete(attachment);
        File file = getAttachmentFile(attachment.getObjectId(), attachment);
        file.delete();

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

    @Transactional(readOnly = true)
    public List<Attachment> getAttachments(Integer objectId,
                                           ObjectType objectType, Pageable pageable) {
        checkNotNull(objectId);
        checkNotNull(objectType);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return attachmentRepository.findByObjectIdAndObjectType(objectId, objectType);
    }

    @Transactional(readOnly = true)
    public Attachment get(Integer id) {
        checkNotNull(id);
        Attachment attachment = attachmentRepository.findOne(id);
        if (attachment == null) {
            throw new ResourceNotFoundException();
        }
        return attachment;
    }

    @Transactional(readOnly = true)
    public List<Attachment> findAll(Integer objectId, ObjectType objectType) {
        checkNotNull(objectId);
        checkNotNull(objectType);
        return attachmentRepository.findByObjectIdAndObjectTypeOrderByAddedOnAsc(objectId, objectType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> findAll(Pageable pageable) {
        return attachmentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Attachment> getMultipleAttachments(List<Integer> attachmentIds) {
        return attachmentRepository.findByIdIn(attachmentIds);
    }

    @Transactional(readOnly = true)
    public List<Attachment> findByExtension(String ext) {
        return attachmentRepository.findByExtension(ext);
    }

    @Transactional(readOnly = true)
    public List<Attachment> findByExtensionIn(Iterable<String> exts) {
        return attachmentRepository.findByExtensionIn(exts);
    }
}
