define(['app/desktop/modules/stores/store.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {
        module.controller('ItemAttributesController', ItemAttributesController);

        function ItemAttributesController($scope, $rootScope, $stateParams, AttributeAttachmentService, $q, $application, $timeout, $state, $cookies, ObjectTypeAttributeService, ObjectAttributeService) {
            var vm = this;

            var objType = $scope.data.objType;
            var obj = $scope.data.obje;
            vm.requiredAttributes = [];
            vm.attributes = [];

            function updateItemAttributes() {
                attributesValidate().then(
                    function (success) {
                        intializationForAttributesSave().then(
                            function (success) {
                                obj.itemDTO.editAttribute = true;
                                $rootScope.hideSidePanel();
                                $rootScope.objId = obj.id;
                                if (vm.attributes.length != 0) {
                                    $rootScope.showSuccessMessage("Item attributes created successfully");
                                } else {
                                    $rootScope.showErrorMessage("No attribute added");
                                }
                                if (objType == 'RECEIVEITEM') {
                                    $rootScope.$broadcast('app.stock.receiveItems');
                                }
                                else if (objType == 'ISSUEITEM') {
                                    $rootScope.$broadcast('app.stock.issueItems');
                                }
                                else if (objType == 'ROADCHALLANITEM') {
                                    $rootScope.$broadcast('app.roadChallan.items');
                                }
                                else if (objType == 'WORKORDERITEM') {
                                    $rootScope.$broadcast('app.workOrder.items');
                                }
                                $scope.callback();
                            }, function (error) {

                            }
                        )
                    }, function (error) {

                    });
            }

            function attributesValidate() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                vm.objectAttributes = [];
                vm.validObjectAttributes = [];
                vm.validattributes = [];
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validattributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.itemAttributes != null && vm.itemAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.itemAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validObjectAttributes.push(attribute);
                    }
                });
                if (vm.requiredAttributes.length == vm.validattributes.length && vm.objectAttributes.length == vm.validObjectAttributes.length) {
                    defered.resolve();
                } else {
                    defered.reject();
                }
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.itemAttributes.length > 0) {
                    angular.forEach(vm.itemAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(obj.id, vm.itemAttributes).then(
                        function (data) {
                            var secCount = 0;
                            if (vm.propertyImageAttributes.length > 0) {
                                angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                    ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, vm.propertyImages.get(propImgAtt.id.attributeDef)).then(
                                        function (data) {
                                        });
                                    secCount++;
                                    if (secCount == vm.propertyImageAttributes.length) {
                                        defered.resolve();
                                    }
                                });
                            } else {
                                defered.resolve();
                            }
                        },
                        function (error) {
                            defered.reject();
                        });
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function loadObjectAttributeDefs() {
                vm.itemAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType(objType).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: obj.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.itemAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.itemAttributes.length == 0) {
                    defered.resolve(true);
                }
                else {
                    angular.forEach(vm.itemAttributes, function (attribute) {
                        attribute.id.objectId = obj.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = vm.propertyAttachmentIds;
                                    attrCount++;
                                    if (attrCount == vm.itemAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.itemAttributes = [];
                                                loadObjectAttributeDefs();
                                                defered.resolve(true);
                                            }, function (error) {
                                                defered.reject(true);
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.itemAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.itemAttributes = [];
                                        loadObjectAttributeDefs();
                                        defered.resolve(true);
                                    }, function (error) {
                                        defered.reject(true);
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
            }

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, objType, attachmentFile).then(
                        function (data) {
                            vm.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        }
                    )
                });
                return defered.promise;
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadObjectAttributeDefs();
                    $scope.$on('app.stores.item.attributes', function addAttributes() {
                        updateItemAttributes();
                    });
                }
            })();
        }
    });