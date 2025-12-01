define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('PrintService', PrintService);

        function PrintService($q, httpFactory) {


            return {
                printPreview: printPreview
            };
            function printPreview(fileType, print) {
                var url = "api/plm/print";
                url += "?fileType={0}".format(fileType);
                return httpFactory.post(url, print);
            }

        }
    }
);