/**
 * Created by Nageshreddy on 13-11-2019.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('AppDetailsService', AppDetailsService);

        function AppDetailsService(httpFactory) {


            return {
                getAppDetails: getAppDetails,
                updateAppDetails: updateAppDetails,
                getSystemInfo: getSystemInfo,
                systemInformation: systemInformation,
                getFileSystemPath: getFileSystemPath,
                saveIpAddress: saveIpAddress

            };

            function getAppDetails() {
                var url = "api/app/details";
                return httpFactory.get(url);
            }

            function systemInformation() {
                var url = "api/app/details/system/information";
                return httpFactory.get(url);
            }

            function updateAppDetails(details) {
                var url = "api/app/details";
                return httpFactory.put(url, details)
            }

            function getSystemInfo() {
                var url = "api/plm/items/systemInfo";
                return httpFactory.get(url);
            }

            function getFileSystemPath() {
                var url = "api/app/details/filesystem/path";
                return httpFactory.get(url);
            }

            function saveIpAddress(address) {
                var url = "api/app/details/ipAddress";
                return httpFactory.post(url, address);
            }

        }
    }
);
