package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Tag;
import com.cassinisys.platform.repo.common.TagRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class TagService implements CrudService<Tag, Integer>,
        PageableService<Tag, Integer> {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional
    public Tag create(Tag tag) {
        checkNotNull(tag);
        tag.setId(null);
        Tag existTag = tagRepository.findByObjectAndLabelEqualsIgnoreCase(tag.getObject(), tag.getLabel());
        if (existTag != null) {
            String message = messageSource.getMessage("tag_already_exists", null, "{0} : Tag already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", tag.getLabel());
            throw new CassiniException(result);
        }
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag update(Tag tag) {
        checkNotNull(tag);
        checkNotNull(tag.getId());
        Tag existTag = tagRepository.findByObjectAndLabelEqualsIgnoreCase(tag.getObject(), tag.getLabel());
        if (existTag != null && !existTag.getId().equals(tag.getId())) {
            String message = messageSource.getMessage("tag_already_exists", null, "{0} : Tag already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", tag.getLabel());
            throw new CassiniException(result);
        }
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        tagRepository.delete(id);
    }


    @Override
    @Transactional(readOnly = true)
    public Tag get(Integer id) {
        checkNotNull(id);
        Tag tag = tagRepository.findOne(id);
        if (tag == null) {
            throw new ResourceNotFoundException();
        }
        return tag;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Tag> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));
        }
        return tagRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Tag> getObjectTags(Integer objectId) {
        return tagRepository.findByObjectOrderByIdDesc(objectId);
    }

    @Transactional(readOnly = true)
    public List<Tag> findMultiple(List<Integer> ids) {
        return tagRepository.findByIdIn(ids);
    }

}
