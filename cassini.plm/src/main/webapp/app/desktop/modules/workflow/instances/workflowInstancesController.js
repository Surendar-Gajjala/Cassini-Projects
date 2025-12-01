define(
    [
        'app/desktop/modules/workflow/workflow.module',
        'bpmn-modeler',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/recentlyVisitedService',
        'app/desktop/modules/workflow/history/workflowHistoryController'
    ],
    function (module, Modeler) {
        module.controller('WorkflowInstancesController', WorkflowInstancesController);

        function WorkflowInstancesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                             ObjectService, WorkflowService, RecentlyVisitedService, DialogService) {

            var parse = angular.element("<div></div>");
            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            $rootScope.viewInfo.title = parse.html($translate.instant('WORKFLOW_ALL_TITLE')).html();
            $rootScope.viewInfo.showDetails = false;

            $scope.objectTypes = ['All', 'ITEM', 'ECO', 'ECR', 'DCO', 'DCR', 'DEVIATION', 'WAIVER', 'ITEMMCO',
                'OEMPARTMCO', 'INSPECTIONPLANREVISION', 'ITEMINSPECTION', 'MATERIALINSPECTION',
                'PROBLEMREPORT', 'NCR', 'QCR', 'PROJECT', 'PROJECTACTIVITY', 'MANUFACTURER', 'MANUFACTURERPART',
                'WORKORDER', 'PLMNPR', 'CUSTOMOBJECT'];
            var vm = this;

            vm.loading = false;

            var viewer = null;
            vm.workflow = null;

            var svgNodes = new Hashtable();

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.filters = {
                name: null,
                searchQuery: null
            };

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            $rootScope.allWorkflowType = "instances";
            function nextPage() {
                if (vm.workflowInstances.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadWorkflowInstances();
                }
            }

            function previousPage() {
                if (vm.workflowInstances.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadWorkflowInstances();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadWorkflowInstances();
            }

            $scope.wfHistory = parse.html($translate.instant('CLICK_TO_SHOW_WFH')).html();
            vm.gotoWorkflow = gotoWorkflow;
            function gotoWorkflow(workflow) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = workflow.workflowRevision;
                vm.recentlyVisited.objectType = "PLMWORKFLOWDEFINITION";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.workflow.editor', {mode: 'edit', workflow: workflow.workflowRevision});
                    }, function (error) {
                        $state.go('app.workflow.editor', {mode: 'edit', workflow: workflow.workflowRevision});
                    }
                );
            }

            function loadWorkflowInstances() {
                $rootScope.showBusyIndicator($('body'));
                $scope.selecteObject = 'All';
                WorkflowService.getAllWorkflowInstances(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.workflowInstances = data;
                        var objects = [];
                        for (var i = 0; i < vm.workflowInstances.content.length; i++) {
                            objects.push(vm.workflowInstances.content[i].workflow);

                        }
                        ObjectService.getObjectReferences(objects, "PLMWORKFLOWDEFINITION", "workflowRevision");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            vm.objectFilter = objectFilter;
            function objectFilter(obje) {
                if (obje == 'All') {
                    $scope.selecteObject = obje;
                    vm.searchText = null;
                    loadWorkflowInstances();
                } else {
                    $rootScope.showBusyIndicator($('body'));
                    $scope.selecteObject = obje;
                    vm.searchText = obje;
                    WorkflowService.getFilterWorkflowInstances(obje, vm.pageable, vm.filters).then(
                        function (data) {
                            vm.workflowInstances = data;

                            var objects = [];

                            for (var i = 0; i < vm.workflowInstances.content.length; i++) {
                                objects.push(vm.workflowInstances.content[i].workflow);
                            }
                            ObjectService.getObjectReferences(objects, "PLMWORKFLOWDEFINITION", "workflowRevision");
                            $rootScope.hideBusyIndicator();
                        })
                }

            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('body'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadWorkflowInstances();
                } else {
                    resetPage();
                    loadWorkflowInstances();
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
            }

            vm.showInstanceDetails = showInstanceDetails;
            function showInstanceDetails(workflow) {
                if (workflow.objectType == "ITEM") {
                    $state.go('app.items.details', {itemId: workflow.workflow.attachedTo, tab: 'details.workflow'});
                } else if (workflow.objectType == "ECO") {
                    $state.go('app.changes.eco.details', {
                        ecoId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "DCO") {
                    $state.go('app.changes.dco.details', {
                        dcoId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "DCR") {
                    $state.go('app.changes.dcr.details', {
                        dcrId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "ECR") {
                    $state.go('app.changes.ecr.details', {
                        ecrId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "MCO") {
                    $state.go('app.changes.mco.details', {
                        mcoId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "DEVIATION") {
                    $state.go('app.changes.variance.details', {
                        varianceId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "INSPECTIONPLANREVISION") {
                    $state.go('app.pqm.inspectionPlan.details', {
                        planId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    })
                } else if (workflow.objectType == "ITEMINSPECTION") {
                    $state.go('app.pqm.inspection.details', {
                        inspectionId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    })
                } else if (workflow.objectType == "PROBLEMREPORT") {
                    $state.go('app.pqm.pr.details', {
                        problemReportId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    })
                } else if (workflow.objectType == "NCR") {
                    $state.go('app.pqm.ncr.details', {ncrId: workflow.workflow.attachedTo, tab: 'details.workflow'})
                } else if (workflow.objectType == "QCR") {
                    $state.go('app.pqm.qcr.details', {qcrId: workflow.workflow.attachedTo, tab: 'details.workflow'})
                } else if (workflow.type == "PROJECT") {
                    $state.go('app.pm.project.details', {
                        projectId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "SPECIFICATION") {
                    $state.go('app.rm.specifications.details', {
                        specId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "REQUIREMENT") {
                    $state.go('app.rm.requirements.details', {
                        requirementId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "MANUFACTURERPART") {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: workflow.id,
                        manufacturePartId: workflow.workflow.attachedTo, tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "MANUFACTURER") {
                    $state.go('app.mfr.details', {
                        manufacturerId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PROJECTACTIVITY") {
                    $state.go('app.pm.project.activity.details', {
                        activityId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PLMNPR") {
                    $state.go('app.nprs.details', {nprId: workflow.workflow.attachedTo, tab: 'details.workflow'});
                } else if (workflow.type == "WORKORDER") {
                    $state.go('app.mro.workOrder.details', {
                        workOrderId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "CUSTOMOBJECT") {
                    $state.go('app.customobjects.details', {
                        customId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PROGRAMTEMPLATE") {
                    $state.go('app.pm.programtemplate.details', {
                        templateId: workflow.workflow.attachedTo,
                        tab: "details.workflow"
                    });
                } else if (workflow.type == "PROGRAM") {
                    $state.go('app.pm.program.details', {
                        programId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "TEMPLATE") {
                    $state.go('app.templates.details', {
                        templateId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "TEMPLATETASK") {
                    $state.go('app.templates.task.details', {
                        taskId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "TEMPLATEACTIVITY") {
                    $state.go('app.templates.activity.details', {
                        activityId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PROJECTACTIVITY") {
                    $state.go('app.pm.project.activity.details', {
                        activityId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PROJECTTASK") {
                    $state.go('app.pm.project.activity.task.details', {
                        activityId: workflow.task.activity,
                        taskId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                }
            }

            vm.showWorkflow = showWorkflow;

            function showWorkflow(workflow) {
                vm.revision = null;
                if (workflow.number != null) {
                    vm.number = workflow.number;
                } else {
                    vm.number = workflow.description;
                }
                if (workflow.revision != null) {
                    vm.revision = workflow.revision;
                }
                vm.workflowName = workflow.workflow.name;
                showWorkflowPreview();
                loadWorkflow(workflow.workflow);
            }

            function showWorkflowPreview() {
                var modal = document.getElementById("workflow-rollup");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.workflow-header').outerHeight();
                    var workflowContentHeight = $('.workflowRollup-content').outerHeight();
                    $(".workflow-content").height(workflowContentHeight - headerHeight);
                }, 200)


                $(window).resize(resizeCanvas);

            }

            $scope.hideWorkflowPreview = hideWorkflowPreview;
            function hideWorkflowPreview() {
                var modal = document.getElementById("workflow-rollup");
                modal.style.display = "none";
                viewer.get('eventBus').fire('diagram.clear');
                $(window).off("resize", resizeCanvas);
                vm.workflow = null;

            }

            function loadWorkflow(workflow) {
                WorkflowService.getWorkflow(workflow.id).then(
                    function (data) {
                        vm.workflow = data;
                        vm.workflow.workflowRevisionObject = null;

                        $scope.workflow = data;
                        vm.workflow.currentStatusObject = getStatusById(vm.workflow.currentStatus);
                        if (viewer == null) {
                            viewer = new Modeler({
                                container: '#canvas',
                                additionalModules: [{
                                    zoomScroll: ['value', '']
                                }]
                            });
                        }
                        viewer.importXML(vm.workflow.diagram, function (err) {
                            if (!err) {
                                resizeCanvas();
                                $rootScope.hideBusyIndicator();

                                highlightRevisionTriggerStatus();

                            } else {
                                console.log('Error loading workflow file:', err);
                                $rootScope.hideBusyIndicator();
                            }
                        });

                        initEvents(viewer);
                        processWorkflowFile(vm.workflow.diagram);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function highlightRevisionTriggerStatus() {
                ObjectService.getObjectByTypeAndId(vm.workflow.attachedToType, vm.workflow.attachedTo).then(
                    function (object) {
                        if (object.objectType === "CHANGE" && (object.changeType === "ECO" || object.changeType === "DCO")) {
                            if (object.revisionCreationType === "ACTIVITY_COMPLETION") {
                                for (var i = 0; i < vm.workflow.statuses.length; i++) {
                                    var status = vm.workflow.statuses[i];
                                    if (object.workflowStatus === status.id) {
                                        var modeling = viewer.get('modeling');
                                        var registry = viewer.get('elementRegistry');
                                        var found = registry.get(status.diagramId);
                                        if (found !== null) {
                                            found.type = "bpmn:ServiceTask";
                                            modeling.updateProperties(found, {type: "bpmn:ServiceTask"});
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function initEvents(viewer) {
                var eventBus = viewer.get('eventBus');
                eventBus.on('element.click', function (e) {
                    // e.element = the model element
                    // e.gfx = the graphical element
                    if (e.element.type == 'bpmn:Task') {
                        var status = mapStatuses.get(e.element.id);
                        if (status != null) {
                            selectStatus(status);
                        }
                    }
                    else {
                        vm.selectedStatus = null;
                    }

                    $scope.$apply();
                });
            }

            function selectStatus(status) {
                vm.selectedStatus = status;
                resizeCanvas();
                //$scope.$apply();
            }

            function resizeCanvas() {
                $timeout(function () {
                    $('.workflow-content').height($('.workflowRollup-content').outerHeight() - 50);
                    $('.workflow-viewer').height($('.workflow-content').outerHeight() - 100);
                    $('.workflow-viewer').width($('.workflow-content').outerWidth() - 400);
                    if (viewer != null) {
                        viewer.get('canvas').zoom('fit-viewport', 'auto');
                    }
                });
            }

            function processWorkflowFile(xml) {
                var moddle = viewer._createModdle({});
                moddle.fromXML(xml, function (err, definitions) {
                    var rootElement = definitions.rootElements[0];
                    var flowElements = rootElement.flowElements;
                    angular.forEach(flowElements, function (element) {
                        if (element.$type == 'bpmn:StartEvent' ||
                            element.$type == 'bpmn:EndEvent' ||
                            element.$type == 'bpmn:IntermediateThrowEvent' ||
                            element.$type == 'bpmn:Task') {
                            var node = $("[data-element-id='" + element.id + "']");
                            if (node.prop('tagName') == 'g') {
                                svgNodes.put(element.id, node)
                            }
                        }
                    });
                    updateStatusColors();
                });

            }

            function updateStatusColors() {
                setColors(vm.workflow.start);
                setColors(vm.workflow.finish);
                angular.forEach(vm.workflow.statuses, function (status) {
                    setColors(status);
                })
            }

            function setColors(status) {
                removeClassSVG(status.diagramId, 'inprocess-status');
                removeClassSVG(status.diagramId, 'onhold-status');
                removeClassSVG(status.diagramId, 'completed-status');
                removeClassSVG(status.diagramId, 'terminated-status');
                if (status.flag == 'INPROCESS') {
                    addClassSVG(status.diagramId, 'inprocess-status');
                }
                else if (status.flag == 'COMPLETED') {
                    addClassSVG(status.diagramId, 'completed-status');
                }
                else if (status.flag == 'TERMINATED') {
                    addClassSVG(status.diagramId, 'terminated-status');
                }
                else if (status.flag == 'ONHOLD') {
                    addClassSVG(status.diagramId, 'onhold-status');
                }
            }


            function addClassSVG(id, className) {
                var node = svgNodes.get(id);
                if (node != null) {
                    node.attr('class', function (index, existingClassNames) {
                        return existingClassNames + ' ' + className;
                    });
                }
            }

            function removeClassSVG(id, className) {
                var node = svgNodes.get(id);
                if (node != null) {
                    node.attr('class', function (index, existingClassNames) {
                        return existingClassNames.replace(className, '');
                    });
                }
            }

            function getStatusById(id) {
                var found = null;

                if (vm.workflow.start.id == id) {
                    found = vm.workflow.start;
                }
                else if (vm.workflow.finish.id == id) {
                    found = vm.workflow.finish;
                }
                else {
                    angular.forEach(vm.workflow.statuses, function (status) {
                        if (status.id == id) {
                            found = status;
                        }
                    })
                }

                return found;
            }

            (function () {
                loadWorkflowInstances();
            })();
        }
    }
);