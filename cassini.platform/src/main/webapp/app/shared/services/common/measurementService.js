define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MeasurementService', MeasurementService);

        function MeasurementService($q, httpFactory) {
            return {
                createMeasurement: createMeasurement,
                getAllMeasurements: getAllMeasurements,
                updateMeasurement: updateMeasurement,
                deleteMeasurement: deleteMeasurement,
                getMeasurement: getMeasurement,
                createMeasurementUnit: createMeasurementUnit,
                updateMeasurementUnit: updateMeasurementUnit,
                deleteMeasurementUnit: deleteMeasurementUnit,
                getMeasurementByName: getMeasurementByName
            };

            function getMeasurement() {
                var url = "api/measurements";
                return httpFactory.get(url);
            }

            function getMeasurementByName(name) {
                var url = "api/measurements/name/" + name;
                return httpFactory.get(url);
            }

            function getAllMeasurements() {
                var url = "api/measurements";
                return httpFactory.get(url);
            }

            function createMeasurement(measurement) {
                var url = "api/measurements";
                return httpFactory.post(url, measurement);
            }

            function deleteMeasurement(measurementId) {
                var url = "api/measurements/" + measurementId;
                return httpFactory.delete(url);
            }

            function updateMeasurement(measurementId, measurement) {
                var url = "api/measurements/" + measurementId;
                return httpFactory.put(url, measurement);
            }

            function createMeasurementUnit(id, measurementUnit) {
                var url = "api/measurements/" + id + "/units";
                return httpFactory.post(url, measurementUnit);
            }

            function deleteMeasurementUnit(measurementId, unitId) {
                var url = "api/measurements/" + measurementId + "/units/" + unitId;
                return httpFactory.delete(url);
            }

            function updateMeasurementUnit(measurementId, measurementUnit) {
                var url = "api/measurements/" + measurementId + "/units/" + measurementUnit.id;
                return httpFactory.put(url, measurementUnit);
            }
        }
    }
);