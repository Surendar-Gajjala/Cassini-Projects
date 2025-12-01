define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ContactService', ContactService);

        function ContactService(httpFactory) {
            return {
                createContact: createContact,
                getContact: getContact,
                updateContact: updateContact,
                deleteContact: deleteContact,
                getAllContacts: getAllContacts,
            };

            function createContact(contact) {
                var url = "api/cms/contacts";
                return httpFactory.post(url, contact)
            }

            function getContact(id) {
                var url = "api/cms/contacts/" + id;
                return httpFactory.get(url)
            }

            function updateContact(contact) {
                var url = "api/cms/contacts/" + contact.id;
                return httpFactory.put(url, contact);
            }

            function deleteContact(contact) {
                var url = "api/cms/contacts/" + contact;
                return httpFactory.delete(url);
            }

            function getAllContacts(pageable, filters) {
                var url = "api/cms/contacts/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&contactNumber={0}&description={1}&contactType={2}&searchQuery={3}".
                    format(filters.contactNumber, filters.description, filters.contactType, filters.searchQuery);
                return httpFactory.get(url);
            }
        }
    }
)