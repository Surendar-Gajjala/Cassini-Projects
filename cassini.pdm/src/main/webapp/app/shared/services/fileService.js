define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('FileService', FileService);

        function FileService(httpFactory) {
            return {
                search: search,
                getFiles: getFiles
            };

            function search(text) {
                var url = "api/pdm/vaults/folders/-1/files/freesearch/" + text;
                return httpFactory.get(url);
            }

            function getFiles() {
                var url = "api/pdm/vaults/folders/-1/files/latest";
                return httpFactory.get(url);
            }
        }
    }
);