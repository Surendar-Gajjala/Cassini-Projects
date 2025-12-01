package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.ItemInstanceStatus;
import com.cassinisys.drdo.model.bom.ItemRevision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@Repository
public interface ItemInstanceRepository extends JpaRepository<ItemInstance, Integer>, QueryDslPredicateExecutor<ItemInstance> {

    @Query("select i from ItemInstance i where i.item.id= :item")
    List<ItemInstance> findByItem(@Param("item") Integer item);

    @Query("select i from ItemInstance i where i.item.id= :item and i.status!= :returnStatus")
    List<ItemInstance> getInstancesByItem(@Param("item") Integer item, @Param("returnStatus") ItemInstanceStatus returnStatus);

    @Query("select i from ItemInstance i where i.item.id= :item and i.status= :returnStatus")
    List<ItemInstance> getReturnInstancesByItem(@Param("item") Integer item, @Param("returnStatus") ItemInstanceStatus returnStatus);

    @Query("select i from ItemInstance i where i.item.id= :item and i.status= :returnStatus and i.section= :section")
    List<ItemInstance> getReturnInstancesByItemAndSection(@Param("item") Integer item, @Param("returnStatus") ItemInstanceStatus returnStatus, @Param("section") Integer section);

    @Query("select i from ItemInstance i where i.item.id= :item and i.status= :returnStatus and i.section is null")
    List<ItemInstance> getReturnInstancesByItemAndSectionIsNull(@Param("item") Integer item, @Param("returnStatus") ItemInstanceStatus returnStatus);

    @Query("select i from ItemInstance i where i.item.id= :item and i.status= :returnStatus and i.section= :section and i.uniqueCode= :uniqueCode")
    List<ItemInstance> getReturnInstancesByItemAndSectionAndUniqueCode(@Param("item") Integer item, @Param("returnStatus") ItemInstanceStatus returnStatus,
                                                                       @Param("section") Integer section, @Param("uniqueCode") String uniqueCode);

    @Query("select i from ItemInstance i where i.item.id= :item and i.status= :returnStatus and i.uniqueCode= :uniqueCode and i.section is null")
    List<ItemInstance> getReturnInstancesByItemAndUniqueCodeAndSectionIsNull(@Param("item") Integer item, @Param("returnStatus") ItemInstanceStatus returnStatus,
                                                                             @Param("uniqueCode") String uniqueCode);


    ItemInstance findByUpnNumber(String upnNumber);

    ItemInstance findByInitialUpn(String upnNumber);

    ItemInstance findByOemNumber(String oemNumber);

    ItemInstance findByOemNumberAndItem(String oemNumber, ItemRevision itemRevision);

    ItemInstance findByOemNumberAndItemAndBom(String oemNumber, ItemRevision itemRevision, Integer bom);

    ItemInstance findByLotNumber(String oemNumber);

    ItemInstance findByLotNumberAndItem(String oemNumber, ItemRevision itemRevision);

    ItemInstance findByLotNumberAndItemAndBom(String oemNumber, ItemRevision itemRevision, Integer bom);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.manufacturer.id= :manufacturer and i.oemNumber= :oemNumber")
    ItemInstance getInstanceByItemAndMfrAndOemNumber(@Param("item") Integer item, @Param("manufacturer") Integer manufacturer, @Param("oemNumber") String oemNumber);

    @Query("SELECT i from ItemInstance i where i.storage.name= :storageName and i.upnNumber= :upnNumber")
    ItemInstance getInstanceByStorageAndUpnNumber(@Param("storageName") String storageName, @Param("upnNumber") String upnNumber);

    @Query("SELECT i from ItemInstance i where i.storage.id= :storage")
    List<ItemInstance> findByStorage(@Param("storage") Integer storage);

    @Query("SELECT i from ItemInstance i where i.storage.id= :storage and i.item.id= :item")
    List<ItemInstance> findByStorageAndItem(@Param("storage") Integer storage, @Param("item") Integer item);

    @Query("SELECT i from ItemInstance i where i.status = 'REJECTED'")
    Page<ItemInstance> getAllReturnInstances(Pageable pageable);

    @Query("SELECT i from ItemInstance i where i.expiryDate < :expiryDate and (i.status= :newStatus or i.status= :submitted or i.status= :accept or i.status= :pAccept or i.status= :review or i.status= :reviewed or i.status= :verified or i.status= :inventory)")
    Page<ItemInstance> getAllExpiredInstances(@Param("expiryDate") Date expiryDate, @Param("newStatus") ItemInstanceStatus newStatus, @Param("submitted") ItemInstanceStatus submitted, @Param("accept") ItemInstanceStatus accept, @Param("pAccept") ItemInstanceStatus pAccept, @Param("review") ItemInstanceStatus review, @Param("reviewed") ItemInstanceStatus reviewed, @Param("verified") ItemInstanceStatus verified, @Param("inventory") ItemInstanceStatus inventory, Pageable pageable);

    @Query("SELECT i from ItemInstance i where i.expiryDate < :expiryDate and (i.status= :newStatus or i.status= :submitted or i.status= :accept or i.status= :pAccept or i.status= :review or i.status= :reviewed or i.status= :verified or i.status= :inventory)")
    List<ItemInstance> getExpiredInstances(@Param("expiryDate") Date expiryDate, @Param("newStatus") ItemInstanceStatus newStatus, @Param("submitted") ItemInstanceStatus submitted, @Param("accept") ItemInstanceStatus accept, @Param("pAccept") ItemInstanceStatus pAccept, @Param("review") ItemInstanceStatus review, @Param("reviewed") ItemInstanceStatus reviewed, @Param("verified") ItemInstanceStatus verified, @Param("inventory") ItemInstanceStatus inventory);

    @Query("SELECT i from ItemInstance i where i.status = 'FAILURE'")
    Page<ItemInstance> getAllFailureInstances(Pageable pageable);

    @Query("SELECT i from ItemInstance i where i.status= :status")
    List<ItemInstance> getInstancesByStatus(@Param("status") ItemInstanceStatus status);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.section= :section and i.bom= :bom and i.status != 'ISSUE' " +
            "and i.status != 'FAILURE' and i.status != 'DISPATCH' and i.status != 'REJECTED'")
    List<ItemInstance> getItemInstancesBySectionAndBom(@Param("item") Integer item, @Param("section") Integer section, @Param("bom") Integer bom);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.section= :section and i.bom= :bom and i.uniqueCode= :uniqueCode and i.status != 'ISSUE' " +
            "and i.status != 'FAILURE' and i.status != 'DISPATCH' and i.status != 'REJECTED' and i.status != 'ACCEPT' and i.status != 'P_ACCEPT' and i.status != 'NEW' and i.status != 'STORE_SUBMITTED'")
    List<ItemInstance> getItemInstancesBySectionAndBomAndUniqueCode(@Param("item") Integer item, @Param("section") Integer section, @Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.bom= :bom and i.uniqueCode= :uniqueCode and i.section is null and i.status != 'ISSUE'" +
            " and i.status != 'FAILURE' and i.status != 'DISPATCH' and i.status != 'REJECTED' and i.status != 'ACCEPT' and i.status != 'P_ACCEPT' and i.status != 'NEW' and i.status != 'STORE_SUBMITTED'")
    List<ItemInstance> getItemInstancesByBomAndUniqueCodeAndSectionIsNull(@Param("item") Integer item, @Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.bom= :bom and i.section is null and i.status != 'ISSUE' and i.status != 'FAILURE'" +
            " and i.status != 'DISPATCH' and i.status != 'REJECTED'")
    List<ItemInstance> getItemInstancesByBomAndSectionIsNull(@Param("item") Integer item, @Param("bom") Integer bom);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.bom= :bom and i.storage.id= :storage")
    List<ItemInstance> getItemInstancesByItemAndBomAndStorage(@Param("item") Integer item, @Param("bom") Integer bom, @Param("storage") Integer storage);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.section= :section and i.bom= :bom and i.status= :verified")
    List<ItemInstance> getVerifiedItemInstancesBySectionAndBom(@Param("item") Integer item, @Param("section") Integer section, @Param("bom") Integer bom, @Param("verified") ItemInstanceStatus verified);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.bom= :bom and i.section is null and i.status= :verified")
    List<ItemInstance> getVerifiedItemInstancesByBomAndSectionIsNull(@Param("item") Integer item, @Param("bom") Integer bom, @Param("verified") ItemInstanceStatus verified);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.section= :section and i.bom= :bom and i.status= :verified and i.uniqueCode= :uniqueCode")
    List<ItemInstance> getVerifiedItemInstancesBySectionAndBomAndUniqueCode(@Param("item") Integer item, @Param("section") Integer section, @Param("bom") Integer bom,
                                                                            @Param("verified") ItemInstanceStatus verified, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.bom= :bom and i.section is null and i.status= :verified and i.uniqueCode= :uniqueCode")
    List<ItemInstance> getVerifiedItemInstancesByBomAndUniqueCodeAndSectionIsNull(@Param("item") Integer item, @Param("bom") Integer bom,
                                                                                  @Param("verified") ItemInstanceStatus verified, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from ItemInstance i where i.manufacturer.id= :manufacturer")
    List<ItemInstance> findItemInstanceByManufacturer(@Param("manufacturer") Integer manufacturer);

    @Query("SELECT i from ItemInstance i where i.status= 'FAILURE_PROCESS'")
    List<ItemInstance> findFailureProcessItemInstance();

    @Query("SELECT i from ItemInstance i where i.status= 'ISSUE' or i.status = 'APPROVED' or i.status = 'P_APPROVED'")
    List<ItemInstance> getIssuedInstances();

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.section= :section and i.bom= :bom and i.uniqueCode= :uniqueCode " +
            "and (i.status = 'ACCEPT' or i.status = 'P_ACCEPT' or i.status = 'NEW' or i.status = 'STORE_SUBMITTED')")
    List<ItemInstance> getOnHoldInstancesBySectionAndBomAndUniqueCode(@Param("item") Integer item, @Param("section") Integer section, @Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from ItemInstance i where i.item.id= :item and i.bom= :bom and i.uniqueCode= :uniqueCode and i.section is null " +
            "and (i.status = 'ACCEPT' or i.status = 'P_ACCEPT' or i.status = 'NEW' or i.status = 'STORE_SUBMITTED')")
    List<ItemInstance> getOnHoldInstancesByBomAndUniqueCodeAndSectionIsNull(@Param("item") Integer item, @Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from ItemInstance i where i.expiryDate > :todayDate and i.expiryDate < :futureDate and (i.status= :newStatus or i.status= :submitted or i.status= :accept or i.status= :pAccept or i.status= :review or i.status= :reviewed or i.status= :verified or i.status= :inventory)")
    List<ItemInstance> getToExpiryItems(@Param("todayDate") Date todayDate, @Param("futureDate") Date futureDate, @Param("newStatus") ItemInstanceStatus newStatus, @Param("submitted") ItemInstanceStatus submitted, @Param("accept") ItemInstanceStatus accept, @Param("pAccept") ItemInstanceStatus pAccept, @Param("review") ItemInstanceStatus review, @Param("reviewed") ItemInstanceStatus reviewed, @Param("verified") ItemInstanceStatus verified, @Param("inventory") ItemInstanceStatus inventory);

    @Query("SELECT i from ItemInstance i where i.expiryDate > :todayDate and i.expiryDate < :futureDate and (i.status= :newStatus or i.status= :submitted or i.status= :accept or i.status= :pAccept or i.status= :review or i.status= :reviewed or i.status= :verified or i.status= :inventory)")
    Page<ItemInstance> getAllToExpiryItems(@Param("todayDate") Date todayDate, @Param("futureDate") Date futureDate, @Param("newStatus") ItemInstanceStatus newStatus, @Param("submitted") ItemInstanceStatus submitted, @Param("accept") ItemInstanceStatus accept, @Param("pAccept") ItemInstanceStatus pAccept, @Param("review") ItemInstanceStatus review, @Param("reviewed") ItemInstanceStatus reviewed, @Param("verified") ItemInstanceStatus verified, @Param("inventory") ItemInstanceStatus inventory, Pageable pageable);

}
