package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMCustomerFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 13-12-2020.
 */
@Repository
public interface CustomerFileRepository extends JpaRepository<PQMCustomerFile, Integer> {
    List<PQMCustomerFile> findByIdIn(Iterable<Integer> ids);

    List<PQMCustomerFile> findByCustomer(Integer customer);

    List<PQMCustomerFile> findByCustomerAndName(Integer mfrId, String name);

    List<PQMCustomerFile> findByCustomerAndLatestTrueOrderByModifiedDateDesc(Integer customer);

    PQMCustomerFile findByCustomerAndNameAndLatestTrue(Integer customer, String name);

    List<PQMCustomerFile> findAllByCustomerAndNameOrderByCreatedDateDesc(@Param("customerId") Integer mfrId, @Param("name") String name);

    List<PQMCustomerFile> findByCustomerAndNameContainingIgnoreCase(Integer customerId, String name);

    List<PQMCustomerFile> findByCustomerAndNameAndLatestFalseOrderByCreatedDateDesc(Integer customer, String name);

    List<PQMCustomerFile> findByCustomerAndNameContainingIgnoreCaseAndLatestTrue(Integer customer, String name);

    List<PQMCustomerFile> findByCustomerAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer customer, String fileNo);

    List<PQMCustomerFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PQMCustomerFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PQMCustomerFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PQMCustomerFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PQMCustomerFile> findByCustomerAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer customer);

    PQMCustomerFile findByCustomerAndNameAndParentFileIsNullAndLatestTrue(Integer customer, String name);

    PQMCustomerFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PQMCustomerFile> findByCustomerAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer customer, String fileType);

    PQMCustomerFile findByCustomerAndFileNoAndLatestTrue(Integer mfrId, String fileNo);

    List<PQMCustomerFile> findByCustomerAndFileNo(Integer mfrId, String fileNo);

    PQMCustomerFile findByNameEqualsIgnoreCaseAndParentFileAndCustomerAndLatestTrue(String name, Integer parent, Integer customer);

    PQMCustomerFile findByNameEqualsIgnoreCaseAndCustomerAndLatestTrue(String name, Integer customer);

    @Query("select i.id from PQMCustomerFile i where i.customer= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByCustomerAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PQMCustomerFile i where i.customer= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByCustomerAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);
}
