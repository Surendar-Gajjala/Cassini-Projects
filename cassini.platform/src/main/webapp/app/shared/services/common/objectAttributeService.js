define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ObjectAttributeService', ObjectAttributeService);

        function ObjectAttributeService($q, httpFactory) {
            return {
                createObjectAttribute: createObjectAttribute,
                updateObjectAttribute: updateObjectAttribute,
                deleteObjectAttribute: deleteObjectAttribute,
                getObjectAttributeById: getObjectAttributeById,
                getAllObjectAttributes: getAllObjectAttributes,
                saveItemObjectAttributes: saveItemObjectAttributes,
                uploadObjectAttributeImage: uploadObjectAttributeImage,
                getImageAttribute: getImageAttribute,
                getCurrencies: getCurrencies,
                getObjectsByObjectType: getObjectsByObjectType,
                getObjectAttributeByIdAndDef: getObjectAttributeByIdAndDef,
                getAttributeValuesByDef: getAttributeValuesByDef

            };

            function getObjectsByObjectType(objectId, pageable) {
                var url = "api/core/objects/" + objectId + "/byObject?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);

            }

            function createObjectAttribute(objectId, attribute) {
                var url = "api/core/objects/" + objectId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateObjectAttribute(objectId, attribute) {
                var url = "api/core/objects/" + objectId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function deleteObjectAttribute(objectId, attributeId) {
                var url = "api/core/objects/" + objectId + "/attributes/" + attributeId;
                return httpFactory.delete(url);
            }

            function getObjectAttributeById(objectId, attributeId) {
                var url = "api/core/objects/" + objectId + "/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function getAllObjectAttributes(objectId) {
                var url = "api/core/objects/" + objectId + "/attributes";
                return httpFactory.get(url);
            }

            function saveItemObjectAttributes(objectId, attributes) {
                var url = "api/core/objects/" + objectId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function uploadObjectAttributeImage(objectId, attributeId, file) {
                var url = "api/core/objects/" + objectId + "/attributes/" + attributeId + "/uploadImage";
                return httpFactory.upload(url, file);
            }

            function getImageAttribute(objectId, attributeId) {
                var url = "api/core/objects/" + objectId + "/attributes/" + attributeId + "/imageAttribute/download";
                return httpFactory.get(url);
            }

            function getCurrencies() {
                var url = "api/core/currencies";
                return httpFactory.get(url);
            }

            function getObjectAttributeByIdAndDef(objectId, attributeDef) {
                var url = "api/core/objects/" + objectId + "/attributes/value/" + attributeDef;
                return httpFactory.get(url);
            }

            function getAttributeValuesByDef(attributeDef) {
                var url = "api/core/objects/" + null + "/attributes/" + attributeDef + "/values";
                return httpFactory.get(url);
            }


        }
    }
);
