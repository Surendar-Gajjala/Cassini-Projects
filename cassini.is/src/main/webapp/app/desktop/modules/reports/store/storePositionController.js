define(['app/desktop/modules/reports/reports.module',
        'app/shared/services/store/topStoreService'
    ],
    function (module) {
        module.controller('StorePositionController', StorePositionController);

        function StorePositionController($scope, $rootScope, $timeout, $state, $cookies, TopStoreService) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-bar-chart";
            $rootScope.viewInfo.title = "Store Position Report";

            vm.exportReport = exportReport;
            vm.stores = [];
            vm.openItemDetails = openItemDetails;
            vm.loadStores = loadStores;
            $scope.selectedRow = 0;
            vm.searchReport = searchReport;
            vm.reportRows = [];
            vm.loading = false;
            vm.startDate = null;
            vm.searchMode = false;
            vm.endDate = moment(new Date()).format("DD/MM/YYYY");
            vm.filters = {
                searchQuery: ""
            };
            vm.selectedStore = null;
            function openItemDetails(store) {
                $('#search-results-container1').hide();
                vm.selectedStore = store;
                vm.filters.searchQuery = store.storeName;
            }

            function loadStores() {
                vm.searchMode = false;
                vm.reportRows = [];
                $scope.selectedRow = 0;
                $('#search-results-container1').show();
                TopStoreService.searchStores(vm.filters).then(
                    function (data) {
                        vm.stores = data;
                    }
                )
            }

            function searchReport() {
                vm.reportRows = [];
                vm.searchMode = true;
                if (vm.endDate.trim() != "") {
                    vm.loading = true;
                    TopStoreService.getReportByDates(vm.selectedStore.id, vm.startDate, vm.endDate).then(
                        function (data) {
                            vm.reportRows = data;
                            vm.loading = false;
                        }
                    )
                } else {
                    $rootScope.showErrorMessage("Please select ToDate");
                }

            }

            function exportReport() {
                $rootScope.showExportMessage("Store Report, Exporting in progress");
                TopStoreService.exportStoreReport().then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        var c = document.cookie.split("; ");
                        for (i in c)
                            document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
                        window.open(url, '_self');
                        $rootScope.showSuccessMessage("Report exported successfully");
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $(document).click(function (e) {
                        if ($(e.target).is('.search-element1 input')) {
                            loadStores();
                        } else
                            $('#search-results-container1').hide();
                    });
                    $(document).on('keydown', function (evt) {
                        if (evt.keyCode == 27) {
                            $('#search-results-container1').hide();
                        }
                    });
                    $(document).on('keydown', function (e) {
                        if ($scope.selectedRow == null) {
                            $scope.selectedRow = 0;
                        }
                        if ($('#search-results-container1').is(':visible')) {
                            if (e.keyCode == 38) {
                                if ($scope.selectedRow == 0) {
                                    return;
                                }
                                $scope.selectedRow--;
                                $scope.$apply();
                                e.preventDefault();
                            }
                            else if (e.keyCode == 40) {
                                if ($scope.selectedRow == vm.stores.length - 1) {
                                    return;
                                }
                                $scope.selectedRow++;
                                $scope.$apply();
                                e.preventDefault();
                            } else if (e.keyCode == 13) {
                                openItemDetails(vm.stores[$scope.selectedRow]);
                                $scope.$apply();
                                e.preventDefault();
                            }
                            else if (e.keyCode == 13) {
                                openItemDetails(vm.stores[$scope.selectedRow]);
                                $scope.$apply();
                                e.preventDefault();
                            }
                        }
                    });

                }
            })();
        }
    }
);