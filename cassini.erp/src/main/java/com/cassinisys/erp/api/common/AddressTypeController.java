package com.cassinisys.erp.api.common;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.common.ERPAddressType;
import com.cassinisys.erp.service.common.AddressTypeService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("common/addresstypes")
@Api(name="AddressTypes",description="AddressTypes endpoint",group="COMMON")
public class AddressTypeController extends BaseController {

	private PageRequestConverter pageRequestConverter;

	private AddressTypeService addressTypeService;

	@Autowired
	public AddressTypeController(AddressTypeService addressTypeService,
			PageRequestConverter pageRequestConverter) {
		this.pageRequestConverter = pageRequestConverter;
		this.addressTypeService = addressTypeService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ERPAddressType create(@RequestBody ERPAddressType addressType) {
		addressType.setTypeId(null);
		return addressTypeService.create(addressType);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPAddressType update(@PathVariable Integer id,
			@RequestBody ERPAddressType addressType) {
		addressType.setTypeId(id);
		return addressTypeService.update(addressType);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer id) {
		addressTypeService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPAddressType get(@PathVariable Integer id) {
		return addressTypeService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPAddressType> getAll() {
		return addressTypeService.getAll();
	}

}
