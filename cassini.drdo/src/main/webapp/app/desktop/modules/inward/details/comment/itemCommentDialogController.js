define(
    [
        'app/desktop/modules/inward/inward.module',
        'app/shared/services/core/inwardService'
    ],
    function (module) {
        module.controller('ItemCommentDialogController', ItemCommentDialogController);

        function ItemCommentDialogController($scope, $stateParams, $rootScope, $timeout, $interval, $state, $cookies, InwardService) {

            var vm = this;
            vm.itemComment = null;

            function create() {
                if (validate()) {
                    $scope.callback(vm.itemComment);
                    $rootScope.hideSidePanel();
                }
            }

            function validate() {
                var valid = true;

                if (vm.itemComment == null || vm.itemComment == "" || vm.itemComment == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter comment");
                }

                return valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.inward.itemInstance.comment', create);
                }
            })();
        }
    }
);