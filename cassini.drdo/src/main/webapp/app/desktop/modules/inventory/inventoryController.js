define(
    [
        'app/desktop/modules/inventory/inventory.module'
    ],
    function (module) {
        module.controller('InventoryController', InventoryController);

        function InventoryController($scope, $rootScope, $timeout, $state, $stateParams, $uibModal, $cookies, $window, $translate,
                                     CommonService, BomService) {

            $rootScope.viewInfo.icon = "fa fa-sitemap";
            $rootScope.viewInfo.title = "Inventory";

            if ($application.homeLoaded == false) {
                return;
            }


            (function () {

            })();
        }
    }
)
;