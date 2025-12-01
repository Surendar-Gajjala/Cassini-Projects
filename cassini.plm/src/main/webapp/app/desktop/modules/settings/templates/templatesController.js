define(
    [
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/desktop/modules/settings/templates/newScriptController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
    ],
    function (module) {
        module.controller('TemplatesController', TemplatesController);

        function TemplatesController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                     CommonService, $translate, ItemService) {

            var vm = this;
            vm.status = false;
            vm.loadPage = loadPage;
            $rootScope.pageName = null;

            function loadPage(el) {
                vm.status = true;
                $rootScope.pageName = el;
                $rootScope.hideBusyIndicator();
            }

            $('.dropdown-submenu .dropdown-submenu a').on("click", function (e) {
                $(this).next('ul').toggle();
                e.stopPropagation();
                e.preventDefault();
            });

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                });

                $timeout(function () {
                    loadPage('shareobjectmail.html');
                });
            })();
        }
    }
);