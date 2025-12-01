/**
 * Created by CassiniSystems on 17-10-2018.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('RequirementsTypeService', RequirementsTypeService);

        function RequirementsTypeService($q, httpFactory) {
            return {
                getClassificationTree: getClassificationTree,
                createItemType: createItemType,
                getItemType: getItemType,
                updateItemType: updateItemType,
                deleteItemType: deleteItemType,
                getChildren: getChildren,

                getAttributes: getAttributes,
                getTypeAttributesWithHierarchy: getTypeAttributesWithHierarchy,
                getAttribute: getAttribute,
                createAttribute: createAttribute,
                updateAttribute: updateAttribute,
                deleteAttribute: deleteAttribute,
                getLifeCycleByName: getLifeCycleByName,
                getLifeCycles: getLifeCycles,
                getLifeCyclesPhases: getLifeCyclesPhases,
                getAllTypeAttributes: getAllTypeAttributes,
                getItemTypeReferences: getItemTypeReferences,
                getAllItemTypesByName: getAllItemTypesByName,
                findItemByAutoNumId: findItemByAutoNumId,
                getReqTypeItems: getReqTypeItems,

                getAttributeValues: getAttributeValues,
                getRequirementByType: getRequirementByType,
                getSpecByType: getSpecByType,
                updateRequirementEdit: updateRequirementEdit,
                getLastAcceptedRequirementEdit: getLastAcceptedRequirementEdit,
                getRequirementEditHistory: getRequirementEditHistory,
                acceptRequirementEditChange: acceptRequirementEditChange,
                rejectRequirementEditChange: rejectRequirementEditChange,
                approveRequirementEdits: approveRequirementEdits,
                getFinalEditHistory: getFinalEditHistory,
                getRequirementVersionHistory: getRequirementVersionHistory,
                attachWorkflow: attachWorkflow,
                deleteWorkflow: deleteWorkflow,
                getWorkflows: getWorkflows,
                getUsedRequirementTypeAttributeValues:getUsedRequirementTypeAttributeValues
            };

            function getAllItemTypesByName(name) {
                var url = "api/m/requirements/byName/" + name;
                return httpFactory.get(url);
            }

            function findItemByAutoNumId(autoNumberId) {
                var url = "api/rm/requirements/item/" + autoNumberId;
                return httpFactory.get(url);
            }

            function getClassificationTree() {
                var url = "api/rm/requirements/tree";
                return httpFactory.get(url);
            }

            function createItemType(itemType) {
                var url = "api/rm/requirements";
                return httpFactory.post(url, itemType);
            }

            function getItemType(typeId) {
                var url = "api/rm/requirements/" + typeId;
                return httpFactory.get(url);
            }

            function updateItemType(itemType) {
                var url = "api/rm/requirements/" + itemType.id;
                return httpFactory.put(url, itemType);
            }

            function deleteItemType(typeId) {
                var url = "api/rm/requirements/" + typeId;
                return httpFactory.delete(url);
            }

            function getChildren(typeId) {
                var url = "api/rm/requirements/" + typeId + "/children";
                return httpFactory.get(url);
            }

            function getAttributes(typeId) {
                var url = "api/rm/requirements/" + typeId + "/attributes";
                return httpFactory.get(url);
            }

            function getTypeAttributesWithHierarchy(typeId) {
                var url = "api/rm/requirements/type/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }


            function getAttribute(typeId, attributeId) {
                var url = "api/rm/requirements/" + typeId + "/attributes/" + attributeId;
                return httpFactory.get(url);

            }

            function createAttribute(typeId, attribute) {
                var url = "api/rm/requirements/" + typeId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateAttribute(typeId, attribute) {
                var url = "api/rm/requirements/" + typeId + "/attributes/" + attribute.id;
                return httpFactory.put(url, attribute);
            }

            function deleteAttribute(typeId, attributeId) {
                var url = "api/rm/requirements/" + typeId + "/attributes/" + attributeId;
                return httpFactory.delete(url);
            }

            function getLifeCycles() {
                var url = 'api/plm/lifecycles';
                return httpFactory.get(url);
            }

            function getLifeCyclesPhases() {
                var url = 'api/plm/lifecycles/phases';
                return httpFactory.get(url);
            }

            function getLifeCycleByName(name) {
                return $q(function (resolve, reject) {
                    getLifeCycles().then(
                        function (data) {
                            var found = [];
                            angular.forEach(data.content, function (item) {
                                if (item.name == name) {
                                    found.push(item);
                                }
                            });

                            resolve(found);
                        }
                    );
                });
            }

            function getAllTypeAttributes(objectType) {
                var url = "api/rm/requirements/attributes/" + objectType;
                return httpFactory.get(url);
            }

            function getItemTypesByIds(ids) {
                var url = "api/rm/specifications/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getItemTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getItemTypesByIds(ids).then(
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

            function getReqTypeItems(typeId) {
                var url = "api/rm/requirements/" + typeId + "/items";
                return httpFactory.get(url);
            }

            function getAttributeValues(attributeId) {
                var url = "api/rm/requirements/items/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function getRequirementByType(typeId, pageable) {
                var url = "api/rm/specifications/requirementType/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSpecByType(typeId, pageable) {
                var url = "api/rm/specifications/specType/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function updateRequirementEdit(requirement) {
                var url = "api/rm/requirements/edit/" + requirement.id + "?notes=" + requirement.notes;
                return httpFactory.put(url, requirement);
            }

            function getLastAcceptedRequirementEdit(reqId) {
                var url = "api/rm/requirements/" + reqId + "/edit/lastAccepted";
                return httpFactory.get(url);
            }

            function getRequirementEditHistory(requirement) {
                var url = "api/rm/requirements/" + requirement + "/edits";
                return httpFactory.get(url);
            }


            function acceptRequirementEditChange(entryEdit) {
                var url = "api/rm/requirements/edit/accept/" + entryEdit.id;
                return httpFactory.put(url, entryEdit);
            }

            function rejectRequirementEditChange(entryEdit) {
                var url = "api/rm/requirements/edit/reject/" + entryEdit.id;
                return httpFactory.put(url, entryEdit);
            }

            function approveRequirementEdits(specRequirement, entryEdit) {
                var url = "api/rm/requirements/" + specRequirement + "/edit/approve/" + entryEdit.id;
                return httpFactory.put(url, entryEdit);
            }

            function getFinalEditHistory(reqIds) {
                var url = "api/rm/requirements/multiple/[" + reqIds + "]";
                return httpFactory.get(url);
            }

            function getRequirementVersionHistory(reqId) {
                var url = "api/rm/requirements/" + reqId + "/versionHistory";
                return httpFactory.get(url);
            }

            function attachWorkflow(reqId, wfId) {
                var url = "api/rm/requirements/" + reqId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteWorkflow(reqId) {
                var url = 'api/rm/requirements/' + reqId + "/workflow/delete";
                return httpFactory.delete(url);
            }

            function getWorkflows(typeId,type) {
                var url = "api/rm/requirements/workflow/" + typeId + "/reqType/" + type;
                return httpFactory.get(url);
            }

            function getUsedRequirementTypeAttributeValues(attributeId) {
                var url = "api/rm/requirements/attributes/" + attributeId;
                return httpFactory.get(url);
            }

        }
    }
);
