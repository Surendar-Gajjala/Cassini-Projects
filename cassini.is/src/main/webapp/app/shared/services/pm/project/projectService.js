define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('ProjectService', ProjectService);

        function ProjectService(httpFactory) {
            return {
                createProject: createProject,
                updateProject: updateProject,
                getProject: getProject,
                getProjects: getProjects,
                getAllProjects: getAllProjects,
                getPageableProjects: getPageableProjects,
                createProjectPerson: createProjectPerson,
                deleteProjectPerson: deleteProjectPerson,
                updateProjectPerson: updateProjectPerson,
                getProjectPersons: getProjectPersons,
                createProjectRole: createProjectRole,
                createPortfolio: createPortfolio,
                getAllPageablePortfolios: getAllPageablePortfolios,
                getAllPortfolios: getAllPortfolios,
                updatePortfolio: updatePortfolio,
                findProjectByPortfolio: findProjectByPortfolio,
                deleteProjectRole: deleteProjectRole,
                updateProjectRole: updateProjectRole,
                getProjectRoles: getProjectRoles,
                getProjectPagedRoles: getProjectPagedRoles,
                getRole: getRole,
                getMultipleRoles: getMultipleRoles,
                createPersonRole: createPersonRole,
                getPersonsRoles: getPersonsRoles,
                getRolesByPersonId: getRolesByPersonId,
                getPersonsByRoleId: getPersonsByRoleId,
                getRoleReferences: getRoleReferences,
                deletePersonRole: deletePersonRole,
                getProjectPagedPersons: getProjectPagedPersons,
                deleteResource: deleteResource,
                getBoqItem: getBoqItem,
                getAllProjectAttributes: getAllProjectAttributes,
                getProjectAttributesRequiredFalse: getProjectAttributesRequiredFalse,
                getAttributesByProjectIdAndAttributeId: getAttributesByProjectIdAndAttributeId,
                getRequiredProjectAttributes: getRequiredProjectAttributes,
                getProjectPerson: getProjectPerson,
                getProjectByBoqId: getProjectByBoqId,
                getProjectReferences: getProjectReferences,
                getProjectsByIds: getProjectsByIds,
                searchProjects: searchProjects,
                getReportByDates: getReportByDates,
                exportProjectMaterialReport: exportProjectMaterialReport,
                createProjectMedia: createProjectMedia,
                getProjectMediaByProject: getProjectMediaByProject,
                getFilterdProjectPersons: getFilterdProjectPersons,
                getAllProjectMedia: getAllProjectMedia,
                freeSearch: freeSearch,
                cloneProject: cloneProject,
                getProjectMediaCount: getProjectMediaCount,
                createOrgChart: createOrgChart,
                getProjectPersonObjects: getProjectPersonObjects,
                deletePersonNode: deletePersonNode,
                copyTasks: copyTasks
            };

            function getPersonsByRoleId(projectId, roleId) {
                var url = "api/projects/" + projectId + "/" + roleId + "/persons";
                return httpFactory.get(url);
            }

            function getBoqItem(projectId, itemId) {
                var url = "api/projects/" + projectId + "/items/" + itemId;
                return httpFactory.get(url);
            }

            function createProject(project) {
                var url = "api/projects";
                return httpFactory.post(url, project);
            }

            function createPortfolio(portfolio) {
                var url = "api/portfolio";
                return httpFactory.post(url, portfolio);
            }

            function findProjectByPortfolio(id) {
                var url = "api/projects/portfolio/" + id;
                return httpFactory.get(url);
            }

            function updateProject(project) {
                var url = "api/projects/" + project.id;
                return httpFactory.put(url, project);
            }

            function updatePortfolio(portfolio) {
                var url = "api/portfolio";
                return httpFactory.put(url, portfolio);
            }

            function getProject(id) {
                var url = "api/projects/" + id;
                return httpFactory.get(url);
            }

            function getAllPageablePortfolios(pageable) {
                var url = "api/portfolio/pageable";
                url += "?page={0}&size={1}".
                    format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getAllPortfolios() {
                var url = "api/portfolio";
                return httpFactory.get(url);
            }

            function getAllProjects() {
                var url = "api/projects";
                return httpFactory.get(url);
            }

            function getProjects() {
                var url = "api/projects";
                return httpFactory.get(url);
            }

            function getPageableProjects(pageable) {
                var url = "api/projects/pageable?page={0}&size={1}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function createProjectPerson(projectId, projectPersons) {
                var url = "api/projects/" + projectId + "/team";
                return httpFactory.post(url, projectPersons);
            }

            function updateProjectPerson(projectId, projectPerson) {
                var url = "api/projects/" + projectId + "/team";
                return httpFactory.put(url, projectPerson);
            }

            function deleteProjectPerson(projectId, personId) {
                var url = "api/projects/" + projectId + "/team/" + personId;
                return httpFactory.delete(url);
            }

            function getProjectPersons(projectId) {
                var url = "api/projects/" + projectId + "/teamPersons";
                return httpFactory.get(url);
            }

            function getProjectPagedPersons(projectId, pageable) {
                var url = "api/projects/" + projectId + "/teamPersons/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function createPersonRole(projectId, personId, personRoles) {
                var url = "api/projects/" + projectId + "/person/" + personId + "/roles";
                return httpFactory.post(url, personRoles);

            }

            function deletePersonRole(projectId, personId, rowId) {
                var url = "api/projects/" + projectId + "/" + personId + "/" + rowId;
                return httpFactory.delete(url);
            }

            function getRolesByPersonId(projectId, personId) {
                var url = "api/projects/" + projectId + "/" + personId + "/roles";
                return httpFactory.get(url);
            }

            function getPersonsRoles(projectId, ids) {
                var url = "api/projects/" + projectId + "/multiple/persons/[" + ids + "]";
                return httpFactory.get(url);
            }

            function createProjectRole(projectId, projectRole) {
                var url = "api/projects/" + projectId + "/roles";
                return httpFactory.post(url, projectRole);
            }

            function getProjectRoles(projectId) {
                var url = "api/projects/" + projectId + "/roles";
                return httpFactory.get(url);

            }

            function getRole(projectId, id) {
                var url = "api/projects/" + projectId + "/roles/" + id;
                return httpFactory.get(url);
            }

            function getProjectPagedRoles(projectId, pageable) {
                var url = "api/projects/" + projectId + "/roles/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);

            }

            function getMultipleRoles(projectId, ids) {
                var url = "api/projects/" + projectId + "/multiple/roles/[" + ids + "]";
                return httpFactory.get(url);
            }


            function deleteProjectRole(projectId, id) {
                var url = "api/projects/" + projectId + "/roles/" + id;
                return httpFactory.delete(url);
            }

            function updateProjectRole(projectId, projectRole) {
                var url = "api/projects/projectRole/" + projectId;
                return httpFactory.put(url, projectRole);
            }

            function deleteResource(projectId, resourceId) {
                var url = "api/projects/" + projectId + "/resources/" + resourceId;
                return httpFactory.delete(url);
            }

            function getRoleReferences(projectId, objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMultipleRoles(projectId, ids).then(
                        function (data) {
                            var map = new Hashtable();
                            angular.forEach(data, function (role) {
                                map.put(role.id, role);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var role = map.get(object[property]);
                                    if (role != null) {
                                        object[property + "Object"] = role;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getAllProjectAttributes(objectType) {
                var url = "api/projects/attributes/" + objectType;
                return httpFactory.get(url);
            }

            function getProjectAttributesRequiredFalse(objectType) {
                var url = "api/projects/requiredFalseAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function getAttributesByProjectIdAndAttributeId(itemIds, attributeIds) {
                var url = "api/projects/objectAttributes/project";
                return httpFactory.post(url, [itemIds, attributeIds]);
            }

            function getRequiredProjectAttributes(objectType) {
                var url = "api/projects/requiredProjectAttributes?objectType=" + objectType;
                return httpFactory.get(url);
            }

            function getProjectPerson(personId) {
                var url = "api/projects/person/" + personId;
                return httpFactory.get(url);

            }

            function getProjectByBoqId(boqId) {
                var url = "api/projects/boqItem/" + boqId;
                return httpFactory.get(url);
            }

            function getProjectsByIds(projectIds) {
                var url = "api/projects/multiple/" + projectIds;
                return httpFactory.get(url);
            }

            function searchProjects(criteria) {
                var url = "api/projects/search?searchQuery={0}".format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function freeSearch(pageable, freetext) {
                var url = "api/projects/freeSearch?page={0}&size={1}".
                    format(pageable.page, pageable.size, pageable.sort.field);

                url += "&searchQuery={0}".
                    format(freetext);
                return httpFactory.get(url);
            }

            function getReportByDates(projectid) {
                var url = "api/projects/report/" + projectid;
                return httpFactory.get(url);
            }

            function exportProjectMaterialReport(projectid) {
                var url = "api/projects/" + projectid + "/report/export/" + 'excel';
                return httpFactory.get(url);
            }

            function getProjectReferences(objects, property) {
                var projectIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && projectIds.indexOf(object[property]) == -1) {
                        projectIds.push(object[property]);
                    }
                });

                if (projectIds.length > 0) {
                    getProjectsByIds(projectIds).then(
                        function (projects) {
                            var map = new Hashtable();
                            angular.forEach(projects, function (project) {
                                map.put(project.id, project);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var project = map.get(object[property]);
                                    if (project != null) {
                                        object[property + "Object"] = project;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function createProjectMedia(projectId, media) {
                var url = "api/projects/" + projectId + "/media";
                return httpFactory.uploadMultiple(url, media);
            }

            function getProjectMediaByProject(projectId) {
                var url = "api/projects/" + projectId + "/media";
                return httpFactory.get(url);
            }

            /* function getFilterdProjectPersons(projectId, pageable, filters) {
             var url = "api/projects/" + projectId + "/projectPersons/filters?page={0}&size={1}".
             format(pageable.page, pageable.size);
             url += "&fullName={0}&phoneMobile={1}".
             format(filters.firstName, filters.phoneMobile);
             return httpFactory.get(url);
             }*/


            function getFilterdProjectPersons(projectId, pageable, freeText) {
                /* var url = "api/is/items/manpower/freesearch?page={0}&size={1}&sort={2}:{3}".*/
                var url = "api/projects/" + projectId + "/projectPersons/filters?page={0}&size={1}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }


            function getAllProjectMedia(projectId, pageable) {
                var url = "api/projects/" + projectId + "/all/media".format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function cloneProject(projectId, project) {
                var url = "api/projects/" + projectId + "/cloneProject";
                return httpFactory.post(url, project);
            }

            function getProjectMediaCount(projectId) {
                var url = "api/projects/" + projectId + "/media/count";
                return httpFactory.get(url);
            }

            function createOrgChart(projectId, project) {
                var url = "api/projects/" + projectId + "/projectPerson";
                return httpFactory.post(url, project);
            }

            function getProjectPersonObjects(projectId) {
                var url = "api/projects/" + projectId + "/projectPersonObjects";
                return httpFactory.get(url);
            }

            function deletePersonNode(projectId, nodeId) {
                var url = "api/projects/" + projectId + "/deleteNode/" + nodeId;
                return httpFactory.delete(url);
            }

            function copyTasks(projectId, project) {
                var url = "api/projects/" + projectId + "/copytasks";
                return httpFactory.post(url, project);
            }

        }
    }
)

;