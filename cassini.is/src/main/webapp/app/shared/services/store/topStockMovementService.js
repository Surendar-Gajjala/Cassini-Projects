define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TopStockMovementService', TopStockMovementService);

        function TopStockMovementService(httpFactory) {
            return {
                createTopStockMovement: createTopStockMovement,
                updateTopStockMovement: updateTopStockMovement,
                deleteTopStockMovement: deleteTopStockMovement,
                getTopStockMovement: getTopStockMovement,
                getTopStockMovements: getTopStockMovements,
                getStockMovementByStoreAndItem: getStockMovementByStoreAndItem,
                getStockMovementByItem: getStockMovementByItem,
                getStockMovementByProjectItemNum: getStockMovementByProjectItemNum,
                getPageableStockMovementByFilter: getPageableStockMovementByFilter,
                getStockMovementByFilter: getStockMovementByFilter,
                getStockMovementByBomItemAndItemNum: getStockMovementByBomItemAndItemNum,
                getStockMovementByStore: getStockMovementByStore,
                getStockMovementsByReceiveId: getStockMovementsByReceiveId,
                getReportByDates: getReportByDates,
                exportStoreReport: exportStoreReport,
                balanceReport: balanceReport,
                exportStockMovements: exportStockMovements
            };

            function createTopStockMovement(topStockMovement) {
                var url = "api/is/stores/stockMovements";
                return httpFactory.post(url, topStockMovement);
            }

            function updateTopStockMovement(topStockMovement) {
                var url = "api/is/stores/stockMovements/" + topStockMovement.rowId;
                return httpFactory.put(url, topStockMovement);
            }

            function deleteTopStockMovement(StockMovementId) {
                var url = "api/is/stores/stockMovements/" + StockMovementId;
                return httpFactory.delete(url);
            }

            function getTopStockMovement(topStockMovement) {
                var url = "api/is/stores/stockMovements/" + topStockMovement.id;
                return httpFactory.get(url);
            }

            function getTopStockMovements() {
                var url = "api/is/stores/stockMovements";
                return httpFactory.get(url);
            }

            function getStockMovementByStoreAndItem(storeId, itemId) {
                var url = "api/is/stores/stockMovements/store/" + storeId + "/byItem/" + itemId;
                return httpFactory.get(url);
            }

            function getStockMovementByStore(storeId, pageable) {
                var url = "api/is/stores/stockMovements/store/" + storeId;
                url += "?page={0}&size={1}".
                    format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getStockMovementByItem(item) {
                var url = "api/is/stores/stockMovements/item/" + item;
                return httpFactory.get(url);
            }

            function getStockMovementByProjectItemNum(itemNumber, projectId) {
                var url = "api/is/stores/stockMovements/boqItemNumber/" + itemNumber + "/project/" + projectId;
                return httpFactory.get(url);
            }

            function getStockMovementByBomItemAndItemNum(itemNumber, bomItem) {
                var url = "api/is/stores/stockMovements/itemNumber/" + itemNumber + "/bomItem/" + bomItem;
                return httpFactory.get(url);
            }

            function getPageableStockMovementByFilter(storeId, pageable, criteria) {
                var url = "api/is/stores/stockMovements/store/" + storeId + "/filters/pageable";
                url += "?itemName={0}&description={1}&itemNumber={2}&projectName={3}&quantity={4}&movementType={5}&itemType={6}&units={7}&fromDate={8}&toDate={9}&storeId={10}".
                    format(criteria.itemName, criteria.description, criteria.itemNumber, criteria.projectName, criteria.quantity, criteria.movementType, criteria.itemType, criteria.units, criteria.fromDate, criteria.toDate, criteria.storeId);

                url += "&page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getStockMovementByFilter(storeId, criteria) {
                var url = "api/is/stores/stockMovements/store/" + storeId + "/filters";
                url += "?itemName={0}&fromDate={1}&itemNumber={2}&toDate={3}&quantity={4}&movementType={5}&store={6}&units={7}".
                    format(criteria.itemName, criteria.fromDate, criteria.itemNumber, criteria.toDate, criteria.quantity, criteria.movementType, criteria.store, criteria.units);
                return httpFactory.get(url);
            }

            function getStockMovementsByReceiveId(receiveId) {
                var url = "api/is/stores/stockMovements/receive/" + receiveId;
                return httpFactory.get(url);
            }

            function getReportByDates(startDate, endDate) {
                var url = "api/is/stores/stockMovements/report?fromDate={0}&toDate={1}"
                    .format(startDate, endDate);
                return httpFactory.get(url);
            }

            function exportStoreReport() {
                var url = "api/is/stores/stockMovements/" + 'excel' + "/report?fromDate={0}&toDate={1}"
                        .format(startDate, endDate);
                return httpFactory.get(url);
            }

            function balanceReport(criteria) {
                var url = "api/is/stores/stockMovements/balancereport";
                url += "?itemName={0}&fromDate={1}&itemNumber={2}&toDate={3}&quantity={4}&movementType={5}&storeId={6}&units={7}".
                    format(criteria.itemName, criteria.fromDate, criteria.itemNumber, criteria.toDate, criteria.quantity, criteria.movementType, criteria.storeId, criteria.units);
                return httpFactory.get(url);
            }

            function exportStockMovements(criteria) {
                var url = "api/is/stores/stockMovements/export/type/" + 'excel';
                return httpFactory.post(url, criteria);
            }

        }
    }
);