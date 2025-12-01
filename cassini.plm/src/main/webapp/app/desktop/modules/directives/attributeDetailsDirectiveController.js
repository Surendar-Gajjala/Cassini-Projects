define(
    [
        'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/ecoService'
    ],

    function (module) {
        module.directive('attributeDetailsView', AttributeDetailsViewDirective);

        function AttributeDetailsViewDirective($rootScope, $compile, $sce, $timeout, $application, $translate, ECOService,
                                               DialogService, ObjectAttributeService, AttributeAttachmentService, ObjectTypeAttributeService,
                                               ItemService, WorkflowDefinitionService, MfrService, $window, CommonService,
                                               MfrPartsService, SpecificationsService, RequirementService) {
            return {
                templateUrl: 'app/desktop/modules/directives/attributeDetailsViewDirective.jsp',
                restrict: 'E',
                scope: {
                    'objectType': '@',
                    'objectId': '=',
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
                    $scope.showObjectValues = showObjectValues;
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
                            }, function (error) {
                                  $rootScope.showErrorMessage(error.message);
                                  $rootScope.hideBusyIndicator();
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

                    /*---- To Save STRING, INTEGER, BOOLEAN, DATE and DOUBLE Values ------*/

                    function saveObjectProperties(attribute) {
                        if (attribute.attributeDef.objectType == 'FILE') {
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
                                                $rootScope.showBusyIndicator($(".view-content"));
                                                AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'FILE', attachmentValue).then(
                                                    function (data) {
                                                        ecoAttachPropertyIds.push(data[0].id);
                                                        attribute.attachmentValues.remove(attachmentValue);
                                                        if (ecoAttachPropertyIds.length > 0) {
                                                            angular.forEach(ecoAttachPropertyIds, function (revAttachId) {
                                                                attribute.value.attachmentValues.push(revAttachId);
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
                                    $rootScope.showBusyIndicator($(".view-content"));
                                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'FILE', attachmentValue).then(
                                        function (data) {
                                            ecoAttachPropertyIds.push(data[0].id);
                                            attribute.attachmentValues.remove(attachmentValue);
                                            if (ecoAttachPropertyIds.length > 0) {
                                                angular.forEach(ecoAttachPropertyIds, function (ecoAttachId) {
                                                    attribute.value.attachmentValues.push(ecoAttachId);
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

                    function deleteAttachments(attribute, attachment) {
                        /*$rootScope.showBusyIndicator($('.view-content'));*/
                        var attachments = attribute.value.attachmentValues;
                        attribute.value.attachmentValues = [];
                        angular.forEach(attachments, function (attach) {
                            if (attach.id != attachment.id) {
                                attribute.value.attachmentValues.push(attach.id);
                            }
                        });

                        if (attribute.attributeDef.objectType == 'FILE') {
                            ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                function (data) {
                                    AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                        function (data) {
                                            loadObjectPropertyValues();
                                            /*$rootScope.hideBusyIndicator();*/
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
                            $rootScope.showBusyIndicator($(".view-content"));
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
                            if (attribute.attributeDef.objectType == 'FILE') {
                                ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                    function (data) {
                                        attribute.showTimeAttribute = false;
                                        $rootScope.showSuccessMessage($rootScope.timeAttributeMessage);
                                        loadObjectPropertyValues();
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
                            if (attribute.attributeDef.objectType == 'FILE') {
                                ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                    function (data) {
                                        attribute.showTimestamp = false;
                                        $rootScope.showSuccessMessage($rootScope.timestampAttributeMessage);
                                        loadObjectPropertyValues();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    }

                    /*------------- It goes to the specified Details Page -------------------*/

                    function showRefValueDetails(attribute) {
                        if (attribute.value.refValue.objectType == 'ITEM') {
                            $state.go('app.items.details', {itemId: attribute.value.refValue.latestRevision});
                        } else if (attribute.value.refValue.objectType == 'ITEMREVISION') {
                            $state.go('app.items.details', {itemId: attribute.value.refValue.id});
                        } else if (attribute.value.refValue.objectType == 'CHANGE') {
                            $state.go('app.changes.ecos.details', {objectId: attribute.value.refValue.id});
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
                        }
                    }

                    /*---------  To get specified Type of selection view  --------*/

                    $scope.previousValue = null;
                    function showObjectValues(attribute) {
                        var options = null;
                        attribute.editMode = true;
                        $scope.previousValue = attribute.value.refValue;
                        if (attribute.attributeDef.refType == 'MANUFACTURER') {

                            options = {
                                title: $rootScope.selectMfrTitle,
                                template: 'app/desktop/modules/select/mfr/mfrSelectionView.jsp',
                                controller: 'MfrSelectionController as mfrSelectVm',
                                resolve: 'app/desktop/modules/select/mfr/mfrSelectionController',
                                width: 600,
                                side: 'left',
                                showMask: true,
                                buttons: [
                                    {text: $rootScope.buttonSelect, broadcast: 'app.select.mfr'}
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
                                title: $rootScope.selectMfrPartTitle,
                                template: 'app/desktop/modules/select/mfrParts/mfrPartSelectionView.jsp',
                                controller: 'MfrPartSelectionController as mfrPartSelectVm',
                                resolve: 'app/desktop/modules/select/mfrParts/mfrPartSelectionController.js',
                                width: 600,
                                side: 'left',
                                showMask: true,
                                buttons: [
                                    {text: $rootScope.buttonSelect, broadcast: 'app.select.mfrPart'}
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
                                title: $rootScope.selectEcoTitle,
                                template: 'app/desktop/modules/select/changes/changeSelectionView.jsp',
                                controller: 'ChangeSelectionController as changeSelectVm',
                                resolve: 'app/desktop/modules/select/changes/changeSelectionController',
                                width: 600,
                                side: 'left',
                                showMask: true,
                                buttons: [
                                    {text: $rootScope.buttonSelect, broadcast: 'app.select.change'}
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
                                title: $rootScope.selectItemTitle,
                                template: 'app/desktop/modules/select/item/itemSelectionView.jsp',
                                controller: 'ItemSelectionController as itemSelectVm',
                                resolve: 'app/desktop/modules/select/item/itemSelectionController',
                                width: 600,
                                side: 'left',
                                showMask: true,
                                buttons: [
                                    {text: $rootScope.buttonSelect, broadcast: 'app.select.item'}
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
                                title: $rootScope.selectWorkflowTitle,
                                template: 'app/desktop/modules/select/workflow/workflowSelectionView.jsp',
                                controller: 'WorkflowSelectionController as wrkSelectVm',
                                resolve: 'app/desktop/modules/select/workflow/workflowSelectionController',
                                width: 600,
                                side: 'left',
                                showMask: true,
                                buttons: [
                                    {text: $rootScope.buttonSelect, broadcast: 'app.select.workflow'}
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
                                title: $rootScope.selectItemRevisionTitle,
                                template: 'app/desktop/modules/select/itemRevision/itemRevisionSelection.jsp',
                                controller: 'ItemRevisionSelectionController as itemRevSelectVm',
                                resolve: 'app/desktop/modules/select/itemRevision/itemRevisionSelectionController',
                                width: 600,
                                side: 'left',
                                showMask: true,
                                buttons: [
                                    {text: $rootScope.buttonSelect, broadcast: 'app.select.itemRevision'}
                                ],
                                callback: function (result) {
                                    attribute.refValue = attribute.value.refValue;
                                    attribute.value.refValue = result;
                                    if (attribute.value.refValue != null) {
                                        $rootScope.showInfoMessage(result.itemNumber + ' : ' + $rootScope.objectItemRevisionInfo)
                                    }
                                }
                            };
                            $rootScope.showSidePanel(options);

                        }
                        else if (attribute.attributeDef.refType == 'PERSON') {
                            options = {
                                title: $rootScope.selectPersonTitle,
                                template: 'app/desktop/modules/select/person/personSelectionView.jsp',
                                controller: 'PersonSelectionController as personSelectVm',
                                resolve: 'app/desktop/modules/select/person/personSelectionController',
                                width: 600,
                                showMask: true,
                                side: 'left',
                                buttons: [
                                    {text: $rootScope.buttonSelect, broadcast: 'app.select.person'}
                                ],
                                callback: function (result) {
                                    attribute.refValue = attribute.value.refValue;
                                    attribute.value.refValue = result;
                                }
                            };
                            $rootScope.showSidePanel(options);

                        }
                    }

                    /*------------- To save OBJECT Property values  -------------------*/

                    var attributeUpdateMessage = parsed.html($translate.instant("ATTRIBUTE_UPDATE_MESSAGE")).html();

                    function saveObject(attribute) {
                        $rootScope.showBusyIndicator($(".view-content"));
                        attribute.value.refValue = attribute.value.refValue.id;
                        if (attribute.value.id == undefined || attribute.value.id == null) {
                            if (attribute.attributeDef.objectType == 'FILE') {
                                ObjectAttributeService.createObjectAttribute($scope.objectId, attribute.value).then(
                                    function (data) {
                                        attribute.editMode = false;
                                        loadObjectPropertyValues();
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage(attributeUpdateMessage);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                        else {
                            if (attribute.attributeDef.objectType == 'FILE') {
                                ObjectAttributeService.updateObjectAttribute($scope.objectId, attribute.value).then(
                                    function (data) {
                                        attribute.editMode = false;
                                        loadObjectPropertyValues();
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage(attributeUpdateMessage);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    }

                    /*------------ RichText Methods ----------------*/
                    $scope.editRichText = editRichText;
                    $scope.cancelRichText = cancelRichText;
                    $scope.saveRichText = saveRichText;
                    $scope.cancelChanges = cancelChanges;

                    function editRichText(attribute) {
                        $(".note-editor").show();
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
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    };

                    function cancelRichText(attribute) {
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
                        attribute.editMode = true;
                        $scope.stringValue = attribute.value.stringValue;
                        $scope.integerValue = attribute.value.integerValue;
                        $scope.dateValue = attribute.value.dateValue;
                        $scope.doubleValue = attribute.value.doubleValue;
                        $scope.booleanValue = attribute.value.booleanValue;
                        $scope.currencyValue = attribute.value.currencyValue;
                        $scope.listValue = attribute.value.listValue;
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
                                        $scope.objectProperties.push(att);
                                    }
                                })
                                loadObjectPropertyValues();
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

                                angular.forEach($scope.objectProperties, function (attribute) {
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
                                                                    $rootScope.showErrorMessage(error.message);
                                                                 }
                                                            )
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                         }
                                                    )
                                                } else if (attribute.attributeDef.refType == 'ITEMREVISION') {
                                                    ItemService.getItemRevision(value.refValue).then(
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
                                                } else if (attribute.attributeDef.refType == 'REQUIREMENT') {
                                                    RequirementService.getRequirement(value.refValue).then(
                                                        function (reqValue) {
                                                            attribute.value.refValue = reqValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
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
                                            if (value.doubleValue != null) {
                                                attribute.value.doubleValue = parseFloat(value.doubleValue).toFixed(5);
                                            } else {
                                                attribute.value.doubleValue = value.doubleValue;
                                            }
                                            attribute.value.booleanValue = value.booleanValue;
                                            attribute.value.hyperLinkValue = value.hyperLinkValue;
                                            attribute.value.dateValue = value.dateValue;
                                            /*attribute.value.listValue = value.listValue;
                                             attribute.value.listValue = value.listValue;
                                             attribute.value.mlistValue = value.mlistValue;*/
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
