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
        module.controller('ItemInstanceController', ItemInstanceController);

        function ItemInstanceController($scope, $rootScope, $timeout, $state, $filter, $stateParams, $uibModal, $cookies, $window, $translate,
                                        CommonService, ItemService, DialogService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.loading = true;

            function loadItemInstances() {
                ItemService.getItemInstances($stateParams.itemId).then(
                    function (data) {
                        vm.itemInstances = data;
                        if ($rootScope.selectedItemRevisionDetails.itemMaster.itemType.hasBom) {
                            vm.itemInstances = $filter('orderBy')(vm.itemInstances, 'instanceName');
                        }
                        vm.loading = false;
                    }
                )
            }

            (function () {
                $scope.$on('app.item.tabActivated', function (event, data) {
                    if (data.tabId == 'details.instances') {
                        loadItemInstances();
                    }
                });
            })();
        }
    }
);