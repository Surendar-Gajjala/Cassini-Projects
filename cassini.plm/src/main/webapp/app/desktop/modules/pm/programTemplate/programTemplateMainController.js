define(
    [
        'app/desktop/modules/pm/pm.module'
    ],
    function (module) {
        module.controller('ProgramTemplateMainController', ProgramTemplateMainController);

        function ProgramTemplateMainController($scope, $rootScope, $state) {

            $rootScope.viewInfo.title = "Program Templates";

            (function () {

            })();
        }
    }
);