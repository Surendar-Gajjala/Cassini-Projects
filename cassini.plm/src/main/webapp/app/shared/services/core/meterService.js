define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MeterService', MeterService);

        function MeterService(httpFactory) {
            return {
                createMeter: createMeter,
                updateMeter: updateMeter,
                getMeter: getMeter,
                getAllMeters: getAllMeters,
                deleteMeter: deleteMeter,
                getMultipleMeters: getMultipleMeters,
                getMeters: getMeters,
                saveMeterAttributes: saveMeterAttributes,
                createAssetMeter: createAssetMeter

            };

            function createMeter(meter) {
                var url = "api/mro/meters";
                return httpFactory.post(url, meter)
            }

            function createAssetMeter(meter) {
                var url = "api/mro/assets/create/assetmeter";
                return httpFactory.post(url, meter)
            }

            function updateMeter(meter) {
                var url = "api/mro/meters/" + meter.id;
                return httpFactory.put(url, meter);
            }

            function getMeter(id) {
                var url = "api/mro/meters/" + id;
                return httpFactory.get(url)
            }

            function deleteMeter(meter) {
                var url = "api/mro/meters/" + meter;
                return httpFactory.delete(url);
            }

            function getAllMeters(pageable, filters) {
                var url = "api/mro/meters/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&name={1}&number={2}&type={3}&asset={4}".
                    format(filters.searchQuery, filters.name, filters.number, filters.type, filters.asset);
                return httpFactory.get(url);
            }

            function getMultipleMeters(meterIds) {
                var url = "api/mro/meters/multiple/[" + meterIds + "]";
                return httpFactory.get(url);
            }

            function getMeters() {
                var url = "api/mro/meters";
                return httpFactory.get(url)
            }

            function saveMeterAttributes(attributes) {
                var url = "api/mro/meters/create/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

        }
    }
);