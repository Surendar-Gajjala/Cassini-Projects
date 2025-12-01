define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],

    function (module) {
        module.controller('NewSectionController', NewSectionController);

        function NewSectionController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, SpecificationsService) {


            var parse = angular.element("<div></div>");
            var item = $scope.data.specSection;

            var vm = this;
            vm.valid = true;
            var specId = $stateParams.specId;

            var specCreatedMsg = parse.html($translate.instant("SECTION_CREATE_MSG")).html();
            var nameValidation = parse.html($translate.instant("SPECIFICATION_NAME_VALIDATION")).html();
            var descriptionValidation = parse.html($translate.instant("DESCRIPTION_VALIDATION")).html();

            vm.newSection = {
                id: null,
                name: null,
                description: null,
                specification: null,
                parent: null
            };

            function createSection() {
                if (validate()) {
                    vm.newSection.specification = specId;
                    if (item != null) {
                        vm.newSection.parent = item.id;
                    }
                    SpecificationsService.createSection(vm.newSection).then(
                        function (data) {
                            $rootScope.showSuccessMessage(specCreatedMsg);
                            vm.newSection = {
                                id: null,
                                name: null,
                                description: null,
                                specification: null,
                                parent: null
                            };
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
                if (vm.newSection.name == null || vm.newSection.name == undefined ||
                    vm.newSection.name == "") {
                    $rootScope.showErrorMessage(nameValidation);
                    valid = false;
                }
                else if (vm.newSection.description == null || vm.newSection.description == undefined ||
                    vm.newSection.description == "") {
                    $rootScope.showErrorMessage(descriptionValidation);
                    valid = false;
                }

                return valid;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.spec.section.new', createSection);
                //}
            })();
        }
    }
)
;