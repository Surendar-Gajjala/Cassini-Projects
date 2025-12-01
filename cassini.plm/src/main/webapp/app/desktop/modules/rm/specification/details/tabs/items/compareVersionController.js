define(['app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/folderDirective',
        'app/desktop/modules/classification/directive/folderController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/item/all/itemSearchDialogueController',
        'app/desktop/modules/item/all/advancedSearchController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/folderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
    ],
    function (module) {
        module.controller('CompareVersionController', CompareVersionController);

        function CompareVersionController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $translate, $cookies, CommonService, SpecificationsService, ItemTypeService, ItemService, DialogService, FolderService, ObjectTypeAttributeService,
                                          AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService,
                                          ItemFileService, SpecificationsService) {
            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.reqIds = [];
            vm.reqVersionAttributes = [];
            vm.reqVersionAttributeschnaged = [];
            vm.reqCurrentAndPreviousVersionAttributes = [];
            vm.currentVersion = null;
            vm.previousVersion = null;
            vm.reqCurrentPreviousVersions = [];
            vm.reqCurrentVersions = [];
            vm.noVersionsMessage = null;
            vm.latestVersions = [];
            vm.previousVersions = [];
            var currencyMap = new Hashtable();

            function compareReqVersionsAndAttributes(requirement) {
                SpecificationsService.getAllRequirementVersionAttributes(requirement.objectNumber, requirement.specification).then(
                    function (data) {
                        vm.requirementVersions = data;
                        angular.forEach(vm.requirementVersions, function (reqAndAttributes) {
                            if (reqAndAttributes.latest == true) {
                                vm.currentVersion = reqAndAttributes.version;
                                vm.reqCurrentVersions.push(reqAndAttributes);
                            }
                            else {
                                vm.previousVersion = reqAndAttributes.version;
                                angular.forEach(vm.reqCurrentVersions, function (val) {
                                    val.previousVersionName = reqAndAttributes.name;
                                    val.previousVersionDescription = reqAndAttributes.description;
                                    vm.reqCurrentPreviousVersions.push(val);
                                })

                            }

                            vm.reqIds.push(reqAndAttributes.id);
                        })

                        SpecificationsService.getVersionAttributes(vm.reqIds).then(
                            function (data) {
                                angular.forEach(data[0], function (firstEle) {
                                    vm.reqVersionAttributes.push(firstEle);
                                })
                                angular.forEach(data[1], function (secondEle) {
                                    vm.reqVersionAttributes.push(secondEle);
                                })

                                $timeout(function () {

                                    angular.forEach(vm.reqVersionAttributes, function (current) {
                                        angular.forEach(vm.requirementVersions, function (val) {

                                            if (val.latest == true && (val.id == current.id.objectId)) {
                                                vm.latestVersions.push(current);
                                            }
                                            if (val.latest == false && (val.id == current.id.objectId)) {
                                                vm.previousVersions.push(current);
                                            }

                                        })
                                    })

                                    comparingPrevAndCurrentVersions(vm.latestVersions, vm.previousVersions);

                                }, 1000)

                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            })

                    }, function (error) {
                        vm.error = true;
                        vm.noVersionsMessage = error.message;
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();

                    })

            }

            vm.finalVersions = [];
            function comparingPrevAndCurrentVersions(currentVersion, previousVersions) {
                angular.forEach(currentVersion, function (current) {

                    if (previousVersions.length > 0) {
                        current.attributeChecked = false;
                        angular.forEach(previousVersions, function (attribute) {
                            if ((attribute.id.attributeDef == current.id.attributeDef) && (attribute.objectTypeAttribute.dataType == current.objectTypeAttribute.dataType)) {
                                current.attributeChecked = true;
                                if (attribute.objectTypeAttribute.dataType == 'TEXT' && current.objectTypeAttribute.dataType == 'TEXT') {
                                    current.preVersionValue = attribute.stringValue;

                                    if (current.preVersionValue != current.stringValue) {
                                        current.valueChaged = true;
                                    }

                                } else if (attribute.objectTypeAttribute.dataType == 'LONGTEXT' && current.objectTypeAttribute.dataType == 'LONGTEXT') {
                                    current.preVersionValue = attribute.longTextValue;
                                    if (current.preVersionValue != current.longTextValue) {
                                        current.valueChaged = true;
                                    }
                                } else if (attribute.objectTypeAttribute.dataType == 'RICHTEXT' && current.objectTypeAttribute.dataType == 'RICHTEXT') {
                                    current.preVersionValue = attribute.richTextValue;
                                    // item[attributeName] = $sce.trustAsHtml(attribute.richTextValue);
                                } else if (attribute.objectTypeAttribute.dataType == 'INTEGER' && current.objectTypeAttribute.dataType == 'INTEGER') {
                                    current.preVersionValue = attribute.integerValue;
                                    if (parseInt(current.preVersionValue) != parseInt(current.integerValue)) {
                                        current.valueChaged = true;
                                    }

                                } else if (attribute.objectTypeAttribute.dataType == 'BOOLEAN' && current.objectTypeAttribute.dataType == 'BOOLEAN') {
                                    current.preVersionValue = attribute.booleanValue;

                                    if (current.preVersionValue != current.booleanValue) {
                                        current.valueChaged = true;
                                    }

                                } else if (attribute.objectTypeAttribute.dataType == 'DOUBLE' && current.objectTypeAttribute.dataType == 'DOUBLE') {
                                    current.preVersionValue = attribute.doubleValue;

                                    if (Math.round(parseFloat(current.preVersionValue) * 100) / 100 != Math.round(parseFloat(current.doubleValue) * 100) / 100) {
                                        current.valueChaged = true;
                                    }

                                } else if (attribute.objectTypeAttribute.dataType == 'DATE' && current.objectTypeAttribute.dataType == 'DATE') {
                                    current.preVersionValue = attribute.dateValue;

                                    if (current.preVersionValue != current.dateValue) {
                                        current.valueChaged = true;
                                    }

                                }
                                else if (attribute.objectTypeAttribute.dataType == 'TIME' && current.objectTypeAttribute.dataType == 'TIME') {
                                    current.preVersionValue = attribute.timeValue;

                                    if (current.preVersionValue.toString() != current.timeValue.toString()) {
                                        current.valueChaged = true;
                                    }

                                } else if (attribute.objectTypeAttribute.dataType == 'TIMESTAMP' && current.objectTypeAttribute.dataType == 'TIMESTAMP') {
                                    current.preVersionValue = attribute.timestampValue;

                                    if (current.preVersionValue.toString() != current.timestampValue.toString()) {
                                        current.valueChaged = true;
                                    }
                                } else if (attribute.objectTypeAttribute.dataType == 'CURRENCY' && current.objectTypeAttribute.dataType == 'CURRENCY') {
                                    if (attribute.currencyType != null) {
                                        current.currVal = currencyMap.get(current.currencyType);
                                        current.currVal = current.currencyValue + current.currVal;
                                        current.preVersionValue = currencyMap.get(attribute.currencyType);
                                        current.preVersionValue = attribute.currencyValue + current.preVersionValue;
                                        if (current.currVal != current.preVersionValue) {
                                            current.valueChaged = true;
                                        }
                                    }
                                } else if (attribute.objectTypeAttribute.dataType == 'IMAGE' && current.objectTypeAttribute.dataType == 'IMAGE') {
                                    if (attribute.imageValue != null) {
                                        current.preVersionValue = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                    }
                                }

                                else if (attribute.objectTypeAttribute.dataType == 'LIST' && current.objectTypeAttribute.dataType == 'LIST') {
                                    if (attribute.listValue != null && attribute.objectTypeAttribute.listMultiple == false) {
                                        current.preVersionSingleValue = attribute.listValue;
                                        if (current.preVersionSingleValue != current.listValue) {
                                            current.valueChaged = true;
                                        }
                                    } else if (attribute.mlistValue.length > 0 && attribute.objectTypeAttribute.listMultiple == true) {
                                        current.preVersionValue = [];
                                        angular.forEach(attribute.mlistValue, function (va) {
                                            current.preVersionValue.push(va);
                                        })

                                        $timeout(function () {
                                            current.valueChaged = false;
                                            angular.forEach(current.mlistValue, function (cueMlist) {
                                                var value = false;
                                                angular.forEach(current.preVersionValue, function (preMList) {
                                                    if (cueMlist == preMList) {
                                                        value = true;
                                                    }
                                                });
                                                if (!value) {
                                                    current.valueChaged = true;
                                                }
                                            })
                                        }, 1000)

                                    }

                                }

                                else if (attribute.objectTypeAttribute.dataType == 'ATTACHMENT' && current.objectTypeAttribute.dataType == 'ATTACHMENT') {
                                    var revisionCurrentAttachmentIds = [];
                                    var revisionPreAttachmentIds = [];
                                    vm.revisionAttachments = [];
                                    vm.revisionAttachments1 = [];
                                    if (attribute.attachmentValues.length > 0) {
                                        angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                            var index = revisionPreAttachmentIds.indexOf(attachmentId);
                                            if (index == -1) {
                                                revisionPreAttachmentIds.push(attachmentId);
                                            }

                                        });
                                        AttributeAttachmentService.getMultipleAttributeAttachments(revisionPreAttachmentIds).then(
                                            function (data) {
                                                vm.revisionAttachments = data;
                                                current.preVersionValue = [];
                                                angular.forEach(vm.revisionAttachments, function (attach) {
                                                    current.preVersionValue.push(attach);
                                                })
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                             })
                                    }
                                    $timeout(function () {
                                        if (current.attachmentValues.length > 0) {
                                            angular.forEach(current.attachmentValues, function (attachmentId) {
                                                var index = revisionCurrentAttachmentIds.indexOf(attachmentId);
                                                if (index == -1) {
                                                    revisionCurrentAttachmentIds.push(attachmentId);
                                                }

                                            });
                                            AttributeAttachmentService.getMultipleAttributeAttachments(revisionCurrentAttachmentIds).then(
                                                function (data) {
                                                    vm.revisionAttachments1 = data;
                                                    current.attachmentValues = [];
                                                    angular.forEach(vm.revisionAttachments1, function (attach) {
                                                        current.attachmentValues.push(attach);
                                                    })
                                                    $timeout(function () {
                                                        current.valueChaged = false;
                                                        angular.forEach(current.attachmentValues, function (cueAtt) {
                                                            var value = false;
                                                            angular.forEach(current.preVersionValue, function (preAtt) {
                                                                if (cueAtt.name == preAtt.name) {
                                                                    value = true;
                                                                }
                                                            });
                                                            if (!value) {
                                                                current.valueChaged = true;
                                                            }
                                                        })
                                                    }, 3000)

                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                }
                                            )
                                        }

                                    }, 1000)

                                }

                                else if (attribute.objectTypeAttribute.dataType == 'OBJECT' && current.objectTypeAttribute.dataType == 'OBJECT') {
                                    if (attribute.objectTypeAttribute.refType != null) {
                                        var objName1 = null;
                                        var objName2 = null;
                                        var objName3 = null;
                                        var objName4 = null;
                                        var objName5 = null;
                                        var objName6 = null;
                                        var objName7 = null;
                                        if (attribute.objectTypeAttribute.refType == 'ITEM') {
                                            if (current.refValue) {
                                                ItemService.getItem(current.refValue).then(
                                                    function (itemValue) {
                                                        current.objectValue = itemValue.itemNumber;
                                                        objName1 = itemValue.itemNumber;
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                        $rootScope.hideBusyIndicator();
                                                    }
                                                )
                                            }

                                            if (attribute.refValue != null) {
                                                ItemService.getItem(attribute.refValue).then(
                                                    function (itemValue) {
                                                        current.preVersionValue = itemValue.itemNumber;
                                                        if (current.preVersionValue != objName1) {
                                                            current.valueChaged = true;
                                                        }
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                        $rootScope.hideBusyIndicator();
                                                    }
                                                )
                                            }

                                        } else if (attribute.objectTypeAttribute.refType == 'ITEMREVISION') {

                                            if (current.refValue != null) {
                                                ItemService.getRevisionId(current.refValue).then(
                                                    function (revisionValue) {
                                                        attribute.objectValue = revisionValue;
                                                        ItemService.getItem(revisionValue.itemMaster).then(
                                                            function (data) {
                                                                current.objectValue.itemMaster = data.itemNumber;
                                                                objName2 = data.itemNumber;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                        $rootScope.hideBusyIndicator();
                                                    }
                                                )
                                            }
                                            if (attribute.refValue != null) {
                                                ItemService.getRevisionId(attribute.refValue).then(
                                                    function (revisionValue) {
                                                        attribute.preVersionValue = revisionValue;
                                                        ItemService.getItem(revisionValue.itemMaster).then(
                                                            function (data) {
                                                                current.preVersionValue.itemMaster = data.itemNumber;
                                                                if (current.preVersionValue.itemMaster != objName2) {
                                                                    current.valueChaged = true;
                                                                }
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

                                        } else if (attribute.objectTypeAttribute.refType == 'CHANGE') {

                                            if (attribute.refValue != null) {
                                                ECOService.getECO(attribute.refValue).then(
                                                    function (changeValue) {
                                                        current.preVersionValue = changeValue.ecoNumber;
                                                        objName3 = changeValue.ecoNumber;
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                    }
                                                )
                                            }
                                            if (current.refValue != null) {
                                                ECOService.getECO(current.refValue).then(
                                                    function (changeValue) {
                                                        current.objectValue = changeValue.ecoNumber;
                                                        if (objName3 != current.objectValue) {
                                                            current.valueChaged = true;
                                                        }
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                        $rootScope.hideBusyIndicator();
                                                    }
                                                )
                                            }

                                        } else if (attribute.objectTypeAttribute.refType == 'WORKFLOW') {

                                            if (attribute.refValue != null) {
                                                WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                    function (workflowValue) {
                                                        current.preVersionValue = workflowValue.name;
                                                        objName4 = workflowValue.name;

                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                        hideBusyIndicator();
                                                    }
                                                )
                                            }
                                            if (current.refValue != null) {
                                                WorkflowDefinitionService.getWorkflowDefinition(current.refValue).then(
                                                    function (workflowValue) {
                                                        current.objectValue = workflowValue.name;
                                                        if (objName4 != current.objectValue) {
                                                            current.valueChaged = true;
                                                        }
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                    }
                                                )
                                            }

                                        } else if (attribute.objectTypeAttribute.refType == 'MANUFACTURER') {

                                            if (attribute.refValue != null) {
                                                MfrService.getManufacturer(attribute.refValue).then(
                                                    function (mfrValue) {
                                                        current.preVersionValue = mfrValue.name;
                                                        objName5 = mfrValue.name;
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                    }
                                                )
                                            }
                                            if (current.refValue != null) {
                                                MfrService.getManufacturer(current.refValue).then(
                                                    function (mfrValue) {
                                                        current.objectValue = mfrValue.name;
                                                        if (objName5 != current.objectValue) {
                                                            current.valueChaged = true;
                                                        }
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                     }
                                                )
                                            }

                                        } else if (attribute.objectTypeAttribute.refType == 'MANUFACTURERPART') {

                                            if (attribute.refValue != null) {
                                                MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                    function (mfrPartValue) {
                                                        current.preVersionValue = mfrPartValue.partNumber;
                                                        objName6 = mfrPartValue.partNumber;
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                     }
                                                )
                                            }
                                            if (current.refValue != null) {
                                                MfrPartsService.getManufacturepart(current.refValue).then(
                                                    function (mfrPartValue) {
                                                        current.objectValue = mfrPartValue.partNumber;
                                                        if (objName6 != current.objectValue) {
                                                            current.valueChaged = true;
                                                        }
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                     }
                                                )
                                            }

                                        } else if (attribute.objectTypeAttribute.refType == 'PERSON') {

                                            if (attribute.refValue != null) {
                                                CommonService.getPerson(attribute.refValue).then(
                                                    function (person) {
                                                        current.preVersionValue = person.person.firstName;
                                                        objName7 = person.person.firstName;
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                     }
                                                )
                                            }
                                            if (current.refValue != null) {
                                                CommonService.getPerson(current.refValue).then(
                                                    function (person) {
                                                        current.objectValue = person.person.firstName;
                                                        if (objName7 != current.objectValue) {
                                                            current.valueChaged = true;
                                                        }
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                     }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        });

                        if (!current.attributeChecked) {

                            if (current.objectTypeAttribute.dataType == 'CURRENCY') {
                                if (current.currencyType != null) {
                                    current.currVal = currencyMap.get(current.currencyType);
                                    current.currVal = current.currencyValue + current.currVal;
                                }
                            }

                            getObjects(current);
                            getAttachments(current);
                        }
                    } else if (previousVersions.length == 0) {
                        if (current.objectTypeAttribute.dataType == 'CURRENCY') {
                            if (current.currencyType != null) {
                                current.currVal = currencyMap.get(current.currencyType);
                                current.currVal = current.currencyValue + current.currVal;
                            }
                        }

                        getObjects(current);
                        getAttachments(current);
                    }

                    vm.finalVersions.push(current);
                })
            }

            function getAttachments(attribute) {
                var revisionCurrentAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                    var index = revisionCurrentAttachmentIds.indexOf(attachmentId);
                    if (index == -1) {
                        revisionCurrentAttachmentIds.push(attachmentId);
                    }

                });
                AttributeAttachmentService.getMultipleAttributeAttachments(revisionCurrentAttachmentIds).then(
                    function (data) {
                        vm.revisionAttachments1 = data;
                        attribute.attachmentValues = vm.revisionAttachments1;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function getObjects(attribute) {
                if (attribute.objectTypeAttribute.dataType == 'OBJECT') {
                    if (attribute.objectTypeAttribute.refType != null) {
                        if (attribute.objectTypeAttribute.refType == 'ITEM') {

                            if (attribute.refValue != null) {
                                ItemService.getItem(attribute.refValue).then(
                                    function (itemValue) {
                                        attribute.objectValue = itemValue.itemNumber;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }

                        } else if (attribute.objectTypeAttribute.refType == 'ITEMREVISION') {

                            if (attribute.refValue != null) {
                                ItemService.getRevisionId(attribute.refValue).then(
                                    function (revisionValue) {
                                        attribute.preVersionValue = revisionValue;
                                        ItemService.getItem(revisionValue.itemMaster).then(
                                            function (data) {
                                                attribute.objectValue.itemMaster = data.itemNumber;
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                             }
                                        )
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }

                        } else if (attribute.objectTypeAttribute.refType == 'CHANGE') {

                            if (attribute.refValue != null) {
                                ECOService.getECO(attribute.refValue).then(
                                    function (changeValue) {
                                        attribute.objectValue = changeValue.ecoNumber;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }

                        } else if (attribute.objectTypeAttribute.refType == 'WORKFLOW') {

                            if (attribute.refValue != null) {
                                WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                    function (workflowValue) {
                                        attribute.objectValue = workflowValue.name;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }

                        } else if (attribute.objectTypeAttribute.refType == 'MANUFACTURER') {

                            if (attribute.refValue != null) {
                                MfrService.getManufacturer(attribute.refValue).then(
                                    function (mfrValue) {
                                        attribute.objectValue = mfrValue.name;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }

                        } else if (attribute.objectTypeAttribute.refType == 'MANUFACTURERPART') {

                            if (attribute.refValue != null) {
                                MfrPartsService.getManufacturepart(attribute.refValue).then(
                                    function (mfrPartValue) {
                                        attribute.objectValue = mfrPartValue.partNumber;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }

                        } else if (attribute.objectTypeAttribute.refType == 'PERSON') {

                            if (attribute.refValue != null) {
                                CommonService.getPerson(attribute.refValue).then(
                                    function (person) {
                                        attribute.objectValue = person.person.firstName;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }

                        }
                    }
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    angular.forEach($application.currencies, function (data) {
                        currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                    })
                    vm.requirementDetails = $scope.data.selectedReq;
                    compareReqVersionsAndAttributes(vm.requirementDetails);
                //}
            })();
        }
    }
)
;

