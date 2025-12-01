/**
 * Created by swapna on 28/12/18.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/store/scrapService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ScrapRequestSelectionController', ScrapRequestSelectionController);

        function ScrapRequestSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies,
                                                 $uibModal, ScrapService, ProjectService, TopStoreService) {

            var vm = this;

            vm.loading = true;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.selectedObj = null;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            var pageable = {
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
                size: pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.scrapRequests = angular.copy(pagedResults);

            function loadScrapRequests() {
                vm.clear = false;
                vm.loading = true;
                ScrapService.getAll(pageable).then(
                    function (data) {
                        vm.scrapRequests = data;
                        ProjectService.getProjectReferences(vm.scrapRequests.content, 'project');
                        TopStoreService.getStoreReferences(vm.scrapRequests.content, 'store');
                        angular.forEach(data.content, function (scrapRequest) {
                            scrapRequest.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.scrapRequests = [];
                    ScrapService.freeTextSearch(pageable, freeText).then(
                        function (data) {
                            vm.scrapRequests = data;
                            vm.clear = true;
                            ProjectService.getProjectReferences(vm.scrapRequests.content, 'project');
                            TopStoreService.getStoreReferences(vm.scrapRequests.content, 'store');
                            angular.forEach(data.content, function (scrapRequest) {
                                scrapRequest.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadScrapRequests();
                }
            }

            function clearFilter() {
                loadScrapRequests();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(scrapRequest, $event) {
                radioChange(scrapRequest, $event);
                selectRadio();
            }

            function radioChange(scrapRequest, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === scrapRequest) {
                    scrapRequest.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = scrapRequest;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.scrapRequests.last != true) {
                    pageable.page++;
                    loadScrapRequests();
                }
            }

            function previousPage() {
                if (vm.scrapRequests.first != true) {
                    pageable.page--;
                    loadScrapRequests();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.scrapRequest.selected', selectRadio);
                    loadScrapRequests();
                }
            })();
        }
    }
)
;
