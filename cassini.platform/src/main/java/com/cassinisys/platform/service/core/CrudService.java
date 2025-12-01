package com.cassinisys.platform.service.core;

import java.util.List;

/**
 * @author reddy
 */
public interface CrudService<T, ID> {

    public T create(T t);

    public T update(T t);

    public void delete(ID id);

    public T get(ID id);

    public List<T> getAll();

}
