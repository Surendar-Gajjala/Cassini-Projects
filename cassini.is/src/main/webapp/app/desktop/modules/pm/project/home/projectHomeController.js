define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/pm/project/home/tabs/basic/basicController',
        'app/desktop/modules/pm/project/home/tabs/tasks/taskHomeController'
    ],
    function (module) {
        module.controller('ProjectHomeController', ProjectHomeController);

        function ProjectHomeController($scope, $uibModal, $rootScope, $timeout, $state, $stateParams, $cookies) {
            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;
            vm.taskDetailsTabActivated = taskDetailsTabActivated;
            vm.update = update;
            vm.back = back;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/pm/project/home/tabs/basic/basicView.jsp',
                    active: true
                },
                tasks: {
                    id: 'details.tasks',
                    heading: 'Tasks',
                    template: 'app/desktop/modules/pm/project/home/tabs/tasks/taskHomeView.jsp',
                    active: false
                }

            };
            function back() {
                $state.go('app.home');
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function taskDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = t;
                    }
                }

                return tab;
            }

            function getLocation() {
                if (!!navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(
                        function (position) {

                        }
                    );
                }
            }

            function update() {
                $rootScope.$broadcast('app.project.update');
            }

            (function () {
                if ($application.homeLoaded == true) {
                    getLocation();
                }
            })();
        }
    }
);