define(['app/desktop/modules/site/sites.module',
        'app/desktop/modules/site/details/tabs/basic/siteBasicDetailsController',
        'app/desktop/modules/site/details/tabs/tasks/siteTasksDetailsController',
        'app/desktop/modules/site/details/tabs/resources/siteResourcesController',
        'app/shared/services/pm/project/projectSiteService'

    ],
    function (module) {
        module.controller('SiteDetailsController', SiteDetailsController);

        function SiteDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, ProjectSiteService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-map-marker";
            $rootScope.viewInfo.title = "Site Details";

            vm.siteDetailsTabActivated = siteDetailsTabActivated;
            vm.back = back;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/site/details/tabs/basic/siteBasicDetailsView.jsp',
                    active: true
                },
                tasks: {
                    id: 'details.tasks',
                    heading: 'Tasks',
                    template: 'app/desktop/modules/site/details/tabs/tasks/siteTasksDetailsView.jsp',
                    active: false
                },
                resources: {
                    id: 'details.resources',
                    heading: 'Resources',
                    template: 'app/desktop/modules/site/details/tabs/resources/siteResourcesView.jsp',
                    active: false
                }
            };
            function back() {
                window.history.back();
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function siteDetailsTabActivated(tabId) {
                $scope.$broadcast('app.site.tabactivated', {tabId: tabId})
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

            $rootScope.loadDetailsCount = loadDetailsCount;
            function loadDetailsCount() {
                ProjectSiteService.getSiteDetailsCount($stateParams.projectId, $stateParams.siteId).then(
                    function (data) {
                        vm.taskCount = data;
                        var tasksTab = document.getElementById("tasks");
                        var resourcesTab = document.getElementById("resources");

                        tasksTab.lastElementChild.innerHTML = vm.tabs.tasks.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.taskCount.tasks);
                        resourcesTab.lastElementChild.innerHTML = vm.tabs.resources.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.taskCount.resource);

                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDetailsCount();
                    $rootScope.showComments('SITE', $stateParams.siteId);
                }
            })();
        }
    }
);