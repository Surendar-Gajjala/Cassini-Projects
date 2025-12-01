define(
    [
        'app/desktop/modules/irsteLogin/irsteLogin.module',
        'messageformat',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/resetPasswordController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/newPasswordController',
        'app/assets/bower_components/cassini-platform/js/utils/uiUtils'
    ],
    function (module) {
        module.controller('IrsteLoginController', IrsteLoginController);

        function IrsteLoginController($scope, $rootScope, $state, $stateParams, $timeout, $cookies, $window, $http,
                                      $application, $translate, LoginService, CommonService) {
            var vm = this;


            var parse = angular.element("<div></div>");

            var incorrectMessage = $translate.instant("INCORRECT_MESSAGE");
            vm.hasError = false;
            vm.errorMessage = incorrectMessage;
            vm.loggingIn = false;
            vm.changeLanguage = changeLanguage;
            vm.userName = $cookies.get('CASSINI_USERNAME_');
            vm.password = null;
            vm.language = null;
            vm.remember = true;
            vm.getCookies = getCookies;
            vm.setCookies = setCookies;
            vm.clearCookies = clearCookies;

            $http.get('application.json').success(function (data) {
                vm.language = data.showLanguage;
                $application.config = data;
            });

            $rootScope.view = 'login';

            vm.login = login;
            vm.resetPassword = resetPassword;

            $scope.verifiedOtp = null;
            $scope.loginName = null;

            $scope.showLoginView = showLoginView;
            $scope.showNewPasswordView = showNewPasswordView;

            vm.pass = null;
            vm.user = null;

            vm.pass = parse.html($translate.instant("PASSWORD")).html();
            vm.user = parse.html($translate.instant("USERNAME")).html();

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

            $scope.$on('$viewContentLoaded', function () {
                setBgImage(getBgIndex());
                hidePreloader();
                window.$("#appview").show();
                $timeout(function () {
                    $('#loginContainer').animate({"margin-top": 0}, 'slow');
                }, 500);
            });


            var bgImages = [
                '1.jpg', '2.jpg', '3.jpg', '4.png', '5.jpg', '6.jpg', '7.jpg', '8.jpg', '9.jpg', '10.jpg',
                '11.jpg', '12.jpg', '13.png', '14.jpg', '16.png', '17.jpg', '18.png', '19.jpg', '20.jpg',
                '21.jpg', '22.jpg', '23.jpg', '24.jpg', '25.jpg', '26.jpg', '27.jpg', '28.jpg', '29.jpg', '30.jpg',
                '31.jpg', '32.jpg', '33.jpg', '34.jpg', '35.jpg', '36.jpg', '37.jpg', '38.jpg', '39.jpg', '40.jpg',
                '41.jpg', '42.jpg', '43.jpg', '44.jpg', '45.jpg', '46.jpg', '47.jpg', '48.jpg', '49.jpg', '50.jpg',
                '51.jpg', '52.jpg', '53.jpg', '55.jpg', '56.jpg', '57.png', '58.jpg', '59.png', '60.png'
            ];

            function getCurrentBgIndex() {
                var bgIndex = $cookies.get('_BG_INDEX_');
                if (bgIndex == null || bgIndex == undefined || bgIndex > bgImages.length - 1) {
                    bgIndex = 0;
                }
                else {
                    bgIndex = parseInt(bgIndex);
                }
                return bgIndex;
            }

            function getBgIndex() {
                var bgIndex = getCurrentBgIndex();
                if (bgIndex == undefined) {
                    bgIndex = 0;
                }
                else {
                    bgIndex = bgIndex + 1;
                }
                $cookies.put('_BG_INDEX_', bgIndex);
                return bgIndex;
            }

            function setBgImage(index) {
                var image = bgImages[0];
                if (bgImages[index] != null && bgImages[index] != undefined) {
                    image = bgImages[index];
                }
                image = "33.jpg";
                $('html').css('background', 'url(app/assets/bower_components/cassini-platform/images/bg/desktop/' + image + ') no-repeat 0 0 fixed');
                $('body').css('background', 'inherit');
            }

            function login() {
                vm.hasError = false;
                vm.loggingIn = true;

                if (validate()) {
                    if (vm.password.match("#")) {
                        vm.password = vm.password.replace((/#/g, '%23'));
                    }

                    vm.loginDTO.login.loginName = vm.userName;
                    vm.loginDTO.login.password = vm.password;
                    LoginService.login(vm.loginDTO).then(
                        function (session) {
                            vm.loggingIn = false;
                            window.$application.session = session.session;
                            window.$application.login = session.session.login;
                            $rootScope.loginPersonDetails = session.session.login;
                            $cookies.put('CASSINI_USERNAME_', vm.userName);
                            if (vm.remember) {
                                setCookies();
                            } else {
                                clearCookies();
                            }
                            $rootScope.$broadcast("app.notification.login", {});
                            $rootScope.showWelcomePage = false;
                            $state.go('app.complaints.all');
                            CommonService.getPersonType(session.session.login.person.personType).then(
                                function (data) {
                                    $rootScope.personTypeDetails = data.name;
                                    if ($rootScope.personTypeDetails == "Administrator") {
                                        $rootScope.showUsersTab = true;
                                        $rootScope.showAssessorsTab = true;
                                    } else {
                                        $rootScope.showUsersTab = false;
                                        $rootScope.showAssessorsTab = false;
                                    }
                                }
                            );
                            $rootScope.checkSession();
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
                        $window.localStorage.setItem("language", JSON.stringify(langKey));
                        $translate.use(langKey);
                        incorrectMessage = parse.html($translate.instant("INCORRECT_MESSAGE")).html();
                        vm.pass = parse.html($translate.instant("PASSWORD")).html();
                        vm.user = parse.html($translate.instant("USERNAME")).html();
                        usernameMessage = parse.html($translate.instant("USERNAME_MESSAGE")).html();
                        passwordMessage = parse.html($translate.instant("PASSWORD_MESSAGE")).html();
                    }, function (error) {
                        $rootScope.showErrorMessage("Language not Changes");
                    }
                )

            }

            function validateJSON(key) {
                try {
                    JSON.parse($window.localStorage.getItem(key));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function getCookies() {
                if (validateJSON("CASSINI_USERNAME_")) {
                    vm.userName = JSON.parse($window.localStorage.getItem("CASSINI_USERNAME_"));
                }
                if (validateJSON("CASSINI_PASSWORD_")) {
                    vm.password = JSON.parse($window.localStorage.getItem("CASSINI_PASSWORD_"));
                }
            }

            function setCookies() {
                $window.localStorage.setItem("CASSINI_USERNAME_", JSON.stringify(vm.userName));
                $window.localStorage.setItem("CASSINI_PASSWORD_", JSON.stringify(vm.password));
            }

            function clearCookies() {
                $window.localStorage.removeItem('CASSINI_USERNAME_');
                $window.localStorage.removeItem('CASSINI_PASSWORD_');
                getCookies();
            }

            (function () {

                if (validateJSON("language")) {
                    var language = JSON.parse($window.localStorage.getItem("language"));
                } else {
                    var language = "en";
                }
                if (language != null && language != undefined) {
                    changeLanguage(language);
                }

                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }
                var sessionExpireMessage = $translate.instant("SESSION_EXPIRE_MESSAGE");
                if ($stateParams.expired != null &&
                    $stateParams.expired != undefined &&
                    $stateParams.expired == 'true') {
                    vm.hasError = true;
                    vm.errorMessage = sessionExpireMessage;
                }
                initPreloader();
                checkSession();
                getCookies();
            })();

        }
    }
);