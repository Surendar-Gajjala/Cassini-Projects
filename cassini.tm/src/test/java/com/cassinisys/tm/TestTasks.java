package com.cassinisys.tm;

import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.tm.model.TMProjectTask;
import com.cassinisys.tm.model.TaskStatus;
import com.cassinisys.tm.model.dto.KeyAndNumber;
import com.cassinisys.tm.repo.ProjectTaskRepository;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Created by reddy on 9/9/16.
 */
public class TestTasks extends BaseTest {
    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private PageRequestConverter pageRequestConverter;


    @Test
    public void testTasks() throws Exception {
        /*
        List<KeyAndNumber> tasks = projectTaskRepository.findTasksGroupedByLocation();
        for(KeyAndNumber k : tasks) {
            System.out.println(k.getKey() + " : " + k.getNumber());
        }

        tasks = projectTaskRepository.findTasksByLocationGroupedByPerson("Hut-2");
        for(KeyAndNumber k : tasks) {
            System.out.println(k.getKey() + " : " + k.getNumber());
        }


        List<Object[]> list = projectTaskRepository.getAllTaskStats();
        for(Object[] arr : list) {
            System.out.println("Location: " + arr[0] + "  Status: " + arr[1] + "  Count: " + arr[2]);
        }
        */

        List<TMProjectTask> taskList = projectTaskRepository.findByLocationAndStatusIn("Hut-1", Arrays.asList(TaskStatus.APPROVED));
        System.out.println(taskList.size());
    }
}
