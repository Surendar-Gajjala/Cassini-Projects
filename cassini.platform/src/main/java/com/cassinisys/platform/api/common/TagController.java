package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Tag;
import com.cassinisys.platform.service.common.TagService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/common/tags")
@Api(tags = "PLATFORM.COMMON", description = "Common endpoints")
public class TagController extends BaseController {

    @Autowired
    private PageRequestConverter pageConverter;

    @Autowired
    private TagService tagService;

    @RequestMapping(method = RequestMethod.POST)
    public Tag create(@RequestBody Tag tag) {
        tag.setId(null);
        return tagService.create(tag);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Tag update(@PathVariable("id") Integer id, @RequestBody Tag tag) {
        tag.setId(id);
        return tagService.update(tag);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        tagService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Tag get(@PathVariable("id") Integer id) {
        return tagService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Tag> getAll(PageRequest page) {
        Pageable pageable = pageConverter.convert(page);
        return tagService.findAll(pageable);
    }

    @RequestMapping(value = "/object/{objectId}/all", method = RequestMethod.GET)
    public List<Tag> getObjectTags(@PathVariable("objectId") Integer objectId) {
        return tagService.getObjectTags(objectId);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<Tag> getMultiple(@PathVariable Integer[] ids) {
        return tagService.findMultiple(Arrays.asList(ids));
    }

}
