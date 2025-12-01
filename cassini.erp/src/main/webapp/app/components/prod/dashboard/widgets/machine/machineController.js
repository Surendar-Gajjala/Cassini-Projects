define(['app/app.modules',
        'app/components/prod/dashboard/widgets/productionWidgetFactory',
        'app/shared/constants/constants'
    ],
    function ($app) {
        $app.controller('MachineController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal', 'productionWidgetFactory', 'CONSTANTS',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal, productionWidgetFactory, CONSTANTS) {

                    var selectedMachine = {},

                        init = function () {
                            productionWidgetFactory.getMachines(false, $scope.pageable).then(
                                function (data) {

                                    $scope.machineList = data;
                                    $scope.loading = false;

                                    angular.forEach($scope.machineList, function (item) {
                                        var p = angular.copy(item);
                                        item.machine = p;

                                        item.editMode = false;
                                    });
                                },

                                function (error) {

                                }
                            );
                        },

                        getWorkcenters = function (callback) {
                            productionWidgetFactory.getWorkCenters(false).then(
                                function (results) {
                                    callback(results);
                                },

                                function (error) {

                                }
                            );
                        };
                    updateChanges = function (currentObj) {
                        productionWidgetFactory.updateMachine(currentObj).then(
                            function (response) {
                                currentObj = response;
                            },

                            function (error) {

                            }
                        );
                    },
                        deleteChanges = function (currentObj) {
                            productionWidgetFactory.deleteMachine(currentObj).then(
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
                        $scope.machineList = [];//$scope.paginationObj;
                    $scope.workcenterList = [];


                    $scope.constants = CONSTANTS;

                    $scope.addNewMachine = function () {
                        getWorkcenters(function (workcenters) {
                            var modalInstance = $modal.open({
                                animation: true,
                                templateUrl: 'app/components/prod/dashboard/widgets/machine/machineDialog.jsp',
                                controller: ['$scope', '$modalInstance', 'productionWidgetFactory', 'CONSTANTS', 'workcenters', function ($scope, $modalInstance, productionWidgetFactory, CONSTANTS, workcenters) {
                                    var validate = function (form) {
                                        angular.forEach(form.$error.required, function (field) {
                                            field.$setDirty();
                                        });
                                        return form.$valid;
                                    };
                                    $scope.constants = CONSTANTS;

                                    $scope.machine = {
                                        name: '',
                                        description: '',
                                        workcenter: null,
                                        editMode: true
                                    };

                                    $scope.workcenterList = workcenters;

                                    $scope.create = function (form) {
                                        if (validate(form)) {

                                            productionWidgetFactory.createMachine($scope.machine).then(
                                                function () {
                                                    $modalInstance.close();
                                                },

                                                function (error) {

                                                }
                                            );
                                        }
                                    };

                                    /*      $scope.workcenterValidator = function(workcenter) {
                                     return workcenter.id ? true : false;
                                     };
                                     */
                                    $scope.cancel = function () {
                                        $modalInstance.dismiss('cancel');
                                    };
                                }],
                                size: 'md',
                                resolve: {
                                    workcenters: function () {
                                        return workcenters;
                                    }
                                }
                            });

                            modalInstance.result.then(
                                function () {
                                    init();
                                },
                                function () {

                                }
                            );
                        });
                    };

                    $scope.hideEditMode = function (selectedObj) {
                        selectedObj.name = selectedMachine.name;
                        selectedObj.description = selectedMachine.description;
                        selectedObj.workcenter = selectedMachine.workcenter;
                        selectedObj.editMode = false;
                        selectedMachine = {};
                    };

                    $scope.acceptChanges = function (selectedObj) {
                        $timeout(function () {
                            selectedObj.editMode = false;
                        }, 500);
                        updateChanges(selectedObj);
                    };

                    $scope.removeItem = function (index, machine) {
                        $scope.machineList.splice(index, 1);
                        deleteChanges(machine);
                    };

                    $scope.showEditMode = function (machine) {
                        selectedMachine = angular.copy(machine);
                        $scope.machine = machine;
                        productionWidgetFactory.getWorkCenters(false).then(
                            function (data) {
                                $scope.workcenterList = data;
                            },
                            function (error) {

                            }
                        );

                        machine.editMode = true;

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