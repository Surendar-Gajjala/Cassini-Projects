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
        'app/shared/services/core/activityService'

    ],
    function (module) {
        module.controller('ExternalUserController', ExternalUserController);

        function ExternalUserController($scope, $rootScope, $sce, $translate, $cookieStore, $window, $timeout, $application, $state, $stateParams, $cookies, $uibModal,
                                        CommonService, PersonGroupService, ItemTypeService, ItemService, DialogService, ShareService, ProjectService, ActivityService) {

            var vm = this;
            vm.itemsearch = [];
            vm.advancedsearch = [];
            vm.selectedAttributes = [];
            vm.selectedObjectAttributes = [];
            vm.objectIds = [];
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
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
            //vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.sharedItemsNextPage = sharedItemsNextPage;
            vm.sharedItemsPreviousPage = sharedItemsPreviousPage;
            vm.flag = false;

            function resetPage() {
                vm.items = angular.copy(pagedResults);
                vm.pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;
                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                }
            }

            function sharedItemsNextPage() {
                if (vm.sharedObjects.last != true) {
                    $rootScope.showBusyIndicator();
                    vm.pageable.page++;
                    loadSharedItems();
                }
            }

            function sharedItemsPreviousPage() {
                if (vm.sharedObjects.first != true) {
                    $rootScope.showBusyIndicator();
                    vm.pageable.page--;
                    loadSharedItems();
                }
            }

            function clearFilter() {
                loadSharedItems();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

            function showItem(item) {
                $state.go('app.items.details', {
                    itemId: item.objectIdObject.latestRevision,
                    permission: item.permission
                });
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
                }
                $scope.$evalAsync();
            }

            function assignValues() {
                vm.sharedItems = [];
                CommonService.getPersonReferences(vm.sharedObjects.content, 'sharedTo');
                CommonService.getPersonReferences(vm.sharedObjects.content, 'sharedBy');
                ItemService.getItemReferences(vm.sharedObjects.content, 'objectId', "shared");
                angular.forEach(vm.sharedObjects.content, function (item) {
                    ItemService.getItem(item.objectId).then(
                        function (data) {
                            vm.item = data;
                            if (vm.item.thumbnail != null) {
                                item.thumbnailImage = "api/plm/items/" + item.objectId + "/itemImageAttribute/download?" + new Date().getTime();
                            }
                        })

                });

                vm.loading = false;
            }

            vm.sharedPerson = $rootScope.loginPersonDetails;
            vm.sharedItemIds = [];
            vm.personIds = [];

            function loadSharedItems() {
                var map = new Hashtable();
                vm.sharedItemView = true;
                ShareService.getGroupsByPerson(vm.sharedPerson.person.id).then(
                    function (data) {
                        vm.personIds.push(vm.sharedPerson.person.id);
                        if (data.length > 0) {
                            angular.forEach(data, function (groupMember) {
                                vm.personIds.push(groupMember);
                            })
                        }
                        ShareService.getItemsBySharedPersonId(vm.pageable, vm.personIds).then(
                            function (data) {
                                vm.sharedObjects = data;
                                assignValues();
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                )
            }

            vm.sharedItem = sharedItem;
            function sharedItem() {
                vm.sharedItemView = true;
                vm.sharedProjectView = false;
                vm.activityView = false;
                vm.taskView = false;
                /*loadSharedItems();*/
            }

            vm.sharedProject = sharedProject;

            function sharedProject() {
                vm.sharedProjectView = true;
                vm.sharedItemView = false;
                vm.activityView = false;
                vm.taskView = false;
                loadProjects();
            }

            vm.sharedActivity = sharedActivity;

            function sharedActivity() {
                vm.activityView = true;
                vm.sharedItemView = false;
                vm.sharedProjectView = false;
                vm.taskView = false;
                loadPersonActivitys();
            }

            vm.sharedTask = sharedTask;
            function sharedTask() {
                vm.taskView = true;
                vm.activityView = false;
                vm.sharedItemView = false;
                vm.sharedProjectView = false;
                loadPersonTasks();
            }

            vm.showProject = showProject;
            function showProject(project) {
                $state.go('app.pm.project.details', {
                    projectId: project.id
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
                    }
                )
            }

            //----------------------- Activity Details -------------------------- //
            function loadPersonActivitys() {
                ActivityService.getPersonActivitys(login.person.id, vm.pageable).then(
                    function (data) {
                        vm.personActivitys = data;
                    }
                )
            }

            vm.openActivityDetails = openActivityDetails;
            function openActivityDetails(activity) {
                /*$window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify(vm.projectPlanTabId));*/
                $state.go('app.pm.project.activity.details', {activityId: activity.id})
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
                    }
                )
            }

            vm.showTaskDetails = showTaskDetails;
            function showTaskDetails(task) {
                $state.go('app.pm.project.activity.task.details', {activityId: task.activity, taskId: task.id})
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
                            }
                        )
                    } else if (task.percentComplete == 100) {
                        task.status = "FINISHED";
                        ActivityService.updateActivityTask(task.activity, task).then(
                            function (data) {
                                task.editMode = false;
                                $rootScope.showSuccessMessage(taskUpdateMsg);
                            }
                        )
                    } else {
                        ActivityService.updateActivityTask(task.activity, task).then(
                            function (data) {
                                task.editMode = false;
                                $rootScope.showSuccessMessage(taskUpdateMsg);
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

            (function () {
                loadSharedItems();
                loadProjects();
                loadPersonActivitys();
                loadPersonTasks();
            })();
        }
    }
)
;