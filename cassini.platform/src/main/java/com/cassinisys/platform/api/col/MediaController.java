package com.cassinisys.platform.api.col;

/**
 * Created by swapna on 23/11/18.
 */

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Location;
import com.cassinisys.platform.model.col.Media;
import com.cassinisys.platform.service.col.MediaService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/col/media")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class MediaController extends BaseController {

    @Autowired
    private MediaService mediaService;

    @RequestMapping(value = "/object/{objectId}", method = RequestMethod.POST)
    public List<Media> uploadMedia(@PathVariable("objectId") Integer objectId, MultipartHttpServletRequest request, Location location) throws Exception {
        return mediaService.uploadMedia(objectId, request, location);
    }

    @RequestMapping(value = "/object/{objectId}", method = RequestMethod.GET)
    public List<Media> getMediaByObjectId(@PathVariable("objectId") Integer objectId) {
        return mediaService.getMediaByObjectId(objectId);
    }

    @RequestMapping(value = "/{mediaId}/bytes", method = RequestMethod.GET)
    public void getMediaById(@PathVariable("mediaId") Integer mediaId, HttpServletResponse response) {
        mediaService.getMediaById(mediaId, response);
    }

    @RequestMapping(value = "/{mediaId}", method = RequestMethod.DELETE)
    public void deleteMediaById(@PathVariable("mediaId") Integer mediaId) {
        mediaService.deleteMedia(mediaId);
    }

    @RequestMapping(value = "/{id}/preview", method = RequestMethod.GET)
    public void previewFile(@PathVariable("id") Integer id, HttpServletResponse response) throws Exception {
        mediaService.previewFile(id, response);
    }
}
