/**
 * Created by swapna on 21/01/19.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('SubContractService', SubContractService);

        function SubContractService(httpFactory) {
            return {
                createContractor: createContractor,
                getContractors: getContractors,
                getContractorsByIds: getContractorsByIds,
                getActiveContractors: getActiveContractors,
                getPageableContractors: getPageableContractors,
                getContractor: getContractor,
                updateContractor: updateContractor,
                deleteContractor: deleteContractor,
                findContractorByContactPerson: findContractorByContactPerson,
                getContractorReferences: getContractorReferences,
                contractorsFreeTextSearch: contractorsFreeTextSearch,

                createWorkOrder: createWorkOrder,
                getWorkOrders: getWorkOrders,
                getPageableWorkOrders: getPageableWorkOrders,
                getWorkOrder: getWorkOrder,
                updateWorkOrder: updateWorkOrder,
                deleteWorkOrder: deleteWorkOrder,
                findContractorWorkOrders: findContractorWorkOrders,
                workOrdersFreeTextSearch: workOrdersFreeTextSearch,

                createWorkOrderItem: createWorkOrderItem,
                createWorkOrderItems: createWorkOrderItems,
                getWorkOrderItems: getWorkOrderItems,
                getPageableWorkOrderItems: getPageableWorkOrderItems,
                getWorkOrderItem: getWorkOrderItem,
                updateWorkOrderItem: updateWorkOrderItem,
                deleteWorkOrderItem: deleteWorkOrderItem
            };

            function createContractor(contractor) {
                var url = "api/is/contracts/contractors";
                return httpFactory.post(url, contractor);
            }

            function getContractors() {
                var url = "api/is/contracts/contractors";
                return httpFactory.get(url);
            }

            function getContractorsByIds(ids) {
                var url = "api/is/contracts/contractors/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getActiveContractors() {
                var url = "api/is/contracts/contractors/active";
                return httpFactory.get(url);
            }

            function getPageableContractors(pageable) {
                var url = "api/is/contracts/contractors/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getContractor(contractorId) {
                var url = "api/is/contracts/contractors/" + contractorId;
                return httpFactory.get(url);
            }

            function updateContractor(contractor) {
                var url = "api/is/contracts/contractors";
                return httpFactory.post(url, contractor);
            }

            function deleteContractor(contractorId) {
                var url = "api/is/contracts/contractors/" + contractorId;
                return httpFactory.delete(url);
            }

            function findContractorByContactPerson(contactPerson) {
                var url = "api/is/contracts/contractors/contactPerson" + contactPerson;
                return httpFactory.get(url);
            }

            function contractorsFreeTextSearch(pageable, criteria) {
                var url = "api/is/contracts/contractors/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function getContractorReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getContractorsByIds(ids).then(
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

            function createWorkOrder(workOrder) {
                var url = "api/is/contracts/workOrders";
                return httpFactory.post(url, workOrder);
            }

            function getWorkOrders() {
                var url = "api/is/contracts/workOrders";
                return httpFactory.get(url);
            }

            function getPageableWorkOrders(pageable) {
                var url = "api/is/contracts/workOrders/pageable?page={0}&size={1}".
                    format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getWorkOrder(workOrderId) {
                var url = "api/is/contracts/workOrders/" + workOrderId;
                return httpFactory.get(url);
            }

            function updateWorkOrder(workOrder) {
                var url = "api/is/contracts/workOrders";
                return httpFactory.post(url, workOrder);
            }

            function deleteWorkOrder(workOrderId) {
                var url = "api/is/contracts/workOrders/" + workOrderId;
                return httpFactory.delete(url);
            }

            function findContractorWorkOrders(contractorId) {
                var url = "api/is/contracts/workOrders/contractor" + contractorId;
                return httpFactory.get(url);
            }

            function workOrdersFreeTextSearch(pageable, criteria) {
                var url = "api/is/contracts/workOrders/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function createWorkOrderItem(workOrderId, workOrderItem) {
                var url = "api/is/contracts/workOrders/" + workOrderId + "/items";
                return httpFactory.post(url, workOrderItem);
            }

            function createWorkOrderItems(workOrderId, workOrderItems) {
                var url = "api/is/contracts/workOrders/" + workOrderId + "/items/multiple";
                return httpFactory.post(url, workOrderItems);
            }

            function getWorkOrderItems(workOrderId) {
                var url = "api/is/contracts/workOrders/" + workOrderId + "/items";
                return httpFactory.get(url);
            }

            function getPageableWorkOrderItems(workOrderId, pageable) {
                var url = "api/is/contracts/workOrders/" + workOrderId + "/items/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getWorkOrderItem(workOrderId, workOrderItemId) {
                var url = "api/is/contracts/workOrders/" + workOrderId + "/items/" + workOrderItemId;
                return httpFactory.get(url);
            }

            function updateWorkOrderItem(workOrderId, workOrderItem) {
                var url = "api/is/contracts/workOrders/" + workOrderId + "/items";
                return httpFactory.post(url, workOrderItem);
            }

            function deleteWorkOrderItem(workOrderId, workOrderItemId) {
                var url = "api/is/contracts/workOrders/" + workOrderId + "/items/" + workOrderItemId;
                return httpFactory.delete(url);
            }

        }
    }
);