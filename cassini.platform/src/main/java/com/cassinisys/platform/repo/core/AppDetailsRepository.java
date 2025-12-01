package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.AppDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Nageshreddy on 13-11-2019.
 */
@Repository
public interface AppDetailsRepository extends JpaRepository<AppDetails, Integer>, QueryDslPredicateExecutor<AppDetails> {
    @Query(value = "SELECT pg_database_size('cassiniapps') As totalDiskSpace,\n" +
            "       CAST(pg_database_size('cassiniapps') - (SELECT SUM(pg_total_relation_size(table_schema || '.' || table_name)  )\n" +
            "                                                              FROM information_schema.tables WHERE table_schema = :defaultSchema) As bigint) As  availableSpace,\n" +
            "       CAST((SELECT SUM(pg_total_relation_size(table_schema || '.' || table_name) )\n" +
            "                            FROM information_schema.tables WHERE table_schema = :defaultSchema) As bigint)   As usageSpace", nativeQuery = true)
    Object[] getSystemInformation(@Param("defaultSchema") String defaultSchema);

    AppDetails findByOptionName(String ip_address);
}
