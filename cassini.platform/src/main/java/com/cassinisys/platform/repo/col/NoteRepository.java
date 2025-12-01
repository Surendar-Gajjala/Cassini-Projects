package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.Note;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author reddy
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByObjectTypeAndObjectId(ObjectType objectType, Integer objectId);
}
