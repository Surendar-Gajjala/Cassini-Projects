define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStockIssuedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('StockIssueBasicController', StockIssueBasicController);

        function StockIssueBasicController($scope, $rootScope, $timeout, $state, $sce, $stateParams, CommonService,
                                           TopStockIssuedService, TaskService, ProjectService, AttributeAttachmentService,
                                           ObjectAttributeService, DialogService) {

            var vm = this;
            vm.stockIssue = null;
            vm.loading = true;
            vm.currencies = [];
            vm.loading = true;
            vm.updateStockIssue = updateStockIssue;
            vm.objectTypeAttributes = [];
            vm.updateObjectAttribute = updateObjectAttribute;
            vm.updateToEditMode = updateToEditMode;
            vm.cancelChanges = cancelChanges;
            vm.changeTime = changeTime;
            vm.cancelTime = cancelTime;
            vm.cancelCurrencyChanges = cancelCurrencyChanges;
            vm.changeCurrencyValue = changeCurrencyValue;
            vm.showImage = showImage;
            vm.change = change;
            vm.cancel = cancel;
            vm.currencies = [];
            vm.currencyMap = new Hashtable();
            vm.openAttachment = openAttachment;
            vm.deleteAttachments = deleteAttachments;
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.saveAttachments = saveAttachments;
            vm.saveBooleanProperties = saveBooleanProperties;
            vm.saveTimeProperty = saveTimeProperty;
            vm.changeTimestamp = changeTimestamp;
            vm.clearBrowse = true;

            vm.showSelectionDialog = showSelectionDialog;

            vm.flags = [{
                name: "True",
                value: true
            },
                {
                    name: "False",
                    value: false
                }
            ];

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

            function loadObjectAttributeTypes() {
                TopStockIssuedService.getIssueTypeAttributes($rootScope.storeId, vm.stockIssue.id, vm.stockIssue.materialIssueType.id).then(
                    function (data) {
                        vm.objectTypeAttributes = data.objectTypeAttributes;
                        angular.forEach(vm.objectTypeAttributes, function (attributeType) {
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
                                    objectId: vm.stockIssue.id,
                                    attributeDef: attributeType.id
                                }
                                attributeType.attributeValue.id = Id;
                                if (attributeType.dataType == 'CURRENCY') {
                                    if (attributeType.attributeValue.currencyType != null) {
                                        attributeType.attributeValue[attributeType.name + 'type'] = vm.currencyMap.get(attribute.currencyType);
                                    }
                                }
                                attributeType.attributeValue.imagePath = "api/core/objects/" + vm.stockIssue.id + "/attributes/" + attributeType.id + "/imageAttribute/download?" + new Date().getTime();
                                if (attributeType.attributeValue.attachmentValues != null) {
                                    if (attributeType.attributeValue.attachmentValues.length > 0) {
                                        attributeType.attachmentIds = [];
                                        var counter = 0;
                                        angular.forEach(attributeType.attributeValue.attachmentValues, function (attachment) {
                                            counter++;
                                            if (attachment != null) {
                                                attributeType.attachmentIds.push(attachment);
                                            }
                                            if (counter == attributeType.attributeValue.attachmentValues.length) {
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attributeType.attachmentIds).then(
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
                angular.forEach(vm.objectTypeAttributes, function (attributeType) {
                    if (attributeType.dataType == 'OBJECT' && attributeType.attributeValue.refValue != null) {
                        var objectSelector = $application.getObjectSelector(attributeType.refType);
                        if (objectSelector != null && attributeType.attributeValue.refValue != null) {
                            objectSelector.getDetails(attributeType.attributeValue.refValue, attributeType, attributeType.attributeValue.refValue);
                        }
                    }
                });
            }

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
                    title: 'Delete Attachment',
                    message: 'Are you sure you want to delete this attachment?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var index = attribute.attributeValue.attachmentValues.indexOf(attachment);
                        if (index != -1) {
                            AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                function (data) {
                                    attribute.attributeValue.attachmentValues.splice(index, 1);
                                    ObjectAttributeService.updateObjectAttribute(vm.stockIssue.id, attribute.attributeValue.attachmentValues).then(
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
                attribute.showAttachment = true;
            }

            function cancelAttachment(attribute) {
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
                modalImg.src = "api/core/objects/" + vm.stockIssue.id + "/attributes/" + attribute.id + "/imageAttribute/download?" + new Date().getTime();

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
                    ObjectAttributeService.updateObjectAttribute(vm.stockIssue.id, attribute.attributeValue).then(
                        function (data) {
                            attribute.showTimeAttribute = false;
                        }
                    )

                }
                else if (attribute.timestampValue != null) {
                    attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    attribute.attributeValue.timestampValue = attribute.timestampValue;
                    ObjectAttributeService.updateObjectAttribute(vm.stockIssue.id, attribute.attributeValue).then(
                        function (data) {
                            attribute.showTimestamp = false;
                        }
                    )

                }
            }

            /* flags array for boolean dataType */

            function saveBooleanProperties(attribute) {
                ObjectAttributeService.updateObjectAttribute(vm.stockIssue.id, attribute.attributeValue).then(
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
                        ObjectAttributeService.uploadObjectAttributeImage(vm.stockIssue.id, attribute.id, attribute.imageValue).then(
                            function (data) {
                                vm.clearBrowse = true;
                                attribute.newImageValue = null;
                                loadObjectAttributeTypes();
                            }
                        )
                    }
                } else {
                    ObjectAttributeService.updateObjectAttribute(vm.stockIssue.id, attribute.attributeValue).then(
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
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            vm.currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );
            }

            function saveAttachments(attribute) {
                if (attribute.attachmentValues.length > 0) {
                    var itemAttachPropertyIds = [];
                    var itemAttachments = attribute.attachmentValues;
                    if (attribute.attributeValue.attachmentValues != null) {
                        itemAttachments = itemAttachments.concat(attribute.attributeValue.attachmentValues);
                    }
                    attribute.attributeValue.attachmentValues = [];
                    angular.forEach(itemAttachments, function (attachment) {
                        if (attachment != null) {
                            itemAttachPropertyIds.push(attachment.id);
                        }
                    });
                    angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                        var itemAttachmentExists = false;
                        angular.forEach(itemAttachments, function (itemAttachment) {
                            if ((itemAttachment.objectId != undefined && vm.stockIssue.id == itemAttachment.objectId) && (attribute.id == itemAttachment.attributeDef) && (attachmentValue.name == itemAttachment.name)) {
                                itemAttachmentExists = true;
                                itemAttachPropertyIds.remove(itemAttachment.id);

                                var options = {
                                    title: 'Adding attribute attachment',
                                    message: attachmentValue.name + " : Attachment already exists! Do you want to override?",
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        AttributeAttachmentService.saveAttributeAttachment(vm.stockIssue.id, attribute.id, "ISSUE", attachmentValue).then(
                                            function (data) {
                                                attribute.showAttachment = true;
                                                itemAttachPropertyIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (itemAttachPropertyIds.length > 0) {
                                                    angular.forEach(itemAttachPropertyIds, function (revAttachId) {
                                                        attribute.attributeValue.attachmentValues.push(revAttachId);
                                                    });
                                                    ObjectAttributeService.updateObjectAttribute(vm.stockIssue.id, attribute.attributeValue).then(
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
                                    } else {
                                        attribute.showAttachment = false;
                                    }
                                });
                            }
                        });
                        if (itemAttachmentExists == false) {
                            AttributeAttachmentService.saveAttributeAttachment(vm.stockIssue.id, attribute.id, "ISSUE", attachmentValue).then(
                                function (data) {
                                    attribute.showAttachment = true;
                                    itemAttachPropertyIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    if (itemAttachPropertyIds.length > 0) {
                                        angular.forEach(itemAttachPropertyIds, function (itemAttachId) {
                                            attribute.attributeValue.attachmentValues.push(itemAttachId);
                                        });
                                        ObjectAttributeService.updateObjectAttribute(vm.stockIssue.id, attribute.attributeValue).then(
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
                    objectSelector.show($rootScope, function (object, displayValue) {
                        attribute.attributeValue.refValue = object.id;
                        attribute.refValueString = displayValue;
                        ObjectAttributeService.updateObjectAttribute(vm.stockIssue.id, attribute.attributeValue).then(
                            function () {

                            });
                    });
                }
            }

            function loadStockIssue() {
                TopStockIssuedService.getTopStockIssue($rootScope.storeId, $stateParams.issueId).then(
                    function (data) {
                        $rootScope.issueId = data.id;
                        vm.stockIssue = data;
                        $rootScope.viewInfo.title = "Stock Issue :" + vm.stockIssue.issueNumberSource;
                        vm.loading = false;
                        CommonService.getPersonReferences([vm.stockIssue], 'createdBy');
                        CommonService.getPersonReferences([vm.stockIssue], 'modifiedBy');
                        if (vm.stockIssue.task != null) {
                            TaskService.getProjectTask(vm.stockIssue.project, vm.stockIssue.task).then(
                                function (data) {
                                    vm.stockIssue.taskName = data.name;
                                }
                            );
                        }
                        ProjectService.getProject(vm.stockIssue.project).then(
                            function (project) {
                                vm.stockIssue.projectName = project.name;
                            }
                        ).then(
                            CommonService.getPerson(vm.stockIssue.issuedTo).then(
                                function (person) {
                                    vm.stockIssue.issuedToPerson = person.fullName;
                                }
                            )
                        );
                        loadObjectAttributeTypes();
                    }
                )
            }

            function updateStockIssue() {
                TopStockIssuedService.updateTopStockIssue($rootScope.storeId, vm.stockIssue).then(
                    function (data) {
                        vm.stockIssue = data;
                        TaskService.getProjectTask(vm.stockIssue.project, vm.stockIssue.task).then(
                            function (data) {
                                vm.stockIssue.taskName = data.name;
                                CommonService.getPersonReferences([vm.stockIssue], 'createdBy');
                                CommonService.getPersonReferences([vm.stockIssue], 'modifiedBy');
                            }
                        );
                        ProjectService.getProject(vm.stockIssue.project).then(
                            function (project) {
                                vm.stockIssue.projectName = project.name;
                            }
                        ).then(
                            CommonService.getPerson(vm.stockIssue.issuedTo).then(
                                function (person) {
                                    vm.stockIssue.issuedToPerson = person.fullName;
                                }
                            )
                        ).then(
                            function () {
                                $rootScope.showSuccessMessage("Stock Issue updated successfully");
                            }
                        );
                    }
                );
            }

            (function () {
                loadStockIssue();
                loadCurrencies();
                $scope.$on('app.stock.issueItems', function (event, data) {
                })
            })();
        }
    }
);