define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('salesRegionFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getSalesRegions: function (criteria, pageable) {
                            var url = "api/crm/salesregions";

                            url += "?name={0}&district={1}&state={2}&country={3}&salesRep={4}".
                                format(criteria.name, criteria.district, criteria.state,
                                criteria.country, criteria.salesRep);

                            url += "&page={0}&size={1}".format(pageable.page - 1, pageable.size);

                            return httpFactory.get(url);
                        },
                        saveSalesRegion: function (salesRegion) {
                            var url = "api/crm/salesregions";
                            return httpFactory.post(url, salesRegion);
                        },
                        deleteRegion: function (regionId) {
                            var url = "api/crm/salesregions/" + regionId;
                            return httpFactory.delete(url);
                        }
                    }
                }
            ]
        );
    }
);