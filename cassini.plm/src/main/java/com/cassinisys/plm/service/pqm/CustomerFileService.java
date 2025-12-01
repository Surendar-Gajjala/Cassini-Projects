package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.CustomerEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.PQMCustomer;
import com.cassinisys.plm.model.pqm.PQMCustomerFile;
import com.cassinisys.plm.repo.plm.FileDownloadHistoryRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.CustomerFileRepository;
import com.cassinisys.plm.repo.pqm.PQMCustomerRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by GSR on 13-12-2020.
 */
@Service
@Transactional
public class CustomerFileService implements CrudService<PQMCustomerFile, Integer>, PageableService<PQMCustomerFile, Integer> {

    @Autowired
    private CustomerFileRepository customerFileRepository;
    @Autowired
    private PQMCustomerRepository customerRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private FileHelpers fileHelpers;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ObjectFileService qualityFileService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    public PQMCustomerFile create(PQMCustomerFile customerFile) {
        checkNotNull(customerFile);
        customerFile.setId(null);
        customerFile = customerFileRepository.save(customerFile);
        PQMCustomer customer = customerRepository.findOne(customerFile.getCustomer());
        return customerFile;
    }

    @Override
    public PQMCustomerFile update(PQMCustomerFile customerFile) {
        PQMCustomer customer = customerRepository.findOne(customerFile.getCustomer());
        PQMCustomerFile customerFile1 = customerFileRepository.findOne(customerFile.getId());
        customerFile = customerFileRepository.save(customerFile1);
        return customerFile;
    }

    @Transactional
    public PQMCustomerFile updateFile(Integer fileId, PQMCustomerFile customerFile) {
        PQMCustomerFile customerFile1 = customerFileRepository.findOne(fileId);
        PQMCustomer customer = customerRepository.findOne(customerFile1.getCustomer());
        if (!customerFile1.getLocked().equals(customerFile.getLocked())) {
                /* App events */
            if (customerFile.getLocked()) {
                applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFileLockedEvent(customer.getId(), customerFile1));
            } else {
                applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFileUnlockedEvent(customer.getId(), customerFile1));
            }
        }
        customerFile1.setDescription(customerFile.getDescription());
        customerFile1.setLocked(customerFile.getLocked());
        customerFile1.setLockedBy(customerFile.getLockedBy());
        customerFile1.setLockedDate(customerFile.getLockedDate());
        customerFile = customerFileRepository.save(customerFile1);
        return customerFile;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PQMCustomerFile customerFile = customerFileRepository.findOne(id);
        if (customerFile == null) {
            throw new ResourceNotFoundException();
        }
        customerFileRepository.delete(id);
        PQMCustomer customer = customerRepository.findOne(customerFile.getCustomer());
    }

    public PQMCustomerFile updateFileName(Integer id, String newFileName) throws IOException {
        PQMCustomerFile file1 = customerFileRepository.findOne(id);
        PQMCustomerFile oldFile = (PQMCustomerFile) Utils.cloneObject(file1, PQMCustomerFile.class);
        String oldName = file1.getName();
        file1.setLatest(false);
        PQMCustomerFile plmCustomerFile = customerFileRepository.save(file1);
        PQMCustomerFile customerFile = (PQMCustomerFile) Utils.cloneObject(plmCustomerFile, PQMCustomerFile.class);
        if (customerFile != null) {
            customerFile.setId(null);
            customerFile.setName(newFileName);
            customerFile.setVersion(file1.getVersion() + 1);
            customerFile.setLatest(true);
            customerFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            customerFile = customerFileRepository.save(customerFile);
            if (customerFile.getParentFile() != null) {
                PQMCustomerFile parent = customerFileRepository.findOne(customerFile.getParentFile());
                parent.setModifiedDate(customerFile.getModifiedDate());
                parent = customerFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(file1.getId(), customerFile.getId());
            String dir = "";
            if (customerFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(customerFile.getCustomer(), id);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + customerFile.getCustomer();
            }
            dir = dir + File.separator + customerFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + customerFile.getCustomer() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PQMCustomer customer = customerRepository.findOne(customerFile.getCustomer());
            /* App Events */
            applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFileRenamedEvent(customer.getId(), oldFile, customerFile, "Rename"));
        }
        return customerFile;
    }

    public List<PQMCustomerFile> replaceCustomerFiles(Integer customerId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PQMCustomerFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PQMCustomerFile oldFile = null;
        PQMCustomer customer = customerRepository.findOne(customerId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        PQMCustomerFile plmCustomerFile = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    PQMCustomerFile customerFile = null;
                    plmCustomerFile = customerFileRepository.findOne(fileId);
                    String oldName = "";
                    if (plmCustomerFile != null && plmCustomerFile.getParentFile() != null) {
                        customerFile = customerFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        customerFile = customerFileRepository.findByCustomerAndNameAndParentFileIsNullAndLatestTrue(customerId, name);
                    }
                    if (customerFile != null) {
                        comments = commentRepository.findAllByObjectId(customerFile.getId());
                    }
                    if (plmCustomerFile != null) {
                        oldName = plmCustomerFile.getName();
                        plmCustomerFile.setLatest(false);
                        plmCustomerFile = customerFileRepository.save(plmCustomerFile);
                    }
                    if (customerFile != null) {
                        comments = commentRepository.findAllByObjectId(customerFile.getId());
                    }
                    customerFile = new PQMCustomerFile();
                    customerFile.setName(name);
                    if (plmCustomerFile != null && plmCustomerFile.getParentFile() != null) {
                        customerFile.setParentFile(plmCustomerFile.getParentFile());
                    }
                    if (plmCustomerFile != null) {
                        customerFile.setFileNo(plmCustomerFile.getFileNo());
                        customerFile.setVersion(plmCustomerFile.getVersion() + 1);
                        customerFile.setReplaceFileName(plmCustomerFile.getName() + " Replaced to " + name);
                        oldFile = JsonUtils.cloneEntity(plmCustomerFile, PQMCustomerFile.class);
                    }
                    customerFile.setCreatedBy(login.getPerson().getId());
                    customerFile.setModifiedBy(login.getPerson().getId());
                    customerFile.setCustomer(customerId);
                    customerFile.setSize(file.getSize());
                    customerFile.setFileType("FILE");
                    customerFile = customerFileRepository.save(customerFile);
                    if (customerFile.getParentFile() != null) {
                        PQMCustomerFile parent = customerFileRepository.findOne(customerFile.getParentFile());
                        parent.setModifiedDate(customerFile.getModifiedDate());
                        parent = customerFileRepository.save(parent);
                    }
                    if (plmCustomerFile != null) {
                        qualityFileService.copyFileAttributes(plmCustomerFile.getId(), customerFile.getId());
                    }
                    String dir = "";
                    if (plmCustomerFile != null && plmCustomerFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(customerId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + customerId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + customerFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(customerFile);
                      /* App Events */
                    applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFileRenamedEvent(customer.getId(), plmCustomerFile, customerFile, "Replace"));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer customerPartId, Integer fileId) {
        String path = "";
        PQMCustomerFile customerPartFile = customerFileRepository.findOne(fileId);
        if (customerPartFile.getParentFile() != null) {
            path = utilService.visitParentFolder(customerPartId, customerPartFile.getParentFile(), path);
        } else {
            path = File.separator + customerPartId;
        }
        return path;
    }

    public void deleteCustomerFile(Integer customerId, Integer id) {
        checkNotNull(id);
        PQMCustomerFile customerFile = customerFileRepository.findOne(id);
        PQMCustomer customer = customerRepository.findOne(customerId);
        List<PQMCustomerFile> customerFiles = customerFileRepository.findByCustomerAndFileNo(customerId, customerFile.getFileNo());
        for (PQMCustomerFile file : customerFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(customerId, file.getId());
            fileSystemService.deleteDocumentFromDiskFolder(file.getId(), dir);
            customerFileRepository.delete(file.getId());
        }
        if (customerFile.getParentFile() != null) {
            PQMCustomerFile parent = customerFileRepository.findOne(customerFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = customerFileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFileDeletedEvent(customer.getId(), customerFile));
    }

    @Override
    public PQMCustomerFile get(Integer id) {
        checkNotNull(id);
        PQMCustomerFile customerFile = customerFileRepository.findOne(id);
        if (customerFile == null) {
            throw new ResourceNotFoundException();
        }
        return customerFile;
    }

    @Override
    public List<PQMCustomerFile> getAll() {
        return customerFileRepository.findAll();
    }

    public List<PQMCustomerFile> getAllFileVersions(Integer customerId, String name) {
        return customerFileRepository.findAllByCustomerAndNameOrderByCreatedDateDesc(customerId, name);
    }

    @Override
    public Page<PQMCustomerFile> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null)
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        return customerFileRepository.findAll(pageable);
    }

    public List<PQMCustomerFile> findByCustomer(Integer customer) {
        List<PQMCustomerFile> customerFiles = customerFileRepository.findByCustomerAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(customer);
        customerFiles.forEach(customerFile -> {
            customerFile.setParentObject(PLMObjectType.CUSTOMER);
            if (customerFile.getFileType().equals("FOLDER")) {
                customerFile.setCount(customerFileRepository.getChildrenCountByParentFileAndLatestTrue(customerFile.getId()));
                customerFile.setCount(customerFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(customerFile.getCustomer(), customerFile.getId()));
            }
        });
        return customerFiles;
    }

    public List<PQMCustomerFile> findByCustomerLatest(Integer customer) {
        List<PQMCustomerFile> customerFiles = customerFileRepository.findByCustomerAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(customer);
        customerFiles.forEach(customerFile -> {
            customerFile.setParentObject(PLMObjectType.CUSTOMER);
            if (customerFile.getFileType().equals("FOLDER")) {
                customerFile.setCount(customerFileRepository.getChildrenCountByParentFileAndLatestTrue(customerFile.getId()));
            }
        });
        return customerFiles;
    }

    public List<PQMCustomerFile> uploadFiles(Integer customerId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PQMCustomerFile> uploadedFiles = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        PQMCustomer customer = customerRepository.findOne(customerId);
        String[] fileExtension = null;
        List<PQMCustomerFile> newFiles = new ArrayList<>();
        List<PQMCustomerFile> versionedFiles = new ArrayList<>();
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fNames = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    boolean versioned = false;
                    PQMCustomerFile customerFile = null;
                    if (folderId == 0) {
                        customerFile = customerFileRepository.findByCustomerAndNameAndParentFileIsNullAndLatestTrue(customerId, name);
                    } else {
                        customerFile = customerFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (customerFile != null) {
                        customerFile.setLatest(false);
                        Integer oldVersion = customerFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = customerFile.getFileNo();
                        oldFile = customerFile.getId();
                /*customerFile.setVersion(newVersion);*/
                        versioned = true;
                        customerFileRepository.save(customerFile);
                    }
                    if (customerFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    customerFile = new PQMCustomerFile();
                    customerFile.setName(name);
                    customerFile.setFileNo(autoNumber1);
                    customerFile.setCreatedBy(login.getPerson().getId());
                    customerFile.setModifiedBy(login.getPerson().getId());
            /*customerFile.setVersion(1);*/
                    customerFile.setVersion(version);
                    customerFile.setCustomer(customerId);
                    customerFile.setSize(file.getSize());
                    customerFile.setFileType("FILE");
                    if (folderId != 0) {
                        customerFile.setParentFile(folderId);
                    }
                    customerFile = customerFileRepository.save(customerFile);
                    if (customerFile.getParentFile() != null) {
                        PQMCustomerFile parent = customerFileRepository.findOne(customerFile.getParentFile());
                        parent.setModifiedDate(customerFile.getModifiedDate());
                        parent = customerFileRepository.save(parent);
                    }
                    if (customerFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, customerFile.getId());
                    }
                    if (fNames == null) {
                        fNames = customerFile.getName();

                    } else {
                        fNames = fNames + "  , " + customerFile.getName();
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + customerId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(customerId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + customerFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(customerFile);
                    if (versioned) {
                        versionedFiles.add(customerFile);
                    } else {
                        newFiles.add(customerFile);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          /* App Events */
        if (newFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFilesAddedEvent(customer.getId(), newFiles));
        }
        if (versionedFiles.size() > 0) {
            applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFilesVersionedEvent(customer.getId(), versionedFiles));
        }
        return uploadedFiles;
    }

    public File getCustomerFile(Integer customerId, Integer fileId) {
        checkNotNull(customerId);
        checkNotNull(fileId);
        PQMCustomer customer = customerRepository.findOne(customerId);
        if (customer == null) {
            throw new ResourceNotFoundException();
        }
        PQMCustomerFile customerFile = customerFileRepository.findOne(fileId);
        if (customerFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(customerId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<PQMCustomerFile> findByCustomerFilesName(Integer customerId, String name) {
        return customerFileRepository.findByCustomerAndNameContainingIgnoreCaseAndLatestTrue(customerId, name);
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer customerId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PQMCustomerFile plmItemFile = customerFileRepository.findOne(fileId);
        PQMCustomer customer = customerRepository.findOne(plmItemFile.getCustomer());
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFileDownloadedEvent(customer.getId(), plmItemFile));
        return plmFileDownloadHistory;
    }

    public List<PQMCustomerFile> getAllFileVersionComments(Integer customerId, Integer fileId, ObjectType objectType) {
        List<PQMCustomerFile> itemFiles = new ArrayList<>();
        PQMCustomerFile customerFile = customerFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(customerFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(customerFile.getId());
        if (comments.size() > 0) {
            customerFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            customerFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(customerFile);
        List<PQMCustomerFile> files = customerFileRepository.findByCustomerAndFileNoAndLatestFalseOrderByCreatedDateDesc(customerFile.getCustomer(), customerFile.getFileNo());
        if (files.size() > 0) {
            for (PQMCustomerFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                itemFiles.add(file);
            }
        }
        return itemFiles;
    }

    public PQMCustomerFile createCustomerFolder(Integer customerId, PQMCustomerFile customerFile) {
        customerFile.setId(null);
        String folderNumber = null;
        PQMCustomer customer = customerRepository.findOne(customerId);
        PQMCustomerFile existFolderName = null;
        if (customerFile.getParentFile() != null) {
            existFolderName = customerFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndCustomerAndLatestTrue(customerFile.getName(), customerFile.getParentFile(), customerId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(customerFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", customerFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = customerFileRepository.findByNameEqualsIgnoreCaseAndCustomerAndLatestTrue(customerFile.getName(), customerId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", customerFile.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        customerFile.setCustomer(customerId);
        customerFile.setFileNo(folderNumber);
        customerFile.setFileType("FOLDER");
        customerFile = customerFileRepository.save(customerFile);
        if (customerFile.getParentFile() != null) {
            PQMCustomerFile parent = customerFileRepository.findOne(customerFile.getParentFile());
            parent.setModifiedDate(customerFile.getModifiedDate());
            parent = customerFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(customerId, customerFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFoldersAddedEvent(customer.getId(), customerFile));
        return customerFile;
    }

    private String getParentFileSystemPath(Integer customerId, Integer fileId) {
        String path = "";
        PQMCustomerFile customerFile = customerFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (customerFile.getParentFile() != null) {
            path = utilService.visitParentFolder(customerId, customerFile.getParentFile(), path);
        } else {
            path = File.separator + customerId + File.separator + customerFile.getId();
        }
        return path;
    }

    public PLMFile moveCustomerFileToFolder(Integer id, PQMCustomerFile plmCustomerFile) throws Exception {
        PQMCustomerFile file = customerFileRepository.findOne(plmCustomerFile.getId());
        PQMCustomerFile existFile = (PQMCustomerFile) Utils.cloneObject(file, PQMCustomerFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getCustomer(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getCustomer() + File.separator + existFile.getId();
        }
        if (plmCustomerFile.getParentFile() != null) {
            PQMCustomerFile existItemFile = customerFileRepository.findByParentFileAndNameAndLatestTrue(plmCustomerFile.getParentFile(), plmCustomerFile.getName());
            PQMCustomerFile folder = customerFileRepository.findOne(plmCustomerFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmCustomerFile = customerFileRepository.save(plmCustomerFile);
            }
        } else {
            PQMCustomerFile existItemFile = customerFileRepository.findByCustomerAndNameAndParentFileIsNullAndLatestTrue(plmCustomerFile.getCustomer(), plmCustomerFile.getName());
            PQMCustomer customer = customerRepository.findOne(plmCustomerFile.getCustomer());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmCustomerFile = customerFileRepository.save(plmCustomerFile);
            }
        }
        if (plmCustomerFile != null) {
            String dir = "";
            if (plmCustomerFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmCustomerFile.getCustomer(), plmCustomerFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmCustomerFile.getCustomer() + File.separator + plmCustomerFile.getId();
            }
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;
            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            File e = new File(oldFileDir);
            System.gc();
            Thread.sleep(1000L);
            FileUtils.deleteQuietly(e);
            List<PQMCustomerFile> oldVersionFiles = customerFileRepository.findByCustomerAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getCustomer(), existFile.getFileNo());
            for (PQMCustomerFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getCustomer(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getCustomer() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmCustomerFile.getCustomer(), plmCustomerFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmCustomerFile.getParentFile());
                oldVersionFile = customerFileRepository.save(oldVersionFile);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }
                instream = null;
                outstream = null;
                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                e = new File(oldFileDir);
                System.gc();
                Thread.sleep(1000L);
                FileUtils.deleteQuietly(e);
            }
        }
        return plmCustomerFile;
    }

    public List<PQMCustomerFile> getCustomerFolderChildren(Integer folderId) {
        List<PQMCustomerFile> customerFiles = customerFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        customerFiles.forEach(customerFile -> {
            customerFile.setParentObject(PLMObjectType.CUSTOMER);
            if (customerFile.getFileType().equals("FOLDER")) {
                customerFile.setCount(customerFileRepository.getChildrenCountByParentFileAndLatestTrue(customerFile.getId()));
                customerFile.setCount(customerFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(customerFile.getCustomer(), customerFile.getId()));
            }
        });
        return customerFiles;
    }

    public void deleteFolder(Integer customerId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(customerId, folderId);
        PQMCustomerFile file = customerFileRepository.findOne(folderId);
        PQMCustomer customer = customerRepository.findOne(customerId);
        List<PQMCustomerFile> customerFiles = customerFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) customerFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        customerFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        applicationEventPublisher.publishEvent(new CustomerEvents.CustomerFoldersDeletedEvent(customer.getId(), file));
    }

    public void generateZipFile(Integer customerId, HttpServletResponse response) throws FileNotFoundException, IOException {
        PQMCustomer plmCustomer = customerRepository.findOne(customerId);
        List<PQMCustomerFile> plmCustomerFiles = customerFileRepository.findByCustomerAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(customerId);
        ArrayList<String> fileList = new ArrayList<>();
        plmCustomerFiles.forEach(plmCustomerFile -> {
            File file = getCustomerFile(customerId, plmCustomerFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = plmCustomer.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "CUSTOMER",customerId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public PQMCustomerFile getLatestUploadedCustomerFile(Integer customerId, Integer fileId) {
        PQMCustomerFile customerFile = customerFileRepository.findOne(fileId);
        PQMCustomerFile plmCustomerFile = customerFileRepository.findByCustomerAndFileNoAndLatestTrue(customerFile.getCustomer(), customerFile.getFileNo());
        return plmCustomerFile;
    }

    public PLMFile updateFileDescription(Integer customerId, Integer id, PQMCustomerFile plmCustomerFile) {
        PLMFile file = fileRepository.findOne(id);
        if (file != null) {
            file.setDescription(plmCustomerFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PQMCustomerFile> pasteFromClipboard(Integer customerId, Integer fileId, List<PLMFile> files) {
        List<Integer> folderIds = new ArrayList<>();
        files.forEach(file -> {
            if (file.getFileType().equals("FOLDER")) {
                folderIds.add(file.getId());
            }
        });
        List<PQMCustomerFile> fileList = new ArrayList<>();
        for (PLMFile file : files) {
            Boolean canCreate = true;
            if (file.getParentFile() != null && folderIds.indexOf(file.getParentFile()) != -1) {
                canCreate = false;
            }
            if (file.getFileType().equals("FILE") && canCreate) {
                PQMCustomerFile customerFile = new PQMCustomerFile();
                PQMCustomerFile existFile = null;
                if (fileId != 0) {
                    customerFile.setParentFile(fileId);
                    existFile = customerFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndCustomerAndLatestTrue(file.getName(), fileId, customerId);
                } else {
                    existFile = customerFileRepository.findByCustomerAndNameAndParentFileIsNullAndLatestTrue(customerId, file.getName());
                }
                if (existFile == null) {
                    customerFile.setName(file.getName());
                    customerFile.setDescription(file.getDescription());
                    customerFile.setCustomer(customerId);
                    customerFile.setVersion(1);
                    customerFile.setSize(file.getSize());
                    customerFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    customerFile.setFileNo(autoNumber1);
                    customerFile.setFileType("FILE");
                    customerFile = customerFileRepository.save(customerFile);
                    customerFile.setParentObject(PLMObjectType.CUSTOMER);
                    fileList.add(customerFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + customerId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (customerFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(customerId, customerFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + customerId + File.separator + customerFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                }
            } else if (file.getFileType().equals("FOLDER") && canCreate) {
                PQMCustomerFile customerFile = new PQMCustomerFile();
                PQMCustomerFile existFile = null;
                if (fileId != 0) {
                    customerFile.setParentFile(fileId);
                    existFile = customerFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndCustomerAndLatestTrue(file.getName(), fileId, customerId);
                } else {
                    existFile = customerFileRepository.findByCustomerAndNameAndParentFileIsNullAndLatestTrue(customerId, file.getName());
                }
                if (existFile == null) {
                    customerFile.setName(file.getName());
                    customerFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    customerFile.setVersion(1);
                    customerFile.setSize(0L);
                    customerFile.setCustomer(customerId);
                    customerFile.setFileNo(folderNumber);
                    customerFile.setFileType("FOLDER");
                    customerFile = customerFileRepository.save(customerFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + customerId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(customerId, customerFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(customerFile);
                    copyFolderFiles(customerId, file.getParentObject(), file.getId(), customerFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer id, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> plmFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        plmFiles.forEach(plmFile -> {
            PQMCustomerFile customerFile = new PQMCustomerFile();
            customerFile.setParentFile(parent);
            customerFile.setName(plmFile.getName());
            customerFile.setDescription(plmFile.getDescription());
            customerFile.setCustomer(id);
            String folderNumber = null;
            if (plmFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                customerFile.setVersion(1);
                customerFile.setFileNo(folderNumber);
                customerFile.setSize(plmFile.getSize());
                customerFile.setFileType("FILE");
                customerFile = customerFileRepository.save(customerFile);
                customerFile.setParentObject(PLMObjectType.CUSTOMER);
                plmFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmFile);

                String dir = "";
                if (customerFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(id, customerFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + id + File.separator + customerFile.getId();
                }
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    try {
                        fDir.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileInputStream instream = null;
                FileOutputStream outstream = null;
                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            } else {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                customerFile.setVersion(1);
                customerFile.setSize(0L);
                customerFile.setFileNo(folderNumber);
                customerFile.setFileType("FOLDER");
                customerFile = customerFileRepository.save(customerFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(id, customerFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(id, objectType, plmFile.getId(), customerFile.getId());
            }
        });
    }

    public void undoCopiedCustomerFiles(Integer ecoId, List<PQMCustomerFile> customerFiles) {
        customerFiles.forEach(customerFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(ecoId, customerFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(customerFile.getId(), dir);
            customerFileRepository.delete(customerFile.getId());
        });
    }
}