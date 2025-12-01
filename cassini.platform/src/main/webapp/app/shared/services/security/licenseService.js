define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('LicenseService', LicenseService);

        function LicenseService(httpFactory) {
            return {
                getLicense: getLicense,
                saveLicense: saveLicense,
                isLicenseValid: isLicenseValid,
                getNoOfDaysToExpire: getNoOfDaysToExpire,
                checkActiveUserLicenses: checkActiveUserLicenses
            };

            function getLicense() {
                var url = "api/license";
                return httpFactory.get(url);
            }

            function saveLicense(license) {
                var url = "api/license";
                return httpFactory.post(url, license);
            }

            function isLicenseValid() {
                var url = "api/license/validate";
                return httpFactory.get(url);
            }

            function getNoOfDaysToExpire() {
                var url = "api/license/days/expire";
                return httpFactory.get(url);
            }

            function checkActiveUserLicenses(license) {
                var url = "api/license/check/active/users";
                return httpFactory.post(url, license);
            }

        }
    }
);