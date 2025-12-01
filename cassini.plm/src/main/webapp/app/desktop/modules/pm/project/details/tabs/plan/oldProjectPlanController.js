define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/milestoneService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/recentlyVisitedService'
    ],
    function (module) {
        module.controller('OldProjectPlanController', OldProjectPlanController);

        function OldProjectPlanController($scope, $rootScope, $state, $timeout, $sce, $window, $stateParams, $translate, ProjectService,
                                          ActivityService, CommonService, MilestoneService, DialogService, ItemService, DialogService, ObjectTypeAttributeService,
                                          AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService) {

            var vm = this;
            var projectId = $stateParams.projectId;
            vm.wbsItems = [];
            vm.loading = true;
            var lastSelectedWbs = null;
            var maxIndex = 0;

            $rootScope.addWbs = addWbs;
            $rootScope.addActivity = addActivity;
            $rootScope.addMilestone = addMilestone;
            $rootScope.createDuplicateWbs = createDuplicateWbs;
            $rootScope.createNewTemplate = createNewTemplate;
            $rootScope.showWbsButton = true;
            $rootScope.showActivityAndMilestone = false;
            $rootScope.showCreateDuplicateWbs = false;

            vm.selectWbs = selectWbs;
            vm.editWbs = editWbs;
            vm.toggleNode = toggleNode;
            vm.deleteWbs = deleteWbs;
            vm.finishMilestone = finishMilestone;

            vm.newWbs = {
                id: null,
                name: null,
                description: null,
                parent: null,
                project: vm.project
            };

            var parsed = angular.element("<div></div>");

            vm.clickToEditTitle = parsed.html($translate.instant("CLICK_TO_EDIT")).html();
            vm.clickToFinishTitle = parsed.html($translate.instant("CLICK_TO_FINISH")).html();
            vm.clickToDeleteTitle = parsed.html($translate.instant("CLICK_TO_DELETE")).html();
            vm.clickToSelectWbsTitle = parsed.html($translate.instant("CLICK_TO_SELECT_WBS")).html();
            vm.activityTitle = parsed.html($translate.instant("ACTIVITY")).html();
            vm.milestoneTitle = parsed.html($translate.instant("MILESTONE")).html();
            var wbsUpdatedMessage = parsed.html($translate.instant("WBS_UPDATED_MESSAGE")).html();
            var wbsCreatedMessage = parsed.html($translate.instant("WBS_CREATED_MESSAGE")).html();
            vm.createWbs = parsed.html($translate.instant("CREATE_WBS")).html();
            vm.addActivity = parsed.html($translate.instant("ADD_ACTIVITYS")).html();
            var updateWbs = parsed.html($translate.instant("UPDATE_WBS")).html();
            var editWbsTitle = parsed.html($translate.instant("EDIT_WBS")).html();
            var createActivity = parsed.html($translate.instant("CREATE_ACTIVITY")).html();
            var updateActivity = parsed.html($translate.instant("UPDATE_ACTIVITY")).html();
            var editActivityTitle = parsed.html($translate.instant("EDIT_ACTIVITY")).html();
            var createMilestone = parsed.html($translate.instant("CREATE_MILESTONE")).html();
            var updateMilestone = parsed.html($translate.instant("UPDATE_MILESTONE")).html();
            var editMilestone = parsed.html($translate.instant("EDIT_MILESTONE")).html();
            var newWBS = parsed.html($translate.instant("NEW_WBS")).html();
            var newActivity = parsed.html($translate.instant("NEW_ACTIVITY")).html();
            var newMilestone = parsed.html($translate.instant("NEW_MILESTONE")).html();
            var wbsDeletedMessage = parsed.html($translate.instant("WBS_DELETED_MESSAGE")).html();
            var activityDeletedMessage = parsed.html($translate.instant("ACTIVITY_DELETED_MESSAGE")).html();
            var milestoneDeletedMessage = parsed.html($translate.instant("MILESTONE_DELETED_MESSAGE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var DeletedMessage = parsed.html($translate.instant("PROJECT_DELETED_MESSAGE")).html();
            var createTemplate = parsed.html($translate.instant("CREATE_TEMPLATE")).html();
            var templateCreatedMessage = parsed.html($translate.instant("TEMPLATE_CREATE_MESSAGE")).html();
            var newTemplate = parsed.html($translate.instant("NEW_TEMPLATE")).html();
            var milestoneUpdateMsg = parsed.html($translate.instant("MILESTONE_UPDATED_MESSAGE")).html();
            var copyWbsMessage = parsed.html($translate.instant("COPY_WBS_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var sequenceUpdateMessage = parsed.html($translate.instant("SEQUENCE_UPDATED_MESSAGE")).html();
            vm.showTasks = parsed.html($translate.instant("CLICK_TO_SHOW_TASKS")).html();
            vm.Tasks = parsed.html($translate.instant("TASKS")).html();
            vm.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();

            function selectWbs(wbs) {
                wbs.selected = !wbs.selected;
                if (wbs.selected == true) {
                    lastSelectedWbs = wbs;
                    if (wbs.children == 0) {

                    }
                    $rootScope.showCreateDuplicateWbs = true;
                    $rootScope.showWbsButton = false;
                    $rootScope.showActivityAndMilestone = true;
                }
                else {
                    wbs.selected = false;
                    lastSelectedWbs = null;
                    $rootScope.showWbsButton = true;
                    $rootScope.showActivityAndMilestone = false;
                    $rootScope.showCreateDuplicateWbs = false;
                }

                angular.forEach(vm.wbsItems, function (wbsItem) {
                    if (wbs.id != wbsItem.id) {
                        wbsItem.selected = false;
                    }
                })
            }

            function loadProjectPlan() {
                vm.wbsItems = [];
                ProjectService.getProjectWbsStructure(projectId).then(
                    function (data) {
                        vm.projectWbs = data;
                        //populateBomItems(data);
                        populateChildren(data);
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function populateChildren(data) {
                angular.forEach(data, function (item) {
                    item.newName = item.name;
                    item.newDescription = item.description;
                    item.isNew = false;
                    item.editMode = false;
                    if (item.percentComplete != undefined) {
                        if (item.percentComplete < 100 && item.percentComplete > 0) {
                            item.percentComplete = parseInt(item.percentComplete);
                        }
                    }

                    if (lastSelectedWbs != null) {
                        if (lastSelectedWbs.id == item.id) {
                            item.selected = true;
                        }
                    }
                    vm.wbsItems.push(item);
                    populateChildren(item.children);
                    CommonService.getPersonReferences(item.children, 'modifiedBy');
                    CommonService.getPersonReferences(item.children, 'createdBy');
                    loadActivityAttributeValues();
                });
            }

            function populateBomItems(data) {
                angular.forEach(data, function (item) {
                    item.newName = item.name;
                    item.newDescription = item.description;
                    item.isNew = false;
                    item.editMode = false;
                    if (item.percentComplete != undefined) {
                        if (item.percentComplete < 100 && item.percentComplete > 0) {
                            item.percentComplete = parseInt(item.percentComplete);
                        }
                    }

                    if (lastSelectedWbs != null) {
                        if (lastSelectedWbs.id == item.id) {
                            item.selected = true;
                        }
                    }
                    vm.wbsItems.push(item);
                    populateBomItems(item.activities);
                    populateBomItems(item.milestones);
                    CommonService.getPersonReferences(item.activities, 'modifiedBy');
                    CommonService.getPersonReferences(item.activities, 'createdBy');
                    loadActivityAttributeValues();
                });
            }

            function loadProject() {
                ProjectService.getProject(projectId).then(
                    function (data) {
                        vm.project = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function addWbs() {
                var options = {
                    title: newWBS,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/wbs/newWbsView.jsp',
                    controller: 'NewWbsController as newWbsVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/wbs/newWbsController',
                    width: 600,
                    showMask: true,
                    data: {
                        wbsElementData: null,
                        wbsMode: 'New'
                    },
                    buttons: [
                        {text: vm.createWbs, broadcast: 'app.project.plan.wbs.new'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(wbsCreatedMessage);
                        //$rootScope.hideSidePanel();
                        loadProjectPlan();
                        //$rootScope.showWbsButton = true;
                        //$rootScope.showActivityAndMilestone = false;
                        $rootScope.loadProjectPercentageComplete();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function addActivity(wbs) {
                var options = {
                    title: newActivity,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/activity/newWbsActivityView.jsp',
                    controller: 'NewWbsActivityController as newWbsActivityVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/activity/newWbsActivityController',
                    width: 700,
                    data: {
                        activityWbsData: wbs,
                        activityMode: 'New',
                        projectData: vm.project
                    },
                    buttons: [
                        {text: createActivity, broadcast: 'app.project.plan.activity.new'}
                    ],
                    callback: function (data) {
                        //$rootScope.hideSidePanel();
                        loadProjectPlan();
                        //$rootScope.showWbsButton = true;
                        //$rootScope.showActivityAndMilestone = false;
                        $rootScope.loadProjectPercentageComplete();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function addMilestone(wbs) {
                var options = {
                    title: newMilestone,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/milestone/newWbsMilestoneView.jsp',
                    controller: 'NewWbsMilestoneController as newWbsMilestoneVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/milestone/newWbsMilestoneController',
                    width: 700,
                    showMask: true,
                    data: {
                        milestoneWbsData: wbs,
                        milestoneMode: 'New',
                        projectData: vm.project
                    },
                    buttons: [
                        {text: createMilestone, broadcast: 'app.project.plan.milestone.new'}
                    ],
                    callback: function (data) {
                        //$rootScope.hideSidePanel();
                        loadProjectPlan();
                        /*$rootScope.showWbsButton = true;
                         $rootScope.showActivityAndMilestone = false;*/

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function editWbs(wbs) {
                if (wbs.objectType == 'PROJECTACTIVITY') {
                    var options = {
                        title: editActivityTitle,
                        showMask: true,
                        template: 'app/desktop/modules/pm/project/details/tabs/plan/activity/editWbsActivityView.jsp',
                        controller: 'EditWbsActivityController as editWbsActivityVm',
                        resolve: 'app/desktop/modules/pm/project/details/tabs/plan/activity/editWbsActivityController',
                        width: 600,
                        data: {
                            activityData: wbs,
                            activityMode: 'Edit',
                            projectData: vm.project
                        },
                        buttons: [
                            {text: updateActivity, broadcast: 'app.project.plan.activity.edit'}
                        ],
                        callback: function (data) {
                            //$rootScope.hideSidePanel();
                            loadProjectPlan();
                        }
                    };

                    $rootScope.showSidePanel(options);

                } else if (wbs.objectType == "PROJECTMILESTONE") {
                    var options = {
                        title: editMilestone,
                        template: 'app/desktop/modules/pm/project/details/tabs/plan/milestone/editWbsMilestoneView.jsp',
                        controller: 'EditWbsMilestoneController as editWbsMilestoneVm',
                        resolve: 'app/desktop/modules/pm/project/details/tabs/plan/milestone/editWbsMilestoneController',
                        width: 600,
                        showMask: true,
                        data: {
                            milestoneData: wbs,
                            milestoneMode: 'Edit',
                            projectData: vm.project
                        },
                        buttons: [
                            {text: updateMilestone, broadcast: 'app.project.plan.milestone.edit'}
                        ],
                        callback: function (data) {
                            //$rootScope.hideSidePanel();
                            loadProjectPlan();
                        }
                    };

                    $rootScope.showSidePanel(options);
                } else if (wbs.objectType == "PROJECTWBSELEMENT") {
                    var options = {
                        title: editWbsTitle,
                        template: 'app/desktop/modules/pm/project/details/tabs/plan/wbs/editWbsView.jsp',
                        controller: 'EditWbsController as editWbsVm',
                        resolve: 'app/desktop/modules/pm/project/details/tabs/plan/wbs/editWbsController',
                        width: 600,
                        showMask: true,
                        data: {
                            wbsElementData: wbs,
                            wbsMode: 'Edit',
                            projectData: vm.project
                        },
                        buttons: [
                            {text: updateWbs, broadcast: 'app.project.plan.wbs.edit'}
                        ],
                        callback: function (data) {
                            $rootScope.showSuccessMessage(wbsUpdatedMessage);
                            //$rootScope.hideSidePanel();
                            loadProjectPlan();
                        }
                    };

                    $rootScope.showSidePanel(options);
                }

            }

            function getIndexTopInsertNewChild(lastSelected) {
                var index = 0;

                if (lastSelected.children != undefined && lastSelected.children != null) {
                    index = lastSelected.children.length;
                    angular.forEach(lastSelected.children, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }

            function toggleNode(wbs) {
                wbs.expanded = !wbs.expanded;
                var index = vm.wbsItems.indexOf(wbs);
                if (wbs.expanded == false) {
                    removeChildren(wbs);
                } else {
                    ProjectService.getWbsChildren(projectId, wbs.id).then(
                        function (data) {
                            wbs.children = [];
                            wbs.children = data;
                            /*wbs.activities = data.activities;
                             wbs.milestones = data.milestones;
                             angular.forEach(data.activities, function (wbsChild) {
                             wbs.children.push(wbsChild);
                             });

                             angular.forEach(data.milestones, function (wbsChild) {
                             wbs.children.push(wbsChild);
                             });*/

                            angular.forEach(wbs.children, function (wbsChild) {
                                index = index + 1;
                                vm.wbsItems.splice(index, 0, wbsChild);
                                loadActivityAttributeValues();
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeChildren(wbs) {
                if (wbs.children != null && wbs.children != undefined) {
                    angular.forEach(wbs.children, function (item) {
                        var index = vm.wbsItems.indexOf(item);
                        vm.wbsItems.splice(index, 1);
                    });
                    wbs.expanded = false;
                }

                /*if (wbs.activities != null && wbs.activities != undefined) {
                 angular.forEach(wbs.activities, function (item) {
                 var index = vm.wbsItems.indexOf(item);
                 vm.wbsItems.splice(index, 1);
                 });
                 wbs.expanded = false;
                 }

                 if (wbs.milestones != null && wbs.milestones != undefined) {
                 angular.forEach(wbs.milestones, function (item) {
                 var index = vm.wbsItems.indexOf(item);
                 vm.wbsItems.splice(index, 1);
                 });
                 wbs.expanded = false;
                 }*/

            }

            function deleteWbs(wbs) {
                var options = null;
                if (wbs.objectType == 'PROJECTWBSELEMENT') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + wbs.name + " ] " + itemDelete + "?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            ProjectService.deleteWBSElement(projectId, wbs.id).then(
                                function (data) {
                                    var index = vm.wbsItems.indexOf(wbs);
                                    vm.wbsItems.splice(index, 1);
                                    $rootScope.showSuccessMessage(wbsDeletedMessage);
                                    loadProjectPlan();
                                    $rootScope.showWbsButton = true;
                                    $rootScope.showActivityAndMilestone = false;
                                    $rootScope.showCreateDuplicateWbs = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                } else if (wbs.objectType == 'PROJECTACTIVITY') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + wbs.name + " ] " + vm.activityTitle + " ?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            ActivityService.deleteActivity(wbs.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(activityDeletedMessage);
                                    loadProjectPlan();
                                    $rootScope.showWbsButton = true;
                                    $rootScope.showActivityAndMilestone = false;
                                    $rootScope.showCreateDuplicateWbs = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                } else if (wbs.objectType == 'PROJECTMILESTONE') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + wbs.name + " ] " + vm.milestoneTitle + "?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            MilestoneService.deleteMilestone(wbs.parent, wbs).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(milestoneDeletedMessage);
                                    loadProjectPlan();
                                    $rootScope.showWbsButton = true;
                                    $rootScope.showActivityAndMilestone = false;
                                    $rootScope.showCreateDuplicateWbs = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                }

            }

            function finishMilestone(milestone) {
                milestone.wbs = milestone.parent;
                MilestoneService.finishWbsMilestone(milestone.wbs, milestone).then(
                    function (data) {
                        vm.milestone = data;
                        loadProjectPlan();
                        $rootScope.showSuccessMessage(milestoneUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.openActivityDetails = openActivityDetails;
            function openActivityDetails(wbs) {
                $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify(vm.projectPlanTabId));
                $state.go('app.pm.project.activity.details', {activityId: wbs.id})
            }

            vm.activityPopover = {
                templateUrl: 'app/desktop/modules/pm/project/details/tabs/plan/activityPopoverTemplate.jsp'
            };

            function createNewTemplate() {
                var options = {
                    title: newTemplate,
                    template: 'app/desktop/modules/template/new/newTemplateView.jsp',
                    controller: 'NewTemplateController as newTemplateVm',
                    resolve: 'app/desktop/modules/template/new/newTemplateController',
                    width: 600,
                    showMask: true,
                    data: {
                        projectPlan: vm.project
                    },
                    buttons: [
                        {text: createTemplate, broadcast: 'app.project.template.new'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(templateCreatedMessage);
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function createDuplicateWbs() {
                ProjectService.copyWbs(projectId, lastSelectedWbs.id).then(
                    function (data) {
                        lastSelectedWbs = null;
                        $rootScope.showWbsButton = true;
                        $rootScope.showCreateDuplicateWbs = false;
                        $rootScope.showActivityAndMilestone = false;
                        loadProjectPlan();
                        $rootScope.showSuccessMessage(copyWbsMessage);
                    },
                    function (error) {
                        $rootScope.showWarningMessage(error.message);
                    }
                )
            }

            vm.selectedAttributes = [];
            vm.selectedObjectAttributes = [];
            vm.objectIds = [];
            vm.itemIds = [];
            vm.attributeIds = [];
            var currencyMap = new Hashtable();
            $rootScope.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            function showTypeAttributes() {
                var selectedAttributes = angular.copy(vm.selectedAttributes);
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/activityTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/activityTypeAttributesController',
                    controller: 'ActivityTypeAttributesController as activityTypeAttributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: selectedAttributes
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("allActivityattributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadProjectPlan();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("allActivityattributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("allActivityattributes"));
                    //JSON.parse($window.localStorage.getItem("requirements"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('myModal23');
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

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);

            }

            /*    Show Modal dialogue for RichText*/
            vm.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                vm.modalValue = data
                mymodal.modal('show');
            }

            function loadActivityAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.wbsItems, function (wbs) {
                    if (wbs.objectType == "PROJECTACTIVITY" || wbs.objectType == "PROJECTMILESTONE") {
                        if (wbs.plannedStartDate) {
                            wbs.plannedStartDatede = moment(wbs.plannedStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (wbs.plannedFinishDate) {
                            wbs.plannedFinishDatede = moment(wbs.plannedFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (wbs.actualStartDate) {
                            wbs.actualStartDatede = moment(wbs.actualStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (wbs.actualFinishDate) {
                            wbs.actualFinishDatede = moment(wbs.actualFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        vm.itemIds.push(wbs.id);
                    }
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.wbsItems, function (item) {

                                if (item.objectType == "PROJECTACTIVITY") {
                                    var attributes = [];

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
                                            } else if (selectatt.dataType == 'TIME') {
                                                item[attributeName] = attribute.timeValue;
                                            } else if (selectatt.dataType == 'LIST' && !selectatt.listMultiple) {
                                                item[attributeName] = attribute.listValue;
                                            } else if (selectatt.dataType == 'LIST' && selectatt.listMultiple) {
                                                item[attributeName] = attribute.mlistValue;
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

                                }
                            })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.updateWbsSeq = updateWbsSeq;
            function updateWbsSeq(actualRow, targetRow) {
                $rootScope.showBusyIndicator($('.view-content'));
                if (actualRow.objectType == "PROJECTWBSELEMENT") {
                    ProjectService.updateWbsItemSeq(actualRow.id, targetRow.id).then(
                        function (data) {
                            loadProjectPlan();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(sequenceUpdateMessage);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    ActivityService.updateWbsChildrenSeq(actualRow.id, targetRow.id).then(
                        function (data) {
                            loadProjectPlan();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(sequenceUpdateMessage);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function updateSequenceNumbers() {
                ProjectService.updateProjectSequenceNumbers(projectId).then(
                    function (data) {
                        loadProjectPlan();
                        loadProject();
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
                     }
                )
            }

            vm.finnishActivity = finnishActivity;
            function finnishActivity(activity) {
                activity.wbs = activity.parent;
                ActivityService.finishActivity(activity.id).then(
                    function (data) {
                        vm.activity = data;
                        loadProjectPlan();
                        $rootScope.showSuccessMessage(milestoneUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {
                    if (data.tabId == 'details.oldPlan') {
                        vm.projectPlanTabId = data.tabId;

                        if (validateJSON()) {
                            $window.localStorage.setItem("lastSelectedActivityTab", JSON.stringify('details.tasks'));
                            var setAttributes = JSON.parse($window.localStorage.getItem("allActivityattributes"));
                        }
                        else {
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
                                        $window.localStorage.setItem("allActivityattributes", "");
                                        vm.selectedAttributes = setAttributes
                                    } else {
                                        vm.selectedAttributes = setAttributes;
                                    }
                                    updateSequenceNumbers();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        else {
                            updateSequenceNumbers();
                        }

                    }
                });
            })();
        }
    }
);