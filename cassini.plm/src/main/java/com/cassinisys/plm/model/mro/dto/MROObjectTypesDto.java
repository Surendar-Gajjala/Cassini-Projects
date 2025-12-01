package com.cassinisys.plm.model.mro.dto;

import com.cassinisys.plm.model.mro.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 23-10-2020.
 */
@Data
public class MROObjectTypesDto {

    List<MROAssetType> assetTypes = new ArrayList<>();

    List<MROMeterType> meterTypes = new ArrayList<>();

    List<MROSparePartType> mroSparePartTypes = new ArrayList<>();

    List<MROWorkRequestType> workRequestTypes = new ArrayList<>();

    List<MROWorkOrderType> workOrderTypes = new ArrayList<>();

}
