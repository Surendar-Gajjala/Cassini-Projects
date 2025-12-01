define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('WbsService', WbsService);

        function WbsService($stateParams, httpFactory) {
            return {
                getAllWbs: getAllWbs,
                getMultipleWbs: getMultipleWbs,
                getMultipleWbsWithTasks: getMultipleWbsWithTasks,
                getRootWbs: getRootWbs,
                getWbsChildren: getWbsChildren,
                createWbs: createWbs,
                updateWbs: updateWbs,
                deleteWbs: deleteWbs,
                getWbsTree: getWbsTree,
                getWbs: getWbs,
                getProjectWbs: getProjectWbs,
                getWbsByProject: getWbsByProject,
                getChildWbsByName: getChildWbsByName,
                getParentWbsByName: getParentWbsByName,
                getAttributesByWbsIdAndAttributeId: getAttributesByWbsIdAndAttributeId,
                getRequiredWbsAttributes: getRequiredWbsAttributes,
                finishWbs: finishWbs,
                createGantt: createGantt,
                createLinks: createLinks,
                getLink:getLink
            };

            function getMultipleWbs(projectId, ids) {
                var url = "api/projects/" + projectId + "/appWbs/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMultipleWbsWithTasks(projectId, objects, property) {
                var wbsIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && wbsIds.indexOf(object[property]) == -1) {
                        wbsIds.push(object[property]);
                    }
                });

                if (wbsIds.length > 0) {
                    getMultipleWbs(projectId, wbsIds).then(
                        function (wbss) {
                            var map = new Hashtable();
                            angular.forEach(wbss, function (wbs) {
                                map.put(wbs.id, wbs);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var wbs = map.get(object[property]);
                                    if (wbs != null) {
                                        object[property + "Object"] = wbs;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getRootWbs(projectId) {
                var url = "api/projects/" + projectId + "/wbs";
                return httpFactory.get(url);
            }

            function getAllWbs(projectId) {
                var url = "api/projects/" + projectId + "/wbs/root/all";
                return httpFactory.get(url);
            }

            function getProjectWbs(projectId) {
                var url = "api/projects/" + projectId + "/wbs/all";
                return httpFactory.get(url);
            }

            function createWbs(projectId, wbs) {
                var url = "api/projects/" + projectId + "/wbs";
                return httpFactory.post(url, wbs);
            }

            function getWbs(projectId, wbsId) {
                var url = "api/projects/" + projectId + "/wbs/" + wbsId;
                return httpFactory.get(url);
            }

            function getWbsChildren(projectId, wbsId) {
                var url = "api/projects/" + projectId + "/wbs/" + wbsId + "/children";
                return httpFactory.get(url);
            }

            function getWbsTree(projectId) {
                var url = "api/projects/" + projectId + "/appWbs/tree/";
                return httpFactory.get(url);
            }

            function updateWbs(projectId, wbs) {
                var url = "api/projects/" + projectId + "/wbs/" + wbs.id;
                return httpFactory.put(url, wbs);
            }

            function deleteWbs(projectId, wbsId) {
                var url = "api/projects/" + projectId + "/appWbs/" + wbsId;
                return httpFactory.delete(url);
            }

            function getWbsByProject(projectId, pageable) {
                var url = "api/projects/" + projectId + "/appWbs/pageable/";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getChildWbsByName(projectId, wbsName, parentId) {
                var url = "api/projects/" + projectId + "/wbs/parent/" + parentId + "/wbsName?wbsName={0}".format(wbsName);
                return httpFactory.get(url);
            }

            function getParentWbsByName(projectId, wbsName) {
                var url = "api/projects/" + projectId + "/wbs/parent/" + wbsName;
                return httpFactory.get(url);
            }

            function getAttributesByWbsIdAndAttributeId(itemIds, attributeIds) {
                var url = "api/projects/objectAttributes/project";
                return httpFactory.post(url, [itemIds, attributeIds]);
            }


            function getRequiredWbsAttributes(objectType) {
                var url = "api/projects/requiredProjectAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function finishWbs(projectId, wbs) {
                var url = "api/projects/" + projectId + "/wbs/finish/" + wbs.id;
                return httpFactory.post(url, wbs);
            }

            function createGantt(projectId, data){
                var url = "api/projects/" + projectId + "/wbs/createGantt";
                return httpFactory.post(url, data);
            }

            function createLinks(projectId, link){
                var url = "api/projects/" + projectId + "/wbs/createLinks";
                return httpFactory.post(url, link);
            }

            function getLink(projectId) {
                var url = "api/projects/" + projectId + "/wbs/getLinks";
                return httpFactory.get(url);
            }
        }
    }
);