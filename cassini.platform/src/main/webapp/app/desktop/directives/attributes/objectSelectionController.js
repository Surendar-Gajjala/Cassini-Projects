define([ 'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('ObjectSelectionController', ObjectSelectionController);

        function ObjectSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                        $uibModal, ObjectAttributeService) {

            var vm = this;

            var attribute = $scope.data.selectedAtrribute;

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

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };


            vm.selectedObj = null;
            vm.objects = angular.copy(pagedResults);

            function nextPage() {
                pageable.page++;
                loadObjects();
            }

            function previousPage() {
                pageable.page--;
                loadObjects();
            }


            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    ObjectAttributeService.freeTextSearch(pageable, vm.searchTerm).then(
                        function (data) {
                            vm.objects = data;
                            vm.clear = true;
                        }
                    )
                } else {
                    resetPage();
                    loadObjects();
                }
            }

            function clearFilter() {
                loadObjects();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
                vm.searchTerm = null;
                loadObjects();
            }

            function loadObjects() {
                $scope.clear = false;
                vm.loading = true;
                ObjectAttributeService.getObjects(attribute,pageable).then(
                    function (data) {
                        vm.objects = data;
                        angular.forEach(data, function (obj) {
                            obj.isChecked = false;
                        })
                        vm.loading = false;
                    }
                );
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
                    $scope.callback(vm.selectedObj);
                    $rootScope.hideSidePanel('left');
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.select.object', selectRadio);
                    loadObjects();
                //}
            })();
        }
    }
)
;
