package com.cassinisys.documents.repo;

import com.cassinisys.documents.model.dm.DMFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 11/12/18.
 */
@Repository
public interface DMFolderRepository extends JpaRepository<DMFolder, Integer>, QueryDslPredicateExecutor<DMFolder> {

    List<DMFolder> findByParentIsNullOrderByCreatedDateAsc();

    List<DMFolder> findByParentOrderByCreatedDateAsc(Integer parent);

    List<DMFolder> findByParent(Integer id);

    DMFolder findByParentIsNullAndNameEqualsIgnoreCase(String name);

    DMFolder findByParentAndNameEqualsIgnoreCase(Integer parent, String name);
}
