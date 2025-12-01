define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/lifecycleService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/desktop/modules/settings/relationships/new/relationClassificationTreeDirective',
        'app/desktop/modules/classification/directive/itemClassTreeDirective',
        'app/desktop/modules/settings/relationships/new/relationClassificationTreeController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService'
    ],
    function (module) {
        module.controller('SearchDialogueController', SearchDialogueController);

        function SearchDialogueController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies,
                                          ItemService, LifecycleService, LoginService, ObjectTypeAttributeService) {

            var parsed = angular.element("<div></div>");
            var vm = this;
            var searchVaildation = parsed.html($translate.instant("SEARCH_MESSAGE_VALIDATION")).html();
            var containsTitle = parsed.html($translate.instant("CONTAINS_TITLE")).html();
            var startsWithTitle = parsed.html($translate.instant("STARTS_WITH_TITLE")).html();
            var endsWithTitle = parsed.html($translate.instant("ENDS_WITH_TITLE")).html();
            var equalsTitle = parsed.html($translate.instant("EQUALS_TITLE")).html();
            var lessthanTitle = parsed.html($translate.instant("LESS_THAN_TITLE")).html();
            var greaterthanTitle = parsed.html($translate.instant("GREATER_THAN_TITLE")).html();
            var lessthanorequalsTitle = parsed.html($translate.instant("LESSTHAN_OR_EQUALS_TITLE")).html();
            var greaterthanorequalsTitle = parsed.html($translate.instant("GREATERTHAN_OR_EQUALS_TITLE")).html();
            var ecoNumberTitle = parsed.html($translate.instant("ECO_NUMBER_TITLE")).html();
            var ecoDescriptionTitle = parsed.html($translate.instant("ECO_DESCRIPTION_TITLE")).html();
            var ecoStatusTitle = parsed.html($translate.instant("ECO_STATUS_TITLE")).html();
            var ecoTitle = parsed.html($translate.instant("ECO_TITLE")).html();
            var ecoFileNameTitle = parsed.html($translate.instant("ECO_FILE_NAME_TITLE")).html();
            var ecoFileVersionTitle = parsed.html($translate.instant("ECO_FILE_VERSION_TITLE")).html();
            vm.selectTitle = parsed.html($translate.instant("SELECT")).html();
            vm.enterValueTitle = parsed.html($translate.instant("ENTER_VALUE_TITLE")).html();
            var advancedSearchMessage = parsed.html($translate.instant("ADVANCED_SEARCH_MESSAGE")).html();
            var isemptyTitle = parsed.html($translate.instant("IS_EMPTY_TITLE")).html();
            var doesNotContainTitle = parsed.html($translate.instant("DOES_NOT_CONTAIN_TITLE")).html();
            var doesNotStartWithTitle = parsed.html($translate.instant("DOES_NOT_START_WITH_TITLE")).html();
            var doesNotEndWithTitle = parsed.html($translate.instant("DOES_NOT_END_WITH_TITLE")).html();
            var isNotEmptyTitle = parsed.html($translate.instant("IS_NOT_EMPTY_TITLE")).html();
            var notEqualTitle = parsed.html($translate.instant("NOT_EQUAL_TITLE")).html();
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
            var attributeValue = parsed.html($translate.instant("ATTRIBUTE_VALUE")).html();
            vm.selectTitle = parsed.html($translate.instant("SELECT")).html();
            vm.enterValueTitle = parsed.html($translate.instant("ENTER_VALUE_TITLE")).html();
            var lifeCycle = parsed.html($translate.instant("ITEM_ALL_LIFECYCLE")).html();

            vm.itemSimpleSearch = false;
            vm.ecoSimpleSearch = false;
            vm.itemAdvanceSearch = false;
            vm.ecoAdvanceSearch = false;
            vm.objectType = $scope.data.selectedType;
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
            $scope.itemsMode = $scope.data.itemsMode;

            vm.onSelectType = onSelectType;
            vm.search = search;

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    if (vm.itemSimpleSearch == true) {
                        vm.selectedType = itemType;
                        vm.itemType = itemType;
                        vm.filters.itemType = itemType;
                    } else if (vm.itemAdvanceSearch == true) {
                        vm.itemType = itemType.id;
                        vm.itemName = itemType.name;
                    }
                }
            }

            function search() {
                if (vm.objectType == "ITEM") {
                    if (vm.itemSimpleSearch == true) {
                        if (itemSimpleValidate()) {
                            $rootScope.hideSidePanel();
                            $scope.callback(vm.filters);
                        }
                    } else if (vm.itemAdvanceSearch == true) {
                        if (itemAdvanceValidate()) {
                            $rootScope.hideSidePanel();
                            $scope.callback(vm.items);
                        }
                    }

                } else if (vm.objectType == "ECO") {
                    if (vm.ecoSimpleSearch == true) {
                        if (ecoSampleValidate()) {
                            $scope.callback(vm.ecoFilters);
                            $rootScope.hideSidePanel();
                        }
                    } else if (vm.ecoAdvanceSearch == true) {
                        if (ecoAdvanceValidate()) {
                            $scope.callback(vm.ecos);
                            $rootScope.hideSidePanel();
                        }
                    }
                }
            }

            function itemSimpleValidate() {
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

            /* ----------------- itemAdvancedSearch ---------------------*/

            vm.row = null;
            vm.search = search;
            vm.onAddItem = onAddItem;
            vm.show = false;
            vm.itemTree = itemTree;
            vm.item = null;
            vm.deleteItem = deleteItem;
            vm.items = [];

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
                ],
                boolean: [
                    {name: 'equals', label: equalsTitle}
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
                selectedType: null,
                textValue: true,
                itemTree1: false,
                part: false,
                attributeId: null
            };

            vm.selectedType = {
                name: null
            };

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
            vm.onSelectItemType = onSelectItemType;
            function onSelectItemType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.itemType = itemType.id;
                    vm.itemName = itemType.name;
                }
            }

            //vm.textValue = true;
            function itemTree(item, item1) {
                if (item.label == itemItemTypeTitle) {
                    item1.itemTree1 = true;
                    item1.textValue = false;
                    item1.part = false;
                }
                else if (item.label == lifeCycle) {
                    item1.itemTree1 = false;
                    item1.textValue = false;
                    item1.part = true;
                } else if (item.label != lifeCycle && item.label != itemItemTypeTitle) {
                    item1.itemTree1 = false;
                    item1.textValue = true;
                    item1.part = false;
                }
                if (item.attributeId != undefined && item.attributeId != null)
                    item1.attributeId = item.attributeId;
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

            /*   function search() {
             if (itemAdvanceValidate()) {
             $rootScope.hideSidePanel();
             $scope.callback(vm.items);
             }
             }*/

            function itemAdvanceValidate() {
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


            /*-----------------------ecoSimpleSearch -------------------------------- */

            vm.selectTitle = parsed.html($translate.instant("SELECT")).html();
            vm.selectTitle1 = parsed.html($translate.instant("SELECT")).html();
            vm.descriptionTitle = parsed.html($translate.instant("DESCRIPTION")).html();
            vm.numberTitle = parsed.html($translate.instant("NUMBER")).html();
            vm.title = parsed.html($translate.instant("TITLE")).html();
            var searchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();

            vm.search = search;
            vm.ecoStatuses = [];
            vm.ecoFilters = {
                ecoNumber: null,
                title: null,
                description: null,
                status: null,
                statusType: null,
                //ecoOwnerObject: $application.login.person
                ecoOwnerObject: {
                    id: null
                }
            };

            vm.Persons = [];

            function ecoSampleValidate() {
                var valid = true;
                $rootScope.closeNotification();
                if ((vm.ecoFilters.ecoNumber != "" && vm.ecoFilters.ecoNumber != null && vm.ecoFilters.ecoNumber != undefined) ||
                    (vm.ecoFilters.statusType != null) ||
                    (vm.ecoFilters.ecoOwnerObject.id != null) ||
                    (vm.ecoFilters.title != "" && vm.ecoFilters.title != null && vm.ecoFilters.title != undefined) ||
                    (vm.ecoFilters.description != "" && vm.ecoFilters.description != null && vm.ecoFilters.description != undefined) ||
                    (vm.ecoFilters.status != "" && vm.ecoFilters.status != null && vm.ecoFilters.status != undefined)) {
                    valid = true;
                } else {
                    $rootScope.showInfoMessage(searchValue);
                    valid = false;
                }

                return valid;
            }

            /* function search() {
             if (ecoSamplevalidate()) {
             $scope.callback(vm.filters);
             $rootScope.hideSidePanel();
             }
             }*/

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            /* --------------------------ecoAdvancedSearch --------------------------*/
            vm.onAddeco = onAddeco;
            vm.deleteeco = deleteeco;
            vm.search = search;
            vm.show = false;
            vm.ecos = [];

            vm.ecoNames = [
                {field: 'eco.number', label: ecoNumberTitle, operator: vm.dataTypeOperators.string},
                {field: 'eco.description', label: ecoStatusTitle, operator: vm.dataTypeOperators.string},
                {field: 'eco.status', label: ecoDescriptionTitle, operator: vm.dataTypeOperators.string},
                {field: 'eco.title', label: ecoTitle, operator: vm.dataTypeOperators.string},
                //{field: 'eco.owner', operator: vm.dataTypeOperators.string},
                {field: 'ecoFile.name', label: ecoFileNameTitle, operator: vm.dataTypeOperators.string},
                {field: 'ecoFile.version', label: ecoFileVersionTitle, operator: vm.dataTypeOperators.integer}].sort();

            var searchEcos = {
                ecoType: null,
                operator: null,
                value: null
            };

            vm.selectedType = {
                name: null
            };

            function onAddeco() {
                var newRow = angular.copy(searchEcos);
                newRow.parent = vm.row;
                vm.ecos.push(newRow);
            }

            function onAddEcos() {
                angular.forEach($scope.data.ecoFilters, function (filter) {
                    vm.ecos.push(filter);
                })
            }

            /* function search() {
             if (ecoAdvanceValidate()) {
             $scope.callback(vm.ecos);
             $rootScope.hideSidePanel();
             }

             }*/

            function ecoAdvanceValidate() {
                var valid = true;
                var valid1 = true;
                var count = vm.ecos.length;
                if (vm.ecos.length >= 1) {
                    angular.forEach(vm.ecos, function (item) {
                        if (item.field == undefined) {
                            valid1 = false;
                        }
                        else if (item.operator == null || item.operator == "" || item.operator == undefined) {
                            valid1 = false;
                        }
                        else if (item.value == null || item.value == "" || item.value == undefined) {
                            valid1 = false;
                        }
                        if (valid1 == false && vm.ecos.length > 1) {
                            var index = vm.ecos.indexOf(item);
                            vm.ecos.splice(index, 1);
                        }
                    })
                }

                if (vm.ecos.length >= 1 && valid1 == false && vm.ecos.length >= count) {
                    valid = false;
                    $rootScope.showInfoMessage(advancedSearchMessage);
                } else {
                    $rootScope.closeNotification();
                }

                return valid;
            }

            function deleteeco(eco) {
                var index = vm.ecos.indexOf(eco);
                vm.ecos.splice(index, 1);
            }

            vm.selectType = selectType;
            function selectType(value, event) {
                if (value == "Simple") {
                    if (vm.objectType == "ITEM") {
                        vm.items = [];
                        vm.itemSimpleSearch = true;
                        vm.itemAdvanceSearch = false;
                    } else if (vm.objectType == 'ECO') {
                        vm.ecos = [];
                        vm.ecoSimpleSearch = true;
                        vm.ecoAdvanceSearch = false;
                    }
                }
                if (value == "Advance") {
                    if (vm.objectType == "ITEM") {
                        vm.items = [];
                        onAddItem();
                        vm.itemAdvanceSearch = true;
                        vm.itemSimpleSearch = false;
                    } else if (vm.objectType == 'ECO') {
                        vm.ecos = [];
                        onAddeco();
                        vm.ecoAdvanceSearch = true;
                        vm.ecoSimpleSearch = false;
                    }
                }
            }

            var attributeTypes = ['STRING', 'INTEGER', 'DOUBLE', 'BOOLEAN', 'LIST', 'TEXT', 'LONGTEXT', 'RICHTEXT'];

            function loadItemTypeAttributes() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType('ITEMTYPE').then(
                    function (data) {
                        var typeAttributes = data;
                        angular.forEach(typeAttributes, function (att) {
                            if (attributeTypes.indexOf(att.dataType) != -1) {
                                var obj1 = {
                                    field: 'attribute.' + att.name,
                                    label: 'attribute.' + att.name,
                                    operator: vm.dataTypeOperators.string,
                                    attributeId: att.id
                                };
                                if (att.dataType == 'DOUBLE' || att.dataType == 'INTEGER') {
                                    obj1.operator = vm.dataTypeOperators.integer;
                                } else if (att.dataType == 'BOOLEAN') {
                                    obj1.operator = vm.dataTypeOperators.boolean;
                                }

                                vm.itemNames.push(obj1);
                            }

                        });
                        //loadItemAttributes();
                    }
                )
            }

            function loadItemAttributes() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType('ITEM').then(
                    function (data) {
                        var typeAttributes = data;
                        angular.forEach(typeAttributes, function (att) {
                            var obj1 = {
                                field: 'attribute.' + att.name,
                                label: 'attribute.' + att.name,
                                operator: vm.dataTypeOperators.string,
                                attributeId: att.id
                            };
                            if (att.dataType == 'DOUBLE' || att.dataType == 'INTEGER') {
                                obj1.operator = vm.dataTypeOperators.integer;
                            } else if (att.dataType == 'BOOLEAN') {
                                obj1.operator = vm.dataTypeOperators.boolean;
                            }

                            vm.itemNames.push(obj1);
                        });
                    })
            }

            (function () {
                $rootScope.$on('app.items.search', search);
                var objectType = $scope.data.selectedType;
                loadPersons();
                loadItemTypeAttributes();
                if (objectType == 'ITEM') {
                    lifeCycleStatus();
                    vm.itemSimpleSearch = true;
                } else {
                    vm.ecoSimpleSearch = true;
                }
            })();
        }
    }
);