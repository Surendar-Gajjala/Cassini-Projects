define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/desktop/directives/person-avatar/personAvatarDirective'
    ],
    function (module) {
        module.controller('RoleDetailsController', RoleDetailsController);

        function RoleDetailsController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                       $state, $stateParams, $cookies, GroupService, PersonGroupService, CommonService) {

            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            var parsed = angular.element("<div></div>");
            var roleId = $stateParams.roleId;

            vm.details = 'basic';


            vm.showBasicRoleDetails = showBasicRoleDetails;
            function showBasicRoleDetails() {
                vm.details = 'basic';
                $state.go('app.roledetails.basic', {roleId: roleId})
            }

            vm.showRoleUsers = showRoleUsers;
            function showRoleUsers() {
                vm.details = 'users';
                $state.go('app.roledetails.users', {roleId: roleId})
            }

            vm.showRolePermissions = showRolePermissions;
            function showRolePermissions() {
                vm.details = 'permissions';
                $state.go('app.roledetails.permissions', {roleId: roleId})
            }

            vm.gotoAdmin = gotoAdmin;
            function gotoAdmin() {
                $state.go('app.newadmin.roles');
            }

            vm.personGroup = null;
            $rootScope.personGroup = vm.personGroup;

            function loadRoleDetails() {
                GroupService.getGroupById(roleId).then(
                    function (data) {
                        vm.personGroup = data;
                        $rootScope.personGroup = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            (function () {
                loadRoleDetails();
                var url = $state.current.url;
                if (url === '/basic') {
                    vm.details = 'basic';
                    showBasicRoleDetails();
                } else if (url === '/users') {
                    vm.details = 'users';
                    showRoleUsers();
                }
                else if (url === '/permissions') {
                    vm.details = 'permissions';
                    showRolePermissions();
                } else {
                    vm.details = 'basic';
                    showBasicRoleDetails();
                }
                $('#contentpanel').css({'overflow-y': 'auto'});

                $scope.$on("$destroy", function () {
                    $('#contentpanel').css({'overflow-y': 'hidden'});
                });

            })();
        }
    }
);