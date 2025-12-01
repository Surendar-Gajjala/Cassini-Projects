define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController'
    ],
    function (module) {

        module.controller('ItemCopyController', ItemCopyController);

        function ItemCopyController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                    AutonumberService, ItemTypeService, ItemService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();

            vm.autoNumber = autoNumber;
            vm.newItem = null;

            function copyItemData() {
                vm.newItem = angular.copy($scope.data.itemData);
                vm.newItem.itemNumber = null;
                vm.newItem.itemName = null;
                vm.newItem.description = null;
            }

            function autoNumber() {
                if (vm.newItem.itemType != null && vm.newItem.itemType.itemNumberSource != null) {
                    var source = vm.newItem.itemType.itemNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newItem.itemNumber = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function copyItem() {
                if (vm.newItem.itemNumber != null && vm.newItem.itemNumber != "" && vm.newItem.itemName != null && vm.newItem.itemName != "" ) {
                    $scope.callback(vm.newItem);
                } else {
                    if (vm.newItem.itemNumber == null || vm.newItem.itemNumber == "") {
                        $rootScope.showWarningMessage(itemNumberValidation)
                    }
                    else if (vm.newItem.itemName == null || vm.newItem.itemName == "") {
                        $rootScope.showWarningMessage(itemNameValidation)
                    }
                }
            }

            (function () {
                copyItemData();
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.items.copy', copyItem);
                //}
            })();
        }
    }
)
;