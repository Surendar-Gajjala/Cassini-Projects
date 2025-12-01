define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService'
    ],
    function (module) {
        module.controller('GlossaryRevisionHistoryController', GlossaryRevisionHistoryController);

        function GlossaryRevisionHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                                   GlossaryService) {
            var vm = this;

            vm.data = $scope.data;
            vm.itemRevisionHisotry = [];

            vm.showGlossaryRevision = showGlossaryRevision;

            function loadGlossaryRevisionHistory() {
                GlossaryService.getGlossaryRevisionHistory(vm.data.glossaryId).then(
                    function (data) {
                        vm.glossaryRevisionHistory = data;
                    }
                )
            }

            function showGlossaryRevision(glossary) {
                $state.go('app.rm.glossary.details', {glossaryId: glossary.id})
            }

            (function () {
                loadGlossaryRevisionHistory();
            })();
        }
    }
);