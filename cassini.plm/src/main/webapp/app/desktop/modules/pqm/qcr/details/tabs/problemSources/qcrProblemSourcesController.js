define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/qcrService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('QcrProblemSourcesController', QcrProblemSourcesController);

        function QcrProblemSourcesController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, DialogService, QcrService) {
            var vm = this;

            vm.qcrId = $stateParams.qcrId;


            var parsed = angular.element("<div></div>");
            $scope.addProblemSourcesTitle = parsed.html($translate.instant("ADD_PROBLEM_SOURCES")).html();
            var sourceRemovedMsg = parsed.html($translate.instant("PROBLEM_SOURCE_REMOVED")).html();
            var removeProblemSourceTitle = parsed.html($translate.instant("REMOVE_PROBLEM_SOURCE")).html();
            var removeProblemSourceDialogMsg = parsed.html($translate.instant("REMOVE_PROBLEM_SOURCE_D_MSG")).html();
            var removeProblemSourceAddedMsg = parsed.html($translate.instant("PROBLEM_SOURCE_ADDED")).html();
            var wfStartedOnePsSbt = parsed.html($translate.instant("WF_STARTED_ONE_PS_SBT")).html();


            var emptyQcrPr = {
                id: null,
                qcr: vm.qcrId,
                pr: null,
                prDto: null
            };

            var emptyQcrNcr = {
                id: null,
                qcr: vm.qcrId,
                ncr: null,
                ncrDto: null
            };

            vm.problemSources = [];
            vm.addProblemSources = addProblemSources;
            function addProblemSources() {
                vm.selectedSources = [];
                var options = {
                    title: "Select Problem Source",
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/problemSources/problemSourceSelectionView.jsp',
                    controller: 'ProblemSourcesSelectionController as problemSourcesSelectionVm',
                    resolve: 'app/desktop/modules/pqm/qcr/details/tabs/problemSources/problemSourcesSelectionController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: $rootScope.add, broadcast: 'add.select.problemSources'}
                    ],
                    callback: function (result) {
                        angular.forEach(result, function (source) {
                            if ($rootScope.qcr.qcrFor == "PR") {
                                var qcrPr = angular.copy(emptyQcrPr);
                                qcrPr.prDto = source;
                                vm.selectedSources.push(qcrPr);
                            } else {
                                var qcrNcr = angular.copy(emptyQcrNcr);
                                qcrNcr.ncrDto = source;
                                vm.selectedSources.push(qcrNcr);
                            }
                        })
                        var promise = null;
                        $rootScope.showBusyIndicator($('.view-container'));
                        if ($rootScope.qcr.qcrFor == "PR") {
                            promise = QcrService.createPrProblemSources(vm.qcrId, vm.selectedSources);
                        } else {
                            promise = QcrService.createNcrProblemSources(vm.qcrId, vm.selectedSources);
                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    vm.selectedSources = [];
                                    $rootScope.showSuccessMessage(removeProblemSourceAddedMsg);
                                    loadProblemSources();
                                    $rootScope.loadQcrDetails();
                                    $rootScope.hideSidePanel();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function loadProblemSources() {
                $rootScope.showBusyIndicator();
                vm.loading=true;
                var promise = null;
                if ($rootScope.qcr.qcrFor == "PR") {
                    promise = QcrService.getPrProblemSources(vm.qcrId);
                } else {
                    promise = QcrService.getNcrProblemSources(vm.qcrId);
                }

                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.problemSources = data;
                            vm.loading=false;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                if ($rootScope.qcrWorkflowStarted && $rootScope.qcrDetailCount.problemSources == 1) {
                    $rootScope.showWarningMessage(wfStartedOnePsSbt);
                } else {
                    var options = {
                        title: removeProblemSourceTitle,
                        message: removeProblemSourceDialogMsg,
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            var promise = null;
                            $rootScope.showBusyIndicator($('.view-container'));
                            if ($rootScope.qcr.qcrFor == "PR") {
                                promise = QcrService.deletePrProblemSource(vm.qcrId, item.id);
                            } else {
                                promise = QcrService.deleteNcrProblemSource(vm.qcrId, item.id);
                            }

                            if (promise != null) {
                                promise.then(
                                    function (data) {
                                        $rootScope.loadQcrDetails();
                                        $rootScope.showSuccessMessage(sourceRemovedMsg);
                                        loadProblemSources();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    });
                }
            }

            $scope.showSource = showSource;
            function showSource() {
                $window.localStorage.setItem("lastSelectedQcrTab", JSON.stringify("details.problemSource"));
            }

            (function () {
                $scope.$on('app.qcr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.problemSource') {
                        $scope.qcrFor = $rootScope.qcr.qcrFor;
                        loadProblemSources();
                    }
                })
            })();
        }
    }
)
;