define(
    [
        'app/desktop/modules/item/item.module'
    ],
    function (module) {
        module.controller('UserRolesController', UserRolesController);

        function UserRolesController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                     $state, $stateParams, $cookies, PersonGroupService, CommonService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var userId = $stateParams.userId;

            vm.userRoles = [
                {name: 'Administrator', description: 'System administrator', selected: true},
                {name: 'Item Manager', description: 'Item manager', selected: true},
                {name: 'Project Manager', description: 'Project manager', selected: false, default: true},
                {name: 'Change Analyst', description: 'Change analyst', selected: true},
                {name: 'Quality Analyst', description: 'Quality analyst', selected: false},
                {name: 'Project Manager', description: 'Project manager', selected: false},
                {name: 'Change Analyst', description: 'Change analyst', selected: true},
                {name: 'Quality Analyst', description: 'Quality analyst', selected: false},
                {name: 'Project Manager', description: 'Project manager', selected: false},
                {name: 'Change Analyst', description: 'Change analyst', selected: true},
                {name: 'Quality Analyst', description: 'Quality analyst', selected: false}

            ];

            function loadPersonGroups() {
                $rootScope.showBusyIndicator();
                if ($rootScope.loginPersonDetails.isSuperUser) {
                    CommonService.getPersonGroups(userId).then(
                        function (data) {
                            vm.personRoles = data;
                            loadGroups();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    CommonService.getPersonGroups(userId).then(
                        function (data) {
                            vm.userRoles = data;
                            angular.forEach(vm.userRoles, function (role) {
                                role.selected = true;
                            })
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadGroups() {
                vm.userRoles = [];
                $rootScope.showBusyIndicator();
                PersonGroupService.getAllPersonGroups().then(
                    function (data) {
                        angular.forEach(data, function (group) {
                            if (group.isActive && $rootScope.loginDetails.external == group.external) {
                                group.selected = checkGroup(group);
                                vm.userRoles.push(group);
                            }
                        });
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function checkGroup(group) {
                var valid = false;
                angular.forEach(vm.personRoles, function (role) {
                    if (role.groupId == group.groupId) {
                        valid = true;
                    }
                })
                return valid;
            }

            vm.changeRole = changeRole;
            function changeRole(role) {
                if (role.id != $rootScope.loginDetails.person.defaultGroup) {
                    if (role.selected) {
                        var groupMember = {
                            rowId: null,
                            person: vm.person,
                            personGroup: role
                        }
                        $rootScope.showBusyIndicator();
                        PersonGroupService.createGroupMember(userId, groupMember).then(
                            function (data) {
                                $rootScope.showSuccessMessage("User role assigned successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        $rootScope.showBusyIndicator();
                        PersonGroupService.deletePersonGroupMember(role.groupId, userId).then(
                            function (data) {
                                $rootScope.showSuccessMessage("User role removed successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function loadPerson() {
                vm.userRoles = [];
                CommonService.getPerson(userId).then(
                    function (data) {
                        vm.person = data;
                        loadPersonGroups();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadPerson();
            })();
        }
    }
);