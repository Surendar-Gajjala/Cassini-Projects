define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programTemplateService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ProgramTemplateBasicInfoController', ProgramTemplateBasicInfoController);

        function ProgramTemplateBasicInfoController($scope, $rootScope, $stateParams, $translate, ProgramTemplateService, CommonService) {

            var vm = this;

            vm.templateId = $stateParams.templateId;
            var parsed = angular.element("<div></div>");

            var templateUpdatedMessage = parsed.html($translate.instant("TEMPLATE_UPDATE_MESSAGE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();

            vm.updateTemplate = updateTemplate;

            vm.templateName = null;
            vm.templateDescription = null;
            function loadTemplate() {
                ProgramTemplateService.getProgramTemplate(vm.templateId).then(
                    function (data) {
                        vm.template = data;
                        vm.templateName = vm.template.name;
                        $rootScope.programTemplate = data;
                        CommonService.getMultiplePersonReferences([vm.template], ['createdBy', 'modifiedBy']);
                        $rootScope.viewInfo.title = vm.template.name;
                        //$rootScope.viewInfo.description = vm.template.description;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function updateTemplate() {
                if (validate()) {
                    ProgramTemplateService.updateProgramTemplate(vm.template).then(
                        function (data) {
                            loadTemplate();
                            $rootScope.showSuccessMessage(templateUpdatedMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function validate() {
                var valid = true;

                if (vm.template.name == "" || vm.template.name == null) {
                    vm.template.name = vm.templateName;
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadTemplate();
                //}
            })();
        }
    }
)