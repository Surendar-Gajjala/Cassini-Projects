package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.UserReadComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 24-08-2021.
 */
@Repository
public interface UserReadCommentRepository extends JpaRepository<UserReadComment, Integer>, QueryDslPredicateExecutor<UserReadComment> {

    @Query("select i from UserReadComment i where i.comment in :commentIds and i.person= :person")
    List<UserReadComment> getUserReadsByCommentsAndPerson(@Param("commentIds") List<Integer> commentIds, @Param("person") Integer person);

    @Query("select i from UserReadComment i where i.comment= :commentId and i.person= :person")
    UserReadComment getUserReadByCommentAndPerson(@Param("commentId") Integer commentId, @Param("person") Integer person);

    @Query("select count (i) from UserReadComment i where i.person= :person and i.read = false")
    Integer getUserUnreadCommentCount(@Param("person") Integer person);

    @Query("select i.comment from UserReadComment i where i.person= :person and i.read = false")
    List<Integer> getUnreadCommentIdsByPerson(@Param("person") Integer person);

    @Query("select i from UserReadComment i where i.person= :person and i.read = false")
    List<UserReadComment> getUnreadCommentsByPerson(@Param("person") Integer person);

    @Query("select count (i) from UserReadComment i where i.person= :person and i.read = false and i.comment not in :commentIds")
    Integer getUnreadCommentCountWithoutCommentIds(@Param("person") Integer person, @Param("commentIds") List<Integer> commentIds);

    @Query("select i.comment from UserReadComment i where i.person= :person and i.read = false and i.comment not in :commentIds")
    List<Integer> getUnreadCommentIdsWithoutCommentIds(@Param("person") Integer person, @Param("commentIds") List<Integer> commentIds);
}
