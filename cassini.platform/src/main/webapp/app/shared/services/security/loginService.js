define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('LoginService', LoginService);

        function LoginService(httpFactory) {
            return {
                login: login,
                logout: logout,
                current: current,
                checkPortal: checkPortal,
                setupPortalAccount: setupPortalAccount,
                createLogin: createLogin,
                getLogins: getLogins,
                getLogin: getLogin,
                getAllLogins: getAllLogins,
                getLoginName: getLoginName,
                updateLogin: updateLogin,
                resetPassword: resetPassword,
                verifyOtp: verifyOtp,
                setNewPassword: setNewPassword,
                changePassword: changePassword,
                changePasswordByPersonId: changePasswordByPersonId,
                setMobileDevice: setMobileDevice,
                getIsActiveLogins: getIsActiveLogins,
                changeLanguage: changeLanguage,
                getLoginReferences: getLoginReferences,
                verifyLogin: verifyLogin,
                getActiveLogins: getActiveLogins,
                getAllActiveLogins: getAllActiveLogins,
                passwordReset: passwordReset,
                updateUserPreference: updateUserPreference,
                getUserPreference: getUserPreference,
                savePreferredPage: savePreferredPage,
                createLoginPerson: createLoginPerson,
                getFilteredLogins: getFilteredLogins,
                getLoginByPerson: getLoginByPerson,
                resetUserPassword: resetUserPassword,
                getInternalActiveLogins: getInternalActiveLogins,
                getIsActiveAndExternalLoginsCount: getIsActiveAndExternalLoginsCount,
                getUsersCount: getUsersCount,
                getAllFilteredActiveLogins: getAllFilteredActiveLogins,
                resetTwoFactorAuthenticationPassword: resetTwoFactorAuthenticationPassword,
                verifyTwoFactorAuthenticationPassword: verifyTwoFactorAuthenticationPassword,
                verifyPersonEmail: verifyPersonEmail,
                resendPersonEmailPasscode: resendPersonEmailPasscode
            };

            function getIsActiveLogins(active) {
                var url = "api/security/login/byActive/" + active;
                return httpFactory.get(url);
            }

            function getIsActiveAndExternalLoginsCount(active, external) {
                var url = "api/security/login/byActive/" + active + "/external/" + external;
                return httpFactory.get(url);
            }

            function login(login) {
                var url = "api/security/login/validate" + "?token=" + (new Date()).getTime();
                return httpFactory.post(url, login);
            }

            function logout() {
                var url = "api/security/logout";
                return httpFactory.get(url);
            }

            function current() {
                var url = "api/security/session/current?bust=" + (new Date()).getTime();
                return httpFactory.get(url);
            }

            function createLogin(login, phone, email) {
                var url = "api/security/login?phone={0}&email={1}".format(phone, email);
                return httpFactory.post(url, login);
            }

            function createLoginPerson(login) {
                var url = "api/security/login/person";
                return httpFactory.post(url, login);
            }

            function getLoginByPerson(personId) {
                var url = "api/security/login/person/" + personId;
                return httpFactory.get(url);
            }

            function getLogin(loginId) {
                var url = "api/security/login/" + loginId;
                return httpFactory.get(url);
            }

            function getLoginName(loginId) {
                var url = "api/security/loginName/" + loginId;
                return httpFactory.get(url);
            }

            function updateLogin(login) {
                var url = "api/security/" + login.id;
                return httpFactory.put(url, login);
            }

            function getLogins(pageable) {
                var url = "api/security/login?page={0}&size={1}".format(pageable.page - 1, pageable.size);
                return httpFactory.get(url);
            }

            function getAllActiveLogins(pageable) {
                var url = "api/security/login/active?page={0}&size={1}".format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getAllLogins() {
                var url = "api/security/login/all";
                return httpFactory.get(url);
            }

            function getFilteredLogins(pageable, filters) {
                var url = "api/security/logins/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function resetPassword(login) {
                var url = "api/security/login/resetpwd";
                return httpFactory.post(url, login);
            }

            function verifyOtp(login) {
                var url = "api/security/login/resetpwd/verify";
                return httpFactory.post(url, login);
            }

            function setNewPassword(login) {
                var url = "api/security/login/newpassword";
                return httpFactory.post(url, login);
            }

            function changePassword(login) {
                var url = "api/security/login/changepassword";
                return httpFactory.post(url, login);
            }

            function changePasswordByPersonId(personId, oldPassword, newPassword) {
                var url = "api/security/login/changepassword/" + personId + "?oldPassword={0}&newPassword={1}".
                        format(oldPassword, newPassword);
                return httpFactory.get(url);
            }

            function setMobileDevice(sessionId, device) {
                var url = "api/security/session/" + sessionId + "/mobiledevice";
                return httpFactory.post(url, device);
            }

            function changeLanguage(language) {
                var url = "api/security/language/" + language;
                return httpFactory.get(url);
            }

            function getLoginsByIds(ids) {
                var url = "api/security/login/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getLoginReferences(objects, property) {
                var personIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && personIds.indexOf(object[property]) == -1) {
                        personIds.push(object[property].id);
                    }
                });

                if (personIds.length > 0) {
                    getLoginsByIds(personIds).then(
                        function (logins) {
                            var map = new Hashtable();
                            angular.forEach(logins, function (login) {
                                map.put(login.person.id, login);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var login = map.get(object[property].id);
                                    if (login != null) {
                                        object[property + "Object"] = login;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function verifyLogin(user, password) {
                password = encodeURI(password);
                var url = "api/security/login/verify/" + user + "/" + password + "?token=" + (new Date()).getTime();
                return httpFactory.get(url);
            }

            function getActiveLogins() {
                var url = "api/security/logins/active";
                return httpFactory.get(url);
            }

            function getInternalActiveLogins() {
                var url = "api/security/logins/active/internal";
                return httpFactory.get(url);
            }

            function passwordReset(resetPassword) {
                var url = "api/security/login/passwordReset";
                return httpFactory.post(url, resetPassword);
            }


            function updateUserPreference(userPreference) {
                var url = "api/security/login/preferences";
                return httpFactory.post(url, userPreference);
            }

            function getUserPreference(loginId) {
                var url = "api/security/login/" + loginId + "/preferences";
                return httpFactory.get(url);
            }

            function savePreferredPage(userPreference) {
                var url = "api/security/preferredPage";
                return httpFactory.post(url, userPreference);
            }

            function checkPortal() {
                var url = "api/security/checkportal";
                return httpFactory.get(url);
            }

            function setupPortalAccount(portalAccount) {
                var url = "api/security/checkportal";
                return httpFactory.post(url, portalAccount);
            }

            function resetUserPassword(login) {
                var url = "api/security/login/" + login + "/resetpwd";
                return httpFactory.post(url);
            }

            function resetTwoFactorAuthenticationPassword(loginId) {
                var url = "api/security/login/twofactorauthentication/reset";
                return httpFactory.post(url, loginId);
            }

            function verifyTwoFactorAuthenticationPassword(loginId, passcode) {
                var url = "api/security/login/" + loginId + "/twofactorauthentication/verify";
                return httpFactory.post(url, passcode);
            }

            function verifyPersonEmail(personId, passcode) {
                var url = "api/security/person/" + personId + "/email/verify";
                return httpFactory.post(url, passcode);
            }

            function resendPersonEmailPasscode(personId) {
                var url = "api/security/person/email/passcode";
                return httpFactory.post(url, personId);
            }

            function getUsersCount() {
                var url = "api/security/users/count";
                return httpFactory.get(url);
            }

            function getAllFilteredActiveLogins(pageable, filters) {
                var url = "api/security/login/active/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }
        }
    }
);