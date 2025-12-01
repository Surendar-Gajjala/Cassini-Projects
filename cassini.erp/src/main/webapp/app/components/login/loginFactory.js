define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function (app) {
        app.factory('loginFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        login: function (user, password) {
                            var dfd = $q.defer(),
                                url = "api/security/login/" + user + "/" + password;
                            return httpFactory.get(url);
                        },

                        logout: function () {
                            var dfd = $q.defer(),
                                url = "api/security/logout";
                            return httpFactory.get(url);
                        },

                        current: function () {
                            var dfd = $q.defer(),
                                url = "api/security/session/current";
                            return httpFactory.get(url);
                        },

                        createLogin: function(login, phone, email) {
                            var dfd = $q.defer(),
                                url = "api/security/login?phone={0}&email={1}".format(phone, email);
                            return httpFactory.post(url, login);
                        },

                        getLogins: function(pageable) {
                            var dfd = $q.defer(),
                                url = "api/security/login?page={0}&size={1}".format(pageable.page-1, pageable.size);
                            return httpFactory.get(url);
                        },

                        resetPassword: function(login) {
                            var dfd = $q.defer(),
                                url = "api/security/login/resetpassword";
                            return httpFactory.post(url, login);
                        },

                        changePassword: function(oldPassword, newPassword) {
                            var dfd = $q.defer(),
                                url = "api/security/login/changepassword?oldPassword={0}&newPassword={1}".
                                            format(oldPassword, newPassword);
                            return httpFactory.get(url);
                        },

                        setMobileDevice: function(sessionId, device) {
                            var url = "api/security/session/" + sessionId + "/mobiledevice";
                            return httpFactory.post(url, device);
                        },

                        disablePushNotification: function(sessionId) {
                            var url = "api/security/session/" + sessionId + "/mobiledevice/disablenotification";
                            return httpFactory.get(url);
                        },

                        enablePushNotification: function(sessionId) {
                            var url = "api/security/session/" + sessionId + "/mobiledevice/enablenotification";
                            return httpFactory.get(url);
                        }
                    }
                }
            ]);
});