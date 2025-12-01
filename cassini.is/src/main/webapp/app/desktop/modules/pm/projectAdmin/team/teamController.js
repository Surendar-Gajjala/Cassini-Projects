define(['app/desktop/modules/pm/pm.module'
    ],
    function (module) {
        module.controller('TeamController', TeamController);

        function TeamController($scope, $rootScope, $timeout, $state, $stateParams) {
            $rootScope.viewInfo.icon = "flaticon-prize3";
            $rootScope.viewInfo.title = "Team";

            var vm = this;
            vm.setTabActive = setTabActive;
            vm.addPerson = addPerson;
            vm.newRole = newRole;
            vm.back = back;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.flag = false;
            vm.tabs = [];
            $rootScope.projectPersons = [];

            var teamTabs = [
                {
                    heading: 'Team',
                    active: true,
                    icon: 'fa fa-users',
                    state: 'app.pm.project.projectAdmin.team.allPersons',
                    view: 'Team'
                },
                {
                    heading: 'Role',
                    icon: ' fa-user-plus',
                    active: false,
                    state: 'app.pm.project.projectAdmin.team.role',
                    view: 'Role'
                }
            ];

            function tabs() {
                angular.forEach(teamTabs, function (tab) {
                    if (tab.heading == 'Team') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.team.view') || $rootScope.hasPermission('permission.team.addPerson') || $rootScope.hasPermission('permission.team.editTeam')) {
                            vm.tabs.push(tab);
                        }
                    }
                    if (tab.heading == 'Role') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.team.roles') || $rootScope.hasPermission('permission.team.newRole') || $rootScope.hasPermission('permission.team.deleteRole')) {
                            vm.tabs.push(tab);
                        }
                    }
                });
                vm.currentTab = vm.tabs[0].view;

                $timeout(function () {
                    var rightHeight = $('.view-content').outerHeight();
                    $('.team-view').height(rightHeight - 52);
                }, 500)
            }

            function setTabActive(tab) {
                if (tab.state != null) {
                    $state.go(tab.state);
                    vm.currentTab = tab.view;
                    if (vm.currentTab == 'Team') {
                        vm.flag = false;
                    } else if (vm.currentTab == 'Role') {
                        vm.flag = true;
                    }
                }
            }

            function back() {
                $state.go('app.pm.project.projectAdmin.team.allPersons');
            }

            function addPerson() {
                $rootScope.$broadcast('app.team.person');
            }

            function newRole() {
                $rootScope.$broadcast('app.team.role');
            }

            function nextPage() {
                $scope.$broadcast('app.team.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.team.previousPageDetails');
            }

            $rootScope.resizeTeamView = resizeTeamView;
            function resizeTeamView() {
                $timeout(function () {
                    var rightHeight = $('.view-content').outerHeight();
                    $('.team-view').height(rightHeight - 72);
                }, 100);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    tabs();
                    $(window).resize(function () {
                        resizeTeamView();
                    });
                    $timeout(function () {
                        $state.go('app.pm.project.projectAdmin.team.allPersons');
                    }, 100)
                }
            })();
        }
    }
);