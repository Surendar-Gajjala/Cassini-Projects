define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TopStockIssuedService', TopStockIssuedService);

        function TopStockIssuedService(httpFactory) {
            return {
                createTopStockIssue: createTopStockIssue,
                updateTopStockIssue: updateTopStockIssue,
                deleteTopStockIssue: deleteTopStockIssue,
                getTopStockIssue: getTopStockIssue,
                getTopStockIssues: getTopStockIssues,
                getPagedStockIssues: getPagedStockIssues,
                getStockIssuedQtyByProjectAndItemNumber: getStockIssuedQtyByProjectAndItemNumber,
                getStockIssueItems: getStockIssueItems,
                getPagedStockIssueItems: getPagedStockIssueItems,
                getPagedTopStockIssues: getPagedTopStockIssues,
                getProjectResourcesInventory: getProjectResourcesInventory,
                getProjectItems: getProjectItems,
                stockIssuedFreeTextSearch: stockIssuedFreeTextSearch,
                saveIssueTypeAttributes: saveIssueTypeAttributes,
                getStockIssuesByAttributes: getStockIssuesByAttributes,
                getStockIssuedItemsByAttributes: getStockIssuedItemsByAttributes,
                getStockIssuesByType: getStockIssuesByType,
                getStoreIssuedItemsReport: getStoreIssuedItemsReport,
                exportStockIssuedItemsReport: exportStockIssuedItemsReport,
                createIssueToPerson: createIssueToPerson,
                getMinimumDate: getMinimumDate,
                getIssueTypeAttributes: getIssueTypeAttributes
            };

            function createTopStockIssue(storeId, topStockIssued) {
                var url = "api/is/stores/" + storeId + "/stockIssues";
                return httpFactory.post(url, topStockIssued);
            }

            function updateTopStockIssue(storeId, topStockIssued) {
                var url = "api/is/stores/" + storeId + "/stockIssues/" + topStockIssued.id;
                return httpFactory.put(url, topStockIssued);
            }

            function deleteTopStockIssue(storeId, StockIssuedId) {
                var url = "api/is/stores/" + storeId + "/stockIssues/" + StockIssuedId;
                return httpFactory.delete(url);
            }

            function getTopStockIssue(storeId, id) {
                var url = "api/is/stores/" + storeId + "/stockIssues/" + id;
                return httpFactory.get(url);
            }

            function getStockIssueItems(storeId, id) {
                var url = "api/is/stores/" + storeId + "/stockIssues/" + id + "/items";
                return httpFactory.get(url);
            }

            function getPagedStockIssueItems(storeId, id, pageable) {
                var url = "api/is/stores/" + storeId + "/stockIssues/" + id + "/items/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }


            function getTopStockIssues(storeId) {
                var url = "api/is/stores/" + storeId + "/stockIssues";
                return httpFactory.get(url);
            }

            function getPagedTopStockIssues(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockIssues/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getStockIssuedQtyByProjectAndItemNumber(projectId, itemNumber) {
                var url = "api/is/stores/" + storeId + "/stockIssues/project/" + projectId + "/itemNumber/" + itemNumber;
                return httpFactory.get(url);
            }

            function getProjectResourcesInventory(storeId, projectId, taskId) {
                var url = "api/is/stores/" + storeId + "/stockIssues/project/" + projectId + "/task/" + taskId;
                return httpFactory.get(url);
            }

            //to get project Items or masterdata items based on project id
            function getProjectItems(storeId, projectId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockIssues/project/" + projectId + "/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getPagedStockIssues(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockIssues/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function stockIssuedFreeTextSearch(storeId, pageable, freeText) {
                var url = "api/is/stores/" + storeId + "/stockIssues/freesearch?page={0}&size={1}&sort={2}".
                        format(pageable.page, pageable.size, pageable.sort.field);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function saveIssueTypeAttributes(storeId, stockIssueId, attributes) {
                var url = "api/is/stores/" + storeId + "/stockIssues/" + stockIssueId + "/attributes";
                return httpFactory.post(url, attributes);
            }

            function getStockIssuesByType(storeId, issueTypeId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockIssues/type/" + issueTypeId + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getStoreIssuedItemsReport(storeId, criteria) {
                var url = "api/is/stores/" + storeId + "/stockIssues/report?fromDate={0}&toDate={1}".format(criteria.fromDate, criteria.toDate);
                return httpFactory.get(url);
            }

            function exportStockIssuedItemsReport(storeId, criteria) {
                var url = "api/is/stores/" + storeId + "/stockIssues/export/type/" + 'excel';
                return httpFactory.post(url, criteria);
            }

            function getStockIssuesByAttributes(storeId, pageable, criteria) {
                var url = "api/is/stores/" + storeId + "/stockIssues/by/attributes/pageable";

                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.post(url, criteria);
            }

            function getStockIssuedItemsByAttributes(storeId, criteria) {
                var url = "api/is/stores/" + storeId + "/stockIssues/by/attributes";
                return httpFactory.post(url, criteria);
            }

            function createIssueToPerson(person) {
                var url = "api/common/persons/issue/person";
                return httpFactory.post(url, person);
            }

            function getMinimumDate(storeId) {
                var url = "api/is/stores/" + storeId + "/stockIssues/minimumdate";
                return httpFactory.get(url);
            }

            function getIssueTypeAttributes(storeId, stockIssueId, issueTypeId) {
                var url = "api/is/stores/" + storeId + "/stockIssues/" + stockIssueId + "/issueType/" + issueTypeId + "/attributes";
                return httpFactory.get(url);
            }
        }
    }
);