define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ItemService', ItemService);

        function ItemService(httpFactory) {
            return {
                searchItems: searchItems,
                getManpower: getManpower,
                getMachines: getMachines,
                getMaterials: getMaterials,
                updateManpower: updateManpower,
                updateMachine: updateMachine,
                updateMaterial: updateMaterial,
                getItemsByType: getItemsByType,
                getMaterialItemReferences: getMaterialItemReferences,
                findByItemNumber: findByItemNumber,
                getBoqItemsByItemNumber: getBoqItemsByItemNumber,
                getItemReferences: getItemReferences,
                getMachineItem: getMachineItem,
                getMaterialItem: getMaterialItem,
                getManpowerItem: getManpowerItem,
                createManpowerItem: createManpowerItem,
                createMaterialItem: createMaterialItem,
                createMachineItem: createMachineItem,
                deleteItem: deleteItem,
                deleteMachine: deleteMachine,
                deleteManPower: deleteManPower,
                getMaterialItemAttributes: getMaterialItemAttributes,
                getMachineItemAttributes: getMachineItemAttributes,
                getManpowerItemAttributes: getManpowerItemAttributes,
                saveManpowerItemAttributes: saveManpowerItemAttributes,
                saveMaterialItemAttributes: saveMaterialItemAttributes,
                saveMachineItemAttributes: saveMachineItemAttributes,
                updateManpowerItemAttributes: updateManpowerItemAttributes,
                updateMaterialItemAttributes: updateMaterialItemAttributes,
                updateMachineItemAttributes: updateMachineItemAttributes,
                machineFreeTextSearch: machineFreeTextSearch,
                materialFreeTextSearch: materialFreeTextSearch,
                manpowerFreeTextSearch: manpowerFreeTextSearch,
                getMaterialItemsByType: getMaterialItemsByType,
                getMachineItemsByType: getMachineItemsByType,
                getManpowerItemsByType: getManpowerItemsByType,
                getMaterialsByType: getMaterialsByType,
                getMachinesByType: getMachinesByType,
                getManpowerByType: getManpowerByType,
                getAllMaterialTypesByName: getAllMaterialTypesByName,
                getAllMachineTypesByName: getAllMachineTypesByName,
                getAllManpowerTypesByName: getAllManpowerTypesByName,
                getAllTypeAttributes: getAllTypeAttributes,
                findItemByAutoNumId: findItemByAutoNumId,
                findMachineItemsByAttribute: findMachineItemsByAttribute,
                findMaterialItemsByAttribute: findMaterialItemsByAttribute,
                findManpowerItemsByAttribute: findManpowerItemsByAttribute,
                findReceiveTypeItemsByAttribute: findReceiveTypeItemsByAttribute,
                findIssueTypeItemsByAttribute: findIssueTypeItemsByAttribute,
                findMaterialByItemNumber: findMaterialByItemNumber,
                findMachineByItemNumber: findMachineByItemNumber,
                getAttributesByItemIdAndAttributeId: getAttributesByItemIdAndAttributeId,
                getTypeAttributesRequiredFalse: getTypeAttributesRequiredFalse,
                getTypeAttributesRequiredTrue: getTypeAttributesRequiredTrue,
                getItemsByAutoNumberIds: getItemsByAutoNumberIds,
                getActiveManpower: getActiveManpower,
                getMaterialTypeByItemNumber: getMaterialTypeByItemNumber,
                getMachineTypeByItemNumber: getMachineTypeByItemNumber,
                findMaterialItemByBoq: findMaterialItemByBoq,
                findMachineItemByBoq: findMachineItemByBoq,
                getMasterdataItems: getMasterdataItems,
                getMaterialsByItemNumbers: getMaterialsByItemNumbers,
                getMachinesByItemNumbers: getMachinesByItemNumbers,
                getItemsByBoqIds: getItemsByBoqIds,
                getProjectItems: getProjectItems,
                getProjectMaterials: getProjectMaterials,
                getAllMaterials: getAllMaterials,
                createMultipleManpowerItems: createMultipleManpowerItems,
                getProjectPersonsByFilters: getProjectPersonsByFilters,
                getAttributesByObjectIdsAndAttributeIds: getAttributesByObjectIdsAndAttributeIds,
                getExistingPersons: getExistingPersons,
                getNonManpowerPersons: getNonManpowerPersons,
                searchStoreMaterials: searchStoreMaterials,
                uploadImage: uploadImage,
                getReceiveItemsByType: getReceiveItemsByType,
                getIssueItemsByType: getIssueItemsByType,
                updateReceiveTypeItemAttributes: updateReceiveTypeItemAttributes,
                updateIssueTypeItemAttributes: updateIssueTypeItemAttributes,
                saveReceiveTypeItemAttributes: saveReceiveTypeItemAttributes,
                saveIssueTypeItemAttributes: saveIssueTypeItemAttributes,
                getMaterialItemsDTO: getMaterialItemsDTO,
                getMachineItemReferences: getMachineItemReferences
            };

            function getAllMaterialTypesByName(itemName) {
                var url = "api/is/items/allMaterialTypesByName/" + itemName;
                return httpFactory.get(url);
            }

            function getMaterialTypeByItemNumber(itemNumber) {
                var url = "api/is/items/materialTypeByItemNumber/" + itemNumber;
                return httpFactory.get(url);
            }

            function getMachineTypeByItemNumber(itemNumber) {
                var url = "api/is/items/machineTypeByItemNumber/" + itemNumber;
                return httpFactory.get(url);
            }

            function getAllManpowerTypesByName(itemName) {
                var url = "api/is/items/allManpowerTypesByName/" + itemName;
                return httpFactory.get(url);
            }

            function getAllMachineTypesByName(itemName) {
                var url = "api/is/items/allMachineTypesByName/" + itemName;
                return httpFactory.get(url);
            }

            function getMaterialsByType(itemType) {
                var url = "api/is/items/allMaterials/" + itemType.objectType + "/" + itemType.id;
                return httpFactory.get(url);
            }

            function getMachinesByType(itemType) {
                var url = "api/is/items/allMachines/" + itemType.objectType + "/" + itemType.id;
                return httpFactory.get(url);
            }

            function getManpowerByType(itemType) {
                var url = "api/is/items/allManpower/" + itemType.objectType + "/" + itemType.id;
                return httpFactory.get(url);
            }

            function getBoqItemsByItemNumber(projectId, itemNumber) {
                var url = "api/projects/" + projectId + "/boq/" + itemNumber + "/boqItems";
                return httpFactory.get(url);
            }

            function findMaterialItemByBoq(boqId) {
                var url = "api/is/items/materials/boq/" + boqId;
                return httpFactory.get(url);
            }

            function findMachineItemByBoq(boqId) {
                var url = "api/is/items/machines/boq/" + boqId;
                return httpFactory.get(url);
            }

            function getItemsByBoqIds(taskId, boqIds) {
                var url = "api/is/items/task/" + taskId + "/multiple/boq/" + boqIds;
                return httpFactory.get(url);
            }

            function getMaterialItemsByType(typeId, pageable) {
                var url = "api/is/items/allMaterials/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMachineItemsByType(typeId, pageable) {
                var url = "api/is/items/allMachines/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getManpowerItemsByType(typeId, pageable) {
                var url = "api/is/items/allManpower/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getReceiveItemsByType(typeId, pageable) {
                var url = "api/is/items/receives/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getIssueItemsByType(typeId, pageable) {
                var url = "api/is/items/issues/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getAllMaterials() {
                var url = "api/is/items/getAllMaterials";
                return httpFactory.get(url);
            }

            function getMaterials(pageable) {
                var url = "api/is/items/allMaterials?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMachines(pageable) {
                var url = "api/is/items/allMachines?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getManpower(pageable) {
                var url = "api/is/items/allManpower?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function searchItems(searchQuery, pageable) {
                var url = "api/is/items/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(searchQuery);
                return httpFactory.get(url);
            }

            function getItemsByType(typeId, pageable) {
                var url = "api/is/items/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMaterialItemReferences(ids) {
                var url = "api/is/items/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMachineReferences(ids) {
                var url = "api/is/items/machine/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMaterialsByItemNumbers(itemNumbers) {
                var url = "api/is/items/materials/itemNumbers/[" + itemNumbers + "]";
                return httpFactory.get(url);
            }

            function getMachinesByItemNumbers(itemNumbers) {
                var url = "api/is/items/machines/itemNumbers/[" + itemNumbers + "]";
                return httpFactory.get(url);
            }


            function getItemReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMaterialItemReferences(ids).then(
                        function (data) {
                            var map = new Hashtable();

                            angular.forEach(data, function (item) {
                                map.put(item.id, item);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var item = map.get(object[property]);
                                    if (item != null) {
                                        object[property + "Object"] = item;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getMaterialItem(materialItemId) {
                var url = "api/is/items/materialItem/" + materialItemId;
                return httpFactory.get(url);
            }

            function getMachineItem(machineItemId) {
                var url = "api/is/items/machineItem/" + machineItemId;
                return httpFactory.get(url);
            }

            function getManpowerItem(manpowerId) {
                var url = "api/is/items/manpowerItem/" + manpowerId;
                return httpFactory.get(url);
            }

            function createMaterialItem(item) {
                var url = "api/is/items/material";
                return httpFactory.post(url, item);
            }

            function createMachineItem(item) {
                var url = "api/is/items/machine";
                return httpFactory.post(url, item);
            }

            function createManpowerItem(item) {
                var url = "api/is/items/manpower";
                return httpFactory.post(url, item);
            }

            function createMultipleManpowerItems(items) {
                var url = "api/is/items/manpower/multiple/items";
                return httpFactory.post(url, items);
            }

            function updateManpower(manpower) {
                var url = "api/is/items/updateManpower/" + manpower.id;
                return httpFactory.put(url, manpower);
            }

            function updateMaterial(material) {
                var url = "api/is/items/updateMaterial/" + material.id;
                return httpFactory.put(url, material);
            }

            function updateMachine(machine) {
                var url = "api/is/items/updateMachine/" + machine.id;
                return httpFactory.put(url, machine);
            }

            function deleteItem(itemId) {
                var url = "api/is/items/material/" + itemId;
                return httpFactory.delete(url);
            }

            function deleteMachine(itemId) {
                var url = "api/is/items/machine/" + itemId;
                return httpFactory.delete(url);
            }

            function deleteManPower(itemId) {
                var url = "api/is/items/manPower/" + itemId;
                return httpFactory.delete(url);
            }

            function getManpowerItemAttributes(itemId) {
                var url = "api/is/items/" + itemId + "/manpower/attributes";
                return httpFactory.get(url);
            }

            function getMachineItemAttributes(itemId) {
                var url = "api/is/items/" + itemId + "/machine/attributes";
                return httpFactory.get(url);
            }

            function getMaterialItemAttributes(itemId) {
                var url = "api/is/items/" + itemId + "/material/attributes";
                return httpFactory.get(url);
            }

            function saveManpowerItemAttributes(itemId, attributes) {
                var url = "api/is/items/manpower/" + itemId + "/attributes";
                return httpFactory.post(url, attributes);
            }

            function saveMaterialItemAttributes(itemId, attributes) {
                var url = "api/is/items/material/" + itemId + "/attributes";
                return httpFactory.post(url, attributes);
            }

            function saveReceiveTypeItemAttributes(itemId, attributes) {
                var url = "api/is/items/receiveTypes/" + itemId + "/attributes";
                return httpFactory.post(url, attributes);
            }

            function saveIssueTypeItemAttributes(itemId, attributes) {
                var url = "api/is/items/issueTypes/" + itemId + "/attributes";
                return httpFactory.post(url, attributes);
            }

            function saveMachineItemAttributes(itemId, attributes) {
                var url = "api/is/items/machine/" + itemId + "/attributes";
                return httpFactory.post(url, attributes);
            }

            function updateManpowerItemAttributes(itemId, attributes) {
                var url = "api/is/items/manpower/" + itemId + "/attributes";
                return httpFactory.put(url, attributes);
            }

            function updateMaterialItemAttributes(itemId, attributes) {
                var url = "api/is/items/material/" + itemId + "/attributes";
                return httpFactory.put(url, attributes);
            }

            function updateMachineItemAttributes(itemId, attributes) {
                var url = "api/is/items/machine/" + itemId + "/attributes";
                return httpFactory.put(url, attributes);
            }

            function updateReceiveTypeItemAttributes(itemId, attributes) {
                var url = "api/is/items/receiveType/" + itemId + "/attributes";
                return httpFactory.put(url, attributes);
            }

            function updateIssueTypeItemAttributes(itemId, attributes) {
                var url = "api/is/items/issueType/" + itemId + "/attributes";
                return httpFactory.put(url, attributes);
            }


            function findByItemNumber(itemNumber) {
                var url = "api/is/items/number/" + itemNumber;
                return httpFactory.get(url);
            }

            function materialFreeTextSearch(pageable, criteria) {
                var url = "api/is/items/material/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.post(url, criteria);
            }

            function machineFreeTextSearch(pageable, criteria) {
                var url = "api/is/items/machine/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.post(url, criteria);
            }

            function manpowerFreeTextSearch(pageable, criteria) {
                var url = "api/is/items/manpower/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.post(url, criteria);
            }

            function getAllTypeAttributes(objectType) {
                var url = "api/is/items/attributes/" + objectType;
                return httpFactory.get(url);
            }

            function getTypeAttributesRequiredFalse(objectType) {
                var url = "api/is/items/requiredFalseAttributes?objectType=" + objectType;
                return httpFactory.get(url);
            }

            function getTypeAttributesRequiredTrue(objectType) {
                var url = "api/is/items/requiredTrueAttributes?objectType=" + objectType;
                return httpFactory.get(url);
            }

            function getAttributesByObjectIdsAndAttributeIds(objectIds, attributeIds) {
                var url = "api/is/items/objects/objectAttributes";
                return httpFactory.post(url, [objectIds, attributeIds]);
            }

            function findItemByAutoNumId(autoNumberId) {
                var url = "api/is/items/machine/item/" + autoNumberId;
                return httpFactory.get(url);
            }

            function getItemsByAutoNumberIds(autoNumberIds) {
                var url = "api/is/items/autoNumbers/multiple/[" + autoNumberIds + "]";
                return httpFactory.get(url);
            }

            function findMachineItemsByAttribute(attributeId) {
                var url = "api/is/items/machine/attributeId/" + attributeId;
                return httpFactory.get(url);
            }

            function findMaterialItemsByAttribute(attributeId) {
                var url = "api/is/items/material/attributeId/" + attributeId;
                return httpFactory.get(url);
            }

            function findManpowerItemsByAttribute(attributeId) {
                var url = "api/is/items/manpower/attributeId/" + attributeId;
                return httpFactory.get(url);
            }

            function findReceiveTypeItemsByAttribute(attributeId) {
                var url = "api/is/items/receiveType/attributeId/" + attributeId;
                return httpFactory.get(url);
            }

            function findIssueTypeItemsByAttribute(attributeId) {
                var url = "api/is/items/issueType/attributeId/" + attributeId;
                return httpFactory.get(url);
            }

            function findMaterialByItemNumber(itemNumber) {
                var url = "api/is/items/material/number/" + itemNumber;
                return httpFactory.get(url);
            }

            function findMachineByItemNumber(itemNumber) {
                var url = "api/is/items/machine/number/" + itemNumber;
                return httpFactory.get(url);
            }

            function getAttributesByItemIdAndAttributeId(itemIds, attributeIds) {
                var url = "api/is/items/objectAttributes/material";
                return httpFactory.post(url, [itemIds, attributeIds]);
            }

            function getActiveManpower() {
                var url = "api/is/items/allActiveManpower";
                return httpFactory.get(url);
            }

            function getMasterdataItems() {
                var url = "api/is/items/itemsDTO";
                return httpFactory.get(url);
            }

            function getProjectItems(projectId) {
                var url = "api/is/items/project/" + projectId + "/itemsDTO";
                return httpFactory.get(url);
            }

            function getProjectMaterials(projectId, pageable) {
                var url = "api/is/items/project/" + projectId + "/materials/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function searchStoreMaterials(projectId, storeId, pageable, freeText) {
                var url = "api/is/items/project/" + projectId + "/store/" + storeId + "/materials/freeSearch?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function getProjectPersonsByFilters(projectId, criteria) {
                var url = "api/is/items/" + projectId + "/filters";
                url += "?firstName={0}&lastName={1}".format(criteria.firstName, criteria.lastName);
                return httpFactory.get(url);
            }

            //login persons who were not added into manpower items for the selected type
            function getExistingPersons(itemType) {
                var url = "api/is/items/itemType/" + itemType + "/nonManpowerpersons";
                return httpFactory.get(url);
            }

            //List of manpowerItems
            function getNonManpowerPersons(itemType) {
                var url = "api/is/items/itemType/" + itemType + "/manpowerItems";
                return httpFactory.get(url);
            }

            function uploadImage(personId, file) {
                var url = "api/is/items/" + personId + "/uploadImage";
                return httpFactory.upload(url, file);
            }

            function getMaterialItemsDTO(storeId, pageable, freeText) {
                var url = "api/is/items/materials/store/" + storeId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }


            function getMachineItemReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMachineReferences(ids).then(
                        function (data) {
                            var map = new Hashtable();

                            angular.forEach(data, function (item) {
                                map.put(item.id, item);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var item = map.get(object[property]);
                                    if (item != null) {
                                        object[property + "Object"] = item;
                                    }
                                }
                            });
                        }
                    );
                }
            }
        }
    }
);