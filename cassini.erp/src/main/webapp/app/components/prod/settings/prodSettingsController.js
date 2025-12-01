define(['app/app.modules',
        'bootstrap-timepicker',
        'app/components/prod/settings/prodSettingsFactory',
        'app/shared/directives/commonDirectives',
        'app/shared/constants/constants'],
    function($app) {
        $app.controller('ProductionSettingsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'prodSettingsFactory', 'CONSTANTS',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies, prodSettingsFactory, CONSTANTS) {
                    var newMaterialType = {
                            "description": "",
                            "id": "",
                            "name": "",
                            "editMode": true
                        },
                        newProductType = {
                            "description": "",
                            "id": "",
                            "name": "",
                            "editMode": true
                        },
                        newWorkShift = {
                            startTime: moment(new Date()).format('hh:mm A'),
                            endTime: moment(new Date()).add(1, 'h').format('hh:mm A'),
                            "name": "",
                            "shiftId": "",
                            "editMode": true
                        },
                        init = function() {
                            getMaterialTypes();
                            getProductTypes();
                            getWorkShifts();
                           },
                            getMaterialTypes = function(){
                            prodSettingsFactory.materialTypes().then (
                                function(materialTypes) {
                                    $scope.materialTypes = materialTypes;
                                },

                                function (error) {

                                }
                            );
                        },
                            getProductTypes = function(){
                            prodSettingsFactory.productTypes().then (
                                function(productTypes) {
                                    $scope.productTypes = productTypes;
                                },

                                function (error) {

                                }
                            );
                        },
                            getWorkShifts = function(){
                            prodSettingsFactory.workShifts().then (
                                function(workShifts) {
                                    $scope.workShifts = workShifts;
                                },

                                function (error) {

                                }
                            );
                            },
                        updateChanges = function(currentObj,settingType) {
                            prodSettingsFactory.updateSettings(currentObj,settingType).then (
                                function(response) {
                                    currentObj.id = response.id;
                                },

                                function (error) {

                                }
                            );
                        },
                        deleteChanges = function(currentObj,settingType) {
                            prodSettingsFactory.deleteSettings(currentObj,settingType).then (
                                function(response) {

                                },

                                function (error) {

                                }
                            );
                        };

                    $rootScope.iconClass = "fa fa-wrench";
                    $rootScope.viewTitle = "PROD Settings";

                    $scope.timeFormatShift=function(time){
                        return moment(time).format('hh:mm A')
                    }
                    $scope.materialTypes = [];
                    $scope.productTypes = [];
                    $scope.workShifts = [];
                    $scope.constants = CONSTANTS;


                    $scope.settingTypes = {
                        'material':'MATERIAL',
                        'product':'PRODUCT',
                        'workShift': 'WORKSHIFT'
                     };

                    $scope.showEditMode = function (selectedObj) {
                        selectedObj.editMode = true;

                        selectedObj.startTime=moment(selectedObj.startTime).format('hh:mm A')
                        selectedObj.endTime=moment(selectedObj.endTime).format('hh:mm A')

                    };

                    $scope.hideEditMode = function (selectedObj,settingType) {
                        $timeout(function() {
                            if(selectedObj.id === ''){
                                if(settingType === $scope.settingTypes.material) {
                                    $scope.materialTypes.pop();
                                }else if(settingType === $scope.settingTypes.product) {
                                    $scope.productTypes.pop();
                                }else if(settingType === $scope.settingTypes.workShift) {
                                    $scope.workShifts.pop();
                                }
                              }else{
                                selectedObj.editMode = false;
                            }
                        }, 500);
                    };

                    $scope.acceptChanges = function(selectedObj,settingType) {
                        $timeout(function() {
                            selectedObj.editMode = false;
                        }, 500);

                      //  selectedObj.startTime=moment(selectedObj.startTime).format('hh:mm A')
                       // selectedObj.endTime=moment(selectedObj.endTime).format('hh:mm A')

                        updateChanges(selectedObj,settingType);
                    };

                    $scope.addMaterialType = function($event,settingType) {
                        $event.stopPropagation();
                        $event.preventDefault();
                        var temp = {};

                        if(settingType === $scope.settingTypes.material) {
                            temp = angular.copy(newMaterialType);
                            $scope.materialTypes.push(temp);
                        }else if(settingType === $scope.settingTypes.product) {
                            temp = angular.copy(newProductType);
                            $scope.productTypes.push(temp);
                        }else if(settingType === $scope.settingTypes.workShift) {
                            temp = angular.copy(newWorkShift);
                            $scope.workShifts.push(temp);
                        }


                    };

                    $scope.removeItem = function(index, selectedObj,settingType) {

                        if(settingType === $scope.settingTypes.material) {
                            $scope.materialTypes.splice(index,1);
                        }else if(settingType === $scope.settingTypes.product) {
                            $scope.productTypes.splice(index,1);

                        }else if(settingType === $scope.settingTypes.workShift) {
                            $scope.workShifts.splice(index,1);

                        }

                        deleteChanges(selectedObj,settingType);
                    };

                    init();

                    (function() {
                        $('#shiftStartTime').timepicker({defaultTime: 'current'});
                        $('#shiftStartTime').timepicker().on('changeTime.timepicker', function(e) {
                            $scope.workShift.startTime = e.time.value;
                        });
                        $('#shiftEndTime').timepicker({defaultTime: 'current'});
                        $('#shiftEndTime').timepicker().on('changeTime.timepicker', function(e) {
                            $scope.workShift.endTime = e.time.value;
                        });

                    })();
                }
            ]
        );
    }
);

