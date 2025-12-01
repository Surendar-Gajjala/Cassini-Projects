package com.cassinisys.is.repo.col;
/**
 * The Class is for ProjectMessageRepository
 **/

import com.cassinisys.is.model.col.ISProjectMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMessageRepository extends
        JpaRepository<ISProjectMessage, Integer> {
    /**
     * The method used to findByProject for ISProjectMessage
     **/
    public Page<ISProjectMessage> findByProject(Integer projectId,
                                                Pageable pageable);

    /**
     * The method used to findByProjectAndSentToId of ISProjectMessage
     **/
    public Page<ISProjectMessage> findByProjectAndSentToId(Integer projectId,
                                                           Integer sentToId, Pageable pageable);

    /**
     * The method used to findByProjectAndSentBy of ISProjectMessage
     **/
    public Page<ISProjectMessage> findByProjectAndSentBy(Integer projectId,
                                                         Integer sentBy, Pageable pageable);

    /**
     * The method used to findByIdIn from the list of ISProjectMessage
     **/
    public List<ISProjectMessage> findByIdIn(Iterable<Integer> ids);

}
