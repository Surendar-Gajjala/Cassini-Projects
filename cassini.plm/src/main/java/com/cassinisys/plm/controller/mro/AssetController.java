package com.cassinisys.plm.controller.mro;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.AssetCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetSparePart;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.dto.AssetResourcesDto;
import com.cassinisys.plm.model.mro.dto.WorkOrderDto;
import com.cassinisys.plm.service.mro.AssetService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
@RestController
@RequestMapping("/mro/assets")
@Api(tags = "PLM.MRO", description = "MRO Related")
public class AssetController extends BaseController {
    @Autowired
    private AssetService assetService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MROAsset create(@RequestBody MROAsset asset) {
        return assetService.create(asset);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MROAsset update(@PathVariable("id") Integer id,
                           @RequestBody MROAsset asset) {
        return assetService.update(asset);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        assetService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MROAsset get(@PathVariable("id") Integer id) {
        return assetService.get(id);
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getAssetCounts(@PathVariable("id") Integer id) {
        return assetService.getDetailsCount(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MROAsset> getAll() {
        return assetService.getAll();
    }

    @RequestMapping(value = "/create/attributes/multiple", method = RequestMethod.POST)
    public void saveObjectAttributes(@RequestBody List<MROObjectAttribute> attributes) {
        assetService.savemroObjectAttributes(attributes);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MROAsset> filterAssets(PageRequest pageRequest, AssetCriteria assetCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return assetService.getAllAssetsByPageable(pageable, assetCriteria);
    }

    @RequestMapping(value = "/resources/{type}/{typeId}", method = RequestMethod.GET)
    public AssetResourcesDto getAssetResourcesByType(@PathVariable("type") String type, @PathVariable("typeId") Integer id) {
        return assetService.getAssetResourcesByType(id, type);
    }

    @RequestMapping(value = "/resources/{resourceId}", method = RequestMethod.GET)
    public List<MROAsset> getAssetsByResource(@PathVariable("resourceId") Integer resourceId) {
        return assetService.getAssetsByResource(resourceId);
    }

    @RequestMapping(value = "/{assetid}/parts/multiple", method = RequestMethod.POST)
    public List<MROAssetSparePart> createWorkOrderSpareParts(@PathVariable("assetid") Integer assetId, @RequestBody List<MROAssetSparePart> asetParts) {
        return assetService.createAssetSpareParts(assetId, asetParts);
    }

    @RequestMapping(value = "/{assetid}/parts", method = RequestMethod.GET)
    public List<MROAssetSparePart> getWorkOrderSpareParts(@PathVariable("assetid") Integer assetId) {
        return assetService.getAssetSpareParts(assetId);
    }

    @RequestMapping(value = "/{assetid}/parts/{partid}", method = RequestMethod.DELETE)
    public void deleteWorkOrderSparePart(@PathVariable("assetid") Integer assetId, @PathVariable("partid") Integer partId) {
        assetService.deleteAssetSparePart(partId);
    }

    @RequestMapping(value = "/{assetid}/workorders", method = RequestMethod.GET)
    public List<WorkOrderDto> getWorkOrdersByAsset(@PathVariable("assetid") Integer assetId) {
        return assetService.getAllWorkOrdersByAsset(assetId);
    }

    @RequestMapping(value = "/delete/asset/{assetid}/meter/{meterid}", method = RequestMethod.DELETE)
    public void deleteAssetMeters(@PathVariable("assetid") Integer assetId, @PathVariable("meterid") Integer meterId) {
        assetService.deleteAssetMeter(assetId, meterId);
    }

    @RequestMapping(value = "/create/assetmeter", method = RequestMethod.POST)
    public MROAsset createAssetMeter(@RequestBody MROAsset asset) {
        return assetService.createAssetMeters(asset);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MROAsset> getAssetsByType(@PathVariable("typeId") Integer id,
                                             PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return assetService.getAssetsByType(id, pageable);
    }

}