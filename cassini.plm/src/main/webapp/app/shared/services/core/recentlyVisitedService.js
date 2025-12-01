define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('RecentlyVisitedService', RecentlyVisitedService);

        function RecentlyVisitedService(httpFactory) {
            return {
                createRecentlyVisited: createRecentlyVisited,
                updateRecentlyVisited: updateRecentlyVisited,
                deleteRecentlyVisited: deleteRecentlyVisited,
                getRecentlyVisited: getRecentlyVisited,
                getAllRecentlyVisited: getAllRecentlyVisited,
                getFreeTextSearchFiles: getFreeTextSearchFiles,
                updateFileNumbers: updateFileNumbers,
                getTopSearchResults: getTopSearchResults,
                getPersonsWithoutLogin: getPersonsWithoutLogin
            };

            function createRecentlyVisited(recentlyVisited) {
                var url = "api/plm/recentlyVisited";
                return httpFactory.post(url, recentlyVisited);
            }

            function updateRecentlyVisited(recentlyVisited) {
                var url = "api/plm/recentlyVisited/" + recentlyVisited.id;
                return httpFactory.put(url, recentlyVisited);
            }

            function deleteRecentlyVisited(templateId) {
                var url = "api/plm/recentlyVisited/" + templateId;
                return httpFactory.delete(url);
            }

            function getRecentlyVisited(templateId) {
                var url = "api/plm/recentlyVisited/" + templateId;
                return httpFactory.get(url);
            }

            function getFreeTextSearchFiles(searchText, filter) {
                var url = "api/plm/recentlyVisited/commonFiles/freeTextSearch/" + searchText + "/filter/" + filter;
                return httpFactory.get(url);
            }

            function getAllRecentlyVisited(personId, pageable) {
                var url = "api/plm/recentlyVisited/person/" + personId + "/all?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function updateFileNumbers() {
                var url = 'api/plm/recentlyVisited/files/updateFileNo';
                return httpFactory.get(url);
            }

            function getTopSearchResults(pageable, filters, type) {
                var url = "api/plm/recentlyVisited/topsearch/" + type + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}&tagType={1}".
                    format(filters.searchQuery, filters.objectType);
                return httpFactory.get(url);
            }

            function getPersonsWithoutLogin() {
                var url = 'api/plm/recentlyVisited/personswithoutlogin';
                return httpFactory.get(url);
            }
        }
    }
);