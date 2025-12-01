define(
    [
        'app/desktop/modules/shared/shared.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/shareService',
        'app/shared/services/core/programService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/objectFileService'
    ],

    function (module) {
        module.controller('ShareFilesFoldersController', ShareFilesFoldersController);

        function ShareFilesFoldersController($scope, $rootScope, $stateParams, $translate, LoginService, $timeout, ShareService, ProgramService, ProjectService, ObjectFileService) {

            var vm = this;
            vm.permissions = ['READ', 'WRITE'];
            var parsed = angular.element("<div></div>");
            vm.filesFolders = $scope.data.sharedFiles;
            vm.objectType = $scope.data.objectType;
            vm.objectId = $scope.data.objectId;
            vm.processing = false;
            vm.fileShareType = $scope.data.fileShareType;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            vm.shareObject = {
                shareId: null,
                parentObjectId: vm.objectId,
                parentObjectType: vm.objectType,
                objectId: null,
                shareType: "PERSON",
                sharedObjectType: null,
                sharedTo: null,
                permission: null,
                sharedToObjects: [],
                sharedBy: $rootScope.loginPersonDetails.person.id
            };


            vm.selectPermission = parsed.html($translate.instant("ITEM_SHARED_PERMISSION")).html();
            vm.selectGroupMsg = parsed.html($translate.instant("SELECT_GROUP_MSG")).html();
            vm.selectUserMsg = parsed.html($translate.instant("SELECT_USER_MSG")).html();
            vm.selectPersonType = parsed.html($translate.instant("SELECT_TYPE")).html();
            vm.selectUserType = parsed.html($translate.instant("SELECT_USER_TYPE")).html();
            vm.noExternalUsersMsg = parsed.html($translate.instant("EXTERNAL_USER_MSG")).html();
            var selectUserAndGroupValidation = parsed.html($translate.instant("SELECT_USER_GROUP")).html();
            var selectPermissionValidation = parsed.html($translate.instant("SELECT_PERMISSION")).html();

            vm.sharedToObjects = [];

            function shareSelectedObjects() {
                $rootScope.hideBusyIndicator($('#rightSidePanel'));
                if (vm.fileShareType == "PERSON") {
                    var promise = null;
                    if (validate()) {
                        vm.processing = true;

                        /*--------------  For Multiple Users -----------------------*/

                        if (vm.shareObject.shareType == "PERSON") {
                            angular.forEach(vm.sharedToObjects, function (login) {
                                vm.shareObject.sharedToObjects.push(login.person.id);
                            });
                        }
                        angular.forEach(vm.filesFolders, function (file) {
                            file.parentFolder = null;
                            file.folderChildren = [];
                        });
                        promise = ShareService.shareMultipleFileObjects(vm.filesFolders, vm.shareObject)
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    vm.shared = data;
                                    vm.shareObject = {
                                        shareId: null,
                                        parentObjectId: null,
                                        parentObjectType: null,
                                        objectId: null,
                                        shareType: null,
                                        permission: null,
                                        sharedObjectType: null,
                                        sharedTo: null,
                                        sharedToObjects: [],
                                        sharedBy: null
                                    };
                                    vm.sharedToObjects = [];
                                    $scope.callback();
                                    vm.processing = false;
                                    $rootScope.hideBusyIndicator($('#rightSidePanel'));
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage("Folders/Files shared successfully");
                                }, function (error) {
                                    vm.processing = false;
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator($('#rightSidePanel'));
                                }
                            )
                        }
                    }
                } else {
                    if (vm.selectedObjects.length == 0) {
                        $rootScope.showWarningMessage("Please select at least one project to share");
                    } else {
                        $rootScope.showBusyIndicator();
                        angular.forEach(vm.filesFolders, function (file) {
                            file.parentFolder = null;
                            file.folderChildren = [];
                        });
                        angular.forEach(vm.selectedObjects, function (object) {
                            object.parentObject = null;
                            object.programChildren = [];
                            object.children = [];
                        });
                        var objectFileDto = {
                            files: vm.filesFolders,
                            objects: vm.selectedObjects
                        };
                        ObjectFileService.shareObjectFilesToObjects(vm.objectId, vm.objectType, objectFileDto).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Files shared successfully");
                                $scope.callback(data);
                                $rootScope.hideSidePanel();
                                $rootScope.hideBusyIndicator();
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
                }
                else if (vm.shareObject.permission == null || vm.shareObject.permission == "" || vm.shareObject.permission == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectPermissionValidation);
                }

                return valid;
            }

            var map = new Map();
            vm.users = [];
            function loadUsers() {
                vm.users = [];
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (user) {
                            if (user.isActive == true && user.external) {
                                if (!map.has(user.person.id)) {
                                    vm.users.push(user);
                                }
                            }
                        })
                        $rootScope.hideBusyIndicator($('#rightSidePanel'));
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator($('#rightSidePanel'));
                    }
                )
            }

            function loadProjects() {
                if ($rootScope.projectInfo != null && $rootScope.projectInfo != undefined && $rootScope.projectInfo.program != null) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.programProjects = [];
                    ProgramService.getProgramProjectFolders($rootScope.projectInfo.program).then(
                        function (data) {
                            vm.programProjects = [];
                            vm.projectCount = 0;
                            angular.forEach(data, function (programProject) {
                                programProject.programChildren = [];
                                programProject.selected = false;
                                programProject.level = 0;
                                programProject.expanded = true;
                                programProject.parentObject = null;
                                vm.programProjects.push(programProject);
                                var index = vm.programProjects.indexOf(programProject);

                                angular.forEach(programProject.children, function (child) {
                                    index++;
                                    vm.projectCount++;
                                    child.selected = false;
                                    child.level = programProject.level + 1;
                                    child.expanded = false;
                                    child.programChildren = [];
                                    child.parentObject = programProject;
                                    programProject.programChildren.push(child);
                                    vm.programProjects.splice(index, 0, child);
                                })
                            })
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.programProjects = [];
                    ProjectService.getProgramNullProjects().then(
                        function (data) {
                            vm.programProjects = [];
                            angular.forEach(data, function (programProject) {
                                programProject.programChildren = [];
                                programProject.selected = false;
                                programProject.level = 0;
                                programProject.expanded = false;
                                programProject.parentObject = null;
                                vm.programProjects.push(programProject);
                            })
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.toggleNode = toggleNode;
            function toggleNode(programProject) {
                if (programProject.expanded == null || programProject.expanded == undefined) {
                    programProject.expanded = false;
                }
                programProject.expanded = !programProject.expanded;
                var index = vm.programProjects.indexOf(programProject);
                if (programProject.expanded == false) {
                    removeChildren(programProject)
                } else {
                    angular.forEach(programProject.children, function (child) {
                        index++;
                        child.level = programProject.level + 1;
                        child.expanded = false;
                        child.programChildren = [];
                        child.parentObject = programProject;
                        programProject.programChildren.push(child);
                        vm.programProjects.splice(index, 0, child);
                    })
                }
            }

            function populateChildren(programProject, lastIndex) {
                angular.forEach(programProject.children, function (item) {
                    lastIndex++;
                    item.expanded = true;
                    item.level = programProject.level + 1;
                    item.programChildren = [];
                    vm.programProjects.splice(lastIndex, 0, item);
                    programProject.programChildren.push(item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }

            function removeChildren(programProject) {
                if (programProject != null && programProject.programChildren != null && programProject.programChildren != undefined) {
                    angular.forEach(programProject.programChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.programProjects.indexOf(programProject);
                    vm.programProjects.splice(index + 1, programProject.programChildren.length);
                    programProject.programChildren = [];
                    programProject.expanded = false;

                }
            }

            vm.selectedObjects = [];
            vm.selectCheck = selectCheck;
            function selectCheck(object) {
                if (object.selected) {
                    vm.selectedObjects.push(object);
                } else {
                    vm.selectedObjects.splice(vm.selectedObjects.indexOf(object), 1);
                }

                /*var count = 0;
                 var totalCount = 0;
                 angular.forEach(vm.programProjects, function (project) {
                 totalCount++;
                 if (project.selected) {
                 count++;
                 }
                 angular.forEach(project.programChildren, function (child) {
                 totalCount++;
                 if (child.selected) {
                 count++;
                 }
                 });
                 })
                 if (count == totalCount - 1) {
                 vm.selectAllCheck = true;
                 } else {
                 vm.selectAllCheck = false;
                 }*/
            }

            vm.selectAllCheck = false;
            vm.selectAll = selectAll;
            function selectAll() {
                if (vm.selectAllCheck) {
                    angular.forEach(vm.programProjects, function (project) {
                        if (project.project != null && project.project != vm.objectId) {
                            project.selected = true;
                            vm.selectedObjects.push(project);
                        }
                    })
                } else {
                    angular.forEach(vm.programProjects, function (project) {
                        project.selected = false;
                    })
                    vm.selectedObjects = [];
                }
            }


            (function () {
                $rootScope.$on('app.shareSelected.files.folders', shareSelectedObjects);
                if (vm.fileShareType == "PERSON") {
                    loadUsers();
                } else {
                    vm.programProjects = [];
                    loadProjects();
                }
            })();
        }
    }
);