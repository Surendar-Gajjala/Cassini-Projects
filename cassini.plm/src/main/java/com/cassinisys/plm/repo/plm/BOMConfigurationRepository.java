package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.BOMConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 16-03-2020.
 */
@Repository
public interface BOMConfigurationRepository extends JpaRepository<BOMConfiguration, Integer> {

    BOMConfiguration findByItemAndName(Integer itemId, String name);

    List<BOMConfiguration> findByItem(Integer itemId);

}
