package com.cassinisys.plm.controller.rm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ItemCriteria;
import com.cassinisys.plm.model.dto.PersonsDto;
import com.cassinisys.plm.model.plm.dto.TopSearchDto;
import com.cassinisys.plm.model.rm.CommonFileDto;
import com.cassinisys.plm.model.rm.RecentlyVisited;
import com.cassinisys.plm.service.rm.RecentlyVisitedService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subra on 21-09-2018.
 */
@RestController
@RequestMapping("/plm/recentlyVisited")
@Api(tags = "PLM.RM", description = "Requirement Related")
public class RecentlyVisitedController extends BaseController {

    @Autowired
    private RecentlyVisitedService recentlyVisitedService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public RecentlyVisited create(@RequestBody RecentlyVisited recentlyVisited) {
        return recentlyVisitedService.create(recentlyVisited);
    }

    @RequestMapping(value = "/{visitedId}", method = RequestMethod.PUT)
    public RecentlyVisited update(@PathVariable("visitedId") Integer visitedId, @RequestBody RecentlyVisited recentlyVisited) {
        return recentlyVisitedService.update(recentlyVisited);
    }

    @RequestMapping(value = "/{visitedId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("visitedId") Integer visitedId) {
        recentlyVisitedService.delete(visitedId);
    }

    @RequestMapping(value = "/{visitedId}", method = RequestMethod.GET)
    public RecentlyVisited getRecentlyVisited(@PathVariable("visitedId") Integer visitedId) {
        return recentlyVisitedService.get(visitedId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RecentlyVisited> getAll() {
        return recentlyVisitedService.getAll();
    }

    @RequestMapping(value = "person/{personId}/all", method = RequestMethod.GET)
    public Page<RecentlyVisited> getAllRecentlyVisited(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return recentlyVisitedService.getAllRecentlyVisited(personId, pageable);
    }

    @RequestMapping(value = "/commonFiles/freeTextSearch/{searchText}/filter/{filter}", method = RequestMethod.GET)
    public Page<CommonFileDto> getFreeTextSearchFiles(@PathVariable("searchText") String searchText, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return recentlyVisitedService.getFreeTextSearchFiles(searchText, pageable);
    }

    @RequestMapping(value = "/files/updateFileNo", method = RequestMethod.GET)
    public void updateFileNo() {
        recentlyVisitedService.updateFileNo();
    }

    @RequestMapping(value = "/topsearch/{objectType}", method = RequestMethod.GET)
    public TopSearchDto getItemTopSearch(@PathVariable("objectType") String objectType, PageRequest pageRequest, ItemCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        TopSearchDto topSearchDto = recentlyVisitedService.getItemTopSearch(pageable, criteria, objectType);
        return topSearchDto;
    }

    @RequestMapping(value = "/personswithoutlogin", method = RequestMethod.GET)
    public PersonsDto getPersonsWithoutLogin() {
        return recentlyVisitedService.getPersonsWithoutLogin();
    }
}
