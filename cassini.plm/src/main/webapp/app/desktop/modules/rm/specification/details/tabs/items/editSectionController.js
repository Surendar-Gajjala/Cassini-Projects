define(['app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('EditSectionController', EditSectionController);

        function EditSectionController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $translate, $cookies, SpecificationsService) {
            var vm = this;

            var parsed = angular.element("<div></div>");
            var nameValidation = parsed.html($translate.instant("SPECIFICATION_NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();

            function create() {
                if (validate()) {
                    SpecificationsService.updateSection(vm.sectionDetails).then(
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

            function validate() {
                var valid = true;
                if (vm.sectionDetails.name == null || vm.sectionDetails.name == undefined || vm.sectionDetails.name == "") {
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                } else if (vm.sectionDetails.description == null || vm.sectionDetails.description == undefined || vm.sectionDetails.description == "") {
                    $rootScope.showWarningMessage(descriptionValidation);
                    valid = false;
                }

                return valid;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    vm.sectionDetails = $scope.data.sectionDetails;
                    vm.sectionDetails = {
                        id: vm.sectionDetails.id,
                        name: vm.sectionDetails.name,
                        description: vm.sectionDetails.description

                    };
                    $rootScope.$on('app.section.edit', create);
                //}
            })();
        }
    }
);

