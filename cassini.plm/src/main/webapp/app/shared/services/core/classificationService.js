define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ClassificationService', ClassificationService);

        function ClassificationService(httpFactory) {
            return {
                getClassificationTree: getClassificationTree,
                createType: createType,
                getType: getType,
                updateType: updateType,
                deleteType: deleteType,
                getChildren: getChildren,

                getAttributes: getAttributes,
                getAttributesWithHierarchy: getAttributesWithHierarchy,
                getAttribute: getAttribute,
                createAttribute: createAttribute,
                updateAttribute: updateAttribute,
                deleteAttribute: deleteAttribute,
                getExclusionAttributes: getExclusionAttributes,
                getConfigurableAttributes: getConfigurableAttributes,
                getAllClassificationTree: getAllClassificationTree,
                getAssignedObjectType: getAssignedObjectType,
                getObjectTypeAttributesWithHierarchy: getObjectTypeAttributesWithHierarchy,
                getObjectAttributeValues: getObjectAttributeValues,
                updateAttributeAttachmentValues: updateAttributeAttachmentValues,
                updateAttributeImageValue: updateAttributeImageValue,
                getCustomObjectAttributes: getCustomObjectAttributes
            };

            function getClassificationTree(type) {
                var url = "api/plm/classification/{0}".format(type);
                return httpFactory.get(url);
            }

            function getAllClassificationTree() {
                var url = "api/plm/classification/" + null + "/tree/all";
                return httpFactory.get(url);
            }

            function createType(type, itemType) {
                var url = "api/plm/classification/{0}".format(type);
                return httpFactory.post(url, itemType);
            }

            function getType(type, typeId) {
                var url = "api/plm/classification/{0}/{1}".format(type, typeId);
                return httpFactory.get(url);
            }

            function updateType(type, itemType) {
                var url = "api/plm/classification/{0}/{1}".format(type, itemType.id);
                return httpFactory.put(url, itemType);
            }

            function deleteType(type, typeId) {
                var url = "api/plm/classification/{0}/{1}".format(type, typeId);
                return httpFactory.delete(url);
            }

            function getChildren(type, typeId) {
                var url = "api/plm/classification/{0}/{1}/children".format(type, typeId);
                return httpFactory.get(url);
            }

            function getAttributes(type, typeId) {
                var url = "api/plm/classification/{0}/{1}/attributes".format(type, typeId);
                return httpFactory.get(url);
            }

            function getAttributesWithHierarchy(type, typeId) {
                var url = "api/plm/classification/{0}/{1}/attributes?hierarchy=true".format(type, typeId);
                return httpFactory.get(url);
            }

            function getObjectTypeAttributesWithHierarchy(type, typeId, objectId) {
                var url = "api/plm/classification/{0}/{1}/{2}/attributes?hierarchy=true".format(type, typeId, objectId);
                return httpFactory.get(url);
            }

            function getObjectAttributeValues(type, typeId, objectId) {
                var url = "api/plm/classification/{0}/{1}/{2}/attributes/values".format(type, typeId, objectId);
                return httpFactory.get(url);
            }

            function updateAttributeAttachmentValues(objectType, objectId, attributeId, file) {
                var url = "api/plm/classification/" + objectType + "/" + objectId + "/" + attributeId + "/attachments/upload";
                return httpFactory.uploadMultiple(url, file);
            }

            function updateAttributeImageValue(objectType, objectId, attributeId, file) {
                var url = "api/plm/classification/" + objectType + "/" + objectId + "/" + attributeId + "/images/upload";
                return httpFactory.upload(url, file);
            }

            function getAttribute(type, typeId, attributeId) {
                var url = "api/plm/classification/{0}/{1}/attributes/{2}".format(type, typeId, attributeId);
                return httpFactory.get(url);
            }

            function createAttribute(type, typeId, attribute) {
                var url = "api/plm/classification/{0}/{1}/attributes".format(type, typeId);
                return httpFactory.post(url, attribute);
            }

            function updateAttribute(type, typeId, attribute) {
                var url = "api/plm/classification/{0}/{1}/attributes/{2}".format(type, typeId, attribute.id);
                return httpFactory.put(url, attribute);
            }

            function deleteAttribute(type, typeId, attributeId) {
                var url = "api/plm/classification/{0}/{1}/attributes/{2}".format(type, typeId, attributeId);
                return httpFactory.delete(url);
            }

            function getExclusionAttributes(type, typeId) {
                var url = "api/plm/classification/{0}/{1}/exclusion".format(type, typeId);
                return httpFactory.get(url);
            }

            function getConfigurableAttributes(type, typeId) {
                var url = "api/plm/classification/{0}/{1}/configurable/attributes".format(type, typeId);
                return httpFactory.get(url);
            }

            function getAssignedObjectType(type, assignedId, assignedType) {
                var url = "api/plm/classification/{0}/{1}/assigned/{2}".format(type, assignedId, assignedType);
                return httpFactory.get(url);
            }

            function getCustomObjectAttributes(typeId) {
                var url = "api/plm/customobjects/type/{0}/attributes".format(typeId);
                return httpFactory.get(url);
            }

        }
    }
);