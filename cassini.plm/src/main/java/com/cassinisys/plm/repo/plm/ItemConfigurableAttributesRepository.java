package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.ItemConfigurableAttributes;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 14-05-2020.
 */
@Repository
public interface ItemConfigurableAttributesRepository extends JpaRepository<ItemConfigurableAttributes, Long> {

    List<ItemConfigurableAttributes> findByItem(Integer item);

    List<ItemConfigurableAttributes> findByItemOrderByIdAsc(Integer item);

    ItemConfigurableAttributes findByItemAndAttribute(Integer item, PLMItemTypeAttribute attribute);
}
