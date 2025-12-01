define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('CustomObjectService', CustomObjectService);

        function CustomObjectService(httpFactory) {
            var TYPE_BASE_URL = "api/customobjects";

            function createCustomObject(customObject) {
                var url = TYPE_BASE_URL;
                return httpFactory.post(url, customObject);
            }

            function updateCustomObject(id, customObject) {
                var url = TYPE_BASE_URL + "/" + id;
                return httpFactory.put(url, customObject);
            }

            function deleteCustomObject(id) {
                var url = TYPE_BASE_URL + "/" + id;
                return httpFactory.delete(url);
            }

            function getCustomObject(id) {
                var url = TYPE_BASE_URL + "/" + id;
                return httpFactory.get(url);
            }

            function sendNotification(customObject) {
                var url = TYPE_BASE_URL + "/" + customObject.id + "/notification";
                return httpFactory.post(url, customObject);
            }

            function getAllCustomObjects() {
                var url = TYPE_BASE_URL + "/all";
                return httpFactory.get(url);
            }

            function getCustomObjectsBySupplier(id) {
                var url = TYPE_BASE_URL + "/supplier/" + id;
                return httpFactory.get(url);
            }

            function getCustomObjectsByTypeAndSupplier(type, id) {
                var url = TYPE_BASE_URL + "/type/" + type + "/supplier/" + id;
                return httpFactory.get(url);
            }

            function getCustomObjects(pageable, filters) {
                var url = TYPE_BASE_URL + "/objects?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&searchQuery={1}&number={2}&name={3}&object={4}&bomObject={5}&related={6}&customType={7}&supplier={8}".
                    format(filters.type, filters.searchQuery, filters.number, filters.name, filters.object, filters.bomObject, filters.related, filters.customType, filters.supplier);
                return httpFactory.get(url);
            }

            function updateAttributeAttachmentValues(objectType, objectId, attributeId, file) {
                var url = TYPE_BASE_URL + "/" + objectType + "/" + objectId + "/" + attributeId + "/attachments/upload";
                return httpFactory.uploadMultiple(url, file);
            }

            function updateAttributeImageValue(objectType, objectId, attributeId, file) {
                var url = TYPE_BASE_URL + "/" + objectType + "/" + objectId + "/" + attributeId + "/images/upload";
                return httpFactory.upload(url, file);
            }


            function updateCustomAttribute(objectId, attribute) {
                var url = TYPE_BASE_URL + "/" + objectId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function createMultipleCustomObjectBom(objectId, bomList) {
                var url = TYPE_BASE_URL + "/" + objectId + "/bom/multiple";
                return httpFactory.post(url, bomList);
            }

            function createCustomObjectBom(objectId, bom) {
                var url = TYPE_BASE_URL + "/" + objectId + "/bom";
                return httpFactory.post(url, bom);
            }

            function updateCustomObjectBom(objectId, bom) {
                var url = TYPE_BASE_URL + "/" + objectId + "/bom/" + bom.id;
                return httpFactory.put(url, bom);
            }

            function deleteCustomObjectBom(objectId, bomId) {
                var url = TYPE_BASE_URL + "/" + objectId + "/bom/" + bomId;
                return httpFactory.delete(url);
            }

            function getCustomObjectBom(objectId, bomId) {
                var url = TYPE_BASE_URL + "/" + objectId + "/bom/" + bomId;
                return httpFactory.get(url);
            }

            function getAllCustomObjectBom(objectId, hierarchy) {
                var url = TYPE_BASE_URL + "/" + objectId + "/bom?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }


            function getAllRelatedCustomObjects(objectId) {
                var url = TYPE_BASE_URL + "/" + objectId + "/relatedobjects";
                return httpFactory.get(url);
            }

            function createRelatedMultipleCustomObject(objectId, objects) {
                var url = TYPE_BASE_URL + "/" + objectId + "/related/multiple";
                return httpFactory.post(url, objects);
            }

            function createRelatedCustomObject(objectId, object) {
                var url = TYPE_BASE_URL + "/" + objectId + "/related";
                return httpFactory.post(url, object);
            }

            function updateRelatedCustomObject(objectId, object) {
                var url = TYPE_BASE_URL + "/" + objectId + "/related/" + object.id;
                return httpFactory.put(url, object);
            }

            function deleteRelatedCustomObject(objectId, relatedId) {
                var url = TYPE_BASE_URL + "/" + objectId + "/related/" + relatedId;
                return httpFactory.delete(url);
            }

            function getCustomObjectsByType(typeId) {
                var url = TYPE_BASE_URL + "/type/" + typeId;
                return httpFactory.get(url);
            }

            function getCustomObjectWhereUsed(objectId, hierarchy) {
                var url = TYPE_BASE_URL + "/" + objectId + "/whereused?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }


            function searchCustomObject(pageable, criteria) {
                var url = TYPE_BASE_URL + "/simpleSearch?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                if (criteria.type == null) {
                    url += "&number={0}&name={1}&description={2}".format(criteria.number, criteria.name, criteria.description);
                } else {
                    url += "&number={0}&name={1}&description={2}&type={3}".format(criteria.number, criteria.name, criteria.description, criteria.type);
                }
                return httpFactory.get(url);
            }

            function advancedSearchCustomObject(pageable, criteria, typeId) {
                var url = TYPE_BASE_URL + "/advancedsearch/" + typeId + "?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                return httpFactory.post(url, criteria);
            }

            return {
                createCustomObject: createCustomObject,
                updateCustomObject: updateCustomObject,
                deleteCustomObject: deleteCustomObject,
                getAllCustomObjects: getAllCustomObjects,
                getCustomObject: getCustomObject,
                updateAttributeAttachmentValues: updateAttributeAttachmentValues,
                updateAttributeImageValue: updateAttributeImageValue,
                updateCustomAttribute: updateCustomAttribute,
                getCustomObjects: getCustomObjects,

                createMultipleCustomObjectBom: createMultipleCustomObjectBom,
                createCustomObjectBom: createCustomObjectBom,
                updateCustomObjectBom: updateCustomObjectBom,
                getCustomObjectBom: getCustomObjectBom,
                getAllCustomObjectBom: getAllCustomObjectBom,
                deleteCustomObjectBom: deleteCustomObjectBom,

                getAllRelatedCustomObjects: getAllRelatedCustomObjects,
                createRelatedMultipleCustomObject: createRelatedMultipleCustomObject,
                createRelatedCustomObject: createRelatedCustomObject,
                updateRelatedCustomObject: updateRelatedCustomObject,
                deleteRelatedCustomObject: deleteRelatedCustomObject,
                getCustomObjectsByType: getCustomObjectsByType,
                searchCustomObject: searchCustomObject,
                advancedSearchCustomObject: advancedSearchCustomObject,
                getCustomObjectWhereUsed: getCustomObjectWhereUsed,
                getCustomObjectsBySupplier: getCustomObjectsBySupplier,
                getCustomObjectsByTypeAndSupplier: getCustomObjectsByTypeAndSupplier,
                sendNotification: sendNotification
            };
        }
    }
);
