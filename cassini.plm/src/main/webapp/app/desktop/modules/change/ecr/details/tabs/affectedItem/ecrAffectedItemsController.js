define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcrService',
        'app/desktop/directives/affected-items/affectedItemsDirective'

    ],
    function (module) {
        module.controller('ECRAffectedItemsController', ECRAffectedItemsController);

        function ECRAffectedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate, ECRService, DialogService, CommentsService) {
            var vm = this;

            vm.ecrId = $stateParams.ecrId;

            var parsed = angular.element("<div></div>");

            var qcrItemDeleteValidation = parsed.html($translate.instant("QCR_ITEM_DELETE_VALID")).html();
            var prItemDeleteValidation = parsed.html($translate.instant("PR_ITEM_DELETE_VALID")).html();
            $scope.addAffectedItemTitle = parsed.html($translate.instant("ADD_AFFECTED_ITEMS")).html();
            $scope.saveItemTitle = parsed.html($translate.instant("SAVE_THIS_ITEM")).html();

            var removeItems = parsed.html($translate.instant("REMOVE_ITEM")).html();
            var itemRemoveTitle = parsed.html($translate.instant("REMOVE_ITEM_TITLE_MSG")).html();
            var itemRemoveMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();
            var wfStartedOneAffectedItemSbt = parsed.html($translate.instant("WF_STARTED_ONE_AT_SBT")).html();

            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                if ($rootScope.ecr.startWorkflow && $rootScope.ecrDetailsCount.affectedItems == 1) {
                    $rootScope.showWarningMessage(wfStartedOneAffectedItemSbt);
                } else {
                    if (!item.qcrItem && item.problemReportList.length == 0) {
                        var options = {
                            title: removeItems,
                            message: itemRemoveTitle,
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($(".view-container"));
                                ECRService.deleteEcrAffectedItem(vm.ecrId, item.id).then(
                                    function (data) {
                                        $rootScope.loadECRCounts();
                                        $rootScope.showSuccessMessage(itemRemoveMsg);
                                        $scope.$broadcast('app.affectedItems.view', {});
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        });
                    } else {
                        if (item.problemReportList.length > 0) {
                            $rootScope.showWarningMessage(item.itemNumber + " : " + prItemDeleteValidation);
                        } else {
                            $rootScope.showWarningMessage(item.itemNumber + " : " + qcrItemDeleteValidation);
                        }
                    }
                }

            }

            (function () {
                $scope.$on('app.ecr.tabActivated', function (event, args) {
                    if (args.tabId == 'details.affectedItems') {
                        $scope.$broadcast('app.affectedItems.view', {});
                    }
                });
            })();
        }
    }
);