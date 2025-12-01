define(['app/app.modules',
        'app/components/prod/material/materialFactory',
        'app/shared/constants/listValues'],
    function($app) {
        $app.controller('MaterialInventoryHistoryController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'ERPLists',
                'materialFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies, ERPLists,
                         materialFactory) {


                    $scope.loading = true;

                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 0,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {}
                    };

                    $scope.history = $scope.emptyPagedResults;
                    $scope.statuses = ERPLists.inventoryStatuses;


                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.history.content = [];
                        loadMaterialInventoryHistory();
                    };

                    function loadMaterialInventoryHistory() {
                        $scope.history = $scope.emptyPagedResults;
                        materialFactory.getMaterialInventoryHistory($scope.selectedMaterial.id, $scope.pageable).then (
                            function(data) {
                                $scope.loading = false;
                                $scope.history = data;
                            }
                        )
                    }

                    (function() {
                        loadMaterialInventoryHistory();
                    })();
                }
            ]
        );
    }
);