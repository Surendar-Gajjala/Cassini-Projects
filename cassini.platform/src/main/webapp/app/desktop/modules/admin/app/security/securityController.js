define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module'],

    function(module){

        module.controller('SecurityController',SecurityController);

        function SecurityController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                  LoginService) {
            var vm = this;


            $rootScope.iconClass = "fa fa-lock";
            $rootScope.viewTitle = "Security";

            vm.setTabActive = setTabActive;

            vm.tabs = [
                { heading: 'Logins', active: true, icon:'fa-key', state: 'app.admin.security.logins', view: 'logins' },
                { heading: 'Roles & Permissions', icon:'fa-lock', active: false, state: 'app.admin.security.roles', view: 'roles' },
                { heading: 'User Sessions', icon:'flaticon-businessman276', active: false, state: 'app.admin.security.sessions', view: 'sessions' }
            ];
            vm.currentTab = vm.tabs[0].view;

             function setTabActive(tab) {
                if(tab.state != null) {
                    $state.go(tab.state);
                    vm.currentTab = tab.view;
                }
            };

            function setActiveFlag(index) {
                angular.forEach($scope.tabs, function(t) {
                    t.active = false;
                });
                for(var i=0; i<$scope.tabs.length; i++) {
                    if(i == index) {
                        $scope.tabs[i].active = true;
                    }
                }

            };

            (function () {

            })();
        }
    }
)