define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('BomService', BomService);

        function BomService(httpFactory) {
            return {
                getProjectBoms: getProjectBoms,
                getProjectBom: getProjectBom,
                createProjectBom: createProjectBom,
                updateProjectBom: updateProjectBom,
                deleteProjectBom: deleteProjectBom,
                createBomItem: createBomItem,
                updateBomItem: updateBomItem,
                deleteBomItem: deleteBomItem,
                deleteBomItems: deleteBomItems,
                getBomItems: getBomItems,
                saveBomItems: saveBomItems,
                getBomItemsByIds: getBomItemsByIds,
                getBoqItemReferences: getBoqItemReferences,
                searchBomItems: searchBomItems,
                getAllBoqItems: getAllBoqItems,
                getBomItem: getBomItem,
                getAllItems: getAllItems,
                getPagedBoqItems: getPagedBoqItems,
                createCostToProject: createCostToProject,
                getCostToProject: getCostToProject,
                deleteCostToProject: deleteCostToProject,
                getBomItemsByType: getBomItemsByType,
                getItemsForTask: getItemsForTask,
                getBomItemByItemNumberAndboqName: getBomItemByItemNumberAndboqName,
                getBoqItemByProjectAndItemNumber: getBoqItemByProjectAndItemNumber,
                getBomItemByItemNumber: getBomItemByItemNumber,
                getBoqItemByItemNumber: getBoqItemByItemNumber,
                getBoqItem: getBoqItem,
                importBoq: importBoq,
                exportBoq: exportBoq,
                getBoqItemsByFilters: getBoqItemsByFilters,
                importPdfFile: importPdfFile,
                getPdfFile: getPdfFile
            };

            function getBomItemByItemNumberAndboqName(projectId, itemNumber, boqName) {
                var url = "api/projects/" + projectId + "/boq/itemNumber/" + itemNumber + "/" + boqName;
                return httpFactory.get(url);
            }

            function getBomItemByItemNumber(projectId, itemNumber) {
                var url = "api/projects/" + projectId + "/boq/itemNumber/" + itemNumber;
                return httpFactory.get(url);
            }

            function getBomItemsByType(projectId, resourceType, pageable) {
                var url = "api/projects/" + projectId + "/boq/resourceType/" + resourceType + "/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getItemsForTask(projectId, taskId, resourceType, pageable) {
                var url = "api/projects/" + projectId + "/boq/task/" + taskId + "/resourceType/" + resourceType + "/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getCostToProject(projectId, itemId) {
                var url = "api/projects/" + projectId + "/boq/costToProject/" + itemId;
                return httpFactory.get(url);
            }

            function deleteCostToProject(projectId, boqId) {
                var url = "api/projects/" + projectId + "/boq/costToProject/" + boqId;
                return httpFactory.delete(url);
            }

            function createCostToProject(projectId, costToProject) {
                var url = "api/projects/" + projectId + "/boq/costToProject";
                return httpFactory.post(url, costToProject);
            }

            function getProjectBoms(projectId) {
                var url = "api/projects/" + projectId + "/boq";
                return httpFactory.get(url);
            }

            function getProjectBom(projectId, boqId) {
                var url = "api/projects/" + projectId + "/boq/" + boqId;
                return httpFactory.get(url);
            }

            function createProjectBom(projectId, boq) {
                var url = "api/projects/" + projectId + "/boq";
                return httpFactory.post(url, boq);
            }

            function updateProjectBom(projectId, boq) {
                var url = "api/projects/" + projectId + "/boq/" + boq.id;
                return httpFactory.put(url, boq);
            }

            function deleteProjectBom(projectId, boqId) {
                var url = "api/projects/" + projectId + "/boq/" + boqId;
                return httpFactory.delete(url);
            }

            function getAllBoqItems(projectId) {
                var url = "api/projects/" + projectId + "/boq/items";
                return httpFactory.get(url);
            }

            function getPagedBoqItems(projectId, pageable) {
                var url = "api/projects/" + projectId + "/boq/items/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllItems(pageable) {
                var url = "api/is/items";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function searchBomItems(projectId, freetext) {
                var url = "api/projects/" + projectId + "/boq/search?&searchQuery={0}".
                        format(freetext);
                return httpFactory.get(url);
            }

            function getBomItems(projectId, boqId) {
                var url = "api/projects/" + projectId + "/boq/" + boqId + "/items";
                return httpFactory.get(url);
            }

            function createBomItem(projectId, boqId, item) {
                var url = "api/projects/" + projectId + "/boq/" + boqId + "/items";
                return httpFactory.post(url, item);
            }

            function saveBomItems(projectId, boqId, items) {
                var url = "api/projects/" + projectId + "/boq/" + boqId + "/items/multiple";
                return httpFactory.post(url, items);
            }

            function getBomItem(projectId, boqId, itemNumbers) {
                var url = "api/projects/" + projectId + "/boq/" + boqId + "/items/" + itemNumbers;
                return httpFactory.get(url);
            }

            function updateBomItem(projectId, boqId, item) {
                var url = "api/projects/" + projectId + "/boq/" + boqId + "/items/" + item.itemId;
                return httpFactory.put(url, item);
            }

            function deleteBomItem(projectId, boqId, item) {
                var url = "api/projects/" + projectId + "/boq/" + boqId + "/items/" + item.id;
                return httpFactory.delete(url);
            }

            function deleteBomItems(projectId, boqId, items) {
                var url = "api/projects/" + projectId + "/boq/" + boqId + "/items/" + items;
                return httpFactory.delete(url);
            }

            function getBomItemsByIds(projectId, ids) {

                var url = "api/projects/" + projectId + "/boq/multiple/items/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getBoqItemReferences(projectId, objects, property) {

                var boqItemIds = [];
                var iMap = new Hashtable();
                angular.forEach(objects, function (object) {
                    var boqItemId = object[property];
                    if (iMap.get(boqItemId) == null) {
                        boqItemIds.push(boqItemId);
                        iMap.put(boqItemId, object);
                    }
                });

                if (boqItemIds.length > 0) {
                    getBomItemsByIds(projectId, boqItemIds).then(
                        function (boqItems) {
                            var map = new Hashtable();
                            angular.forEach(boqItems, function (boqItem) {
                                map.put(boqItem.id, boqItem);

                            });

                            angular.forEach(objects, function (object) {
                                var id = object[property];
                                var boqItem = map.get(id);
                                if (boqItem != null) {
                                    object[property + "Object"] = boqItem;
                                }
                            });
                        }
                    )
                }
            }

            function getBoqItemByProjectAndItemNumber(projectId, itemNumber) {
                var url = "api/projects/" + projectId + "/boq/boqItem/" + itemNumber;
                return httpFactory.get(url);
            }

            function getBoqItem(boqId) {
                var url = "api/projects/boq/boqItem/" + boqId;
                return httpFactory.get(url);
            }

            function importBoq(files, projectId) {
                var url = "api/projects/" + projectId + "/boq/import";
                return httpFactory.uploadMultiple(url, files);
            }

            function importPdfFile(files, projectId) {
                var url = "api/projects/" + projectId + "/boq/pdf/import";
                return httpFactory.upload(url, files);
            }

            // ItemController
            function getBoqItemByItemNumber(itemNumber) {
                var url = "api/is/items/boq/boqItem/" + itemNumber;
                return httpFactory.get(url);
            }

            function exportBoq(projectId, fileType, objects) {
                var url = "api/projects/" + projectId + "/boq/export";
                url += "?fileType={0}".format(fileType);
                return httpFactory.post(url, objects);
            }

            function getBoqItemsByFilters(projectId, taskId, pageable, filters) {
                var url = "api/projects/" + projectId + "/boq/task/" + taskId + "/filters?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                url += "&itemNumber={0}&itemName={1}&itemType={2}&searchQuery={3}".
                    format(filters.itemNumber, filters.itemName, filters.itemType, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getPdfFile(projectId) {
                var url = "api/projects/" + projectId + "/boq/pdf";
                return httpFactory.get(url);
            }

            /*  function getBoqItemsByFilters(projectId,pageable, freeText) {
             var url = "api/projects/" + projectId + "/boq/pageable?page={0}&size={1}".
             format(pageable.page, pageable.size);
             url += "&itemNumber={0}&itemName={1}&itemType={2}".
             format(filters.itemNumber,filters.itemName, filters.itemType);
             return httpFactory.get(url);
             }*/


            /*function getBoqItemsByFilters(projectId, pageable, freeText) {
             /!* var url = "api/is/items/manpower/freesearch?page={0}&size={1}&sort={2}:{3}".*!/
             var url = "api/projects/" + projectId + "/boq/filters?page={0}&size={1}".
             format(pageable.page, pageable.size);
             url += "&searchQuery={0}".
             format(freeText);
             return httpFactory.get(url);
             }*/


        }
    }
);