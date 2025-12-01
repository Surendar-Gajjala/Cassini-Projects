define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ItemTypeService', ItemTypeService);

        function ItemTypeService(httpFactory) {
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
            };

            function getClassificationTree() {
                var url = "api/pdm/itemTypes/tree";
                return httpFactory.get(url);
            }

            function createItemType(itemType) {
                var url = "api/pdm/itemTypes";
                return httpFactory.post(url, itemType);
            }

            function getItemType(typeId) {
                var url = "api/pdm/itemTypes/" + typeId;
                return httpFactory.get(url);
            }

            function updateItemType(itemType) {
                var url = "api/pdm/itemTypes/" + itemType.id;
                return httpFactory.put(url, itemType);
            }

            function deleteItemType(typeId) {
                var url = "api/pdm/itemTypes/" + typeId;
                return httpFactory.delete(url);
            }

            function getChildren(typeId) {
                var url = "api/pdm/itemTypes/" + typeId + "/children";
                return httpFactory.get(url);
            }

            function getAttributes(typeId) {
                var url = "api/pdm/itemTypes/" + typeId + "/attributes";
                return httpFactory.get(url);
            }

            function getAttributesWithHierarchy(typeId) {
                var url = "api/pdm/itemTypes/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getAttribute(typeId, attributeId) {
                var url = "api/pdm/itemTypes/" + typeId + "/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function createAttribute(typeId, attribute) {
                var url = "api/pdm/itemTypes/" + typeId + "/attribute";
                return httpFactory.post(url, attribute);
            }

            function updateAttribute(typeId, attribute) {
                var url = "api/pdm/itemTypes/" + typeId + "/attribute/" + attribute.id;
                return httpFactory.put(url, attribute);
            }

            function deleteAttribute(typeId, attributeId) {
                var url = "api/pdm/itemTypes/" + typeId + "/attribute/" + attributeId;
                return httpFactory.delete(url);
            }

        }
    }
);