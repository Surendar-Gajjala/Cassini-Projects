define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('SearchService', SearchService);

        function SearchService(httpFactory) {
            return {
                createSearch: createSearch,
                getSavedSearches: getSavedSearches,
                deleteSavedSearch: deleteSavedSearch,
                getSavedSearch: getSavedSearch,
                getSavedSearchesCount:getSavedSearchesCount
            };

            function createSearch(search) {
                var url = "api/plm/savedsearches";
                return httpFactory.post(url, search);
            }

            function getSavedSearches(objectType){
                var url = "api/plm/savedsearches/byType/"+objectType;
                return httpFactory.get(url);
            }
            function deleteSavedSearch(savedSearchId){
                var url = "api/plm/savedsearches/"+savedSearchId;
                return httpFactory.delete(url);
            }
            function getSavedSearch(owner, pageable){
                var url  = "api/plm/savedsearches/byowner/"+owner;
                url += "?page={0}&size={1}&sort={2}:{3}".
                format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSavedSearchesCount() {
                var url = "api/plm/savedsearches/all/count";
                return httpFactory.get(url);
            }

        }
    }
);