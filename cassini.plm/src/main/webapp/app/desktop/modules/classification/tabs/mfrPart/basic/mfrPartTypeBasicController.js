define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/mfrPartsService'
    ],
    function (module) {
        module.controller('MfrPartTypeBasicController', MfrPartTypeBasicController);

        function MfrPartTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, ItemTypeService,
                                            ClassificationService, AutonumberService, CommonService, DialogService, LovService, $translate, MfrPartsService) {


            var parsed = angular.element("<div></div>");
            var vm = this;
            vm.valid = true;
            vm.error = "";
            vm.mfrPartType = null;
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
            $rootScope.mfrPartTypeSelected = mfrPartTypeSelected;
            function mfrPartTypeSelected() {
                if ($rootScope.selectedmfrPartType != null) {
                    vm.mfrPartType = $rootScope.selectedmfrPartType;
                    nodeId = $rootScope.selectedmfrPartTypeNodId;
                    if (vm.mfrPartType != null && vm.mfrPartType != undefined) {
                        if (vm.mfrPartType.id == undefined || vm.mfrPartType.id == null) {
                            vm.attributesShow = false;
                            //$scope.$apply();
                        } else {
                            vm.attributesShow = true;
                            //$scope.$apply();
                        }
                        vm.mfrPartType.attributes = [];
                        loadMfrPartTypeParts();
                    } else {
                        if (!$scope.$$phase) $scope.$apply();
                    }
                }

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

            vm.mfrPartTypePartsExist = false;
            function loadMfrPartTypeParts() {
                if (vm.mfrPartType.id != null) {
                    MfrPartsService.getMfrPartsByType(vm.mfrPartType.id, pageable).then(
                        function (data) {
                            vm.mfrPartTypeParts = data.content;
                            if (vm.mfrPartTypeParts.length > 0) {
                                vm.mfrPartTypePartsExist = true;
                            } else {
                                vm.mfrPartTypePartsExist = false;
                            }
                            $scope.$off('app.mfrPartType.selected', mfrPartTypeSelected);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
            }

            var mfrPartTypeSuccessMessage = $translate.instant("MFR_PART_TYPE_SUCCESS_MESSAGE");
            var mfrPartTypeUpdateMessage = $translate.instant("MFR_PART_TYPE_UPDATE_MESSAGE");
            var mfrPartTypeValidation = $translate.instant("MANUFACTURER_PART_TYPE_NAME_EMPTY");
            var Duplicate = $translate.instant("DUPLICATE_MESSAGE");

            function onSave() {
                if (vm.mfrPartType != null && validate()) {
                    var id = $rootScope.selectedClassificationType.id;
                    $rootScope.selectedClassificationType.id = null;
                    ClassificationService.updateType('MANUFACTURERPARTTYPE', vm.mfrPartType).then(
                        function (data) {
                            if (data != null) {
                                $rootScope.selectedClassificationType = data;
                                $rootScope.showSuccessMessage(data.name + " : " + mfrPartTypeUpdateMessage);
                                $rootScope.$broadcast("app.classification.update", {
                                    nodeId: nodeId,
                                    nodeName: vm.mfrPartType.name
                                });
                            }
                            else if (data == null) {
                                $rootScope.selectedClassificationType.id = id;
                                $rootScope.showErrorMessage(vm.mfrPartType.name + " : " + Duplicate);
                            }
                        },
                        function (error) {
                            $rootScope.selectedClassificationType.id = id;
                            $rootScope.showErrorMessage(vm.mfrPartType.name + " : " + Duplicate);
                        }
                    )
                }
            }

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.mfrPartType.name == null || vm.mfrPartType.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(mfrPartTypeValidation);
                }

                return vm.valid;
            }

            (function () {
                $scope.$on('app.mfrPartType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        $rootScope.mfrPartTypeSelected();
                        // $scope.$on('app.mfrPartType.selected', mfrPartTypeSelected);
                        $scope.$on('app.mfrPartType.save', onSave);
                    }
                })
                //if ($application.homeLoaded == true) {
                //$scope.$on('app.mfrPartType.selected', mfrPartTypeSelected);
                $scope.$on('app.mfrPartType.save', onSave);

                //}
            })();
        }
    });