package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Note;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.col.NoteService;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/col/notes")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class NoteController extends BaseController {

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private NoteService noteService;
	
	@RequestMapping(method = RequestMethod.POST)
	public Note create(@RequestBody Note note) {
		note.setId(null);
		return noteService.create(note);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Note update(@PathVariable("id") Integer id,
			@RequestBody Note note) {
		note.setId(id);
		return noteService.update(note);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		noteService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Note get(@PathVariable("id") Integer id) {
		return noteService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Note> getAll(@RequestParam(value = "objectType", required = false) ObjectType objectType,
							 @RequestParam(value = "objectId", required = false) Integer objectId) {
		return noteService.getObjectNotes(objectType, objectId);
	}
}
