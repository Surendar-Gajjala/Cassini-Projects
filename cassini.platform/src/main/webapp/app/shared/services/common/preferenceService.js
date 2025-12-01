/**
 * Created by Nageshreddy on 13-11-2019.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('PreferenceService', PreferenceService);

        function PreferenceService(httpFactory) {


            return {
                createPreference: createPreference,
                updatePreference: updatePreference,
                getUserChangeApprovalPassword: getUserChangeApprovalPassword,
                createChangeApprovalPassword: createChangeApprovalPassword,
                getPreferenceByKey: getPreferenceByKey,
                checkChangeApprovalPassword: checkChangeApprovalPassword,
                getPreferencesByContext: getPreferencesByContext
            };

            function createPreference(preference) {
                var url = "api/preferences";
                return httpFactory.post(url, preference);
            }

            function updatePreference(preference) {
                var url = "api/preferences/" + preference.id;
                return httpFactory.put(url, preference);
            }

            function getUserChangeApprovalPassword(loginId) {
                var url = "api/preferences/changeApprovalPassword/" + loginId;
                return httpFactory.get(url);
            }

            function createChangeApprovalPassword(preference) {
                var url = "api/preferences/changeApprovalPassword";
                return httpFactory.post(url, preference);
            }

            function getPreferenceByKey(key) {
                var url = "api/preferences/preferenceByKey?key=" + key;
                return httpFactory.get(url);
            }

            function getPreferencesByContext(context) {
                var url = "api/preferences/context/" + context;
                return httpFactory.get(url);
            }

            function checkChangeApprovalPassword(loginId, password) {
                var url = "api/preferences/checkPassword/" + loginId + "?password=" + password;
                return httpFactory.get(url);
            }

        }
    }
);
