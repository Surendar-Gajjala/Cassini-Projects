define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('prodSettingsFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    var settingTypes = {
                            'material':'MATERIAL',
                            'product':'PRODUCT',
                            'workShift': 'WORKSHIFT'
                        },
                        getSettingsURL = function(settingType){
                            var url = '';
                            if(settingType === settingTypes.material) {
                                url = "api/production/materialtypes";
                            }else if(settingType === settingTypes.product){
                                url = "api/production/producttypes";
                            }else if(settingType === settingTypes.workShift) {
                                url = "api/production/workshift";
                            }
                            return url;
                        };
                    return {
                        materialTypes: function () {
                            var url = "api/production/materialtypes";
                            return httpFactory.get(url);
                        },
                        productTypes: function() {
                            var url = "api/production/producttypes";
                            return httpFactory.get(url);
                        },
                        workShifts: function() {
                            var url = "api/production/workshift";
                            return httpFactory.get(url);
                        },
                        updateSettings: function(selectedObj, settingTypes) {

                            if(settingTypes == "WORKSHIFT"){
                            var params = (selectedObj.shiftId === "") ? "" : ("/" + selectedObj.shiftId),
                                url = getSettingsURL(settingTypes) + params,
                                requestType = (selectedObj.shiftId === "") ? httpFactory.post(url,selectedObj) : httpFactory.put(url,selectedObj);
                            return requestType;
                            }else{
                                var params = (selectedObj.id === "") ? "" : ("/" + selectedObj.id),
                                    url = getSettingsURL(settingTypes) + params,
                                    requestType = (selectedObj.id === "") ? httpFactory.post(url,selectedObj) : httpFactory.put(url,selectedObj);
                                return requestType;
                            }
                        },
                        deleteSettings: function(selectedObj,settingTypes){
                            if(settingTypes == "WORKSHIFT") {
                                var url = getSettingsURL(settingTypes) + "/" + selectedObj.shiftId;
                                return httpFactory.delete(url, selectedObj);
                            }else{
                                var url = getSettingsURL(settingTypes) + "/" + selectedObj.id;
                                return httpFactory.delete(url, selectedObj);
                            }
                        }
                    }
                }
            ]
        );
    }
);