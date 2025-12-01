package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.Media;
import com.cassinisys.platform.model.col.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 23/11/18.
 */
@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {

    List<Media> findByObjectIdOrderByCreatedDateDesc(Integer objectId);

    List<Media> findByIdIn(Iterable<Integer> ids);

    @Query(
            "SELECT count(i) FROM Media i WHERE i.objectId= :objectId and i.mediaType= :mediaType"
    )
    Integer findCountByObjectIdAndExtension(@Param("objectId") Integer objectId, @Param("mediaType") MediaType mediaType);

    @Query(
            "SELECT i FROM Media i WHERE i.objectId= :objectId and i.mediaType= :mediaType"
    )
    List<Media> getMediaByType(@Param("objectId") Integer objectId, @Param("mediaType") MediaType mediaType);

    Media findByObjectIdAndFileName(Integer objectId, String fileName);
}
