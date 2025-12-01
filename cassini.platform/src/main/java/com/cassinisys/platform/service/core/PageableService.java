package com.cassinisys.platform.service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author reddy
 */
public interface PageableService<T, ID> {

	public Page<T> findAll(Pageable pageable);

}
