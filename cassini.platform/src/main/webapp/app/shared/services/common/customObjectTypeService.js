define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('CustomObjectTypeService', CustomObjectTypeService);

        function CustomObjectTypeService(httpFactory) {
            var TYPE_BASE_URL = "api/customobjecttypes";


            function createCustomObjectType(type) {
                var url = TYPE_BASE_URL;
                return httpFactory.post(url, type);
            }

            function updateCustomObjectType(type) {
                var url = TYPE_BASE_URL + "/" + type.id;
                return httpFactory.put(url, type);
            }

            function deleteCustomObjectType(type) {
                var url = TYPE_BASE_URL + "/" + type.id;
                return httpFactory.delete(url, type);
            }

            function getCustomObjectTypeClassificationTree() {
                var url = TYPE_BASE_URL + "/tree";
                return httpFactory.get(url);
            }

            function getCustomObjectTypeTree(typeId) {
                var url = TYPE_BASE_URL + "/" + typeId + "/tree";
                return httpFactory.get(url);
            }

            function getAttributes(typeId) {
                var url = TYPE_BASE_URL + "/" + typeId + "/attributes";
                return httpFactory.get(url);
            }

            function createAttribute(typeId, attribute) {
                var url = TYPE_BASE_URL + "/" + typeId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateAttribute(typeId, attribute) {
                var url = TYPE_BASE_URL + "/" + typeId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function deleteAttribute(typeId, attributeId) {
                var url = TYPE_BASE_URL + "/" + typeId + "/attributes/" + attributeId;
                return httpFactory.delete(url);
            }

            function getCustomObjectAttributesWithHierarchy(type) {
                var url = TYPE_BASE_URL + "/" + type + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getCustomObjectTypes() {
                var url = TYPE_BASE_URL + "/types";
                return httpFactory.get(url);
            }

            function getCustomObjectType(id) {
                var url = TYPE_BASE_URL + "/" + id;
                return httpFactory.get(url);
            }

            function getCustomObjectTypeAttributes(objectType, typeId) {
                var url = TYPE_BASE_URL + "/" + objectType + "/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getCustomObjectTypeAttributeValues(objectType, typeId, objectId) {
                var url = TYPE_BASE_URL + "/" + objectType + "/" + typeId + "/" + objectId + "/attributes/values";
                return httpFactory.get(url);
            }

            function getNavigationCustomObjectTypes() {
                var url = TYPE_BASE_URL + "/navigation/types";
                return httpFactory.get(url);
            }

            function getCustomObjectByName(name) {
                var url = TYPE_BASE_URL + "/byname/" + name;
                return httpFactory.get(url);
            }

            return {
                createCustomObjectType: createCustomObjectType,
                updateCustomObjectType: updateCustomObjectType,
                deleteCustomObjectType: deleteCustomObjectType,
                getCustomObjectTypeClassificationTree: getCustomObjectTypeClassificationTree,
                getAttributes: getAttributes,
                createAttribute: createAttribute,
                updateAttribute: updateAttribute,
                deleteAttribute: deleteAttribute,
                getCustomObjectAttributesWithHierarchy: getCustomObjectAttributesWithHierarchy,
                getCustomObjectTypeAttributes: getCustomObjectTypeAttributes,
                getCustomObjectTypeAttributeValues: getCustomObjectTypeAttributeValues,
                getCustomObjectTypes: getCustomObjectTypes,
                getCustomObjectType: getCustomObjectType,
                getCustomObjectTypeTree: getCustomObjectTypeTree,
                getNavigationCustomObjectTypes: getNavigationCustomObjectTypes,
                getCustomObjectByName: getCustomObjectByName
            };
        }
    }
);
