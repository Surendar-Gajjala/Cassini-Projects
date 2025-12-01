define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/projectService'

    ],
    function (module) {
        module.controller('GlossaryAttributesController', GlossaryAttributesController);

        function GlossaryAttributesController($scope, $translate, $rootScope, $timeout, $sce, $state, $stateParams, $cookies,
                                              GlossaryService, CommonService, DialogService, ObjectAttributeService, ObjectTypeAttributeService, ECOService, MfrService,
                                              AttributeAttachmentService, WorkflowDefinitionService, MfrPartsService, ItemService, ProjectService) {

            var vm = this;
            var glossaryId = $stateParams.glossaryId;
            vm.glossaryProperties = [];

            var parsed = angular.element("<div></div>");
            var successMsg = $translate.instant("PROJECT_ATTRIBUTE_UPDATE_MESSAGE");
            var integerValidation = parsed.html($translate.instant("INTEGER_VALIDATION")).html();
            var selectProject = parsed.html($translate.instant("SELECT_PROJECT")).html();
            var selectItemRevision = parsed.html($translate.instant("SELECT_ITEMREVISION")).html();
            var selectWorkFlow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            var selectChange = parsed.html($translate.instant("SELECT_CHANGE")).html();
            var selectItem = parsed.html($translate.instant("SELECT_ITEM")).html();
            var selectManufacturer = parsed.html($translate.instant("SELECT_MANUFACTURER")).html();
            var selectManufacturerPart = parsed.html($translate.instant("SELECT_MANUFACTURERPART")).html();

            vm.saveTimeProperty = saveTimeProperty;
            vm.deleteAttachments = deleteAttachments;
            vm.saveObject = saveObject;
            vm.saveTimeProperty = saveTimeProperty;
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
            vm.currencies = null;

            function loadGlossary() {
                vm.loading = true;
                GlossaryService.getGlossary(glossaryId).then(
                    function (data) {
                        vm.glossary = data;
                        CommonService.getPersonReferences([$rootScope.selectedGlossary], 'createdBy');

                        loadGlossaryObjectProperties();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadGlossaryObjectProperties() {
                vm.glossaryProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("TERMINOLOGY").then(
                    function (data) {
                        vm.loading = false;
                        angular.forEach(data, function (attribute) {
                            if (attribute.visible) {
                                var att = {
                                    id: {
                                        objectId: glossaryId,
                                        attributeDef: attribute.id
                                    },
                                    attributeDef: attribute,
                                    value: {
                                        id: {
                                            objectId: glossaryId,
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
                                vm.glossaryProperties.push(att);
                            }
                        });

                        loadObjectProperties();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            }

            function loadObjectProperties() {
                ObjectAttributeService.getAllObjectAttributes(glossaryId).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.glossaryProperties, function (attribute) {
                            if (attribute.attributeDef.visible) {
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

                                    if (attribute.attributeDef.dataType == 'LIST') {
                                        if (value.listValue != null) {
                                            attribute.value.listValue = value.listValue;
                                            attribute.value.deleteFlag = false;
                                        }
                                        if (value.listValue == null) {
                                            attribute.value.listValue = attribute.attributeDef.defaultListValue;
                                            attribute.value.deleteFlag = true;
                                        }

                                        if (value.mlistValue != null) {
                                            attribute.value.mlistValue = value.mlistValue;
                                        }
                                        if (value.mlistValue == null) {
                                            attribute.value.mlistValue.push(attribute.attributeDef.defaultListValue);
                                        }

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
                                    if (attribute.value.dateValue)
                                        attribute.value.dateValuede = moment(attribute.value.dateValue, "DD/MM/YYYY").format("DD.MM.YYYY");
                                    /* attribute.value.listValue = value.listValue;
                                     attribute.value.mlistValue = value.mlistValue;*/
                                    attribute.value.timeValue = value.timeValue;
                                    attribute.value.timestampValue = value.timestampValue;
                                    attribute.value.imageValue = value.imageValue;
                                    attribute.value.currencyValue = value.currencyValue;
                                    attribute.value.currencyType = value.currencyType;
                                    attribute.value.textValue = value.textValue;
                                    attribute.value.longTextValue = value.longTextValue;
                                    attribute.value.hyperLinkValue = value.hyperLinkValue;
                                    attribute.value.richTextValue = value.richTextValue;
                                    if (value.richTextValue != null) {
                                        attribute.value.encodedRichTextValue = $sce.trustAsHtml(value.richTextValue);
                                    }
                                    attribute.value.richTextValue = value.richTextValue;
                                    if (value.currencyType != null) {
                                        attribute.value.encodedCurrencyType = currencyMap.get(value.currencyType);
                                    }
                                    attribute.value.attachmentValues = value.attachmentValues;
                                    attribute.value.mfrImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                } else {
                                    if (attribute.attributeDef.dataType == "LIST") {
                                        if (attribute.value.listValue == null && attribute.attributeDef.listMultiple == false) {
                                            attribute.value.listValue = attribute.attributeDef.defaultListValue;
                                            attribute.defaultListValue = false;
                                            attribute.value.deleteFlag = true;
                                        }
                                        if (attribute.value.mlistValue.length == 0 && attribute.attributeDef.listMultiple == true) {
                                            attribute.value.mlistValue.push(attribute.attributeDef.defaultListValue);
                                        }
                                    }
                                    if (attribute.value.stringValue == null) {
                                        attribute.value.stringValue = attribute.attributeDef.defaultTextValue;
                                    }
                                }
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function back() {
                window.history.back();
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
                } else if (attribute.attributeDef.refType == 'PERSON') {
                    CommonService.getPerson(value.refValue).then(
                        function (person) {
                            attribute.value.refValue = person;
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
            }

            /* function deleteAttachments(attribute, attachment) {
             var options = {
             title: $rootScope.deleteDialogTitle,
             message: $rootScope.deleteDialogMessage,
             okButtonClass: 'btn-danger'
             };
             DialogService.confirm(options, function (yes) {
             if (yes == true) {
             var attachments = attribute.value.attachmentValues;
             attribute.value.attachmentValues = [];
             angular.forEach(attachments, function (attach) {
             if (attach.id != attachment.id) {
             attribute.value.attachmentValues.push(attach.id);
             }
             });
             if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
             ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
             function (data) {
             AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
             function (data) {
             loadObjectProperties();
             $rootScope.hideBusyIndicator();
             $rootScope.showSuccessMessage(attachment.name + ' ' + $rootScope.deleteMessage);
             }
             )

             }
             )
             }
             }
             });
             }
             */

            function deleteAttachments(attribute, attachment) {
                $rootScope.showBusyIndicator($('.view-content'));
                var attachments = attribute.value.attachmentValues;
                attribute.value.attachmentValues = [];
                angular.forEach(attachments, function (attach) {
                    if (attach.id != attachment.id) {
                        attribute.value.attachmentValues.push(attach.id);
                    }
                });

                if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                    ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
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

            function saveProperties(attribute) {
                if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                    ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
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
                    ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
                        function (data) {
                            ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                function (data) {
                                    attribute.changeImage = false;
                                    attribute.newImageValue = null;
                                    attribute.value.ecoImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                    $rootScope.showSuccessMessage($rootScope.imageMessage);
                                    loadObjectProperties();
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
                        ObjectAttributeService.createObjectAttribute(glossaryId, attribute.value).then(
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
                        ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
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

            function saveObject(attribute) {
                $rootScope.showBusyIndicator($(".view-content"));
                attribute.value.refValue = attribute.value.refValue.id;
                if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                    ObjectAttributeService.createObjectAttribute(glossaryId, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage(successMsg)
                            $rootScope.hideBusyIndicator();
                            attribute.editMode = false;
                            loadObjectProperties();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                        ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage(successMsg)
                                $rootScope.hideBusyIndicator();
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
            var noFileSelected = parsed.html($translate.instant("ATTACHMENT_WARNING_MESSAGE")).html();

            function saveAttachments(attribute) {
                if (attribute.attachmentValues.length == 0) {
                    $rootScope.showWarningMessage(noFileSelected);
                }
                if (attribute.attachmentValues.length > 0) {
                    var glossaryAttachPropertyIds = [];
                    var glossaryAttachments = attribute.value.attachmentValues;
                    attribute.value.attachmentValues = [];
                    angular.forEach(glossaryAttachments, function (attachment) {
                        glossaryAttachPropertyIds.push(attachment.id);
                    });
                    angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                        var mfrAttachmentExists = false;
                        angular.forEach(glossaryAttachments, function (glossaryAttachment) {
                            if ((attribute.id.objectId == glossaryAttachment.objectId) && (attribute.id.attributeDef == glossaryAttachment.attributeDef) && (attachmentValue.name == glossaryAttachment.name)) {
                                mfrAttachmentExists = true;
                                glossaryAttachPropertyIds.remove(glossaryAttachment.id);

                                var options = {
                                    title: $rootScope.attachmentDialogTitle,
                                    message: attachmentValue.name + " : " + $rootScope.attachmentDialogMessage,
                                    okButtonClass: 'btn-danger'
                                }
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TERMINOLOGY', attachmentValue).then(
                                            function (data) {
                                                glossaryAttachPropertyIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (glossaryAttachPropertyIds.length > 0) {
                                                    angular.forEach(glossaryAttachPropertyIds, function (revAttachId) {
                                                        attribute.value.attachmentValues.push(revAttachId);
                                                    });
                                                    ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
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
                                    glossaryAttachPropertyIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    if (glossaryAttachPropertyIds.length > 0) {
                                        angular.forEach(glossaryAttachPropertyIds, function (mfrAttachId) {
                                            attribute.value.attachmentValues.push(mfrAttachId);
                                        });
                                        ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
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
                        ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
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
                    if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
                        ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
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
                window.open(url);
                // window.open(url);
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
                    ObjectAttributeService.updateObjectAttribute(vm.glossary.id, attribute.value).then(
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

            vm.cancelChanges = cancelChanges;
            function cancelChanges(attribute) {
                attribute.editMode = false;
                attribute.value.refValue = vm.previousValue;
                attribute.changeCurrency = false;
                attribute.value.stringValue = $rootScope.stringValue;
                attribute.value.integerValue = $rootScope.integerValue;
                attribute.value.dateValue = $rootScope.dateValue;
                attribute.value.doubleValue = $rootScope.doubleValue;
                attribute.value.booleanValue = $rootScope.booleanValue;
                attribute.value.currencyValue = $rootScope.currencyValue;
                attribute.value.listValue = $rootScope.listValue;
                attribute.value.mlistValue = $rootScope.mlistValue;
                attribute.value.longTextValue = $rootScope.longTextValue;
                attribute.value.hyperLinkValue = $rootScope.hyperLinkValue;
                loadObjectProperties();
            }

            vm.editListValue = editListValue;
            vm.cancelListValue = cancelListValue;
            function editListValue(attribute) {
                attribute.editMode = true;
                $rootScope.mlistValue = attribute.value.mlistValue;
            }

            function cancelListValue(attribute) {
                attribute.value.mlistValue = $rootScope.mlistValue;
                attribute.editMode = false;
            }

            /*         vm.saveListProperties = saveListProperties;
             function saveListProperties(attribute) {
             if (!attribute.attributeDef.listMultiple) {
             var listValue = attribute.value.listValue;
             attribute.value.listValue = [];
             attribute.value.listValue.push(listValue);
             }
             if (attribute.attributeDef.objectType == 'TERMINOLOGY') {
             ObjectAttributeService.updateObjectAttribute(glossaryId, attribute.value).then(
             function (data) {
             $rootScope.showSuccessMessage(successMsg);
             attribute.listValueEditMode = false;
             attribute.editMode = false;
             attribute.changeCurrency = false;
             loadObjectProperties();
             }
             )
             }
             }*/

            vm.showSelectionDialog = showSelectionDialog;
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

            vm.deleteAttribute = deleteAttribute;
            var attrtibuteValueRemove = parsed.html($translate.instant("ATTRIBUTE_VALUE_REMOVE_MSG")).html();

            function deleteAttribute(attribute) {
                ObjectAttributeService.deleteObjectAttribute(vm.glossary.id, attribute.value.id.attributeDef).then(
                    function (data) {
                        attribute.defaultListValue = false;
                        attribute.value.listValue = attribute.attributeDef.defaultListValue;
                        attribute.value.deleteFlag = true;
                        $rootScope.showSuccessMessage(attrtibuteValueRemove);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.cancelListValue = cancelListValue;
            function cancelListValue(attribute) {
                attribute.value.listValue = $rootScope.listvalue;
                attribute.editMode = false;
            }

            $scope.showAttributeImage = showAttributeImage;
            function showAttributeImage(attribute) {
                var modal = document.getElementById('item-thumbnail-basic' + attribute.attributeDef.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + attribute.attributeDef.id);
                $("#thumbnail-image-basic" + attribute.attributeDef.id).width($('#thumbnail-view-basic' + attribute.attributeDef.id).outerWidth());
                $("#thumbnail-image-basic" + attribute.attributeDef.id).height($('#thumbnail-view-basic' + attribute.attributeDef.id).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                $scope.$on('app.glossary.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        ObjectAttributeService.getCurrencies().then(
                            function (data) {
                                vm.currencies = data;
                                angular.forEach(vm.currencies, function (currency) {
                                    currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                                })
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                        loadGlossary();
                    }
                })
            })();
        }
    }
)
;

