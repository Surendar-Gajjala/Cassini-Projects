define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('MBOMWhereUsedController', MBOMWhereUsedController);

        function MBOMWhereUsedController($scope, $translate, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                         MBOMService, WorkflowService, ECOService, DCOService, CommonService) {
            var vm = this;

            vm.loading = true;
            vm.mbomId = $stateParams.mbomId;
            vm.item = null;
            vm.showBOP = showBOP;
            $rootScope.loadBOPs = loadBOPs;
            var parsed = angular.element("<div></div>");

            vm.changeOrders = parsed.html($translate.instant("CHANGE_ORDERS")).html();

            function loadBOPs() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                MBOMService.getMBOMWhereUsed(vm.mbomId).then(
                    function (data) {
                        vm.bops = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function showBOP(bop) {
                $state.go('app.mes.bop.details', {bopId: bop.latestRevision, tab: "details.basic"});
            }

            var create = parsed.html($translate.instant("CREATE")).html();
            var newBOPHeading = parsed.html($translate.instant("New BOP")).html();

            $rootScope.createMBOMBOP = createMBOMBOP;
            function createMBOMBOP() {
                var options = {
                    title: newBOPHeading,
                    template: 'app/desktop/modules/mes/bop/new/newBOPView.jsp',
                    controller: 'NewBOPController as newBOPVm',
                    resolve: 'app/desktop/modules/mes/bop/new/newBOPController',
                    width: 700,
                    showMask: true,
                    data: {
                        bopCreationType: "MBOM",
                        mbom: $rootScope.mbom.id,
                        mbomRevision: $rootScope.mbomRevision.id
                    },
                    buttons: [
                        {text: create, broadcast: 'app.bop.new'}
                    ],
                    callback: function (bop) {
                        loadBOPs();
                        $rootScope.loadMbomTabCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                $scope.$on('app.mbom.tabActivated', function (event, data) {
                    if (data.tabId == 'details.whereUsed') {
                        $('.tab-content .tab-pane').css("overflow", "auto");
                        loadBOPs();
                    }
                });
            })();
        }
    }
);