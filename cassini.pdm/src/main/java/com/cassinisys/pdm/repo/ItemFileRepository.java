package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMItem;
import com.cassinisys.pdm.model.PDMItemFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface ItemFileRepository extends JpaRepository<PDMItemFile, Integer>{
    List<PDMItemFile> findByItemAndName(Integer itemId, String name);
    List<PDMItemFile> findByItemAndLatestTrueOrderByModifiedDateDesc(Integer item);
    PDMItemFile findByItemAndNameAndLatestTrue(Integer item,String name);

    @Query(
            "SELECT f FROM PDMItemFile f WHERE f.item.id = :itemId AND f.name = :name ORDER BY f.version DESC"
    )
    List<PDMItemFile> findAllByItemAndName(@Param("itemId") Integer itemId, @Param("name") String name);
}
