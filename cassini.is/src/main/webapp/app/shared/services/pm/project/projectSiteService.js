define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('ProjectSiteService', ProjectSiteService);

        function ProjectSiteService($stateParams, httpFactory) {
            return {
                getAllSites: getAllSites,
                getSites: getSites,
                getSite: getSite,
                createSite: createSite,
                updateSite: updateSite,
                deleteProjectSite: deleteProjectSite,
                getPagedSitesByProject: getPagedSitesByProject,
                getSitesByProject: getSitesByProject,
                createSiteStores: createSiteStores,
                getSiteStores: getSiteStores,
                deleteSiteStore: deleteSiteStore,
                getSiteStoresBySite: getSiteStoresBySite,
                getSiteStoreByStoreId: getSiteStoreByStoreId,
                freeTextSearch: freeTextSearch,
                getSiteByName: getSiteByName,
                getSiteDetailsCount: getSiteDetailsCount
            };


            function getAllSites() {
                var url = "api/sites";
                return httpFactory.get(url);
            }

            function getSites(pageable) {
                var url = "api/sites/pageable";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function createSite(site) {
                var url = "api/sites";
                return httpFactory.post(url, site);
            }

            function updateSite(siteId, site) {
                var url = "api/sites/" + siteId;
                return httpFactory.put(url, site);
            }

            function getSite(siteId) {
                var url = "api/sites/" + siteId;
                return httpFactory.get(url);
            }

            function deleteProjectSite(projectId, siteId) {
                var url = "api/sites/" + projectId + "/site/" + siteId;
                return httpFactory.delete(url);
            }

            function freeTextSearch(projectId, pageable, criteria) {
                var url = "api/sites/" + projectId + "/freesearch?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&site={1}&store={2}".
                    format(criteria.searchQuery, criteria.site, criteria.store)
                return httpFactory.get(url);
            }

            function getPagedSitesByProject(projectId, pageable) {
                var url = "api/sites/byProject/pageable/" + projectId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSitesByProject(projectId) {
                var url = "api/sites/byProject/" + projectId;
                return httpFactory.get(url);
            }

            function createSiteStores(siteId, siteStores) {
                var url = "api/sites/" + siteId + "/stores";
                return httpFactory.post(url, siteStores);
            }

            function getSiteStores(ids) {
                var url = "api/sites/multiple/sites/[" + ids + "]";
                return httpFactory.get(url);
            }

            function deleteSiteStore(siteId, rowId) {
                var url = "api/sites/" + siteId + "/" + rowId;
                return httpFactory.delete(url);
            }

            function getSiteStoresBySite(siteId) {
                var url = "api/sites/" + siteId + "/stores";
                return httpFactory.get(url);
            }

            function getSiteStoreByStoreId(storeId) {
                var url = "api/sites/store/" + storeId;
                return httpFactory.get(url);
            }

            function getSiteByName(projectId, siteName) {
                var url = "api/sites/" + projectId + "/siteName/" + siteName;
                return httpFactory.get(url);
            }

            function getSiteDetailsCount(projectId, siteId) {
                var url = "api/sites/" + projectId + "/site/" + siteId;
                return httpFactory.get(url);
            }
        }
    }
);