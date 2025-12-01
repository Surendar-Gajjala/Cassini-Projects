define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/machineService',
        'app/shared/services/core/equipmentService',
        'app/shared/services/core/toolService',
        'app/shared/services/core/instrumentService',
        'app/shared/services/core/materialService',
        'app/desktop/modules/directives/mesObjectTypeDirective'

    ],
    function (module) {
        module.controller('SelectResourceController', SelectResourceController);

        function SelectResourceController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                          MachineService, EquipmentService, ToolService, InstrumentService, MaterialService) {
            var vm = this;
            vm.objectType = 'MACHINETYPE';
            vm.loading = true;
            vm.onOk = onOk;
            vm.selectedResources = [];
            var workOrderId = $scope.data.selectedWorkOrderId;
            var mode = $scope.data.mode;
            vm.item = null;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchResources = searchResources;
            vm.checkAll = checkAll;
            vm.select = select;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $scope.check = false;
            var parsed = angular.element("<div></div>");
            vm.partNumber = parsed.html($translate.instant("NUMBER")).html();
            vm.partName = parsed.html($translate.instant("NAME")).html();
            var SelectAtleastOnePart = parsed.html($translate.instant("SELECT_WORKORDER_RESOURCE")).html();
            var PleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();
            var machineLabel = parsed.html($translate.instant("MACHINE_LABEL")).html();
            var equipmentLabel = parsed.html($translate.instant("EQUIPMENT_LABEL")).html();
            var instrumentLabel = parsed.html($translate.instant("INSTRUMENT_LABEL")).html();
            var toolLabel = parsed.html($translate.instant("TOOL_LABEL")).html();
            var materialLabel = parsed.html($translate.instant("MATERIAL_LABEL")).html();
            vm.selectedLabel = parsed.html($translate.instant("MACHINE")).html();
            vm.replacementType = false;
            vm.objectTypes = [{lable: parsed.html($translate.instant("MACHINE")).html(), objectType: 'MACHINETYPE'},
                {lable: parsed.html($translate.instant("EQUIPMENT")).html(), objectType: 'EQUIPMENTTYPE'},
                {lable: parsed.html($translate.instant("INSTRUMENT")).html(), objectType: 'INSTRUMENTTYPE'},
                {lable: parsed.html($translate.instant("TOOL")).html(), objectType: 'TOOLTYPE'},
                {lable:parsed.html($translate.instant("MATERIAL")).html(), objectType: 'MATERIALTYPE'}];

            vm.filters = {
                number: null,
                name: null,
                type: '',
                description: null,
                searchQuery: null,
                typeName: null,
                workOrder: ''
            };

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.resources = angular.copy(pagedResults);

            function loadMachines() {
                vm.loading = true;
                MachineService.getAllMachines(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.resources = data;
                        angular.forEach(vm.resources.content, function (part) {
                            part.checked = false;
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            function loadEquipments() {
                vm.loading = true;
                EquipmentService.getAllEquipments(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.resources = data;
                        angular.forEach(vm.resources.content, function (part) {
                            part.checked = false;
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            function loadInstruments() {
                vm.loading = true;
                InstrumentService.getAllInstruments(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.resources = data;
                        angular.forEach(vm.resources.content, function (part) {
                            part.checked = false;
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }


            function loadTools() {
                vm.loading = true;
                ToolService.getAllTools(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.resources = data;
                        angular.forEach(vm.resources.content, function (part) {
                            part.checked = false;
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }


            function loadMaterials() {
                vm.loading = true;
                MaterialService.getAllMaterials(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.resources = data;
                        angular.forEach(vm.resources.content, function (part) {
                            part.checked = false;
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            function loadResources() {
                vm.resources = [];
                if (vm.objectType != null && vm.objectType != '') {
                    if (vm.objectType == 'MACHINETYPE') {
                        loadMachines();
                    }
                    else if (vm.objectType == 'EQUIPMENTTYPE') {
                        loadEquipments();
                    }
                    else if (vm.objectType == 'INSTRUMENTTYPE') {
                        loadInstruments();
                    }
                    else if (vm.objectType == 'TOOLTYPE') {
                        loadTools();
                    }
                    else if (vm.objectType == 'MATERIALTYPE') {
                        loadMaterials();
                    }
                }

            }


            $scope.onSelected = function (selectedItem) {
                vm.filters = {
                    number: null,
                    name: null,
                    type: '',
                    description: null,
                    searchQuery: null,
                    typeName: null,
                    workOrder: ''
                };
                vm.pageable.page = 0;
                vm.type = null;
                vm.clear = true;
                vm.objectType = selectedItem.objectType;
                setReferenceIds();
            }

            function onOk() {
                if (vm.selectedResources.length == 0) {
                    $rootScope.showWarningMessage(SelectAtleastOnePart)
                } else {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedResources);
                }
            }

            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedResources = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.resources.content, function (resource) {
                        resource.selected = vm.selectAllCheck;
                        vm.selectedResources.push(resource);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.resources.content, function (resource) {
                        resource.selected = vm.selectAllCheck;
                        vm.selectedResources = [];
                    });
                }
            }

            vm.selectAllCheck = false;
            function select(resource) {
                var flag = true;
                if (resource.selected == false) {
                    vm.selectAllCheck = false;
                    var index = vm.selectedResources.indexOf(resource);
                    vm.selectedResources.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedResources, function (selectedItem) {
                        if (selectedItem.id == resource.id) {
                            flag = false;
                            var index = vm.selectedResources.indexOf(resource);
                            vm.selectedResources.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedResources.push(resource);
                    }

                    if (vm.selectedResources.length == vm.resources.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedResources.length != vm.resources.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            function onSelectType(type) {
                if (type != null && type != undefined) {
                    vm.type = type;
                    vm.filters.typeName = type.name;
                    vm.filters.type = type.id;
                    searchResources();
                }
            }

            function searchResources() {
                if (vm.filters.name == null && vm.filters.type == "" && vm.filters.number == null) {
                    $rootScope.showWarningMessage(PleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    vm.clear = true;
                    setReferenceIds();
                }
            }

            function clearFilter() {
                vm.filters = {
                    number: null,
                    name: null,
                    type: '',
                    description: null,
                    searchQuery: null,
                    typeName: null,
                    workOrder: ''
                };
                vm.objectType = 'MACHINETYPE';
                vm.selectedLabel = "Machine";
                vm.type = null;
                vm.clear = false;
                setReferenceIds();
            }

            function nextPage() {
                if (!vm.resources.last) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedResources = [];
                    setReferenceIds();
                }
            }

            function previousPage() {
                if (!vm.resources.first) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedResources = [];
                    setReferenceIds();
                }
            }

            function setReferenceIds() {
                vm.selectedResources = [];
                if ($scope.data.mode == "WORKORDER") {
                    vm.filters.workOrder = $scope.data.selectedWorkOrderId;
                    loadResources();
                }
            }

            (function () {
                setReferenceIds();
                $rootScope.$on('app.workorder.resources.add', onOk);
            })();
        }
    }
)
;