define(
    [
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/system/systemsettingsController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/application/applicationsettingsController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('PreferencesController', PreferencesController);

        function PreferencesController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                       CommonService, $translate) {

            var vm = this;
            vm.status = false;
            vm.loadPage = loadPage;
            vm.onLoad = onLoad;
            vm.pageName = null;
            $rootScope.isAdminGroup = false;

            function onLoad() {
                $rootScope.hideBusyIndicator();
            }

            function loadPage(el) {
                vm.status = true;
                vm.pageName = el;
            }

            vm.active = 0;
            vm.tabs = {
                system: {
                    id: 'details.basic',
                    heading: "System",
                    template: 'app/assets/bower_components/cassini-platform/app/desktop/modules/common/system/systemsettingsView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                application: {
                    id: 'details.attributes',
                    heading: 'Application',
                    template: 'app/assets/bower_components/cassini-platform/app/desktop/modules/common/application/applicationsettingsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                }
            };

            $('.dropdown-submenu .dropdown-submenu a').on("click", function (e) {
                $(this).next('ul').toggle();
                e.stopPropagation();
                e.preventDefault();
            });

            function resize() {
                var height1 = $("#settingsPane").outerHeight();
                $('.tab-content').height(height1 - 120);
                $('.tab-pane').height(height1 - 120);
                $rootScope.hideBusyIndicator();
            }

            (function () {
                $(window).resize(function () {
                    resize()
                });
                $timeout(function () {
                    resize();
                }, 300);
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                });
                if ($rootScope.loginPersonDetails.isSuperUser) {
                    $rootScope.isAdminGroup = true;
                }
            })();
        }
    }
);