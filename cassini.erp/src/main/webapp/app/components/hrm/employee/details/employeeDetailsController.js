define(['app/app.modules',
        'app/components/hrm/employee/details/employeeDetailsFactory',
        'app/components/hrm/employee/details/changePasswordController',
        'app/shared/constants/constants',
        'app/components/hrm/employee/employeeFactory',
        'app/components/common/commonFactory'
    ],
    function($app) {
        $app.controller('EmployeeDetailsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies', 'employeeDetailsFactory', 'commonFactory', 'employeeFactory', '$modal', 'CONSTANTS', '$timeout',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, employeeDetailsFactory, commonFactory,employeeFactory, $modal, CONSTANTS, $timeout) {

                    var timeOffData = {},
                        init = function() {
                            $scope.loginEmp = $app.login;
                            getEmployeeDetails();
                            getAllStates();
                            getAllCountries();
                            getAttendanceDetails();
                            employeePayDetails();
                            getTimeOffTypes();
                            getEmpTimeOffList(false);
                        },
                        getEmployeeDetails = function() {
                            var employeeId = $stateParams.employeeId;
                            employeeDetailsFactory.employeeDetails(employeeId).then (
                                function(details) {
                                    $scope.employeeDetails = details;
                                    getDepartmentDetails();
                                },

                                function (error) {

                                }
                            );
                        },
                        updateEmployee = function() {
                            //addressType: commonFactory.getAddressTypeByName("Billing")
                            $scope.employeeDetails.addresses[0].addressType = commonFactory.getAddressTypeByName("Home");
                            var empDetails = $scope.employeeDetails;
                            employeeDetailsFactory.updateEmployeeDetails(empDetails).then (
                             function(data) {

                             },

                             function (error) {

                             }
                             );
                        },
                        getDepartmentDetails = function() {
                            var depId = $scope.employeeDetails.department.id;
                            employeeDetailsFactory.departmentDetails(depId).then (
                                function(details) {
                                    $scope.departmentDetails = details;
                                },

                                function (error) {

                                }
                            );
                        },
                        getTimeOffTypes  = function() {
                            employeeDetailsFactory.timeOffTypes().then (
                                function(result) {
                                    $scope.timeOffTypes = result;
                                },

                                function (error) {

                                }
                            );
                        },
                        getAllStates = function() {
                            employeeFactory.allStates().then (
                                function(states) {
                                    $scope.allStates = states;
                                },
                                function (error) {
                                }
                            );
                        },
                        getAllCountries = function() {
                            employeeFactory.allCountries().then (
                                function(courtries) {
                                    $scope.allCountries = courtries;
                                },
                                function (error) {
                                }
                            );
                        },
                        getAttendanceDetails = function() {
                            var dt = new Date(),
                                month = dt.getMonth(),
                                year = dt.getFullYear(),
                                data = {month:month,type:$scope.tabtype.attendace,year:year};

                            $scope.currentMonth(data);
                        },
                        getEmpTimeOffList = function(isUpdate) {
                            var dt = new Date(),
                                month = dt.getMonth(),
                                year = dt.getFullYear(),
                                data = {month:month,type:$scope.tabtype.timeoff,year:year};
                            if(isUpdate){
                                $scope.currentMonth(timeOffData);
                            }else {
                                $scope.currentMonth(data);
                            }
                        },
                        employeePayDetails = function(){
                            var dt = new Date(),
                                employeeId = $stateParams.employeeId,
                                year = dt.getFullYear();

                            employeeDetailsFactory.employeePayDetails(employeeId,year).then (
                                function(data) {
                                    constructPayRoll(data);
                                },

                                function (error) {

                                }
                            );
                        },
                        constructPayRoll = function(data) {
                            var payRoll = {};

                            payRoll.allowanceTypes = data[0].data;
                            payRoll.deductionTypes = data[1].data;
                            payRoll.salaryList = data[2].data;

                            $scope.payRollObj = payRoll;

                        };

                    $rootScope.iconClass = "fa flaticon-businessman276";
                    $rootScope.viewTitle = "Employee Details";
                    $scope.employeeDetails = {};
                    $scope.attendanceList = [];
                    $scope.timeOffList = [];
                    $scope.departmentDetails = {};
                    $scope.timeOffTypes = [];
                    $scope.loginEmp = {};
                    $scope.changePassword = false;
                    $scope.payRollObj = {};
                    $scope.monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
                    $scope.changePasswordTemplate = "app/components/hrm/employee/details/changePasswordView.jsp";
                    $scope.constants = CONSTANTS;
                    $scope.tabtype = {timeoff:'TIMEOFF',attendace:'ATTENDANCE'};
                    $scope.loading = false;

                    $scope.stateValidator = function(state) {
                        var isValid = false;
                        if(state && state.id) {
                            isValid = true;
                        }
                        return isValid;
                    };

                    $scope.showChangePassword = function() {
                        $scope.changePassword = true;
                    };

                    $scope.cancelChangePassword = function() {
                        $scope.changePassword = false;
                    };

                    $scope.editEmpInfo = function() {
                        $scope.employeeDetails.isEdit = true;
                    };

                    $scope.updateEmpInfo = function() {
                        $timeout(function() {
                            $scope.employeeDetails.isEdit  = false;
                        }, 500);
                        updateEmployee();
                    };

                    $scope.getTimeOffType =  function(timeOffId){
                        var timeOfType = '';
                        angular.forEach($scope.timeOffTypes, function(value,key) {
                            if(value.id === timeOffId){
                                timeOfType = value.name;
                                return false;
                            }
                        });
                        return timeOfType;
                    };

                    $scope.isCurrentUserSameAsEmployee = function() {
                        var id = $app.login.person.id;
                        if($scope.employeeDetails != null && $scope.employeeDetails.id == id) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    };

                    $scope.timeOffRequest = function() {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/hrm/employee/templates/timeOffRequestModal.jsp',
                            resolve: {
                                empData: function () {
                                    return {"loginEmp": $scope.loginEmp, "timeOffTypes": $scope.timeOffTypes, "constants":$scope.constants};
                                }
                            },
                            controller: ['$scope', '$modalInstance', 'employeeDetailsFactory', 'empData', function ($scope, $modalInstance, employeeDetailsFactory, empData) {
                                var getTimeOffCount = function() {
                                    var startDate,endDate;

                                    if($scope.timeoff && $scope.timeoff.startDate && $scope.timeoff.endDate){
                                        startDate = $scope.timeoff.startDate.split('/').join('-'),
                                        endDate = $scope.timeoff.endDate.split('/').join('-');

                                        if($scope.startEndValidator()){
                                            employeeDetailsFactory.timeOffCount(startDate,endDate).then (
                                                function(response) {
                                                    $scope.timeoff.numOfDays  = response;
                                                },

                                                function (error) {

                                                }
                                            );
                                        }
                                    }

                                };

                                $scope.timeOffTypes = empData.timeOffTypes;
                                $scope.constants = empData.constants;

                                $scope.close = function () {
                                    $modalInstance.close();
                                };

                                $scope.validateTimeOf = function(timeofForm){
                                    angular.forEach(timeofForm.$error.required, function(field) {
                                        field.$setDirty();
                                    });

                                    if(timeofForm.$valid){
                                        $scope.timeOfRequest();
                                    }
                                };

                                $scope.$watch('timeoff.startDate', function() {
                                    getTimeOffCount();
                                });

                                $scope.$watch('timeoff.endDate', function() {
                                    getTimeOffCount();
                                });

                                $scope.startEndValidator = function() {
                                    var isValid = true,
                                        startDate,endDate;

                                    if($scope.timeoff){
                                        startDate = Date.parse($scope.timeoff.startDate);
                                        endDate = Date.parse($scope.timeoff.endDate);
                                        if(startDate >= endDate){
                                            isValid = false;
                                        }
                                    }
                                    return isValid;
                                };

                                $scope.timeOfRequest = function() {
                                    $scope.timeoff.employeeId = empData.loginEmp.person.id;
                                    employeeDetailsFactory.timeOfRequest($scope.timeoff).then (
                                        function(response) {
                                            getEmpTimeOffList(true);
                                            $modalInstance.close();
                                        },

                                        function (error) {

                                        }
                                    );
                                }
                            }]
                        });

                        modalInstance.result.then(function () {
                            console.log('Modal dismissed at: ' + new Date());
                        }, function () {
                            console.log('Modal dismissed at: ' + new Date());
                        });
                    };

                    $scope.formatDate = function(dt) {
                        return dt.split(',')[0];
                    };

                    $scope.currentMonth = function(data) {
                        var employeeId = $stateParams.employeeId;
                        if(data.type === $scope.tabtype.timeoff) {
                            timeOffData = data;
                            employeeDetailsFactory.timeOffByMonthAndYear(employeeId,data.month+1,data.year).then (
                                function(timeOffList) {
                                    $scope.loading = false;
                                    $scope.timeOffList = timeOffList;
                                },

                                function (error) {
                                    $scope.loading = false;
                                }
                            );
                        }else {
                            employeeDetailsFactory.attendanceDetails(employeeId,data.month+1,data.year).then (
                                function(attendance) {
                                    $scope.loading = false;
                                    $scope.attendanceList = attendance;
                                },

                                function (error) {
                                    $scope.loading = false;
                                }
                            );
                        }

                    };

                  init();
                }
            ]
        );
    }
);