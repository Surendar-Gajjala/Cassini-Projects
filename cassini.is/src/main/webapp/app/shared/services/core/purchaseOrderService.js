define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('PurchaseOrderService', PurchaseOrderService);

        function PurchaseOrderService(httpFactory) {
            return {
                getPurchaseOrders: getPurchaseOrders,
                filterPurchaseOrders: filterPurchaseOrders,
                createPurchaseOrder: createPurchaseOrder,
                getPurchaseOrder: getPurchaseOrder,
                updatePurchaseOrder: updatePurchaseOrder,
                deletePurchaseOrder: deletePurchaseOrder,
                getPurchaseOrderItems: getPurchaseOrderItems,
                setPurchaseOrderItems: setPurchaseOrderItems,
                receivePurchaseOrder: receivePurchaseOrder,
                getPurchaseOrderByIds: getPurchaseOrderByIds,
                getPurchaseOrderReferences: getPurchaseOrderReferences
            };

            function getPurchaseOrders(pageable) {
                var url = "api/im/pos?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function filterPurchaseOrders(filters, pageable) {
                var url = "api/im/pos/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&orderNumber={0}&status={1}".format(filters.orderNumber, filters.status);
                return httpFactory.get(url);
            }

            function createPurchaseOrder(po) {
                var url = "api/im/pos";
                return httpFactory.post(url, po);
            }

            function getPurchaseOrder(poId) {
                var url = "api/im/pos/" + poId;
                return httpFactory.get(url);
            }

            function updatePurchaseOrder(po) {
                var url = "api/im/pos/" + po.id;
                return httpFactory.put(url, po);
            }

            function deletePurchaseOrder(poId) {
                var url = "api/im/pos/" + poId;
                return httpFactory.delete(url);
            }

            function getPurchaseOrderItems(poId) {
                var url = "api/im/pos/" + poId + "/items";
                return httpFactory.get(url);
            }

            function setPurchaseOrderItems(poId, items) {
                var url = "api/im/pos/" + poId + "/items";
                return httpFactory.post(url, items);
            }

            function receivePurchaseOrder(poId) {
                var url = "api/im/pos/" + poId + "/receive";
                return httpFactory.get(url);
            }

            function getPurchaseOrderByIds(ids) {
                var url = "api/im/pos/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getPurchaseOrderReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function(object) {
                    if(object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if(ids.length > 0) {
                    getPurchaseOrderByIds(ids).then(
                        function(data) {
                            var map = new Hashtable();

                            angular.forEach(data, function(item) {
                                map.put(item.id, item);
                            });

                            angular.forEach(objects, function(object) {
                                if(object[property] != null) {
                                    var item = map.get(object[property]);
                                    if(item != null) {
                                        object[property + "Object"] = item;
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