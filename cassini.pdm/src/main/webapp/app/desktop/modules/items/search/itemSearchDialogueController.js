define(
    [
        'app/desktop/modules/items/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/itemTypeService',
        'app/shared/services/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController'
    ],
    function (module) {
        module.controller('ItemSearchDialogueController', ItemSearchDialogueController);

        function ItemSearchDialogueController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              ItemService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.attributes = [];
            vm.newItem = {
                itemType: null,
                itemNumber: null,
                description: null,
                revision: null,
                status: null
            };

            vm.selectedType = {
                name: null
            };

            vm.criteria = {
                itemType: null,
                itemNumber: null,
            };

            vm.filters = {
                itemType: null,
                itemNumber: null,
                revision: null,
                status: null,
                description: null
            };

            vm.onSelectType = onSelectType;
            vm.search = search;

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemType = itemType;
                    vm.filters.itemType = itemType;
                }
            }

            function search() {

                $scope.callback(vm.filters);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.items.search', search);
                }
            })();
        }
    }
);