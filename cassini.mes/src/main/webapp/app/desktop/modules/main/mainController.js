define(['app/desktop/modules/main/main.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',

    ],
    function (module) {
        module.controller('MainController', MainController);

        function MainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                LoginService, CommonService, DialogService) {


            $rootScope.viewInfo = {
                icon: 'fa-home',
                title: 'Home'
            };


            $rootScope.notification = {
                class: 'fa-check',
                type: "alert-success",
                message: ""
            };


            var vm = this;

            vm.feedback = feedback;
            vm.showProfile = showProfile;
            vm.logout = logout;


            function feedback() {
                $state.go('app.help.feedback');
            }

            function showProfile(login) {
                $state.go('app.admin.logindetails', {loginId: login.id});
            }

            function checkSession() {
                LoginService.current().then(
                    function (session) {
                        if (session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.user = session.login;
                            CommonService.initialize();
                        }
                    }
                );
            }

            function logout() {
                DialogService.confirmLogout(function (yes) {
                    if (yes == true) {
                        LoginService.logout().then(
                            function (data) {
                                vm.logout = data;
                                $rootScope.$broadcast("app.notifications.logout");
                                $state.go('login', {}, {reload: true});
                            },

                            function (error) {
                                console.error(error);
                            }
                        );
                    }
                })
            }

            $rootScope.closeNotification = function () {
                hideNotification();
            };

            $rootScope.showNotification = function (message, type) {
                $rootScope.notification.type = type;
                $rootScope.notification.message = message;
                showNotification();
            };

            $rootScope.showSuccessMessage = function (message) {
                $rootScope.notification.check = 'fa-check';
                $rootScope.showNotification(message, 'alert-success');
            };

            $rootScope.showErrorMessage = function (message) {
                $rootScope.notification.class = 'fa-ban';
                $rootScope.showNotification(message, 'alert-danger');
            };

            $rootScope.showWarningMessage = function (message) {
                $rootScope.notification.class = 'fa-warning';
                $rootScope.showNotification(message, 'alert-warning');
            };

            $rootScope.showInfoMessage = function (message) {
                $rootScope.notification.class = 'fa-info-circle';
                $rootScope.showNotification(message, 'alert-info');
            };

            (function () {
                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }

                $scope.$on('$viewContentLoaded', function () {
                    initNotificationPanel();

                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams) {
                            hideNotification();

                            $timeout(function () {
                                positionNotification();
                            }, 500);
                        }
                    );
                });
                checkSession();
            })();
        }
    }
);
