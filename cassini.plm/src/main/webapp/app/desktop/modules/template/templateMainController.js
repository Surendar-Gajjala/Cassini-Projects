define(
    [
        'app/desktop/modules/template/template.module'
    ],
    function (module) {
        module.controller('TemplateMainController', TemplateMainController);

        function TemplateMainController($scope, $rootScope, $state) {

            $rootScope.viewInfo.title = "Templates";

            (function () {

            })();
        }
    }
);