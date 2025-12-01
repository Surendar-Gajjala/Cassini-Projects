define(['app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('MfrFilesService', MfrFilesService);

        function MfrFilesService(httpFactory) {
            return {
                getManufacturerfiles: getManufacturerfiles,
                getManufacturefile: getManufacturefile,
                createManufacturefile: createManufacturefile,
                updateManufacturefile: updateManufacturefile,
                deleteManufacturefile: deleteManufacturefile,
                getAllFileVersions: getAllFileVersions,
                getManufacturerLatestFiles: getManufacturerLatestFiles,
                getMfrFileName: getMfrFileName,
                updateMfrFileDownloadHistory: updateMfrFileDownloadHistory,
                getAllFileVersionAndCommentsAndDownloads: getAllFileVersionAndCommentsAndDownloads,
                updateMfrFileName: updateMfrFileName,
                deleteMfrFolder: deleteMfrFolder,
                createMfrFolder: createMfrFolder,
                getMfrFolderChildren: getMfrFolderChildren,
                getMfrPartFolderChildren: getMfrPartFolderChildren,
                moveMfrFileToFolder: moveMfrFileToFolder,
                deleteMfrPartFolder: deleteMfrPartFolder,
                createMfrPartFolder: createMfrPartFolder,
                moveMfrPartFileToFolder: moveMfrPartFileToFolder,
                getLatestUploadedMfrFile: getLatestUploadedMfrFile,
                getLatestUploadedMfrPartFile: getLatestUploadedMfrPartFile,
                updateMfrFileDescription: updateMfrFileDescription,
                updateMfrPartFileDescription: updateMfrPartFileDescription,
                pasteMfrFilesFromClipboard: pasteMfrFilesFromClipboard,
                pasteMfrPartFilesFromClipboard: pasteMfrPartFilesFromClipboard,
                undoCopiedFiles: undoCopiedFiles,
                undoCopiedMfrPartsFiles: undoCopiedMfrPartsFiles

            };
            function getManufacturerfiles(mfrId) {
                var url = "api/plm/mfr/" + mfrId + "/files";
                return httpFactory.get(url);
            }

            function getManufacturerLatestFiles(mfrId) {
                var url = "api/plm/mfr/" + mfrId + "/latestFiles";
                return httpFactory.get(url);
            }

            function getManufacturefile(mfrId, fileId) {
                var url = "api/plm/mfr/" + mfrId + "/files/" + fileId;
                return httpFactory.get(url);
            }

            function createManufacturefile(mfr) {
                var url = "api/plm/mfr/" + mfr.id + "/files";
                return httpFactory.post(url, mfr);
            }

            function updateManufacturefile(mfr, file) {
                var url = "api/plm/mfr/" + mfr + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function deleteManufacturefile(mfr, file) {
                var url = "api/plm/mfr/" + mfr + "/files/" + file;
                return httpFactory.delete(url);
            }

            function getAllFileVersions(mfrId, fileId) {
                var url = 'api/plm/mfr/' + mfrId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }

            function getAllFileVersionAndCommentsAndDownloads(mfrId, fileId, type) {
                var url = 'api/plm/mfr/' + mfrId + "/files/" + fileId + "/versionComments/" + type;
                return httpFactory.get(url);
            }

            function getMfrFileName(mfrId, name) {
                var url = 'api/plm/mfr/' + mfrId + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function updateMfrFileDownloadHistory(mfrId, fileId) {
                var url = 'api/plm/mfr/' + mfrId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function updateMfrFileName(mfrId, fileId, newFileName) {
                var url = 'api/plm/mfr/' + mfrId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }


            function deleteMfrFolder(mfrId, folderId) {
                var url = 'api/plm/mfr/' + mfrId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url)
            }

            function createMfrFolder(mfrId, folder) {
                var url = 'api/plm/mfr/' + mfrId + "/folder";
                return httpFactory.post(url, folder)
            }

            function getMfrFolderChildren(mfrId, folderId) {
                var url = 'api/plm/mfr/' + mfrId + "/" + folderId + "/children";
                return httpFactory.get(url);
            }

            function getMfrPartFolderChildren(mfrId, folderId) {
                var url = 'api/plm/mfr/' + mfrId + '/parts/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function moveMfrFileToFolder(mfrId, file) {
                var url = 'api/plm/mfr/' + mfrId + "/move";
                return httpFactory.put(url, file);
            }

            function deleteMfrPartFolder(mfrId, mfrPartId, folderId) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + mfrPartId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url);
            }

            function createMfrPartFolder(mfrId, folder) {
                var url = "api/plm/mfr/" + mfrId + "/parts/folder";
                return httpFactory.post(url, folder);
            }

            function moveMfrPartFileToFolder(mfrId, file) {
                var url = 'api/plm/mfr/' + mfrId + '/parts/move';
                return httpFactory.put(url, file);
            }

            function getLatestUploadedMfrFile(mfrId, fileId) {
                var url = 'api/plm/mfr/' + mfrId + '/files/' + fileId + '/latest/uploaded';
                return httpFactory.get(url);
            }

            function getLatestUploadedMfrPartFile(mfrId, mfrPartId, fileId) {
                var url = 'api/plm/mfr/' + mfrId + '/parts/' + mfrPartId + '/files/' + fileId + '/latest/uploaded';
                return httpFactory.get(url);
            }

            function updateMfrFileDescription(mfrId, file) {
                var url = "api/plm/mfr/" + mfrId + "/files/" + file.fileId + "/update";
                return httpFactory.put(url, file);
            }

            function updateMfrPartFileDescription(mfrId, file) {
                var url = "api/plm/mfr/" + mfrId + "/parts/files/" + file.fileId + "/update";
                return httpFactory.put(url, file);
            }

            function pasteMfrFilesFromClipboard(mfrId, fileId, files) {
                var url = "api/plm/mfr/" + mfrId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function pasteMfrPartFilesFromClipboard(mfrId, fileId, files) {
                var url = "api/plm/mfr/" + mfrId + "/parts/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedFiles(mfrId, files) {
                var url = "api/plm/mfr/" + mfrId + "/files/undo";
                return httpFactory.put(url, files);
            }

            function undoCopiedMfrPartsFiles(mfrId, files) {
                var url = "api/plm/mfr/" + mfrId + "/parts/files/undo";
                return httpFactory.put(url, files);
            }
        }
    }
);
