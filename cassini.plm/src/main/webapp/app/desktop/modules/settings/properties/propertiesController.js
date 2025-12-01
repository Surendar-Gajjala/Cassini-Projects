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

            var item = parsed.html($translate.instant("ITEMS_ALL_TITLE")).html();
            var itemRevision = parsed.html($translate.instant("ITEM_REVISION")).html();
            var workflow = parsed.html($translate.instant("WORKFLOW")).html();
            var manufacturer = parsed.html($translate.instant("MANUFACTURER")).html();
            var manufacturerPart = parsed.html($translate.instant("MANUFACTURER_PART")).html();
            var bomItem = parsed.html($translate.instant("BOM_ITEM")).html();
            var change = parsed.html($translate.instant("CHANGE")).html();
            var person = parsed.html($translate.instant("PERSON")).html();
            var project = parsed.html($translate.instant("NO_OF_PROJECTS")).html();
            var activity = parsed.html($translate.instant("ACTIVITY")).html();
            var task = parsed.html($translate.instant("TASK")).html();
            var glossary = parsed.html($translate.instant("TERMINOLOGY")).html();
            var specification = parsed.html($translate.instant("SPECIFICATION")).html();
            var requirement = parsed.html($translate.instant("REQUIREMENT")).html();
            var fileTitle = parsed.html($translate.instant("FILE")).html();
            var glossaryEntry = parsed.html($translate.instant("TERMINOLOGY_ENTRY")).html();
            var qualityTitle = parsed.html($translate.instant("QUALITY")).html();
            $scope.deleteAttributeTitle = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_TITLE")).html();
            $scope.saveAttributeTitle = parsed.html($translate.instant("SAVE_ATTRIBUTE")).html();
            $scope.cancelChanges = parsed.html($translate.instant("CANCEL_CHANGES")).html();


            vm.objectTypes = [
                {label: item, name: 'ITEM'},
                {label: itemRevision, name: 'ITEMREVISION'},
                {label: change, name: 'CHANGE'},
                {label: qualityTitle, name: "QUALITY"},
                {label: bomItem, name: 'BOMITEM'},
                {label: manufacturer, name: 'MANUFACTURER'},
                {label: manufacturerPart, name: 'MANUFACTURERPART'},
                {label: project, name: 'PROJECT'},
                {label: activity, name: 'PROJECTACTIVITY'},
                {label: task, name: 'PROJECTTASK'},
                {label: requirement, name: 'REQUIREMENT'},
                {label: fileTitle, name: 'FILE'}

            ];

            vm.selectedType = vm.objectTypes[0];

            vm.properties = [];

            vm.lovs = null;
            vm.dataTypes = [
                'TEXT',
                'LONGTEXT',
                'RICHTEXT',
                'INTEGER',
                'DOUBLE',
                'DATE',
                'BOOLEAN',
                'LIST',
                'TIME',
                'TIMESTAMP',
                'CURRENCY',
                'OBJECT',
                'IMAGE',
                'ATTACHMENT',
                'HYPERLINK'
            ];

            vm.refTypes = ['ITEM', 'ITEMREVISION', 'CHANGE', 'WORKFLOW', 'MANUFACTURER', 'MANUFACTURERPART', 'PERSON', 'PROJECT', 'REQUIREMENT'];

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
                var height = $(".view-content").outerHeight();
                $('#dropdownMenu').height(height - 100);
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

            vm.initLovtypeDropdowns = initLovtypeDropdowns;
            function initLovtypeDropdowns(index) {
                var prop = vm.properties[index];
                if (prop.lovTypeMode) {
                    var b1 = '#dropdownMenuButton3' + index;
                    var m1 = '#dropdownMenu3' + index;

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
                    visible: true,
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

            var propertyDeletedMessage = parsed.html($translate.instant("DELETE_ATTRIBUTE_MESSAGE")).html();

            function deleteProperty(prop) {
                ObjectTypeAttributeService.deleteObjectTypeAttribute(vm.selectedType, prop.id).then(
                    function (data) {
                        $rootScope.showSuccessMessage(propertyDeletedMessage);
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

            var propertySavedMessage = parsed.html($translate.instant("ATTRIBUTE_SAVED_MESSAGE")).html();

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
                            $rootScope.showSuccessMessage(propertySavedMessage);
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

            var propertyNameValidation = $translate.instant("ATTRIBUTE_NAME_VALIDATION");
            var propertyDescriptionValidation = $translate.instant("ATTRIBUTE_DESCRIPTION_VALIDATION");
            var propertyDataTypeValidation = $translate.instant("ATTRIBUTE_DATA_TYPE_VALIDATION");
            var systemAttributeValidation = $translate.instant("ATTRIBUTE_NEW_NAME_VALIDATION");
            var listValueValidation = parsed.html($translate.instant("LIST_VALUE_VALIDATION")).html();

            function validate(prop) {
                var valid = true;
                if (prop.name == null || prop.name == undefined
                    || prop.name == "" || prop.name == newAttribute) {
                    $rootScope.showErrorMessage(propertyNameValidation);
                    valid = false;
                }/* else if (prop.description == null || prop.description == undefined
                 || prop.description == "") {
                 $rootScope.showErrorMessage(propertyDescriptionValidation);
                 valid = false;
                 }*/
                else if (prop.dataType == null || prop.dataType == undefined
                    || prop.dataType == "") {
                    $rootScope.showErrorMessage(propertyDataTypeValidation);
                    valid = false;
                } else if (prop.dataType == 'LIST' &&
                    (prop.newLov == null || prop.newLov == "" || prop.newLov == undefined)) {
                    $rootScope.showErrorMessage(listValueValidation);
                    valid = false;
                } else {
                    angular.forEach(vm.properties, function (itemProp) {
                        if (itemProp.name == prop.newName && itemProp.id != prop.id) {
                            $rootScope.showErrorMessage(systemAttributeValidation);
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
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function promptDeleteProperty(index) {
                var width = $('#property' + index).width();
                var height = $('#property' + index).height();
                $('#property' + index).css({position: 'relative'});

                $('#propertyMask' + index).width(width + 2);
                $('#propertyMask' + index).height(height + 2);
                $('#propertyMask' + index).css({display: 'table'});
            }

            function loadLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.lovs = data;
                        loadProperties();
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
)
;