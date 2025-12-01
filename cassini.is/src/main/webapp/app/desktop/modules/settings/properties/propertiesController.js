define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('PropertiesController', PropertiesController);

        function PropertiesController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                      ObjectTypeAttributeService, LovService) {
            var vm = this;
            vm.lovs = [];

            vm.selectedType = null;
            vm.objectTypes = [
                {label: 'Material', name: 'MATERIAL'},
                {label: 'Machine', name: 'MACHINE'},
                {label: 'Manpower', name: 'MANPOWER'},
                {label: 'Project', name: 'PROJECT'},
                /* {label: 'WBS', name: 'WBS'},*/
                {label: 'Task', name: 'TASK'},
                {label: 'Store', name: 'STORE'},
                {label: 'Site', name: 'SITE'},
                /* {label: 'Boq Item', name: 'BOQITEM'},*/
                {label: 'Problem', name: 'PROBLEM'},
                {label: 'Issue', name: 'ISSUE'},
                {label: 'Issue Item', name: 'ISSUEITEM'},
                {label: 'Receive', name: 'RECEIVE'},
                {label: 'Receive Item', name: 'RECEIVEITEM'},
                {label: 'Loan', name: 'LOAN'},
                {label: 'Requisition', name: 'CUSTOM_REQUISITION'},
                {label: 'Indent', name: 'CUSTOM_INDENT'},
                {label: 'Purchase Order', name: 'CUSTOM_PURCHASEORDER'},
                {label: 'Road Challan', name: 'CUSTOM_ROADCHALAN'},
                {label: 'Road Challan Item', name: 'ROADCHALLANITEM'},
                {label: 'Scrap Request', name: 'SCRAPREQUEST'},
                {label: 'Supplier', name: 'SUPPLIER'},
                {label: 'Stock Return', name: 'STOCKRETURN'},
                {label: 'Contractor', name: 'CONTRACTOR'},
                {label: 'Work Order', name: 'WORKORDER'},
                {label: 'Work Order Item', name: 'WORKORDERITEM'}
            ];

            vm.properties = [];

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
                // 'OBJECT',
                'IMAGE',
                'ATTACHMENT',
                'OBJECT'
            ];

            vm.refTypes = ['PERSON', 'MATERIAL', 'MACHINE', 'MANPOWER', 'PROJECT', 'TASK', 'STORE', 'SITE', 'PROBLEM', 'ISSUE', 'RECEIVE', 'LOAN', 'CUSTOM_REQUISITION', 'CUSTOM_INDENT', 'CUSTOM_PURCHASEORDER', 'CUSTOM_ROADCHALAN', 'SCRAPREQUEST', 'SUPPLIER', 'STOCKRETURN', 'CONTRACTOR', 'WORKORDER'];

            vm.initReftypeDropdown = initReftypeDropdown;
            vm.initDropdown = initDropdown;
            vm.initDatatypeDropdown = initDatatypeDropdown;
            vm.initLovtypeDropdown = initLovtypeDropdown;
            vm.addProperty = addProperty;
            vm.deleteProperty = deleteProperty;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.promptDeleteProperty = promptDeleteProperty;
            vm.hideMask = hideMask;
            vm.loadProperties = loadProperties;
            vm.editProperty = editProperty;
            var typeMap = new Hashtable();

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

            vm.initReftypeDropdown = initReftypeDropdown;
            function initReftypeDropdown(index) {
                var prop = vm.properties[index];
                if (prop.refTypeMode) {
                    var b = '#dropdownMenuButton1' + index;
                    var m = '#dropdownMenu1' + index;

                    $(m).css({top: $(b).offset().top + 20, left: $(b).offset().left});
                    prop.dataTypeMode = false;
                }
            }

            function initDropdown() {
                var b = '#dropdownMenuButton';
                var m = '#dropdownMenu';

                var pos = $(b).offset();
                var width = $(b).outerWidth();
                var calc = (pos.left + width) - 160;

                $(m).css({top: pos.top + 20, left: calc});
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
                    name: 'New property',
                    newName: 'New property',
                    description: '',
                    dataType: null,
                    newDataType: null,
                    refType: null,
                    required: false,
                    editMode: true,
                    showBusy: false,
                    lov: null,
                    newRefType: null,
                    newLov: null,
                    lovTypeMode: false,
                    dataTypeMode: true
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
                        $rootScope.showSuccessMessage("Property deleted successfully");
                        vm.properties.remove(prop);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        loadProperties();
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
                var promise = null;
                if (prop.id == null) {
                    if (validate(prop)) {
                        promise = ObjectTypeAttributeService.createObjectTypeAttribute(vm.selectedType, prop);
                    }
                }
                else {
                    promise = ObjectTypeAttributeService.updateObjectTypeAttribute(vm.selectedType, prop);
                }
                prop.showBusy = true;
                promise.then(
                    function (data) {
                        prop.id = data.id;
                        $rootScope.showSuccessMessage("Property saved successfully");
                        prop.showBusy = false;
                        prop.editMode = false;
                        prop.lovTypeMode = false;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        prop.showBusy = false;
                        loadProperties();
                    }
                );

            }

            function validate(prop) {
                var valid = true;
                if (prop.name == null || prop.name == undefined
                    || prop.name == "" || prop.name == 'New property') {
                    $rootScope.showErrorMessage('Property Name cannot be empty');
                    valid = false;
                } else if (typeMap.get(prop.name) != null) {
                    $rootScope.showErrorMessage("{0} Name already exists".format(prop.name));
                    valid = false;
                } else if (prop.dataType == null || prop.dataType == undefined
                    || prop.dataType == "") {
                    $rootScope.showErrorMessage('Property Data Type cannot be empty');
                    valid = false;
                }

                return valid;
            }

            /* function loadTypeAttributes() {
             ObjectTypeAttributeService.getObjectTypeAttributesByType(vm.selectedType).then(
             function (data) {
             angular.forEach(data, function (typeAttribute) {
             typeMap.put(typeAttribute.name, typeAttribute);
             })
             }
             )
             }
             */
            function cancelChanges(prop) {
                prop.newName = prop.name;
                prop.newDescription = prop.description;
                prop.newDataType = prop.dataType;
                if (prop.id == null) {
                    vm.properties.remove(prop);

                }
                loadProperties();
            }

            function hideMask(index) {
                $('#propertyMask' + index).hide();
            }

            function loadProperties() {
                typeMap.clear();
                ObjectTypeAttributeService.getObjectTypeAttributesByType(vm.selectedType.name).then(
                    function (data) {
                        vm.properties = data;
                        angular.forEach(vm.properties, function (prop) {
                            typeMap.put(prop.name, prop);
                            prop.newName = prop.name;
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