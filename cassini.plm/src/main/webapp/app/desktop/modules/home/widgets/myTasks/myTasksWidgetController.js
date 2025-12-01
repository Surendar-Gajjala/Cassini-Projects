define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/userTasksService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('UserTasksWidgetController', UserTasksWidgetController);

        function UserTasksWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $translate,
                                           UserTasksService, ObjectService, CommonService, ProjectService, ActivityService, ItemService) {

            var vm = this;

            vm.loading = true;
            var owner = null;
            vm.userTasks = [];

            function loadUserTasks() {
                UserTasksService.getUserTasks(owner).then(
                    function (data) {
                        vm.userTasks = data;
                        vm.loading = false;
                        loadWorkflowAttachedTo();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function loadWorkflowAttachedTo() {
                var workflows = [];
                angular.forEach(vm.userTasks, function (task) {
                    if (task.sourceObject.objectType === 'PLMWORKFLOWSTATUS') {
                        workflows.push(task.contextObject);
                    }
                });

                if (workflows.length > 0) {
                    angular.forEach(workflows, function (workflow) {
                        ObjectService.getObjectReferences([workflow], workflow.attachedToType, "attachedTo");
                        if (workflow.attachedToType == "ITEMREVISION") {
                            ItemService.getItemByRevision(workflow.attachedTo).then(
                                function (item) {
                                    workflow.itemNumber = item.itemNumber;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                 }
                            )
                        }
                    });
                }
            }

            vm.showChangeWorkflow = showChangeWorkflow;
            function showChangeWorkflow(task) {
                var activity = task.sourceObject;
                var workflow = task.contextObject;
                var attachedTo = workflow.attachedTo;
                if (workflow.attachedToType == 'CHANGE') {
                    ObjectService.getObjectByTypeAndId('CHANGE', attachedTo).then(
                        function (object) {
                            if (object.changeType === 'ECO') {
                                $state.go('app.changes.eco.details', {
                                    ecoId: attachedTo,
                                    tab: 'details.workflow',
                                    workflowActivity: activity.id
                                });
                            } else if (object.changeType === 'ECR') {
                                $state.go('app.changes.ecr.details', {
                                    ecrId: attachedTo,
                                    tab: 'details.workflow',
                                    workflowActivity: activity.id
                                });
                            } else if (object.changeType === 'DCO') {
                                $state.go('app.changes.dco.details', {
                                    dcoId: attachedTo,
                                    tab: 'details.workflow',
                                    workflowActivity: activity.id
                                });
                            } else if (object.changeType === 'DCR') {
                                $state.go('app.changes.dcr.details', {
                                    dcrId: attachedTo,
                                    tab: 'details.workflow',
                                    workflowActivity: activity.id
                                });
                            } else if (object.changeType === 'MCO') {
                                $state.go('app.changes.mco.details', {
                                    mcoId: attachedTo,
                                    tab: 'details.workflow',
                                    workflowActivity: activity.id
                                });
                            } else if (object.changeType === 'WAIVER' || object.changeType === 'DEVIATION') {
                                $state.go('app.changes.variance.details', {
                                    varianceId: attachedTo,
                                    tab: 'details.workflow',
                                    workflowActivity: activity.id
                                });
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                } else if (workflow.attachedToType == 'INSPECTIONPLANREVISION') {
                    $state.go('app.pqm.inspectionPlan.details', {
                        planId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'ITEMINSPECTION' || workflow.attachedToType == 'MATERIALINSPECTION') {
                    $state.go('app.pqm.inspection.details', {
                        inspectionId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'PROBLEMREPORT') {
                    $state.go('app.pqm.pr.details', {
                        problemReportId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'NCR') {
                    $state.go('app.pqm.ncr.details', {
                        ncrId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'QCR') {
                    $state.go('app.pqm.qcr.details', {
                        qcrId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'ITEMREVISION') {
                    $state.go('app.items.details', {
                        itemId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {
                        manufacturerId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: workflow.attachedToObject.manufacturer,
                        manufacturePartId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'REQUIREMENT') {
                    $state.go('app.req.requirements.details', {
                        requirementId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'SPECIFICATION') {
                    $state.go('app.rm.specifications.details', {
                        specId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'PROJECT') {
                    $state.go('app.pm.project.details', {
                        projectId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'PROJECTACTIVITY') {
                    $state.go('app.pm.project.activity.details', {
                        activityId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'PLMNPR') {
                    $state.go('app.nprs.details', {
                        nprId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                } else if (workflow.attachedToType == 'MROWORKORDER') {
                    $state.go('app.mro.workOrder.details', {
                        workOrderId: attachedTo,
                        tab: 'details.workflow',
                        workflowActivity: activity.id
                    });
                }
            }

            vm.showProjectActivity = showProjectActivity;
            function showProjectActivity(task) {
                var activity = task.sourceObject;
                $state.go('app.pm.project.activity.details', {activityId: activity.id});
            }

            vm.showProjectTask = showProjectTask;
            function showProjectTask(usertask) {
                ActivityService.getActivityTask(1, usertask.source).then(
                    function (task) {
                        ActivityService.getActivity(task.activity).then(
                            function (activity) {
                                ProjectService.getWBSElement(1, activity.wbs).then(
                                    function (wbs) {
                                        $rootScope.projectId = wbs.project.id;
                                        $state.go('app.pm.project.activity.task.details', {
                                            activityId: activity.id,
                                            taskId: task.id
                                        });
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                );
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            (function () {
                $timeout(function () {
                    $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                    if ($rootScope.localStorageLogin != null) {
                        owner = $rootScope.localStorageLogin.login.person.id;
                    }
                    loadUserTasks();
                })
            })();
        }
    }
);