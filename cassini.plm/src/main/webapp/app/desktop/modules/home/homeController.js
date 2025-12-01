define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/desktop/modules/home/widgets/recentlyVisited/recentlyVisitedWidgetController',
        'app/desktop/modules/home/widgets/savedSearches/savedSearchesWidgetController',
        'app/desktop/modules/home/widgets/conversations/conversationsWidget',
        'app/desktop/modules/home/widgets/myTasks/myTasksWidgetController',
        'app/desktop/modules/home/dashboard/homeDashboardController',
        'app/desktop/modules/home/internal/internalUserController',
        'app/desktop/modules/home/external/externalUserController'
    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate, $application) {

            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = $translate.instant("HOME_TITLE");
            $rootScope.viewInfo.showDetails = true;
            $rootScope.fromMyTaskWidget = null;
            var vm = this;

            (function () {

            })();

        }
    }
);