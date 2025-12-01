define(['app/desktop/modules/item/item.module',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/lifecycleService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController'
    ],
    function (module) {
        module.controller('AdvancedSearchController', AdvancedSearchController);

        function AdvancedSearchController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, ItemTypeService, LifecycleService, ClassificationService) {
            var vm = this;

            vm.row = null;
            vm.search = search;
            vm.onAddItem = onAddItem;
            vm.show = false;
            vm.itemTree = itemTree;
            vm.item = null;
            vm.deleteItem = deleteItem;
            vm.items = [];

            var parsed = angular.element("<div></div>");

            var containsTitle = parsed.html($translate.instant("CONTAINS_TITLE")).html();
            var startsWithTitle = parsed.html($translate.instant("STARTS_WITH_TITLE")).html();
            var endsWithTitle = parsed.html($translate.instant("ENDS_WITH_TITLE")).html();
            var equalsTitle = parsed.html($translate.instant("EQUALS_TITLE")).html();
            var isemptyTitle = parsed.html($translate.instant("IS_EMPTY_TITLE")).html();
            var doesNotContainTitle = parsed.html($translate.instant("DOES_NOT_CONTAIN_TITLE")).html();
            var doesNotStartWithTitle = parsed.html($translate.instant("DOES_NOT_START_WITH_TITLE")).html();
            var doesNotEndWithTitle = parsed.html($translate.instant("DOES_NOT_END_WITH_TITLE")).html();
            var isNotEmptyTitle = parsed.html($translate.instant("IS_NOT_EMPTY_TITLE")).html();
            var notEqualTitle = parsed.html($translate.instant("NOT_EQUAL_TITLE")).html();
            var lessthanTitle = parsed.html($translate.instant("LESS_THAN_TITLE")).html();
            var greaterthanTitle = parsed.html($translate.instant("GREATER_THAN_TITLE")).html();
            var lessthanorequalsTitle = parsed.html($translate.instant("LESSTHAN_OR_EQUALS_TITLE")).html();
            var greaterthanorequalsTitle = parsed.html($translate.instant("GREATERTHAN_OR_EQUALS_TITLE")).html();
            var itemItemNumberTitle = parsed.html($translate.instant("ITEM_ITEMNUMBER_TITLE")).html();
            var itemDescriptionTitle = parsed.html($translate.instant("ITEM_DESCRIPTION_TITLE")).html();
            var itemItemTypeTitle = parsed.html($translate.instant("ITEM_ITEMTYPE_TITLE")).html();
            var itemNameTitle = parsed.html($translate.instant("ITEM_NAME_TITLE")).html();
            var itemRevisionTitle = parsed.html($translate.instant("ITEM_REVISION_TITLE")).html();
            var itemFileNameTitle = parsed.html($translate.instant("ITEM_FILENAME_TITLE")).html();
            var itemFileVersionTitle = parsed.html($translate.instant("ITEM_FILEVERSION_TITLE")).html();
            var bomItemNumberTitle = parsed.html($translate.instant("BOM_ITEMNUMBER_TITLE")).html();
            var bomNotesTitle = parsed.html($translate.instant("BOM_NOTES_TITLE")).html();
            var bomCommentsTitle = parsed.html($translate.instant("BOM_COMMENTS_TITLE")).html();
            var bomRefdesTitle = parsed.html($translate.instant("BOM_REFDES_TITLE")).html();
            vm.selectTitle = parsed.html($translate.instant("SELECT")).html();
            vm.enterValueTitle = parsed.html($translate.instant("ENTER_VALUE_TITLE")).html();
            var advancedSearchMessage = parsed.html($translate.instant("ADVANCED_SEARCH_MESSAGE")).html();
            var lifeCycle = parsed.html($translate.instant("ITEM_ALL_LIFECYCLE")).html();

            vm.dataTypeOperators = {
                string: [
                    {name: 'contains', label: containsTitle},
                    {name: 'startswith', label: startsWithTitle},
                    {name: 'endswith', label: endsWithTitle},
                    {name: 'equals', label: equalsTitle},
                    {name: 'doesnotcontain', label: doesNotContainTitle},
                    {name: 'doesnotstartwith', label: doesNotStartWithTitle},
                    {name: 'doesnotendwith', label: doesNotEndWithTitle},
                    {name: 'notequal', label: notEqualTitle}
                ],
                nstring: [
                    {name: 'contains', label: containsTitle},
                    {name: 'startswith', label: startsWithTitle},
                    {name: 'endswith', label: endsWithTitle},
                    {name: 'equals', label: equalsTitle},
                    {name: 'isempty', label: isemptyTitle},
                    {name: 'doesnotcontain', label: doesNotContainTitle},
                    {name: 'doesnotstartwith', label: doesNotStartWithTitle},
                    {name: 'doesnotendwith', label: doesNotEndWithTitle},
                    {name: 'isnotempty', label: isNotEmptyTitle},
                    {name: 'notequal', label: notEqualTitle}
                ],
                integer: [
                    {name: 'equals', label: equalsTitle},
                    {name: 'lessthan', label: lessthanTitle},
                    {name: 'greaterthan', label: greaterthanTitle},
                    {name: 'lessthanorequals', label: lessthanorequalsTitle},
                    {name: 'greaterthanorequals', label: greaterthanorequalsTitle}
                ],
                status: [
                    {name: 'equals', label: equalsTitle},
                    {name: 'notequal', label: notEqualTitle}
                ]
            };

            vm.itemNames = [
                {field: 'item.itemNumber', label: itemItemNumberTitle, operator: vm.dataTypeOperators.string},
                {field: 'item.description', label: itemDescriptionTitle, operator: vm.dataTypeOperators.nstring},
                {field: 'item.itemType', label: itemItemTypeTitle, operator: vm.dataTypeOperators.string},
                {field: 'item.name', label: itemNameTitle, operator: vm.dataTypeOperators.string},
                {field: 'item.lifeCycle', label: lifeCycle, operator: vm.dataTypeOperators.status},
                {field: 'item.revision', label: itemRevisionTitle, operator: vm.dataTypeOperators.status},
                {field: 'itemFile.name', label: itemFileNameTitle, operator: vm.dataTypeOperators.nstring},
                {field: 'itemFile.version', label: itemFileVersionTitle, operator: vm.dataTypeOperators.integer},
                {field: 'bom.itemNumber', label: bomItemNumberTitle, operator: vm.dataTypeOperators.string},
                {field: 'bom.notes', label: bomCommentsTitle, operator: vm.dataTypeOperators.nstring},
                {field: 'bom.refdes', label: bomRefdesTitle, operator: vm.dataTypeOperators.nstring}].sort();

            var searchItems = {
                itemType: null,
                operator: null,
                value: null,
                selectedType: null
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

            function onAddItems() {
                angular.forEach($scope.data.advancedFilters, function (filter) {

                    vm.items.push(filter);
                })
            }

            vm.itemType = null;
            vm.itemName = null;
            vm.onSelectType = onSelectType;
            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.itemType = itemType.id;
                    vm.itemName = itemType.name;
                }
            }

            vm.textValue = true;
            function itemTree(item) {
                if (item.label == "Item Type") {
                    vm.itemTree1 = true;
                    vm.textValue = false;
                    vm.part = false;
                }
                else if (item.label == "Lifecycle") {
                    vm.itemTree1 = false;
                    vm.textValue = false;
                    vm.part = true;
                } else if (item.label != "Lifecycle" && item.label != "Item Type") {
                    vm.itemTree1 = false;
                    vm.textValue = true;
                    vm.part = false;
                }
                $scope.$evalAsync();

            }

            function lifeCycleStatus(item) {
                LifecycleService.getAllPhases(item).then(
                    function (data) {
                        vm.lifeCyclePhase = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function deleteItem(item) {
                var index = vm.items.indexOf(item);
                vm.items.splice(index, 1);
            }

            function search() {
                if (validate()) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.items);
                }
            }

            function validate() {
                var valid = true;
                var valid1 = true;
                var count = vm.items.length;
                if (vm.items.length >= 1) {
                    angular.forEach(vm.items, function (item) {
                        if (item.field == undefined) {
                            valid1 = false;
                        }
                        else if (item.operator == null || item.operator == "" || item.operator == undefined) {
                            valid1 = false;
                        }
                        else if (item.field.label == "Item Type") {
                            item.value = vm.itemName;
                        }
                        else if (item.field.label == "Lifecycle") {
                            item.value = vm.phase;
                        }
                        else if ((item.value == null || item.value == "" || item.value == undefined ) &&
                            item.operator.name !== "isnotempty" && item.operator.name !== "isempty") {
                            valid1 = false;
                        }
                        if (valid1 == false && vm.items.length > 1) {
                            var index = vm.items.indexOf(item);
                            vm.items.splice(index, 1);
                        }
                    })
                }

                if (vm.items.length >= 1 && valid1 == false && vm.items.length >= count) {
                    valid = false;
                    $rootScope.showInfoMessage(advancedSearchMessage);
                } else {
                    $rootScope.closeNotification();
                }

                return valid;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    lifeCycleStatus();
                    if ($scope.data.advancedFilters != null) {
                        onAddItems();
                    } else {
                        onAddItem();
                    }

                    $rootScope.$on('app.items.advsearch', search);
                //}
            })();
        }
    }
);
