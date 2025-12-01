package com.cassinisys.is.repo.tm;

import com.cassinisys.is.model.tm.AttachmentType;
import com.cassinisys.is.model.tm.ISTaskFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Cassinisys on 18-05-2017.
 */
@Repository
public interface TaskFilesRepository extends
        JpaRepository<ISTaskFiles, Integer> {
    /**
     * The method used to findByTaskAndType for the List of ISTaskFiles
     **/
    List<ISTaskFiles> findByTaskAndType(Integer taskId, AttachmentType type);

}
