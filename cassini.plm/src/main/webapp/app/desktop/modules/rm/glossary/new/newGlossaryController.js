define(['app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('NewGlossaryController', NewGlossaryController);

        function NewGlossaryController($scope, $rootScope, $timeout, $sce, $state, $translate, $cookies, GlossaryService, ECOService, MfrService, MfrPartsService, ItemService, ProjectService,
                                       CommonService, ItemTypeService, ObjectAttributeService, AttributeAttachmentService, WorkflowDefinitionService,
                                       DialogService) {
            var vm = this;

            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var currencyMap = new Hashtable();

            vm.glossaryAttributes = [];
            vm.glossaryRequiredAttributes = [];
            vm.allGlossaryAttribtues = [];
            vm.languages = [];

            vm.glossaryLanguages = [];
            vm.newLanguage = false;

            vm.emptyGlossaryDetail = {
                id: null,
                name: null,
                description: null,
                language: null,
                glossary: null,
                defaultLanguage: null
            };

            vm.deleteAttachments = deleteAttachments;
            vm.saveObject = saveObject;
            vm.saveTimeProperty = saveTimeProperty;
            vm.showObjectValues = showObjectValues;
            vm.showRefValueDetails = showRefValueDetails;
            vm.saveProperties = saveProperties;
            vm.saveAttachments = saveAttachments;
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.showImageProperty = showImageProperty;
            vm.cancelToListValue = cancelToListValue;
            vm.saveImage = saveImage;
            vm.addToListValue = addToListValue;
            vm.openPropertyAttachment = openPropertyAttachment;
            vm.validateAttribute = validateAttribute;
            vm.changeTime = changeTime;
            vm.cancelTime = cancelTime;
            vm.changeTimestamp = changeTimestamp;
            vm.change = change;
            vm.cancel = cancel;
            vm.changeCurrencyValue = changeCurrencyValue;
            vm.currencies = $application.currencies;

            var parsed = angular.element("<div></div>");
            var updatedSuccessfullyMsg = parsed.html($translate.instant("UPDATED_SUCCESS_MESSAGE")).html();
            var NameValidation = parsed.html($translate.instant("PROJECT_NAME_VALIDATION")).html();
            var DescriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var validation = parsed.html($translate.instant("CANNOT_BE_EMPTY")).html();
            var manager = parsed.html($translate.instant("PROJECT_MANAGER")).html();
            var integerValidation = parsed.html($translate.instant("INTEGER_VALIDATION")).html();
            var selectProject = parsed.html($translate.instant("SELECT_PROJECT")).html();
            var selectItemRevision = parsed.html($translate.instant("SELECT_ITEMREVISION")).html();
            var selectWorkFlow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            var selectChange = parsed.html($translate.instant("SELECT_CHANGE")).html();
            var selectItem = parsed.html($translate.instant("SELECT_ITEM")).html();
            var selectManufacturer = parsed.html($translate.instant("SELECT_MANUFACTURER")).html();
            var selectManufacturerPart = parsed.html($translate.instant("SELECT_MANUFACTURERPART")).html();
            var noFileSelected = parsed.html($translate.instant("ATTACHMENT_WARNING_MESSAGE")).html();
            var successMsg = $translate.instant("PROJECT_ATTRIBUTE_UPDATE_MESSAGE");
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var enterNameIn = parsed.html($translate.instant("ENTER_NAME_IN")).html();
            var enterDescriptionIn = parsed.html($translate.instant("ENTER_DESCRIPTION_IN")).html();
            var enterLanguage = parsed.html($translate.instant("ENTER_LANGUAGE")).html();

            vm.byDefault = parsed.html($translate.instant("BY_DEFAULT")).html();
            vm.defaultLanguageTitle = parsed.html($translate.instant("DEFAULT_LANGUAGE")).html();
            vm.clickOnLanguageToChange = parsed.html($translate.instant("CLICK_ON_LANGUAGE_TO_CHANGE")).html();
            vm.clickOn = parsed.html($translate.instant("CLICK_ON")).html();
            vm.toChange = parsed.html($translate.instant("TO_CHANGE")).html();
            vm.supportedLanguages = parsed.html($translate.instant("SUPPORTED_LANGUAGES")).html();
            vm.note = parsed.html($translate.instant("NOTE")).html();
            vm.defaultTitle = parsed.html($translate.instant("DEFAULT")).html();
            var deleteLanguageTitle = parsed.html($translate.instant("DELETE_LANGUAGE")).html();
            var deleteLanguageDialogMsg = parsed.html($translate.instant("DELETE_LANGUAGE_MESSAGE")).html();
            var languageDeletedMsg = parsed.html($translate.instant("LANGUAGE_DELETED_MSG")).html();
            var languageUpdatedMsg = parsed.html($translate.instant("LANGUAGE_UPDATED_MSG")).html();
            vm.selectLanguage = parsed.html($translate.instant("SELECT_LANGUAGE")).html();
            vm.setDefaultLanguage = parsed.html($translate.instant("SET_DEFAULT_LANGUAGE")).html();
            vm.createNewLanguage = parsed.html($translate.instant("CREATE_NEW_LANGUAGE")).html();

            vm.createNew = createNew;
            vm.createLanguage = createLanguage;
            vm.cancelChanges = cancelChanges;
            vm.selectLanguageType = selectLanguageType;
            vm.setDefaultLanguage = setDefaultLanguage;
            vm.editLanguage = editLanguage;
            vm.cancelLanguageChanges = cancelLanguageChanges;
            vm.updateLanguage = updateLanguage;
            vm.deleteLanguage = deleteLanguage;

            function setDefaultLanguage(language) {
                if (language.defaultLanguage == false) {
                    language.defaultLanguage = true;
                    GlossaryService.setGlossaryLanguage(language).then(
                        function (data) {
                            $scope.defaultLanguageName = language.language;
                            vm.glossary.defaultLanguage = data;
                            angular.forEach(vm.languages, function (lang) {
                                if (lang.id != language.id) {
                                    lang.defaultLanguage = false;
                                }
                            });

                            if (language.selected == false) {
                                language.selected = true;
                                var index = vm.glossaryLanguages.length - 1;
                                vm.newGlossaryDetail = angular.copy(vm.emptyGlossaryDetail);
                                vm.newGlossaryDetail.language = data;
                                vm.newGlossaryDetail.languageName = data.language;
                                vm.glossaryLanguages.splice(index, 0, vm.newGlossaryDetail);
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }

            }

            function createNew() {
                vm.newLanguage = true;
                vm.language = {
                    id: null,
                    code: null,
                    language: null
                }
            }

            function createLanguage() {
                if (vm.language.language == null || vm.language.language == "") {
                    $rootScope.showWarningMessage(enterLanguage);
                } else {
                    vm.language.language = vm.language.language.toUpperCase();
                    GlossaryService.createGlossaryLanguage(vm.language).then(
                        function (data) {
                            vm.newLanguage = false;
                            data.selected = true;
                            data.languageName = data.language;
                            vm.languages.push(data);
                            var index = vm.glossaryLanguages.length - 1;
                            vm.newGlossaryDetail = angular.copy(vm.emptyGlossaryDetail);
                            vm.newGlossaryDetail.language = data;
                            vm.newGlossaryDetail.languageName = data.language;
                            vm.glossaryLanguages.splice(index, 0, vm.newGlossaryDetail);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            function editLanguage(language) {
                language.editMode = true;
                language.oldLanguage = language.language;
            }

            function updateLanguage(language) {
                if (language.language == null || language.language == "") {
                    $rootScope.showWarningMessage(enterLanguage);
                } else {
                    language.language = language.language.toUpperCase();
                    GlossaryService.updateGlossaryLanguage(language).then(
                        function (data) {
                            vm.newLanguage = false;
                            language.editMode = false;
                            language.languageName = data.language;
                            $rootScope.showSuccessMessage(languageUpdatedMsg);
                            angular.forEach(vm.glossaryLanguages, function (languageType) {
                                if (languageType.language.id == language.id) {
                                    languageType.languageName = language.languageName;
                                }
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function deleteLanguage(language) {

                var options = {
                    title: deleteLanguageTitle,
                    message: deleteLanguageDialogMsg,
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        GlossaryService.deleteGlossaryLanguage(language.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(languageDeletedMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                             }
                        )
                    }
                })
            }

            function cancelChanges() {
                vm.newLanguage = false;
            }

            function cancelLanguageChanges(language) {
                language.editMode = false;
                language.language = language.oldLanguage;
            }

            function selectLanguageType(language) {
                if (language.selected == true) {

                    var index = vm.glossaryLanguages.length - 1;
                    vm.newGlossaryDetail = angular.copy(vm.emptyGlossaryDetail);
                    vm.newGlossaryDetail.language = language;
                    vm.newGlossaryDetail.languageName = language.language;
                    vm.glossaryLanguages.splice(index, 0, vm.newGlossaryDetail);
                } else {
                    angular.forEach(vm.glossaryLanguages, function (languageType) {
                        if (languageType.language.id == language.id) {
                            var index = vm.glossaryLanguages.indexOf(languageType);
                            vm.glossaryLanguages.splice(index, 1);
                        }
                    })
                }

            }

            function create() {
                if (vm.mode == "NEW") {
                    if (validateLanguages()) {
                        vm.validattributes = [];
                        angular.forEach(vm.glossaryRequiredAttributes, function (attribute) {
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
                        if (vm.validattributes.length == vm.glossaryRequiredAttributes.length) {
                            vm.glossary.glossaryDetails = [];
                            angular.forEach(vm.glossaryLanguages, function (lang) {
                                if (lang.languageName != "Attributes") {
                                    vm.glossary.glossaryDetails.push(lang);
                                }
                            });
                            $rootScope.showBusyIndicator($('#rightSidePanel'));
                            GlossaryService.createGlossary(vm.glossary).then(
                                function (data) {
                                    vm.glossary = data;
                                    saveGlossaryProperties();
                                    $rootScope.hideSidePanel();
                                    $scope.callback();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                    }
                }

                if (vm.mode == "EDIT") {
                    if (validateLanguages()) {
                        vm.validattributes = [];
                        angular.forEach(vm.glossaryRequiredAttributes, function (attribute) {
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
                        if (vm.validattributes.length == vm.glossaryRequiredAttributes.length) {
                            vm.glossary.glossaryDetails = [];
                            angular.forEach(vm.glossaryLanguages, function (lang) {
                                if (lang.languageName != "Attributes") {
                                    vm.glossary.glossaryDetails.push(lang);
                                }
                            })
                            $rootScope.showBusyIndicator($('#rightSidePanel'));
                            GlossaryService.updateGlossary(vm.glossary).then(
                                function (data) {
                                    vm.glossary = data;
                                    saveGlossaryProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                }
            }

            function validate() {
                var valid = true;
                if (vm.glossary.name == "" || vm.glossary.name == null || vm.glossary.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation)
                }

                return valid;
            }

            function validateLanguages() {
                var valid = true;
                angular.forEach(vm.glossaryLanguages, function (language) {
                    if (language.languageName != "Attributes") {
                        if (valid) {
                            if (language.name == null || language.name == "" || language.name == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage(enterNameIn + "" + language.language.language);
                            }
                        }
                    }
                });

                return valid;
            }

            function saveGlossaryProperties() {
                angular.forEach(vm.glossaryRequiredAttributes, function (reqatt) {
                    vm.glossaryAttributes.push(reqatt);
                });
                if (vm.glossaryAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.glossaryAttributes, function (attribute) {
                        attribute.id.objectId = vm.glossary.id;
                        /*if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addGlossaryPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.glossary.id, vm.glossaryAttributes).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    var count = 0;
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                count++;
                                                if (count == vm.propertyImageAttributes.length) {
                                                    $rootScope.hideSidePanel();
                                                    $scope.callback();
                                                    $rootScope.hideBusyIndicator();
                                                    vm.glossary = {
                                                        id: null,
                                                        revision: null,
                                                        lifeCyclePhase: null,
                                                        isReleased: null,
                                                        releasedDate: null,
                                                        latest: false,
                                                        number: null,
                                                        defaultDetail: null,
                                                        defaultGlossaryDetail: null,
                                                        glossaryDetails: [],
                                                        defaultLanguage: null
                                                    };
                                                }
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    })
                                } else {
                                    $rootScope.hideSidePanel();
                                    $scope.callback();
                                    $rootScope.hideBusyIndicator();
                                    vm.glossary = {
                                        id: null,
                                        revision: null,
                                        lifeCyclePhase: null,
                                        isReleased: null,
                                        releasedDate: null,
                                        latest: false,
                                        number: null,
                                        defaultDetail: null,
                                        defaultGlossaryDetail: null,
                                        glossaryDetails: [],
                                        defaultLanguage: null
                                    };
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
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                    vm.glossary = {
                        id: null,
                        revision: null,
                        lifeCyclePhase: null,
                        isReleased: null,
                        releasedDate: null,
                        latest: false,
                        number: null,
                        defaultDetail: null,
                        defaultGlossaryDetail: null,
                        glossaryDetails: [],
                        defaultLanguage: null
                    };
                }
            }

            /*------------ RichText Methods ----------------*/
            vm.editRichText = editRichText;
            vm.cancelRichText = cancelRichText;
            vm.saveRichText = saveRichText;

            function editRichText(attribute) {
                /* $(".note-editor").show();*/
                attribute.editMode = true;
                $rootScope.richTextValue = attribute.value.richTextValue;
                $timeout(function () {
                    $('.note-current-fontname').text('Arial');
                });
            }

            function saveRichText(attribute) {
                attribute.editMode = false;
                if (attribute.value.richTextValue != null) {
                    ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                        function (data) {
                            attribute.editMode = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                            loadGlossaryCustomProperties();

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            };

            function cancelRichText(attribute) {
                attribute.editMode = false;
                attribute.value.richTextValue = $rootScope.richTextValue;
                /*$(".note-editor").hide();*/
            };

            function addGlossaryPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TERMINOLOGY', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                });

                return propertyAttachmentIds;
            }

            function loadGlossaryProperties() {
                ItemTypeService.getAllTypeAttributes("TERMINOLOGY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.glossary.id,
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
                                vm.glossaryAttributes.push(att);
                            } else {
                                vm.glossaryRequiredAttributes.push(att);
                            }
                            vm.allGlossaryAttribtues.push(att);
                        });
                        , function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
            }

            function loadGlossaryLanguages() {
                GlossaryService.getGlossaryLanguages().then(
                    function (data) {
                        vm.languages = data;
                        if (vm.mode == "NEW") {
                            vm.glossaryLanguages = [];
                            angular.forEach(vm.languages, function (language) {
                                if (language.defaultLanguage == true) {
                                    language.editMode = false;
                                    language.selected = true;
                                    $scope.defaultLanguageName = language.language;
                                    language.languageName = language.language;
                                    vm.newGlossaryDetail = angular.copy(vm.emptyGlossaryDetail);
                                    vm.newGlossaryDetail.language = language;
                                    vm.newGlossaryDetail.languageName = language.language;
                                    vm.glossaryLanguages.push(vm.newGlossaryDetail);
                                    vm.glossary.defaultGlossaryDetail = vm.newGlossaryDetail;
                                    vm.glossary.defaultLanguage = language;
                                } else {
                                    language.editMode = false;
                                    language.selected = false;
                                    language.languageName = language.language;
                                }
                            });

                            $timeout(function () {
                                vm.newGlossaryDetail = angular.copy(vm.emptyGlossaryDetail);
                                vm.newGlossaryDetail.languageName = "Attributes";
                                vm.glossaryLanguages.push(vm.newGlossaryDetail);
                            }, 100);
                        } else {
                            angular.forEach(vm.glossaryLanguages, function (glossaryLang) {
                                angular.forEach(vm.languages, function (language) {
                                    if (glossaryLang.language.language == language.language) {
                                        language.selected = true;
                                        language.disableLanguage = true;
                                        glossaryLang.languageName = glossaryLang.language.language;
                                    }
                                });
                            });

                            $timeout(function () {
                                vm.newGlossaryDetail = angular.copy(vm.emptyGlossaryDetail);
                                vm.newGlossaryDetail.languageName = "Attributes";
                                vm.glossaryLanguages.push(vm.newGlossaryDetail);
                            }, 100);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.glossaryCustomAttributes = [];

            function loadGlossaryCustomProperties() {
                ItemTypeService.getAllTypeAttributes("TERMINOLOGY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.glossary.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.glossary.id,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    booleanValue: null,
                                    dateValue: null,
                                    textValue: null,
                                    longTextValue: null,
                                    richTextValue: " ",
                                    listValue: null,
                                    mlistValue: [],
                                    newListValue: null,
                                    listValueEditMode: false,
                                    timeValue: null,
                                    timestampValue: null,
                                    refValue: null,
                                    imageValue: null,
                                    attachmentValues: [],
                                    currencyValue: null,
                                    currencyType: null
                                },
                                changeImage: false,
                                imageValue: null,
                                mlistValue: [],
                                newImageValue: null,
                                timeValue: null,
                                showAttachment: false,
                                attachmentValues: [],
                                showTimeAttribute: false,
                                showTimestamp: false,
                                timestampValue: null,
                                editMode: false,
                                changeCurrency: false,
                                editTimeValue: null
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

                            vm.glossaryCustomAttributes.push(att);
                        });
                        loadObjectProperties();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadObjectProperties() {
                ObjectAttributeService.getAllObjectAttributes(vm.glossary.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.glossaryCustomAttributes, function (attribute) {
                            var attachmentIds = [];
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                if (value.attachmentValues.length > 0) {
                                    angular.forEach(value.attachmentValues, function (attachment) {
                                        attachmentIds.push(attachment);
                                    });
                                    AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                        function (data) {
                                            vm.mfrPropertyAttachments = data;
                                            attribute.value.attachmentValues = vm.mfrPropertyAttachments;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                         }
                                    )
                                }
                                if (value.refValue != null) {
                                    addRefValue(attribute, value);
                                }
                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                if (value.doubleValue != null) {
                                    attribute.value.doubleValue = parseFloat(value.doubleValue).toFixed(5);
                                } else {
                                    attribute.value.doubleValue = value.doubleValue;
                                }
                                attribute.value.booleanValue = value.booleanValue;
                                attribute.value.dateValue = value.dateValue;
                                attribute.value.listValue = value.listValue;
                                attribute.value.mlistValue = value.mlistValue;
                                attribute.value.timeValue = value.timeValue;
                                attribute.value.timestampValue = value.timestampValue;
                                attribute.value.imageValue = value.imageValue;
                                attribute.value.currencyValue = value.currencyValue;
                                attribute.value.currencyType = value.currencyType;
                                attribute.value.longTextValue = value.longTextValue;
                                attribute.value.richTextValue = value.richTextValue;
                                if (value.richTextValue != null) {
                                    attribute.value.encodedRichTextValue = $sce.trustAsHtml(value.richTextValue);
                                    $timeout(function () {
                                        $('.note-current-fontname').text('Arial');
                                    }, 1000);
                                }
                                if (value.currencyType != null) {
                                    attribute.value.encodedCurrencyType = currencyMap.get(value.currencyType);
                                }
                                attribute.value.attachmentValues = value.attachmentValues;
                                attribute.value.mfrImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /*---------------  To get RefValue Details By RefId  -----------*/

            function addRefValue(attribute, value) {

                if (attribute.attributeDef.refType == 'ITEM') {
                    ItemService.getItem(value.refValue).then(
                        function (itemValue) {
                            attribute.value.refValue = itemValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (attribute.attributeDef.refType == 'ITEMREVISION') {
                    ItemService.getRevisionId(value.refValue).then(
                        function (revisionValue) {
                            attribute.value.refValue = revisionValue;
                            ItemService.getItem(revisionValue.itemMaster).then(
                                function (data) {
                                    attribute.value.refValue.itemMaster = data.itemNumber;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (attribute.attributeDef.refType == 'CHANGE') {
                    ECOService.getECO(value.refValue).then(
                        function (changeValue) {
                            attribute.value.refValue = changeValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (attribute.attributeDef.refType == 'WORKFLOW') {
                    WorkflowDefinitionService.getWorkflowDefinition(value.refValue).then(
                        function (workflowValue) {
                            attribute.value.refValue = workflowValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (attribute.attributeDef.refType == 'MANUFACTURER') {
                    MfrService.getManufacturer(value.refValue).then(
                        function (mfrValue) {
                            attribute.value.refValue = mfrValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (attribute.attributeDef.refType == 'MANUFACTURERPART') {
                    MfrPartsService.getManufacturepart(value.refValue).then(
                        function (mfrPartValue) {
                            attribute.value.refValue = mfrPartValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (attribute.attributeDef.refType == 'PROJECT') {
                    ProjectService.getProject(value.refValue).then(
                        function (projectValue) {
                            attribute.value.refValue = projectValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else if (attribute.attributeDef.refType == 'PERSON') {
                    CommonService.getPerson(value.refValue).then(
                        function (person) {
                            attribute.value.refValue = person;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function deleteAttachments(attribute, attachment) {
                var options = {
                    title: $rootScope.deleteDialogTitle,
                    message: $rootScope.deleteDialogMessage,
                    okButtonClass: 'btn-danger'
                };
                var attachments = attribute.value.attachmentValues;
                attribute.value.attachmentValues = [];
                angular.forEach(attachments, function (attach) {
                    if (attach.id != attachment.id) {
                        attribute.value.attachmentValues.push(attach.id);
                    }
                });
                if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                    ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                        function (data) {
                            AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                function (data) {
                                    loadObjectProperties();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(attachment.name + ' ' + $rootScope.deleteMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function saveProperties(attribute) {
                if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                    ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage(successMsg);
                            attribute.listValueEditMode = false;
                            attribute.editMode = false;
                            attribute.changeCurrency = false;
                            loadObjectProperties();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function saveImage(attribute) {
                if (attribute.newImageValue != null) {
                    attribute.imageValue = attribute.newImageValue;
                    ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                        function (data) {
                            ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                function (data) {
                                    attribute.changeImage = false;
                                    attribute.newImageValue = null;
                                    attribute.value.ecoImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                    $rootScope.showSuccessMessage($rootScope.imageMessage);
                                    loadObjectProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            /*------------- To save OBJECT Property values  -------------------*/

            function saveObject(attribute) {
                attribute.value.refValue = attribute.value.refValue.id;
                if (attribute.value.id == undefined || attribute.value.id == null) {
                    if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                        ObjectAttributeService.createObjectAttribute(vm.glossary.id, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage(successMsg)
                                attribute.editMode = false;
                                loadObjectProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
                else {
                    if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                        ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage(successMsg)
                                attribute.editMode = false;
                                loadObjectProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            /*----------- To save Mfr Attachment Property --------------------*/

            function saveAttachments(attribute) {
                if (attribute.attachmentValues.length == 0) {
                    $rootScope.showWarningMessage(noFileSelected);
                }
                if (attribute.attachmentValues.length > 0) {
                    var mfrAttachPropertyIds = [];
                    var mfrAttachments = attribute.value.attachmentValues;
                    attribute.value.attachmentValues = [];
                    angular.forEach(mfrAttachments, function (attachment) {
                        mfrAttachPropertyIds.push(attachment.id);
                    });
                    angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                        var mfrAttachmentExists = false;
                        angular.forEach(mfrAttachments, function (mfrAttachment) {
                            if ((attribute.id.objectId == mfrAttachment.objectId) && (attribute.id.attributeDef == mfrAttachment.attributeDef) && (attachmentValue.name == mfrAttachment.name)) {
                                mfrAttachmentExists = true;
                                mfrAttachPropertyIds.remove(mfrAttachment.id);

                                var options = {
                                    title: $rootScope.attachmentDialogTitle,
                                    message: attachmentValue.name + " : " + $rootScope.attachmentDialogMessage,
                                    okButtonClass: 'btn-danger'
                                }
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TERMINOLOGY', attachmentValue).then(
                                            function (data) {
                                                mfrAttachPropertyIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (mfrAttachPropertyIds.length > 0) {
                                                    angular.forEach(mfrAttachPropertyIds, function (revAttachId) {
                                                        attribute.value.attachmentValues.push(revAttachId);
                                                    });
                                                    ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                                                        function (data) {
                                                            if (attribute.attachmentValues.length == 0) {
                                                                loadObjectProperties();
                                                                attribute.showAttachment = false;
                                                                $rootScope.showSuccessMessage($rootScope.attachmentMessage);
                                                            }
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                            $rootScope.hideBusyIndicator();
                                                        }
                                                    )
                                                }
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    } else {
                                        attribute.showAttachment = false;
                                        loadObjectProperties();
                                    }
                                });
                            }
                        })
                        if (mfrAttachmentExists == false) {
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TERMINOLOGY', attachmentValue).then(
                                function (data) {
                                    mfrAttachPropertyIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    if (mfrAttachPropertyIds.length > 0) {
                                        angular.forEach(mfrAttachPropertyIds, function (mfrAttachId) {
                                            attribute.value.attachmentValues.push(mfrAttachId);
                                        });
                                        ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                                            function (data) {
                                                if (attribute.attachmentValues.length == 0) {
                                                    loadObjectProperties();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage($rootScope.attachmentMessage);
                                                }

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })
                }
            }

            /*-----------------  To save TIME & TIMESTAMP Attribute values  --------------*/
            var mfrtimeMessage = $translate.instant("MFR_TIME_ATTRIBUTE_MESSAGE");
            var mfrtimestampMessage = $translate.instant("MFR_ATTRIBUTE_TIMESTAMP_MESSAGE");

            function saveTimeProperty(attribute) {
                if (attribute.timeValue != null) {
                    /*attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");*/
                    attribute.value.timeValue = attribute.timeValue;
                    if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                        ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage(mfrtimeMessage);
                                loadObjectProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                             }
                        )
                    }
                }
                if (attribute.timestampValue != null) {
                    attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    attribute.value.timestampValue = attribute.timestampValue;
                    if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                        ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage(mfrtimestampMessage);
                                loadObjectProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                             }
                        )
                    }
                }
            }

            function showImageProperty(attribute) {
                var modal = document.getElementById('myModal');
                var modalImg = document.getElementById("img01");

                modal.style.display = "block";
                modalImg.src = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeimage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function openPropertyAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                //launchUrl(url);
                window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            function isInteger(x) {
                return x % 1 === 0;
            }

            function validateAttribute(value) {
                if (isInteger(value)) {
                    return true;
                } else {
                    $rootScope.showWarningMessage(integerValidation);
                }
            }

            /*------------- It goes to the specified Details Page -------------------*/

            function showRefValueDetails(attribute) {
                if (attribute.value.refValue.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'CHANGE') {
                    $state.go('app.changes.ecos.details', {ecoId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.value.refValue.manufacturer,
                        manufacturePartId: attribute.value.refValue.id
                    });
                } else if (attribute.value.refValue.objectType == 'TERMINOLOGY') {
                    $state.go('app.pm.project.details', {projectId: attribute.value.refValue.id});
                }
            }

            /*---------  To get specified Type of selection view  --------*/

            vm.previousValue = null;
            function showObjectValues(attribute) {
                var options = null;
                attribute.editMode = true;
                vm.previousValue = attribute.value.refValue;
                if (attribute.attributeDef.refType == 'MANUFACTURER') {

                    options = {
                        title: selectManufacturer,
                        template: 'app/desktop/modules/select/mfrSelectionView.jsp',
                        controller: 'MfrSelectionController as mfrSelectVm',
                        resolve: 'app/desktop/modules/select/mfrSelectionController',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.mfr'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.name + ' : ' + $rootScope.objectMfrInfo)
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'MANUFACTURERPART') {
                    options = {
                        title: selectManufacturerPart,
                        template: 'app/desktop/modules/select/mfrPartSelectionView.jsp',
                        controller: 'MfrPartSelectionController as mfrPartSelectVm',
                        resolve: 'app/desktop/modules/select/mfrPartSelectionController.js',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.mfrPart'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.partName + ' : ' + $rootScope.objectMfrPartInfo)
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'CHANGE') {
                    options = {
                        title: selectChange,
                        template: 'app/desktop/modules/select/changeSelectionView.jsp',
                        controller: 'ChangeSelectionController as changeSelectVm',
                        resolve: 'app/desktop/modules/select/changeSelectionController',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.change'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.ecoNumber + ' : ' + $rootScope.objectECOInfo)
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'ITEM') {
                    options = {
                        title: selectItem,
                        template: 'app/desktop/modules/select/itemSelectionView.jsp',
                        controller: 'ItemSelectionController as itemSelectVm',
                        resolve: 'app/desktop/modules/select/itemSelectionController',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.item'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.itemNumber + ' : ' + $rootScope.objectItemInfo)
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'WORKFLOW') {
                    options = {
                        title: selectWorkflow,
                        template: 'app/desktop/modules/select/workflowSelectionView.jsp',
                        controller: 'WorkflowSelectionController as wrkSelectVm',
                        resolve: 'app/desktop/modules/select/workflowSelectionController',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.workflow'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.name + ' : ' + $rootScope.objectWorkflowInfo)
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'ITEMREVISION') {
                    options = {
                        title: selectItemRevision,
                        template: 'app/desktop/modules/select/itemRevisionSelection.jsp',
                        controller: 'ItemRevisionSelectionController as itemRevSelectVm',
                        resolve: 'app/desktop/modules/select/itemRevisionSelectionController',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.itemRevision'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(attribute.value.refValue.itemMaster + ' : ' + $rootScope.objectItemRevisionInfo)
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'PROJECT') {
                    options = {
                        title: selectProject,
                        template: 'app/desktop/modules/select/projectSelectionView.jsp',
                        controller: 'ProjectSelectionController as projectSelectVm',
                        resolve: 'app/desktop/modules/select/projectSelectionController',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.project'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.name + ' : ' + $rootScope.objectProjectInfo)
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }

            }

            function addToListValue(attribute) {
                if (attribute.value.listValue == undefined) {
                    attribute.value.listValue = [];
                }

                attribute.value.listValue.push(attribute.newListValue);
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function changeTime(attribute) {
                attribute.showTimeAttribute = true;
            }

            function cancelTime(attribute) {
                attribute.showTimeAttribute = false;
                attribute.showTimestamp = false;
            }

            function addAttachment(attribute) {
                attribute.showAttachment = true;
            }

            function changeTimestamp(attribute) {
                attribute.showTimestamp = true;
            }

            function cancelAttachment(attribute) {
                attribute.showAttachment = false;
                attribute.attachmentValues = [];
            }

            function change(attribute) {
                attribute.changeImage = true;
            }

            function cancel(attribute) {
                attribute.changeImage = false;
            }

            function changeCurrencyValue(attribute) {
                attribute.changeCurrency = true;
            }

            vm.editListValue = editListValue;
            vm.listCancelChanges = listCancelChanges;
            function editListValue(attribute) {
                attribute.editMode = true;
                $rootScope.mlistValue = attribute.value.mlistValue;
            }

            function listCancelChanges(attribute) {
                attribute.editMode = false;
                attribute.value.mlistValue = $rootScope.mlistValue;
            }

            var attrtibuteValueRemove = $translate.instant("ATTRIBUTE_VALUE_REMOVE_MSG");
            vm.deleteAttribute = deleteAttribute;
            function deleteAttribute(attribute) {
                ObjectAttributeService.deleteObjectAttribute(vm.glossary.id, attribute.value.id.attributeDef).then(
                    function (data) {
                        attribute.value.listValue = null;
                        $rootScope.showSuccessMessage(attrtibuteValueRemove);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.showSelectionDialog = showSelectionDialog;
            function showSelectionDialog(objectType, attribute) {
                vm.previousValue = attribute.value.refValue;
                var objectSelector = $application.getObjectSelector(objectType);
                if (objectSelector != null) {
                    if (objectType == "PERSON" && attribute.value.refValue != null && attribute.value.refValue != "") {
                        $rootScope.personObjectValue = attribute.value.refValue.id;
                    }
                    objectSelector.show($rootScope, function (object, displayValue) {
                        attribute.refValue = object.id;
                        attribute.editMode = true;
                        attribute.value.refValue = object;

                    });
                }
            }

            vm.editList = editList;
            vm.cancelListValue = cancelListValue;

            function editList(attribute) {
                attribute.editMode = true;
                $rootScope.listValue = attribute.value.listValue;
            }

            function cancelListValue(attribute) {
                attribute.value.listValue = $rootScope.listValue;
                attribute.editMode = false;
            }

            (function () {
                //if ($application.homeLoaded == true) {

                    vm.mode = $scope.data.glossaryMode;
                    vm.glossaryDetail = $scope.data.glossaryDetails;
                    if (vm.mode == "NEW") {
                        vm.glossary = {
                            id: null,
                            revision: null,
                            lifeCyclePhase: null,
                            isReleased: null,
                            releasedDate: null,
                            latest: false,
                            number: null,
                            defaultDetail: null,
                            defaultGlossaryDetail: null,
                            glossaryDetails: [],
                            defaultLanguage: null
                        };
                        loadGlossaryProperties();
                        loadGlossaryLanguages();
                    }
                    if (vm.mode == "EDIT") {
                        vm.glossary = {
                            id: vm.glossaryDetail.id,
                            revision: vm.glossaryDetail.revision,
                            lifeCyclePhase: vm.glossaryDetail.lifeCyclePhase,
                            isReleased: vm.glossaryDetail.isReleased,
                            releasedDate: vm.glossaryDetail.releasedDate,
                            latest: vm.glossaryDetail.latest,
                            number: vm.glossaryDetail.number,
                            defaultDetail: vm.glossaryDetail.defaultDetail,
                            glossaryDetails: vm.glossaryDetail.glossaryDetails,
                            defaultLanguage: vm.glossaryDetail.defaultLanguage,
                            createdDate: vm.glossaryDetail.createdDate,
                            createdBy: vm.glossaryDetail.createdBy
                        };
                        vm.glossaryLanguages = vm.glossaryDetail.glossaryDetails;
                        loadGlossaryLanguages();
                        loadGlossaryCustomProperties();
                    }
                    $rootScope.$on('app.glossary.new', create);
                //}
            })();
        }
    }
);
