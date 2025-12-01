define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('TaskService', TaskService);

        function TaskService(httpFactory) {
            return {
                createProjectTask: createProjectTask,
                updateProjectTask: updateProjectTask,
                getProjectTask: getProjectTask,
                getProjectTasks: getProjectTasks,
                assignTaskTo: assignTaskTo,
                getListTasks: getListTasks,
                getTaskAssignedTo: getTaskAssignedTo,
                getTasksByAssignedDate: getTasksByAssignedDate,
                deleteProjectTask: deleteProjectTask,
                getAllTasks: getAllTasks,
                freeTextSearch: freeTextSearch,
                getLocations: getLocations,
                getTaskHistory: getTaskHistory,
                getTasks: getTasks,
                getTaskImages: getTaskImages,
                addTaskImage: addTaskImage,
                getReasons: getReasons,
                createPendingReason: createPendingReason,
                getPendingReasonById: getPendingReasonById,
                getDepartmentTasksByPersonIds:getDepartmentTasksByPersonIds,
                getTasksByPersonAndDate:getTasksByPersonAndDate,
                getAllTaskStats: getAllTaskStats,
                getTasksByLocationAndStatus: getTasksByLocationAndStatus
            };


            function getTasksByPersonAndDate(data){
                var url = "api/projects/null/tasks/print";
               url += "?assignedTo={0}&assignedDate={1}:{1}".format(data.name.id, data.date);
                return httpFactory.get(url/*,data.name.id, data.date*/);

            }

            function getDepartmentTasksByPersonIds(ids) {
                var url = "api/projects/null/tasks/persons/["+ids +"]";
                return httpFactory.get(url);
            }

            function getPendingReasonById(id) {
                var url = "api/projects/null/tasks/pendingreason/" + id;
                return httpFactory.get(url);
            }

            function getReasons() {
                var url = "api/projects/null/tasks/pendingreason/reasons";
                return httpFactory.get(url);
            }

            function createPendingReason(pendingReason) {
                var url = "api/projects/null/tasks/pendingreason";
                return httpFactory.post(url, pendingReason);
            }

            function createProjectTask(projectId, projectTask) {
                var url = "api/projects/" + projectId + "/tasks";
                return httpFactory.post(url, projectTask);
            }

            function updateProjectTask(projectId, projectTask) {
                var url = "api/projects/" + projectId + "/tasks/" + projectTask.id;
                return httpFactory.put(url, projectTask);
            }

            function getProjectTasks(projectId, pageable) {
                var url = "api/projects/" + projectId + "/tasks";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllTasks(pageable, criteria) {
                var url = "api/projects/null/tasks/pageable";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&project={0}&searchQuery={1}&status={2}&name={3}&assignedDate={4}:{4}&shift={5}&workLocation={6}&assignedTo={7}&verifiedBy={8}&approvedBy={9}".
                        format(criteria.project, criteria.searchQuery, criteria.status, criteria.name,
                        criteria.assignedDate, criteria.shift, criteria.workLocation, criteria.assignedTo,
                        criteria.verifiedBy, criteria.approvedBy);
                return httpFactory.get(url);
            }

            function getListTasks(criteria) {
                var url = "api/projects/null/tasks/projecttasks";
                url += "?project={0}&searchQuery={1}&status={2}&name={3}&assignedDate={4}:{4}&shift={5}&workLocation={6}&assignedTo={7}&verifiedBy={8}&approvedBy={9}".
                    format(criteria.project, criteria.searchQuery, criteria.status, criteria.name,
                    criteria.assignedDate, criteria.shift, criteria.workLocation, criteria.assignedTo,
                    criteria.verifiedBy, criteria.approvedBy);
                return httpFactory.get(url);
            }

            function getProjectTask(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId;
                return httpFactory.get(url);
            }

            function assignTaskTo(projectId, taskId, personId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/assignedTo/" + personId;
                return httpFactory.put(url);
            }

            function getTaskAssignedTo(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/assignedTo";
                return httpFactory.get(url);
            }

            function deleteProjectTask(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId;
                return httpFactory.delete(url);
            }


            function freeTextSearch(criteria, pageable) {
                var url = "api/projects/" + criteria.project + "/tasks/freesearch?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&project={1}".
                    format(criteria.searchQuery, criteria.project);
                return httpFactory.get(url);
            }

            function getLocations() {
                var url = "api/projects/null/tasks/locations";
                return httpFactory.get(url);
            }

            function getTasksByAssignedDate(criteria, pageable) {
                if (criteria.project == undefined) {
                    var url = "api/projects/null/tasks/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                } else {
                    var url = "api/projects/" + criteria.project + "/tasks/pageable?page={0}&size={1}&sort={2}:{3}".
                            format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                }

                url += "&assignedDate={0}:{0}&project={1}&status={2}".
                    format(criteria.assignedDate, criteria.project, criteria.status);

                return httpFactory.get(url);
            }

            function getTasks(criteria, pageable) {
                var url = "api/projects/null/tasks/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&assignedDate={0}:{0}&project={1}&status={2}&name={3}&description={4}&assignedTo={5}&verifiedBy={6}&approvedBy={7}".
                    format(criteria.assignedDate, criteria.project, criteria.status, criteria.name, criteria.description,
                    criteria.assignedTo, criteria.verifiedBy, criteria.approvedBy);
                return httpFactory.get(url);
            }

            function getTaskHistory(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/history";
                return httpFactory.get(url);
            }

            function getTaskImages(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/images";
                return httpFactory.get(url);
            }

            function addTaskImage(projectId, taskId, image) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/images";
                return httpFactory.post(url, image);
            }

            function getAllTaskStats(projectId) {
                var url = "api/projects/" + projectId + "/tasks/stats";
                return httpFactory.get(url);
            }

            function getTasksByLocationAndStatus(location, statuses) {
                var url = "api/projects/1/tasks/filter?location=" + location + "&status=" + statuses;
                return httpFactory.get(url);
            }
        }
    }
);