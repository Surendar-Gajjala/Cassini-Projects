define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/qcrService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('QcrCaPaController', QcrCaPaController);

        function QcrCaPaController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window,
                                   DialogService, QcrService, CommonService) {
            var vm = this;
            vm.qcrId = $stateParams.qcrId;

            var parsed = angular.element("<div></div>");
            var addButton = parsed.html($translate.instant("CREATE")).html();
            var updateButton = parsed.html($translate.instant("UPDATE")).html();
            var deleteCaPaTitle = parsed.html($translate.instant("DELETE_CAPA")).html();
            var deleteCaPaDialogMsg = parsed.html($translate.instant("DELETE_CAPA_DIALOG_MSG")).html();
            var caPaDeletedMessage = parsed.html($translate.instant("CAPA_DELETED_MSG")).html();
            var caPaUpdatedMessage = parsed.html($translate.instant("CAPA_UPDATED_MSG")).html();
            var newCaPa = parsed.html($translate.instant("NEW_CAPA")).html();
            var updateCaPa = parsed.html($translate.instant("UPDATE_CAPA")).html();
            $scope.addCapaTitle = parsed.html($translate.instant("ADD_CAPA")).html();
            var atleastOneProblemSource = parsed.html($translate.instant("ATLEAST_ONE_PROBLEM_SOURCE")).html();

            vm.addCapa = addCapa;

            function addCapa() {
                if ($rootScope.qcrDetailCount.problemSources == 0) {
                    $rootScope.showWarningMessage(atleastOneProblemSource);
                } else {
                    var options = {
                        title: newCaPa,
                        template: 'app/desktop/modules/pqm/qcr/details/tabs/capa/newCaPaView.jsp',
                        controller: 'NewCaPaController as newCaPaVm',
                        resolve: 'app/desktop/modules/pqm/qcr/details/tabs/capa/newCaPaController',
                        width: 600,
                        showMask: true,
                        data: {
                            capaMode: "NEW"
                        },
                        buttons: [
                            {text: addButton, broadcast: 'add.qcr.capa.new'}
                        ],
                        callback: function (result) {
                            $rootScope.loadQcrDetails();
                            loadCaPa();
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            vm.showAddButton = true;
            function loadCaPa() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                QcrService.getAllQCRCaPa(vm.qcrId).then(
                    function (data) {
                        vm.capas = data;
                        if (vm.capas.length > 0) {
                            if (vm.capas[0].result == "NONE" || vm.capas[0].result == "PASS") {
                                vm.showAddButton = false;
                            } else {
                                vm.showAddButton = true;
                            }
                        } else {
                            vm.showAddButton = true;
                        }
                        CommonService.getPersonReferences(vm.capas, 'auditedBy');
                        CommonService.getPersonReferences(vm.capas, 'createdBy');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.editCaPa = editCaPa;
            function editCaPa(capa, type) {
                var options = {
                    title: updateCaPa,
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/capa/newCaPaView.jsp',
                    controller: 'NewCaPaController as newCaPaVm',
                    resolve: 'app/desktop/modules/pqm/qcr/details/tabs/capa/newCaPaController',
                    width: 600,
                    showMask: true,
                    data: {
                        capaData: capa,
                        capaMode: type
                    },
                    buttons: [
                        {text: updateButton, broadcast: 'add.qcr.capa.new'}
                    ],
                    callback: function (result) {
                        $rootScope.loadQcrDetails();
                        loadCaPa();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(capa) {
                capa.editMode = false;
                capa.auditNotes = capa.oldAuditNotes;
            }


            vm.deleteCaPa = deleteCaPa;

            function deleteCaPa(capa) {
                var options = {
                    title: deleteCaPaTitle,
                    message: deleteCaPaDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        QcrService.deleteQCRCaPa(vm.qcrId, capa.id).then(
                            function (data) {
                                $rootScope.loadQcrDetails();
                                $rootScope.showSuccessMessage(caPaDeletedMessage);
                                loadCaPa();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });

            }


            (function () {
                $scope.$on('app.qcr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.capa') {
                        loadCaPa();
                    }
                })
            })();
        }
    }
)
;