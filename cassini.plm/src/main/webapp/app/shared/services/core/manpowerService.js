define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ManpowerService', ManpowerService);

        function ManpowerService(httpFactory) {
            return {
                createManpower: createManpower,
                updateManpower: updateManpower,
                getManpower: getManpower,
                deleteManpower: deleteManpower,
                getAllManpower: getAllManpower,
                getMultipleManpower: getMultipleManpower,
                saveManpowerAttributes:saveManpowerAttributes,
                getAllFilterdManpower: getAllFilterdManpower,
                createManpowerContact: createManpowerContact,
                updateManpowerContact: updateManpowerContact,
                deleteManpowerContact: deleteManpowerContact,
                getManpowerContact: getManpowerContact,
                getManpowerContacts: getManpowerContacts,
                getAllManpowerPersons: getAllManpowerPersons,
                getManpowerContactExitOrNot: getManpowerContactExitOrNot
            };

            function createManpower(manpower) {
                var url = "api/mes/manpowers";
                return httpFactory.post(url, manpower)
            }

            function updateManpower(manpower) {
                var url = "api/mes/manpowers/" + manpower.id;
                return httpFactory.put(url,manpower)
            }

            function getManpower(manpowerId) {
                var url = "api/mes/manpowers/" + manpowerId;
                return httpFactory.get(url);
            }

            function deleteManpower(manpower) {
                var url = "api/mes/manpowers/" + manpower;
                return httpFactory.delete(url);
            }
            
            function getAllManpower(pageable, filters) {
                var url = "api/mes/manpowers/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleManpower(manpowerIds) {
                var url = "api/mes/manpowers/multiple/[" + manpowerIds + "]";
                return httpFactory.get(url);
            }

            function saveManpowerAttributes(id,attributes) {
                var url = "api/mes/manpowers/" + id + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getAllFilterdManpower(pageable, filters) {
                var url = "api/mes/manpowers/filtered?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}&operation={4}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery, filters.operation);
                return httpFactory.get(url);
            }
            function createManpowerContact(manpowerId,contact) {
                var url = "api/mes/manpowers/" + manpowerId + "/contacts";
                return httpFactory.post(url, contact)
            }


            function updateManpowerContact(manpowerId,contact) {
                var url = "api/mes/manpowers/" + manpowerId + "/contacts/" + contact.id;
                return httpFactory.put(url, contact);
            }


            function deleteManpowerContact(manpowerId,contactId) {
                var url = "api/mes/manpowers/" + manpowerId + "/contacts/" + contactId;
                return httpFactory.delete(url);
            }

            function getManpowerContacts(manpowerId) {
                var url = "api/mes/manpowers/" + manpowerId + "/contacts";
                return httpFactory.get(url);
            }

            function getManpowerContact(manpowerId,contactId) {
                var url = "api/mes/manpowers/" + manpowerId + "/contacts/" + contactId;
                return httpFactory.get(url);
            }

            function getAllManpowerPersons(pageable, filters) {
                var url = "api/mes/manpowers/contacts/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&shift={1}".
                    format(filters.searchQuery, filters.shift);
                return httpFactory.get(url);
            }

            function getManpowerContactExitOrNot(contactId) {
                var url = "api/mes/manpowers/contacts/" + contactId;
                return httpFactory.get(url);
            }

        }
    }
);