define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ReqIFService', ReqIFService);

        function ReqIFService($q, httpFactory) {
            return {
                convertToReqIF: convertToReqIF,
                convertToCassini: convertToCassini
            };

            function convertToReqIF(specId) {
                var url = "/api/integration/reqif/toreqif/" + specId;
                return httpFactory.get(url);
            }

            function convertToCassini(file) {
                var url = "/api/integration/reqif/import";
                return httpFactory.upload(url, file);
            }
        }
    }
);
