package com.cassinisys.is.repo.tm;

import com.cassinisys.is.model.tm.ISProjectTaskImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 24-09-2018.
 */
@Repository
public interface ProjectTaskImageRepository extends JpaRepository<ISProjectTaskImage, Integer> {

    ISProjectTaskImage findByTaskAndImageName(Integer taskId, String name);

    List<ISProjectTaskImage> findByTask(Integer task);
}
