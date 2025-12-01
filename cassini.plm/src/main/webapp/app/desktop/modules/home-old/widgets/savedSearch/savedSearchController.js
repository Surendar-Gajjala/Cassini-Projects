define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/searchService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('SavedSearchController', SavedSearchController);

        function SavedSearchController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$translate,
                                       SearchService, CommonService) {
            var vm = this;

            vm.savedSearches = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showSavedResult = showSavedResult;
            vm.loading = true;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.pagedResults = {
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

            vm.savedSearches = angular.copy(vm.pagedResults);
            var owner = null;

            function nextPage() {
                vm.pageable.page++;
                loadSavedSearch();
            }

            function previousPage() {
                vm.pageable.page--;
                loadSavedSearch();
            }

            var parsed = angular.element("<div></div>");
            vm.public = parsed.html($translate.instant("PUBLIC")).html();
            vm.private = parsed.html($translate.instant("PRIVATE")).html();


            function loadSavedSearch() {
                vm.clear = false;
                vm.loading = true;
                SearchService.getSavedSearch(owner, vm.pageable).then(
                    function (data) {
                        vm.savedSearches = data;
                        angular.forEach(vm.savedSearches.content, function (savedSearch) {
                            if (savedSearch.objectType != "CHANGE") {
                                savedSearch.objectType = savedSearch.objectType;
                            } else {
                                savedSearch.objectType = "ECO";
                            }

                            savedSearch.ownerType = savedSearch.type;
                            savedSearch.type = savedSearch.objectType;
                        });
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.savedSearches.content, "owner");
                    }
                )
            }

            $rootScope.searchQuery = null;
            $rootScope.searchType = null;
            function showSavedResult(savedSearch) {
                if (savedSearch.objectType == 'ITEM') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $state.go("app.items.all");
                } else if (savedSearch.objectType == 'CHANGE') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $state.go("app.changes.ecos.all");
                } else if (savedSearch.objectType == 'MANUFACTURER') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $state.go("app.mfr.all");
                }
            }

            (function () {
                $timeout(function () {
                    $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                    if ($rootScope.localStorageLogin != null) {
                        owner = $rootScope.localStorageLogin.login.person.id;
                    }
                    loadSavedSearch();
                }, 1000);

            })();
        }

    });
