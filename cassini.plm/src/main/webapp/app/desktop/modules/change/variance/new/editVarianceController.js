define(['app/desktop/modules/change/change.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/varianceService'
    ],
    function (module) {
        module.controller('EditVarianceController', EditVarianceController);

        function EditVarianceController($scope, $rootScope, VarianceService,$timeout, $sce, $state, $translate, $cookies, CommonService, LoginService) {
            var vm = this;

            vm.variance = {
                id: null,
                title: null,
                description: null,
                varianceType: null,
                reasonForVariance: null,
                currentRequirement: null,
                requirementDeviation: null,
                originatorObject: null,
                approverObject: null,
                effectivityType: null,
                effectiveFrom: null,
                effectiveTo: null,
                notes: null,
                status: 'NEW'
            };

            vm.creating = false;
            vm.valid = true;
            vm.error = "";
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            var updatedSuccessfullyMsg = parsed.html($translate.instant("UPDATED_SUCCESS_MESSAGE")).html();

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "ASC"
                }
            };
            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            var ecoNumberValidation = $translate.instant("ECO_NUMBER_VALIDATION");
            var titleValidation = $translate.instant("TITLE_VALIDATION");
            var reasonForChangeValidation = $translate.instant("REASONFOR_CHANGE_VALIDATION");
            var typeValidation = parsed.html($translate.instant("SPECIFICATION_TYPE_VALIDATION")).html();
            var workflowValidation = $translate.instant("WORKFLOW_VALIDATION");
            var descriptionValidation = $translate.instant("DESCRIPTION_VALIDATION");
            var currentReqValidation = $translate.instant("CURRENT_REQUIREMENT_VALIDATION");
            var reqDeviationValidation = $translate.instant("REQUIREMENT_DEVIATION_VALIDATION");
            var originatorValidation = $translate.instant("ORIGINATOR_VALIDATION");
            var effectiveTypeValidation = $translate.instant("EFFECTIVE_TYPE_VALIDATION");

            function validate() {
                vm.valid = true;
                if (vm.variance.changeType == null || vm.variance.changeType == undefined ||
                    vm.variance.changeType == "") {
                    $rootScope.showWarningMessage(typeValidation);
                    vm.valid = false;
                } else if (vm.variance.varianceNumber == null || vm.variance.varianceNumber == undefined ||
                    vm.variance.varianceNumber == "") {
                    $rootScope.showWarningMessage(ecoNumberValidation);
                    vm.valid = false;
                } else if (vm.variance.title == null || vm.variance.title == undefined ||
                    vm.variance.title == "") {
                    $rootScope.showWarningMessage(titleValidation);
                    vm.valid = false;
                }  else if (vm.variance.description == null || vm.variance.description == undefined ||
                    vm.variance.description == "") {
                    $rootScope.showWarningMessage(descriptionValidation);
                    vm.valid = false;
                } else if (vm.variance.reasonForVariance == null || vm.variance.reasonForVariance == undefined ||
                    vm.variance.reasonForVariance == "") {
                    $rootScope.showWarningMessage(reasonForChangeValidation);
                    vm.valid = false;
                } else if (vm.variance.currentRequirement == null || vm.variance.currentRequirement == undefined ||
                    vm.variance.currentRequirement == "") {
                    $rootScope.showWarningMessage(currentReqValidation);
                    vm.valid = false;
                } else if (vm.variance.requirementDeviation == null || vm.variance.requirementDeviation == undefined ||
                    vm.variance.requirementDeviation == "") {
                    $rootScope.showWarningMessage(reqDeviationValidation);
                    vm.valid = false;
                } else if (vm.variance.originator == null || vm.variance.originator == undefined ||
                    vm.variance.originator == "") {
                    $rootScope.showWarningMessage(originatorValidation);
                    vm.valid = false;
                } else if ((vm.variance.effectivityType == null || vm.variance.effectivityType == undefined ||
                    vm.variance.effectivityType == "") && $rootScope.varianceType != "Waiver") {
                    $rootScope.showWarningMessage(effectiveTypeValidation);
                    vm.valid = false;
                }

                return vm.valid;
            }


            function update() {
                if (validate() == true) {
                    vm.creating = true;
                    vm.variance.originator = vm.variance.originatorObject.id;
                    VarianceService.updateVariance(vm.varianceId, vm.variance).then(
                        function (data) {
                            $rootScope.hideSidePanel('right');
                            $scope.callback(data);
                            vm.creating = false;
                            $rootScope.showSuccessMessage(vm.variance.varianceNumber + " " + updatedSuccessfullyMsg);
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                            vm.creating = false;
                        }
                    )

                }
            }

            function loadPersons() {
                LoginService.getActiveLogins().then(
                    function (data) {
                        angular.forEach(data, function(person){
                            if(!person.external){
                                vm.persons.push(person.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function  loadVariance() {
                VarianceService.getVariance(vm.varianceId).then(
                    function(data){
                        vm.variance = data;
                        CommonService.getPersonReferences([vm.variance], 'originator');
                        CommonService.getPersonReferences([vm.variance], 'approver');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    vm.varianceId = $scope.data.varianceId;
                    loadVariance();
                    loadPersons();
                    $rootScope.$on('app.variance.edit', update);
                //}
            })();
        }
    }
);