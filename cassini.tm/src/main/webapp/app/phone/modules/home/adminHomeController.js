define(
    [
        'app/phone/modules/home/home.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/phone/directives/mobileDirectives'

    ],
    function (module) {
        module.controller('AdminHomeController', AdminHomeController);

        function AdminHomeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                TaskService, ProjectService, CommonService) {

            var vm = this;

            vm.totalCounts = {
                all: 0,
                completed: 0,
                pending: 0
            };

            vm.statusMap = {
                all: [],
                completed: [],
                pending: []
            };
            vm.show = 'all';
            vm.locations = [];

            vm.showTasksByStatus = showTasksByStatus;
            vm.showTasks = showTasks;


            function loadTaskStats() {
                TaskService.getAllTaskStats(1).then(
                    function(stats) {
                        var map = new Hashtable();
                        angular.forEach(stats, function(stat) {
                            var location = map.get(stat[0]);
                            if(location == null) {
                                location = {
                                    name: stat[0],
                                    counts: {
                                        completed: 0,
                                        pending: 0
                                    }
                                };
                                map.put(stat[0], location);
                                vm.statusMap.all.push(location);
                            }

                            var status = stat[1];

                            if(status == 'APPROVED') {
                                location.counts.completed += stat[2];
                                vm.totalCounts.completed += stat[2];
                                vm.statusMap.completed.push(location);
                            }
                            else{
                                location.counts.pending += stat[2];
                                vm.totalCounts.pending += stat[2];
                                vm.statusMap.pending.push(location);
                            }

                            vm.totalCounts.all += stat[2];

                        });


                        vm.locations = vm.statusMap.all;
                    }
                )
            }

            function showTasksByStatus(status) {
                vm.show = status;
                vm.locations = vm.statusMap[status];
            }

            function showTasks(location, statuses, status) {

            }

            (function() {
                loadTaskStats();
            })();
        }
    }
);