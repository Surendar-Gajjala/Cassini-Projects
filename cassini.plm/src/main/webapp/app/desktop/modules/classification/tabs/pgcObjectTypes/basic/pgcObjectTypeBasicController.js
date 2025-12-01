define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/pgcObjectTypeService'
    ],
    function (module) {
        module.controller('PGCObjectTypeBasicController', PGCObjectTypeBasicController);
        function PGCObjectTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                              PGCObjectTypeService, CommonService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            $rootScope.loadPgcBasicInfo = loadPgcBasicInfo;
            function loadPgcBasicInfo() {
                if ($rootScope.selectedPgcObjectType != null) {
                    PGCObjectTypeService.getPgcObjectTypeByType($rootScope.selectedPgcObjectType.id, $rootScope.selectedPgcObjectType.objectType).then(
                        function (data) {
                            $rootScope.pgcObjectType = data;
                            loadPgcObjectTypeObjects();
                            $scope.$off('app.pgcObjectType.selected', loadPgcBasicInfo);
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadPgcObjectTypeObjects() {
                PGCObjectTypeService.getObjectsCountByType($rootScope.selectedPgcObjectType.id, $rootScope.selectedPgcObjectType.objectType).then(
                    function (data) {
                        if (data > 0) {
                            $rootScope.pgcTypeObjectsExist = true;
                        } else {
                            $rootScope.pgcTypeObjectsExist = false;
                        }
                    }
                )
            }

            var lifecycleHasNoValues = parsed.html($translate.instant("LIFECYCLE_HAS_NO_VALUES")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            vm.onSave = onSave;
            function onSave() {
                if (validate()) {
                    if ($rootScope.selectedPgcObjectType != null) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if ($rootScope.selectedPgcObjectType.objectType == "PGCSUBSTANCETYPE") {
                            PGCObjectTypeService.updateSubstanceType($rootScope.pgcObjectType).then(
                                function (data) {
                                    $rootScope.loadPgcBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedPgcObjectTypeNode,
                                        nodeName: $rootScope.pgcObjectType.name
                                    });
                                    $rootScope.showSuccessMessage("Substance Type updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedPgcObjectType.objectType == "PGCSUBSTANCEGROUPTYPE") {
                            PGCObjectTypeService.updateSubstanceGroupType($rootScope.pgcObjectType).then(
                                function (data) {
                                    $rootScope.loadPgcBasicInfo();
                                    $rootScope.showSuccessMessage("Substancegroup Type updated successfully");
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedPgcObjectTypeNode,
                                        nodeName: $rootScope.pgcObjectType.name
                                    });
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedPgcObjectType.objectType == "PGCSPECIFICATIONTYPE") {
                            PGCObjectTypeService.updateSpecificationType($rootScope.pgcObjectType).then(
                                function (data) {
                                    $rootScope.loadPgcBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedPgcObjectTypeNode,
                                        nodeName: $rootScope.pgcObjectType.name
                                    });
                                    $rootScope.showSuccessMessage("Specification Type updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedPgcObjectType.objectType == "PGCDECLARATIONTYPE") {
                            PGCObjectTypeService.updateDeclarationType($rootScope.pgcObjectType).then(
                                function (data) {
                                    $rootScope.loadPgcBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedPgcObjectTypeNode,
                                        nodeName: $rootScope.pgcObjectType.name
                                    });
                                    $rootScope.showSuccessMessage("Declaration Type updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else {
                            $rootScope.hideBusyIndicator();
                        }
                    }
                }
            }

            vm.onSelectLifecycle = onSelectLifecycle;
            function onSelectLifecycle(lifecycle) {
                if (lifecycle.phases.length == 0) {
                    $rootScope.pgcObjectType.lifecycle = null;
                    $rootScope.showWarningMessage(lifecycle.name + " : " + lifecycleHasNoValues);
                }
            }

            function validate() {
                var valid = true;
                if ($rootScope.pgcObjectType.name == "" || $rootScope.pgcObjectType.name == "" || $rootScope.pgcObjectType.name == undefined) {
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                }

                return valid;
            }

            (function () {
                $scope.$on('app.selectedPgcType.tabActivated', function (event, data) {
                    $rootScope.loadPgcBasicInfo();
                })
            })();
        }
    }
);