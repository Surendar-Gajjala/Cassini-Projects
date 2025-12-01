define(
    [
        'app/desktop/modules/mro/mro.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/meterService',
        'app/shared/services/core/sparePartsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService'

    ],
    function (module) {

        module.controller('NewMeterController', NewMeterController);

        function NewMeterController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, MeterService, LoginService,
                                    ObjectTypeAttributeService, AutonumberService, SparePartService, MESObjectTypeService, AttributeAttachmentService, MeasurementService) {

            var vm = this;

            var parsed = angular.element("<div></div>");


            vm.selectedMeterType = null;
            vm.attributes = [];
            vm.validattributes = [];

            vm.meterReadingTypes = ["ABSOLUTE", "CHANGE"];
            vm.types = ["CONTINUOUS", "GUAGE"];

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var measurementValidation = parsed.html($translate.instant("PLEASE_SELECT_QOM")).html();
            var measurementUnitValidation = parsed.html($translate.instant("PLEASE_SELECT_UOM")).html();
            var MeterManagerValidation = parsed.html($translate.instant("SPAREPART_MANAGER_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();
            $scope.selectQom = parsed.html($translate.instant("SELECT_QOM")).html();
            $scope.selectUom = parsed.html($translate.instant("SELECT_UOM")).html();

            vm.newMeter = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null,
                meterType: "CONTINUOUS",
                meterReadingType: "ABSOLUTE",
                qom: null,
                uom: null,
                qomObject: null,
                uomObject: null
            };

            vm.onSelectType = onSelectType;
            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newMeter.type = objectType;
                    vm.type = objectType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.type != null && vm.type.autoNumberSource != null) {
                    var source = vm.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newMeter.number = data;
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                SparePartService.getObjectAttributesWithHierarchy(vm.type.id).then(
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

            function createNewMeter() {
                create().then(function () {
                    vm.newMeter = {
                        id: null,
                        type: null,
                        number: null,
                        name: null,
                        description: null,
                        meterType: "CONTINUOUS",
                        meterReadingType: "ABSOLUTE",
                        qom: null,
                        uom: null,
                        qomObject: null,
                        uomObject: null
                    };
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })

            }


            function validate() {
                var valid = true;
                if (vm.newMeter.type == null || vm.newMeter.type == undefined ||
                    vm.newMeter.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newMeter.number == null || vm.newMeter.number == undefined ||
                    vm.newMeter.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newMeter.name == null || vm.newMeter.name == undefined ||
                    vm.newMeter.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }
                else if (vm.newMeter.name == null || vm.newMeter.name == undefined ||
                    vm.newMeter.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }
                else if (vm.newMeter.qomObject == null || vm.newMeter.qomObject == undefined ||
                    vm.newMeter.qomObject == "") {
                    $rootScope.showWarningMessage(measurementValidation);
                    valid = false;
                }
                else if (vm.newMeter.uomObject == null || vm.newMeter.uomObject == undefined ||
                    vm.newMeter.uomObject == "") {
                    $rootScope.showWarningMessage(measurementUnitValidation);
                    valid = false;
                }

                return valid;
            }

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

            var insertedSuccefully = parsed.html($translate.instant("METER_ADDED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate() && validateRequiredAttributes()) {
                    vm.newMeter.qom = vm.newMeter.qomObject.id;
                    vm.newMeter.uom = vm.newMeter.uomObject.id;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    MeterService.createMeter(vm.newMeter).then(
                        function (data) {
                            vm.newMeter = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.newMeter = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        name: null,
                                        description: null,
                                        meterType: "CONTINUOUS",
                                        meterReadingType: "ABSOLUTE",
                                        qom: null,
                                        uom: null,
                                        qomObject: null,
                                        uomObject: null
                                    };
                                    vm.type = null;
                                    vm.selectedMeterType = null;
                                    vm.attributes = [];
                                    $scope.callback();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(insertedSuccefully);
                                }
                            )
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                return dfd.promise;
            }


            function addMeterPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MROBJECTTYPE', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })
                return propertyAttachmentIds;
            }


            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newMeter.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addMeterPropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        MeterService.saveMeterAttributes(vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        MeterService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
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
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectQOM = selectQOM;
            function selectQOM(value) {
                vm.newMeter.uomObject = null;
            }

            (function () {
                $rootScope.hideBusyIndicator();
                loadPersons();
                loadMeasurements();
                $rootScope.$on('app.meter.new', createNewMeter);
                //}
            })();
        }
    }
);