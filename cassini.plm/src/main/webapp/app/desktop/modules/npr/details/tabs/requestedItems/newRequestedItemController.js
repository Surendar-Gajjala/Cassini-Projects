define(
    [
        'app/desktop/modules/npr/npr.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/shared/services/core/nprService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/qualityTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewRequestedItemController', NewRequestedItemController);

        function NewRequestedItemController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                            NprService, ItemTypeService, QualityTypeService, ClassificationService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var Each = parsed.html($translate.instant("EACH")).html();
            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var unitsValidation = parsed.html($translate.instant("UNITS_VALIDATION")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");

            var nprId = $scope.data.selectedNpr;

            vm.newNprItem = {
                id: null,
                itemType: null,
                itemName: null,
                description: null,
                makeOrBuy: "BUY",
                itemAttributes: [],
                units: Each,
                isTemporary: true,
                npr: nprId,
                notes: null,
                latestRevision: null
            };

            vm.onSelectType = onSelectType;

            function onSelectType(itemType) {
                vm.newNprItem.itemType = itemType;
                loadAttributeDefs();
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                ItemTypeService.getAttributesWithHierarchy(vm.newNprItem.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newNprItem.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: null,
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                doubleValue: null,
                                measurementUnit: null,
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
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }

                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }

                            vm.attributes.push(att);
                        });
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validate() {
                var valid = true;
                if (vm.newNprItem.itemType == null || vm.newNprItem.itemType == "" || vm.newNprItem.itemType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(itemTypeValidation);
                } else if (vm.newNprItem.itemName == null || vm.newNprItem.itemName == "" || vm.newNprItem.itemName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(itemNameValidation);
                } else if (vm.newNprItem.units == null || vm.newNprItem.units == "" || vm.newNprItem.units == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(unitsValidation)
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if ((vm.newItemAttributes.length > 0) && !validateItemAttributes()) {
                    valid = false;
                } else if ((vm.newItemRevisionAttributes.length > 0) && !validateItemRevisionAttributes()) {
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

            function validateItemAttributes() {
                var valid = true;
                angular.forEach(vm.newItemAttributes, function (attribute) {
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

            function validateItemRevisionAttributes() {
                var valid = true;
                angular.forEach(vm.newItemRevisionAttributes, function (attribute) {
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


            function create() {
                if (validate()) {
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
                    vm.itemImageAttributes = [];
                    vm.itemAttachmentAttributes = [];
                    vm.itemImages = new Hashtable();
                    vm.itemAttachments = new Hashtable();

                    angular.forEach(vm.newItemAttributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.itemImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.itemImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.itemAttachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.itemAttachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });

                    vm.itemRevisionImageAttributes = [];
                    vm.itemRevisionAttachmentAttributes = [];
                    vm.itemRevisionImages = new Hashtable();
                    vm.itemRevisionAttachments = new Hashtable();

                    angular.forEach(vm.newItemRevisionAttributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.itemRevisionImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.itemRevisionImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.itemRevisionAttachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.itemRevisionAttachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    vm.newNprItem.objectAttributes = vm.newItemAttributes;
                    vm.newNprItem.revisionObjectAttributes = vm.newItemRevisionAttributes;
                    vm.newNprItem.itemTypeAttributes = vm.attributes;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    NprService.createNprItem(nprId, vm.newNprItem).then(
                        function (data) {
                            vm.newNprItem = data;
                            saveAttributes().then(
                                function (attributes) {
                                    $scope.callback(vm.newNprItem);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage("Item created successfully");
                                    vm.newNprItem = {
                                        id: null,
                                        itemType: null,
                                        itemName: null,
                                        description: null,
                                        makeOrBuy: "BUY",
                                        itemAttributes: [],
                                        units: Each,
                                        isTemporary: true,
                                        npr: nprId,
                                        notes: null,
                                        latestRevision: null
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

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0 || vm.itemImageAttributes.length > 0
                    || vm.itemAttachmentAttributes.length > 0 || vm.itemRevisionImageAttributes.length > 0 || vm.itemRevisionAttachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                        if (imgAtt.attributeDef.revisionSpecific) {
                            ClassificationService.updateAttributeImageValue(vm.newNprItem.itemType.objectType, vm.newNprItem.latestRevision, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else {
                            ClassificationService.updateAttributeImageValue(vm.newNprItem.itemType.objectType, vm.newNprItem.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                        if (imgAtt.attributeDef.revisionSpecific) {
                            ClassificationService.updateAttributeAttachmentValues(vm.newNprItem.itemType.objectType, vm.newNprItem.latestRevision, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else {
                            ClassificationService.updateAttributeAttachmentValues(vm.newNprItem.itemType.objectType, vm.newNprItem.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                    angular.forEach(vm.itemImageAttributes, function (imgAtt) {
                        ClassificationService.updateAttributeImageValue("ITEM", vm.newNprItem.id, imgAtt.id.attributeDef, vm.itemImages.get(imgAtt.id.attributeDef)).then(
                            function (data) {
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    })

                    angular.forEach(vm.itemAttachmentAttributes, function (imgAtt) {
                        ClassificationService.updateAttributeAttachmentValues("ITEM", vm.newNprItem.id, imgAtt.id.attributeDef, vm.itemAttachments.get(imgAtt.id.attributeDef)).then(
                            function (data) {
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    })

                    angular.forEach(vm.itemRevisionImageAttributes, function (imgAtt) {
                        ClassificationService.updateAttributeImageValue("ITEMREVISION", vm.newNprItem.id, imgAtt.id.attributeDef, vm.itemRevisionImages.get(imgAtt.id.attributeDef)).then(
                            function (data) {
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    })

                    angular.forEach(vm.itemRevisionAttachmentAttributes, function (imgAtt) {
                        ClassificationService.updateAttributeAttachmentValues("ITEMREVISION", vm.newNprItem.id, imgAtt.id.attributeDef, vm.itemRevisionAttachments.get(imgAtt.id.attributeDef)).then(
                            function (data) {
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    })
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function loadObjectAttributeDefs() {
                vm.newItemAttributes = [];
                vm.newItemRevisionAttributes = [];
                ItemTypeService.getAllTypeAttributes("ITEM").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newNprItem.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                newListValue: null,
                                mlistValue: [],
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                measurementUnit: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }

                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.newItemAttributes.push(att);
                        });
                        return ItemTypeService.getAllTypeAttributes("ITEMREVISION");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                ).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newNprItem.latestRevision,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                stringValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                measurementUnit: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.newItemRevisionAttributes.push(att);
                        });
                    }
                )
            }

            (function () {
                loadObjectAttributeDefs();
                $rootScope.$on('app.newNprItem.new', create);
            })();
        }
    }
)
;