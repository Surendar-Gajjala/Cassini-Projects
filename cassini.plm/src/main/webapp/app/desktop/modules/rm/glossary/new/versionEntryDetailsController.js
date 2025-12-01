define(['app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
    ],
    function (module) {
        module.controller('VersionEntryDetailsController', VersionEntryDetailsController);

        function VersionEntryDetailsController($scope, $rootScope, $timeout, $sce, $state, $translate, $cookies, GlossaryService, CommonService, DialogService,
                                               ObjectAttributeService, ObjectTypeAttributeService, ECOService, MfrService,
                                               AttributeAttachmentService, WorkflowDefinitionService, MfrPartsService, ItemService, ProjectService) {
            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.glossaryEntryProperties = [];

            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var successMsg = $translate.instant("PROJECT_ATTRIBUTE_UPDATE_MESSAGE");
            var integerValidation = parsed.html($translate.instant("INTEGER_VALIDATION")).html();
            var selectProject = parsed.html($translate.instant("SELECT_PROJECT")).html();
            var selectItemRevision = parsed.html($translate.instant("SELECT_ITEMREVISION")).html();
            var selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            var selectChange = parsed.html($translate.instant("SELECT_CHANGE")).html();
            var selectItem = parsed.html($translate.instant("SELECT_ITEM")).html();
            var selectManufacturer = parsed.html($translate.instant("SELECT_MANUFACTURER")).html();
            var selectManufacturerPart = parsed.html($translate.instant("SELECT_MANUFACTURERPART")).html();

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
            vm.currencies = null;

            function updateVersion() {
                if (validate()) {
                    GlossaryService.updateGlossaryEntry(vm.versionEntry).then(
                        function (data) {
                            vm.versionEntry = data;
                            $rootScope.hideSidePanel();
                            $scope.callback(data);
                            vm.versionEntry = {
                                id: null,
                                name: null,
                                description: null,
                                notes: null
                            };
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.versionEntry.name == "" || vm.versionEntry.name == null || vm.versionEntry.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.versionEntry.description == "" || vm.versionEntry.description == null || vm.versionEntry.description == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(descriptionValidation);
                }

                return valid;
            }

            function loadGlossaryObjectProperties() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType("TERMINOLOGYENTRY").then(
                    function (data) {
                        vm.loading = false;
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.entryId,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.entryId,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    longTextValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    booleanValue: null,
                                    dateValue: null,
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
                            vm.glossaryEntryProperties.push(att);
                        });

                        loadObjectProperties();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            }

            function loadObjectProperties() {
                ObjectAttributeService.getAllObjectAttributes(vm.entryId).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.glossaryEntryProperties, function (attribute) {
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
                                    if (attribute.attributeDef.refType == 'ITEM') {
                                        ItemService.getItem(value.refValue).then(
                                            function (itemValue) {
                                                attribute.value.refValue = itemValue;
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    } else if (attribute.attributeDef.refType == 'ITEMREVISION') {
                                        ItemService.getItemRevision(value.refValue).then(
                                            function (revisionValue) {
                                                attribute.value.refValue = revisionValue;
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
                                    } else if (attribute.attributeDef.refType == 'GLOSSARYENTRY') {
                                        ProjectService.getProject(value.refValue).then(
                                            function (projectValue) {
                                                attribute.value.refValue = projectValue;
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    }
                                }
                                attribute.value.stringValue = value.stringValue;
                                attribute.value.longTextValue = value.longTextValue;
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
                                attribute.value.refValue = value.refValue;
                                attribute.value.imageValue = value.imageValue;
                                attribute.value.currencyValue = value.currencyValue;
                                attribute.value.richTextValue = value.richTextValue;
                                if (value.richTextValue != null) {
                                    attribute.value.encodedRichTextValue = $sce.trustAsHtml(value.richTextValue);
                                    $timeout(function () {
                                        $('.note-current-fontname').text('Arial');
                                    }, 1000);
                                }
                                if (value.currencyType != null) {
                                    attribute.value.currencyType = value.currencyType;
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

            function back() {
                window.history.back();
            }

            function deleteAttachments(attribute, attachment) {
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
                        if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                            ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
                });
            }

            function saveProperties(attribute) {
                if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                    ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
                    ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
                        ObjectAttributeService.createObjectAttribute(vm.entryId, attribute.value).then(
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
                        ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'GLOSSARYENTRY', attachmentValue).then(
                                            function (data) {
                                                glossaryAttachPropertyIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (glossaryAttachPropertyIds.length > 0) {
                                                    angular.forEach(glossaryAttachPropertyIds, function (revAttachId) {
                                                        attribute.value.attachmentValues.push(revAttachId);
                                                    });
                                                    ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'GLOSSARYENTRY', attachmentValue).then(
                                function (data) {
                                    glossaryAttachPropertyIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    if (glossaryAttachPropertyIds.length > 0) {
                                        angular.forEach(glossaryAttachPropertyIds, function (mfrAttachId) {
                                            attribute.value.attachmentValues.push(mfrAttachId);
                                        });
                                        ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
                    attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                    attribute.value.timeValue = attribute.timeValue;
                    if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                        ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
                    if (attribute.attributeDef.objectType == 'TERMINOLOGYENTRY') {
                        ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
                } else if (attribute.value.refValue.objectType == 'GLOSSARYENTRY') {
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
                else if (attribute.attributeDef.refType == 'GLOSSARYENTRY') {
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

            /*------------ RichText Methods ----------------*/
            vm.editRichText = editRichText;
            vm.cancelRichText = cancelRichText;
            vm.saveRichText = saveRichText;

            function editRichText(attribute) {
                attribute.editMode = true;
                $rootScope.richTextValue = attribute.value.richTextValue;
                $timeout(function () {
                    $('.note-current-fontname').text('Arial');
                });
            }

            function saveRichText(attribute) {
                attribute.editMode = false;
                if (attribute.value.richTextValue != null) {
                    ObjectAttributeService.updateObjectAttribute(vm.entryId, attribute.value).then(
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
            };

            (function () {
                //if ($application.homeLoaded == true) {
                loadGlossaryObjectProperties();
                vm.glossaryEntryDetail = $scope.data.glossaryEntryDetails;
                vm.entryId = vm.glossaryEntryDetail.id;
                vm.versionEntry = {
                    id: vm.glossaryEntryDetail.id,
                    version: vm.glossaryEntryDetail.version,
                    createdBy: vm.glossaryEntryDetail.createdBy,
                    createdDate: vm.glossaryEntryDetail.createdDate,
                    defaultDetail: vm.glossaryEntryDetail.defaultDetail,
                    glossaryEntryDetails: vm.glossaryEntryDetail.glossaryEntryDetails
                };
                //}
            })();
        }
    }
);
