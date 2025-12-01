define(
    [
        'app/desktop/modules/pm/project/activity/activity.module',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('ActivityTasksController', ActivityTasksController);

        function ActivityTasksController($scope, $stateParams, $rootScope, $translate, $timeout, $state, CommonService, AttributeAttachmentService, ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, $window, ObjectTypeAttributeService, ActivityService,
                                         DialogService) {

            var vm = this;
            var activityId = $stateParams.activityId;
            var taskId = $stateParams.taskId;

            vm.loading = true;
            vm.deleteTask = deleteTask;
            vm.selectTask = selectTask;
            vm.showTaskDetails = showTaskDetails;

            $rootScope.addActivityTasks = addActivityTasks;
            vm.finishActivityTask = finishActivityTask;
            $rootScope.showTaskTypeAttributes = showTaskTypeAttributes;

            var parsed = angular.element("<div></div>");

            var taskDialogTitle = parsed.html($translate.instant("TASK_DIALOG_TITLE")).html();
            var taskDialogMessage = parsed.html($translate.instant("TASK_DIALOG_MESSAGE")).html();
            var taskDeletedMessage = parsed.html($translate.instant("TASK_DELETED_MESSAGE")).html();
            vm.deleteTitle = parsed.html($translate.instant("DELETE")).html();
            var newActivityTask = parsed.html($translate.instant("NEW_ACTIVITY_TASK")).html();
            var createTask = parsed.html($translate.instant("CREATE_TASK")).html();
            var taskCreatedMessage = parsed.html($translate.instant("TASK_CREATED_MESSAGE")).html();
            vm.deleteTaskTitle = parsed.html($translate.instant("FINISH_TASK_TITLE")).html();
            vm.editTaskTitle = parsed.html($translate.instant("CLICK_TO_EDIT")).html();
            vm.updateTaskTitle = parsed.html($translate.instant("CLICK_TO_UPDATE")).html();
            vm.cancelTitle = parsed.html($translate.instant("CANCEL_CHANGES")).html();
            var activityAssignMsg = parsed.html($translate.instant("ACTIVITY_ASSIGN_MSG")).html();
            var activity = parsed.html($translate.instant("ACTIVITY")).html();
            var taskUpdateMsg = parsed.html($translate.instant("TASK_UPDATE_MSG")).html();
            var enterPositiveNumber = parsed.html($translate.instant("ENTER_POSITIVE_NUMBER")).html();
            var enterValidPercent = parsed.html($translate.instant("ENTER_VALID_PERCENT")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var sequenceUpdatedMessage = parsed.html($translate.instant("SEQUENCE_UPDATED_MESSAGE")).html();

            function addActivityTasks() {
                if ($rootScope.selectedWbsActivity.assignedTo == null) {
                    $rootScope.showWarningMessage(activityAssignMsg + "[" + vm.activity.name + "]" + activity);
                } else {
                    var options = {
                        title: newActivityTask,
                        template: 'app/desktop/modules/pm/project/activity/details/task/newActivityTaskView.jsp',
                        controller: 'NewActivityTaskController as newActivityTaskVm',
                        resolve: 'app/desktop/modules/pm/project/activity/details/task/newActivityTaskController',
                        width: 600,
                        showMask: true,
                        buttons: [
                            {text: createTask, broadcast: 'app.project.activity.task.new'}
                        ],
                        callback: function (data) {
                            $rootScope.showSuccessMessage(taskCreatedMessage);
                            //$rootScope.hideSidePanel();
                            loadActivity();
                            $rootScope.loadActivityCount();
                            $rootScope.loadActivityPercentComplete();
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            function loadActivityTasks() {
                ActivityService.getActivityTasks(activityId).then(
                    function (data) {
                        vm.activityTasks = data;
                        vm.loading = false;
                        vm.finishedTasks = [];
                        loadTaskAttributeValues();
                        CommonService.getPersonReferences(vm.activityTasks, 'assignedTo');
                        angular.forEach(vm.activityTasks, function (task) {
                            task.percentComplete = parseInt(task.percentComplete);
                            task.editMode = false;
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function deleteTask(task) {
                var options = {
                    title: taskDialogTitle,
                    message: taskDialogMessage + " [ " + task.name + " ]" + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ActivityService.deleteActivityTask(activityId, task.id).then(
                            function (data) {
                                var index = vm.activityTasks.indexOf(task);
                                vm.activityTasks.splice(index, 1);
                                $rootScope.loadActivityCount();
                                $rootScope.showSuccessMessage(taskDeletedMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function selectTask(task) {
                if (vm.selectedTask != null && vm.selectedTask !== task) {
                    vm.selectedTask.checked = false;
                }
                if (vm.selectedTask === task) {
                    vm.selectedTask = null;
                    $rootScope.showFinishButton = false;
                } else {
                    vm.selectedTask = task;
                    $rootScope.showFinishButton = true;
                }
            }

            function finishActivityTask(task) {
                task.percentComplete = 100;
                task.status = "FINISHED";
                ActivityService.updateActivityTask(activityId, task).then(
                    function (data) {
                        vm.activityTask = data;
                        $rootScope.showSuccessMessage(taskUpdateMsg);
                        loadActivityTasks();
                        $rootScope.loadActivityPercentComplete();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadActivity() {
                ActivityService.getActivity(activityId).then(
                    function (data) {
                        vm.activity = data;
                        loadActivityTasks();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function editTask(task) {
                task.newName = task.name;
                task.newDescription = task.description;
                task.newPercentComplete = task.percentComplete;
                task.newStatus = task.status;
                task.editMode = true;
            }

            vm.editTask = editTask;
            vm.updateTask = updateTask;
            vm.cancelChanges = cancelChanges;

            function cancelChanges(task) {
                task.name = task.newName;
                task.description = task.newDescription;
                task.percentComplete = task.newPercentComplete;
                task.status = task.newStatus;
                task.editMode = false;
            }

            function updateTask(task) {
                if (validate(task)) {
                    if (task.percentComplete > 0 && task.percentComplete < 100) {
                        task.status = "INPROGRESS";
                        ActivityService.updateActivityTask(task.activity, task).then(
                            function (data) {
                                loadActivityTasks();
                                $rootScope.loadActivityPercentComplete();
                                $rootScope.showSuccessMessage(taskUpdateMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if (task.percentComplete == 100) {
                        task.status = "FINISHED";
                        ActivityService.updateActivityTask(task.activity, task).then(
                            function (data) {
                                loadActivityTasks();
                                $rootScope.loadActivityPercentComplete();
                                $rootScope.showSuccessMessage(taskUpdateMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        ActivityService.updateActivityTask(task.activity, task).then(
                            function (data) {
                                loadActivityTasks();
                                $rootScope.loadActivityPercentComplete();
                                $rootScope.showSuccessMessage(taskUpdateMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }

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

            $('#myModalHorizontal').on('hidden', function () {
                $(this).data('modal').$element.removeData();
            })

            function validate(task) {
                var valid = true;

                if (task.percentComplete < 0) {
                    valid = false;
                    $rootScope.showWarningMessage(enterPositiveNumber);
                } else if (task.percentComplete > 100) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidPercent)
                }

                return valid;
            }

            function showTaskDetails(task) {
                $window.localStorage.setItem("lastSelectedActivityTab", JSON.stringify(vm.activityTabId));
                $state.go('app.pm.project.activity.task.details', {activityId: activityId, taskId: task.id})
            }

            vm.selectedAttributes = [];
            vm.objectIds = [];
            var currencyMap = new Hashtable();
            vm.removeAttribute = removeAttribute;

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            function showTaskTypeAttributes() {
                var selectedAttributes = angular.copy(vm.selectedAttributes);
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/pm/project/activity/details/task/taskTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/pm/project/activity/details/task/taskTypeAttributesController',
                    controller: 'TaskTypeAttributesController as taskTypeAttributesVm',
                    width: 500,
                    data: {

                        selectedAttributes: selectedAttributes,
                        objectType: "PROJECTTASK"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("allProjectTaskAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadActivity();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("allProjectattributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("allProjectTaskAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.selectedAttributes = [];
            vm.objectIds = [];
            function loadTaskAttributeValues() {
                vm.projectTaskIds = [];
                vm.attributeIds = [];
                CommonService.getPersonReferences(vm.activityTasks, 'modifiedBy');
                CommonService.getPersonReferences(vm.activityTasks, 'createdBy');
                angular.forEach(vm.activityTasks, function (item) {
                    vm.projectTaskIds.push(item.id);

                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.projectTaskIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.projectTaskIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.activityTasks, function (item) {
                                var attributes = [];
                                var revisionAttributes = vm.selectedObjectAttributes[item.latestRevision];
                                if (revisionAttributes != null && revisionAttributes != undefined) {
                                    attributes = attributes.concat(revisionAttributes);
                                }
                                var itemAttributes = vm.selectedObjectAttributes[item.id];
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
                                        } else if (selectatt.dataType == 'LIST' && !selectatt.listMultiple) {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'LIST' && selectatt.listMultiple) {
                                            item[attributeName] = attribute.mlistValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'HYPERLINK') {
                                            item[attributeName] = attribute.hyperLinkValue;
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
                            })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.updateTaskSeq = updateTaskSeq;
            function updateTaskSeq(actualRow, targetRow) {
                $rootScope.showBusyIndicator($('.view-content'))
                ActivityService.updateActivityTaskSeq(activityId, actualRow.id, targetRow.id).then(
                    function (data) {
                        loadActivityTasks();
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(sequenceUpdatedMessage);
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.activity.tabActivated', function (event, data) {
                    if (data.tabId == 'details.tasks') {
                        vm.activityTabId = data.tabId;
                        loadActivity();
                        vm.selectedTask = null;
                    }
                    if (validateJSON()) {
                        var setAttributes = JSON.parse($window.localStorage.getItem("allProjectTaskAttributes"));
                    } else {
                        var setAttributes = null;
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
                                    $window.localStorage.setItem("allProjectTaskAttributes", "");
                                    vm.selectedAttributes = setAttributes
                                } else {
                                    vm.selectedAttributes = setAttributes;
                                }
                            }, function (error) {
                                  $rootScope.showErrorMessage(error.message);
                                  $rootScope.hideBusyIndicator();
                             }
                        )
                    }
                })
            })();
        }
    }
);