define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('SpecificationBasicInfoController', SpecificationBasicInfoController);

        function SpecificationBasicInfoController($scope, $window, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                                  CommonService, SpecificationsService) {

            var vm = this;
            var specId = $stateParams.specId;

            vm.updateSpecification = updateSpecification;

            $rootScope.demoteSpecification = demoteSpecification;
            $rootScope.promoteSpecification = promoteSpecification;
            $rootScope.reviseSpecification = reviseSpecification;

            /*----------- Promote And Demote Requirement ------*/
            var parsed = angular.element("<div></div>");
            var statusUpdateMsg = parsed.html($translate.instant("STATUS_UPDATE_MSG")).html();
            var newRevisionCreateMsg = parsed.html($translate.instant("NEW_TERMINOLOGY_MSG")).html();
            var specificationUpdateMsg = parsed.html($translate.instant("SPECIFICATION_UPDATE_MSG")).html();
            var NameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();

            function promoteSpecification() {
                SpecificationsService.promoteRequirement(specId).then(
                    function (data) {
                        $rootScope.loadSpecification();
                        $rootScope.showSuccessMessage(statusUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function demoteSpecification() {
                SpecificationsService.demoteRequirement(specId).then(
                    function (data) {
                        $rootScope.loadSpecification();
                        $rootScope.showSuccessMessage(statusUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function reviseSpecification() {
                SpecificationsService.reviseRequirement(specId).then(
                    function (data) {
                        $rootScope.selectedSpecification = data;
                        $window.localStorage.setItem("requirements", "");
                        $rootScope.showSuccessMessage(newRevisionCreateMsg + " : " + $rootScope.selectedSpecification.version);
                        $state.go('app.rm.specifications.details', {specId: $rootScope.selectedSpecification.id})
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function updateSpecification() {
                if (validate()) {
                    SpecificationsService.updateSpecification($rootScope.selectedSpecification).then(
                        function (data) {
                            $rootScope.loadSpecification();
                            $rootScope.showSuccessMessage(specificationUpdateMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

            }

            function validate() {
                var valid = true;
                if ($rootScope.selectedSpecification.name == null || $rootScope.selectedSpecification.name == ""
                    || $rootScope.selectedSpecification.name == undefined) {
                    valid = false;
                    $rootScope.selectedSpecification.name = $rootScope.specName;
                    $rootScope.showWarningMessage(NameValidation);

                }
                return valid;
            }

            (function () {

            })();
        }
    }
);