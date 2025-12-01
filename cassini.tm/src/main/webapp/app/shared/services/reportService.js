define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ReportService', ReportService);

        function ReportService(httpFactory) {
            return {

            };


        }
    }
)
;