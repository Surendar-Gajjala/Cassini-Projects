define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('QualityTypeService', QualityTypeService);

        function QualityTypeService($q, httpFactory) {
            return {
                getQualityTypeByType: getQualityTypeByType,
                getObjectsByType: getObjectsByType,
                getQualityTypeTree: getQualityTypeTree,

                createInspectionPlanType: createInspectionPlanType,
                updateInspectionPlanType: updateInspectionPlanType,
                deleteInspectionPlanType: deleteInspectionPlanType,
                getInspectionPlanType: getInspectionPlanType,
                getAllInspectionPlanTypes: getAllInspectionPlanTypes,
                getMultipleInspectionPlanTypes: getMultipleInspectionPlanTypes,

                createPrType: createPrType,
                updatePrType: updatePrType,
                deletePrType: deletePrType,
                getPrType: getPrType,
                getAllPrTypes: getAllPrTypes,
                getMultiplePrTypes: getMultiplePrTypes,

                createNcrType: createNcrType,
                updateNcrType: updateNcrType,
                deleteNcrType: deleteNcrType,
                getNcrType: getNcrType,
                getAllNcrTypes: getAllNcrTypes,
                getMultipleNcrTypes: getMultipleNcrTypes,

                createQcrType: createQcrType,
                updateQcrType: updateQcrType,
                deleteQcrType: deleteQcrType,
                getQcrType: getQcrType,
                getAllQcrTypes: getAllQcrTypes,
                getMultipleQcrTypes: getMultipleQcrTypes,

                getInspectionPlanTypeTree: getInspectionPlanTypeTree,
                getPrTypeTree: getPrTypeTree,
                getNcrTypeTree: getNcrTypeTree,
                getQcrTypeTree: getQcrTypeTree,
                getQtType: getQtType,
                getQualityHistoryByDateWise: getQualityHistoryByDateWise,
                getUsedAttributeValues: getUsedAttributeValues,
                createQualityObject: createQualityObject,
                getQualityTypeWorkflows: getQualityTypeWorkflows,
                updateQualityAttributeSeq: updateQualityAttributeSeq,
                getAllQualityTypeAttributes: getAllQualityTypeAttributes,
                getQualityTypeReferences: getQualityTypeReferences,
                getMultipleQualityTypes: getMultipleQualityTypes,

                createProductInspectionPlanType: createProductInspectionPlanType,
                createMaterialInspectionPlanType: createMaterialInspectionPlanType,
                updateProductInspectionPlanType: updateProductInspectionPlanType,
                updateMaterialInspectionPlanType: updateMaterialInspectionPlanType,
                getProductInspectionPlanType: getProductInspectionPlanType,
                getMaterialInspectionPlanType: getMaterialInspectionPlanType,
                getMultipleProductInspectionPlanTypes: getMultipleProductInspectionPlanTypes,
                getMultipleMaterialInspectionPlanTypes: getMultipleMaterialInspectionPlanTypes,
                getProductInspectionPlanTypeTree: getProductInspectionPlanTypeTree,
                getMaterialInspectionPlanTypeTree: getMaterialInspectionPlanTypeTree,
                getPersonByGroupNameAndPermission: getPersonByGroupNameAndPermission,
                getQualityObjects: getQualityObjects,


                updatePpapType: updatePpapType,
                createPpapType: createPpapType,
                deletePpapType: deletePpapType,
                getPpapType: getPpapType,
                getAllPpapTypes: getAllPpapTypes,
                getMultiplePpapTypes: getMultiplePpapTypes,
                getPpapTypeTree: getPpapTypeTree,

                createSupplierAuditType: createSupplierAuditType,
                updateSupplierAuditType: updateSupplierAuditType,
                deleteSupplierAuditType: deleteSupplierAuditType,
                getSupplierAuditType: getSupplierAuditType,
                getAllSupplierAuditTypes: getAllSupplierAuditTypes,
                getMultipleSupplierAuditTypes: getMultipleSupplierAuditTypes,
                getSupplierAuditTypeTree : getSupplierAuditTypeTree
            };

            function getQualityTypeByType(id, type) {
                var url = "api/pqm/qualitytypes/" + id + "/" + type;
                return httpFactory.get(url);
            }

            function getObjectsByType(id, type) {
                var url = "api/pqm/qualitytypes/" + id + "/" + type + "/count";
                return httpFactory.get(url);
            }

            function createInspectionPlanType(planType) {
                var url = "api/pqm/qualitytypes/inspectionplantypes";
                return httpFactory.post(url, planType);
            }

            function createProductInspectionPlanType(planType) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/product";
                return httpFactory.post(url, planType);
            }

            function createMaterialInspectionPlanType(planType) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/material";
                return httpFactory.post(url, planType);
            }

            function updateInspectionPlanType(planType) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function updateProductInspectionPlanType(planType) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/product/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function updateMaterialInspectionPlanType(planType) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/material/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteInspectionPlanType(id) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/" + id;
                return httpFactory.delete(url);
            }

            function getInspectionPlanType(id) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/" + id;
                return httpFactory.get(url);
            }

            function getProductInspectionPlanType(id) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/product/" + id;
                return httpFactory.get(url);
            }

            function getMaterialInspectionPlanType(id) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/material/" + id;
                return httpFactory.get(url);
            }

            function getAllInspectionPlanTypes() {
                var url = "api/pqm/qualitytypes/inspectionplantypes";
                return httpFactory.get(url);
            }

            function getMultipleInspectionPlanTypes(ids) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getMultipleProductInspectionPlanTypes(ids) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/product/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getMultipleMaterialInspectionPlanTypes(ids) {
                var url = "api/pqm/qualitytypes/inspectionplantypes/material/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getMultipleQualityTypes(ids) {
                var url = "api/pqm/qualitytypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function createPrType(planType) {
                var url = "api/pqm/qualitytypes/prtypes";
                return httpFactory.post(url, planType);
            }

            function updatePrType(planType) {
                var url = "api/pqm/qualitytypes/prtypes/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deletePrType(id) {
                var url = "api/pqm/qualitytypes/prtypes/" + id;
                return httpFactory.delete(url);
            }

            function getPrType(id) {
                var url = "api/pqm/qualitytypes/prtypes/" + id;
                return httpFactory.get(url);
            }

            function getAllPrTypes() {
                var url = "api/pqm/qualitytypes/prtypes";
                return httpFactory.get(url);
            }

            function getMultiplePrTypes(ids) {
                var url = "api/pqm/qualitytypes/prtypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function createNcrType(planType) {
                var url = "api/pqm/qualitytypes/ncrtypes";
                return httpFactory.post(url, planType);
            }

            function updateNcrType(planType) {
                var url = "api/pqm/qualitytypes/ncrtypes/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteNcrType(id) {
                var url = "api/pqm/qualitytypes/ncrtypes/" + id;
                return httpFactory.delete(url);
            }

            function getNcrType(id) {
                var url = "api/pqm/qualitytypes/ncrtypes/" + id;
                return httpFactory.get(url);
            }

            function getAllNcrTypes() {
                var url = "api/pqm/qualitytypes/ncrtypes";
                return httpFactory.get(url);
            }

            function getMultipleNcrTypes(ids) {
                var url = "api/pqm/qualitytypes/ncrtypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function createQcrType(planType) {
                var url = "api/pqm/qualitytypes/qcrtypes";
                return httpFactory.post(url, planType);
            }

            function updateQcrType(planType) {
                var url = "api/pqm/qualitytypes/qcrtypes/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteQcrType(id) {
                var url = "api/pqm/qualitytypes/qcrtypes/" + id;
                return httpFactory.delete(url);
            }

            function getQcrType(id) {
                var url = "api/pqm/qualitytypes/qcrtypes/" + id;
                return httpFactory.get(url);
            }

            function getAllQcrTypes() {
                var url = "api/pqm/qualitytypes/qcrtypes";
                return httpFactory.get(url);
            }

            function getMultipleQcrTypes(ids) {
                var url = "api/pqm/qualitytypes/qcrtypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getInspectionPlanTypeTree() {
                var url = "api/pqm/qualitytypes/inspectionplantypes/tree";
                return httpFactory.get(url);
            }

            function getProductInspectionPlanTypeTree() {
                var url = "api/pqm/qualitytypes/inspectionplantypes/product/tree";
                return httpFactory.get(url);
            }

            function getMaterialInspectionPlanTypeTree() {
                var url = "api/pqm/qualitytypes/inspectionplantypes/material/tree";
                return httpFactory.get(url);
            }

            function getPrTypeTree() {
                var url = "api/pqm/qualitytypes/prtypes/tree";
                return httpFactory.get(url);
            }

            function getNcrTypeTree() {
                var url = "api/pqm/qualitytypes/ncrtypes/tree";
                return httpFactory.get(url);
            }

            function getQcrTypeTree() {
                var url = "api/pqm/qualitytypes/qcrtypes/tree";
                return httpFactory.get(url);
            }

            function getQtType(id) {
                var url = "api/pqm/qualitytypes/" + id;
                return httpFactory.get(url);
            }

            function getQualityHistoryByDateWise(id, type, pageable, filter) {
                var url = "api/pqm/qualitytypes/" + id + "/" + type + "/history?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&date={1}&user={2}&revision={3}".format(filter.type, filter.date, filter.user, filter.revision);
                return httpFactory.get(url);
            }

            function getUsedAttributeValues(id) {
                var url = "api/pqm/qualitytypes/attributes/" + id;
                return httpFactory.get(url);
            }

            function createQualityObject(objectType, dto) {
                var url = "api/pqm/qualitytypes/" + objectType + "/object";
                return httpFactory.post(url, dto);
            }

            function getQualityTypeWorkflows(typeId, type) {
                var url = "api/pqm/qualitytypes/" + typeId + "/" + type + "/workflows";
                return httpFactory.get(url);
            }

            function updateQualityAttributeSeq(targetId, actualId) {
                var url = "api/pqm/qualitytypes/" + targetId + "/attributeseq/" + actualId;
                return httpFactory.post(url);
            }

            function getAllQualityTypeAttributes(objectType, type) {
                var url = "api/pqm/qualitytypes/" + objectType + "/attributes/" + type;
                return httpFactory.get(url);
            }

            function getQualityTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMultipleQualityTypes(ids).then(
                        function (data) {
                            var map = new Hashtable();

                            angular.forEach(data, function (itemType) {
                                map.put(itemType.id, itemType);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var itemType = map.get(object[property]);
                                    if (itemType != null) {
                                        object[property + "Object"] = itemType;
                                    }
                                }
                            });

                            if (callback != null && callback != undefined) {
                                callback();
                            }
                        }
                    );
                }
            }

            function getPersonByGroupNameAndPermission(groupName, permission) {
                var url = "api/pqm/qualitytypes/quality/analysts?groupName=" + groupName + "&permission=" + permission;
                return httpFactory.get(url);
            }

            function getQualityTypeTree() {
                var url = "api/pqm/qualitytypes/tree";
                return httpFactory.get(url);
            }

            function getQualityObjects(pageable, filters) {
                var url = "api/pqm/qualitytypes/all/quality/objects?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&searchQuery={1}".
                    format(filters.type, filters.searchQuery);
                return httpFactory.get(url);
            }


            // --------------------------------------PPAPTYPE--------------------------------------
            
            function createPpapType(ppapType) {
                var url = "api/pqm/qualitytypes/ppaptypes";
                return httpFactory.post(url, ppapType);
            }

            function updatePpapType(ppapType){
                var url = "api/pqm/qualitytypes/ppaptypes/" + ppapType.id;
                return httpFactory.put(url, ppapType);
            }
            function deletePpapType(id) {
                var url = "api/pqm/qualitytypes/ppaptypes/" + id;
                return httpFactory.delete(url);
            }

            function getPpapType(id) {
                var url = "api/pqm/qualitytypes/ppaptypes/" + id;
                return httpFactory.get(url);
            }

            function getAllPpapTypes() {
                var url = "api/pqm/qualitytypes/ppaptypes";
                return httpFactory.get(url);
            }

            function getMultiplePpapTypes(ids) {
                var url = "api/pqm/qualitytypes/ppaptypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }
            function getPpapTypeTree() {
                var url = "api/pqm/qualitytypes/ppaptypes/tree";
                return httpFactory.get(url);
            }

            function createSupplierAuditType(planType) {
                var url = "api/pqm/qualitytypes/supplieraudittypes";
                return httpFactory.post(url, planType);
            }

            function updateSupplierAuditType(planType) {
                var url = "api/pqm/qualitytypes/supplieraudittypes/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteSupplierAuditType(id) {
                var url = "api/pqm/qualitytypes/supplieraudittypes/" + id;
                return httpFactory.delete(url);
            }

            function getSupplierAuditType(id) {
                var url = "api/pqm/qualitytypes/supplieraudittypes/" + id;
                return httpFactory.get(url);
            }

            function getAllSupplierAuditTypes() {
                var url = "api/pqm/qualitytypes/supplieraudittypes";
                return httpFactory.get(url);
            }

            function getMultipleSupplierAuditTypes(ids) {
                var url = "api/pqm/qualitytypes/supplieraudittypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }
            function getSupplierAuditTypeTree() {
                var url = "api/pqm/qualitytypes/supplieraudittypes/tree";
                return httpFactory.get(url);
            }


        }
    }
)
;