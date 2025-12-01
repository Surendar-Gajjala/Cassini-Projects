package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.TodoItem;
import com.cassinisys.platform.model.col.TodoList;
import com.cassinisys.platform.service.col.TodoListService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/col/todo")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class TodoListController extends BaseController {

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private TodoListService todoListService;
	
	@RequestMapping(method = RequestMethod.POST)
	public TodoList create(@RequestBody TodoList todoList) {
		todoList.setId(null);
		return todoListService.create(todoList);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public TodoList update(@PathVariable("id") Integer id,
			@RequestBody TodoList todoList) {
		todoList.setId(id);
		return todoListService.update(todoList);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		todoListService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public TodoList get(@PathVariable("id") Integer id) {
		return todoListService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<TodoList> getAll(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return todoListService.findAll(pageable);
	}

	@RequestMapping(value = "/{todoId}/items", method = RequestMethod.POST)
	public TodoItem createItem(@PathVariable("todoId") Integer todoId,
			@RequestBody TodoItem item) {
		item.setId(null);
		return todoListService.createItem(item);
	}

	@RequestMapping(value = "/{todoId}/items/{itemId}",
			method = RequestMethod.PUT)
	public TodoItem updateItem(@PathVariable("todoId") Integer todoId,
			@PathVariable("itemId") Integer itemId, @RequestBody TodoItem item) {
		item.setId(itemId);
		return todoListService.updateItem(item);
	}

	@RequestMapping(value = "/{todoId}/items/{itemId}",
			method = RequestMethod.DELETE)
	public void deleteItem(@PathVariable("todoId") Integer todoId,
			@PathVariable("itemId") Integer itemId) {
		todoListService.deleteItem(itemId);
	}

	@RequestMapping(value = "/{todoId}/items", method = RequestMethod.GET)
	public Page<TodoItem> getItems(@PathVariable("todoId") Integer todoId,
			PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return todoListService.getItems(todoId, pageable);
	}

}
