define(['app/desktop/modules/sharing/sharing.module',
    'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
    'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
    'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
    'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
    'app/shared/services/core/shareService',
    'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
    'app/desktop/modules/sharing/all/sharedController',
    'app/desktop/directives/all-view-icons/allViewIconsDirective'
],
    function (module) {
        module.controller('AllSharedToController', AllSharedToController);

        function AllSharedToController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
            $uibModal, ShareService, ItemService, PersonGroupService, CommonService, LoginService) {

            $rootScope.viewInfo.icon = "las la-share";
            $rootScope.viewInfo.title = $translate.instant("SHARING");
            $rootScope.viewInfo.showDetails = false;
            var parsed = angular.element("<div></div>");

            var vm = this;
            vm.loading = true;
            vm.loadSharedObjectsBySharedBy = loadSharedObjectsBySharedBy;

            nodeId = 0;
            documetRootNode = null;

            vm.loading = true;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            vm.sharedAllItems = sharedAllItems;
            function sharedAllItems() {
                vm.pageable.page = 0;
                $rootScope.itemsView = true;
                $rootScope.mfrView = false;
                $rootScope.programsView = false;
                $rootScope.mfrPartView = false;
                $rootScope.supplierView = false;
                $rootScope.declarationView = false;
                $rootScope.projectView = false;
                $rootScope.folderView = true;
                $rootScope.loadSharedItems();
            }

            vm.sharedMfr = sharedMfr;
            function sharedMfr() {
                vm.pageable.page = 0;
                $rootScope.itemsView = false;
                $rootScope.mfrView = true;
                $rootScope.programsView = false;
                $rootScope.mfrPartView = false;
                $rootScope.declarationView = false;
                $rootScope.projectView = false;
                $rootScope.supplierView = false;
                $rootScope.folderView = true;
                $rootScope.loadSharedMfrs();
            }

            vm.sharedMfrPart = sharedMfrPart;
            function sharedMfrPart() {
                vm.pageable.page = 0;
                $rootScope.itemsView = false;
                $rootScope.mfrView = false;
                $rootScope.programsView = false;
                $rootScope.mfrPartView = true;
                $rootScope.supplierView = false;
                $rootScope.declarationView = false;
                $rootScope.projectView = false;
                $rootScope.folderView = false;
                $rootScope.sharedMfrPart();
            }

            vm.sharedSupplier = sharedSupplier;
            function sharedSupplier() {
                vm.pageable.page = 0;
                $rootScope.itemsView = false;
                $rootScope.mfrView = false;
                $rootScope.programsView = false;
                $rootScope.mfrPartView = false;
                $rootScope.supplierView = true;
                $rootScope.declarationView = false;
                $rootScope.projectView = false;
                $rootScope.folderView = false;
                $rootScope.sharedSupplier();
            }

            vm.sharedFolder = sharedFolder;
            function sharedFolder() {
                vm.pageable.page = 0;
                $rootScope.itemsView = false;
                $rootScope.mfrView = false;
                $rootScope.programsView = false;
                $rootScope.mfrPartView = false;
                $rootScope.supplierView = false;
                $rootScope.folderView = true;
                $rootScope.declarationView = false;
                $rootScope.projectView = false;
                $rootScope.sharedFolder();
            }

            vm.sharedProgramObjects = sharedProgramObjects;
            function sharedProgramObjects() {
                vm.pageable.page = 0;
                $rootScope.itemsView = false;
                $rootScope.mfrView = false;
                $rootScope.programsView = true;
                $rootScope.mfrPartView = false;
                $rootScope.declarationView = false;
                $rootScope.projectView = false;
                $rootScope.supplierView = false;
                $rootScope.folderView = false;
                $rootScope.sharedProgram();
            }

            vm.sharedDeclaration = sharedDeclaration;
            function sharedDeclaration() {
                vm.pageable.page = 0;
                $rootScope.itemsView = false;
                $rootScope.mfrView = false;
                $rootScope.programsView = false;
                $rootScope.mfrPartView = false;
                $rootScope.declarationView = true;
                $rootScope.projectView = false;
                $rootScope.supplierView = false;
                $rootScope.folderView = true;
                $rootScope.loadSharedDeclarations();
            }

            vm.sharedProjectsObjects = sharedProjectsObjects;
            function sharedProjectsObjects() {
                vm.pageable.page = 0;
                $rootScope.itemsView = false;
                $rootScope.mfrView = false;
                $rootScope.programsView = false;
                $rootScope.mfrPartView = false;
                $rootScope.declarationView = false;
                $rootScope.projectView = true;
                $rootScope.supplierView = false;
                $rootScope.folderView = false;
                $rootScope.showSharedProjects();
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
                } else if (vm.supplierView) {
                    loadSharedBySupplierObjects();
                } else if (vm.declarationView) {
                    loadSharedByDeclarationObjects();
                } else if (vm.projectView) {
                    loadSharedByProjectObjects(vm.currentTab);
                }
            }

            function initSharedToTree() {
                documentFolderTree = $('#sharingObjectTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "Users",
                            iconCls: 'users-node',
                            attributes: {
                                type: 'ROOT'
                            },

                        }
                    ],

                    onSelect: onSelectFolder
                });

                documetRootNode = documentFolderTree.tree('find', 0);
                constructPersonTree();


            }

            function constructPersonTree() {
                var nodes = [];
                angular.forEach(vm.sharedTo, function (person) {
                    var node = makeNode(person);
                    nodes.push(node);
                });

                documentFolderTree.tree('append', {
                    parent: documetRootNode.target,
                    data: nodes
                });

            }

            vm.sharedTo = [];
            function loadSharedTo() {
                ShareService.getSharedToPersons().then(
                    function (data) {
                        vm.sharedTo = data;
                        initSharedToTree();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.folderObject = null;
            vm.previousSelectedFolder = 0;
            vm.selectedFolderNode = null;
            $rootScope.person = null;
            function onSelectFolder(person) {
                $rootScope.person = null;
                $rootScope.person = person.attributes.person;
                vm.person = person.attributes.person;
                vm.personId = person.attributes.person.id;
                loadSharedCounts();
                $scope.$broadcast('app.shared.object.type', { typeObject: person });
                $rootScope.loadingObject();
            }


            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                if (vm.itemsView) {
                    loadSharedToObjects();
                } else if (vm.mfrView) {
                    $rootScope.loadSharedMfrs();
                } else if (vm.programsView) {
                    loadSharedPrograms();
                } else if (vm.mfrPartView) {
                    loadSharedMfrParts();
                } else if (vm.supplierView) {
                    loadSharedSuppliers();
                } else if (vm.folderView) {
                    loadSharedFolders();
                } else if (vm.declarationView) {
                    loadSharedDeclarations();
                } else if (vm.projectView && vm.currentTab == "project") {
                    loadSharedProject();
                }
            }

            vm.sharedCounts = [];
            vm.loadSharedCounts = loadSharedCounts;
            function loadSharedCounts() {
                ShareService.getPersonSharedCounts(vm.personId).then(
                    function (data) {
                        vm.sharedCounts = data;
                        loadSharedObjects();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    $('#sharingObjectTree').tree('expandAll');
                }
                $('#sharingObjectTree').tree('doFilter', vm.searchValue);
            }

            function makeNode(person) {
                return {
                    id: ++nodeId,
                    text: person.fullName,
                    iconCls: "user-node",
                    attributes: {
                        person: person,
                        type: "PERSON"
                    }
                };
            }

            function loadSharedObjects() {
                if (vm.sharedCounts.item > 0) {
                    sharedAllItems();
                } else if (vm.sharedCounts.mfr > 0) {
                    sharedMfr();
                } else if (vm.sharedCounts.mfrPart > 0) {
                    sharedMfrPart();
                } else if (vm.sharedCounts.program > 0) {
                    sharedProgramObjects();
                } else if (vm.sharedCounts.supplier > 0) {
                    sharedSuppliers();
                } else if (vm.sharedCounts.folder > 0) {
                    sharedFolder();
                } else if (vm.sharedCounts.project > 0) {
                    sharedProjectsObjects();
                } else if (vm.sharedCounts.declaration > 0) {
                    sharedDeclaration();
                }
            }


            (function () {
                loadSharedTo();
            })();
        }

    }
);