define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('RoleUsersController', RoleUsersController);

        function RoleUsersController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                     $state, $stateParams, $i18n, GroupService, PersonGroupService, LoginService, CommonService) {

            var vm = this;
            var roleId = $stateParams.roleId;
            vm.personGroup = null;
            $rootScope.personGroup = vm.personGroup;

            function loadRoleDetails() {
                GroupService.getGroupById(roleId).then(
                    function (data) {
                        vm.personGroup = data;
                        $rootScope.personGroup = data;
                        loadUsers();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    })
            }

            vm.roleUsers = [];

            vm.pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function loadUsers() {
                $rootScope.showBusyIndicator();
                LoginService.getFilteredLogins(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.users = data;
                        vm.roleUsers = [];
                        PersonGroupService.getLoginPersonGroupReferences(vm.users.content, 'defaultGroup');
                        angular.forEach(vm.users.content, function (user) {
                            if (user.external == vm.personGroup.external) {
                                user.person.selected = false;
                                angular.forEach(vm.personGroup.groupMember, function (person) {
                                    if (user.person.id == person.person.id) {
                                        user.person.selected = true;
                                    }
                                });
                                vm.roleUsers.push(user);
                            }
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.showBusyIndicator();
                    });
            }

            vm.attachedRoleUser = attachedRoleUser;
            var groupUpdatedMessage = $i18n.getValue("GROUP_UPDATED_MESSAGE");

            function attachedRoleUser(user) {
                if (user.person.selected) {
                    var grpMember = {person: null, personGroup: null};
                    grpMember.person = user.person;
                    grpMember.personObject = user;
                    vm.personGroup.groupMember.push(grpMember);
                    $rootScope.showBusyIndicator();
                    PersonGroupService.updatePersonGroup(vm.personGroup).then(
                        function (data) {
                            vm.personGroup = data;
                            $rootScope.showSuccessMessage("User added successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    $rootScope.showBusyIndicator();
                    PersonGroupService.deletePersonGroupMember(vm.personGroup.groupId, user.person.id).then(
                        function (data) {
                            angular.forEach(vm.personGroup.groupMember, function (person1) {
                                if (user.person.id == person1.person.id) {
                                    var index = vm.personGroup.groupMember.indexOf(person1);
                                    vm.personGroup.groupMember.splice(index, 1);
                                }
                            });
                            $rootScope.showSuccessMessage("User removed successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.searchMode = true;
                    vm.filters.searchQuery = freeText;
                    loadUsers();
                } else {
                    resetPage();
                }
            }

            vm.filters = {
                searchQuery: null
            };
            vm.resetPage = resetPage;
            function resetPage() {
                vm.roleUsers = [];
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                vm.searchMode = false;
                vm.noResults = false;
                loadUsers();
            }

            (function () {
                loadRoleDetails();
            })();
        }
    }
);