/**
 * Created by Nageshreddy on 23-08-2020.
 */
define(
    [
        'app/desktop/modules/exim/exim.module',
        'app/shared/services/core/eximService'
    ],
    function (module) {
        module.controller('ImportColumnMapController', ImportColumnMapController);

        function ImportColumnMapController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, ExImService) {

            var parsed = angular.element("<div></div>");
            var vm = this;

            vm.headers = $scope.data.headers;
            vm.headerObjs = [];
            vm.mapHeaders = [];
            vm.objs = [];
            vm.headers1 = [];

            function loadValues() {

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

                setObjectColumns();
            }

            function setObjectColumns() {
                angular.forEach(vm.objs, function (obj) {
                        switch (obj) {
                            case "Item Type":
                                vm.mapHeaders.pushArray(["Type Class", "Type Path", "Type Description"]);
                                break;
                            case "Item":
                                vm.mapHeaders.pushArray(["Item Number", "Item Name", "Item Description", "Revision", "Item LifeCycle", "Units", "Make/Buy"]);
                                break;
                            case "Bom":
                                vm.mapHeaders.pushArray(["Level", "Quantity", "RefDes", "Bom Notes"]);
                                break;
                            case "Manufacturer Type" :
                                vm.mapHeaders.push("Manufacturer Type");
                                break;
                            case "Manufacturer" :
                                vm.mapHeaders.pushArray(["Manufacturer", "Manufacturer Description", "Manufacturer LifeCycle", "Manufacturer PhoneNumber", "Manufacturer Contact Person"]);
                                break;
                            case "Manufacturer Part Type":
                                vm.mapHeaders.pushArray(["Manufacturer Part Type", "Manufacturer Part Type Desc"]);
                                break;
                            case "Manufacturer Part":
                                vm.mapHeaders.pushArray(["Manufacturer Part Number", "Manufacturer Part Name", "Manufacturer Part Description", "Manufacturer Part Status", "Manufacturer Part LifeCycle"]);
                                break;
                            case "Manufacturer Part Item":
                                vm.mapHeaders.push("Manufacturer Part Item Status");
                                break;
                        }
                    }
                );
                vm.headers1 = [];
                setMappingColumns();
            }


            function setMappingColumns() {
                angular.copy(vm.headers, vm.headers1);
                angular.forEach(vm.mapHeaders, function (obj) {
                    var ob = {
                        header: obj,
                        mapHeader: ""
                    };
                    vm.headerObjs.push(ob);
                });

                angular.forEach(vm.headerObjs, function (obj) {
                    if (vm.headers.includes(obj.header)) {
                        obj.mapHeader = obj.header;
                        removeFromHeader(obj.header);
                    }
                });
                vm.headers1.push("Do not map this field");
            }

            Array.prototype.pushArray = function (arr) {
                this.push.apply(this, arr);
            };

            function removeFromHeader(valu) {
                var index = vm.headers1.indexOf(valu);
                if (index !== -1) {
                    vm.headers1.splice(index, 1);
                }
            }

            function doMapping() {
                var headMap = {};
                angular.forEach(vm.headerObjs, function (obje) {
                    if (obje.mapHeader != "") {
                        headMap[obje.mapHeader] = obje.header;
                    }
                });

                ExImService.sendMappingHeaders(headMap).then(
                    function (data) {
                        $scope.callback();
                        $rootScope.hideSidePanel();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                vm.headers = $scope.data.headers;
                loadValues();
                $rootScope.$on('app.exim.mapping', doMapping);
            })();
        }
    }
)
;