
/**
 * Created by swapna on 1/4/18.
 */

define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ActivityService', ActivityService);

        function ActivityService(httpFactory) {
            return {
                createActivity: createActivity,
                createActivites: createActivites,
                getActivity: getActivity,
                updateActivity: updateActivity,
                deleteActivity: deleteActivity,

                getActivityFiles: getActivityFiles,
                uploadActivityFiles: uploadActivityFiles,
                deleteActivityFile: deleteActivityFile,

                createActivityDeliverable: createActivityDeliverable,
                getActivityDeliverable: getActivityDeliverable,
                updateActivityDeliverable: updateActivityDeliverable,
                deleteActivityDeliverable: deleteActivityDeliverable,
                getActivityDeliverables: getActivityDeliverables,
                createActivityDeliverables: createActivityDeliverables,

                createActivityTask: createActivityTask,
                createActivityTasks:createActivityTasks,
                getActivityTask: getActivityTask,
                updateActivityTask: updateActivityTask,
                deleteActivityTask: deleteActivityTask,
                getActivityTasks: getActivityTasks,

                getTaskFiles: getTaskFiles,
                uploadTaskFiles: uploadTaskFiles,
                deleteTaskFile: deleteTaskFile,
                getActivityFileVersions: getActivityFileVersions,
                getActivityPercentComplete: getActivityPercentComplete,
                getPersonTasks: getPersonTasks,
                getActivityTaskByName: getActivityTaskByName,
                updateActivityFileDownloadHistory: updateActivityFileDownloadHistory,

                createTaskDeliverables: createTaskDeliverables,
                getTaskDeliverables: getTaskDeliverables,
                getTaskFileVersions: getTaskFileVersions,
                updateTaskFileDownloadHistory: updateTaskFileDownloadHistory,
                getDeliverablesByActivity: getDeliverablesByActivity,
                getItemsByActivity: getItemsByActivity,
                deleteActivityItemReference: deleteActivityItemReference,
                getItemsByTask: getItemsByTask,
                deleteTaskItemReference: deleteTaskItemReference,
                saveActivityItemReferences: saveActivityItemReferences,
                saveTaskItemReferences: saveTaskItemReferences,
                getProjectItems: getProjectItems,
                getActivityItems: getActivityItems,
                deleteTaskDeliverable: deleteTaskDeliverable,
                updateTaskFile: updateTaskFile,
                updateActivityFile: updateActivityFile,

                getActivityDeliverableByActivity: getActivityDeliverableByActivity,
                getTaskDeliverableByTask: getTaskDeliverableByTask,
                getDeliverablesByTask: getDeliverablesByTask,
                getActivityFileVersionAndCommentsAndDownloads: getActivityFileVersionAndCommentsAndDownloads,
                getTaskFileVersionsAndCommentsAndDownloads: getTaskFileVersionsAndCommentsAndDownloads,
                getGlossaryDeliverablesByActivity: getGlossaryDeliverablesByActivity,
                getFilteredItemDeliverables: getFilteredItemDeliverables,
                getFilteredGlossaryDeliverables: getFilteredGlossaryDeliverables,
                createGlossaryDeliverables: createGlossaryDeliverables,
                getAllActivityDeliverables: getAllActivityDeliverables,
                getAllTaskDeliverables: getAllTaskDeliverables,
                activityFileName: activityFileName,
                activityTaskFileName: activityTaskFileName,
                getForgeAuthentication: getForgeAuthentication,
                showForgeFile: showForgeFile,
                getPersonActivitys: getPersonActivitys,
                getPersonActivityTasks: getPersonActivityTasks,
                updateFileDescription: updateFileDescription,
                deleteActivityFolder: deleteActivityFolder,
                createActivityFolder: createActivityFolder,
                uploadActivityFolderFiles: uploadActivityFolderFiles,
                getActivityFolderChildren: getActivityFolderChildren,
                moveActivityFileToFolder: moveActivityFileToFolder,
                deleteActivityTaskFolder: deleteActivityTaskFolder,
                createActivityTaskFolder: createActivityTaskFolder,
                uploadActivityTaskFolderFiles: uploadActivityTaskFolderFiles,
                moveActivityTaskFileToFolder: moveActivityTaskFileToFolder,
                getActivityTaskFolderChildren: getActivityTaskFolderChildren,
                getActivityCount: getActivityCount,
                getTaskCount: getTaskCount,
                getAssignedTasks: getAssignedTasks,
                getLatestUploadedTaskFile: getLatestUploadedTaskFile,
                getLatestUploadedActivityFile: getLatestUploadedActivityFile,
                updateWbsChildrenSeq: updateWbsChildrenSeq,
                updateActivityTaskSeq: updateActivityTaskSeq,
                pasteActivityFilesFromClipboard: pasteActivityFilesFromClipboard,
                pasteTaskFilesFromClipboard: pasteTaskFilesFromClipboard,
                pasteDeliverablesToActivity: pasteDeliverablesToActivity,
                pasteDeliverablesToTask: pasteDeliverablesToTask,
                undoCopiedActivityFiles: undoCopiedActivityFiles,
                undoCopiedTaskFiles: undoCopiedTaskFiles,
                undoActivityDeliverables: undoActivityDeliverables,
                undoTaskDeliverables: undoTaskDeliverables,
                getAllSpecDeliverables: getAllSpecDeliverables,
                getAllReqDeliverables: getAllReqDeliverables,
                finishActivity: finishActivity,
                attachWorkflow: attachWorkflow,
                deleteWorkflow: deleteWorkflow,
                finishActivityAndTaskDeliverable: finishActivityAndTaskDeliverable,
                getActivityFilesByName: getActivityFilesByName,
                getTaskFilesByName: getTaskFilesByName,
                attachTaskWorkflow: attachTaskWorkflow,
                deleteTaskWorkflow: deleteTaskWorkflow
            };

            function createActivity(activity) {
                var url = "api/plm/projects/wbs/activities";
                return httpFactory.post(url, activity);
            }

            function createActivites(projectId, activityList) {
                var url = "api/plm/projects/wbs/activities/activityList/"+projectId;
                return httpFactory.post(url, activityList);
            }

            function getForgeAuthentication() {
                var url = "api/plm/forge/authenticate";
                return httpFactory.get(url);
            }

            function showForgeFile(urn) {
                var url = "api/plm/forge/view/" + urn;
                return httpFactory.get(url);
            }

            function getActivity(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId;
                return httpFactory.get(url);
            }

            function getActivityPercentComplete(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/percentComplete";
                return httpFactory.get(url);
            }

            function updateActivity(activity) {
                var url = "api/plm/projects/wbs/activities/" + activity.id;
                return httpFactory.put(url, activity);
            }

            function deleteActivity(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId;
                return httpFactory.delete(url);
            }

            function getActivityFiles(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/files";
                return httpFactory.get(url);
            }

            function uploadActivityFiles(activityId, files) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/files";
                return httpFactory.post(url, files);
            }

            function deleteActivityFile(activityId, fileId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function createActivityDeliverable(activityId, deliverable) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/deliverables";
                return httpFactory.post(url, deliverable);
            }

            function createActivityDeliverables(activityId, deliverable) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/deliverables/multiple";
                return httpFactory.post(url, deliverable);
            }

            function createGlossaryDeliverables(activityId, deliverable) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/glossaryDeliverables/multiple";
                return httpFactory.post(url, deliverable);
            }

            function getActivityDeliverable(activityId, deliverableId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/deliverables/" + deliverableId;
                return httpFactory.get(url);
            }

            function updateActivityDeliverable(activityId, deliverable) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/deliverables/" + deliverable.id;
                return httpFactory.put(url, deliverable);
            }

            function deleteActivityDeliverable(activityId, deliverableId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/deliverables/" + deliverableId;
                return httpFactory.delete(url);
            }

            function getActivityDeliverables(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/deliverables";
                return httpFactory.get(url);
            }

            function getAllActivityDeliverables(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/allDeliverables";
                return httpFactory.get(url);
            }

            function getAllSpecDeliverables(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/specDeliverables";
                return httpFactory.get(url);
            }

            function getAllReqDeliverables(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/reqDeliverables";
                return httpFactory.get(url);
            }

            function createActivityTask(activityId, task) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks";
                return httpFactory.post(url, task);
            }

            function createActivityTasks(projectId, activityId, taskList) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/taskList/"+ projectId;
                return httpFactory.post(url, taskList);
            }

            function getActivityTaskByName(activityId, taskName) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/byName?taskName=" + taskName;
                return httpFactory.get(url);
            }

            function getActivityTask(activityId, taskId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId;
                return httpFactory.get(url);
            }

            function updateActivityTask(activityId, task) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + task.id;
                return httpFactory.put(url, task);
            }

            function deleteActivityTask(activityId, taskId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId;
                return httpFactory.delete(url);
            }

            function getActivityTasks(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks";
                return httpFactory.get(url);
            }

            function getTaskFiles(activityId, taskId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/files";
                return httpFactory.get(url);
            }

            function uploadTaskFiles(activityId, taskId, files) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/files";
                return httpFactory.post(url, files);
            }

            function deleteTaskFile(activityId, taskId, fileId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function updateTaskFile(activityId, taskId, file) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/tasks/" +taskId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function updateActivityFile(activityId, file) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function getTaskFileVersions(activityId, taskId, fileId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }

            function getTaskFileVersionsAndCommentsAndDownloads(activityId, taskId, fileId, type) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/files/" + fileId + "/versionComments/" + type;
                return httpFactory.get(url);
            }

            function getDeliverablesByActivity(activityId, pageable) {
                var url = "api/plm/projects/wbs/activities/deliverables/activity/" + activityId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getGlossaryDeliverablesByActivity(activityId, pageable) {
                var url = "api/plm/projects/wbs/activities/glossaryDeliverables/activity/" + activityId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getFilteredItemDeliverables(filter, pageable) {
                var url = "api/plm/projects/wbs/activities/items/filter?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&objectId={0}&objectType={1}&itemType={2}&itemNumber={3}&name={4}&description={5}"
                    .format(filter.objectId, filter.objectType, filter.itemType, filter.itemNumber, filter.name, filter.description);
                return httpFactory.get(url);
            }

            function getFilteredGlossaryDeliverables(filter, pageable) {
                var url = "api/plm/projects/wbs/activities/glossary/filter?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&objectId={0}&objectType={1}&itemType={2}&itemNumber={3}&name={4}&description={5}"
                    .format(filter.objectId, filter.objectType, filter.itemType, filter.itemNumber, filter.name, filter.description);
                return httpFactory.get(url);
            }

            function getActivityDeliverableByActivity(activityId, pageable, criteria) {
                var url = "api/plm/projects/wbs/activities/deliverables/byActivity/" + activityId + "?page={0}&size={1}&sort={2}:{3}".
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

            function getDeliverablesByTask(taskId, pageable) {
                var url = "api/plm/projects/wbs/activities/deliverables/task/" + taskId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getTaskDeliverableByTask(taskId, pageable, criteria) {
                var url = "api/plm/projects/wbs/activities/deliverables/byTask/" + taskId + "?page={0}&size={1}&sort={2}:{3}".
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

            function getActivityFileVersions(activityId, fileId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }

            function getActivityFileVersionAndCommentsAndDownloads(activityId, fileId, type) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/files/" + fileId + "/versionComments/" + type;
                return httpFactory.get(url);
            }

            function getPersonTasks(personId, pageable) {
                var url = "api/plm/projects/wbs/activities/activityStructure/" + personId + "?page={0}&size={1}&sort{2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function updateActivityFileDownloadHistory(activityId, fileId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function updateTaskFileDownloadHistory(activityId, taskId, fileId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getTaskDeliverables(activityId, taskId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/deliverables";
                return httpFactory.get(url);
            }

            function getAllTaskDeliverables(activityId, taskId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/allDeliverables";
                return httpFactory.get(url);
            }

            function deleteTaskDeliverable(activityId, taskId, deliverable) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/deliverables/" + deliverable;
                return httpFactory.delete(url);
            }

            function createTaskDeliverables(activityId, taskId, deliverables) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/deliverables/multiple";
                return httpFactory.post(url, deliverables);
            }

            function getItemsByActivity(activityId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/referenceItems";
                return httpFactory.get(url);
            }

            function deleteActivityItemReference(activityId, referenceItem) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/referenceItems/" + referenceItem.id;
                return httpFactory.delete(url);
            }

            function saveActivityItemReferences(activityId, itemReferences) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/referenceItems/multiple";
                return httpFactory.post(url, itemReferences);
            }

            function saveTaskItemReferences(activityId, taskId, itemReferences) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/referenceItems/multiple";
                return httpFactory.post(url, itemReferences);
            }

            function getItemsByTask(activityId, taskId) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/referenceItems";
                return httpFactory.get(url);
            }

            function deleteTaskItemReference(activityId, taskId, referenceItem) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/referenceItems/" + referenceItem.id;
                return httpFactory.delete(url);
            }

            function getProjectItems(activityId, pageable) {
                var url = "api/plm/projects/wbs/activities/items/" + activityId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);

            }

            function getActivityItems(activityId, taskId, pageable) {
                var url = "api/plm/projects/wbs/activities/" + activityId + "/tasks/" + taskId + "/items?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);

            }

            function activityFileName(activityId, fileId, newFileName) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function activityTaskFileName(activityId, taskId, fileId, newFileName) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/tasks/" + taskId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function getPersonActivitys(personId, pageable) {
                var url = "api/plm/projects/wbs/activities/personActivity/" + personId + "?page={0}&size={1}&sort{2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getPersonActivityTasks(personId, pageable) {
                var url = "api/plm/projects/wbs/activities/personActivityTask/" + personId + "?page={0}&size={1}&sort{2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function updateFileDescription(files) {
                var url = "api/plm/projects/wbs/activities/file/" + files.fileId;
                return httpFactory.put(url, files);
            }


            function deleteActivityFolder(activityId, folderId) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url)
            }

            function createActivityFolder(activityId, folder) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/folder";
                return httpFactory.post(url, folder)
            }

            function uploadActivityFolderFiles(activityId, folderId, files) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/folder/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function getActivityFolderChildren(activityId, folderId) {
                var url = 'api/plm/projects/wbs/activities/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function getActivityTaskFolderChildren(activityId, folderId) {
                var url = 'api/plm/projects/wbs/activities/tasks/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function moveActivityFileToFolder(folderId, file) {
                var url = 'api/plm/projects/wbs/activities/' + folderId + "/move";
                return httpFactory.put(url, file);
            }

            function deleteActivityTaskFolder(taskId, folderId) {
                var url = "api/plm/projects/wbs/activities/tasks/" + taskId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url);
            }

            function createActivityTaskFolder(taskId, folder) {
                var url = "api/plm/projects/wbs/activities/tasks/" + taskId + "/folder";
                return httpFactory.post(url, folder);
            }

            function uploadActivityTaskFolderFiles(activityId, taskId, folderId, files) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/tasks/" + taskId + "/folder/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function moveActivityTaskFileToFolder(folderId, file) {
                var url = 'api/plm/projects/wbs/activities/tasks/' + folderId + "/move";
                return httpFactory.put(url, file);
            }

            function getActivityCount(activityId) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/details";
                return httpFactory.get(url);
            }

            function getTaskCount(taskId) {
                var url = 'api/plm/projects/wbs/activities/tasks/' + taskId + "/details";
                return httpFactory.get(url);
            }

            function getAssignedTasks(personId, pageable) {
                var url = "api/plm/projects/wbs/activities/tasks/" + personId + "?page={0}&size={1}&sort{2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getLatestUploadedTaskFile(taskId, fileId) {
                var url = 'api/plm/projects/wbs/activities/tasks/' + taskId + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function getLatestUploadedActivityFile(taskId, fileId) {
                var url = 'api/plm/projects/wbs/activities/' + taskId + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function updateWbsChildrenSeq(actualId, targetId) {
                var url = 'api/plm/projects/wbs/activities/' + actualId + "/" + targetId + "/updateSequence";
                return httpFactory.get(url);
            }

            function updateActivityTaskSeq(activityId, actualId, targetId) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + '/tasks/' + actualId + "/" + targetId + "/updateSequence";
                return httpFactory.get(url);
            }

            function pasteActivityFilesFromClipboard(activityId, fileId, files) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function pasteTaskFilesFromClipboard(taskId, fileId, files) {
                var url = 'api/plm/projects/wbs/activities/tasks/' + taskId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function pasteDeliverablesToActivity(activityId, deliverables) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/deliverables/paste";
                return httpFactory.put(url, deliverables);
            }

            function pasteDeliverablesToTask(taskId, deliverables) {
                var url = 'api/plm/projects/wbs/activities/tasks/' + taskId + "/deliverables/paste";
                return httpFactory.put(url, deliverables);
            }

            function undoActivityDeliverables(activityId, deliverables) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/deliverables/undo";
                return httpFactory.put(url, deliverables);
            }

            function undoTaskDeliverables(taskId, deliverables) {
                var url = 'api/plm/projects/wbs/activities/tasks/' + taskId + "/deliverables/undo";
                return httpFactory.put(url, deliverables);
            }

            function undoCopiedActivityFiles(activityId, files) {
                var url = 'api/plm/projects/wbs/activities/' + activityId + "/files/undo";
                return httpFactory.put(url, files);
            }

            function undoCopiedTaskFiles(activityId, files) {
                var url = 'api/plm/projects/wbs/activities/tasks/' + activityId + "/files/undo";
                return httpFactory.put(url, files);
            }

            function finishActivity(activityId) {
                var url = 'api/plm/projects/wbs/activities/finish/' + activityId;
                return httpFactory.post(url);
            }

            function finishActivityAndTaskDeliverable(type, deliverable) {
                var url = 'api/plm/projects/wbs/activities/deliverable/' + type + '/finish';
                return httpFactory.post(url, deliverable);
            }

            function attachWorkflow(itemId, wfId) {
                var url = "api/plm/projects/wbs/activities/" + itemId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }
            function deleteWorkflow(itemId) {
                var url = 'api/plm/projects/wbs/activities/' + itemId + "/workflow/delete";
                return httpFactory.delete(url);
            }

            function getActivityFilesByName(activity, name) {
                var url = "api/plm/projects/wbs/activities/" + activity + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function getTaskFilesByName(task, name) {
                var url = "api/plm/projects/wbs/activities/task/" + task + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function attachTaskWorkflow(itemId, wfId) {
                var url = "api/plm/projects/wbs/activities/task/" + itemId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteTaskWorkflow(itemId) {
                var url = 'api/plm/projects/wbs/activities/task/' + itemId + "/workflow/delete";
                return httpFactory.delete(url);
            }
        }
    }
);