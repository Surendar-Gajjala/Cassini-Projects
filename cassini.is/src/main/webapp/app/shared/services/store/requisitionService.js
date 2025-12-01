/**
 * Created by swapna on 19/09/18.
 */
define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('RequisitionService', RequisitionService);

        function RequisitionService(httpFactory) {
            return {
                createRequisition: createRequisition,
                getAllRequisitions: getAllRequisitions,
                getPageableRequisitions: getPageableRequisitions,
                getAllPageableRequisitions: getAllPageableRequisitions,
                requisitionFreeTextSearch: requisitionFreeTextSearch,
                updateRequisition: updateRequisition,
                getPageableRequisitionsByProject: getPageableRequisitionsByProject,
                getRequisitionItems: getRequisitionItems,
                getPagedRequisitionItems: getPagedRequisitionItems,
                getRequisition: getRequisition,
                createRequisitionItems: createRequisitionItems,
                getRequiredRequisitionAttributes: getRequiredRequisitionAttributes,
                getAttributesByRequisitionIdsAndAttributeId: getAttributesByRequisitionIdsAndAttributeId,
                printRequisitionChallan: printRequisitionChallan,
                findNonRequisitionItems: findNonRequisitionItems
            };

            function getRequiredRequisitionAttributes(objectType) {
                var url = "api/is/stores/requisitions/requiredIndentAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function getAttributesByRequisitionIdsAndAttributeId(requisitionIds, attributeIds) {
                var url = "api/is/stores/requisitions/objectAttributes";
                return httpFactory.post(url, [requisitionIds, attributeIds]);
            }
            
            function createRequisition(storeId, requisition) {
                var url = "api/is/stores/" + storeId + "/requisitions";
                return httpFactory.post(url, requisition)
            }

            function getRequisition(storeId, requisitionId) {
                var url = "api/is/stores/" + storeId + "/requisitions/" + requisitionId;
                return httpFactory.get(url)
            }
            
            function getAllRequisitions(storeId) {
                var url = "api/is/stores/" + storeId + "/requisitions";
                return httpFactory.get(url)
            }

            function getAllPageableRequisitions(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/requisitions/all?page={0}&size={1}&sort={2}".
                        format(pageable.page, pageable.size, pageable.sort.field);
                return httpFactory.get(url)
            }

            function getPageableRequisitions(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/requisitions/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url)
            }

            function requisitionFreeTextSearch(storeId, pageable, freeText) {
                var url = "api/is/stores/" + storeId + "/requisitions/freesearch?page={0}&size={1}&sort={2}".
                    format(pageable.page, pageable.size, pageable.sort.field);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function updateRequisition(storeId, requisition) {
                var url = "api/is/stores/" + storeId + "/requisitions/" + requisition.id ;
                return httpFactory.put(url, requisition)
            }

            function getPageableRequisitionsByProject(storeId, projectId, pageable) {
                var url = "api/is/stores/" + storeId + "/requisitions/" + projectId + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url)
            }

            function getRequisitionItems(storeId, requisitionId) {
                var url = "api/is/stores/" + storeId + "/requisitions/" + requisitionId + "/customRequisitionItems";
                return httpFactory.get(url)
            }

            function getPagedRequisitionItems(storeId, requisitionId, pageable) {
                var url = "api/is/stores/" + storeId + "/requisitions/" + requisitionId + "/customRequisitionItems/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url)
            }


            function createRequisitionItems(storeId, requisitionId, customRequisitionItems) {
                var url = "api/is/stores/" + storeId + "/requisitions/" + requisitionId + "/customRequisitionItem" ;
                return httpFactory.post(url, customRequisitionItems)
            }

            function printRequisitionChallan(customer, storeId, requisitionId) {
                var url = "api/is/stores/" + storeId + "/requisitions/" + requisitionId + "/printRequisitionChallan?customer= " + customer;
                return httpFactory.get(url);
            }

            function findNonRequisitionItems(storeId, requisitionId, projectId) {
                var url = "api/is/stores/" + storeId + "/requisitions/" + requisitionId + "/project/" + projectId;
                return httpFactory.get(url)
            }

        }
    }
);