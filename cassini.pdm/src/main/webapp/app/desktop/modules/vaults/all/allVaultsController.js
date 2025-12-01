define(
    [
        'app/desktop/modules/vaults/vault.module',
        'split-pane',
        'app/shared/services/vaultService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',

    ],
    function (module) {
        module.controller('AllVaultsController', AllVaultsController);

        function AllVaultsController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $uibModal,
                                     CommonService, VaultService, DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-files-o";
            $rootScope.viewInfo.title = "Vaults";
            $rootScope.viewInfo.showDetails = false;


            var vm = this;
            vm.loading = true;

            $scope.pagenumbers = [20, 50, 100];
            vm.clear = false;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.selectAll = selectAll;
            vm.showVault = showVault;
            vm.vaults = angular.copy(pagedResults);
            vm.showNewVault = showNewVault;
            vm.deleteVault = deleteVault;
            vm.clearFilter = clearFilter;
            vm.freeTextSearch = freeTextSearch;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.flag = false;
            vm.allVaultSelected = false;

            var freeTextQuery = null;
            var searchMode = null;

            function clearFilter() {

                vm.clear = false;
            }

            function selectAll() {

                vm.allVaultSelected = !vm.allVaultSelected;
                angular.forEach(vm.vaults.content, function (vault) {

                    vault.selected = vm.allVaultSelected;

                });

            }

            function deleteVault(vault) {
                var options = {
                    title: 'Delete Item',
                    message: 'Are you sure you want to delete this vault?',
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        VaultService.deleteVault(vault.id).then(
                            function (data) {
                                var index = vm.vaults.content.indexOf(vault);
                                vm.vaults.content.splice(index, 1);
                                $rootScope.showErrorMessage("Vault deleted successfully!");
                            }
                        )
                    }
                });
            }

            function showNewVault() {
                var options = {
                    title: 'New Vault',
                    template: 'app/desktop/modules/vaults/new/newVaultView.jsp',
                    controller: 'NewVaultController as newVaultVm',
                    resolve: 'app/desktop/modules/vaults/new/newVaultController',
                    width: 600,
                    showMask: true,
                    data: {},
                    buttons: [
                        {text: 'Create', broadcast: 'app.vaults.new'}
                    ],
                    callback: function () {
                        $rootScope.hideSidePanel();
                        loadVaults();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function freeTextSearch(freeText) {
                freeTextQuery = freeText;
                searchMode = "freetext";
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    VaultService.freeTextSearch(vm.pageable, freeText).then(
                        function (data) {
                            vm.vaults = data
                            CommonService.getPersonReferences(vm.vaults.content, 'createdBy');
                            CommonService.getPersonReferences(vm.vaults.content, 'modifiedBy');
                            vm.clear = true;
                        }
                    )
                } else {
                    resetPage();
                    loadVaults();
                }
            }


            function showVault(vault) {

                $state.go('app.vaults.details', {vaultId: vault.id, vaultName: vault.name});
            }

            function resetPage() {
                vm.pageable.page = 0;
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;

                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;

                }
            }


            function loadVaults() {

                vm.clear = false;
                vm.loading = true;

                VaultService.getAllVaultsPagable(vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.vaults = data;
                        angular.forEach(vm.vaults.content, function (vault) {

                            vault.selected = false;

                        });
                        CommonService.getPersonReferences(vm.vaults.content, 'createdBy');
                        CommonService.getPersonReferences(vm.vaults.content, 'modifiedBy');
                    }
                )
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadVaults();
                });
            })();
        }
    }
)
;