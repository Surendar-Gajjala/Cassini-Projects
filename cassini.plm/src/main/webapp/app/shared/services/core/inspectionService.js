define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('InspectionService', InspectionService);

        function InspectionService(httpFactory) {
            return {
                createInspection: createInspection,
                updateInspection: updateInspection,
                deleteInspection: deleteInspection,
                getInspection: getInspection,
                getAllItemInspections: getAllItemInspections,
                getMultipleInspections: getMultipleInspections,
                createInspectionAttribute: createInspectionAttribute,
                updateInspectionAttribute: updateInspectionAttribute,
                getInspectionChecklists: getInspectionChecklists,
                getInspectionChecklistChildren: getInspectionChecklistChildren,
                getDetailsCount: getDetailsCount,
                getInspectionFiles: getInspectionFiles,
                getInspectionChecklistParams: getInspectionChecklistParams,
                updateInspectionChecklistParams: updateInspectionChecklistParams,
                updateInspectionChecklist: updateInspectionChecklist,
                getInspectionRelatedItems: getInspectionRelatedItems,
                createRelatedItems: createRelatedItems,
                updateRelatedItem: updateRelatedItem,
                deleteRelatedItem: deleteRelatedItem,
                getRejectedItemInspectionsByProduct: getRejectedItemInspectionsByProduct,
                getRejectedMaterialInspections: getRejectedMaterialInspections,
                getItemInspections: getItemInspections,
                updateItemInspection: updateItemInspection,
                deleteItemInspection: deleteItemInspection,
                getItemInspection: getItemInspection,
                updateMaterialInspection: updateMaterialInspection,
                deleteMaterialInspection: deleteMaterialInspection,
                getMaterialInspection: getMaterialInspection,
                getAllMaterialInspections: getAllMaterialInspections,
                createItemRelatedItems: createItemRelatedItems,
                updateItemRelatedItem: updateItemRelatedItem,
                deleteItemRelatedItem: deleteItemRelatedItem,
                getItemInspectionRelatedItems: getItemInspectionRelatedItems,
                getMaterialInspectionRelatedItems: getMaterialInspectionRelatedItems,
                createMaterialRelatedItems: createMaterialRelatedItems,
                updateMaterialRelatedItem: updateMaterialRelatedItem,
                deleteMaterialRelatedItem: deleteMaterialRelatedItem

            };

            function createInspection(inspection) {
                var url = "api/pqm/inspections";
                return httpFactory.post(url, inspection);
            }

            function updateInspection(id, inspection) {
                var url = "api/pqm/inspections/" + id;
                return httpFactory.put(url, inspection);
            }

            function deleteInspection(inspectionId) {
                var url = "api/pqm/inspections/" + inspectionId;
                return httpFactory.delete(url);
            }

            function getInspection(inspectionId) {
                var url = "api/pqm/inspections/" + inspectionId;
                return httpFactory.get(url);
            }

            function getAllItemInspections(pageable, filters) {
                var url = "api/pqm/inspections/items/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&inspectionNumber={0}&description={1}&searchQuery={2}".
                    format(filters.inspectionNumber, filters.description, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getItemInspections(itemId) {
                var url = "api/pqm/inspections/item/" + itemId;
                return httpFactory.get(url);
            }

            function getMultipleInspections(planIds) {
                var url = "api/pqm/inspections/multiple/[" + planIds + "]";
                return httpFactory.get(url);
            }

            function createInspectionAttribute(inspectionId, attribute) {
                var url = "api/pqm/inspections/" + inspectionId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateInspectionAttribute(inspectionId, attribute) {
                var url = "api/pqm/inspections/" + inspectionId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function getInspectionChecklists(inspectionId) {
                var url = "api/pqm/inspections/" + inspectionId + "/checklists";
                return httpFactory.get(url);
            }

            function updateInspectionChecklist(inspectionId, checklistId, checklist) {
                var url = "api/pqm/inspections/" + inspectionId + "/checklists/" + checklistId;
                return httpFactory.put(url, checklist);
            }

            function getInspectionChecklistChildren(inspectionId, checklistId) {
                var url = "api/pqm/inspections/" + inspectionId + "/checklists/" + checklistId + "/children";
                return httpFactory.get(url);
            }

            function getDetailsCount(inspectionId) {
                var url = "api/pqm/inspections/" + inspectionId + "/details/count";
                return httpFactory.get(url);
            }

            function getInspectionFiles(inspectionId) {
                var url = "api/pqm/inspections/" + inspectionId + "/files";
                return httpFactory.get(url);
            }

            function getInspectionChecklistParams(inspectionId, checklistId) {
                var url = "api/pqm/inspections/" + inspectionId + "/checklists/" + checklistId + "/params";
                return httpFactory.get(url);
            }

            function updateInspectionChecklistParams(inspectionId, checklistId, params) {
                var url = "api/pqm/inspections/" + inspectionId + "/checklists/" + checklistId + "/params";
                return httpFactory.put(url, params);
            }

            function getInspectionRelatedItems(inspectionId) {
                var url = "api/pqm/inspections/" + inspectionId + "/relateditems";
                return httpFactory.get(url);
            }

            function createRelatedItems(inspectionId, relatedItems) {
                var url = "api/pqm/inspections/" + inspectionId + "/relateditems";
                return httpFactory.post(url, relatedItems);
            }

            function updateRelatedItem(inspectionId, relatedItem) {
                var url = "api/pqm/inspections/" + inspectionId + "/relateditems/" + relatedItem.id;
                return httpFactory.put(url, relatedItem);
            }

            function deleteRelatedItem(inspectionId, relatedItem) {
                var url = "api/pqm/inspections/" + inspectionId + "/relateditems/" + relatedItem;
                return httpFactory.delete(url);
            }

            function getRejectedMaterialInspections() {
                var url = "api/pqm/inspections/materials/rejected";
                return httpFactory.get(url);
            }

            function getRejectedItemInspectionsByProduct(productId) {
                var url = "api/pqm/inspections/items/" + productId + "/rejected";
                return httpFactory.get(url);
            }

            function updateItemInspection(id, inspection) {
                var url = "api/pqm/inspections/items/" + id;
                return httpFactory.put(url, inspection);
            }

            function deleteItemInspection(inspectionId) {
                var url = "api/pqm/inspections/items/" + inspectionId;
                return httpFactory.delete(url);
            }

            function getItemInspection(inspectionId) {
                var url = "api/pqm/inspections/items/" + inspectionId;
                return httpFactory.get(url);
            }

            function updateMaterialInspection(id, inspection) {
                var url = "api/pqm/inspections/materials/" + id;
                return httpFactory.put(url, inspection);
            }

            function deleteMaterialInspection(inspectionId) {
                var url = "api/pqm/inspections/materials/" + inspectionId;
                return httpFactory.delete(url);
            }

            function getMaterialInspection(inspectionId) {
                var url = "api/pqm/inspections/materials/" + inspectionId;
                return httpFactory.get(url);
            }


            function getAllMaterialInspections(pageable, filters) {
                var url = "api/pqm/inspections/materials/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&inspectionNumber={0}&description={1}&searchQuery={2}".
                    format(filters.inspectionNumber, filters.description, filters.searchQuery);
                return httpFactory.get(url);
            }

            function createItemRelatedItems(inspectionId, relatedItems) {
                var url = "api/pqm/inspections/items/" + inspectionId + "/relateditems";
                return httpFactory.post(url, relatedItems);
            }

            function updateItemRelatedItem(inspectionId, relatedItem) {
                var url = "api/pqm/inspections/items/" + inspectionId + "/relateditems/" + relatedItem.id;
                return httpFactory.put(url, relatedItem);
            }

            function deleteItemRelatedItem(inspectionId, relatedItem) {
                var url = "api/pqm/inspections/items/" + inspectionId + "/relateditems/" + relatedItem;
                return httpFactory.delete(url);
            }

            function getItemInspectionRelatedItems(inspectionId) {
                var url = "api/pqm/inspections/items/" + inspectionId + "/relateditems";
                return httpFactory.get(url);
            }

            function getMaterialInspectionRelatedItems(inspectionId) {
                var url = "api/pqm/inspections/materials/" + inspectionId + "/relateditems";
                return httpFactory.get(url);
            }

            function createMaterialRelatedItems(inspectionId, relatedItems) {
                var url = "api/pqm/inspections/materials/" + inspectionId + "/relateditems";
                return httpFactory.post(url, relatedItems);
            }

            function updateMaterialRelatedItem(inspectionId, relatedItem) {
                var url = "api/pqm/inspections/materials/" + inspectionId + "/relateditems/" + relatedItem.id;
                return httpFactory.put(url, relatedItem);
            }

            function deleteMaterialRelatedItem(inspectionId, relatedItem) {
                var url = "api/pqm/inspections/materials/" + inspectionId + "/relateditems/" + relatedItem;
                return httpFactory.delete(url);
            }
        }
    }
);