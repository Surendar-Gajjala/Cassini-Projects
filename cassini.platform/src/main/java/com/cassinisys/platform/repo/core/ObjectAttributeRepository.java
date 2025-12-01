package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.ObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface ObjectAttributeRepository extends
        JpaRepository<ObjectAttribute, Integer> {

    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.objectId= :objectId"
    )
    List<ObjectAttribute> findByObjectId(@Param("objectId") Integer objectId);

    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.objectId in :objectIds and a.id.attributeDef in :attributeDefIds"
    )
    List<ObjectAttribute> findByObjectIdsInAndAttributeDefIdsIn(@Param("objectIds") Integer[] objectIds, @Param("attributeDefIds") Integer[] attributeDefIds);


    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.objectId in :objectIds and a.id.attributeDef= :attributeDefId"
    )
    List<ObjectAttribute> findByObjectIdsInAndAttributeDefId(@Param("objectIds") Integer[] objectIds, @Param("attributeDefId") Integer attributeDefId);

    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.objectId= :objectId and a.id.attributeDef= :attributeDefId"
    )
    ObjectAttribute findByObjectIdAndAttributeDefId(@Param("objectId") Integer objectId, @Param("attributeDefId") Integer attributeDefId);


    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.attributeDef= :attributeDefId"
    )
    List<ObjectAttribute> getAllObjectAttributesByAttributeDefId(@Param("attributeDefId") Integer attributeDefId);

    List<ObjectAttribute> findByStringValue(String stringValue);

    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.attributeDef= :attributeDef"
    )
    List<ObjectAttribute> findByAttributeDef(@Param("attributeDef") Integer id);

    @Query("select a.id.objectId from ObjectAttribute a where a.id.objectId = :objectId and a.id.attributeDef= :attributeDefId and (LOWER(CAST(a.stringValue AS text))  LIKE LOWER('%'  || :searchText || '%') or " + " LOWER (CAST(a.longTextValue as text)) LIKE LOWER('%' || :searchText || '%')or " + " LOWER (CAST(a.integerValue as text)) LIKE LOWER('%' || :searchText || '%')or " + " LOWER (CAST(a.listValue as text)) LIKE LOWER('%' || :searchText || '%')or " + " LOWER (CAST(a.doubleValue as text)) LIKE LOWER('%' || :searchText || '%')or " + " LOWER (CAST(a.booleanValue as text)) LIKE LOWER('%' || :searchText || '%')or " + " LOWER (CAST(a.currencyValue as text)) LIKE LOWER('%' || :searchText || '%')or " + " LOWER (CAST(a.dateValue as text)) LIKE LOWER('%' || :searchText || '%')or " + " LOWER (CAST(a.timeValue as text)) LIKE LOWER('%' || :searchText || '%')or " + " LOWER (CAST(a.timestampValue as text)) LIKE LOWER('%' || :searchText || '%'))")
    List<Integer> findObjectIdsByObjectIdAndAttValue(@Param("objectId") Integer objectId, @Param("attributeDefId") Integer attributeDefId, @Param("searchText") String searchText);

    @Query("select a.id.objectId from ObjectAttribute a where a.id.objectId = :objectId and a.id.attributeDef= :attributeDefId and LOWER (CAST(a.doubleValue as text)) LIKE LOWER('%' || :searchText || '%')")
    List<Integer> findObjectIdsByObjectIdAndDoubleValue(@Param("objectId") Integer objectId, @Param("attributeDefId") Integer attributeDefId, @Param("searchText") Double searchText);


    @Query("select a.id.objectId from ObjectAttribute a where a.id.objectId = :objectId and a.id.attributeDef= :attributeDefId and LOWER (CAST(a.booleanValue as text)) LIKE LOWER('%' || :searchText || '%')")
    List<Integer> findObjectIdsByObjectIdAndBooleanValue(@Param("objectId") Integer objectId, @Param("attributeDefId") Integer attributeDefId, @Param("searchText") Boolean searchText);

    @Query("select a.id.objectId from ObjectAttribute a where a.id.objectId = :objectId and a.id.attributeDef= :attributeDefId and (CAST(a.mListValue as text)) LIKE '%' || :searchText || '%'")
    List<Integer> findObjectIdsByObjectIdAndmListValue(@Param("objectId") Integer objectId, @Param("attributeDefId") Integer attributeDefId, @Param("searchText") String searchText);


    @Query("select a.id.objectId from ObjectAttribute a where a.id.objectId = :objectId and a.id.attributeDef= :attributeDefId and LOWER (CAST(a.dateValue as text)) LIKE LOWER('%' || :searchText || '%')")
    List<Integer> findObjectIdsByObjectIdAndDateValue(@Param("objectId") Integer objectId, @Param("attributeDefId") Integer attributeDefId, @Param("searchText") Date searchText);

    @Query("select a from ObjectAttribute a where a.id.objectId = :objectId and a.id.attributeDef= :attributeDefId and (LOWER(CAST(a.stringValue AS text))  LIKE LOWER(\'%\'  || :searchText || \'%\') or  LOWER (CAST(a.longTextValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\')or  LOWER (CAST(a.integerValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\')or  LOWER (CAST(a.listValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\')or  LOWER (CAST(a.doubleValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\')or  LOWER (CAST(a.booleanValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\')or  LOWER (CAST(a.currencyValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\')or  LOWER (CAST(a.dateValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\')or  LOWER (CAST(a.timeValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\')or  LOWER (CAST(a.timestampValue as text)) LIKE LOWER(\'%\' || :searchText || \'%\'))")
    ObjectAttribute findByObjectIdAndAttValue(@Param("objectId") Integer var1, @Param("attributeDefId") Integer var2, @Param("searchText") String var3);

    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.objectId = :objectId and a.id.attributeDef in :attributeDefIds"
    )
    List<ObjectAttribute> findByObjectIdAndAttributeDefIdsIn(@Param("objectId") Integer objectId, @Param("attributeDefIds") Integer[] attributeDefIds);

    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.objectId= :objectId and a.id.attributeDef= :attributeDefId and a.listValue= :listValue"
    )
    ObjectAttribute findByObjectIdAndAttributeDefIdAndListValue(@Param("objectId") Integer objectId, @Param("attributeDefId") Integer attributeDefId, @Param("listValue") String listValue);

    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.objectId in :objectIds and a.id.attributeDef= :attributeDefId and a.listValue= :listValue"
    )
    ObjectAttribute findByObjectIdsAndAttributeDefIdAndListValue(@Param("objectIds") Integer[] objectIds, @Param("attributeDefId") Integer attributeDefId, @Param("listValue") String listValue);

    @Query(
            "SELECT a FROM ObjectAttribute a WHERE a.id.objectId= :objectId and a.id.attributeDef in :attributeDefIds and a.listValue in :listValues"
    )
    List<ObjectAttribute> findByObjectIdAndAttributeDefIdsAndListValues(@Param("objectId") Integer objectId, @Param("attributeDefIds") Integer[] attributeDefIds, @Param("listValues") String[] listValues);

    @Query("SELECT count(ot) FROM com.cassinisys.platform.model.core.ObjectAttribute ot, com.cassinisys.platform.model.core.ObjectTypeAttribute ota" +
            " WHERE ot.id.attributeDef = ota.id and ota.lov.id= :lov and (ot.listValue= :listValue or (CAST(ot.mListValue as text)) LIKE '%' || :listValue || '%')")
    Integer getAttributeLovValueCount(@Param("lov") Integer lov, @Param("listValue") String listValue);

}
