package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.TodoItem;
import com.cassinisys.platform.model.col.TodoList;
import com.cassinisys.platform.repo.col.ToDoListRepository;
import com.cassinisys.platform.repo.col.TodoItemRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class TodoListService implements CrudService<TodoList, Integer>,
		PageableService<TodoList, Integer> {

	@Autowired
	private ToDoListRepository todoListRepository;

	@Autowired
	private TodoItemRepository todoItemRepository;

	@Override
	@Transactional
	public TodoList create(TodoList todoList) {
		checkNotNull(todoList);
		todoList.setId(null);
		todoList = todoListRepository.save(todoList);

		return todoList;
	}

	@Override
	@Transactional
	public TodoList update(TodoList todoList) {
		checkNotNull(todoList);
		checkNotNull(todoList.getId());
		todoList = todoListRepository.save(todoList);
				
		return todoList;
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		TodoList todoList = todoListRepository.findOne(id);
		if (todoList == null) {
			throw new ResourceNotFoundException();
		}
		todoListRepository.delete(todoList);
		}


	@Override
	@Transactional(readOnly = true)
	public TodoList get(Integer id) {
		checkNotNull(id);
		TodoList todoList = todoListRepository.findOne(id);
		if (todoList == null) {
			throw new ResourceNotFoundException();
		}
		return todoList;
	}


	@Override
	@Transactional(readOnly = true)
	public List<TodoList> getAll() {
		return todoListRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<TodoList> findAll(Pageable pageable) {
		checkNotNull(pageable);
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.DESC,
							"createdBy")));

		}
		return todoListRepository.findAll(pageable);
	}

	@Transactional
	public TodoItem createItem(TodoItem todoItem) {
		checkNotNull(todoItem);
		todoItem.setId(null);
		TodoList todoList = todoListRepository
				.findOne(todoItem.getTodoList());
		if (todoList == null) {
			throw new ResourceNotFoundException();
		}
		TodoItem item = todoItemRepository.save(todoItem);

		return item;
	}

	@Transactional
	public TodoItem updateItem(TodoItem todoItem) {
		checkNotNull(todoItem);
		checkNotNull(todoItem.getId());
		TodoList todoList = todoListRepository
				.findOne(todoItem.getTodoList());
		if (todoList == null) {
			throw new ResourceNotFoundException();
		}
		TodoItem item = todoItemRepository.save(todoItem);

		return item;
	}

	@Transactional
	public void deleteItem(Integer todoItemId) {
		checkNotNull(todoItemId);
		TodoItem item = todoItemRepository.findOne(todoItemId);
		if (item == null) {
			throw new ResourceNotFoundException();
		}
		TodoList todoList = todoListRepository.findOne(item.getTodoList());
		todoItemRepository.delete(item);

		}
	@Transactional(readOnly = true)
	public Page<TodoItem> getItems(Integer id, Pageable pageable) {
		checkNotNull(id);
		checkNotNull(pageable);
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order("id")));

		}
		return todoItemRepository.findByTodoList(id, pageable);
	}

}
