package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.ManufacturerPartStatus;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ItemManufacturerPartRepository extends JpaRepository<PLMItemManufacturerPart, Integer>, QueryDslPredicateExecutor<PLMItemManufacturerPart> {

    List<PLMItemManufacturerPart> findByItem(Integer itemId);

    @Query("select count (i) from PLMItemManufacturerPart i where i.item= :itemId")
    Integer getMfrPartsCountByItem(@Param("itemId") Integer itemId);

    @Query("select count (i) from PLMItemManufacturerPart i where i.manufacturerPart.id= :partId")
    Integer getMfrPartCount(@Param("partId") Integer partId);

    List<PLMItemManufacturerPart> findByItemOrderByModifiedDateDesc(Integer itemId);


    List<PLMItemManufacturerPart> findByManufacturerPart(PLMManufacturerPart manufacturerPart);

    PLMItemManufacturerPart findByItemAndManufacturerPart(Integer itemId, PLMManufacturerPart manufacturerPart);

    PLMItemManufacturerPart findByItemAndStatus(Integer id, ManufacturerPartStatus status);

    List<PLMItemManufacturerPart> findByItemInAndStatus(Iterable<Integer> ids, ManufacturerPartStatus status);

    @Query("select i.manufacturerPart.id from PLMItemManufacturerPart i where i.status= :status and i.item= :id")
    List<Integer> getPartIdsByStatusAndItem(@Param("status") ManufacturerPartStatus status, @Param("id") Integer id);

    List<PLMItemManufacturerPart> findByStatusAndItemOrderByIdAsc(ManufacturerPartStatus status, Integer id);

    @Query("SELECT DISTINCT m.manufacturerPart FROM PLMItemManufacturerPart m")
    List<Integer> findByManufacturerPartDistinct();

    void deleteByManufacturerPart(PLMManufacturerPart mfrPart);

    void deleteByItemAndManufacturerPart(Integer item, PLMManufacturerPart mfrPart);

    List<PLMItemManufacturerPart> findByManufacturerPartAndStatus(PLMManufacturerPart part, ManufacturerPartStatus status);

    @Query("SELECT i FROM PLMItemManufacturerPart i WHERE i.item= :item and i.manufacturerPart.id!= :id")
    List<PLMItemManufacturerPart> getPartWithOutPreferredItems(@Param("item") Integer item, @Param("id") Integer id);


}
