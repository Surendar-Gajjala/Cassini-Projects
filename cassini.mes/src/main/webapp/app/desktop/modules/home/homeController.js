define(['app/desktop/modules/home/home.module'
    ],

    function(module){
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $state, $cookies){


            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;

            $scope.$on('$viewContentLoaded', function(){
                $timeout(function() {
                    $application.homeLoaded = true;
                    window.$("#preloader").hide();
                    window.$("#appview").show();

                    //loadPendingPurchaseOrders();
                    //loadPendingRequisitions();
                    //loadReorderItems();

                }, 1000);
            });


        }


    }


);