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
        module.controller('NewGlossaryEntryController', NewGlossaryEntryController);

        function NewGlossaryEntryController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $translate, $cookies, GlossaryService,
                                            CommonService, ItemTypeService, ObjectAttributeService, AttributeAttachmentService, DialogService) {
            var vm = this;

            var parsed = angular.element("<div></div>");

            var glossaryId = $stateParams.glossaryId;
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var entryUpdateMsg = parsed.html($translate.instant("ENTRY_UPDATE_MSG")).html();
            var entryCreateMsg = parsed.html($translate.instant("ENTRY_CREATE_MSG")).html();
            var entriesAddedMsg = parsed.html($translate.instant("ENTRIES_ADDED_MSG")).html();
            var enterNameIn = parsed.html($translate.instant("ENTER_NAME_IN")).html();
            var enterDescriptionIn = parsed.html($translate.instant("ENTER_DESCRIPTION_IN")).html();

            vm.entryLanguages = [];

            vm.glossaryEntryDetails = [];

            vm.glossaryEntry = {
                id: null,
                version: null,
                latest: null,
                defaultDetail: null,
                defaultGlossaryEntryDetail: null,
                glossaryEntryDetails: []
            };

            vm.newGlossaryEntryDetail = {
                id: null,
                name: null,
                description: null,
                glossaryEntry: null,
                language: null
            };

            function create() {
                if (validateEntryDetails()) {
                    vm.validattributes = [];
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
                        vm.glossaryEntry.glossaryEntryDetails = [];
                        angular.forEach(vm.glossaryEntryDetails, function (detail) {
                            if (detail.languageName != "Attributes") {
                                vm.glossaryEntry.glossaryEntryDetails.push(detail);
                            }
                        });
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        GlossaryService.addGlossaryEntryItem(glossaryId, vm.glossaryEntry).then(
                            function (data) {
                                vm.glossaryEntry = data;
                                saveEntryProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validateEntryDetails() {
                var valid = true;
                angular.forEach(vm.glossaryEntryDetails, function (detail) {
                    if (detail.languageName != "Attributes" && detail.language.defaultLanguage == true) {
                        if (valid) {
                            if (detail.name == null || detail.name == "" || detail.name == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage(enterNameIn + " " + detail.language.language);
                            } else if (detail.description == null || detail.description == "" || detail.description == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage(enterDescriptionIn + " " + detail.language.language)
                            }
                        }
                    } else if (detail.languageName != "Attributes" && detail.language.defaultLanguage == false) {
                        if (valid) {
                            if (detail.name != null && (detail.description == null || detail.description == "" || detail.description == undefined)) {
                                valid = false;
                                $rootScope.showWarningMessage(enterDescriptionIn + " " + detail.language.language);
                            }
                        }
                    }

                });

                return valid;
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
                        /*if (attribute.timeValue != null) {
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
                                    var count = 0;
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                            ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                                function (data) {
                                                    count++;
                                                    if (count == vm.propertyImageAttributes.length) {
                                                        $rootScope.hideSidePanel();
                                                        $rootScope.showSuccessMessage(entryCreateMsg);
                                                        $scope.callback();
                                                        $rootScope.hideBusyIndicator();
                                                        vm.glossaryEntry = {
                                                            id: null,
                                                            version: null,
                                                            latest: null,
                                                            defaultDetail: null,
                                                            defaultGlossaryEntryDetail: null,
                                                            glossaryEntryDetails: []
                                                        };
                                                        vm.glossaryEntryDetails = [];
                                                    }
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                }
                                            )
                                        }
                                    )
                                } else {
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage(entryCreateMsg);
                                    $scope.callback();
                                    $rootScope.hideBusyIndicator();
                                    vm.glossaryEntry = {
                                        id: null,
                                        version: null,
                                        latest: null,
                                        defaultDetail: null,
                                        defaultGlossaryEntryDetail: null,
                                        glossaryEntryDetails: []
                                    };
                                    vm.glossaryEntryDetails = [];
                                }

                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }, 2000);
                } else {
                    $rootScope.hideSidePanel();
                    $rootScope.showSuccessMessage(entryCreateMsg);
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                    vm.glossaryEntry = {
                        id: null,
                        version: null,
                        latest: null,
                        defaultDetail: null,
                        defaultGlossaryEntryDetail: null,
                        glossaryEntryDetails: []
                    };
                    vm.glossaryEntryDetails = [];
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

            function loadEntryDetails() {
                angular.forEach($rootScope.selectedGlossaryLanguages, function (language) {
                    var newEntryDetail = angular.copy(vm.newGlossaryEntryDetail);
                    newEntryDetail.language = language.language;
                    newEntryDetail.languageName = language.language.language;
                    vm.glossaryEntryDetails.push(newEntryDetail);
                    if (language.language.defaultLanguage) {
                        vm.glossaryEntry.defaultGlossaryEntryDetail = newEntryDetail;
                    }
                })

                $timeout(function () {
                    var newEntryDetail = angular.copy(vm.newGlossaryEntryDetail);
                    newEntryDetail.languageName = "Attributes";
                    vm.glossaryEntryDetails.push(newEntryDetail);
                }, 100)
            }

            function loadEntryCustomProperties() {
                vm.entryAttributes = [];
                vm.entryRequiredAttributes = [];

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
                                mlistValue: [],
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
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple) {
                                att.mlistValue.push(attribute.defaultListValue);
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
                    loadEntryCustomProperties();
                    loadEntryDetails();
                    $rootScope.$on('app.glossary.entry.newEntry', create);
                //}
            })();
        }
    }
);
