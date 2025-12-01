define(['app/desktop/modules/change/change.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/ecoService'
    ],
    function (module) {
        module.controller('ChangeSelectionController', ChangeSelectionController);

        function ChangeSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                           $uibModal, ECOService) {

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
            vm.selectAttributeDef = $scope.data.selectAttDef;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"
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

            vm.searchTerm = null;
            vm.selectedObj = null;
            vm.changes = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            var pleaseSelectOne = parsed.html($translate.instant("PLEASE_SELECT_ATLEAST_ONE")).html();
            var objectWord = parsed.html($translate.instant("OBJECT_WORD")).html();

            function nextPage() {
                vm.pageable.page++;
                loadChanges();
            }

            function previousPage() {
                vm.pageable.page--;
                loadChanges();
            }

            vm.filters = {
                type: '',
                searchQuery: null
            };

            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    loadChanges();
                } else {
                    resetPage();
                    loadChanges();
                }
            }

            function clearFilter() {
                loadChanges();
                $scope.freeTextQuery = null;
                vm.filters.searchQuery = null;
                vm.clear = false;
            }

            function resetPage() {
                vm.changes = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadChanges();
            }

            function loadChanges() {
                vm.clear = false;
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType != null) {
                    vm.filters.type = vm.selectAttributeDef.refSubType;
                }
                ECOService.getChangeObjects(vm.pageable, vm.filters).then(
                    function (data) {
                        loadSelectedObjects(data);
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }


            function loadSelectedObjects(data) {
                vm.changes = data;
                var existObjectId = false;
                angular.forEach(vm.changes.content, function (eco) {
                    if (objectId != null && objectId != undefined && objectId != "" && objectId == eco.id) {
                        vm.changes.content.splice(vm.changes.content.indexOf(eco), 1);
                        existObjectId = true;
                    }
                    eco.checked = false;
                });
                if (existObjectId) {
                    vm.changes.totalElements = vm.changes.totalElements - 1;
                    vm.changes.numberOfElements = vm.changes.numberOfElements - 1;
                }
            }

            function selectRadioChange(eco, $event) {
                radioChange(eco, $event);
                selectRadio();
            }

            function radioChange(eco, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === eco) {
                    eco.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = eco;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    if (vm.selectAttributeDef.refSubTypeName != null) {
                        $rootScope.showWarningMessage(pleaseSelectOne + " " + angular.lowercase(vm.selectAttributeDef.refSubTypeName) + " " + objectWord);
                    } else {
                        $rootScope.showWarningMessage(pleaseSelectOne + " " + angular.lowercase(vm.selectAttributeDef.refType) + " " + objectWord);
                    }
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

            $rootScope.showChangeTitle = "ECO Title";

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.select.change', selectRadio);
                loadChanges();
                //}
            })();
        }
    }
)
;
