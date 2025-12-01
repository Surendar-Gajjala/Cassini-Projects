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
        module.controller('CustomSearchDialogueController', CustomSearchDialogueController);

        function CustomSearchDialogueController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies,
                                                ItemService, LifecycleService, LoginService, ObjectTypeAttributeService) {

            var parsed = angular.element("<div></div>");
            var vm = this;

            var objectId = $scope.data.objectTypeId;
            var objectType = $scope.data.selectedType;
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
            var itemItemTypeTitle = parsed.html($translate.instant("ITEM_ITEMTYPE_TITLE")).html();
            vm.enterValueTitle = parsed.html($translate.instant("ENTER_VALUE_TITLE")).html();
            var lifeCycle = parsed.html($translate.instant("ITEM_ALL_LIFECYCLE")).html();

            vm.itemSimpleSearch = false;
            vm.customSimpleSearch = false;
            vm.ecoSimpleSearch = false;
            vm.itemAdvanceSearch = false;
            vm.customAdvanceSearch = false;
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
                itemNumber: null
            };

            vm.filters = {
                itemType: null,
                itemNumber: null,
                revision: null,
                status: null,
                itemName: null,
                itemClass: ''
            };

            vm.customFilters = {
                type: $scope.data.objectTypeId,
                number: null,
                name: null,
                description: null
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
                if (vm.objectType == "CUSTOMOBJECTTYPE") {
                    if (vm.customSimpleSearch == true) {
                        if (customSimpleValidate()) {
                            $scope.callback(vm.customFilters);
                            $rootScope.hideSidePanel();
                        }
                    } else if (vm.customAdvanceSearch == true) {
                        if (customAdvanceValidate()) {
                            $scope.callback(vm.customObjects);
                            $rootScope.hideSidePanel();
                        }
                    }
                }
            }

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

            vm.customNames = [
                {field: 'object.number', label: "Object Number", operator: vm.dataTypeOperators.string},
                {field: 'object.description', label: "Object Description", operator: vm.dataTypeOperators.nstring},
                {field: 'object.name', label: "Object Name", operator: vm.dataTypeOperators.string}].sort();

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

            var searchObjects = {
                type: null,
                operator: null,
                value: null,
                textValue: true
            };

            vm.selectedType = {
                name: null
            };

            function onAddeco() {
                var newRow = angular.copy(searchEcos);
                newRow.parent = vm.row;
                vm.ecos.push(newRow);
            }

            vm.onAddObject = onAddObject;
            vm.customObjects = [];
            function onAddObject() {
                var newRow = angular.copy(searchObjects);
                newRow.parent = vm.row;
                vm.customObjects.push(newRow);
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

            function customAdvanceValidate() {
                var valid = true;
                var valid1 = true;
                var count = vm.customObjects.length;
                if (vm.customObjects.length >= 1) {
                    angular.forEach(vm.customObjects, function (item) {
                        if (item.field == undefined) {
                            valid1 = false;
                        }
                        else if (item.operator == null || item.operator == "" || item.operator == undefined) {
                            valid1 = false;
                        }
                        else if (item.value == null || item.value == "" || item.value == undefined) {
                            valid1 = false;
                        }
                        if (valid1 == false && vm.customObjects.length > 1) {
                            var index = vm.customObjects.indexOf(item);
                            vm.customObjects.splice(index, 1);
                        }
                    })
                }

                if (vm.customObjects.length >= 1 && valid1 == false && vm.customObjects.length >= count) {
                    valid = false;
                    $rootScope.showInfoMessage(advancedSearchMessage);
                } else {
                    $rootScope.closeNotification();
                }

                return valid;
            }

            function customSimpleValidate() {
                var valid = true;
                $rootScope.closeNotification();

                if ((vm.customFilters.name != "" && vm.customFilters.name != null && vm.customFilters.name != undefined) ||
                    (vm.customFilters.number != "" && vm.customFilters.number != null && vm.customFilters.number != undefined) ||
                    (vm.customFilters.description != "" && vm.customFilters.description != null && vm.customFilters.description != undefined)) {
                    valid = true;
                } else {
                    $rootScope.showInfoMessage(searchVaildation);
                    valid = false;
                }

                return valid;
            }

            function deleteeco(eco) {
                var index = vm.ecos.indexOf(eco);
                vm.ecos.splice(index, 1);
            }

            vm.deleteCustomObject = deleteCustomObject;
            function deleteCustomObject(obj) {
                var index = vm.customObjects.indexOf(obj);
                vm.customObjects.splice(index, 1);
            }

            vm.selectType = selectType;
            function selectType(value, event) {
                if (value == "Simple") {
                    if (vm.objectType == 'CUSTOMOBJECTTYPE') {
                        vm.customObjects = [];
                        vm.customSimpleSearch = true;
                        vm.customAdvanceSearch = false;
                    }
                }
                if (value == "Advance") {
                    if (vm.objectType == 'CUSTOMOBJECTTYPE') {
                        vm.customObjects = [];
                        onAddObject();
                        vm.customSimpleSearch = false;
                        vm.customAdvanceSearch = true;
                    }
                }
            }

            var attributeTypes = ['STRING', 'INTEGER', 'DOUBLE', 'BOOLEAN', 'LIST', 'TEXT', 'LONGTEXT', 'RICHTEXT'];

            function loadItemTypeAttributes() {
                ObjectTypeAttributeService.getObjectTypeAttributesByObjectIdAndObjectType(objectId, objectType).then(
                    function (data) {
                        var typeAttributes = data.objectTypeAttributes;
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
                                vm.customNames.push(obj1);
                            }
                        });
                    }
                )
            }


            (function () {
                $rootScope.$on('app.customObjects.search', search);
                loadPersons();
                loadItemTypeAttributes();
                selectType('Simple', "");
            })();
        }
    }
);