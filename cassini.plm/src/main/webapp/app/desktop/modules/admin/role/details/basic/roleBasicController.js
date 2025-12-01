define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/profileService',
    ],
    function (module) {
        module.controller('RoleBasicController', RoleBasicController);

        function RoleBasicController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                     $state, $stateParams, $i18n, CommonService, ProfileService, PersonGroupService, DialogService, GroupService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var roleId = $stateParams.roleId;
            vm.roleName = null;
            vm.personGroup = null;
            $rootScope.personGroup = null;

            var nameValidation = $i18n.getValue("NAME_CANNOT_BE_EMPTY");
            var profileValidation = $i18n.getValue("VALIDATE_PROFILE_MESSAGE");
            var groupUpdatedMessage = $i18n.getValue("GROUP_UPDATED_MESSAGE");
            var roleUpdateMsg = $i18n.getValue("ROLE_BASIC_UPDATED_MESSAGE");
            $scope.yes = $i18n.getValue("YES");
            $scope.no = $i18n.getValue("NO");
            $scope.selectProfile = $i18n.getValue("SELECT_PROFILE");


            function loadRoleDetails() {
                GroupService.getGroupById(roleId).then(
                    function (data) {
                        vm.roleName = data.name;
                        vm.personGroup = data;
                        $rootScope.personGroup = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function validateGroup() {
                vm.valid = true;
                if (vm.roleName == null || vm.roleName == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(nameValidation);
                }
                else if (vm.personGroup.profile == null || vm.personGroup.profile == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(profileValidation);
                }

                return vm.valid;
            }

            vm.updateRole = updateRole;

            function updateRole() {
                if (validateGroup()) {
                    vm.personGroup.name = vm.roleName;
                    PersonGroupService.updatePersonGroup(vm.personGroup).then(
                        function (data) {
                            $rootScope.showSuccessMessage(roleUpdateMsg);
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                        }
                    );
                }
            }

            function loadProfiles() {
                ProfileService.getAllProfiles().then(
                    function (data) {
                        vm.allProfiles = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            (function () {
                loadRoleDetails();
                loadProfiles();
            })();
        }
    }
);