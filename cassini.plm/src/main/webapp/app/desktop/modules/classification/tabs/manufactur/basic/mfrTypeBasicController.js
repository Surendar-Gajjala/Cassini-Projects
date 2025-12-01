define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/mfrService'
    ],
    function (module) {
        module.controller('MfrTypeBasicController', MfrTypeBasicController);
        function MfrTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, ItemTypeService,
                                        ClassificationService, AutonumberService, CommonService, DialogService, LovService, $translate, MfrService) {

            var parsed = angular.element("<div></div>");
            var vm = this;
            vm.valid = true;
            vm.error = "";
            vm.mfrType = null;
            vm.autoNumbers = [];
            vm.onSave = onSave;
            var nodeId = null;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            $rootScope.mfrTypeSelected = mfrTypeSelected;
            function mfrTypeSelected() {
                if ($rootScope.selectedmfrType != null) {
                    vm.mfrType = $rootScope.selectedmfrType;
                    nodeId = $rootScope.selectedmfrTypeNodId;
                    if (vm.mfrType != null && vm.mfrType != undefined) {
                        if (vm.mfrType.id == undefined || vm.mfrType.id == null) {
                            vm.attributesShow = false;
                            //$scope.$apply();
                        } else {
                            vm.attributesShow = true;
                            //$scope.$apply();
                        }
                        vm.mfrType.attributes = [];
                        /*loadAutoNumbers();
                         loadAllLovs();
                         loadLifeCycles();*/
                        loadMfrTypeMfrs();
                    } else {
                        if (!$scope.$$phase) $scope.$apply();
                    }
                }

            }


            var mfrTypeSuccessMessage = $translate.instant("MFR_TYPE_SUCCESS_MESSAGE");
            var mfrTypeUpdateMessage = $translate.instant("MFR_TYPE_UPDATE_MESSAGE");
            var mfrTypeValidation = $translate.instant("MANUFACTURER_TYPE_NAME_EMPTY");
            var Duplicate = $translate.instant("DUPLICATE_MESSAGE");

            function onSave() {
                if (vm.mfrType != null && validate()) {
                    var id = $rootScope.selectedClassificationType.id;
                    $rootScope.selectedClassificationType.id = null;
                    ClassificationService.updateType('MANUFACTURERTYPE', vm.mfrType).then(
                        function (data) {
                            if (data != null) {
                                $rootScope.selectedClassificationType = data;
                                $rootScope.showSuccessMessage(data.name + " : " + mfrTypeUpdateMessage);
                                $rootScope.$broadcast("app.classification.update", {
                                    nodeId: nodeId,
                                    nodeName: vm.mfrType.name
                                });
                            }
                            else if (data == null) {
                                $rootScope.selectedClassificationType.id = id;
                                $rootScope.showErrorMessage(vm.mfrType.name + " : " + Duplicate);
                            }
                        },
                        function (error) {
                            $rootScope.selectedClassificationType.id = id;
                            $rootScope.showErrorMessage(vm.mfrType.name + " : " + Duplicate);
                        }
                    )
                }
            }

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.mfrType.name == null || vm.mfrType.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(mfrTypeValidation);
                }

                return vm.valid;
            }

            vm.newLov = "";
            $scope.getLimitedWord = getLimitedWord;
            function getLimitedWord(word, size) {
                if (word.length <= size) {
                    return word;
                } else {
                    return word.substr(0, size) + '...';
                }
            }

            vm.mfrTypeMfrsExist = false;

            function loadMfrTypeMfrs() {
                if (vm.mfrType.id != null) {
                    MfrService.getMfrsByType(vm.mfrType.id, pageable).then(
                        function (data) {
                            vm.mfrTypeMfrs = data.content;
                            if (vm.mfrTypeMfrs.length > 0) {
                                vm.mfrTypeMfrsExist = true;
                            } else {
                                vm.mfrTypeMfrsExist = false;
                            }
                            $scope.$off('app.mfrType.selected', mfrTypeSelected);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
            }

            (function () {
                $scope.$on('app.mfrType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        $rootScope.mfrTypeSelected();
                        //$scope.$on('app.mfrType.selected', mfrTypeSelected);
                        $scope.$on('app.mfrType.save', onSave);
                    }
                })
                //if ($application.homeLoaded == true) {
                /* $scope.$on('app.mfrType.selected', mfrTypeSelected);
                 $scope.$on('app.mfrType.save', onSave);*/

                //}
            })();
        }
    }
);