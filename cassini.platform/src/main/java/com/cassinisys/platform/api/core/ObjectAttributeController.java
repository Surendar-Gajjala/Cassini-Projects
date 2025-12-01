package com.cassinisys.platform.api.core;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.service.core.ObjectAttributeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created GSR on 03-07-2017.
 */
@RestController
@RequestMapping("/core/objects/{objectId}/attributes")
@Api(tags = "PLATFORM.CORE",description = "Core endpoints")
public class ObjectAttributeController extends BaseController {

    @Autowired
    private ObjectAttributeService objectAttributeService;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ObjectAttribute createAttribute(@RequestBody ObjectAttribute objectAttribute) {
        return objectAttributeService.createAttribute(objectAttribute);

    }

    @RequestMapping(value = "{attributeId}/uploadImage", method = RequestMethod.POST)
    public ObjectAttribute uploadImage(@PathVariable("objectId") Integer objectId,
                                       @PathVariable("attributeId") Integer id, MultipartHttpServletRequest request) {
        ObjectAttribute att = objectAttributeService.findByObjectIdsInAndAttributeDefIdsIn(objectId, id);
        if (att != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    att.setImageValue(file.getBytes());
                    att = objectAttributeService.updateAttribute(att);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            ObjectAttribute objectAttribute = new ObjectAttribute();
            objectAttribute.setId(new ObjectAttributeId(objectId, id));

            objectAttribute = objectAttributeRepository.save(objectAttribute);

            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    objectAttribute.setImageValue(file.getBytes());
                    objectAttribute = objectAttributeService.updateAttribute(objectAttribute);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return att;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ObjectAttribute updateAttribute(@RequestBody ObjectAttribute objectAttribute) {
        return objectAttributeService.updateAttribute(objectAttribute);
    }

    @RequestMapping(value = "/{attributeId}", method = RequestMethod.DELETE)
    public void deleteAttribute(@PathVariable("objectId") Integer objectId,
                                @PathVariable("attributeId") Integer attributeId) {
        objectAttributeService.deleteAttribute(objectId, attributeId);
    }

    @RequestMapping(value = "/{attributeId}", method = RequestMethod.GET)
    public ObjectAttribute getAttribute(@PathVariable("attributeId") Integer attributeId) {
        return objectAttributeService.getAttribute(attributeId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ObjectAttribute> getAttributes(@PathVariable("objectId") Integer objectId) {
        return objectAttributeService.getAttributes(objectId);
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public List<ObjectAttribute> saveObjectAttributes(@PathVariable("objectId") Integer objectId,
                                                      @RequestBody List<ObjectAttribute> attributes) {
        return objectAttributeService.saveObjectAttributes(attributes);
    }

    @RequestMapping(value = "/{attributeId}/imageAttribute/download", method = RequestMethod.GET)
    public void downloadImageAttribute(@PathVariable("objectId") Integer objectId,
                                       @PathVariable("attributeId") Integer attributeId,
                                       HttpServletResponse response) {
        objectAttributeService.getImageAttribute(objectId, attributeId, response);
    }

    @RequestMapping(value = "/value/{attributeDef}", method = RequestMethod.GET)
    public ObjectAttribute getObjectAttributeByIdAndDef(@PathVariable("objectId") Integer objectId,
                                                        @PathVariable("attributeDef") Integer attributeDef) {
        return objectAttributeService.getObjectAttributeByIdAndDef(objectId, attributeDef);
    }

    @RequestMapping(value = "/{attributeId}/values", method = RequestMethod.GET)
    public List<ObjectAttribute> getUsedAttributes(@PathVariable("attributeId") Integer attributeId) {
        return objectAttributeService.getUsedAttributes(attributeId);
    }


}
