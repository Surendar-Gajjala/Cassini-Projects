define(['app/app.modules',
        'app/components/prod/dashboard/widgets/productionWidgetFactory',
        'app/shared/constants/constants'
    ],
    function ($app) {
        $app.controller('ProcessStepController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal', 'productionWidgetFactory', 'CONSTANTS',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal,
                          productionWidgetFactory, CONSTANTS) {

                    var selectedProcessStep = {},

                        init = function () {
                            productionWidgetFactory.getProcessSteps(false, $scope.pageable).then(
                                function (data) {

                                    $scope.processStepList = data;
                                    $scope.loading = false;

                                    angular.forEach($scope.processStepList, function (item) {
                                        var p = angular.copy(item);
                                        item.processStep = p;

                                        item.editMode = false;
                                    });
                                },

                                function (error) {

                                }
                            );
                        },

                        getProcessDropDownData = function (callback) {
                            productionWidgetFactory.getProcessDropdownData().then(
                                function (results) {
                                    callback(results);
                                },

                                function (error) {

                                }
                            );
                        };

                    updateChanges = function (currentObj) {
                        productionWidgetFactory.updateProcessStep(currentObj).then(
                            function (response) {
                                currentObj = response;
                            },

                            function (error) {

                            }
                        );
                    },
                        deleteChanges = function (currentObj) {
                            productionWidgetFactory.deleteProcessStep(currentObj).then(
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
                        $scope.processStepList = [];// $scope.paginationObj;
                    $scope.workcenterList = [];
                    $scope.processList = [];


                    $scope.constants = CONSTANTS;

                    $scope.addNewProcessStep = function () {
                        getProcessDropDownData(function (processworkcenter) {
                            var modalInstance = $modal.open({
                                animation: true,
                                templateUrl: 'app/components/prod/dashboard/widgets/processStep/processStepDialog.jsp',
                                controller: ['$scope', '$modalInstance', 'productionWidgetFactory', 'CONSTANTS', 'processworkcenter', function ($scope, $modalInstance, productionWidgetFactory, CONSTANTS, processworkcenter) {
                                    var validate = function (form) {
                                        angular.forEach(form.$error.required, function (field) {
                                            field.$setDirty();
                                        });

                                        return form.$valid;
                                    };
                                    $scope.constants = CONSTANTS;

                                    $scope.processStep = {
                                        name: '',
                                        description: '',
                                        process: null,
                                        workcenter: null,
                                        sequenceNumber: 1,
                                        editMode: true
                                    };

                                    $scope.workcenterList = processworkcenter[0].data;
                                    $scope.processList = processworkcenter[1].data;

                                    $scope.create = function (form) {
                                        if (validate(form)) {
                                            productionWidgetFactory.createProcessStep($scope.processStep).then(
                                                function () {
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
                                resolve: {
                                    processworkcenter: function () {
                                        return processworkcenter;
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
                        selectedObj.name = selectedProcessStep.name;
                        selectedObj.description = selectedProcessStep.description;
                        selectedObj.workcenter = selectedProcessStep.workcenter;
                        selectedObj.process = selectedProcessStep.process;
                        selectedObj.editMode = false;
                        selectedProcessStep = {};
                    };

                    $scope.acceptChanges = function (selectedObj) {
                        $timeout(function () {
                            selectedObj.editMode = false;
                        }, 500);
                        updateChanges(selectedObj);
                    };

                    $scope.removeItem = function (index, processStep) {
                        $scope.processStepList.splice(index, 1);
                        deleteChanges(processStep);
                    };

                    $scope.showEditMode = function (processStep) {
                        selectedProcessStep = angular.copy(processStep);
                        $scope.processStep = processStep;
                        productionWidgetFactory.getProcessDropdownData().then(
                            function (data) {
                                $scope.workcenterList = data[0].data;
                                $scope.processList = data[1].data;

                            },
                            function (error) {

                            }
                        );

                        processStep.editMode = true;

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