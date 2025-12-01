define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('BomService', BomService);

        function BomService($q, httpFactory) {
            return {
                getBomTree: getBomTree,
                createBom: createBom,

                createEBom: createEBom,
                updateEBom: updateEBom,
                getAllBoms: getAllBoms,

                createBomItem: createBomItem,
                updateBomItem: updateBomItem,
                deleteBomItem: deleteBomItem,
                getItemBom: getItemBom,
                createNewBomItem: createNewBomItem,
                createBomInstance: createBomInstance,
                deleteBomInstance: deleteBomInstance,
                getInstanceBom: getInstanceBom,
                searchInwardItems: searchInwardItems,
                searchBomInstanceItems: searchBomInstanceItems,
                getInstanceToggle: getInstanceToggle,
                getBomInstances: getBomInstances,
                getBomItemChildren: getBomItemChildren,
                getBomStructureByBom: getBomStructureByBom,
                getBomItemBySelectedType: getBomItemBySelectedType,
                updateBomItemInstance: updateBomItemInstance,
                getSectionParts: getSectionParts,
                getByInstanceById: getByInstanceById,
                getDrdoImage: getDrdoImage,
                getSelectedItemStructure: getSelectedItemStructure,
                getSelectedSectionsStructure: getSelectedSectionsStructure,
                getRequestReportByInstance: getRequestReportByInstance,
                getRequestReportBySection: getRequestReportBySection,
                getChildrenByItem: getChildrenByItem,
                updateBomInstance: updateBomInstance,
                getBomInstance: getBomInstance,
                validateStorageByBom: validateStorageByBom,
                getBomInstanceTarbDocument: getBomInstanceTarbDocument,
                getBomInstanceSectionTarbDocument: getBomInstanceSectionTarbDocument,
                getReqItemsBySection: getReqItemsBySection,
                getRequestedItemsBySections: getRequestedItemsBySections,
                getBomInstanceItemByItemInstance: getBomInstanceItemByItemInstance,
                importBomParts: importBomParts,
                getWorkCenterItemsByBom: getWorkCenterItemsByBom,
                getBomItemBySelectedTypeAndSearchText: getBomItemBySelectedTypeAndSearchText,
                searchBomInstanceItemsBySection: searchBomInstanceItemsBySection,
                synchronizeBom: synchronizeBom,
                getSectionsByInstance: getSectionsByInstance,
                synchronizeBomSection: synchronizeBomSection,
                synchronizeBomUnit: synchronizeBomUnit,
                getReqItemsByUnits: getReqItemsByUnits,
                updateUniqueCodes: updateUniqueCodes,
                searchBomItems: searchBomItems,
                searchBom: searchBom,
                getRequestedItemsBySection: getRequestedItemsBySection,
                getSectionByBomId: getSectionByBomId,
                getReportByInstanceChildren: getReportByInstanceChildren,
                searchItemReport: searchItemReport,
                getSectionsByBom: getSectionsByBom,
                getChildrenByBomItem: getChildrenByBomItem
            };

            function createBom(bom) {
                var url = "api/drdo/bom";
                return httpFactory.post(url, bom);
            }

            function getBomTree() {
                var url = "api/drdo/bom/bomTree";
                return httpFactory.get(url);
            }

            function getAllBoms() {
                var url = "api/drdo/bom";
                return httpFactory.get(url);
            }

            function createEBom(item) {
                var url = "api/drdo/bom/item";
                return httpFactory.post(url, item);
            }

            function updateEBom(bom) {
                var url = "api/drdo/bom/item/" + bom.id;
                return httpFactory.put(url, bom);
            }

            function createBomItem(itemId, item) {
                var url = "api/drdo/bom/" + itemId + "/item";
                return httpFactory.post(url, item);
            }

            function createNewBomItem(itemId, item) {
                var url = "api/drdo/bom/" + itemId + "/item/new";
                return httpFactory.post(url, item);
            }

            function updateBomItem(item) {
                var url = "api/drdo/bom/item/" + item.id;
                return httpFactory.put(url, item);
            }

            function deleteBomItem(itemId) {
                var url = "api/drdo/bom/item/delete/" + itemId;
                return httpFactory.delete(url);
            }

            function getItemBom(itemId, versity) {
                var url = "api/drdo/bom/items/" + itemId + "?versity=" + versity;
                return httpFactory.get(url);
            }

            function getBomItemChildren(itemId) {
                var url = "api/drdo/bom/items/" + itemId + "/children";
                return httpFactory.get(url);
            }

            function createBomInstance(bomId, instance) {
                var url = "api/drdo/bom/instance/" + bomId;
                return httpFactory.post(url, instance);
            }

            function deleteBomInstance(bomId) {
                var url = "api/drdo/bom/instance/" + bomId;
                return httpFactory.delete(url);
            }

            function getInstanceToggle(id) {
                var url = "api/drdo/bom/items/instance/" + id + "/toggle";
                return httpFactory.get(url);
            }

            function getInstanceBom(id, versity) {
                var url = "api/drdo/bom/items/instance/" + id + "?versity=" + versity;
                return httpFactory.get(url);
            }

            function searchInwardItems(filter, pageable) {
                var url = "api/drdo/bom/inwardSearchResults?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&bom={1}".format(filter.searchQuery, filter.bom);
                return httpFactory.get(url);
            }

            function searchBomInstanceItems(filter, pageable) {
                var url = "api/drdo/bom/requestSearchResults?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&bom={1}".format(filter.searchQuery, filter.bom);
                return httpFactory.get(url);
            }

            function searchBomInstanceItemsBySection(filter, pageable) {
                var url = "api/drdo/bom/requestSearch/section?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&bom={1}&section={2}&subsystem={3}&unit={4}".format(filter.searchQuery, filter.bom, filter.section, filter.subsystem, filter.unit);
                return httpFactory.get(url);
            }

            function getBomInstances(bomId) {
                var url = "api/drdo/bom/instances/" + bomId;
                return httpFactory.get(url);
            }

            function getBomStructureByBom(bomId) {
                var url = "api/drdo/bom/structure/tree/" + bomId;
                return httpFactory.get(url);
            }

            function getBomItemBySelectedType(bomItem, storageId) {
                var url = "api/drdo/bom/" + bomItem + "/parts?storageId=" + storageId;
                return httpFactory.get(url);
            }

            function getBomItemBySelectedTypeAndSearchText(bomItem, storageId, searchText) {
                var url = "api/drdo/bom/" + bomItem + "/storage/" + storageId + "/parts?searchText=" + searchText;
                return httpFactory.get(url);
            }

            function searchBomItems(storageId, pageable, filter) {
                var url = "api/drdo/bom/storage/" + storageId + "/searchItems?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&bom={1}&section={2}&subsystem={3}&unit={4}".format(filter.searchQuery, filter.bom, filter.section, filter.subsystem, filter.unit);
                return httpFactory.get(url);
            }

            function searchBom(bomId, searchText, versity) {
                var url = "api/drdo/bom/" + bomId + "/search?searchText=" + searchText + "&versity=" + versity;
                return httpFactory.get(url);
            }

            function updateBomItemInstance(id, bomItemInstance) {
                var url = "api/drdo/bom/instance/" + id;
                return httpFactory.put(url, bomItemInstance);
            }

            function getSectionParts(path) {
                var url = "api/drdo/bom/by/section/" + path;
                return httpFactory.get(url);
            }

            function getByInstanceById(id) {
                var url = "api/drdo/bom/instances/byId/" + id;
                return httpFactory.get(url);
            }

            function getDrdoImage() {
                var url = "api/drdo/bom/drdologo";
                return httpFactory.get(url);
            }

            function getSelectedItemStructure(bomItemId) {
                var url = "api/drdo/bom/" + bomItemId + "/structure";
                return httpFactory.get(url);
            }

            function getSelectedSectionsStructure(sectionIds) {
                var url = "api/drdo/bom/sections/structure/[" + sectionIds + "]";
                return httpFactory.get(url);
            }

            function getRequestReportByInstance(instanceId) {
                var url = "api/drdo/bom/instance/requestReport/" + instanceId;
                return httpFactory.get(url);
            }

            function getRequestReportBySection(instanceId, sectionId) {
                var url = "api/drdo/bom/instance/requestReport/" + instanceId + "/section/" + sectionId;
                return httpFactory.get(url);
            }

            function getSectionsByInstance(instanceId, admin, versity) {
                var url = "api/drdo/bom/instance/" + instanceId + "/sections?admin=" + admin + "&versity=" + versity;
                return httpFactory.get(url);
            }

            function getSectionsByBom(bomId) {
                var url = "api/drdo/bom/" + bomId + "/sections";
                return httpFactory.get(url);
            }

            function getChildrenByBomItem(bomItemId) {
                var url = "api/drdo/bom/bomItem/" + bomItemId + "/children";
                return httpFactory.get(url);
            }

            function getChildrenByItem(instanceId) {
                var url = "api/drdo/bom/instance/" + instanceId + "/children";
                return httpFactory.get(url);
            }

            function getReqItemsBySection(instanceId, section) {
                var url = "api/drdo/bom/instance/" + instanceId + "/reqItems";
                return httpFactory.post(url, section);
            }

            function getReportByInstanceChildren(instanceId, section) {
                var url = "api/drdo/bom/instance/" + instanceId + "/report";
                return httpFactory.post(url, section);
            }

            function searchItemReport(filter, missileIds) {
                var url = "api/drdo/bom/items/report?searchQuery={0}&bom={1}&section={2}&subsystem={3}&unit={4}&workCenter={5}".
                    format(filter.searchQuery, filter.bom, filter.section, filter.subsystem, filter.unit, filter.workCenter);
                return httpFactory.post(url, missileIds);
            }

            function getReqItemsByUnits(instanceId, unitIds) {
                var url = "api/drdo/bom/instance/" + instanceId + "/reqItems/[" + unitIds + "]";
                return httpFactory.get(url);
            }

            function updateBomInstance(id, bomItemInstance) {
                var url = "api/drdo/bom/bomInstance/" + id;
                return httpFactory.put(url, bomItemInstance);
            }

            function getBomInstance(id) {
                var url = "api/drdo/bom/bomInstance/byId/" + id;
                return httpFactory.get(url);
            }

            function getBomInstanceTarbDocument(instanceId) {
                var url = "api/drdo/bom/instance/" + instanceId + "/document";
                return httpFactory.get(url);
            }

            function getBomInstanceSectionTarbDocument(instanceId, sectionId) {
                var url = "api/drdo/bom/instance/" + instanceId + "/section/" + sectionId + "/document";
                return httpFactory.get(url);
            }

            function validateStorageByBom(bomId) {
                var url = "api/drdo/bom/" + bomId + "/validate/storage";
                return httpFactory.get(url);
            }

            function getRequestedItemsBySections(instanceId) {
                var url = "api/drdo/bom/" + instanceId + "/sections/requestItems";
                return httpFactory.get(url);
            }

            function getRequestedItemsBySection(instanceId, sectionId, searchText) {
                var url = "api/drdo/bom/" + instanceId + "/section/" + sectionId + "/requestItems?searchText=" + searchText;
                return httpFactory.get(url);
            }

            function getBomInstanceItemByItemInstance(instance) {
                var url = "api/drdo/bom/bomInstanceItem/byInstance/" + instance;
                return httpFactory.get(url);
            }

            function importBomParts(parentId, files) {
                var url = "api/drdo/bom/" + parentId + "/import";
                return httpFactory.upload(url, files);
            }

            function getWorkCenterItemsByBom(pageable, filter) {
                var url = "api/drdo/bom/workCenter/items?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&bom={1}&versity={2}".format(filter.searchQuery, filter.bom, filter.versity);
                return httpFactory.get(url);
            }

            function synchronizeBom(bomId) {
                var url = "api/drdo/bom/" + bomId + "/synchronize";
                return httpFactory.get(url);
            }

            function synchronizeBomSection(bomId, section) {
                var url = "api/drdo/bom/" + bomId + "/synchronize/" + section;
                return httpFactory.get(url);
            }

            function synchronizeBomUnit(bomId, unit) {
                var url = "api/drdo/bom/" + bomId + "/synchronize/byunit/" + unit;
                return httpFactory.get(url);
            }

            function updateUniqueCodes() {
                var url = "api/drdo/bom/update/uniqueCode";
                return httpFactory.get(url);
            }

            function getSectionByBomId(id) {
                var url = "api/drdo/bom/sections/byBom/" + id;
                return httpFactory.get(url);
            }
        }
    }
);