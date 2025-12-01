define(
    [
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('CustomObjectTypeBasicController', CustomObjectTypeBasicController);

        function CustomObjectTypeBasicController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, CustomObjectTypeService,
                                                 CommonService, $translate, CustomObjectService, AutonumberService, LovService) {

            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.customType = {
                name: null,
                description: null,
                isRevisioned: true,
                hasLifecycle: true,
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
            var lifecycleHasNoValues = parsed.html($translate.instant("LIFECYCLE_HAS_NO_VALUES")).html();
            vm.rulesButton = parsed.html($translate.instant("EXCLUSIONS_BUTTON")).html();
            var bom = parsed.html($translate.instant("ITEM_DETAILS_TAB_BOM")).html();
            var relatedItems = parsed.html($translate.instant("RELATED_DETAILS_TAB_OBJECT")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var whereUsedTabHeading = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();

            vm.tabs = [
                {label: bom, value: "bom", selected: false},
                {label: relatedItems, value: "relatedObjects", selected: false},
                {label: whereUsedTabHeading, value: "whereUsed", selected: false}
            ];

            $rootScope.customTypeSelected = customTypeSelected;
            function customTypeSelected() {
                vm.customType = $rootScope.selectedCustomType;
                if (!$scope.$$phase) $scope.$apply();
                nodeId = $rootScope.selectedCustomTypeNodId;
                if (vm.customType != null && vm.customType != undefined) {
                    vm.customType.parentTypeObject = {};
                    loadTypeObjects();
                    loadDisplayTabs();
                }
            }
            vm.disableTab = disableTab;
            function disableTab(type, tab) {
                var valid = true;
                if (type == "CUSTOMOBJECT") {
                    if (tab.value == "bom" || tab.value == "relatedObjects" || tab.value == "whereUsed") {
                        valid = true;
                        tab.selected = false;
                    }
                } 
                return valid;
            }

            var customTypeUpdateMessage = $translate.instant("CUSTOM_TYPE_UPDATE_MESSAGE");
            var Duplicate = $translate.instant("DUPLICATE_MESSAGE");

            function onSave() {
                if (vm.customType != null && validate()) {
                    var id = $rootScope.selectedClassificationType.id;
                    $rootScope.selectedClassificationType.id = null;
                    vm.customType.tabs = [];
                    angular.forEach(vm.tabs, function (tab) {
                        if (tab.selected) {
                            vm.customType.tabs.push(tab.value);
                        }
                    });
                    CustomObjectTypeService.updateCustomObjectType(vm.customType).then(
                        function (data) {
                            if (data != null) {
                                $rootScope.selectedClassificationType = data;
                                $rootScope.showSuccessMessage(data.name + " : " + customTypeUpdateMessage);
                                $rootScope.$broadcast("app.classification.update", {
                                    nodeId: nodeId,
                                    nodeName: vm.customType.name
                                })
                                $rootScope.loadCustomObjectTypes();
                            }
                            else if (data == null) {
                                $rootScope.selectedClassificationType.id = id;
                                $rootScope.showErrorMessage(vm.customType.name + " : " + Duplicate);
                            }
                        },
                        function (error) {
                            $rootScope.selectedClassificationType.id = id;
                            $rootScope.showErrorMessage(vm.customType.name + " : " + Duplicate);
                        }
                    )
                }
            }

            var customTypeValidation = $translate.instant("CUSTOM_TYPE_NAME_EMPTY");

            function validate() {
                vm.valid = true;
                $rootScope.closeNotification();
                if (vm.customType.name == null || vm.customType.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(customTypeValidation);
                } else if (vm.customType.isRevisioned && vm.customType.revisionSequence == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("please select the RevisionSequence");
                } else if (vm.customType.hasLifecycle && vm.customType.hasLifecycle == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("please select the Lifecycle");
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

            function loadDisplayTabs() {
                if (vm.customType.tabs != null) {
                    angular.forEach(vm.tabs, function (tab) {
                        tab.selected = false;
                        var index = vm.customType.tabs.indexOf(tab.value);
                        if (index != -1) {
                            tab.selected = false;
                        }
                    })
                } else {
                    angular.forEach(vm.tabs, function (tab) {
                        tab.selected = false;
                    })
                }
            }


            vm.onSelectLifecycle = onSelectLifecycle;
            function onSelectLifecycle(lifecycle) {
                if (lifecycle.phases.length == 0) {
                    vm.customType.lifecycle = null;
                    $rootScope.showWarningMessage(lifecycle.name + " : " + lifecycleHasNoValues);
                }
            }

            vm.selectRevision = selectRevision;
            function selectRevision(customType) {
                if (customType.isRevisioned) {
                    customType.hasLifecycle = true;
                } else {
                    customType.hasLifecycle = false;
                }
            }

            function loadTypeObjects() {
                if (vm.customType.id != null) {
                    CustomObjectService.getCustomObjectsByType(vm.customType.id).then(
                        function (data) {
                            vm.typeObjects = data;
                            if (vm.typeObjects > 0) {
                                vm.typeObjectsExist = true;
                            } else {
                                vm.typeObjectsExist = false;
                            }
                        }
                    )
                }
            }

            (function () {
                $scope.$on('app.customType.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        $rootScope.customTypeSelected();
                        $scope.$on('app.customType.save', onSave);
                    }
                })
                /*  $scope.$on('app.customType.selected', customTypeSelected);
                 $scope.$on('app.customType.save', onSave);*/
            })();
        }
    }
)
;