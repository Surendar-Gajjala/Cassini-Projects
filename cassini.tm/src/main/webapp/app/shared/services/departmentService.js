define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('DepartmentService', DepartmentService);

        function DepartmentService(httpFactory) {
            return {
                createDepartment: createDepartment,
                getPagedDepartments: getPagedDepartments,
                createPersonsByDepartment: createPersonsByDepartment,
                getAllDepartments: getAllDepartments,
                getDepartmentById: getDepartmentById,
                updateDepartment: updateDepartment,
                deleteDepartment: deleteDepartment,
                freeTextSearch: freeTextSearch,
                getMultiple: getMultiple,
                getPersonsByDepartment: getPersonsByDepartment,
                deleteDepartmentPerson: deleteDepartmentPerson,
                getMultiplePersons: getMultiplePersons
            };

            function createDepartment(department) {
                var url = "api/departments";
                return httpFactory.post(url, department);
            }

            function getPagedDepartments(pageable) {
                var url = "api/departments";
                    url += "?page={0}&size={1}&sort={2}:{3}".
                 format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllDepartments() {
                var url = "api/departments";
                /*url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);*/
                return httpFactory.get(url);
            }

            function getDepartmentById(deptId) {
                var url = "api/departments/" + deptId;
                return httpFactory.get(url);
            }

            function updateDepartment(departments) {
                var url = "api/departments/" + departments.id;
                return httpFactory.put(url, departments);
            }

            function deleteDepartment(departmentId) {
                var url = "api/departments/" + departmentId;
                return httpFactory.delete(url);
            }

            function freeTextSearch(criteria, pageable) {
                var url = "api/departments/freesearch";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function getPersonsByDepartment(departmentId) {
                var url = "api/departments/" + departmentId + "/persons";
                return httpFactory.get(url);
            }

            function createPersonsByDepartment(departmentId, departmentPerson) {
                var url = "api/departments/" + departmentId + "/persons";
                return httpFactory.post(url, departmentPerson);
            }

            function deleteDepartmentPerson(departmentId, personId){
                var url = "api/departments/" + departmentId + "/persons/" + personId;
                return httpFactory.delete(url);
            }

            function getMultiplePersons(ids){
                var url = "api/departments/multiple/persons/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMultiple(ids){
                var url = "api/departments/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

        }
    }
);