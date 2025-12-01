/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective'
    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $window, compare, $state, $cookies, $uibModal,
                                CommonService) {

            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;



            $scope.$on('$viewContentLoaded', function () {
                $timeout(function () {
                    $application.homeLoaded = true;
                    window.$("#preloader").hide();
                    window.$("#appview").show();
                }, 1000);
            });


        }
    }
);