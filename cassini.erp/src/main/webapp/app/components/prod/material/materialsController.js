define(['app/app.modules', 'app/components/prod/material/materialFactory',
        'app/components/prod/material/dialog/newMaterialController',
        'app/components/prod/material/category/newMaterialCategoryController'],
    function ($app) {
        $app.controller('MaterialsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal', 'materialFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal, materialFactory) {

                    $rootScope.iconClass = "fa flaticon-plan2";
                    $rootScope.viewTitle = "All Materials";

                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 15,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };
                    $scope.pageable = {
                        page: 1,
                        size: 15,
                        sort: {
                            label: "createdDate",
                            field: "createdDate",
                            order: "desc"
                        }
                    };

                    $scope.supNames = '';
                    $scope.materials = $scope.emptyPagedResults;
                    $scope.loadingMaterials = true;

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('materials-view-tb');
                    });

                    $scope.pageChanged = function () {
                        $scope.loading = true;
                        $scope.materials.content = [];
                        loadMaterials();
                    };
                    $scope.showMaterial = function (material) {
                        $state.go('app.prod.materialDetails', {materialId: material.id});
                    };


                    function loadMaterials() {
                        materialFactory.getMaterials($scope.pageable).then(
                            function (data) {
                                $scope.materials = data;

                                angular.forEach($scope.materials.content, function (item) {
                                    var p = angular.copy(item);
                                    item.material = p;
                                    angular.forEach(item.material.suppliers, function (ittem) {
                                        var cc = angular.copy(ittem);
                                        $scope.supNames = $scope.supNames + cc.name + ",";
                                    });
                                    if ($scope.supNames.endsWith(",")) {
                                        $scope.supNames = $scope.supNames.substring(0, $scope.supNames.length - 1);
                                    }

                                    item.supName = $scope.supNames;
                                    $scope.supNames = '';
                                });
                                $scope.loadingMaterials = false;
                            }
                        )
                    };


                    $rootScope.addMaterial = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/material/dialog/newMaterialView.jsp',
                            controller: 'NewMaterialController',
                            size: 'lg'
                        });

                        modalInstance.result.then(
                            function (result) {
                                if (result != "") {
                                    loadMaterials();
                                }

                                /*     var p = angular.copy(result);
                                 result.material = p;
                                 angular.forEach(result.material.suppliers, function(ittem) {
                                 var cc=angular.copy(ittem);
                                 $scope.supNames=$scope.supNames+cc.name+",";
                                 });
                                 if($scope.supNames.endsWith(","))
                                 {
                                 $scope.supNames = $scope.supNames.substring(0,$scope.supNames.length - 1);
                                 }

                                 result.supName = $scope.supNames;
                                 $scope.supNames='';

                                 $scope.materials.content.unshift(result);*/
                            }
                            //   $scope.materials.content.unshift(result);
                            //  $state.go($state.current,{},{reload:true});


                        );

                    };


                    $rootScope.addMaterialCategory = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/material/category/newMaterialCategoryView.jsp',
                            controller: 'NewMaterialCategoryController',
                            size: 'lg'
                        });

                        modalInstance.result.then(
                            function (result) {
                                if (result != "") {
                                    //loadMaterials();
                                }
                            }
                        );

                    };

                    $scope.showDetails = function (material) {
                        $state.go('app.prod.materials', {materialId: material.id});
                    };

                    (function () {
                        loadMaterials();
                    })();
                }
            ]
        );
    }
);


