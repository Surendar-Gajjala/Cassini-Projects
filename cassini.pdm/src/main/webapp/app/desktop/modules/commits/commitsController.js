define(
    [
        'app/desktop/modules/commits/commits.module',
        'app/shared/services/vaultsDetailsService'

    ],
    function (module) {
        module.controller('CommitsController', CommitsController);

        function CommitsController($scope, $rootScope, $timeout, $interval, $state, $cookies, $uibModal, VaultsDetailsService,
                                   CommonService) {


            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Commits";
            $rootScope.viewInfo.showDetails = true;

            var vm = this;
            vm.selectedCommit = null;
            vm.downloadFile = downloadFile;

            vm.selectCommits = selectCommits;
            vm.fileSizeToString = fileSizeToString;

            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            function loadCommits() {
                VaultsDetailsService.getAllCommits().then(
                    function (data) {
                        vm.commits = data;
                        CommonService.getPersonReferences(vm.commits, 'createdBy');
                    })
            }

            function selectCommits(commit) {
                vm.selectedCommit = commit;
                VaultsDetailsService.getCommits(commit.id).then(
                    function (data) {
                        vm.commitedFiles = data;
                        CommonService.getPersonReferences(vm.commitedFiles, 'createdBy');
                    }
                );

            }

            function downloadFile(commit) {
                var url = "{0}//{1}/api/pdm/vaults/folders/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    commit.folder, commit.id);
                launchUrl(url);
            }


            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadCommits();
                });
            })();
        }
    }
)
;