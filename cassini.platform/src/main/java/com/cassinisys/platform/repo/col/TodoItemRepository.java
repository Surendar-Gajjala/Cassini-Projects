package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.TodoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author reddy
 */
public interface TodoItemRepository extends JpaRepository<TodoItem, Integer> {

	public Page<TodoItem> findByTodoList(Integer todoListId, Pageable pageable);

}
