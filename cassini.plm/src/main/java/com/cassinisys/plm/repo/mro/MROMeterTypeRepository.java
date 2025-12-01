package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROMeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR Cassini on 17-11-2020.
 */
@Repository
public interface MROMeterTypeRepository extends JpaRepository<MROMeterType, Integer> {

    List<MROMeterType> findByIdIn(Iterable<Integer> ids);

    List<MROMeterType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MROMeterType> findByParentTypeIsNullOrderByIdAsc();

    List<MROMeterType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MROMeterType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MROMeterType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MROMeterType> findByParentTypeOrderByIdAsc(Integer id);

    MROMeterType findByName(String name);

    List<MROMeterType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);
}
