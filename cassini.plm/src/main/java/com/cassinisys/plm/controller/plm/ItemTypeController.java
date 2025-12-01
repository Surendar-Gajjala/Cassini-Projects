package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import com.cassinisys.plm.model.dto.ClassificationTypesDto;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartTypeAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerTypeAttribute;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.rm.RmObjectTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflowTypeAttribute;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/plm/itemtypes")
@Api(tags = "PLM.ITEMS", description = "Items Related")
public class ItemTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @RequestMapping(method = RequestMethod.POST)
    public PLMItemType create(@RequestBody PLMItemType itemType) {
        return itemTypeService.create(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMItemType update(@PathVariable("id") Integer id,
                              @RequestBody PLMItemType itemType) {
        return itemTypeService.update(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        itemTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMItemType get(@PathVariable("id") Integer id) {
        return itemTypeService.get(id);
    }

	/*@RequestMapping(value = "/getAllAttributeName/{name}", method = RequestMethod.GET)
    public boolean getAllAttributeName(@PathVariable("name") String name) {
		 ObjectTypeAttribute objectTypeAttribute= objectTypeAttributeRepository.findByName(name);
        if(objectTypeAttribute != null) return true;
        else return false;
	}*/

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMItemType> getAll() {
        return itemTypeService.getRootTypes();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMItemType> getMultiple(@PathVariable Integer[] ids) {
        return itemTypeService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public List<PLMItemType> getChildren(@PathVariable("id") Integer id) {
        return itemTypeService.getChildren(id);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<PLMItemType> getClassificationTree() {
        return itemTypeService.getClassificationTree();
    }

    @RequestMapping(value = "/tree/all", method = RequestMethod.GET)
    public List<PLMItemType> getItemTypeTree() {
        return itemTypeService.getItemTypeTree();
    }

    @RequestMapping(value = "/{itemClass}/tree", method = RequestMethod.GET)
    public List<PLMItemType> getClassificationTreeByClass(@PathVariable("itemClass") ItemClass itemClass) {
        return itemTypeService.getClassificationTreeByClass(itemClass);
    }

    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.GET)
    public List<PLMItemTypeAttribute> getAttributes(@PathVariable("typeId") Integer typeId,
                                                    @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return itemTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/{typeId}/{revisionId}/attributes", method = RequestMethod.GET)
    public List<PLMItemTypeAttribute> getAttributes(@PathVariable("typeId") Integer typeId, @PathVariable("revisionId") Integer revisionId,
                                                    @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return itemTypeService.getAttributes(typeId, revisionId, hierarchy);
    }

    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.POST)
    public PLMItemTypeAttribute createAttribute(@PathVariable("typeId") Integer typeId,
                                                @RequestBody PLMItemTypeAttribute attribute) {
        return itemTypeService.createAttribute(attribute);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.GET)
    public PLMItemTypeAttribute getAttribute(@PathVariable("typeId") Integer typeId,
                                             @PathVariable("attributeId") Integer attributeId) {
        return itemTypeService.getAttribute(attributeId);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.PUT)
    public PLMItemTypeAttribute updateAttribute(@PathVariable("typeId") Integer typeId,
                                                @PathVariable("attributeId") Integer attributeId,
                                                @RequestBody PLMItemTypeAttribute attribute) {
        return itemTypeService.updateAttribute(attribute);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.DELETE)
    public void deleteAttribute(@PathVariable("typeId") Integer typeId,
                                @PathVariable("attributeId") Integer attributeId) {
        itemTypeService.deleteAttribute(attributeId);
    }

    @RequestMapping(value = "/attributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getAllItemTypeAttributes(@PathVariable("objectType") String objectType) {
        return itemTypeService.getAllItemTypeAttributes(objectType);
    }

    @RequestMapping(value = "/byName", method = RequestMethod.GET)
    public PLMItemType getByItemTypeName(@RequestParam("name") String name) {
        return itemTypeService.getItemTypeName(name);
    }

    @RequestMapping(value = "/item/{autoNum}", method = RequestMethod.GET)
    public void findItemByAutoNumId(@PathVariable("autoNum") Integer autoNum) {
        itemTypeService.findItemByAutoNumId(autoNum);
    }

    @RequestMapping(value = "/{itemTypeId}/items", method = RequestMethod.GET)
    public List<PLMItem> getItemTypeItems(@PathVariable("itemTypeId") Integer itemTypeId) {
        return itemTypeService.getItemTypeItems(itemTypeId);
    }

    @RequestMapping(value = "/items/attributes/{attributeId}", method = RequestMethod.GET)
    public List<PLMItemAttribute> getAttributeUsedItems(@PathVariable("attributeId") Integer attributeId) {
        return itemService.getItemUsedAttributes(attributeId);
    }

    @RequestMapping(value = "/{typeId}/attribute/byName", method = RequestMethod.GET)
    public PLMItemTypeAttribute getAttributeByName(@PathVariable("typeId") Integer typeId,
                                                   @RequestParam("name") String name) {
        return itemTypeService.getAttributeByName(typeId, name);
    }

    @RequestMapping(value = "/getAttributeName/{name}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getAttributeName(@PathVariable("name") String name) {
        return itemTypeService.getAttributeName(name);
    }

    @RequestMapping(value = "/{targetId}/attributeSeq/{actId}", method = RequestMethod.POST)
    public PLMItemTypeAttribute changeAttributeSeq(@PathVariable("targetId") Integer targetId,
                                                   @PathVariable("actId") Integer actId) {
        return itemTypeService.changeAttributeSeq(targetId, actId);
    }

    @RequestMapping(value = "/change/{targetId}/attributeSeq/{actId}", method = RequestMethod.POST)
    public PLMChangeTypeAttribute changeEcoAttributeSeq(@PathVariable("targetId") Integer targetId,
                                                        @PathVariable("actId") Integer actId) {
        return itemTypeService.changeEcoAttributeSeq(targetId, actId);
    }

    @RequestMapping(value = "/mfr/{targetId}/attributeSeq/{actId}", method = RequestMethod.POST)
    public PLMManufacturerTypeAttribute changeMfrAttributeSeq(@PathVariable("targetId") Integer targetId,
                                                              @PathVariable("actId") Integer actId) {
        return itemTypeService.changeMfrAttributeSeq(targetId, actId);
    }

    @RequestMapping(value = "/part/{targetId}/attributeSeq/{actId}", method = RequestMethod.POST)
    public PLMManufacturerPartTypeAttribute changeMfrPartAttributeSeq(@PathVariable("targetId") Integer targetId,
                                                                      @PathVariable("actId") Integer actId) {
        return itemTypeService.changeMfrPartAttributeSeq(targetId, actId);
    }

    @RequestMapping(value = "/workflow/{targetId}/attributeSeq/{actId}", method = RequestMethod.POST)
    public PLMWorkflowTypeAttribute changeWorkflowAttributeSeq(@PathVariable("targetId") Integer targetId,
                                                               @PathVariable("actId") Integer actId) {
        return itemTypeService.changeWorkflowAttributeSeq(targetId, actId);
    }

    @RequestMapping(value = "/rm/{targetId}/attributeSeq/{actId}", method = RequestMethod.POST)
    public RmObjectTypeAttribute changeRmAttributeSeq(@PathVariable("targetId") Integer targetId,
                                                      @PathVariable("actId") Integer actId) {
        return itemTypeService.changeRmAttributeSeq(targetId, actId);
    }

    @RequestMapping(value = "/generateSeq", method = RequestMethod.POST)
    public PLMItemTypeAttribute generateAttributeSeq() {
        return itemTypeService.generateAttributeSeq();
    }

    @RequestMapping(value = "/lov/{id}", method = RequestMethod.GET)
    public ClassificationTypesDto getItemTypesByLov(@PathVariable("id") Integer id) {
        return itemTypeService.getItemTypesByLov(id);
    }

    @RequestMapping(value = "/{typeId}/getTypeAttributeName/{name}", method = RequestMethod.GET)
    public PLMItemTypeAttribute getTypeAttributeName(@PathVariable("typeId") Integer typeId, @PathVariable("name") String name) {
        return itemTypeService.getTypeAttributeName(typeId, name);
    }

    @RequestMapping(value = "/bomRollupAttributes", method = RequestMethod.GET)
    public List<PLMItemTypeAttribute> getBomRollupAttributes() {
        return itemTypeService.getBomRollupAttributes();
    }

    @RequestMapping(value = "/lovs", method = RequestMethod.GET)
    public List<Lov> getAllLovs() {
        return itemTypeService.getAllLovs();
    }

    @RequestMapping(value = "/lovs/{id}/value/count", method = RequestMethod.GET)
    @Produces({"text/plain"})
    public Integer getLovValueUsedCount(@PathVariable("id") Integer id, @RequestParam("lovValue") String lovValue) {
        return itemTypeService.getLovValueUsedCount(id, lovValue);
    }

    @RequestMapping(value = "/autoNumber/{id}/value/count", method = RequestMethod.GET)
    @Produces({"text/plain"})
    public Integer getAutoNumberValueUsedCount(@PathVariable("id") Integer id, @RequestParam("autoNumber") String autoNumber) {
        return itemTypeService.getAutonumberValueUsedCount(id, autoNumber);
    }

    @RequestMapping(value = "/attribute/{attributeId}/values", method = RequestMethod.GET)
    public List<ObjectAttribute> getAttributeValues(@PathVariable("attributeId") Integer attributeId) {
        return itemTypeService.getAttributeValues(attributeId);
    }

    @RequestMapping(value = "/lifecycles", method = RequestMethod.GET)
    public List<PLMLifeCycle> getItemTypeLifecycles() {
        return itemTypeService.getItemTypeLifecycles();
    }
}
