define(
    [
        'app/phone/modules/home/home.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/phone/modules/home/adminHomeController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/phone/directives/mobileDirectives'
    ],
    function(module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                TaskService, ProjectService, CommonService) {

            $rootScope.viewName = "Home";
            $rootScope.backgroundColor = "#1565c0";

            var vm = this;

            vm.loading = true;
            vm.tasks = [];
            vm.completedTasks = [];
            vm.pendingTasks = [];
            vm.isAdmin = false;

            vm.nextDay = nextDay;
            vm.previousDay = previousDay;
            vm.finishTask = finishTask;
            vm.pendingTask = pendingTask;
            vm.verifyTask = verifyTask;
            vm.approveTask = approveTask;
            vm.showTaskDetails = showTaskDetails;
            vm.showDailyTasks = showDailyTasks;
            vm.showCompletedTasks = showCompletedTasks;
            vm.showPendingTasks = showPendingTasks;

            vm.loginPerson = null;

            var currentDate = moment();
            vm.currentDateLabel = "Today";
            vm.viewName = "daily";

            var pageable = {
                page: 0,
                size: 100,
                sort: {
                    field: "modifiedDate"
                }
            };

            var filters = {
                project: null,
                name: null,
                description: null,
                status: null,
                assignedTo: $application.login.person.id,
                verifiedBy: null,
                approvedBy: null,
                searchQuery: null,
                assignedDate: currentDate.format("DD/MM/YYYY")
            };

            function nextDay() {
                currentDate.add(1, 'days');
                update();
            }

            function previousDay() {
                currentDate.subtract(1, 'days');
                update();
            }

            function update() {
                if(currentDate.isSame(moment(), 'day')) {
                    vm.currentDateLabel = "Today";
                }
                else {
                    vm.currentDateLabel = currentDate.format("DD/MM/YYYY");
                }

                filters.assignedDate = currentDate.format("DD/MM/YYYY");

                vm.tasks = [];
                loadTasks();
            }

            function pendingTask(task) {
                task.status = "PENDING";
                TaskService.updateProjectTask(task.project, task).then(
                    function (data) {

                    }
                )
            }

            function finishTask(task) {
                task.status = "FINISHED";
                TaskService.updateProjectTask(task.project, task).then(
                    function (data) {

                    }
                )
            }

            function verifyTask(task) {
                task.status = "VERIFIED";
                TaskService.updateProjectTask(task.project, task).then(
                    function (data) {

                    }
                )
            }

            function approveTask(task) {
                task.status = "APPROVED";
                TaskService.updateProjectTask(task.project, task).then(
                    function (data) {

                    }
                )
            }

            function loadTasksByStatus(status) {
                var flters = angular.copy(filters);
                flters.assignedTo = $application.login.person.id;
                flters.verifiedBy = null;
                flters.approvedBy = null;
                flters.assignedDate = null;
                flters.status = status;

                var map = new Hashtable();
                vm.loading = true;

                TaskService.getTasks(flters, pageable).then(
                    function (data) {
                        angular.forEach(data.content, function(task) {
                            map.put(task.id, task);
                            if(status != 'APPROVED') {
                                vm.pendingTasks.push(task);
                            }
                            else if(status == 'APPROVED') {
                                vm.completedTasks.push(task);
                            }
                        });

                        flters.assignedTo = null;
                        flters.verifiedBy = $application.login.person.id;
                        return TaskService.getTasks(flters, pageable);
                    }
                ).then (
                    function(data) {
                        angular.forEach(data.content, function(task) {
                            if(map.get(task.id) == null) {
                                map.put(task.id, task);
                                if(status != 'APPROVED') {
                                    vm.pendingTasks.push(task);
                                }
                                else if(status == 'APPROVED') {
                                    vm.completedTasks.push(task);
                                }
                            }
                        });
                        flters.assignedTo = null;
                        flters.verifiedBy = null;
                        flters.approvedBy = $application.login.person.id;
                        return TaskService.getTasks(flters, pageable);
                    }
                ).then (
                    function(data) {
                        angular.forEach(data.content, function(task) {
                            if(map.get(task.id) == null) {
                                map.put(task.id, task);
                                if(status != 'APPROVED') {
                                    vm.pendingTasks.push(task);
                                }
                                else if(status == 'APPROVED') {
                                    vm.completedTasks.push(task);
                                }
                            }
                        });

                        var tasks = [];
                        if(status != 'APPROVED') {
                            tasks = vm.pendingTasks;
                        }
                        else if(status == 'APPROVED') {
                            tasks = vm.completedTasks;
                        }

                        CommonService.getPersonReferences(tasks, 'assignedTo');
                        CommonService.getPersonReferences(tasks, 'verifiedBy');
                        CommonService.getPersonReferences(tasks, 'approvedBy');
                        vm.loading = false;
                    }
                );
            }

            function loadTasks() {
                vm.loading = true;
                filters.assignedTo = $application.login.person.id;
                filters.verifiedBy = null;
                filters.approvedBy = null;

                var map = new Hashtable();

                TaskService.getTasks(filters, pageable).then(
                    function (data) {
                        angular.forEach(data.content, function(task) {
                            map.put(task.id, task);
                            vm.tasks.push(task);
                        });

                        filters.assignedTo = null;
                        filters.verifiedBy = $application.login.person.id;
                        return TaskService.getTasks(filters, pageable);
                    }
                ).then (
                    function(data) {
                        angular.forEach(data.content, function(task) {
                            if(map.get(task.id) == null) {
                                map.put(task.id, task);
                                vm.tasks.push(task);
                            }
                        });
                        filters.assignedTo = null;
                        filters.verifiedBy = null;
                        filters.approvedBy = $application.login.person.id;
                        return TaskService.getTasks(filters, pageable);
                    }
                ).then (
                    function(data) {
                        angular.forEach(data.content, function(task) {
                            if(map.get(task.id) == null) {
                                map.put(task.id, task);
                                vm.tasks.push(task);
                            }
                        });
                        CommonService.getPersonReferences(vm.tasks, 'assignedTo');
                        CommonService.getPersonReferences(vm.tasks, 'verifiedBy');
                        CommonService.getPersonReferences(vm.tasks, 'approvedBy');

                        angular.forEach(vm.tasks, function (task) {
                            ProjectService.getProjectById(task.project).then(function (data) {
                                task.project = data.id;
                            })
                        });
                        vm.loading = false;
                        vm.loginPerson = $application.login.person.id;
                    }
                );
            }

            function showTaskDetails(task) {
                $state.go('app.task.details', {taskId: task.id, projectId: task.project});
            }

            function showCompletedTasks() {
                vm.viewName = "completed";
                vm.completedTasks = [];
                loadTasksByStatus('APPROVED');
            }

            function showPendingTasks() {
                vm.viewName = "pending";
                vm.pendingTasks = [];
                loadTasksByStatus('PENDING')
            }

            function showDailyTasks() {
                vm.viewName = "daily";
            }

            (function() {
                $scope.$on('$viewContentLoaded', function(){
                    $timeout(function() {
                        $application.homeLoaded = true;
                        window.$("#appview").show();

                        if($application.login.loginName == 'guest' ||
                            $application.login.loginName == 'admin') {
                            vm.isAdmin = true;
                        }
                        else {
                            loadTasks();
                        }
                    }, 1000);
                });
            })();
        }
    }
);