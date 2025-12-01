define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/relationshipService',
        'app/shared/services/core/relatedItemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('RelatedItemAttributesController', RelatedItemAttributesController);

        function RelatedItemAttributesController($scope, $rootScope, $sce, $state, $timeout, $stateParams, $translate, RelationshipService,
                                                 RelatedItemService, AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService,
                                                 MfrPartsService, ItemService, ObjectAttributeService, DialogService, CommonService) {

            var vm = this;

            vm.relatedItem = $scope.data.selectedRelatedItemData;
            var currencyMap = new Hashtable();
            vm.loading = true;
            vm.saveRelatedItemAttribute = saveRelatedItemAttribute;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;
            vm.change = change;
            vm.cancel = cancel;
            vm.changeTime = changeTime;
            vm.cancelTime = cancelTime;
            vm.changeTimestamp = changeTimestamp;
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.cancelChanges = cancelChanges;
            vm.changeCurrencyValue = changeCurrencyValue;
            vm.saveTime = saveTime;
            vm.saveImage = saveImage;
            vm.saveAttachments = saveAttachments;
            vm.currencies = $application.currencies;
            vm.saveObject = saveObject;
            vm.showRefValueDetails = showRefValueDetails;
            vm.deleteAttachments = deleteAttachments;
            vm.openAttachment = openAttachment;

            vm.choices = [
                {
                    name: "True",
                    value: true
                },
                {
                    name: "False",
                    value: false
                }
            ];

            function addToListValue(attribute) {
                if (attribute.value.listValue == undefined) {
                    attribute.value.listValue = [];
                }

                attribute.value.listValue.push(attribute.newListValue);
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function change(attribute) {
                attribute.changeImage = true;
            }

            function cancel(attribute) {
                attribute.changeImage = false;
            }

            function changeTime(attribute) {
                attribute.showTimeAttribute = true;
            }

            function cancelTime(attribute) {
                attribute.showTimeAttribute = false;
                attribute.showTimestamp = false;
            }

            function changeTimestamp(attribute) {
                attribute.showTimestamp = true;
            }

            function addAttachment(attribute) {
                attribute.showAttachment = true;
            }

            function cancelAttachment(attribute) {
                attribute.showAttachment = false;
                attribute.attachmentValues = [];
                attribute.revAttachmentValues = [];
            }

            function cancelChanges(attribute) {
                attribute.editMode = false;
                attribute.value.refValue = vm.previousValue;
                attribute.changeCurrency = false;
            }

            function changeCurrencyValue(attribute) {
                attribute.changeCurrency = true;
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                if (vm.relatedItem.fromItemRevision.id == $rootScope.itemRevision.id) {
                    vm.lifeCycleStatus = vm.relatedItem.toItemRevision.lifeCyclePhase.phaseType;
                } else {
                    vm.lifeCycleStatus = vm.relatedItem.fromItemRevision.lifeCyclePhase.phaseType;
                }

                RelationshipService.getAllAttributesByRelationship(vm.relatedItem.relationship.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.relatedItem.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.relatedItem.id,
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
                                showAttachment: false,
                                attachmentValues: [],
                                timeValue: null,
                                showTimeAttribute: false,
                                timestampValue: null,
                                showTimestamp: false,
                                editMode: false,
                                changeCurrency: false
                            };

                            vm.attributes.push(att);
                        });
                        loadAttributes();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadAttributes() {
                RelatedItemService.getRelatedItemAttributes(vm.relatedItem.id).then(
                    function (data) {
                        var map = new Hashtable();
                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.attributes, function (attribute) {
                                var attachmentIds = [];
                                var value = map.get(attribute.attributeDef.id);
                                if (value != null) {
                                    if (value.attachmentValues.length > 0) {
                                        angular.forEach(value.attachmentValues, function (attachment) {
                                            attachmentIds.push(attachment);
                                        })
                                        AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                            function (data) {
                                                vm.attachments = data;
                                                attribute.value.attachmentValues = vm.attachments;
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    }
                                    if (value.refValue != null) {
                                        addRefValue(attribute, value);
                                    }
                                    attribute.value.stringValue = value.stringValue;
                                    attribute.value.longTextValue = value.longTextValue;
                                    attribute.value.integerValue = value.integerValue;
                                    attribute.value.doubleValue = value.doubleValue;
                                    attribute.value.booleanValue = value.booleanValue;
                                    attribute.value.mlistValue = value.mlistValue;
                                    attribute.value.dateValue = value.dateValue;
                                    attribute.value.listValue = value.listValue;
                                    attribute.value.timeValue = value.timeValue;
                                    attribute.value.timestampValue = value.timestampValue;
                                    attribute.value.imageValue = value.imageValue;
                                    attribute.value.currencyValue = value.currencyValue;
                                    attribute.value.currencyType = value.currencyType;
                                    if (value.currencyType != null) {
                                        attribute.value.encodedCurrencyType = currencyMap.get(value.currencyType);
                                    }
                                    if (value.richTextValue != null) {
                                        attribute.value.encodedRichTextValue = $sce.trustAsHtml(value.richTextValue);
                                        $timeout(function () {
                                            $('.note-current-fontname').text('Arial');
                                        }, 1000);
                                    }
                                    attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                }
                            }
                        );
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

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
                    ECOService.getChangeObject(value.refValue).then(
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
                }
            }

            function saveRelatedItemAttribute(attribute) {
                if (attribute.value.id == undefined || attribute.value.id == null) {
                    RelatedItemService.createRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                        function (data) {
                            attribute.value.id = data.id;
                            attribute.listValueEditMode = false;
                            attribute.changeCurrency = false;
                            attribute.editMode = false;
                            loadAttributes();
                            $rootScope.showSuccessMessage($rootScope.attributeCreateMessage);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    RelatedItemService.updateRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                        function (data) {
                            attribute.value.id = data.id;
                            attribute.listValueEditMode = false;
                            attribute.changeCurrency = false;
                            attribute.editMode = false;
                            loadAttributes();
                            $rootScope.showSuccessMessage($rootScope.attributeCreateMessage);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function saveTime(attribute) {
                if (attribute.timeValue != null) {
                    /* attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                     attribute.value.timeValue = attribute.timeValue;*/
                    RelatedItemService.updateRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                        function (data) {
                            attribute.showTimeAttribute = false;
                            $rootScope.showSuccessMessage($rootScope.timeAttributeMessage);
                            loadAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (attribute.timestampValue != null) {
                    attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    attribute.value.timestampValue = attribute.timestampValue;
                    RelatedItemService.updateRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                        function (data) {
                            attribute.showTimestamp = false;
                            $rootScope.showSuccessMessage($rootScope.timestampAttributeMessage);
                            loadAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function saveImage(attribute) {
                if (attribute.newImageValue == null) {
                    $rootScope.showWarningMessage($rootScope.imageWarningMessage);
                }
                if (attribute.newImageValue != null) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    attribute.imageValue = attribute.newImageValue;
                    RelatedItemService.updateRelatedItemAttribute(attribute.id.objectId, attribute.value).then(
                        function (data) {
                            ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                function (data) {
                                    attribute.changeImage = false;
                                    attribute.newImageValue = null;
                                    attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                    $rootScope.showSuccessMessage($rootScope.imageMessage);
                                    loadAttributes();
                                    $rootScope.hideBusyIndicator();
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

            function saveAttachments(attribute) {
                if (attribute.attachmentValues.length == 0) {
                    $rootScope.showWarningMessage($rootScope.attachmentWarningMessage);
                }
                if (attribute.attachmentValues.length > 0) {
                    var attachmentIds = [];
                    var attachments = attribute.value.attachmentValues;
                    attribute.value.attachmentValues = [];
                    angular.forEach(attachments, function (attachment) {
                        attachmentIds.push(attachment.id);
                    });
                    angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                        var itemAttachmentExists = false;
                        angular.forEach(attachments, function (itemAttachment) {
                            if ((attribute.id.objectId == itemAttachment.objectId) && (attribute.id.attributeDef == itemAttachment.attributeDef) && (attachmentValue.name == itemAttachment.name)) {
                                itemAttachmentExists = true;
                                attachmentIds.remove(itemAttachment.id);

                                var options = {
                                    title: $rootScope.attachmentDialogTitle,
                                    message: attachmentValue.name + " : " + $rootScope.attachmentDialogMessage,
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($("#rightSidePanel"));
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'RELATEDITEM', attachmentValue).then(
                                            function (data) {
                                                attachmentIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (attachmentIds.length > 0) {
                                                    angular.forEach(attachmentIds, function (attachId) {
                                                        attribute.value.attachmentValues.push(attachId);
                                                    });
                                                    RelatedItemService.updateRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                                                        function (data) {
                                                            $rootScope.hideBusyIndicator();
                                                            if (attribute.attachmentValues.length == 0) {
                                                                loadAttributes();
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
                                    } else {
                                        attribute.showAttachment = false;
                                        loadAttributes();
                                    }
                                });
                            }
                        });
                        if (itemAttachmentExists == false) {
                            $rootScope.showBusyIndicator($("#rightSidePanel"));
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'RELATEDITEM', attachmentValue).then(
                                function (data) {
                                    attachmentIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    if (attachmentIds.length > 0) {
                                        angular.forEach(attachmentIds, function (attachId) {
                                            attribute.value.attachmentValues.push(attachId);
                                        });
                                        RelatedItemService.updateRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                                            function (data) {
                                                $rootScope.hideBusyIndicator();
                                                if (attribute.attachmentValues.length == 0) {
                                                    loadAttributes();
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

                    });
                }

            }

            function saveObject(attribute) {
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                attribute.value.refValue = attribute.value.refValue.id;
                if (attribute.value.id == undefined || attribute.value.id == null) {
                    RelatedItemService.createRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage($rootScope.attributeCreateMessage);
                            attribute.editMode = false;
                            loadAttributes();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else {
                    RelatedItemService.updateRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                            attribute.editMode = false;
                            loadAttributes();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function showRefValueDetails(attribute) {
                if (attribute.value.refValue.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'CHANGE') {
                    if (attribute.value.refValue.changeType == 'ECO') {
                        $state.go('app.changes.eco.details', {objectId: attribute.value.refValue.id});
                    } else if (attribute.value.refValue.changeType == 'DCO') {
                        $state.go('app.changes.dco.details', {objectId: attribute.value.refValue.id});
                    } else if (attribute.value.refValue.changeType == 'ECR') {
                        $state.go('app.changes.ecr.details', {objectId: attribute.value.refValue.id});
                    } else if (attribute.value.refValue.changeType == 'DCR') {
                        $state.go('app.changes.dcr.details', {objectId: attribute.value.refValue.id});
                    }
                } else if (attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.value.refValue.manufacturer,
                        manufacturePartId: attribute.value.refValue.id
                    });
                }
            }

            vm.previousValue = null;
            vm.showSelectionDialog = showSelectionDialog;
            function showSelectionDialog(objectType, attribute) {
                $scope.previousValue = attribute.value.refValue;
                var objectSelector = $application.getObjectSelector(objectType);
                if (objectSelector != null) {
                    if (attribute.value.refValue != null && attribute.value.refValue != "") {
                        $rootScope.objectAttributeValue = attribute.value.refValue.id;
                    }
                    if (objectType == "PERSON" && attribute.value.refValue != null && attribute.value.refValue != "") {
                        $rootScope.personObjectValue = attribute.value.refValue.id;
                    }
                    objectSelector.show($rootScope, attribute.attributeDef, function (object, displayValue) {
                        attribute.refValue = object.id;
                        attribute.editMode = true;
                        attribute.value.refValue = object;

                    });
                }
            }


            function deleteAttachments(attribute, attachment) {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                var attachments = attribute.value.attachmentValues;
                attribute.value.attachmentValues = [];
                angular.forEach(attachments, function (attach) {
                    if (attach.id != attachment.id) {
                        attribute.value.attachmentValues.push(attach.id);
                    }
                });
                if (attribute.attributeDef.dataType == "ATTACHMENT") {
                    RelatedItemService.updateRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                        function (data) {
                            AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                function (data) {
                                    loadAttributes();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(attachment.name + ' : ' + $rootScope.deleteMessage);
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

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
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
                    RelatedItemService.updateRelatedItemAttribute(vm.relatedItem.id, attribute.value).then(
                        function (data) {
                            attribute.editMode = false;
                            /* $(".note-editor").hide();*/
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                            loadAttributes();

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            };

            function cancelRichText(attribute) {
                attribute.editMode = false;
                attribute.value.richTextValue = $rootScope.richTextValue;
                /*$(".note-editor").hide();*/
            };

            vm.editListValue = editListValue;
            function editListValue(attribute) {
                attribute.editMode = true;
                attribute.value.listValue = null;
                $rootScope.mlistValue = attribute.value.mlistValue;
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                loadAttributeDefs();
            })();
        }
    }
);