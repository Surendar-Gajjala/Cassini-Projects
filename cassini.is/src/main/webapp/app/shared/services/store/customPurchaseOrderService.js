/**
 * Created by SRAVAN on 9/19/2018.
 */
define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('CustomPurchaseOrderService', CustomPurchaseOrderService);

        function CustomPurchaseOrderService(httpFactory) {
            return {
                createPurchaseOrder: createPurchaseOrder,
                updateCustomPurchaseOrder: updateCustomPurchaseOrder,
                updateCustomPurchaseOrderItem: updateCustomPurchaseOrderItem,
                getPurchaseOrder: getPurchaseOrder,
                getPageablePurchaseOrders: getPageablePurchaseOrders,
                getAllPurchaseOrders: getAllPurchaseOrders,
                deletePurchaseItem: deletePurchaseItem,
                getSupplierByPurchaseOrderId: getSupplierByPurchaseOrderId,
                getPurchaseOrderReferences: getPurchaseOrderReferences,
                purchaseOrderFreeTextSearch: purchaseOrderFreeTextSearch
            };

            function createPurchaseOrder(purchaseOrder) {
                var url = "api/is/stores/purchaseOrders";
                return httpFactory.post(url, purchaseOrder);
            }

            function updateCustomPurchaseOrder(purchaseOrder) {
                var url = "api/is/stores/purchaseOrders/" + purchaseOrder.id;
                return httpFactory.put(url, purchaseOrder);
            }

            function updateCustomPurchaseOrderItem(purchaseOrder, purchaseOrderItem) {
                var url = "api/is/stores/purchaseOrders/" + purchaseOrder.id + "/purchaseOrderItem/" + purchaseOrderItem.id;
                return httpFactory.put(url, purchaseOrderItem);
            }

            function getPurchaseOrder(purchaseOrderId) {
                var url = "api/is/stores/purchaseOrders/" + purchaseOrderId;
                return httpFactory.get(url);
            }

            function getPageablePurchaseOrders(storeId, pageable) {
                var url = "api/is/stores/purchaseOrders/byStore/" + storeId + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllPurchaseOrders(pageable) {
                var url = "api/is/stores/purchaseOrders/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field);
                return httpFactory.get(url);
            }

            function deletePurchaseItem(purchaseId, purchaseItemId) {
                var url = "api/is/stores/purchaseOrders/" + purchaseId + "/purchaseOrderItem/" + purchaseItemId;
                return httpFactory.delete(url);
            }

            function getSupplierByPurchaseOrderId(purchaseId) {
                var url = "api/is/stores/purchaseOrders/" + purchaseId + "/supplier/" ;
                return httpFactory.get(url);
            }

            function getPurchaseOrdersByIds(poIds) {
                var url = "api/is/stores/purchaseOrders/multiple/" + poIds;
                return httpFactory.get(url);
            }

            function purchaseOrderFreeTextSearch(pageable, freeText) {
                var url = "api/is/stores/purchaseOrders/freesearch?page={0}&size={1}&sort={2}".
                    format(pageable.page, pageable.size, pageable.sort.field);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function getPurchaseOrderReferences(objects, property) {
                var poIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && poIds.indexOf(object[property]) == -1) {
                        poIds.push(object[property]);
                    }
                });

                if (poIds.length > 0) {
                    getPurchaseOrdersByIds(poIds).then(
                        function (purchaseOrders) {
                            var map = new Hashtable();
                            angular.forEach(purchaseOrders, function (purchaseOrder) {
                                map.put(purchaseOrder.id, purchaseOrder);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var purchaseOrder = map.get(object[property]);
                                    if (purchaseOrder != null) {
                                        object[property + "Object"] = purchaseOrder;
                                    }
                                }
                            });
                        }
                    );
                }
            }
        }
    }
);