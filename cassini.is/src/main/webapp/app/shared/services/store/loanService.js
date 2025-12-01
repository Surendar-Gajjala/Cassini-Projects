/**
 * Created by swapna on 10/08/18.
 */
define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('LoanService', LoanService);

        function LoanService(httpFactory) {
            return {
                createLoan: createLoan,
                getLoansIssuedByStore: getLoansIssuedByStore,
                getPagedLoansIssued:getPagedLoansIssued,
                getPagedLoansReceived:getPagedLoansReceived,
                getLoanById: getLoanById,
                updateLoan: updateLoan,
                createLoanIssueItems: createLoanIssueItems,
                getLoanItems: getLoanItems,
                getLoansReceivedByStore: getLoansReceivedByStore,
                getLoanReturnItemsDetails: getLoanReturnItemsDetails,
                createLoanReturnItems: createLoanReturnItems,
                getLoanReturnItemHistory: getLoanReturnItemHistory,
                printLoanChallan: printLoanChallan,
                getAllLoans: getAllLoans,
                freeTextSearch: freeTextSearch
            };

            function createLoan(storeId, loan) {
                var url = "api/is/stores/" + storeId + "/loans";
                return httpFactory.post(url, loan);
            }

            function getLoansIssuedByStore(storeId) {
                var url = "api/is/stores/" + storeId + "/loans/issued";
                return httpFactory.get(url);
            }

            function getPagedLoansIssued(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/loans/issued/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url)
            }

            function getPagedLoansReceived(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/loans/received/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url)
            }

            function getLoansReceivedByStore(storeId) {
                var url = "api/is/stores/" + storeId + "/loans/received";
                return httpFactory.get(url);
            }

            function getLoanById(storeId, loanId) {
                var url = "api/is/stores/" + storeId + "/loans/" + loanId;
                return httpFactory.get(url);
            }

            function updateLoan(storeId, loan) {
                var url = "api/is/stores/" + storeId + "/loans";
                return httpFactory.put(url, loan);
            }

            function createLoanIssueItems(fromStore, toStore, toProject, loanItems ){
                var url = "api/is/stores/" + fromStore + "/loans/items/toStore/" + toStore + "/toProject/" + toProject;
                return httpFactory.post(url, loanItems);
            }

            function getLoanItems(storeId, loanId, pageable) {
                var url = "api/is/stores/" + storeId + "/loans/" + loanId + "/loanItems/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getLoanReturnItemsDetails(storeId, loanId, pageable) {
                var url = "api/is/stores/" + storeId + "/loans/" + loanId + "/returnItems/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function createLoanReturnItems(storeId, loanId, returnItems) {
                var url = "api/is/stores/" + storeId + "/loans/" + loanId + "/returnItems";
                return httpFactory.post(url, returnItems);
            }

            function getLoanReturnItemHistory(storeId, loanId, itemId) {
                var url = "api/is/stores/" + storeId + "/loans/" + loanId + "/itemHistory/" + itemId;
                return httpFactory.get(url);
            }

            function printLoanChallan(storeId, loanId) {
                var url = "api/is/stores/" + storeId + "/loans/" + loanId + "/printLoanIssueChallan";
                return httpFactory.get(url);
            }

            function getAllLoans(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/loans/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function freeTextSearch(storeId, pageable, freeText) {
                var url = "api/is/stores/" + storeId + "/loans/freesearch?page={0}&size={1}&sort={2}".
                    format(pageable.page, pageable.size, pageable.sort.field);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

        }
    }
);