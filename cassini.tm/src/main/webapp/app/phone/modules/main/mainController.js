define(
    [
        'app/phone/modules/main/main.module',
        'moment',
        'app/shared/services/personService',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/phone/directives/mobileDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/sessionService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/authorizationService'
    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController ($scope, $rootScope, $timeout, $state, $cookies, $mdDialog, $mdSidenav,
                                 $application, LoginService, CommonService, PersonService) {
            window.moment = moment;

            var vm = this;

            $rootScope.viewName = "Cassini.TM";
            $rootScope.backgroundColor = "#1565c0";

            vm.user = {
                name: "",
                phone: ""
            };

            vm.historyLast = true;
            vm.isAdmin = false;

            vm.toggleSidenav = toggleSidenav;
            vm.showHome = showHome;
            vm.logout = logout;
            vm.showItems = showItems;
            vm.goToState = goToState;
            vm.showProfile = showProfile;
            vm.goBack = goBack;

            $rootScope.goToState = goToState;
            $rootScope.showPersonDetails = showPersonDetails;
            $rootScope.showProfile = showProfile;


            function toggleSidenav(menuId) {
                $mdSidenav(menuId).toggle();
            }

            function showHome() {
                $state.go('app.home');
            }

            function logout() {
                var confirm = $mdDialog.confirm()
                    .title('Logout')
                    .content('Are you sure you want to logout?')
                    .ariaLabel('Logout')
                    .ok('Yes')
                    .cancel('No');

                $mdDialog.show(confirm).then(
                    function() {
                        LoginService.logout().then(
                            function() {
                                App2AndroidBridge.logout();
                                $state.go('login', {fromapp: 'true'}, { reload: true });
                            }
                        )
                    },
                    function() {

                    }
                );
            }

            function checkSession() {
                LoginService.current().then (
                    function(session) {
                        if(session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.user.name = session.login.person.firstName;

                            if(session.login.person.lastName != null && session.login.person.lastName.trim() != "") {
                                vm.user.name += ", " + session.login.person.lastName;
                            }
                            vm.user.phone = session.login.person.phoneMobile;

                            loadPersonRole();
                        }
                    }
                );
            }

            function showItems() {
                $state.go('app.items');
            }

            function goToState(state, params) {
                $timeout(function() {
                    if(params != undefined && params != null) {
                        $state.go(state, params);
                    }
                    else {
                        $state.go(state);
                    }
                }, 500);
            }

            function showPersonDetails(personId) {
                $state.go("app.person.details", {personId: personId});
            }

            function showProfile() {
                showPersonDetails($application.login.person.id);
            }

            $rootScope.showMessage = function(title, message) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.body))
                        .clickOutsideToClose(true)
                        .title(title)
                        .textContent(message)
                        .ariaLabel('Cassini.TM')
                        .ok('OK')
                        .targetEvent(null)
                );
            };


            $rootScope.sortPersons = function (persons) {
                persons.sort(function(p1, p2) {
                    return p1.firstName.toLowerCase().localeCompare(p2.firstName.toLowerCase())
                });
            };

            function goBack() {
                window.history.back();
            }

            function loadPersonRole() {
                var person = $application.login.person;
                PersonService.getRoleByPerson(person.id).then(
                    function (data) {
                        if ($application.login.loginName == 'admin' ||
                            data.role == "Administrator") {
                            vm.isAdmin = true;
                        }

                        CommonService.initialize();
                        if($application.homeLoaded == false) {
                            $state.go('app.home');
                        }
                    }
                )
            }

            (function() {
                if(window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }

                $rootScope.$on('$stateChangeStart',
                    function (event, toState, toParams, fromState, fromParams) {
                        if(toState.name != "app.home" && $application.homeLoaded == false) {
                            $state.go('app.home');
                        }
                    }
                );

                $rootScope.$on('$stateChangeSuccess',
                    function (event, toState, toParams, fromState, fromParams) {

                    }
                );

                checkSession();
            })();
        }
    }
);