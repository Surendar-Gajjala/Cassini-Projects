define(
    [
        'app/desktop/modules/exim/exim.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/eximService'
    ],
    function (module) {
        module.controller('ImportController', ImportController);

        function ImportController($scope, $rootScope, $timeout, $interval, $state, $cookies, $translate,
                                  ItemService, ExImService) {

            var vm = this;
            var file;
            $rootScope.viewInfo.icon = "fa fa-download";
            $rootScope.viewInfo.title = "Import";
            $rootScope.viewInfo.showDetails = false;
            $scope.importData = importData;

            var parsed = angular.element("<div></div>");
            var dataImported = parsed.html($translate.instant("DATA_IMPORTED")).html();
            var mappingColumns = parsed.html($translate.instant("MAPPING_COLUMNS")).html();
            var dataImporter = parsed.html($translate.instant("DATA_IMPORTER")).html();
            var importFileWithProperData = parsed.html($translate.instant("IMPORT_FILE_WITH_PROPER_DATA")).html();
            var importFileWithProperFormatMessage = parsed.html($translate.instant("PLEASE_IMPORT_PROPER_FILE_FOR_REF_SEE_HELP")).html();

            /*function importData() {
             var fileElem = document.getElementById("importFile");
             var file = fileElem.files[0];
             ExImService.importData(file).then(
             function (data) {
             if (data == true) {
             $rootScope.showSuccessMessage(dataImported);
             }
             },
             function (error) {
             $rootScope.showErrorMessage(error.message);
             }
             )
             }*/

            function importData() {
                var fileElem = document.getElementById("importFile");
                file = fileElem.files[0];
                ExImService.getHeadersFromFile(file).then(
                    function (data) {
                        vm.headers = data;
                        mapColumns();
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        var fileElem = document.getElementById("importFile").value = "";
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function startImport(objs) {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.importUploadedFile(file).then(
                    function (data) {
                        $rootScope.showSuccessMessage(dataImported);
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        var fileElem = document.getElementById("importFile").value = "";
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function mapColumns() {
                vm.objs = [];
                angular.forEach(vm.headers, function (header) {

                        switch (header) {
                            case "Type Path":
                                vm.objs.push("Item Type");
                                break;
                            case "Item Number":
                                vm.objs.push("Item");
                                break;
                            case "Level":
                                vm.objs.push("Bom");
                                break;
                            case "Manufacturer Type" :
                                vm.objs.push("Manufacturer Type");
                                break;
                            case "Manufacturer" :
                                vm.objs.push("Manufacturer");
                                break;
                            case "Manufacturer Part Type":
                                vm.objs.push("Manufacturer Part Type");
                                break;
                            case "Manufacturer Part Number":
                                vm.objs.push("Manufacturer Part");
                                break;
                            case "Manufacturer Part Item Status":
                                vm.objs.push("Manufacturer Part Item");
                                break;
                        }

                        if (!vm.objs.includes("Item") && header.includes("Item")) {
                            vm.objs.push("Item");
                        }

                        if (!vm.objs.includes("Item Type") && (header.includes("Item Type") || header.includes("ItemType"))) {
                            vm.objs.push("Item Type");
                        }
                    }
                );

                if (vm.headers.length > 2 && vm.objs.length > 1) {
                    var options = {
                        title: mappingColumns,
                        template: 'app/desktop/modules/exim/import/importColumnsMappingView.jsp',
                        controller: 'ImportColumnMapController as mapVm',
                        resolve: 'app/desktop/modules/exim/import/importColumnMapController',
                        width: 650,
                        data: {
                            headers: vm.headers
                        },
                        buttons: [
                            {text: "Import", broadcast: 'app.exim.mapping'}
                        ],
                        callback: function (objs) {
                            startImport(objs);
                        }
                    };
                    $rootScope.showSidePanel(options);
                } else {
                    $rootScope.showErrorMessage(importFileWithProperFormatMessage);
                }
            }


            (function () {

            })();
        }
    }
);