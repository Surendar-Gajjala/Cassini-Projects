package com.cassinisys.plm.controller.wf;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.plm.dto.ObjectCountsDto;
import com.cassinisys.plm.model.wf.UserTaskStatus;
import com.cassinisys.plm.model.wf.dto.TasksByStatusDTO;
import com.cassinisys.plm.model.wf.dto.UserTaskDto;
import com.cassinisys.plm.service.rm.RecentlyVisitedService;
import com.cassinisys.plm.service.tm.UserTaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plm/usertasks")
@Api(tags = "PLM.WF", description = "Workflow Related")
public class UserTasksController extends BaseController {
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private RecentlyVisitedService recentlyVisitedService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping("/{id}")
    public List<UserTaskDto> getUserTasks(@PathVariable Integer id, @RequestParam(name = "status", required = false) UserTaskStatus status) {
        if (status == null) {
            return userTaskService.getAllByUser(id);
        } else {
            return userTaskService.getAllByUserAndStatus(id, status);
        }
    }

    @RequestMapping("/{id}/mobile")
    public List<UserTaskDto> getUserTasksForMobile(@PathVariable Integer id) {
        return userTaskService.getTasksByUser(id);
    }

    @RequestMapping("/countsbystatus")
    public List<TasksByStatusDTO> getTaskCountsByStatus() {
        return userTaskService.getTaskCountsByStatus();
    }

    @RequestMapping("/counts/status")
    public Integer getUserTaskCountsByStatus() {
        return userTaskService.getUserTaskCountsByStatus();
    }

    @RequestMapping("/object/counts")
    public ObjectCountsDto getObjectCounts() {
        return recentlyVisitedService.getObjectCounts();
    }


    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public Page<Comment> getAllComments(@RequestParam(value = "objectType", required = false) ObjectType objectType,
                                        @RequestParam(value = "objectId", required = false) Integer objectId,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return userTaskService.getAllComments(objectType, objectId, pageable);
    }

    @RequestMapping(value = "/comments/all/{person}/count", method = RequestMethod.GET)
    public Integer getConversationCountByPerson(@PathVariable("person") Integer person) {
        return userTaskService.getConversationCountByPerson(person);
    }

    @RequestMapping(value = "/comments/person/{personId}/unread/all", method = RequestMethod.GET)
    public Page<Comment> getAllUnreadMessageByPerson(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return userTaskService.getAllUnreadMessageByPerson(personId, pageable);
    }

    @RequestMapping(value = "/comments/search", method = RequestMethod.GET)
    public Page<Comment> searchComments(@RequestParam String query,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return userTaskService.searchComments(query, pageable);
    }

}
