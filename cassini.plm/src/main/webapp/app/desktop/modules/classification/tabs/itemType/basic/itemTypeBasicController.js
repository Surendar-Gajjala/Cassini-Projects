define(
    [
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('ItemTypeBasicController', ItemTypeBasicController);

        function ItemTypeBasicController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                         CommonService, $translate, ItemTypeService, AutonumberService, ClassificationService, LovService) {

            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.itemType = {
                name: null,
                description: null,
                tabs: null
            };
            vm.autoNumbers = [];
            vm.revSequences = [];
            vm.lifecycleStates = [];
            vm.lifecycles = [];
            vm.lovs = [];

            vm.onSave = onSave;
            vm.error = "";
            var nodeId = null;
            var parsed = angular.element("<div></div>");
            var bom = parsed.html($translate.instant("ITEM_DETAILS_TAB_BOM")).html();
            var changes = parsed.html($translate.instant("ITEM_DETAILS_TAB_CHANGES")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var relatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var mfrParts = parsed.html($translate.instant("ITEM_DETAILS_TAB_MANUFACTURER_PARTS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var projects = parsed.html($translate.instant("PROJECTS")).html();
            var requirements = parsed.html($translate.instant("REQUIREMENTS")).html();
            var Inspections = parsed.html($translate.instant("INSPECTIONS")).html();
            var specifications = parsed.html($translate.instant("COMPLIANCE")).html();
            var variance = parsed.html($translate.instant("VARIANCES")).html();
            var quality = parsed.html($translate.instant("QUALITY")).html();
            var itemHistory = parsed.html($translate.instant("ITEM_HISTORY")).html();
            var configured = parsed.html($translate.instant("ITEM_DETAILS_TAB_CONFIGURED")).html();
            var attributeUsedInExclusion = parsed.html($translate.instant("ATTRIBUTE_USED_IN_EXCLUSION")).html();
            var pleaseSaveNewLov = parsed.html($translate.instant("PLEASE_SAVE_NEW_LOV")).html();
            var enterOneListValue = parsed.html($translate.instant("ENTER_ONE_LIST_VALUE")).html();
            var enterListValue = parsed.html($translate.instant("ENTER_LIST_VALUE")).html();
            var valueCannotBe = parsed.html($translate.instant("VALUE_CANNOT_BE")).html();
            var enterValueListName = parsed.html($translate.instant("ENTER_VALUE_LIST_NAME")).html();
            var the_notAllowed = parsed.html($translate.instant("THE_NOT_ALLOWED")).html();
            var specialCharacterNotAllowed = parsed.html($translate.instant("SPECIAL_CHARACTER_NOT_ALLOWED")).html();
            var valueAlreadyExist = parsed.html($translate.instant("VALUE_ALREADY_EXIST")).html();
            var lifecycleHasNoValues = parsed.html($translate.instant("LIFECYCLE_HAS_NO_VALUES")).html();
            vm.rulesButton = parsed.html($translate.instant("EXCLUSIONS_BUTTON")).html();

            vm.tabs = [
                {label: bom, value: "bom", selected: true},
                {label: whereUsed, value: "whereUsed", selected: true},
                {label: changes, value: "changes", selected: true},
                {label: filesTabHeading, value: "files", selected: true},
                {label: variance, value: "variance", selected: true},
                {label: quality, value: "quality", selected: true},
                {label: mfrParts, value: "mfrParts", selected: true},
                {label: relatedItems, value: "relatedItems", selected: true},
                {label: projects, value: "projects", selected: true},
                {label: specifications, value: "specifications", selected: true},
                {label: requirements, value: "requirements", selected: true}
            ];

            if ($rootScope.selectedItemType.itemClass == "PRODUCT") {
                vm.tabs.push({label: Inspections, value: "inspections", selected: true});
            }

            vm.itemClasses = [
                {label: "Product", value: "PRODUCT"},
                {label: "Assembly", value: "ASSEMBLY"},
                {label: "Part", value: "PART"},
                {label: "Document", value: "DOCUMENT"},
                {label: "Other", value: "OTHER"}
            ];

            $rootScope.itemTypeSelected = itemTypeSelected;
            function itemTypeSelected() {
                if ($rootScope.selectedItemType != null) {
                    vm.itemType = $rootScope.selectedItemType;
                    if (!$scope.$$phase) $scope.$apply();
                    nodeId = $rootScope.selectedItemTypeNodId;
                    if (vm.itemType != null && vm.itemType != undefined) {
                        vm.itemType.parentTypeObject = {};
                        loadParentTypeObject();
                        loadItemTypeItems();
                        loadDisplayTabs();
                    }
                }


            }

            function loadParentTypeObject() {
                if (vm.itemType.parentType != null && vm.itemType.parentType !== 0 &&
                    vm.itemType.parentType !== undefined) {
                    ClassificationService.getType("ITEMTYPE", vm.itemType.parentType).then(
                        function (data) {
                            vm.itemType.parentTypeObject = data;
                            $scope.$off('app.itemType.selected', itemTypeSelected);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            function loadDisplayTabs() {
                if (vm.itemType.tabs != null) {
                    angular.forEach(vm.tabs, function (tab) {
                        tab.selected = false;
                        var index = vm.itemType.tabs.indexOf(tab.value);
                        if (index != -1) {
                            tab.selected = true;
                        }
                    })
                } else {
                    angular.forEach(vm.tabs, function (tab) {
                        tab.selected = true;
                    })
                }
                //$scope.$off('app.itemType.selected', itemTypeSelected);
            }

            vm.itemTypeItemExist = false;
            vm.itemTypeJson = new Map();
            vm.itemTypeJsonReflection = new Map();

            function loadItemTypeItems() {
                if (vm.itemType.id != null) {
                    ItemTypeService.getItemTypeItems(vm.itemType.id).then(
                        function (data) {
                            vm.itemTypeItems = data;
                            if (vm.itemTypeItems.length > 0) {
                                vm.itemTypeItemExist = true;
                            } else {
                                vm.itemTypeItemExist = false;
                            }
                            $scope.$off('app.itemType.selected', itemTypeSelected);

                        }
                    )
                }
            }

            var itemTypeSuccessMessage = $translate.instant("ITEM_TYPE_SUCCESS_MESSAGE");
            var itemTypeUpdateMessage = $translate.instant("ITEM_TYPE_UPDATE_MESSAGE");
            var Duplicate = $translate.instant("DUPLICATE_MESSAGE");

            function onSave() {
                if (vm.itemType != null && validate()) {
                    var id = $rootScope.selectedClassificationType.id;
                    $rootScope.selectedClassificationType.id = null;
                    vm.itemType.tabs = [];
                    angular.forEach(vm.tabs, function (tab) {
                        if (tab.selected) {
                            vm.itemType.tabs.push(tab.value);
                        }
                    })
                    ClassificationService.updateType('ITEMTYPE', vm.itemType).then(
                        function (data) {
                            if (data != null) {
                                $rootScope.selectedClassificationType = data;
                                $rootScope.showSuccessMessage(data.name + " : " + itemTypeUpdateMessage);
                                $rootScope.$broadcast("app.classification.update", {
                                    nodeId: nodeId,
                                    nodeName: vm.itemType.name
                                })
                            }
                            else if (data == null) {
                                $rootScope.selectedClassificationType.id = id;
                                $rootScope.showErrorMessage(vm.itemType.name + " : " + Duplicate);
                            }
                        },
                        function (error) {
                            $rootScope.selectedClassificationType.id = id;
                            $rootScope.showErrorMessage(vm.itemType.name + " : " + Duplicate);
                        }
                    )
                }
            }

            var itemTypeValidation = $translate.instant("ITEM_TYPE_NAME_EMPTY");
            var lifecycleValidation = $translate.instant("SELECT_LIFECYCLE");
            var itemClassValidation = $translate.instant("SELECT_ITEM_CLASS");

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.itemType.name == null || vm.itemType.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(itemTypeValidation);
                } else if (vm.itemType.lifecycle == null || vm.itemType.lifecycle == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(lifecycleValidation);
                } else if (vm.itemType.itemClass == null || vm.itemType.itemClass == "" || vm.itemType.itemClass == undefined) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(itemClassValidation);
                }

                return vm.valid;
            }

            $scope.getLimitedWord = getLimitedWord;
            function getLimitedWord(word, size) {
                if (word.length <= size) {
                    return word;
                } else {
                    return word.substr(0, size) + '...';
                }
            }

            vm.onSelectLifecycle = onSelectLifecycle;
            function onSelectLifecycle(lifecycle) {
                if (lifecycle.phases.length == 0) {
                    vm.itemType.lifecycle = null;
                    $rootScope.showWarningMessage(lifecycle.name + " : " + lifecycleHasNoValues);
                }
            }

            vm.onSelectItemClass = onSelectItemClass;
            function onSelectItemClass(itemClass) {
                vm.itemType.itemClass = itemClass.value;
            }


            (function () {
                $scope.$on('app.itemType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        $rootScope.itemTypeSelected();
                        $scope.$on('app.itemtype.save', onSave);
                    }
                })
            })();
        }
    }
)
;