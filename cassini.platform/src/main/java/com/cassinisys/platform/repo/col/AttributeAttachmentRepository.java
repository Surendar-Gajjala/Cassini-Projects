package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.AttributeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 024 24-Jul -17.
 */
@Repository
public interface AttributeAttachmentRepository extends JpaRepository<AttributeAttachment, Integer> {

    List<AttributeAttachment> findByObjectIdAndAttributeDef(Integer objectId, Integer attributeDef);

    List<AttributeAttachment> findByObjectId(Integer objectId);

    List<AttributeAttachment> findByIdIn(Iterable<Integer> attachmentIds);
}
