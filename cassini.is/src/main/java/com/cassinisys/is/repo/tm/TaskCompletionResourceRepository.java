package com.cassinisys.is.repo.tm;

import com.cassinisys.is.model.tm.ISTaskCompletionResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 30-01-2020.
 */
@Repository
public interface TaskCompletionResourceRepository extends JpaRepository<ISTaskCompletionResource, Integer> {

    List<ISTaskCompletionResource> findByTaskHistory(Integer id);
}
