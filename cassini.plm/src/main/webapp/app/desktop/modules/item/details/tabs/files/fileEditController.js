define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/varianceService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/glossaryService',
        'app/shared/services/core/objectFileService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/mfrFilesService',
        'app/shared/services/core/changeFileService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('FileEditController', FileEditController);

        function FileEditController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, $translate, CommonService, ItemTypeService, ItemService, ItemFileService,
                                    DialogService, DCOService, DCRService, ActivityService, ProjectService, GlossaryService, SpecificationsService, MfrFilesService, AttributeAttachmentService,
                                    VarianceService, MfrService, MfrPartsService, ECOService, WorkflowDefinitionService, ObjectAttributeService, ObjectFileService, ChangeFileService) {
            var vm = this;

            vm.loading = true;
            vm.files = [];
            vm.showDropzone = false;
            vm.showFileDropzone = false;
            vm.filesView = $scope.data.editFile;
            var currencyMap = new Hashtable();
            var parsed = angular.element("<div></div>");
            var fileDescriptionMsg = null;
            var ecoId = $stateParams.ecoId;
            var mode = $scope.data.mode;
            vm.fileEditPermission = $scope.data.fileEditPermission;
            vm.files = {
                fileId: vm.filesView.id,
                id: vm.filesView.object,
                name: vm.filesView.name,
                description: vm.filesView.description,
                locked: vm.filesView.locked,
                lockedBy: vm.filesView.lockedBy
            };
            var descriptionMsg = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();

            function editFile() {
                
                    var qualityFileDto = {};
                    qualityFileDto.objectFile = vm.files;
                    ObjectFileService.updateObjectFile(vm.filesView.object, vm.filesView.id, mode, qualityFileDto).then(
                        function (data) {
                            $scope.callback(data);
                            $rootScope.hideSidePanel();
                            if ($scope.data.editFile.description == null || $scope.data.editFile.description == "") {
                                fileDescriptionMsg = parsed.html($translate.instant("FILE_UPDATED_MSG")).html();
                                $rootScope.showSuccessMessage(fileDescriptionMsg);
                            } else {
                                fileDescriptionMsg = parsed.html($translate.instant("FILE_UPDATED_MSG")).html();
                                $rootScope.showSuccessMessage(fileDescriptionMsg);
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                
            }

            function loadObjectAttributeDefs() {
                vm.fileProperties = [];
                ItemTypeService.getAllTypeAttributes("FILE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            if (attribute.visible) {
                                var att = {
                                    id: {
                                        objectId: vm.filesView.id,
                                        attributeDef: attribute.id
                                    },
                                    attributeDef: attribute,
                                    value: {
                                        id: {
                                            objectId: vm.filesView.id,
                                            attributeDef: attribute.id
                                        },
                                        stringValue: null,
                                        integerValue: null,
                                        doubleValue: null,
                                        booleanValue: null,
                                        dateValue: null,
                                        listValue: null,
                                        hyperLinkValue: null,
                                        mlistValue: [],
                                        textValue: null,
                                        longTextValue: null,
                                        richTextValue: null,
                                        newListValue: null,
                                        listValueEditMode: false,
                                        timeValue: null,
                                        timestampValue: null,
                                        refValue: null,
                                        imageValue: null,
                                        attachmentValues: [],
                                        currencyValue: null,
                                        currencyType: null,
                                        defaultListValue: false
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
                                vm.fileProperties.push(att);
                            }

                        });
                        loadObjectAttributes();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /*------- To get Property values by itemId and revisionId ----------*/

            function loadObjectAttributes() {
                ObjectAttributeService.getAllObjectAttributes(vm.filesView.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.fileProperties, function (attribute) {
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
                                                vm.itemPropertyAttachments = data;
                                                attribute.value.attachmentValues = vm.itemPropertyAttachments;
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
                                    attribute.value.booleanValue = value.booleanValue;
                                    if (value.doubleValue != null) {
                                        attribute.value.doubleValue = parseFloat(value.doubleValue).toFixed(5);
                                    } else {
                                        attribute.value.doubleValue = value.doubleValue;
                                    }
                                    attribute.value.dateValue = value.dateValue;
                                    attribute.value.timeValue = value.timeValue;
                                    attribute.value.timestampValue = value.timestampValue;
                                    attribute.value.hyperLinkValue = value.hyperLinkValue;
                                    attribute.value.imageValue = value.imageValue;
                                    attribute.value.currencyValue = value.currencyValue;
                                    attribute.value.textValue = value.textValue;
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
                                    attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                } else {
                                    if (attribute.attributeDef.dataType == "LIST") {
                                        if (attribute.value.listValue == null && attribute.attributeDef.listMultiple == false) {
                                            attribute.value.listValue = attribute.attributeDef.defaultListValue;
                                            attribute.defaultListValue = false;
                                            attribute.value.deleteFlag = true
                                        }
                                        if (attribute.value.mlistValue.length == 0 && attribute.attributeDef.listMultiple == true) {
                                            attribute.value.mlistValue.push(attribute.attributeDef.defaultListValue);
                                        }
                                    }
                                    if (attribute.attributeDef.dataType == "BOOLEAN") {
                                        if (attribute.value.booleanValue == false || attribute.value.booleanValue == null) {
                                            attribute.value.booleanValue = false;
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
                loadObjectAttributeDefs();
                $rootScope.$on('app.items.file.edit', editFile);
            })();
        }
    }
);
