define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService'],

    function(module){


        module.controller('GroupDialogController',GroupDialogController);

        function GroupDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModalInstance,
                                       LoginService,CommonService,PersonGroupService){
            var  vm=this;

            vm.creating=true;
            vm.valid=true;
            vm.error="";

            vm.personGroup = {
                name: null,
                description: null

            };


            vm.create=create;
            vm.cancel=cancel;

            $rootScope.viewInfo.icon="fa fa-key";
            $rootScope.viewInfo.title="New Group";


            function validate(){
                vm.valid=true;
                if(vm.personGroup.name==null){
                    vm.valid=false;
                    vm.error="Name Cannot Be Empty";
                }

                return vm.valid;
            }

            function create() {
                if(validate()) {
                    vm.creating = true;
                    PersonGroupService.createPersonGroup(vm.personGroup).then(
                        function(data) {
                            vm.creating = false;
                            $uibModalInstance.close(data);
                        }

                    );

                }
            }


            /*  function cancel(){
             $state.go('app.admin.logins')
             }*/

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            (function () {

            })();
        }
    }
);
