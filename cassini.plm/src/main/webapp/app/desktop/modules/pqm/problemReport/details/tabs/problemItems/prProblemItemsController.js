define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/directives/affected-items/affectedItemsDirective'
    ],
    function (module) {
        module.controller('PrProblemItemsController', PrProblemItemsController);

        function PrProblemItemsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, DialogService, ProblemReportService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            $scope.saveItemTitle = parsed.html($translate.instant("SAVE_THIS_ITEM")).html();
            $scope.addProblemItemTitle = parsed.html($translate.instant("ADD_PROBLEM_ITEMS")).html();
            vm.problemReportId = $stateParams.problemReportId;
            var removeItems = parsed.html($translate.instant("REMOVE_ITEM")).html();
            var itemRemoveTitle = parsed.html($translate.instant("REMOVE_ITEM_TITLE_MSG")).html();
            var itemRemoveMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();
            var wfStartedOnePiSbt = parsed.html($translate.instant("WF_STARTED_ONE_PI_SBT")).html();

            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                if ($rootScope.prWorkflowStarted && $rootScope.problemReportDetailsCount.problemItems == 1) {
                    $rootScope.showWarningMessage(wfStartedOnePiSbt);
                } else {
                    var options = {
                        title: removeItems,
                        message: itemRemoveTitle,
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($(".view-container"));
                            ProblemReportService.deletePrAffectedItem(vm.problemReportId, item.id).then(
                                function (data) {
                                    $rootScope.loadProblemReportDetails();
                                    $rootScope.showSuccessMessage(itemRemoveMsg);
                                    $scope.$broadcast('app.affectedItems.view', {});
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    });
                }

            }


            $scope.showProblemItem = showProblemItem;
            function showProblemItem() {
                $window.localStorage.setItem("lastSelectedProblemReportTab", JSON.stringify("details.problemItem"));
            }

            (function () {
                $scope.$on('app.problemReport.tabActivated', function (event, data) {
                    if (data.tabId == 'details.problemItem') {
                        $scope.$broadcast('app.affectedItems.view', {});
                    }
                })
            })();
        }
    }
)
;