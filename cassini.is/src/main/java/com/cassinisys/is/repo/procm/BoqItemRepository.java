package com.cassinisys.is.repo.procm;
/**
 * The Class is for BoqItemRepository
 **/

import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.ISBoqItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoqItemRepository extends JpaRepository<ISBoqItem, Integer>, QueryDslPredicateExecutor<ISBoqItem> {
    /**
     * The method used to findByBoq of ISBoqItem
     **/
    List<ISBoqItem> findByBoqOrderById(Integer boqId);

    /**
     * The method used to findAllByProject of ISBoqItem
     **/
    Page<ISBoqItem> findAllByProject(Integer projectId, Pageable pageable);

    /**
     * The method used to findByProject of ISBoqItem
     **/

    List<ISBoqItem> findByProject(Integer projectId);

    /**
     * The method used to findByIdIn of ISBoqItem
     **/
    List<ISBoqItem> findByIdIn(List<Integer> ids);

    /**
     * The method used to findByItemNumber of ISBoqItem
     **/
    List<ISBoqItem> findByItemNumber(String itemNumber);

    /**
     * The method used to findByItemNumberIn of ISBoqItem
     **/
    List<ISBoqItem> findByItemNumberIn(Iterable<String> itemNumbers);

    /**
     * The method used to findByItemNumberAndBoqName of ISBoqItem
     **/
    ISBoqItem findByItemNumberAndBoqNameAndProject(String itemNumber, String boqName, Integer projectId);

    /**
     * The method used to findByItemType of ISBoqItem
     **/
    List<ISBoqItem> findByProjectAndItemType(Integer projectId, ResourceType resourceType);

    Page<ISBoqItem> findByProjectAndItemType(Integer projectId, ResourceType resourceType, Pageable pageable);

    List<ISBoqItem> findByProjectAndItemNumber(Integer projectId, String itemNumber);

    ISBoqItem findByBoqAndItemNumber(Integer boqId, String itemNumber);

    List<ISBoqItem> findByBoq(Integer boqId);

    ISBoqItem findByBoqAndProject(Integer boqId, Integer projectId);

}
