package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCDeclarationSpecification;
import com.cassinisys.plm.model.pgc.PGCSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCDeclarationSpecificationRepository extends JpaRepository<PGCDeclarationSpecification, Integer> {

    @Query("select i.specification.id from PGCDeclarationSpecification i where i.declaration= :declaration")
    List<Integer> getSpecificationIdsByDeclaration(@Param("declaration") Integer declaration);

    @Query("select i from PGCDeclarationSpecification i where i.declaration= :declaration")
    List<PGCDeclarationSpecification> getDeclarationSpecifications(@Param("declaration") Integer declaration);

    @Query("select count(i) from PGCDeclarationSpecification i where i.declaration= :declaration")
    Integer getDeclarationSpecificationCount(@Param("declaration") Integer declaration);

    @Query("select i from PGCDeclarationSpecification i where i.declaration= :declaration and i.specification.id= :specification")
    PGCDeclarationSpecification getByDeclarationAndSpecification(@Param("declaration") Integer declaration, @Param("specification") Integer specification);

    List<PGCDeclarationSpecification> findBySpecification(PGCSpecification specification);

}
