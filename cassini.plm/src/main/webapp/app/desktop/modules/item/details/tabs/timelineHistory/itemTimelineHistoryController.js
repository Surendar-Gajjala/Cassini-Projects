define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/directives/timelineDirective',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('ItemTimelineHistoryController', ItemTimelineHistoryController);

        function ItemTimelineHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemService) {
            var vm = this;

            $scope.loading = true;
            vm.itemId = $stateParams.itemId;

            function loadItemRevisions() {
                ItemService.getItemRevisionIds(vm.itemId).then(
                    function (data) {
                        vm.itemRevisionIds = data;
                        $timeout(function () {
                            $scope.$broadcast('app.object.timeline', {});
                        }, 500);
                    }
                )
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if ($rootScope.selectedMasterItemId != null) {
                        $stateParams.itemId = $rootScope.selectedMasterItemId;
                        vm.itemId = $stateParams.itemId;
                    } else {
                        vm.itemId = $stateParams.itemId;
                    }
                    if (data.tabId == 'details.itemTimelineHistory') {
                        loadItemRevisions();
                    }

                });
            })();
        }
    }
);