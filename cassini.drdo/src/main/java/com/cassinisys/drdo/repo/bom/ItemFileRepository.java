package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@Repository
public interface ItemFileRepository extends JpaRepository<ItemFile, Integer> {

    List<ItemFile> findAllByItemAndName(Integer item, String name);

    List<ItemFile> findByItem(Integer item);

    List<ItemFile> findByItemAndLatestTrueOrderByModifiedDateDesc(Integer item);

    ItemFile findByItemAndNameAndLatestTrue(Integer item, String name);

    List<ItemFile> findByItemAndNameAndLatestFalseOrderByCreatedDateDesc(Integer item, String name);

    List<ItemFile> findByItemAndNameContainingIgnoreCaseAndLatestTrue(Integer item, String name);
}
