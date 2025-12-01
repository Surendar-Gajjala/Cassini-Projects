define(['app/desktop/modules/col/col.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/communication/communicationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/itemService'],
    function (module) {
        module.controller('EditGroupDialogController', EditGroupDialogController);

        function EditGroupDialogController($scope, $rootScope, $timeout, $state, ItemService, LoginService, $stateParams, $cookies, $sce, CommonService, CommunicationService) {

            $rootScope.viewInfo.icon = "fa fa-comments";
            var vm = this;

            vm.selectedUsers = [];
            vm.userList = [];
            vm.groups = [];
            vm.users = [];

            vm.create = create;
            vm.isUpdate = false;
            var groupMap = new Hashtable();
            var selectedGroup = $scope.data.selectedGroup;
            vm.mode = $scope.data.modeType;

            vm.removeUser = removeUser;

            vm.pageable = {
                page: 1,
                size: 10,
                sort: {
                    label: "loginTime",
                    field: "loginTime",
                    order: "desc"
                }
            };

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function validateGroup() {
                vm.valid = true;

                if (vm.name == null || vm.name == undefined || vm.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Group Name cannot be empty");
                }
                else if (groupMap.get(vm.name) != null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("{0} Name already exists".format(vm.name));
                }
                else if (vm.description == null || vm.description == undefined || vm.description == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Group Description cannot be empty");
                }
                return vm.valid;
            }

            function create() {
                var list = [],
                    data = {};

                if (vm.mode == "NEW") {
                    if (validateGroup()) {
                        angular.forEach(vm.selectedUsers, function (value, key) {
                            list.push({
                                "person": value.id,
                                "ctxObjectType": "PROJECT",
                                "ctxObjectId": $state.params.projectId
                            });
                        });

                        data = {
                            "name": vm.name,
                            "description": vm.description,
                            "ctxObjectType": "PROJECT",
                            "ctxObjectId": $state.params.projectId,
                            "msgGrpMembers": list
                        };

                        CommunicationService.createGroup(data).then(
                            function (result) {
                                $scope.callback(result);
                                vm.creating = false;
                                $rootScope.showSuccessMessage("Group created successfully");
                                $scope.hideSidePanel("right");
                            }
                        )
                    }

                } else if (vm.mode == "EDIT") {
                    angular.forEach(vm.selectedUsers, function (selectedUser) {
                        if (checkUser(selectedUser)) {
                            $rootScope.showErrorMessage("This person is already in group");

                        } else {
                            list.push({
                                "person": selectedUser.id,
                                "ctxObjectType": "PROJECT",
                                "ctxObjectId": $state.params.projectId
                            });
                            data = {
                                "name": selectedGroup.name,
                                "description": selectedGroup.description,
                                "ctxObjectType": "PROJECT",
                                "ctxObjectId": $state.params.projectId,
                                "msgGrpMembers": list
                            };

                            CommunicationService.updateGroup(data, selectedGroup.id).then(
                                function (result) {
                                    vm.users = result;
                                    $scope.callback(result);
                                    vm.creating = false;
                                    $rootScope.showSuccessMessage("Group updated successfully");
                                    $scope.hideSidePanel("right");
                                })
                        }

                    });

                }
            }

            function checkUser(selectedUser) {
                var exists = false;
                angular.forEach(selectedGroup.msgGrpMembers, function (mem) {
                    if (mem.person == selectedUser.id) {
                        exists = true;
                    }
                });

                return exists;
            }

            function loadPersons() {
                var users = [];
                LoginService.getLogins(vm.pageable).then(
                    function (data) {
                        var logins = data.content;
                        angular.forEach(logins, function (login) {
                            users.push(login.person);
                        });
                        vm.userList = getRemainingGrpMembers(selectedGroup, users);
                    }
                )
            }

            function findGrpMembersById(users, id) {
                for (var i = 0; i < users.length; i++) {
                    if (users[i].id == id && !isUserSelected(users[i].id)) {
                        vm.selectedUsers.push(users[i]);
                        return i;
                    }
                }
                return -1;
            }

            function isUserSelected(personId) {
                var flag = false;
                angular.forEach(vm.selectedUsers, function (user) {
                    if (personId == user.id) {
                        flag = true;
                    }
                });
                return flag;
            }

            function getRemainingGrpMembers(selectedGroup, users) {
                var persons = angular.copy(users);
                angular.forEach(selectedGroup.msgGrpMembers, function (grpMember) {
                    var index = findGrpMembersById(persons, grpMember.person);
                    if (index != -1) {
                        users.splice(index, 1);
                    }
                });
                CommonService.getPersonReferences(vm.selectedUsers, 'person');
                return users;
            }

            function loadGroups() {
                CommunicationService.getGroups($stateParams.projectId).then(
                    function (data) {
                        vm.groups = data.content;
                        angular.forEach(vm.groups, function (group) {
                            group.selected = false;
                        });

                    }
                );

            }

            function removeUser(item) {
                var found = findMemberByPerson(item.id);
                if (found != null) {
                    CommunicationService.deleteGrpMember(found.id).then(
                        function (data) {
                            loadGroups();
                            $rootScope.showSuccessMessage("Member deleted successfully");
                        })
                }
            }

            function findMemberByPerson(personId) {
                var found = null;
                angular.forEach(selectedGroup.msgGrpMembers, function (member) {
                    if (member.person == personId) {
                        found = member;
                    }
                });

                return found;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadPersons();
                    loadGroups();
                    $timeout(function () {
                        if (vm.mode == "EDIT") {
                            vm.name = selectedGroup.name;
                            vm.description = selectedGroup.description;
                        }
                        $rootScope.$on('app.group.edit', create);
                    }, 2000);
                }
            })();
        }
    }
)
;