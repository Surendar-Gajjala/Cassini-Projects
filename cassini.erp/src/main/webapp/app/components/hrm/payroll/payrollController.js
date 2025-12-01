define(['app/app.modules', 'app/components/hrm/payroll/payrollFactory'],
    function ($app) {
        $app.controller('PayrollController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'payrollFactory', '$modal',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, payrollFactory, $modal) {

                    $rootScope.currentViewName = null;
                    var templateList = {
                            'employeePay': 'app/components/hrm/payroll/templates/employeePay.jsp',
                            'employeePayRead': 'app/components/hrm/payroll/templates/employeePayRead.jsp',
                            'employeePayEdit': 'app/components/hrm/payroll/templates/employeePayEdit.jsp'
                        },
                        staticMonths = [
                            [{name: 'JAN', index: 1}, {name: 'FEB', index: 2}, {name: 'MAR', index: 3}],
                            [{name: 'APR', index: 4}, {name: 'MAY', index: 5}, {name: 'JUN', index: 6}],
                            [{name: 'JUL', index: 7}, {name: 'AUG', index: 8}, {name: 'SEP', index: 9}],
                            [{name: 'OCT', index: 10}, {name: 'NOV', index: 11}, {name: 'DEC', index: 12}]
                        ],
                        newPayUpdatedList = [],
                        newPayRollObj = {},
                        selectedMonth = 1,
                        numOfWorkingDays = {},
                        init = function () {
                            payRollDetails();
                            employeePayDetails();
                            //attendanceHelper();
                        },

                        payRollDetails = function () {
                            payrollFactory.payRollDetails($scope.currentYear).then(
                                function (data) {
                                    $scope.payRollList = data;
                                },

                                function (error) {

                                }
                            );
                        },
                        payRollByMonth = function (month) {
                            payrollFactory.payRollByMonth(month, $scope.currentYear).then(
                                function (data) {
                                    constructNewPayRoll(data);
                                },

                                function (error) {

                                }
                            );
                        },
                    /*attendanceHelper = function(){
                     payrollFactory.attendanceHelper().then (
                     function(data) {
                     numOfWorkingDays = data.numOfDaysInMonth - (data.numOfSundays + 2);
                     },

                     function (error) {

                     }
                     );
                     },*/
                        employeePayDetails = function () {
                            payrollFactory.employeePayDetails().then(
                                function (data) {
                                    constructPayRoll(data);
                                },

                                function (error) {

                                }
                            );
                        },
                        updateEmployeePay = function (payroll) {
                            payrollFactory.updateEmployeePay(payroll).then(
                                function (data) {
                                    updateTotal();
                                },

                                function (error) {

                                }
                            );
                        },
                        updateNewEmployeePay = function (payroll) {
                            for (var i = newPayUpdatedList.length - 1; i >= 0; i--) {
                                if (payroll.emp.id === newPayUpdatedList[i].emp.id) {
                                    newPayUpdatedList.splice(i, 1);
                                    break;
                                }
                            }

                            newPayUpdatedList.push(payroll);
                            updateNewPayRollTotal();
                        },
                        newPayroll = function (month) {
                            payrollFactory.newPayroll(month).then(
                                function (data) {
                                    constructNewPayRoll(data);
                                },

                                function (error) {

                                }
                            );
                        },
                        constructPayRoll = function (data) {
                            var payRoll = {};

                            payRoll.salaryList = data[0].data;
                            payRoll.allowanceTypes = data[1].data;
                            payRoll.deductionTypes = data[2].data;

                            angular.forEach(payRoll.salaryList, function (value) {

                                    if (value.emp.id != 1) {
                                        value.empSal.newSalary = value.empSal.salary;
                                        value.empSal.newBonus = value.empSal.bonus;
                                    }
                                }
                            )

                            $scope.payRollObj = payRoll;
                            updateTotal();
                        },
                        calculateSalary = function (list) {
                            var salaryList = list,
                                perDaySalary = 0;

                            angular.forEach(salaryList, function (value, key) {
                                if (value.empSalHist != null) {
                                    perDaySalary = value.empSalHist.salary / numOfWorkingDays;

                                    if (value.attendence < numOfWorkingDays) {
                                        value.empSalHist.salary = (numOfWorkingDays - value.attendence) * perDaySalary;
                                    }
                                }

                            });

                            return salaryList;
                        },
                        constructNewPayRoll = function (data) {
                            var payRoll = {};
                            newPayRollObj = data;
                            payRoll.salaryList = data.empPayrollPayLst;//calculateSalary(data.empPayrollPayLst);
                            payRoll.allowanceTypes = $scope.payRollObj.allowanceTypes;
                            payRoll.deductionTypes = $scope.payRollObj.deductionTypes;

                            $scope.newPayrollObj = payRoll;
                            updateNewPayRollTotal();
                        },
                        getNetProfit = function (payroll) {
                            var netProfit,
                                allowances = 0,
                                deductions = 0,
                                salary = (payroll.empSal === null) ? 0 : parseInt(payroll.empSal.bonus) + parseInt(payroll.empSal.salary);

                            angular.forEach(payroll.empDeductions, function (value, key) {
                                deductions += parseInt(value.deductionValue);
                            })

                            angular.forEach(payroll.empAllowances, function (value, key) {
                                allowances += parseInt(value.allowanceValue);
                            })

                            netProfit = (salary + allowances) - deductions;

                            return netProfit;
                        },
                        getNewPayrollNetProfit = function (payroll) {
                            var netProfit,
                                allowances = 0,
                                deductions = 0,
                                salary = (payroll.empSalHist === null) ? 0 : parseInt(payroll.empSalHist.bonus) + parseInt(payroll.empSalHist.salary);

                            angular.forEach(payroll.empDeductionsHist, function (value, key) {
                                deductions += parseInt(value.deductionAmt);
                            })

                            angular.forEach(payroll.empAllowancesHist, function (value, key) {
                                allowances += parseInt(value.allowancePaid);
                            })

                            netProfit = (salary + allowances) - deductions;

                            return netProfit;
                        },
                        updateTotal = function () {
                            var salary = 0,
                                bonus = 0,
                                netProfit = 0;

                            angular.forEach($scope.payRollObj.salaryList, function (value, key) {
                                $scope.payRollObj.salaryList[key].netProfit = getNetProfit(value);
                                salary += (value.empSal) ? parseInt(value.empSal.salary) : 0;
                                bonus += (value.empSal) ? parseInt(value.empSal.bonus) : 0;
                                netProfit += $scope.payRollObj.salaryList[key].netProfit;
                            })

                            for (var k = 0; k < $scope.payRollObj.allowanceTypes.length; k++) {
                                $scope.payRollObj.allowanceTypes[k].total = 0;
                            }

                            for (var k = 0; k < $scope.payRollObj.deductionTypes.length; k++) {
                                $scope.payRollObj.deductionTypes[k].total = 0;
                            }

                            for (var i = 0; i < $scope.payRollObj.salaryList.length; i++) {
                                var currentAllowances = $scope.payRollObj.salaryList[i].empAllowances;
                                for (var j = 0; j < currentAllowances.length; j++) {
                                    for (var k = 0; k < $scope.payRollObj.allowanceTypes.length; k++) {
                                        if (currentAllowances[j].employeeAllowanceId.allowanceType === $scope.payRollObj.allowanceTypes[k].id) {
                                            $scope.payRollObj.allowanceTypes[k].total += parseInt(currentAllowances[j].allowanceValue);
                                            break;
                                        }
                                    }
                                }
                            }

                            for (var i = 0; i < $scope.payRollObj.salaryList.length; i++) {
                                var currentDeductions = $scope.payRollObj.salaryList[i].empDeductions;
                                for (var j = 0; j < currentDeductions.length; j++) {
                                    for (var k = 0; k < $scope.payRollObj.deductionTypes.length; k++) {
                                        if (currentDeductions[j].employeeDeductionId.deductionType === $scope.payRollObj.deductionTypes[k].id) {
                                            $scope.payRollObj.deductionTypes[k].total += parseInt(currentDeductions[j].deductionValue);
                                            break;
                                        }
                                    }
                                }
                            }

                            $scope.payRollObj.totalSalary = salary;
                            $scope.payRollObj.totalbonus = bonus;
                            $scope.payRollObj.totalNetProfit = netProfit;
                        },
                        updateNewPayRollTotal = function () {
                            var salary = 0,
                                bonus = 0,
                                netProfit = 0;

                            angular.forEach($scope.newPayrollObj.salaryList, function (value, key) {
                                $scope.newPayrollObj.salaryList[key].netProfit = getNewPayrollNetProfit(value);
                                salary += (value.empSalHist) ? parseInt(value.empSalHist.salary) : 0;
                                bonus += (value.empSalHist) ? parseInt(value.empSalHist.bonus) : 0;
                                netProfit += $scope.newPayrollObj.salaryList[key].netProfit;
                            })

                            for (var k = 0; k < $scope.newPayrollObj.allowanceTypes.length; k++) {
                                $scope.newPayrollObj.allowanceTypes[k].total = 0;
                            }

                            for (var k = 0; k < $scope.newPayrollObj.deductionTypes.length; k++) {
                                $scope.newPayrollObj.deductionTypes[k].total = 0;
                            }

                            for (var i = 0; i < $scope.newPayrollObj.salaryList.length; i++) {
                                var currentAllowances = $scope.newPayrollObj.salaryList[i].empAllowancesHist;
                                if (currentAllowances != null) {
                                    for (var j = 0; j < currentAllowances.length; j++) {
                                        for (var k = 0; k < $scope.newPayrollObj.allowanceTypes.length; k++) {
                                            if (currentAllowances[j].allowanceHistoryId.allowanceType === $scope.newPayrollObj.allowanceTypes[k].id) {
                                                $scope.newPayrollObj.allowanceTypes[k].total += parseInt(currentAllowances[j].allowancePaid);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            for (var i = 0; i < $scope.newPayrollObj.salaryList.length; i++) {
                                var currentDeductions = $scope.newPayrollObj.salaryList[i].empDeductionsHist;
                                if (currentDeductions != null) {
                                    for (var j = 0; j < currentDeductions.length; j++) {
                                        for (var k = 0; k < $scope.newPayrollObj.deductionTypes.length; k++) {
                                            if (currentDeductions[j].deductionHistoryId.deductionType === $scope.newPayrollObj.deductionTypes[k].id) {
                                                $scope.newPayrollObj.deductionTypes[k].total += parseInt(currentDeductions[j].deductionAmt);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }


                            $scope.newPayrollObj.totalSalary = salary;
                            $scope.newPayrollObj.totalbonus = bonus;
                            $scope.newPayrollObj.totalNetProfit = netProfit;
                        };

                    $rootScope.iconClass = "fa flaticon-finance-and-business4";
                    $rootScope.viewTitle = "Payroll";

                    $scope.currentYear = new Date().getFullYear();
                    $scope.payRollObj = {};
                    $scope.newPayrollObj = {};

                    $scope.payRollList = [];
                    $scope.currentTemplate = templateList.employeePay;

                    $scope.months = angular.copy(staticMonths);

                    $scope.showEditMode = function (payroll) {
                        payroll.editMode = true;
                    };

                    $scope.isPayrollRun = function (month) {
                        var isPayrollRun = false,
                            isCurrentMonth = (month === new Date().getMonth());

                        if (isCurrentMonth) {
                            angular.forEach($scope.payRollList, function (value, key) {
                                if (value.month === month) {
                                    if (value.totalAmount > 0) {
                                        isPayrollRun = true;
                                        return false;
                                    }
                                    ;
                                }
                            });
                        }

                        return isPayrollRun;
                    }

                    $scope.isMonthCurrent = function (index) {
                        return new Date().getMonth() == index;
                    };

                    $scope.isMonthBeforeCurrentMonth = function (index) {
                        return (index < new Date().getMonth());
                    };

                    $scope.isYearCurrent = function () {
                        return ($scope.currentYear == new Date().getFullYear());
                    };

                    $scope.nextYear = function () {
                        $scope.currentYear++;
                        $scope.months = [];
                        $scope.months = angular.copy(staticMonths);
                        payRollDetails();
                    };

                    $scope.previousYear = function () {
                        $scope.currentYear--;
                        $scope.months = angular.copy(staticMonths);
                        payRollDetails();
                    };

                    $scope.acceptChanges = function (payroll) {
                        if (payroll.empSal.newSalary == "" || payroll.empSal.newSalary == undefined) {
                            payroll.empSal.newSalary = 0;
                        }
                        if (payroll.empSal.newBonus == "" || payroll.empSal.newSalary == undefined) {
                            payroll.empSal.newBonus = 0;
                        }
                        payroll.empSal.salary = payroll.empSal.newSalary;
                        payroll.empSal.bonus = payroll.empSal.newBonus;
                        $timeout(function () {
                            payroll.editMode = false;
                        }, 500);

                        updateEmployeePay(payroll);
                    };

                    $scope.acceptNewPayChanges = function (payroll) {
                        $timeout(function () {
                            payroll.editMode = false;
                        }, 500);

                        updateNewEmployeePay(payroll);
                    };

                    $scope.hideEditMode = function (payroll) {
                        //payroll.editMode = true;

                        payroll.empSal.newSalary = payroll.empSal.salary;
                        payroll.empSal.newBonus = payroll.empSal.bonus;
                        $timeout(function () {
                            payroll.editMode = false;
                        }, 500);
                    };

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('employeePayEdit-view-tb')
                    });

                    $scope.viewPayRoll = function (month) {
                        //month = 9;
                        $rootScope.currentViewName = 'employee-pay-read';
                        $scope.currentTemplate = templateList.employeePayRead;
                        payRollByMonth(month);
                    };

                    $scope.runPayRoll = function (month) {
                        selectedMonth = month;
                        $scope.currentTemplate = templateList.employeePayEdit;
                        $rootScope.currentViewName = 'employee-pay-edit';
                        newPayroll(month);
                    };

                    $scope.getMonthTotalSalary = function (month) {
                        var totalSalary = 0;
                        angular.forEach($scope.payRollList, function (value, key) {
                            if (value.month === month) {
                                totalSalary = value.totalAmount;
                                return false;
                            }
                        });

                        return totalSalary;

                    };

                    $rootScope.navigate = function () {
                        $scope.currentTemplate = templateList.employeePay;
                        $rootScope.currentViewName = 'employee-pay';
                    };

                    $rootScope.runNewPayroll = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/hrm/payroll/templates/payRollConfirmModal.jsp',
                            resolve: {
                                payrollData: function () {
                                    return {
                                        "month": selectedMonth,
                                        "year": $scope.currentYear,
                                        "totalSalary": $scope.newPayrollObj.totalSalary,
                                        "payrollList": newPayUpdatedList
                                    };
                                }
                            },
                            controller: ['$scope', '$modalInstance', 'payrollFactory', 'payrollData', function ($scope, $modalInstance, payrollFactory, payrollData) {

                                $scope.close = function () {
                                    $modalInstance.close();
                                };

                                $scope.runPayRollRequest = function () {
                                    newPayRollObj.month = payrollData.month;
                                    newPayRollObj.year = payrollData.year;
                                    newPayRollObj.totalAmount = payrollData.totalSalary;
                                    newPayRollObj.empPayrollPayLst = payrollData.payrollList;
                                    payrollFactory.runNewPayRoll(newPayRollObj).then(
                                        function (response) {
                                            newPayUpdatedList = [];
                                            $modalInstance.close();

                                            $rootScope.navigate();
                                            init();
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

                    init();
                }
            ]
        )
        ;
    }
)
;




