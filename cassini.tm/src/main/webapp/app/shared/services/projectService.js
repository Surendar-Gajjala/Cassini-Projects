/**
 * Created by Anusha on 11-07-2016.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ProjectService', ProjectService);

        function ProjectService(httpFactory) {
            return {
                createProject: createProject,
                getProjects: getProjects,
                getProjectsByIds: getProjectsByIds,
                getProjectReferences: getProjectReferences,
                deleteProjectItem: deleteProjectItem,
                getProjectById:getProjectById,
                updateProject:updateProject,
                freeTextSearch:freeTextSearch
            };

            function createProject(project) {
                var url = "api/projects";
                return httpFactory.post(url, project);
            }

            function getProjects(/*pageable*/) {
                var url = "api/projects";
               /* url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);*/
                return httpFactory.get(url);
            }

            function getProjectById(projectId) {
                var url = "api/projects/"+ projectId;
                return httpFactory.get(url);
            }

            function updateProject(project) {
                var url = "api/projects/" + project.id;
                return httpFactory.put(url, project);
            }

            function freeTextSearch(criteria, pageable) {
                var url = "api/projects/freesearch?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function deleteProjectItem(projectId) {
                var url = "api/projects/"+ projectId;
                return httpFactory.delete(url);
            }


            function getProjectsByIds(projectIds) {
                var url = "api/projects/multiple/[" + projectIds + ']';
                return httpFactory.get(url);
            }

            function getProjectReferences(objects, property) {
                var projectIds = [];
                var idMap = new Hashtable();
                angular.forEach(objects, function(object) {
                    var projectId = object[property];
                    if(idMap.get(projectId) == null) {
                        projectIds.push(projectId);
                        idMap.put(projectId, object);
                    }
                });

                if(projectIds.length > 0) {
                    getProjectsByIds(projectIds).then (
                        function(projects) {
                            var map = new Hashtable();
                            angular.forEach(projects, function(project) {
                                map.put(project.id, project);
                            });

                            angular.forEach(objects, function(object) {
                                var id = object[property];
                                var project = map.get(id);
                                if(project != null) {
                                    object[property + "Object"] = project;
                                }
                            });
                        }
                    )
                }
            }


        }
    }
);