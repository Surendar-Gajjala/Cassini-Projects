define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/varianceService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'

    ],
    function (module) {
        module.controller('VarianceBasicInfoController', VarianceBasicInfoController);

        function VarianceBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, CommonService, VarianceService, $translate) {
            var vm = this;

            vm.varianceId = $stateParams.varianceId;
            $rootScope.loadBasicVariance = loadBasicVariance;
            vm.variance = null;
            $scope.title = null;
            $scope.reasonForVariance = null;
            $scope.description = null;
            $scope.currentRequirement = null;
            $scope.requirementDeviation = null;
            vm.loading = true;
            vm.editEffectiveTo = false;
            vm.editEffectiveFrom = false;
            vm.varianceStatus = false;
            vm.updateEffectiveFrom = updateEffectiveFrom;
            vm.updateEffectiveTo = updateEffectiveTo;
            var parsed = angular.element("<div></div>");

            function loadBasicVariance() {
                VarianceService.getVariance(vm.varianceId).then(
                    function (data) {
                        vm.variance = data;
                        $rootScope.variance = data;
                        $rootScope.startVarianceWorkflow = vm.variance.startWorkflow;
                        $scope.title = vm.variance.title;
                        $scope.reasonForVariance = vm.variance.reasonForVariance;
                        $scope.description = vm.variance.description;
                        $scope.currentRequirement = vm.variance.currentRequirement;
                        $scope.requirementDeviation = vm.variance.requirementDeviation;
                        if (vm.variance.createdDate) {
                            vm.variance.createdDatede = moment(vm.variance.createdDate, "$rootScope.applicationDateSelectFormat, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        }
                        if (vm.variance.modifiedDate) {
                            vm.variance.modifiedDatede = moment(vm.variance.modifiedDate, "$rootScope.applicationDateSelectFormat, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        }
                        if (vm.variance.statusType == 'REJECTED' || vm.variance.statusType == 'RELEASED') {
                            vm.varianceStatus = true;
                        }
                        if ($rootScope.varianceType == 'Deviation' || vm.variance.varianceType == 'DEVIATION') {
                            $rootScope.viewInfo.title = deviationTitle;
                        }
                        if ($rootScope.varianceType == 'Waiver' || vm.variance.varianceType == 'WAIVER') {
                            $rootScope.viewInfo.title = waiverTitle;
                        }
                        $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                            format(vm.variance.varianceNumber, vm.variance.status)
                        CommonService.getPersonReferences([vm.variance], 'originator');
                        CommonService.getPersonReferences([vm.variance], 'modifiedBy');
                        CommonService.getPersonReferences([vm.variance], 'createdBy');
                        vm.loading = false;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var effectiveFromValidation = parsed.html($translate.instant("EFFECTIVE_FROM_VALIDATION")).html();
            var effectiveFromAfterCreatedDate = parsed.html($translate.instant("EFFECTIVE_FROM_AFTER_CREATION")).html();
            var effectiveToAfterCreatedDate = parsed.html($translate.instant("EFFECTIVE_TO_AFTER_CREATION")).html();
            var effectiveToValidation = parsed.html($translate.instant("EFFECTIVE_TO_VALIDATION")).html();
            var deviationTitle = parsed.html($translate.instant("DEVIATION_TITLE")).html();
            var waiverTitle = parsed.html($translate.instant("WAIVER_TITLE")).html();
            vm.validateEffectiveFrom = validateEffectiveFrom;
            function validateEffectiveFrom() {
                var valid = true;
                if (vm.variance.effectiveFrom != null && vm.variance.effectiveFrom != "" && (vm.variance.effectiveTo == null || vm.variance.effectiveTo == "")) {
                    var createdDate = moment(vm.variance.createdDate, $rootScope.applicationDateSelectFormat);
                    var effectiveFrom = moment(vm.variance.effectiveFrom, $rootScope.applicationDateSelectFormat);
                    var value = effectiveFrom.isSame(createdDate) || effectiveFrom.isAfter(createdDate);
                    if (!value) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveFromAfterCreatedDate);
                    }
                } else if (vm.variance.effectiveFrom != null && vm.variance.effectiveFrom != null && vm.variance.effectiveTo != "" && vm.variance.effectiveTo != "") {
                    var effectiveTo = moment(vm.variance.effectiveTo, $rootScope.applicationDateSelectFormat);
                    var effectiveFrom = moment(vm.variance.effectiveFrom, $rootScope.applicationDateSelectFormat);
                    var val = effectiveFrom.isSame(effectiveTo) || effectiveFrom.isBefore(effectiveTo);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveFromValidation);
                    }
                }

                return valid;
            }

            vm.validateEffectiveTo = validateEffectiveTo;
            function validateEffectiveTo() {
                var valid = true;
                if (vm.variance.effectiveTo != null && vm.variance.effectiveTo != "" && (vm.variance.effectiveFrom == null || vm.variance.effectiveFrom == "")) {
                    var createdDate = moment(vm.variance.createdDate, $rootScope.applicationDateSelectFormat);
                    var effectiveTo = moment(vm.variance.effectiveTo, $rootScope.applicationDateSelectFormat);
                    var value = effectiveTo.isSame(createdDate) || effectiveTo.isAfter(createdDate);
                    if (!value) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveToAfterCreatedDate);
                    }
                } else if (vm.variance.effectiveFrom != null && vm.variance.effectiveFrom != null && vm.variance.effectiveTo != "" && vm.variance.effectiveTo != "") {
                    var effectiveTo = moment(vm.variance.effectiveTo, $rootScope.applicationDateSelectFormat);
                    var effectiveFrom = moment(vm.variance.effectiveFrom, $rootScope.applicationDateSelectFormat);
                    var val = effectiveTo.isSame(effectiveFrom) || effectiveTo.isAfter(effectiveFrom);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveToValidation);
                    }
                }

                return valid;
            }

            function updateEffectiveFrom() {
                if (validateEffectiveFrom()) {
                    updateVariance();
                }
            }

            function updateEffectiveTo() {
                if (validateEffectiveTo()) {
                    updateVariance();
                }
            }


            $scope.effectiveFromPlaceholder = parsed.html($translate.instant("EFFECTIVE_FROM_PLACEHOLDER")).html();
            $scope.effectiveToPlaceholder = parsed.html($translate.instant("EFFECTIVE_TO_PLACEHOLDER")).html();
            var deviationUpdateMessage = parsed.html($translate.instant("DEVIATION_UPDATED")).html();
            var waiverUpdateMessage = parsed.html($translate.instant("WAIVER_UPDATED")).html();
            vm.updateVariance = updateVariance;
            function updateVariance() {
                if (validateVarience()) {
                    VarianceService.updateVariance(vm.varianceId, vm.variance).then(
                        function (data) {
                            $scope.title = data.title;
                            $scope.reasonForVariance = data.reasonForVariance;
                            $scope.description = data.description;
                            $scope.currentRequirement = data.currentRequirement;
                            $scope.requirementDeviation = data.requirementDeviation;
                            vm.editEffectiveTo = false;
                            vm.editEffectiveFrom = false;
                            vm.variance.description = data.description;
                            if ($rootScope.varianceType == 'Deviation') $rootScope.showSuccessMessage(deviationUpdateMessage);
                            if ($rootScope.varianceType == 'Waiver') $rootScope.showSuccessMessage(waiverUpdateMessage);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            var itemNameValidation = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var reasonForVarianceValidation = parsed.html($translate.instant("REASON_FOR_CHANGE_VALIDATION")).html();
            var descriptionValidation = $translate.instant("DESCRIPTION_NOT_EMPTY");
            var reqDeviationValidation = $translate.instant("REQUIREMENT_DEVIATION_VALIDATION");
            var currentReqValidation = $translate.instant("CURRENT_REQUIREMENT_VALIDATION");
            var reasonForChangeValidation = $translate.instant("REASONFOR_CHANGE_VALIDATION");

            function validateVarience() {
                var valid = true;
                if (vm.variance.title == null || vm.variance.title == ""
                    || vm.variance.title == undefined) {
                    valid = false;
                    vm.variance.title = $scope.title;
                    $rootScope.showWarningMessage(itemNameValidation);

                } else if (vm.variance.description == null || vm.variance.description == ""
                    || vm.variance.description == undefined) {
                    valid = false;
                    vm.variance.description = $scope.description;
                    $rootScope.showWarningMessage(descriptionValidation);

                }
                else if (vm.variance.reasonForVariance == null || vm.variance.reasonForVariance == ""
                    || vm.variance.reasonForVariance == undefined) {
                    valid = false;
                    vm.variance.reasonForVariance = $scope.reasonForVariance;
                    $rootScope.showWarningMessage(reasonForVarianceValidation);

                } else if (vm.variance.currentRequirement == null || vm.variance.currentRequirement == ""
                    || vm.variance.currentRequirement == undefined) {
                    valid = false;
                    vm.variance.currentRequirement = $scope.currentRequirement;
                    $rootScope.showWarningMessage(currentReqValidation);

                } else if (vm.variance.requirementDeviation == null || vm.variance.requirementDeviation == ""
                    || vm.variance.requirementDeviation == undefined) {
                    valid = false;
                    vm.variance.requirementDeviation = $scope.requirementDeviation;
                    $rootScope.showWarningMessage(reqDeviationValidation);

                }

                return valid;
            }


            (function () {
                //if ($application.homeLoaded == true) {
                loadBasicVariance();
                //}
                $scope.$on('app.variance.tabactivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        vm.varinceDetailsTabId = data.tabId;
                        loadBasicVariance();
                    }
                });
            })();
        }
    }
);