define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/wbsService',
        'app/desktop/modules/tm/all/wbsTreeSelectDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/pm/project/projectSiteService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/itemService',
        'app/shared/services/issue/issueService'
    ],
    function (module) {
        module.controller('TasksController', TasksController);

        function TasksController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                 TaskService, WbsService, DialogService, CommonService, ProjectSiteService, ProjectService, $window,
                                 ObjectTypeAttributeService, AttributeAttachmentService, ItemService, IssueService) {
            $rootScope.viewInfo.icon = "fa flaticon-deadlines";
            $rootScope.viewInfo.title = "Tasks";

            var vm = this;

            vm.loginPerson = window.$application.login.person;
            vm.login = window.$application.login;
            vm.loading = true;
            vm.tasks = [];
            vm.persons = [];
            vm.projectPersons = [];
            vm.taskAttributes = [];
            vm.sites = [];
            vm.wbsItems = [];
            vm.objectIds = [];
            vm.requiredTaskAttributes = [];
            var currencyMap = new Hashtable();
            vm.flag = null;
            vm.clear = false;
            vm.newTaskButton = true;
            vm.myTask = false;

            $scope.plannedStartDate = null;
            $scope.plannedFinishDate = null;
            $scope.ActualStartDate = null;
            $scope.ActualFinishDate = null;
            $scope.inspectedOn = null;
            $scope.freeTextQuery = null;
            $rootScope.showTaskNotification = true;

            vm.newTask = newTask;
            vm.applyFilters = applyFilters;
            vm.finishTask = finishTask;
            vm.deleteProjectTask = deleteProjectTask;
            vm.freeTextSearch = freeTextSearch;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.resetFilters = resetFilters;
            vm.sortValues = sortValues;
            vm.loadTasksByLogin = loadTasksByLogin;
            vm.showTaskAttributes = showTaskAttributes;
            vm.showTaskDetails = showTaskDetails;
            vm.removeAttribute = removeAttribute;
            vm.openAttachment = openAttachment;
            vm.showImage = showImage;
            vm.showRequiredImage = showRequiredImage;
            vm.assignPerson = assignPerson;

            vm.listStatus = ['ASSIGNED', 'INPROGRESS', 'FINISHED'];
            vm.inspectionResults = ['ACCEPTED', 'REJECTED'];
            vm.flags = ['YES', 'NO'];

            var pageable = {
                page: $rootScope.taskPage,
                size: 20,
                sort: {
                    field: "modifiedDate"

                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            //$rootScope.tasks = pagedResults;
            vm.tasks = pagedResults;

            vm.emptyFilters = {
                name: null,
                description: null,
                site: null,
                person: "",
                wbsItem: null,
                status: null,
                percentComplete: null,
                siteObject: null,
                personObject: null,
                wbsItemObject: null,
                percentCompleteObject: null,
                plannedStartDate: null,
                plannedFinishDate: null,
                actualStartDate: null,
                actualFinishDate: null,
                searchQuery: null,
                delayTask: false,
                project: $stateParams.projectId,
                inspectedByPerson: null,
                inspectionResult: null,
                inspectedOn: null,
                subContract: null,
                inspectedBy: ''
            };

            vm.filters = angular.copy(vm.emptyFilters);

            function resetFilters() {
                vm.filters = angular.copy(vm.emptyFilters);
                vm.flag = null;
                $scope.plannedStartDate = null;
                $scope.plannedFinishDate = null;
                $scope.actualStartDate = null;
                $scope.actualFinishDate = null;
                pageable.page = 0;
                $rootScope.taskPage = 0;
                $("#tasks").prop('checked', true);
                loadTasks();
            }

            function previousPage() {
                if (vm.tasks.first != true) {
                    pageable.page--;
                    $rootScope.taskPage--;
                    loadTasks();

                }
            }

            function nextPage() {
                if (vm.tasks.last != true) {
                    pageable.page++;
                    $rootScope.taskPage++;
                    loadTasks();
                }
            }

            function resetPage() {
                pageable.page = 0;
                $rootScope.taskPage = 0
            }

            function applyFilters() {
                pageable.page = 0;
                $rootScope.taskPage = 0;
                loadTasks();
            }

            $scope.$watch('plannedStartDate', function (date) {
                vm.filters.plannedStartDate = date;
                if ($scope.plannedStartDate != null) {
                    vm.applyFilters();
                }
            });

            $scope.$watch('plannedFinishDate', function (date) {
                vm.filters.plannedFinishDate = date;
                if ($scope.plannedFinishDate != null) {
                    vm.applyFilters();
                }
            });

            $scope.$watch('actualStartDate', function (date) {
                vm.filters.actualStartDate = date;
                if ($scope.actualStartDate != null) {
                    vm.applyFilters();
                }
            });

            $scope.$watch('actualFinishDate', function (date) {
                vm.filters.actualFinishDate = date;
                if ($scope.actualFinishDate != null) {
                    vm.applyFilters();
                }
            });

            $scope.$watch('inspectedOn', function (date) {
                vm.filters.inspectedOn = date;
                if ($scope.inspectedOn != null) {
                    vm.applyFilters();
                }
            });

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    TaskService.freeTextSearch($stateParams.projectId, pageable, vm.filters).then(
                        function (data) {
                            vm.tasks = data;
                            $rootScope.setValues(vm.tasks);

                        }
                    );
                    vm.clear = true;

                } else {
                    resetPage();
                    loadTasks();
                }
            }

            function newTask() {
                var options = {
                    title: 'New Task',
                    showMask: true,
                    template: 'app/desktop/modules/tm/new/newTaskView.jsp',
                    controller: 'NewTaskController as newTaskVm',
                    resolve: 'app/desktop/modules/tm/new/newTaskController',
                    width: 600,
                    data: {projectTask: true},
                    buttons: [
                        {text: 'Create', broadcast: 'app.task.new'}
                    ],
                    callback: function () {
                        loadTasks();
                        loadProjectPersons();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function loadTasks() {
                vm.tasks = pagedResults;
                vm.loading = true;
                vm.filters.site = vm.filters.siteObject != null || undefined ? vm.filters.siteObject.id : null;
                vm.filters.wbsItem = vm.filters.wbsItemObject != null || undefined ? vm.filters.wbsItemObject.id : null;
                vm.filters.person = vm.filters.personObject != null || undefined ? vm.filters.personObject.id : "";
                vm.filters.inspectedBy = vm.filters.inspectedByPerson != null || undefined ? vm.filters.inspectedByPerson.id : "";
                if (vm.filters.subContract != null) {
                    vm.filters.subContract = vm.filters.subContract == 'YES' ? true : false;
                    vm.flag = vm.filters.subContract == true ? 'YES' : 'NO';
                }

                TaskService.getProjectTasks($stateParams.projectId, vm.filters, pageable).then(
                    function (data) {
                        vm.tasks = data;
                        vm.loading = false;
                        $rootScope.setValues(vm.tasks);
                        loadTaskAttributeValues();
                        angular.forEach(vm.tasks.content, function (task) {
                            task.subContract = task.subContract == true ? 'YES' : 'NO';
                            task.hasProblems = false;
                            loadTaskProblems(task);
                        })
                    }
                );
            }

            function showTaskDetails(task) {
                $rootScope.taskId = task.id;
                $state.go('app.pm.project.taskdetails', {taskId: task.id});
            }

            $rootScope.setValues = function (tasks) {
                TaskService.getSiteReferences(tasks.content, 'site');
                CommonService.getPersonReferences(tasks.content, 'person');
                CommonService.getPersonReferences(tasks.content, 'inspectedBy');
                WbsService.getMultipleWbsWithTasks($stateParams.projectId, tasks.content, 'wbsItem');
            };

            function loadTasksByLogin() {
                vm.tasks = pagedResults;
                vm.loading = true;
                vm.filters.person = vm.loginPerson.id;
                TaskService.getProjectTasks($stateParams.projectId, vm.filters, pageable).then(
                    function (data) {
                        vm.tasks = data;
                        vm.loading = false;
                        $rootScope.setValues(vm.tasks);
                        loadTaskAttributeValues();
                        angular.forEach(vm.tasks.content, function (task) {
                            task.subContract = task.subContract == true ? 'YES' : 'NO';
                            task.hasProblems = false;
                            loadTaskProblems(task);
                        })
                    }
                );
            }

            function updateTask(task) {
                TaskService.updateProjectTask($stateParams.projectId, task).then(
                    function (data) {
                        task.percentComplete = data.percentComplete;
                        task.actualFinishDate = data.actualFinishDate;
                    }
                )
            }

            function finishTask(task) {
                if (task.totalUnitsCompleted == task.totalUnits) {
                    task.status = 'FINISHED';
                    if (task.person == $application.login.person.id) {
                        $rootScope.$broadcast("mytasks.decrement.finished");
                    }
                    task.actualFinishDate = moment(new Date()).format("DD/MM/YYYY");
                    TaskService.updateProjectTask($stateParams.projectId, task).then(
                        function (data) {
                            updateTask(task);
                            $rootScope.showSuccessMessage("Task finished successfully");
                        }
                    )
                }
                else {
                    $rootScope.showErrorMessage("Total Units of Work must be finished to finish this Task");
                }
            }

            function loadTaskProblems(task) {
                IssueService.getIssues("TASK", task.id).then(
                    function (data) {
                        angular.forEach(data, function (problem) {
                            if (problem.status != 'CLOSED') {
                                task.hasProblems = true;
                                return;
                            }
                        });
                    });
            }

            function deleteProjectTask(task) {
                var options = {
                    title: 'Delete Task',
                    message: 'Are you sure you want to delete this (' + task.name + ') Task?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TaskService.deleteProjectTask($stateParams.projectId, task.id).then(
                            function (data) {
                                var index = vm.tasks.content.indexOf(task);
                                vm.tasks.content.splice(index, 1);
                                $rootScope.showSuccessMessage(task.name + ": Task deleted successfully");
                                loadTasks();
                            })
                    }
                });
            }

            function loadProjectPersons() {
                vm.persons = [];
                ProjectService.getProjectPersons($stateParams.projectId).then(
                    function (data) {
                        vm.projectPersons = data;
                        loadProjectSites();
                        loadWbsItems();
                        angular.forEach(vm.projectPersons, function (obj) {
                            loadPersonsById(obj.person);
                        })
                    }
                );
            }

            function loadProjectSites() {
                ProjectSiteService.getPagedSitesByProject($stateParams.projectId, pageable).then(
                    function (data) {
                        vm.sites = data.content;
                    });
            }

            function loadWbsItems() {
                WbsService.getWbsByProject($stateParams.projectId, pageable).then(
                    function (data) {
                        vm.wbsItems = data.content;
                    });
            }

            function loadPersonsById(person) {
                vm.loading = false;
                CommonService.getPerson(person).then(
                    function (data) {
                        vm.persons.push(data);
                    }
                )
            }

            function sortValues(values) {
                values.sort(compare);
            }

            function showTaskAttributes() {
                var options = {
                    title: 'Task Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.taskAttributes,
                        attributesMode: 'TASK'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.taskAttributes = result;
                        $window.localStorage.setItem("taskAttributes", JSON.stringify(vm.taskAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadTasks();
                        loadProjectPersons();
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("taskAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function showImage(attribute) {
                var modal = document.getElementById('taskModal');
                var modalImg = document.getElementById('taskImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showRequiredImage(attribute) {
                var modal = document.getElementById('taskModal1');
                var modalImg = document.getElementById('taskImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function removeAttribute(att) {
                vm.taskAttributes.remove(att);
                $window.localStorage.setItem("taskAttributes", JSON.stringify(vm.taskAttributes));
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function loadRequiredTaskAttributes() {
                TaskService.getRequiredTaskAttributes($stateParams.projectId, "TASK").then(
                    function (data) {
                        vm.requiredTaskAttributes = data;
                        //loadTasks();
                    }
                )
            }

            function loadTaskAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach(vm.tasks.content, function (item) {
                    item.refValueString = null;
                    vm.itemIds.push(item.id);
                });
                angular.forEach(vm.taskAttributes, function (taskAttribute) {
                    if (taskAttribute.id != null && taskAttribute.id != "" && taskAttribute.id != 0) {
                        vm.attributeIds.push(taskAttribute.id);
                    }
                });

                angular.forEach(vm.requiredTaskAttributes, function (taskAttribute) {
                    if (taskAttribute.id != null && taskAttribute.id != "" && taskAttribute.id != 0) {
                        vm.requiredAttributeIds.push(taskAttribute.id);
                    }
                });

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.taskAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.tasks.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.selectedObjectAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.taskAttachments = data;
                                                        item[attributeName] = vm.taskAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }

                if (vm.itemIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.requiredAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.requiredTaskAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.tasks.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.requiredAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.requiredMaterialAttachments = data;
                                                        item[attributeName] = vm.requiredMaterialAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }

            }

            function assignPerson(task) {
                task.subContract = task.subContract == 'YES' ? true : false;
                task.person = task.personObject.id;
                TaskService.updateProjectTask($stateParams.projectId, task).then(
                    function (data) {
                        task = data;
                        task.status = data.status;
                        task.subContract = data.subContract == true ? 'YES' : 'NO';
                        $rootScope.showSuccessMessage("Person assigned successfully");
                    }
                );
            }

            vm.updateSite = updateSite;
            function updateSite(task) {
                task.subContract = task.subContract == 'YES' ? true : false;
                task.site = task.siteObject.id;
                TaskService.updateProjectTask($stateParams.projectId, task).then(
                    function (data) {
                        task = data;
                        task.status = data.status;
                        task.subContract = data.subContract == true ? 'YES' : 'NO';
                        $rootScope.showSuccessMessage("Site updated successfully");
                    }
                );
            }

            vm.onSelectWbsTree = onSelectWbsTree;

            function onSelectWbsTree(node) {
                var data = node.attributes.wbs.children;
                if (data.length == 0 || data.length == null || data.length == "") {
                    $rootScope.closeNotification();
                    vm.wbs = node.attributes.wbs;
                    vm.filters.wbsItemObject = vm.wbs;
                    window.$("body").trigger("click");
                }
                /*else if (data.length != 0 && data.length != null && data.length != "") {
                 $rootScope.showWarningMessage("Please click Children Node");
                 }*/
            }

            vm.importTask = importTask;
            function importTask() {
                var options = {
                    title: 'Import Tasks',
                    template: 'app/desktop/modules/tm/all/taskImportView.jsp',
                    controller: 'TaskImportController as taskImportVm',
                    resolve: 'app/desktop/modules/tm/all/taskImportController',
                    width: 600,
                    buttons: [
                        {text: 'Create', broadcast: 'app.project.copy.task'}
                    ],
                    callback: function (data) {
                        $rootScope.hideSidePanel();
                        loadTasks();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadRequiredTaskAttributes();
                    loadProjectPersons();
                    $scope.$on('app.project.tasks', loadTasks);
                    var tasks = $rootScope.hasPermission('permission.tasks.all');
                    if (tasks) {
                        document.getElementById("tasks").checked = true;
                        vm.filters.delayTask = false;
                        loadTasks();
                    } else {
                        document.getElementById("mytasks").checked = true;
                        vm.filters.delayTask = false;
                        loadTasksByLogin();
                    }

                    $('#myForm input[type=radio]').change(function () {
                        if (this.value === "tasks" || this.value == undefined) {
                            vm.filters.delayTask = false;
                            loadTasks();
                        }
                        else if (this.value === "myTasks") {
                            vm.filters.delayTask = false;
                            loadTasksByLogin();
                        } else if (this.value === "delayTask") {
                            vm.filters.delayTask = true;
                            vm.filters.person = '';
                            if (vm.login.isSuperUser) {
                                loadTasks();
                            } else {
                                loadTasksByLogin();
                            }
                        }
                    });
                    if ($scope.$parent.project.actualFinishDate != null) {
                        vm.newTaskButton = false;
                    }
                }
            })();
        }
    }
)
;