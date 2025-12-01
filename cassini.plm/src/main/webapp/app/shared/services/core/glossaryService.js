define(['app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('GlossaryService', GlossaryService);

        function GlossaryService(httpFactory) {
            return {
                createGlossary: createGlossary,
                updateGlossary: updateGlossary,
                deleteGlossary: deleteGlossary,
                getGlossarys: getGlossarys,
                getAllGlossarys: getAllGlossarys,
                getGlossary: getGlossary,
                getAllPhases: getAllPhases,
                glossaryFreeTextSearch: glossaryFreeTextSearch,
                glossaryEntryFreeTextSearch: glossaryEntryFreeTextSearch,

                getAllEntries: getAllEntries,
                getAllEntriesByLanguage: getAllEntriesByLanguage,

                getGlossaryFiles: getGlossaryFiles,
                updateGlossaryFileDownloadHistory: updateGlossaryFileDownloadHistory,
                deleteGlossaryFile: deleteGlossaryFile,
                getGlossaryFileVersions: getGlossaryFileVersions,
                getGlossaryFileDownloadHistory: getGlossaryFileDownloadHistory,

                createGlossaryEntry: createGlossaryEntry,
                updateGlossaryEntry: updateGlossaryEntry,
                deleteGlossaryEntry: deleteGlossaryEntry,
                getAllGlossaryEntries: getAllGlossaryEntries,

                promoteGlossary: promoteGlossary,
                demoteGlossary: demoteGlossary,
                reviseGlossary: reviseGlossary,

                deleteGlossaryEntryItem: deleteGlossaryEntryItem,
                getGlossaryEntryItems: getGlossaryEntryItems,
                saveGlossaryEntryItems: saveGlossaryEntryItems,

                getGlossaryRevisionHistory: getGlossaryRevisionHistory,
                getEntryVersions: getEntryVersions,
                getEntryVersionsByLanguage: getEntryVersionsByLanguage,
                importGlossaryEntryItems: importGlossaryEntryItems,
                printGlossary: printGlossary,
                getGlossaryFileVersionsAndCommentsAndDownloads: getGlossaryFileVersionsAndCommentsAndDownloads,
                addGlossaryEntryItem: addGlossaryEntryItem,
                getAllProjectGlossaryDeliverables: getAllProjectGlossaryDeliverables,
                createGlossaryDeliverable: createGlossaryDeliverable,
                getProjectGlossaries: getProjectGlossaries,
                deleteGlossaryDeliverable: deleteGlossaryDeliverable,
                getProjectGlossaryDeliverable: getProjectGlossaryDeliverable,

                createGlossaryLanguage: createGlossaryLanguage,
                deleteGlossaryLanguage: deleteGlossaryLanguage,
                getGlossaryLanguages: getGlossaryLanguages,
                updateGlossaryLanguage: updateGlossaryLanguage,

                updateGlossaryDetail: updateGlossaryDetail,
                getGlossaryDetails: getGlossaryDetails,
                printGlossaryByLanguage: printGlossaryByLanguage,
                entryFreeTextSearch: entryFreeTextSearch,
                glossaryEntrySearchItem: glossaryEntrySearchItem,
                setGlossaryLanguage: setGlossaryLanguage,
                updateGlossaryEntryEdit: updateGlossaryEntryEdit,
                getEntryEditHistory: getEntryEditHistory,
                acceptEntryEditChange: acceptEntryEditChange,
                rejectEntryEditChange: rejectEntryEditChange,
                approveEntryEdits: approveEntryEdits,
                getLastAcceptedEntryEdit: getLastAcceptedEntryEdit,


                createGlossaryPersons: createGlossaryPersons,
                getAllGlossaryPersons: getAllGlossaryPersons,
                getAllPersons: getAllPersons,
                createGlossaryPermission: createGlossaryPermission,
                deleteGlossaryPerson: deleteGlossaryPerson,
                getAllTypeAttributes: getAllTypeAttributes,
                updateFileName: updateFileName,
                createGlossaryFolder: createGlossaryFolder,
                uploadGlossaryFolderFiles: uploadGlossaryFolderFiles,
                getGlossaryFolderChildren: getGlossaryFolderChildren,
                deleteGlossaryFolder: deleteGlossaryFolder,
                moveGlossaryFileToFolder: moveGlossaryFileToFolder,
                getGlossaryCounts: getGlossaryCounts,
                getLatestUploadedGlossaryFile: getLatestUploadedGlossaryFile,
                updateFileDescription: updateFileDescription,
                pasteGlossaryFilesFromClipboard: pasteGlossaryFilesFromClipboard,
                undoCopiedFiles: undoCopiedFiles,
                getGlossaryFilesByName: getGlossaryFilesByName
            };

            function createGlossary(glossary) {
                var url = "api/rm/glossarys";
                return httpFactory.post(url, glossary);
            }

            function updateGlossary(glossary) {
                var url = "api/rm/glossarys/" + glossary.id;
                return httpFactory.put(url, glossary);
            }

            function deleteGlossary(glossary) {
                var url = "api/rm/glossarys/" + glossary.id;
                return httpFactory.delete(url);
            }

            function getGlossary(id) {
                var url = "api/rm/glossarys/" + id;
                return httpFactory.get(url);
            }

            function getAllGlossarys() {
                var url = "api/rm/glossarys/all";
                return httpFactory.get(url);
            }

            function getGlossarys(pageable) {
                var url = "api/rm/glossarys/getAllGlossarys?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getAllPhases() {
                var url = "api/plm/lifecycles/phases";
                return httpFactory.get(url);
            }

            function glossaryFreeTextSearch(freeText, pageable) {
                var url = "api/rm/glossarys/freeTextSearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function glossaryEntryFreeTextSearch(freeText, pageable) {
                var url = "api/rm/glossaryEntries/freeTextSearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function getGlossaryFiles(id) {
                var url = "api/rm/glossarys/" + id + "/files";
                return httpFactory.get(url);
            }

            function updateGlossaryFileDownloadHistory(glossaryId, fileId) {
                var url = "api/rm/glossarys/" + glossaryId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function deleteGlossaryFile(glossaryId, fileId) {
                var url = "api/rm/glossarys/" + glossaryId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function getGlossaryFileVersions(glossaryId, fileId) {
                var url = "api/rm/glossarys/" + glossaryId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }

            function getGlossaryFileVersionsAndCommentsAndDownloads(glossaryId, fileId, type) {
                var url = "api/rm/glossarys/" + glossaryId + "/files/" + fileId + "/versionComments/" + type;
                return httpFactory.get(url);
            }

            function getGlossaryFileDownloadHistory(glossaryId, fileId) {
                var url = "api/rm/glossarys/" + glossaryId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.get(url);
            }

            function createGlossaryEntry(glossary) {
                var url = "api/rm/glossaryEntries";
                return httpFactory.post(url, glossary);
            }

            function addGlossaryEntryItem(glossaryId, glossary) {
                var url = "api/rm/glossaryEntries/glossary/" + glossaryId;
                return httpFactory.post(url, glossary);
            }

            function updateGlossaryEntry(glossary) {
                var url = "api/rm/glossaryEntries/" + glossary.id;
                return httpFactory.put(url, glossary);
            }

            function updateGlossaryEntryEdit(glossaryId, glossaryEntry) {
                var url = "api/rm/glossaryEntries/" + glossaryId + "/edit/" + glossaryEntry.id;
                return httpFactory.put(url, glossaryEntry);
            }

            function deleteGlossaryEntry(glossary) {
                var url = "api/rm/glossaryEntries/" + glossary.id;
                return httpFactory.delete(url);
            }

            function getAllGlossaryEntries(pageable) {
                var url = "api/rm/glossaryEntries/getAllGlossaryEntries?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllEntries(glossaryId, pageable) {
                var url = "api/rm/glossaryEntries/getEntries/" + glossaryId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllEntriesByLanguage(glossaryId, pageable, language) {
                var url = "api/rm/glossaryEntries/getEntries/" + glossaryId + "/language/" + language + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getGlossaryEntryItems(glossaryId) {
                var url = "api/rm/glossarys/" + glossaryId + "/entryItems";
                return httpFactory.get(url);
            }

            function saveGlossaryEntryItems(glossaryId, entries) {
                var url = "api/rm/glossarys/" + glossaryId + "/entryItems/multiple";
                return httpFactory.post(url, entries);
            }

            function deleteGlossaryEntryItem(glossaryId, entryItem) {
                var url = "api/rm/glossarys/" + glossaryId + "/entryItems/" + entryItem.id;
                return httpFactory.delete(url);
            }

            function promoteGlossary(glossaryId) {
                var url = "api/rm/glossarys/" + glossaryId + "/promote";
                return httpFactory.put(url);
            }

            function demoteGlossary(glossaryId) {
                var url = "api/rm/glossarys/" + glossaryId + "/demote";
                return httpFactory.put(url);
            }

            function reviseGlossary(glossaryId) {
                var url = "api/rm/glossarys/" + glossaryId + "/revise";
                return httpFactory.put(url);
            }

            function getGlossaryRevisionHistory(glossaryId) {
                var url = "api/rm/glossarys/" + glossaryId + "/revisionHistory";
                return httpFactory.get(url);
            }

            function getEntryVersions(entry) {
                var url = "api/rm/glossaryEntries/" + entry + "/versions";
                return httpFactory.get(url);
            }

            function getEntryVersionsByLanguage(entry, language) {
                var url = "api/rm/glossaryEntries/" + entry + "/versions/language/" + language;
                return httpFactory.get(url);
            }

            function getEntryEditHistory(entry, language) {
                var url = "api/rm/glossaryEntries/" + entry + "/edits/language/" + language;
                return httpFactory.get(url);
            }

            function acceptEntryEditChange(entryId, entryEdit) {
                var url = "api/rm/glossaryEntries/" + entryId + "/edit/accept/" + entryEdit.id;
                return httpFactory.put(url, entryEdit);
            }

            function rejectEntryEditChange(entryId, entryEdit) {
                var url = "api/rm/glossaryEntries/" + entryId + "/edit/reject/" + entryEdit.id;
                return httpFactory.put(url, entryEdit);
            }

            function approveEntryEdits(glossaryId, entry) {
                var url = "api/rm/glossaryEntries/" + glossaryId + "/edit/approve/" + entry.id;
                return httpFactory.put(url, entry);
            }

            function getLastAcceptedEntryEdit(entryId, language) {
                var url = "api/rm/glossaryEntries/" + entryId + "/edit/lastAccepted/" + language;
                return httpFactory.get(url);
            }

            function importGlossaryEntryItems(glossaryId, importComment, files) {
                var url = "api/rm/glossarys/" + glossaryId + "/import?importComment=" + importComment;
                return httpFactory.upload(url, files)
            }

            function printGlossary(glossaryId) {
                var url = "api/rm/glossarys/printGlossary/" + glossaryId;
                return httpFactory.get(url);
            }

            function printGlossaryByLanguage(glossaryId, language) {
                var url = "api/rm/glossarys/printGlossary/" + glossaryId + "/language/" + language;
                return httpFactory.get(url);
            }

            function getAllProjectGlossaryDeliverables(projectId) {
                var url = "api/plm/projects/" + projectId + "/projectGlossarydeliverables";
                return httpFactory.get(url);
            }

            function createGlossaryDeliverable(projectId, glossarys) {
                var url = "api/plm/projects/" + projectId + "/glossaryDeliverables/multiple";
                return httpFactory.post(url, glossarys);
            }

            function getProjectGlossaries(projectId) {
                var url = "api/plm/projects/" + projectId + "/ProjectGlossariesDeliverables";
                return httpFactory.get(url);
            }

            function deleteGlossaryDeliverable(projectId, glossaryId) {
                var url = "api/plm/projects/" + projectId + "/glossaryDeliverables/" + glossaryId;
                return httpFactory.delete(url);
            }

            function getProjectGlossaryDeliverable(project, pageable, filters) {
                var url = "api/plm/projects/" + project + "/glossaryDeliverable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&itemNumber={1}&itemType={2}&description={3}"
                    .format(filters.name, filters.itemNumber, filters.itemType, filters.description);
                return httpFactory.get(url);
            }

            function createGlossaryLanguage(glossaryLanguage) {
                var url = "api/rm/glossarys/languages";
                return httpFactory.post(url, glossaryLanguage);
            }

            function updateGlossaryLanguage(glossaryLanguage) {
                var url = "api/rm/glossarys/languages/" + glossaryLanguage.id;
                return httpFactory.put(url, glossaryLanguage);
            }

            function setGlossaryLanguage(glossaryLanguage) {
                var url = "api/rm/glossarys/languages/default/" + glossaryLanguage.id;
                return httpFactory.put(url, glossaryLanguage);
            }

            function deleteGlossaryLanguage(glossaryLanguageId) {
                var url = "api/rm/glossarys/languages/" + glossaryLanguageId;
                return httpFactory.delete(url);
            }

            function getGlossaryLanguages() {
                var url = "api/rm/glossarys/languages";
                return httpFactory.get(url);
            }

            function updateGlossaryDetail(glossaryLanguage) {
                var url = "api/rm/glossarys/details/" + glossaryLanguage.id;
                return httpFactory.put(url, glossaryLanguage);
            }

            function getGlossaryDetails(glossaryId, language) {
                var url = "api/rm/glossarys/" + glossaryId + "/glossaryDetails/" + language;
                return httpFactory.get(url);
            }

            function entryFreeTextSearch(glossaryId, language, searchTerm) {
                var url = "api/rm/glossarys/" + glossaryId + "/glossaryDetails/" + language + "/entrySearch/" + searchTerm;
                return httpFactory.get(url);
            }

            function glossaryEntrySearchItem(glossaryId, searchTerm, selectedLanguage) {
                var url = "api/rm/glossarys/" + glossaryId + "/glossaryEntrySearch/" + searchTerm + "/language/" + selectedLanguage;
                return httpFactory.get(url);
            }


            function createGlossaryPersons(glossaryId, persons) {
                var url = "api/rm/glossarys/" + glossaryId + "/permission/add";
                return httpFactory.post(url, persons);
            }

            function getAllGlossaryPersons(glossaryId) {
                var url = "api/rm/glossarys/" + glossaryId + "/glossaryPersons";
                return httpFactory.get(url);
            }

            function getAllPersons(glossaryId) {
                var url = "api/rm/glossarys/" + glossaryId + "/persons";
                return httpFactory.get(url);
            }

            function createGlossaryPermission(glossarys) {
                var url = "api/rm/glossarys/glossaryPermission";
                return httpFactory.post(url, glossarys);
            }

            function deleteGlossaryPerson(glossarys) {
                var url = 'api/rm/glossarys/delete/glossaryPerson/' + glossarys;
                return httpFactory.delete(url);
            }

            function updateFileName(glossaryId, fileId, newFileName) {
                var url = 'api/rm/glossarys/' + glossaryId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function getAllTypeAttributes(objectType) {
                var url = "api/rm/glossarys/attributes/" + objectType;
                return httpFactory.get(url);
            }


            function createGlossaryFolder(glossaryId, folder) {
                var url = 'api/rm/glossarys/' + glossaryId + "/folder";
                return httpFactory.post(url, folder)
            }

            function uploadGlossaryFolderFiles(glossaryId, folderId, files) {
                var url = 'api/rm/glossarys/' + glossaryId + "/folder/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function getGlossaryFolderChildren(folderId) {
                var url = 'api/rm/glossarys/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function moveGlossaryFileToFolder(glossaryId, file) {
                var url = 'api/rm/glossarys/' + glossaryId + "/move";
                return httpFactory.put(url, file);
            }

            function deleteGlossaryFolder(glossaryId, folderId) {
                var url = 'api/rm/glossarys/' + glossaryId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url)
            }

            function getGlossaryCounts(glossaryId, lanugage) {
                var url = 'api/rm/glossarys/' + glossaryId + "/count/" + lanugage;
                return httpFactory.get(url);
            }


            function getLatestUploadedGlossaryFile(glossaryId, fileId) {
                var url = 'api/rm/glossarys/' + glossaryId + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function pasteGlossaryFilesFromClipboard(glossaryId, fileId, files) {
                var url = 'api/rm/glossarys/' + glossaryId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function updateFileDescription(file) {
                var url = "api/rm/glossarys/files/" + file.fileId;
                return httpFactory.put(url, file);
            }

            function undoCopiedFiles(glossaryId, files) {
                var url = 'api/rm/glossarys/' + glossaryId + "/files/undo";
                return httpFactory.put(url, files);
            }
          
            function getGlossaryFilesByName(glossaryId, name) {
                var url = "api/rm/glossarys/" + glossaryId + "/byName/" + name;
                return httpFactory.get(url);
            }
        }
    }
);
