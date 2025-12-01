define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ScrapService', ScrapService);

        function ScrapService(httpFactory) {
            return {
                create: create,
                updateScrap: updateScrap,
                get: get,
                getAll: getAll,
                getScrapItemProperties: getScrapItemProperties,
                freeTextSearch: freeTextSearch

            };

            function create(scrap) {
                var url = "api/is/stores/scraps";
                return httpFactory.post(url, scrap);
            }


            function updateScrap(scrap) {
                var url = "api/is/stores/scraps/" + scrap.id;
                return httpFactory.put(url, scrap);
            }
            
            function getAll(pageable) {
                var url = "api/is/stores/scraps?page={0}&size={1}&sort={2}:{3}".
                format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function get(scrapId) {
                var url = "api/is/stores/scraps/" + scrapId;
                return httpFactory.get(url);
            }

            function getScrapItemProperties(objectType) {
                var url = "api/is/stores/scrapAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function freeTextSearch(pageable, freetext) {
                var url = "api/is/stores/scraps/freesearch?page={0}&size={1}&sort={2}".
                    format(pageable.page, pageable.size, pageable.sort.field);

                url += "&searchQuery={0}".
                    format(freetext);
                return httpFactory.get(url);
            }

        }
    }
);