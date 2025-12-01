define(
    [
        'app/desktop/modules/vaults/vault.module',
        'app/shared/services/vaultsDetailsService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'


    ],
    function (module) {
        module.controller('NewCommitController', NewCommitController);

        function NewCommitController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $uibModal,
                                     VaultsDetailsService, DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.files = $scope.data.selectedCommitFiles;

            vm.fileSizeToString = fileSizeToString;
            vm.newCommit = {
                id: null,
                comments: null,
                sha: null

            };

            function validate() {
                var valid = true;

                if (vm.newCommit.comments == null || vm.newCommit.comments == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Please enter comment');
                }

                return valid;
            }

            function createCommit() {
                if (validate()) {
                    VaultsDetailsService.createCommit(vm.newCommit).then(
                        function (data) {
                            $scope.callback(data);
                        }, function (error) {

                        }
                    )

                }


            }

            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }


            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.commit.new', createCommit);
                }
            })();
        }
    }
)
;