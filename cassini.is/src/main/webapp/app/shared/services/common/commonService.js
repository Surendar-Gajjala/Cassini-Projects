define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('CommonService', CommonService);

        function CommonService($q, httpFactory) {
            var countriesAndStates = [];
            var addressTypes = [];
            var personTypes = [];
            var statesMap = new Hashtable();

            function getAllPersons() {
                var url = "api/common/persons/all";
                return httpFactory.get(url);
            }

            function getPersons(personIds) {
                var url = "api/common/persons/multiple/[" + personIds + "]";
                return httpFactory.get(url);
            }

            function getPerson(personId) {
                var url = "api/common/persons/" + personId;
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
                                        object[property + "Object"] = person;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getLovByName(name) {
                return $q(function (resolve, reject) {
                    getLovs().then(
                        function (data) {
                            var found = null;
                            angular.forEach(data.content, function (item) {
                                if (item.name == name) {
                                    found = item;
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

            return {
                getPersons: getPersons,
                getMultipleWbs: getMultipleWbs,
                getAllPersons: getAllPersons,
                getPerson: getPerson,
                createPerson: createPerson,
                updatePerson: updatePerson,
                deletePerson: deletePerson,
                getPersonTypes: getPersonTypes,
                getPersonTypeByName: getPersonTypeByName,
                getPersonReferences: getPersonReferences,


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
                getLovByName: getLovByName,

                getCountries: getCountries,
                getCountryByName: getCountryByName,
                getCountryAndStatesMapByCountry: getCountryAndStatesMapByCountry,

                getStates: getStates,
                getStateById: getStateById,
                getCountriesAndStates: getCountriesAndStates,

                initialize: initialize
            };

        }
    }
);