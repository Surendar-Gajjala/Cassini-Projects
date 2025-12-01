/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/pm/project/projectSiteService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('SiteSelectionController', SiteSelectionController);

        function SiteSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                         ProjectSiteService) {

            var vm = this;

            vm.loading = true;
            vm.selectedObj = null;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
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
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.sites = angular.copy(pagedResults);

            function loadSites() {
                vm.clear = false;
                vm.loading = true;
                ProjectSiteService.getSites(pageable).then(
                    function (data) {
                        vm.sites = data;
                        angular.forEach(vm.sites.content, function (site) {
                            site.checked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.sites = [];
                    ProjectSiteService.freeTextSearch(null, pageable, freeText).then(
                        function (data) {
                            vm.sites = data;
                            vm.clear = true;
                            vm.sites = data;
                            angular.forEach(vm.sites.content, function (site) {
                                site.checked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadSites();
                }
            }

            function clearFilter() {
                loadSites();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(site, $event) {
                radioChange(site, $event);
                selectRadio();
            }

            function radioChange(site, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === site) {
                    site.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = site;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select site");
                }
            }

            function nextPage() {
                if (vm.sites.last != true) {
                    pageable.page++;
                    loadSites();
                }
            }

            function previousPage() {
                if (vm.sites.first != true) {
                    pageable.page--;
                    loadSites();
                }
            }

            (function () {
                $rootScope.$on('app.site.selected', selectRadio);
                loadSites();
            })();
        }
    }
)
;

