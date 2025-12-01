define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('RoadChallanService', RoadChallanService);

        function RoadChallanService(httpFactory) {
            return {
                createRoadChallan: createRoadChallan,
                getAllRoadChallans: getAllRoadChallans,
                roadChallanFreeTextSearch: roadChallanFreeTextSearch,
                getRoadChallanItems: getRoadChallanItems,
                getRoadChallan: getRoadChallan,
                createRoadChallanItems: createRoadChallanItems,
                getPagedRoadChallans: getPagedRoadChallans,
                getPagedRoadChallanItems: getPagedRoadChallanItems
            };

            function createRoadChallan(storeId, roadchallan) {
                var url = "api/is/stores/" + storeId + "/roadChalans";
                return httpFactory.post(url, roadchallan)
            }

            function getAllRoadChallans(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/roadChalans?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url)
            }

            function roadChallanFreeTextSearch(storeId, pageable, freeText) {
                var url = "api/is/stores/" + storeId + "/roadChalans/freesearch?page={0}&size={1}&sort={2}".
                        format(pageable.page, pageable.size, pageable.sort.field);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }


            function getRoadChallan(storeId, roadChallanId) {
                var url = "api/is/stores/" + storeId + "/roadChalans/" + roadChallanId;
                return httpFactory.get(url)
            }

            function getRoadChallanItems(storeId, roadchallanId) {
                var url = "api/is/stores/" + storeId + "/roadChalans/" + roadchallanId + "/roadChalanitems";
                return httpFactory.get(url)
            }

            function createRoadChallanItems(storeId, roadChallanId, customRoadChallanItems) {
                var url = "api/is/stores/" + storeId + "/roadChalans/"  + roadChallanId + "/roadChalanItem" ;
                return httpFactory.post(url, customRoadChallanItems)
            }

            function getPagedRoadChallans(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/roadChalans/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url)
            }

            function getPagedRoadChallanItems(storeId, roadchallanId, pageable) {
                var url = "api/is/stores/" + storeId + "/roadChalans/" + roadchallanId + "/roadChalanitems/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

        }
    }
);