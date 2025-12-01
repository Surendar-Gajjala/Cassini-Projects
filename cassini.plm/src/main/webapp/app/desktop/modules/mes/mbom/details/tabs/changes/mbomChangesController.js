define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mcoService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('MBOMChangesController', MBOMChangesController);

        function MBOMChangesController($scope, $translate, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                       MBOMService, WorkflowService, MCOService, DCOService, CommonService) {
            var vm = this;

            vm.loading = true;
            vm.mbomId = $stateParams.mbomId;
            vm.item = null;
            vm.showMco = showMco;
            $rootScope.loadChanges = loadChanges;
            vm.collapseRequests = true;
            vm.collapseOrders = true;
            var parsed = angular.element("<div></div>");

            vm.changeOrders = parsed.html($translate.instant("CHANGE_ORDERS")).html();

            function loadChanges() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                vm.mcoChanges = [];
                MBOMService.getMBOMChanges(vm.mbomId).then(
                    function (data) {
                        vm.mcoChanges = data;
                        CommonService.getPersonReferences(vm.mcoChanges, 'changeAnalyst');
                        WorkflowService.getWorkflowReferences(vm.mcoChanges, 'workflow');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.expandAllChangeOrders = expandAllChangeOrders;
            function expandAllChangeOrders() {
                vm.collapseOrders = true;
            }


            vm.collapseAllChangeOrders = collapseAllChangeOrders;
            function collapseAllChangeOrders() {
                vm.collapseOrders = false;
            }

            function showMco(change) {
                $state.go('app.changes.mco.details', {mcoId: change.id, tab: "details.basic"});
            }

            var newMcoTitle = parsed.html($translate.instant("NEW_MCO")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            $rootScope.createMBOMMco = createMBOMMco;
            function createMBOMMco() {
                var options = {
                    title: newMcoTitle,
                    template: 'app/desktop/modules/change/mco/new/newMcoView.jsp',
                    controller: 'NewMCOController as newMcoVm',
                    resolve: 'app/desktop/modules/change/mco/new/newMcoController',
                    width: 600,
                    data: {
                        mcoType: "ITEMMCO"
                    },
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.mcos.new'}
                    ],
                    callback: function (mco) {
                        $rootScope.showBusyIndicator();
                        var toRevision = "";
                        var revs = $rootScope.mbom.type.revisionSequence.values;
                        var index = revs.indexOf($rootScope.mbomRevision.revision);
                        if (index != -1) {
                            toRevision = revs[index + 1];
                        }
                        var affectedItem = {
                            id: null,
                            mco: mco.id,
                            item: vm.mbomId,
                            fromRevision: $rootScope.mbomRevision.revision,
                            toRevision: toRevision
                        };
                        MCOService.createMcoMbom(mco.id, affectedItem).then(
                            function (data) {
                                loadChanges();
                                $rootScope.loadMBOMDetails();
                                $rootScope.loadMbomTabCounts();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                $scope.$on('app.mbom.tabActivated', function (event, data) {
                    if (data.tabId == 'details.changes') {
                        $('.tab-content .tab-pane').css("overflow", "auto");
                        loadChanges(vm.mbomId);
                    }
                });
            })();
        }
    }
);