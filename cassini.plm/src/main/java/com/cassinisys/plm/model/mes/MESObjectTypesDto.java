package com.cassinisys.plm.model.mes;

import com.cassinisys.plm.model.mro.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 23-10-2020.
 */
@Data
public class MESObjectTypesDto {

    List<MESPlantType> plantTypes = new ArrayList<>();

    List<MESWorkCenterType> workCenterTypes = new ArrayList<>();

    List<MESMachineType> machineTypes = new ArrayList<>();

    List<MESAssemblyLineType> assemblyLineTypes = new ArrayList<>();

    List<MESEquipmentType> equipmentTypes = new ArrayList<>();

    List<MESInstrumentType> instrumentTypes = new ArrayList<>();

    List<MESToolType> toolTypes = new ArrayList<>();

    List<MESJigsFixtureType> jigsFixtureTypes = new ArrayList<>();

    List<MESMaterialType> materialTypes = new ArrayList<>();

    List<MESManpowerType> manpowerTypes = new ArrayList<>();

    List<MESOperationType> operationTypes = new ArrayList<>();

    List<MESProductionOrderType> productionOrderTypes = new ArrayList<>();

    List<MROAssetType> assetTypes = new ArrayList<>();

    List<MROMeterType> meterTypes = new ArrayList<>();

    List<MROSparePartType> mroSparePartTypes = new ArrayList<>();

    List<MROWorkRequestType> workRequestTypes = new ArrayList<>();

    List<MROWorkOrderType> workOrderTypes = new ArrayList<>();
    
    List<MESMBOMType> mbomTypes = new ArrayList<>();

    List<MESBOPType> bopTypes = new ArrayList<>();

}
