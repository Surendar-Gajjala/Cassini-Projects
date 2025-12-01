define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MailServerService', MailServerService);

        function MailServerService(httpFactory) {
            var baseUrl = "api/col/mailServers";
            return {

                createMailServer: createMailServer,
                updateMailServer: updateMailServer,
                getAllMailServers: getAllMailServers,
                getMailServer: getMailServer,
                deleteMailServer: deleteMailServer,

                createObjectMailSettings: createObjectMailSettings,
                updateObjectMailSettings: updateObjectMailSettings,
                getAllObjectMailSettings: getAllObjectMailSettings,
                getObjectMailSetting: getObjectMailSetting,
                deleteObjectMailSetting: deleteObjectMailSetting

            };

            function createObjectMailSettings(mailServer) {
                return httpFactory.post(baseUrl + "/objectMailSettings", mailServer);
            }

            function updateObjectMailSettings(mailServer) {
                var url = baseUrl + "/objectMailSettings/" + mailServer.objectId;
                return httpFactory.put(url, mailServer);
            }

            function getAllObjectMailSettings() {
                return httpFactory.get(baseUrl + "/objectMailSettings/all");
            }

            function getObjectMailSetting(id) {
                return httpFactory.get(baseUrl + "/objectMailSettings/" + id);
            }

            function deleteObjectMailSetting(id) {
                return httpFactory.delete(baseUrl + "/objectMailSettings/" + id);
            }

            function createMailServer(mailServer) {
                return httpFactory.post(baseUrl, mailServer);
            }

            function updateMailServer(mailServer) {
                var url = baseUrl + "/" + mailServer.id;
                return httpFactory.put(url, mailServer);
            }

            function getAllMailServers() {
                return httpFactory.get(baseUrl + "/all");
            }

            function getMailServer(id) {
                return httpFactory.get(baseUrl + "/" + id);
            }

            function deleteMailServer(id) {
                return httpFactory.delete(baseUrl + "/" + id);
            }
        }
    }
);

