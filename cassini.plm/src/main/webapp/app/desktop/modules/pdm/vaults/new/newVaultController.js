define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/shared/services/core/pdmVaultService'
    ],
    function (module) {
        module.controller('NewVaultController', NewVaultController);

        function NewVaultController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $application, $cookieStore, $translate,
                                    PDMVaultService) {

            var vm = this;

            vm.newVault = {
                name: null,
                description: null
            };

            var parsed = angular.element("<div></div>");
            var vaultCreateSuccessMsg = parsed.html($translate.instant("VAULT_CREATED_SUCCESS")).html();
            var vaultEditSuccessMsg = parsed.html($translate.instant("VAULT_EDIT_SUCCESS")).html();

            function createNewVault() {
                if (validate()) {
                    if (vm.newVault.id != undefined && vm.newVault.id != null) {
                        PDMVaultService.updateVault(vm.newVault).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(vaultEditSuccessMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        PDMVaultService.createVault(vm.newVault).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(vaultCreateSuccessMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate() {
                if (vm.newVault.name == null || vm.newVault.name.trim() === "") {
                    $rootScope.showErrorMessage("Vault name is required");
                    return false;
                }
                else if (vm.newVault.description == null || vm.newVault.description.trim() === "") {
                    $rootScope.showErrorMessage("Vault description is required");
                    return false;
                }

                return true;
            }

            (function () {
                $rootScope.$on('app.pdm.vault.new', createNewVault);
                if ($scope.data.vault != null && $scope.data.vault != undefined) {
                    vm.newVault = $scope.data.vault;
                }
            })();
        }
    }
);