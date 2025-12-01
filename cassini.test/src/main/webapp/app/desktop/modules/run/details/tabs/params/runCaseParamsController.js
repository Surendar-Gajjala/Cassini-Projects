define(
    [
        'app/desktop/modules/run/run.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('RunCaseParamsController', RunCaseParamsController);

        function RunCaseParamsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, DialogService) {
            var vm = this;

            vm.loading = true;

            (function () {

            })();
        }
    }
);
