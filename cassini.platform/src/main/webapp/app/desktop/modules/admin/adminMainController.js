define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module'
    ],
    function (module) {
        module.controller('AdminMainController', AdminMainController);

        function AdminMainController($scope, $rootScope, $timeout, $state, $cookies,$translate) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-user";
            $rootScope.viewInfo.title = $translate.instant("ADMIN_TITLE");



            (function () {

            })();
        }
    }
);