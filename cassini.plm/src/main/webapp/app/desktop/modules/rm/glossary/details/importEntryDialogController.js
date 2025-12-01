define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService'
    ],
    function (module) {
        module.controller('ImportEntryDialogController', ImportEntryDialogController);

        function ImportEntryDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                             GlossaryService) {
            var vm = this;

            var glossaryId = $stateParams.glossaryId;

            vm.selectedFile = null;
            vm.importComment = "";

            var parsed = angular.element("<div></div>");
            var pleaseUploadFile = parsed.html($translate.instant("PLEASE_UPLOAD_ENTRY_FILE")).html();
            vm.selectFileTitle = parsed.html($translate.instant("SELECT_FILE")).html();

            function importEntry() {
                if (vm.selectedFile == null) {
                    $rootScope.showWarningMessage(pleaseUploadFile);
                } else {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    GlossaryService.importGlossaryEntryItems(glossaryId, vm.importComment, vm.selectedFile).then(
                        function (data) {
                            if (data.ignoredRows.length > 0) {
                                $rootScope.showWarningMessage(data.executedRows + " : Imported and " + data.ignoredRowsMessage + ": Rows ignored.")
                            } else {
                                $rootScope.showSuccessMessage(data.executedRows + " : Entries imported successfully");
                            }
                            vm.selectedFile = null;
                            vm.importComment = "";
                            $scope.callback(data);
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showWarningMessage(error.message);
                        }
                    )
                }
            }

            (function () {
                $scope.$on('app.glossary.importGlossaryEntry', importEntry);
            })();
        }
    }
);