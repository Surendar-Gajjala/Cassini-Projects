define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('BomGroupService', BomGroupService);

        function BomGroupService($q, httpFactory) {
            return {
                createBomGroup: createBomGroup,
                updateBomGroup: updateBomGroup,
                getBomGroup: getBomGroup,
                deleteBomGroup: deleteBomGroup,
                getAllBomGroups: getAllBomGroups,
                getFilteredBomGroups: getFilteredBomGroups,

                findItemsByBomGroupId: findItemsByBomGroupId,
                getBomGroupTypesByBom: getBomGroupTypesByBom
            };

            function createBomGroup(bomGroup) {
                var url = "api/drdo/bomGroups";
                return httpFactory.post(url, bomGroup);
            }

            function updateBomGroup(bomGroup) {
                var url = "api/drdo/bomGroups/" + bomGroup.id;
                return httpFactory.put(url, bomGroup);
            }

            function getBomGroup(bomGroup) {
                var url = "api/drdo/bomGroups/" + bomGroup.id;
                return httpFactory.get(url);
            }

            function deleteBomGroup(bomGroupId) {
                var url = "api/drdo/bomGroups/" + bomGroupId;
                return httpFactory.delete(url);
            }

            function getAllBomGroups(pageable) {
                var url = "api/drdo/bomGroups/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getFilteredBomGroups(pageable, filters) {
                var url = "api/drdo/bomGroups/filter?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&name={1}".format(filters.type, filters.name);
                return httpFactory.get(url);
            }

            function findItemsByBomGroupId(bomGroupId) {
                var url = "api/drdo/bomGroups/" + bomGroupId + "/items";
                return httpFactory.get(url);
            }

            function getBomGroupTypesByBom(pageable, filters) {
                var url = "api/drdo/bomGroups/types?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&name={1}&bom={2}".format(filters.type, filters.name, filters.bom);
                return httpFactory.get(url);
            }

        }
    }
);