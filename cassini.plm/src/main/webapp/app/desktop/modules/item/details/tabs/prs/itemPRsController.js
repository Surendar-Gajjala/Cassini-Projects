define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('ItemPRsController', ItemPRsController);

        function ItemPRsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                   ItemService, ItemTypeService) {
            var vm = this;

            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.prs = [];

            function loadPRs() {
                ItemService.getItemPRs(vm.itemId).then(
                    function (data) {
                        vm.prs = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    /*loadPRs();*/
                //}
            })();
        }
    }
);