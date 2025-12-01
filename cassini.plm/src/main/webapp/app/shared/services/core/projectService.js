/**
 * Created by swapna on 1/4/18.
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
                getAllProjects: getAllProjects,
                getProject: getProject,
                updateProject: updateProject,
                deleteProject: deleteProject,
                getProjects: getProjects,
                freeTextSearch: freeTextSearch,


                createProjectMember: createProjectMember,
                createProjectMembers: createProjectMembers,
                createProjectMultipleMembers: createProjectMultipleMembers,
                getProjectMember: getProjectMember,
                updateProjectMember: updateProjectMember,
                deleteProjectMember: deleteProjectMember,
                getProjectMembers: getProjectMembers,

                createWBSElement: createWBSElement,
                createWBSElements: createWBSElements,
                createLinks: createLinks,
                getLinks: getLinks,
                getWBSElements: getWBSElements,
                getWBSElement: getWBSElement,
                updateWBSElement: updateWBSElement,
                deleteWBSElement: deleteWBSElement,
                getRootWbsElements: getRootWbsElements,
                getWBSElementChildren: getWBSElementChildren,
                getWBSActivities: getWBSActivities,
                getWBSMilestones: getWBSMilestones,

                getProjectFiles: getProjectFiles,
                uploadProjectFiles: uploadProjectFiles,
                getFileVersions: getFileVersions,
                deleteProjectFile: deleteProjectFile,

                createProjectDeliverable: createProjectDeliverable,
                createProjectDeliverables: createProjectDeliverables,
                getProjectDeliverable: getProjectDeliverable,
                updateProjectDeliverable: updateProjectDeliverable,
                deleteProjectDeliverable: deleteProjectDeliverable,
                getProjectDeliverables: getProjectDeliverables,
                getAllProjectDeliverables: getAllProjectDeliverables,

                getAllTypeAttributes: getAllTypeAttributes,

                createProjectItemReference: createProjectItemReference,
                updateProjectItemReference: updateProjectItemReference,
                deleteProjectItemReference: deleteProjectItemReference,
                getProjectItemReference: getProjectItemReference,
                getItemsByProject: getItemsByProject,
                saveMultipleItemReferences: saveMultipleItemReferences,
                getAllItems: getAllItems,
                searchItemReferences: searchItemReferences,
                getProjectWbsTree: getProjectWbsTree,
                getParentWbsByName: getParentWbsByName,
                getChildWbsByName: getChildWbsByName,
                getWbsChildren: getWbsChildren,
                getProjectWbsStructure: getProjectWbsStructure,
                getProjectPercentageComplete: getProjectPercentageComplete,
                createProjectWithTemplate: createProjectWithTemplate,
                addProjectWithTemplate: addProjectWithTemplate,
                saveAsProjectTemplate: saveAsProjectTemplate,
                updateProjectFileDownloadHistory: updateProjectFileDownloadHistory,
                getProjectFileVersionsAndCommentsAndDownloads: getProjectFileVersionsAndCommentsAndDownloads,
                getProjectItemDeliverables: getProjectItemDeliverables,
                getProjectAndGlossaryDeliverables: getProjectAndGlossaryDeliverables,
                updateFileName: updateFileName,
                copyWbs: copyWbs,
                getProjectReferences: getProjectReferences,
                getPersonProjects: getPersonProjects,
                getWbsproject: getWbsproject,
                getAllProjectMembers: getAllProjectMembers,
                updateProjectFile: updateProjectFile,

                deleteProjectFolder: deleteProjectFolder,
                createProjectFolder: createProjectFolder,
                uploadProjectFolderFiles: uploadProjectFolderFiles,
                getProjectFolderChildren: getProjectFolderChildren,
                moveProjectFileToFolder: moveProjectFileToFolder,
                getProjectDetails: getProjectDetails,
                updateProjectFileDescription: updateProjectFileDescription,
                updateProjectTaskFileDescription: updateProjectTaskFileDescription,
                updateWbsItemSeq: updateWbsItemSeq,
                pasteProjectFilesFromClipboard: pasteProjectFilesFromClipboard,
                pasteDeliverablesToProject: pasteDeliverablesToProject,
                undoCopiedFiles: undoCopiedFiles,
                undoProjectDeliverables: undoProjectDeliverables,
                getLatestUploadedProjectFile: getLatestUploadedProjectFile,
                updateProjectSequenceNumbers: updateProjectSequenceNumbers,
                getWorkflows: getWorkflows,
                attachWorkflow: attachWorkflow,
                deleteWorkflow: deleteWorkflow,
                finishProjectDeliverable: finishProjectDeliverable,
                getProjectFilesByName: getProjectFilesByName,
                getFilteredProjects: getFilteredProjects,
                createProjectReqDocuments: createProjectReqDocuments,
                createProjectReqDocument: createProjectReqDocument,
                updateProjectReqDocument: updateProjectReqDocument,
                deleteProjectReqDocument: deleteProjectReqDocument,
                getProjectReqDocuments: getProjectReqDocuments,
                subscribe: subscribe,
                getProjectAttributesWithHierarchy: getProjectAttributesWithHierarchy,
                updateProjectPerson: updateProjectPerson,
                createProjectPerson: createProjectPerson,
                getProjectManagers: getProjectManagers,
                getProgramNullProjects: getProgramNullProjects,
                getProjectTasksAssignedToCount: getProjectTasksAssignedToCount
            };

            function createProject(project) {
                var url = "api/plm/projects";
                return httpFactory.post(url, project);
            }

            function saveAsProjectTemplate(template, projectId) {
                var url = "api/plm/projects/" + projectId + "/template";
                return httpFactory.post(url, template);
            }

            function createProjectWithTemplate(project, templateId) {
                var url = "api/plm/projects/template?template=" + templateId;
                return httpFactory.post(url, project);
            }

            function addProjectWithTemplate(project, templateId) {
                var url = "api/plm/projects/template/add?template=" + templateId;
                return httpFactory.post(url, project);
            }

            function getAllProjects() {
                var url = "api/plm/projects";
                return httpFactory.get(url);
            }

            function getProgramNullProjects() {
                var url = "api/plm/projects/programnull";
                return httpFactory.get(url);
            }

            function getProjects(pageable) {
                var url = "api/plm/projects?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getFilteredProjects(pageable, filter) {
                var url = "api/plm/projects/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&program={1}&type={2}&projectManager={3}".format(filter.searchQuery, filter.program, filter.type, filter.projectManager);
                return httpFactory.get(url);
            }

            function createProjectMembers(projectId, projectPersons) {
                var url = "api/plm/projects/" + projectId + "/team";
                return httpFactory.post(url, projectPersons);
            }

            function getProject(projectId) {
                var url = "api/plm/projects/" + projectId;
                return httpFactory.get(url);
            }

            function getProjectPercentageComplete(projectId) {
                var url = "api/plm/projects/" + projectId + "/percentComplete";
                return httpFactory.get(url);
            }

            function updateProject(project) {
                var url = "api/plm/projects/" + project.id;
                return httpFactory.put(url, project);
            }

            function deleteProject(projectId) {
                var url = "api/plm/projects/" + projectId;
                return httpFactory.delete(url);
            }

            function getProjectManagers() {
                var url = "api/plm/projects/projectManagers";
                return httpFactory.get(url);
            }

            function createProjectMember(projectId, person) {
                var url = "api/plm/projects/" + projectId + "/memebers";
                return httpFactory.post(url, person);
            }

            function getProjectMember(projectId, personId) {
                var url = "api/plm/projects/" + projectId + "/memebers/" + personId;
                return httpFactory.get(url);
            }

            function updateProjectMember(projectId, person) {
                var url = "api/plm/projects/" + projectId + "/members/" + person.id;
                return httpFactory.put(url, person);
            }

            function deleteProjectMember(projectId, personId) {
                var url = "api/plm/projects/" + projectId + "/members/" + personId;
                return httpFactory.delete(url);
            }

            function getProjectMembers(projectId, pageable) {
                var url = "api/plm/projects/" + projectId + "/members?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function createWBSElement(projectId, wbs) {
                var url = "api/plm/projects/" + projectId + "/wbs";
                return httpFactory.post(url, wbs);
            }

            function createWBSElements(projectId, wbsList) {
                var url = "api/plm/projects/" + projectId + "/wbsList";
                return httpFactory.post(url, wbsList);
            }

            function createLinks(projectId, links) {
                var url = "api/plm/projects/" + projectId + "/createLinks";
                return httpFactory.post(url, links);
            }

            function getLinks(projectId) {
                var url = "api/plm/projects/" + projectId + "/getLinks";
                return httpFactory.get(url);
            }

            function getWBSElements(projectId) {
                var url = "api/plm/projects/" + projectId + "/wbs";
                return httpFactory.get(url);
            }

            function getWBSElement(projectId, wbsId) {
                var url = "api/plm/projects/" + projectId + "/wbs/" + wbsId;
                return httpFactory.get(url);
            }

            function updateWBSElement(projectId, wbs) {
                var url = "api/plm/projects/" + projectId + "/wbs/" + wbs.id;
                return httpFactory.put(url, wbs);
            }

            function deleteWBSElement(projectId, wbsId) {
                var url = "api/plm/projects/" + projectId + "/wbs/" + wbsId;
                return httpFactory.delete(url);
            }

            function getRootWbsElements(projectId) {
                var url = "api/plm/projects/" + projectId + "/wbs/roots";
                return httpFactory.get(url);
            }

            function getWBSElementChildren(projectId, wbsId) {
                var url = "api/plm/projects/" + projectId + "/wbs/" + wbsId + "/children";
                return httpFactory.get(url);
            }

            function getWBSActivities(projectId, wbsId) {
                var url = "api/plm/projects/" + projectId + "/wbs" + wbsId + "/activities";
                return httpFactory.get(url);
            }

            function getWBSMilestones(projectId, wbsId) {
                var url = "api/plm/projects/" + projectId + "/wbs" + wbsId + "/milestones";
                return httpFactory.get(url);
            }

            function getProjectFiles(projectId) {
                var url = "api/plm/projects/" + projectId + "/files";
                return httpFactory.get(url);
            }

            function uploadProjectFiles(projectId, files) {
                var url = "api/plm/projects/" + projectId + "/files";
                return httpFactory.post(url, files);
            }

            function getFileVersions(projectId, fileId) {
                var url = "api/plm/projects/" + projectId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }

            function getProjectFileVersionsAndCommentsAndDownloads(projectId, fileId, type) {
                var url = "api/plm/projects/" + projectId + "/files/" + fileId + "/versionComments/" + type;
                return httpFactory.get(url);
            }

            function deleteProjectFile(projectId, fileId) {
                var url = "api/plm/projects/" + projectId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function createProjectDeliverable(projectId, deliverable) {
                var url = "api/plm/projects/" + projectId + "/deliverables";
                return httpFactory.post(url, deliverable);
            }

            function createProjectDeliverables(projectId, items) {
                var url = "api/plm/projects/" + projectId + "/deliverables/multiple";
                return httpFactory.post(url, items);
            }

            function getProjectDeliverable(projectId, deliverableId) {
                var url = "api/plm/projects/" + projectId + "/deliverables/" + deliverableId;
                return httpFactory.get(url);
            }

            function updateProjectDeliverable(projectId, deliverable) {
                var url = "api/plm/projects/" + projectId + "/deliverables/" + deliverable.id;
                return httpFactory.put(url, deliverable);
            }

            function deleteProjectDeliverable(projectId, deliverableId) {
                var url = "api/plm/projects/" + projectId + "/deliverables/" + deliverableId;
                return httpFactory.delete(url);
            }

            function getProjectDeliverables(projectId) {
                var url = "api/plm/projects/" + projectId + "/deliverables";
                return httpFactory.get(url);
            }

            function getAllProjectDeliverables(projectId) {
                var url = "api/plm/projects/" + projectId + "/projectdeliverables";
                return httpFactory.get(url);
            }

            function getProjectItemDeliverables(projectId, pageable, filters) {
                var url = "api/plm/projects/" + projectId + "/itemDeliverable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&itemNumber={1}&itemType={2}&description={3}"
                    .format(filters.name, filters.itemNumber, filters.itemType, filters.description);
                return httpFactory.get(url);
            }

            function freeTextSearch(criteria, pageable) {
                var url = "api/plm/projects/freetextsearch";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(criteria);
                return httpFactory.get(url);
            }

            function getAllTypeAttributes(objectType) {
                var url = "api/plm/projects/attributes/" + objectType;
                return httpFactory.get(url);
            }

            function createProjectItemReference(projectId, itemReference) {
                var url = "api/plm/projects/" + projectId + "/itemReferences";
                return httpFactory.post(url, itemReference);
            }

            function updateProjectItemReference(projectId, itemReference) {
                var url = "api/plm/projects/" + projectId + "/itemReferences/" + itemReference.id;
                return httpFactory.put(url, itemReference);
            }

            function deleteProjectItemReference(projectId, itemReference) {
                var url = "api/plm/projects/" + projectId + "/itemReferences/" + itemReference.id;
                return httpFactory.delete(url);
            }

            function getProjectItemReference(projectId, itemReferenceId) {
                var url = "api/plm/projects/" + projectId + "/itemReferences/" + itemReferenceId;
                return httpFactory.get(url);
            }

            function getItemsByProject(projectId) {
                var url = "api/plm/projects/" + projectId + "/itemReferences";
                return httpFactory.get(url);
            }

            function saveMultipleItemReferences(projectId, itemReferences) {
                var url = "api/plm/projects/" + projectId + "/itemReferences/multiple";
                return httpFactory.post(url, itemReferences);
            }

            function getAllItems(projectId, pageable) {
                var url = "api/plm/projects/items/" + projectId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);

            }

            function searchItemReferences(projectId, objectType, pageable, criteria) {
                var url = "api/plm/projects/" + projectId + "/items/type/" + objectType + "/search?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                if (criteria.itemType == null) {
                    url += "&itemNumber={0}&itemName={1}&revision={2}&status={3}".
                        format(criteria.itemNumber, criteria.itemName, criteria.revision, criteria.status);
                } else {
                    url += "&itemNumber={0}&itemName={1}&revision={2}&status={3}&itemType={4}".
                        format(criteria.itemNumber, criteria.itemName, criteria.revision, criteria.status, criteria.itemType.id);
                }
                return httpFactory.get(url);
            }

            function getProjectWbsTree(projectId) {
                var url = "api/plm/projects/" + projectId + "/wbsTree";
                return httpFactory.get(url);
            }

            function getProjectWbsStructure(projectId) {
                var url = "api/plm/projects/" + projectId + "/wbsStructure";
                return httpFactory.get(url);
            }

            function getParentWbsByName(projectId, parentName) {
                var url = "api/plm/projects/" + projectId + "/parent/" + parentName;
                return httpFactory.get(url);
            }

            function getChildWbsByName(projectId, parentId, wbsName) {
                var url = "api/plm/projects/" + projectId + "/parent/" + parentId + "/wbsName?wbsName={0}".format(wbsName);
                return httpFactory.get(url);
            }

            function getWbsChildren(projectId, wbs) {
                var url = "api/plm/projects/" + projectId + "/wbs/children/" + wbs;
                return httpFactory.get(url);
            }

            function updateProjectFileDownloadHistory(projectId, fileId) {
                var url = "api/plm/projects/" + projectId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getProjectAndGlossaryDeliverables(projectId) {
                var url = "api/plm/projects/" + projectId + "/projectDeliverables";
                return httpFactory.get(url);
            }

            function updateFileName(projectId, fileId, newFileName) {
                var url = 'api/plm/projects/' + projectId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function pasteProjectFilesFromClipboard(projectId, fileId, files) {
                var url = 'api/plm/projects/' + projectId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files)
            }

            function copyWbs(project, wbs) {
                var url = "api/plm/projects/" + project + "/copy/wbs/" + wbs;
                return httpFactory.post(url);
            }

            function getProjectReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getProjectsByIds(ids).then(
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

            function getProjectsByIds(ids) {
                var url = "api/plm/projects/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getPersonProjects(personId) {
                var url = "api/plm/projects/person/" + personId;
                return httpFactory.get(url);
            }

            function getWbsproject(wbsId) {
                var url = "api/plm/projects/wbs/" + wbsId;
                return httpFactory.get(url);
            }

            function deleteProjectFolder(projectId, folderId) {
                var url = 'api/plm/projects/' + projectId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url)
            }

            function createProjectFolder(projectId, folder) {
                var url = 'api/plm/projects/' + projectId + "/folder";
                return httpFactory.post(url, folder)
            }

            function uploadProjectFolderFiles(projectId, folderId, files) {
                var url = 'api/plm/projects/' + projectId + "/folder/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function getProjectFolderChildren(projectId, folderId) {
                var url = 'api/plm/projects/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function moveProjectFileToFolder(folderId, file) {
                var url = 'api/plm/projects/' + folderId + "/move";
                return httpFactory.put(url, file);
            }

            function getAllProjectMembers(projectId) {
                var url = "api/plm/projects/team/members/" + projectId;
                return httpFactory.get(url);
            }

            function updateProjectFile(projectId, file) {
                var url = 'api/plm/projects/' + projectId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function getProjectDetails(projectId) {
                var url = "api/plm/projects/" + projectId + "/details";
                return httpFactory.get(url);
            }

            function updateProjectFileDescription(files) {
                var url = "api/plm/projects/project/file/" + files.fileId;
                return httpFactory.put(url, files);
            }

            function updateProjectTaskFileDescription(files) {
                var url = "api/plm/projects/project/task/file/" + files.fileId;
                return httpFactory.put(url, files);
            }

            function updateWbsItemSeq(actualId, targetId) {
                var url = "api/plm/projects/" + actualId + "/" + targetId + "/updateSequence";
                return httpFactory.get(url);
            }

            function pasteDeliverablesToProject(projectId, deliverables) {
                var url = "api/plm/projects/" + projectId + "/deliverables/paste";
                return httpFactory.put(url, deliverables);
            }

            function undoProjectDeliverables(projectId, deliverables) {
                var url = "api/plm/projects/" + projectId + "/deliverables/undo";
                return httpFactory.put(url, deliverables);
            }

            function undoCopiedFiles(projectId, files) {
                var url = "api/plm/projects/" + projectId + "/files/undo";
                return httpFactory.put(url, files)
            }

            function getLatestUploadedProjectFile(projectId, fileId) {
                var url = 'api/plm/projects/' + projectId + '/files/' + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function updateProjectSequenceNumbers(projectId) {
                var url = 'api/plm/projects/' + projectId + '/update/sequence';
                return httpFactory.get(url);
            }

            function getWorkflows(type) {
                var url = "api/plm/projects/workflow/" + type;
                return httpFactory.get(url);
            }

            function attachWorkflow(itemId, wfId) {
                var url = "api/plm/projects/" + itemId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteWorkflow(itemId) {
                var url = 'api/plm/projects/' + itemId + "/workflow/delete";
                return httpFactory.delete(url);
            }


            function finishProjectDeliverable(deliverable) {
                var url = 'api/plm/projects/deliverable/finish';
                return httpFactory.post(url, deliverable);
            }

            function getProjectFilesByName(project, name) {
                var url = "api/plm/projects/" + project + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function createProjectReqDocuments(project, projectReqDocument) {
                var url = "api/plm/projects/" + project + "/reqdocuments/multiple";
                return httpFactory.post(url, projectReqDocument)
            }

            function createProjectReqDocument(project, projectReqDocument) {
                var url = "api/plm/projects/" + project + "/reqdocuments";
                return httpFactory.post(url, projectReqDocument)
            }

            function updateProjectReqDocument(project, projectReqDocument) {
                var url = "api/plm/projects/" + project + "/reqdocuments/" + projectReqDocument.id;
                return httpFactory.put(url, projectReqDocument)
            }

            function deleteProjectReqDocument(project, projectReqDocumentId) {
                var url = "api/plm/projects/" + project + "/reqdocuments/" + projectReqDocumentId;
                return httpFactory.delete(url)
            }

            function getProjectReqDocuments(project) {
                var url = "api/plm/projects/" + project + "/reqdocuments";
                return httpFactory.get(url)
            }

            function subscribe(project) {
                var url = "api/plm/projects/subscribe/" + project;
                return httpFactory.post(url);
            }

            function getProjectAttributesWithHierarchy(objectType) {
                var url = "api/plm/projects/attributes/" + objectType + "?hierarchy=true";
                return httpFactory.get(url);
            }

            function createProjectPerson(projectId, projectPerson) {
                var url = "api/plm/projects/" + projectId + "/member";
                return httpFactory.post(url, projectPerson);
            }

            function updateProjectPerson(projectId, projectPerson) {
                var url = "api/plm/projects/" + projectId + "/member/" + projectPerson.id;
                return httpFactory.put(url, projectPerson);
            }

            function createProjectMultipleMembers(projectId, projectPersons) {
                var url = "api/plm/projects/" + projectId + "/member/multiple";
                return httpFactory.post(url, projectPersons);
            }

            function getProjectTasksAssignedToCount(projectId, personId) {
                var url = "api/plm/projects/" + projectId + "/person/" + personId + "/assignedTo/count";
                return httpFactory.get(url);
            }

        }
    }
);
