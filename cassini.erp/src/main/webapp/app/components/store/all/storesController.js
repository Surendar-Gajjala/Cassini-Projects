define(['app/app.modules',
        'app/components/store/storeFactory',
        'app/components/store/all/newStoreDialogController'
    ],
    function (module) {
        module.controller('StoresController', StoresController);

        function StoresController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $modal,
                                  StoreFactory) {
            $rootScope.iconClass = "fa flaticon-agreement";
            $rootScope.viewTitle = "Stores";

            $scope.loading = true;
            $scope.showSearchMode = false;
            $scope.stores = [];
            $scope.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "createdDate",
                    order: "ASC"
                }
            };

            $scope.showImage = showImage;
            $scope.showRequiredImage = showRequiredImage;
            $scope.openAttachment = openAttachment;

            $scope.$on('$viewContentLoaded', function () {
                $rootScope.setToolbarTemplate('stores-view-tb');
            });

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: $scope.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            $rootScope.stores = pagedResults;

            $scope.emptyFilters = {
                storeName: null,
                description: null,
                locationName: null,
                createdBy: "",
                createdOn: null,
                searchQuery: null
            };

            $scope.filters = angular.copy($scope.emptyFilters);
            $scope.resetPage = resetPage;


            $rootScope.newStore = newStore;
            $scope.openStore = openStore;
            $scope.deleteStore = deleteStore;

            $rootScope.selectedStore = null;

            $scope.persons = [];
            $scope.applyFilters = applyFilters;
            $scope.resetFilters = resetFilters;

            function applyFilters() {
                $scope.pageable.page = 0;
                loadStores();
            }

            function resetFilters() {
                $scope.filters = angular.copy($scope.emptyFilters);
                $scope.pageable.page = 0;
                loadStores();
            };


            function resetPage() {
                $scope.showSearchMode = false;
                loadStores();
            }

            function openStore(store) {
                $state.go('app.store.details', {storeId: store.id});
                $rootScope.selectedStore = store;
            }

            function loadStores() {
                $scope.storeIds = [];
                $scope.requiredAttributeIds = [];
                $scope.filters.createdBy = $scope.filters.createdBy != null || undefined ? $scope.filters.createdBy.id : "";
                StoreFactory.getStores($scope.pageable).then(
                    function (data) {
                        $scope.stores = data;
                        $scope.loading = false;
                    }
                );
            }

            function newStore() {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'app/components/store/all/newStoreDialog.jsp',
                    controller: 'NewStoreDialogController',
                    size: 'md',
                    resolve: {
                        storeType: function () {
                            return 'Stores';
                        },
                        "dialogTitle": "New Store"
                    }
                });

                modalInstance.result.then(
                    function () {
                        loadStores();
                    }
                );
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.filters.searchQuery = freeText;
                    StoreFactory.freeTextSearch($scope.pageable, $scope.filters).then(
                        function (data) {
                            $scope.stores = data;
                            $scope.showSearchMode = true;
                        }
                    );
                } else {
                    resetPage();
                    loadStores();
                }
            }

            function deleteStore(store) {
                StoreFactory.deleteStore(store.id).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Store deleted successfully");
                        loadStores();
                    }
                )
            }

            function showImage(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showRequiredImage(attribute) {
                var modal = document.getElementById('myModal1');
                var modalImg = document.getElementById('img02');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }


            (function () {
                loadStores();
            })();
        }
    }
)
;