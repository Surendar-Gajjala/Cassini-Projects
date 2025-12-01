define(['app/app.modules',
        'app/components/home/roles/admin/adminHomeController',
        'app/components/home/roles/salesrep/salesRepHomeController',
        'app/components/home/roles/orderentry/orderEntryHomeController',
        'app/components/home/roles/orderprocessing/orderProcessingHomeController',
        'app/components/home/roles/hr/hrManagerHomeController',
        'app/components/home/roles/shipping/shippingManagerHomeController',
        'app/components/home/roles/warehouse/warehouseManagerHomeController'
    ],
    function (app) {
        app.controller('HomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state',

                function ($scope, $rootScope, $timeout, $interval, $state) {
                    $rootScope.iconClass = "fa fa-home";
                    $rootScope.viewTitle = "Home";

                    $scope.quickActionVisible = true;

                    $scope.toggleQuickActions = function() {
                        if($scope.quickActionVisible == true) {
                            $(".quick-actions").parent().slideUp("slow");
                        }
                        else {
                            $(".quick-actions").parent().slideDown("slow");
                        }
                        $scope.quickActionVisible = !$scope.quickActionVisible;
                    };


                    $scope.$on('$viewContentLoaded', function () {
                        $('#appNotification').hide();
                        app.homeLoaded = true;
                        console.log("Home controller loaded");

                        var docHeight = $(window).height();
                        $('#workspace').height(docHeight-51);
                    });
                }
            ]
        );
    }
);