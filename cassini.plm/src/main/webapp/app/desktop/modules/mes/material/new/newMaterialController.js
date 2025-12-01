define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/materialService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/qualityTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewMaterialController', NewMaterialController);

        function NewMaterialController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CommonService,
                                       AutonumberService, MaterialService, QualityTypeService, ClassificationService, MESObjectTypeService, MeasurementService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var materialNumberValidation = parsed.html($translate.instant("MATERIAL_NUMBER_VALIDATION")).html();
            var materialTypeValidation = parsed.html($translate.instant("MATERIAL_TYPE_VALIDATION")).html();
            var materialNameValidation = parsed.html($translate.instant("MATERIAL_NAME_VALIDATION")).html();
            var measurementValidation = parsed.html($translate.instant("QOM_VALIDATION")).html();
            var measurementUnitValidation = parsed.html($translate.instant("UOM_VALIDATION")).html();
            var materialCreatedMsg = parsed.html($translate.instant("MATERIAL_CREATED_MSG")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();

            vm.onSelectType = onSelectType;
            vm.newMaterial = {
                id: null,
                type: null,
                number: null,
                description: null,
                qomObject: null,
                uomObject: null,
                qom: null,
                uom: null,
                imageFile: null
            };


            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newMaterial.type = objectType;
                    vm.type = objectType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newMaterial.type != null && vm.newMaterial.type.autoNumberSource != null) {
                    var source = vm.newMaterial.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newMaterial.number = data;
                            loadAttributeDefs();
                            loadMeasurements();
                        }
                    )
                }
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newMaterial.type = vm.type;
                    vm.newMaterial.qom = vm.newMaterial.qomObject.id;
                    vm.newMaterial.uom = vm.newMaterial.uomObject.id;
                    vm.imageAttributes = [];
                    vm.attachmentAttributes = [];
                    vm.images = new Hashtable();
                    vm.attachments = new Hashtable();
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.attachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.attachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    vm.newMaterial.mesObjectAttributes = vm.attributes;
                    MaterialService.createMaterial(vm.newMaterial).then(
                        function (data) {
                            document.getElementById("imageId").value = "";
                            if (vm.newMaterial.imageFile != null) {
                                MaterialService.uploadImage(data.id, vm.newMaterial.imageFile).then(
                                    function (data) {
                                    }
                                );
                            }
                            vm.newMaterial = data;
                            saveAttributes().then(
                                function (attributes) {
                                    $scope.callback(vm.newMaterial);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(materialCreatedMsg);
                                    vm.newMaterial = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        description: null,
                                        qom: null,
                                        uom: null
                                    };
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newMaterial.type == null || vm.newMaterial.type == undefined ||
                    vm.newMaterial.type == "") {
                    $rootScope.showErrorMessage(materialTypeValidation);
                    valid = false;
                }
                else if (vm.newMaterial.name == null || vm.newMaterial.name == undefined ||
                    vm.newMaterial.name == "") {
                    $rootScope.showErrorMessage(materialNameValidation);
                    valid = false;
                }

                else if (vm.newMaterial.number == null || vm.newMaterial.number == undefined ||
                    vm.newMaterial.number == "") {
                    $rootScope.showErrorMessage(materialNumberValidation);
                    valid = false;
                }
                else if (vm.newMaterial.qomObject == null || vm.newMaterial.qomObject == undefined ||
                    vm.newMaterial.qomObject == "") {
                    $rootScope.showErrorMessage(measurementValidation);
                    valid = false;
                }
                else if (vm.newMaterial.uomObject == null || vm.newMaterial.uomObject == undefined) {
                    $rootScope.showErrorMessage(measurementUnitValidation);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }


            function validateAttributes() {
                var valid = true;
                angular.forEach(vm.attributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("MESOBJECTTYPE", vm.newMaterial.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    );
                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("MESOBJECTTYPE", vm.newMaterial.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.type.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
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
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
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
                )
            }

            function loadMeasurements() {
                MeasurementService.getAllMeasurements().then(
                    function (data) {
                        vm.measurements = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.material.new', create);
                //}
            })();
        }
    }
)
;