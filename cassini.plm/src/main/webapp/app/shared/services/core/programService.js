/**
 * Created by swapna on 1/4/18.
 */

define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ProgramService', ProgramService);

        function ProgramService(httpFactory) {
            return {

                createProgram: createProgram,
                getAllPrograms: getAllPrograms,
                getProgram: getProgram,
                updateProgram: updateProgram,
                deleteProgram: deleteProgram,
                getPrograms: getPrograms,
                getProgramCounts: getProgramCounts,
                getProgramReferences: getProgramReferences,
                createProgramResource: createProgramResource,
                createProgramResources: createProgramResources,
                getProgramResource: getProgramResource,
                updateProgramResource: updateProgramResource,
                deleteProgramResource: deleteProgramResource,
                getProgramResources: getProgramResources,
                getProgramPercentageComplete: getProgramPercentageComplete,
                createProgramProjects: createProgramProjects,
                createProgramProject: createProgramProject,
                getProgramProject: getProgramProject,
                updateProgramProject: updateProgramProject,
                deleteProgramProject: deleteProgramProject,
                getProgramProjects: getProgramProjects,
                attachProgramWorkflow: attachProgramWorkflow,
                sendTasksNotification: sendTasksNotification,
                getProgramManagers: getProgramManagers,
                getProgramProjectManagers: getProgramProjectManagers,
                getDrillDownProgramProjects: getDrillDownProgramProjects,
                getProgramProjectPlan: getProgramProjectPlan,
                createProgramProjectObject: createProgramProjectObject,
                updateProgramProjectObject: updateProgramProjectObject,
                deleteProgramProjectObject: deleteProgramProjectObject,
                getProgramProjectAssignedTos: getProgramProjectAssignedTos,
                getProgramProjectPlanByAssignedTo: getProgramProjectPlanByAssignedTo,
                getProgramProjectFolders: getProgramProjectFolders
            };

            function createProgram(project) {
                var url = "api/plm/programs";
                return httpFactory.post(url, project);
            }

            function getPrograms() {
                var url = "api/plm/programs";
                return httpFactory.get(url);
            }

            function getAllPrograms(pageable, filter) {
                var url = "api/plm/programs/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&type={1}&programManager={2}".format(filter.searchQuery, filter.type, filter.programManager);
                return httpFactory.get(url);
            }

            function getProgram(programId) {
                var url = "api/plm/programs/" + programId;
                return httpFactory.get(url);
            }

            function getProgramPercentageComplete(programId) {
                var url = "api/plm/programs/" + programId + "/percentComplete";
                return httpFactory.get(url);
            }

            function updateProgram(project) {
                var url = "api/plm/programs/" + project.id;
                return httpFactory.put(url, project);
            }

            function deleteProgram(programId) {
                var url = "api/plm/programs/" + programId;
                return httpFactory.delete(url);
            }


            function getProgramCounts(programId) {
                var url = "api/plm/programs/" + programId + "/counts";
                return httpFactory.get(url);
            }

            function createProgramResources(programId, resources) {
                var url = "api/plm/programs/" + programId + "/resources/multiple";
                return httpFactory.post(url, resources);
            }

            function createProgramResource(programId, resource) {
                var url = "api/plm/programs/" + programId + "/resources";
                return httpFactory.post(url, resource);
            }

            function getProgramResource(programId, resourceId) {
                var url = "api/plm/programs/" + programId + "/resources/" + resourceId;
                return httpFactory.get(url);
            }

            function updateProgramResource(programId, resource) {
                var url = "api/plm/programs/" + programId + "/resources/" + resource.id;
                return httpFactory.put(url, resource);
            }

            function deleteProgramResource(programId, resourceId) {
                var url = "api/plm/programs/" + programId + "/resources/" + resourceId;
                return httpFactory.delete(url);
            }

            function getProgramResources(programId) {
                var url = "api/plm/programs/" + programId + "/resources";
                return httpFactory.get(url);
            }

            function createProgramProjects(programId, projects) {
                var url = "api/plm/programs/" + programId + "/projects/multiple";
                return httpFactory.post(url, projects);
            }

            function sendTasksNotification(id, type) {
                var url = "api/plm/programs/" + id + "/type/" + type + "/tasks/notifications";
                return httpFactory.get(url);
            }

            function createProgramProject(programId, project) {
                var url = "api/plm/programs/" + programId + "/projects";
                return httpFactory.post(url, project);
            }

            function getProgramProject(programId, projectId) {
                var url = "api/plm/programs/" + programId + "/projects/" + projectId;
                return httpFactory.get(url);
            }

            function updateProgramProject(programId, project) {
                var url = "api/plm/programs/" + programId + "/projects/" + project.id;
                return httpFactory.put(url, project);
            }

            function deleteProgramProject(programId, projectId) {
                var url = "api/plm/programs/" + programId + "/projects/" + projectId;
                return httpFactory.delete(url);
            }

            function createProgramProjectObject(programId, project) {
                var url = "api/plm/programs/" + programId + "/projects/object";
                return httpFactory.post(url, project);
            }

            function updateProgramProjectObject(programId, project) {
                var url = "api/plm/programs/" + programId + "/projects/object/" + project.id;
                return httpFactory.put(url, project);
            }

            function deleteProgramProjectObject(programId, projectId) {
                var url = "api/plm/programs/" + programId + "/projects/object/" + projectId;
                return httpFactory.delete(url);
            }

            function getProgramProjects(programId) {
                var url = "api/plm/programs/" + programId + "/projects";
                return httpFactory.get(url);
            }

            function getProgramProjectFolders(programId) {
                var url = "api/plm/programs/" + programId + "/projects/folders";
                return httpFactory.get(url);
            }

            function getDrillDownProgramProjects(programId) {
                var url = "api/plm/programs/" + programId + "/projects/drilldown";
                return httpFactory.get(url);
            }

            function getProgramProjectPlan(programId, projectId) {
                var url = "api/plm/programs/" + programId + "/projects/" + projectId + "/plan";
                return httpFactory.get(url);
            }

            function getProgramProjectPlanByAssignedTo(programId, assignedTo) {
                var url = "api/plm/programs/" + programId + "/projects/plan/" + assignedTo + "/all";
                return httpFactory.get(url);
            }

            function getProgramManagers() {
                var url = "api/plm/programs/programManagers";
                return httpFactory.get(url);
            }

            function getProgramReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getProgramsByIds(ids).then(
                        function (data) {
                            var map = new Hashtable();
                            angular.forEach(data, function (item) {
                                map.put(item.id, item);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var item = map.get(object[property]);
                                    if (item != null) {
                                        object[property + "Object"] = item;
                                    }
                                }
                            });

                        }
                    );
                }
            }

            function getProgramsByIds(ids) {
                var url = "api/plm/programs/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function attachProgramWorkflow(id, wfId) {
                var url = "api/plm/programs/" + id + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function getProgramProjectManagers(id) {
                var url = "api/plm/programs/" + id + "/projectManagers";
                return httpFactory.get(url);
            }

            function getProgramProjectAssignedTos(id) {
                var url = "api/plm/programs/" + id + "/projects/assignedTos";
                return httpFactory.get(url);
            }
        }
    }
);
