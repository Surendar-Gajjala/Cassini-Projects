define(['app/desktop/modules/layout/layout.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/layoutService',
        'app/shared/services/projectService'
    ],
    function (module) {
        module.controller('LayoutController', LayoutController);

        function LayoutController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, AttachmentService,
                                  LayoutService, ProjectService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-files-o";
            $rootScope.viewInfo.title = "Layout";

            vm.currentMonthDays = [];
            vm.layoutDrawings = [];
            vm.attachments = null;
            vm.today = new Date();
            vm.layoutDrawing = {
                project: null
            };
            vm.projects = null;
            vm.day = vm.today.getDate();
            vm.month = vm.today.getMonth();
            vm.currentMonth = vm.today.getMonth() + 1;
            vm.year = vm.today.getFullYear();
            vm.currentYear = vm.today.getFullYear();
            vm.previousMonth = previousMonth;
            vm.downloadFile = downloadFile;
            vm.nextMonth = nextMonth;
            vm.project = null;
            vm.layoutProject = layoutProject;
            vm.modifiedDate = null;
            $scope.addFile = addFile;
            var monthNames = ["January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            ];

            function layoutProject(project) {
                vm.project = project;
            }

            function previousMonth() {
                vm.currentMonthDays = [];
                if (vm.month == 0) {
                    vm.year -= 1;
                    vm.modifiedDate = new Date(vm.year, 11, 1);
                } else {
                    vm.month -= 1;
                    vm.modifiedDate = new Date(vm.year, vm.month, 1);
                }
                vm.month = vm.modifiedDate.getMonth();
                vm.year = vm.modifiedDate.getFullYear();
                loadDays();
            }

            function nextMonth() {
                vm.currentMonthDays = [];
                if (vm.month == 11) {
                    vm.year += 1;
                    vm.modifiedDate = new Date(vm.year, 0, 1);
                } else {
                    vm.month += 1;
                    vm.modifiedDate = new Date(vm.year, vm.month, 1);
                }
                vm.month = vm.modifiedDate.getMonth();
                vm.year = vm.modifiedDate.getFullYear();
                loadDays();
            }

            function daysInMonth(month, year) {
                return new Date(year, month, 0).getDate();
            }

            function loadDays() {
                var days = daysInMonth(vm.month + 1, vm.year);
                for (var i = 1; i <= days; i++) {
                    vm.currentMonthDays.push(i);
                }
                vm.monthName = monthNames[vm.month];
            }

            function addFile(files) {

                vm.layoutDrawing.project = vm.project.id;
                LayoutService.createLayoutDrawing(vm.layoutDrawing).then(
                    function (data) {
                        vm.layoutDrawing = data;
                        AttachmentService.saveAttachment(vm.layoutDrawing.id, vm.layoutDrawing.objectType, files[0]).then(
                            function (data) {

                            })
                    })
            }

            function loadProjects() {
                ProjectService.getProjects().then(
                    function (data) {
                        vm.projects = data;
                    });
            }

            function downloadFile(day) {
                var date = moment(new Date(vm.year, vm.month, day)).format('YYYY-MM-DD');
                LayoutService.findLayoutByDate(date).then(
                    function (data) {
                        vm.layoutDrawings = data;
                        if (vm.layoutDrawings.length > 0) {
                            AttachmentService.getAttachments(vm.layoutDrawings[0].objectType, vm.layoutDrawings[0].id).then(
                                function (data) {
                                    vm.attachments = data;
                                    var url = "api/col/attachments/" + vm.attachments[0].id + "/download";
                                    var winProps = 'scrollbars=yes,menubar=no,width=500, resizable=yes,toolbar=no,location=no,status=no';
                                    window.open(url, '', winProps);
                                })
                        }
                        else {
                            $rootScope.showErrorMessage("Layout Drawings are not available");
                        }
                    })
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDays();
                    loadProjects();
                }
            })();
        }
    }
)
;