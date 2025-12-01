define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/shared/services/core/pdmVaultService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('AllVaultsController', AllVaultsController);

        function AllVaultsController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $application, $cookieStore, $translate,
                                     PDMVaultService, DialogService) {
            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            var parsed = angular.element("<div></div>");
            var deleteVaultTitle = parsed.html($translate.instant("DELETE_VAULT")).html();
            var deleteVaultDialogMsg = parsed.html($translate.instant("VAULT_DIALOG_DELETE_MESSAGE")).html();
            var vaultDeletedMsg = parsed.html($translate.instant("VAULT_DELETED_MSG")).html();

            $scope.freeTextQuery = null;
            vm.vaults = [];
            $scope.vaultId = null;
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
            vm.filters = {
                searchQuery: null
            };
            vm.newVault = newVault;
            function newVault() {
                var options = {
                    title: 'New Vault',
                    template: 'app/desktop/modules/pdm/vaults/new/newVaultView.jsp',
                    controller: 'NewVaultController as newVaultVm',
                    resolve: 'app/desktop/modules/pdm/vaults/new/newVaultController',
                    width: 500,
                    data: {},
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.pdm.vault.new'}
                    ],
                    callback: function (newVault) {
                        newVault.assemblasCount = 0;
                        newVault.partsCount = 0;
                        newVault.drawingsCount = 0;
                        newVault.commitsCount = 0;
                        vm.vaults.unshift(newVault);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadVaults() {
                vm.vaults = [];
                PDMVaultService.getVaults().then(
                    function (data) {
                        vm.vaults = data;
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSearchVaults() {
                vm.vaults = [];
                PDMVaultService.getSearchVaults(vm.filters).then(
                    function (data) {
                        vm.vaults = data;
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showVaultDetails = showVaultDetails;
            function showVaultDetails(e, vaultId) {
                if ($(e.target).closest(".vault-icons").length) {
                    return;
                }
                $state.go("app.pdm.vaultdetails", {vaultId: vaultId});
            }

            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadSearchVaults();
                } else {
                    resetPage();
                }
            }

            vm.resetPage = resetPage;
            function resetPage() {
                vm.files = angular.copy(pagedResults);
                vm.filesList = vm.files.content;
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadVaults();
            }

            vm.edit = edit;
            function edit(vault) {
                var vault1 = angular.copy(vault);
                var options = {
                    title: 'Edit Vault',
                    template: 'app/desktop/modules/pdm/vaults/new/newVaultView.jsp',
                    controller: 'NewVaultController as newVaultVm',
                    resolve: 'app/desktop/modules/pdm/vaults/new/newVaultController',
                    width: 500,
                    data: {vault: vault1},
                    showMask: true,
                    buttons: [
                        {text: "Save", broadcast: 'app.pdm.vault.new'}
                    ],
                    callback: function (newVault) {
                        console.log(newVault);
                        vault.name = newVault.name;
                        vault.description = newVault.description;
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.deleteVault = deleteVault;
            function deleteVault(vault) {
                var options = {
                    title: deleteVaultTitle,
                    message: deleteVaultDialogMsg.format(vault.name),
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            PDMVaultService.deleteVault(vault.id).then(
                                function (data) {
                                    var i = vm.vaults.indexOf(vault);
                                    if (i != -1) vm.vaults.splice(i, 1);
                                    $rootScope.showSuccessMessage(vaultDeletedMsg);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }

            (function () {
                $scope.$parent.vaultId = vm.vaultId;
                loadVaults();
            })();
        }
    }
);