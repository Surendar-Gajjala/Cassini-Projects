define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('CommonService', CommonService);

        function CommonService($q, httpFactory) {
            var countriesAndStates = [];
            var addressTypes = [];
            var personTypes = [];
            var statesMap = new Hashtable();

            function findAllPersons() {
                var url = "api/common/persons/all";
                return httpFactory.get(url);
            }

            function savePassword(context, password) {
                var url = "api/common/persons/savePassword/" + context + "/password/" + password;
                return httpFactory.post(url);
            }

            function saveLogoutTime(context, time) {
                var url = "api/common/persons/saveLogoutTime/" + context + "/time/" + time;
                return httpFactory.post(url);
            }

            function saveCustomLogo(file) {
                var url = "api/common/persons/saveCustomLogo/uploadLogo";
                return httpFactory.upload(url, file);
            }

            function deleteCustomLogo(Id) {
                var url = "api/common/persons/deleteCustomLogo/" + Id;
                return httpFactory.put(url);
            }

            function getPreferenceByKey(key) {
                var url = "api/common/persons/getPreferenceByKey/" + key;
                return httpFactory.get(url);
            }

            function getSessionTime() {
                var url = 'api/common/persons/getSessionTime';
                return httpFactory.get(url);
            }

            function saveTheme(context, theme) {
                var url = "api/common/persons/saveTheme/" + context + "/theme/" + theme;
                return httpFactory.post(url);
            }

            function getPreferences() {
                var url = "api/common/persons/preferences";
                return httpFactory.get(url);
            }

            function updatePassword(id, password) {
                var url = "api/common/persons/updatePassword/" + id + "/password/" + password;
                return httpFactory.put(url);
            }

            function updateCustomLogo(id, file) {
                var url = "api/common/persons/customLogo/" + id + "/uploadLogo";
                return httpFactory.upload(url, file);
            }

            function updateLogoutTime(id, time) {
                var url = "api/common/persons/updateLogoutTime/" + id + "/time/" + time;
                return httpFactory.put(url);
            }

            function updateTheme(id, theme) {
                var url = "api/common/persons/updateTheme/" + id + "/theme/" + theme;
                return httpFactory.put(url);
            }

            function updateSystemDateFormat(preference) {
                var url = "api/common/persons/update/dateformat/" + preference.id;
                return httpFactory.put(url, preference);
            }

            function saveFileSize(context, fileSize) {
                var url = "api/common/persons/saveFileSize/" + context + "/fileSize/" + fileSize;
                return httpFactory.post(url);
            }

            function saveFileType(context, fileType) {
                var url = "api/common/persons/saveFileType/" + context + "/fileType";
                return httpFactory.post(url, fileType);
            }

            function updateFileSize(id, fileSize) {
                var url = "api/common/persons/updateFileSize/" + id + "/fileSize/" + fileSize;
                return httpFactory.put(url);
            }

            function updateFileType(id, fileType) {
                var url = "api/common/persons/updateFileType/" + id + "/fileType";
                return httpFactory.post(url, fileType);
            }

            function updateRecurringItem(id, itemSize) {
                var url = "api/common/persons/updateRecurringItem/" + id + "/recurringItem/" + itemSize;
                return httpFactory.post(url);
            }

            function getPreferenceById(Id) {
                var url = "api/common/persons/getPreferenceById/" + Id;
                return httpFactory.get(url);
            }

            function getPreferenceByContext(context) {
                var url = "api/common/persons/getPreferenceByContext/" + context;
                return httpFactory.get(url);
            }

            function exportReport(fileType, objects) {
                var url = "api/common/exports";
                url += "?fileType={0}".format(fileType);
                return httpFactory.post(url, objects);
            }

            function getPersonsByPersonType(persontype, pageable) {
                var url = "api/common/persons/persontype/" + persontype + "/pageable";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllPersonsByPersonType(persontype) {
                var url = "api/common/persons/persontype/" + persontype;
                return httpFactory.get(url);
            }

            function getAllPersons() {
                var url = "api/common/persons/all";
                return httpFactory.get(url);
            }

            function getAllActivePersons() {
                var url = "api/common/persons/activePersons";
                return httpFactory.get(url);
            }

            function freeTextSearch(criteria, pageable) {
                var url = "api/common/persons/freesearch";
                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&objectPerson={1}".format(criteria.searchQuery, criteria.objectPerson);
                return httpFactory.get(url);
            }

            function getPersons(personIds) {
                var url = "api/common/persons/multiple/[" + personIds + "]";
                return httpFactory.get(url);
            }

            function getPageablePersons(pageable) {
                var url = "api/common/persons/?page={0}&size={1}".
                    format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getPerson(personId) {
                var url = "api/common/persons/" + personId;
                return httpFactory.get(url);
            }

            function getPersonByEmail(email) {
                var url = "api/common/persons/" + email + "/email";
                return httpFactory.get(url);
            }

            function createPerson(person) {
                var url = "api/common/persons";
                return httpFactory.post(url, person);
            }

            function updatePerson(person) {
                var url = "api/common/persons/" + person.id;
                return httpFactory.put(url, person);
            }

            function deletePerson(personId) {
                var url = "api/common/persons/" + personId;
                return httpFactory.delete(url);
            }

            function getLovs() {
                var url = 'api/core/lovs';
                return httpFactory.get(url);
            }

            function getPersonRoleLovs() {
                var url = 'api/core/lovs/personLovs';
                return httpFactory.get(url);
            }

            function getAddressesByIds(addressIds) {
                var url = "api/common/addresses/multiple/[" + addressIds + "]";
                return httpFactory.get(url);
            }

            function getAddresses() {
                var url = "api/common/addresses";
                return httpFactory.get(url);
            }

            function getAddress(addressId) {
                var url = "api/common/addresses/" + addressId;
                return httpFactory.get(url);
            }

            function createAddress(address) {
                var url = "api/common/addresses";
                return httpFactory.post(url, address);
            }

            function updateAddress(address) {
                var url = "api/common/addresses/" + address.id;
                return httpFactory.put(url, address);
            }

            function deleteAddress(addressId) {
                var url = "api/common/addresses/" + addressId;
                return httpFactory.delete(url);
            }

            function getCountries() {
                var url = "api/common/countries";
                return httpFactory.get(url);
            }

            function getStates(country) {
                var url = "api/common/countries/" + country + "/states";
                return httpFactory.get(url);
            }

            function getCountriesAndStates() {
                var url = "api/common/countries/states";
                return httpFactory.get(url);
            }

            function getAddressTypes() {
                var url = "api/common/addresstypes";
                return httpFactory.get(url);
            }

            function getPersonTypes() {
                var url = "api/common/persontypes";
                return httpFactory.get(url);
            }

            function getPersonType(personTypeId) {
                var url = "api/common/persontypes/" + personTypeId;
                return httpFactory.get(url);
            }

            function getPersonTypeByName(name) {
                var personType = null;
                angular.forEach(personTypes, function (type) {
                    if (type.name == name) {
                        personType = type;
                    }
                });

                return personType;
            }

            function getAddressTypeByName(name) {
                var addressType = null;
                angular.forEach(addressTypes, function (type) {
                    if (type.name == name) {
                        addressType = type;
                    }
                });

                return addressType;
            }

            function getCountryByName(name) {
                var country = null;
                angular.forEach(countriesAndStates, function (item) {
                    if (item.country.name == name) {
                        country = item.country;
                    }
                });

                return country;
            }

            function getCountryAndStatesMapByCountry(name) {
                var countryAndStates = null;
                angular.forEach(countriesAndStates, function (item) {
                    if (item.country.name == name) {
                        countryAndStates = item;
                    }
                });

                return countryAndStates;
            }

            function getAddressReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getAddressesByIds(ids).then(
                        function (data) {
                            var map = new Hashtable();

                            angular.forEach(data, function (item) {
                                map.put(item.id, item);
                            });

                            angular.forEach(data, function (address) {
                                address.stateObject = getStateById(address.state);
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

            function getPersonReferences(objects, property) {
                var personIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && personIds.indexOf(object[property]) == -1) {
                        personIds.push(object[property]);
                    }
                });

                if (personIds.length > 0) {
                    getPersons(personIds).then(
                        function (persons) {
                            var map = new Hashtable();
                            angular.forEach(persons, function (person) {
                                map.put(person.id, person);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var person = map.get(object[property]);
                                    if (person != null) {
                                        if (person.lastName == null) {
                                            person.fullName = person.firstName;
                                            object[property + "Object"] = person;
                                        }
                                        else {
                                            person.fullName = person.firstName + " " + person.lastName;
                                            object[property + "Object"] = person
                                        }
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getMultiplePersonReferences(objects, properties) {
                var personIds = [];
                angular.forEach(objects, function (object) {
                    angular.forEach(properties, function (property) {
                        if (object[property] != null && personIds.indexOf(object[property]) == -1) {
                            personIds.push(object[property]);
                        }
                    });

                });

                if (personIds.length > 0) {
                    getPersons(personIds).then(
                        function (persons) {
                            var map = new Hashtable();
                            angular.forEach(persons, function (person) {
                                map.put(person.id, person);
                            });

                            angular.forEach(objects, function (object) {
                                angular.forEach(properties, function (property) {
                                    if (object[property] != null) {
                                        var person = map.get(object[property]);
                                        if (person != null) {
                                            if (person.lastName == null) {
                                                person.fullName = person.firstName;
                                                object[property + "Object"] = person;
                                            }
                                            else {
                                                person.fullName = person.firstName + " " + person.lastName;
                                                object[property + "Object"] = person
                                            }
                                        }
                                    }
                                });
                            });
                        }
                    );
                }
            }

            function getLovByName(name) {
                var url = "api/core/lovs/lovByName/" + name;
                return httpFactory.get(url);
            }

            function getLovByType(type) {
                return $q(function (resolve, reject) {
                    getLovs().then(
                        function (data) {
                            var found = [];
                            angular.forEach(data.content, function (item) {
                                if (item.type == type) {
                                    found.push(item);
                                }
                            });

                            resolve(found);
                        }
                    );
                });
            }

            function initialize() {
                getCountriesAndStates().then(
                    function (data) {
                        countriesAndStates = data;
                        buildStatesMap();
                        return getAddressTypes();
                    }
                ).then(
                    function (data) {
                        addressTypes = data.content;
                        return getPersonTypes();
                    }
                ).then(
                    function (data) {
                        personTypes = data.content;
                    }
                )
            }

            function buildStatesMap() {
                angular.forEach(countriesAndStates, function (item) {
                    angular.forEach(item.states, function (state) {
                        statesMap.put(state.id, state);
                    });
                });
            }

            function getStateById(id) {
                return statesMap.get(id);
            }

            function getAllPersonTypes() {
                return httpFactory.get("api/common/persontypes/all");
            }

            function getPersonGroups(personId) {
                return httpFactory.get("api/common/persons/" + personId + "/groups");
            }

            function getPersonGroupPermissions(personId) {
                return httpFactory.get("api/common/persons/" + personId + "/groups/permissions");
            }

            function uploadPersonImage(itemId, file) {
                var url = "api/common/persons/" + itemId + "/image";
                return httpFactory.upload(url, file);
            }

            function deletePersonImage(personId) {
                var url = "api/common/persons/" + personId + "/image";
                return httpFactory.delete(url);
            }

            function getPersonsCountWithoutLogin() {
                var url = "api/common/persons/withoutlogincount";
                return httpFactory.get(url);
            }

            function saveForgeDetails(objs) {
                var url = "api/preferences/multiple";
                return httpFactory.post(url, objs);
            }

            function updateLicenceInfo(preference) {
                var url = "api/preferences/licence/" + preference.id;
                return httpFactory.put(url, preference);
            }

            function saveHolidays(list) {
                var url = "api/preferences/holiday";
                return httpFactory.post(url, list);
            }

            function saveWorkingDays(days) {
                var url = "api/preferences/workingDays/" + days;
                return httpFactory.post(url);
            }

            return {
                findAllPersons: findAllPersons,
                getAllPersons: getAllPersons,
                getPersons: getPersons,
                getPageablePersons: getPageablePersons,
                freeTextSearch: freeTextSearch,
                getPerson: getPerson,
                createPerson: createPerson,
                updatePerson: updatePerson,
                deletePerson: deletePerson,
                getAllPersonsByPersonType: getAllPersonsByPersonType,
                getPersonsByPersonType: getPersonsByPersonType,
                getPersonTypes: getPersonTypes,
                getPersonTypeByName: getPersonTypeByName,
                getPersonReferences: getPersonReferences,
                getMultiplePersonReferences: getMultiplePersonReferences,


                getAddresses: getAddresses,
                getAddressesByIds: getAddressesByIds,
                getAddress: getAddress,
                createAddress: createAddress,
                deleteAddress: deleteAddress,
                updateAddress: updateAddress,
                getAddressTypes: getAddressTypes,
                getAddressTypeByName: getAddressTypeByName,
                getAddressReferences: getAddressReferences,

                getLovs: getLovs,
                getPersonRoleLovs: getPersonRoleLovs,
                getLovByName: getLovByName,
                getLovByType: getLovByType,

                getCountries: getCountries,
                getCountryByName: getCountryByName,
                getCountryAndStatesMapByCountry: getCountryAndStatesMapByCountry,

                getStates: getStates,
                getStateById: getStateById,
                getCountriesAndStates: getCountriesAndStates,
                exportReport: exportReport,
                initialize: initialize,
                getPersonType: getPersonType,
                getPersonByEmail: getPersonByEmail,
                getAllPersonTypes: getAllPersonTypes,
                savePassword: savePassword,
                saveLogoutTime: saveLogoutTime,
                saveCustomLogo: saveCustomLogo,
                saveTheme: saveTheme,
                updatePassword: updatePassword,
                updateCustomLogo: updateCustomLogo,
                updateLogoutTime: updateLogoutTime,
                updateTheme: updateTheme,
                updateSystemDateFormat: updateSystemDateFormat,
                getPreferenceById: getPreferenceById,
                getPreferenceByContext: getPreferenceByContext,
                saveFileSize: saveFileSize,
                saveFileType: saveFileType,
                updateFileType: updateFileType,
                updateFileSize: updateFileSize,
                getSessionTime: getSessionTime,
                getPreferences: getPreferences,
                getPreferenceByKey: getPreferenceByKey,
                deleteCustomLogo: deleteCustomLogo,
                updateRecurringItem: updateRecurringItem,
                getPersonGroups: getPersonGroups,
                uploadPersonImage: uploadPersonImage,
                deletePersonImage: deletePersonImage,
                getPersonsCountWithoutLogin: getPersonsCountWithoutLogin,
                saveForgeDetails: saveForgeDetails,
                updateLicenceInfo: updateLicenceInfo,
                saveHolidays: saveHolidays,
                saveWorkingDays: saveWorkingDays,
                getPersonGroupPermissions: getPersonGroupPermissions,
                getAllActivePersons: getAllActivePersons
            };

        }
    }
);