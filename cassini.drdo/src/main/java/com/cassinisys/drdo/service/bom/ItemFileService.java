package com.cassinisys.drdo.service.bom;

import com.cassinisys.drdo.model.bom.ItemFile;
import com.cassinisys.drdo.model.bom.ItemRevision;
import com.cassinisys.drdo.repo.bom.FileRepository;
import com.cassinisys.drdo.repo.bom.ItemFileRepository;
import com.cassinisys.drdo.repo.bom.ItemRepository;
import com.cassinisys.drdo.repo.bom.ItemRevisionRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 22/12/15.
 */
@Service
public class ItemFileService implements CrudService<ItemFile, Integer>,
        PageableService<ItemFile, Integer> {

    @Autowired
    private ItemFileRepository itemFileRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FileRepository fileRepository;

    @Override
    @Transactional
    public ItemFile create(ItemFile pdmItemFile) {
        pdmItemFile.setId(null);
        pdmItemFile = itemFileRepository.save(pdmItemFile);

        return pdmItemFile;
    }

    @Override
    @Transactional
    public ItemFile update(ItemFile pdmItemFile) {
        pdmItemFile = itemFileRepository.save(pdmItemFile);
        return pdmItemFile;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ItemFile pdmItemFile = itemFileRepository.findOne(id);
        ItemRevision item = itemRevisionRepository.findOne(pdmItemFile.getItem());

        itemFileRepository.delete(id);

        List<ItemFile> files = findByItem(item);
        if (files.size() == 0) {
            item.setHasFiles(Boolean.FALSE);
            itemRevisionRepository.save(item);
        }
    }


    @Transactional
    public void deleteItemFile(Integer itemId, Integer id) {
        checkNotNull(id);
        ItemFile plmItemFile = itemFileRepository.findOne(id);

        List<ItemFile> itemFiles = itemFileRepository.findAllByItemAndName(itemId, plmItemFile.getName());
        for (ItemFile itemFile : itemFiles) {
            if (itemFile == null) {
                throw new ResourceNotFoundException();
            }
            itemFileRepository.delete(itemFile.getId());
        }

        ItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        if (itemRevision != null) {
            List<ItemFile> files = itemFileRepository.findByItem(itemRevision.getId());
            if (files.size() == 0) {
                itemRevision.setHasFiles(false);
            }
            itemRevision = itemRevisionRepository.save(itemRevision);
        }
    }

    @Override
    public ItemFile get(Integer id) {
        return itemFileRepository.findOne(id);
    }

    @Override
    public List<ItemFile> getAll() {
        return itemFileRepository.findAll();
    }

    @Override
    public Page<ItemFile> findAll(Pageable pageable) {
        return itemFileRepository.findAll(pageable);
    }

    public List<ItemFile> findByItem(ItemRevision item) {
        return itemFileRepository.findByItemAndLatestTrueOrderByModifiedDateDesc(item.getId());
    }

    public List<ItemFile> findByItemandFileName(ItemRevision item, String name) {
        return itemFileRepository.findByItemAndNameContainingIgnoreCaseAndLatestTrue(item.getId(), name);
    }

    public List<ItemFile> findAllByItem(ItemRevision item) {
        return itemFileRepository.findByItem(item.getId());
    }

    @Transactional
    public ItemFile uploadContentAsFile(Integer itemId, String fileName, byte[] bytes) {
        ItemFile itemFile = null;
        try {
            List<Comment> comments = new ArrayList<>();
            ItemRevision revision = itemRevisionRepository.findOne(itemId);
            Login login = sessionWrapper.getSession().getLogin();
            itemFile = itemFileRepository.findByItemAndNameAndLatestTrue(revision.getId(), fileName);

            if (itemFile != null) {
                comments = commentRepository.findAllByObjectId(itemFile.getId());
            }

            Integer version = 1;
            if (itemFile != null) {
                itemFile.setLatest(false);
                Integer oldVersion = itemFile.getVersion();
                version = oldVersion + 1;
                itemFileRepository.save(itemFile);
            }

            itemFile = new ItemFile();
            itemFile.setName(fileName);
            itemFile.setCreatedBy(login.getPerson().getId());
            itemFile.setModifiedBy(login.getPerson().getId());
            itemFile.setItem(revision.getId());
            itemFile.setVersion(version);
            itemFile.setSize((long) bytes.length);
            itemFile = itemFileRepository.save(itemFile);

            for (Comment comment : comments) {
                Comment newComment = new Comment();
                newComment.setCommentedDate(comment.getCommentedDate());
                newComment.setCommentedBy(comment.getCommentedBy());
                newComment.setComment(comment.getComment());
                newComment.setObjectType(comment.getObjectType());
                newComment.setObjectId(itemFile.getId());
                newComment.setReplyTo(comment.getReplyTo());
                commentRepository.save(newComment);
            }


            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + revision.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }

            String path = dir + File.separator + itemFile.getId();
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(bytes, new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemFile;
    }

    @Transactional
    public List<ItemFile> uploadItemFiles(Integer itemId, Map<String, MultipartFile> fileMap) {
        List<ItemFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();

        ItemRevision revision = itemRevisionRepository.findOne(itemId);
        Login login = sessionWrapper.getSession().getLogin();

        String fileNames = null;

        try {
            for (MultipartFile file : fileMap.values()) {

                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                ItemFile itemFile = itemFileRepository.findByItemAndNameAndLatestTrue(revision.getId(), name);

                if (itemFile != null) {
                    comments = commentRepository.findAllByObjectId(itemFile.getId());
                }

                Integer version = 1;
                if (itemFile != null) {
                    itemFile.setLatest(false);
                    Integer oldVersion = itemFile.getVersion();
                    version = oldVersion + 1;
                    itemFileRepository.save(itemFile);
                }

                itemFile = new ItemFile();
                itemFile.setName(name);
                itemFile.setCreatedBy(login.getPerson().getId());
                itemFile.setModifiedBy(login.getPerson().getId());
                itemFile.setItem(revision.getId());
                itemFile.setVersion(version);
                itemFile.setSize(file.getSize());
                itemFile = itemFileRepository.save(itemFile);

                revision.setHasFiles(true);

                revision = itemRevisionRepository.save(revision);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + revision.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                String path = dir + File.separator + itemFile.getId();
                saveDocumentToDisk(file, path);

                uploaded.add(itemFile);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

       /* item.setHasFiles(Boolean.TRUE);*/
        /*itemRepository.save(item);*/

        return uploaded;
    }

    public List<ItemFile> getAllFileVersions(Integer itemRevision, String name) {
        List<ItemFile> plmItemFiles = itemFileRepository.findAllByItemAndName(itemRevision, name);
        for (ItemFile plmItemFile : plmItemFiles) {
            List<Comment> comments = commentRepository.findAllByObjectId(plmItemFile.getId());
            plmItemFile.setComments(comments);
        }
        return plmItemFiles;
    }


    @Transactional
    protected void saveDocumentToDisk(MultipartFile multipartFile, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    @Transactional
    protected void saveFileToDisk(File fileToSave, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(new FileInputStream(fileToSave), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }


    public File getItemFile(Integer itemId, Integer fileId) {
        checkNotNull(itemId);
        checkNotNull(fileId);
        ItemRevision revision = itemRevisionRepository.findOne(itemId);
        if (revision == null) {
            throw new ResourceNotFoundException();
        }
        ItemFile itemFile = itemFileRepository.findOne(fileId);
        if (itemFile == null) {
            throw new ResourceNotFoundException();
        }

        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + itemId + File.separator + fileId;
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional
    public ItemFile copyItemFile(ItemRevision oldItem, ItemRevision newItem, ItemFile itemFile) {
        ItemFile newItemFile = null;
        File file = getItemFile(oldItem.getId(), itemFile.getId());
        if (file != null) {
            newItemFile = new ItemFile();
            Login login = sessionWrapper.getSession().getLogin();

            newItemFile.setName(itemFile.getName());
            newItemFile.setCreatedBy(login.getPerson().getId());
            newItemFile.setModifiedBy(login.getPerson().getId());
            newItemFile.setItem(newItem.getId());
            newItemFile.setVersion(itemFile.getVersion());
            newItemFile.setSize(itemFile.getSize());
            newItemFile.setLatest(itemFile.getLatest());
            newItemFile = itemFileRepository.save(newItemFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + newItem.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }

            String path = dir + File.separator + newItemFile.getId();
            saveFileToDisk(file, path);
        }

        return newItemFile;
    }

    public List<Comment> getAllFileComments(Integer itemId, Integer fileId, ObjectType objectType) {
        List<Comment> comments = new ArrayList<>();
        List<Comment> fileComments = commentRepository.findByObjectIdAndObjectType(fileId, objectType);
        ItemFile plmItemFile = itemFileRepository.findOne(fileId);
        if (fileComments.size() > 0) {
            comments.addAll(fileComments);
        }
        List<ItemFile> files = itemFileRepository.findByItemAndNameAndLatestFalseOrderByCreatedDateDesc(plmItemFile.getItem(), plmItemFile.getName());
        if (files.size() > 0) {
            for (ItemFile file : files) {
                List<Comment> commentList = commentRepository.findByObjectIdAndObjectType(file.getId(), objectType);
                if (commentList.size() > 0) {
                    comments.addAll(commentList);
                }
            }
        }
        return comments;
    }

    public List<ItemFile> getAllFileVersionComments(Integer itemId, Integer fileId, ObjectType objectType) {
        List<ItemFile> itemFiles = new ArrayList<>();

        ItemFile plmItemFile = itemFileRepository.findOne(fileId);

        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(plmItemFile.getId(), objectType);
        plmItemFile.setComments(comments);
        itemFiles.add(plmItemFile);

        List<ItemFile> files = itemFileRepository.findByItemAndNameAndLatestFalseOrderByCreatedDateDesc(plmItemFile.getItem(), plmItemFile.getName());
        if (files.size() > 0) {
            for (ItemFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
            }
        }


        return itemFiles;
    }
}
