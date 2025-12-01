define(
    [
        'app/desktop/modules/template/templateActivity/templateActivity.module',
        'app/shared/services/core/templateActivityService'
    ],
    function (module) {
        module.controller('TemplateActivityBasicInfoController', TemplateActivityBasicInfoController);

        function TemplateActivityBasicInfoController($scope, $stateParams, $rootScope, $timeout, $sce, $state, $translate,
                                                     CommonService, TemplateActivityService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            var activityId = $stateParams.activityId;
            vm.updateActivity = updateActivity;

            function loadTemplateActivity() {
                TemplateActivityService.getTemplateActivity(activityId).then(
                    function (data) {
                        vm.activity = data;
                        vm.activityName = vm.activity.name;
                        $rootScope.viewInfo.title = vm.activity.name;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var updateTemplateActivity = parsed.html($translate.instant("ACTIVITY_UPDATED_MESSAGE")).html();
            var activityNameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();

            function updateActivity(activity) {
                if (activity.name == null || activity.name == "") {
                    $rootScope.showWarningMessage("activityNameValidation");
                    vm.activity.name = vm.activityName;
                } else {
                    $rootScope.showBusyIndicator();
                    TemplateActivityService.updateTemplateActivity(activity).then(
                        function (data) {
                            vm.activity = data;
                            $rootScope.viewInfo.title = vm.activity.name;
                            $rootScope.showSuccessMessage(updateTemplateActivity);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            vm.activity.name = vm.activityName;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                loadTemplateActivity();
            })();
        }
    }
);