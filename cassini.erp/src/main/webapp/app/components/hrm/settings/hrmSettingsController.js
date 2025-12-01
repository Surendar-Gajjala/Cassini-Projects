define(['app/app.modules',
        'app/components/hrm/settings/hrmSettingsFactory',
        'app/shared/directives/commonDirectives',
        'app/shared/constants/constants'],
    function($app) {
        $app.controller('HRMSettingsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'hrmSettingsFactory', 'CONSTANTS',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies, hrmSettingsFactory, CONSTANTS) {
                    var newEmployeeType = {
                            "description": "",
                            "id": "",
                            "name": "",
                            "editMode": true
                        },
                        newDepartmentType = {
                            "description": "",
                            "id": "",
                            "name": "",
                            "editMode": true
                        },
                        newAllowanceType = {
                            "description": "",
                            "name": "",
                            "typeCode": "",
                            "id": "",
                            "editMode": true
                        },
                        newDeductionType = {
                            "description": "",
                            "name": "",
                            "typeCode": "",
                            "id": "",
                            "editMode": true
                        },
                        newTimeoffType = {
                            "description": "",
                            "id": "",
                            "name": "",
                            "typeCode": "",
                            "editMode": true
                        },
                       /* newLoanType = {
                            "id": "",
                            "typeCode": "",
                            "name": "",
                            "description": "",
                            "editMode": true
                        },*/
                        newHoliday = {
                            "id": "",
                            "name":"",
                            "description":"",
                            "date":"",
                            "editMode": true
                        },
                        init = function() {
                            getEmployeeTypes();
                            getDepartments();
                            getAllowances();
                            getDeductions();
                            getTimeOffTypes();
                            getHolidays();
                        },
                        getEmployeeTypes = function(){
                            hrmSettingsFactory.employeeTypes().then (
                                function(employeeTypes) {
                                    $scope.employeeTypes = employeeTypes;
                                },

                                function (error) {

                                }
                            );
                        },
                        getDepartments = function(){
                            hrmSettingsFactory.departments().then (
                                function(departments) {
                                    $scope.departments = departments;
                                },

                                function (error) {

                                }
                            );
                        },
                        getAllowances = function(){
                            hrmSettingsFactory.allowances().then (
                                function(allowances) {
                                    $scope.allowances = allowances;
                                },

                                function (error) {

                                }
                            );
                        },
                        getDeductions = function(){
                            hrmSettingsFactory.deductions().then (
                                function(deductions) {
                                    $scope.deductions = deductions;
                                },

                                function (error) {

                                }
                            );
                        },
                        getTimeOffTypes = function(){
                            hrmSettingsFactory.timeofftypes().then (
                                function(timeofftypes) {
                                    $scope.timeofftypes = timeofftypes;
                                },

                                function (error) {

                                }
                            );
                        },
                        getHolidays = function(){
                            hrmSettingsFactory.holidays().then (
                                function(holidays) {
                                    $scope.holidays = holidays;
                                },

                                function (error) {

                                }
                            );
                        },
                        updateChanges = function(currentObj,settingType) {
                            hrmSettingsFactory.updateSettings(currentObj,settingType).then (
                                function(response) {
                                    currentObj.id = response.id;
                                },
                                function (error) {
                                }
                            );
                        },
                        deleteChanges = function(currentObj,settingType) {

                                hrmSettingsFactory.deleteSettings(currentObj,settingType).then (
                                    function(response) {
                                    },
                                    function (error) {
                                    }
                                );
                        };

                    $rootScope.iconClass = "fa fa-wrench";
                    $rootScope.viewTitle = "HRM Settings";

                    $scope.employeeTypes = [];
                    $scope.departments = [];
                    $scope.allowances = [];
                    $scope.deductions = [];
                    $scope.timeofftypes = [];
                    $scope.holidays = [];
                    $scope.constants = CONSTANTS;

                    $scope.settingTypes = {
                        'employee':'EMPLOYEE',
                        'department':'DEPARTMENT',
                        'allowance': 'ALLOWANCE',
                        'deduction': 'DEDUCTION',
                        'timeOff': 'TIMEOFF',
                        'holiday':'HOLIDAY'
                    };

                    $scope.showEditMode = function (selectedObj) {
                        selectedObj.editMode = true;
                    };

                    $scope.hideEditMode = function (settingobj,typeName) {
                        if($scope.settingTypes.employee === typeName) {
                            settingobj.newName = settingobj.name;
                            settingobj.newDescription = settingobj.description;
                            settingobj.editMode = false;
                        }
                        else if( $scope.settingTypes.department === typeName) {
                            settingobj.newName = settingobj.name;
                            settingobj.newDescription = settingobj.description;
                            settingobj.editMode = false;
                        }
                        else if( $scope.settingTypes.allowance === typeName) {
                            settingobj.newName = settingobj.name;
                            settingobj.newDescription = settingobj.description;
                            settingobj.editMode = false;
                        }
                        else if( $scope.settingTypes.deduction === typeName) {
                            settingobj.newName = settingobj.name;
                            settingobj.newDescription = settingobj.description;
                            settingobj.editMode = false;
                        }
                        else if( $scope.settingTypes.timeOff === typeName) {
                            settingobj.newName = settingobj.name;
                            settingobj.newDescription = settingobj.description;
                            settingobj.editMode = false;
                        }
                        else if( $scope.settingTypes.holiday === typeName) {
                            settingobj.newName = settingobj.name;
                            settingobj.newHdate = settingobj.date;
                            settingobj.newDescription = settingobj.description;
                            settingobj.editMode = false;
                        }
                    };

                    $scope.acceptChanges = function (selectedObj,settingType) {
                        selectedObj.editMode = false;

                        $timeout(function() {
                            selectedObj.showValues = true;
                        }, 500);

                        $scope.saveSettingType(selectedObj,settingType);
                    };

                    $scope.saveSettingType = function(selectedObj,settingType) {
                        selectedObj.name = selectedObj.newName;
                        selectedObj.description = selectedObj.newDescription;
                        if(settingType == "HOLIDAY"){
                            selectedObj.name = selectedObj.newName;
                            selectedObj.description = selectedObj.newDescription;
                            selectedObj.date = selectedObj.newHdate;
                        }
                        hrmSettingsFactory.saveSettingType(settingType).then(
                            function(data) {
                                selectedObj = data;
                            },
                            function(error) {
                                console.error(error);
                            }
                        );
                    };

                    $scope.addEmployeeType = function($event,settingType) {
                        $event.stopPropagation();
                        $event.preventDefault();
                        var temp = {};
                        if(settingType === $scope.settingTypes.employee) {
                            temp = angular.copy(newEmployeeType);
                            $scope.employeeTypes.push(temp);
                        }else if(settingType === $scope.settingTypes.department) {
                            temp = angular.copy(newDepartmentType);
                            $scope.departments.push(temp);
                        }else if(settingType === $scope.settingTypes.allowance) {
                            temp = angular.copy(newAllowanceType);
                            $scope.allowances.push(temp);
                        }else if(settingType === $scope.settingTypes.deduction) {
                            temp = angular.copy(newDeductionType);
                            $scope.deductions.push(temp);
                        }else if(settingType === $scope.settingTypes.timeOff) {
                            temp = angular.copy(newTimeoffType);
                            $scope.timeofftypes.push(temp);
                        }
                        else if(settingType === $scope.settingTypes.holiday) {
                            temp = angular.copy(newHoliday);
                            $scope.holidays.push(temp);
                        }
                    };

                    $scope.removeItem = function(index, selectedObj,settingType) {

                        if(settingType === $scope.settingTypes.employee) {
                            $scope.employeeTypes.splice(index,1);
                        }else if(settingType === $scope.settingTypes.department) {
                            $scope.departments.splice(index,1);
                        }else if(settingType === $scope.settingTypes.allowance) {
                            $scope.allowances.splice(index,1);
                        }else if(settingType === $scope.settingTypes.deduction) {
                            $scope.deductions.splice(index,1);
                        }else if(settingType === $scope.settingTypes.timeOff) {
                            $scope.timeofftypes.splice(index,1);
                        }
                        else if(settingType === $scope.settingTypes.holiday) {
                            $scope.holidays.splice(index,1);
                        }
                        deleteChanges(selectedObj,settingType);
                    };
                    init();
                }
            ]
        );
    }
);

