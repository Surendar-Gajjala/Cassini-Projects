define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('IssueService', IssueService);

        function IssueService(httpFactory) {
            return {
                createIssue: createIssue,
                updateIssue: updateIssue,
                deleteIssue: deleteIssue,
                getIssue: getIssue,
                getIssues: getIssues,
                getAllIssues: getAllIssues,
                getIssueDetails: getIssueDetails,
                getIssuedItems: getIssuedItems,

                createIssueItems: createIssueItems,
                getIssueItems: getIssueItems,
                issueSelectedItems: issueSelectedItems,
                getIssuesByInstance: getIssuesByInstance,
                newIssue: newIssue,
                approveIssue: approveIssue,
                approveIssueItem: approveIssueItem,
                issueItems: issueItems,
                partiallyApproveIssueItem: partiallyApproveIssueItem,
                rejectIssueItem: rejectIssueItem,
                receiveItems: receiveItems,
                approveItem: approveItem,
                getIssueReportByMissile: getIssueReportByMissile,
                resetReturnedItem: resetReturnedItem,
                checkRejectedItemAlreadyIssued: checkRejectedItemAlreadyIssued,
                addRejectedItemToInventory: addRejectedItemToInventory,
                getIssuedPersonPermission: getIssuedPersonPermission
            };

            function createIssue(issue) {
                var url = "api/drdo/issues";
                return httpFactory.post(url, issue);
            }

            function newIssue(issue) {
                var url = "api/drdo/issues/new";
                return httpFactory.post(url, issue);
            }

            function issueSelectedItems(issue) {
                var url = "api/drdo/issues/items";
                return httpFactory.post(url, issue);
            }

            function updateIssue(issue) {
                var url = "api/drdo/issues/" + issue.id;
                return httpFactory.put(url, issue);
            }

            function deleteIssue(issueId) {
                var url = "api/drdo/issues/" + issueId;
                return httpFactory.delete(url);
            }

            function getIssue(issueId) {
                var url = "api/drdo/issues/" + issueId;
                return httpFactory.get(url);
            }

            function getIssuedItems(issueId) {
                var url = "api/drdo/issues/" + issueId + "/items";
                return httpFactory.get(url);
            }

            function getIssueDetails(issueId) {
                var url = "api/drdo/issues/" + issueId + "/details";
                return httpFactory.get(url);
            }

            function getIssuesByInstance(instanceId) {
                var url = "api/drdo/issues/" + instanceId + "/all";
                return httpFactory.get(url);
            }

            function getIssues() {
                var url = "api/drdo/issues";
                return httpFactory.get(url);
            }

            function getAllIssues(pageable, filter) {
                var url = "api/drdo/issues/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&fromDate={0}&toDate={1}&month={2}&adminPermission={3}&storeApprove={4}&bdlQcApprove={5}&notification={6}&bdlPpcReceive={7}&casApprove={8}&bdlApprove={9}&searchQuery={10}&missile={11}&versity={12}&versityQc={13}&versityPpc={14}&versityApprove={15}"
                    .format(filter.fromDate, filter.toDate, filter.month, filter.adminPermission, filter.storeApprove, filter.bdlQcApprove, filter.notification, filter.bdlPpcReceive,
                    filter.casApprove, filter.bdlApprove, filter.searchQuery, filter.missile, filter.versity, filter.versityQc, filter.versityPpc, filter.versityApprove);
                return httpFactory.get(url);
            }

            function createIssueItems(issueId, issueItems) {
                var url = "api/drdo/issues/" + issueId + "/items";
                return httpFactory.post(url, issueItems);
            }

            function getIssueItems(issueId) {
                var url = "api/drdo/issues/" + issueId + "/items";
                return httpFactory.get(url);
            }

            function approveIssue(issue) {
                var url = "api/drdo/issues/" + issue.id + "/approve";
                return httpFactory.put(url, issue);
            }


            function approveIssueItem(issueId, issueItem) {
                var url = "api/drdo/issues/" + issueId + "/item/" + issueItem.id + "/approve";
                return httpFactory.put(url, issueItem);
            }

            function approveItem(issueId, issueItem) {
                var url = "api/drdo/issues/" + issueId + "/item/" + issueItem.id + "/approveItem";
                return httpFactory.put(url, issueItem);
            }

            function partiallyApproveIssueItem(issueId, issuedItem) {
                var url = "api/drdo/issues/" + issueId + "/item/" + issuedItem.issueItem.id + "/partially/approve";
                return httpFactory.put(url, issuedItem);
            }

            function rejectIssueItem(issueId, issuedItem) {
                var url = "api/drdo/issues/" + issueId + "/item/" + issuedItem.issueItem.id + "/reject";
                return httpFactory.put(url, issuedItem);
            }

            function resetReturnedItem(issueId, issuedItem) {
                var url = "api/drdo/issues/" + issueId + "/item/" + issuedItem.issueItem.id + "/reset";
                return httpFactory.put(url, issuedItem);
            }

            function checkRejectedItemAlreadyIssued(issueId, issuedItem) {
                var url = "api/drdo/issues/" + issueId + "/item/" + issuedItem.issueItem.id + "/check";
                return httpFactory.put(url, issuedItem);
            }

            function addRejectedItemToInventory(issueId, issuedItem) {
                var url = "api/drdo/issues/" + issueId + "/item/" + issuedItem.issueItem.id + "/inventory";
                return httpFactory.put(url, issuedItem);
            }

            function issueItems(issue) {
                var url = "api/drdo/issues/" + issue.id + "/issue/items";
                return httpFactory.put(url, issue);
            }

            function receiveItems(issueId, issueItems) {
                var url = "api/drdo/issues/" + issueId + "/issue/receive";
                return httpFactory.put(url, issueItems);
            }

            function getIssueReportByMissile(missileId, sectionId, searchText) {
                var url = "api/drdo/issues/missile/" + missileId + "/section/" + sectionId + "?searchText=" + searchText;
                return httpFactory.get(url);
            }

            function getIssuedPersonPermission(personId) {
                var url = "api/drdo/issues/" + personId + "/permission";
                return httpFactory.get(url);
            }
        }
    }
);