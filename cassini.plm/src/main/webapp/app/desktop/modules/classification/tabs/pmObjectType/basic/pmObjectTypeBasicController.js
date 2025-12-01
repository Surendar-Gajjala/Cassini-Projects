define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/pmObjectTypeService',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('PMObjectTypeBasicController', PMObjectTypeBasicController);
        function PMObjectTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                             MESObjectTypeService, PMObjectTypeService, AutonumberService, ItemTypeService, CommonService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            $rootScope.loadPMBasicInfo = loadPMBasicInfo;
            function loadPMBasicInfo() {
                if ($rootScope.selectedObjectType != null && $rootScope.selectedObjectType != undefined && $rootScope.selectedObjectType.objectType != undefined && $rootScope.selectedObjectType.id != undefined) {
                    PMObjectTypeService.getPMObjectTypeByType($rootScope.selectedObjectType.id, $rootScope.selectedObjectType.objectType).then(
                        function (data) {
                            $rootScope.resourceType = data;
                            $scope.$evalAsync();
                            $scope.$off('app.pmType.selected', loadPMBasicInfo);
                            loadDisplayTabs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var projects = parsed.html($translate.instant("PROJECTS")).html();
            var requirements = parsed.html($translate.instant("REQUIREMENTS")).html();
            var team = parsed.html($translate.instant("TEAM")).html();
            var plan = parsed.html($translate.instant("PLAN")).html();
            var deliverables = parsed.html($translate.instant("DELIVERABLES")).html();
            var referenceItems = parsed.html($translate.instant("REFERENCE_ITEMS")).html();
            var workflow = parsed.html($translate.instant("WORKFLOW")).html();
            var resources = parsed.html($translate.instant("RESOURCES")).html();
            var reviewers = parsed.html($translate.instant("REVIEWERS")).html();
            var items = parsed.html($translate.instant("ITEMS")).html();


            vm.tabs = [
                {label: team, value: "team", selected: true},
                {label: plan, value: "plan", selected: true},
                {label: filesTabHeading, value: "files", selected: true},
                {label: requirements, value: "requirements", selected: true},
                {label: deliverables, value: "deliverables", selected: true},
                {label: referenceItems, value: "referenceItems", selected: true},
                {label: workflow, value: "workflow", selected: true},
            ];

            vm.programTabs = [

                {label: resources, value: "resources", selected: true},
                {label: projects, value: "projects", selected: true},
                {label: filesTabHeading, value: "files", selected: true},
                {label: workflow, value: "workflow", selected: true},

            ];

            vm.requirementDocumentTabs = [

                {label: reviewers, value: "reviewers", selected: true},
                {label: requirements, value: "requirements", selected: true},
                {label: filesTabHeading, value: "files", selected: true},
                {label: workflow, value: "workflow", selected: true},

            ];

            vm.activityAndTaskTabs = [

                {label: filesTabHeading, value: "files", selected: true},
                {label: deliverables, value: "deliverables", selected: true},
                {label: referenceItems, value: "referenceItems", selected: true},
                {label: workflow, value: "workflow", selected: true},

            ];

            vm.requirementTabs = [

                {label: reviewers, value: "reviewers", selected: true},
                {label: items, value: "items", selected: true},
                {label: filesTabHeading, value: "files", selected: true},
                {label: workflow, value: "workflow", selected: true},

            ];

            vm.checkForMandatoryTab = checkForMandatoryTab;
            function checkForMandatoryTab(type, tab) {
                var valid = false;
                if (type == "PROJECT") {
                    if (tab.value == "team" || tab.value == "plan") {
                        valid = true;
                        tab.selected = true;
                    }
                } else if (type == "PROGRAM") {
                    if (tab.value == "projects") {
                        valid = true;
                        tab.selected = true;
                    }
                } else if (type == "REQUIREMENTDOCUMENTTYPE") {
                    if (tab.value == "requirements") {
                        valid = true;
                        tab.selected = true;
                    }
                } else if (type == "PROJECTACTIVITY" || type == "PROJECTTASK") {
                    if (tab.value == "files" && $rootScope.resourceType != null && $rootScope.resourceType != undefined && $rootScope.resourceType.typeObjectHasFiles) {
                        valid = true;
                        tab.selected = true;
                    }
                }

                return valid;
            }


            var projectTypeUpdateMessage = parsed.html($translate.instant("PROJECT_TYPE_UPDATE_MESSAGE")).html();
            var reqTypeUpdateMessage = parsed.html($translate.instant("REQ_TYPE_UPDATE_MESSAGE")).html();
            var reqDocTypeUpdateMessage = parsed.html($translate.instant("REQ_DOC_TYPE_UPDATE_MESSAGE")).html();
            var lifecycleHasNoValues = parsed.html($translate.instant("LIFECYCLE_HAS_NO_VALUES")).html();
            var typeUpdateMessage = parsed.html($translate.instant("TYPE_UPDATE_MESSAGE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            vm.onSave = onSave;
            function onSave() {
                if (validate()) {
                    if ($rootScope.selectedObjectType != null) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if ($rootScope.selectedObjectType.objectType == "PMOBJECTTYPE") {
                            $rootScope.resourceType.tabs = [];
                            if ($rootScope.resourceType.type == "PROJECT") {
                                angular.forEach(vm.tabs, function (tab) {
                                    if (tab.selected) {
                                        $rootScope.resourceType.tabs.push(tab.value);
                                    }
                                })
                            } else if ($rootScope.resourceType.type == "PROGRAM") {
                                angular.forEach(vm.programTabs, function (tab) {
                                    if (tab.selected) {
                                        $rootScope.resourceType.tabs.push(tab.value);
                                    }
                                })
                            } else if ($rootScope.resourceType.type == "PROJECTACTIVITY") {
                                angular.forEach(vm.activityAndTaskTabs, function (tab) {
                                    if (tab.selected) {
                                        $rootScope.resourceType.tabs.push(tab.value);
                                    }
                                })
                            } else if ($rootScope.resourceType.type == "PROJECTTASK") {
                                angular.forEach(vm.activityAndTaskTabs, function (tab) {
                                    if (tab.selected) {
                                        $rootScope.resourceType.tabs.push(tab.value);
                                    }
                                })
                            }


                            PMObjectTypeService.updatePmType($rootScope.resourceType).then(
                                function (data) {
                                    $rootScope.loadPMBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedObjectTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeUpdateMessage);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedObjectType.objectType == "REQUIREMENTTYPE") {
                            $rootScope.resourceType.tabs = [];
                            angular.forEach(vm.requirementTabs, function (tab) {
                                if (tab.selected) {
                                    $rootScope.resourceType.tabs.push(tab.value);
                                }
                            })

                            PMObjectTypeService.updateReqType($rootScope.resourceType).then(
                                function (data) {
                                    $rootScope.loadPMBasicInfo();
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeUpdateMessage);
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedObjectTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedObjectType.objectType == "REQUIREMENTDOCUMENTTYPE") {
                            $rootScope.resourceType.tabs = [];
                            angular.forEach(vm.requirementDocumentTabs, function (tab) {
                                if (tab.selected) {
                                    $rootScope.resourceType.tabs.push(tab.value);
                                }
                            })
                            PMObjectTypeService.updateReqDocType($rootScope.resourceType).then(
                                function (data) {
                                    $rootScope.loadPMBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedObjectTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeUpdateMessage);
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

            function loadDisplayTabs() {
                if ($rootScope.resourceType.type == "PROJECT") {
                    if ($rootScope.resourceType.tabs != null) {
                        angular.forEach(vm.tabs, function (tab) {
                            tab.selected = false;
                            var index = $rootScope.resourceType.tabs.indexOf(tab.value);
                            if (index != -1) {
                                tab.selected = true;
                            }
                        })
                    } else {
                        angular.forEach(vm.tabs, function (tab) {
                            tab.selected = true;
                        })
                    }

                } else if ($rootScope.resourceType.type == "PROGRAM") {

                    if ($rootScope.resourceType.tabs != null) {
                        angular.forEach(vm.programTabs, function (tab) {
                            tab.selected = false;
                            var index = $rootScope.resourceType.tabs.indexOf(tab.value);
                            if (index != -1) {
                                tab.selected = true;
                            }
                        })
                    } else {
                        angular.forEach(vm.programTabs, function (tab) {
                            tab.selected = true;
                        })
                    }

                } else if ($rootScope.resourceType.type == "PROJECTACTIVITY") {

                    if ($rootScope.resourceType.tabs != null) {
                        angular.forEach(vm.activityAndTaskTabs, function (tab) {
                            tab.selected = false;
                            var index = $rootScope.resourceType.tabs.indexOf(tab.value);
                            if (index != -1) {
                                tab.selected = true;
                            }
                        })
                    } else {
                        angular.forEach(vm.activityAndTaskTabs, function (tab) {
                            tab.selected = true;
                        })
                    }

                } else if ($rootScope.resourceType.type == "PROJECTTASK") {

                    if ($rootScope.resourceType.tabs != null) {
                        angular.forEach(vm.activityAndTaskTabs, function (tab) {
                            tab.selected = false;
                            var index = $rootScope.resourceType.tabs.indexOf(tab.value);
                            if (index != -1) {
                                tab.selected = true;
                            }
                        })
                    } else {
                        angular.forEach(vm.activityAndTaskTabs, function (tab) {
                            tab.selected = true;
                        })
                    }
                }
                else if ($rootScope.resourceType.objectType == "REQUIREMENTTYPE") {

                    if ($rootScope.resourceType.tabs != null) {
                        angular.forEach(vm.requirementTabs, function (tab) {
                            tab.selected = false;
                            var index = $rootScope.resourceType.tabs.indexOf(tab.value);
                            if (index != -1) {
                                tab.selected = true;
                            }
                        })
                    } else {
                        angular.forEach(vm.requirementTabs, function (tab) {
                            tab.selected = true;
                        })
                    }
                }

                else if ($rootScope.resourceType.objectType == "REQUIREMENTDOCUMENTTYPE") {

                    if ($rootScope.resourceType.tabs != null) {
                        angular.forEach(vm.requirementDocumentTabs, function (tab) {
                            tab.selected = false;
                            var index = $rootScope.resourceType.tabs.indexOf(tab.value);
                            if (index != -1) {
                                tab.selected = true;
                            }
                        })
                    } else {
                        angular.forEach(vm.requirementDocumentTabs, function (tab) {
                            tab.selected = true;
                        })
                    }
                }
            }

            vm.onSelectLifecycle = onSelectLifecycle;
            function onSelectLifecycle(lifecycle) {
                if (lifecycle.phases.length == 0) {
                    $rootScope.resourceType.lifecycle = null;
                    $rootScope.showWarningMessage(lifecycle.name + " : " + lifecycleHasNoValues);
                }
            }

            function validate() {
                var valid = true;
                if ($rootScope.resourceType.name == "" || $rootScope.resourceType.name == "" || $rootScope.resourceType.name == undefined) {
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                }

                return valid;
            }

            (function () {
                $scope.$on('app.pmType.tabActivated', function (event, data) {
                    $rootScope.loadPMBasicInfo();
                })
            })();
        }
    }
);