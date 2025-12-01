package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Note;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.NoteRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class NoteService implements CrudService<Note, Integer>,
		PageableService<Note, Integer> {

	@Autowired
	private NoteRepository noteRepository;

	
	@Override
	@Transactional
	public Note create(Note note) {
		checkNotNull(note);
		note.setId(null);
		note = noteRepository.save(note);

		return note;
	}

	@Override
	@Transactional
	public Note update(Note note) {
		checkNotNull(note);
		checkNotNull(note.getId());
		note = noteRepository.save(note);

		return note;
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		Note note = noteRepository.findOne(id);
		if (note == null) {
			throw new ResourceNotFoundException();
		}
		noteRepository.delete(note);

	}

	@Override
	@Transactional(readOnly = true)
	public Note get(Integer id) {
		checkNotNull(id);
		Note note = noteRepository.findOne(id);
		if (note == null) {
			throw new ResourceNotFoundException();
		}
		return note;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Note> getAll() {
		return noteRepository.findAll();
	}


	@Transactional(readOnly = true)
	public List<Note> getObjectNotes(ObjectType objectType, Integer objectId) {
		return noteRepository.findByObjectTypeAndObjectId(objectType, objectId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Note> findAll(Pageable pageable) {
		checkNotNull(pageable);
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order("createdDate")));
		}
		return noteRepository.findAll(pageable);
	}

}
