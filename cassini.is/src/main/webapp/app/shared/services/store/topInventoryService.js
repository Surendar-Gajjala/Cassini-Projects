define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TopInventoryService', TopInventoryService);

        function TopInventoryService(httpFactory) {
            return {
                createTopInventory: createTopInventory,
                updateTopInventory: updateTopInventory,
                deleteTopInventory: deleteTopInventory,
                getTopInventory: getTopInventory,
                getTopInventories: getTopInventories,
                getAllCustomInventories: getAllCustomInventories,
                searchCustomInventories: searchCustomInventories,
                getInventoryByBoq: getInventoryByBoq,
                getInventoryByProjectAndItemAndStore: getInventoryByProjectAndItemAndStore,
                getInventoryByItemNumber: getInventoryByItemNumber,
                getStoreInventoryforLoan: getStoreInventoryforLoan,
                getInventoryByItemNumberAndProject: getInventoryByItemNumberAndProject,
                //getProjectInventoryByTaskIds: getProjectInventoryByTaskId,
                getProjectMaterialShortageDTO: getProjectMaterialShortageDTO,
                getInventoryByBoqItemNumber: getInventoryByBoqItemNumber,
                getInventoryByFilters: getInventoryByFilters,
                getUnallocatedProjectInventoryBoq: getUnallocatedProjectInventoryBoq,
                allocateItemsToProject: allocateItemsToProject,
                getProjectInventoryByItems: getProjectInventoryByItems
            };

            function createTopInventory(topInventory) {
                var url = "api/is/stores/inventory";
                return httpFactory.post(url, topInventory);
            }

            function updateTopInventory(topInventory) {
                var url = "api/is/stores/inventory/" + topInventory.id;
                return httpFactory.put(url, topInventory);
            }

            function deleteTopInventory(InventoryId) {
                var url = "api/is/stores/inventory/" + InventoryId;
                return httpFactory.delete(url);
            }

            function getTopInventory(topInventoryId) {
                var url = "api/is/stores/inventory/" + topInventoryId;
                return httpFactory.get(url);
            }

            function getTopInventories() {
                var url = "api/is/stores/inventory";
                return httpFactory.get(url);
            }

            function getInventoryByBoq(boqId) {
                var url = "api/is/stores/inventory/boq/" +boqId;
                return httpFactory.get(url);
            }

            function getAllCustomInventories(pageable) {
                var url = "api/is/stores/inventory/allInvStoreDetails/pageable?page={0}&size={1}".
                    format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function searchCustomInventories(pageable, freeText) {
                var url = "api/is/stores/inventory/allInvStoreDetails/freesearch?page={0}&size={1}".
                    format(pageable.page, pageable.size);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function getInventoryByProjectAndItemAndStore(projectId, itemId, storeId) {
                var url = "api/is/stores/inventory/item/" +itemId+ "/store/" +storeId+ "/project/" +projectId;
                return httpFactory.get(url);
            }

            function getInventoryByItemNumber(itemNumber) {
                var url = "api/is/stores/inventory/getInventoryByItemNumber/" + itemNumber;
                return httpFactory.get(url);
            }

            function getInventoryByItemNumberAndProject(projectId, itemNumber) {
                var url = "api/is/stores/inventory/project/" + projectId+ "/item/" + itemNumber;
                return httpFactory.get(url);
            }

            function getStoreInventoryforLoan(storeId, projectId, pageable) {
                var url = "api/is/stores/inventory/project/" + projectId + "/store/" + storeId + "/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            //function getProjectInventoryByTaskId(projectId, taskId) {
            //    var url = "api/is/stores/inventory/project/" + projectId + "/task/" + taskId + "";
            //    return httpFactory.get(url);
            //}

            function getProjectMaterialShortageDTO(projectId, taskId) {
                var url = "api/is/stores/inventory/project/" + projectId ;
                return httpFactory.get(url);
            }

            function getInventoryByBoqItemNumber(projectId, itemNumber, boqId) {
                var url = "api/is/stores/inventory/project/" + projectId + "/itemNumber/" + itemNumber + "/boq/" +boqId;
                return httpFactory.get(url);
            }

            function getInventoryByFilters(storeId,pageable, criteria) {
                var url = "api/is/stores/inventory/store/" + storeId + "/filters?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.post(url, criteria);
            }

            function getUnallocatedProjectInventoryBoq(storeId, projectId ) {
                var url = "api/is/stores/inventory/project/" + projectId + "/store/" + storeId + "/inventoryBoq";
                return httpFactory.get(url);
            }

            function allocateItemsToProject(storeId, projectId, items) {
                var url = "api/is/stores/inventory/project/" + projectId + "/store/" + storeId ;
                return httpFactory.put(url, items);
            }

            function getProjectInventoryByItems(projectId) {
                var url = "api/is/stores/inventory/project/" + projectId + "/items";
                return httpFactory.get(url);
            }

        }
    }
);