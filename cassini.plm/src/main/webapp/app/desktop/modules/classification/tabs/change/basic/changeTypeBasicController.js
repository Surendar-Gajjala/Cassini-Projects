define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/ecoService'
    ],
    function (module) {
        module.controller('ChangeTypeBasicController', ChangeTypeBasicController);
        function ChangeTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, ItemTypeService,
                                           ClassificationService, AutonumberService, CommonService, DialogService, LovService, $translate, ECOService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.valid = true;
            vm.error = "";
            vm.changeType = null;
            vm.autoNumbers = [];
            vm.revSequences = [];
            vm.lifecycleStates = [];
            vm.lifecycles = [];
            vm.onSave = onSave;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var changeTypeUpdateMessage = parsed.html($translate.instant("CHANGE_TYPE_UPDATE_MESSAGE")).html();
            var Duplicate = $translate.instant("DUPLICATE_MESSAGE");

            function onSave() {
                if (vm.changeType != null && validate()) {
                    var id = $rootScope.selectedClassificationType.id;
                    $rootScope.selectedClassificationType.id = null;
                    ClassificationService.updateType('CHANGETYPE', vm.changeType).then(
                        function (data) {
                            if (data != null) {
                                $rootScope.selectedClassificationType = data;
                                $rootScope.showSuccessMessage(data.name + " : " + changeTypeUpdateMessage);
                                $rootScope.$broadcast("app.classification.update", {
                                    nodeId: nodeId,
                                    nodeName: vm.changeType.name
                                });
                            }
                            else if (data == null) {
                                $rootScope.selectedClassificationType.id = id;
                                $rootScope.showErrorMessage(vm.changeType.name + " : " + Duplicate);
                            }
                        },
                        function (error) {
                            $rootScope.selectedClassificationType.id = id;
                            $rootScope.showErrorMessage(vm.changeType.name + " : " + Duplicate);
                        }
                    )
                }
            }

            var changeTypeValidation = parsed.html($translate.instant("CHANGE_TYPE_VALIDATION")).html();
            var changeReasonTypeValidation = parsed.html($translate.instant("CHANGE_REASON_TYPE_VALIDATION")).html();

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.changeType.name == null || vm.changeType.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(changeTypeValidation);
                } else if ((vm.changeType.objectType == 'ECRTYPE' || vm.changeType.objectType == 'DCRTYPE') && vm.changeType.changeReasonTypes == null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage(changeReasonTypeValidation)
                }

                return vm.valid;
            }

            var nodeId = null;
            $rootScope.changeTypeSelected = changeTypeSelected;
            function changeTypeSelected() {
                if ($rootScope.selectedChangeType != null) {
                    vm.changeType = $rootScope.selectedChangeType;
                    nodeId = $rootScope.selectedChangeNodId;
                    if (vm.changeType != null && vm.changeType != undefined) {
                        if (vm.changeType.id == undefined || vm.changeType.id == null) {
                            vm.attributesShow = false;
                        } else {
                            vm.attributesShow = true;
                        }
                        vm.changeType.attributes = [];
                        loadChanges();
                    } else {
                        if (!$scope.$$phase) $scope.$apply();
                    }

                }

            }

            function loadChanges() {
                if (vm.changeType.id != null) {
                    ECOService.getObjectsByType(vm.changeType.id).then(
                        function (data) {
                            if (data > 0) {
                                vm.changeTypeECOsExist = true;
                            } else {
                                vm.changeTypeECOsExist = false;
                            }
                            $scope.$off('app.changeType.selected', changeTypeSelected);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
            }

            (function () {
                $scope.$on('app.changeType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        $rootScope.changeTypeSelected();
                        $scope.$on('app.changeType.save', onSave);
                    }
                })
                //if ($application.homeLoaded == true) {
                /* $scope.$on('app.changeType.selected', changeTypeSelected);
                 $scope.$on('app.changeType.save', onSave);*/
                //}
            })();
        }
    }
);