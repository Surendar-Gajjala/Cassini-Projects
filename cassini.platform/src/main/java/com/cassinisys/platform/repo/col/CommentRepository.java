package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author reddy
 */
public interface CommentRepository extends JpaRepository<Comment, Integer>, QueryDslPredicateExecutor<Comment> {

    Page<Comment> findByObjectIdAndObjectTypeAndReplyToIsNull(
            Integer objectId, ObjectType objectType, Pageable pageable);

    Page<Comment> findByIdIn(Iterable<Integer> ids, Pageable pageable);

    Page<Comment> findByReplyTo(Integer replyTo, Pageable pageable);

    List<Comment> findAllByObjectId(Integer fileId);

    List<Comment> findByObjectIdAndObjectType(Integer fileId, ObjectType objectType);

    List<Comment> findByObjectIdAndObjectTypeOrderByCommentedDateDesc(Integer fileId, ObjectType objectType);

    @Modifying
    @Query(
            value = "DELETE FROM Comment c WHERE c.objectId= :objectId"
    )
    void deleteCommentByObjectId(@Param("objectId") Integer objectId);

    @Query("select i.id from Comment i where i.objectId in :objectIds")
    List<Integer> findByObjectIdIn(@Param("objectIds") List<Integer> objectIds);

    @Query("select count (i) from Comment i where i.objectId= :objectId and i.objectType= :objectType")
    Integer getCommentCountByObjectIdAndObjectType(@Param("objectId") Integer objectId, @Param("objectType") ObjectType objectType);
}
