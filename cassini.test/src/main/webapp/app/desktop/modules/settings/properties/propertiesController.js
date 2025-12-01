define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('PropertiesController', PropertiesController);

        function PropertiesController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                      ObjectTypeAttributeService, LovService, $translate) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            $scope.deleteAttributeTitle = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_TITLE")).html();
            $scope.saveAttributeTitle = parsed.html($translate.instant("SAVE_ATTRIBUTE")).html();

            vm.selectedType = null;
            vm.objectTypes = [
                {label: "Run Scenario", name: 'RUNSCENARIO'},
                {label: "Run Plan", name: 'RUNPLAN'},
                {label: "Run Suite", name: 'RUNSUITE'},
                {label: "Run Case", name: 'RUNCASE'}

            ];
            vm.objectTypes1 = [
                {label: "Test Scenario", name: 'TESTSCENARIO'},
                {label: "Test Plan", name: 'TESTPLAN'},
                {label: "Test Suite", name: 'TESTSUITE'},
                {label: "Test Case", name: 'TESTCASE'},
                {label: "Test Run", name: 'TESTRUN'},
                {label: "Test Run Configuration", name: 'TESTRUNCONFIGURATION'}

            ];

            vm.properties = [];

            vm.lovs = null;
            vm.dataTypes = [
                'STRING',
                'INTEGER',
                'DOUBLE',
                'DATE',
                'BOOLEAN',
                'TIME',
                'TIMESTAMP',
                'IMAGE'

            ];

            vm.initDropdown = initDropdown;
            vm.initDatatypeDropdown = initDatatypeDropdown;
            vm.initReftypeDropdown = initReftypeDropdown;
            vm.initLovtypeDropdown = initLovtypeDropdown;
            vm.addProperty = addProperty;
            vm.deleteProperty = deleteProperty;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.promptDeleteProperty = promptDeleteProperty;
            vm.hideMask = hideMask;
            vm.loadProperties = loadProperties;
            vm.editProperty = editProperty;


            function initDropdown() {
                var b = '#dropdownMenuButton';
                var m = '#dropdownMenu';

                var pos = $(b).offset();
                var width = $(b).outerWidth();
                var calc = (pos.left + width) - 160;

                $(m).css({top: pos.top + 20, left: calc});
            }

            function initDatatypeDropdown(index) {
                var prop = vm.properties[index];
                if (prop.editMode) {
                    var b = '#dropdownMenuButton' + index;
                    var m = '#dropdownMenu' + index;

                    $(m).css({top: $(b).offset().top + 20, left: $(b).offset().left});
                    if (prop.newRefType == null) {
                        prop.refTypeMode = true;
                    }
                    if (prop.newLov == null) {
                        prop.lovTypeMode = true;
                    }
                }
            }

            function initReftypeDropdown(index) {
                var prop = vm.properties[index];
                if (prop.refTypeMode) {
                    var b = '#dropdownMenuButton1' + index;
                    var m = '#dropdownMenu1' + index;

                    $(m).css({top: $(b).offset().top + 20, left: $(b).offset().left});
                    prop.dataTypeMode = false;
                }
            }

            function initLovtypeDropdown(index) {
                var prop = vm.properties[index];
                if (prop.lovTypeMode) {
                    var b1 = '#dropdownMenuButton2' + index;
                    var m1 = '#dropdownMenu2' + index;

                    $(m1).css({top: $(b1).offset().top + 20, left: $(b1).offset().left});
                    prop.dataTypeMode = false;
                }
            }

            var newAttribute = parsed.html($translate.instant("NEW_ATTRIBUTE")).html();

            function addProperty() {
                var prop = {
                    id: null,
                    objectType: vm.selectedType.name,
                    name: newAttribute,
                    newName: newAttribute,
                    description: '',
                    dataType: null,
                    newDataType: null,
                    refType: null,
                    lov: null,
                    newRefType: null,
                    newLov: null,
                    required: false,
                    editMode: true,
                    dataTypeMode: true,
                    refTypeMode: false,
                    lovTypeMode: false,
                    showBusy: false
                };

                vm.properties.unshift(prop);

                $timeout(function () {
                    var index = vm.properties.indexOf(prop);
                    $('#nameInput' + index).select();
                }, 100);
            }

            function deleteProperty(prop) {
                ObjectTypeAttributeService.deleteObjectTypeAttribute(vm.selectedType, prop.id).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Attribute deleted successfully");
                        vm.properties.remove(prop);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function editProperty(prop) {
                prop.newName = prop.name;
                prop.newDescription = prop.description;
                prop.newDataType = prop.dataType;
                prop.dataTypeMode = true;
                prop.editMode = true;
                prop.refTypeMode = true;
                prop.lovTypeMode = true;
            }

            function applyChanges(prop) {
                prop.name = prop.newName;
                prop.description = prop.newDescription;
                prop.dataType = prop.newDataType;
                prop.refType = prop.newRefType;
                prop.lov = prop.newLov;
                if (validate(prop)) {
                    var promise = null;
                    if (prop.id == null) {
                        promise = ObjectTypeAttributeService.createObjectTypeAttribute(vm.selectedType, prop);
                    }
                    else {
                        promise = ObjectTypeAttributeService.updateObjectTypeAttribute(vm.selectedType, prop);
                    }
                    prop.showBusy = true;
                    promise.then(
                        function (data) {
                            prop.id = data.id;
                            prop.ppName = data.name;
                            $rootScope.showSuccessMessage("Attribute saved successfully");
                            prop.showBusy = false;
                            prop.editMode = false;
                            prop.refTypeMode = false;
                            prop.lovTypeMode = false;
                            prop.dataTypeMode = false;
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            prop.showBusy = false;
                        }
                    );
                }

            }

            function validate(prop) {
                var valid = true;
                if (prop.name == null || prop.name == undefined
                    || prop.name == "" || prop.name == newAttribute) {
                    $rootScope.showErrorMessage("Attribute Name cannot be empty");
                    valid = false;
                }
                else if (prop.dataType == null || prop.dataType == undefined
                    || prop.dataType == "") {
                    $rootScope.showErrorMessage("Attribute Data Type cannot be empty");
                    valid = false;
                } else {
                    angular.forEach(vm.properties, function (itemProp) {
                        if (itemProp.name == prop.newName && itemProp.id != prop.id) {
                            $rootScope.showErrorMessage("Attribute Name already exist, please enter another Name");
                            valid = false;
                        }
                    })
                }

                return valid;
            }


            function cancelChanges(prop) {
                prop.newName = prop.name;
                prop.newDescription = prop.description;
                prop.newDataType = prop.dataType;
                if (prop.id == null) {
                    vm.properties.remove(prop);
                }
            }

            function hideMask(index) {
                $('#propertyMask' + index).hide();
            }

            function loadProperties() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType(vm.selectedType.name).then(
                    function (data) {
                        vm.properties = data;
                        angular.forEach(vm.properties, function (prop) {
                            prop.ppName = prop.name;
                            prop.newName = vm.ppName;
                            prop.newDescription = prop.description;
                            prop.newDataType = prop.dataType;
                            prop.newRefType = prop.refType;
                            prop.newLov = prop.lov;
                        })
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function promptDeleteProperty(index) {
                var width = $('#property' + index).width();
                var height = $('#property' + index).height();

                $('#propertyMask' + index).width(width + 2);
                $('#propertyMask' + index).height(height + 2);
                $('#propertyMask' + index).css({display: 'table'});
            }

            function loadLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.lovs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadLovs();
            })();
        }
    }
);