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
        module.controller('ExternalUserTasksController', ExternalUserTasksController);

        function ExternalUserTasksController($scope, $rootScope, $sce, $translate, $cookieStore, $window, $timeout, $application, $state, $stateParams, $cookies, $uibModal,
                                             CommonService, DialogService, UserTasksService, ObjectService, ProjectService, ActivityService, ItemService) {

            var vm = this;
            vm.loading = true;
            var owner = null;
            vm.userTasks = [];

            function loadUserTasks() {
                UserTasksService.getUserTasks(owner).then(
                    function (data) {
                        vm.tasks = data;
                        angular.forEach(vm.tasks, function (task) {
                            if (task.status === 'PENDING') {
                                vm.userTasks.push(task);
                            }
                        });
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showChangeWorkflow = showChangeWorkflow;
            function showChangeWorkflow(task) {
                if (task.objectType === 'ECO') {
                    $state.go('app.changes.eco.details', {
                        ecoId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType === 'ECR') {
                    $state.go('app.changes.ecr.details', {
                        ecrId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType === 'DCO') {
                    $state.go('app.changes.dco.details', {
                        dcoId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType === 'DCR') {
                    $state.go('app.changes.dcr.details', {
                        dcrId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType === 'ITEMMCO' || task.objectType == "OEMPARTMCO") {
                    $state.go('app.changes.mco.details', {
                        mcoId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType === 'WAIVER' || task.objectType === 'DEVIATION') {
                    $state.go('app.changes.variance.details', {
                        varianceId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'INSPECTIONPLANREVISION') {
                    $state.go('app.pqm.inspectionPlan.details', {
                        planId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'ITEMINSPECTION' || task.objectType == 'MATERIALINSPECTION') {
                    $state.go('app.pqm.inspection.details', {
                        inspectionId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'PROBLEMREPORT') {
                    $state.go('app.pqm.pr.details', {
                        problemReportId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'NCR') {
                    $state.go('app.pqm.ncr.details', {
                        ncrId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'QCR') {
                    $state.go('app.pqm.qcr.details', {
                        qcrId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {
                        itemId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {
                        manufacturerId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: task.parentObjectId,
                        manufacturePartId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {
                        requirementId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'SPECIFICATION') {
                    $state.go('app.rm.specifications.details', {
                        specId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'PROJECT') {
                    $state.go('app.pm.project.details', {
                        projectId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'PROJECTACTIVITY') {
                    $state.go('app.pm.project.activity.details', {
                        activityId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'PLMNPR') {
                    $state.go('app.nprs.details', {
                        nprId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'MROWORKORDER') {
                    $state.go('app.mro.workOrder.details', {
                        workOrderId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'CUSTOMOBJECT') {
                    $state.go('app.customobjects.details', {
                        customId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'PROGRAM') {
                    $state.go('app.pm.program.details', {
                        programId: task.objectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                } else if (task.objectType == 'PROJECTTASK') {
                    $state.go('app.pm.project.activity.task.details', {
                        taskId: task.objectId,
                        activityId: task.parentObjectId,
                        tab: 'details.workflow',
                        workflowActivity: task.source
                    });
                }
            }

            vm.showProjectActivity = showProjectActivity;
            function showProjectActivity(task) {
                $state.go('app.pm.project.activity.details', {activityId: task.source});
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
                                            taskId: task.id,
                                            permission: "WRITE"
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

            vm.showRequirementDetails = showRequirementDetails;
            function showRequirementDetails(task) {
                $state.go('app.req.requirements.details', {
                    requirementId: task.objectId,
                    tab: 'details.basic'
                });
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
)
;