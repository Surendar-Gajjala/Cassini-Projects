define(['app/app.modules',
        'app/components/prod/dashboard/widgets/productionWidgetFactory',
        'app/components/prod/dashboard/widgets/workshiftemp/workShiftEmpSelectionController',
    ],
    function ($app) {
        $app.controller('EmployeeShiftController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal', 'productionWidgetFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal,
                          productionWidgetFactory) {

                    var selectedWorkShiftEmp = {},
                        getWorkShifts = function () {
                            productionWidgetFactory.getWorkShifts().then(
                                function (data) {
                                    $scope.workShifts = data;
                                    angular.forEach($scope.workShifts, function (item) {
                                        var p = angular.copy(item);
                                        item.workShift = p;

                                        item.defaultSelect = false;
                                    });
                                    $scope.workShifts[0].defaultSelect = true;
                                    $scope.selectedShiftId = $scope.workShifts[0].shiftId;

                                    $scope.emptyFilters.shift = $scope.workShifts[0].shiftId;
                                    $scope.filters = angular.copy($scope.emptyFilters);
                                    return productionWidgetFactory.getWorkShiftEmpsByShiftId($scope.workShifts[0].shiftId);
                                }
                            ).then(
                                function (data) {
                                    $scope.workShiftEmpList = data;
                                    $scope.loading = false;

                                    angular.forEach($scope.workShiftEmpList, function (item) {
                                        var p = angular.copy(item);
                                        item.workShiftEmp = p;

                                        item.editMode = false;
                                    });
                                }
                            );
                        }
                    updateChanges = function (currentObj) {
                        productionWidgetFactory.updateWorkShiftEmp(currentObj).then(
                            function (response) {
                                currentObj = response;
                            },

                            function (error) {

                            }
                        );
                    },
                        deleteChanges = function (currentObj) {
                            productionWidgetFactory.deleteWorkShiftEmp(currentObj).then(
                                function (response) {

                                    init();
                                },

                                function (error) {

                                }
                            );
                        };

                    $scope.selectedShiftId = 0;

                    $scope.pageable = {
                        page: 1,
                        size: 5,
                        sort: {
                            label: "ShiftId",
                            field: "shiftId",
                            order: "asc"
                        }
                    };
                    $scope.emptyFilters = {
                        shift: 1,
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);
                    $scope.workShifts = [];
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
                        $scope.workShiftEmpList = $scope.paginationObj;
                    $scope.employeesList = $scope.paginationObj;
                    $scope.selectedEmps = [];

                    $scope.workShiftEmp = {
                        employee: null,
                        shiftId: 0,
                        workcenter: null,
                        editMode: true
                    };
                    $scope.workcenterList = [];

                    $scope.init = function (shiftId) {
                        $scope.selectedShiftId = shiftId;
                        $scope.emptyFilters.shift = shiftId;
                        $scope.filters = angular.copy($scope.emptyFilters);

                        productionWidgetFactory.getWorkShiftEmpsByShiftId(shiftId).then(
                            function (data) {
                                $scope.workShiftEmpList = data;
                                $scope.loading = false;

                                angular.forEach($scope.workShiftEmpList, function (item) {
                                    var p = angular.copy(item);
                                    item.workShiftEmp = p;

                                    item.editMode = false;
                                });
                            },

                            function (error) {

                            }
                        );
                    };
                    $rootScope.addWorkShiftEmps = function () {
                        $scope.selectedEmps = [];
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/dashboard/widgets/workshiftemp/employeesSelectionDialog.jsp',
                            controller: 'EmployeesShiftSelectionController',
                            size: 'lg',
                            resolve: {
                                selectedEmployee: function () {
                                    return null;
                                },
                                "dialogTitle": "Select employees for shift"
                            }
                        });

                        modalInstance.result.then(
                            function (empSelectList) {
                                console.log(empSelectList);
                                angular.forEach(empSelectList.content, function (item) {

                                    if (item.selected == true) {
                                        var workShiftEmp = {
                                            employee: item,
                                            shiftId: $scope.selectedShiftId
                                        };
                                        $scope.selectedEmps.push(workShiftEmp);
                                    }
                                });
                                productionWidgetFactory.createWorkShiftEmp($scope.selectedEmps).then(
                                    function (response) {
                                        $scope.selectedEmps = [];
                                        $scope.init($scope.selectedShiftId);
                                    },
                                    function (error) {
                                    }
                                );
                            }
                        );
                    };

                    $scope.hideEditMode = function (selectedObj) {
                        selectedObj.employee = selectedWorkShiftEmp.employee;
                        selectedObj.shiftId = selectedWorkShiftEmp.shiftId;
                        selectedObj.workcenter = selectedWorkShiftEmp.workcenter;
                        selectedObj.editMode = false;
                        selectedWorkShiftEmp = {};
                    };

                    $scope.acceptChanges = function (selectedObj) {
                        $timeout(function () {
                            selectedObj.editMode = false;
                        }, 500);
                        updateChanges(selectedObj);
                    };

                    $scope.removeItem = function (index, workShiftEmp) {
                        $scope.workShiftEmpList.splice(index, 1);
                        deleteChanges(workShiftEmp);
                    };

                    $scope.showEditMode = function (workShiftEmp) {
                        selectedWorkShiftEmp = angular.copy(workShiftEmp);
                        $scope.workShiftEmp = workShiftEmp;

                        productionWidgetFactory.getWorkCenters(false).then(
                            function (data) {
                                $scope.workcenterList = data;
                            },
                            function (error) {

                            }
                        );

                        workShiftEmp.editMode = true;

                    };

                    $scope.pageChanged = function () {
                        $scope.init($scope.selectedShiftId);
                    };

                    getWorkShifts();

                }
            ]
        );
    }
);