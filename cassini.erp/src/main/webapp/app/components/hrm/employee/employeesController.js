define(['app/app.modules', 'app/components/hrm/employee/employeeFactory', 'app/shared/constants/constants'],
    function ($app) {
        $app.controller('EmployeesController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'employeeFactory', 'CONSTANTS',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, employeeFactory, CONSTANTS) {
                    var templateList = {
                            'allEmployeeGrid': 'app/components/hrm/employee/templates/allEmployeesGrid.jsp?bust=' + (new Date()).getTime(),
                            'newEmployee': 'app/components/hrm/employee/templates/newEmployeeView.jsp?bust=' + (new Date()).getTime()
                        },
                        employee = {
                            "personType": {"typeId": 1},
                            "title": "",
                            employeeNumber:"",
                            "firstName": "",
                            "lastName": "",
                            "middleName": "",
                            "phoneOffice": "",
                            "phoneMobile": "",
                            "email": "",
                            "dateOfBirth": null,
                            "dateOfHire": null,
                            "manager": {"firstName": "Choose a Manager"},
                            "employeeType": {"id": 1},
                            "department": {"name": "Choose a Department"},
                            "jobTitle": "",
                            "status": "ACTIVE",
                            "picture": "",
                            "addresses": [
                                {
                                    "addressType": {typeId: 1},
                                    "addressText": "",
                                    "city": "",
                                    "state": {"name": "Choose a State"},
                                    "country": {"name": "Choose a Country"},
                                    "pincode": ""
                                }]
                        },
                        init = function () {
                            getAllEmployees();
                            getAllDepartments();
                            getAllStates();
                        },
                        getAllEmployees = function () {
                            employeeFactory.allEmployees($scope.pageable).then(
                                function (employees) {
                                    $scope.employees = employees;
                                },

                                function (error) {

                                }
                            );
                        },
                        getAllDepartments = function () {
                            employeeFactory.allDepartments().then(
                                function (departments) {
                                    $scope.departments = departments;
                                },

                                function (error) {

                                }
                            );
                        },
                        getAllStates = function () {
                            employeeFactory.allStates().then(
                                function (states) {
                                    $scope.allStates = states;
                                },

                                function (error) {

                                }
                            );
                        },
                        getDistricts = function () {
                            var stateId = $scope.employee.addresses[0].state.id;
                            employeeFactory.districts(stateId).then(
                                function (districts) {
                                    $scope.districts = districts;
                                },

                                function (error) {

                                }
                            );
                        },
                        saveNewEmployee = function () {
                            var employee = $scope.employee;
                            employee.manager = employee.manager.id;
                            employeeFactory.saveEmployee(employee).then(
                                function (response) {
                                    $scope.close();
                                },

                                function (error) {

                                }
                            );
                        },
                        validateCurrentTab = function (form) {
                            angular.forEach(form.$error.required, function (field) {
                                field.$setDirty();
                            });

                            var value = form.$valid;
                            return value;
                        };

                    $scope.$on('$viewContentLoaded', function () {

                    });

                    $rootScope.$on('$includeContentLoaded', function (event, url) {
                        if (url == templateList.allEmployeeGrid) {
                            $rootScope.setToolbarTemplate("employees-view-tb");
                        }
                    });

                    $scope.departmentValidator = function (dep) {
                        return dep.id ? true : false;
                    };

                    $scope.managerValidator = function (mngr) {
                        return mngr.id ? true : false;
                    };

                    $scope.countryValidator = function (country) {
                        return country.id ? true : false;
                    };

                    $scope.districtValidator = function (district) {
                        return district.id ? true : false;
                    };

                    $scope.stateValidator = function (state) {
                        return state.id ? true : false;
                    };


                    $rootScope.iconClass = "fa flaticon-businessman276";
                    $rootScope.viewTitle = "Employees";

                    $scope.constants = CONSTANTS;

                    $scope.employee = angular.copy(employee);

                    $scope.pageable = {
                        page: 1,
                        size: 15,
                        sort: {}
                    };

                    $scope.currentTemplate = templateList.allEmployeeGrid;
                    $scope.employees = {};
                    $scope.departments = []
                    $scope.allStates = [];
                    $scope.districts = [];
                    $scope.countries = [
                        {
                            "id": 3,
                            "name": "India"
                        }
                    ];

                    $scope.tabList = {
                        'personDetails': 'personDetails',
                        'employeeDetails': 'employeeDetails',
                        'addressDetails': 'addressDetails'
                    };
                    $scope.currentTab = $scope.tabList.personDetails;

                    $rootScope.addNewEmployee = function () {
                        $scope.currentTemplate = templateList.newEmployee;
                    };

                    $scope.close = function () {
                        $scope.currentTemplate = templateList.allEmployeeGrid;
                        $scope.employee = angular.copy(employee);
                        getAllEmployees();
                    };

                    $scope.updateTab = function (currentTab, personDetails, employeeDetails, addressDetails) {
                        if ($scope.currentTab == $scope.tabList.personDetails) {
                            if (!validateCurrentTab(personDetails)) return false;
                        } else if ($scope.currentTab == $scope.tabList.employeeDetails) {
                            if (!validateCurrentTab(employeeDetails)) return false;
                        } else {
                            if (!validateCurrentTab(addressDetails)) return false;
                        }

                        $scope.currentTab = currentTab;
                        console.log(currentTab + ' currentTab....');
                    };

                    $scope.navigateTab = function (type, personDetails, employeeDetails, addressDetails) {
                        var nextTab = $scope.tabList.personDetails,
                            currentTab = $scope.currentTab;

                        if (currentTab == $scope.tabList.personDetails) {

                            if (!validateCurrentTab(personDetails) || !$scope.validateName($scope.employee.firstName)
                                || !$scope.validateName($scope.employee.middleName) || !$scope.validateName($scope.employee.lastName)
                            || !$scope.validatePhoneNumber($scope.employee.phoneMobile) || !$scope.validatePhoneNumber($scope.employee.phoneOffice))
                                return false;
                        } else if (currentTab == $scope.tabList.employeeDetails) {
                            if (!validateCurrentTab(employeeDetails) || !$scope.validateJobTitle())
                                return false;
                        } else {
                            if (!validateCurrentTab(addressDetails) || !$scope.validateCityName())
                                return false;
                        }
                        if (type === 'NEXT') {
                            if (currentTab === $scope.tabList.personDetails) {
                                nextTab = $scope.tabList.employeeDetails;
                            } else {
                                nextTab = $scope.tabList.addressDetails;

                            }
                        } else {
                            saveNewEmployee();
                        }

                        $scope.currentTab = nextTab;
                    };

                    $scope.previous = function (type) {
                        var currentTab = $scope.currentTab;
                        if (type === 'PREVIOUS') {
                            if (currentTab === $scope.tabList.employeeDetails) {
                                var nextTab = $scope.tabList.personDetails
                                $scope.currentTab = nextTab;
                            } else if (currentTab === $scope.tabList.addressDetails) {
                                var nextTab = $scope.tabList.employeeDetails
                                $scope.currentTab = nextTab;
                            }
                        }
                    }

                    $scope.stateChange = function () {
                        //getDistricts();
                    };

                    $scope.validateEmpNum = function (empNum) {

                        var iChars = "!#%^$*+=[]{}|<>?";
                        var value = empNum;
                        var valid = true;
                        for (var i = 0; i < value.length; i++) {
                            if (iChars.indexOf(value[i]) != -1) {
                                valid = false;
                                break;
                            }
                        }
                        if (!valid) {
                            if (value == $scope.employee.employeeNumber) {
                                $rootScope.showErrorMessage("Employee Number does't have any special charachters such as(!#%^$*+=[]{}|<>?)");
                            }

                            return valid;
                        }
                        else {
                            return valid;
                        }
                    };

                    $scope.validateName = function (name) {

                        var iChars = "!#%^$*+=[]{}|<>?";
                        var value = name;
                        var valid = true;
                        for (var i = 0; i < value.length; i++) {
                            if (iChars.indexOf(value[i]) != -1) {
                                valid = false;
                                break;
                            }
                        }
                        if (!valid) {
                            if (value == $scope.employee.firstName) {
                                $rootScope.showErrorMessage("Employee FirstName does't have any special charachters such as(!#%^$*+=[]{}|<>?)");
                            }
                            else if (value == $scope.employee.middleName) {
                                $rootScope.showErrorMessage("Employee MiddleName does't have any special characters such as(!#%^$*+=[]{}|<>?)");
                            }
                            else if (value == $scope.employee.lastName) {
                                $rootScope.showErrorMessage("Employee LastName does't have any special characters such as(!#%^$*+=[]{}|<>?)");
                            }
                            return valid;
                        }
                        else {
                            return valid;
                        }
                    };

                    $scope.validateJobTitle = function () {

                        var iChars = "!#%^$*+=[]{}|<>?0123456789";
                        var value = $scope.employee.jobTitle;
                        var valid = true;
                        for (var i = 0; i < value.length; i++) {
                            if (iChars.indexOf(value[i]) != -1) {
                                valid = false;
                                break;
                            }
                        }
                        if (!valid) {
                               $rootScope.showErrorMessage("Employee JobTitle does't have any special Characters and Numbers");
                               return valid;
                        }
                        else {
                            return valid;
                        }
                    };

                    $scope.validateCityName = function () {

                        var iChars = "!#%^$*+=[]{}|<>?";
                        var value = $scope.employee.addresses[0].city;
                        var valid = true;
                        for (var i = 0; i < value.length; i++) {
                            if (iChars.indexOf(value[i]) != -1) {
                                valid = false;
                                break;
                            }
                        }
                        if (!valid) {
                            $rootScope.showErrorMessage("Enter valid City Name");
                            return valid;
                        }
                        else {
                            return valid;
                        }
                    };

                    $scope.validatePhoneNumber = function(phoneNumber){
                        var nChars = "0123456789 +-_";
                        var value = phoneNumber;
                        var valid = true;
                        for(var i = 0; i < value.length; i++){
                            if (nChars.indexOf(value[i]) == -1) {
                                valid = false;
                                break;
                            }
                        }
                        if (!valid) {
                            if(value == $scope.employee.phoneMobile){
                            $rootScope.showErrorMessage("Please Enter valid Mobile Phone Number");
                            }else if(value == $scope.employee.phoneOffice){
                                $rootScope.showErrorMessage("Please Enter valid Office Phone Number");
                            }
                            return valid;
                        }
                        else {
                            return valid;
                        }
                    }


                    $scope.pageChanged = function () {
                        getAllEmployees();
                    };
                    init();
                }
            ]
        );
    }
);