define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ExImService', ExImService);

        function ExImService(httpFactory) {
            return {
                exportData: exportData,
                importData: importData,
                getHeadersFromFile: getHeadersFromFile,
                sendMappingHeaders: sendMappingHeaders,
                downloadObjectTypeTemplate: downloadObjectTypeTemplate,
                importUploadedObjectFile: importUploadedObjectFile
            };

            function exportData(objects) {
                var url = "api/plm/exim/export?objects=" + objects;
                return httpFactory.get(url);
            }

            function importData(file) {
                var url = "api/plm/exim/import";
                return httpFactory.upload(url, file);
            }

            function getHeadersFromFile(file) {
                var url = "api/plm/exim/get/headers";
                return httpFactory.upload(url, file);
            }

            function sendMappingHeaders(objectType, headers) {
                var url = "api/plm/exim/send/headers/"+objectType;
                return httpFactory.post(url, headers);
            }

            function downloadObjectTypeTemplate(objectType) {
                var url = "api/plm/exim/download/" + objectType + "/excel";
                return httpFactory.get(url);
            }

            function importUploadedObjectFile(objectType, file) {
                var url = "api/plm/exim/import/" + objectType + "/file";
                return httpFactory.upload(url, file);
            }
        }
    }
);