define(
    [
        'app/desktop/modules/exim/exim.module',
        'app/shared/services/core/eximService'
    ],
    function (module) {
        module.controller('ExportController', ExportController);

        function ExportController($scope, $rootScope, $timeout, $interval, $state, $cookies, $http, $sce, $translate,
                                  FileSaver, Blob, ExImService) {

            $rootScope.viewInfo.icon = "fa fa-upload";
            $rootScope.viewInfo.title = "Export";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;

            vm.exportData = exportData;
            vm.onExportFinish = onExportFinish;

            var parsed = angular.element("<div></div>");
            var selectAtleastOneObject = parsed.html($translate.instant("SELECT_ATLEAST_ONE_OBJECT")).html();

            vm.exportUrl = "";

            vm.options = [
                {key: "lovs", name: "List of Values", description: "Export all list of values", selected: false},
                {key: "lifecycles", name: "Lifecycles", description: "Export all lifecycles", selected: false},
                {
                    key: "autonumbers",
                    name: "Autonumber Sources",
                    description: "Export all autonumber sources",
                    selected: false
                },
                {
                    key: "classification",
                    name: "Classification",
                    description: "Export entire classification tree along with attributes",
                    selected: false
                },
                {key: "relationships", name: "Relationships", description: "Export all relationships", selected: false},
                {key: "workflows", name: "Workflows", description: "Export all workflows", selected: false},
                {
                    key: "groups",
                    name: "Groups/Permissions",
                    description: "Export all groups & their permissions",
                    selected: false
                }
            ];

            function onExportFinish() {

            }

            function exportData() {
                $rootScope.showBusyIndicator("#export-view");
                var objects = "";
                for (var i = 0; i < vm.options.length; i++) {
                    var o = vm.options[i];
                    if (o.selected) {
                        if (objects !== "") {
                            objects += ",";
                        }
                        objects += o.key;
                    }
                }

                if (objects === "") {
                    $rootScope.hideBusyIndicator();
                    $rootScope.showWarningMessage(selectAtleastOneObject);
                }
                else {
                    ExImService.exportData(objects).then(
                        function (data) {
                            vm.exportUrl = "/api/plm/exim/export/getfile?timestamp=" + (new Date()).getTime();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
            }

            (function () {

            })();
        }
    }
);