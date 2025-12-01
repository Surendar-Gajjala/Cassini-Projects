define(
    [
        'app/phone/modules/task/task.module'
    ],
    function (module) {
        module.controller('NoteDialogController', NoteDialogController);

        function NoteDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $mdDialog) {
            var vm = this;

            $scope.note = null;

            $scope.onCancel = function() {
                $mdDialog.cancel();
            };
            $scope.onOk = function() {
                $mdDialog.hide($scope.note);
            };


            (function () {

            })();
        }
    }
);