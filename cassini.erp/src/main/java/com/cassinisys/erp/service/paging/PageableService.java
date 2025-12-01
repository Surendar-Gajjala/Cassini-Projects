package com.cassinisys.erp.service.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author koti
 */
public interface PageableService<T, ID> {

	public Page<T> findAll(Pageable pageable);

}
