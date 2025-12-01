package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.Bom;
import com.cassinisys.drdo.model.bom.ItemRevision;
import com.cassinisys.drdo.model.bom.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 07-10-2018.
 */
@Repository
public interface BomRepository extends JpaRepository<Bom, Integer> {

    Bom findByItem(ItemRevision itemId);

    @Query("SELECT i FROM Bom i where i.item.id=:id")
    Bom findByItemRevisionId(@Param("id") Integer id);

    @Query("SELECT i from Bom i where i.item.itemMaster.itemType.name= :name")
    List<Bom> findSystemTypeBoms(@Param("name") String name);

    @Query("SELECT i from Bom i where i.item.itemMaster.itemType in :types")
    List<Bom> findSystemTypeBoms(@Param("types") List<ItemType> types);

}
