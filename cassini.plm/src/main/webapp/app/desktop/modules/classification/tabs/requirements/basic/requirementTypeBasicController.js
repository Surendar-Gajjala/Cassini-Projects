define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('RequirementTypeBasicController', RequirementTypeBasicController);
        function RequirementTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                                ClassificationService, ItemTypeService, AutonumberService,
                                                CommonService, DialogService, LovService, RequirementsTypeService) {

            var parsed = angular.element("<div></div>");
            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.objectType = {
                name: null,
                description: null
            };
            vm.autoNumbers = [];
            vm.revSequences = [];
            vm.lifecycles = [];
            vm.lovs = [];
            vm.refTypes = ['ITEM', 'ITEMREVISION', 'CHANGE', 'WORKFLOW', 'MANUFACTURER', 'MANUFACTURERPART', 'PERSON', 'PROJECT', 'REQUIREMENT'];
            var nodeId = null;
            function reqTypeSelected(event, args) {
                vm.objectType = args.typeObject;
                /*$scope.$apply();*/
                nodeId = args.nodeId;
                if (vm.objectType != null && vm.objectType != undefined) {
                    if (vm.objectType.editable == undefined) {
                        vm.objectType.editable = true;
                    }
                    if (vm.objectType.id == undefined || vm.objectType.id == null) {
                        vm.attributesShow = false;
                        //$scope.$apply();
                    } else {
                        vm.attributesShow = true;
                        //$scope.$apply();
                    }
                    vm.objectType.attributes = [];
                    loadAutoNumbers();
                    loadLovs();
                    loadItemTypeItems();
                } else {
                    //$scope.$apply();
                }
            }

            vm.objectTypeItemExist = false;

            function loadItemTypeItems() {
                if (vm.objectType.id != null) {
                    RequirementsTypeService.getReqTypeItems(vm.objectType.id).then(
                        function (data) {
                            vm.itemTypeItems = data;
                            if (vm.itemTypeItems > 0) {
                                vm.objectTypeItemExist = true;
                            } else {
                                vm.objectTypeItemExist = false;
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
            }

            function cancelChanges(attribute) {
                vm.hide = false;
                if (attribute.id == null || attribute.id == undefined) {
                    vm.objectType.attributes.splice(vm.objectType.attributes.indexOf(attribute), 1);
                    vm.error = null;
                }
                else {
                    attribute.newName = attribute.name;
                    attribute.newDescription = attribute.description;
                    attribute.newDataType = attribute.dataType;
                    attribute.editMode = false;
                    attribute.inVisible = false;
                    attribute.defaultValue = false;
                    vm.error = null;
                    $timeout(function () {
                        attribute.showValues = true;
                    }, 500);
                }
            }

            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autoNumbers = data;
                        angular.forEach(data, function (item) {
                            if (vm.objectType.numberSource == null &&
                                item.name == "Default Part Number Source") {
                                vm.objectType.numberSource = item;
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function loadLovs() {
                CommonService.getLovByType('Revision Sequence').then(
                    function (data) {
                        vm.revSequences = data;
                        angular.forEach(data, function (item) {
                            if (item.name == 'Default Revision Sequence') {
                                vm.objectType.revisionSequence = item;
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                );

                ItemTypeService.getLifeCycles().then(
                    function (data) {
                        vm.lifecycles = data;
                        angular.forEach(data, function (item) {
                            if (item.name == 'Default Requirements Lifecycle') {
                                vm.objectType.lifecycle = item;
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                );
            }

            function loadAllLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        var emptyLov = {
                            id: null,
                            name: "Add New"
                        }
                        vm.lovs = data;
                        vm.lovs.unshift(emptyLov);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.newLov = "";
            $scope.getLimitedWord = getLimitedWord;
            function getLimitedWord(word, size) {
                if (word.length <= size) {
                    return word;
                } else {
                    return word.substr(0, size) + '...';
                }
            }

            var requirementTypeSuccessMessage = $translate.instant("REQUIREMENT_TYPE_SUCCESS_MESSAGE");
            var requirementTypeUpdateMessage = $translate.instant("REQUIREMENT_UPDATE_MESSAGE");
            var specTypeSuccessMessage = $translate.instant("SPEC_TYPE_SUCCESS_MESSAGE");
            var specTypeUpdateMessage = $translate.instant("SPEC_UPDATE_MESSAGE");
            var Duplicate = $translate.instant("DUPLICATE_MESSAGE");

            vm.onSave = onSave;
            function onSave() {
                if (vm.objectType != null && validate()) {
                    var id = $rootScope.selectedClassificationType.id;
                    $rootScope.selectedClassificationType.id = null;
                    if (vm.objectType.objectType == 'REQUIREMENTTYPE') {

                        ClassificationService.updateType('REQUIREMENTTYPE', vm.objectType).then(
                            function (data) {
                                if (data != null) {
                                    $rootScope.selectedClassificationType = data;
                                    $rootScope.showSuccessMessage(data.name + " : " + requirementTypeUpdateMessage);
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.objectType.name
                                    })
                                }
                                else if (data == null) {
                                    $rootScope.selectedClassificationType.id = id;
                                    $rootScope.showErrorMessage(vm.objectType.name + " : " + Duplicate);
                                }
                            },
                            function (error) {
                                $rootScope.selectedClassificationType.id = id;
                                $rootScope.showErrorMessage(vm.objectType.name + " : " + Duplicate);
                            }
                        )

                    }

                    else if (vm.objectType.objectType == 'SPECIFICATIONTYPE') {

                        ClassificationService.updateType('SPECIFICATIONTYPE', vm.objectType).then(
                            function (data) {
                                if (data != null) {
                                    $rootScope.selectedClassificationType = data;
                                    $rootScope.showSuccessMessage(data.name + " : " + specTypeUpdateMessage);
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.objectType.name
                                    })
                                }
                                else if (data == null) {
                                    $rootScope.selectedClassificationType.id = id;
                                    $rootScope.showErrorMessage(vm.objectType.name + " : " + Duplicate);
                                }
                            },
                            function (error) {
                                $rootScope.selectedClassificationType.id = id;
                                $rootScope.showErrorMessage(vm.objectType.name + " : " + Duplicate);
                            }
                        )

                    }

                }
            }

            var specTypeValidation = $translate.instant("SPECIFICATION_TYPE_NAME_VALIDATION");
            var reqTypeValidation = $translate.instant("REQUIREMENT_TYPE_NAME_VALIDATION");

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.objectType.name == null || vm.objectType.name == "") {
                    vm.valid = false;
                    if(vm.objectType.objectType == 'SPECIFICATIONTYPE'){
                        $rootScope.showWarningMessage(specTypeValidation);
                    }
                    if(vm.objectType.objectType == 'REQUIREMENTTYPE'){
                        $rootScope.showWarningMessage(reqTypeValidation);
                    }
                }
                return vm.valid;
            }


            (function () {
                $scope.$on('app.rmType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        $scope.$on('app.rmType.selected', reqTypeSelected);
                        $scope.$on('app.rmType.save', onSave);
                        loadAllLovs();
                    }
                })
                //if ($application.homeLoaded == true) {
                    $scope.$on('app.rmType.selected', reqTypeSelected);
                    $scope.$on('app.rmType.save', onSave);
                    loadAllLovs();

                //}
            })();
        }
    }
);