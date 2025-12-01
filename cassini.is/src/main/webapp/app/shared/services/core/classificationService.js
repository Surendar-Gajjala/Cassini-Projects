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
                createTypeAttribute: createTypeAttribute,
                updateTypeAttribute: updateTypeAttribute,
                deleteTypeAttribute: deleteTypeAttribute
            };

            function getClassificationTree(type) {
                var url = "api/classification/{0}".format(type);
                return httpFactory.get(url);
            }

            function createType(type, itemType) {
                var url = "api/classification/{0}".format(type);
                return httpFactory.post(url, itemType);
            }

            function getType(type, typeId) {
                var url = "api/classification/{0}/{1}".format(type, typeId);
                return httpFactory.get(url);
            }

            function updateType(type, itemType) {
                var url = "api/classification/{0}/{1}".format(type, itemType.id);
                return httpFactory.put(url, itemType);
            }

            function deleteType(type, typeId) {
                var url = "api/classification/{0}/{1}".format(type, typeId);
                return httpFactory.delete(url);
            }

            function getChildren(type, typeId) {
                var url = "api/classification/{0}/{1}/children".format(type, typeId);
                return httpFactory.get(url);
            }

            function getAttributes(type, typeId) {
                var url = "api/classification/{0}/{1}/attributes".format(type, typeId);
                return httpFactory.get(url);
            }

            function getAttributesWithHierarchy(type, typeId) {
                var url = "api/classification/{0}/{1}/attributes?hierarchy=true".format(type, typeId);
                return httpFactory.get(url);
            }

            function getAttribute(type, typeId, attributeId) {
                var url = "api/classification/{0}/{1}/attributes/{2}".format(type, typeId, attributeId);
                return httpFactory.get(url);
            }

            function createTypeAttribute(type, typeId, attribute) {
                var url = "api/classification/{0}/{1}/attributes".format(type, typeId);
                return httpFactory.post(url, attribute);
            }

            function updateTypeAttribute(type, typeId, attribute) {
                var url = "api/classification/{0}/{1}/attributes/{2}".format(type, typeId, attribute.id);
                return httpFactory.put(url, attribute);
            }

            function deleteTypeAttribute(type, typeId, attributeId) {
                var url = "api/classification/{0}/{1}/attributes/{2}".format(type, typeId, attributeId);
                return httpFactory.delete(url);
            }
        }
    }
);