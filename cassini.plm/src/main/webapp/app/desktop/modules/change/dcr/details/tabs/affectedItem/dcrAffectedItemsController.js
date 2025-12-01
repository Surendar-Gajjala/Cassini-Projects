define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcrService',
        'app/desktop/directives/affected-items/affectedItemsDirective'

    ],
    function (module) {
        module.controller('DCRAffectedItemsController', DCRAffectedItemsController);

        function DCRAffectedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate, DCRService, DialogService) {
            var vm = this;

            vm.dcrId = $stateParams.dcrId;

            var parsed = angular.element("<div></div>");
            $scope.addAffectedItemTitle = parsed.html($translate.instant("ADD_AFFECTED_ITEMS")).html();
            $scope.saveItemTitle = parsed.html($translate.instant("SAVE_THIS_ITEM")).html();

            var removeItems = parsed.html($translate.instant("REMOVE_ITEM")).html();
            var itemRemoveTitle = parsed.html($translate.instant("REMOVE_ITEM_TITLE_MSG")).html();
            var itemRemoveMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();

            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                var options = {
                    title: removeItems,
                    message: itemRemoveTitle,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($(".view-container"));
                        DCRService.deleteDcrAffectedItem(vm.dcrId, item.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(itemRemoveMsg);
                                $scope.$broadcast('app.affectedItems.view', {});
                                $rootScope.loadDCRCounts();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            (function () {
                $scope.$on('app.dcr.tabActivated', function (event, args) {
                    if (args.tabId == 'details.affectedItems') {
                        $scope.$broadcast('app.affectedItems.view', {});
                    }
                });
            })();
        }
    }
);