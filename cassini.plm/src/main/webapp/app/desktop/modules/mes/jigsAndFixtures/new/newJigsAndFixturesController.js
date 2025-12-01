define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/materialService',
        'app/shared/services/core/jigsFixService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/qualityTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/directives/mes-asset/mesAssetDirectiveController',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewJigsAndFixturesController', NewJigsAndFixturesController);

        function NewJigsAndFixturesController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CommonService, ClassificationService,
                                              AutonumberService, MaterialService, LoginService, QualityTypeService, ObjectTypeAttributeService, MESObjectTypeService, JigsFixtureService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var jigsCreatedMsg = parsed.html($translate.instant("JIG_CREATED_MSG")).html();
            var fixtureCreatedMsg = parsed.html($translate.instant("FIXTURE_CREATED_MSG")).html();
            var nameValidateMsg = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var typeValidateMsg = parsed.html($translate.instant("TYPE_CANNOT_BE_EMPTY")).html();
            var numberValidateMsg = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var objType = $scope.data.selectedObject;
            $scope.actionType = $scope.data.actionType;
            vm.onSelectType = onSelectType;
            vm.newJigAndFix = {
                id: null,
                type: null,
                number: null,
                description: null,
                requiresMaintenance: true,
                active: true,
                jigType: objType,
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

            }
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


            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newJigAndFix.type = objectType;
                    vm.type = objectType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newJigAndFix.type != null && vm.newJigAndFix.type.autoNumberSource != null) {
                    var source = vm.newJigAndFix.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newJigAndFix.number = data;
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newJigAndFix.type = vm.type;
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
                    vm.newAsset.resourceType = vm.newJigAndFix.type.objectType;
                    /*  vm.newAsset.name = vm.newJigAndFix.name;
                     vm.newAsset.description = vm.newJigAndFix.description;*/
                    vm.newJigAndFix.asset = vm.newAsset;
                    vm.newJigAndFix.mesObjectAttributes = vm.attributes;
                    JigsFixtureService.createJigsFix(vm.newJigAndFix).then(
                        function (data) {
                            var jigFixture = data;
                            document.getElementById("imageId").value = "";
                            if (vm.newJigAndFix.imageFile != null) {
                                JigsFixtureService.uploadImage(data.id, vm.newJigAndFix.imageFile).then(
                                    function (data) {
                                    }
                                );
                            }
                            vm.newJigAndFix = data.jigsFixture;
                            saveAttributes().then(
                                function (attributes) {
                                    $scope.callback(jigFixture);
                                    $rootScope.hideBusyIndicator();
                                    if (objType == 'JIG') {
                                        $rootScope.showSuccessMessage(jigsCreatedMsg);
                                    } else {
                                        $rootScope.showSuccessMessage(fixtureCreatedMsg);
                                    }
                                    vm.type = null;
                                    vm.newJigAndFix = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        description: null,
                                        requiresMaintenance: true,
                                        active: true,
                                        jigType: $scope.data.selectedObject,
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
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var assetTypeValidation = parsed.html($translate.instant("ASSET_TYPE_VALIDATION")).html();
            var assetNumberValidation = parsed.html($translate.instant("ASSET_NUMBER_VALIDATION")).html();
            var metersValidation = parsed.html($translate.instant("ASSET_METER_VALIDATION")).html();
            var assetNameValidation = parsed.html($translate.instant("ASSET_NAME_VALIDATION")).html();


            function validate() {
                var valid = true;
                if (vm.newJigAndFix.type == null || vm.newJigAndFix.type == undefined ||
                    vm.newJigAndFix.type == "") {
                    $rootScope.showErrorMessage(typeValidateMsg);
                    valid = false;
                }
                else if (vm.newJigAndFix.number == null || vm.newJigAndFix.number == undefined ||
                    vm.newJigAndFix.number == "") {
                    $rootScope.showErrorMessage(numberValidateMsg);
                    valid = false;
                }
                else if (vm.newJigAndFix.name == null || vm.newJigAndFix.name == undefined ||
                    vm.newJigAndFix.name == "") {
                    $rootScope.showErrorMessage(nameValidateMsg);
                    valid = false;
                } else if (vm.newJigAndFix.requiresMaintenance && (vm.newAsset.type == null || vm.newAsset.type == "" || vm.newAsset.type == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetTypeValidation);
                } else if (vm.newJigAndFix.requiresMaintenance && (vm.newAsset.number == null || vm.newAsset.number == "" || vm.newAsset.number == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNumberValidation);
                }
                else if (vm.newJigAndFix.requiresMaintenance && (vm.newAsset.name == null || vm.newAsset.name == "" || vm.newAsset.name == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNameValidation);
                }

                else if (vm.newJigAndFix.requiresMaintenance && vm.newAsset.metered && vm.newAsset.meters.length <= 0) {
                    $rootScope.showWarningMessage(metersValidation);
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
                            ClassificationService.updateAttributeImageValue("MESOBJECTTYPE", vm.newJigAndFix.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
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
                            ClassificationService.updateAttributeAttachmentValues("MESOBJECTTYPE", vm.newJigAndFix.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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

            $scope.selectJigFixtureType = selectJigFixtureType;
            function selectJigFixtureType(value) {
                objType = value;
                vm.newJigAndFix = {
                    id: null,
                    type: null,
                    number: null,
                    description: null,
                    requiresMaintenance: true,
                    active: true,
                    jigType: objType,
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

                }
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
                vm.type = null;
                $scope.$evalAsync();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.jigsAndFixtures.new', create);
                //}
            })();
        }
    }
)
;
