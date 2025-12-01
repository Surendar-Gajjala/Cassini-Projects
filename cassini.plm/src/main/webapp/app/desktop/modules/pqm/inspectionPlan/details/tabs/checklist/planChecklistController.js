define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/inspectionPlanService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('PlanChecklistController', PlanChecklistController);

        function PlanChecklistController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window,
                                         InspectionPlanService, DialogService) {
            var vm = this;
            vm.planId = $stateParams.planId;
            vm.checklists = [];

            vm.loading = true;

            var parsed = angular.element("<div></div>");
            var createButton = parsed.html($translate.instant("CLOSE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_CHECKLIST")).html();
            var deleteChecklistDialogMessage = parsed.html($translate.instant("DELETE_CHECKLIST_DIALOG_MESSAGE")).html();
            var checklistDeletedMessage = parsed.html($translate.instant("CHECKLIST_DELETED_MESSAGE")).html();
            var checklistSavedMessage = parsed.html($translate.instant("CHECKLIST_SAVED_MSG")).html();
            var enterTitle = parsed.html($translate.instant("ENTER_TITLE")).html();
            var deleteSectionTitle = parsed.html($translate.instant("DELETE_CHECKLIST_SECTION")).html();
            var deleteSectionDialogMessage = parsed.html($translate.instant("DELETE_CHECKLIST_SECTION_DIALOG_MESSAGE")).html();
            var sectionDeletedMessage = parsed.html($translate.instant("CHECKLIST_SECTION_DELETED_MESSAGE")).html();
            var sectionSavedMessage = parsed.html($translate.instant("CHECKLIST_SECTION_SAVED_MSG")).html();
            var wfStartedOneChecklistSbt = parsed.html($translate.instant("WF_STARTED_ONE_CHECKLIST_SBT")).html();
            $scope.addSectionTitle = parsed.html($translate.instant("ADD_SECTION")).html();
            $scope.addChecklistTitle = parsed.html($translate.instant("ADD_CHECKLIST")).html();


            var emptyChecklist = {
                id: null,
                inspectionPlan: vm.planId,
                title: null,
                summary: null,
                procedure: null,
                type: null,
                seq: null,
                parent: null
            };

            function loadChecklists() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                InspectionPlanService.getInspectionPlanChecklists(vm.planId).then(
                    function (data) {
                        vm.checklists = [];
                        angular.forEach(data, function (item) {
                            item.isNew = false;
                            item.editMode = false;
                            item.expanded = false;
                            item.level = 0;
                            item.count = 0;
                            item.checklistChildren = [];
                            if (item.children.length > 0) {
                                item.hasChecklist = true;
                            } else {
                                item.hasChecklist = false;
                            }
                            vm.checklists.push(item);
                            if (vm.expandAll) {
                                var index = vm.checklists.indexOf(item);
                                index = populateChildren(item, index);
                            }
                        });
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function populateChildren(section, lastIndex) {
                angular.forEach(section.children, function (checklist) {
                    lastIndex++;
                    checklist.isNew = false;
                    checklist.expanded = true;
                    checklist.editMode = false;
                    checklist.hasChecklist = false;
                    checklist.level = section.level + 1;
                    checklist.count = 0;
                    checklist.checklistChildren = [];
                    if (checklist.procedure != null) {
                        checklist.encodedProcedure = $sce.trustAsHtml(checklist.procedure);
                    }
                    vm.checklists.splice(lastIndex, 0, checklist);
                    section.count = section.count + 1;
                    section.checklistChildren.push(checklist);
                    section.expanded = true;
                    section.hasChecklist = true;
                    section.expanded = true;
                    lastIndex = populateChildren(checklist, lastIndex)
                });

                return lastIndex;
            }

            vm.expandAll = false;
            vm.expandAllSections = expandAllSections;
            function expandAllSections() {
                vm.expandAll = !vm.expandAll;
                loadChecklists();
            }

            vm.toggleNode = toggleNode;
            function toggleNode(section) {
                $rootScope.showBusyIndicator($('.view-container'));
                if (section.expanded == null || section.expanded == undefined) {
                    section.expanded = false;
                }
                section.expanded = !section.expanded;
                var index = vm.checklists.indexOf(section);
                if (section.expanded == false) {
                    var sectionIndex = vm.checklists.indexOf(section);
                    vm.checklists.splice(sectionIndex + 1, section.checklistChildren.length);
                    section.checklistChildren = [];
                    section.expanded = false;
                    checkExpandAllIcon();
                    $rootScope.hideBusyIndicator();
                } else {
                    InspectionPlanService.getChecklistChildren(vm.planId, section.id).then(
                        function (data) {
                            angular.forEach(data, function (checklist) {
                                checklist.isNew = false;
                                checklist.editMode = false;
                                checklist.hasChecklist = false;
                                checklist.level = section.level + 1;
                                if (section.checklistChildren == undefined) {
                                    section.checklistChildren = [];
                                }
                                if (checklist.procedure != null) {
                                    checklist.encodedProcedure = $sce.trustAsHtml(checklist.procedure);
                                }
                                section.checklistChildren.push(checklist);
                                section.hasChecklist = true;
                            })

                            angular.forEach(section.checklistChildren, function (checklist) {
                                index = index + 1;
                                vm.checklists.splice(index, 0, checklist);
                            })
                            checkExpandAllIcon();
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function checkExpandAllIcon() {
                vm.expandAll = false;
                var childCount = 0;
                var expandedCount = 0;
                angular.forEach(vm.checklists, function (checklist) {
                    if (checklist.level == 0) {
                        if (checklist.hasChecklist) {
                            childCount++;
                            if (!checklist.expanded) {
                                vm.expandAll = false;
                            } else {
                                expandedCount++;
                            }
                        }
                    }
                })
                if (childCount == expandedCount) {
                    vm.expandAll = true;
                }
            }

            vm.addSection = addSection;
            function addSection() {
                var newChecklist = angular.copy(emptyChecklist);
                newChecklist.type = "SECTION";
                newChecklist.editMode = true;
                newChecklist.isNew = true;
                vm.checklists.push(newChecklist);
            }

            vm.addChecklist = addChecklist;
            function addChecklist(section) {
                if (!section.expanded) {
                    var index = vm.checklists.indexOf(section);
                    section.expanded = true;
                    angular.forEach(section.children, function (checklist) {
                        checklist.isNew = false;
                        checklist.editMode = false;
                        index = index + 1;
                        if (section.checklistChildren == undefined) {
                            section.checklistChildren = [];
                        }
                        checklist.level = 1;
                        section.checklistChildren.push(checklist);
                        section.hasChecklist = true;
                        vm.checklists.splice(index, 0, checklist);
                    })
                }
                $timeout(function () {
                    var newChecklist = angular.copy(emptyChecklist);
                    newChecklist.editMode = true;
                    newChecklist.isNew = true;
                    newChecklist.type = "CHECKLIST";
                    newChecklist.parent = section.id;
                    newChecklist.level = 1;
                    var index = vm.checklists.indexOf(section);
                    if (section.checklistChildren == undefined) {
                        section.checklistChildren = [];
                    }
                    index = index + section.checklistChildren.length + 1;
                    newChecklist.parentChecklist = section;
                    section.checklistChildren.push(newChecklist);

                    vm.checklists.splice(index, 0, newChecklist);
                }, 500)
            }

            vm.onCancel = onCancel;
            function onCancel(item) {
                if (item.type == "SECTION") {
                    if (item.isNew) {
                        var index = vm.checklists.indexOf(item);
                        vm.checklists.splice(index, 1);
                    } else {
                        item.editMode = false;
                        item.isNew = false;
                        item.title = item.newTitle;
                    }
                } else {
                    if (item.isNew) {
                        if (item.parentChecklist != null) {
                            var checklistIndex = item.parentChecklist.checklistChildren.indexOf(item);
                            item.parentChecklist.checklistChildren.splice(checklistIndex, 1);
                        }
                        vm.checklists.splice(vm.checklists.indexOf(item), 1);
                    } else {
                        item.editMode = false;
                        item.isNew = false;
                        item.title = item.newTitle;
                        item.summary = item.newSummary;
                        item.prodedure = item.newProcedure;
                    }
                }
            }

            vm.onOk = onOk;
            function onOk(checklist) {
                if (validate(checklist)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    var parentChecklist = checklist.parentChecklist;
                    checklist.parentChecklist = null;
                    if (checklist.id == null || checklist.id == undefined || checklist.id == "") {
                        InspectionPlanService.createInspectionPlanChecklist(vm.planId, checklist).then(
                            function (data) {
                                checklist.id = data.id;
                                checklist.seq = data.seq;
                                checklist.procedure = data.procedure;
                                checklist.editMode = false;
                                checklist.isNew = false;
                                checklist.parentChecklist = parentChecklist;
                                checklist.level = 0;
                                if (checklist.type == "CHECKLIST") {
                                    if (checklist.procedure != null) {
                                        checklist.encodedProcedure = $sce.trustAsHtml(checklist.procedure);
                                    }
                                    checklist.paramsCount = 0;
                                    checklist.attachmentCount = 0;
                                    checklist.level = 1;
                                    checklist.parentChecklist.hasChecklist = true;
                                    checklist.parentChecklist.expanded = true;
                                    $rootScope.showSuccessMessage(checklistSavedMessage);
                                } else {
                                    $rootScope.showSuccessMessage(sectionSavedMessage);
                                }
                                checkExpandAllIcon();
                                $rootScope.loadPlanDetails();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                checklist.parentChecklist = parentChecklist;
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        InspectionPlanService.updateInspectionPlanChecklist(vm.planId, checklist).then(
                            function (data) {
                                checklist.id = data.id;
                                checklist.seq = data.seq;
                                checklist.procedure = data.procedure;
                                checklist.editMode = false;
                                checklist.isNew = false;
                                checklist.parentChecklist = parentChecklist;
                                if (checklist.type == "CHECKLIST") {
                                    if (checklist.procedure != null) {
                                        checklist.encodedProcedure = $sce.trustAsHtml(checklist.procedure);
                                    }
                                    $rootScope.showSuccessMessage(checklistSavedMessage);
                                } else {
                                    $rootScope.showSuccessMessage(sectionSavedMessage);
                                }
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                checklist.parentChecklist = parentChecklist;
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            vm.editChecklist = editChecklist;
            function editChecklist(checklist) {
                checklist.editMode = true;
                checklist.newTitle = checklist.title;
                checklist.newSummary = checklist.summary;
                checklist.newProcedure = checklist.prodedure;
            }

            vm.deleteChecklist = deleteChecklist;
            function deleteChecklist(checklist) {
                if (checklist.type == "CHECKLIST" && $rootScope.planWorkflowStarted && $rootScope.inspectionDetailsCount.checklists == 1) {
                    $rootScope.showWarningMessage(wfStartedOneChecklistSbt)
                } else if (checklist.type == "SECTION" && $rootScope.planWorkflowStarted && $rootScope.inspectionDetailsCount.sections == 1) {
                    $rootScope.showWarningMessage(wfStartedOneChecklistSbt)
                } else {
                    var options;
                    if (checklist.type == "CHECKLIST") {
                        options = {
                            title: deleteDialogTitle,
                            message: deleteChecklistDialogMessage + " [" + checklist.title + "] ?",
                            okButtonClass: 'btn-danger'
                        };
                    } else {
                        options = {
                            title: deleteSectionTitle,
                            message: deleteSectionDialogMessage + " [" + checklist.title + "] ?",
                            okButtonClass: 'btn-danger'
                        };
                    }

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            InspectionPlanService.deleteInspectionPlanChecklist(vm.planId, checklist.id).then(
                                function (data) {
                                    if (checklist.type == "SECTION") {
                                        $rootScope.showSuccessMessage(sectionDeletedMessage);
                                    } else {
                                        $rootScope.showSuccessMessage(checklistDeletedMessage);
                                    }
                                    checkExpandAllIcon();
                                    $rootScope.loadPlanDetails();
                                    loadChecklists();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    });
                }
            }

            function validate(checklist) {
                var valid = true;
                if (checklist.title == null || checklist.title == "" || checklist.title == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterTitle);
                }

                return valid;
            }

            vm.addChecklistParameters = addChecklistParameters;
            function addChecklistParameters(checklist) {
                var options = {
                    title: "Parameters",
                    template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/checklist/planChecklistParamsView.jsp',
                    controller: 'PlanChecklistParamsController as planChecklistParamsVm',
                    resolve: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/checklist/planChecklistParamsController',
                    width: 700,
                    showMask: true,
                    data: {
                        checklistDetails: checklist,
                        checklistParentId: vm.planId,
                        checklistMode: "PLAN"
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.checklists.params'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.addAttachments = addAttachments;
            function addAttachments(checklist) {
                var options = {
                    title: "Attachments",
                    template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/checklist/checklistAttachmentsView.jsp',
                    controller: 'ChecklistAttachmentsController as checklistAttachmentsVm',
                    resolve: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/checklist/checklistAttachmentsController',
                    width: 600,
                    showMask: true,
                    data: {
                        checklistDetails: checklist,
                        checklistParentId: vm.planId,
                        uploadChecklistId: checklist.id,
                        checklistMode: "PLAN",
                        checklistPermission: $rootScope.hasPermission('inspectionplan', 'edit') && !$rootScope.inspectionPlanRevision.released && !$rootScope.inspectionPlanRevision.rejected
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.checklists.attachments'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.showChecklistProcedure = showChecklistProcedure;
            $scope.hideProcedureDialog = hideProcedureDialog;
            function showChecklistProcedure(checklist) {
                checklist.oldProcedure = checklist.procedure;
                vm.checklistProcedure = checklist;
                showProcedureDialog()
            }

            function showProcedureDialog() {
                var modal = document.getElementById("checklist-procedure");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.checklist-header').outerHeight();
                    var checklistContentHeight = $('.checklist-content').outerHeight();
                    $(".procedure-content").height(checklistContentHeight - (headerHeight + 20));
                    $(".note-editable").height($(".procedure-content").outerHeight() - 120);
                    $(".note-editor").height($(".procedure-content").outerHeight() - 30);
                }, 200)
            }

            function hideProcedureDialog() {
                var modal = document.getElementById("checklist-procedure");
                modal.style.display = "none";
                vm.checklistProcedure.procedure = vm.checklistProcedure.oldProcedure;
            }

            (function () {
                $scope.$on('app.inspectionPlan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.checklist') {
                        loadChecklists();
                    }
                })
            })();
        }
    }
)
;