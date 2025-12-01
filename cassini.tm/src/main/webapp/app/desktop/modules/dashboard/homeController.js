define(
    [
        'app/desktop/modules/dashboard/home.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/desktop/modules/dashboard/homeDetailsController',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'

    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModal,
                                httpFactory, TaskService, ProjectService, CommonService) {
            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;

            vm.images = [
                'app/assets/images/bzarri/miscellaneous/miscellaneous-10.jpeg',
                'app/assets/images/bzarri/d-day/d-day-18.jpg',
                'app/assets/images/bzarri/miscellaneous/miscellaneous-7.jpeg',
                'app/assets/images/bzarri/drm-conference/drm-conference-1.jpg',
                'app/assets/images/bzarri/drm-conference/drm-conference-7.jpg',
                'app/assets/images/bzarri/drm-conference/drm-conference-29.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-159.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-142.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-158.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-74.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-91.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-22.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-51.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-60.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-108.jpg',
                'app/assets/images/bzarri/staff-conference/staff-conference-110.jpg'
            ];

            vm.news = [];

            //addImages('drm-conference', 56);
            //addImages('staff-conference', 169);

            vm.totalCounts = {
                total: 0,
                assigned: 0,
                verified: 0,
                approved: 0,
                pending: 0
            };

            vm.chunks = [];
            vm.statusMap = {
                all: [],
                assigned: [],
                verified: [],
                approved: [],
                pending: []
            };
            vm.show = 'all';

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
                                        assigned: 0,
                                        pending: 0,
                                        verified: 0,
                                        approved: 0
                                    }
                                };
                                map.put(stat[0], location);
                                vm.statusMap.all.push(location);
                            }

                            var status = stat[1];

                            if(status == 'ASSIGNED') {
                                location.counts.assigned += stat[2];
                                vm.totalCounts.assigned += stat[2];
                                vm.statusMap.assigned.push(location);
                            }
                            else if(status == 'VERIFIED') {
                                location.counts.verified += stat[2];
                                vm.totalCounts.verified += stat[2];
                                vm.statusMap.verified.push(location);
                            }
                            else if(status == 'APPROVED') {
                                location.counts.approved += stat[2];
                                vm.totalCounts.approved += stat[2];
                                vm.statusMap.approved.push(location);
                            }
                            else if(status == 'FINISHEDPENDING' ||
                                    status == 'VERIFIEDPENDING' ||
                                    status == 'APPROVEDPENDING') {
                                location.counts.pending += stat[2];
                                vm.totalCounts.pending += stat[2];
                                vm.statusMap.pending.push(location);
                            }

                            vm.totalCounts.total += stat[2];

                        });


                        vm.chunks = vm.statusMap.all.chunk(1);
                    }
                )
            }

            function showTasksByStatus(status) {
                vm.show = status;
                vm.chunks = vm.statusMap[status].chunk(1);
            }

            function showTasks(location, statuses, status) {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/dashboard/homeDetailsDialog.jsp',
                    controller: 'HomeDetailsController as homeDetailsVm',
                    size: 'md',
                    resolve: {
                        location: function() {
                            return location;
                        },
                        statuses: function() {
                            return statuses;
                        },
                        statusLabel: function() {
                            return status;
                        }
                    }
                });
                modalInstance.result.then(
                    function (result) {
                    }
                );
            }

            function loadNews() {
                var url = 'news.json?bust=' + (new Date()).getTime();
                httpFactory.get(url).then(
                    function(data) {
                        vm.news = data;
                    }
                )
            }


            $scope.$on('$viewContentLoaded', function(){
                $timeout(function() {
                    $application.homeLoaded = true;
                    window.$("#preloader").hide();
                    window.$("#appview").show();

                    var imgSlider = new SimpleSlider( document.getElementById('myslider') );

                    loadTaskStats();
                    loadNews();
                }, 1000);
            });
        }
    }
);