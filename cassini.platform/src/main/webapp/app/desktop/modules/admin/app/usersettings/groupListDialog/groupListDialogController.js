define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService'],

    function (module) {


        module.controller('GroupListDialogController', GroupListDialogController);

        function GroupListDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                           LoginService, CommonService, PersonGroupService, GroupService, $translate) {
            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.groupList = [];
            vm.loading = false;
            var personGroups = [];
            var person = $scope.data.login.person;
            vm.create = create;

            var parsed = angular.element("<div></div>");
            var groupSelection = parsed.html($translate.instant("PLEASE_SELECT_GROUP")).html();

            function create() {
                var selectedList = [];
                angular.forEach(vm.groupList, function (item) {
                    if (item.isSelected) {
                        selectedList.push(item);
                    }
                });
                if (selectedList.length > 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(selectedList);
                } else {
                    $rootScope.showWarningMessage(groupSelection);

                }

            }

            function loadPersonGroups() {
                PersonGroupService.getPersonGroupsByPersonId(person.id).then(
                    function (data) {
                        personGroups = data;
                        angular.forEach(personGroups, function (personGroup) {
                            angular.forEach(vm.groupList, function (group) {
                                if (personGroup.groupId == group.groupId) {
                                    var index = vm.groupList.indexOf(group);
                                    vm.groupList.splice(index, 1);
                                }
                            });
                        });

                    }
                );
            }

            function loadAllGroups() {
                vm.leafGroups = [];
                vm.groupList = [];
                GroupService.getAllGroups().then(
                    function (data) {
                        vm.allGroups = data;
                        angular.forEach(vm.allGroups, function (group) {
                            if (group.groupChildren != null && group.groupChildren.length > 0) {
                                populateGroups(group.groupChildren);
                            }
                            if (group.isActive == true && group.external == false) {
                                vm.groupList.push(group);
                            }
                        });
                        loadPersonGroups();
                    }
                )
            }

            function populateGroups(groupChildren) {
                angular.forEach(groupChildren, function (child) {
                    if (child.isActive == true) {
                        vm.groupList.push(child);
                    }
                    if (child.groupChildren != null && child.groupChildren.length > 0) {
                        populateGroups(child.groupChildren);
                    }
                })
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    vm.loadingGroups = false;
                    $rootScope.$on('app.groups.group', create);
                    vm.pageable = {
                        page: 1,
                        size: 20,
                        sort: {
                            field: "createdDate",
                            order: "ASC"
                        }
                    };

                    angular.forEach(vm.groupList, function (group) {
                        if (group.isActive == false || group.external != person.external) {
                            var index = vm.groupList.indexOf(group);
                            vm.groupList.splice(index, 1);
                        }
                    });

                    loadAllGroups();
                //}
            })();
        }
    }
);
