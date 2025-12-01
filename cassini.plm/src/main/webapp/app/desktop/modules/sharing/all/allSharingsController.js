define(['app/desktop/modules/sharing/sharing.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/shareService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/directives/all-view-icons/allViewIconsDirective'
    ],
    function (module) {
        module.controller('AllSharingsController', AllSharingsController);

        function AllSharingsController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                       $uibModal, ShareService, ItemService, PersonGroupService, CommonService, LoginService) {

            $rootScope.viewInfo.icon = "las la-share";
            $rootScope.viewInfo.title = $translate.instant("SHARING");
            $rootScope.viewInfo.showDetails = false;
            var parsed = angular.element("<div></div>");
            var vm = this;
            vm.loading = true;
            var map = new Hashtable();
            vm.showItem = showItem;
            vm.nextPage = nextPage;
            var person = $rootScope.person;
            vm.previousPage = previousPage;
            vm.loadSharedObjectsBySharedBy = loadSharedObjectsBySharedBy;
            vm.loadSharedObjectsBySharedTo = loadSharedObjectsBySharedTo;
            vm.clear = true;
            vm.searchMode = false;
            vm.searchModeExternal = false;
            vm.externalUser = {
                person: null
            };

            vm.sharedObjectFilter = {
                searchQuery: null,
                person: '',
                personIds: [],
                sharedObjectType: null
            };

            vm.internalUsers = parsed.html($translate.instant("SELECT_INTERNAL_USER_TITLE")).html();
            vm.externalUsers = parsed.html($translate.instant("SELECT_EXTERNAL_USER_TITLE")).html();
            vm.externalGroups = parsed.html($translate.instant("SELECT_EXTERNAL_GROUP_TITLE")).html();
            vm.selectItemNos = parsed.html($translate.instant("SELECT_ITEM_NO_PER_PAGE")).html();
            vm.configurableItem = parsed.html($translate.instant("CONFIGURABLE_ITEM")).html();
            vm.configuredItem = parsed.html($translate.instant("CONFIGURED_ITEM")).html();
            vm.projectHeading = parsed.html($translate.instant("PROJECT")).html();
            vm.activityHeading = parsed.html($translate.instant("ACTIVITY")).html();
            vm.taskHeading = parsed.html($translate.instant("TASK")).html();

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };


            vm.selectTab = selectTab;
            function selectTab(id) {
                vm.pageable.page = 0;
                if (id == "details.task") {
                    loadSharedProjectTasks();
                    vm.currentTab = "task";
                } else if (id == "details.activity") {
                    loadSharedProjectActivities();
                    vm.currentTab = "activity";
                } else {
                    loadSharedProject();
                    vm.currentTab = "project";
                }
            }

            vm.active = 0;
            vm.tabs = {
                project: {
                    id: 'details.project',
                    heading: vm.projectHeading,
                    template: 'app/desktop/modules/sharing/tabs/sharedProjectView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                activity: {
                    id: 'details.activity',
                    heading: vm.activityHeading,
                    template: 'app/desktop/modules/sharing/tabs/sharedProjectActivityView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                task: {
                    id: 'details.task',
                    heading: vm.taskHeading,
                    template: 'app/desktop/modules/sharing/tabs/sharedProjectTaskView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }
            };

            function nextPage() {
                if (vm.itemsView) {
                    if (vm.sharedObjects.last != true) {
                        vm.pageable.page++;
                        loadSharedToObjects();
                    }
                } else if (vm.mfrView) {
                    if (vm.sharedMfrs.last != true) {
                        vm.pageable.page++;
                        loadSharedMfrs();
                    }
                }else if (vm.programsView) {
                    if (vm.sharedPrograms.last != true) {
                        vm.pageable.page++;
                        loadSharedPrograms();
                    }
                } else if (vm.mfrPartView) {
                    if (vm.sharedMfrParts.last != true) {
                        vm.pageable.page++;
                        loadSharedMfrParts();
                    }
                }

                else if (vm.supplierView) {
                    if (vm.sharedSuppliers.last != true) {
                        vm.pageable.page++;
                        loadSharedSuppliers();
                    }
                }


                else if (vm.folderView) {
                    if (vm.sharedFolders.last != true) {
                        vm.pageable.page++;
                        loadSharedFolders();
                    }
                }

                else if (vm.declarationView) {
                    if (vm.sharedDeclarations.last != true) {
                        vm.pageable.page++;
                        loadSharedDeclarations();
                    }
                } else if (vm.projectView && vm.currentTab == "project") {
                    if (vm.sharedProjects.last != true) {
                        vm.pageable.page++;
                        loadSharedProject();
                    }
                } else if (vm.projectView && vm.currentTab == "activity") {
                    if (vm.sharedProjectActivities.last != true) {
                        vm.pageable.page++;
                        loadSharedProjectActivities();
                    }
                } else if (vm.projectView && vm.currentTab == "task") {
                    if (vm.sharedProjectTasks.last != true) {
                        vm.pageable.page++;
                        loadSharedProjectTasks();
                    }
                }
            }

            function previousPage() {
                if (vm.itemsView) {
                    if (vm.sharedObjects.first != true) {
                        vm.pageable.page--;
                        loadSharedToObjects();
                    }
                } else if (vm.mfrView) {
                    if (vm.sharedMfrs.first != true) {
                        vm.pageable.page--;
                        loadSharedMfrs();
                    }
                }else if (vm.programsView) {
                    if (vm.sharedPrograms.first != true) {
                        vm.pageable.page--;
                        loadSharedPrograms();
                    }
                } else if (vm.mfrPartView) {
                    if (vm.sharedMfrParts.first != true) {
                        vm.pageable.page--;
                        loadSharedMfrParts();
                    }
                }
                else if (vm.supplierView) {
                    if (vm.sharedSuppliers.first != true) {
                        vm.pageable.page--;
                        loadSharedSuppliers();
                    }
                }

                else if (vm.folderView) {
                    if (vm.sharedFolders.first != true) {
                        vm.pageable.page--;
                        loadSharedFolders();
                    }
                }

                else if (vm.declarationView) {
                    if (vm.sharedDeclarations.first != true) {
                        vm.pageable.page--;
                        loadSharedDeclarations();
                    }
                } else if (vm.projectView && vm.currentTab == "project") {
                    if (vm.sharedProjects.first != true) {
                        vm.pageable.page--;
                        loadSharedProject();
                    }
                } else if (vm.projectView && vm.currentTab == "activity") {
                    if (vm.sharedProjectActivities.first != true) {
                        vm.pageable.page--;
                        loadSharedProjectActivities();
                    }
                } else if (vm.projectView && vm.currentTab == "task") {
                    if (vm.sharedProjectTasks.first != true) {
                        vm.pageable.page--;
                        loadSharedProjectTasks();
                    }
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                if (vm.itemsView) {
                    loadSharedToObjects();
                } else if (vm.mfrView) {
                    loadSharedMfrs();
                } else if (vm.programsView) {
                    loadSharedPrograms();
                } else if (vm.mfrPartView) {
                    loadSharedMfrParts();
                }

                else if (vm.supplierView) {
                    loadSharedSuppliers();
                }

                else if (vm.folderView) {
                    loadSharedFolders();
                }
                else if (vm.declarationView) {
                    loadSharedDeclarations();
                } else if (vm.projectView && vm.currentTab == "project") {
                    loadSharedProject();
                } else if (vm.projectView && vm.currentTab == "activity") {
                    loadSharedProjectActivities();
                } else if (vm.projectView && vm.currentTab == "task") {
                    loadSharedProjectTasks();
                }
            }

            function showItem(sharedItem) {
                $state.go('app.items.details', {itemId: sharedItem.latestRevision});
            }

            vm.internalUser = null;
            function loadSharedObjectsBySharedBy() {
                vm.pageable.page = 0;
                if (vm.itemsView) {
                    loadSharedByObjects();
                } else if (vm.mfrView) {
                    loadSharedByMfrObjects();
                } else if (vm.mfrPartView) {
                    loadSharedByMfrPartObjects();
                }

                else if (vm.supplierView) {
                    loadSharedBySupplierObjects();
                }


                /*else if (vm.folderView) {
                 loadSharedByFolderObjects();
                 }*/

                else if (vm.declarationView) {
                    loadSharedByDeclarationObjects();
                } else if (vm.projectView) {
                    loadSharedByProjectObjects(vm.currentTab);
                }
            }

            function loadSharedByObjects() {
                if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getItemsBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedObjects = data;
                            // assignValues();
                            vm.searchModeExternal = true;
                            vm.searchMode = false;

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedObjects = data;
                            // assignValues();
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser == null && vm.externalUser != null) {
                    ShareService.getGroupsByPerson(vm.externalUser).then(
                        function (data) {
                            vm.ids.push(vm.externalUser);
                            if (data.length > 0) {
                                angular.forEach(data, function (groupMember) {
                                    vm.ids.push(groupMember);
                                })
                            }
                            ShareService.getItemsBySharedToPerson(vm.pageable, vm.ids).then(
                                function (data) {
                                    vm.sharedObjects = data;
                                    //assignValues();
                                    vm.searchMode = true;
                                    vm.searchModeExternal = false;

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    loadSharedObjects();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }

            function loadSharedByMfrObjects() {
                vm.mfrView = true;
                if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getMfrsBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedMfrs = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getMfrsInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedMfrs = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser == null && vm.externalUser != null) {
                    ShareService.getGroupsByPerson(vm.externalUser).then(
                        function (data) {
                            vm.ids.push(vm.externalUser);
                            if (data.length > 0) {
                                angular.forEach(data, function (groupMember) {
                                    vm.ids.push(groupMember);
                                })
                            }
                            ShareService.getMfrsBySharedToPerson(vm.pageable, vm.ids).then(
                                function (data) {
                                    vm.sharedMfrs = data;
                                    vm.searchMode = true;
                                    vm.searchModeExternal = false;

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    loadSharedMfrs();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }

            function loadSharedByMfrPartObjects() {
                vm.mfrPartView = true;
                if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getMfrPartsBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedMfrParts = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getMfrPartsInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedMfrParts = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser == null && vm.externalUser != null) {
                    ShareService.getGroupsByPerson(vm.externalUser).then(
                        function (data) {
                            vm.ids.push(vm.externalUser);
                            if (data.length > 0) {
                                angular.forEach(data, function (groupMember) {
                                    vm.ids.push(groupMember);
                                })
                            }
                            ShareService.getMfrPartsBySharedToPerson(vm.pageable, vm.ids).then(
                                function (data) {
                                    vm.sharedMfrParts = data;
                                    vm.searchMode = true;
                                    vm.searchModeExternal = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    loadSharedMfrParts();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }


            function loadSharedBySupplierObjects() {
                vm.supplierView = true;
                if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getSuppliersBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedSuppliers = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getSuppliersInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedSuppliers = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser == null && vm.externalUser != null) {
                    ShareService.getGroupsByPerson(vm.externalUser).then(
                        function (data) {
                            vm.ids.push(vm.externalUser);
                            if (data.length > 0) {
                                angular.forEach(data, function (groupMember) {
                                    vm.ids.push(groupMember);
                                })
                            }
                            ShareService.getSuppliersBySharedToPerson(vm.pageable, vm.ids).then(
                                function (data) {
                                    vm.sharedSuppliers = data;
                                    vm.searchMode = true;
                                    vm.searchModeExternal = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    loadSharedSuppliers();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }


            function loadSharedByDeclarationObjects() {
                vm.declarationView = true;
                if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getDeclarationsBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedDeclarations = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getDeclarationsInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedDeclarations = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser == null && vm.externalUser != null) {
                    ShareService.getGroupsByPerson(vm.externalUser).then(
                        function (data) {
                            vm.ids.push(vm.externalUser);
                            if (data.length > 0) {
                                angular.forEach(data, function (groupMember) {
                                    vm.ids.push(groupMember);
                                })
                            }
                            ShareService.getDeclarationsBySharedToPerson(vm.pageable, vm.ids).then(
                                function (data) {
                                    vm.sharedDeclarations = data;
                                    vm.searchMode = true;
                                    vm.searchModeExternal = false;
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    loadSharedDeclarations();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }

            function loadSharedByProjectObjects(type) {
                vm.projectView = true;
                var objectType = null;
                if (type == "project") objectType = "PROJECT";
                else if (type == "activity") objectType = "PROJECTACTIVITY";
                else if (type == "task") objectType = "PROJECTTASK";
                if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getProjectObjectsBySharedBy(vm.pageable, vm.internalUser, objectType).then(
                        function (data) {
                            if (type == "project") {
                                vm.sharedProjects = data;
                                parseProjectObjectDates(vm.sharedProjects, "PROJECT");
                            } else if (type == "activity") {
                                vm.sharedProjectActivities = data;
                                parseProjectObjectDates(vm.sharedProjectActivities, "PROJECTACTIVITY");
                            } else if (type == "task") {
                                vm.sharedProjectTasks = data;
                                parseProjectObjectDates(vm.sharedProjectTasks, "PROJECTTASK");
                            }
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getProjectObjectsInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable, objectType).then(
                        function (data) {
                            if (type == "project") {
                                vm.sharedProjects = data;
                                parseProjectObjectDates(vm.sharedProjects, "PROJECT");
                            } else if (type == "activity") {
                                vm.sharedProjectActivities = data;
                                parseProjectObjectDates(vm.sharedProjectActivities, "PROJECTACTIVITY");
                            } else if (type == "task") {
                                vm.sharedProjectTasks = data;
                                parseProjectObjectDates(vm.sharedProjectTasks, "PROJECTTASK");
                            }
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser == null && vm.externalUser != null) {
                    ShareService.getGroupsByPerson(vm.externalUser).then(
                        function (data) {
                            vm.ids.push(vm.externalUser);
                            if (data.length > 0) {
                                angular.forEach(data, function (groupMember) {
                                    vm.ids.push(groupMember);
                                })
                            }
                            ShareService.getProjectObjectsBySharedToPerson(vm.pageable, vm.ids, objectType).then(
                                function (data) {
                                    if (type == "project") {
                                        vm.sharedProjects = data;
                                        parseProjectObjectDates(vm.sharedProjects, "PROJECT");
                                    } else if (type == "activity") {
                                        vm.sharedProjectActivities = data;
                                        parseProjectObjectDates(vm.sharedProjectActivities, "PROJECTACTIVITY");
                                    } else if (type == "task") {
                                        vm.sharedProjectTasks = data;
                                        parseProjectObjectDates(vm.sharedProjectTasks, "PROJECTTASK");
                                    }
                                    vm.searchMode = true;
                                    vm.searchModeExternal = false;
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    if (type == "project") loadSharedProject();
                    else if (type == "activity") loadSharedProjectActivities();
                    else if (type == "task") loadSharedProjectTasks();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }

            vm.externalUser = null;
            vm.externalGroup = null;
            function loadSharedObjectsBySharedTo() {
                vm.pageable.page = 0;
                if (vm.itemsView) {
                    loadSharedToObjects();
                } else if (vm.mfrView) {
                    loadSharedToMfrObjects();
                } else if (vm.mfrPartView) {
                    loadSharedToMfrPartObjects();
                }
                else if (vm.supplierView) {
                    loadSharedToSupplierObjects();
                }
                else if (vm.declarationView) {
                    loadSharedToDeclarationObjects();
                } else if (vm.projectView) {
                    loadSharedToProjectObjects(vm.currentTab);
                }
            }

            function loadSharedToObjects() {
                vm.ids = [];
                if (vm.externalUser != null && vm.internalUser == null) {
                    ShareService.getItemsBySharedTo(vm.pageable, vm.externalUser).then(
                        function (data) {
                            vm.sharedObjects = data;
                            //assignValues();
                            vm.searchMode = true;
                            vm.searchModeExternal = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedObjects = data;
                            //assignValues();
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getItemsBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedObjects = data;
                            //assignValues();
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadSharedObjects();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }

            function loadSharedToMfrObjects() {
                vm.ids = [];
                vm.mfrView = true;
                if (vm.externalUser != null && vm.internalUser == null) {
                    ShareService.getMfrsBySharedPersonId(vm.pageable, vm.externalUser).then(
                        function (data) {
                            vm.sharedMfrs = data;
                            vm.searchMode = true;
                            vm.searchModeExternal = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getMfrsInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedMfrs = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getMfrsBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedMfrs = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadSharedMfrs();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }

            function loadSharedToMfrPartObjects() {
                vm.ids = [];
                vm.mfrPartView = true;
                if (vm.externalUser != null && vm.internalUser == null) {
                    ShareService.getMfrPartsBySharedTo(vm.pageable, vm.externalUser).then(
                        function (data) {
                            vm.sharedMfrParts = data;
                            vm.searchMode = true;
                            vm.searchModeExternal = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getMfrPartsInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedMfrParts = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getMfrPartsBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedMfrParts = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadSharedMfrParts();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }


            function loadSharedToSupplierObjects() {
                vm.ids = [];
                vm.supplierView = true;
                if (vm.externalUser != null && vm.internalUser == null) {
                    ShareService.getSuppliersBySharedTo(vm.pageable, vm.externalUser).then(
                        function (data) {
                            vm.sharedSuppliers = data;
                            vm.searchMode = true;
                            vm.searchModeExternal = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getSuppliersInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedSuppliers = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getSuppliersBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedSuppliers = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadSharedSuppliers();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }


            function loadSharedToDeclarationObjects() {
                vm.ids = [];
                vm.declarationView = true;
                if (vm.externalUser != null && vm.internalUser == null) {
                    ShareService.getDeclarationsBySharedTo(vm.pageable, vm.externalUser).then(
                        function (data) {
                            vm.sharedDeclarations = data;
                            vm.searchMode = true;
                            vm.searchModeExternal = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getDeclarationsInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable).then(
                        function (data) {
                            vm.sharedDeclarations = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getDeclarationsBySharedBy(vm.pageable, vm.internalUser).then(
                        function (data) {
                            vm.sharedDeclarations = data;
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadSharedDeclarations();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }

            function loadSharedToProjectObjects(type) {
                vm.ids = [];
                vm.projectView = true;
                var objectType = null;
                if (type == "project") objectType = "PROJECT";
                else if (type == "activity") objectType = "PROJECTACTIVITY";
                else if (type == "task") objectType = "PROJECTTASK";
                if (vm.externalUser != null && vm.internalUser == null) {
                    ShareService.getProjectObjectsBySharedTo(vm.pageable, vm.externalUser, objectType).then(
                        function (data) {
                            if (type == "project") {
                                vm.sharedProjects = data;
                                parseProjectObjectDates(vm.sharedProjects, "PROJECT");
                            } else if (type == "activity") {
                                vm.sharedProjectActivities = data;
                                parseProjectObjectDates(vm.sharedProjectActivities, "PROJECTACTIVITY");
                            } else if (type == "task") {
                                vm.sharedProjectTasks = data;
                                parseProjectObjectDates(vm.sharedProjectTasks, "PROJECTTASK");
                            }
                            vm.searchMode = true;
                            vm.searchModeExternal = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.internalUser != null && vm.externalUser != null) {
                    ShareService.getProjectObjectsInternalAndExternalObjects(vm.internalUser, vm.externalUser, vm.pageable, objectType).then(
                        function (data) {
                            if (type == "project") {
                                vm.sharedProjects = data;
                                parseProjectObjectDates(vm.sharedProjects, "PROJECT");
                            } else if (type == "activity") {
                                vm.sharedProjectActivities = data;
                                parseProjectObjectDates(vm.sharedProjectActivities, "PROJECTACTIVITY");
                            } else if (type == "task") {
                                vm.sharedProjectTasks = data;
                                parseProjectObjectDates(vm.sharedProjectTasks, "PROJECTTASK");
                            }
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (vm.internalUser != null && vm.externalUser == null) {
                    ShareService.getProjectObjectsBySharedBy(vm.pageable, vm.internalUser, objectType).then(
                        function (data) {
                            if (type == "project") {
                                vm.sharedProjects = data;
                                parseProjectObjectDates(vm.sharedProjects, "PROJECT");
                            } else if (type == "activity") {
                                vm.sharedProjectActivities = data;
                                parseProjectObjectDates(vm.sharedProjectActivities, "PROJECTACTIVITY");
                            } else if (type == "task") {
                                vm.sharedProjectTasks = data;
                                parseProjectObjectDates(vm.sharedProjectTasks, "PROJECTTASK");
                            }
                            vm.searchModeExternal = true;
                            vm.searchMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    if (type == "project") loadSharedProject();
                    else if (type == "activity") loadSharedProjectActivities();
                    else if (type == "task") loadSharedProjectTasks();
                    vm.searchMode = false;
                    vm.searchModeExternal = false;
                }
            }

            vm.users = [];
            vm.external = [];
            function loadUsers() {
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (user) {
                            if (user.isActive == true && user.external == false) {
                                vm.users.push(user);
                            } else if (user.isActive == true && user.external == true) {
                                user.personId = user.person.id;
                                user.personName = user.person.firstName;
                                vm.external.push(user);
                            }
                        });
                        loadSharedMfrs();
                        loadSharedMfrParts();
                        loadSharedDeclarations();
                        loadSharedSuppliers();
                        loadSharedFolders();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadGroups() {
                PersonGroupService.getAllPersonGroups().then(
                    function (data) {
                        angular.forEach(data, function (val) {
                            if (val.external) {
                                val.personId = val.groupId;
                                val.personName = val.name;
                                vm.external.push(val);
                            }
                        });
                        loadSharedCounts();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /* function assignValues() {
             vm.sharedItems = [];
             CommonService.getPersonReferences(vm.sharedObjects.content, 'sharedTo');
             CommonService.getPersonReferences(vm.sharedObjects.content, 'sharedBy');
             PersonGroupService.getPersonGroupReferences(vm.sharedObjects.content, 'sharedTo');

             ItemService.getItemReferences(vm.sharedObjects.content, 'objectId', "shared");

             angular.forEach(vm.sharedObjects.content, function (sharedItem) {
             ItemService.getItem(sharedItem.objectId).then(
             function (data) {
             vm.item = data;
             if (vm.item.thumbnail != null) {
             sharedItem.thumbnailImage = "api/plm/items/" + sharedItem.objectId + "/itemImageAttribute/download?" + new Date().getTime();
             }
             }, function (error) {
             $rootScope.showErrorMessage(error.message);
             $rootScope.hideBusyIndicator();
             })

             });
             vm.loading = false;
             }*/

            vm.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('myModal234');
                var modalImg = document.getElementById('img134');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }


            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('item-thumbnail' + item.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + item.id);
                $("#thumbnail-image" + item.id).width($('#thumbnail-view' + item.id).outerWidth());
                $("#thumbnail-image" + item.id).height($('#thumbnail-view' + item.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                };
                $scope.$evalAsync();
            }

            function loadSharedObjects() {
                $rootScope.showBusyIndicator();
                vm.itemsView = true;
                ShareService.getAllSharedObjects(vm.pageable).then(
                    function (data) {
                        vm.sharedObjects = data;
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                        // assignValues();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSharedMfrs() {
                ShareService.getAllSharedMfrObjects(vm.pageable).then(
                    function (data) {
                        vm.sharedMfrs = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSharedPrograms() {
                ShareService.getAllSharedProgramObjects(vm.pageable).then(
                    function (data) {
                        vm.sharedPrograms = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            
            function loadSharedMfrParts() {
                vm.sharedObjectFilter.sharedObjectType = "MANUFACTURERPART";
                vm.sharedObjectFilter.person = person;
                ShareService.getMfrPartsBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                    function (data) {
                        vm.sharedMfrParts = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function loadSharedSuppliers() {
                ShareService.getAllSharedSupplierObjects(vm.pageable).then(
                    function (data) {
                        vm.sharedSuppliers = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function loadSharedFolders() {
                ShareService.getAllSharedFolderObjects(vm.pageable).then(
                    function (data) {
                        vm.sharedFolders = data;
                        angular.forEach(vm.sharedFolders.content, function (obj) {
                            obj.level = 0;
                            obj.expanded = false;
                            obj.folderChildren = [];
                            obj.parentFolder = null;
                        });
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSharedDeclarations() {
                ShareService.getAllSharedDeclarationObjects(vm.pageable).then(
                    function (data) {
                        vm.sharedDeclarations = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showMfrDetails = showMfrDetails;
            function showMfrDetails(mfr) {
                $state.go('app.mfr.details', {manufacturerId: mfr.id});
            }

            vm.showSupplierDetails = showSupplierDetails;
            function showSupplierDetails(supplier) {
                $state.go('app.mfr.supplier.details', {supplierId: supplier.id});
            }

            vm.showProgramDetails = showProgramDetails;
            function showProgramDetails(program) {
                $state.go('app.pm.program.details', {programId: program.id});
            }


            vm.showMfrPartDetails = showMfrPartDetails;
            function showMfrPartDetails(part) {
                $state.go('app.mfr.mfrparts.details', {mfrId: part.mfrId, manufacturePartId: part.id});
            }

            vm.showDeclaration = showDeclaration;
            function showDeclaration(declaration) {
                $state.go('app.compliance.declaration.details', {declarationId: declaration.id, tab: 'details.basic'});
            }

            vm.sharedAllItems = sharedAllItems;
            function sharedAllItems() {
                vm.pageable.page = 0;
                vm.itemsView = true;
                vm.mfrView = false;
                vm.programsView = false;
                vm.mfrPartView = false;
                vm.supplierView = false;
                vm.declarationView = false;
                vm.projectView = false;
                vm.folderView = true;
                loadSharedObjects();
            }

            vm.sharedMfr = sharedMfr;
            function sharedMfr() {
                vm.pageable.page = 0;
                vm.itemsView = false;
                vm.mfrView = true;
                vm.programsView = false;
                vm.mfrPartView = false;
                vm.declarationView = false;
                vm.projectView = false;
                vm.supplierView = false;
                vm.folderView = true;
                loadSharedMfrs();
            }

            vm.sharedProgramObjects = sharedProgramObjects;
            function sharedProgramObjects() {
                vm.pageable.page = 0;
                vm.itemsView = false;
                vm.mfrView = false;
                vm.programsView = true;
                vm.mfrPartView = false;
                vm.declarationView = false;
                vm.projectView = false;
                vm.supplierView = false;
                vm.folderView = false;
                loadSharedPrograms();
            }

            vm.sharedMfrPart = sharedMfrPart;
            function sharedMfrPart() {
                vm.pageable.page = 0;
                vm.itemsView = false;
                vm.mfrView = false;
                vm.programsView = false;
                vm.mfrPartView = true;
                vm.supplierView = false;
                vm.declarationView = false;
                vm.projectView = false;
                vm.folderView = false;
                loadSharedMfrParts();
            }

            vm.sharedSupplier = sharedSupplier;
            function sharedSupplier() {
                vm.pageable.page = 0;
                vm.itemsView = false;
                vm.mfrView = false;
                vm.programsView = false;
                vm.mfrPartView = false;
                vm.supplierView = true;
                vm.declarationView = false;
                vm.projectView = false;
                vm.folderView = false;
                loadSharedSuppliers();
            }


            vm.sharedFolder = sharedFolder;
            function sharedFolder() {
                vm.pageable.page = 0;
                vm.itemsView = false;
                vm.mfrView = false;
                vm.programsView = false;
                vm.mfrPartView = false;
                vm.supplierView = false;
                vm.folderView = true;
                vm.declarationView = false;
                vm.projectView = false;
                loadSharedFolders();
            }

            vm.sharedDeclaration = sharedDeclaration;
            function sharedDeclaration() {
                vm.pageable.page = 0;
                vm.itemsView = false;
                vm.mfrView = false;
                vm.programsView = false;
                vm.mfrPartView = false;
                vm.declarationView = true;
                vm.projectView = false;
                vm.supplierView = false;
                vm.folderView = true;
                loadSharedDeclarations();
            }

            vm.sharedProjectsObjects = sharedProjectsObjects;
            function sharedProjectsObjects() {
                vm.pageable.page = 0;
                vm.itemsView = false;
                vm.mfrView = false;
                vm.programsView = false;
                vm.mfrPartView = false;
                vm.declarationView = false;
                vm.projectView = true;
                vm.supplierView = false;
                vm.folderView = false;
                if (vm.currentTab == "project") loadSharedProject();
                else if (vm.currentTab == "activity") loadSharedProjectActivities();
                else if (vm.currentTab == "task") loadSharedProjectTasks();
            }


            function loadSharedProject() {
                ShareService.getAllSharedProjectObjects(vm.pageable, "PROJECT").then(
                    function (data) {
                        vm.loading = false;
                        vm.sharedProjects = data;
                        parseProjectObjectDates(vm.sharedProjects, "PROJECT");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSharedProjectActivities() {
                ShareService.getAllSharedProjectObjects(vm.pageable, "PROJECTACTIVITY").then(
                    function (data) {
                        vm.loading = false;
                        vm.sharedProjectActivities = data;
                        parseProjectObjectDates(vm.sharedProjectActivities, "PROJECTACTIVITY");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSharedProjectTasks() {
                ShareService.getAllSharedProjectObjects(vm.pageable, "PROJECTTASK").then(
                    function (data) {
                        vm.loading = false;
                        vm.sharedProjectTasks = data;
                        parseProjectObjectDates(vm.sharedProjectTasks, "PROJECTTASK");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function parseProjectObjectDates(objects, type) {
                if (type == "PROJECT") {
                    angular.forEach(objects.content, function (task) {
                        if (task.percentComplete > 0 && task.percentComplete < 100) {
                            task.percentComplete = parseInt(task.percentComplete);
                        }
                    });
                    CommonService.getPersonReferences(objects.content, 'projectManager');
                    vm.sharedProjects.content = objects.content;
                } else if (type == "PROJECTACTIVITY") {
                    angular.forEach(objects.content, function (task) {
                        if (task.percentComplete > 0 && task.percentComplete < 100) {
                            task.percentComplete = parseInt(task.percentComplete);
                        }
                    });
                    CommonService.getPersonReferences(objects.content, 'assignedTo');
                    vm.sharedProjectActivities.content = objects.content;
                } else if (type == "PROJECTTASK") {
                    angular.forEach(objects.content, function (task) {
                        if (task.percentComplete > 0 && task.percentComplete < 100) {
                            task.percentComplete = parseInt(task.percentComplete);
                        }
                    });
                    angular.forEach(objects.content, function (task) {
                        task.editMode = false;
                    });
                    CommonService.getPersonReferences(objects.content, 'assignedTo');
                    vm.sharedProjectTasks.content = objects.content;
                }
            }

            vm.sharedCounts = {};
            vm.loadSharedCounts = loadSharedCounts;
            function loadSharedCounts() {
                ShareService.getPersonSharedCounts("all").then(
                    function (data) {
                        vm.sharedCounts = data;
                        var sharedProject = document.getElementById("sharedProject1");
                        var sharedActivity = document.getElementById("sharedActivity1");
                        var sharedTask = document.getElementById("sharedTask1");
                        sharedProject.lastElementChild.innerHTML = vm.tabs.project.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.sharedCounts.project);
                        sharedActivity.lastElementChild.innerHTML = vm.tabs.activity.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.sharedCounts.activity);
                        sharedTask.lastElementChild.innerHTML = vm.tabs.task.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.sharedCounts.task);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showTaskDetails = showTaskDetails;
            function showTaskDetails(task) {
                $rootScope.projectId = task.project;
                $state.go('app.pm.project.activity.task.details', {
                    activityId: task.activity,
                    taskId: task.id,
                    permission: task.permission
                })
            }

            vm.showProject = showProject;
            function showProject(project) {
                $state.go('app.pm.project.details', {
                    projectId: project.id,
                    permission: project.permission
                });
            }

            vm.openActivityDetails = openActivityDetails;
            function openActivityDetails(activity) {
                $state.go('app.pm.project.activity.details', {activityId: activity.id, permission: activity.permission})
            }


            $scope.toggleNode = toggleNode;
            function toggleNode(folder) {
                //$rootScope.showBusyIndicator($('.view-content'));
                if (folder.expanded == null || folder.expanded == undefined) {
                    folder.expanded = false;
                }
                folder.expanded = !folder.expanded;
                var index = vm.sharedFolders.content.indexOf(folder);
                if (folder.expanded == false) {
                    removeChildren(folder);
                }
                else {
                    ShareService.getFolderChildren(folder.fileDto.id).then(
                        function (data) {
                            var childrenData = data;
                            folder.count = childrenData.length;
                            angular.forEach(childrenData, function (item) {
                                item.editMode = false;
                                item.expanded = false;
                                item.level = folder.level + 1;
                                item.parentFolder = folder;
                                item.folderChildren = [];
                                if (item.createdDate) {
                                    item.createdDatede = moment(item.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                if (item.modifiedDate) {
                                    item.modifiedDatede = moment(item.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                folder.folderChildren.push(item);
                            });

                            angular.forEach(folder.folderChildren, function (item) {
                                index = index + 1;
                                if (item.createdDate) {
                                    item.createdDatede = moment(item.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                if (item.modifiedDate) {
                                    item.modifiedDatede = moment(item.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                vm.sharedFolders.content.splice(index, 0, item);
                            });
                            $timeout(function () {
                                calculateColumnWidthForSticky();
                            }, 200);
                            $rootScope.hideBusyIndicator();

                            $scope.$evalAsync();

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }
        
            function calculateColumnWidthForSticky() {
                var selectInputWidth = $('.select-input').outerWidth();
                var fileNameWidth = $('.file-name').outerWidth();

                $('.select-input').css("left", -4 + "px");
                $('.select-input').width(selectInputWidth);

                $('.file-name').css("left", ((selectInputWidth) - 4) + "px");
                $('.file-name').width(selectInputWidth + fileNameWidth);
            }

            function removeChildren(folder) {
                if (folder != null && folder.folderChildren != null && folder.folderChildren != undefined) {
                    angular.forEach(folder.folderChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.sharedFolders.content.indexOf(folder);
                    vm.sharedFolders.content.splice(index + 1, folder.folderChildren.length);
                    folder.folderChildren = [];
                    folder.expanded = false;
                }

                $scope.$evalAsync();
                $rootScope.hideBusyIndicator();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadSharedObjects();
                loadUsers();
                loadGroups();
                //}
            })();
        }

    }
)
;