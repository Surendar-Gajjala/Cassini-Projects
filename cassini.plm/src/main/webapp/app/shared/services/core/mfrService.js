define(['app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('MfrService', MfrService);

        function MfrService(httpFactory) {
            return {
                getManufacturers: getManufacturers,
                getManufacturer: getManufacturer,
                createManufacture: createManufacture,
                updateManufacture: updateManufacture,
                deleteManufacture: deleteManufacture,
                freeTextSearch: freeTextSearch,
                getMultipleManufacturers: getMultipleManufacturers,
                getMfrTypeReferences: getMfrTypeReferences,
                getMfrTypesByIds: getMfrTypesByIds,
                getMfrAttributesWithHierarchy: getMfrAttributesWithHierarchy,
                saveMfrAttributes: saveMfrAttributes,
                createMfrAttribute: createMfrAttribute,
                getMfrAttributes: getMfrAttributes,
                updateMfrAttribute: updateMfrAttribute,
                getByName: getByName,
                getMfrsByType: getMfrsByType,
                getAllManufacturerTypesByName: getAllManufacturerTypesByName,
                getMfrAttributeValues: getMfrAttributeValues,
                getAllManufacturers: getAllManufacturers,
                updateMfrImageValue: updateMfrImageValue,
                getMfrCounts: getMfrCounts,
                promoteManufacturer: promoteManufacturer,
                demoteManufacturer: demoteManufacturer,
                attachWorkflow: attachWorkflow,
                deleteWorkflow: deleteWorkflow,
                getWorkflows: getWorkflows,
                getMfrType: getMfrType,
                getMfrPartType: getMfrPartType,
                getMfrsByTypeId: getMfrsByTypeId,
                getMfrTypeTree: getMfrTypeTree

            };

            function getAllManufacturerTypesByName(name) {
                var url = "api/plm/mfr/ByName/" + name;
                return httpFactory.get(url);
            }

            function createMfrAttribute(mfrId, attribute) {
                var url = "api/plm/mfr/" + mfrId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateMfrAttribute(mfrId, attribute) {
                var url = "api/plm/mfr/" + mfrId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function freeTextSearch(pageable, freeText) {
                var url = "api/plm/mfr/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function getAllManufacturers() {
                var url = "api/plm/mfr/all";
                return httpFactory.get(url);
            }

            function getManufacturers(pageable,filters) {

                var url = "api/plm/mfr?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                
                url += "&searchQuery={0}&type={1}".
                    format(filters.searchQuery,filters.type);
                return httpFactory.get(url);
            }

            function getManufacturer(mfrId) {
                var url = "api/plm/mfr/" + mfrId;
                return httpFactory.get(url);
            }

            function getMultipleManufacturers(mftIds) {
                var url = "api/plm/mfr/multiple/[" + mftIds + "]";
                return httpFactory.get(url);
            }

            function createManufacture(obj) {
                var url = "api/plm/mfr";
                return httpFactory.post(url, obj);
            }

            function updateManufacture(manufacturer) {
                var url = "api/plm/mfr/" + manufacturer.id;
                return httpFactory.put(url, manufacturer);
            }

            function deleteManufacture(manufacturerId) {
                var url = "api/plm/mfr/" + manufacturerId;
                return httpFactory.delete(url);
            }

            function getByName(name) {
                var url = "api/plm/mfr/byName/" + name;
                return httpFactory.get(url);
            }

            function getMfrTypesByIds(ids) {
                var url = "api/plm/mfr/mfrTypes/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMfrAttributesWithHierarchy(type) {
                var url = "api/plm/mfr/mfrTypes/" + type + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function saveMfrAttributes(mfrId, attributes) {
                var url = "api/plm/mfr/" + mfrId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getMfrAttributes(mfrId) {
                var url = "api/plm/mfr/" + mfrId + "/attributes";
                return httpFactory.get(url);
            }

            function getMfrsByType(typeId, pageable) {
                var url = "api/plm/mfr/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMfrTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMfrTypesByIds(ids).then(
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

            function getMfrAttributeValues(attributeId) {
                var url = "api/plm/mfr/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function updateMfrImageValue(objectId, attributeId, file) {
                var url = "api/plm/mfr/updateImageValue/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function getMfrCounts(mfrId) {
                var url = "api/plm/mfr/" + mfrId + "/count";
                return httpFactory.get(url);
            }

            function promoteManufacturer(mfrId, manufacturer) {
                var url = "api/plm/mfr/" + mfrId + "/promote";
                return httpFactory.put(url, manufacturer);
            }

            function demoteManufacturer(mfrId, manufacturer) {
                var url = "api/plm/mfr/" + mfrId + "/demote";
                return httpFactory.put(url, manufacturer);
            }

            function attachWorkflow(reqId, wfId) {
                var url = "api/plm/mfr/" + reqId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteWorkflow(reqId) {
                var url = 'api/plm/mfr/' + reqId + "/workflow/delete";
                return httpFactory.delete(url);
            }

            function getWorkflows(typeId, type) {
                var url = "api/plm/mfr/workflow/" + typeId + "/mfrType/" + type;
                return httpFactory.get(url);
            }

            function getMfrType(type) {
                var url = "api/plm/mfr/mfrType/" + type;
                return httpFactory.get(url)
            }

            function getMfrPartType(type) {
                var url = "api/plm/mfr/part/type/" + type;
                return httpFactory.get(url)
            }

            function getMfrsByTypeId(pageable, filters) {
                var url = "api/plm/mfr/mfrtype?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&type={1}".format(filters.searchQuery, filters.type);
                return httpFactory.get(url);
            }

            function getMfrTypeTree() {
                var url = "api/plm/mfr/type/tree";
                return httpFactory.get(url);
            }

        }
    }
);
