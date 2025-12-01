define(['app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('NewEntryController', NewEntryController);

        function NewEntryController($scope, $rootScope, $timeout, $sce, $state, $translate, $cookies, GlossaryService,
                                    CommonService, ItemTypeService, ObjectAttributeService, AttributeAttachmentService) {
            var vm = this;

            var parsed = angular.element("<div></div>");

            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");

            function create() {
                if (vm.mode == "NEW") {
                    if (validate()) {
                        vm.validattributes = [];
                        angular.forEach(vm.entryRequiredAttributes, function (attribute) {
                            if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                attribute.attributeDef.dataType != 'TIMESTAMP') {
                                if ($rootScope.checkAttribute(attribute)) {
                                    vm.validattributes.push(attribute);
                                }
                                else {
                                    $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                                }
                            } else {
                                vm.validattributes.push(attribute);
                            }
                        });
                        if (vm.validattributes.length == vm.entryRequiredAttributes.length) {
                            GlossaryService.createGlossaryEntry(vm.glossaryEntry).then(
                                function (data) {
                                    vm.glossaryEntry = data;
                                    saveEntryProperties();

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }

                if (vm.mode == "EDIT") {
                    if (validate()) {
                        vm.validattributes = [];
                        angular.forEach(vm.entryRequiredAttributes, function (attribute) {
                            if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                attribute.attributeDef.dataType != 'TIMESTAMP') {
                                if ($rootScope.checkAttribute(attribute)) {
                                    vm.validattributes.push(attribute);
                                }
                                else {
                                    $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                                }
                            } else {
                                vm.validattributes.push(attribute);
                            }
                        });
                        if (vm.validattributes.length == vm.entryRequiredAttributes.length) {
                            GlossaryService.updateGlossaryEntry(vm.glossaryEntry).then(
                                function (data) {
                                    vm.glossaryEntry = data;
                                    saveEntryProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }
            }

            function saveEntryProperties() {
                angular.forEach(vm.entryRequiredAttributes, function (reqatt) {
                    vm.entryAttributes.push(reqatt);
                });
                if (vm.entryAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.entryAttributes, function (attribute) {
                        attribute.id.objectId = vm.glossaryEntry.id;
                        /* if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addEntryPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.glossaryEntry.id, vm.entryAttributes).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                             }
                                        )
                                    })
                                }
                                $rootScope.hideSidePanel();
                                $scope.callback();
                                vm.glossaryEntry = {
                                    name: null,
                                    description: null
                                };
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }, 2000);
                } else {
                    $rootScope.hideSidePanel();
                    $scope.callback();
                    vm.glossaryEntry = {
                        name: null,
                        description: null
                    };
                }
            }

            function addEntryPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TERMINOLOGYENTRY', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                });

                return propertyAttachmentIds;
            }

            function validate() {
                var valid = true;
                if (vm.glossaryEntry.name == "" || vm.glossaryEntry.name == null || vm.glossaryEntry.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.glossaryEntry.description == "" || vm.glossaryEntry.description == null || vm.glossaryEntry.description == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(descriptionValidation);
                }

                return valid;
            }

            vm.entryAttributes = [];
            vm.entryRequiredAttributes = [];

            function loadEntryCustomProperties() {
                ItemTypeService.getAllTypeAttributes("TERMINOLOGYENTRY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.glossaryEntry.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
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

                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.required == false) {
                                vm.entryAttributes.push(att);
                            } else {
                                vm.entryRequiredAttributes.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    vm.mode = $scope.data.glossaryEntryMode;
                    vm.glossaryEntryDetail = $scope.data.glossaryEntryDetails;
                    if (vm.mode == "NEW") {
                        vm.glossaryEntry = {
                            id: null,
                            name: null,
                            description: null,
                            version: null,
                            latest: null,
                            notes: null
                        };
                    }
                    if (vm.mode == "EDIT") {
                        vm.glossaryEntry = {
                            id: vm.glossaryEntryDetail.id,
                            name: vm.glossaryEntryDetail.name,
                            version: vm.glossaryEntryDetail.version,
                            latest: vm.glossaryEntryDetail.latest,
                            description: vm.glossaryEntryDetail.description
                        };

                    }
                    $rootScope.$on('app.glossary.entry.new', create);
                    loadEntryCustomProperties();
                //}
            })();
        }
    }
);
