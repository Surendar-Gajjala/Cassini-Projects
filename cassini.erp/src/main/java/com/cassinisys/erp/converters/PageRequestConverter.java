package com.cassinisys.erp.converters;

import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.google.common.base.Strings;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * @author Darek Osiennik
 */
@Component
public class PageRequestConverter implements Converter<ERPPageRequest, Pageable> {

	public PageRequestConverter() {
	}

	@Override
	public Pageable convert(ERPPageRequest source) {
		checkNotNull(source);
		PageRequest pageRequest = new PageRequest(source.getPage(),
				source.getSize());
		String sort = source.getSort();
		List<Order> orders = new ArrayList<Order>();
		if (!Strings.isNullOrEmpty(sort)) {
			String[] sortItems = sort.split(",");
			for (String sortItem : sortItems) {
				Order order;

				sortItem = sortItem.trim();
				String sortDir, property;
				int index = sortItem.indexOf(':');

				if(index != -1) {
					property = sortItem.substring(0, index).trim();
					sortDir = sortItem.substring(index+1).trim();

					if(sortDir.equalsIgnoreCase("asc")) {
						order = new Order(Direction.ASC, property);
					}
					else {
						order = new Order(Direction.DESC, property);
					}
				}
				else {
					order = new Order(Direction.ASC, sortItem);
				}
				orders.add(order);
			}
			pageRequest = new PageRequest(source.getPage(), source.getSize(),
					new Sort(orders));
		}
		return pageRequest;

	}
}
