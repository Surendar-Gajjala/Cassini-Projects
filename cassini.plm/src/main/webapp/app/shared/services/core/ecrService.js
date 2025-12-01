define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ECRService', ECRService);

        function ECRService(httpFactory) {
            return {

                createECR: createECR,
                getECR: getECR,
                updateECR: updateECR,
                deleteECR: deleteECR,
                getAllECRs: getAllECRs,
                attachEcrWorkflow: attachEcrWorkflow,
                deleteEcrWorkflow: deleteEcrWorkflow,

                createEcrItem: createEcrItem,
                createEcrItems: createEcrItems,
                updateEcrItem: updateEcrItem,
                getAffectedItems: getAffectedItems,
                getFilteredItems: getFilteredItems,
                createEcrRelatedItems: createEcrRelatedItems,
                getEcrRelatedItems: getEcrRelatedItems,
                deleteEcrAffectedItem: deleteEcrAffectedItem,
                deleteEcrRelatedItem: deleteEcrRelatedItem,
                getEcrDetailsCount: getEcrDetailsCount,
                getFilteredEcrItems: getFilteredEcrItems,
                createECOChangeRequest: createECOChangeRequest,
                getECRChangeRequestItems: getECRChangeRequestItems,
                deleteEcoChangeRequest: deleteEcoChangeRequest,
                createECRProblemReports: createECRProblemReports,
                createECRProblemReport: createECRProblemReport,
                getECRProblemReports: getECRProblemReports,
                deleteECRProblemReport: deleteECRProblemReport,
                getChangeAnalysts: getChangeAnalysts,
                getStatus: getStatus,
                getUrgency: getUrgency,
                getOriginators: getOriginators,
                getRequesters: getRequesters,
                getChangeReasonType: getChangeReasonType
            };

            function createECR(ecr) {
                var url = "api/cms/ecrs";
                return httpFactory.post(url, ecr)
            }

            function getECR(id) {
                var url = "api/cms/ecrs/" + id;
                return httpFactory.get(url)
            }

            function updateECR(ecr) {
                var url = "api/cms/ecrs/" + ecr.id;
                return httpFactory.put(url, ecr);
            }

            function deleteECR(ecr) {
                var url = "api/cms/ecrs/" + ecr;
                return httpFactory.delete(url);
            }

            function attachEcrWorkflow(dcrId, wfId) {
                var url = "api/cms/ecrs/" + dcrId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteEcrWorkflow(dcrId) {
                var url = 'api/cms/ecrs/' + dcrId + "/workflow/delete";
                return httpFactory.delete(url);
            }


            function getAllECRs(pageable, filters) {
                var url = "api/cms/ecrs/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&crNumber={0}&crType={1}&descriptionofChange={2}&searchQuery={3}&changeAnalyst={4}&status={5}&urgency={6}&originator={7}&requestedBy={8}&changeReasonType={9}".
                    format(filters.crNumber, filters.crType, filters.descriptionofChange, filters.searchQuery, filters.changeAnalyst, filters.status, filters.urgency, filters.originator, filters.requestedBy, filters.changeReasonType);
                return httpFactory.get(url);
            }

            function createEcrItem(dcrId, ecrItem) {
                var url = "api/cms/ecrs/" + dcrId + "/affectedItem";
                return httpFactory.post(url, ecrItem)
            }

            function updateEcrItem(dcrId, ecrItem) {
                var url = "api/cms/ecrs/" + dcrId + "/affectedItem/" + ecrItem.id;
                return httpFactory.put(url, ecrItem)
            }

            function createEcrItems(dcrId, ecrItem) {
                var url = "api/cms/ecrs/" + dcrId + "/affectedItem/multiple";
                return httpFactory.post(url, ecrItem)
            }

            function getAffectedItems(ecr) {
                var url = "api/cms/ecrs/affectedItems/" + ecr;
                return httpFactory.get(url);
            }

            function getFilteredItems(pageable, filters) {
                var url = "api/cms/ecrs/filteredItems?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&itemNumber={0}&itemName={1}&itemType={2}&ecr={3}&dco={4}".
                    format(filters.itemNumber, filters.itemName, filters.itemType, filters.ecr, filters.dco);
                return httpFactory.get(url);
            }

            function createEcrRelatedItems(ecr, items) {
                var url = "api/cms/ecrs/" + ecr + "/relatedItems/multiple";
                return httpFactory.post(url, items);
            }

            function getEcrRelatedItems(ecr) {
                var url = "api/cms/ecrs/relatedItems/" + ecr;
                return httpFactory.get(url);
            }

            function deleteEcrAffectedItem(ecr, id) {
                var url = "api/cms/ecrs/" + ecr + "/affectedItem/delete/" + id;
                return httpFactory.delete(url)
            }

            function deleteEcrRelatedItem(ecr, id) {
                var url = "api/cms/ecrs/" + ecr + "/relatedItem/delete/" + id;
                return httpFactory.delete(url)
            }

            function getEcrDetailsCount(id) {
                var url = "api/cms/ecrs/" + id + "/details/count";
                return httpFactory.get(url)
            }

            function getFilteredEcrItems(pageable, filters) {
                var url = "api/cms/ecrs/filtered/ecr/items?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&crNumber={0}&crType={1}&eco={2}&title={3}".
                    format(filters.crNumber, filters.crType, filters.eco, filters.title);
                return httpFactory.get(url);
            }

            function createECOChangeRequest(eco, items) {
                var url = "api/cms/ecrs/" + eco + "/changeRequest/multiple";
                return httpFactory.post(url, items);
            }

            function getECRChangeRequestItems(eco) {
                var url = "api/cms/ecrs/" + eco + "/changeRequestItems";
                return httpFactory.get(url);
            }

            function deleteEcoChangeRequest(ecrId, crId) {
                var url = "api/cms/ecrs/" + ecrId + "/changeRequestItems/" + crId;
                return httpFactory.delete(url);
            }

            function createECRProblemReports(ecrId, items) {
                var url = "api/cms/ecrs/" + ecrId + "/problemreports/multiple";
                return httpFactory.post(url, items);
            }

            function createECRProblemReport(ecrId, items) {
                var url = "api/cms/ecrs/" + ecrId + "/problemreports";
                return httpFactory.post(url, items);
            }

            function getECRProblemReports(ecrId) {
                var url = "api/cms/ecrs/" + ecrId + "/problemreports";
                return httpFactory.get(url);
            }

            function deleteECRProblemReport(ecrId, prId) {
                var url = "api/cms/ecrs/" + ecrId + "/problemreports/" + prId;
                return httpFactory.delete(url);
            }

            function getChangeAnalysts() {
                var url = "api/cms/ecrs/changeAnalysts" ;
                return httpFactory.get(url);
            }

            function getStatus(){
                var url = "api/cms/ecrs/status" ;
                return httpFactory.get(url);   
            }

            function getUrgency(){
                var url = "api/cms/ecrs/urgency" ;
                return httpFactory.get(url);   
            }
            
            function getOriginators() {
                var url = "api/cms/ecrs/originators" ;
                return httpFactory.get(url);
            }

            function getRequesters() {
                var url = "api/cms/ecrs/requesters" ;
                return httpFactory.get(url);
            }

            function getChangeReasonType() {
                var url = "api/cms/ecrs/changeReasonTypes" ;
                return httpFactory.get(url);
            }
 
        }
    }
);