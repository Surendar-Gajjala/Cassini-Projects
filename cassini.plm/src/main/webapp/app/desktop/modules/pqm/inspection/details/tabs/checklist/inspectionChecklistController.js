define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/inspectionService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('InspectionChecklistController', InspectionChecklistController);

        function InspectionChecklistController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window,
                                               InspectionPlanService, InspectionService, DialogService, LoginService, CommonService) {
            var vm = this;
            vm.inspectionId = $stateParams.inspectionId;
            vm.checklists = [];

            vm.loading = true;

            var parsed = angular.element("<div></div>");
            var createButton = parsed.html($translate.instant("CLOSE")).html();
            var checklistUpdated = parsed.html($translate.instant("CHECKLIST_UPDATED")).html();
            var attachmentsTitle = parsed.html($translate.instant("ATTACHMENTS")).html();
            var parametersTitle = parsed.html($translate.instant("PARAMETERS")).html();
            var assignedToCannotBeEmpty = parsed.html($translate.instant("ASSIGNED_TO_NOT_EMPTY")).html();
            var notesToCannotBeEmpty = parsed.html($translate.instant("NOTES_NOT_EMPTY")).html();

            vm.results = ['NONE', 'PASS', 'FAIL'];
            vm.expandAll = false;
            $rootScope.loadInspectionChecklists = loadInspectionChecklists;
            function loadInspectionChecklists() {
                vm.loading = true;
                InspectionService.getInspectionChecklists(vm.inspectionId).then(
                    function (data) {
                        vm.checklists = [];
                        angular.forEach(data, function (item) {
                            item.expanded = false;
                            item.level = 0;
                            item.count = 0;
                            item.editMode = false;
                            item.checklistChildren = [];
                            if (item.children.length > 0) {
                                item.hasChecklist = true;
                            } else {
                                item.hasChecklist = false;
                            }
                            item.count = item.children.length;
                            vm.checklists.push(item);
                            if (vm.expandAll) {
                                var index = vm.checklists.indexOf(item);
                                index = populateChildren(item, index);
                            }
                        });
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.checklists, 'assignedTo');
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function populateChildren(section, lastIndex) {
                angular.forEach(section.children, function (checklist) {
                    lastIndex++;
                    section.expanded = true;
                    checklist.editMode = false;
                    checklist.hasChecklist = false;
                    checklist.level = section.level + 1;
                    checklist.checklistChildren = [];
                    if (checklist.planChecklist.procedure != null) {
                        checklist.planChecklist.encodedProcedure = $sce.trustAsHtml(checklist.planChecklist.procedure);
                    }
                    vm.checklists.splice(lastIndex, 0, checklist);
                    section.count = section.count + 1;
                    section.checklistChildren.push(checklist);
                    section.hasChecklist = true;
                    lastIndex = populateChildren(checklist, lastIndex)
                });

                return lastIndex;
            }

            vm.expandAllSections = expandAllSections;
            function expandAllSections() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.expandAll = !vm.expandAll;
                loadInspectionChecklists();
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
                    InspectionService.getInspectionChecklistChildren(vm.inspectionId, section.id).then(
                        function (data) {
                            section.count = data.length;
                            angular.forEach(data, function (checklist) {
                                checklist.isNew = false;
                                checklist.editMode = false;
                                checklist.hasChecklist = false;
                                checklist.level = section.level + 1;
                                if (section.checklistChildren == undefined) {
                                    section.checklistChildren = [];
                                }
                                if (checklist.planChecklist.procedure != null) {
                                    checklist.planChecklist.encodedProcedure = $sce.trustAsHtml(checklist.planChecklist.procedure);
                                }
                                section.checklistChildren.push(checklist);
                                section.hasChecklist = true;
                            })

                            angular.forEach(section.checklistChildren, function (checklist) {
                                index = index + 1;
                                vm.checklists.splice(index, 0, checklist);
                            })
                            checkExpandAllIcon();
                            CommonService.getPersonReferences(vm.checklists, 'assignedTo');
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
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

            vm.editChecklist = editChecklist;
            function editChecklist(checklist) {
                checklist.editMode = true;
                checklist.oldNotes = checklist.notes;
                checklist.oldResult = checklist.result;
                checklist.oldAssignedToObject = checklist.assignedToObject;
                checklist.oldAssignedTo = checklist.assignedTo;
            }


            vm.onCancel = onCancel;
            function onCancel(checklist) {
                checklist.editMode = false;
                checklist.notes = checklist.oldNotes;
                checklist.result= checklist.oldResult;
                checklist.assignedToObject = checklist.oldAssignedToObject;
                checklist.assignedTo = checklist.oldAssignedTo;
            }

            vm.selectAssignedTo = selectAssignedTo;
            function selectAssignedTo(checklist, person) {
                checklist.assignedToObject = person;
            }

            vm.addChecklistParameters = addChecklistParameters;
            function addChecklistParameters(checklist) {
                if (checklist.assignedTo == null || checklist.assignedTo == "") {
                    $rootScope.showWarningMessage("Assigned To cannot be empty");
                } else {
                    var options = {
                        title: parametersTitle,
                        template: 'app/desktop/modules/pqm/inspection/details/tabs/checklist/inspectionChecklistParamsView.jsp',
                        controller: 'InspectionChecklistParamsController as inspectionChecklistParamsVm',
                        resolve: 'app/desktop/modules/pqm/inspection/details/tabs/checklist/inspectionChecklistParamsController',
                        width: 700,
                        showMask: true,
                        data: {
                            checklistDetails: checklist
                        },
                        buttons: [
                            {text: createButton, broadcast: 'app.checklist.params'}
                        ],
                        callback: function () {
                            $timeout(function () {
                                loadInspectionChecklists();
                            }, 500);
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            vm.addAttachments = addAttachments;
            function addAttachments(checklist) {
                if (checklist.assignedTo == null || checklist.assignedTo == "") {
                    $rootScope.showWarningMessage(assignedToCannotBeEmpty);
                } else {
                    var options = {
                        title: attachmentsTitle,
                        template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/checklist/checklistAttachmentsView.jsp',
                        controller: 'ChecklistAttachmentsController as checklistAttachmentsVm',
                        resolve: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/checklist/checklistAttachmentsController',
                        width: 600,
                        showMask: true,
                        data: {
                            checklistDetails: checklist,
                            checklistParentId: vm.inspectionId,
                            uploadChecklistId: checklist.id,
                            checklistMode: "ITEMINSPECTION",
                            checklistPermission: $rootScope.hasPermission('inspection','edit') && !$rootScope.inspection.released && $rootScope.inspection.statusType != 'REJECTED'
                        },
                        buttons: [
                            {text: createButton, broadcast: 'app.checklists.attachments'}
                        ],
                        callback: function () {
                            $timeout(function () {
                                loadInspectionChecklists();
                            }, 500);
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            vm.updateChecklist = updateChecklist;
            function updateChecklist(checklist) {
                if (validateChecklist(checklist)) {
                    InspectionService.updateInspectionChecklist(vm.inspectionId, checklist.id, checklist).then(
                        function (data) {
                            checklist.editMode = false;
                            checklist.status = data.status;
                            $rootScope.showSuccessMessage(checklistUpdated);
                            $rootScope.hideBusyIndicator();
                            $rootScope.loadInspectionDetails();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateChecklist(checklist) {
                var valid = true;
                if (checklist.assignedTo == null || checklist.assignedTo == "" || checklist.assignedTo == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(assignedToCannotBeEmpty);
                } else if (checklist.paramsCount == 0 && (checklist.result == "PASS" || checklist.result == "FAIL") && (checklist.notes == null || checklist.notes == "" || checklist.notes == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(notesToCannotBeEmpty);
                }

                return valid;
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $scope.showChecklistProcedure = showChecklistProcedure;
            $scope.hideProcedureDialog = hideProcedureDialog;
            function showChecklistProcedure(checklist) {
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
            }

            (function () {
                $scope.$on('app.inspection.tabActivated', function (event, data) {
                    if (data.tabId == 'details.checklist') {
                        loadInspectionChecklists();
                        loadPersons();
                    }
                })
            })();
        }
    }
)
;