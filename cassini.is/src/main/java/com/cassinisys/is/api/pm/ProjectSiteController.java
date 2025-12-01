package com.cassinisys.is.api.pm;
/**
 * The Class is for ProjectSiteController
 **/

import com.cassinisys.is.filtering.SiteCriteria;
import com.cassinisys.is.model.pm.ISProjectSite;
import com.cassinisys.is.model.pm.ProjectSiteDto;
import com.cassinisys.is.model.pm.SiteResourceDto;
import com.cassinisys.is.model.tm.DetailsCountDto;
import com.cassinisys.is.model.tm.ISProjectTask;
import com.cassinisys.is.model.tm.ProjectTaskDto;
import com.cassinisys.is.service.pm.ProjectSiteService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "/sites", description = "Sites endpoint")
@RestController
@RequestMapping("/sites")
public class ProjectSiteController extends BaseController {

    @Autowired
    private ProjectSiteService siteService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    /**
     * The method used getMultipleTasksBySite in ISProjectTask
     **/
    @RequestMapping(value = "/siteIds/[{siteIds}]", method = RequestMethod.GET)
    public List<ISProjectTask> getMultipleTasksBySite(@PathVariable Integer[] siteIds) {
        return siteService.findMultipleSitesByIds(Arrays.asList(siteIds));
    }

    /**
     * The method used for creating the ISProjectSite
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProjectSite create(@RequestBody ISProjectSite site) {
        return siteService.create(site);
    }

    /**
     * The method used for updating the ISProjectSite
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISProjectSite update(@PathVariable("id") Integer id,
                                @RequestBody ISProjectSite site) {
        site.setId(id);
        return siteService.update(site);
    }

    /**
     * The method used for deleting the ISProjectTask
     **/
    @RequestMapping(value = "/{projectId}/site/{siteId}", method = RequestMethod.DELETE)
    public void deleteSiteAndStores(@PathVariable("projectId") Integer projectId, @PathVariable("siteId") Integer siteId) {
        siteService.delete(siteId);
    }

    /**
     * The method used to get the value  for ISProjectSite
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISProjectSite get(@PathVariable("id") Integer id) {
        return siteService.get(id);
    }

    /**
     * The method used to getall the values  for ISProjectSite
     **/
    @RequestMapping(method = RequestMethod.GET)
    public List<ISProjectSite> getAll() {
        return siteService.getAll();
    }

    /**
     * The method used to find the ISProjectSite by project
     **/

    @RequestMapping(value = "/byProject/pageable/{projectId}", method = RequestMethod.GET)
    public Page<ISProjectSite> findByProject(@PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return siteService.findByProject(projectId, pageable);
    }

    /**
     * The method used to findall sites of the ISProjectSite
     **/
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISProjectSite> findAll(PageRequest pageRequest) {
        return siteService.findAll(pageRequest);
    }

    /**
     * The method used for freeTextSearch of ISSites
     **/
    @RequestMapping(value = "/{projectId}/freesearch", method = RequestMethod.GET)
    public Page<ISProjectSite> freeTextSearch(@PathVariable("projectId") Integer projectId,
                                              PageRequest pageRequest,
                                              SiteCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISProjectSite> siteStores = siteService.freeTextSearch(pageable, criteria);
        return siteStores;
    }

    @RequestMapping(value = "/{projectId}/siteName/{siteName}", method = RequestMethod.GET)
    public ISProjectSite getSiteByName(@PathVariable("projectId") Integer projectId, @PathVariable("siteName") String siteName) {
        return siteService.getSiteByName(projectId, siteName);
    }

    @RequestMapping(value = "/{projectId}/site/{siteId}", method = RequestMethod.GET)
    public DetailsCountDto getSiteDetailsCount(@PathVariable("projectId") Integer projectId, @PathVariable("siteId") Integer siteId) {
        return siteService.getSiteDetailsCount(projectId, siteId);
    }

    @RequestMapping(value = "/{siteId}/tasks", method = RequestMethod.GET)
    public List<ProjectTaskDto> getSiteTasks(@PathVariable("siteId") Integer siteId) {
        return siteService.getSiteTasks(siteId);
    }

    @RequestMapping(value = "/{siteId}/resources", method = RequestMethod.GET)
    public List<SiteResourceDto> getSiteResources(@PathVariable("siteId") Integer siteId) {
        return siteService.getSiteResources(siteId);
    }

    @RequestMapping(value = "{projectId}/siteList", method = RequestMethod.GET)
    public List<ProjectSiteDto> getSitesByProject(@PathVariable("projectId") Integer projectId) {
        return siteService.getSitesByProject(projectId);
    }
}
