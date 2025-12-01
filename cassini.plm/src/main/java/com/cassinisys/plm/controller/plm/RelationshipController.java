package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ItemCriteria;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMRelationship;
import com.cassinisys.plm.model.plm.PLMRelationshipAttribute;
import com.cassinisys.plm.service.plm.RelationshipAttributeService;
import com.cassinisys.plm.service.plm.RelationshipService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 011 11-Jan -18.
 */
@RestController
@RequestMapping("/plm/relationships")
@Api(tags = "PLM.ITEMS", description = "Items Related")
public class RelationshipController extends BaseController {

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private RelationshipAttributeService relationshipAttributeService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PLMRelationship create(@RequestBody PLMRelationship relationship) {
        return relationshipService.create(relationship);
    }

    @RequestMapping(value = "/byRelationshipName", method = RequestMethod.POST)
    public PLMRelationship getRelationshipByName(@RequestBody PLMRelationship relationship) {
        return relationshipService.getRelationshipByName(relationship);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMRelationship update(@PathVariable("id") Integer id, @RequestBody PLMRelationship relationship) {
        return relationshipService.update(relationship);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void update(@PathVariable("id") Integer id) {
        relationshipService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMRelationship get(@PathVariable("id") Integer id) {
        return relationshipService.get(id);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PLMRelationshipAttribute createAttribute(@PathVariable("id") Integer id, @RequestBody PLMRelationshipAttribute relationshipAttribute) {
        return relationshipAttributeService.create(relationshipAttribute);
    }

    @RequestMapping(value = "/{id}/attributes/{attributeId}", method = RequestMethod.PUT)
    public PLMRelationshipAttribute update(@PathVariable("id") Integer id, @PathVariable("attributeId") Integer attributeId,
                                           @RequestBody PLMRelationshipAttribute relationshipAttribute) {
        return relationshipAttributeService.update(relationshipAttribute);
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("attributeId") Integer attributeId) {
        relationshipAttributeService.delete(attributeId);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.GET)
    public List<PLMRelationshipAttribute> getAllAttributesByRelationship(@PathVariable("id") Integer id) {
        return relationshipAttributeService.getAllAttributesByRelationship(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PLMRelationship> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return relationshipService.getAllRelationship(pageable);
    }

    @RequestMapping(value = "/itemType/{id}", method = RequestMethod.GET)
    public List<PLMRelationship> getRelationshipsByFromType(@PathVariable("id") Integer id) {
        return relationshipService.getRelationshipsByFromType(id);
    }

    @RequestMapping(value = "/itemType/{id}/itemRevision/{revisionId}", method = RequestMethod.GET)
    public List<PLMItem> getRelationshipsByFromTypeAndFromItem(@PathVariable("id") Integer id, @PathVariable("revisionId") Integer revisionId) {
        return relationshipService.getRelationshipsByFromTypeAndFromItem(id, revisionId);
    }

    @RequestMapping(value = "/{id}/itemRevision/{revisionId}", method = RequestMethod.GET)
    public Page<PLMItem> getItemByRelationshipAndFromItem(@PathVariable("id") Integer id, @PathVariable("revisionId") Integer revisionId, PageRequest pageRequest, ItemCriteria itemCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return relationshipService.getItemByRelationshipAndFromItem(id, revisionId, pageable, itemCriteria);
    }

    @RequestMapping(value = "/fromType/{fromTypeId}/toType/{toTypeId}", method = RequestMethod.GET)
    public PLMRelationship getRelationshipsByFromTypeAndFromType(@PathVariable("fromTypeId") Integer fromTypeId, @PathVariable("toTypeId") Integer toTypeId) {
        return relationshipService.getRelationshipsByFromTypeAndFromType(fromTypeId, toTypeId);
    }

}
