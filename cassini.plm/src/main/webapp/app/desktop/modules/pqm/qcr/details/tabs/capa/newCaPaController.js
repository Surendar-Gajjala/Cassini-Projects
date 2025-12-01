define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/modules/pqm/directives/qualityTypeDirective',
        'app/shared/services/core/qcrService'
    ],
    function (module) {

        module.controller('NewCaPaController', NewCaPaController);

        function NewCaPaController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                   QcrService, LoginService, ObjectTypeAttributeService, QualityTypeService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var enterRootCause = parsed.html($translate.instant("ENTER_ROOT_CAUSE")).html();
            var enterPreventiveAction = parsed.html($translate.instant("ENTER_PREVENTIVE_ACTION")).html();
            var enterCorrectiveAction = parsed.html($translate.instant("ENTER_CORRECTIVE_ATION")).html();
            var enterAuditNotes = parsed.html($translate.instant("ENTER_AUDIT_NOTES")).html();
            var caPaCreated = parsed.html($translate.instant("CAPA_CREATED")).html();
            var caPaUpdated = parsed.html($translate.instant("CAPA_UPDATED")).html();

            vm.capaMode = $scope.data.capaMode;
            vm.newCaPa = {
                id: null,
                qcr: $stateParams.qcrId,
                rootCauseAnalysis: null,
                correctiveAction: null,
                preventiveAction: null,
                capaNotes: null,
                auditNotes: null,
                result: "NONE",
                latest: true
            };
            vm.results = ['NONE', 'PASS', 'FAIL'];

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    var promise = null;
                    if ($scope.data.capaMode == "NEW") {
                        promise = QcrService.createQCRCaPa($stateParams.qcrId, vm.newCaPa);
                    } else if ($scope.data.capaMode == "AUDIT") {
                        promise = QcrService.updateQCRCaPaAudit($stateParams.qcrId, vm.newCaPa)
                    } else if ($scope.data.capaMode == "EDIT") {
                        promise = QcrService.updateQCRCaPa($stateParams.qcrId, vm.newCaPa)
                    }

                    if (promise != null) {
                        promise.then(
                            function (data) {
                                $scope.callback();
                                $rootScope.hideBusyIndicator();
                                $rootScope.hideSidePanel();
                                if (vm.capaMode == "EDIT" || vm.capaMode == "AUDIT") {
                                    $rootScope.showSuccessMessage(caPaUpdated);
                                } else {
                                    $rootScope.showSuccessMessage(caPaCreated);
                                }
                                vm.newCaPa = {
                                    id: null,
                                    qcr: null,
                                    rootCauseAnalysis: null,
                                    correctiveAction: null,
                                    preventiveAction: null,
                                    capaNotes: null
                                };
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate() {
                var valid = true;
                if ((vm.capaMode == "NEW" || vm.capaMode == "EDIT") && (vm.newCaPa.rootCauseAnalysis == null || vm.newCaPa.rootCauseAnalysis == "" || vm.newCaPa.rootCauseAnalysis == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterRootCause);
                } else if ((vm.capaMode == "NEW" || vm.capaMode == "EDIT") && (vm.newCaPa.correctiveAction == null || vm.newCaPa.correctiveAction == "" || vm.newCaPa.correctiveAction == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterCorrectiveAction);
                } else if ((vm.capaMode == "NEW" || vm.capaMode == "EDIT") && (vm.newCaPa.preventiveAction == null || vm.newCaPa.preventiveAction == "" || vm.newCaPa.preventiveAction == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterPreventiveAction);
                } else if ((vm.capaMode == "AUDIT") && (vm.newCaPa.auditNotes == null || vm.newCaPa.auditNotes == "" || vm.newCaPa.auditNotes == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterAuditNotes);
                }

                return valid;
            }


            (function () {
                //if ($application.homeLoaded == true) {
                    if ($scope.data.capaMode == "AUDIT" || $scope.data.capaMode == "EDIT") {
                        vm.newCaPa = angular.copy($scope.data.capaData);
                    }
                    $rootScope.$on('add.qcr.capa.new', create);
                //}
            })();
        }
    }
)
;