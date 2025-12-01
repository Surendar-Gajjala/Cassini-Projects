package com.cassinisys.is.repo.tm;

import com.cassinisys.is.model.tm.ISTaskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface TaskFileRepository extends JpaRepository<ISTaskFile, Integer> {

    List<ISTaskFile> findByTask(Integer task);

    List<ISTaskFile> findByTaskAndLatestTrueOrderByModifiedDateDesc(Integer task);

    List<ISTaskFile> findByTaskAndLatestTrueOrderByCreatedDateDesc(Integer task);

    ISTaskFile findByTaskAndNameAndLatestTrue(Integer task, String name);

    List<ISTaskFile> findAllByTaskAndNameOrderByCreatedDateDesc(@Param("taskId") Integer taskId, @Param("name") String name);
}
