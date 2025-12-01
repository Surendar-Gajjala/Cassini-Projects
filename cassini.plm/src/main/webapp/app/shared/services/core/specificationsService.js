define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('SpecificationsService', SpecificationsService);

        function SpecificationsService($q, httpFactory) {
            return {
                createSpecification: createSpecification,
                saveAttributes: saveAttributes,
                saveImageValue: saveImageValue,
                findAllSpecifications: findAllSpecifications,
                findById: findById,
                getRmObjectFiles: getRmObjectFiles,
                updateFileDownloadHistory: updateFileDownloadHistory,
                getAllFileVersionComments: getAllFileVersionComments,
                deleteSpecFile: deleteSpecFile,
                getRevisionId: getRevisionId,
                getAttributesWithHierarchy: getAttributesWithHierarchy,
                getItemAttributes: getItemAttributes,
                getItem: getItem,
                getItemRevisionAttributes: getItemRevisionAttributes,
                createSpecAttribute: createSpecAttribute,
                updateSpecAttribute: updateSpecAttribute,
                createSection: createSection,
                createRequirement: createRequirement,
                getAllSpecSections: getAllSpecSections,
                getSectionChildren: getSectionChildren,
                updateRequirement: updateRequirement,
                getSpecRequirementsCounts: getSpecRequirementsCounts,
                getRequirement: getRequirement,
                finishRequirement: finishRequirement,
                updateSpecification: updateSpecification,
                getRequirementSearch: getRequirementSearch,
                getAllRequirements: getAllRequirements,
                getSpecificationDeliverable: getSpecificationDeliverable,
                getRequirementDeliverable: getRequirementDeliverable,
                createSpecificationDeliverable: createSpecificationDeliverable,
                getSpecificationDeliverables: getSpecificationDeliverables,
                getRequirementDeliverables: getRequirementDeliverables,
                createRequirementDeliverable: createRequirementDeliverable,
                deleteSpecificationDeliverable: deleteSpecificationDeliverable,
                deleteRequirementDeliverable: deleteRequirementDeliverable,
                subscribeSpecificationAndRequirement: subscribeSpecificationAndRequirement,
                getSubscribeByPerson: getSubscribeByPerson,
                getAllSubscribesByPerson: getAllSubscribesByPerson,
                deleteSpecification: deleteSpecification,
                deleteSpecElement: deleteSpecElement,
                promoteRequirement: promoteRequirement,
                demoteRequirement: demoteRequirement,
                getSpecificationRevisionHistory: getSpecificationRevisionHistory,
                reviseRequirement: reviseRequirement,
                specificationFreeTextSearch: specificationFreeTextSearch,
                updateSection: updateSection,
                updateSpecRequirement: updateSpecRequirement,
                reOrderRequirement: reOrderRequirement,
                acceptFinalData: acceptFinalData,
                createSpecPersons: createSpecPersons,
                getAllSpecPersons: getAllSpecPersons,
                getAllPersons: getAllPersons,
                createSpecPermission: createSpecPermission,
                deleteSpecPerson: deleteSpecPerson,
                updateSpecFile: updateSpecFile,
                importFile: importFile,
                getAllRequirementVersions: getAllRequirementVersions,
                getParentObject: getParentObject,
                getAllRequirementVersionAttributes: getAllRequirementVersionAttributes,
                getVersionAttributes: getVersionAttributes,
                getRequirementAttributeSearch: getRequirementAttributeSearch,
                updateSpecFileName: updateSpecFileName,
                updateReqFileName: updateReqFileName,
                getRequirementItemDeliverables: getRequirementItemDeliverables,
                createRequirementDeliverables: createRequirementDeliverables,
                getRequirementDeliverableItems: getRequirementDeliverableItems,
                deleteDeliverable: deleteDeliverable,
                deleteSpecFolder: deleteSpecFolder,
                createSpecFolder: createSpecFolder,
                uploadSpecFolderFiles: uploadSpecFolderFiles,
                getSpecFolderChildren: getSpecFolderChildren,
                moveSpecFileToFolder: moveSpecFileToFolder,
                getSpecificationDetails: getSpecificationDetails,
                getReqDetails: getReqDetails,
                updateFileDescription: updateFileDescription,
                getLatestUploadedObjectFile: getLatestUploadedObjectFile,
                pasteObjectFilesFromClipboard: pasteObjectFilesFromClipboard,
                undoCopiedObjectFiles: undoCopiedObjectFiles,
                requirementFreeTextSearch: requirementFreeTextSearch,
                getRequirements: getRequirements,
                getWorkflows: getWorkflows,
                attachWorkflow: attachWorkflow,
                deleteWorkflow: deleteWorkflow,
                getObjectType: getObjectType,
                getSpecFilesByName: getSpecFilesByName

            };

            function createSpecification(specification) {
                var url = "api/rm/specifications";
                return httpFactory.post(url, specification);
            }

            function updateSpecification(specification) {
                var url = "api/rm/specifications/update/" + specification.id;
                return httpFactory.put(url, specification);
            }


            function saveAttributes(specificationId, attributes) {
                var url = "api/rm/specifications/" + specificationId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function saveImageValue(objectId, attributeId, file) {
                var url = "api/rm/specifications/updateImageValue/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function findAllSpecifications(pageable) {
                var url = "api/rm/specifications?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function findById(id) {
                var url = "api/rm/specifications/" + id;
                return httpFactory.get(url);
            }

            function getRmObjectFiles(specId) {
                var url = 'api/rm/specifications/' + specId + "/files";
                return httpFactory.get(url);
            }


            function getAllFileVersionComments(specId, fileId, objectType) {
                var url = 'api/rm/specifications/' + specId + "/files/" + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }

            function deleteSpecFile(specId, fileId) {
                var url = 'api/rm/specifications/' + specId + "/files/" + fileId;
                return httpFactory.delete(url);
            }


            function getRevisionId(revisionId) {
                var url = "api/rm/specifications/" + revisionId;
                return httpFactory.get(url);
            }


            function getAttributesWithHierarchy(specTypeId) {
                var url = "api/rm/specifications/spec/" + specTypeId + "/specAttributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getItemAttributes(specId) {
                var url = "api/rm/specifications/" + specId + "/attributes";
                return httpFactory.get(url);
            }


            function getItem(itemId) {
                var url = "api/rm/specifications/revisions/" + itemId;
                return httpFactory.get(url);
            }


            function getItemRevisionAttributes(revisionId) {
                var url = "api/rm/specifications/" + revisionId + "/revisionAttributes";
                return httpFactory.get(url);
            }


            function createSpecAttribute(itemId, attribute) {
                var url = "api/rm/specifications/" + itemId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateSpecAttribute(itemId, attribute) {
                var url = "api/rm/specifications/" + itemId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function createSection(section) {
                var url = "api/rm/specifications/specSection";
                return httpFactory.post(url, section);
            }

            function createRequirement(requirement) {
                var url = "api/rm/specifications/specRequirement";
                return httpFactory.post(url, requirement);
            }

            function getAllSpecSections(specId) {
                var url = "api/rm/specifications/allSpecSections/" + specId;
                return httpFactory.get(url);
            }

            function getSectionChildren(specId, sectionId) {
                var url = "api/rm/specifications/allSpecSections/" + specId + "/children/" + sectionId;
                return httpFactory.get(url);
            }


            function getRequirement(id) {
                var url = "api/rm/requirements/" + id;
                return httpFactory.get(url);
            }

            function updateRequirement(requirement) {
                var url = "api/rm/specifications/updateRequirement/" + requirement.id;
                return httpFactory.put(url, requirement);
            }

            function finishRequirement(requirement) {
                var url = "api/rm/specifications/finishRequirement/" + requirement.id;
                return httpFactory.put(url, requirement);
            }

            function getSpecRequirementsCounts(specId) {
                var url = "api/rm/specifications" + specId + "/requirements/counts";
                return httpFactory.get(url);
            }

            function getRequirementSearch(specId, pageable, filter) {
                var url = "api/rm/specifications/requirementSearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&specification={1}&assignedTo={2}&status={3}&plannedFinishdate={4}&name={5}&description={6}&objectNumber={7}&version={8}&attributeSearch={9}"
                    .format(filter.searchQuery, specId, filter.assignedTo, filter.status, filter.plannedFinishdate, filter.name, filter.description, filter.objectNumber, filter.version, filter.attributeSearch);
                return httpFactory.get(url);
            }

            function getRequirementAttributeSearch(specId, pageable, filter) {
                var url = "api/rm/specifications/AttributeSearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.post(url, filter);

            }

            function getAllRequirements(personId, pageable) {
                var url = "api/rm/requirements/assignedTo/" + personId + "/?page={0}&size={1}&sort{2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getSpecificationDeliverable(project, pageable, filters) {
                var url = "api/plm/projects/" + project + "/specificationDeliverable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&description={1}&type={2}"
                    .format(filters.name, filters.description, filters.type);
                return httpFactory.get(url);
            }


            function getRequirementDeliverable(project, pageable, filters) {
                var url = "api/plm/projects/" + project + "/requirementDeliverable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&description={1}&type={2}"
                    .format(filters.name, filters.description, filters.type);
                return httpFactory.get(url);

            }


            function createSpecificationDeliverable(projectId, objectType, glossarys) {
                var url = "api/plm/projects/" + projectId + "/" + objectType + "/specificationDeliverables/multiple";
                return httpFactory.post(url, glossarys);
            }

            function getSpecificationDeliverables(projectId) {
                var url = "api/plm/projects/" + projectId + "/specDeliverables";
                return httpFactory.get(url);
            }

            function getRequirementDeliverables(projectId) {
                var url = "api/plm/projects/" + projectId + "/reqDeliverables";
                return httpFactory.get(url);
            }

            function createRequirementDeliverable(projectId, objectType, glossarys) {
                var url = "api/plm/projects/" + projectId + "/" + objectType + "/requirementDeliverables/multiple";
                return httpFactory.post(url, glossarys);
            }


            function deleteSpecificationDeliverable(projectId, specId) {
                var url = "api/plm/projects/" + projectId + "/specificationDeliverables/" + specId;
                return httpFactory.delete(url);
            }

            function deleteRequirementDeliverable(projectId, reqId) {
                var url = "api/plm/projects/" + projectId + "/requirementDeliverables/" + reqId;
                return httpFactory.delete(url);
            }

            function subscribeSpecificationAndRequirement(specReq) {
                var url = "api/rm/specifications/subscribe/" + specReq;
                return httpFactory.post(url);
            }

            function getSubscribeByPerson(specOrReqId, personId) {
                var url = "api/rm/specifications/subscribe/" + specOrReqId + "/" + personId;
                return httpFactory.get(url);
            }


            function getAllSubscribesByPerson(personId, pageable) {
                var url = "api/rm/specifications/subscribe/person/" + personId + "/all?page={0}&size={1}&sort{2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function deleteSpecification(specId) {
                var url = 'api/rm/specifications/delete/' + specId;
                return httpFactory.delete(url);
            }


            function deleteSpecElement(id) {
                var url = 'api/rm/specifications/specElement/' + id;
                return httpFactory.delete(url);
            }


            function getParentObject(id) {
                var url = 'api/rm/specifications/parent/section/' + id;
                return httpFactory.get(url);
            }

            function getAllRequirementVersionAttributes(objectNumber, specId) {
                var url = 'api/rm/specifications/requirement/versions/' + objectNumber + "/" + specId;
                return httpFactory.get(url);
            }


            function getVersionAttributes(reqIds) {
                var url = "api/rm/specifications/version/attributes/[" + reqIds + "]";
                return httpFactory.get(url);
            }


            function promoteRequirement(reqId) {
                var url = "api/rm/specifications/" + reqId + "/promote";
                return httpFactory.put(url);
            }

            function demoteRequirement(reqId) {
                var url = "api/rm/specifications/" + reqId + "/demote";
                return httpFactory.put(url);
            }

            function getSpecificationRevisionHistory(reqId) {
                var url = "api/rm/specifications/" + reqId + "/revisionHistory";
                return httpFactory.get(url);
            }

            function reviseRequirement(reqId) {
                var url = "api/rm/specifications/" + reqId + "/revise";
                return httpFactory.put(url);
            }

            function specificationFreeTextSearch(criteria, pageable) {
                var url = "api/rm/specifications/specSearch";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(criteria);
                return httpFactory.get(url);
            }

            function acceptFinalData(reqId) {
                var url = "api/rm/specifications/" + reqId + "/ ";
                return httpFactory.get(url);
            }

            function updateSection(section) {
                var url = "api/rm/specifications/specSection/update/" + section.id;
                return httpFactory.put(url, section);
            }

            function updateSpecRequirement(specRequirement, dragRowParent) {
                var url = "api/rm/specifications/update/specRequirement/" + specRequirement.id + "/" + dragRowParent;
                return httpFactory.put(url, specRequirement);
            }

            function reOrderRequirement(specRequirement, targetId, targerParent) {
                var url = "api/rm/specifications/reOrder/specRequirement/" + targetId + "/" + targerParent;
                return httpFactory.put(url, specRequirement);
            }

            function updateSection(section) {
                var url = "api/rm/specifications/specSection/update/" + section.id;
                return httpFactory.put(url, section);
            }


            function createSpecPersons(specId, persons) {
                var url = "api/rm/specifications/" + specId + "/permission/add";
                return httpFactory.post(url, persons);
            }

            function getAllSpecPersons(specId) {
                var url = "api/rm/specifications/" + specId + "/specPersons";
                return httpFactory.get(url);
            }

            function getAllPersons(specId) {
                var url = "api/rm/specifications/" + specId + "/persons";
                return httpFactory.get(url);
            }

            function createSpecPermission(spec) {
                var url = "api/rm/specifications/specPermission";
                return httpFactory.post(url, spec);
            }

            function deleteSpecPerson(spec) {
                var url = 'api/rm/specifications/delete/specPerson/' + spec;
                return httpFactory.delete(url);
            }

            function updateSpecFile(specId, file) {
                var url = 'api/rm/specifications/' + specId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function importFile(specId, file, format) {
                var url = 'api/rm/specifications/' + specId + "/import?format=" + format;
                return httpFactory.upload(url, file);
            }


            function getAllRequirementVersions(specId) {
                var url = "api/rm/specifications/reqVersions/" + specId;
                return httpFactory.get(url);
            }

            function updateSpecFileName(specId, fileId, newFileName) {
                var url = 'api/rm/specifications/' + specId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function updateReqFileName(reqId, fileId, newFileName) {
                var url = 'api/rm/requirements/' + reqId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function updateFileDownloadHistory(specId, fileId) {
                var url = 'api/rm/specifications/' + specId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getRequirementItemDeliverables(reqId, pageable, filters) {
                var url = "api/rm/requirements/" + reqId + "/itemDeliverable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&itemNumber={1}&itemType={2}&description={3}"
                    .format(filters.name, filters.itemNumber, filters.itemType, filters.description);
                return httpFactory.get(url);
            }

            function createRequirementDeliverables(reqId, items) {
                var url = "api/rm/requirements/" + reqId + "/deliverables/multiple";
                return httpFactory.post(url, items);
            }

            function getRequirementDeliverableItems(reqId) {
                var url = "api/rm/requirements/" + reqId + "/reqDeliverables";
                return httpFactory.get(url);
            }

            function deleteDeliverable(reqId, itemId) {
                var url = "api/rm/requirements/" + reqId + "/deliverable/" + itemId;
                return httpFactory.delete(url);
            }

            function deleteSpecFolder(specId, folderId) {
                var url = 'api/rm/specifications/' + specId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url)
            }

            function createSpecFolder(specId, folder) {
                var url = 'api/rm/specifications/' + specId + "/folder";
                return httpFactory.post(url, folder)
            }

            function uploadSpecFolderFiles(specId, folderId, files) {
                var url = 'api/rm/specifications/' + specId + "/folder/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function getSpecFolderChildren(specId, folderId) {
                var url = 'api/rm/specifications/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function moveSpecFileToFolder(folderId, file) {
                var url = 'api/rm/specifications/' + folderId + "/move";
                return httpFactory.put(url, file);
            }

            function getSpecificationDetails(specId) {
                var url = 'api/rm/specifications/' + specId + "/details";
                return httpFactory.get(url);
            }

            function getReqDetails(specId) {
                var url = 'api/rm/requirements/' + specId + "/details";
                return httpFactory.get(url);
            }

            function updateFileDescription(files) {
                var url = "api/rm/specifications/files/" + files.fileId;
                return httpFactory.put(url, files);
            }

            function getLatestUploadedObjectFile(specId, fileId) {
                var url = 'api/rm/specifications/' + specId + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function pasteObjectFilesFromClipboard(specId, fileId, files) {
                var url = 'api/rm/specifications/' + specId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedObjectFiles(specId, files) {
                var url = 'api/rm/specifications/' + specId + "/files/undo";
                return httpFactory.put(url, files);
            }

            function requirementFreeTextSearch(freeText, pageable) {
                var url = "api/rm/requirements/freeTextSearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function getRequirements(pageable) {
                var url = "api/rm/requirements/all/?page={0}&size={1}&sort{2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getWorkflows(typeId,type) {
                var url = "api/rm/specifications/workflow/" + typeId + "/specType/" + type;
                return httpFactory.get(url);
            }

            function attachWorkflow(itemId, wfId) {
                var url = "api/rm/specifications/" + itemId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteWorkflow(itemId) {
                var url = 'api/rm/specifications/' + itemId + "/workflow/delete";
                return httpFactory.delete(url);
            }

            function getObjectType(id){
                var url = "api/rm/specifications/type/" + id;
                return httpFactory.get(url);
            }

            function getSpecFilesByName(spec, name) {
                var url = "api/rm/specifications/" + spec + "/byName/" + name;
                return httpFactory.get(url);
            }

        }
    }
);
