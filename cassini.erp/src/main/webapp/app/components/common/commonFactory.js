define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('commonFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {

                    var emptyAddress = {

                    };

                    var countriesAndStates = [];
                    var addressTypes = [];
                    var personTypes = [];

                    var getCountries = function() {
                        var dfd = $q.defer(),
                            url = "api/common/countries";
                        return httpFactory.get(url);
                    };

                    var getStates = function(country) {
                        var dfd = $q.defer(),
                            url = "api/common/countries/" + country + "/states";
                        return httpFactory.get(url);
                    };

                    var getCountriesAndStates = function() {
                        var dfd = $q.defer(),
                            url = "api/common/countries/states";
                        return httpFactory.get(url);
                    };

                    var getAddressTypes = function() {
                        var dfd = $q.defer(),
                            url = "api/common/addresstypes";
                        return httpFactory.get(url);
                    };

                    var getPersonTypes = function() {
                        var dfd = $q.defer(),
                            url = "api/common/persontypes";
                        return httpFactory.get(url);
                    };

                    var getTemplate = function(path) {
                        var dfd = $q.defer();
                        return httpFactory.get(path);
                    };

                    function getPersonTypeByName(name) {
                        var personType = null;
                        angular.forEach(personTypes, function(type) {
                            if(type.name == name) {
                                personType = type;
                            }
                        });

                        return personType;
                    }

                    function getAddressTypeByName(name) {
                        var addressType = null;
                        angular.forEach(addressTypes, function(type) {
                            if(type.name == name) {
                                addressType = type;
                            }
                        });

                        return addressType;
                    }

                    function getCountryByName(name) {
                        var country = null;
                        angular.forEach(countriesAndStates, function(item) {
                             if(item.country.name == name) {
                                 country = item.country;
                             }
                        });

                        return country;
                    }

                    function getCountryAndStatesMapByCountry(name) {
                        var countryAndStates = null;
                        angular.forEach(countriesAndStates, function(item) {
                            if(item.country.name == name) {
                                countryAndStates = item;
                            }
                        });

                        return countryAndStates;
                    }

                    function initialize() {
                        getCountriesAndStates().then(
                            function(data) {
                                countriesAndStates = data;
                                return getAddressTypes();
                            }
                        ).then(
                            function(data) {
                                addressTypes = data;
                                return getPersonTypes();
                            }
                        ).then(
                            function(data) {
                                personTypes = data;
                            }
                        )
                    }

                    return {
                        initialize: initialize,

                        getCountries: getCountries,
                        getCountryByName: getCountryByName,
                        getCountryAndStatesMapByCountry: getCountryAndStatesMapByCountry,

                        getStates: getStates,
                        getCountriesAndStates: getCountriesAndStates,

                        getAddressTypes: getAddressTypes,
                        getAddressTypeByName: getAddressTypeByName,

                        getPersonTypes: getPersonTypes,
                        getPersonTypeByName: getPersonTypeByName,

                        getTemplate: getTemplate
                    }
                }
            ]
        );
    }
);