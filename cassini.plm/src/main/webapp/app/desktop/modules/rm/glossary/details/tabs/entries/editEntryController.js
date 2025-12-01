define(['app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/projectService'
    ],
    function (module) {
        module.controller('EditEntryController', EditEntryController);

        function EditEntryController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $translate, $cookies, GlossaryService,
                                     CommonService, ItemTypeService, ObjectAttributeService, AttributeAttachmentService, DialogService, ECOService, MfrService,
                                     WorkflowDefinitionService, MfrPartsService, ItemService, ProjectService) {
            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.saveTimeProperty = saveTimeProperty;
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
            var currencyMap = new Hashtable();
            vm.currencies = $application.currencies;

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

            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var entryUpdateMsg = parsed.html($translate.instant("ENTRY_UPDATE_MSG")).html();

            vm.entryLanguages = [];

            function create() {
                if (validateEntryDetails()) {
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
                        angular.forEach(vm.entryLanguages, function (lang) {
                            if (lang.languageName != "Attributes") {
                                vm.glossaryEntry.glossaryEntryDetails.push(lang);
                            }
                        })
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        GlossaryService.updateGlossaryEntryEdit($stateParams.glossaryId, vm.glossaryEntry).then(
                            function (data) {
                                vm.glossaryEntry = data;
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(entryUpdateMsg);
                                $scope.callback();
                                $rootScope.hideBusyIndicator();
                                vm.glossaryEntry = {
                                    id: null,
                                    version: null,
                                    latest: null,
                                    defaultDetail: null,
                                    glossaryEntryDetails: [],
                                    defaultDetails: null
                                };
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
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

            var enterNameIn = parsed.html($translate.instant("ENTER_NAME_IN")).html();
            var enterDescriptionIn = parsed.html($translate.instant("ENTER_DESCRIPTION_IN")).html();

            function validateEntryDetails() {
                var valid = true;
                angular.forEach(vm.entryLanguages, function (detail) {
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

            vm.entryAttributes = [];
            vm.entryRequiredAttributes = [];
            vm.glossaryEntryAttributes = [];

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
                                value: {
                                    id: {
                                        objectId: vm.glossaryEntry.id,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    booleanValue: null,
                                    dateValue: null,
                                    textValue: null,
                                    longTextValue: null,
                                    richTextValue: null,
                                    hyperLinkValue: null,
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
                                newImageValue: null,
                                showAttachment: false,
                                attachmentValues: [],
                                timeValue: null,
                                showTimeAttribute: false,
                                timestampValue: null,
                                showTimestamp: false,
                                editMode: false,
                                richMode: false,
                                changeCurrency: false
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }

                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }

                            vm.glossaryEntryAttributes.push(att);
                        });
                        loadObjectProperties();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadObjectProperties() {
                ObjectAttributeService.getAllObjectAttributes(vm.glossaryEntry.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.glossaryEntryAttributes, function (attribute) {
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
                                attribute.value.textValue = value.textValue;
                                attribute.value.longTextValue = value.longTextValue;
                                attribute.value.longTextValue = value.longTextValue;
                                attribute.value.hyperLinkValue = value.hyperLinkValue;
                                if (value.richTextValue != null) {
                                    attribute.value.encodedRichTextValue = $sce.trustAsHtml(value.richTextValue);
                                }
                                attribute.value.richTextValue = value.richTextValue;
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

            vm.emptyEntryDetail = {
                id: null,
                name: null,
                description: null,
                language: null,
                glossaryEntry: null,
                notes: null
            };

            function loadGlossaryLanguages() {
                angular.forEach($rootScope.selectedGlossaryLanguages, function (glossaryLang) {
                    var languageExist = false;
                    var entryLanguageData = null;
                    angular.forEach(vm.glossaryEntry.glossaryEntryDetails, function (entryLanguage) {
                        if (glossaryLang.language.language == entryLanguage.language.language) {
                            languageExist = true;
                            entryLanguageData = entryLanguage;
                            entryLanguageData.languageName = entryLanguage.language.language;
                        }
                    });

                    if (languageExist) {
                        vm.entryLanguages.push(entryLanguageData)
                    } else {
                        vm.newEntryDetail = angular.copy(vm.emptyEntryDetail);
                        vm.newEntryDetail.language = glossaryLang.language;
                        vm.newEntryDetail.languageName = glossaryLang.language.language;
                        vm.entryLanguages.push(vm.newEntryDetail);
                    }
                })

                $timeout(function () {
                    vm.newEntryDetail = angular.copy(vm.emptyEntryDetail);
                    vm.newEntryDetail.languageName = "Attributes";
                    vm.entryLanguages.push(vm.newEntryDetail);
                }, 100);
            }

            var successMsg = $translate.instant("PROJECT_ATTRIBUTE_UPDATE_MESSAGE");

            function deleteAttachments(attribute, attachment) {
                var attachments = attribute.value.attachmentValues;
                attribute.value.attachmentValues = [];
                angular.forEach(attachments, function (attach) {
                    if (attach.id != attachment.id) {
                        attribute.value.attachmentValues.push(attach.id);
                    }
                });
                if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                    ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                        function (data) {
                            AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                function (data) {
                                    loadObjectProperties();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(attachment.name + ' ' + $rootScope.deleteMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            /*---------------  To get RefValue Details By RefId  -----------*/

            function addRefValue(attribute, value) {

                if (attribute.attributeDef.refType == 'ITEM') {
                    ItemService.getItem(value.refValue).then(
                        function (itemValue) {
                            attribute.value.refValue = itemValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
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
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (attribute.attributeDef.refType == 'CHANGE') {
                    ECOService.getECO(value.refValue).then(
                        function (changeValue) {
                            attribute.value.refValue = changeValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (attribute.attributeDef.refType == 'WORKFLOW') {
                    WorkflowDefinitionService.getWorkflowDefinition(value.refValue).then(
                        function (workflowValue) {
                            attribute.value.refValue = workflowValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (attribute.attributeDef.refType == 'MANUFACTURER') {
                    MfrService.getManufacturer(value.refValue).then(
                        function (mfrValue) {
                            attribute.value.refValue = mfrValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (attribute.attributeDef.refType == 'MANUFACTURERPART') {
                    MfrPartsService.getManufacturepart(value.refValue).then(
                        function (mfrPartValue) {
                            attribute.value.refValue = mfrPartValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (attribute.attributeDef.refType == 'PROJECT') {
                    ProjectService.getProject(value.refValue).then(
                        function (projectValue) {
                            attribute.value.refValue = projectValue;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else if (attribute.attributeDef.refType == 'PERSON') {
                    CommonService.getPerson(value.refValue).then(
                        function (person) {
                            attribute.value.refValue = person;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function saveProperties(attribute) {
                if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                    ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage(successMsg);
                            attribute.listValueEditMode = false;
                            attribute.editMode = false;
                            attribute.changeCurrency = false;
                            loadObjectProperties();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function saveImage(attribute) {
                if (attribute.newImageValue != null) {
                    attribute.imageValue = attribute.newImageValue;
                    ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
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
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            /*------------- To save OBJECT Property values  -------------------*/

            function saveObject(attribute) {
                attribute.value.refValue = attribute.value.refValue.id;
                if (attribute.value.id == undefined || attribute.value.id == null) {
                    if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                        ObjectAttributeService.createObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage(successMsg)
                                attribute.editMode = false;
                                loadObjectProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
                else {
                    if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                        ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage(successMsg)
                                attribute.editMode = false;
                                loadObjectProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
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
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TERMINOLOGYENTRY', attachmentValue).then(
                                            function (data) {
                                                mfrAttachPropertyIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (mfrAttachPropertyIds.length > 0) {
                                                    angular.forEach(mfrAttachPropertyIds, function (revAttachId) {
                                                        attribute.value.attachmentValues.push(revAttachId);
                                                    });
                                                    ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                                                        function (data) {
                                                            if (attribute.attachmentValues.length == 0) {
                                                                loadObjectProperties();
                                                                attribute.showAttachment = false;
                                                                $rootScope.showSuccessMessage($rootScope.attachmentMessage);
                                                            }
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
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
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TERMINOLOGYENTRY', attachmentValue).then(
                                function (data) {
                                    mfrAttachPropertyIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    if (mfrAttachPropertyIds.length > 0) {
                                        angular.forEach(mfrAttachPropertyIds, function (mfrAttachId) {
                                            attribute.value.attachmentValues.push(mfrAttachId);
                                        });
                                        ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                                            function (data) {
                                                if (attribute.attachmentValues.length == 0) {
                                                    loadObjectProperties();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage($rootScope.attachmentMessage);
                                                }

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
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
                    /* attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");*/
                    attribute.value.timeValue = attribute.timeValue;
                    if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                        ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage(mfrtimeMessage);
                                loadObjectProperties();
                            }, function (error) {
                                  $rootScope.showErrorMessage(error.message);
                                  $rootScope.hideBusyIndicator();
                             }
                        )
                    }
                }
                if (attribute.timestampValue != null) {
                    attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    attribute.value.timestampValue = attribute.timestampValue;
                    if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                        ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage(mfrtimestampMessage);
                                loadObjectProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
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
                } else if (attribute.value.refValue.objectType == 'TERMINOLOGYENTRY') {
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
                        title: selectWorkFlow,
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

            /*------------ RichText Methods ----------------*/
            vm.editRichText = editRichText;
            vm.cancelRichText = cancelRichText;
            vm.saveRichText = saveRichText;

            function editRichText(attribute) {
                attribute.editMode = true;
                $rootScope.richTextValue = attribute.value.richTextValue;
            }

            function saveRichText(attribute) {
                attribute.editMode = false;
                if (attribute.value.richTextValue != null) {
                    ObjectAttributeService.updateObjectAttribute(vm.glossaryEntry.id, attribute.value).then(
                        function (data) {
                            attribute.editMode = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                            loadObjectProperties();

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
            vm.objectCancelChanges = objectCancelChanges;
            function objectCancelChanges(attribute) {
                attribute.editMode = false;
                attribute.value.refValue = vm.previousValue;
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
                ObjectAttributeService.deleteObjectAttribute(vm.glossaryEntry.id, attribute.value.id.attributeDef).then(
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

            (function () {
                //if ($application.homeLoaded == true) {
                vm.glossaryEntryDetail = $scope.data.glossaryEntryDetails;
                vm.glossaryEntry = {
                    id: vm.glossaryEntryDetail.id,
                    version: vm.glossaryEntryDetail.version,
                    latest: vm.glossaryEntryDetail.latest,
                    glossaryEntryDetails: vm.glossaryEntryDetail.glossaryEntryDetails,
                    createdDate: vm.glossaryEntryDetail.createdDate,
                    createdBy: vm.glossaryEntryDetail.createdBy
                };
                loadGlossaryLanguages();
                $rootScope.$on('app.glossary.entry.edit', create);
                loadEntryCustomProperties();
                //}
            })();
        }
    }
);
