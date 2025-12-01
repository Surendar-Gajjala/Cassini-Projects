define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ShareService', ShareService);

        function ShareService(httpFactory) {
            return {
                createShare: createShare,
                updateShare: updateShare,
                deleteShare: deleteShare,
                getShare: getShare,
                shareMultiple: shareMultiple,
                getSharedObjectByPersonAndItem: getSharedObjectByPersonAndItem,
                getAllSharedObjectsByItemAndPerson: getAllSharedObjectsByItemAndPerson,
                getItemSharedPerson: getItemSharedPerson,
                getItemsBySharedPersonId: getItemsBySharedPersonId,
                getGroupsByPerson: getGroupsByPerson,
                getItemsBySharedBy: getItemsBySharedBy,
                getItemsBySharedTo: getItemsBySharedTo,
                getItemsByExternalGroup: getItemsByExternalGroup,
                getAllSharedObjects: getAllSharedObjects,
                getItemsBySharedToPerson: getItemsBySharedToPerson,
                getInternalAndExternalObjects: getInternalAndExternalObjects,
                getProjectsBySharedPersonId: getProjectsBySharedPersonId,
                shareMultipleMfr: shareMultipleMfr,
                getMfrsBySharedPersonId: getMfrsBySharedPersonId,
                shareMultipleMfrParts: shareMultipleMfrParts,
                getMfrPartsBySharedPersonId: getMfrPartsBySharedPersonId,
                getProgramsBySharedPersonId: getProgramsBySharedPersonId,
                getSuppliersBySharedPersonId: getSuppliersBySharedPersonId,
                getCustomObjectsBySharedPersonId: getCustomObjectsBySharedPersonId,
                getMfrsInternalAndExternalObjects: getMfrsInternalAndExternalObjects,
                getMfrPartsInternalAndExternalObjects: getMfrPartsInternalAndExternalObjects,
                getMfrsBySharedBy: getMfrsBySharedBy,
                getMfrPartsBySharedBy: getMfrPartsBySharedBy,
                getAllSharedMfrObjects: getAllSharedMfrObjects,
                getAllSharedMfrPartObjects: getAllSharedMfrPartObjects,
                getMfrsBySharedToPerson: getMfrsBySharedToPerson,
                getMfrPartsBySharedToPerson: getMfrPartsBySharedToPerson,
                getDeclarationsBySharedPersonId: getDeclarationsBySharedPersonId,
                getAllSharedDeclarationObjects: getAllSharedDeclarationObjects,
                getDeclarationsBySharedBy: getDeclarationsBySharedBy,
                getDeclarationsBySharedToPerson: getDeclarationsBySharedToPerson,
                getDeclarationsInternalAndExternalObjects: getDeclarationsInternalAndExternalObjects,
                getMfrPartsBySharedTo: getMfrPartsBySharedTo,
                getDeclarationsBySharedTo: getDeclarationsBySharedTo,
                getProjectObjectsBySharedPersonId: getProjectObjectsBySharedPersonId,
                shareMultipleProjects: shareMultipleProjects,
                shareMultiplePrograms: shareMultiplePrograms,
                getByObjectId: getByObjectId,
                getCounts: getCounts,
                getAllSharedProjectObjects: getAllSharedProjectObjects,
                getProjectObjectsBySharedTo: getProjectObjectsBySharedTo,
                getProjectObjectsInternalAndExternalObjects: getProjectObjectsInternalAndExternalObjects,
                getProjectObjectsBySharedBy: getProjectObjectsBySharedBy,
                getProjectObjectsBySharedToPerson: getProjectObjectsBySharedToPerson,
                shareMultipleSuppliers: shareMultipleSuppliers,
                getSuppliersInternalAndExternalObjects: getSuppliersInternalAndExternalObjects,
                getSuppliersBySharedBy: getSuppliersBySharedBy,
                getSuppliersBySharedTo: getSuppliersBySharedTo,
                getSuppliersBySharedToPerson: getSuppliersBySharedToPerson,
                getAllSharedSupplierObjects: getAllSharedSupplierObjects,
                shareMultipleFolders: shareMultipleFolders,
                getFoldersBySharedPersonId: getFoldersBySharedPersonId,
                getFolderChildren: getFolderChildren,
                getAllSharedFolderObjects: getAllSharedFolderObjects,
                shareMultipleCustomObjects: shareMultipleCustomObjects,
                getCustomObjectsInternalAndExternalObjects: getCustomObjectsInternalAndExternalObjects,
                getCustomObjectsBySharedBy: getCustomObjectsBySharedBy,
                getCustomObjectsBySharedTo: getCustomObjectsBySharedTo,
                getCustomObjectsBySharedToPerson: getCustomObjectsBySharedToPerson,
                getAllSharedCustomObjects: getAllSharedCustomObjects,
                shareMultipleFileObjects: shareMultipleFileObjects,
                getExternalSharedFoldersAndFiles: getExternalSharedFoldersAndFiles,
                getExternalSharedRootAndFilesByPerson: getExternalSharedRootAndFilesByPerson,
                getExternalFolderFilesByPerson: getExternalFolderFilesByPerson,
                getAllSharedProgramObjects: getAllSharedProgramObjects,
                getSharedToPersons: getSharedToPersons,
                getPersonSharedCounts: getPersonSharedCounts
            };

            function updateShare(sharedObject) {
                var url = "api/plm/sharedObjects";
                return httpFactory.put(url, sharedObject);
            }

            function deleteShare(sharedObjectId) {
                var url = "api/plm/sharedObjects/" + sharedObjectId;
                return httpFactory.delete(url);
            }

            function getShare(sharedObjectId) {
                var url = "api/plm/sharedObjects/" + sharedObjectId;
                return httpFactory.get(url);
            }

            function shareMultiple(items, sharedObject) {
                var url = "api/plm/sharedObjects/multiple?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy);
                return httpFactory.post(url, items);
            }

            function getSharedObjectByPersonAndItem(objectId, personId) {
                var url = "api/plm/sharedObjects/" + objectId + "/" + personId;
                return httpFactory.get(url);
            }

            function getAllSharedObjectsByItemAndPerson(objectIds, personIds) {
                var url = "api/plm/sharedObjects/getSharedObjects";
                return httpFactory.post(url, [objectIds, personIds]);
            }

            function getItemSharedPerson(itemId) {
                var url = "api/plm/sharedObjects/byItem/" + itemId;
                return httpFactory.get(url);
            }

            function getItemsBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/items/person/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getProjectsBySharedPersonId(personId) {
                var url = "api/plm/sharedObjects/projects/person/[" + personId + "]";
                return httpFactory.get(url);
            }

            function getGroupsByPerson(personId) {
                var url = "api/common/persongroups/groups/byPerson/" + personId;
                return httpFactory.get(url);
            }

            function getItemsBySharedBy(pageable, sharedBy) {
                var url = "api/plm/sharedObjects/items/sharedBy/" + sharedBy + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getItemsBySharedTo(pageable, sharedTo) {
                var url = "api/plm/sharedObjects/items/sharedTo/" + sharedTo + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getItemsByExternalGroup(pageable, group) {
                var url = "api/plm/sharedObjects/items/externalGroup/" + group + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getItemsBySharedToPerson(pageable, ids) {
                var url = "api/plm/sharedObjects/items/sharedTo/[" + ids + "]?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllSharedObjects(pageable) {
                var url = "api/plm/sharedObjects/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);

            }

            function getInternalAndExternalObjects(internal, external, pageable) {
                var url = "api/plm/sharedObjects/items/sharedBy/" + internal + "/sharedTo/" + external + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function createShare(sharedObject) {
                var url = "api/plm/sharedObjects";
                return httpFactory.post(url, sharedObject);
            }

            function shareMultipleMfr(mfrs, sharedObject) {
                var url = "api/plm/sharedObjects/multiple/mfrs?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy);
                return httpFactory.post(url, mfrs);
            }

            function getMfrsBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/mfrs/person/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getAllSharedMfrObjects(pageable) {
                var url = "api/plm/sharedObjects/mfrs/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllSharedProgramObjects(pageable) {
                var url = "api/plm/sharedObjects/programs/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function shareMultipleMfrParts(parts, sharedObject) {
                var url = "api/plm/sharedObjects/multiple/mfr/parts?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy);
                return httpFactory.post(url, parts);
            }

            function shareMultipleSuppliers(suppliers, sharedObject) {
                var url = "api/plm/sharedObjects/multiple/suppliers?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy);
                return httpFactory.post(url, suppliers);
            }

            function shareMultipleCustomObjects(customObjects, sharedObject) {
                var url = "api/plm/sharedObjects/multiple/customobjects?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy);
                return httpFactory.post(url, customObjects);
            }

            function shareMultipleFolders(suppliers, sharedObject) {
                var url = "api/plm/sharedObjects/multiple/folders?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy);
                return httpFactory.post(url, suppliers);
            }

            function getMfrPartsBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/mfr/parts/person?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getProgramsBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/program/person?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getSuppliersBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/suppliers/person?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getCustomObjectsBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/customobjects/person?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getFoldersBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/folders/person?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }


            function getExternalSharedFoldersAndFiles(personId) {
                var url = "api/plm/sharedObjects/shared/external/folders/by/person/" + personId;
                return httpFactory.get(url);
            }


            function getExternalSharedRootAndFilesByPerson(personId) {
                var url = "api/plm/sharedObjects/external/root/files/by/" + personId;
                return httpFactory.get(url);
            }

            function getExternalFolderFilesByPerson(folderId, personId, objectType) {
                var url = "api/plm/sharedObjects/external/folder/files/by/" + personId + "/" + folderId + "/" + objectType;
                return httpFactory.get(url);
            }

            function getDeclarationsBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/declarations/person?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getProjectObjectsBySharedPersonId(pageable, filters, objectType) {
                var url = "api/plm/sharedObjects/project/{0}/person?page={1}&size={2}&sort={3}:{4}".
                    format(objectType, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getAllSharedMfrPartObjects(pageable) {
                var url = "api/plm/sharedObjects/mfr/parts/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getProjectsBySharedPersonId(pageable, filters) {
                var url = "api/plm/sharedObjects/project/person?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&person={1}&personIds={2}&sharedObjectType={3}".format(filters.searchQuery, filters.person, [filters.personIds], filters.sharedObjectType);
                return httpFactory.get(url);
            }

            function getMfrsInternalAndExternalObjects(internal, external, pageable) {
                var url = "api/plm/sharedObjects/mfrs/sharedBy/" + internal + "/sharedTo/" + external + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMfrsBySharedBy(pageable, sharedBy) {
                var url = "api/plm/sharedObjects/mfrs/sharedBy/" + sharedBy + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMfrPartsInternalAndExternalObjects(internal, external, pageable) {
                var url = "api/plm/sharedObjects/mfr/parts/sharedBy/" + internal + "/sharedTo/" + external + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMfrPartsBySharedBy(pageable, sharedBy) {
                var url = "api/plm/sharedObjects/mfr/parts/sharedBy/" + sharedBy + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMfrPartsBySharedTo(pageable, sharedTo) {
                var url = "api/plm/sharedObjects/mfr/parts/sharedTo/" + sharedTo + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMfrPartsBySharedToPerson(pageable, ids) {
                var url = "api/plm/sharedObjects/mfr/parts/sharedTo/[" + ids + "]?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getMfrsBySharedToPerson(pageable, ids) {
                var url = "api/plm/sharedObjects/mfrs/sharedTo/[" + ids + "]?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getAllSharedDeclarationObjects(pageable) {
                var url = "api/plm/sharedObjects/declarations/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getDeclarationsBySharedBy(pageable, sharedBy) {
                var url = "api/plm/sharedObjects/declarations/sharedBy/" + sharedBy + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getDeclarationsBySharedTo(pageable, sharedTo) {
                var url = "api/plm/sharedObjects/declarations/sharedTo/" + sharedTo + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getDeclarationsBySharedToPerson(pageable, ids) {
                var url = "api/plm/sharedObjects/declarations/sharedTo/[" + ids + "]?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getDeclarationsInternalAndExternalObjects(internal, external, pageable) {
                var url = "api/plm/sharedObjects/declarations/sharedBy/" + internal + "/sharedTo/" + external + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function shareMultipleProjects(projects, sharedObject) {
                var url = "api/plm/sharedObjects/multiple/project?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy);
                return httpFactory.post(url, projects);
            }

            function shareMultiplePrograms(programs, sharedObject) {
                var url = "api/plm/sharedObjects/multiple/program?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy);
                return httpFactory.post(url, programs);
            }

            function shareMultipleFileObjects(plmFiles, sharedObject) {
                var url = "api/plm/sharedObjects/multiple/files/by/object?sharedToObjects={0}&shareType={1}&permission={2}&sharedBy={3}&parentObjectId={4}&parentObjectType={5}".
                    format([sharedObject.sharedToObjects], sharedObject.shareType, sharedObject.permission, sharedObject.sharedBy, sharedObject.parentObjectId, sharedObject.parentObjectType);
                return httpFactory.post(url, plmFiles);
            }

            function getByObjectId(objectId) {
                var url = "api/plm/sharedObjects/objectId/" + objectId;
                return httpFactory.get(url);
            }

            function getCounts(type) {
                var url = "api/plm/sharedObjects/counts/" + type;
                return httpFactory.get(url);
            }

            
            function getPersonSharedCounts(personId) {
                var url = "api/plm/sharedObjects/person/"+personId+"/counts";
                return httpFactory.get(url);
            }

            function getAllSharedProjectObjects(pageable, type) {
                var url = "api/plm/sharedObjects/project/" + type + "/?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getProjectObjectsBySharedBy(pageable, sharedBy, type) {
                var url = "api/plm/sharedObjects/project/" + type + "/sharedBy/" + sharedBy + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getProjectObjectsBySharedTo(pageable, sharedTo, type) {
                var url = "api/plm/sharedObjects/project/" + type + "/sharedTo/" + sharedTo + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getProjectObjectsInternalAndExternalObjects(internal, external, pageable, type) {
                var url = "api/plm/sharedObjects/project/" + type + "/sharedBy/" + internal + "/sharedTo/" + external + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getProjectObjectsBySharedToPerson(pageable, ids, type) {
                var url = "api/plm/sharedObjects/project/" + type + "/sharedTo/[" + ids + "]?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getAllSharedSupplierObjects(pageable) {
                var url = "api/plm/sharedObjects/suppliers/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getSuppliersInternalAndExternalObjects(internal, external, pageable) {
                var url = "api/plm/sharedObjects/suppliers/sharedBy/" + internal + "/sharedTo/" + external + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSuppliersBySharedBy(pageable, sharedBy) {
                var url = "api/plm/sharedObjects/suppliers/sharedBy/" + sharedBy + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSuppliersBySharedTo(pageable, sharedTo) {
                var url = "api/plm/sharedObjects/suppliers/sharedTo/" + sharedTo + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSuppliersBySharedToPerson(pageable, ids) {
                var url = "api/plm/sharedObjects/suppliers/sharedTo/[" + ids + "]?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getAllSharedCustomObjects(pageable) {
                var url = "api/plm/sharedObjects/customobjects/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getCustomObjectsInternalAndExternalObjects(internal, external, pageable) {
                var url = "api/plm/sharedObjects/suppliers/sharedBy/" + internal + "/sharedTo/" + external + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getCustomObjectsBySharedBy(pageable, sharedBy) {
                var url = "api/plm/sharedObjects/customobjects/sharedBy/" + sharedBy + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getCustomObjectsBySharedTo(pageable, sharedTo) {
                var url = "api/plm/sharedObjects/customobjects/sharedTo/" + sharedTo + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getCustomObjectsBySharedToPerson(pageable, ids) {
                var url = "api/plm/sharedObjects/customobjects/sharedTo/[" + ids + "]?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getFolderChildren(folderId) {
                var url = "api/plm/sharedObjects/folders/" + folderId + "/children";
                return httpFactory.get(url);
            }

            function getAllSharedFolderObjects(pageable) {
                var url = "api/plm/sharedObjects/folders/?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSharedToPersons() {
                var url = "api/plm/sharedObjects/sharedTo";
                return httpFactory.get(url);
            }


        }
    }
);