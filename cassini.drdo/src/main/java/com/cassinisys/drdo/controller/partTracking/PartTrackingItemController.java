package com.cassinisys.drdo.controller.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTrackingItems;
import com.cassinisys.drdo.model.partTracking.PartTrackingScannedUPN;
import com.cassinisys.drdo.model.partTracking.TrackValue;
import com.cassinisys.drdo.service.partTracking.PartTrackingItemsService;
import com.cassinisys.drdo.service.partTracking.PartTrackingScannedUPNService;
import com.cassinisys.drdo.service.partTracking.TrackValueService;
import com.cassinisys.platform.api.core.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@RestController
@RequestMapping(value = "drdo/itemPartTrackings")
@Api(name = "PartTracking", description = "PartTracking endpoint", group = "IM")
public class PartTrackingItemController extends BaseController {

    @Autowired
    private PartTrackingItemsService partTrackingItemsService;

    @Autowired
    private TrackValueService trackValueService;

    @Autowired
    private PartTrackingScannedUPNService scannedUpnService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public PartTrackingItems get(@PathVariable("id") Integer id) {
        return partTrackingItemsService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public PartTrackingItems save(@RequestBody PartTrackingItems imPartTrackingsItem) {
        PartTrackingItems imPartTrackingsItem1 = partTrackingItemsService.create(imPartTrackingsItem);
        return imPartTrackingsItem1;
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public List<PartTrackingItems> saveMultiple(@RequestBody List<PartTrackingItems> imPartTrackingsItems) {
        return partTrackingItemsService.saveMultiple(imPartTrackingsItems);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public PartTrackingItems update(@RequestBody PartTrackingItems imPartTrackingsItem) {
        PartTrackingItems imPartTrackingsItem1 = partTrackingItemsService.update(imPartTrackingsItem);
        return imPartTrackingsItem1;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        partTrackingItemsService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<PartTrackingItems> getAll() {
        List<PartTrackingItems> imPartTrackingsItems = partTrackingItemsService.getAll();
        return imPartTrackingsItems;
    }

    @RequestMapping(value = "/bomId/{item}", method = RequestMethod.GET)
    public List<PartTrackingItems> getByItem(@PathVariable("item") Integer item) {
        List<PartTrackingItems> imPartTrackingsItems = partTrackingItemsService.getByItem(item);
        return imPartTrackingsItems;
    }

    @RequestMapping(value = "/partTracking", method = RequestMethod.GET)
    public List<TrackValue> getPartTrackings() {
        return trackValueService.getAll();
    }

    @RequestMapping(value = "/partTracking", method = RequestMethod.POST)
    public TrackValue createPartTracking(@RequestBody TrackValue imPartTracking) {
        return trackValueService.create(imPartTracking);
    }

    @RequestMapping(value = "/partTracking/{id}", method = RequestMethod.PUT)
    public TrackValue updatePartTracking(@PathVariable("id") Integer id, @RequestBody TrackValue imPartTracking) {
        imPartTracking.setId(id);
        return trackValueService.update(imPartTracking);
    }

    @RequestMapping(value = "/partTracking/byCheckId/[{checkIds}]", method = RequestMethod.GET)
    public List<TrackValue> getByPartTrackingId(@PathVariable("checkIds") Integer[] partTrackings) {
        return trackValueService.getByPartTrackingId(Arrays.asList(partTrackings));
    }

    @RequestMapping(value = "/partTracking/report/{bomId}", method = RequestMethod.GET, produces = "text/html")
    public String partTrackingExportFile(@PathVariable("bomId") Integer bomId,
                                         HttpServletResponse response) {
        String fileName = trackValueService.generateReport(bomId, response);
        return fileName;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/file/{fileId}/download")
    public void downloadExportFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        trackValueService.downloadExportFile(fileId, response);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/scannedUpns/{trackStep}")
    public List<PartTrackingScannedUPN> getByPartTrackingStep(@PathVariable("trackStep") Integer trackStep) {
        return scannedUpnService.findByTrackStep(trackStep);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/scannedUpns")
    public List<PartTrackingScannedUPN> saveScannedPartTrackingUpns(@RequestBody List<PartTrackingScannedUPN> scannedUPNs) {

        return scannedUpnService.saveScannedPartTrackingUpns(scannedUPNs);
    }
}
