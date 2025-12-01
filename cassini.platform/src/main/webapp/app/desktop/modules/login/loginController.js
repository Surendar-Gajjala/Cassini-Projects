define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'messageformat',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/forgeService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/checkPortalController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/resetPasswordController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/newPasswordController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/updateEmailController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/updateLicenceController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/twoFactorAuthenticationController',
        'app/assets/bower_components/cassini-platform/js/utils/uiUtils',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/appDetailsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/themeSwitcherController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService'

    ],
    function (module, MessageFormat) {
        module.controller('LoginController', LoginController);

        function LoginController($scope, $rootScope, $state, $stateParams, $timeout, $cookies, $window, $http, $interval,
                                 $application, $translate, LoginService, ForgeService, AppDetailsService, CommonService, LicenseService) {
            var vm = this;

            window.MessageFormat = MessageFormat;
            var parse = angular.element("<div></div>");

            MessageFormat.locale.es = function (n) {
                return n === 1 ? "one" : "other"
            };

            MessageFormat.locale.de = function (n) {
                return n === 1 ? "one" : "other"
            };
            var incorrectMessage = $translate.instant("INCORRECT_MESSAGE");
            vm.hasError = false;
            vm.errorMessage = incorrectMessage;
            vm.loggingIn = false;
            vm.changeLanguage = changeLanguage;
            vm.userName = $cookies.get('CASSINI_USERNAME_');
            vm.password = null;
            vm.language = null;
            vm.defaultLanguage = null;
            vm.getCookies = getCookies;
            vm.setCookies = setCookies;
            vm.clearCookies = clearCookies;
            vm.remember = true;

            $rootScope.view = 'login';

            vm.login = login;
            vm.resetPassword = resetPassword;

            $scope.verifiedOtp = null;
            $scope.loginName = null;

            $scope.showLoginView = showLoginView;
            $scope.showNewPasswordView = showNewPasswordView;

            vm.pass = null;
            vm.user = null;

            vm.random = Math.floor((Math.random() * 1000000) + 1);

            vm.pass = parse.html($translate.instant("PASSWORD")).html();
            vm.user = parse.html($translate.instant("USERNAME")).html();
            $rootScope.licensesFraud = false;
            $scope.$on('$viewContentLoaded', function () {
                hidePreloader();
                window.$("#appview").show();
            });


            $rootScope.showUpdateEmail = showUpdateEmail;
            function showUpdateEmail(person, formType) {
                $rootScope.personObject = person;
                $rootScope.view = "updateEmail";
                $rootScope.emailFormType = formType;
            }

            $rootScope.showTwoFactorAuthentication = showTwoFactorAuthentication;
            function showTwoFactorAuthentication(type) {
                $rootScope.view = "twoFactorAuthentication";
                if (type == "sendPasscode") {
                    $timeout(function () {
                        $rootScope.resetTwoFactorAuthentication();
                    }, 1500);
                } else {
                    $rootScope.hideBusy();
                }
            }

            $rootScope.showLicenceForm = showLicenceForm;
            function showLicenceForm() {
                $rootScope.view = "updateLicence";
            }

            vm.loginDTO = {
                login: {
                    loginName: null,
                    password: null,
                    person: null,
                    id: null,
                    isActive: false,
                    external: false,
                    oldPassword: null,
                    newPassword: null,
                    flag: false
                }
            };

            vm.languageKey = 'en';
            $rootScope.emailFormType = "update";
            vm.systemTheme = null;
            vm.systemDateFormat = null;
            vm.loadUserPreference = loadUserPreference;
            $rootScope.applicationDateFormat = null;
            function loadUserPreference(loginId) {
                LoginService.getUserPreference(loginId).then(
                    function (data) {
                        $rootScope.userPreferences = data;
                        if ($rootScope.userPreferences != null) {

                            if ($rootScope.userPreferences.userTheme != null && $rootScope.userPreferences.userTheme != "") {
                                $rootScope.switchTheme($rootScope.userPreferences.userTheme, "refresh");
                            }
                        }
                        else {
                            $rootScope.switchTheme("slategrey", "refresh");
                        }
                    }
                );
            }

            function changeTheme(el) {
                var theme = el;
                if (theme == "Blue") {
                    $('#' + el).css('display', 'none');
                    $("#footer").css('background-color', "#1E5A8A");
                    $(".headerbar").css('background-color', "#2a6fa8");
                }
                if (theme != null && theme != "Blue") {
                    $('#' + el).css('display', 'block');
                    $("#footer").css('background-color', el);
                    $(".headerbar").css('background-color', el);
                } else {
                    $('#' + el).css('display', 'none');
                    $("#footer").css('background-color', "#1E5A8A");
                    $(".headerbar").css('background-color', "#2a6fa8");
                }
            }

            function setHeaders(session) {
                if (session.accessToken != "" && session.accessToken != null && session.accessToken != undefined) {
                    $http.defaults.headers.common['Authorization'] = 'Bearer ' + session.accessToken;
                    $window.localStorage.setItem('token', session.accessToken);
                }
                if (session.refreshToken != "" && session.refreshToken != null && session.refreshToken != undefined) {
                    $http.defaults.headers.common['X-Refresh-Token'] = session.refreshToken;
                    $window.localStorage.setItem('refreshToken', session.refreshToken);
                }
            }

            $rootScope.localStorageLogin = null;
            function login() {
                $rootScope.localStorageLogin = null;
                $rootScope.loginPersonDetails = null;
                vm.hasError = false;
                vm.loggingIn = true;
                $rootScope.userPreferences = null;
                if (validate()) {
                    if (vm.password.match("#")) {
                        vm.password = vm.password.replace((/#/g, '%23'));
                    }

                    vm.loginDTO.login.loginName = vm.userName;
                    vm.loginDTO.login.password = vm.password;

                    $window.localStorage.removeItem('token');
                    $window.localStorage.removeItem('refreshToken');

                    LoginService.login(vm.loginDTO).then(
                        function (session) {
                            setHeaders(session.session);
                            vm.loggingIn = false;
                            initApp();
                            loadUserPreference(session.session.login.id);
                            //remove the sample_data and env saved data from localStorage
                            localStorage.clear();
                            localStorage.setItem("local_storage_login", JSON.stringify(session.session));
                            if (localStorage.getItem('local_storage_login') != null) {
                                $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                            }
                            window.$application.session = session.session;
                            $rootScope.personGroups1 = session.session.login.groups;
                            $rootScope.loginPersonDetails = session.session.login;
                            $rootScope.personInfo = session.session.login.person;
                            window.$application.login = session.session.login;

                            $cookies.put('CASSINI_USERNAME_', vm.userName);
                            if (window.$application.session != null || window.$application.session != undefined) {
                                if (window.$application.session.login != null || window.$application.session.login != undefined) {
                                    if (window.$application.session.login.person != null || window.$application.session.login.person != undefined) {
                                        //checkPortalAccount();
                                        proceedLogin();
                                    }
                                }
                            }

                            if (vm.remember) {
                                setCookies();
                            } else {
                                clearCookies();
                            }
                            $rootScope.$broadcast("app.notification.login", {});

                            //$state.go('app.home', {}, {reload: true});

                        },

                        function (error) {
                            vm.hasError = true;
                            vm.loggingIn = false;
                            vm.errorMessage = error.message;
                        }
                    );
                } else {
                    vm.loggingIn = false;
                    vm.hasError = true;
                }
            }

            function proceedLogin() {
                LicenseService.isLicenseValid().then(
                    function (data) {
                        if (data.licensesFraud) {
                            $rootScope.licensesFraud = true;
                            $rootScope.showLicenceForm();
                        } else if (data.valid) {
                            $application.validateLicense = true;
                            if (window.$application.session.login.person.email == null ||
                                window.$application.session.login.person.email === undefined ||
                                window.$application.session.login.person.email === "") {
                                $timeout(function () {
                                    $rootScope.showUpdateEmail(window.$application.session.login.person, "update")
                                }, 1000)
                            } else {
                                if (!window.$application.session.login.person.emailVerified) {
                                    $timeout(function () {
                                        $rootScope.showUpdateEmail(window.$application.session.login.person, "verify")
                                    }, 1000)
                                } else if (window.$application.session.hasTwoFactorAuthentication) {
                                    showTwoFactorAuthentication("show");
                                } else {
                                    $application.validateLicense = true;
                                    $state.go('app.home', {}, {reload: true});
                                }
                            }
                            $window.localStorage.setItem('validateLicense', true);
                        } else if (!data.valid) {
                            LicenseService.getNoOfDaysToExpire().then(
                                function (data1) {
                                    $window.localStorage.setItem('validateLicense', false);
                                    $rootScope.showGracePeriod = true;
                                    $rootScope.showLicenceEnterForm = true;
                                    $rootScope.hasLicenseError = false;
                                    $rootScope.gracePeriod = data1;
                                    $rootScope.showLicenceForm();
                                }
                            )
                        }
                    }, function (error) {
                        $rootScope.showLicenceEnterForm = true;
                        $rootScope.showGracePeriod = false;
                        $rootScope.errorLicenseMessage = error.message;
                        $rootScope.showLicenceForm();
                    });
            }

            function checkPortalAccount() {
                LoginService.checkPortal().then(
                    function (data) {
                        if (data != null && data !== undefined && data) {
                            proceedLogin();
                        }
                        else {
                            $rootScope.view = "portal";
                        }
                    },
                    function (error) {
                        $rootScope.view = "portal";
                    }
                )
            }

            var usernameMessage = $translate.instant("USERNAME_MESSAGE");
            var passwordMessage = $translate.instant("PASSWORD_MESSAGE");

            function validate() {
                var valid = true;
                if (vm.userName == null || vm.userName == "" || vm.userName == undefined) {
                    vm.errorMessage = usernameMessage;
                    valid = false;
                } else if (vm.password == null || vm.password == "" || vm.password == undefined) {
                    vm.errorMessage = passwordMessage;
                    valid = false;
                }

                return valid;
            }

            function checkSession() {
                LoginService.current().then(
                    function (session) {
                        if (session != null || session != "") {
                            //$state.go('app.home');
                        }
                    }
                );
            }

            function resetPassword() {
                vm.hasError = false;
                vm.errorMessage = "";
                $rootScope.view = 'reset';
            }

            function showLoginView() {
                $rootScope.view = 'login';
            }

            function showNewPasswordView() {
                $rootScope.view = "newPassword";
            }


            function changeLanguage(langKey) {
                LoginService.changeLanguage(langKey).then(
                    function (data) {
                        vm.languageKey = langKey;
                        $translate.use(langKey)
                            .then(function (languageId) {
                                $window.localStorage.setItem("language", languageId);
                                incorrectMessage = parse.html($translate.instant("INCORRECT_MESSAGE")).html();
                                vm.pass = parse.html($translate.instant("PASSWORD")).html();
                                vm.user = parse.html($translate.instant("USERNAME")).html();
                                usernameMessage = parse.html($translate.instant("USERNAME_MESSAGE")).html();
                                passwordMessage = parse.html($translate.instant("PASSWORD_MESSAGE")).html();
                            });

                    }, function (error) {
                        $rootScope.showErrorMessage("Language not Changes");
                    }
                )

            }

            function validateJSON(key) {
                try {
                    $window.localStorage.getItem(key);
                } catch (e) {
                    return false;
                }
                return true;
            }

            function getCookies() {
                /*if (validateJSON("CASSINI_USERNAME_")) {
                 vm.userName = JSON.parse($window.localStorage.getItem("CASSINI_USERNAME_"));
                 }
                 if (validateJSON("CASSINI_PASSWORD_")) {
                 vm.password = JSON.parse($window.localStorage.getItem("CASSINI_PASSWORD_"));
                 }*/
            }

            function setCookies() {
                $window.localStorage.setItem("CASSINI_USERNAME_", JSON.stringify(vm.userName));
            }

            function clearCookies() {
                $window.localStorage.removeItem('CASSINI_USERNAME_');
                getCookies();
            }


            $rootScope.showAppLanguage = true;
            $rootScope.AppPassWordStrength = null;
            $rootScope.AppForgeEnable = null;
            $rootScope.AppDefaultLanguage = null;
            $rootScope.AppEnableLoginAttempts = false;
            $rootScope.AppLoginAttempts = null;

            vm.listDetails = [];
            function loadAppSettingsDetails() {
                vm.listDetails = [];
                AppDetailsService.getAppDetails().then(
                    function (data) {
                        vm.listDetails = data;
                        angular.forEach(vm.listDetails, function (option) {
                            if (option.optionKey == 6) {
                                $rootScope.showAppLanguage = (option.value.toLowerCase() === 'true');
                            }

                            if (option.optionKey == 8) {
                                $rootScope.AppForgeEnable = option.value;
                            }
                            if (option.optionKey == 9) {
                                if (option.value == 'English') {
                                    $rootScope.AppDefaultLanguage = 'en';
                                }
                                if (option.value == 'German') {
                                    $rootScope.AppDefaultLanguage = 'de';
                                }
                                var localLangKey = $window.localStorage.getItem("language");
                                vm.defaultLanguage = $rootScope.AppDefaultLanguage;
                                if (vm.defaultLanguage != null || vm.defaultLanguage != undefined || vm.defaultLanguage != "") {
                                    if (localLangKey != null) {
                                        changeLanguage(localLangKey);
                                    }
                                    else {
                                        changeLanguage(vm.defaultLanguage);
                                    }

                                }

                            }

                            if (option.optionKey == 11) {
                                $rootScope.AppLoginAttempts = option.value;

                            }
                        });

                    }
                )
            }

            function loadPreferences() {
                CommonService.getPreferences().then(
                    function (data) {
                        angular.forEach(data, function (item) {
                            if (item.preferenceKey == "SYSTEM.THEME") {
                                vm.systemTheme = item.stringValue;
                            }
                            if (item.preferenceKey == "SYSTEM.DATE.FORMAT") {
                                vm.systemDateFormat = item.stringValue;
                            }
                        })

                    }
                )
            }

            function initApp() {
                loadPreferences();
                if (validateJSON("language")) {
                    var language = $window.localStorage.getItem("language");
                } else {
                    var language = vm.defaultLanguage;
                }
                if (language != null && language != undefined) {
                    changeLanguage(language);
                }

                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }

                getCookies();
            }

            var imgIndex = randomInteger(1, 8);
            var images = [
                'electric-vehicle.jpg', 'hightech-electronic.jpg', 'space-tech.jpg',
                'industrial-automation.jpg', 'electric-bike.jpg', 'cad-team.jpg',
                'digital-manufacturing.jpg', 'medical-tech.jpg', 'iot.jpg'
            ];

            $.fn.preload = function () {
                this.each(function () {
                    $('<img/>')[0].src = this;
                });
            };

            function randomInteger(min, max) {
                return Math.floor(Math.random() * (max - min + 1)) + min;
            }

            function preLoadImages() {
                var imgs = [];
                angular.forEach(images, function (image) {
                    imgs.push('app/assets/images/login/' + image);
                });

                $(imgs).preload();
            }

            var quotes = null;
            var quoteIndex = -1;

            function showNextQuote() {
                if (quotes == null) {
                    quotes = $(".quotes");
                }
                ++quoteIndex;
                quotes
                    .eq(quoteIndex % quotes.length)
                    .fadeIn(2000)
                    .delay(2000)
                    .fadeOut(2000, showNextQuote);
            }

            $rootScope.showBusy = function (parent) {
                var w = null;
                var h = null;
                if (parent != null && parent != undefined) {
                    var pos = $(parent).offset();
                    w = $(parent).outerWidth();
                    h = $(parent).outerHeight();

                    $('#busy-indicator').css({top: pos.top, left: pos.left, width: w, height: h})
                }
                else {
                    w = $(window).outerWidth();
                    h = $(window).outerHeight();
                    $('#busy-indicator').css({top: 0, left: 0, width: w, height: h})
                }
                $('#busy-indicator').show();
            };

            $rootScope.hideBusy = function () {
                $('#busy-indicator').hide();
            };

            (function () {
                loadAppSettingsDetails();
                initPreloader();
                checkSession();
                if ($rootScope.hideBusyIndicator !== undefined && $rootScope.hideBusyIndicator != null) {
                    $rootScope.hideBusyIndicator();
                }

                preLoadImages();

                $scope.$on('$viewContentLoaded', function () {
                    $timeout(function () {
                        showNextQuote();
                    }, 1000);
                });

                $timeout(function () {
                    vm.hasError = false;
                    vm.errorMessage = null;
                }, 1000 * 60 * 5);
            })();
        }
    }
);