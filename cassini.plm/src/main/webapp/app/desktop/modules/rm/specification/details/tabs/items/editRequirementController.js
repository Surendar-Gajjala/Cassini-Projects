define(['app/desktop/modules/rm/rm.module',
        'app/shared/services/core/requirementsTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('EditRequirementController', EditRequirementController);

        function EditRequirementController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $translate, $cookies, RequirementsTypeService) {
            var vm = this;

            var parse = angular.element("<div></div>");

            function create() {
                if (validation()) {
                    RequirementsTypeService.updateRequirementEdit(vm.requirementDetails).then(
                        function (data) {
                            $rootScope.hideSidePanel();
                            $scope.callback(data);
                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    )
                }
            }

            var descriptionValidation = parse.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var nameValidation = parse.html($translate.instant("SPECIFICATION_NAME_VALIDATION")).html();

            function validation() {
                var valid = true;
                if (vm.requirementDetails.name == null || vm.requirementDetails.name == "") {
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                } else if (vm.requirementDetails.description == null || vm.requirementDetails.description == "") {
                    $rootScope.showWarningMessage(descriptionValidation);
                    valid = false;
                }
                return valid;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    vm.requirementDetails = $scope.data.requirementDetails;
                    vm.requirementDetails = {
                        id: vm.requirementDetails.id,
                        name: vm.requirementDetails.name,
                        description: vm.requirementDetails.description,
                        notes: ""

                    };
                    $rootScope.$on('app.requirement.edit', create);
                //}
            })();
        }
    }
);

