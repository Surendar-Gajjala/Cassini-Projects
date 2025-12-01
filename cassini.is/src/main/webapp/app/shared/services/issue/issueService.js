define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('IssueService', IssueService);

        function IssueService(httpFactory) {
            return {
                getPageableIssues: getPageableIssues,
                getIssue: getIssue,
                getIssues: getIssues,
                createIssue: createIssue,
                updateExistedIssue: updateExistedIssue,
                deleteIssue: deleteIssue,
                freeSearch: freeSearch,
                getIssueTypes: getIssueTypes,
                createProblemMedia: createProblemMedia,
                getProblemMedia: getProblemMedia,
                getIssuesCountByPriority: getIssuesCountByPriority,
                getIssueDetailsCount: getIssueDetailsCount
            };

            function freeSearch(pageable, criteria) {
                var url = "api/issues/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}&targetObjectType={1}".
                    format(criteria.searchQuery, criteria.targetObjectType);
                return httpFactory.get(url);
            }

            function createIssue(issue) {
                var url = "api/issues";
                return httpFactory.post(url, issue);
            }

            function getPageableIssues(objectType, objectId, pageable) {
                var url = "api/issues/pageable?targetObjectType={0}&targetObjectId={1}".format(objectType, objectId);
                url += "&page={0}&size={1}&sort={2}".
                    format(pageable.page, pageable.size, pageable.sort.field);
                return httpFactory.get(url);
            }

            function getIssues(objectType, objectId) {
                var url = "api/issues/targetObjectType/" + objectType + "/targetObjectId/" + objectId;
                return httpFactory.get(url);
            }

            function getIssue(issueId) {
                var url = "api/issues/" + issueId;
                return httpFactory.get(url);
            }

            function updateExistedIssue(issue) {
                var url = "api/issues/" + issue.id;
                return httpFactory.put(url, issue);
            }

            function deleteIssue(issueId) {
                var url = "api/issues/" + issueId;
                return httpFactory.delete(url);
            }

            function getIssueTypes() {
                var url = "api/issuetypes/";
                return httpFactory.get(url);
            }

            function createProblemMedia(problemId, media) {
                var url = "api/issues/" + problemId + "/media";
                return httpFactory.uploadMultiple(url, media);
            }

            function getProblemMedia(problemId) {
                var url = "api/issues/" + problemId + "/media";
                return httpFactory.get(url);
            }

            function getIssuesCountByPriority(objectId) {
                var url = "api/issues/count/targetObjectId/" + objectId;
                return httpFactory.get(url);
            }

            function getIssueDetailsCount(id) {
                var url = "api/issues/" + id + "/count";
                return httpFactory.get(url);
            }
        }
    }
);