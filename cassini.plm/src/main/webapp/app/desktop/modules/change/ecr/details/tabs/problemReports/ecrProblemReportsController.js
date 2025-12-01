define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecrService',
        'app/desktop/directives/affected-items/affectedItemsDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('ECRProblemReportsController', ECRProblemReportsController);

        function ECRProblemReportsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                             $translate, ECRService, DialogService, CommentsService) {
            var vm = this;

            vm.ecrId = $stateParams.ecrId;
            var parsed = angular.element("<div></div>");

            var removePr = parsed.html($translate.instant("REMOVE_PR")).html();
            var prRemoveTitle = parsed.html($translate.instant("REMOVE_PR_TITLE_MSG")).html();
            var prRemovedMsg = parsed.html($translate.instant("REMOVED_PR_SUCCESS_MSG")).html();

            vm.deleteProblemReport = deleteProblemReport;

            function deleteProblemReport(problemReport) {
                var options = {
                    title: removePr,
                    message: prRemoveTitle,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($(".view-container"));
                        ECRService.deleteECRProblemReport(vm.ecrId, problemReport.id).then(
                            function (data) {
                                $rootScope.loadBasicECR();
                                $rootScope.loadECRCounts();
                                loadECRProblemReports();
                                $rootScope.showSuccessMessage(prRemovedMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function loadECRProblemReports() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ECRService.getECRProblemReports(vm.ecrId).then(
                    function (data) {
                        vm.problemReports = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var emptyECRPR = {
                id: null,
                ecr: vm.ecrId,
                problemReport: null
            };
            var addButton = parsed.html($translate.instant("ADD")).html();
            var prsAddedMsg = parsed.html($translate.instant("PRS_ADDED_MSG")).html();
            $scope.addPrsTitle = parsed.html($translate.instant("ADD_PRS")).html();

            vm.addProblemReports = addProblemReports;
            function addProblemReports() {
                var options = {
                    title: $scope.addPrsTitle,
                    template: 'app/desktop/modules/change/ecr/details/tabs/problemReports/selectProblemReportView.jsp',
                    controller: 'SelectProblemReportController as selectProblemReportVm',
                    resolve: 'app/desktop/modules/change/ecr/details/tabs/problemReports/selectProblemReportController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedEcrId: vm.ecrId
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.ecr.problemreports.add'}
                    ],
                    callback: function (result) {
                        vm.selectedProblemReports = [];
                        angular.forEach(result, function (problemReport) {
                            var part = angular.copy(emptyECRPR);
                            part.editMode = true;
                            part.isNew = true;
                            part.problemReport = problemReport.id;
                            vm.selectedProblemReports.push(part);
                        });
                        $rootScope.showBusyIndicator($('.view-container'));
                        ECRService.createECRProblemReports(vm.ecrId, vm.selectedProblemReports).then(
                            function (data) {
                                $rootScope.loadBasicECR();
                                $rootScope.loadECRCounts();
                                loadECRProblemReports();
                                $rootScope.showSuccessMessage(prsAddedMsg);
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
                $scope.$on('app.ecr.tabActivated', function (event, args) {
                    if (args.tabId == 'details.problemReports') {
                        loadECRProblemReports();
                    }
                });
            })();
        }
    }
);