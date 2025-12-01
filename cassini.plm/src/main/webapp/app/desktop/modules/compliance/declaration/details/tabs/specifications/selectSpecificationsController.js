define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/specificationService',
        'app/desktop/modules/directives/pgcObjectTypeDirective'
    ],
    function (module) {
        module.controller('SelectSpecificationsController', SelectSpecificationsController);

        function SelectSpecificationsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                                SpecificationService) {
            var vm = this;

            vm.loading = true;
            vm.onOk = onOk;
            vm.selectedSpecifications = [];
            var declaration = $scope.data.selectedDeclarationId;

            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchSpecifications = searchSpecifications;
            vm.checkAll = checkAll;
            vm.select = select;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $scope.check = false;
            var parsed = angular.element("<div></div>");
            vm.partNumber = parsed.html($translate.instant("PART_NUMBER")).html();
            vm.partName = parsed.html($translate.instant("PART_NAME")).html();
            var SelectAtleastOnePart = parsed.html($translate.instant("SELECT_SPECIFICATION")).html();
            var PleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();
            $scope.select = parsed.html($translate.instant("SELECT_TYPE")).html();
            vm.replacementType = false;

            vm.filters = {
                number: null,
                name: null,
                type: '',
                description: null,
                searchQuery: null,
                typeName: null,
                declaration: '',
                item: ''
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
            vm.specifications = angular.copy(pagedResults);
            function loadSpecifications() {
                SpecificationService.getAllSpecifications(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.specifications = data;
                        angular.forEach(vm.specifications.content, function (substance) {
                            substance.checked = false;
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function onOk() {
                if (vm.selectedSpecifications.length == 0) {
                    $rootScope.showWarningMessage(SelectAtleastOnePart)
                } else {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedSpecifications);
                }
            }

            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedSpecifications = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.specifications.content, function (sparePart) {
                        sparePart.selected = vm.selectAllCheck;
                        vm.selectedSpecifications.push(sparePart);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.specifications.content, function (sparePart) {
                        sparePart.selected = vm.selectAllCheck;
                        vm.selectedSpecifications = [];
                    });
                }
            }

            vm.selectAllCheck = false;
            function select(sparePart) {
                var flag = true;
                if (sparePart.selected == false) {
                    vm.selectAllCheck = false;
                    var index = vm.selectedSpecifications.indexOf(sparePart);
                    vm.selectedSpecifications.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedSpecifications, function (selectedItem) {
                        if (selectedItem.id == sparePart.id) {
                            flag = false;
                            var index = vm.selectedSpecifications.indexOf(sparePart);
                            vm.selectedSpecifications.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedSpecifications.push(sparePart);
                    }

                    if (vm.selectedSpecifications.length == vm.specifications.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedSpecifications.length != vm.specifications.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            function onSelectType(partType) {
                if (partType != null && partType != undefined) {
                    vm.partType = partType;
                    vm.filters.typeName = partType.name;
                    vm.filters.type = partType.id;
                    searchSpecifications();
                }
            }

            function searchSpecifications() {
                if (vm.filters.number == null && vm.filters.type == "" && vm.filters.name == null) {
                    $rootScope.showWarningMessage(PleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    vm.clear = true;
                    SpecificationService.getAllSpecifications(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.specifications = data;
                            angular.forEach(vm.specifications.content, function (substance) {
                                substance.checked = false;
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        })
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
                    declaration: '',
                    item: ''

                };
                vm.partType = null;
                vm.clear = false;
                setReferenceIds();
            }

            function nextPage() {
                if (!vm.specifications.last) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedSpecifications = [];
                    setReferenceIds();
                }
            }

            function previousPage() {
                if (!vm.specifications.first) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedSpecifications = [];
                    setReferenceIds();
                }
            }

            function setReferenceIds() {
                if ($scope.data.mode == "ITEM") {
                    vm.filters.item = $scope.data.selectedItemId;
                    loadSpecifications();
                }
                else if ($scope.data.mode == "DECLARATION") {
                    vm.filters.declaration = $scope.data.selectedDeclarationId;
                    loadSpecifications();
                }
            }


            (function () {
                setReferenceIds();
                $rootScope.$on('app.declaration.specifications.add', onOk);
            })();
        }
    }
);