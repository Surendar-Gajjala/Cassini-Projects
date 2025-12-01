define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/new/loginDialogController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/groups/groupDialogController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService'
    ],

    function(module){

        module.controller('LoginsController',LoginsController);

        function LoginsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                  LoginService , PersonGroupService) {
            var vm = this;
            vm.logins=[];
            vm.groups=[];

            $rootScope.viewInfo.icon = "fa fa-key";
            $rootScope.viewInfo.title = "Login";

            vm.loadingLogins = true;
            vm.loadingGroups = true;

            vm.showNewLoginDialog = showNewLoginDialog;
            vm.showNewGroupDialog = showNewGroupDialog;
            vm.openLogin=openLogin;

            function showNewLoginDialog() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/new/loginDialogView.jsp',
                    controller: 'LoginDialogController as loginDialogVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        $rootScope.showSuccessMessage("Login Created successfully!");
                        vm.logins.push(result);
                    }
                );
            }

            function showNewGroupDialog() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/groups/groupDialogView.jsp',
                    controller: 'GroupDialogController as groupDialogVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        $rootScope.showSuccessMessage("Person Group Created successfully!");
                        vm.groups.push(result);

                    }
                );
            }

            function openLogin(login) {
                $state.go('app.admin.logindetails', { loginId: login.id });
            }


            function loadLogins() {
                var pageable = {
                    page: 1,
                    size: 20,
                    sort: {
                        field: "createdDate",
                        order: "ASC"
                    }
                };
                LoginService.getLogins(pageable).then(
                    function(data) {
                        vm.logins = data.content;
                        vm.loadingLogins = false;
                    }
                )
            }


            function loadGroups() {
                var pageable = {
                    page: 1,
                    size: 20,
                    sort: {
                        field: "createdDate",
                        order: "ASC"
                    }
                };
                PersonGroupService.getPersonGroups(pageable).then(
                    function(data) {
                        vm.groups = data.content;
                        vm.loadingGroups = false;
                    }
                )
            }

            (function() {
                //if($application.homeLoaded == true) {
                    loadLogins();
                    loadGroups();
                //}
            })();
        }
    }
)