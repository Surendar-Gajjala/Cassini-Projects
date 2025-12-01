define(['app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mfrPartsService'
    ],
    function (module) {
        module.controller('MfrPartSelectionController', MfrPartSelectionController);

        function MfrPartSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                            $uibModal, MfrPartsService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            $scope.freeTextQuery = null;
            var objectId = $scope.data.existObjectId;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
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

            vm.filters = {
                partNumber: null,
                partName: null,
                description: null,
                mfrPartType: '',
                type:'',
                manufacturer: '',
                freeTextSearch: true,
                searchQuery: null
            };
            vm.selectedObj = null;
            vm.manufacturersParts = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var pleaseSelectOnePart = parsed.html($translate.instant("PLEASE_SELECT_ONE_PART")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            vm.selectAttributeDef = $scope.data.selectAttDef;

            function nextPage() {
                vm.pageable.page++;
                loadMfrParts();
            }

            function previousPage() {
                vm.pageable.page--;
                loadMfrParts();
            }

            vm.searchFilter = {
                searchQuery: null,
                type: ''
            };

            vm.searchTerm = null;
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    if (vm.selectAttributeDef.refSubType == null) {
                        promise = MfrPartsService.selectionFreeTextSearch(vm.pageable, vm.searchTerm)
                    } else if (vm.selectAttributeDef.refSubType != null) {
                        vm.searchFilter.type = vm.selectAttributeDef.refSubType;
                        vm.searchFilter.searchQuery = vm.searchTerm;
                        promise = MfrPartsService.getMfrPartsByTypeId(vm.pageable, vm.searchFilter)
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                loadSelectedMfrs(data);
                                vm.clear = true;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                } else {
                    resetPage();
                    loadMfrParts();
                }
            }

            function clearFilter() {
                loadMfrParts();
                vm.clear = false;
            }

            function resetPage() {
                vm.manufacturersParts = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                vm.searchFilter.searchQuery = null;
                $scope.freeTextQuery = null;
                loadMfrParts();
            }

            function loadMfrParts() {
                vm.manufacturersParts = [];
                vm.clear = false;
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType == null) {
                    promise = MfrPartsService.getMfrParts(vm.pageable, vm.filters)
                } else if (vm.selectAttributeDef.refSubType != null) {
                    vm.searchFilter.type = vm.selectAttributeDef.refSubType;
                    promise = MfrPartsService.getMfrPartsByTypeId(vm.pageable, vm.searchFilter)
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            loadSelectedMfrs(data);
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadSelectedMfrs(data) {
                vm.manufacturersParts = data;
                var existObjectId = false;
                angular.forEach(vm.manufacturersParts.content, function (part) {
                    if (objectId != null && objectId != "" && objectId != undefined && objectId == part.id) {
                        vm.manufacturersParts.content.splice(vm.manufacturersParts.content.indexOf(part), 1);
                        existObjectId = true;
                    }
                    part.checked = false;
                });
                if (existObjectId) {
                    vm.manufacturersParts.totalElements = vm.manufacturersParts.totalElements - 1;
                    vm.manufacturersParts.numberOfElements = vm.manufacturersParts.numberOfElements - 1;
                }
            }

            function selectRadioChange(mfrPart, $event) {
                radioChange(mfrPart, $event);
                selectRadio();
            }

            function radioChange(mfrPart, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === mfrPart) {
                    mfrPart.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = mfrPart;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage(pleaseSelectOnePart);
                }
            }

            module.directive('autoFocus', ['$timeout', function ($timeout) {
                return {
                    restrict: 'A',
                    link: function ($scope, $element) {
                        $timeout(function () {
                            $element[0].focus();
                        });
                    }
                }
            }]);

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.select.mfrPart', selectRadio);
                loadMfrParts();
                //}
            })();
        }
    }
)
;