define(['app/desktop/modules/items/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/itemTypeService',
        'app/shared/services/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController'
    ],
    function (module) {
        module.controller('AdvancedSearchController', AdvancedSearchController);

        function AdvancedSearchController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            var vm = this;

            vm.row = null;
            vm.search = search;
            vm.onAddItem = onAddItem;
            vm.show = false;
            vm.deleteItem = deleteItem;
            vm.items = [];
            vm.dataTypeOperators = {
                string: [
                    'contains',
                    'startswith',
                    'endswith',
                    'equals'
                ],
                integer: [
                    'equals',
                    'lessthan',
                    'greterthan',
                    'lessthanorequals',
                    'greterthanorequals'
                ]
            };

            vm.itemNames = [
                {field: 'item.itemNumber', operator: vm.dataTypeOperators.string},
                /*{field: 'item.status', operator: vm.dataTypeOperators.string},*/
                {field: 'item.description', operator: vm.dataTypeOperators.string},
                {field: 'item.itemType', operator: vm.dataTypeOperators.string},
                /*{field: 'item.revision', operator: vm.dataTypeOperators.string},*/
                /*{field: 'itemFile.name', operator: vm.dataTypeOperators.string},
                 {field: 'itemFile.version', operator: vm.dataTypeOperators.integer},
                 {field: 'attribute.doubleValue', operator: vm.dataTypeOperators.string},
                 {field: 'attribute.attributeDef', operator: vm.dataTypeOperators.string},
                 {field: 'bom.itemNumber', operator: vm.dataTypeOperators.string},
                 {field: 'bom.notes', operator: vm.dataTypeOperators.string},
                 {field: 'bom.refdes', operator: vm.dataTypeOperators.string},
                 {field: 'reference.itemNumber', operator: vm.dataTypeOperators.string},
                 {field: 'reference.notes', operator: vm.dataTypeOperators.string},
                 {field: 'reference.status', operator: vm.dataTypeOperators.string},
                 {field: 'reference.revision', operator: vm.dataTypeOperators.string}*/].sort();

            var searchItems = {
                field: null,
                operator: null,
                value: null
            };
            vm.selectedType = {
                name: null
            };
            vm.filters = [];

            function onAddItem() {
                var newRow = angular.copy(searchItems);
                newRow.parent = vm.row;
                vm.items.push(newRow);
            }

            function deleteItem(item) {
                var index = vm.items.indexOf(item);
                vm.items.splice(index, 1);
            }

            function search() {
                vm.filters = [];
                angular.forEach(vm.items, function (item) {

                    if (item.field == null) {
                        var filter = {
                            field: item.field,
                            operator: item.operator,
                            value: item.value
                        }

                    } else {
                        var filter = {
                            field: item.field.field,
                            operator: item.operator,
                            value: item.value
                        }
                    }

                    vm.filters.push(filter);
                })

                if (validate()) {

                    $rootScope.hideSidePanel(vm.filters);
                    $scope.callback(vm.filters);
                }
            }

            function validate() {
                var valid = true;

                angular.forEach(vm.filters, function (filter) {

                    if (filter.field == null || filter.field == undefined || filter.field == "") {
                        valid = false;
                        $rootScope.showErrorMessage('Field cannot be empty');
                    }
                    else if (filter.operator == null || filter.operator == undefined || filter.operator == "") {
                        valid = false;
                        $rootScope.showErrorMessage('Operator cannot be empty');
                    }
                    else if (filter.value == null || filter.value == undefined || filter.value == "") {
                        valid = false;
                        $rootScope.showErrorMessage('Value cannot be empty');
                    }

                })
                return valid;

            }

            (function () {
                if ($application.homeLoaded == true) {
                    onAddItem();
                    $rootScope.$on('app.items.advsearch', search);
                }
            })();
        }
    }
);
