package com.cassinisys.plm.repo.plm;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.plm.PLMSavedSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Repository
public interface SavedSearchRepository extends JpaRepository<PLMSavedSearch, Integer> {

    public List<PLMSavedSearch> findByObjectType(ObjectType objectType);

    public List<PLMSavedSearch> findBySearchObjectType(ObjectType objectType);

    public Page<PLMSavedSearch> findByOwner(Integer owner, Pageable pageable);

    PLMSavedSearch findByNameEqualsIgnoreCase(String name);

    PLMSavedSearch findByObjectTypeAndTypeAndNameEqualsIgnoreCase(ObjectType objectType, String type, String name);

    PLMSavedSearch findBySearchObjectTypeAndTypeAndNameEqualsIgnoreCase(ObjectType objectType, String type, String name);

    PLMSavedSearch findByOwnerAndObjectTypeAndTypeAndNameEqualsIgnoreCase(Integer owner, ObjectType objectType, String type, String name);

    PLMSavedSearch findByOwnerAndSearchObjectTypeAndTypeAndNameEqualsIgnoreCase(Integer owner, ObjectType objectType, String type, String name);

}
