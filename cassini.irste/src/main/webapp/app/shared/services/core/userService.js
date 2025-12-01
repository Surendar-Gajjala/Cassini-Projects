/**
 * Created by Nageshreddy on 19-11-2018.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('UserService', UserService);
        var baseURL = "api/irste/users";

        function UserService(httpFactory) {
            return {
                createResponder: createResponder,
                getResponders: getResponders,
                createResponderUtility: createResponderUtility,
                createComplainant: createComplainant,
                deleteResponder: deleteResponder,
                getAllResponders: getAllResponders,
                addResponderUtility: addResponderUtility,
                deleteResponderUtility: deleteResponderUtility,
                getFilterUsers: getFilterUsers,
                getByUtilityAndPersonType: getByUtilityAndPersonType,
                freeTextSearch: freeTextSearch
            };

            function addResponderUtility(utility, responder, personType) {
                return httpFactory.post(baseURL + "/responderUtilities/" + utility + "/" + responder+ "/" + personType);
            }

            function deleteResponderUtility(utility, responder) {
                return httpFactory.delete(baseURL + "/responderUtilities/" + utility + "/" + responder);
            }

            function getByUtilityAndPersonType(personType, utility) {
                return httpFactory.get(baseURL + "/responderUtilities/" + personType + "/" + utility + "/byUtility");
            }

            function createResponder(responder) {
                return httpFactory.post(baseURL, responder);
            }

            function createComplainant(person) {
                return httpFactory.post(baseURL + "/complainant", person);
            }

            function createResponderUtility(id, utilities) {
                return httpFactory.post(baseURL + "/" + id + "/utilities", utilities);
            }

            function getResponders(pageable) {
                var url = baseURL + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getFilterUsers(pageable, filters) {
                var url = baseURL + "/users/pageable/filters?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&presentStatus={0}&flowStep={1}&searchQuery={2}&personTypes={3}"
                    .format(filters.presentStatus, filters.flowStep, filters.searchQuery, filters.personTypes);
                return httpFactory.get(url);
            }

            function getAllResponders(personType) {
                return httpFactory.get(baseURL + "/all/" + personType);
            }

            function deleteResponder(id) {
                return httpFactory.delete(baseURL + "/" + id);
            }

            function freeTextSearch(pageable, filters) {
                var url = baseURL + "/freeTextSearch?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&location={0}&utility={1}&searchQuery={2}&personTypes={3}".format(filters.firstName, filters.designation, filters.searchQuery, filters.personTypes);
                return httpFactory.get(url);
            }

        }

    }
);