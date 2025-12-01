define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ItemTypeService', ItemTypeService);

        function ItemTypeService($q, httpFactory) {
            return {
                getClassificationTree: getClassificationTree,
                createItemType: createItemType,
                getItemType: getItemType,
                updateItemType: updateItemType,
                deleteItemType: deleteItemType,
                getChildren: getChildren,

                getAttributes: getAttributes,
                getAttributesWithHierarchy: getAttributesWithHierarchy,
                getAttribute: getAttribute,
                createAttribute: createAttribute,
                updateAttribute: updateAttribute,
                deleteAttribute: deleteAttribute,
                getLifeCycleByName: getLifeCycleByName,
                getLifeCycles: getLifeCycles,
                getObjectTypeAttributes: getObjectTypeAttributes,
                getLifeCyclesPhases: getLifeCyclesPhases,
                getItemTypeReferences: getItemTypeReferences,
                getMachineAttributesWithHierarchy: getMachineAttributesWithHierarchy,
                getMaterialAttributesWithHierarchy: getMaterialAttributesWithHierarchy,
                getManpowerAttributesWithHierarchy: getManpowerAttributesWithHierarchy,
                getReceiveTypeAttributesWithHierarchy: getReceiveTypeAttributesWithHierarchy,
                getIssueTypeAttributesWithHierarchy: getIssueTypeAttributesWithHierarchy,
                getMaterialTypeReferences: getMaterialTypeReferences,
                getItemTypeAttributesRequiredTrue: getItemTypeAttributesRequiredTrue,
                getItemTypeAttributesRequiredFalse: getItemTypeAttributesRequiredFalse,
                getReceiveTypeReferences: getReceiveTypeReferences,
                getIssueTypeReferences: getIssueTypeReferences,
                getReceiveTypeAttributesByObjectType: getReceiveTypeAttributesByObjectType,
                getIssueTypeAttributesByObjectType: getIssueTypeAttributesByObjectType,
                getMaterialTypeAttributesByObjectType: getMaterialTypeAttributesByObjectType,
                getMachineTypeReferences: getMachineTypeReferences,
                getManpowerTypeReferences: getManpowerTypeReferences
            };

            function getClassificationTree() {
                var url = "api/is/itemtypes/tree";
                return httpFactory.get(url);
            }

            function createItemType(itemType) {
                var url = "api/is/itemtypes";
                return httpFactory.post(url, itemType);
            }

            function getItemType(typeId) {
                var url = "api/is/itemtypes/" + typeId;
                return httpFactory.get(url);
            }

            function updateItemType(itemType) {
                var url = "api/is/itemtypes/" + itemType.id;
                return httpFactory.put(url, itemType);
            }

            function deleteItemType(typeId) {
                var url = "api/is/itemtypes/" + typeId;
                return httpFactory.delete(url);
            }

            function getChildren(typeId) {
                var url = "api/is/itemtypes/" + typeId + "/children";
                return httpFactory.get(url);
            }

            function getAttributes(typeId) {
                var url = "api/is/itemtypes/" + typeId + "/attributes";
                return httpFactory.get(url);
            }

            function getAttributesWithHierarchy(typeId) {
                var url = "api/is/itemtypes/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getAttribute(typeId, attributeId) {
                var url = "api/is/itemtypes/" + typeId + "/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function createAttribute(typeId, attribute) {
                var url = "api/is/itemtypes/" + typeId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateAttribute(typeId, attribute) {
                var url = "api/is/itemtypes/" + typeId + "/attributes/" + attribute.id;
                return httpFactory.put(url, attribute);
            }

            function deleteAttribute(typeId, attributeId) {
                var url = "api/is/itemtypes/" + typeId + "/attributes/" + attributeId;
                return httpFactory.delete(url);
            }

            function getLifeCycles() {
                var url = 'api/is/lifecycles';
                return httpFactory.get(url);
            }

            function getLifeCyclesPhases() {
                var url = 'api/is/lifecycles/phases';
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

            function getItemTypesByIds(ids) {
                var url = "api/is/itemtypes/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMaterialTypesByIds(ids) {
                var url = "api/is/itemtypes/materialType/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMachineTypesByIds(ids) {
                var url = "api/is/itemtypes/machineType/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getManpowerTypesByIds(ids) {
                var url = "api/is/itemtypes/manpowerType/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMachineAttributesWithHierarchy(typeId) {
                var url = "api/is/itemtypes/" + typeId + "/machine/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getMaterialAttributesWithHierarchy(typeId) {
                var url = "api/is/itemtypes/" + typeId + "/material/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getManpowerAttributesWithHierarchy(typeId) {
                var url = "api/is/itemtypes/" + typeId + "/manpower/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getReceiveTypeAttributesWithHierarchy(typeId) {
                var url = "api/is/itemtypes/" + typeId + "/receiveType/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getIssueTypeAttributesByObjectType(objectType) {
                var url = "api/is/itemtypes/" + objectType + "/stockIssueType/attributes";
                return httpFactory.get(url);
            }

            function getReceiveTypeAttributesByObjectType(objectType) {
                var url = "api/is/itemtypes/" + objectType + "/stockReceiveType/attributes";
                return httpFactory.get(url);
            }

            function getIssueTypeAttributesWithHierarchy(typeId) {
                var url = "api/is/itemtypes/" + typeId + "/issueType/attributes?hierarchy=true";
                return httpFactory.get(url);
            }


            function getMaterialReceiveTypeAttributesByIds(ids) {
                var url = "api/is/itemtypes/receiveTypes/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMaterialIssueTypeAttributesByIds(ids) {
                var url = "api/is/itemtypes/issueTypes/multiple/[" + ids + "]";
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

            function getReceiveTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMaterialReceiveTypeAttributesByIds(ids).then(
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

            function getIssueTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMaterialIssueTypeAttributesByIds(ids).then(
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

            function getMaterialTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMaterialTypesByIds(ids).then(
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

            function getMachineTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMachineTypesByIds(ids).then(
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

            function getManpowerTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getManpowerTypesByIds(ids).then(
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

            function getObjectTypeAttributes(objectType) {
                var url = "api/is/itemtypes/attributes/" + objectType;
                return httpFactory.get(url);
            }


            function getItemTypeAttributesRequiredTrue(objectType) {
                var url = "api/is/itemtypes/requiredTrueAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function getItemTypeAttributesRequiredFalse(objectType) {
                var url = "api/is/itemtypes/requiredFalseAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function getMaterialTypeAttributesByObjectType(objectType) {
                var url = "api/is/itemtypes/" + objectType + "/materialType/attributes";
                return httpFactory.get(url);
            }

        }
    }
);