define(['app/desktop/modules/mfr/mfr.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mfrService'
    ],
    function (module) {
        module.controller('MfrSelectionController', MfrSelectionController);

        function MfrSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                        $uibModal, MfrService) {

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
            vm.searchTerm = null;
            $scope.freeTextQuery = null;
            var objectId = $scope.data.existObjectId;
            var parsed = angular.element("<div></div>");
            var pleaseSelectOneMfr = parsed.html($translate.instant("PLEASE_SELECT_ONE_MFR")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            vm.selectAttributeDef = $scope.data.selectAttDef;
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

            vm.selectedObj = null;
            vm.manufacturers = angular.copy(pagedResults);

            function nextPage() {
                vm.pageable.page++;
                loadMfrs();
            }

            function previousPage() {
                vm.pageable.page--;
                loadMfrs();
            }

            vm.filters = {
                searchQuery: null,
                type: ''
            };

            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    if (vm.selectAttributeDef.refSubType == null) {
                        promise = MfrService.freeTextSearch(vm.pageable, vm.searchTerm)
                    } else if (vm.selectAttributeDef.refSubType != null) {
                        vm.filters.type = vm.selectAttributeDef.refSubType;
                        vm.filters.searchQuery = vm.searchTerm;
                        promise = MfrService.getMfrsByTypeId(vm.pageable, vm.filters)
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                loadSelectedMfrParts(data);
                                vm.clear = true;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                } else {
                    resetPage();
                    loadMfrs();
                }
            }

            function clearFilter() {
                loadMfrs();
                vm.clear = false;
            }

            function resetPage() {
                vm.manufacturers = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadMfrs();
            }

            function loadMfrs() {
                vm.manufacturers = [];
                vm.clear = false;
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType == null) {
                    promise = MfrService.getManufacturers(vm.pageable)
                } else if (vm.selectAttributeDef.refSubType != null) {
                    vm.filters.type = vm.selectAttributeDef.refSubType;
                    promise = MfrService.getMfrsByTypeId(vm.pageable, vm.filters)
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            loadSelectedMfrParts(data);
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadSelectedMfrParts(data) {
                vm.manufacturers = data;
                var existObjectId = false;
                angular.forEach(vm.manufacturers.content, function (mfr) {
                    if (objectId != null && objectId != "" && objectId != undefined && objectId == mfr.id) {
                        vm.manufacturers.content.splice(vm.manufacturers.content.indexOf(mfr), 1);
                        existObjectId = true;
                    }
                    mfr.isChecked = false;
                });
                if (existObjectId) {
                    vm.manufacturers.totalElements = vm.manufacturers.totalElements - 1;
                    vm.manufacturers.numberOfElements = vm.manufacturers.numberOfElements - 1;
                }
            }

            function selectRadioChange(mfr, $event) {
                radioChange(mfr, $event);
                selectRadio();
            }

            function radioChange(mfr, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === mfr) {
                    mfr.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = mfr;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage(pleaseSelectOneMfr);
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
                $rootScope.$on('app.select.mfr', selectRadio);
                loadMfrs();
                //}
            })();
        }
    }
)
;
