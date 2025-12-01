package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.JigsFixtureCriteria;
import com.cassinisys.plm.model.mes.MESJigsFixture;
import com.cassinisys.plm.model.mes.dto.JigsFixtureDto;
import com.cassinisys.plm.service.mes.JigsFixtureService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CassiniSystems on 27-10-2020.
 */
@RestController
@RequestMapping("/mes/jigsfixs")
@Api(tags = "PLM.MES", description = "MES Related")
public class JigsFixtureController extends BaseController {


    @Autowired
    private JigsFixtureService jigsFixtureService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESJigsFixture create(@RequestBody MESJigsFixture jigsFixture) {
        return jigsFixtureService.create(jigsFixture);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESJigsFixture update(@PathVariable("id") Integer id,
                                 @RequestBody MESJigsFixture jigsFixture) {
        return jigsFixtureService.update(jigsFixture);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        jigsFixtureService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESJigsFixture get(@PathVariable("id") Integer id) {
        return jigsFixtureService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESJigsFixture> getAll() {
        return jigsFixtureService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESJigsFixture> getMultiple(@PathVariable Integer[] ids) {
        return jigsFixtureService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<JigsFixtureDto> getAllMaterials(PageRequest pageRequest, JigsFixtureCriteria jigsFixtureCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return jigsFixtureService.getAllJigsFixtures(pageable, jigsFixtureCriteria);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.POST)
    public MESJigsFixture uploadImage(@PathVariable("id") Integer id, MultipartHttpServletRequest request) {
        return jigsFixtureService.uploadImage(id, request);
    }

    @RequestMapping(value = "/{id}/image/download", method = RequestMethod.GET)
    public void downloadImage(@PathVariable("id") Integer id,
                              HttpServletResponse response) {
        jigsFixtureService.downloadImage(id, response);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESJigsFixture> getJigsFixturesByType(@PathVariable("typeId") Integer id,
                                                      PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return jigsFixtureService.getJigsFixturesByType(id, pageable);
    }
}
