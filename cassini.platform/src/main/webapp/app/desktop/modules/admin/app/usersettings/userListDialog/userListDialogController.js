define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService'],

    function (module) {


        module.controller('UserListDialogController', UserListDialogController);

        function UserListDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                          LoginService, CommonService, PersonGroupService, $translate) {
            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.userList = [];
            vm.selectedList = [];
            vm.selectAll = selectAll;
            vm.loading = false;
            vm.selectAllCheck = false;
            var modalData = $scope.data.userData;
            vm.create = create;
            var parsed = angular.element("<div></div>");
            var userSelection = parsed.html($translate.instant("PLEASE_SELECT_USER")).html();
            var noUsersValidation = parsed.html($translate.instant("NO_USERS_TO_ADD")).html();

            vm.personGroup = {
                name: null,
                description: null,
                groupMember: [],
                groupId: null,
                isActive: true,
                external: false
            };

            function create() {
                if (vm.selectedList.length > 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedList);
                }

                if (vm.selectedList.length == 0) {
                    $rootScope.showWarningMessage(userSelection);
                }

            }
            vm.selectCheck = selectCheck;
            function selectCheck(user) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedList, function (selectedUser) {
                    if (selectedUser.id == user.id) {
                        flag = false;
                        var index = vm.selectedList.indexOf(user);
                        vm.selectedList.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectedList.push(user);
                }

                if (vm.selectedList.length != vm.userList.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            function selectAll(check) {
                vm.selectedList = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.userList, function (user) {
                        user.isSelected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.userList, function (user) {
                        user.isSelected = true;
                        vm.selectedList.push(user);
                    })
                }
            }

            function loadUsers() {
                LoginService.getAllLogins().then(
                    function (data) {
                        if ($rootScope.lastSelectedGroup != null) {
                            if ($rootScope.lastSelectedGroup.external == true) {
                                angular.forEach(data, function (user) {
                                    if (user.external == true && user.isActive == true) {
                                        vm.userList.push(user);
                                    }
                                });
                            }
                            else {
                                angular.forEach(data, function (user) {
                                    if (user.isActive == true && user.external == false) {
                                        vm.userList.push(user);
                                    }
                                });
                            }
                        }
                        vm.loadingUsers = true;
                        loadGroupPersons();
                    }
                );
            }

            function loadGroupPersons() {
                angular.forEach(modalData.userList, function (person) {
                    angular.forEach(vm.userList, function (user) {
                        if (person.person.id == user.person.id) {
                            var index = vm.userList.indexOf(user);
                            vm.userList.splice(index, 1);
                        }
                    });
                });
            }

            (function () {
                //if ($application.homeLoaded == true) {
                vm.loadingUsers = false;
                $rootScope.$on('app.user.users', create);
                vm.pageable = {
                    page: 1,
                    size: 20,
                    sort: {
                        field: "createdDate",
                        order: "ASC"
                    }
                };
                loadUsers();
                //}
            })();
        }
    }
)
;
