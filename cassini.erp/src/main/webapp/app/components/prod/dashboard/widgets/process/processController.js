define(['app/app.modules',
        'app/components/prod/dashboard/widgets/productionWidgetFactory'
    ],
    function ($app) {
        $app.controller('ProcessController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal', 'productionWidgetFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal,
                          productionWidgetFactory) {

                    var selectedProcess = {},

                        init = function () {
                            productionWidgetFactory.getProcesses(false, $scope.pageable).then(
                                function (data) {

                                    $scope.processList = data;
                                    $scope.loading = false;

                                    angular.forEach($scope.processList, function (item) {
                                        var p = angular.copy(item);
                                        item.process = p;

                                        item.editMode = false;
                                    });
                                },

                                function (error) {

                                }
                            );
                        },
                        updateChanges = function (currentObj) {
                            productionWidgetFactory.updateProcess(currentObj).then(
                                function (response) {
                                    currentObj = response;
                                },

                                function (error) {

                                }
                            );
                        },
                        deleteChanges = function (currentObj) {
                            productionWidgetFactory.deleteProcess(currentObj).then(
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
                        $scope.processList = [];// $scope.paginationObj;

                    $scope.addNewProcess = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/dashboard/widgets/process/processDialog.jsp',
                            controller: ['$scope', '$modalInstance', 'productionWidgetFactory', 'CONSTANTS', function ($scope, $modalInstance, productionWidgetFactory, CONSTANTS) {
                                var validate = function (form) {
                                    angular.forEach(form.$error.required, function (field) {
                                        field.$setDirty();
                                    });

                                    return form.$valid;
                                };
                                $scope.constants = CONSTANTS;

                                $scope.process = {
                                    name: '',
                                    description: '',
                                    editMode: true
                                };


                                $scope.create = function (form) {
                                    if (validate(form)) {
                                        productionWidgetFactory.createProcess($scope.process).then(
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
                        selectedObj.name = selectedProcess.name;
                        selectedObj.description = selectedProcess.description;
                        selectedObj.editMode = false;
                        selectedProcess = {};
                    };

                    $scope.acceptChanges = function (selectedObj) {
                        $timeout(function () {
                            selectedObj.editMode = false;
                        }, 500);
                        updateChanges(selectedObj);
                    };

                    $scope.removeItem = function (index, process) {
                        $scope.processList.splice(index, 1);
                        deleteChanges(process);
                    };

                    $scope.showEditMode = function (process) {
                        selectedProcess = angular.copy(process);
                        $scope.process = process;
                        process.editMode = true;

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