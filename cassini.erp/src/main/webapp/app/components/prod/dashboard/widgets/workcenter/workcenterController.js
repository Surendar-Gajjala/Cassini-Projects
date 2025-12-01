define(['app/app.modules',
        'app/components/prod/dashboard/widgets/productionWidgetFactory'
    ],
    function ($app) {
        $app.controller('WorkcenterController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal', 'productionWidgetFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal,
                          productionWidgetFactory) {

                    var selectedWorkcenter = {},
                        init = function () {
                            productionWidgetFactory.getWorkCenters(false, $scope.pageable).then(
                                function (data) {

                                    $scope.workcenterObj = data;
                                    $scope.loading = false;

                                    angular.forEach($scope.workcenterObj, function (item) {
                                        var p = angular.copy(item);
                                        item.workcenter = p;

                                        item.editMode = false;
                                    });
                                },

                                function (error) {

                                }
                            );
                        },
                        updateChanges = function (currentObj) {
                            productionWidgetFactory.updateWorkCenter(currentObj).then(
                                function (response) {
                                    currentObj = response;
                                },

                                function (error) {

                                }
                            );
                        },
                        deleteChanges = function (currentObj) {
                            productionWidgetFactory.deleteWorkCenter(currentObj).then(
                                function (response) {

                                    init();
                                },

                                function (error) {

                                }
                            );
                        };


                    $scope.pageable = {
                        page: 1,
                        size: 5,
                        sort: {
                            label: "Name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.paginationObj = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    },
                        $scope.workcenterObj = {};// $scope.paginationObj;

                    $scope.addNewWorkcenter = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/dashboard/widgets/workcenter/workcenterDialog.jsp',
                            controller: ['$scope', '$modalInstance', 'productionWidgetFactory', 'CONSTANTS', function ($scope, $modalInstance, productionWidgetFactory, CONSTANTS) {
                                var validate = function (form) {
                                    angular.forEach(form.$error.required, function (field) {
                                        field.$setDirty();
                                    });

                                    return form.$valid;
                                };
                                $scope.constants = CONSTANTS;

                                $scope.workcenter = {
                                    name: '',
                                    description: '',
                                    editMode: true
                                };


                                $scope.create = function (form) {
                                    if (validate(form)) {
                                        productionWidgetFactory.createWorkCenter($scope.workcenter).then(
                                            function (result) {
                                                $modalInstance.close();
                                            },

                                            function (error) {

                                            }
                                        );
                                    }
                                };

                                $scope.cancel = function () {
                                    $modalInstance.dismiss('cancel');
                                };
                            }],
                            size: 'md',
                            resolve: {}
                        });

                        modalInstance.result.then(
                            function () {
                                init();
                            },
                            function () {

                            }
                        );
                    };


                    $scope.hideEditMode = function (selectedObj) {
                        selectedObj.name = selectedWorkcenter.name;
                        selectedObj.description = selectedWorkcenter.description;
                        selectedObj.editMode = false;
                        selectedWorkcenter = {};
                    };

                    $scope.acceptChanges = function (selectedObj) {
                        $timeout(function () {
                            selectedObj.editMode = false;
                        }, 500);
                        updateChanges(selectedObj);
                    };

                    $scope.removeItem = function (index, workcenter) {
                        $scope.workcenterObj.splice(index, 1);
                        deleteChanges(workcenter);
                    };

                    $scope.showEditMode = function (workcenter) {
                        selectedWorkcenter = angular.copy(workcenter);
                        $scope.workcenter = workcenter;
                        workcenter.editMode = true;

                    };

                    $scope.pageChanged = function () {
                        init();
                    };

                    init();
                }
            ]
        );
    }
);