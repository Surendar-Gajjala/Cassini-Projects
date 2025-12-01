define(
    [
        'app/desktop/modules/home/home.module',
        'split-pane',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/shareService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/userTasksService',
        'app/desktop/directives/all-view-icons/allViewIconsDirective',
        'app/shared/services/core/objectFileService',

    ],
    function (module) {
        module.controller('SharedController', SharedController);

        function SharedController($scope, $rootScope, $sce, $translate, $cookieStore, $window, $timeout, $application, $state, $stateParams, $cookies, $uibModal,
                                        CommonService, PersonGroupService, ItemTypeService, UserTasksService, ItemService, DialogService, ShareService, ProjectService, ActivityService, ObjectFileService) {

            var vm = this;
            vm.itemsearch = [];
            vm.advancedsearch = [];
            vm.selectedAttributes = [];
            vm.selectedObjectAttributes = [];
            vm.objectIds = [];
            var owner = null;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.viewInfo.showDetails = false;
            $rootScope.loginPersonDetails = session.login;
            var login = $rootScope.loginPersonDetails;
            vm.activityPercentage = 0;
            $rootScope.activityPercent = 0;
            vm.loading = true;
            vm.clear = false;
            vm.mode = null;

            var parsed = angular.element("<div></div>");
            var taskUpdateMsg = parsed.html($translate.instant("TASK_UPDATE_MSG")).html();
            var enterPositiveNumber = parsed.html($translate.instant("ENTER_POSITIVE_NUMBER")).html();
            var enterValidPercent = parsed.html($translate.instant("ENTER_VALID_PERCENT")).html();
            var taskDialogTitle = parsed.html($translate.instant("TASK_DIALOG_TITLE")).html();
            var taskDialogMessage = parsed.html($translate.instant("TASK_DIALOG_MESSAGE")).html();
            var taskDeletedMessage = parsed.html($translate.instant("TASK_DELETED_MESSAGE")).html();
            vm.deleteTitle = parsed.html($translate.instant("DELETE")).html();
            var taskDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var updateActivity = parsed.html($translate.instant("UPDATE")).html();
            var activityUpdatedMessage = parsed.html($translate.instant("ACTIVITY_UPDATED_MESSAGE")).html();
            var editActivityTitle = parsed.html($translate.instant("EDIT_ACTIVITY")).html();
            vm.configurableItem = parsed.html($translate.instant("CONFIGURABLE_ITEM")).html();
            vm.configuredItem = parsed.html($translate.instant("CONFIGURED_ITEM")).html();
            vm.shareItem = parsed.html($translate.instant("SHARE_MESSAGE")).html();
            vm.showFileTitle = parsed.html($translate.instant("SHOW_FILE_TITLE")).html();
            vm.showBomTitle = parsed.html($translate.instant("SHOW_BOM_TITLE")).html();
            vm.showPartTitle = parsed.html($translate.instant("SHOW_PART_TITLE")).html();
            vm.showChangesTitle = parsed.html($translate.instant("SHOW_CHANGES_TITLE")).html();
            vm.showVarianceTitle = parsed.html($translate.instant("SHOW_VARIANCE_TITLE")).html();
            vm.showQualityTitle = parsed.html($translate.instant("SHOW_QUALITY_TITLE")).html();
            vm.projectHeading = parsed.html($translate.instant("PROJECT")).html();
            vm.activityHeading = parsed.html($translate.instant("ACTIVITY")).html();
            vm.taskHeading = parsed.html($translate.instant("TASK")).html();
            $scope.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();


            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'ITEM',
                owner: $rootScope.localStorageLogin.login.person.id
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.items = angular.copy(pagedResults);
            vm.sharedObjects = angular.copy(pagedResults);
            vm.showItem = showItem;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.pageSize = pageSize;
            vm.flag = false;

            function resetPage() {
                vm.pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
                vm.sharedObjectFilter.searchQuery = null;
                loadSharedItems();
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                if (vm.sharedItemView == true) {
                    loadSharedItems();
                } else if (vm.mfrView == true) {
                    loadSharedMfrs();
                } else if (vm.mfrPartView == true) {
                    loadSharedMfrParts();
                } else if (vm.programView == true) {
                    loadSharedPrograms();
                }

                else if (vm.supplierView == true) {
                    loadSharedSuppliers();
                }
                else if (vm.customObjectView == true) {
                    loadSharedCustomObjects();
                }

                else if (vm.folderView == true) {
                    loadSharedFolders();
                }
                else if (vm.declarationView == true) {
                    loadDeclarations();
                } else if (vm.sharedProjectView == true) {
                    if (vm.currentTab == "project") {
                        loadSharedProject();
                    } else if (vm.currentTab == "activity") {
                        loadSharedProjectActivities();
                    } else if (vm.currentTab == "task") {
                        loadSharedProjectTasks();
                    }
                }
            }

            function nextPage() {
                if (vm.sharedItemView == true) {
                    if (vm.sharedObjects.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.mfrView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedItems();
                    }
                } else if (vm.mfrView == true) {
                    if (vm.sharedMfrs.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedMfrs();
                    }
                } else if (vm.mfrPartView == true) {
                    if (vm.sharedMfrParts.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedMfrParts();
                    }
                } else if (vm.programView == true) {
                    if (vm.sharedPrograms.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedPrograms();
                    }
                }
                else if (vm.supplierView == true) {
                    if (vm.sharedSuppliers.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.declarationView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedSuppliers();
                    }
                }

                else if (vm.customObjectView == true) {
                    if (vm.sharedCustomObjects.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.declarationView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedSuppliers();
                    }
                }

                else if (vm.folderView == true) {
                    if (vm.sharedFolders.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.folderView = true;
                        vm.supplierView = false;
                        vm.declarationView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.sharedFoldersView = false;
                        loadSharedSuppliers();
                    }
                }

                else if (vm.declarationView == true) {
                    if (vm.sharedDeclarations.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.mfrView = false;
                        vm.folderView = false;
                        vm.supplierView = false;
                        vm.sharedFoldersView = false;
                        loadDeclarations();
                    }
                } else if (vm.sharedProjectView == true) {
                    if (vm.currentTab == "project" && vm.sharedProjects.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedProject();
                    } else if (vm.currentTab == "activity" && vm.sharedProjectActivities.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.declarationView = false;
                        vm.folderView = false;
                        vm.supplierView = false;
                        vm.sharedFoldersView = false;
                        loadSharedProjectActivities();
                    } else if (vm.currentTab == "task" && vm.sharedProjectTasks.last != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page++;
                        vm.sharedItemView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.declarationView = false;
                        vm.folderView = false;
                        vm.supplierView = false;
                        vm.sharedFoldersView = false;
                        loadSharedProjectTasks();
                    }
                }

            }

            function previousPage() {
                if (vm.sharedItemView == true) {
                    if (vm.sharedObjects.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.mfrView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedItems();
                    }
                } else if (vm.mfrView == true) {
                    if (vm.sharedMfrs.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedMfrs();
                    }
                } else if (vm.mfrPartView == true) {
                    if (vm.sharedMfrParts.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedMfrParts();
                    }
                } else if (vm.programView == true) {
                    if (vm.sharedPrograms.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        vm.sharedFoldersView = false;
                        vm.folderView = false;
                        loadSharedPrograms();
                    }
                }
                else if (vm.supplierView == true) {
                    if (vm.sharedSuppliers.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.sharedFoldersView = false;
                        vm.declarationView = false;
                        vm.folderView = false;
                        loadSharedSuppliers();
                    }
                }

                else if (vm.customObjectView == true) {
                    if (vm.sharedCustomObjects.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.declarationView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadSharedCustomObjects();
                    }
                }

                else if (vm.folderView == true) {
                    if (vm.sharedFolders.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.mfrView = false;
                        vm.folderView = true;
                        vm.sharedFoldersView = false;
                        vm.declarationView = false;
                        loadSharedMfrParts();
                    }
                }


                else if (vm.declarationView == true) {
                    if (vm.sharedDeclarations.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.mfrView = false;
                        vm.supplierView = false;
                        vm.folderView = false;
                        vm.sharedFoldersView = false;
                        loadDeclarations();
                    }
                } else if (vm.sharedProjectView == true) {
                    if (vm.currentTab == "project" && vm.sharedProjects.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.folderView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.declarationView = false;
                        vm.sharedFoldersView = false;
                        vm.supplierView = false;
                        loadSharedProject();
                    } else if (vm.currentTab == "activity" && vm.sharedProjectActivities.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.sharedFoldersView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.folderView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        loadSharedProjectActivities();
                    } else if (vm.currentTab == "task" && vm.sharedProjectTasks.first != true) {
                        $rootScope.showBusyIndicator();
                        vm.pageable.page--;
                        vm.sharedItemView = false;
                        vm.sharedFoldersView = false;
                        vm.mfrPartView = false;
                        vm.programView = false;
                        vm.folderView = false;
                        vm.declarationView = false;
                        vm.supplierView = false;
                        loadSharedProjectTasks();
                    }
                }
            }

            function clearFilter() {
                loadSharedItems();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

            vm.noDataImage = true;
            $rootScope.loadingObject = loadingObject;
            function loadingObject(){
                vm.sharedObjects = null;
                vm.sharedMfrs = null;
                vm.sharedProjects = null;
                vm.sharedMfrParts = null;
                vm.sharedPrograms = null;
                vm.sharedSuppliers = null;
                vm.sharedCustomObjects= null;
                vm.sharedFolders = null;
                vm.sharedDeclarations = null;
                vm.noDataImage = true;

            }


            function showItem(item) {
                $rootScope.externalUserShow = null;
                $state.go('app.items.details', {
                    itemId: item.latestRevision,
                    permission: item.permission
                });
                $rootScope.externalUserShow = "Item";
            }

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

            vm.sharedItemIds = [];
            vm.personIds = [];
            vm.sharedObjectFilter = {
                searchQuery: null,
                person: '',
                personIds: [],
                sharedObjectType: null
            };
            $rootScope.loadSharedItems = loadSharedItems;
            function loadSharedItems() {
                vm.sharedObjects = [];
                vm.loading = true;
                    vm.sharedItemView = true;
                    vm.itemTitle = 'Items';
                    ShareService.getGroupsByPerson(owner).then(
                        function (data) {
                            vm.personIds.push(owner);
                            if (data.length > 0) {
                                angular.forEach(data, function (groupMember) {
                                    vm.personIds.push(groupMember);
                                })
                            }
                            vm.sharedObjectFilter.sharedObjectType = "ITEM";
                            vm.sharedObjectFilter.personIds = vm.personIds;
                            vm.sharedObjectFilter.person = owner
                            ShareService.getItemsBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                                function (data) {
                                    vm.sharedObjects = data;
                                    vm.loading = false;
                                    vm.noDataImage = false;
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                

            }
            $rootScope.loadSharedObjects = loadSharedObjects;
            function loadSharedObjects() {
                $rootScope.showBusyIndicator();
                vm.itemsView = true;
                ShareService.getAllSharedObjects(vm.pageable).then(
                    function (data) {
                        vm.sharedObjects = data;
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                        vm.noDataImage = false;
                      
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            

            $rootScope.sharedItem = sharedItem;
            function sharedItem() {
                $rootScope.externalUserShow = "Item";
                vm.pageable.page = 0;
                vm.sharedItemView = true;
                vm.sharedProjectView = false;
                vm.sharedProjectActivityView = false;
                vm.sharedProjectTaskView = false;
                vm.activityView = false;
                vm.taskView = false;
                vm.mfrPartView = false;
                vm.programView = false;
                vm.mfrView = false;
                vm.folderView = false;
                vm.declarationView = false;
                vm.supplierView = false;
                vm.itemTitle = 'Items';
                vm.loading = false;
                vm.sharedFoldersView = false;
                vm.customObjectView = false;
                loadSharedItems();
            }

            $rootScope.sharedMfr = sharedMfr;

            function sharedMfr() {
                $rootScope.externalUserShow = "Mfr";
                vm.pageable.page = 0;
                vm.sharedProjectView = false;
                vm.sharedItemView = false;
                vm.sharedProjectActivityView = false;
                vm.sharedProjectTaskView = false;
                vm.mfrView = true;
                vm.supplierView = false;
                vm.sharedItemView = false;
                vm.mfrPartView = false;
                vm.programView = false;
                vm.activityView = false;
                vm.taskView = false;
                vm.folderView = false;
                vm.declarationView = false;
                vm.itemTitle = 'Manufacturers';
                vm.loading = false;
                vm.sharedFoldersView = false;
                vm.customObjectView = false;
                loadSharedMfrs();
            }

            $rootScope.sharedMfrPart = sharedMfrPart;

            function sharedMfrPart() {
                $rootScope.externalUserShow = "MfrPart";
                vm.pageable.page = 0;
                vm.sharedItemView = false;
                vm.sharedProjectView = false;
                vm.mfrView = false;
                vm.sharedItemView = false;
                vm.mfrPartView = true;
                vm.programView = false;
                vm.activityView = false;
                vm.taskView = false;
                vm.declarationView = false;
                vm.supplierView = false;
                vm.folderView = false;
                vm.itemTitle = 'Manufacturer Parts';
                vm.loading = false;
                vm.sharedFoldersView = false;
                vm.customObjectView = false;
                loadSharedMfrParts();
            }

            $rootScope.sharedProgram = sharedProgram;

            function sharedProgram() {
                $rootScope.externalUserShow = "program";
                vm.pageable.page = 0;
                vm.sharedItemView = false;
                vm.sharedProjectView = false;
                vm.mfrView = false;
                vm.sharedItemView = false;
                vm.mfrPartView = false;
                vm.programView = true;
                vm.activityView = false;
                vm.taskView = false;
                vm.declarationView = false;
                vm.supplierView = false;
                vm.folderView = false;
                vm.itemTitle = 'Programs';
                vm.sharedFoldersView = false;
                vm.loading = false;
                vm.customObjectView = false;
                loadSharedPrograms();
            }

            $rootScope.sharedSupplier = sharedSupplier;
            function sharedSupplier() {
                $rootScope.externalUserShow = "Supplier";
                vm.pageable.page = 0;
                vm.sharedProjectView = false;
                vm.mfrView = false;
                vm.sharedItemView = false;
                vm.supplierView = true;
                vm.sharedItemView = false;
                vm.mfrPartView = false;
                vm.programView = false;
                vm.activityView = false;
                vm.taskView = false;
                vm.folderView = false;
                vm.declarationView = false;
                vm.itemTitle = 'Suppliers';
                vm.sharedFoldersView = false;
                vm.loading = false;
                vm.customObjectView = false;
                loadSharedSuppliers();
            }

            $rootScope.sharedCustomObject = sharedCustomObject;
            function sharedCustomObject() {
                $rootScope.externalUserShow = "CustomObject";
                vm.pageable.page = 0;
                vm.sharedProjectView = false;
                vm.mfrView = false;
                vm.sharedItemView = false;
                vm.supplierView = false;
                vm.sharedItemView = false;
                vm.mfrPartView = false;
                vm.programView = false;
                vm.customObjectView = true;
                vm.activityView = false;
                vm.taskView = false;
                vm.folderView = false;
                vm.declarationView = false;
                vm.sharedFoldersView = false;
                vm.itemTitle = 'Custom Objects';
                vm.loading = false;
                loadSharedCustomObjects();
            }


            $rootScope.sharedFolder = sharedFolder;

            function sharedFolder() {
                $rootScope.externalUserShow = "Folder";
                vm.pageable.page = 0;
                vm.sharedProjectView = false;
                vm.mfrView = false;
                vm.supplierView = false;
                vm.sharedItemView = false;
                vm.folderView = true;
                vm.sharedItemView = false;
                vm.mfrPartView = false;
                vm.programView = false;
                vm.customObjectView = false;
                vm.activityView = false;
                vm.taskView = false;
                vm.declarationView = false;
                vm.sharedFoldersView = false;
                vm.itemTitle = 'Folders';
                vm.loading = false;
                loadSharedFolders();
            }


            $rootScope.sharedExternalUsersFoldersAndFiles = sharedExternalUsersFoldersAndFiles;

            function sharedExternalUsersFoldersAndFiles() {
                $rootScope.externalUserShow = "Folders";
                vm.pageable.page = 0;
                vm.sharedProjectView = false;
                vm.mfrView = false;
                vm.supplierView = false;
                vm.sharedItemView = false;
                vm.folderView = false;
                vm.sharedItemView = false;
                vm.mfrPartView = false;
                vm.programView = false;
                vm.customObjectView = false;
                vm.activityView = false;
                vm.taskView = false;
                vm.declarationView = false;
                vm.itemTitle = 'Folders';
                vm.loading = false;
                vm.sharedFoldersView = true;
                nodeId = 0;
                sharedFolderTree = null;
                rootNode = null;
                documentFolderTree = null;
                documentNodeId = 0;
                documetRootNode = null;
                $scope.selectedType = "FILE";
                vm.previousSelectedNode = null;
                initFoldersTree();

            }

            var nodeId = 0;
            var rootNode = null;
            var sharedFolderTree = null;

            var documentFolderTree = null;
            var documentNodeId = 0;
            var documetRootNode = null;

            function initFoldersTree() {

                documentFolderTree = $('#documentFolderTree').tree({
                    data: [
                        {
                            id: documentNodeId,
                            text: "Document Folders",
                            iconCls: 'folders-root',
                            attributes: {
                                type: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectDocumentFolder
                });
                documetRootNode = documentFolderTree.tree('find', 0);

                sharedFolderTree = $('#sharedFolderTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "Shared Folders",
                            iconCls: 'folders-root',
                            attributes: {
                                type: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectFolder
                });

                rootNode = sharedFolderTree.tree('find', 0);

                loadFolders();
            }

            function loadFolders() {
                $rootScope.showBusyIndicator($('.view-container'));
                ShareService.getExternalSharedFoldersAndFiles(owner).then(
                    function (data) {
                        vm.sharedExternalUserFolders = data;
                        var nodes = [];
                        var documentNodes = [];
                        angular.forEach(data, function (folder) {
                            if (folder.objectType == "FILE") {
                                folder.name = folder.parentObjectName + " - " + folder.name;
                                var node = makeNode(folder);
                                if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                    node = visitChildren(node, folder.children);
                                    node.state = "closed";
                                }
                                nodes.push(node);
                            }
                            else {
                                var node = makeNode(folder);
                                if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                    node = visitChildren(node, folder.children);
                                    node.state = "closed";
                                }
                                documentNodes.push(node);
                            }

                        });
                        sharedFolderTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });

                        documentFolderTree.tree('append', {
                            parent: documetRootNode.target,
                            data: documentNodes
                        });
                        rootNode = sharedFolderTree.tree('find', 0);
                        if (rootNode != null) {
                            rootNode.target.className = "tree-node tree-node-hover tree-node-selected";
                        }

                        vm.previousSelectedNode = rootNode;
                        loadRootFiles();

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )


            }

            vm.sharedExternalUserFolders = [];
            function loadRootFiles() {
                vm.loading = true;
                vm.sharedExternalUserFolders = [];
                ShareService.getExternalSharedRootAndFilesByPerson(owner).then(
                    function (data) {
                        vm.sharedExternalUserFolders = data;
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;

                    }, function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function visitChildren(parent, folders) {
                var nodes = [];
                angular.forEach(folders, function (folder) {
                    var node = makeNode(folder);
                    node.children = [];
                    if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                        node.state = "closed";
                        node = visitChildren(node, folder.children);
                    }
                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
                return parent;
            }

            function makeNode(folder) {
                var text = folder.name;
                if (folder.count != null && folder.count != "" && folder.count > 0) {
                    text = folder.name + "<span class='label label-default label-count' style='margin-left: 5px;margin-top: -2px;font-size: 12px;background-color: #e4dddd;padding: 1px 4px !important;' title='Files'>" + folder.count + "</span>";
                }
                return {
                    id: ++nodeId,
                    text: text,
                    iconCls: "pdm-folder",
                    attributes: {
                        folder: folder,
                        type: "FOLDER"
                    }
                };
            }


            $rootScope.folderObject = null;
            vm.previousSelectedNode = null;
            vm.selectedFolderNode = null;
            $scope.selectedType = null;
            function onSelectFolder(node) {
                $scope.selectedType = null;
                $rootScope.folderObject = null;
                if (vm.previousSelectedNode != null) {
                    if (vm.previousSelectedNode.target.className == 'tree-node tree-node-hover tree-node-selected' || vm.previousSelectedNode.target.className == 'tree-node tree-node-selected') {

                        if (vm.previousSelectedNode.target.className == 'tree-node tree-node-hover tree-node-selected') {
                            vm.previousSelectedNode.target.className = "tree-node tree-node-selected";
                        }
                        else {
                            vm.previousSelectedNode.target.className = "tree-node";
                        }

                    }
                }

                vm.previousSelectedNode = node;

                if (node.attributes.type == "ROOT") {
                    $scope.selectedType = "FILE";
                    loadRootFiles();
                } else {
                    $rootScope.folderObject = node.attributes.folder;
                    loadSharedFolderFiles();
                }
                $scope.$evalAsync();

            }


            function onSelectDocumentFolder(node) {
                $scope.selectedType = null;
                $rootScope.folderObject = null;
                if (vm.previousSelectedNode != null) {
                    if (vm.previousSelectedNode.target.className == 'tree-node tree-node-hover tree-node-selected' || vm.previousSelectedNode.target.className == 'tree-node tree-node-selected') {
                        if (vm.previousSelectedNode.target.className == 'tree-node tree-node-hover tree-node-selected') {
                            vm.previousSelectedNode.target.className = "tree-node tree-node-selected";
                        }
                        else {
                            vm.previousSelectedNode.target.className = "tree-node";
                        }
                    }

                }
                vm.previousSelectedNode = node;

                if (node.attributes.type == "ROOT") {
                    $scope.selectedType = "DOCUMENT";
                    vm.sharedExternalUserFolders = [];
                } else {
                    $rootScope.folderObject = node.attributes.folder;
                    loadSharedFolderFiles();
                }
                $scope.$evalAsync();

            }


            function loadSharedFolderFiles() {
                vm.loading = true;
                vm.sharedExternalUserFolders = [];
                ShareService.getExternalFolderFilesByPerson($rootScope.folderObject.id, owner, $rootScope.folderObject.objectType).then(
                    function (data) {
                        vm.sharedExternalUserFolders = data;
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;

                    }, function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    vm.sharedExternalUserFolders = [];
                    $('#documentFolderTree').tree('expandAll');
                    c
                    $('#sharedFolderTree').tree('expandAll');
                }
                $('#documentFolderTree').tree('doFilter', vm.searchValue);
                $('#sharedFolderTree').tree('doFilter', vm.searchValue);

            }

            $rootScope.showSharedDeclaration = showSharedDeclaration;

            function showSharedDeclaration() {
                $rootScope.externalUserShow = "Declaration";
                vm.pageable.page = 0;
                vm.sharedProjectView = false;
                vm.mfrView = false;
                vm.sharedItemView = false;
                vm.mfrPartView = false;
                vm.programView = false;
                vm.activityView = false;
                vm.taskView = false;
                vm.supplierView = false;
                vm.declarationView = true;
                vm.folderView = false;
                vm.customObjectView = false;
                vm.sharedFoldersView = false;
                vm.itemTitle = 'Declarations';
                vm.loading = false;
                loadDeclarations();
            }

            $rootScope.showSharedProjects = showSharedProjects;
            function showSharedProjects() {
                vm.itemTitle = 'Projects';
                $rootScope.externalUserShow = "Projects";
                vm.pageable.page = 0;
                vm.sharedProjectView = true;
                vm.mfrView = false;
                vm.customObjectView = false;
                vm.folderView = false;
                vm.sharedItemView = false;
                vm.supplierView = false;
                vm.mfrPartView = false;
                vm.programView = false;
                vm.activityView = false;
                vm.taskView = false;
                vm.declarationView = false;
                vm.sharedFoldersView = false;
                loadSharedProject();
            
                
            }

            vm.sharedProject = sharedProject;
            function sharedProject() {
                vm.sharedProjectView = true;
                vm.mfrView = true;
                vm.sharedItemView = false;
                vm.activityView = false;
                vm.folderView = false;
                vm.customObjectView = false;
                vm.supplierView = false;
                vm.taskView = false;
                vm.sharedFoldersView = false;
                loadProjects();
            }

            vm.sharedActivity = sharedActivity;

            function sharedActivity() {
                vm.activityView = true;
                vm.sharedItemView = false;
                vm.sharedProjectView = false;
                vm.customObjectView = false;
                vm.taskView = false;
                vm.folderView = false;
                vm.supplierView = false;
                vm.sharedFoldersView = false;
                loadPersonActivitys();
            }

            vm.sharedTask = sharedTask;
            function sharedTask() {
                vm.taskView = true;
                vm.activityView = false;
                vm.sharedItemView = false;
                vm.sharedProjectView = false;
                vm.folderView = false;
                vm.customObjectView = false;
                vm.supplierView = false;
                vm.sharedFoldersView = false;
                loadPersonTasks();
            }

            vm.showProject = showProject;
            function showProject(project) {
                $window.localStorage.setItem("shared-permission", project.permission);
                $state.go('app.pm.project.details', {
                    projectId: project.id,
                    permission: project.permission
                });
            }

            function loadProjects() {
                ProjectService.getPersonProjects(login.person.id).then(
                    function (data) {
                        vm.personProjects = data;
                        CommonService.getPersonReferences(vm.personProjects, 'projectManager');
                        angular.forEach(vm.personProjects, function (project) {
                            if (project.percentComplete > 0 && project.percentComplete < 100) {
                                project.percentComplete = parseFloat(project.percentComplete).toFixed(2);
                            }
                            if (project.plannedStartDate) {
                                project.plannedStartDatede = moment(project.plannedStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.plannedFinishDate) {
                                project.plannedFinishDatede = moment(project.plannedFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.actualStartDate) {
                                project.actualStartDatede = moment(project.actualStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.actualFinishDatede) {
                                project.actualFinishDatede = moment(project.actualFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            //----------------------- Activity Details -------------------------- //
            function loadPersonActivitys() {
                ActivityService.getPersonActivitys(login.person.id, vm.pageable).then(
                    function (data) {
                        vm.personActivitys = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.openActivityDetails = openActivityDetails;
            function openActivityDetails(activity) {
                /*$window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify(vm.projectPlanTabId));*/
                $state.go('app.pm.project.activity.details', {activityId: activity.id, permission: activity.permission})
            }

            vm.editActivity = editActivity;
            function editActivity(wbs) {
                var options = {
                    title: editActivityTitle,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/activity/editWbsActivityView.jsp',
                    controller: 'EditWbsActivityController as editWbsActivityVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/activity/editWbsActivityController',
                    width: 550,
                    data: {
                        activityData: wbs,
                        activityMode: 'External',
                        projectData: null
                    },
                    buttons: [
                        {text: updateActivity, broadcast: 'app.project.plan.activity.edit'}
                    ],
                    callback: function (data) {
                        loadPersonActivitys();
                        $rootScope.showSuccessMessage(activityUpdatedMessage)

                    }
                };

                $rootScope.showSidePanel(options);
            }

            //----------------------- Task Details -------------------------- //

            function loadPersonTasks() {
                ActivityService.getPersonActivityTasks(login.person.id, vm.pageable).then(
                    function (data) {
                        vm.personTasks = data;
                        CommonService.getPersonReferences(vm.personTasks.content, 'assignedTo');
                        angular.forEach(vm.personTasks.content, function (task) {
                            task.editMode = false;
                        })
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

            vm.editTask = editTask;
            function editTask(task) {
                task.newName = task.name;
                task.newDescription = task.description;
                task.newPercentComplete = task.percentComplete;
                task.newStatus = task.status;
                task.editMode = true;
            }

            vm.updateTask = updateTask;
            vm.cancelChanges = cancelChanges;
            function cancelChanges(task) {
                task.name = task.newName;
                task.description = task.newDescription;
                task.percentComplete = task.newPercentComplete;
                task.status = task.newStatus;
                task.editMode = false;
            }

            function updateTask(task) {
                if (validate(task)) {
                    if (task.percentComplete > 0 && task.percentComplete < 100) {
                        task.status = "INPROGRESS";
                        ActivityService.updateActivityTask(task.activity, task).then(
                            function (data) {
                                task.editMode = false;
                                $rootScope.showSuccessMessage(taskUpdateMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else if (task.percentComplete == 100) {
                        task.status = "FINISHED";
                        ActivityService.updateActivityTask(task.activity, task).then(
                            function (data) {
                                task.editMode = false;
                                $rootScope.showSuccessMessage(taskUpdateMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        ActivityService.updateActivityTask(task.activity, task).then(
                            function (data) {
                                task.editMode = false;
                                $rootScope.showSuccessMessage(taskUpdateMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }

            }

            function validate(task) {
                var valid = true;

                if (task.percentComplete < 0) {
                    valid = false;
                    $rootScope.showWarningMessage(enterPositiveNumber);
                } else if (task.percentComplete > 100) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidPercent)
                }

                return valid;
            }

            vm.finishActivityTask = finishActivityTask;
            function finishActivityTask(task) {
                task.percentComplete = 100;
                task.status = "FINISHED";
                ActivityService.updateActivityTask(task.activity, task).then(
                    function (data) {
                        vm.personTasks = data;
                        loadPersonTasks();
                        vm.taskView = true;
                        $rootScope.showSuccessMessage(taskUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.deleteTask = deleteTask;
            function deleteTask(task) {
                var options = {
                    title: taskDialogTitle,
                    message: taskDialogMessage + " " + task.name + taskDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ActivityService.deleteActivityTask(task.activity, task.id).then(
                            function (data) {
                                var index = vm.personTasks.content.indexOf(task);
                                vm.personTasks.content.splice(index, 1);
                                $rootScope.showSuccessMessage(taskDeletedMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            function loadSharedMfrs() {
                vm.sharedObjectFilter.sharedObjectType = "MANUFACTURER";
                vm.sharedObjectFilter.person = owner;
                ShareService.getMfrsBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                    function (data) {
                        vm.sharedMfrs = data;
                        vm.noDataImage = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function loadSharedMfrParts() {
                vm.sharedObjectFilter.sharedObjectType = "MANUFACTURERPART";
                vm.sharedObjectFilter.person = owner;
                ShareService.getMfrPartsBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                    function (data) {
                        vm.sharedMfrParts = data;
                        vm.noDataImage = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSharedPrograms() {
                vm.sharedObjectFilter.sharedObjectType = "PROGRAM";
                vm.sharedObjectFilter.person = owner;
                ShareService.getProgramsBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                    function (data) {
                        vm.sharedPrograms = data;
                        vm.noDataImage = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function loadSharedSuppliers() {
                vm.sharedObjectFilter.sharedObjectType = "MFRSUPPLIER";
                vm.sharedObjectFilter.person = owner;
                ShareService.getSuppliersBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                    function (data) {
                        vm.sharedSuppliers = data;
                        vm.noDataImage = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSharedCustomObjects() {
                vm.sharedObjectFilter.sharedObjectType = "CUSTOMOBJECT";
                vm.sharedObjectFilter.person = owner;
                ShareService.getCustomObjectsBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                    function (data) {
                        vm.sharedCustomObjects = data;
                        vm.noDataImage = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.noPermission = parsed.html($translate.instant('NO_PERMISSION_PERFORM')).html();
            function loadSharedFolders() {
                vm.sharedObjectFilter.sharedObjectType = "DOCUMENT";
                vm.sharedObjectFilter.person = owner;
                ShareService.getFoldersBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                    function (data) {
                        vm.sharedFolders = data;
                        angular.forEach(vm.sharedFolders.content, function (obj) {
                            obj.level = 0;
                            obj.expanded = false;
                            obj.folderChildren = [];
                            obj.parentFolder = null;
                        });
                        vm.noDataImage = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function loadDeclarations() {
                vm.sharedObjectFilter.sharedObjectType = "PGCDECLARATION";
                vm.sharedObjectFilter.person = owner;
                ShareService.getDeclarationsBySharedPersonId(vm.pageable, vm.sharedObjectFilter).then(
                    function (data) {
                        vm.sharedDeclarations = data;
                        vm.noDataImage = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSharedProject() {
                vm.sharedObjectFilter.sharedObjectType = "PROJECT";
                vm.sharedObjectFilter.person = owner;
                ShareService.getProjectObjectsBySharedPersonId(vm.pageable, vm.sharedObjectFilter, "project").then(
                    function (data) {
                        vm.loading = false;
                        vm.sharedProjects = data;
                        angular.forEach(vm.sharedProjects.content, function (project) {
                            if (project.percentComplete > 0 && project.percentComplete < 100) {
                                project.percentComplete = parseInt(project.percentComplete);
                            }
                            if (project.plannedStartDate) {
                                project.plannedStartDatede = moment(project.plannedStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.plannedFinishDate) {
                                project.plannedFinishDatede = moment(project.plannedFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.actualStartDate) {
                                project.actualStartDatede = moment(project.actualStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.actualFinishDatede) {
                                project.actualFinishDatede = moment(project.actualFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            var firstLetter = "";
                            var lastLetter = "";
                            if (project.managerFirstName != null && project.managerFirstName != "") {
                                firstLetter = project.managerFirstName.substring(0, 1).toUpperCase();
                            }
                            if (project.managerLastName != null && project.managerLastName != "") {
                                lastLetter = project.managerLastName.substring(0, 1).toUpperCase();
                            }
                            project.managerWord = firstLetter + "" + lastLetter;
                            if (project.hasManagerImage) {
                                project.managerImage = "api/common/persons/" + project.projectManager + "/image/download?" + new Date().getTime();
                            }
                            angular.forEach(project.projectMembers, function (projectMember) {
                                var firstLetter = "";
                                var lastLetter = "";
                                if (projectMember.firstName != null && projectMember.firstName != "") {
                                    firstLetter = projectMember.firstName.substring(0, 1).toUpperCase();
                                }
                                if (projectMember.lastName != null && projectMember.lastName != "") {
                                    lastLetter = projectMember.lastName.substring(0, 1).toUpperCase();
                                }
                                projectMember.imageWord = firstLetter + "" + lastLetter;
                                if (projectMember.hasImage) {
                                    projectMember.personImage = "api/common/persons/" + projectMember.id + "/image/download?" + new Date().getTime();
                                }
                            })
                        });
                        CommonService.getPersonReferences(vm.sharedProjects.content, 'projectManager');
                        vm.noDataImage = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSharedProjectActivities() {
                vm.sharedObjectFilter.sharedObjectType = "PROJECTACTIVITY";
                vm.sharedObjectFilter.person = owner;
                ShareService.getProjectObjectsBySharedPersonId(vm.pageable, vm.sharedObjectFilter, "activity").then(
                    function (data) {
                        vm.loading = false;
                        vm.sharedProjectActivities = data;
                        angular.forEach(vm.sharedProjectActivities.content, function (activity) {
                            if (activity.percentComplete > 0 && activity.percentComplete < 100) {
                                activity.percentComplete = parseInt(activity.percentComplete);
                            }
                        });
                        CommonService.getPersonReferences(vm.sharedProjectActivities.content, 'assignedTo');
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSharedProjectTasks() {
                vm.sharedObjectFilter.sharedObjectType = "PROJECTTASK";
                vm.sharedObjectFilter.person = owner;
                ShareService.getProjectObjectsBySharedPersonId(vm.pageable, vm.sharedObjectFilter, "task").then(
                    function (data) {
                        vm.loading = false;
                        vm.sharedProjectTasks = data;
                        angular.forEach(vm.sharedProjectTasks.content, function (task) {
                            if (task.percentComplete > 0 && task.percentComplete < 100) {
                                task.percentComplete = parseInt(task.percentComplete);
                            }
                        });
                        CommonService.getPersonReferences(vm.sharedProjectTasks.content, 'assignedTo');
                        angular.forEach(vm.sharedProjectTasks.content, function (task) {
                            task.editMode = false;
                        });
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showMfrDetails = showMfrDetails;
            function showMfrDetails(mfr) {
                $rootScope.externalUserShow = null;
                $state.go('app.mfr.details', {manufacturerId: mfr.id, permission: mfr.permission});
                $rootScope.externalUserShow = "Mfr";
            }

            vm.showProgramDetails = showProgramDetails;
            function showProgramDetails(program) {
                $rootScope.externalUserShow = null;
                $window.localStorage.setItem("shared-permission", program.permission);
                $state.go('app.pm.program.details', {
                    programId: program.id,
                    tab: 'details.basic',
                    permission: program.permission
                });
                $rootScope.externalUserShow = "Program";
            }


            vm.showSupplierDetails = showSupplierDetails;
            function showSupplierDetails(supplier) {
                $rootScope.externalUserShow = null;
                $window.localStorage.setItem("shared-permission", supplier.permission);
                $state.go('app.mfr.supplier.details', {supplierId: supplier.id, permission: supplier.permission});
                $rootScope.externalUserShow = "Supplier";
            }

            vm.showCustomObjectDetails = showCustomObjectDetails;
            function showCustomObjectDetails(custom) {
                $rootScope.externalUserShow = null;
                $state.go('app.customobjects.details', {customId: custom.id, permission: custom.permission});

                $rootScope.externalUserShow = "CustomObject";
            }


            vm.showMfrPartDetails = showMfrPartDetails;
            function showMfrPartDetails(part) {
                $rootScope.externalUserShow = null;
                $state.go('app.mfr.mfrparts.details', {
                    mfrId: part.mfrId,
                    manufacturePartId: part.id,
                    permission: part.permission
                });
                $rootScope.externalUserShow = "MfrPart";
                $window.localStorage.setItem("shared-permission", part.permission);
            }

            $rootScope.showAssignedTasks = showAssignedTasks;
            function showAssignedTasks() {
                var options = {
                    title: 'Assigned Tasks',
                    template: 'app/desktop/modules/home/external/externalUserTasksView.jsp',
                    controller: 'ExternalUserTasksController as externalUserTaskVm',
                    resolve: 'app/desktop/modules/home/external/externalUserTasksController',
                    width: 600,
                    showMask: true,
                    data: {},
                    buttons: [],
                    callback: function () {
                        $timeout(function () {
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            $rootScope.externalUserTasks = [];
            function loadExternalUserTasks() {
                vm.loading = true;
                UserTasksService.getUserTasks(owner).then(
                    function (data) {
                        angular.forEach(data, function (task) {
                            if (task.status === 'PENDING') {
                                $rootScope.externalUserTasks.push(task);
                            }
                        });
                        loadSharedCounts();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showDeclaration = showDeclaration;
            function showDeclaration(declaration) {
                $rootScope.externalUserShow = null;
                $state.go('app.compliance.declaration.details', {
                    declarationId: declaration.id,
                    tab: 'details.basic',
                    permission: declaration.permission
                });
                $rootScope.externalUserShow = "Declaration";
            }

            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                //$rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.sharedObjectFilter.searchQuery = freeText;
                    loadSharedItems();
                } else {
                    resetPage();
                }
            }

            vm.active = 0;
            vm.tabs = {
                project: {
                    id: 'details.project',
                    heading: vm.projectHeading,
                    template: 'app/desktop/modules/home/external/sharedProjectView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                activity: {
                    id: 'details.activity',
                    heading: vm.activityHeading,
                    template: 'app/desktop/modules/home/external/sharedProjectActivityView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                task: {
                    id: 'details.task',
                    heading: vm.taskHeading,
                    template: 'app/desktop/modules/home/external/sharedProjectTaskView.jsp',
                    index: 2,
                    active: false,
                    activated: false
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
                }
                // else if (id == "details.project"){
                //     loadSharedProject();
                //     vm.currentTab = "project";
                // }
            }

            vm.loadSharedCounts = loadSharedCounts;
            vm.sharedCounts = [];
            function loadSharedCounts() {
                ShareService.getPersonSharedCounts(personId).then(
                    function (data) {
                        vm.sharedCounts = data;
                        console.log(vm.sharedCounts);
                        var sharedProject = document.getElementById("sharedProject");
                        var sharedActivity = document.getElementById("sharedActivity");
                        var sharedTask = document.getElementById("sharedTask");
                        if (sharedProject != null) {
                            sharedProject.lastElementChild.innerHTML = vm.tabs.project.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.sharedCounts.project);
                        }
                        if (sharedActivity != null) {
                            sharedActivity.lastElementChild.innerHTML = vm.tabs.activity.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.sharedCounts.activity);
                        }
                        if (sharedTask != null) {
                            sharedTask.lastElementChild.innerHTML = vm.tabs.task.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.sharedCounts.task);
                        }
                        loadSharedItems();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
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

            $rootScope.downloadFile = downloadFile;
            function downloadFile(file) {
                $rootScope.showBusyIndicator();
                ObjectFileService.updateObjectFileDownloadHistory(file.parentFile, "DOCUMENT", file.id).then(
                    function (data) {
                        var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                            format(window.location.protocol, window.location.host,
                            file.parentFile, "DOCUMENT", file.id);
                        $rootScope.downloadFileFromIframe(url);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.sharedFileDownload = sharedFileDownload;
            function sharedFileDownload(file) {

                ObjectFileService.updateObjectFileDownloadHistory(file.parentObjectId, file.type, file.id).then(
                    function (data) {
                        var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                            format(window.location.protocol, window.location.host,
                            file.parentObjectId, file.type, file.id);
                        $rootScope.downloadFileFromIframe(url);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $scope.filePreview = filePreview;
            function filePreview(file) {
                let permission = $rootScope.hasPermission((file.objectType).toLowerCase(), "preview");
                if (permission) {
                    var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/preview".format(window.location.protocol, window.location.host, file.parentObjectId, file.type, file.id);
                    var newWindow = window.open(url, "_blank");
                    newWindow.addEventListener('load', function () {
                        newWindow.document.title = file.name;
                    });
                    $timeout(function () {
                        window.close();
                    }, 2000);
                } else {
                    $rootScope.showErrorMessage("You do not have permission to perform this action");
                }
            }

            $scope.getFileIcon = getFileIcon;
            function getFileIcon(fileName) {
                var fileExtensionPattern = /\.([0-9a-z]+)(?=[?#])|(\.)(?:[\w]+)$/gmi;
                if (fileName.includes(".")) {
                    var extension = fileName.toLowerCase().match(fileExtensionPattern)[0];
                    if ([".html", ".csv", ".ttf", ".exe", ".log", ".bat", ".css", ".jsp", ".js", ".sql", ".java", ".class", ".c", ".net", ".py", ".ipynb"].indexOf(extension) > -1) {
                        return "fa fa-file-code-o";
                    }
                    else if ([".png", ".jpeg", ".jpg", ".bmp", ".eps", ".gif", ".pict", ".esd", ".tif"].indexOf(extension) > -1) {
                        return "fa fa-file-photo-o";
                    }
                    else if ([".xlsx", ".xls"].indexOf(extension) > -1) {
                        return "fa fa-file-excel-o";
                    }
                    else if ([".mp4", ".avi", ".flv", ".mpg", ".mpeg", ".mov", ".wmv"].indexOf(extension) > -1) {
                        return "fa fa-file-movie-o";
                    }
                    else if ([".mp3", ".wav", ".mid", ".mkv"].indexOf(extension) > -1) {
                        return "fa fa-file-audio-o";
                    }
                    else if ([".docx", ".doc"].indexOf(extension) > -1) {
                        return "fa fa-file-word-o";
                    }
                    else if ([".pdf"].indexOf(extension) > -1) {
                        return "fa fa-file-pdf-o";
                    }
                    else if ([".ppt"].indexOf(extension) > -1) {
                        return "fa fa-file-powerpoint-o";
                    }
                    else if ([".txt", ".rtf", ".wps", ".wpd"].indexOf(extension) > -1) {
                        return "fa fa-file-text-o";
                    }
                    else if ([".zip", ".war", ".jar", ".arc", ".arj", ".gz", ".hqx", ".sit", ".tar", ".z"].indexOf(extension) > -1) {
                        return "fa fa-file-zip-o";
                    }
                    else {
                        return "fa fa-file-o";
                    }
                }
            }

            $scope.getFileIconColor = getFileIconColor;
            function getFileIconColor(fileName) {
                var fileExtensionPattern = /\.([0-9a-z]+)(?=[?#])|(\.)(?:[\w]+)$/gmi;
                if (fileName.includes(".")) {
                    var extension = fileName.toLowerCase().match(fileExtensionPattern)[0];
                    if ([".html", ".csv", ".ttf", ".exe", ".log", ".bat", ".css", ".jsp", ".js", ".sql", ".java", ".class", ".c", ".net", ".py", ".ipynb"].indexOf(extension) > -1) {
                        return "{'color': '#e699ff'}";
                    }
                    else if ([".png", ".jpeg", ".jpg", ".bmp", ".eps", ".gif", ".pict", ".esd", ".tif"].indexOf(extension) > -1) {
                        return "{'color': '#9999ff'}";
                    }
                    else if ([".xlsx", ".xls"].indexOf(extension) > -1) {
                        return "{'color': '#00b377'}";
                    }
                    else if ([".mp4", ".avi", ".flv", ".mpg", ".mpeg", ".mov", ".wmv"].indexOf(extension) > -1) {
                        return "{'color': '#ff6600'}";
                    }
                    else if ([".mp3", ".wav", ".mid", ".mkv"].indexOf(extension) > -1) {
                        return "{'color': '#ff6600'}";
                    }
                    else if ([".docx", ".doc"].indexOf(extension) > -1) {
                        return "{'color': '#4d79ff'}";
                    }
                    else if ([".pdf"].indexOf(extension) > -1) {
                        return "{'color': '#ff0000'}";
                    }
                    else if ([".ppt"].indexOf(extension) > -1) {
                        return "{'color': '#ff531a'}";
                    }
                    else if ([".txt", ".rtf", ".wps", ".wpd"].indexOf(extension) > -1) {
                        return "{'color': '#5b1f07'}";
                    }
                    else if ([".zip", ".war", ".jar", ".arc", ".arj", ".gz", ".hqx", ".sit", ".tar", ".z"].indexOf(extension) > -1) {
                        return "{'color': '#ffb84d'}";
                    }
                    else {
                        return "{'color': '#ff6600'}";
                    }
                }
            }

            function sharedTypeDetailsSelected(event, args) {
                $rootScope.selectedPerson = args.typeObject.attributes.person;
                owner = $rootScope.selectedPerson.id;
                //loadSharedItems();
            }

            (function () {
                $scope.$on('app.shared.object.type', sharedTypeDetailsSelected);
            })();
        }
    }
)
;
