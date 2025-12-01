define(['app/desktop/modules/widgets/tasks/taskWidget.module',
        'app/desktop/modules/widgets/tasks/tasksWidgetController',
        'app/desktop/modules/widgets/persons/personsWidgetController',
        'app/desktop/modules/widgets/reports/reportsWidgetController',
        'app/desktop/modules/widgets/departments/departmentWidgetController',
        'app/desktop/modules/widgets/accommodation/accommodationWidgetController'

    ],
    function ($app) {
        $app.controller('HomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {
                    $scope.templates = {
                        tasksWidget: "app/desktop/modules/widgets/tasks/tasksWidget.jsp",
                        //delayTasksWidget: "app/desktop/modules/widgets/delaytasks/delayTasksWidget.jsp",
                        pendingTasksWidget: "app/desktop/modules/widgets/tasks/pendingTasksWidget.jsp",
                        completedTasksWidget: "app/desktop/modules/widgets/tasks/completedTasksWidget.jsp",
                        totalTasksWidget: "app/desktop/modules/widgets/tasks/totalTasksWidget.jsp",
                        personsWidget: "app/desktop/modules/widgets/persons/personsWidget.jsp",
                        reportsWidget: "app/desktop/modules/widgets/reports/reportsWidget.jsp",
                        departmentsWidget: "app/desktop/modules/widgets/departments/departmentWidget.jsp",
                        accommodationWidget: "app/desktop/modules/widgets/accommodation/accommodationWidget.jsp"
                    };

                    $rootScope.viewInfo.icon = "fa fa-home";
                    $rootScope.viewInfo.title = "Home";


                    $scope.$on('$viewContentLoaded', function(){
                        $timeout(function() {
                            $application.homeLoaded = true;
                            window.$("#preloader").hide();
                            window.$("#appview").show();

                        }, 1000);
                    });
                }
            ]
        );
    }
);