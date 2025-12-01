package com.cassinisys.platform.service.col;

import com.cassinisys.platform.events.CommonEvents;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.*;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.repo.col.UserReadCommentRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class CommentService implements CrudService<Comment, Integer>,
        PageableService<Comment, Integer> {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private MimetypesFileTypeMap mimeTypes = null;

    @Value("classpath:mime.types.txt")
    private Resource reportsResource;

    @Autowired
    private ObjectService objectService;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private UserReadCommentRepository userReadCommentRepository;


    @Override
    @Transactional
    public Comment create(Comment comment) {
        checkNotNull(comment);
        comment.setId(null);
        comment.setCommentedBy(sessionWrapper.getSession().getLogin().getPerson());
        comment.setCommentedDate(new Date());
        comment = commentRepository.save(comment);
        Integer commentId = comment.getId();
        List<UserReadComment> userReadComments = new ArrayList<>();
        final Comment comment1 = comment;
        List<Login> logins = loginRepository.findAll();
        logins.forEach(login -> {
            if (!comment1.getCommentedBy().getId().equals(login.getPerson().getId())) {
                UserReadComment userReadComment = new UserReadComment();
                userReadComment.setPerson(login.getPerson().getId());
                userReadComment.setComment(commentId);
                userReadComments.add(userReadComment);
            }
        });

        if (userReadComments.size() > 0) {
            userReadCommentRepository.save(userReadComments);
        }

        applicationEventPublisher.publishEvent(new CommonEvents.CommentAdded(comment));
        applicationEventPublisher.publishEvent(new CommonEvents.SendPushNotificationOnCommentAdd(comment));
        return comment;
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {
        checkNotNull(comment);
        checkNotNull(comment.getId());
        comment.setCommentedDate(new Date());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        commentRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment get(Integer id) {
        checkNotNull(id);
        Comment comment = commentRepository.findOne(id);
        if (comment == null) {
            throw new ResourceNotFoundException();
        }

        comment.setObject(objectRepository.findByIdAndObjectType(comment.getObjectId(), comment.getObjectType()));
        comment.getImages().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.IMAGE));
        comment.getVideos().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.VIDEO));
        comment.getAttachments().addAll(attachmentRepository.findByObjectIdAndObjectType(comment.getId(), ObjectType.ATTACHMENT));
        return comment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "commentedDate")));
        }
        return commentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getRootComments(ObjectType objectType,
                                         Integer objectId, Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "commentedDate")));
        }

        Page<Comment> comments = null;
        if (objectId != null) {
            comments = commentRepository.findByObjectIdAndObjectTypeAndReplyToIsNull(
                    objectId, objectType, pageable);
        } else {
            comments = commentRepository.findAll(pageable);
        }

        Map<String, List<Integer>> typeIdMap = new HashMap<>();
        comments.getContent().forEach(c -> {
            if (c.getObjectId() != null) {
                List<Integer> ids = typeIdMap.computeIfAbsent(c.getObjectType().toString(), k -> new ArrayList<>());
                ids.add(c.getObjectId());
            }
        });

        Set<String> keys = typeIdMap.keySet();
        Map<Integer, CassiniObject> map = new HashMap<>();
        keys.forEach(key -> {
            List<CassiniObject> objs = objectService.findMultipleByType(typeIdMap.get(key), key);
            objs.forEach(o -> map.put(o.getId(), o));
        });


        if (comments.getContent().size() > 0) {
            comments.getContent().forEach(comment -> {
                comment.setObject(map.get(comment.getObjectId()));
                comment.getImages().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.IMAGE));
                comment.getVideos().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.VIDEO));
                comment.getAttachments().addAll(attachmentRepository.findByObjectIdAndObjectType(comment.getId(), ObjectType.ATTACHMENT));
            });
        }
        return comments;
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllComments(ObjectType objectType, Integer objectId) {
        return commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(objectId, objectType);
    }

    @Transactional(readOnly = true)
    public Integer getAllCommentsCount(ObjectType objectType, Integer objectId) {
        return commentRepository.getCommentCountByObjectIdAndObjectType(objectId, objectType);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getReplies(Integer commentId, Pageable pageable) {
        checkNotNull(commentId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "commentedDate")));
        }
        return commentRepository.findByReplyTo(commentId, pageable);
    }

    @Transactional
    public Comment uploadCommentMedia(Integer objectId, MultipartHttpServletRequest request) throws Exception {
        Map<String, MultipartFile> map = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(map.values());
        Comment comment = commentRepository.findOne(objectId);
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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String fname = file.getOriginalFilename();
                    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                    Attachment attachment = new Attachment();
                    attachment.setAddedBy(comment.getCommentedBy().getId());
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

                    saveAttachmentToDisk(attachment, file);
                }
            }

            comment.getImages().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.IMAGE));
            comment.getVideos().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.VIDEO));
            comment.getAttachments().addAll(attachmentRepository.findByObjectIdAndObjectType(comment.getId(), ObjectType.ATTACHMENT));
        }
        return comment;
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

    public Page<Comment> searchComments(String query, Pageable pageable) {
        List<Predicate> predicates = new ArrayList<>();
        String[] arr = query.split(" ");
        for (String s : arr) {
            predicates.add(QComment.comment1.comment.containsIgnoreCase(s.trim()));
        }
        Predicate predicate = ExpressionUtils.allOf(predicates);
        Page<Comment> comments = commentRepository.findAll(predicate, pageable);

        Map<String, List<Integer>> typeIdMap = new HashMap<>();
        comments.getContent().forEach(c -> {
            if (c.getObjectId() != null) {
                List<Integer> ids = typeIdMap.computeIfAbsent(c.getObjectType().toString(), k -> new ArrayList<>());
                ids.add(c.getObjectId());
            }
        });

        Set<String> keys = typeIdMap.keySet();
        Map<Integer, CassiniObject> map = new HashMap<>();
        keys.forEach(key -> {
            List<CassiniObject> objs = objectService.findMultipleByType(typeIdMap.get(key), key);
            objs.forEach(o -> map.put(o.getId(), o));
        });


        if (comments.getContent().size() > 0) {
            comments.getContent().forEach(comment -> {
                comment.setObject(map.get(comment.getObjectId()));
                comment.getImages().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.IMAGE));
                comment.getVideos().addAll(mediaRepository.getMediaByType(comment.getId(), MediaType.VIDEO));
                comment.getAttachments().addAll(attachmentRepository.findByObjectIdAndObjectType(comment.getId(), ObjectType.ATTACHMENT));
            });
        }

        return comments;
    }

    public Long getMessageCount() {
        return commentRepository.count();
    }

    public Integer getMessageCountByPerson(Integer person) {
        return userReadCommentRepository.getUserUnreadCommentCount(person);
    }

    @Transactional(readOnly = true)
    public List<UserReadComment> getUserReadsByPersonAndComments(Integer person, List<Integer> comments) {
        List<UserReadComment> userReadComments = userReadCommentRepository.getUserReadsByCommentsAndPerson(comments, person);
        return userReadComments;
    }

    @Transactional
    public UserReadComment updateUserReadComment(Integer comment, Integer person) {
        UserReadComment userReadComment = userReadCommentRepository.getUserReadByCommentAndPerson(comment, person);
        if (userReadComment != null) {
            userReadComment.setRead(true);
            userReadComment = userReadCommentRepository.save(userReadComment);
        }
        return userReadComment;
    }

    @Transactional
    public List<UserReadComment> updateAllUnreadComments(Integer person) {
        List<UserReadComment> userReadComments = userReadCommentRepository.getUnreadCommentsByPerson(person);
        if (userReadComments.size() > 0) {
            userReadComments.forEach(userReadComment -> {
                userReadComment.setRead(true);
            });
            userReadComments = userReadCommentRepository.save(userReadComments);
        }
        return userReadComments;
    }
}
