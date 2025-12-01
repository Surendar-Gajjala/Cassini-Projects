define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/toolService',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/directives/mes-asset/mesAssetDirectiveController',
    ],
    function (module) {

        module.controller('NewToolController', NewToolController);

        function NewToolController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                   LoginService, ObjectTypeAttributeService, AutonumberService, MESObjectTypeService, ToolService, AttributeAttachmentService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.autoNumber = autoNumber;

            vm.newTool = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null,
                requiresMaintenance: true,
                imageFile: null,
                manufacturerData: {
                    object: null,
                    mfrName: null,
                    mfrDescription: null,
                    mfrModelNumber: null,
                    mfrPartNumber: null,
                    mfrSerialNumber: null,
                    mfrLotNumber: null,
                    mfrDate: null
                }
            };
            vm.newAsset = {
                id: null,
                type: null,
                name: null,
                number: null,
                description: null,
                metered: false,
                resource: null,
                resourceType: "",
                meters: []
            };

            vm.onSelectType = onSelectType;
            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.selectedToolType = objectType;
                    vm.newTool.type = objectType;
                    autoNumber();
                }
            }

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            /*
             * Validate Tool Object
             * */
            var assetTypeValidation = parsed.html($translate.instant("ASSET_TYPE_VALIDATION")).html();
            var assetNumberValidation = parsed.html($translate.instant("ASSET_NUMBER_VALIDATION")).html();
            var metersValidation = parsed.html($translate.instant("ASSET_METER_VALIDATION")).html();
            var assetNameValidation = parsed.html($translate.instant("ASSET_NAME_VALIDATION")).html();

            function validate() {
                var valid = true;
                if (vm.newTool.type == null || vm.newTool.type == undefined ||
                    vm.newTool.type == "") {
                    $rootScope.showErrorMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newTool.number == null || vm.newTool.number == undefined ||
                    vm.newTool.number == "") {
                    $rootScope.showErrorMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newTool.name == null || vm.newTool.name == undefined ||
                    vm.newTool.name == "") {
                    $rootScope.showErrorMessage(itemNameValidation);
                    valid = false;
                } else if (vm.newTool.requiresMaintenance && (vm.newAsset.type == null || vm.newAsset.type == "" || vm.newAsset.type == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetTypeValidation);
                } else if (vm.newTool.requiresMaintenance && (vm.newAsset.number == null || vm.newAsset.number == "" || vm.newAsset.number == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNumberValidation);
                }
                else if (vm.newTool.requiresMaintenance && (vm.newAsset.name == null || vm.newAsset.name == "" || vm.newAsset.name == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNameValidation);
                }
                else if (vm.newTool.requiresMaintenance && vm.newAsset.metered && vm.newAsset.meters.length <= 0) {
                    $rootScope.showWarningMessage(metersValidation);
                    valid = false;
                }

                else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            /*
             * Validate Attributes
             * */
            function validateRequiredAttributes() {
                var valid = true;
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (valid) {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            } else {
                                valid = false;
                                $rootScope.showWarningMessage("Please enter" + " " + attribute.attributeDef.name);
                            }
                        }
                    })
                }
                return valid;
            }

            /*
             * Create Tool
             *
             * */
            var insertedSuccefully = parsed.html($translate.instant("TOOL_CREATED")).html();

            function create() {
                var dfd = $q.defer();
                $rootScope.showBusyIndicator();
                vm.validattributes = [];
                if (validate() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newAsset.resourceType = vm.newTool.type.objectType;
                    /* vm.newAsset.name = vm.newTool.name;
                     vm.newAsset.description = vm.newTool.description;*/
                    vm.newTool.asset = vm.newAsset;
                    ToolService.createTool(vm.newTool).then(
                        function (data) {
                            if (vm.newTool.imageFile != null) {
                                ToolService.uploadImage(data.id, vm.newTool.imageFile).then(
                                    function (data) {
                                    }
                                );
                            }
                            vm.newTool = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.attributes = [];
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(insertedSuccefully);
                                    $rootScope.hideSidePanel();
                                    $scope.callback(vm.newTool);
                                }
                            )
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    $rootScope.hideBusyIndicator();
                }
                return dfd.promise;
            }

            function addToolPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TOOLTYPE', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            /*
             * Save Tool Attributes
             * */
            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newTool.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addToolPropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        ToolService.saveToolAttributes(vm.newTool.id, vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        ToolService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {

                                                defered.resolve();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }

                            }, function () {
                                defered.reject();
                            })
                    }, 2000)
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function autoNumber() {
                if (vm.selectedToolType != null) {
                    var source = vm.selectedToolType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newTool.number = data;
                            loadToolTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadToolTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.newTool.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newTool.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                integerValue: null,
                                stringValue: null,
                                doubleValue: null,
                                imageValue: null,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss")
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }
                            vm.attributes.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            (function () {
                $rootScope.hideBusyIndicator();
                $rootScope.$on('app.tool.new', create);
            })();
        }
    }
);