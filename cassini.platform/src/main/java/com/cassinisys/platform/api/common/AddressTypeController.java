package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Address;
import com.cassinisys.platform.model.common.AddressType;
import com.cassinisys.platform.service.common.AddressTypeService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/common/addresstypes")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class AddressTypeController extends BaseController {

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private AddressTypeService addressTypeService;
	
	@RequestMapping(method = RequestMethod.POST)
	public AddressType create(@RequestBody AddressType addressType) {
		addressType.setId(null);
		return addressTypeService.create(addressType);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public AddressType update(@PathVariable("id") Integer id,
			@RequestBody AddressType addressType) {
		addressType.setId(id);
		return addressTypeService.update(addressType);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		addressTypeService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AddressType get(@PathVariable("id") Integer id) {
		return addressTypeService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<AddressType> getAll(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return addressTypeService.findAll(pageable);
	}

	@RequestMapping(value = "/{id}/addresses", method = RequestMethod.GET)
	public Page<Address> getAddresses(@PathVariable("id") Integer id,
			PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return addressTypeService.getAddresses(id, pageable);
	}

}
