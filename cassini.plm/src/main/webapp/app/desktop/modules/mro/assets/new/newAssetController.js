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
        'app/desktop/modules/directives/mesResourceTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/assetService',
        'app/shared/services/core/sparePartsService',
        'app/shared/services/core/meterService'

    ],
    function (module) {

        module.controller('NewAssetController', NewAssetController);

        function NewAssetController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, AssetService, LoginService,
                                    ObjectTypeAttributeService, AutonumberService, SparePartService, MESObjectTypeService, AttributeAttachmentService, MeterService) {

            var vm = this;
            var parsed = angular.element("<div></div>");

            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var resourceValidation = parsed.html($translate.instant("ASSET_RESOURCE_VALIDATION")).html();
            var metersValidation = parsed.html($translate.instant("ASSET_METER_VALIDATION")).html();
            $scope.selectResource = parsed.html($translate.instant("SELECT_RESOURCE")).html();
            $scope.selectMeter = parsed.html($translate.instant("SELECT_METER")).html();


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

            vm.onSelectType = onSelectType;
            vm.resources = [];
            function onSelectType(object) {
                vm.resources = [];
                vm.type = null;
                vm.newAsset.name = null;
                /*vm.newAsset = {
                 id: null,
                 name: null,
                 description: null,
                 metered: false,
                 resource: null,
                 resourceType: "",
                 meters: []
                 };*/
                vm.resourceName = null;
                if (object != null && object != undefined) {
                    vm.newAsset.resourceType = object.objectType;
                    if (object.objectType == "PLANTTYPE") {

                        AssetService.getResourcesByType(object.objectType, object.id).then(
                            function (data) {
                                vm.resources = data.plants;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )

                    }
                    else if (object.objectType == "WORKCENTERTYPE") {
                        AssetService.getResourcesByType(object.objectType, object.id).then(
                            function (data) {
                                vm.resources = data.workCenters;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                    else if (object.objectType == "MACHINETYPE") {
                        AssetService.getResourcesByType(object.objectType, object.id).then(
                            function (data) {
                                vm.resources = data.machines;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                    else if (object.objectType == "EQUIPMENTTYPE") {
                        AssetService.getResourcesByType(object.objectType, object.id).then(
                            function (data) {
                                vm.resources = data.equipments;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                    else if (object.objectType == "INSTRUMENTTYPE") {
                        AssetService.getResourcesByType(object.objectType, object.id).then(
                            function (data) {
                                vm.resources = data.instruments;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                    else if (object.objectType == "JIGFIXTURETYPE") {
                        AssetService.getResourcesByType(object.objectType, object.id).then(
                            function (data) {
                                vm.resources = data.jigsFixtures;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else if (object.objectType == "TOOLTYPE") {
                        AssetService.getResourcesByType(object.objectType, object.id).then(
                            function (data) {
                                vm.resources = data.tools;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    vm.type = object;
                    //autoNumber();
                }
            }

            vm.selectResource = selectResource;
            function selectResource(value) {
                vm.newAsset.resource = value.id;
                vm.newAsset.name = value.name;
            }

            vm.onSelectAssetType = onSelectAssetType;
            function onSelectAssetType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newAsset.type = objectType;
                    vm.assetType = objectType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.assetType != null && vm.assetType.autoNumberSource != null) {
                    var source = vm.assetType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newAsset.number = data;
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
                SparePartService.getObjectAttributesWithHierarchy(vm.assetType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.assetType.id,
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

            vm.meterIds = [];
            vm.meters = [];
            vm.getMeters = getMeters;
            function getMeters() {
                vm.meters = [];
                vm.meterIds = [];
                if (vm.newAsset.metered == true) {
                    MeterService.getMeters().then(
                        function (data) {
                            vm.meters = data;

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else {
                    vm.newAsset.meters = [];
                }

            }


            function createNewAsset() {
                create().then(function () {
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
                    vm.resourceName = null;
                    vm.type = null;
                    vm.assetType = null;
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })

            }

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();

            function validate() {
                var valid = true;
                if (vm.type == null || vm.type == undefined || vm.type.name == "") {
                    valid = false;
                    $rootScope.showWarningMessage(itemTypeValidation);
                }
                else if (vm.resourceName == null || vm.resourceName == undefined ||
                    vm.newAsset.name == "") {
                    $rootScope.showWarningMessage(resourceValidation);
                    valid = false;
                }
                else if (vm.newAsset.type == null || vm.newAsset.type == undefined ||
                    vm.newAsset.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newAsset.name == null || vm.newAsset.name == undefined ||
                    vm.newAsset.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }

                else if (vm.newAsset.number == null || vm.newAsset.number == undefined ||
                    vm.newAsset.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newAsset.metered == true) {
                    if (vm.meterIds.length <= 0) {
                        $rootScope.showWarningMessage(metersValidation);
                        valid = false;
                    }

                }
                else if (vm.attributes.length > 0 && !validateAttributes()) {
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

            var insertedSuccefully = parsed.html($translate.instant("ASSET_ADDED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate()) {
                    vm.newAsset.meters = vm.meterIds;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    AssetService.createAsset(vm.newAsset).then(
                        function (data) {
                            vm.asset = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.newAsset = {
                                        id: null,
                                        name: null,
                                        number: null,
                                        description: null,
                                        metered: false,
                                        resource: null,
                                        resourceType: "",
                                        type: null,
                                        meters: []
                                    };
                                    vm.resourceName = null;
                                    vm.type = null;
                                    vm.assetType = null;
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


            function addSparePartPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MROOBJECTTYPE', attachmentFile).then(
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
                        attribute.id.objectId = vm.asset.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addSparePartPropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        AssetService.saveAssetAttributes(vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        SparePartService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }, function () {
                                                defered.reject();
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


            (function () {
                $rootScope.hideBusyIndicator();
                autoNumber();
                $rootScope.$on('app.asset.new', createNewAsset);
                //}
            })();
        }
    }
)
;