define(['app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('AppNavController', AppNavController);

        function AppNavController($scope, $rootScope, LoginService, $timeout, $state, $cookies) {
            var vm = this;
            vm.permissions = $application.login.permissions;
            if (vm.permissions == undefined) {
                checkSession();

            }
            //static for testing
            vm.navigation = [{
                "id": "admin",
                "text": "Admin",
                "icon": "fa-database",
                "state": null,
                "active": false,
                "children": [{
                    "id": "admin.dashboard",
                    "text": "Dashboard",
                    "icon": "fa-tachometer nav-icon-font",
                    "state": "app.admin.logins",
                    "active": false,
                    "children": []
                }, {
                    "id": "admin.security",
                    "text": "Manage Security",
                    "icon": "flaticon-searching1 nav-icon-font",
                    "state": "app.admin.security.logins",
                    "active": false,
                    "children": []
                }, {
                    "id": "admin.settings",
                    "text": "Settings",
                    "icon": "fa-wrench nav-icon-font",
                    "state": "app.admin.settings",
                    "active": false,
                    "children": []
                }]
            }];
            function checkSession() {
                LoginService.current().then(
                    function (session) {
                        if (session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.permissions = session.login.permissions;
                        }
                    }
                );
            }


            (function () {

                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);