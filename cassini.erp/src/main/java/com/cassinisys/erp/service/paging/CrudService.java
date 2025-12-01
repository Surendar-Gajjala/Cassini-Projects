package com.cassinisys.erp.service.paging;

import java.util.List;

/**
 * @author lkoti
 */
public interface CrudService<T, ID> {

	public T create(T t);

	public T update(T t);

	public void delete(ID id);

	public T get(ID id);

	public List<T> getAll();

}
