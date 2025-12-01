define(
    [
        'app/desktop/modules/rm/rm.module',
        'table-dragger',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/requirementsTypeService',
        'app/desktop/modules/classification/directive/folderDirective',
        'app/desktop/modules/classification/directive/folderController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/item/all/itemSearchDialogueController',
        'app/desktop/modules/item/all/advancedSearchController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/folderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('SpecificationItemsController', SpecificationItemsController);

        function SpecificationItemsController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, $translate, $window, $application,
                                              CommonService, SpecificationsService, ItemTypeService, ItemService, DialogService, FolderService, ObjectTypeAttributeService,
                                              AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService,
                                              ItemFileService, RecentlyVisitedService, RequirementsTypeService, LoginService, LovService) {

            var vm = this;
            var specId = $stateParams.specId;
            vm.selectedAttribute = [];
            vm.selectedObjectAttributes = [];
            vm.objectIds = [];
            vm.itemIds = [];
            vm.attributeIds = [];

            vm.mode = null;
            vm.mode = $stateParams.mode;
            var currencyMap = new Hashtable();
            vm.editRequirement = editRequirement;
            vm.showRevisionHistory = showRevisionHistory;
            vm.showEditHistory = showEditHistory;
            vm.promoteRequirement = promoteRequirement;
            vm.demoteRequirement = demoteRequirement;
            vm.editSection = editSection;

            vm.specItems = [];
            vm.requirements = [];
            function loadSpecSections() {
                vm.loading = true;
                SpecificationsService.getAllSpecSections(specId).then(
                    function (data) {
                        angular.forEach(data, function (item) {
                            item.parentBom = null;
                            item.isNew = false;
                            item.editItemNumber = false;
                            item.selected = false;
                            item.editMode = false;
                            item.expanded = false;
                            item.level = 0;
                            item.isRoot = true;
                            item.sectionChildren = [];
                        });
                        vm.specItems = data;
                        vm.loading = false;
                        initDragger();
                        dragMe();
                        loadItemAttributeValues();

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem(specId + "specattributes"));
                    //JSON.parse($window.localStorage.getItem("requirements"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function validateLastReqJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedRequirementHeight"));
                    //JSON.parse($window.localStorage.getItem("requirements"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadItemAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.specItems, function (item) {
                    if (item.requirement != undefined && item.requirement.objectType == "REQUIREMENT") {
                        vm.itemIds.push(item.requirement.id);
                    }
                });
                angular.forEach(vm.selectedAttribute, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedAttribute, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.specItems, function (item) {

                                if (item.requirement != undefined && item.requirement.objectType == "REQUIREMENT") {
                                    var attributes = [];

                                    var itemAttributes = vm.selectedObjectAttributes[item.requirement.id];
                                    if (itemAttributes != null && itemAttributes != undefined) {
                                        attributes = attributes.concat(itemAttributes);
                                    }
                                    angular.forEach(attributes, function (attribute) {
                                        var selectatt = map.get(attribute.id.attributeDef);
                                        if (selectatt != null) {
                                            var attributeName = selectatt.id;
                                            if (selectatt.dataType == 'TEXT') {
                                                item[attributeName] = attribute.stringValue;
                                            } else if (selectatt.dataType == 'LONGTEXT') {
                                                item[attributeName] = attribute.longTextValue;
                                            } else if (selectatt.dataType == 'RICHTEXT') {
                                                item[attributeName] = attribute;

                                            } else if (selectatt.dataType == 'INTEGER') {
                                                item[attributeName] = attribute.integerValue;
                                            } else if (selectatt.dataType == 'BOOLEAN') {
                                                item[attributeName] = attribute.booleanValue;
                                            } else if (selectatt.dataType == 'DOUBLE') {
                                                item[attributeName] = attribute.doubleValue;
                                            } else if (selectatt.dataType == 'DATE') {
                                                item[attributeName] = attribute.dateValue;
                                            } else if (selectatt.dataType == 'LIST') {
                                                if (attribute.listValue != null) {
                                                    item[attributeName] = attribute.listValue;
                                                } else if (attribute.mlistValue.length > 0) {
                                                    item[attributeName] = attribute.mlistValue;
                                                }
                                            } else if (selectatt.dataType == 'LIST') {
                                                item[attributeName] = attribute.mlistValue;
                                            } else if (selectatt.dataType == 'TIME') {
                                                item[attributeName] = attribute.timeValue;
                                            } else if (selectatt.dataType == 'TIMESTAMP') {
                                                item[attributeName] = attribute.timestampValue;

                                            } else if (selectatt.dataType == 'CURRENCY') {
                                                item[attributeName] = attribute.currencyValue;
                                                if (attribute.currencyType != null) {
                                                    item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                                }
                                            } else if (selectatt.dataType == 'ATTACHMENT') {
                                                var revisionAttachmentIds = [];
                                                if (attribute.attachmentValues.length > 0) {
                                                    angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                        revisionAttachmentIds.push(attachmentId);
                                                    });
                                                    AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                        function (data) {
                                                            vm.revisionAttachments = data;
                                                            item[attributeName] = vm.revisionAttachments;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                         }
                                                    )
                                                }
                                            } else if (selectatt.dataType == 'IMAGE') {
                                                if (attribute.imageValue != null) {
                                                    item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                }
                                            } else if (selectatt.dataType == 'OBJECT') {
                                                if (selectatt.refType != null) {
                                                    if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                                        ItemService.getItem(attribute.refValue).then(
                                                            function (itemValue) {
                                                                item[attributeName] = itemValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                                        ItemService.getRevisionId(attribute.refValue).then(
                                                            function (revisionValue) {
                                                                item[attributeName] = revisionValue;
                                                                ItemService.getItem(revisionValue.itemMaster).then(
                                                                    function (data) {
                                                                        item[attributeName].itemMaster = data.itemNumber;
                                                                    }
                                                                )
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                                        ECOService.getECO(attribute.refValue).then(
                                                            function (changeValue) {
                                                                item[attributeName] = changeValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                                        WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                            function (workflowValue) {
                                                                item[attributeName] = workflowValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                                        MfrService.getManufacturer(attribute.refValue).then(
                                                            function (mfrValue) {
                                                                item[attributeName] = mfrValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                                        MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                            function (mfrPartValue) {
                                                                item[attributeName] = mfrPartValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                                        CommonService.getPerson(attribute.refValue).then(
                                                            function (person) {
                                                                item[attributeName] = person;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    })

                                }
                            })

                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    );
                }
            }

            vm.createSection = createSection;
            $rootScope.createSection = createSection;
            vm.createRequirement = createRequirement;
            vm.showRequirementDetails = showRequirementDetails;
            var parse = angular.element("<div></div>");
            var newReqTitle = parse.html($translate.instant("NEW_REQUIREMENT")).html();
            var newSectionTitle = parse.html($translate.instant("NEW_SECTION")).html();
            vm.addRootSection = parse.html($translate.instant("ADD_SECTION_TITLE")).html();
            var create = parse.html($translate.instant("CREATE")).html();

            function createSection(parent) {
                var options = {
                    title: newSectionTitle,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/newSectionView.jsp',
                    controller: 'NewSectionController as newSecVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/items/newSectionController',
                    width: 500,
                    showMask: true,
                    data: {
                        specSection: parent
                    },
                    buttons: [
                        {text: create, broadcast: 'app.spec.section.new'}
                    ],
                    callback: function (item) {
                        reInitializeColResize();
                        item.editMode = false;
                        item.isNew = false;
                        if (parent == null) {
                            item.level = 0;
                            item.sectionChildren = [];
                            vm.specItems.push(item);

                            $application.sessionDataStorage.put(specId + "requirements", vm.specItems);
                            //$window.localStorage.setItem("requirements", JSON.stringify(vm.specItems));

                        } else {
                            var index = vm.specItems.indexOf(parent);
                            if (parent.sectionChildren == undefined) {
                                parent.sectionChildren = [];
                            }
                            parent.expanded = true;
                            parent.children.push(item);
                            item.level = parent.level + 1;
                            index = index + getIndexTopInsertNewChild(parent) + 1;
                            parent.sectionChildren.push(item);

                            vm.specItems.splice(index, 0, item);

                            if (item.parent != null) {
                                SpecificationsService.getParentObject(item.parent).then(
                                    function (data) {
                                        angular.forEach(vm.specItems, function (spec) {
                                            if (data.id == spec.id) {
                                                spec.canAddSection = data.canAddSection;
                                                spec.canAddRequirement = data.canAddRequirement;

                                                $scope.$evalAsync();
                                            }

                                        })

                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    })
                            }
                            $application.sessionDataStorage.put(specId + "requirements", vm.specItems);
                            //$window.localStorage.setItem("requirements", JSON.stringify(vm.specItems));

                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function createRequirement(parent) {
                var options = {
                    title: newReqTitle,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/newRequirementView.jsp',
                    controller: 'NewRequirementsController as newReqVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/items/newRequirementController',
                    width: 675,
                    showMask: true,
                    data: {
                        specSection: parent
                    },
                    buttons: [
                        {text: create, broadcast: 'app.spec.requirement.new'}
                    ],
                    callback: function (item) {
                        reInitializeColResize();
                        item.editMode = false;
                        item.isNew = false;
                        item.reqEdits = item.requirement.requirementEditLength;

                        if (parent == null) {
                            item.level = 0;
                            item.sectionChildren = [];
                            vm.specItems.push(item);
                            $application.sessionDataStorage.put(specId + "requirements", vm.specItems);
                            //$window.localStorage.setItem("requirements", JSON.stringify(vm.specItems));
                            loadRequirementVersions();
                            angular.forEach(vm.specItems, function (specItem) {
                                if (specItem.id == item.parent) {
                                    specItem.requirementsEdit = specItem.requirementsEdit + 1;

                                    if (specItem.parent != null) {
                                        visitParentSpecSection(specItem);
                                    }
                                }

                            })
                        } else {
                            var index = vm.specItems.indexOf(parent);
                            if (parent.sectionChildren == undefined) {
                                parent.sectionChildren = [];
                            }
                            parent.expanded = true;
                            parent.children.push(item);
                            item.level = parent.level + 1;
                            index = index + getIndexTopInsertNewChild(parent) + 1;
                            parent.sectionChildren.push(item);
                            loadRequirementVersions();
                            vm.specItems.splice(index, 0, item);
                            vm.requirements = [];
                            angular.forEach(vm.specItems, function (specItem) {
                                if (specItem.objectType == "SPECREQUIREMENT") {
                                    vm.requirements.push(specItem.requirement);
                                }
                                if (specItem.id == item.parent) {
                                    specItem.requirementsEdit = specItem.requirementsEdit + 1;

                                    if (specItem.parent != null) {
                                        visitParentSpecSection(specItem);
                                    }
                                }

                            })
                            CommonService.getPersonReferences(vm.requirements, 'modifiedBy');
                            CommonService.getPersonReferences(vm.requirements, 'createdBy');
                            if (item.parent != null) {
                                SpecificationsService.getParentObject(item.parent).then(
                                    function (data) {
                                        angular.forEach(vm.specItems, function (spec) {
                                            if (data.id == spec.id) {
                                                spec.canAddSection = data.canAddSection;
                                                spec.canAddRequirement = data.canAddRequirement;

                                            }
                                        })
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    })
                            }
                            $application.sessionDataStorage.put(specId + "requirements", vm.specItems);
                            //$window.localStorage.setItem("requirements", JSON.stringify(vm.specItems));

                        }
                        loadItemAttributeValues();
                        $rootScope.loadSpecification();

                        initDragger();
                        dragMe();
                        $rootScope.loadSpecCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getIndexTopInsertNewChild(bomItem) {
                var index = 0;

                if (bomItem.sectionChildren != undefined && bomItem.sectionChildren != null) {
                    index = bomItem.sectionChildren.length;
                    angular.forEach(bomItem.sectionChildren, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }

            vm.toggleNode = toggleNode;
            function toggleNode(specSection) {
                reInitializeColResize();

                if (specSection.expanded == null || specSection.expanded == undefined) {
                    specSection.expanded = false;
                }
                specSection.expanded = !specSection.expanded;
                var index = vm.specItems.indexOf(specSection);
                if (specSection.expanded == false) {
                    removeChildren(specSection);
                    $(".JCLRgrips").css("display", "none");
                }
                else {
                    SpecificationsService.getSectionChildren(specId, specSection.id).then(
                        function (data) {
                            angular.forEach(data, function (item) {
                                item.isNew = false;
                                item.editMode = false;
                                item.expanded = false;
                                item.level = specSection.level + 1;
                                item.sectionChildren = [];
                                specSection.sectionChildren.push(item);

                            });

                            angular.forEach(specSection.sectionChildren, function (item) {
                                index = index + 1;
                                vm.specItems.splice(index, 0, item);
                                $application.sessionDataStorage.put(specId + "requirements", vm.specItems);
                                /* $window.localStorage.setItem("requirements", JSON.stringify(vm.specItems));*/

                            });

                            angular.forEach(vm.specItems, function (item) {
                                if (item.sectionChildren != null || item.sectionChildren != undefined || item.sectionChildren.length > 0) {
                                    angular.forEach(item.sectionChildren, function (item1) {
                                        if (item1.requirement != undefined && item1.requirement.objectType == "REQUIREMENT") {
                                            vm.requirements.push(item1.requirement);

                                        }
                                    });
                                }
                            });
                            CommonService.getPersonReferences(vm.requirements, 'modifiedBy');
                            CommonService.getPersonReferences(vm.requirements, 'createdBy');
                            loadItemAttributeValues();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

                initDragger();
                dragMe();
            }

            function removeChildren(specSection) {
                if (specSection != null && specSection.sectionChildren != null && specSection.sectionChildren != undefined) {
                    angular.forEach(specSection.sectionChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.specItems.indexOf(specSection);
                    vm.specItems.splice(index + 1, specSection.sectionChildren.length);
                    specSection.sectionChildren = [];
                    specSection.expanded = false;
                }

                initDragger();
                dragMe();
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function showRequirementDetails(req) {
                var div = document.getElementById("specItemsView");
                $window.localStorage.setItem("lastSelectedSpecificationTab", JSON.stringify('details.sections'));
                $window.localStorage.setItem("lastSelectedRequirementHeight", JSON.stringify(div.scrollTop));
                $state.go('app.rm.requirements.details', {requirementId: req.id});
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                vm.recentlyVisited.objectId = req.id;
                vm.recentlyVisited.objectType = req.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);
                //launchUrl(url);
            }

            vm.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.showImage1 = showImage1;
            function showImage1(attribute) {
                var modal = document.getElementById('myModal21');
                var modalImg = document.getElementById('img031');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            var parsed = angular.element("<div></div>");
            var deleteSpecSectionDialogTitle = parsed.html($translate.instant("DELETE_SECTION")).html();
            var deleteSpecSectionDialogMessage = parsed.html($translate.instant("DELETE_SECTION_DIALOG_MESSAGE")).html();
            var specSectionDeletedMessage = parsed.html($translate.instant("SECTION_DELETE_MSG")).html();
            var deleteSpecReqDialogTitle = parsed.html($translate.instant("DELETE_REQUIREMENT")).html();
            var deleteSpecReqDialogMessage = parsed.html($translate.instant("DELETE_REQUIREMENT_DIALOG_MESSAGE")).html();
            var specReqDeletedMessage = parsed.html($translate.instant("REQUIREMENT_DELETE_MSG")).html();
            var searchValidation = parsed.html($translate.instant("SEARCH_MESSAGE_VALIDATION")).html();
            vm.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();
            var requirementsAddedToClipboard = parsed.html($translate.instant("REQUIREMENTS_ADDED_TO_CLIPBOARD")).html();

            $scope.searchTitle = parsed.html($translate.instant("SEARCH")).html();
            $scope.clearTitle = parsed.html($translate.instant("CLEAR")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();

            $scope.deleteSpecElement = function (item) {
                if (item.type == 'SECTION') {

                    var options = {
                        title: deleteSpecSectionDialogTitle,
                        message: deleteSpecSectionDialogMessage + " [ " + item.name + " ] " + " ?",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            SpecificationsService.deleteSpecElement(item.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(item.name + specSectionDeletedMessage);
                                    $rootScope.loadSpecification();
                                    if (item.parent != null) {
                                        angular.forEach(vm.specItems, function (item1) {
                                            if (item1.id == item.parent) {
                                                removeChildren(item1);
                                                item1.sectionChildren = [];
                                                item1.children = [];
                                                item1.expanded = true;
                                                toggleNode(item1);
                                                $timeout(function () {
                                                    item1.expanded = false;
                                                    toggleNode(item1);
                                                }, 100)
                                            }
                                        });
                                    }
                                    else {
                                        loadSpecSections();
                                    }

                                    $timeout(function () {
                                        SpecificationsService.getParentObject(item.parent).then(
                                            function (data) {
                                                angular.forEach(vm.specItems, function (spec) {
                                                    if (data.id == spec.id) {
                                                        spec.canAddSection = data.canAddSection;
                                                        spec.canAddRequirement = data.canAddRequirement;
                                                    }
                                                })
                                                reInitializeColResize();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                             })

                                    }, 1000)

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    });

                }
                if (item.type == 'REQUIREMENT') {
                    var options = {
                        title: deleteSpecReqDialogTitle,
                        message: deleteSpecReqDialogMessage + " [ " + item.requirement.name + " ] " + " ?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            SpecificationsService.deleteSpecElement(item.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(item.requirement.name + specReqDeletedMessage);
                                    $rootScope.loadSpecification();
                                    angular.forEach(vm.specItems, function (item1) {
                                        if (item1.id == item.parent) {
                                            removeChildren(item1);
                                            item1.sectionChildren = [];
                                            item1.children = [];
                                            item1.expanded = true;
                                            item1.requirementsEdit = item1.requirementsEdit - item.reqEdits;
                                            if (item1.parent != null) {
                                                removeEditFromParentSpecSection(item1, item.reqEdits);
                                            }

                                            toggleNode(item1);
                                            $timeout(function () {
                                                item1.expanded = false;
                                                toggleNode(item1);
                                            }, 100)
                                        }
                                    });

                                    $timeout(function () {
                                        SpecificationsService.getParentObject(item.parent).then(
                                            function (data) {
                                                angular.forEach(vm.specItems, function (spec) {
                                                    if (data.id == spec.id) {
                                                        spec.canAddSection = data.canAddSection;
                                                        spec.canAddRequirement = data.canAddRequirement;
                                                    }

                                                })
                                                reInitializeColResize();

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                             })

                                    }, 1000)

                                    // loadSpecSections();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    });
                }

            };

            var pageable = {
                page: 0,
                size: 1000,
                sort: {
                    field: "createdDate",
                    order: "ASC"
                }
            };

            vm.pageMode = "SECTIONS";
            vm.searchText = "";
            vm.freeTextSearch = freeTextSearch;
            vm.listStatus = ['NONE', 'PENDING', 'FINISHED'];
            vm.booleanValue = ['true', 'false'];
            vm.persons = [];
            function loadPersons() {
                LoginService.getAllLogins().then(
                    function (data) {
                        vm.persons = [];
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                        loadAllLovs();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadRequirementVersions = loadRequirementVersions;
            vm.reqVersions = [];
            function loadRequirementVersions() {
                vm.reqVersions = [];
                SpecificationsService.getAllRequirementVersions(specId).then(
                    function (data) {
                        angular.forEach(data, function (rev) {
                            if (rev == 0) {
                                rev = '-';
                            }
                            vm.reqVersions.push(rev);
                        })

                        if (validateLastReqJSON()) {
                            var reqHeight = JSON.parse($window.localStorage.getItem("lastSelectedRequirementHeight"));
                            if (reqHeight != null) {
                                $timeout(function () {
                                    $('#specItemsView').animate({
                                        scrollTop: reqHeight
                                    }, 'slow');
                                    $window.localStorage.setItem("lastSelectedRequirementHeight", "");
                                }, 200)
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.reqSearchFilter = {
                name: null,
                description: null,
                assignedTo: [],
                status: null,
                objectNumber: null,
                version: '',
                version1: '',
                searchQuery: null,
                specification: '',
                plannedFinishdate: null,
                attributeSearch: false
            };

            vm.reqMode = "REQUIREMENTS";

            function freeTextSearch(mode) {
                if (vm.reqSearchFilter.searchQuery == "") {
                    clearFilter()

                }
                else if (mode == "REQUIREMENTS") {
                    $(".JCLRgrips").css("display", "none");
                    $timeout(function () {
                        $scope.$broadcast('reInitializeColResizable', "");
                    }, 1000)
                    $rootScope.requirementFreeTextSearchFilterExist = true;
                    $rootScope.requirementFreeTextSearchFilter = vm.reqSearchFilter;
                    vm.loading = true;
                    vm.searchText = vm.reqSearchFilter.searchQuery;
                    vm.reqSearchFilter.attributeSearch = false;
                    if (vm.reqSearchFilter.version1 == '-') {
                        vm.reqSearchFilter.version = 0;
                    } else {
                        vm.reqSearchFilter.version = vm.reqSearchFilter.version1
                    }
                    if (vm.reqAttributeFilter.attributeId.length > 0) {
                        vm.reqAttributeFilter.searchAttributes = vm.searchAttributes;
                        vm.reqAttributeFilter.specification = $stateParams.specId;
                        vm.reqAttributeFilter.attributeSearch = true;
                        vm.pageMode = "REQUIREMENTS";
                        SpecificationsService.getRequirementAttributeSearch(specId, pageable, vm.reqAttributeFilter).then(
                            function (data) {
                                if (data == "") {
                                    vm.loading = false;
                                    vm.specRequirements = angular.copy(pagedResults)
                                } else {
                                    vm.specRequirements = data;
                                    vm.loading = false;
                                    loadSearchAttributeValues();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if (vm.reqSearchFilter.name != null || vm.reqSearchFilter.description != null || vm.reqSearchFilter.assignedTo.length > 0 || vm.reqSearchFilter.status != null || vm.reqSearchFilter.objectNumber != null || vm.reqSearchFilter.searchQuery != null || vm.reqSearchFilter.plannedFinishdate != null || vm.reqSearchFilter.version != "" || vm.reqSearchFilter.version1 != "") {
                        vm.pageMode = "REQUIREMENTS";
                        vm.reqSearchFilter.attributeSearch = false;
                        SpecificationsService.getRequirementSearch(specId, pageable, vm.reqSearchFilter).then(
                            function (data) {
                                vm.specRequirements = data;
                                vm.loading = false;
                                loadSearchAttributeValues();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);

                            }
                        )
                    } else {
                        $rootScope.showWarningMessage(searchValidation);
                        vm.loading = false;
                        vm.pageMode = "SECTIONS";
                        $rootScope.requirementFreeTextSearchFilter = null;
                        $rootScope.requirementFreeTextSearchFilterExist = false;
                        $rootScope.sectiontoggle = $application.sessionDataStorage.get(specId + "requirements");
                        if ($rootScope.sectiontoggle != null) {
                            vm.specItems = $rootScope.sectiontoggle;
                        }
                    }
                } else {
                    vm.pageMode = "SECTIONS";
                    $rootScope.requirementFreeTextSearchFilter = null;
                    $rootScope.requirementFreeTextSearchFilterExist = false;
                    loadSpecSections();
                }
            }

            vm.search = search;
            function search(object) {
                var index = vm.reqAttributeFilter.attributeId.indexOf(object.objectTypeAttribute.id);
                if (index == -1) {
                    vm.reqAttributeFilter.attributeId.push(object.objectTypeAttribute.id);
                }
                if (object.text == "" || object.longText == "" || object.integer == "" || object.double == "" || object.list == "" || object.date == "" || object.currency == "") {
                    var index = vm.reqAttributeFilter.attributeId.indexOf(object.objectTypeAttribute.id);
                    vm.reqAttributeFilter.attributeId.splice(index);
                }
            }

            vm.mListValueSelect = mListValueSelect;
            vm.removeMlistValue = removeMlistValue;

            function removeMlistValue(object, select) {
                var index = object.mlistValue.indexOf(select);
                object.mlistValue.splice(index);
            }

            function mListValueSelect(object, select) {
                object.mlistValue.push(select);
                var index = vm.reqAttributeFilter.attributeId.indexOf(object.id);
                if (index == -1) {
                    vm.reqAttributeFilter.attributeId.push(object.id);
                }
            }

            vm.attributeBoolean = attributeBoolean;
            function attributeBoolean(object) {
                if (object.objectTypeAttribute.dataType == 'BOOLEAN') {
                    object.booleanSearch = true;
                    var index = vm.reqAttributeFilter.attributeId.indexOf(object.id);
                    if (index == -1) {
                        vm.reqAttributeFilter.attributeId.push(object.id);
                    }
                }
                if (object.objectTypeAttribute.dataType == 'DOUBLE') {
                    object.doubleSearch = true;
                    var index = vm.reqAttributeFilter.attributeId.indexOf(object.id);
                    if (index == -1) {
                        vm.reqAttributeFilter.attributeId.push(object.id);
                    }
                }
                if (object.objectTypeAttribute.dataType == 'TIME') {
                    var index = vm.reqAttributeFilter.attributeId.indexOf(object.id);
                    if (index == -1) {
                        vm.reqAttributeFilter.attributeId.push(object.id);
                    }
                }

            }

            vm.requirementSearch = false;
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.reqAttributeFilter = {
                specification: '',
                attributeSearch: true,
                searchQuery: null,
                attributeId: [],
                searchAttributes: []
            };

            vm.attributeSearch = attributeSearch;
            function attributeSearch() {
                vm.reqAttributeFilter.searchAttributes = vm.searchAttributes;
                vm.reqAttributeFilter.specification = $stateParams.specId;
                vm.pageMode = "REQUIREMENTS";
                SpecificationsService.getRequirementAttributeSearch(specId, pageable, vm.reqAttributeFilter).then(
                    function (data) {
                        if (data == "") {
                            vm.specRequirements = angular.copy(pagedResults)
                        } else {
                            vm.specRequirements = data;
                            vm.loading = false;
                            loadSearchAttributeValues();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
                /* if (vm.reqAttributeFilter.text != null || vm.reqAttributeFilter.longText != null || vm.reqAttributeFilter.date != null || vm.reqAttributeFilter.integer != null || (vm.reqAttributeFilter.aBoolean != null && vm.reqAttributeFilter.booleanSearch != false) || (vm.reqAttributeFilter.aDouble != null && vm.reqAttributeFilter.doubleSearch != false) || vm.reqAttributeFilter.list != null || vm.reqAttributeFilter.time != null || vm.reqAttributeFilter.currency != null || vm.reqAttributeFilter.mlistValue.length > 0) {
                 vm.reqAttributeFilter.searchAttributes = vm.searchAttributes;
                 vm.pageMode = "REQUIREMENTS";
                 SpecificationsService.getRequirementAttributeSearch(specId, pageable, vm.reqAttributeFilter).then(
                 function (data) {
                 if (data == "") {
                 vm.specRequirements = angular.copy(pagedResults)
                 } else {
                 vm.specRequirements = data;
                 vm.loading = false;
                 loadSearchAttributeValues();
                 }
                 }
                 )
                 } /!*else {
                 $rootScope.showWarningMessage("Please enter attribute value to search")
                 }*!/*/
            }

            vm.clearAttFilter = clearAttFilter;
            function clearAttFilter() {
                vm.reqAttributeFilter = {
                    specification: '',
                    attributeSearch: false,
                    searchQuery: null,
                    attributeId: [],
                    searchAttributes: []
                };

                angular.forEach(vm.searchAttributes, function (attribute) {
                    attribute.text = null,
                        attribute.longText = null,
                        attribute.date = null,
                        attribute.integer = null,
                        attribute.aBoolean = false,
                        attribute.aDouble = 0.0,
                        attribute.list = null,
                        attribute.time = null,
                        attribute.currency = null,
                        attribute.attributeId = [],
                        attribute.mlistValue = [],
                        attribute.booleanSearch = false,
                        attribute.doubleSearch = false
                })
            }

            $scope.clear = function ($event, $select) {
                $rootScope.sectiontoggle = $application.sessionDataStorage.get(specId + "requirements");
                if ($rootScope.sectiontoggle != null) {
                    vm.specItems = $rootScope.sectiontoggle;
                    $select.selected = undefined;
                    //reset search query
                    $select.search = undefined;
                    //focus and open dropdown
                    $select.activate();

                }
                else {
                    loadSpecSections();
                    $select.selected = undefined;
                    //reset search query
                    $select.search = undefined;
                    //focus and open dropdown
                    $select.activate();

                }
            };

            vm.clearFilter = clearFilter;
            function clearFilter() {
                $(".JCLRgrips").css("display", "none");
                $timeout(function () {
                    $scope.$broadcast('reInitializeColResizable', "");
                }, 1000)
                vm.reqSearchFilter = {
                    name: null,
                    description: null,
                    assignedTo: [],
                    status: null,
                    objectNumber: null,
                    version: '',
                    version1: '',
                    searchQuery: null,
                    specification: '',
                    plannedFinishdate: null
                };
                vm.pageMode = "SECTIONS";
                $rootScope.requirementFreeTextSearchFilter = null;
                $rootScope.requirementFreeTextSearchFilterExist = false;
                $rootScope.sectiontoggle = $application.sessionDataStorage.get(specId + "requirements");
                if ($rootScope.sectiontoggle != null) {
                    vm.specItems = $rootScope.sectiontoggle;
                    clearAttFilter();
                }
                else {
                    loadSpecSections();
                    clearAttFilter();
                }

            }

            function loadAllLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.lovValues = [];
                        vm.lovs = data;
                        angular.forEach(vm.lovs, function (lov) {
                            angular.forEach(lov.values, function (value) {
                                vm.lovValues.push(value);
                            })

                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            var parsed = angular.element("<div></div>");
            $rootScope.showReqTypeAttributes = showReqTypeAttributes;
            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var revisionHistory = parsed.html($translate.instant("ITEM_DETAILS_REVISION_HISTORY")).html();
            var versionHistory = parsed.html($translate.instant("VERSION_HISTORY")).html();
            var requirementEditHistory = parsed.html($translate.instant("REQUIREMENT_EDIT_HISTORY")).html();
            var statusUpdateMsg = parsed.html($translate.instant("STATUS_UPDATE_MSG")).html();
            vm.editRequirementTitle = parsed.html($translate.instant("EDIT_REQUIREMENT_TITLE")).html();
            vm.editSectioTitle = parsed.html($translate.instant("EDIT_REQUIREMENT_TITLE")).html();
            vm.showRevisionHistoryTitle = parsed.html($translate.instant("SHOW_REVISION_HISTORY")).html();
            vm.showEditHistoryTitle = parsed.html($translate.instant("SHOW_REQUIREMENT_EDITHISTORY")).html();
            vm.showPromoteTitle = parsed.html($translate.instant("SHOW_PROMOTE_TITLE")).html();
            vm.showDemoteTitle = parsed.html($translate.instant("SHOW_DEMOTE_TITLE")).html();
            var requirementUpdated = parsed.html($translate.instant("REQUIREMENT_UPDATE")).html();
            var sectionUpdated = parsed.html($translate.instant("SECTION_UPDATE")).html();
            vm.deleteTitle = parsed.html($translate.instant("DELETE_TITLE")).html();
            var update = parsed.html($translate.instant("UPDATE")).html();
            var editSection = parsed.html($translate.instant("EDIT_SECTION")).html();

            vm.searchAttributes = [];

            var emptySearchAttribute = {
                objectTypeAttribute: null,
                text: null,
                longText: null,
                date: null,
                integer: null,
                aBoolean: false,
                aDouble: 0.0,
                list: null,
                time: null,
                currency: null,
                attributeId: [],
                mlistValue: [],
                attributeSearch: true,
                booleanSearch: false,
                doubleSearch: false
            };

            function showReqTypeAttributes() {
                var selecteAttrs = angular.copy(vm.selectedAttribute);
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/rm/requirements/reqTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/rm/requirements/reqTypeAttributesController',
                    controller: 'RequirementTypeAttributesController as reqTypeAttributesVm',
                    width: 500,
                    data: {
                        selectedAttributes: selecteAttrs
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttribute = result;

                        angular.forEach(vm.selectedAttribute, function (attrbute) {
                            var newSearchAttribute = angular.copy(emptySearchAttribute);
                            newSearchAttribute.objectTypeAttribute = attrbute;
                            vm.searchAttributes.push(newSearchAttribute);
                        })
                        // loadColResize();
                        /* $scope.$broadcast('reInitializeColResizable', result);*/
                        /* $timeout(function () {
                         $scope.$broadcast('reInitializeColResizable', "");
                         }, 1000)*/

                        $(".JCLRgrips").css("display", "none");
                        $timeout(function () {
                            $scope.$broadcast('reInitializeColResizable', "");
                        }, 1000)
                        $window.localStorage.setItem(specId + "specattributes", JSON.stringify(vm.selectedAttribute));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }

                        loadItemAttributeValues();
                        //loadSpecSections();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeReqAttribute = removeReqAttribute;
            function removeReqAttribute(att) {
                angular.forEach(vm.searchAttributes, function (attribute) {
                    if (attribute.objectTypeAttribute.id != null && attribute.objectTypeAttribute.id == att.id) {
                        vm.searchAttributes.splice(vm.searchAttributes.indexOf(attribute), 1);
                    } else if (attribute.objectTypeAttribute.name != null && attribute.objectTypeAttribute.name == att.name) {
                        vm.searchAttributes.splice(vm.searchAttributes.indexOf(attribute), 1);
                    }
                })
                vm.selectedAttribute.splice(vm.selectedAttribute.indexOf(att), 1);
                $window.localStorage.setItem(specId + "specattributes", JSON.stringify(vm.selectedAttribute));
                $(".JCLRgrips").css("display", "none");
                $timeout(function () {
                    $scope.$broadcast('reInitializeColResizable', "");
                }, 1000)
                //loadColResize();
                /*$scope.$broadcast('reInitializeColResizable', att);*/

                /*$timeout(function () {
                 $scope.$broadcast('reInitializeColResizable', "");
                 }, 1000)*/
            }

            function initDragger() {
                /*
                 $timeout(function () {
                 var el = document.getElementById('specItemsTable');
                 var dragger = tableDragger(el, {
                 mode: 'row',
                 dragHandler: '.handle',
                 onlyBody: true,
                 animation: 300
                 });
                 dragger.on('drop', function (from, to) {

                 });
                 }, 1000);
                 */
            }

            function editSection(item) {
                var options = {
                    title: editSection,
                    showMask: true,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/editSectionView.jsp',
                    controller: 'EditSectionController as editSectionVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/items/editSectionController',
                    width: 500,
                    data: {
                        sectionDetails: item
                    },
                    buttons: [
                        {text: update, broadcast: 'app.section.edit'}
                    ],
                    callback: function (data) {
                        $(".JCLRgrips").css("display", "none");
                        $timeout(function () {
                            $scope.$broadcast('reInitializeColResizable', "");
                        }, 1000)
                        if (data != null) {
                            item.name = data.name;
                            item.description = data.description;
                            $rootScope.showSuccessMessage(sectionUpdated);
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function editRequirement(specRequirement) {
                var options = {
                    title: "Edit Requirement",
                    showMask: true,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/editRequirementView.jsp',
                    controller: 'EditRequirementController as editReqVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/items/editRequirementController',
                    width: 700,
                    data: {
                        requirementDetails: specRequirement.requirement
                    },
                    buttons: [
                        {text: "Update", broadcast: 'app.requirement.edit'}
                    ],
                    callback: function (result) {

                        specRequirement.reqEdits = result.requirementEditLength;

                        angular.forEach(vm.specItems, function (specItem) {
                            if (specItem.id == specRequirement.parent) {
                                specItem.requirementsEdit = specItem.requirementsEdit + 1;

                                if (specItem.parent != null) {
                                    visitParentSpecSection(specItem);
                                }
                            }
                        })

                        $rootScope.showSuccessMessage(requirementUpdated);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function visitParentSpecSection(specSection) {
                angular.forEach(vm.specItems, function (specItem) {
                    if (specItem.id == specSection.parent) {
                        specItem.requirementsEdit = specItem.requirementsEdit + 1;

                        if (specItem.parent != null) {
                            visitParentSpecSection(specItem);
                        }
                    }
                })
            }

            function removeEditFromParentSpecSection(specSection, reqEdits) {
                angular.forEach(vm.specItems, function (specItem) {
                    if (specItem.id == specSection.parent) {
                        specItem.requirementsEdit = specItem.requirementsEdit - reqEdits;

                        if (specItem.parent != null) {
                            removeEditFromParentSpecSection(specItem, reqEdits);
                        }
                    }
                })
            }

            function showRevisionHistory(requirement) {
                var options = {
                    title: versionHistory,
                    template: 'app/desktop/modules/rm/specification/all/specRequirementRevisionHistoryView.jsp',
                    controller: 'SpecRequirementRevisionHistoryController as requirementRevisionHistoryVm',
                    resolve: 'app/desktop/modules/rm/specification/all/specRequirementRevisionHistoryController',
                    width: 700,
                    data: {
                        requirementId: requirement
                    },
                    callback: function (msg) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showEditHistory(item) {
                var options = {
                    title: requirementEditHistory,
                    template: 'app/desktop/modules/rm/specification/all/requirementVersionHistoryView.jsp',
                    controller: 'RequirementVersionHistoryController as requirementEditHistoryVm',
                    resolve: 'app/desktop/modules/rm/specification/all/requirementVersionHistoryController',
                    width: 700,
                    data: {

                        requirementId: item
                    },
                    callback: function (data) {
                        item.requirement = data.requirements;
                        angular.forEach(vm.specItems, function (specItem) {
                            if (specItem.id == item.parent && data.status == "FINAL") {
                                specItem.requirementsEdit = specItem.requirementsEdit - item.reqEdits;

                                if (specItem.parent != null) {
                                    removeEditFromParentSpecSection(specItem, item.reqEdits);
                                }
                            }

                        })
                        $(".JCLRgrips").css("display", "none");
                        $timeout(function () {
                            $scope.$broadcast('reInitializeColResizable', "");
                        }, 1000)
                        item.reqEdits = 0;

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function promoteRequirement(item) {
                SpecificationsService.promoteRequirement(item.requirement.id).then(
                    function (data) {
                        item.requirement.lifecyclePhase = data.lifecyclePhase;
                        $rootScope.showSuccessMessage(statusUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function demoteRequirement(item) {
                SpecificationsService.demoteRequirement(item.requirement.id).then(
                    function (data) {
                        item.requirement.lifecyclePhase = data.lifecyclePhase;
                        $rootScope.showSuccessMessage(statusUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSearchAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.specRequirements.content, function (item) {
                    if (item.requirement != undefined && item.requirement.objectType == "REQUIREMENT") {
                        vm.itemIds.push(item.requirement.id);
                    }
                });
                angular.forEach(vm.selectedAttribute, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedAttribute, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.specRequirements.content, function (item) {

                                if (item.requirement != undefined && item.requirement.objectType == "REQUIREMENT") {
                                    var attributes = [];

                                    var itemAttributes = vm.selectedObjectAttributes[item.requirement.id];
                                    if (itemAttributes != null && itemAttributes != undefined) {
                                        attributes = attributes.concat(itemAttributes);
                                    }
                                    angular.forEach(attributes, function (attribute) {
                                        var selectatt = map.get(attribute.id.attributeDef);
                                        if (selectatt != null) {
                                            var attributeName = selectatt.id;
                                            if (selectatt.dataType == 'TEXT') {
                                                item[attributeName] = attribute.stringValue;
                                            } else if (selectatt.dataType == 'LONGTEXT') {
                                                item[attributeName] = attribute.longTextValue;
                                            } else if (selectatt.dataType == 'RICHTEXT') {
                                                item[attributeName] = attribute;
                                            } else if (selectatt.dataType == 'INTEGER') {
                                                item[attributeName] = attribute.integerValue;
                                            } else if (selectatt.dataType == 'BOOLEAN') {
                                                item[attributeName] = attribute.booleanValue;
                                            } else if (selectatt.dataType == 'DOUBLE') {
                                                item[attributeName] = attribute.doubleValue;
                                            } else if (selectatt.dataType == 'DATE') {
                                                item[attributeName] = attribute.dateValue;
                                            } else if (selectatt.dataType == 'LIST') {
                                                if (attribute.listValue != null) {
                                                    item[attributeName] = attribute.listValue;
                                                } else if (attribute.mlistValue.length > 0) {
                                                    item[attributeName] = attribute.mlistValue;
                                                }
                                            } else if (selectatt.dataType == 'TIME') {
                                                item[attributeName] = attribute.timeValue;
                                            } else if (selectatt.dataType == 'TIMESTAMP') {
                                                item[attributeName] = attribute.timestampValue;

                                            } else if (selectatt.dataType == 'CURRENCY') {
                                                item[attributeName] = attribute.currencyValue;
                                                if (attribute.currencyType != null) {
                                                    item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                                }
                                            } else if (selectatt.dataType == 'ATTACHMENT') {
                                                var revisionAttachmentIds = [];
                                                if (attribute.attachmentValues.length > 0) {
                                                    angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                        revisionAttachmentIds.push(attachmentId);
                                                    });
                                                    AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                        function (data) {
                                                            vm.revisionAttachments = data;
                                                            item[attributeName] = vm.revisionAttachments;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                         }
                                                    )
                                                }
                                            } else if (selectatt.dataType == 'IMAGE') {
                                                if (attribute.imageValue != null) {
                                                    item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                }
                                            } else if (selectatt.dataType == 'OBJECT') {
                                                if (selectatt.refType != null) {
                                                    if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                                        ItemService.getItem(attribute.refValue).then(
                                                            function (itemValue) {
                                                                item[attributeName] = itemValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                                        ItemService.getRevisionId(attribute.refValue).then(
                                                            function (revisionValue) {
                                                                item[attributeName] = revisionValue;
                                                                ItemService.getItem(revisionValue.itemMaster).then(
                                                                    function (data) {
                                                                        item[attributeName].itemMaster = data.itemNumber;
                                                                    }, function (error) {
                                                                        $rootScope.showErrorMessage(error.message);
                                                                     }
                                                                )
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                                        ECOService.getECO(attribute.refValue).then(
                                                            function (changeValue) {
                                                                item[attributeName] = changeValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                                        WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                            function (workflowValue) {
                                                                item[attributeName] = workflowValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                                        MfrService.getManufacturer(attribute.refValue).then(
                                                            function (mfrValue) {
                                                                item[attributeName] = mfrValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                                        MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                            function (mfrPartValue) {
                                                                item[attributeName] = mfrPartValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                                        CommonService.getPerson(attribute.refValue).then(
                                                            function (person) {
                                                                item[attributeName] = person;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                             }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    })

                                }
                            })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            var actualRow = null;
            var targetObject = null
            vm.requirementsChecking = [];
            vm.sectionsChecking = [];
            vm.sequencesChecking = [];
            vm.subSectionsChecking = [];
            function updateSpecChildrensAfterDrop(targetObject, actualRow) {

                if (targetObject.parent == null) {
                    var dragRowParent = actualRow.parent;
                    actualRow.parent = targetObject.id;
                    SpecificationsService.updateSpecRequirement(actualRow, dragRowParent).then(
                        function (data) {
                            $(vm.draggingElementHide).hide();
                            removeChildren(targetObject);
                            targetObject.sectionChildren = [];
                            targetObject.children = [];

                            angular.forEach(vm.specItems, function (item) {
                                if (item.id == dragRowParent) {
                                    removeChildren(item);
                                    item.sectionChildren = [];
                                    item.children = [];
                                    targetObject.expanded = false;
                                    toggleNode(targetObject);
                                    $timeout(function () {
                                        item.expanded = false;
                                        toggleNode(item);
                                    }, 100)
                                }
                            });

                        }, function (error) {
                            $(vm.draggingElementHide).hide();
                            $rootScope.showWarningMessage(error.message);
                            removeChildren(targetObject);
                            targetObject.sectionChildren = [];
                            targetObject.children = [];

                            angular.forEach(vm.specItems, function (item) {
                                if (item.id == dragRowParent) {
                                    removeChildren(item);
                                    item.sectionChildren = [];
                                    item.children = [];
                                    targetObject.expanded = false;
                                    toggleNode(targetObject);
                                    $timeout(function () {
                                        item.expanded = false;
                                        toggleNode(item);
                                    }, 100)
                                }
                            });

                        }
                    )
                }

                //If Target Object is Not Null
                else {
                    var dragRowParent = actualRow.parent;
                    actualRow.parent = targetObject.id;
                    SpecificationsService.updateSpecRequirement(actualRow, dragRowParent).then(
                        function (data) {
                            $(vm.draggingElementHide).hide();
                            removeChildren(targetObject);
                            targetObject.sectionChildren = [];
                            targetObject.children = [];

                            angular.forEach(vm.specItems, function (item) {
                                if (item.id == dragRowParent) {
                                    removeChildren(item);
                                    item.sectionChildren = [];
                                    item.children = [];
                                    targetObject.expanded = false;
                                    toggleNode(targetObject);
                                    $timeout(function () {
                                        item.expanded = false;
                                        toggleNode(item);
                                    }, 100)
                                }
                            });

                        }, function (error) {
                            $(vm.draggingElementHide).hide();
                            $rootScope.showWarningMessage(error.message);
                            removeChildren(targetObject);
                            targetObject.sectionChildren = [];
                            targetObject.children = [];

                            angular.forEach(vm.specItems, function (item) {
                                if (item.id == dragRowParent) {
                                    removeChildren(item);
                                    item.sectionChildren = [];
                                    item.children = [];
                                    targetObject.expanded = false;
                                    toggleNode(targetObject);
                                    $timeout(function () {
                                        item.expanded = false;
                                        toggleNode(item);
                                    }, 100)
                                }
                            });

                        }
                    )
                }

            }

            function noDuplicateSequences(targetObject, actualRow) {
                //If Target Object parent is null
                vm.sequencesChecking = [];
                vm.requirementsChecking = [];
                vm.subSectionsChecking = [];
                if (targetObject != null || targetObject != undefined && actualRow != null || actualRow != undefined) {
                    if (targetObject.sectionChildren != null || targetObject.sectionChildren != undefined || targetObject.sectionChildren.length > 0) {
                        angular.forEach(targetObject.sectionChildren, function (item1) {
                            if (item1.requirement != undefined && item1.requirement.objectType == "REQUIREMENT") {
                                if (actualRow.seqNumber == item1.seqNumber) {
                                    vm.sequencesChecking.push(item1.seqNumber);
                                }
                            }

                        });
                        if (vm.sequencesChecking.length == 0) {
                            updateSpecChildrensAfterDrop(targetObject, actualRow)
                        }
                        else {
                            $(vm.draggingElementHide).hide();
                            removeChildren(targetObject);
                            targetObject.sectionChildren = [];
                            targetObject.children = [];

                            angular.forEach(vm.specItems, function (item) {
                                if (item.id == actualRow.parent) {
                                    removeChildren(item);
                                    item.sectionChildren = [];
                                    item.children = [];
                                    targetObject.expanded = true;
                                    toggleNode(targetObject);
                                    $timeout(function () {
                                        item.expanded = false;
                                        toggleNode(item);
                                    }, 100)
                                }
                            });
                        }

                    }

                }

                else {
                    updateSpecChildrensAfterDrop(targetObject, actualRow)
                }

            }

            module.directive('dragMe', dragMe)
            module.directive('dropOnMe', dropOnMe);
            var draggingElement = null;
            vm.draggedId = null;
            vm.dropPlaceId = null;
            vm.actualRow = null;
            vm.targetObject = null;
            vm.draggingElementHide = null;
            function dragMe() {
                var DDO = {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        element.prop('draggable', true);
                        element.on('dragstart', function (event) {
                            draggingElement = element;
                            console.log("FROM:" + element[0].id);
                            vm.draggedId = element[0].id;
                            vm.actualRow = vm.specItems[vm.draggedId];
                            if (vm.actualRow.type == 'SECTION') {
                                element.prop('draggable', false);
                                event.preventDefault();
                            }
                            else {
                                element.prop('draggable', true);
                                event.dataTransfer.setData('text', event.target.id)
                            }

                        });
                    }
                };
                return DDO;
            }

            function dropOnMe() {
                vm.specItems = [];
                var lastDragOverId = "";
                var lastDragOverElem = null;
                var DDO = {
                    restrict: 'A',
                    link: function (scope, element, attrs) {

                        element.on('dragover', function (event) {
                            event.preventDefault();
                            var id = element[0].id;
                            if (lastDragOverId != id) {
                                element.removeClass('hover');
                                if (lastDragOverElem != null) {
                                    element.removeClass('hover');
                                }
                                element.addClass('hover');
                                lastDragOverElem = element;
                                lastDragOverId = id;
                            }
                        });
                        element.on('drop', function (event) {
                            event.preventDefault();

                            console.log("TO:" + element[0].id);
                            vm.dropPlaceId = element[0].id;

                            if (draggingElement != null) {
                                //Get Drag Object and Target Object
                                vm.actualRow = vm.specItems[vm.draggedId];
                                vm.targetObject = vm.specItems[vm.dropPlaceId];
                                console.log("Dropping...");
                                lastDragOverElem.removeClass('hover');
                                if (vm.targetObject != null || vm.targetObject != undefined) {
                                    //If target Object is SECTION
                                    if (vm.targetObject.type == 'SECTION') {
                                        noDuplicateSequences(vm.targetObject, vm.actualRow);
                                        vm.draggingElementHide = draggingElement[0];
                                        draggingElement[0].parentNode.insertBefore(draggingElement[0], element[0].nextSibling);
                                        lastDragOverElem.removeClass('hover');
                                        $(vm.draggingElementHide).hide();
                                        draggingElement = null;

                                    }

                                    if (vm.targetObject.type == vm.actualRow.type) {
                                        draggingElement[0].parentNode.insertBefore(draggingElement[0], element[0].nextSibling);
                                        lastDragOverElem.removeClass('hover');
                                        draggingElement = null;
                                    }
                                }

                            }
                        });
                    }
                };
                return DDO;
            }

            /*    Show Modal dialogue for RichText*/
            $scope.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                vm.modalValue = data
                mymodal.modal('show');
            }

            /* -----  ReInitialize columns */
            vm.reInitializeColResize = reInitializeColResize;
            function reInitializeColResize() {
                $(".JCLRgrips").css("display", "none");
                $timeout(function () {
                    $scope.$broadcast('reInitializeColResizable', "");
                }, 1000)
            }

            vm.compareVersion = compareVersion;
            var sidePanelTitle = $translate.instant("COMPARE");

            function compareVersion(requirement) {
                var options = {
                    title: sidePanelTitle,
                    showMask: true,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/compareVersionView.jsp',
                    controller: 'CompareVersionController as compareVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/items/compareVersionController',
                    width: 800,
                    data: {
                        selectedReq: requirement
                    }/*,
                     buttons: [
                     {text: update, broadcast: 'app.section.edit'}
                     ],
                     callback: function (data) {
                     if (data != null) {
                     item.name = data.name;
                     item.description = data.description;
                     $rootScope.showSuccessMessage(sectionUpdated);
                     }
                     }*/
                };

                $rootScope.showSidePanel(options);
            }

            vm.selectRequirement = selectRequirement;
            vm.selectedRequirements = [];

            function selectRequirement(req) {
                if (req.selected) {
                    vm.selectedRequirements.push(req);
                } else {
                    vm.selectedRequirements.splice(vm.selectedRequirements.indexOf(req), 1);
                }

                if (vm.selectedRequirements.length > 0) {
                    $rootScope.showRequirementsCopyToClipBoard = true;
                } else {
                    $rootScope.showRequirementsCopyToClipBoard = false;
                }
            }

            $rootScope.copyRequirementsToClipBoard = copyRequirementsToClipBoard;
            $rootScope.clearAndCopyRequirementsToClipBoard = clearAndCopyRequirementsToClipBoard;

            function copyRequirementsToClipBoard() {
                angular.forEach(vm.selectedRequirements, function (req) {
                    req.selected = false;
                    $application.clipboard.deliverables.requirementIds.push(req.requirement.id);
                })
                $rootScope.clipBoardRequirements = $application.clipboard.deliverables.requirementIds;
                $rootScope.clipBoardDeliverables.requirementIds = $application.clipboard.deliverables.requirementIds;
                vm.selectedRequirements = [];
                $rootScope.showRequirementsCopyToClipBoard = false;
                $rootScope.showSuccessMessage(requirementsAddedToClipboard);
            }

            function clearAndCopyRequirementsToClipBoard() {
                $application.clipboard.deliverables.requirementIds = [];
                copyRequirementsToClipBoard();
            }


            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('item-thumbnail' + item.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + item.id);
                $("#thumbnail-image" + item.id).width($('#thumbnail-view' + item.id).outerWidth());
                $("#thumbnail-image" + item.id).height($('#thumbnail-view' + item.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            $rootScope.sectiontoggle = [];
            (function () {
                loadPersons();
                $scope.$on('app.spec.imported', function (event, data) {
                    loadSpecSections();
                });

                $window.onresize = function () {
                    reInitializeColResize();
                    $scope.$apply();
                }

                $scope.$on('app.spec.tabactivated', function (event, data) {
                    /*loadColResize();*/

                    if (data.tabId == 'details.sections') {
                        $timeout(function () {
                            reInitializeColResize();
                        }, 1000)
                        loadRequirementVersions();
                        angular.forEach($application.currencies, function (data) {
                            currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                        });

                        if (validateJSON()) {
                            //Get SessionStorage data
                            $rootScope.sectiontoggle = $application.sessionDataStorage.get(specId + "requirements");
                            var setAttributes = JSON.parse($window.localStorage.getItem(specId + "specattributes"));
                        } else {
                            var setAttributes = null;
                        }
                        //SessionStorage Object checking
                        if ($rootScope.sectiontoggle != null) {
                            vm.specItems = [];
                            //var a = $rootScope.selectedRequirement;
                            //vm.specItems = $rootScope.sectiontoggle;
                            if ($rootScope.selectedRequirement != null || $rootScope.selectedRequirement != undefined) {
                                angular.forEach($rootScope.sectiontoggle, function (item) {
                                    if (item.type == 'REQUIREMENT') {
                                        if (item.requirement.id == $rootScope.selectedRequirement.id) {
                                            item.requirement = $rootScope.selectedRequirement;
                                        }
                                    }

                                    vm.specItems.push(item);
                                })
                                // reInitializeColResize();
                            }
                            else {
                                vm.specItems = $rootScope.sectiontoggle;
                                //reInitializeColResize();
                            }
                        }
                        else {
                            dragMe();
                            loadSpecSections();
                        }
                        if (setAttributes != null && setAttributes != undefined) {
                            angular.forEach(setAttributes, function (setAtt) {
                                if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                                    vm.objectIds.push(setAtt.id);
                                }
                            });
                            ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                                function (data) {
                                    if (data.length == 0) {
                                        setAttributes = null;
                                        vm.searchAttributes = [];
                                        $window.localStorage.setItem(specId + "specattributes", "");
                                        vm.selectedAttribute = setAttributes;
                                        angular.forEach(vm.selectedAttribute, function (attrbute) {
                                            var newSearchAttribute = angular.copy(emptySearchAttribute);
                                            newSearchAttribute.objectTypeAttribute = attrbute;
                                            vm.searchAttributes.push(newSearchAttribute);
                                        })
                                        loadItemAttributeValues();

                                    } else {
                                        vm.searchAttributes = [];
                                        vm.selectedAttribute = setAttributes;
                                        angular.forEach(vm.selectedAttribute, function (attrbute) {
                                            var newSearchAttribute = angular.copy(emptySearchAttribute);
                                            newSearchAttribute.objectTypeAttribute = attrbute;
                                            vm.searchAttributes.push(newSearchAttribute);
                                        })
                                        loadItemAttributeValues();
                                    }

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                        if (!$rootScope.requirementFreeTextSearchFilterExist) {
                            /* loadSpecSections();*/
                        } else {
                            vm.reqSearchFilter = $rootScope.requirementFreeTextSearchFilter;

                            freeTextSearch("REQUIREMENTS");
                        }

                    }
                });
            })();
        }
    }
)
;