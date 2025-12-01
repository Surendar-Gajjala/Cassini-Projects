define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('ItemReportController', ItemReportController);

        function ItemReportController($scope, $rootScope, $timeout, $state, $stateParams, $uibModal, $cookies, $window, $translate,
                                      CommonService, ItemService, DialogService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.loading = true;
            vm.itemReport = [];

            function loadItemReport() {
                ItemService.getItemReport($stateParams.itemId).then(
                    function (data) {
                        vm.itemReport = data;
                        angular.forEach(vm.itemReport, function (report) {
                            report.showDetails = false;
                        })
                        vm.loading = false;
                    }
                )
            }

            vm.showDetails = showDetails;
            function showDetails(report) {
                report.showDetails = !report.showDetails;
            }

            (function () {
                $scope.$on('app.item.tabActivated', function (event, data) {
                    if (data.tabId == 'details.report') {
                        loadItemReport();
                    }
                });
            })();
        }
    }
);