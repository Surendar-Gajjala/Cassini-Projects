/**
 * Created by Nageshreddy on 08-11-2018.
 */
define(
    [
        'app/desktop/modules/users/users.module'
    ],
    function (module) {
        module.controller('UsersController', UsersController);

        function UsersController($scope, $rootScope, $timeout, $window, $state, $cookies) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-university";
            $rootScope.viewInfo.title = "Users";

            (function () {

            })();
        }
    }
);