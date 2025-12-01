define(
    [
        'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/inspectionService',
        'app/shared/services/core/problemReportService',
        'app/shared/services/core/ncrService',
        'app/shared/services/core/qcrService',
    ],

    function (module) {
        module.directive('basicAttributeDetailsView', BasicAttributeDetailsViewDirective);

        function BasicAttributeDetailsViewDirective($rootScope, $compile, $sce, $timeout, $state, $translate, $application, DialogService, ECOService, ObjectAttributeService, AttributeAttachmentService,
                                                    ObjectTypeAttributeService, ItemService, WorkflowDefinitionService, MfrService, $window, CommonService, ReqDocumentService,
                                                    MfrPartsService, SpecificationsService, ProjectService, MESObjectTypeService, RequirementService, CustomObjectService, InspectionPlanService, InspectionService, ProblemReportService,
                                                    NcrService, QcrService) {
            return {
                templateUrl: 'app/desktop/modules/directives/basicAttributeDetailsViewDirective.jsp',
                restrict: 'E',
                scope: {
                    'objectType': '@',
                    'objectId': '=',
                    'qualityType': "@",
                    'updateObjectAttribute': '=',
                    'hasPermission': '='
                },
                link: function ($scope, $elem, attrs) {

                    /* declaring variables & functions */

                    $scope.loading = true;
                    $scope.saveObjectProperties = saveObjectProperties;
                    $scope.validateAttribute = validateAttribute;
                    $scope.addToListValue = addToListValue;
                    $scope.cancelToListValue = cancelToListValue;
                    $scope.ecoProperties = [];
                    $scope.opened = {};
                    $scope.change = change;
                    $scope.cancel = cancel;
                    $scope.addAttachment = addAttachment;
                    $scope.cancelAttachment = cancelAttachment;
                    $scope.openPropertyAttachment = openPropertyAttachment;
                    $scope.saveAttachments = saveAttachments;
                    $scope.saveImage = saveImage;
                    $scope.showImageProperty = showImageProperty;
                    $scope.changeTime = changeTime;
                    $scope.cancelTime = cancelTime;
                    $scope.changeTimestamp = changeTimestamp;
                    $scope.saveTimeProperty = saveTimeProperty;
                    $scope.showRefValueDetails = showRefValueDetails;
                    $scope.saveObject = saveObject;
                    $scope.deleteAttachments = deleteAttachments;
                    $scope.changeCurrencyValue = changeCurrencyValue;

                    var currencyMap = new Hashtable();
                    $scope.currencies = $application.currencies;

                    var parsed = angular.element("<div></div>");

                    $scope.showHyperLink = showHyperLink;
                    function showHyperLink(url) {
                        $window.open(url);

                    };

                    $scope.flags = [{
                        name: "True",
                        value: true
                    },
                        {
                            name: "False",
                            value: false
                        }
                    ];

                    function change(attribute) {
                        attribute.changeImage = true;
                    }

                    function cancel(attribute) {
                        attribute.changeImage = false;
                    }

                    function addAttachment(attribute) {
                        attribute.showAttachment = true;
                    }

                    function cancelAttachment(attribute) {
                        attribute.showAttachment = false;
                        attribute.attachmentValues = [];
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

                    function addToListValue(attribute) {
                        if (attribute.value.listValue == undefined) {
                            attribute.value.listValue = [];
                        }

                        attribute.value.listValue.push(attribute.newListValue);
                    }

                    function cancelToListValue(attribute) {
                        attribute.listValueEditMode = false;
                    }

                    function changeCurrencyValue(attribute) {
                        attribute.changeCurrency = true;
                    }

                    $scope.showSelectionDialog = showSelectionDialog;
                    function showSelectionDialog(objectType, attribute) {
                        $scope.previousValue = attribute.value.refValue;
                        var objectSelector = $application.getObjectSelector(objectType);
                        if (objectSelector != null) {
                            if (attribute.value.refValue != null && attribute.value.refValue != "") {
                                $rootScope.objectAttributeValue = attribute.value.refValue.id;
                            }
                            objectSelector.show($rootScope, attribute.attributeDef, function (object, displayValue) {
                                attribute.refValue = object.id;
                                attribute.editMode = true;
                                attribute.value.refValue = object;
                                if (objectType == 'ITEMREVISION') {
                                    attribute.value.refValue.itemMaster = object.itemMasterObject.itemNumber;
                                }

                            });
                        }
                    }

                    $scope.open = function ($event, elementOpened) {
                        $event.preventDefault();
                        $event.stopPropagation();

                        $scope.opened[elementOpened] = !$scope.opened[elementOpened];
                    };

                    $scope.$on('attributes', function (event, arg) {
                        $scope.attributes = arg;
                    });

                    $scope.deleteAttribute = deleteAttribute;
                    var attrtibuteValueRemove = parsed.html($translate.instant("ATTRIBUTE_VALUE_REMOVE_MSG")).html();

                    function deleteAttribute(attribute) {
                        ObjectAttributeService.deleteObjectAttribute($scope.objectId, attribute.value.id.attributeDef).then(
                            function (data) {
                                attribute.defaultListValue = false;
                                attribute.value.listValue = attribute.attributeDef.defaultListValue;
                                attribute.value.deleteFlag = true;
                                $rootScope.showSuccessMessage(attrtibuteValueRemove);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    $scope.objectCancelChanges = objectCancelChanges;
                    function objectCancelChanges(attribute) {
                        attribute.editMode = false;
                        attribute.value.refValue = $scope.previousValue;
                    }

                    function isInteger(x) {
                        return x % 1 === 0;
                    }

                    /*------------- To check INTEGER value is valid or Not --------------*/

                    function validateAttribute(value) {
                        if (isInteger(value)) {
                            return true;
                        } else {
                            return $scope.integerValid;
                        }
                    }

                    function validateAttributeValues(attribute) {
                        var valid = true;

                        if (attribute.attributeDef.dataType == "DOUBLE" && attribute.attributeDef.measurement != null && attribute.value.measurementUnit == null) {
                            valid = false;
                            $rootScope.showWarningMessage("Please select Measurement type");
                        }

                        return valid;
                    }

                    /*---- To Save STRING, INTEGER, BOOLEAN, DATE and DOUBLE Values ------*/

                    function saveObjectProperties(attribute) {
                        if (validateAttributeValues(attribute)) {
                            ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                                    attribute.listValueEditMode = false;
                                    attribute.changeCurrency = false;
                                    attribute.editMode = false;
                                    loadObjectPropertyValues();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    /*----------- To save ECO Attachment Property --------------------*/

                    function saveAttachments(attribute) {
                        if (attribute.attachmentValues.length == 0) {
                            $rootScope.showWarningMessage($rootScope.attachmentWarningMessage);
                        }
                        if (attribute.attachmentValues.length > 0) {
                            var ecoAttachPropertyIds = [];
                            var ecoAttachments = attribute.value.attachmentValues;
                            attribute.value.attachmentValues = [];
                            angular.forEach(ecoAttachments, function (attachment) {
                                ecoAttachPropertyIds.push(attachment.id);
                            });
                            angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                                var ecoAttachmentExists = false;
                                angular.forEach(ecoAttachments, function (ecoAttachment) {
                                    if ((attribute.id.objectId == ecoAttachment.objectId) && (attribute.id.attributeDef == ecoAttachment.attributeDef) && (attachmentValue.name == ecoAttachment.name)) {
                                        ecoAttachmentExists = true;
                                        ecoAttachPropertyIds.remove(ecoAttachment.id);

                                        var options = {
                                            title: $rootScope.attachmentDialogTitle,
                                            message: attachmentValue.name + " : " + $rootScope.attachmentDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        }
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                $rootScope.showBusyIndicator($('.view-content'));
                                                AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, $scope.qualityType, attachmentValue).then(
                                                    function (data) {
                                                        ecoAttachPropertyIds.push(data[0].id);
                                                        attribute.attachmentValues.remove(attachmentValue);
                                                        if (ecoAttachPropertyIds.length > 0) {
                                                            angular.forEach(ecoAttachPropertyIds, function (revAttachId) {
                                                                if (attribute.value.attachmentValues.indexOf(revAttachId) == -1) {
                                                                    attribute.value.attachmentValues.push(revAttachId);
                                                                }
                                                            });
                                                            ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                                                function (data) {
                                                                    $rootScope.hideBusyIndicator();
                                                                    if (attribute.attachmentValues.length == 0) {
                                                                        loadObjectPropertyValues();
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
                                                loadObjectPropertyValues();
                                            }
                                        });
                                    }
                                })
                                if (ecoAttachmentExists == false) {
                                    $rootScope.showBusyIndicator($('.view-content'));
                                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, $scope.qualityType, attachmentValue).then(
                                        function (data) {
                                            ecoAttachPropertyIds.push(data[0].id);
                                            attribute.attachmentValues.remove(attachmentValue);
                                            if (ecoAttachPropertyIds.length > 0) {
                                                angular.forEach(ecoAttachPropertyIds, function (ecoAttachId) {
                                                    if (attribute.value.attachmentValues.indexOf(ecoAttachId) == -1) {
                                                        attribute.value.attachmentValues.push(ecoAttachId);
                                                    }
                                                });
                                                ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                                    function (data) {
                                                        $rootScope.hideBusyIndicator();
                                                        if (attribute.attachmentValues.length == 0) {
                                                            loadObjectPropertyValues();
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

                    function deleteAttachments(attribute, attachment) {
                        $rootScope.showBusyIndicator($('.view-content'));
                        var attachments = attribute.value.attachmentValues;
                        attribute.value.attachmentValues = [];
                        angular.forEach(attachments, function (attach) {
                            if (attach.id != attachment.id) {
                                attribute.value.attachmentValues.push(attach.id);
                            }
                        });


                        ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                            function (data) {
                                AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                    function (data) {
                                        loadObjectPropertyValues();
                                        /*$rootScope.hideBusyIndicator();*/
                                        $rootScope.showSuccessMessage(attachment.name + ' ' + $rootScope.deleteMessage);
                                    },
                                    function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )

                    }

                    /*----------  To Download Attachment File  -----------------------*/

                    function openPropertyAttachment(attachment) {
                        var url = "{0}//{1}/api/col/attachments/{2}/download".
                            format(window.location.protocol, window.location.host,
                            attachment.id);
                        $rootScope.downloadFileFromIframe(url);
                    }

                    /*----------------  To Save IMAGE Property Value  ---------------------*/

                    function saveImage(attribute) {
                        if (attribute.newImageValue != null) {
                            $rootScope.showBusyIndicator($('#rightSidePanel'));
                            attribute.imageValue = attribute.newImageValue;
                            ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                function (data) {
                                    ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                        function (data) {
                                            attribute.changeImage = false;
                                            attribute.newImageValue = null;
                                            attribute.value.ecoImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                            loadObjectPropertyValues();
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage($rootScope.imageMessage);
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    /*-------------  To Display Large Image  ------------------------*/

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

                    /*-----------------  To save TIME & TIMESTAMP Attribute values  --------------*/

                    function saveTimeProperty(attribute) {
                        if (attribute.timeValue != null) {
                            /* attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");*/
                            attribute.value.timeValue = attribute.timeValue;

                            ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                function (data) {
                                    attribute.showTimeAttribute = false;
                                    $rootScope.showSuccessMessage($rootScope.timeAttributeMessage);
                                    loadObjectPropertyValues();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )

                        }
                        if (attribute.timestampValue != null) {
                            attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                            attribute.value.timestampValue = attribute.timestampValue;

                            ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                function (data) {
                                    attribute.showTimestamp = false;
                                    $rootScope.showSuccessMessage($rootScope.timestampAttributeMessage);
                                    loadObjectPropertyValues();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )

                        }
                    }

                    /*------------- It goes to the specified Details Page -------------------*/

                    function showRefValueDetails(attribute) {
                        if (attribute.value.refValue.objectType == 'ITEM') {
                            $state.go('app.items.details', {itemId: attribute.value.refValue.latestRevision});
                        } else if (attribute.value.refValue.objectType == 'ITEMREVISION') {
                            $state.go('app.items.details', {itemId: attribute.value.refValue.id});
                        } else if (attribute.value.refValue.objectType == 'CHANGE') {
                            if (attribute.value.refValue.changeType == 'ECO') {
                                $state.go('app.changes.eco.details', {ecoId: attribute.value.refValue.id});
                            } else if (attribute.value.refValue.changeType == 'DCO') {
                                $state.go('app.changes.dco.details', {dcoId: attribute.value.refValue.id});
                            } else if (attribute.value.refValue.changeType == 'ECR') {
                                $state.go('app.changes.ecr.details', {ecrId: attribute.value.refValue.id});
                            } else if (attribute.value.refValue.changeType == 'DCR') {
                                $state.go('app.changes.dcr.details', {dcrId: attribute.value.refValue.id});
                            } else if (attribute.value.refValue.changeType == 'DEVIATION' || attribute.value.refValue.changeType == 'WAIVER') {
                                $state.go('app.changes.variance.details', {varianceId: attribute.value.refValue.id});
                            }
                        } else if (attribute.value.refValue.objectType == 'OEMPARTMCO' || attribute.value.refValue.objectType == 'ITEMMCO') {
                            $state.go('app.changes.mco.details', {mcoId: attribute.value.refValue.id});
                        } else if (attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION') {
                            $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.value.refValue.id});
                        } else if (attribute.value.refValue.objectType == 'MANUFACTURER') {
                            $state.go('app.mfr.details', {manufacturerId: attribute.value.refValue.id});
                        } else if (attribute.value.refValue.objectType == 'MANUFACTURERPART') {
                            $state.go('app.mfr.mfrparts.details', {
                                mfrId: attribute.value.refValue.manufacturer,
                                manufacturePartId: attribute.value.refValue.id
                            });
                        } else if (attribute.value.refValue.objectType == 'REQUIREMENT') {
                            $state.go('app.req.requirements.details', {requirementId: attribute.value.refValue.latestVersion});
                        } else if (attribute.value.refValue.objectType == 'REQUIREMENTDOCUMENT') {
                            $state.go('app.req.document.details', {
                                reqId: attribute.value.refValue.latestRevision,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'PLANT') {
                            $state.go('app.mes.masterData.plant.details', {
                                plantId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'ASSEMBLYLINE') {
                            $state.go('app.mes.masterData.assemblyline.details', {
                                assemblyLineId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'WORKCENTER') {
                            $state.go('app.mes.masterData.workcenter.details', {
                                workcenterId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'MACHINE') {
                            $state.go('app.mes.masterData.machine.details', {
                                machineId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'EQUIPMENT') {
                            $state.go('app.mes.masterData.equipment.details', {
                                equipmentId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'INSTRUMENT') {
                            $state.go('app.mes.masterData.instrument.details', {
                                instrumentId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'TOOL') {
                            $state.go('app.mes.masterData.tool.details', {
                                toolId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'JIGFIXTURE') {
                            $state.go('app.mes.jigsAndFixture.details', {
                                jigsAndFixtureId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'MATERIAL') {
                            $state.go('app.mes.masterData.material.details', {
                                materialId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'MANPOWER') {
                            $state.go('app.mes.masterData.manpower.details', {
                                manpowerId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'OPERATION') {
                            $state.go('app.mes.masterData.operation.details', {
                                operationId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'PRODUCTIONORDER') {
                            $state.go('app.mes.productionOrder.details', {
                                productionOrderId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.value.refValue.objectType == 'MROASSET') {
                            $state.go('app.mro.asset.details', {
                                assetId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'MROMETER') {
                            $state.go('app.mro.meter.details', {
                                meterId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'MROSPAREPART') {
                            $state.go('app.mro.sparePart.details', {
                                sparePartId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'MROWORKREQUEST') {
                            $state.go('app.mro.workOrder.details', {
                                productionOrderId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'MROWORKORDER') {
                            $state.go('app.mro.workRequest.details', {
                                workRequestId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'CUSTOMOBJECT') {
                            $state.go('app.customobjects.details', {
                                customId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'PRODUCTINSPECTIONPLAN' || attribute.value.refValue.objectType == 'MATERIALINSPECTIONPLAN') {
                            $state.go('app.pqm.inspectionPlan.details', {
                                planId: attribute.value.refValue.latestRevision,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'PROBLEMREPORT') {
                            $state.go("app.pqm.pr.details", {
                                problemReportId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'NCR') {
                            $state.go("app.pqm.ncr.details", {
                                ncrId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.value.refValue.objectType == 'QCR') {
                            $state.go("app.pqm.qcr.details", {
                                qcrId: attribute.value.refValue.id,
                                tab: 'details.basic'
                            });
                        }
                    }

                    /*------------- To save OBJECT Property values  -------------------*/

                    var attributeUpdateMessage = parsed.html($translate.instant("ATTRIBUTE_UPDATE_MESSAGE")).html();

                    function saveObject(attribute) {
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        attribute.value.refValue = attribute.value.refValue.id;
                        if (attribute.value.id == undefined || attribute.value.id == null) {
                            ObjectAttributeService.createObjectAttribute($scope.objectId, attribute.value).then(
                                function (data) {
                                    attribute.editMode = false;
                                    loadObjectPropertyValues();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(attributeUpdateMessage);
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        else {
                            ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                function (data) {
                                    attribute.editMode = false;
                                    loadObjectPropertyValues();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(attributeUpdateMessage);
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )

                        }
                    }

                    /*------------ RichText Methods ----------------*/
                    $scope.editRichText = editRichText;
                    $scope.cancelRichText = cancelRichText;
                    $scope.saveRichText = saveRichText;
                    $scope.cancelChanges = cancelChanges;

                    function editRichText(attribute) {
                        $(".note-editor").show();
                        attribute.oldRichTextValue = attribute.value.richTextValue;
                        attribute.editMode = true;
                    }

                    function saveRichText(attribute) {
                        attribute.editMode = false;
                        if (attribute.value.richTextValue != null) {
                            ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                                    attribute.editMode = false;
                                    $(".note-editor").hide();
                                    loadObjectPropertyValues();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    };

                    function cancelRichText(attribute) {
                        attribute.value.richTextValue = attribute.oldRichTextValue;
                        attribute.editMode = false;
                        $(".note-editor").hide();
                    }

                    $scope.changeAttribute = changeAttribute;
                    function changeAttribute(attribute) {
                        $scope.stringValue = null;
                        $scope.integerValue = null;
                        $scope.dateValue = null;
                        $scope.doubleValue = null;
                        $scope.booleanValue = null;
                        $scope.currencyValue = null;
                        $scope.textValue = null;
                        $scope.longTextValue = null;
                        $scope.hyperLinkValue = null;
                        $scope.listValue = null;
                        $scope.mlistValue = [];
                        attribute.editMode = true;
                        $scope.stringValue = attribute.value.stringValue;
                        $scope.integerValue = attribute.value.integerValue;
                        $scope.dateValue = attribute.value.dateValue;
                        $scope.doubleValue = attribute.value.doubleValue;
                        $scope.booleanValue = attribute.value.booleanValue;
                        $scope.currencyValue = attribute.value.currencyValue;
                        $scope.listValue = attribute.value.listValue;
                        $scope.mlistValue = attribute.value.mlistValue;
                        $scope.textValue = attribute.value.textValue;
                        $scope.longTextValue = attribute.value.longTextValue;
                        $scope.hyperLinkValue = attribute.value.hyperLinkValue;
                        $scope.attributeMeasurementUnit = attribute.value.measurementUnit;

                    }

                    function cancelChanges(attribute) {
                        attribute.editMode = false;
                        attribute.value.refValue = $scope.previousValue;
                        attribute.changeCurrency = false;
                        attribute.value.stringValue = $scope.stringValue;
                        attribute.value.integerValue = $scope.integerValue;
                        attribute.value.dateValue = $scope.dateValue;
                        attribute.value.doubleValue = $scope.doubleValue;
                        attribute.value.booleanValue = $scope.booleanValue;
                        attribute.value.currencyValue = $scope.currencyValue;
                        attribute.value.listValue = $scope.listValue;
                        attribute.value.mlistValue = $scope.mlistValue;
                        attribute.value.longTextValue = $scope.longTextValue;
                        attribute.value.hyperLinkValue = $scope.hyperLinkValue;
                    }

                    $scope.editListValue = editListValue;
                    function editListValue(attribute) {
                        attribute.editMode = true;
                        $scope.listValue = attribute.value.listValue;
                        $scope.mlistValue = attribute.value.mlistValue;
                    }

                    $scope.cancelListValue = cancelListValue;
                    function cancelListValue(attribute) {
                        attribute.value.listValue = $scope.listValue;
                        attribute.editMode = false;
                    }

                    function loadObjectPropertyAttributes() {
                        $scope.objectProperties = [];
                        $scope.groupAttributes = [];
                        $scope.propertiesList = [];
                        var groupAttributeMap = new Hashtable();
                        ObjectTypeAttributeService.getObjectTypeAttributesByType($scope.objectType).then(
                            function (data) {
                                angular.forEach(data, function (attribute) {
                                    if (attribute.visible) {
                                        var att = {
                                            id: {
                                                objectId: $scope.objectId,
                                                attributeDef: attribute.id
                                            },
                                            attributeDef: attribute,
                                            value: {
                                                id: {
                                                    objectId: $scope.objectId,
                                                    attributeDef: attribute.id
                                                },
                                                stringValue: null,
                                                integerValue: null,
                                                doubleValue: null,
                                                booleanValue: null,
                                                dateValue: null,
                                                longTextValue: null,
                                                formulaValue: null,
                                                richTextValue: null,
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
                                                currencyType: null,
                                                hyperLinkValue: null
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
                                            changeCurrency: false
                                        };

                                        if(attribute.plugin == false) {
                                            if (attribute.attributeGroup != null && attribute.attributeGroup != "") {
                                                var groupAttribute = groupAttributeMap.get(attribute.attributeGroup);
                                                if (groupAttribute == null) {
                                                    var groupAttributes = {
                                                        groupName: attribute.attributeGroup,
                                                        objectProperties: [att]
                                                    }
                                                    groupAttributeMap.put(attribute.attributeGroup, groupAttributes);
                                                } else {
                                                    groupAttribute.objectProperties.push(att);
                                                    groupAttributeMap.put(attribute.attributeGroup, groupAttribute)

                                                }
                                            } else {
                                                $scope.objectProperties.push(att);
                                            }
                                            $scope.propertiesList.push(att);
                                        }
                                    }
                                });
                                $scope.groupAttributes = groupAttributeMap.values();
                                if ($scope.objectId != undefined) loadObjectPropertyValues();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    /*-------------------------  To get Object Properties Values by 'id'  -----------------*/

                    function loadObjectPropertyValues() {
                        ObjectAttributeService.getAllObjectAttributes($scope.objectId).then(
                            function (data) {
                                var map = new Hashtable();

                                angular.forEach(data, function (attribute) {
                                    map.put(attribute.id.attributeDef, attribute);
                                });

                                angular.forEach($scope.propertiesList, function (attribute) {
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
                                                        $scope.ecoPropertyAttachments = data;
                                                        attribute.value.attachmentValues = $scope.ecoPropertyAttachments;
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
                                                            ItemService.getRevisionId(attribute.value.refValue.latestRevision).then(
                                                                function (revisionValue) {
                                                                    attribute.value.refValue.itemRevision = revisionValue;
                                                                }, function (error) {
                                                                    //$rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
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
                                                                    //$rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'CHANGE') {
                                                    ECOService.getChangeObject(value.refValue).then(
                                                        function (changeValue) {
                                                            attribute.value.refValue = changeValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'WORKFLOW') {
                                                    WorkflowDefinitionService.getWorkflowDefinition(value.refValue).then(
                                                        function (workflowValue) {
                                                            attribute.value.refValue = workflowValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'MANUFACTURER') {
                                                    MfrService.getManufacturer(value.refValue).then(
                                                        function (mfrValue) {
                                                            attribute.value.refValue = mfrValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'MANUFACTURERPART') {
                                                    MfrPartsService.getManufacturepart(value.refValue).then(
                                                        function (mfrPartValue) {
                                                            attribute.value.refValue = mfrPartValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'PERSON') {
                                                    CommonService.getPerson(value.refValue).then(
                                                        function (person) {
                                                            attribute.value.refValue = person;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'PROJECT') {
                                                    ProjectService.getProject(value.refValue).then(
                                                        function (person) {
                                                            attribute.value.refValue = person;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'REQUIREMENT') {
                                                    RequirementService.getRequirement(value.refValue).then(
                                                        function (reqValue) {
                                                            attribute.value.refValue = reqValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'REQUIREMENTDOCUMENT') {
                                                    ReqDocumentService.getReqDocument(value.refValue).then(
                                                        function (reqValue) {
                                                            attribute.value.refValue = reqValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
                                                else if (attribute.attributeDef.refType == 'MESOBJECT') {
                                                    MESObjectTypeService.getMESObject(value.refValue).then(
                                                        function (reqValue) {
                                                            attribute.value.refValue = reqValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'MROOBJECT') {
                                                    MESObjectTypeService.getMROObject(value.refValue).then(
                                                        function (reqValue) {
                                                            attribute.value.refValue = reqValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'CUSTOMOBJECT') {
                                                    CustomObjectService.getCustomObject(value.refValue).then(
                                                        function (reqValue) {
                                                            attribute.value.refValue = reqValue;
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'QUALITY') {
                                                    WorkflowDefinitionService.getWorkflowAssignableTypeObjectType(value.refValue).then(
                                                        function (reqValue) {
                                                            if (reqValue.objectType == "PRODUCTINSPECTIONPLAN" || reqValue.objectType == "MATERIALINSPECTIONPLAN") {
                                                                InspectionPlanService.getInspectionPlan(value.refValue).then(
                                                                    function (data) {
                                                                        attribute.value.refValue = data;
                                                                    }
                                                                )
                                                            } else if (reqValue.objectType == "PROBLEMREPORT") {
                                                                ProblemReportService.getProblemReport(value.refValue).then(
                                                                    function (data) {
                                                                        attribute.value.refValue = data;
                                                                    }
                                                                )
                                                            } else if (reqValue.objectType == "NCR") {
                                                                NcrService.getNcr(value.refValue).then(
                                                                    function (data) {
                                                                        attribute.value.refValue = data;
                                                                    }
                                                                )
                                                            } else if (reqValue.objectType == "QCR") {
                                                                QcrService.getQcr(value.refValue).then(
                                                                    function (data) {
                                                                        attribute.value.refValue = data;
                                                                    }
                                                                )
                                                            }
                                                        }, function (error) {
                                                            //$rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
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
                                            attribute.value.formulaValue = value.formulaValue;
                                            if (value.doubleValue != null) {

                                                if (attribute.attributeDef.measurement != null && value.measurementUnit != null) {
                                                    var measurement = attribute.attributeDef.measurement;
                                                    attribute.value.measurementUnit = value.measurementUnit;
                                                    var measurementUnits = measurement.measurementUnits;
                                                    var baseUnit = null;
                                                    var attributeUnit = null;
                                                    angular.forEach(measurementUnits, function (unit) {
                                                        if (unit.baseUnit) {
                                                            baseUnit = unit;
                                                        }
                                                        if (value.measurementUnit.id == unit.id) {
                                                            attributeUnit = unit;
                                                        }
                                                    })

                                                    var baseUnitIndex = measurementUnits.indexOf(baseUnit);
                                                    var attributeIndex = measurementUnits.indexOf(attributeUnit);

                                                    if (attributeIndex != baseUnitIndex) {
                                                        attribute.value.doubleValue = value.doubleValue * attributeUnit.conversionFactor;
                                                    } else {
                                                        attribute.value.doubleValue = value.doubleValue;
                                                    }
                                                } else {
                                                    attribute.value.doubleValue = parseFloat(value.doubleValue).toFixed(5);
                                                }
                                            } else {
                                                attribute.value.doubleValue = value.doubleValue;
                                            }
                                            attribute.value.booleanValue = value.booleanValue;
                                            attribute.value.hyperLinkValue = value.hyperLinkValue;
                                            attribute.value.dateValue = value.dateValue;
                                            attribute.value.integerValue = value.integerValue;
                                            attribute.value.formulaValue = value.formulaValue;
                                            attribute.value.timeValue = value.timeValue;
                                            attribute.value.timestampValue = value.timestampValue;
                                            attribute.value.imageValue = value.imageValue;
                                            attribute.value.currencyValue = value.currencyValue;
                                            attribute.value.longTextValue = value.longTextValue;
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
                                            attribute.value.ecoImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                        }
                                        else {
                                            if (attribute.attributeDef.dataType == "LIST") {
                                                if (attribute.value.listValue == null && attribute.attributeDef.listMultiple == false) {
                                                    attribute.value.listValue = attribute.attributeDef.defaultListValue;
                                                    attribute.defaultListValue = false;
                                                    attribute.value.deleteFlag = true;
                                                }
                                                if (attribute.value.mlistValue != null && attribute.value.mlistValue.length == 0 && attribute.attributeDef.listMultiple == true) {
                                                    attribute.value.mlistValue.push(attribute.attributeDef.defaultListValue);
                                                }
                                            }
                                            if (attribute.value.stringValue == null) {
                                                attribute.value.stringValue = attribute.attributeDef.defaultTextValue;
                                            }
                                            if (attribute.attributeDef.dataType == "BOOLEAN") {
                                                attribute.value.booleanValue = false;
                                            }
                                        }

                                    }
                                });
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $scope.showThumbnailImage = showThumbnailImage;
                    function showThumbnailImage(attribute) {
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

                    $scope.checkValue = checkValue;
                    function checkValue(attribute, value) {
                        if (attribute.value.mlistValue.indexOf(value) == -1) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    $scope.selectMultiListCheckBox = selectMultiListCheckBox;
                    function selectMultiListCheckBox(attribute, value) {
                        if (attribute.value.mlistValue.indexOf(value) == -1) {
                            attribute.value.mlistValue.push(value);
                        } else {
                            attribute.value.mlistValue.splice(attribute.value.mlistValue.indexOf(value), 1);
                        }
                    }

                    $scope.checkForHideAttribute = checkForHideAttribute;
                    function checkForHideAttribute(attribute) {
                        var hide = false;
                        if (attribute.attributeDef.objectType == "CUSTOMOBJECT" && attribute.attributeDef.name == "Albonair Internal Audit") {
                            hide = true;
                        }
                        return hide;
                    }

                    (function () {
                        angular.forEach($application.currencies, function (data) {
                            currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                        })
                        loadObjectPropertyAttributes();
                    })();
                }
            }
        }
    }
);
