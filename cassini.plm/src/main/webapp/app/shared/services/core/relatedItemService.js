define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('RelatedItemService', RelatedItemService);

        function RelatedItemService(httpFactory) {
            return {
                createRelatedItem: createRelatedItem,
                updateRelatedItem: updateRelatedItem,
                deleteRelatedItem: deleteRelatedItem,
                getRelatedItem: getRelatedItem,
                getAllRelatedItems: getAllRelatedItems,
                getRelatedItemByRelationship: getRelatedItemByRelationship,
                getRelatedItemsByItem: getRelatedItemsByItem,
                createMultipleRelatedItems: createMultipleRelatedItems,
                createRelatedItemAttributes: createRelatedItemAttributes,
                getRelatedItemAttributes: getRelatedItemAttributes,
                createRelatedItemAttribute: createRelatedItemAttribute,
                updateRelatedItemAttribute: updateRelatedItemAttribute,
                getRelatedItems: getRelatedItems,

                getObjectRelatedItems: getObjectRelatedItems,
                deleteObjectRelatedItem: deleteObjectRelatedItem
            };

            function createRelatedItem(relatedItem) {
                var url = "api/plm/relatedItems";
                return httpFactory.post(url, relatedItem);
            }

            function updateRelatedItem(relatedItem) {
                var url = "api/plm/relatedItems/" + relatedItem.id;
                return httpFactory.put(url, relatedItem);
            }

            function deleteRelatedItem(relatedItem) {
                var url = "api/plm/relatedItems/" + relatedItem.id;
                return httpFactory.delete(url);
            }

            function getRelatedItem(relatedItem) {
                var url = "api/plm/relatedItems/" + relatedItem.id;
                return httpFactory.get(url);
            }

            function getAllRelatedItems() {
                var url = "api/plm/relatedItems";
                return httpFactory.get(url);
            }

            function getRelatedItemByRelationship(relationshipId) {
                var url = "api/plm/relatedItems/relationship/" + relationshipId;
                return httpFactory.get(url);
            }

            function getRelatedItemsByItem(item) {
                var url = "api/plm/relatedItems/item/" + item.id;
                return httpFactory.get(url);
            }

            function getRelatedItems(item) {
                var url = "api/plm/relatedItems/" + item.id + "/items";
                return httpFactory.get(url);
            }

            function createMultipleRelatedItems(relatedItems) {
                var url = "api/plm/relatedItems/multiple";
                return httpFactory.post(url, relatedItems);
            }

            function createRelatedItemAttributes(relatedItemId, attributes) {
                var url = "api/plm/relatedItems/" + relatedItemId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function createRelatedItemAttribute(relatedItemId, attribute) {
                var url = "api/plm/relatedItems/" + relatedItemId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateRelatedItemAttribute(relatedItemId, attributes) {
                var url = "api/plm/relatedItems/" + relatedItemId + "/attributes";
                return httpFactory.put(url, attributes);
            }

            function getRelatedItemAttributes(relatedItemId) {
                var url = "api/plm/relatedItems/" + relatedItemId + "/attributes";
                return httpFactory.get(url);
            }

            function getObjectRelatedItems(objectId,objectType){
                var url = "api/plm/relatedItems/" + objectId + "/items/object?objectType=" + objectType;
                return httpFactory.get(url);
            }
            function deleteObjectRelatedItem(objectId,objectType){
                var url = "api/plm/relatedItems/object/" + objectId + "/item?objectType=" + objectType;
                return httpFactory.delete(url);
            }
        }
    }
);