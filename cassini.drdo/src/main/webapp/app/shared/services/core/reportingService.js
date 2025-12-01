define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ReportingService', ReportingService);

        function ReportingService(httpFactory) {
            return {
                getSystemSummary: getSystemSummary,
                getMissileSummary: getMissileSummary
            };

            function getSystemSummary(id) {
                var url = 'api/drdo/reporting/summary/systems/' + id;
                return httpFactory.get(url);
            }

            function getMissileSummary(id) {
                var url = 'api/drdo/reporting/summary/missiles/' + id;
                return httpFactory.get(url);
            }

        }
    }
);