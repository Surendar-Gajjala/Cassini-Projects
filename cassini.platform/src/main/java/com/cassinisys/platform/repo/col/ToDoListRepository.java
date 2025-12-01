package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author reddy
 */
public interface ToDoListRepository extends JpaRepository<TodoList, Integer> {
}
