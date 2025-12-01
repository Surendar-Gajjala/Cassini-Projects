define([
        'app/desktop/modules/pm/pm.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/pm/project/wbsService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('WbsController', WbsController);

        function WbsController($scope, $rootScope, $timeout, $state, ProjectService, $window, AttributeAttachmentService, CommonService, ObjectAttributeService, $stateParams, $q, TaskService, DialogService, $cookies, WbsService) {
            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            $rootScope.viewInfo.title = "WBS";

            var vm = this;

            vm.toggleNode = toggleNode;

            vm.valid = true;
            vm.hasError = false;
            vm.createWbs = createWbs;
            vm.percentageComplete = 0.0;
            vm.isNew = false;
            vm.loading = true;
            vm.addRow = addRow;
            vm.cancelRow = cancelRow;
            vm.deleteRow = deleteRow;
            vm.selectItem = selectItem;
            vm.cancelWbs = cancelWbs;
            vm.project = parseInt($stateParams.projectId);
            vm.hide = false;
            var lastSelectedItem = null;
            vm.updateWbs = updateWbs;
            vm.wbsData = null;
            var maxIndex = 0;
            var wbsMap = new Hashtable();
            var wbsItemMap = new Hashtable();
            var wbs = null;

            function cancelWbs() {
                wbs = {};
            }

            function showInput() {
                vm.show = true;
            }

            function createWbs(wbs) {
                if (validate(wbs)) {
                    lastSelectedItem = wbs.parent;
                    wbs.assignedToObject = wbs.assignedTo;
                    wbs.assignedTo = wbs.assignedTo.id;
                    if (lastSelectedItem.level != -1) {
                        wbs.parent = lastSelectedItem.id;
                        WbsService.getChildWbsByName($stateParams.projectId, wbs.name, wbs.parent).then(
                            function (wbsName) {
                                vm.existedWbsName = wbsName;
                                if (vm.existedWbsName != null && vm.existedWbsName != "" && vm.existedWbsName != undefined) {
                                    $rootScope.showWarningMessage(wbs.name + " : WBS Name already exists");
                                } else {
                                    WbsService.createWbs($stateParams.projectId, wbs).then(
                                        function (data) {
                                            wbs.id = data.id;
                                            wbs.percentageComplete = data.percentageComplete;
                                            wbs.parent = lastSelectedItem;
                                            if (wbs.parent != null) {
                                                wbs.parent.hasBom = true;
                                                wbs.parent.expanded = true;
                                            }

                                            wbs.isNew = false;
                                            wbs.editMode = false;
                                            //wbs.expanded = false;
                                            wbs.expanded = true;
                                            wbs.level = lastSelectedItem.level + 1;
                                            $rootScope.showSuccessMessage("Child WBS created successfully");
                                            wbsMap.put(wbs.name, wbs.id);
                                            saveAttributes(wbs);
                                        });
                                }
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    } else {
                        WbsService.getParentWbsByName($stateParams.projectId, wbs.name).then(
                            function (wbsName) {
                                vm.existedWbsName = wbsName;
                                if (vm.existedWbsName != null && vm.existedWbsName != "" && vm.existedWbsName != undefined) {
                                    $rootScope.showWarningMessage(wbs.name + " : WBS Name already exists");
                                } else {
                                    wbs.parent = null;
                                    WbsService.createWbs($stateParams.projectId, wbs).then(
                                        function (data) {
                                            wbs.id = data.id;
                                            wbs.percentageComplete = data.percentageComplete;
                                            wbs.parent = {level: -1};
                                            wbs.isNew = false;
                                            //wbs.expanded = false;
                                            wbs.expanded = true;
                                            wbs.editMode = false;

                                            $rootScope.showSuccessMessage("WBS created successfully");
                                            calculateWeightage();
                                            lastSelectedItem = null;
                                            saveAttributes(wbs);
                                        }
                                    );
                                }
                            }
                        );
                    }

                }
                else {
                    vm.hasError = true;
                }
            }

            function addwbsAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PROJECT', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            function saveAttributes(wbs) {
                vm.imageAttributes = [];
                var images = new Hashtable();
                vm.allAttributes = [];
                angular.forEach(wbs.requiredAttributes, function (reqatt) {
                    vm.allAttributes.push(reqatt);
                });
                angular.forEach(wbs.attributes, function (reqatt) {
                    vm.allAttributes.push(reqatt);
                });

                if (vm.allAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.allAttributes, function (attribute) {
                        attribute.id.objectId = wbs.id;
                        if (attribute.timeValue != null) {
                            attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                        }
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addwbsAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(wbs.id, vm.allAttributes).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {

                                            }
                                        )
                                    })
                                }
                                $rootScope.showSuccessMessage(wbs.name + " : WBS created successfully");

                                loadWbs();
                                loadObjectAttributeDefs();
                                $rootScope.hideSidePanel();
                                $rootScope.hideBusyIndicator();

                            },
                            function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }, 3000);
                } else {
                    $rootScope.showSuccessMessage(wbs.name + " : WBS created successfully");
                    $rootScope.hideSidePanel();
                    $rootScope.hideBusyIndicator();

                }
            }

            function calculateWeightage() {
                vm.sum = Number(0);
                vm.wbsParents = [];
                angular.forEach(vm.wbsData, function (wbs) {
                    wbsMap.put(wbs.name, wbs.id);
                    wbs.level = 0;
                    if (wbs.parent == null || wbs.parent.level == -1) {
                        wbs.level = 0;
                        vm.sum += Number(wbs.weightage);
                        vm.wbsParents.push(wbs);
                    }
                });
            }

            function validate(wbs) {
                vm.hasError = false;
                vm.valid = true;
                var parentPlannedStartDate = moment(wbs.parent.plannedStartDate, 'DD/MM/YYYY');
                var parentPlannedFinishDate = moment(wbs.parent.plannedFinishDate, 'DD/MM/YYYY');
                if (wbs.name == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Name cannot be empty");
                }
                /*else if (wbsMap.get(vm.wbs.name) != null) {
                 vm.valid = false;
                 $rootScope.showWarningMessage("{0} name already exists".format(vm.wbs.name));
                 }*/
                else if (wbs.weightage == 0) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Please enter +ve number for WBS Weightage");
                }
                else if (wbs.weightage == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Weightage cannot be empty");
                }
                else if (wbs.weightage < 0) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Please enter +ve number for WBS Weightage");
                }
                else if (wbs.weightage > 100) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Weightage should not exceed 100");
                }
                else if (isNaN(wbs.weightage)) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Please enter +ve number for WBS Weightage");
                }
                else if (wbs.plannedStartDate == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Start Date cannot be empty");
                }
                else if (wbs.plannedFinishDate == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Finish Date cannot be empty");
                }

                else if (lastSelectedItem == null) {
                    vm.wbsParents.push(wbs);
                    angular.forEach(vm.wbsParents, function (parent) {
                        if (parent.id == null || parent.id == undefined || parent.id == "") {
                            if (parent.weightage > 100 - vm.sum) {
                                vm.valid = false;
                                $rootScope.showErrorMessage("Total WBS Weightage should not exceed 100 ");
                            }
                        }
                    });
                }
                else if (lastSelectedItem != null && lastSelectedItem.children != null) {
                    var weight = 0;
                    angular.forEach(lastSelectedItem.children, function (child) {
                        weight = weight + parseInt(child.weightage);
                    });
                    if (weight > 100) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Total Children Weightage should not exceed 100");
                    }
                } else if (wbs.plannedStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY');
                    var todayDate = moment(todayStr, 'DD/MM/YYYY');
                    var projectPlannedStartDate = moment($scope.$parent.project.plannedStartDate, 'DD/MM/YYYY');
                    var projectPlannedFinishDate = moment($scope.$parent.project.plannedFinishDate, 'DD/MM/YYYY');
                    var plannedStartDate = moment(wbs.plannedStartDate, 'DD/MM/YYYY');
                    var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);
                    if (!val1) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Start Date should be on (or) after Today's Date");
                    }
                    if (wbs.parent.level != -1 && (plannedStartDate.isBefore(projectPlannedStartDate) || plannedStartDate.isAfter(projectPlannedFinishDate))) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Start Date should be in b/w project Planned Start & Finish Date");
                    }

                }
                if (wbs.plannedStartDate != null && wbs.plannedFinishDate != null) {
                    var plannedFinishDate = moment(wbs.plannedFinishDate, 'DD/MM/YYYY');
                    var plannedStartDate = moment(wbs.plannedStartDate, 'DD/MM/YYYY');
                    var projectPlannedStartDate = moment($scope.$parent.project.plannedStartDate, 'DD/MM/YYYY');
                    var projectPlannedFinishDate = moment($scope.$parent.project.plannedFinishDate, 'DD/MM/YYYY');
                    if (lastSelectedItem != null) {
                        var parentStartDate = moment(lastSelectedItem.plannedStartDate, 'DD/MM/YYYY');
                        var parentFinishDate = moment(lastSelectedItem.plannedFinishDate, 'DD/MM/YYYY');
                        if (lastSelectedItem.plannedStartDate != null && lastSelectedItem.plannedStartDate != undefined && wbs.parent.level != -1 && parentStartDate.isAfter(plannedStartDate)) {
                            vm.valid = false;
                            $rootScope.showErrorMessage("Child Planned Start Date should be on or after Parent Planned Start Date");
                        }
                        if (lastSelectedItem.plannedStartDate != null && lastSelectedItem.plannedStartDate != undefined && wbs.parent.level != -1 && plannedStartDate.isAfter(parentFinishDate)) {
                            vm.valid = false;
                            $rootScope.showErrorMessage("Child Planned Finish Date should be before Parent Planned Finish Date");
                        }
                    }
                    var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate) || plannedFinishDate.isBefore(projectPlannedStartDate);
                    if (!val) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Finish Date should be after Planned Start Date ");
                    }
                    if (plannedStartDate.isBefore(projectPlannedStartDate) || plannedStartDate.isAfter(projectPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Start Date should be in between Project Planned Start & Finish Date");
                    }
                    if (plannedFinishDate.isAfter(projectPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage(" Planned Finish Date should not exceed project Planned Finish Date");
                    }

                    if (wbs.parent.level != -1 && plannedFinishDate.isAfter(parentPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Finish Date should not exceed Parent Planned Finish Date");
                    }
                    var val2 = plannedStartDate.isBefore(parentPlannedStartDate) || plannedStartDate.isAfter(parentPlannedFinishDate);
                    if (val2 && wbs.parent.level != -1) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Start Date should be in b/w Parent Planned Start & Finish Date")
                    }

                } else if (wbs.actualStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY');
                    var todayDate = moment(todayStr, 'DD/MM/YYYY');
                    var actualStartDate = moment(wbs.actualStartDate, 'DD/MM/YYYY');
                    var val1 = actualStartDate.isSame(todayDate) || actualStartDate.isAfter(todayDate);
                    if (!val1) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Actual Start Date should be on (or) after Today's Date");
                    }
                }

                if (wbs.actualStartDate != null && wbs.actualFinishDate != null) {
                    var actualFinishDate = moment(wbs.actualFinishDate, 'DD/MM/YYYY');
                    var actualStartDate = moment(wbs.actualStartDate, 'DD/MM/YYYY');
                    var val = actualFinishDate.isSame(actualStartDate) || actualFinishDate.isAfter(actualStartDate);
                    if (!val) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Actual Finish Date should be after Actual Start Date");
                    }
                }

                return vm.valid;
            }

            function selectItem(wbs) {
                maxIndex = 0;
                if (wbs.isNew == true || wbs.editMode == true) {
                    return;
                }
                if (lastSelectedItem != null && lastSelectedItem != wbs) {
                    lastSelectedItem.selected = false;
                    lastSelectedItem = null;
                }
                wbs.selected = !wbs.selected;
                if (wbs.selected == true) {
                    lastSelectedItem = wbs;
                    if (wbs.children == 0) {

                    }
                }
                else {
                    wbs.selected = false;
                    lastSelectedItem = null;
                }
            }

            function hasNewRow() {
                var flag = false;
                angular.forEach(vm.wbsData, function (wbs) {
                    if (wbs.isNew == true) {
                        flag = true;
                    }
                });
                return flag;
            }

            function addRow() {
                var index = 0;

                /*   if (hasNewRow()) {
                 $rootScope.showWarningMessage("There is already a new row, cannot add new row");
                 return;
                 }*/
                var newRow = {
                    parent: null,
                    project: $stateParams.projectId,
                    name: null,
                    weightage: null,
                    assignedTo: null,
                    plannedStartDate: null,
                    plannedFinishDate: null,
                    actualStartDate: null,
                    actualFinishDate: null,
                    wbsItem: null,
                    editMode: true
                };

                if (lastSelectedItem == null || lastSelectedItem == undefined) {
                    newRow.parent = {
                        level: -1
                    };
                    if (vm.wbsAttributes != null) {
                        newRow.attributes = angular.copy(vm.wbsAttributes);
                    }
                    if (vm.wbsRequiredAttributes != null) {
                        newRow.requiredAttributes = angular.copy(vm.wbsRequiredAttributes);
                    }
                    vm.wbsData.push(newRow);
                    angular.forEach(vm.wbsData, function (child) {

                        if (child.id == null || child.id == undefined) {
                            child.isNew = true;
                        } else {
                            child.isNew = false;

                        }
                    });
                } else {
                    newRow.parent = lastSelectedItem;
                    //newRow.level = newRow.parent.level + 1;
                    //index = vm.wbsData.indexOf(lastSelectedItem);
                    if (lastSelectedItem.children == undefined) {
                        lastSelectedItem.children = [];
                    }
                    index = getIndexTopInsertNewChild(lastSelectedItem) + 1;
                    if (vm.wbsAttributes != null) {
                        newRow.attributes = angular.copy(vm.wbsAttributes);
                    }
                    if (vm.wbsRequiredAttributes != null) {
                        newRow.requiredAttributes = angular.copy(vm.wbsRequiredAttributes);
                    }
                    lastSelectedItem.children.push(newRow);
                    vm.wbsData.splice(index, 0, newRow);
                    angular.forEach(lastSelectedItem.children, function (child) {
                        if (child.id == null || child.id == undefined) {
                            child.isNew = true;
                            child.expanded = false;
                        } else {
                            child.isNew = false;
                            child.expanded = false;
                        }

                    });

                }

            }

            function getIndexTopInsertNewChild(selectedItem) {
                var index = 0;
                if (selectedItem.name != null && selectedItem != undefined) {
                    index = vm.wbsData.indexOf(selectedItem);
                    if (index >= maxIndex && index != undefined) {
                        maxIndex = index;
                    }
                    if (selectedItem.children != undefined && selectedItem.children != null && selectedItem.children.length > 0) {
                        if (selectedItem.expanded == false) {
                            toggleNode(selectedItem);
                        }
                        angular.forEach(selectedItem.children, function (child) {
                            index = vm.wbsData.indexOf(child);
                            if (index >= maxIndex && index != undefined) {
                                maxIndex = index;
                            }
                            getIndexTopInsertNewChild(child);
                        })
                    }
                }

                return maxIndex;
            }

            function cancelRow(wbs) {
                if (wbs.isNew == true) {
                    var index = vm.wbsData.indexOf(wbs);
                    vm.wbsData.splice(index, 1);
                    angular.forEach(lastSelectedItem.children, function (child) {
                        if (child.id == null || child.id == undefined) {
                            child.isNew = false;
                            child.expanded = false;
                            child.hasBom = false;
                        }

                    });
                    lastSelectedItem.children.pop();
                }
            }

            function deleteRow() {
                if (vm.wbsData.length == null || vm.wbsData.length == 0) {
                    $rootScope.showWarningMessage("There is no WBS Item to delete");
                } else if (lastSelectedItem != null) {
                    TaskService.tasksByWbs($stateParams.projectId, lastSelectedItem.id).then(
                        function (data) {
                            if (data.length == 0 || data.length == null) {
                                var options = {
                                    title: 'Delete Row',
                                    message: 'Are you sure you want to delete this WBS Item?',
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        WbsService.deleteWbs($stateParams.projectId, lastSelectedItem.id).then(
                                            function (data) {
                                                lastSelectedItem = null;
                                                vm.hide = false;
                                                $rootScope.showSuccessMessage("Selected WBS Item deleted successfully");
                                                loadWbs();
                                            });
                                    }
                                });
                            }
                            else {
                                $rootScope.showErrorMessage("This WBS cannot be deleted as it is used in Task")
                            }
                        });
                } else {
                    $rootScope.showWarningMessage("Please select any WBS Item");
                }
            }

            function removeChildren(wbsItem) {
                if (wbsItem != null && wbsItem.children != null && wbsItem.children != undefined) {
                    angular.forEach(wbsItem.children, function (childwbs) {
                        if (childwbs.expanded == true) {
                            removeChildren(childwbs);
                        }
                    });
                    var index = vm.wbsData.indexOf(wbsItem);
                    if (index != -1) {
                        vm.wbsData.splice(index + 1, wbsItem.children.length);
                        wbsItem.expanded = false;
                    }
                }
            }

            function toggleNode(wbsItem) {
                if (wbsItem.expanded == null || wbsItem.expanded == undefined) {
                    wbsItem.expanded = false;
                }
                wbsItem.expanded = !wbsItem.expanded;
                var index = vm.wbsData.indexOf(wbsItem);
                if (wbsItem.expanded == false) {
                    removeChildren(wbsItem);
                }
                else {
                    if (wbsItem.children.length > 0) {
                        levelsOfChildren(wbsItem.children, wbsItem, index);
                    }
                }
            }

            function levelsOfChildren(children, wbsItem, index) {
                CommonService.getPersonReferences(children, 'assignedTo');
                angular.forEach(children, function (child) {
                    wbsItemMap.put(child.id, child);
                    child.level = wbsItem.level + 1;
                    index = index + 1;
                    vm.wbsData.splice(index, 0, child);
                    if (child.children != undefined) {
                        if (child.children.length > 0) {
                            child.hasBom = true;
                            child.isNew = false;
                            child.expanded = false;
                        }
                    }

                });

                vm.loading = false;
                //$rootScope.setValues(vm.wbs);
                angular.forEach(children, function (wbs) {
                    wbs.editMode = false;
                    vm.wbsIds.push(wbs.id);
                })
                angular.forEach(vm.attributesList, function (attribute) {
                    if (attribute.id != null && attribute.id != "" && attribute.id != 0) {
                        vm.requiredAttributeIds.push(attribute.id.attributeDef);
                    }
                });

                if (vm.wbsIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    WbsService.getAttributesByWbsIdAndAttributeId(vm.wbsIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.selectedRequiredAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.attributesList, function (att) {
                                if (att.id.attributeDef != null && att.id.attributeDef != "" && att.id.attributeDef != 0) {
                                    map.put(att.id.attributeDef, att.attributeDef);
                                }
                            });

                            angular.forEach(vm.wbsData, function (item) {
                                var attributes = [];

                                var projectAttributes = vm.selectedRequiredAttributes[item.id];
                                if (projectAttributes != null && projectAttributes != undefined) {
                                    attributes = attributes.concat(projectAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
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
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.projectAttachments = data;
                                                        item[attributeName] = vm.projectAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == "IMAGE") {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }
            }

            function loadWbs() {
                vm.wbsIds = [];
                vm.wbsData = null;
                vm.requiredAttributeIds = [];
                vm.loading = true;
                //vm.filters.createdBy = vm.filters.createdBy != null || undefined ? vm.filters.createdBy.id : "";
                WbsService.getAllWbs($stateParams.projectId).then(
                    function (data) {
                        vm.wbs = data;
                        vm.wbsData = data;
                        vm.sum = 0;
                        vm.wbsParents = [];
                        angular.forEach(vm.wbsData, function (wbs) {
                            wbsItemMap.put(wbs.id, wbs);
                            wbsMap.put(wbs.name, wbs.id);
                            wbs.level = 0;
                            if (wbs.parent == null) {
                                vm.sum += wbs.weightage;
                                vm.wbsParents.push(wbs);
                            }
                            wbs.selected = false;
                            wbs.isNew = false;
                            wbs.expanded = false;
                            wbs.editMode = false;
                            wbs.hasBom = false;
                            wbs.childWeightage = 0;
                            vm.wbsChildren = [];
                            angular.forEach(wbs.children, function (child) {
                                if (child.children.length > 0) {
                                    child.hasBom = true;
                                }
                                if (wbs.child == null) {
                                    wbs.childWeightage += child.weightage;
                                    vm.wbsChildren.push(wbs);
                                }
                                child.level = 0;
                            });
                            if (wbs.children.length > 0) {
                                wbs.hasBom = true;
                                wbs.isNew = false;
                                wbs.expanded = false;
                            }
                        });
                        vm.loading = false;
                        //$rootScope.setValues(vm.wbs);
                        angular.forEach(vm.wbsData, function (wbs) {
                            vm.wbsIds.push(wbs.id);
                        })
                        angular.forEach(vm.attributesList, function (attribute) {
                            if (attribute.id != null && attribute.id != "" && attribute.id != 0) {
                                vm.requiredAttributeIds.push(attribute.id.attributeDef);
                            }
                        });
                        if (vm.wbsIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                            WbsService.getAttributesByWbsIdAndAttributeId(vm.wbsIds, vm.requiredAttributeIds).then(
                                function (data) {
                                    vm.selectedRequiredAttributes = data;

                                    var map = new Hashtable();
                                    angular.forEach(vm.attributesList, function (att) {
                                        if (att.id.attributeDef != null && att.id.attributeDef != "" && att.id.attributeDef != 0) {
                                            map.put(att.id.attributeDef, att.attributeDef);
                                        }
                                    });

                                    angular.forEach(vm.wbsData, function (item) {
                                        var attributes = [];

                                        var projectAttributes = vm.selectedRequiredAttributes[item.id];
                                        if (projectAttributes != null && projectAttributes != undefined) {
                                            attributes = attributes.concat(projectAttributes);
                                        }
                                        angular.forEach(attributes, function (attribute) {
                                            var selectatt = map.get(attribute.id.attributeDef);
                                            if (selectatt != null) {
                                                var attributeName = selectatt.name;
                                                if (selectatt.dataType == 'TEXT') {
                                                    item[attributeName] = attribute.stringValue;
                                                } else if (selectatt.dataType == 'INTEGER') {
                                                    item[attributeName] = attribute.integerValue;
                                                } else if (selectatt.dataType == 'BOOLEAN') {
                                                    item[attributeName] = attribute.booleanValue;
                                                } else if (selectatt.dataType == 'DOUBLE') {
                                                    item[attributeName] = attribute.doubleValue;
                                                } else if (selectatt.dataType == 'DATE') {
                                                    item[attributeName] = attribute.dateValue;
                                                } else if (selectatt.dataType == 'LIST') {
                                                    item[attributeName] = attribute.listValue;
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
                                                    var attachmentIds = [];
                                                    if (attribute.attachmentValues.length > 0) {
                                                        angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                            attachmentIds.push(attachmentId);
                                                        });
                                                        AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                            function (data) {
                                                                vm.projectAttachments = data;
                                                                item[attributeName] = vm.projectAttachments;
                                                            }
                                                        )
                                                    }
                                                } else if (selectatt.dataType == "IMAGE") {
                                                    if (attribute.imageValue != null) {
                                                        item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                    }
                                                }
                                            }
                                        })
                                    })

                                }
                            );
                        }
                        CommonService.getPersonReferences(vm.wbsData, 'assignedTo');

                    });
            }

            function isChildrensWeightageExceed(wbs) {
                var defered = $q.defer();
                if (wbs.parent.id != undefined) {
                    wbs.parent = wbs.parent.id;
                }
                WbsService.getWbsChildren($stateParams.projectId, wbs.parent).then(
                    function (data) {
                        if (data.length != 0) {
                            vm.childrensWeightage = Number(0);
                            vm.counter = 0;
                            angular.forEach(data, function (child) {
                                if (child.id == wbs.id) {
                                    child.weightage = wbs.weightage;
                                }
                                vm.childrensWeightage += Number(child.weightage);
                                vm.counter += 1;
                                if (data.length == vm.counter) {
                                    if (vm.childrensWeightage > 100) {
                                        defered.resolve(true);
                                    } else {
                                        defered.reject(false);
                                    }
                                }
                            });
                        }
                    });
                return defered.promise;
            }

            function validationWhileUpdate(wbs) {
                var id = wbsMap.get(wbs.name);
                if (wbs.parent != null) {
                    var parentPlannedStartDate = moment(wbs.parent.plannedStartDate, 'DD/MM/YYYY');
                    var parentPlannedFinishDate = moment(wbs.parent.plannedFinishDate, 'DD/MM/YYYY');
                }
                if (wbs.name == null || wbs.name == "" || wbs.name == " ") {
                    $rootScope.showWarningMessage("WBS Name cannot be empty");
                    loadWbs();
                }
                else if (id != null && id != wbs.id) {
                    $rootScope.showWarningMessage("{0} Name already exists".format(wbs.name));
                    loadWbs();
                }
                else if (wbs.weightage == 0) {
                    $rootScope.showWarningMessage("Please enter +ve number for WBS Weightage");
                    loadWbs();
                }
                else if (wbs.weightage == null) {
                    $rootScope.showWarningMessage("WBS Weightage cannot be empty");
                    loadWbs();
                }
                else if (wbs.weightage < 0) {
                    $rootScope.showWarningMessage("Please enter +ve number for WBS Weightage");
                    loadWbs();
                }
                else if (wbs.weightage > 100) {
                    $rootScope.showWarningMessage("WBS Weightage should not exceed 100");
                    loadWbs();
                }
                else if (isNaN(wbs.weightage)) {
                    $rootScope.showWarningMessage("Please enter +ve number for WBS Weightage");
                    loadWbs();
                }

                else if (wbs != null) {
                    if (wbs.parent != null && wbs.parent.level == -1) {
                        wbs.parent = null;
                    }
                    if (wbs.parent == 0 || wbs.parent == null) {
                        vm.sum = Number(0);
                        vm.count = 0;
                        angular.forEach(vm.wbsParents, function (parent) {
                            vm.sum += Number(parent.weightage);
                            vm.count += 1;
                            if (vm.wbsParents.length == vm.count) {
                                if (vm.sum > 100) {
                                    loadWbs();
                                    $rootScope.showErrorMessage("Total WBS Weightage should not exceed 100 ");
                                } else {
                                    wbs.assignedTo = wbs.assignedToObject.id;
                                    WbsService.updateWbs($stateParams.projectId, wbs).then(
                                        function (data) {
                                            /*loadWbs();*/
                                            $rootScope.showSuccessMessage("WBS updated successfully");
                                        }
                                    )
                                }
                            }
                        });
                    } else {
                        isChildrensWeightageExceed(wbs).then(
                            function (success) {
                                $rootScope.showErrorMessage("Total Children Weightage should not exceed 100");
                                loadWbs();
                            }, function (failure) {
                                wbs.assignedTo = wbs.assignedToObject.id;
                                WbsService.updateWbs($stateParams.projectId, wbs).then(
                                    function (data) {
                                        /*  loadWbs();*/
                                        $rootScope.showSuccessMessage("WBS updated successfully");
                                    }
                                )
                            });
                    }
                }
            }

            function updateWbs(wbs) {
                validationWhileUpdate(wbs);
            }

            vm.showWbsAttributes = showWbsAttributes;
            vm.wbsAttributes = [];

            function showWbsAttributes() {
                var options = {
                    title: 'WBS Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/proc/materials/all/materialAttributesView.jsp',
                    controller: 'MaterialAttributesController as materialAttributesVm',
                    resolve: 'app/desktop/modules/proc/materials/all/materialAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.wbsAttributes,
                        attributesMode: 'WBS'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.wbsAttributes = result;
                        $window.localStorage.setItem("wbsAttributes", JSON.stringify(vm.wbsAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadWbs();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.wbsAttributes = [];
            vm.wbsRequiredAttributes = [];
            vm.attributesList = [];

            function loadObjectAttributeDefs() {
                vm.attributesList = [];
                vm.wbsRequiredAttributes = [];
                ProjectService.getAllProjectAttributes("WBS").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.wbsData,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                stringValue: null,
                                integerValue: null,
                                doubleValue: null,
                                booleanValue: null,
                                dateValue: null,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timeValue: null,
                                timestampValue: null,
                                refValue: null,
                                imageValue: null,
                                attachmentValues: []
                            };

                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.wbsAttributes.push(att);
                            } else {
                                vm.wbsRequiredAttributes.push(att);
                            }

                            vm.attributesList.push(att);
                        });
                        loadWbs();
                    }
                )
            }

            /*function loadRequiredWbsAttributes() {
             ProjectService.getRequiredProjectAttributes("WBS").then(
             function (data) {
             vm.attributesList = data;
             loadWbs();
             }
             )
             }*/

            function loadProjectPersons() {
                ProjectService.getProjectPersons($stateParams.projectId).then(
                    function (data) {
                        vm.projectPersons = data;
                        angular.forEach(vm.projectPersons, function (obj) {
                            loadPersonsById(obj.person);
                        })
                    }
                );
            }

            function loadPersonsById(person) {
                vm.persons = [];
                vm.loading = false;
                CommonService.getPerson(person).then(
                    function (data) {
                        vm.persons.push(data);
                    }
                )
            }

            vm.finishWbsItem = finishWbsItem;
            function finishWbsItem(wbs) {
                var options = {
                    title: 'Finish WBS Tasks',
                    message: 'Are you sure you want to Finish all the Tasks in this WBS Item?',
                    okButtonClass: 'btn-success'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        vm.finishWbs = [];
                        WbsService.finishWbs($stateParams.projectId, wbs).then(
                            function (data) {
                                vm.finishWbs = data;
                                $rootScope.showSuccessMessage("This WBS tasks are finished")
                                angular.forEach(vm.finishWbs, function (finishWbs) {
                                    wbs = wbsItemMap.get(finishWbs.id);
                                    if (wbs != null) {
                                        wbs.percentageComplete = finishWbs.percentageComplete;
                                    }
                                })
                            }
                        )
                    }
                });
            }

            function loadProject() {
                vm.loading = true;
                ProjectService.getProject($stateParams.projectId).then(
                    function (data) {
                        $rootScope.selectedProject = data;
                        $application.selectedProject = null;
                        $application.selectedProject = data;
                        $rootScope.headerProjectName = true;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$broadcast('app.activate.procurement', {project: {name: 'Procurement'}});
                    loadProjectPersons();
                    loadProject();
                    loadWbs();
                    loadObjectAttributeDefs();

                }
            })();

        }
    }
)
;
