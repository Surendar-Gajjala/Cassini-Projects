define(['app/app.modules',
        'app/components/crm/consignment/consignmentFactory',
        'app/components/crm/consignment/new/newConsignmentController',
        'app/components/crm/consignment/pendingShipmentsController',
        'app/components/crm/consignment/shippedShipmentsController',
        'app/components/home/roles/orderprocessing/orderProcessingHomeController',
        'app/shared/directives/tableDirectives',
        'app/components/crm/order/orderFactory'
    ],
    function ($app) {
        $app.controller('ShippingHomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$location', '$state', '$stateParams', '$cookies',
                'consignmentFactory', 'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $location, $state, $stateParams, $cookies,
                          consignmentFactory, orderFactory) {

                    $rootScope.iconClass = "fa fa-truck";
                    $rootScope.viewTitle = "Shipping";

                }
            ]
        );
    }
);