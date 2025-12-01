define(['app/app.modules',
        'app/components/prod/dashboard/widgets/machine/machineController',
        'app/components/prod/dashboard/widgets/processStep/processStepController',
        'app/components/prod/dashboard/widgets/workcenter/workcenterController',
        'app/components/prod/dashboard/widgets/process/processController',
        'app/components/prod/dashboard/widgets/workshiftemp/workShiftEmployeeController',
        'app/components/prod/dashboard/widgets/bom/bomController'
    ],
    function ($app) {
        $app.controller('ProductionDashboardController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa fa-tachometer";
                    $rootScope.viewTitle = "Production Dashboard";

                    $scope.templates = {
                        machine: "app/components/prod/dashboard/widgets/machine/machine.jsp",
                        workcenter: "app/components/prod/dashboard/widgets/workcenter/workcenter.jsp",
                        process: "app/components/prod/dashboard/widgets/process/process.jsp",
                        procesStep: "app/components/prod/dashboard/widgets/processStep/processStep.jsp",
                        workshiftEmp: "app/components/prod/dashboard/widgets/workshiftemp/empWorkShift.jsp",
                        bom: "app/components/prod/dashboard/widgets/bom/bomView.jsp"
                    };
                }
            ]
        );
    }
);