define(
    [
        'app/desktop/modules/shared/shared.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/shared/services/core/shareService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/supplierService',
        'app/shared/services/core/documentService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/programService'
    ],

    function (module) {
        module.controller('ShareController', ShareController);

        function ShareController($scope, $rootScope, $stateParams, $translate, LoginService, $timeout, PersonGroupService, ShareService, CommonService,
                                 ItemService, MfrService, MfrPartsService, SupplierService, DocumentService, CustomObjectService, ProjectService, ProgramService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.item = null;
            vm.items = null;
            vm.selectedItems = null;
            vm.item = $scope.data.sharedItem;
            vm.items = $scope.data.selectedShareItems;
            vm.selectionType = $scope.data.itemsSharedType;
            vm.objectType = $scope.data.objectType;
            vm.processing = false;

            if (vm.items != null) {
                vm.selectedItems = vm.items.length + " Item Numbers";
                vm.selectedMfrs = vm.items.length + " Manufacturers";
                vm.selectedMfrParts = vm.items.length + " ManufacturerParts";
                vm.selectedSuppliers = vm.items.length + " Suppliers";
                vm.selectedFolders = vm.items.length + " Folders";
                vm.selectedProjects = vm.items.length + " Projects";
                vm.selectedPrograms = vm.items.length + " Programs";
                vm.selectedCustomObjectss = vm.items.length + " Custom Objects";
            }

            vm.permissions = ['READ', 'WRITE'];
            vm.shareTypes = ['PERSON', 'GROUP'];
            vm.shareItem = shareItem;
            vm.itemsIds = [];
            vm.shareId = [];
            var numbers = " ";
            var selectedNumbers = " ";
            vm.existedNumbers = [];
            vm.selectedNumbers = [];
            vm.deleteShare = deleteShare;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            vm.shareObject = {
                shareId: null,
                objectId: null,
                shareType: vm.shareTypes[0],
                sharedObjectType: null,
                sharedTo: null,
                sharedToObjects: [],
                sharedBy: $rootScope.loginPersonDetails.person.id,
                permission: null
            };

            function shareItem() {
                if (validate()) {
                    vm.processing = true;

                    /*---------------   For Multiple Users ------------------------------*/

                    if (vm.shareObject.shareType == "PERSON") {
                        angular.forEach(vm.sharedToObjects, function (login) {
                            vm.shareObject.sharedToObjects.push(login.person.id);
                        });
                    } else if ((vm.shareObject.shareType == "GROUP")) {
                        vm.shareObject.sharedToObjects.push(vm.sharedToGroupObject.groupId);
                    }

                    if (vm.objectType == "PROJECT" || vm.objectType == "PROGRAM" || vm.objectType == "PROJECTACTIVITY" || vm.objectType == "PROJECTTASK") vm.item.objectType = vm.objectType;
                    vm.shareObject.objectId = vm.item.id;
                    vm.shareObject.sharedObjectType = vm.item.objectType;
                    vm.shareObject.sharedBy = $rootScope.loginPersonDetails.person.id;
                    ShareService.createShare(vm.shareObject).then(
                        function (data) {
                            vm.shared = data;
                            vm.processing = false;
                            vm.shareObject = {
                                shareId: null,
                                objectId: null,
                                sharedObjectType: null,
                                sharedTo: null,
                                sharedToObjects: [],
                                permission: null
                            };
                            vm.sharedToObjects = [];
                            vm.sharedToGroupObject = null;
                            $scope.callback(data);
                            $rootScope.hideSidePanel();
                        },
                        function (error) {
                            $scope.callback();
                            $rootScope.hideSidePanel();
                        }
                    )
                }
            }

            var itemCreatedMessage = parsed.html($translate.instant("ITEM_SHARED_MESSAGE")).html();
            var mfrCreatedMessage = parsed.html($translate.instant("MFR_SHARED_MESSAGE")).html();
            var mfrPartCreatedMessage = parsed.html($translate.instant("MFRPART_SHARED_MESSAGE")).html();
            var supplierCreatedMessage = parsed.html($translate.instant("SUPPLIER_SHARED_MESSAGE")).html();
            var customObjectCreatedMessage = parsed.html($translate.instant("CUSTOM_OBJECT_SHARED_MESSAGE")).html();
            var folderCreatedMessage = parsed.html($translate.instant("FOLDER_SHARED_MESSAGE")).html();
            vm.selectPermission = parsed.html($translate.instant("ITEM_SHARED_PERMISSION")).html();
            vm.selectGroupMsg = parsed.html($translate.instant("SELECT_GROUP_MSG")).html();
            vm.selectUserMsg = parsed.html($translate.instant("SELECT_USER_MSG")).html();
            vm.selectPersonType = parsed.html($translate.instant("SELECT_TYPE")).html();
            vm.selectUserType = parsed.html($translate.instant("SELECT_USER_TYPE")).html();
            vm.noExternalUsersMsg = parsed.html($translate.instant("EXTERNAL_USER_MSG")).html();
            var selectUserAndGroupValidation = parsed.html($translate.instant("SELECT_USER_GROUP")).html();
            var selectPermissionValidation = parsed.html($translate.instant("SELECT_PERMISSION")).html();
            var itemSharedMsg = parsed.html($translate.instant("SHARED_SUCCESS_MSG")).html();
            var sharedItemAlreadyExit = parsed.html($translate.instant("SHARED_ITEM_ALREADY_EXIT")).html();
            var sharedItemMsgAnd = parsed.html($translate.instant("SHARED_ITEM_MSG_AND")).html();
            var added = parsed.html($translate.instant("ADDED")).html();
            var itemsAlreadyExists = parsed.html($translate.instant("ITEMS_ALREADY_EXISTS")).html();
            var sharedSuccesfully = parsed.html($translate.instant("SHARED_ITEM_MSG_AND")).html();
            var projectsSharedSucessfully = $translate.instant("PROJECT_ITEM_SHARED_MESSAGE");
            var programsSharedSucessfully = $translate.instant("PROGRAM_ITEM_SHARED_MESSAGE");

            vm.sharedToObjects = [];
            vm.sharedToGroupObject = null;

            function shareSelectedObjects() {
                var promise = null;
                vm.itemsIds = [];
                vm.shareId = [];
                if (validate()) {
                    vm.processing = true;

                    /*--------------  For Multiple Users -----------------------*/

                    if (vm.shareObject.shareType == "PERSON") {
                        angular.forEach(vm.sharedToObjects, function (login) {
                            vm.shareObject.sharedToObjects.push(login.person.id);
                        });
                    } else if (vm.shareObject.shareType == "GROUP") {
                        vm.shareObject.sharedToObjects.push(vm.sharedToGroupObject.groupId);
                    }
                    if (vm.objectType == "ITEM") {
                        promise = ShareService.shareMultiple(vm.items, vm.shareObject);
                    } else if (vm.objectType == "MANUFACTURER") {
                        promise = ShareService.shareMultipleMfr(vm.items, vm.shareObject)
                    } else if (vm.objectType == "MANUFACTURERPART") {
                        promise = ShareService.shareMultipleMfrParts(vm.items, vm.shareObject)
                    }
                    else if (vm.objectType == "MFRSUPPLIER") {
                        promise = ShareService.shareMultipleSuppliers(vm.items, vm.shareObject)
                    }

                    else if (vm.objectType == "CUSTOMOBJECT") {
                        promise = ShareService.shareMultipleCustomObjects(vm.items, vm.shareObject)
                    }

                    else if (vm.objectType == "DOCUMENT") {
                        promise = ShareService.shareMultipleFolders(vm.items, vm.shareObject)
                    }

                    else if (vm.objectType == "PROJECT") {
                        promise = ShareService.shareMultipleProjects(vm.items, vm.shareObject)
                    }
                    else if (vm.objectType == "PROGRAM") {
                        promise = ShareService.shareMultiplePrograms(vm.items, vm.shareObject)
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                vm.shared = data;
                                vm.exists = vm.shared[0].existingItems;
                                vm.added = vm.shared[0].newItems;
                                vm.shareObject = {
                                    shareId: null,
                                    objectId: null,
                                    sharedObjectType: null,
                                    sharedTo: null, sharedToObjects: [],
                                    permission: null
                                };
                                vm.sharedToObjects = [];
                                vm.sharedToGroupObject = null;
                                $scope.callback(vm.items);
                                vm.processing = false;
                                $rootScope.hideSidePanel();
                                if (vm.objectType == "ITEM") {
                                    $rootScope.showSuccessMessage(itemCreatedMessage);
                                } else if (vm.objectType == "MANUFACTURER") {
                                    $rootScope.showSuccessMessage(mfrCreatedMessage);
                                } else if (vm.objectType == "MANUFACTURERPART") {
                                    $rootScope.showSuccessMessage(mfrPartCreatedMessage);
                                }
                                else if (vm.objectType == "MFRSUPPLIER") {
                                    $rootScope.showSuccessMessage(supplierCreatedMessage);
                                }

                                else if (vm.objectType == "CUSTOMOBJECT") {
                                    $rootScope.showSuccessMessage(customObjectCreatedMessage);
                                }

                                else if (vm.objectType == "DOCUMENT") {
                                    $rootScope.showSuccessMessage(folderCreatedMessage);
                                }
                                else if (vm.objectType == "PROJECT") {
                                    $rootScope.showSuccessMessage(projectsSharedSucessfully);
                                } else if (vm.objectType == "PROGRAM") {
                                    $rootScope.showSuccessMessage(programsSharedSucessfully);
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate() {
                var valid = true;
                if (vm.shareObject.shareType == 'PERSON' && vm.sharedToObjects.length == 0) {
                    valid = false;
                    $rootScope.showWarningMessage(selectUserAndGroupValidation);
                } else if (vm.shareObject.shareType == 'GROUP' && vm.sharedToGroupObject == null) {
                    valid = false;
                    $rootScope.showWarningMessage(selectUserAndGroupValidation);
                } else if (vm.shareObject.permission == null || vm.shareObject.permission == "" || vm.shareObject.permission == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectPermissionValidation);
                }

                return valid;
            }

            function loadUsers() {
                vm.users = [];
                if (vm.selectionType == 'itemSelection') {
                    LoginService.getAllLogins().then(
                        function (data) {
                            angular.forEach(data, function (user) {
                                if (user.isActive == true && user.external == true) {
                                    if (!map.has(user.person.id)) {
                                        vm.users.push(user);
                                    }
                                }
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.selectionType != 'itemSelection') {
                    LoginService.getAllLogins().then(
                        function (data) {
                            angular.forEach(data, function (user) {
                                if (user.isActive == true && user.external == true) {
                                    vm.users.push(user);
                                }
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadGroups() {
                vm.groups = [];
                PersonGroupService.getAllPersonGroups().then(
                    function (data) {
                        angular.forEach(data, function (val) {
                            if (val.external) vm.groups.push(val);
                        });
                        /*if (vm.groups.length == 1) vm.sharedToObjects = vm.groups[0];*/
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.itemSharedPersons = [];
            $scope.sharedUsers = [];

            var map = new Map();

            function loadItemSharedUsers() {
                if (vm.selectionType == 'itemSelection') {
                    map = new Map();
                    ShareService.getItemSharedPerson(vm.item.id).then(
                        function (data) {
                            vm.itemSharedPersons = data;
                            angular.forEach(vm.itemSharedPersons, function (itemShared) {
                                map.set(itemShared.userId, itemShared);
                                if (itemShared.shareType == 'PERSON') {
                                    CommonService.getPerson(itemShared.sharedTo).then(
                                        function (person) {
                                            itemShared.sharedTo = person.firstName;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                                if (itemShared.shareType == 'GROUP') {
                                    PersonGroupService.getPersonGrpById(itemShared.sharedTo).then(
                                        function (group) {
                                            itemShared.sharedTo = group.name;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }

                                if (vm.objectType == "ITEM") {
                                    ItemService.getItem(itemShared.objectId).then(
                                        function (item) {
                                            itemShared.objectId = item;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (vm.objectType == "MANUFACTURER") {
                                    MfrService.getManufacturer(itemShared.objectId).then(
                                        function (item) {
                                            itemShared.objectId = item;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (vm.objectType == "MANUFACTURERPART") {
                                    MfrPartsService.getManufacturepart(itemShared.objectId).then(
                                        function (item) {
                                            itemShared.objectId = item;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }

                                else if (vm.objectType == "MFRSUPPLIER") {
                                    SupplierService.getSupplier(itemShared.objectId).then(
                                        function (item) {
                                            itemShared.objectId = item;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }

                                else if (vm.objectType == "CUSTOMOBJECT") {
                                    CustomObjectService.getCustomObject(itemShared.objectId).then(
                                        function (item) {
                                            itemShared.objectId = item;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                                else if (vm.objectType == "DOCUMENT") {
                                    DocumentService.getDocument(itemShared.objectId).then(
                                        function (item) {
                                            itemShared.objectId = item;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                                else if (vm.objectType == "PROJECT") {
                                    ProjectService.getProject(itemShared.objectId).then(
                                        function (item) {
                                            itemShared.objectId = item;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                                else if (vm.objectType == "PROGRAM") {
                                    ProgramService.getProgram(itemShared.objectId).then(
                                        function (item) {
                                            itemShared.objectId = item;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            });
                            $timeout(function () {
                                loadUsers();
                            }, 1000)
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var sharedDeleteMessage = parsed.html($translate.instant("SHARED_ITEM_DELETE")).html();

            function deleteShare(share) {
                if (share.sharedObjectType == 'MANUFACTURERPART') {
                    sharedDeleteMessage = parsed.html($translate.instant("SHARED_PART_DELETE")).html();
                }
                if (share.sharedObjectType == 'MFRSUPPLIER') {
                    sharedDeleteMessage = parsed.html($translate.instant("SHARED_SUPPLIER_DELETE")).html();
                }
                if (share.sharedObjectType == 'CUSTOMOBJECT') {
                    sharedDeleteMessage = parsed.html($translate.instant("SHARED_CUSTOM_OBJECT_DELETE")).html();
                }
                if (share.sharedObjectType == 'DOCUMENT') {
                    sharedDeleteMessage = parsed.html($translate.instant("SHARED_FOLDER_DELETE")).html();
                }
                if (share.sharedObjectType == 'PROJECT') {
                    sharedDeleteMessage = parsed.html($translate.instant("SHARED_PROJECT_DELETE")).html();
                }
                ShareService.deleteShare(share.id).then(
                    function (data) {
                        var index = vm.itemSharedPersons.indexOf(share);
                        vm.itemSharedPersons.splice(index, 1);
                        $rootScope.showSuccessMessage(sharedDeleteMessage);
                        loadItemSharedUsers();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $rootScope.$on('app.share.item', shareItem);
                $rootScope.$on('app.shareSelectedItems.item', shareSelectedObjects);
                loadGroups();
                if (vm.selectionType != 'itemSelection') {
                    loadUsers();
                }
                loadItemSharedUsers();
            })();
        }
    }
);