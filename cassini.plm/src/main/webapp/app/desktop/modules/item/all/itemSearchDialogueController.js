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
        module.controller('ItemSearchDialogueController', ItemSearchDialogueController);

        function ItemSearchDialogueController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies,
                                              ItemService) {

            var parsed = angular.element("<div></div>");
            var searchVaildation = parsed.html($translate.instant("SEARCH_MESSAGE_VALIDATION")).html();

            var vm = this;
            vm.attributes = [];
            vm.newItem = {
                itemType: null,
                itemNumber: null,
                name: null,
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
                itemName: null,
                itemClass: ''
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
                if (validate()) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.filters);
                }
            }

            function validate() {
                var valid = true;
                $rootScope.closeNotification();

                if ((vm.filters.itemName != "" && vm.filters.itemName != null && vm.filters.itemName != undefined) ||
                    (vm.filters.itemType != null) ||
                    (vm.filters.itemNumber != "" && vm.filters.itemNumber != null && vm.filters.itemNumber != undefined) ||
                    (vm.filters.revision != "" && vm.filters.revision != null && vm.filters.revision != undefined) ||
                    (vm.filters.status != "" && vm.filters.status != null && vm.filters.status != undefined)) {
                    valid = true;
                } else {
                    $rootScope.showInfoMessage(searchVaildation);
                    valid = false;
                }

                return valid;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.items.search', search);
                //}
            })();
        }
    }
);