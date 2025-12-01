define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('hrmSettingsFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    var settingTypes = {
                        'employee':'EMPLOYEE',
                        'department':'DEPARTMENT',
                        'allowance': 'ALLOWANCE',
                        'deduction': 'DEDUCTION',
                        'timeOff': 'TIMEOFF',
                        'loan':'LOAN',
                        'holiday':'HOLIDAY'
                    }
              /*      getSettingsURL = function(settingType){
                        var url = '';
                        if(settingType === settingTypes.employee) {
                            url = "api/hrm/employeetype";
                        }else if(settingType === settingTypes.department){
                            url = "api/hrm/departments";
                        }else if(settingType === settingTypes.allowance){
                            url = "api/hrm/allowancetypes";
                        }else if(settingType === settingTypes.deduction){
                            url = "api/hrm/deductiontypes";
                        }else if(settingType === settingTypes.timeOff) {
                            url = "api/hrm/timeofftype";
                        }/!*else if(settingType === settingTypes.loan){
                            url = "api/hrm/loantypes";
                        }*!/else if(settingType === settingTypes.holiday){
                            url = "api/hrm/holidays";
                        }

                        return url;
                    };*/
                    return {
                        employeeTypes: function () {
                            var url = "api/hrm/employeetype";
                            return httpFactory.get(url);
                        },
                        departments: function() {
                            var url = "api/hrm/departments";
                            return httpFactory.get(url);
                        },
                        allowances: function() {
                            var url = "api/hrm/allowancetypes";
                            return httpFactory.get(url);
                        },
                        deductions: function() {
                            var url = "api/hrm/deductiontypes";
                            return httpFactory.get(url);
                        },
                        timeofftypes: function() {
                            var url = "api/hrm/timeofftype";
                            return httpFactory.get(url);
                        },
                        /*loanTypes: function() {
                            var url = "api/hrm/loantypes";
                            return httpFactory.get(url);
                        },*/
                        holidays: function() {
                            var url = "api/hrm/holidays";
                            return httpFactory.get(url);
                        },
                        updateSettings: function(selectedObj, settingTypes) {
                            var params = (selectedObj.id === "") ? "" : ("/" + selectedObj.id),
                                url = getSettingsURL(settingTypes) + params,
                                requestType = (selectedObj.id === "") ? httpFactory.post(url,selectedObj) : httpFactory.put(url,selectedObj);
                            return requestType;
                        },
                        deleteSettings: function(selectedObj,type){
                            var url = getSettingsURL(type) + "/"+selectedObj.id;
                            return  httpFactory.delete(url);
                        },




                        saveSettingType : function(settingType){
                            if(settingType === settingTypes.employee) {
                                url = "api/hrm/employeetype";
                                return httpFactory.get(url);
                            }
                            else if(settingType === settingTypes.department){
                                url = "api/hrm/departments";
                                return httpFactory.get(url);
                            }else if(settingType === settingTypes.allowance){
                                url = "api/hrm/allowancetypes";
                                return httpFactory.get(url);
                            }else if(settingType === settingTypes.deduction){
                                url = "api/hrm/deductiontypes";
                                return httpFactory.get(url);
                            }else if(settingType === settingTypes.timeOff) {
                                url = "api/hrm/timeofftype";
                                return httpFactory.get(url);
                            }else if(settingType === settingTypes.loan){
                                url = "api/hrm/loantypes";
                                return httpFactory.get(url);
                            }else if(settingType === settingTypes.holiday){
                                url = "api/hrm/holidays";
                                return httpFactory.get(url);
                            }
                        }
                    }
                }
            ]
        );
    }
);