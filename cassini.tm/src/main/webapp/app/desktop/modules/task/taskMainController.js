/**
 * Created by Anusha on 07-07-2016.
 */
define(['app/desktop/modules/task/task.module'

    ],
    function (module) {
        module.controller('TaskMainController', TaskMainController);

        function TaskMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            $rootScope.viewInfo.icon = "fa fa-tasks";


            var vm = this;

            (function () {

            })();
        }
    }
);