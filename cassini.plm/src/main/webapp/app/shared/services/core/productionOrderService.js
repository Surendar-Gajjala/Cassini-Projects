define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ProductionOrderService', ProductionOrderService);

        function ProductionOrderService(httpFactory) {
            return {
                createProductionOrder: createProductionOrder,
                updateProductionOrder: updateProductionOrder,
                getProductionOrder: getProductionOrder,
                deleteProductionOrder: deleteProductionOrder,
                getAllProductionOrders: getAllProductionOrders,
                getMultipleProductionOrders: getMultipleProductionOrders,
                getProductionOrdersMinAndMaxDates: getProductionOrdersMinAndMaxDates,
                savePrGanttObjects: savePrGanttObjects,
                createProductionOrderItem: createProductionOrderItem,
                updateProductionOrderItem: updateProductionOrderItem,
                getProductionOrderItem: getProductionOrderItem,
                deleteProductionOrderItem: deleteProductionOrderItem,
                getProductionOrderItems: getProductionOrderItems,
                createProductionOrderItems: createProductionOrderItems,
                promoteProductionOrder: promoteProductionOrder,
                demoteProductionOrder: demoteProductionOrder,
                getAllCalenderProductionOrders: getAllCalenderProductionOrders,
                getProductionOrderCounts: getProductionOrderCounts,
                updateProductionOrderItemInstance: updateProductionOrderItemInstance,
                deleteProductionOrderItemInstance: deleteProductionOrderItemInstance
            };

            function createProductionOrder(productionOrder) {
                var url = "api/mes/productionorders";
                return httpFactory.post(url, productionOrder)
            }


            function updateProductionOrder(productionOrder) {
                var url = "api/mes/productionorders/" + productionOrder.id;
                return httpFactory.put(url, productionOrder);
            }

            function getProductionOrder(id) {
                var url = "api/mes/productionorders/" + id;
                return httpFactory.get(url)
            }

            function getProductionOrderCounts(id) {
                var url = "api/mes/productionorders/" + id + "/counts";
                return httpFactory.get(url)
            }

            function deleteProductionOrder(productionOrder) {
                var url = "api/mes/productionorders/" + productionOrder;
                return httpFactory.delete(url);
            }

            function getAllProductionOrders(pageable, filters) {
                var url = "api/mes/productionorders/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&schedule={1}&unSchedule={2}".
                    format(filters.searchQuery, filters.schedule, filters.unSchedule);
                return httpFactory.get(url);
            }

            function getAllCalenderProductionOrders() {
                var url = "api/mes/productionorders/calender/all";
                return httpFactory.get(url);
            }

            function getMultipleProductionOrders(productionOrderIds) {
                var url = "api/mes/productionorders/multiple/[" + productionOrderIds + "]";
                return httpFactory.get(url);
            }

            function getProductionOrdersMinAndMaxDates() {
                var url = "api/mes/productionorders/minmax/dates";
                return httpFactory.get(url);
            }

            function savePrGanttObjects(prList) {
                var url = "api/mes/productionorders/objects/multiple";
                return httpFactory.post(url, prList);
            }

            function createProductionOrderItem(id, item) {
                var url = "api/mes/productionorders/" + id + "/items";
                return httpFactory.post(url, item)
            }

            function updateProductionOrderItem(id, item) {
                var url = "api/mes/productionorders/" + id + "/items/" + item.id;
                return httpFactory.put(url, item);
            }

            function updateProductionOrderItemInstance(id, item) {
                var url = "api/mes/productionorders/" + id + "/items/" + item.id + "/instance";
                return httpFactory.put(url, item);
            }

            function getProductionOrderItem(id, itemId) {
                var url = "api/mes/productionorders/" + id + "/items/" + itemId;
                return httpFactory.get(url)
            }

            function deleteProductionOrderItem(id, itemId) {
                var url = "api/mes/productionorders/" + id + "/items/" + itemId;
                return httpFactory.delete(url);
            }

            function deleteProductionOrderItemInstance(id, itemId) {
                var url = "api/mes/productionorders/" + id + "/items/" + itemId + "/instance";
                return httpFactory.delete(url);
            }

            function getProductionOrderItems(id) {
                var url = "api/mes/productionorders/" + id + "/items";
                return httpFactory.get(url);
            }

            function createProductionOrderItems(id, items) {
                var url = "api/mes/productionorders/" + id + "/items";
                return httpFactory.post(url, items);
            }

            function promoteProductionOrder(id, order) {
                var url = "api/mes/productionorders/" + id + "/promote";
                return httpFactory.put(url, order);
            }

            function demoteProductionOrder(id, order) {
                var url = "api/mes/productionorders/" + id + "/demote";
                return httpFactory.put(url, order);
            }
        }
    }
);