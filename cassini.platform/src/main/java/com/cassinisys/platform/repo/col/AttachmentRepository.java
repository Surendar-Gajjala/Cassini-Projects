package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author reddy
 */
public interface AttachmentRepository extends
        JpaRepository<Attachment, Integer>, QueryDslPredicateExecutor<Attachment> {
    List<Attachment> findByObjectIdAndObjectType(Integer objectId, Enum objectType);

    List<Attachment> findByObjectIdAndObjectTypeOrderByAddedOnAsc(Integer objectId, Enum objectType);

    Attachment findByObjectId(Integer objectId);

    List<Attachment> findByExtension(String ext);

    List<Attachment> findByExtensionIn(Iterable<String> exts);

    List<Attachment> findByIdIn(Iterable<Integer> attachmentIds);

    Attachment findByObjectIdAndName(Integer objectId, String name);

    @Query("select i.name from Attachment i where i.id in :ids")
    List<String> getAttachmentNamesByIds(@Param("ids") List<Integer> ids);
}
