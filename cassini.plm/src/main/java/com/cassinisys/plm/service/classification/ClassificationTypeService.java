package com.cassinisys.plm.service.classification;

import java.util.List;

/**
 * Created by reddy on 6/13/17.
 */
public interface ClassificationTypeService<T, A> {
    T create(T t);

    T update(T t);

    T get(Integer id);

    void delete(Integer id);

    List<T> getAll();

    List<T> getRootTypes();

    List<T> getChildren(Integer parent);

    List<T> getClassificationTree();

    A createAttribute(A a);

    A updateAttribute(A a);

    void deleteAttribute(Integer id);

    A getAttribute(Integer id);

    List<A> getAttributes(Integer typeId, Boolean hier);

}
