define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('MESObjectTypeService', MESObjectTypeService);

        function MESObjectTypeService($q, httpFactory) {
            return {

                createMaterialType: createMaterialType,
                updateMaterialType: updateMaterialType,
                deleteMaterialType: deleteMaterialType,
                deleteObjectType: deleteObjectType,
                getMaterialType: getMaterialType,
                getAllMaterialTypes: getAllMaterialTypes,
                getMultipleMaterialTypes: getMultipleMaterialTypes,
                getMaterialTypeTree: getMaterialTypeTree,

                getMesObjectTypeByType: getMesObjectTypeByType,
                getObjectsCountByType: getObjectsCountByType,
                getMesObjectsTree: getMesObjectsTree,
                getAllObjectTypeTree: getAllObjectTypeTree,

                createToolType: createToolType,
                updateToolType: updateToolType,
                deleteToolType: deleteToolType,
                getToolType: getToolType,
                getAllToolTypes: getAllToolTypes,
                getMultipleToolTypes: getMultipleToolTypes,
                getToolTypeTree: getToolTypeTree,

                createJigsFixtureType: createJigsFixtureType,
                updateJigsFixtureType: updateJigsFixtureType,
                deleteJigsFixtureType: deleteJigsFixtureType,
                getJigsFixtureType: getJigsFixtureType,
                getAllJigsFixtureTypes: getAllJigsFixtureTypes,
                getMultipleJigsFixtureTypes: getMultipleJigsFixtureTypes,
                getJigsFixtureTypeTree: getJigsFixtureTypeTree,

                createOperationType: createOperationType,
                updateOperationType: updateOperationType,
                deleteOperationType: deleteOperationType,
                getOperationType: getOperationType,
                getAllOperationTypes: getAllOperationTypes,
                getMultipleOperationTypes: getMultipleOperationTypes,
                getOperationTypeTree: getOperationTypeTree,

                createPlantType: createPlantType,
                updatePlantType: updatePlantType,
                deletePlantType: deletePlantType,
                getPlantType: getPlantType,
                getAllPlantTypes: getAllPlantTypes,
                getMultiplePlantTypes: getMultiplePlantTypes,
                getPlantTypeTree: getPlantTypeTree,

                createWorkCenterType: createWorkCenterType,
                updateWorkCenterType: updateWorkCenterType,
                deleteWorkCenterType: deleteWorkCenterType,
                getWorkCenterType: getWorkCenterType,
                getAllWorkCenterTypes: getAllWorkCenterTypes,
                getMultipleWorkCenterTypes: getMultipleWorkCenterTypes,
                getWorkCenterTypeTree: getWorkCenterTypeTree,

                createMachineType: createMachineType,
                updateMachineType: updateMachineType,
                deleteMachineType: deleteMachineType,
                getMachineType: getMachineType,
                getAllMachineTypes: getAllMachineTypes,
                getMultipleMachineTypes: getMultipleMachineTypes,
                getMachineTypeTree: getMachineTypeTree,

                createMBOMType: createMBOMType,
                updateMBOMType: updateMBOMType,
                deleteMBOMType: deleteMBOMType,
                getMBOMType: getMBOMType,
                getAllMBOMTypes: getAllMBOMTypes,
                getMultipleMBOMTypes: getMultipleMBOMTypes,
                getMBOMTypeTree: getMBOMTypeTree,

                createBOPType: createBOPType,
                updateBOPType: updateBOPType,
                deleteBOPType: deleteBOPType,
                getBOPType: getBOPType,
                getAllBOPTypes: getAllBOPTypes,
                getMultipleBOPTypes: getMultipleBOPTypes,
                getBOPTypeTree: getBOPTypeTree,
                getMesObjectTypeTree: getMesObjectTypeTree,

                createProductionOrderType: createProductionOrderType,
                updateProductionOrderType: updateProductionOrderType,
                deleteProductionOrderType: deleteProductionOrderType,
                getProductionOrderType: getProductionOrderType,
                getAllProductionOrderTypes: getAllProductionOrderTypes,
                getMultipleProductionOrderTypes: getMultipleProductionOrderTypes,
                getProductionOrderTypeTree: getProductionOrderTypeTree,

                createManpowerType: createManpowerType,
                updateManpowerType: updateManpowerType,
                deleteManpowerType: deleteManpowerType,
                getManpowerType: getManpowerType,
                getAllManpowerTypes: getAllManpowerTypes,
                getMultipleManpowerTypes: getMultipleManpowerTypes,
                getManpowerTypeTree: getManpowerTypeTree,

                createEquipmentType: createEquipmentType,
                updateEquipmentType: updateEquipmentType,
                deleteEquipmentType: deleteEquipmentType,
                getEquipmentType: getEquipmentType,
                getAllEquipmentTypes: getAllEquipmentTypes,
                getMultipleEquipmentTypes: getMultipleEquipmentTypes,
                getEquipmentTypeTree: getEquipmentTypeTree,

                createInstrumentType: createInstrumentType,
                updateInstrumentType: updateInstrumentType,
                deleteInstrumentType: deleteInstrumentType,
                getInstrumentType: getInstrumentType,
                getAllInstrumentTypes: getAllInstrumentTypes,
                getMultipleInstrumentTypes: getMultipleInstrumentTypes,
                getInstrumentTypeTree: getInstrumentTypeTree,
                getMesObjectAttributesWithHierarchy: getMesObjectAttributesWithHierarchy,
                createMESObjectAttribute: createMESObjectAttribute,
                updateMESObjectAttribute: updateMESObjectAttribute,
                getUsedMesObjectAttributesValues: getUsedMesObjectAttributesValues,
                getUsedMroObjectAttributesValues: getUsedMroObjectAttributesValues,

                getMaterialByType: getMaterialByType,
                getJigsFixturesByType: getJigsFixturesByType,
                getPlantsByType: getPlantsByType,
                getManpowerByType: getManpowerByType,
                getMachinesByType: getMachinesByType,
                getWorkCentersByType: getWorkCentersByType,
                getEquipmentsByType: getEquipmentsByType,
                getInstrumentsByType: getInstrumentsByType,
                getMBOMsByType: getMBOMsByType,
                getBOPsByType: getBOPsByType,
                getProductionOrdersByType: getProductionOrdersByType,
                getToolsByType: getToolsByType,
                getOperationsByType: getOperationsByType,
                getSparePartsByType: getSparePartsByType,
                getAssetsByType: getAssetsByType,
                getMetersByType: getMetersByType,
                getWorkRequestsByType: getWorkRequestsByType,
                getWorkOrdersByType: getWorkOrdersByType,
                getAssemblyLinesByType: getAssemblyLinesByType,

                createSparePartType: createSparePartType,
                updateSparePartType: updateSparePartType,
                deleteSparePartType: deleteSparePartType,
                getSparePartType: getSparePartType,
                getAllSparePartTypes: getAllSparePartTypes,
                getMultipleSparePartTypes: getMultipleSparePartTypes,
                getSparePartTypeTree: getSparePartTypeTree,

                createAssetType: createAssetType,
                updateAssetType: updateAssetType,
                deleteAssetType: deleteAssetType,
                getAssetType: getAssetType,
                getAllAssetTypes: getAllAssetTypes,
                getMultipleAssetTypes: getMultipleAssetTypes,
                getAssetTypeTree: getAssetTypeTree,

                createMeterType: createMeterType,
                updateMeterType: updateMeterType,
                deleteMeterType: deleteMeterType,
                getMeterType: getMeterType,
                getAllMeterTypes: getAllMeterTypes,
                getMultipleMeterTypes: getMultipleMeterTypes,
                getMeterTypeTree: getMeterTypeTree,

                createWorkRequestType: createWorkRequestType,
                updateWorkRequestType: updateWorkRequestType,
                deleteWorkRequestType: deleteWorkRequestType,
                getWorkRequestType: getWorkRequestType,
                getAllWorkRequestTypes: getAllWorkRequestTypes,
                getMultipleWorkRequestTypes: getMultipleWorkRequestTypes,
                getWorkRequestTypeTree: getWorkRequestTypeTree,

                createWorkOrderType: createWorkOrderType,
                updateWorkOrderType: updateWorkOrderType,
                deleteWorkOrderType: deleteWorkOrderType,
                getWorkOrderType: getWorkOrderType,
                getAllWorkOrderTypes: getAllWorkOrderTypes,
                getMultipleWorkOrderTypes: getMultipleWorkOrderTypes,
                getWorkOrderTypeTree: getWorkOrderTypeTree,

                createSupplierType: createSupplierType,
                updateSupplierType: updateSupplierType,
                getSupplierTypeTree: getSupplierTypeTree,
                deleteSupplierType: deleteSupplierType,
                getSuppliersByType: getSuppliersByType,

                getAllMROObjectTypeTree: getAllMROObjectTypeTree,
                getMROType: getMROType,

                createMROObjectAttribute: createMROObjectAttribute,
                updateMROObjectAttribute: updateMROObjectAttribute,
                getRepairWorkOrderTypeTree: getRepairWorkOrderTypeTree,

                createAssemblyLineType: createAssemblyLineType,
                updateAssemblyLineType: updateAssemblyLineType,
                deleteAssemblyLineType: deleteAssemblyLineType,
                getAssemblyLineType: getAssemblyLineType,
                getAllAssemblyLineTypes: getAllAssemblyLineTypes,
                getMultipleAssemblyLineTypes: getMultipleAssemblyLineTypes,
                getAssemblyLineTypeTree: getAssemblyLineTypeTree,

                getMESObjects: getMESObjects,
                getMROObjects: getMROObjects,
                getMESObject: getMESObject,
                getMROObject: getMROObject,
                getMROObjectsTypeTree: getMROObjectsTypeTree,
                getMBOMTypeLifecycles: getMBOMTypeLifecycles

            };

            function getMesObjectTypeByType(id, type) {
                var url = "api/mes/objecttypes/" + id + "/" + type;
                return httpFactory.get(url);
            }

            function getObjectsCountByType(id, type) {
                var url = "api/mes/objecttypes/" + id + "/" + type + "/count";
                return httpFactory.get(url);
            }


            /* --------------------------------MaterialType-------------------------------------------*/

            function createMaterialType(planType) {
                var url = "api/mes/objecttypes/materialtype";
                return httpFactory.post(url, planType);
            }

            function updateMaterialType(planType) {
                var url = "api/mes/objecttypes/materialtype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteMaterialType(id) {
                var url = "api/mes/objecttypes/materialtype/" + id;
                return httpFactory.delete(url);
            }

            function deleteObjectType(id) {
                var url = "api/mes/objecttypes/" + id;
                return httpFactory.delete(url);
            }

            function getMaterialType(id) {
                var url = "api/mes/objecttypes/materialtype/" + id;
                return httpFactory.get(url);
            }

            function getAllMaterialTypes() {
                var url = "api/mes/objecttypes/materialtype";
                return httpFactory.get(url);
            }

            function getMultipleMaterialTypes(ids) {
                var url = "api/mes/objecttypes/materialtype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getMaterialTypeTree() {
                var url = "api/mes/objecttypes/materialtype/tree";
                return httpFactory.get(url);
            }


            /*--------------------------------ToolType--------------------------------------------*/

            function createToolType(planType) {
                var url = "api/mes/objecttypes/tooltype";
                return httpFactory.post(url, planType);
            }

            function updateToolType(planType) {
                var url = "api/mes/objecttypes/tooltype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteToolType(id) {
                var url = "api/mes/objecttypes/tooltype/" + id;
                return httpFactory.delete(url);
            }

            function getToolType(id) {
                var url = "api/mes/objecttypes/tooltype/" + id;
                return httpFactory.get(url);
            }

            function getAllToolTypes() {
                var url = "api/mes/objecttypes/tooltype";
                return httpFactory.get(url);
            }

            function getMultipleToolTypes(ids) {
                var url = "api/mes/objecttypes/tooltype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getToolTypeTree() {
                var url = "api/mes/objecttypes/tooltype/tree";
                return httpFactory.get(url);
            }

            /*    ---------------------------------JigsFixtureType-----------------------------------------*/

            function createJigsFixtureType(planType) {
                var url = "api/mes/objecttypes/jigsfixtype";
                return httpFactory.post(url, planType);
            }

            function updateJigsFixtureType(planType) {
                var url = "api/mes/objecttypes/jigsfixtype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteJigsFixtureType(id) {
                var url = "api/mes/objecttypes/jigsfixtype/" + id;
                return httpFactory.delete(url);
            }

            function getJigsFixtureType(id) {
                var url = "api/mes/objecttypes/jigsfixtype/" + id;
                return httpFactory.get(url);
            }

            function getAllJigsFixtureTypes() {
                var url = "api/mes/objecttypes/jigsfixtype";
                return httpFactory.get(url);
            }

            function getMultipleJigsFixtureTypes(ids) {
                var url = "api/mes/objecttypes/jigsfixtype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getJigsFixtureTypeTree() {
                var url = "api/mes/objecttypes/jigsfixtype/tree";
                return httpFactory.get(url);
            }

            /*   ----------------------------------OperationType---------------------------------------*/

            function createOperationType(operationtype) {
                var url = "api/mes/objecttypes/operationtype";
                return httpFactory.post(url, operationtype);
            }

            function updateOperationType(operationtype) {
                var url = "api/mes/objecttypes/operationtype/" + operationtype.id;
                return httpFactory.put(url, operationtype);
            }

            function deleteOperationType(id) {
                var url = "api/mes/objecttypes/operationtype/" + id;
                return httpFactory.delete(url);
            }

            function getOperationType(id) {
                var url = "api/mes/objecttypes/operationtype/" + id;
                return httpFactory.get(url);
            }

            function getAllOperationTypes() {
                var url = "api/mes/objecttypes/operationtype";
                return httpFactory.get(url);
            }

            function getMultipleOperationTypes(ids) {
                var url = "api/mes/objecttypes/operationtype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getOperationTypeTree() {
                var url = "api/mes/objecttypes/operationtype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------PlantType----------------------------------*/

            function createPlantType(planttype) {
                var url = "api/mes/objecttypes/planttype";
                return httpFactory.post(url, planttype);
            }

            function updatePlantType(planttype) {
                var url = "api/mes/objecttypes/planttype/" + planttype.id;
                return httpFactory.put(url, planttype);
            }

            function deletePlantType(id) {
                var url = "api/mes/objecttypes/planttype/" + id;
                return httpFactory.delete(url);
            }

            function getPlantType(id) {
                var url = "api/mes/objecttypes/planttype/" + id;
                return httpFactory.get(url);
            }

            function getAllPlantTypes() {
                var url = "api/mes/objecttypes/planttype";
                return httpFactory.get(url);
            }

            function getMultiplePlantTypes(ids) {
                var url = "api/mes/objecttypes/planttype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getMesObjectsTree() {
                var url = "api/mes/objecttypes/tree";
                return httpFactory.get(url);
            }

            function getAllObjectTypeTree() {
                var url = "api/mes/objecttypes/all/tree";
                return httpFactory.get(url);
            }

            function getPlantTypeTree() {
                var url = "api/mes/objecttypes/planttype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------WorkCenterType----------------------------------*/

            function createWorkCenterType(workcentertype) {
                var url = "api/mes/objecttypes/workcentertype";
                return httpFactory.post(url, workcentertype);
            }

            function updateWorkCenterType(workcentertype) {
                var url = "api/mes/objecttypes//workcentertype/" + workcentertype.id;
                return httpFactory.put(url, workcentertype);
            }

            function deleteWorkCenterType(id) {
                var url = "api/mes/objecttypes/workcentertype/" + id;
                return httpFactory.delete(url);
            }

            function getWorkCenterType(id) {
                var url = "api/mes/objecttypes/workcentertype/" + id;
                return httpFactory.get(url);
            }

            function getAllWorkCenterTypes() {
                var url = "api/mes/objecttypes/workcentertype";
                return httpFactory.get(url);
            }

            function getMultipleWorkCenterTypes(ids) {
                var url = "api/mes/objecttypes/workcentertype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getWorkCenterTypeTree() {
                var url = "api/mes/objecttypes/workcentertype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------MachineType----------------------------------*/

            function createMachineType(machinetype) {
                var url = "api/mes/objecttypes/machinetype";
                return httpFactory.post(url, machinetype);
            }

            function updateMachineType(machinetype) {
                var url = "api/mes/objecttypes/machinetype/" + machinetype.id;
                return httpFactory.put(url, machinetype);
            }

            function deleteMachineType(id) {
                var url = "api/mes/objecttypes/machinetype/" + id;
                return httpFactory.delete(url);
            }

            function getMachineType(id) {
                var url = "api/mes/objecttypes/machinetype/" + id;
                return httpFactory.get(url);
            }

            function getAllMachineTypes() {
                var url = "api/mes/objecttypes/machinetype";
                return httpFactory.get(url);
            }

            function getMultipleMachineTypes(ids) {
                var url = "api/mes/objecttypes/machinetype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getMachineTypeTree() {
                var url = "api/mes/objecttypes/machinetype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------MBOM----------------------------------*/


            function createMBOMType(mbomtype) {
                var url = "api/mes/objecttypes/mbomtype";
                return httpFactory.post(url, mbomtype);
            }

            function updateMBOMType(mbomtype) {
                var url = "api/mes/objecttypes/mbomtype/" + mbomtype.id;
                return httpFactory.put(url, mbomtype);
            }

            function deleteMBOMType(id) {
                var url = "api/mes/objecttypes/mbomtype/" + id;
                return httpFactory.delete(url);
            }

            function getMBOMType(id) {
                var url = "api/mes/objecttypes/mbomtype/" + id;
                return httpFactory.get(url);
            }

            function getAllMBOMTypes() {
                var url = "api/mes/objecttypes/mbomtype";
                return httpFactory.get(url);
            }

            function getMultipleMBOMTypes(ids) {
                var url = "api/mes/objecttypes/mbomtype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getMBOMTypeTree() {
                var url = "api/mes/objecttypes/mbomtype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------BOP----------------------------------*/


            function createBOPType(boptype) {
                var url = "api/mes/objecttypes/boptype";
                return httpFactory.post(url, boptype);
            }

            function updateBOPType(boptype) {
                var url = "api/mes/objecttypes/boptype/" + boptype.id;
                return httpFactory.put(url, boptype);
            }

            function deleteBOPType(id) {
                var url = "api/mes/objecttypes/boptype/" + id;
                return httpFactory.delete(url);
            }

            function getBOPType(id) {
                var url = "api/mes/objecttypes/boptype/" + id;
                return httpFactory.get(url);
            }

            function getAllBOPTypes() {
                var url = "api/mes/objecttypes/boptype";
                return httpFactory.get(url);
            }

            function getMultipleBOPTypes(ids) {
                var url = "api/mes/objecttypes/bop  type/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getBOPTypeTree() {
                var url = "api/mes/objecttypes/boptype/tree";
                return httpFactory.get(url);
            }

            function getMesObjectTypeTree() {
                var url = "api/mes/objecttypes/tree";
                return httpFactory.get(url);
            }


            /*-------------------------------ProductionOrder----------------------------------*/


            function createProductionOrderType(planttype) {
                var url = "api/mes/objecttypes/productiontype";
                return httpFactory.post(url, planttype);
            }

            function updateProductionOrderType(planttype) {
                var url = "api/mes/objecttypes/productiontype/" + planttype.id;
                return httpFactory.put(url, planttype);
            }

            function deleteProductionOrderType(id) {
                var url = "api/mes/objecttypes/productiontype/" + id;
                return httpFactory.delete(url);
            }

            function getProductionOrderType(id) {
                var url = "api/mes/objecttypes/productiontype/" + id;
                return httpFactory.get(url);
            }

            function getAllProductionOrderTypes() {
                var url = "api/mes/objecttypes/productiontype";
                return httpFactory.get(url);
            }

            function getMultipleProductionOrderTypes(ids) {
                var url = "api/mes/objecttypes/productiontype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getProductionOrderTypeTree() {
                var url = "api/mes/objecttypes/productiontype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------ManPowerType----------------------------------*/

            function createManpowerType(manpower) {
                var url = "api/mes/objecttypes/manpowertype";
                return httpFactory.post(url, manpower);
            }

            function updateManpowerType(manpower) {
                var url = "api/mes/objecttypes/manpowertype/" + manpower.id;
                return httpFactory.put(url, manpower);
            }

            function deleteManpowerType(id) {
                var url = "api/mes/objecttypes/manpowertype/" + id;
                return httpFactory.delete(url);
            }

            function getManpowerType(id) {
                var url = "api/mes/objecttypes/manpowertype/" + id;
                return httpFactory.get(url);
            }

            function getAllManpowerTypes() {
                var url = "api/mes/objecttypes/manpowertype";
                return httpFactory.get(url);
            }

            function getMultipleManpowerTypes(ids) {
                var url = "api/mes/objecttypes/manpowertype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getManpowerTypeTree() {
                var url = "api/mes/objecttypes/manpowertype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------SparePartType----------------------------------*/

            function createSparePartType(manpower) {
                var url = "api/mro/objecttypes/spareparttype";
                return httpFactory.post(url, manpower);
            }

            function updateSparePartType(manpower) {
                var url = "api/mro/objecttypes/spareparttype/" + manpower.id;
                return httpFactory.put(url, manpower);
            }

            function deleteSparePartType(id) {
                var url = "api/mro/objecttypes/spareparttype/" + id;
                return httpFactory.delete(url);
            }

            function getSparePartType(id) {
                var url = "api/mro/objecttypes/spareparttype/" + id;
                return httpFactory.get(url);
            }

            function getAllSparePartTypes() {
                var url = "api/mro/objecttypes/spareparttype";
                return httpFactory.get(url);
            }

            function getMultipleSparePartTypes(ids) {
                var url = "api/mro/objecttypes/spareparttype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getSparePartTypeTree() {
                var url = "api/mro/objecttypes/spareparttype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------EquipmentType----------------------------------*/

            function createEquipmentType(equipmentType) {
                var url = "api/mes/objecttypes/equipmenttype";
                return httpFactory.post(url, equipmentType);
            }

            function updateEquipmentType(equipmentType) {
                var url = "api/mes/objecttypes/equipmenttype/" + equipmentType.id;
                return httpFactory.put(url, equipmentType);
            }

            function deleteEquipmentType(id) {
                var url = "api/mes/objecttypes/equipmenttype/" + id;
                return httpFactory.delete(url);
            }

            function getEquipmentType(id) {
                var url = "api/mes/objecttypes/equipmenttype/" + id;
                return httpFactory.get(url);
            }

            function getAllEquipmentTypes() {
                var url = "api/mes/objecttypes/equipmenttype";
                return httpFactory.get(url);
            }

            function getMultipleEquipmentTypes(ids) {
                var url = "api/mes/objecttypes/equipmenttype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getEquipmentTypeTree() {
                var url = "api/mes/objecttypes/equipmenttype/tree";
                return httpFactory.get(url);
            }


            /*-------------------------------InstrumentType----------------------------------*/

            function createInstrumentType(instrumentType) {
                var url = "api/mes/objecttypes/instrumenttype";
                return httpFactory.post(url, instrumentType);
            }

            function updateInstrumentType(instrumentType) {
                var url = "api/mes/objecttypes/instrumenttype/" + instrumentType.id;
                return httpFactory.put(url, instrumentType);
            }

            function deleteInstrumentType(id) {
                var url = "api/mes/objecttypes/instrumenttype/" + id;
                return httpFactory.delete(url);
            }

            function getInstrumentType(id) {
                var url = "api/mes/objecttypes/instrumenttype/" + id;
                return httpFactory.get(url);
            }

            function getAllInstrumentTypes() {
                var url = "api/mes/objecttypes/instrumenttype";
                return httpFactory.get(url);
            }

            function getMultipleInstrumentTypes(ids) {
                var url = "api/mes/objecttypes/instrumenttype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getInstrumentTypeTree() {
                var url = "api/mes/objecttypes/instrumenttype/tree";
                return httpFactory.get(url);
            }

            function createMESObjectAttribute(objectId, attribute) {
                var url = "api/mes/objecttypes/" + objectId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateMESObjectAttribute(objectId, attribute) {
                var url = "api/mes/objecttypes/" + objectId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function getUsedMesObjectAttributesValues(attributeId) {
                var url = "api/mes/objecttypes/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function getMesObjectAttributesWithHierarchy(typeId) {
                var url = "api/mes/objecttypes/type/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getMaterialByType(typeId, pageable) {
                var url = "api/mes/materials/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getJigsFixturesByType(typeId, pageable) {
                var url = "api/mes/jigsfixs/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getPlantsByType(typeId, pageable) {
                var url = "api/mes/plants/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAssemblyLinesByType(typeId, pageable) {
                var url = "api/mes/assemblylines/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getManpowerByType(typeId, pageable) {
                var url = "api/mes/manpowers/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMachinesByType(typeId, pageable) {
                var url = "api/mes/machines/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getWorkCentersByType(typeId, pageable) {
                var url = "api/mes/materials/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getEquipmentsByType(typeId, pageable) {
                var url = "api/mes/equipments/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getInstrumentsByType(typeId, pageable) {
                var url = "api/mes/instruments/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMBOMsByType(typeId, pageable) {
                var url = "api/mes/mboms/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getBOPsByType(typeId, pageable) {
                var url = "api/mes/bops/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getProductionOrdersByType(typeId, pageable) {
                var url = "api/mes/productionorders/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getToolsByType(typeId, pageable) {
                var url = "api/mes/tools/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSparePartsByType(typeId, pageable) {
                var url = "api/mro/spareparts/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAssetsByType(typeId, pageable) {
                var url = "api/mro/assets/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMetersByType(typeId, pageable) {
                var url = "api/mro/meters/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getWorkRequestsByType(typeId, pageable) {
                var url = "api/mro/workrequests/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getWorkOrdersByType(typeId, pageable) {
                var url = "api/mro/workorders/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getOperationsByType(typeId, pageable) {
                var url = "api/mes/operations/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSuppliersByType(typeId, pageable) {
                var url = "api/mfr/suppliers/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            /*-------------------------------MroObjectType----------------------------------*/

            function getUsedMroObjectAttributesValues(attributeId) {
                var url = "api/mro/objecttypes/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            /*-------------------------------AssetType----------------------------------*/

            function createAssetType(assetType) {
                var url = "api/mro/objecttypes/assettype";
                return httpFactory.post(url, assetType);
            }

            function updateAssetType(assetType) {
                var url = "api/mro/objecttypes/assettype/" + assetType.id;
                return httpFactory.put(url, assetType);
            }

            function deleteAssetType(id) {
                var url = "api/mro/objecttypes/assettype/" + id;
                return httpFactory.delete(url);
            }

            function getAssetType(id) {
                var url = "api/mro/objecttypes/assettype/" + id;
                return httpFactory.get(url);
            }

            function getAllAssetTypes() {
                var url = "api/mro/objecttypes/workrequesttype";
                return httpFactory.get(url);
            }

            function getMultipleAssetTypes(ids) {
                var url = "api/mro/objecttypes/assettype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getAssetTypeTree() {
                var url = "api/mro/objecttypes/assettype/tree";
                return httpFactory.get(url);
            }


            /*-------------------------------MeterType----------------------------------*/

            function createMeterType(meter) {
                var url = "api/mro/objecttypes/metertype";
                return httpFactory.post(url, meter);
            }

            function updateMeterType(meter) {
                var url = "api/mro/objecttypes/metertype/" + meter.id;
                return httpFactory.put(url, meter);
            }

            function deleteMeterType(id) {
                var url = "api/mro/objecttypes/metertype/" + id;
                return httpFactory.delete(url);
            }

            function getMeterType(id) {
                var url = "api/mro/objecttypes/metertype/" + id;
                return httpFactory.get(url);
            }

            function getAllMeterTypes() {
                var url = "api/mro/objecttypes/metertype";
                return httpFactory.get(url);
            }

            function getMultipleMeterTypes(ids) {
                var url = "api/mro/objecttypes/metertype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getMeterTypeTree() {
                var url = "api/mro/objecttypes/metertype/tree";
                return httpFactory.get(url);
            }


            /*-------------------------------WorkRequestType----------------------------------*/

            function createWorkRequestType(manpower) {
                var url = "api/mro/objecttypes/workrequesttype";
                return httpFactory.post(url, manpower);
            }

            function updateWorkRequestType(manpower) {
                var url = "api/mro/objecttypes/workrequesttype/" + manpower.id;
                return httpFactory.put(url, manpower);
            }

            function deleteWorkRequestType(id) {
                var url = "api/mro/objecttypes/workrequesttype/" + id;
                return httpFactory.delete(url);
            }

            function getWorkRequestType(id) {
                var url = "api/mro/objecttypes/workrequesttype/" + id;
                return httpFactory.get(url);
            }

            function getAllWorkRequestTypes() {
                var url = "api/mro/objecttypes/workrequesttype";
                return httpFactory.get(url);
            }

            function getMultipleWorkRequestTypes(ids) {
                var url = "api/mro/objecttypes/workrequesttype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getWorkRequestTypeTree() {
                var url = "api/mro/objecttypes/workrequesttype/tree";
                return httpFactory.get(url);
            }


            /*-------------------------------WorkOrderType----------------------------------*/

            function createWorkOrderType(manpower) {
                var url = "api/mro/objecttypes/workordertype";
                return httpFactory.post(url, manpower);
            }

            function updateWorkOrderType(manpower) {
                var url = "api/mro/objecttypes/workordertype/" + manpower.id;
                return httpFactory.put(url, manpower);
            }

            function deleteWorkOrderType(id) {
                var url = "api/mro/objecttypes/workordertype/" + id;
                return httpFactory.delete(url);
            }

            function getWorkOrderType(id) {
                var url = "api/mro/objecttypes/workordertype/" + id;
                return httpFactory.get(url);
            }

            function getAllWorkOrderTypes() {
                var url = "api/mro/objecttypes/workordertype";
                return httpFactory.get(url);
            }

            function getMultipleWorkOrderTypes(ids) {
                var url = "api/mro/objecttypes/workordertype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getWorkOrderTypeTree() {
                var url = "api/mro/objecttypes/workordertype/tree";
                return httpFactory.get(url);
            }

            function getRepairWorkOrderTypeTree() {
                var url = "api/mro/objecttypes/repairworkordertype/tree";
                return httpFactory.get(url);
            }

            function createMROObjectAttribute(objectId, attribute) {
                var url = "api/mro/objecttypes/" + objectId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateMROObjectAttribute(objectId, attribute) {
                var url = "api/mro/objecttypes/" + objectId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function createSupplierType(supplier) {
                var url = "api/plm/suppliertypes";
                return httpFactory.post(url, supplier);
            }

            function updateSupplierType(supplier) {
                var url = "api/plm/suppliertypes/" + supplier.id;
                return httpFactory.put(url, supplier);
            }

            function getSupplierTypeTree() {
                var url = "api/plm/suppliertypes/tree";
                return httpFactory.get(url);
            }

            function deleteSupplierType(id) {
                var url = "api/plm/suppliertypes/" + id;
                return httpFactory.delete(url);
            }

            function getAllMROObjectTypeTree() {
                var url = "api/mro/objecttypes/all/tree";
                return httpFactory.get(url);
            }

            function getMROType(id) {
                var url = "api/mro/objecttypes/" + id;
                return httpFactory.get(url);
            }

            /*-------------------------------PlantType----------------------------------*/

            function createAssemblyLineType(assemblyLineType) {
                var url = "api/mes/objecttypes/assemblylinetype";
                return httpFactory.post(url, assemblyLineType);
            }

            function updateAssemblyLineType(assemblyLineType) {
                var url = "api/mes/objecttypes/assemblylinetype/" + assemblyLineType.id;
                return httpFactory.put(url, assemblyLineType);
            }

            function deleteAssemblyLineType(id) {
                var url = "api/mes/objecttypes/assemblylinetype/" + id;
                return httpFactory.delete(url);
            }

            function getAssemblyLineType(id) {
                var url = "api/mes/objecttypes/assemblylinetype/" + id;
                return httpFactory.get(url);
            }

            function getAllAssemblyLineTypes() {
                var url = "api/mes/objecttypes/assemblylinetype";
                return httpFactory.get(url);
            }

            function getMultipleAssemblyLineTypes(ids) {
                var url = "api/mes/objecttypes/assemblylinetype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getAssemblyLineTypeTree() {
                var url = "api/mes/objecttypes/assemblylinetype/tree";
                return httpFactory.get(url);
            }

            function getMBOMTypeLifecycles() {
                var url = "api/mes/objecttypes/mbomtype/lifecycles";
                return httpFactory.get(url);
            }

            function getMESObjects(pageable, filters) {
                var url = "api/mes/objecttypes/all/objects?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&searchQuery={1}".
                    format(filters.type, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMROObjects(pageable, filters) {
                var url = "api/mro/objecttypes/all/objects?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&searchQuery={1}".
                    format(filters.type, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMESObject(id) {
                var url = "api/mes/objecttypes/object/" + id;
                return httpFactory.get(url);
            }

            function getMROObject(id) {
                var url = "api/mro/objecttypes/object/" + id;
                return httpFactory.get(url);
            }

            function getMROObjectsTypeTree() {
                var url = "api/mro/objecttypes/all/type/tree";
                return httpFactory.get(url);
            }

        }
    }
)
;