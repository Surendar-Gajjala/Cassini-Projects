package com.cassinisys.plm.controller.mfr;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.PLMSupplierCriteria;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.mfr.PLMSupplierAttribute;
import com.cassinisys.plm.model.mfr.PLMSupplierContact;
import com.cassinisys.plm.model.mfr.PLMSupplierPart;
import com.cassinisys.plm.model.plm.PLMSubscribe;
import com.cassinisys.plm.model.pqm.PQMPPAP;
import com.cassinisys.plm.model.pqm.SupplierAuditDetailsDto;
import com.cassinisys.plm.service.mfr.SupplierService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@RestController
@RequestMapping("/mfr/suppliers")
@Api(tags = "PLM.SUPPLIER", description = "Supplier Related")
public class PLMSupplierController extends BaseController {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PLMSupplier create(@RequestBody PLMSupplier supplier) {
        return supplierService.create(supplier);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMSupplier update(@PathVariable("id") Integer id,
                              @RequestBody PLMSupplier supplier) {
        return supplierService.update(supplier);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        supplierService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMSupplier get(@PathVariable("id") Integer id) {
        return supplierService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMSupplier> getAll() {
        return supplierService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMSupplier> getMultiple(@PathVariable Integer[] ids) {
        return supplierService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/approved", method = RequestMethod.GET)
    public List<PLMSupplier> getApprovedSuppliers() {
        return supplierService.getApprovedSuppliers();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PLMSupplier> filterSuppliers(PageRequest pageRequest, PLMSupplierCriteria supplierCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return supplierService.getAllSuppliersByPageable(pageable, supplierCriteria);
    }

    @RequestMapping(value = "/create/attributes/multiple", method = RequestMethod.POST)
    public void saveSupplierAttributes(@RequestBody List<PLMSupplierAttribute> attributes) {
        supplierService.saveSupplierAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PLMSupplierAttribute updateSupplierAttribute(@PathVariable("id") Integer id,
                                                        @RequestBody PLMSupplierAttribute attribute) {
        return supplierService.updateSupplierAttribute(attribute);
    }

    @RequestMapping(value = "/uploadimageattribute/{objectid}/{attributeid}", method = RequestMethod.POST)
    public PLMSupplier saveImageAttributeValue(@PathVariable("objectid") Integer objectId,
                                               @PathVariable("attributeid") Integer attributeId, MultipartHttpServletRequest request) {
        return supplierService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeid}", method = RequestMethod.GET)
    public Page<PLMSupplier> getSuppliersByType(@PathVariable("typeid") Integer id,
                                                PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return supplierService.getSuppliersByType(id, pageable);
    }

    @RequestMapping(value = "/{supplierid}/count", method = RequestMethod.GET)
    public DetailsCount getSupplierDetails(@PathVariable("supplierid") Integer supplierId) {
        return supplierService.getSupplierDetails(supplierId);
    }
    /*
     * Supplier Contact
     */

    @RequestMapping(value = "/{supplierid}/contacts", method = RequestMethod.GET)
    public List<PLMSupplierContact> getSupplierContacts(@PathVariable("supplierid") Integer supplierId) {
        return supplierService.getSupplierContacts(supplierId);
    }

    @RequestMapping(value = "/{supplierid}/contacts/active", method = RequestMethod.GET)
    public List<PLMSupplierContact> getSupplierActiveContacts(@PathVariable("supplierid") Integer supplierId) {
        return supplierService.getSupplierActiveContacts(supplierId);
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public PLMSupplierContact createContact(@RequestBody PLMSupplierContact contact) throws JsonProcessingException {
        return supplierService.createContactSupplier(contact);
    }

    @RequestMapping(value = "/contact/{id}", method = RequestMethod.PUT)
    public PLMSupplierContact updateContact(@PathVariable("id") Integer id,
                                            @RequestBody PLMSupplierContact contact) {
        return supplierService.updateSupplierContact(contact);
    }

    @RequestMapping(value = "/contact/{id}", method = RequestMethod.GET)
    public PLMSupplierContact getContact(@PathVariable("id") Integer id) {
        return supplierService.getContact(id);
    }

    @RequestMapping(value = "/contact/{id}", method = RequestMethod.DELETE)
    public void deleteContact(@PathVariable("id") Integer id) throws JsonProcessingException {
        supplierService.deleteContact(id);
    }

    @RequestMapping(value = "/create/supplier/mfrparts", method = RequestMethod.POST)
    public List<PLMSupplierPart> createSupplierMfrParts(
            @RequestBody List<PLMSupplierPart> supplierParts) throws JsonProcessingException {
        return supplierService.createSupplierParts(supplierParts);
    }

    @RequestMapping(value = "/{supplierid}/mfrparts", method = RequestMethod.GET)
    public List<PLMSupplierPart> getSupplierMfrParts(@PathVariable("supplierid") Integer supplierId) {
        return supplierService.getSupplierMfrParts(supplierId);
    }

    @RequestMapping(value = "/{supplierid}/mfrparts", method = RequestMethod.POST)
    public PLMSupplierPart createSupplierPart(@PathVariable("supplierid") Integer supplierId,
                                              @RequestBody PLMSupplierPart part) throws JsonProcessingException {
        return supplierService.createSupplierPart(part);
    }

    @RequestMapping(value = "/{supplierid}/mfrparts/{partid}", method = RequestMethod.PUT)
    public PLMSupplierPart updateWorkOrderSparePart(@PathVariable("supplierid") Integer supplierId,
                                                    @PathVariable("partid") Integer partId,
                                                    @RequestBody PLMSupplierPart part) throws JsonProcessingException {
        return supplierService.updateSupplierPart(part);
    }

    @RequestMapping(value = "/delete/supplierpart/{mfrpart}", method = RequestMethod.DELETE)
    public void deleteSupplierMfrPart(@PathVariable("mfrpart") Integer mfrPart) throws JsonProcessingException {
        supplierService.deleteSupplierMfrPart(mfrPart);
    }
    /*
     * Promote and Demote Supplier
     */

    @RequestMapping(value = "/{id}/promote", method = RequestMethod.PUT)
    public PLMSupplier promoteSupplier(@PathVariable("id") Integer id, @RequestBody PLMSupplier supplier) {
        return supplierService.promoteSupplier(id, supplier);
    }

    @RequestMapping(value = "/{id}/demote", method = RequestMethod.PUT)
    public PLMSupplier demoteSupplier(@PathVariable("id") Integer id, @RequestBody PLMSupplier supplier) {
        return supplierService.demoteSupplier(id, supplier);
    }

    @RequestMapping(value = "/{supplierId}/subscribe", method = RequestMethod.POST)
    public PLMSubscribe createSubscribeSupplier(@PathVariable Integer supplierId) {
        return supplierService.createSubscribeSupplier(supplierId);
    }

    @RequestMapping(value = "/{supplierId}/ppaps", method = RequestMethod.GET)
    public List<PQMPPAP> getSupplierPpaps(@PathVariable("supplierId") Integer supplierId) {
        return supplierService.getSupplierPpaps(supplierId);
    }

    @RequestMapping(value = "/{supplierId}/supplieraudits", method = RequestMethod.GET)
    public List<SupplierAuditDetailsDto> getSupplierAudits(@PathVariable("supplierId") Integer supplierId) {
        return supplierService.getSupplierAuditPlans(supplierId);
    }
}