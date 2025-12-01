package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMVault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMVaultRepository extends JpaRepository<PDMVault, Integer>, QueryDslPredicateExecutor<PDMVault> {
    PDMVault findByName(String name);

    List<PDMVault> findByIdIn(Iterable<Integer> ids);

    @Query(value = "select v.vault, f.file_type, count(*) from pdm_file f inner join pdm_vault_object v on f.id = v.id group by v.vault, f.file_type;", nativeQuery = true)
    List<Object[]> getCountsByType();

    @Query("select count (i) from PDMVault i")
    Integer getVaultsCount();

    @Query("select count (i) from PDMVault i where i.id in :versionIds and ((LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%')")
    Integer getVaultIdsBySearchQueryAndVersionIds(@Param("searchText") String searchText, @Param("versionIds") Iterable<Integer> versionIds);

    @Query("select count (i) from PDMVault i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getVaultIdsBySearchQuery(@Param("searchText") String searchText);

}
