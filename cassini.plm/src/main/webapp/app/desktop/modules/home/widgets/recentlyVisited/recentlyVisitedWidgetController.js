define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('RecentlyVisitedController', RecentlyVisitedController);

        function RecentlyVisitedController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $translate,
                                           RecentlyVisitedService, CommonService) {

            var vm = this;

            vm.recentlyVisited = [];
            vm.loading = true;

            var owner = null;

            var pageable = {
                page: 0,
                size: 25,
                sort: {
                    field: "visitedDate",
                    order: "DESC"
                }
            };

            vm.newRecentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function loadRecentlyVisited() {
                RecentlyVisitedService.getAllRecentlyVisited(owner, pageable).then(
                    function (data) {
                        vm.recentlyVisited = data.content;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.showObjectDetails = showObjectDetails;
            function showObjectDetails(recentlyVisited) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                if (recentlyVisited.type == 'ITEM') {
                    $rootScope.seletedItemId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {
                            $state.go('app.items.details', {itemId: recentlyVisited.item.latestRevision});
                        }, function (error) {
                            $state.go('app.items.details', {itemId: recentlyVisited.item.latestRevision});
                        }
                    )
                } else if (recentlyVisited.type == 'ECO') {
                    $state.go('app.changes.eco.details', {ecoId: recentlyVisited.objectId});
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (recentlyVisited.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: recentlyVisited.objectId});
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (recentlyVisited.type == 'TERMINOLOGY') {
                    $state.go('app.rm.glossary.details', {glossaryId: recentlyVisited.objectId});
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (recentlyVisited.type == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: recentlyVisited.objectId, tab: 'details.plan'})
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                    )
                } else if (recentlyVisited.type == 'TEMPLATE') {
                    $state.go('app.templates.details', {templateId: recentlyVisited.objectId});
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (recentlyVisited.type == 'WORKFLOW') {
                    $rootScope.allWorkflowType = "templates";
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: recentlyVisited.objectId});
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (recentlyVisited.type == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: recentlyVisited.manufacturer.id,
                        manufacturePartId: recentlyVisited.objectId
                    });
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (recentlyVisited.type == 'SPECIFICATION') {
                    $state.go('app.rm.specifications.details', {specId: recentlyVisited.objectId})
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                else if (recentlyVisited.type == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: recentlyVisited.objectId});
                    vm.newRecentlyVisited.objectId = recentlyVisited.objectId;
                    vm.newRecentlyVisited.objectType = recentlyVisited.objectType;
                    vm.newRecentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                    RecentlyVisitedService.createRecentlyVisited(vm.newRecentlyVisited).then(
                        function (data) {

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (recentlyVisited.type == "INSPECTIONPLAN") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.pqm.inspectionPlan.details', {
                                planId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.pqm.inspectionPlan.details', {
                                planId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == "INSPECTION") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.pqm.inspection.details', {
                                inspectionId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            })
                        }, function (error) {
                            $state.go('app.pqm.inspection.details', {
                                inspectionId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            })
                        }
                    )
                } else if (recentlyVisited.type == "PROBLEMREPORT") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go("app.pqm.pr.details", {
                                problemReportId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go("app.pqm.pr.details", {
                                problemReportId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == "NCR") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go("app.pqm.ncr.details", {ncrId: recentlyVisited.objectId, tab: 'details.basic'});
                        }, function (error) {
                            $state.go("app.pqm.ncr.details", {ncrId: recentlyVisited.objectId, tab: 'details.basic'});
                        }
                    )
                } else if (recentlyVisited.type == "QCR") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go("app.pqm.qcr.details", {qcrId: recentlyVisited.objectId, tab: 'details.basic'});
                        }, function (error) {
                            $state.go("app.pqm.qcr.details", {qcrId: recentlyVisited.objectId, tab: 'details.basic'});
                        }
                    )
                } else if (recentlyVisited.type == "ECR") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.changes.ecr.details', {
                                ecrId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go("app.changes.ecr.details", {
                                ecrId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == "DCO") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.changes.dco.details', {
                                dcoId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go("app.changes.dco.details", {
                                dcoId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == "DCR") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.changes.dcr.details', {
                                dcrId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go("app.changes.dcr.details", {
                                dcrId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == "MCO") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.changes.mco.details', {
                                mcoId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go("app.changes.mco.details", {
                                mcoId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.objectType == "VARIANCE") {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.changes.variance.details', {
                                varianceId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.changes.variance.details', {
                                varianceId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'PLANT') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.plant.details', {
                                plantId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.plant.details', {
                                plantId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'WORKCENTER') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.workcenter.details', {
                                workcenterId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.workcenter.details', {
                                workcenterId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'MACHINE') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.machine.details', {
                                machineId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.machine.details', {
                                machineId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'INSTRUMENT') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.instrument.details', {
                                instrumentId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.instrument.details', {
                                instrumentId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'EQUIPMENT') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.equipment.details', {
                                equipmentId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.equipment.details', {
                                equipmentId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'TOOL') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.tool.details', {toolId: recentlyVisited.objectId, tab: 'details.basic'});
                        }, function (error) {
                            $state.go('app.mes.masterData.tool.details', {toolId: recentlyVisited.objectId, tab: 'details.basic'});
                        }
                    )
                } else if (recentlyVisited.type == 'JIGFIXTURE') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.jigsAndFixtures.details', {
                                jigsFixId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.jigsAndFixtures.details', {
                                jigsFixId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'MATERIAL') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.material.details', {
                                materialId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.material.details', {
                                materialId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'MANPOWER') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.manpower.details', {
                                manpowerId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.manpower.details', {
                                manpowerId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'SHIFT') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.shift.details', {
                                shiftId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.shift.details', {
                                shiftId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                } else if (recentlyVisited.type == 'OPERATION') {
                    RecentlyVisitedService.createRecentlyVisited(recentlyVisited).then(
                        function (data) {
                            $state.go('app.mes.masterData.operation.details', {
                                operationId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }, function (error) {
                            $state.go('app.mes.masterData.operation.details', {
                                operationId: recentlyVisited.objectId,
                                tab: 'details.basic'
                            });
                        }
                    )
                }
            }

            (function () {
                $timeout(function () {
                    $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                    if ($rootScope.localStorageLogin != null) {
                        owner = $rootScope.localStorageLogin.login.person.id;
                    }
                    loadRecentlyVisited();
                })
            })();

        }

    }
);