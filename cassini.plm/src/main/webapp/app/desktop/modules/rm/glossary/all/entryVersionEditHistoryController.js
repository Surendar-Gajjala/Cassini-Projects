define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('EntryVersionEditHistoryController', EntryVersionEditHistoryController);

        function EntryVersionEditHistoryController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies,
                                                   CommonService, GlossaryService) {
            var vm = this;
            vm.entry = $scope.data.glossaryEntryDetails;
            vm.selectedLanguage = $scope.data.language;

            var parsed = angular.element("<div></div>");

            function loadEntryEditHistory() {
                GlossaryService.getEntryEditHistory(vm.entry.id, vm.selectedLanguage).then(
                    function (data) {
                        vm.entryName = vm.entry.defaultDetail.name;
                        vm.entryEditHistories = data;
                    }
                )
            }

            (function () {
                loadEntryEditHistory();

            })();
        }
    }
);