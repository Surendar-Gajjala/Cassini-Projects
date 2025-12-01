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
                                      ObjectTypeAttributeService, LovService) {
            var vm = this;

            vm.selectedType = null;
            vm.objectTypes = [
                {label: "Item", name: 'ITEMMASTER'},
                {label: "Item Revision", name: 'ITEMREVISION'},
                {label: "Item Instance", name: 'ITEMINSTANCE'},
                {label: "Bom Item", name: 'BOMITEM'},
                {label: "Bom Instance Item", name: 'BOMINSTANCEITEM'},
                {label: "Inward", name: 'INWARD'},
                {label: "Inward Item", name: 'INWARDITEM'},
                {label: "Request", name: 'REQUEST'},
                {label: "Issue", name: 'ISSUE'}

            ];

            vm.properties = [];

            vm.lovs = null;
            vm.dataTypes = [
                'TEXT',
                'INTEGER',
                'DOUBLE',
                'DATE',
                'BOOLEAN',
                'LIST',
                'TIME',
                'TIMESTAMP',
                'CURRENCY',
                'IMAGE',
                'ATTACHMENT'
            ];

            vm.refTypes = ['ITEM', 'ITEMREVISION'];

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

            function addProperty() {
                var prop = {
                    id: null,
                    objectType: vm.selectedType.name,
                    name: "New Attribute",
                    newName: "New Attribute",
                    description: '',
                    dataType: null,
                    newDataType: null,
                    newRequired: false,
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
                prop.newRequired = prop.required;
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
                prop.required = prop.newRequired;
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
                    || prop.name == "" || prop.name == "New Attribute") {
                    $rootScope.showErrorMessage("Please enter Attribute Name");
                    valid = false;
                } else if (prop.description == null || prop.description == undefined
                    || prop.description == "") {
                    $rootScope.showErrorMessage("Please enter description");
                    valid = false;
                }
                else if (prop.dataType == null || prop.dataType == undefined
                    || prop.dataType == "") {
                    $rootScope.showErrorMessage("Please select Data Type");
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
                prop.newRequired = prop.required;
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
                            prop.newName = prop.name;
                            prop.newDescription = prop.description;
                            prop.newDataType = prop.dataType;
                            prop.newRequired = prop.required;
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