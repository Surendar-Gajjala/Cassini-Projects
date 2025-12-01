package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.col.UserReadComment;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.col.CommentService;
import com.cassinisys.platform.service.col.MediaService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/col/comments")
@Api(tags = "PLATFORM.COMMON", description = "Common endpoints")
public class CommentController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MediaService mediaService;

    @RequestMapping(method = RequestMethod.POST)
    public Comment create(@RequestBody Comment comment) {
        comment.setId(null);
        return commentService.create(comment);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Comment update(@PathVariable("id") Integer id,
                          @RequestBody Comment comment) {
        comment.setId(id);
        return commentService.update(comment);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        commentService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Comment get(@PathVariable("id") Integer id) {
        return commentService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Comment> getAll(@RequestParam(value = "objectType", required = false) ObjectType objectType,
                                @RequestParam(value = "objectId", required = false) Integer objectId,
                                PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return commentService.getRootComments(objectType, objectId, pageable);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Comment> getAllComments(@RequestParam("objectType") ObjectType objectType,
                                        @RequestParam("objectId") Integer objectId) {
        return commentService.getAllComments(objectType, objectId);
    }

    @RequestMapping(value = "/all/{person}/count", method = RequestMethod.GET)
    public Integer getMessageCountByPerson(@PathVariable("person") Integer person) {
        return commentService.getMessageCountByPerson(person);
    }

    @RequestMapping(value = "/all/{person}/[{commentIds}]", method = RequestMethod.GET)
    public List<UserReadComment> getUserReadsByPersonAndComments(@PathVariable("person") Integer person, @PathVariable("commentIds") Integer[] ids) {
        return commentService.getUserReadsByPersonAndComments(person, Arrays.asList(ids));
    }

    @RequestMapping(value = "/{commentId}/person/{person}/read", method = RequestMethod.GET)
    public UserReadComment updateUserReadComment(@PathVariable("commentId") Integer commentId, @PathVariable("person") Integer person) {
        return commentService.updateUserReadComment(commentId, person);
    }

    @RequestMapping(value = "/person/{person}/unread/all/update", method = RequestMethod.GET)
    public List<UserReadComment> updateAllUnreadComments(@PathVariable("person") Integer person) {
        return commentService.updateAllUnreadComments(person);
    }

    @RequestMapping(value = "/all/count", method = RequestMethod.GET)
    public Long getMessageCount() {
        return commentService.getMessageCount();
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getAllCommentsCount(@RequestParam("objectType") ObjectType objectType,
                                       @RequestParam("objectId") Integer objectId) {
        return commentService.getAllCommentsCount(objectType, objectId);
    }

    @RequestMapping(value = "/{id}/replies", method = RequestMethod.GET)
    public Page<Comment> getReplies(@PathVariable("id") Integer id,
                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return commentService.getReplies(id, pageable);
    }

    @RequestMapping(value = "/{objectId}/uploadFiles", method = RequestMethod.POST)
    public Comment uploadCommentMedia(@PathVariable("objectId") Integer objectId, MultipartHttpServletRequest request) throws Exception {
        return commentService.uploadCommentMedia(objectId, request);
    }

    @RequestMapping(value = "/image/{objectId}", method = RequestMethod.GET)
    public void getCommentImage(@PathVariable("objectId") Integer objectId, HttpServletResponse response) {
        mediaService.getMediaById(objectId, response);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<Comment> searchComments(@RequestParam String query,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return commentService.searchComments(query, pageable);
    }

}
