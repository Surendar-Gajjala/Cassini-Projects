define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/assemblyLineService',
        'app/shared/services/core/workCenterService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AssemblyLineWorkCentersController', AssemblyLineWorkCentersController);

        function AssemblyLineWorkCentersController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate,
                                                   AssemblyLineService, $application, WorkCenterService, DialogService) {
            var vm = this;
            vm.assemblyLineId = $stateParams.assemblyLineId;

            vm.addWorkCenters = addWorkCenters;
            vm.removeWorkCenter = removeWorkCenter;
            vm.showWorkCenter = showWorkCenter;

            var parsed = angular.element("<div></div>");
            $scope.noWorkCenters = parsed.html($translate.instant("NO_WORK_CENTER")).html();
            $scope.addWorkCenters = parsed.html($translate.instant("ADD_WORK_CENTERS")).html();
            $scope.removeWorkCenter = parsed.html($translate.instant("REMOVE_WORK_CENTER")).html();
            var deleteDialogMessage = parsed.html($translate.instant("REMOVE_DIALOG_MESSAGE")).html();
            var wcRemoveMessage = parsed.html($translate.instant("REMOVE_WORK_CENTER_MSG")).html();

            function loadAssemblyLineWorkCenters() {
                vm.assemblyLineWorkCenters = [];
                AssemblyLineService.getAssemblyLineWorkCenters(vm.assemblyLineId).then(
                    function (data) {
                        vm.assemblyLineWorkCenters = data;
                        $rootScope.hideBusyIndicator();
                        $rootScope.loadAssemblyLineTabCounts();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function addWorkCenters() {
                var options = {
                    title: "Add Work Centers",
                    template: 'app/desktop/modules/mes/assemblyLine/details/tabs/workCenters/selectAssemblyLineWorkCenterView.jsp',
                    controller: 'SelectAssemblyLineWorkCenterController as selectAssemLineWorkCentersVm',
                    resolve: 'app/desktop/modules/mes/assemblyLine/details/tabs/workCenters/selectAssemblyLineWorkCenterController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedAssemblyLineId: vm.assemblyLineId
                    },
                    buttons: [
                        {text: "Add", broadcast: 'app.assl.wcs.add'}
                    ],
                    callback: function (result) {
                        loadAssemblyLineWorkCenters();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeWorkCenter(workCenter) {
                var options = {
                    title: $scope.removeWorkCenter,
                    message: deleteDialogMessage + " [ " + workCenter.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            workCenter.assemblyLine = null;
                            WorkCenterService.updateWorkCenter(workCenter).then(
                                function (data) {
                                    loadAssemblyLineWorkCenters();
                                    $rootScope.showSuccessMessage(wcRemoveMessage);
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

            function showWorkCenter(workCenter) {
                $state.go('app.mes.masterData.workcenter.details', {workcenterId: workCenter.id, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.assemblyLine.tabActivated', function (event, data) {
                    if (data.tabId == 'details.workCenters') {
                        loadAssemblyLineWorkCenters()
                    }
                });
            })();
        }
    }
);



