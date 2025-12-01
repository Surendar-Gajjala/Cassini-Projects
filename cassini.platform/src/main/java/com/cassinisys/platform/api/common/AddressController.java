package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Address;
import com.cassinisys.platform.service.common.AddressService;
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
@RequestMapping("/common/addresses")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class AddressController extends BaseController {

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private AddressService addressService;
	
	@RequestMapping(method = RequestMethod.POST)
	public Address create(@RequestBody Address address) {
		address.setId(null);
		return addressService.create(address);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Address update(@PathVariable("id") Integer id,
			@RequestBody Address address) {
		address.setId(id);
		return addressService.update(address);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		addressService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Address get(@PathVariable("id") Integer id) {
		return addressService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<Address> getAll(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return addressService.findAll(pageable);
	}

	@RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
	public List<Address> getMultiple(@PathVariable Integer[] ids ) {
		return addressService.findMultiple(Arrays.asList(ids));
	}

}
