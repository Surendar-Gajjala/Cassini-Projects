define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('employeeFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getAllEmployees: function () {
                            var url = "api/hrm/employees/all";
                            return httpFactory.get(url);
                        },
                        allEmployees: function (pageable) {
                            var url = "api/hrm/employees?page={0}&size={1}".
                                format(pageable.page-1, pageable.size);
                            return httpFactory.get(url);
                        },
                        searchEmployees: function (criteria, pageable) {
                            var url = "api/hrm/employees";
                            url += "?firstName={0}&lastName={1}&jobTitle={2}&department={3}&manager={4}".
                                        format(criteria.firstName, criteria.lastName, criteria.jobTitle,
                                            criteria.department, criteria.manager);
                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        allDepartments: function () {
                            var url = "api/hrm/departments";
                            return httpFactory.get(url);
                        },
                        allStates: function () {
                            var url = "api/common/states";
                            return httpFactory.get(url);
                        },

                        allCountries: function () {
                            var url = "api/common/countries";
                            return httpFactory.get(url);
                        },
                        districts: function (id) {
                            var url = "api/common/districts/state/" + id;
                            return httpFactory.get(url);
                        },
                        saveEmployee: function(employee) {
                            var url = "api/hrm/employees";
                            return httpFactory.post(url,employee);
                        }
                    }
                }
            ]
        );
    }
);