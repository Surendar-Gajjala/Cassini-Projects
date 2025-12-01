define(
    [
        'app/desktop/modules/vaults/vault.module',
        'app/shared/services/vaultService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'


    ],
    function (module) {
        module.controller('NewVaultController', NewVaultController);

        function NewVaultController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $uibModal,
                                    VaultService, DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.newVault = {
                name: null,
                description: null,

            };

            vm.createVault=createVault;

            function validate() {
                var valid = true;

                if (vm.newVault.name == null || vm.newVault.name=="") {
                    valid = false;
                    $rootScope.showErrorMessage('Vault Name cannot be empty');
                }
                else if (vm.newVault.description == null || vm.newVault.description=="") {
                    valid = false;
                    $rootScope.showErrorMessage('Vault Description cannot be empty');
                }

               return valid;
            }

            function createVault() {

                if(validate()){

                    VaultService.createVault(vm.newVault).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Vault created successfully");
                            $scope.callback();
                        }, function (error) {

                        }
                    )

                }


            }


            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.vaults.new', createVault);
                }
            })();
        }
    }
)
;