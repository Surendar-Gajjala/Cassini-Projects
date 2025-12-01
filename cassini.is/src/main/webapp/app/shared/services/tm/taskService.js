define(['app/shared/services/services.module', 'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'],
    function (module) {
        module.factory('TaskService', ProjectTaskService);

        function ProjectTaskService(httpFactory) {
            return {
                createProjectTask: createProjectTask,
                createTaskFiles: createTaskFiles,
                getTaskFiles: getTaskFiles,
                updateProjectTask: updateProjectTask,
                getProjectTask: getProjectTask,
                getProjectTasks: getProjectTasks,
                assignTaskTo: assignTaskTo,
                getTaskAssignedTo: getTaskAssignedTo,
                deleteProjectTask: deleteProjectTask,
                getListProjectTasks: getListProjectTasks,
                getContractTasks: getContractTasks,
                freeTextSearch: freeTextSearch,
                getAllTasks: getAllTasks,
                getListTasks: getListTasks,
                getSiteReferences: getSiteReferences,
                getTasksBySite: getTasksBySite,
                getPersonsByProjectTask: getPersonsByProjectTask,
                createProjectResource: createProjectResource,
                updateResource: updateResource,
                getProjectResource: getProjectResource,
                getProjectResourcesByType: getProjectResourcesByType,
                getProjectResources: getProjectResources,
                getResourcesByProject: getResourcesByProject,
                getMultipleResourcesByTask: getMultipleResourcesByTask,
                getTaskReferencesByResources: getTaskReferencesByResources,
                getProjectResourcesByTasks: getProjectResourcesByTasks,
                deleteTaskfile: deleteTaskfile,
                deleteTaskAttachment: deleteTaskAttachment,
                getAllFileVersions: getAllFileVersions,
                getProjectTaskfiles: getProjectTaskfiles,
                getTaskLatestFiles: getTaskLatestFiles,
                getTaskfile: getTaskfile,
                createTaskfile: createTaskfile,
                updateTaskfile: updateTaskfile,
                tasksByWbs: tasksByWbs,
                getResourcesByIds: getResourcesByIds,
                getItemAvailableQuantities: getItemAvailableQuantities,
                getReports: getReports,
                executeReport: executeReport,
                updateTaskCompletion: updateTaskCompletion,
                getTaskHistory: getTaskHistory,
                getRequiredTaskAttributes: getRequiredTaskAttributes,
                searchTasks: searchTasks,
                getReportByDates: getReportByDates,
                exportTasksReport: exportTasksReport,
                submitFormData: submitFormData,
                createTaskMedia: createTaskMedia,
                getTaskMedia: getTaskMedia,
                getTaskReferences: getTaskReferences,
                getTasksByIds: getTasksByIds,
                getTasksCount: getTasksCount,
                getPersonsToAddToTask: getPersonsToAddToTask,
                getTaskDetailsCount: getTaskDetailsCount,
                getProjectAndRoleResourcesByType: getProjectAndRoleResourcesByType,
                attachWorkflow: attachWorkflow,
                startWorkflow: startWorkflow,
                promoteWorkflow: promoteWorkflow,
                finishWorkflow: finishWorkflow,
                addApprovers: addApprovers,
                getApprovers: getApprovers,
                getWorkflowHistory: getWorkflowHistory,
                getTaskResources: getTaskResources,
                createTaskResources: createTaskResources,
                getTaskCompletionResources: getTaskCompletionResources
            };

            function submitFormData(objectId, formData) {
                var url = "api/projects/" + null + "/tasks/" + objectId + "/formData";
                return httpFactory.submittingFormData(url, formData);
            }

            function tasksByWbs(projectId, wbsId) {
                var url = "api/projects/" + projectId + "/wbs/tasksByWbs/" + wbsId;
                return httpFactory.get(url);
            }

            function getProjectTaskfiles(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/files";
                return httpFactory.get(url);
            }

            function getTaskLatestFiles(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/latestFiles";
                return httpFactory.get(url);
            }

            function getTaskfile(projectId, taskId, fileId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/files/" + fileId;
                return httpFactory.get(url);
            }

            function createTaskfile(projectId, task) {
                var url = "api/projects/" + projectId + "/tasks/" + task.id + "/upload/files";
                return httpFactory.post(url, task);
            }

            function updateTaskfile(projectId, taskId, file) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function deleteTaskfile(projectId, taskId, fileId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function deleteTaskAttachment(projectId, attachmentId) {
                var url = "api/projects/" + projectId + "/tasks/attachment/" + attachmentId;
                return httpFactory.delete(url);
            }

            function getAllFileVersions(projectId, taskId, fileId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }

            function createTaskFiles(projectId, taskId, taskFiles) {
                var url = "api/projects/" + projectId + "/tasks/ " + taskId + "/files";
                return httpFactory.post(url, taskFiles);
            }

            function getTaskFiles(projectId, taskId, attachmentType) {
                var url = "api/projects/" + projectId + "/tasks/ " + taskId + "/" + attachmentType + "/files";
                return httpFactory.get(url);
            }

            function getProjectResources(projectId, taskIds, resourcetype) {
                var url = "api/projects/" + projectId + "/[" + taskIds + "]/resources/" + resourcetype;
                return httpFactory.get(url);
            }

            function getResourcesByProject(projectId, taskIds) {
                var url = "api/projects/resources/project/" + projectId;
                return httpFactory.get(url);
            }

            function getProjectResourcesByTasks(projectId, taskIds) {
                var url = "api/projects/" + projectId + "/[" + taskIds + "]/resources";
                return httpFactory.get(url);
            }

            function getProjectResourcesByType(projectId, taskId, resourcetype) {
                var url = "api/projects/" + projectId + "/" + taskId + "/resources/" + resourcetype;
                return httpFactory.get(url);
            }

            function createProjectResource(projectId, resourceObjs) {
                var url = "api/projects/" + projectId + "/resources";
                return httpFactory.post(url, resourceObjs);
            }

            function updateResource(projectId, resourceObj) {
                var url = "api/projects/" + projectId + "/resources/update";
                return httpFactory.post(url, resourceObj);
            }

            function getProjectResource(projectId, taskId) {
                var url = "api/projects/" + projectId + "/" + taskId + "/resources";
                return httpFactory.get(url);
            }

            function getResourcesByIds(projectId, resourceIds) {
                var url = "api/projects/" + projectId + "/[" + resourceIds + "]/resources";
                return httpFactory.get(url);
            }

            function getItemAvailableQuantities(projectId, referenceIds) {
                var url = "api/projects/" + projectId + "/[" + referenceIds + "]/available";
                return httpFactory.get(url);
            }

            function getSites(sitesIds) {
                var url = "api/sites/siteIds/[" + sitesIds + "]";
                return httpFactory.get(url);
            }

            function freeTextSearch(projectId, pageable, criteria) {
                var url = "api/projects/" + projectId + "/tasks/freesearch?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function getAllTasks(pageable, criteria) {
                var url = "api/projects/null/tasks/search";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&description={1}&wbsItem={2}&status={3}&percentComplete={4}&plannedStartDate={5}&plannedFinishDate={6}&actualStartDate={7}&actualFinishDate={8}&searchQuery={9}".
                    format(criteria.name, criteria.description, criteria.wbsItem, criteria.status,
                    criteria.percentComplete, criteria.plannedStartDate, criteria.plannedFinishDate, criteria.actualStartDate,
                    criteria.actualFinishDate, criteria.searchQuery);
                return httpFactory.get(url);
            }

            function getListTasks(projectId, criteria) {
                var url = "api/projects/" + projectId + "/tasks/projecttasks";
                url += "?name={0}&description={1}&wbsItem={2}&status={3}&percentComplete={4}&plannedStartDate={5}&plannedFinishDate={6}&actualStartDate={7}&actualFinishDate={8}&searchQuery={9}&person={10}".
                    format(criteria.name, criteria.description, criteria.wbsItem, criteria.status,
                    criteria.percentComplete, criteria.plannedStartDate, criteria.plannedFinishDate, criteria.actualStartDate,
                    criteria.actualFinishDate, criteria.searchQuery, criteria.person);
                return httpFactory.get(url);
            }

            function createProjectTask(projectId, projectTask) {
                var url = "api/projects/" + projectId + "/tasks";
                return httpFactory.post(url, projectTask);
            }

            function updateProjectTask(projectId, projectTask) {
                var url = "api/projects/" + projectId + "/tasks/" + projectTask.id;
                return httpFactory.put(url, projectTask);
            }

            function getProjectTasks(projectId, criteria, pageable) {
                var url = "api/projects/" + projectId + "/tasks/filters";
                url += "?name={0}&description={1}&site={2}&person={3}&wbsItem={4}&status={5}&percentComplete={6}&plannedStartDate={7}&plannedFinishDate={8}&actualStartDate={9}&actualFinishDate={10}&inspectedBy={11}&inspectedOn={12}&inspectionResult={13}&subContract={14}&delayTask={15}".
                    format(criteria.name, criteria.description, criteria.site, criteria.person, criteria.wbsItem, criteria.status, criteria.percentComplete, criteria.plannedStartDate,
                    criteria.plannedFinishDate, criteria.actualStartDate, criteria.actualFinishDate, criteria.inspectedBy, criteria.inspectedOn, criteria.inspectionResult, criteria.subContract, criteria.delayTask);

                url += "&page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getPersonsByProjectTask(projectId) {
                var url = "api/projects/" + projectId + "/tasks" + "/persons";
                return httpFactory.get(url);
            }


            function getListProjectTasks(projectId) {
                var url = "api/projects/" + projectId + "/tasks/all";
                return httpFactory.get(url);
            }

            function getContractTasks(projectId) {
                var url = "api/projects/" + projectId + "/tasks/contract";
                return httpFactory.get(url);
            }

            function getProjectTask(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId;
                return httpFactory.get(url);
            }

            function getTasksBySite(projectId, siteId) {
                var url = "api/projects/" + projectId + "/tasks/bySite/" + siteId;
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

            function getMultipleResourcesByTask(projectId, ids) {
                var url = "api/projects/" + projectId + "/tasks/multiple/[" + ids + "]" + "/resources";
                return httpFactory.get(url);
            }

            function getReports() {
                var url = "api/reporting";
                return httpFactory.get(url);
            }

            function executeReport(report, dateRange) {
                var url = "api/reporting/execute";
                if (dateRange != null && dateRange != undefined) {
                    url += "?dateRange=" + dateRange.startDate + "-" + dateRange.endDate;
                }
                return httpFactory.post(url, report);
            }

            function updateTaskCompletion(projectId, taskId, taskHistory) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/taskHistory";
                return httpFactory.post(url, taskHistory);
            }

            function getTaskHistory(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/taskHistory";
                return httpFactory.get(url);
            }


            function getTaskReferencesByResources(projectId, objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMultipleResourcesByTask(projectId, ids).then(
                        function (data) {
                            var map = new Hashtable();
                            angular.forEach(data, function (resource) {
                                map.put(resource.id, resource);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var resource = map.get(object[property]);
                                    if (resource != null) {
                                        object[property + "Object"] = resource;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getSiteReferences(objects, property) {
                var siteIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && siteIds.indexOf(object[property]) == -1) {
                        siteIds.push(object[property]);
                    }
                });


                if (siteIds.length > 0) {
                    getSites(siteIds).then(
                        function (tasks) {
                            var map = new Hashtable();
                            angular.forEach(tasks, function (task) {
                                map.put(task.id, task);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var task = map.get(object[property]);
                                    if (task != null) {
                                        object[property + "Object"] = task;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getRequiredTaskAttributes(projectId, objectType) {
                var url = "api/projects/" + projectId + "/tasks/requiredTaskAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function searchTasks(projectId, criteria) {
                var url = "api/projects/" + projectId + "/tasks/searchAll?searchQuery={0}".format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function getReportByDates(projectid, startDate, endDate) {
                var url = "api/projects/" + projectid + "/tasks/report?id={0}&fromDate={1}&toDate={2}"
                        .format(projectid, startDate, endDate);
                return httpFactory.get(url);
            }

            function exportTasksReport(projectid, startDate, endDate) {
                var url = "api/projects/" + projectid + "/tasks/" + 'excel' + "/report?fromDate={0}&toDate={1}"
                        .format(startDate, endDate);
                return httpFactory.get(url);
            }

            function createTaskMedia(projectId, taskId, media) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/media";
                return httpFactory.uploadMultiple(url, media);
            }

            function getTaskMedia(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/media";
                return httpFactory.get(url);
            }

            function getTasksByIds(projectId, taskIds) {
                var url = "api/projects/" + projectId + "/tasks/multiple/[" + taskIds + "]";
                return httpFactory.get(url);
            }

            function getPersonsToAddToTask(projectId, taskId, pageable) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/addManpower/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getTaskReferences(projectId, objects, property) {
                var taskIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && taskIds.indexOf(object[property]) == -1) {
                        taskIds.push(object[property]);
                    }
                });

                if (taskIds.length > 0) {
                    getTasksByIds(projectId, taskIds).then(
                        function (tasks) {
                            var map = new Hashtable();
                            angular.forEach(tasks, function (task) {
                                map.put(task.id, task);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var task = map.get(object[property]);
                                    if (task != null) {
                                        object[property + "Object"] = task;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getTasksCount(projectId) {
                var url = "api/projects/" + projectId + "/tasks/count";
                return httpFactory.get(url);
            }

            function getTaskDetailsCount(projectId, taskId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/count";
                return httpFactory.get(url);
            }

            function getProjectAndRoleResourcesByType(projectId, taskId, resourcetype) {
                var url = "api/projects/" + projectId + "/" + taskId + "/resourcesType/[" + resourcetype + "]";
                return httpFactory.get(url);
            }

            function attachWorkflow(projectId, taskId, wfId) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function startWorkflow(taskId) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/workflow/start";
                return httpFactory.get(url);
            }

            function promoteWorkflow(taskId, fromStatus, toStatus) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/workflow/promote?fromStatus={1}&toStatus={2}".format(taskId, fromStatus, toStatus);
                return httpFactory.get(url);
            }

            function finishWorkflow(taskId) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/workflow/finish";
                return httpFactory.get(url);
            }

            function addApprovers(taskId, statusId, approvers) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/workflow/statuses/" + statusId + "/approvers";
                return httpFactory.post(url, approvers);
            }

            function getApprovers(taskId, statusId) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/workflow/statuses/" + statusId + "/approvers";
                return httpFactory.get(url);
            }

            function getWorkflowHistory(taskId) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/workflow/history";
                return httpFactory.get(url);
            }

            function getTaskResources(taskId) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/resources";
                return httpFactory.get(url);
            }

            function createTaskResources(taskId, taskHistory, object) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/resources/" + taskHistory.id + "/object";
                return httpFactory.post(url, object);
            }

            function getTaskCompletionResources(taskId, taskHistory) {
                var url = "api/projects/" + 0 + "/tasks/" + taskId + "/taskResources/" + taskHistory.id;
                return httpFactory.get(url);
            }
        }

    }
);