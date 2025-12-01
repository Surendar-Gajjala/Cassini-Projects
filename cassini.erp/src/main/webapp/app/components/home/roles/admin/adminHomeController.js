define(['app/app.modules',
        'app/components/home/widgets/orders/ordersWidgetController',
        'app/components/home/widgets/returns/returnsWidgetController',
        'app/components/home/widgets/salesreps/fieldReportsWidgetController',
        'app/components/home/widgets/inventory/lowInventoryWidgetController',
        'app/components/home/widgets/reports/dailySalesReportController',
        'app/components/home/widgets/reports/productVsSampleSalesController',
        'app/components/home/widgets/reports/bookSalesReportController',
        'app/components/reporting/directives/reportWidget'
    ],
    function ($app) {
        $app.controller('AdminHomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {
                    $scope.templates = {
                        ordersWidget: "app/components/home/widgets/orders/ordersWidget.jsp",
                        returnsWidget:"app/components/home/widgets/returns/returnsWidget.jsp",
                        customerReports: "app/components/home/widgets/salesreps/fieldReportsWidget.jsp",
                        lowInventoryWidget:"app/components/home/widgets/inventory/lowInventoryWidget.jsp"
                    };

                }
            ]
        );
    }
);