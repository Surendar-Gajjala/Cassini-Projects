define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('InspectionFilesController', InspectionFilesController);

        function InspectionFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application,
                                           CommonService, ItemService, DialogService, InspectionService, ObjectFileService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.files = [];
            vm.showDropzone = false;
            vm.showFileDropzone = false;
            vm.inspectionId = $stateParams.inspectionId;

            (function () {
                $scope.$on('app.inspection.tabActivated', function (event, data) {

                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                        }, 500);
                    }
                });
            })();
        }
    }
)
;