define(['app/app.modules', 'app/components/prod/materialpo/materialsPOFactory',
        'app/components/prod/material/dialog/newMaterialController',
    'app/components/prod/material/category/newMaterialCategoryController'],
    function($app) {
        $app.controller('MaterialsPOController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies','$modal', 'materialsPOFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies,$modal, materialsPOFactory) {

                    $rootScope.iconClass = "fa flaticon-plan2";
                    $rootScope.viewTitle = "Material Purchase Orders";

                      $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 10,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "modifiedDate",
                            field: "modifiedDate",
                            order: "DESC"
                        }
                    };

                    $scope.supNames='';
                    $scope.materialPOs = $scope.emptyPagedResults;

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('materials-view-tb');
                    });

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.materialPOs.content = [];
                        loadMaterialPos();
                    };
                    $scope.showMaterialPO = function(material) {
                        $state.go('app.prod.materialspoDetails', { materialPoId: material.id });
                    };

                    function loadMaterialPos() {
                    	materialsPOFactory.getMaterialPurchaseOrders($scope.pageable).then (
                            function(data) {
                                $scope.materialPOs = data;

                                angular.forEach($scope.materialPOs.content, function(item) {
                                    var p = angular.copy(item);
                                    item.material = p;
                                      
                                    if($scope.supNames.endsWith(","))
                                    {
                                        $scope.supNames = $scope.supNames.substring(0,$scope.supNames.length - 1);
                                    }

                                    item.supName = $scope.supNames;
                                    $scope.supNames='';
                                });

                            }
                        )
                    };



               $rootScope.addMaterialPO = function() {
            	   
            	   $state.go('app.prod.newmaterialspo');

               }; 

                    (function() {
                        loadMaterialPos();
                    })();
                }
            ]
        );
    }
);


