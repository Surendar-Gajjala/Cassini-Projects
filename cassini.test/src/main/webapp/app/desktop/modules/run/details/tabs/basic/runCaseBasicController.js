define(
    [
        'app/desktop/modules/run/run.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/core/testRunService',
        'app/desktop/modules/directive/testTreeDirective',
        'app/shared/services/runConfigurationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('RunCaseBasicInfoController', RunCaseBasicInfoController);

        function RunCaseBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, DialogService, TestRunService, RunConfigurationService,
                                            ObjectAttributeService, ObjectTypeAttributeService) {
            var vm = this;
            vm.loading = true;
            vm.changeAttribute = changeAttribute;
            vm.changeTimestamp = changeTimestamp;
            vm.cancelTime = cancelTime;
            vm.changeTime = changeTime;


            function cancelTime(attribute) {
                attribute.showTimeAttribute = false;
                attribute.showTimestamp = false;
            }

            function changeTimestamp(attribute) {
                attribute.showTimestamp = true;
            }


            function changeTime(attribute) {
                attribute.showTimeAttribute = true;
            }


            function changeAttribute(attribute) {
                $rootScope.stringValue = null;
                $rootScope.integerValue = null;
                $rootScope.dateValue = null;
                attribute.editMode = true;
                $rootScope.stringValue = attribute.value.stringValue;
                $rootScope.integerValue = attribute.value.integerValue;
                $rootScope.dateValue = attribute.value.dateValue;
                $rootScope.doubleValue = attribute.value.doubleValue;

            }


            (function () {
            })();
        }
    }
);