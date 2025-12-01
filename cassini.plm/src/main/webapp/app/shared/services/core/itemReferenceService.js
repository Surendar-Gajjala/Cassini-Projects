define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ItemReferenceService', ItemReferenceService);

        function ItemReferenceService(httpFactory) {
            return {
                getItemReferences: getItemReferences,
                getItemReference: getItemReference,
                createItemReference: createItemReference,
                updateItemReference: updateItemReference,
                deleteItemReference: deleteItemReference

            };

            function getItemReferences(itemId) {
                var url = 'api/plm/items/' + itemId + "/references";
                return httpFactory.get(url);
            }
            function getItemReference(itemId ,refid) {
                var url = 'api/plm/items/' + itemId + "/references/"+refid;
                return httpFactory.get(url);
            }

            function createItemReference(itemId,referenceItem) {
                var url = 'api/plm/items/' + itemId + "/references";
                return httpFactory.post(url, referenceItem);
            }
            function updateItemReference(itemId,referenceItem ,refid) {
                var url = 'api/plm/items/' + itemId + "/references/"+refid;
                return httpFactory.put(url, referenceItem);
            }
            function deleteItemReference(itemId ,referenceItem,refid) {
                var url = 'api/plm/items/' + itemId + "/references/"+refid;
                return httpFactory.delete(url, referenceItem);
            }


        }
    }
);