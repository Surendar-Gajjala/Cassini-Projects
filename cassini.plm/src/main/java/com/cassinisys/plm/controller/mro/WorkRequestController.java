package com.cassinisys.plm.controller.mro;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.WorkRequestCriteria;
import com.cassinisys.plm.model.mro.MROWorkOrder;
import com.cassinisys.plm.model.mro.MROWorkRequest;
import com.cassinisys.plm.model.mro.WorkRequestDto;
import com.cassinisys.plm.service.mro.WorkRequestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by CassiniSystems on 27-10-2020.
 */
@RestController
@RequestMapping("/mro/workrequests")
@Api(tags = "PLM.MES", description = "MES Related")
public class WorkRequestController extends BaseController {


    @Autowired
    private WorkRequestService workRequestService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MROWorkRequest create(@RequestBody MROWorkRequest workRequest) {
        return workRequestService.create(workRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MROWorkRequest update(@PathVariable("id") Integer id,
                                 @RequestBody MROWorkRequest workRequest) {
        return workRequestService.update(workRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        workRequestService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MROWorkRequest get(@PathVariable("id") Integer id) {
        return workRequestService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MROWorkRequest> getAll() {
        return workRequestService.getAll();
    }

    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public List<MROWorkRequest> getAllPendingRequests() {
        return workRequestService.getAllPendingRequests();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MROWorkRequest> getMultiple(@PathVariable Integer[] ids) {
        return workRequestService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{id}/workorders", method = RequestMethod.GET)
    public List<MROWorkOrder> getWorkRequestWorkOrders(@PathVariable("id") Integer id) {
        return workRequestService.getWorkRequestWorkOrders(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<WorkRequestDto> getAllWorkRequests(PageRequest pageRequest, WorkRequestCriteria workRequestCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workRequestService.getAllWorkRequests(pageable, workRequestCriteria);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MROWorkRequest> getObjectsByType(@PathVariable("typeId") Integer id,
                                                 PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workRequestService.getObjectsByType(id, pageable);
    }

    @RequestMapping(value = "/{objectId}/uploadFiles", method = RequestMethod.POST)
    public MROWorkRequest uploadWorkRequestMediaFiles(@PathVariable("objectId") Integer objectId, MultipartHttpServletRequest request) throws Exception {
        return workRequestService.uploadWorkRequestMediaFiles(objectId, request);
    }
}
