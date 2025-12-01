/**
 * Created by Rajabrahmachary on 19-09-2018.
 */
define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('CustomIndentService', CustomIndentService);

        function CustomIndentService(httpFactory) {
            return {
                createIndent: createIndent,
                updateIndent: updateIndent,
                updateIndentItem: updateIndentItem,
                deleteIndentItem: deleteIndentItem,
                getIndent: getIndent,
                getPageableIndentsByStore: getPageableIndentsByStore,
                getPageableIndents: getPageableIndents,
                getRequiredIndentAttributes: getRequiredIndentAttributes,
                getAttributesByIndentIdsAndAttributeId: getAttributesByIndentIdsAndAttributeId,
                customIndentFreeTextSearch: customIndentFreeTextSearch

            };


            /* methods for attributes of indent*/

            function getRequiredIndentAttributes(objectType) {
                var url = "api/is/stores/indents/requiredIndentAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function getAttributesByIndentIdsAndAttributeId(indentIds, attributeIds) {
                var url = "api/is/stores/indents/objectAttributes";
                return httpFactory.post(url, [indentIds, attributeIds]);
            }

            /* end methods for attributes of indent*/

            function deleteIndentItem(indentId, indentItemId) {
                var url = "api/is/stores/indents/" + indentId + "/customIndentItem/" + indentItemId;
                return httpFactory.delete(url);
            }

            function createIndent(indent) {
                var url = "api/is/stores/indents";
                return httpFactory.post(url, indent);
            }

            function updateIndent(indent) {
                var url = "api/is/stores/indents/" + indent.id;
                return httpFactory.put(url, indent);
            }

            function updateIndentItem(indent,indentItem) {
                var url = "api/is/stores/indents/"+ indent.id + "/customIndentItem/" + indentItem.id;
                return httpFactory.put(url, indentItem);
            }

            function getIndent(indentId) {
                var url = "api/is/stores/indents/" + indentId;
                return httpFactory.get(url);
            }

            function getPageableIndentsByStore(pageable,storeId) {
                var url = "api/is/stores/indents/byStore/" + storeId + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getPageableIndents(pageable) {
                var url = "api/is/stores/indents/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field);
                return httpFactory.get(url);
            }

            function customIndentFreeTextSearch(pageable, freeText) {
                var url = "api/is/stores/indents/freesearch?page={0}&size={1}&sort={2}".
                    format(pageable.page, pageable.size, pageable.sort.field);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }
        }
    }
);