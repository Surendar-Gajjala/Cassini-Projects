define(['app/desktop/modules/reports/reports.module',
        'app/shared/services/store/topStockMovementService'
    ],
    function (module) {
        module.controller('StockPositionController', StockPositionController);

        function StockPositionController($scope, $rootScope, $timeout, $state, $cookies, TopStockMovementService) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-line-chart";
            $rootScope.viewInfo.title = "Monthly Stock Report";

            vm.exportReport = exportReport;
            vm.searchReport = searchReport;
            vm.loading = false;
            vm.endDate = moment(new Date()).format("DD/MM/YYYY");
            vm.maprows = [];

            function searchReport() {
                vm.reportRows = [];
                vm.maprows = [];
                if (vm.endDate.trim() != "") {
                    vm.loading = true;
                    TopStockMovementService.getReportByDates(vm.startDate, vm.endDate).then(
                        function (data) {
                            vm.headers = data.headers;
                            vm.maprows = data.isMaterialTypeListMap;
                            vm.loading = false;
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            vm.loading = false;
                        }
                    )
                } else {
                    $rootScope.showErrorMessage("Please select ToDate");
                }

            }

            function exportReport() {
                $rootScope.showExportMessage("Stocks Report Exporting in progress");
                TopStockMovementService.exportStoreReport(vm.startDate, vm.endDate).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        var c = document.cookie.split("; ");
                        for (i in c)
                            document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
                        window.open(url, '_self');
                        $rootScope.showSuccessMessage("Report exported successfully");
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        vm.loading = false;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);