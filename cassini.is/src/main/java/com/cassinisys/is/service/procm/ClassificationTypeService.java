package com.cassinisys.is.service.procm;

import java.util.List;

/**
 * Created by reddy on 6/13/17.
 */
public interface ClassificationTypeService<T, A> {
    public T create(T t);

    public T update(T t);

    public T get(Integer id);

    public void delete(Integer id);

    public List<T> getAll();

    public List<T> getRootTypes();

    public List<T> getChildren(Integer parent);

    public List<T> getClassificationTree();

    public A createAttribute(A a);

    public A updateAttribute(A a);

    public void deleteAttribute(Integer id);

    public A getAttribute(Integer id);

    public List<A> getAttributes(Integer typeId, Boolean hier);

}
