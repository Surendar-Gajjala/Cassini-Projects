/**
 * Created by Rajabrahmachary on 13-07-2017.
 */
define(
    [
        'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],

    function (module) {
        module.directive('attributesDetailsView', AttributesDetailsViewDirective);

        function AttributesDetailsViewDirective($rootScope, $compile, $sce, $timeout, $application,
                                                DialogService, ObjectAttributeService, AttributeAttachmentService, ObjectTypeAttributeService) {
            return {
                templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirective.jsp',
                restrict: 'E',
                scope: {
                    'attributeType': '@',
                    'attributeId': '=',
                    'updateObjectAttribute': '=',
                    'hasPermission': '='
                },
                link: function ($scope, $elem, attrs) {

                    /* declaring variables & functions */

                    $scope.objectTypeAttributes = [];
                    $scope.updateObjectAttribute = updateObjectAttribute;
                    $scope.updateToEditMode = updateToEditMode;
                    $scope.cancelChanges = cancelChanges;
                    $scope.changeTime = changeTime;
                    $scope.cancelTime = cancelTime;
                    $scope.cancelCurrencyChanges = cancelCurrencyChanges;
                    $scope.changeCurrencyValue = changeCurrencyValue;
                    $scope.showImage = showImage;
                    $scope.change = change;
                    $scope.cancel = cancel;
                    $scope.currencies = [];
                    $scope.currencyMap = new Hashtable();
                    $scope.openAttachment = openAttachment;
                    $scope.deleteAttachments = deleteAttachments;
                    $scope.addAttachment = addAttachment;
                    $scope.cancelAttachment = cancelAttachment;
                    $scope.saveAttachments = saveAttachments;
                    $scope.saveBooleanProperties = saveBooleanProperties;
                    $scope.saveTimeProperty = saveTimeProperty;
                    $scope.changeTimestamp = changeTimestamp;
                    $scope.clearBrowse = true;

                    $scope.showSelectionDialog = showSelectionDialog;


                    /* TimeStamp Attributes*/

                    function changeTimestamp(attribute) {
                        attribute.showTimestamp = true;
                    }

                    /* for attachment attributes*/

                    function openAttachment(attachment) {
                        var url = "{0}//{1}/api/col/attachments/{2}/download".
                            format(window.location.protocol, window.location.host,
                            attachment.id);
                        launchUrl(url);
                    }

                    function deleteAttachments(attribute, attachment) {
                        var options = {
                            title: 'Delete attachment',
                            message: 'Are you sure you want to delete this attachment',
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                var index = attribute.attributeValue.attachmentValues.indexOf(attachment);
                                if (index != -1) {
                                    AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                        function (data) {
                                            attribute.attributeValue.attachmentValues.splice(index, 1);
                                            ObjectAttributeService.updateObjectAttribute($scope.attributeId, attribute.attributeValue.attachmentValues).then(
                                                function (data) {
                                                }
                                            )

                                        }
                                    )

                                }
                            }
                        });
                    }

                    function addAttachment(attribute) {
                        document.getElementById("file").value = null;
                        attribute.showAttachment = true;
                    }

                    function cancelAttachment(attribute) {
                        document.getElementById("file").value = null;
                        attribute.showAttachment = false;
                        attribute.attachmentValues = [];
                    }

                    /*  for image attributes*/

                    function cancel(attribute) {
                        attribute.changeImage = false;
                    }

                    function change(attribute) {
                        attribute.changeImage = true;
                    }

                    function showImage(attribute) {
                        var modal = document.getElementById('myModal2');
                        var modalImg = document.getElementById("img03");

                        modal.style.display = "block";
                        modalImg.src = "api/core/objects/" + $scope.attributeId + "/attributes/" + attribute.id + "/imageAttribute/download?" + new Date().getTime();

                        var span = document.getElementsByClassName("closeImage")[0];

                        span.onclick = function () {
                            modal.style.display = "none";
                        }
                    }

                    /* methods for time attribute*/

                    function changeTime(attribute) {
                        attribute.showTimeAttribute = true;
                    }

                    function cancelTime(attribute) {
                        attribute.showTimeAttribute = false;
                        attribute.showTimestamp = false;
                    }

                    function saveTimeProperty(attribute) {
                        if (attribute.editTimeValue != null && attribute.dataType == 'TIME') {
                            attribute.attributeValue.timeValue = attribute.editTimeValue;
                            ObjectAttributeService.updateObjectAttribute($scope.attributeId, attribute.attributeValue).then(
                                function (data) {
                                    attribute.showTimeAttribute = false;
                                }
                            )

                        }
                        else if (attribute.timestampValue != null) {
                            attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                            attribute.attributeValue.timestampValue = attribute.timestampValue;
                            ObjectAttributeService.updateObjectAttribute($scope.attributeId, attribute.attributeValue).then(
                                function (data) {
                                    attribute.showTimestamp = false;
                                }
                            )

                        }
                    }

                    /* flags array for boolean dataType */

                    $scope.flags = [{name: "True", value: true}, {name: "False", value: false}];

                    function saveBooleanProperties(attribute) {
                        ObjectAttributeService.updateObjectAttribute($scope.attributeId, attribute.attributeValue).then(
                            function (data) {
                            }
                        )
                    }


                    /* changing the attribute into edit mode*/

                    function updateToEditMode(attribute) {
                        attribute.actualDate = null;
                        attribute.actualDate = attribute.attributeValue.dateValue;
                        attribute.editMode = true;
                        attribute.dateEdited = true;
                    }

                    function cancelChanges(attribute) {
                        attribute.editMode = false;
                        attribute.attributeValue.dateValue = attribute.actualDate;
                    }

                    /* for getting objectTypes And its attributes*/

                    function updateObjectAttribute(attribute) {
                        if (attribute.dataType == 'IMAGE') {
                            if (attribute.newImageValue != null) {
                                attribute.imageValue = attribute.newImageValue;
                                ObjectAttributeService.uploadObjectAttributeImage($scope.attributeId, attribute.id, attribute.imageValue).then(
                                    function (data) {
                                        $scope.clearBrowse = true;
                                        attribute.newImageValue = null;
                                        loadObjectAttributeTypes();
                                    }
                                )
                            }
                        } else {
                            ObjectAttributeService.updateObjectAttribute($scope.attributeId, attribute.attributeValue).then(
                                function () {
                                    attribute.changeCurrency = false;
                                    attribute.editMode = false;
                                    attribute.listValueEditMode = false;
                                });

                        }
                        loadObjectAttributeTypes();
                    }

                    /*for loading currencies*/


                    function cancelCurrencyChanges(attribute) {
                        attribute.editMode = false;
                        attribute.attributeValue.refValue = attribute.refValue;
                        attribute.changeCurrency = false;
                        attribute.attributeValue.currencyValue = attribute.actualCurrencyValue;
                    }

                    function changeCurrencyValue(attribute) {
                        attribute.changeCurrency = true;
                        attribute.actualCurrencyValue = null;
                        attribute.actualCurrencyValue = attribute.attributeValue.currencyValue;
                    }

                    function loadCurrencies() {
                        ObjectAttributeService.getCurrencies().then(
                            function (data) {
                                $scope.currencies = data;
                                angular.forEach($scope.currencies, function (currency) {
                                    $scope.currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                                });
                            }
                        );
                    }

                    function loadObjectAttributeTypes() {
                        ObjectTypeAttributeService.getObjectTypeAttributesByObjectIdAndObjectType($scope.attributeId, $scope.attributeType).then(
                            function (data) {
                                $scope.objectTypeAttributes = data.objectTypeAttributes;
                                angular.forEach($scope.objectTypeAttributes, function (attributeType) {
                                    attributeType.changeImage = false;
                                    attributeType.imageValue = null;
                                    attributeType.newImageValue = null;
                                    attributeType.timeValue = null;
                                    attributeType.showAttachment = false;
                                    attributeType.attachmentValues = [];
                                    attributeType.showTimeAttribute = false;
                                    attributeType.showTimestamp = false;
                                    attributeType.timestampValue = null;
                                    attributeType.editMode = false;
                                    attributeType.changeCurrency = false;
                                    attributeType.editTimeValue = null;
                                    attributeType.attributeValue = null;
                                    attributeType.refValueString = null;

                                    var attribute = data.attributeMap[attributeType.id];
                                    if (attribute != null) {
                                        attributeType.attributeValue = attribute;
                                        var Id = {
                                            objectId: $scope.attributeId,
                                            attributeDef: attributeType.id
                                        }
                                        attributeType.attributeValue.id = Id;
                                        if (attributeType.dataType == 'CURRENCY') {
                                            if (attributeType.attributeValue.currencyType != null) {
                                                attributeType.attributeValue[attributeType.name + 'type'] = $scope.currencyMap.get(attribute.currencyType);
                                            }
                                        }
                                        attributeType.attributeValue.imagePath = "api/core/objects/" + $scope.attributeId + "/attributes/" + attributeType.id + "/imageAttribute/download?" + new Date().getTime();
                                        if (attributeType.attributeValue.attachmentValues != null) {
                                            if (attributeType.attributeValue.attachmentValues.length > 0) {
                                                var attachmentIds = [];
                                                var counter = 0;
                                                angular.forEach(attributeType.attributeValue.attachmentValues, function (attachment) {
                                                    attachmentIds.push(attachment);
                                                    counter++;
                                                    if (counter == attributeType.attributeValue.attachmentValues.length) {
                                                        AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                            function (data) {
                                                                attributeType.attributeValue.attachmentValues = data;
                                                            }
                                                        )
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });

                                loadObjectAttributeRefValues();

                            },
                            function (error) {

                            });
                    }

                    function loadObjectAttributeRefValues() {
                        angular.forEach($scope.objectTypeAttributes, function (attributeType) {
                            if (attributeType.dataType == 'OBJECT' && attributeType.attributeValue.refValue != null) {
                                var objectSelector = $application.getObjectSelector(attributeType.refType);
                                if (objectSelector != null && attributeType.attributeValue.refValue != null) {
                                    objectSelector.getDetails(attributeType.attributeValue.refValue, attributeType, attributeType.attributeValue.refValue);
                                }
                            }
                        });
                    }

                    /* end for getting objectTypes And its attributes*/

                    function saveAttachments(attribute) {
                        if (attribute.attachmentValues.length > 0) {
                            var itemAttachPropertyIds = [];
                            var itemAttachments = attribute.attributeValue.attachmentValues;
                            attribute.attributeValue.attachmentValues = [];
                            angular.forEach(itemAttachments, function (attachment) {
                                itemAttachPropertyIds.push(attachment.id);
                            });
                            angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                                var itemAttachmentExists = false;
                                angular.forEach(itemAttachments, function (itemAttachment) {
                                    if (($scope.attributeId == itemAttachment.objectId) && (attribute.id == itemAttachment.attributeDef) && (attachmentValue.name == itemAttachment.name)) {
                                        itemAttachmentExists = true;
                                        itemAttachPropertyIds.remove(itemAttachment.id);

                                        var options = {
                                            title: 'Adding attribute attachment',
                                            message: attachmentValue.name + " : Attachment already exists. Do you want to override?",
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                AttributeAttachmentService.saveAttributeAttachment($scope.attributeId, attribute.id, $scope.attributeType, attachmentValue).then(
                                                    function (data) {
                                                        attribute.showAttachment = true;
                                                        itemAttachPropertyIds.push(data[0].id);
                                                        attribute.attachmentValues.remove(attachmentValue);
                                                        if (itemAttachPropertyIds.length > 0) {
                                                            angular.forEach(itemAttachPropertyIds, function (revAttachId) {
                                                                attribute.attributeValue.attachmentValues.push(revAttachId);
                                                            });
                                                            ObjectAttributeService.updateObjectAttribute($scope.attributeId, attribute.attributeValue).then(
                                                                function (data) {
                                                                    if (attribute.attachmentValues.length == 0) {
                                                                        attribute.showAttachment = false;
                                                                    }
                                                                }
                                                            )
                                                        }
                                                    }
                                                )
                                            } else {
                                                attribute.showAttachment = false;
                                            }
                                        });
                                    }
                                });
                                if (itemAttachmentExists == false) {
                                    AttributeAttachmentService.saveAttributeAttachment($scope.attributeId, attribute.id, $scope.attributeType, attachmentValue).then(
                                        function (data) {
                                            attribute.showAttachment = true;
                                            itemAttachPropertyIds.push(data[0].id);
                                            attribute.attachmentValues.remove(attachmentValue);
                                            if (itemAttachPropertyIds.length > 0) {
                                                angular.forEach(itemAttachPropertyIds, function (itemAttachId) {
                                                    attribute.attributeValue.attachmentValues.push(itemAttachId);
                                                });
                                                ObjectAttributeService.updateObjectAttribute($scope.attributeId, attribute.attributeValue).then(
                                                    function (data) {
                                                        if (attribute.attachmentValues.length == 0) {
                                                            attribute.showAttachment = false;
                                                        }

                                                    }
                                                )
                                            }
                                            loadObjectAttributeTypes();
                                        }
                                    )
                                }
                            })
                        }
                    }

                    function showSelectionDialog(objectType, attribute) {
                        var objectSelector = $application.getObjectSelector(objectType);
                        if (objectSelector != null) {
                            if (attribute.attributeValue.refValue != null && attribute.attributeValue.refValue != "") {
                                $rootScope.objectAttributeValue = attribute.attributeValue.refValue;
                            }
                            objectSelector.show($rootScope, function (object, displayValue) {
                                attribute.attributeValue.refValue = object.id;
                                attribute.refValueString = displayValue;
                                ObjectAttributeService.updateObjectAttribute($scope.attributeId, attribute.attributeValue).then(
                                    function () {

                                    });
                            });
                        }
                    }

                    (function () {
                        loadCurrencies();
                        loadObjectAttributeTypes();
                    })();
                }
            }
        }
    }
);
