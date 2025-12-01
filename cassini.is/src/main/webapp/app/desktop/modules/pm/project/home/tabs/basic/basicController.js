define(
    [
        'app/desktop/modules/pm/pm.module',
        'apexcharts',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/mailServerService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController',
        'app/shared/services/issue/issueService',
        'app/shared/services/tm/taskService'
    ],
    function (module, ApexCharts) {
        module.controller('BasicController', BasicController);

        function BasicController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, ProjectService,
                                 CommonService, ItemService, MailServerService, DialogService, IssueService, TaskService) {
            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Basic Details";

            var vm = this;
            var lastSelectedItem = null;
            vm.loading = true;
            vm.projectId = $stateParams.projectId;
            vm.project = null;
            vm.mailServerObject = null;
            vm.update = update;
            vm.updateObjectMail = updateObjectMail;
            vm.updateStartDate = updateStartDate;
            vm.updateFinishDate = updateFinishDate;
            vm.showDetails = showDetails;
            vm.mailServerChanged = mailServerChanged;
            vm.showReceiverPassword = showReceiverPassword;
            vm.showSenderPassword = showSenderPassword;
            vm.status = ["NEW", "ASSIGNED", "INPROGRESS", "CLOSED"];
            var projectMap = new Hashtable();
            vm.persons = [];
            vm.updateProject = updateProject;
            vm.viewDetails = false;
            $rootScope.showUpdateButton = false;
            $rootScope.saveAs = saveAs;
            vm.piechart1 = false;
            vm.piechart1 = false;
            vm.barChart2 = false;
            var barChartLabels_1 = [[0, 'Images'], [1, 'Videos'], [2, 'Files']];
            var barChartLabels_2 = [[0, 'New'], [1, 'Assigned'], [2, 'Inprogresses'], [3, 'Finished']];
            $scope.pieDataset = [];
            $scope.pieOptions = {
                series: {
                    pie: {
                        show: true,
                        radius: 1,
                        label: {
                            show: true,
                            radius: 3 / 4,
                            formatter: labelFormatter,
                            background: {
                                opacity: 0.5
                            }
                        }
                    }
                },
                legend: {
                    show: false
                }
            };
            $scope.mediaOptions = {
                series: {
                    bars: {
                        show: true,
                        barWidth: 0.2,
                        align: 'center',
                        horizontal: true
                    }
                },
                yaxis: {
                    axisLabel: 'X',
                    mode: 'categories',
                    ticks: barChartLabels_1
                },
                colors: ['#2a6fa8']
            };
            $scope.taskOptions = {
                series: {
                    bars: {
                        show: true,
                        barWidth: 0.2,
                        align: 'center',
                        horizontal: true
                    }
                },
                yaxis: {
                    axisLabel: 'X',
                    mode: 'categories',
                    ticks: barChartLabels_2
                },
                colors: ['#2a6fa8']
            };

            $scope.opened = {};

            function labelFormatter(label, series) {
                return "<div style='font-size:8pt; text-align:center; padding:2px; color:white;'>" + label + "<br/>" + series.data[0][1] + "</div>";
            }

            function showDetails() {
                vm.viewDetails = !vm.viewDetails;
                $rootScope.showUpdateButton = !$rootScope.showUpdateButton;
            }

            $scope.open = function ($event, elementOpened) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened[elementOpened] = !$scope.opened[elementOpened];
            };

            function loadMailServerSettings() {
                MailServerService.getObjectMailSetting($stateParams.projectId).then(
                    function (data) {
                        vm.mailServerObject = data;
                        vm.mailServerObject.mailServerObj = null;
                        vm.mailServerObject.recPassword = "";
                        vm.mailServerObject.senPassword = "";

                        if (vm.mailServerObject.receiverPassword != null) {
                            for (var i = 0; i < vm.mailServerObject.receiverPassword.length; i++) {
                                vm.mailServerObject.recPassword += "*";
                            }
                        }
                        if (vm.mailServerObject.senderPassword != null) {
                            for (var j = 0; j < vm.mailServerObject.senderPassword.length; j++) {
                                vm.mailServerObject.senPassword += "*";
                            }
                        }
                        if (vm.mailServerObject.mailServer != null) {
                            MailServerService.getMailServer(vm.mailServerObject.mailServer).then(
                                function (data) {
                                    vm.mailServerObject.mailServerObj = data;
                                }
                            )
                        }
                        if ($rootScope.selectedProject != null) {
                            $rootScope.selectedProject.mailServerObject = vm.mailServerObject;
                        }
                    }
                )
            }

            function mailServerChanged(mailServerObject) {
                vm.mailServerObject.mailServerObj = null;
                vm.mailServerObject.mailServerObj = mailServerObject;
            }

            function validateObjectSettings() {
                var flag = true;
                if (vm.mailServerObject.mailServerObj == undefined || vm.mailServerObject.mailServerObj == null) {
                    $rootScope.showWarningMessage("Mail Server cannot be empty");
                    flag = false;
                }
                else if (vm.mailServerObject.receiverUser == null || vm.mailServerObject.receiverUser == undefined || vm.mailServerObject.receiverUser == "") {
                    flag = false;
                    $rootScope.showWarningMessage("Receiver User cannot be empty");
                }
                else if (vm.mailServerObject.receiverEmail == null || vm.mailServerObject.receiverEmail == undefined || vm.mailServerObject.receiverEmail == "") {
                    flag = false;
                    $rootScope.showWarningMessage("Receiver Email cannot be empty");
                }
                else if (vm.mailServerObject.receiverEmail != null && !validateEmail(vm.mailServerObject.receiverEmail)) {
                    flag = false;
                }
                else if (vm.mailServerObject.receiverPassword == null || vm.mailServerObject.receiverPassword == undefined || vm.mailServerObject.receiverPassword == "") {
                    flag = false;
                    $rootScope.showWarningMessage("Receiver Password cannot be empty");
                }
                else if (vm.mailServerObject.senderUser == null || vm.mailServerObject.senderUser == undefined || vm.mailServerObject.senderUser == "") {
                    flag = false;
                    $rootScope.showWarningMessage("Sender User cannot be empty");
                }
                else if (vm.mailServerObject.senderEmail == null || vm.mailServerObject.senderEmail == undefined || vm.mailServerObject.senderEmail == "") {
                    flag = false;
                    $rootScope.showWarningMessage("Sender Email cannot be empty");
                } else if (vm.mailServerObject.senderEmail != null && !validateEmail(vm.mailServerObject.senderEmail)) {
                    flag = false;
                }
                else if (vm.mailServerObject.senderPassword == null || vm.mailServerObject.senderPassword == undefined || vm.mailServerObject.senderPassword == "") {
                    flag = false;
                    $rootScope.showWarningMessage("Sender Password cannot be empty");
                }
                return flag;
            }

            function validateEmail(email) {
                var flag = true;
                var atpos = email.indexOf("@");
                var dotpos = email.lastIndexOf(".");
                if (email != null && email != undefined && email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        flag = false;
                        $rootScope.showWarningMessage("Invalid email format");
                    }
                }
                return flag
            }

            function updateObjectMail() {
                if (validateObjectSettings()) {
                    if (vm.mailServerObject.mailServer == null) {
                        createObjectMailSettings();
                    }
                    else {
                        if (vm.mailServerObject.mailServerObj != null && vm.mailServerObject.mailServerObj != undefined) {
                            vm.mailServerObject.mailServer = vm.mailServerObject.mailServerObj.id;
                        }
                        MailServerService.updateObjectMailSettings(vm.mailServerObject).then(
                            function (data) {
                                loadMailServerSettings();
                                $rootScope.showSuccessMessage("updated successfully");

                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                }
            }

            function createObjectMailSettings() {
                if (vm.mailServerObject.mailServerObj != null && vm.mailServerObject.mailServerObj != undefined) {
                    vm.mailServerObject.mailServer = vm.mailServerObject.mailServerObj.id;
                }
                vm.mailServerObject.objectId = $stateParams.projectId;
                MailServerService.createObjectMailSettings(vm.mailServerObject).then(
                    function (data) {
                        $rootScope.showSuccessMessage("updated successfully");
                        $rootScope.selectedProject.mailServerObject = data;
                        vm.mailServerObject = data;
                        $rootScope.showUpdateButton = false;
                        loadMailServerSettings();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadProject() {
                vm.loading = true;
                ProjectService.getProject(vm.projectId).then(
                    function (data) {
                        vm.project = data;
                        loadIssuesCount();
                        $rootScope.selectedProject = vm.project;
                        $application.selectedProject = null;
                        $application.selectedProject = vm.project;
                        vm.loading = false;
                        $rootScope.headerProjectName = true;
                        if (vm.project.status == null) {
                            vm.project.status = "NEW";
                        }
                        CommonService.getPersonReferences([vm.project], 'createdBy');
                        CommonService.getPersonReferences([vm.project], 'projectOwner');
                        $timeout(function () {
                            vm.project.createdByPerson = vm.project.createdByObject;
                            vm.project.projectOwnerPerson = vm.project.projectOwnerObject;
                        }, 1000);
                        if ($rootScope.selectedProject.projectOwner == $rootScope.login.person.id) {
                            $rootScope.login.person.isProjectOwner = true;
                            $rootScope.$broadcast('project.tabs');
                        }
                    }
                )
            }

            function updateStartDate() {
                if (vm.project.actualStartDate != null && vm.project.actualStartDate != undefined) {
                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY');
                    var todayDate = moment(todayStr, 'DD/MM/YYYY');
                    var actualStartDate = moment(vm.project.actualStartDate);
                    vm.project.actualStartDate = actualStartDate.format('DD/MM/YYYY');
                    if (actualStartDate.isSame(todayDate) || actualStartDate.isAfter(todayDate)) {
                        update();
                    }
                    else {
                        $rootScope.showErrorMessage("Actual Start Date should be after Today's Date");

                    }
                }
            }

            function updateFinishDate() {
                if (vm.project.actualFinishDate != null && vm.project.actualFinishDate != undefined) {
                    var actualStartDate = moment(vm.project.actualStartDate, 'DD/MM/YYYY');
                    var actualFinishDate = moment(vm.project.actualFinishDate);
                    vm.project.actualFinishDate = actualFinishDate.format('DD/MM/YYYY');
                    if (actualFinishDate.isSame(actualStartDate) || actualFinishDate.isAfter(actualStartDate)) {
                        update();
                    }
                    else {
                        $rootScope.showErrorMessage("Actual Finish Date should be after Actual Start Date");
                    }
                }
            }

            function update() {
                ProjectService.updateProject(vm.project).then(
                    function (data) {
                        vm.project = data;
                        loadProject();
                        $rootScope.showSuccessMessage("Project updated successfully");
                    }
                )
            }

            function activateProjectTab(id) {
                var item = getTabById(id);
                if (item != null) {
                    lastSelectedItem.active = false;
                    item.active = true;
                    lastSelectedItem = item;

                    $timeout(function () {
                        $('.project-headerbar').trigger('click');
                    }, 100);

                    if (item.id == 'project.documents') {
                        $state.go(item.state, {type: 'documents'});
                    }
                    else if (item.id == 'project.drawings') {
                        $state.go(item.state, {type: 'drawings'});
                    }
                    else {
                        $state.go(item.state);
                    }
                }
            }

            function getTabById(id) {
                var found = null;
                angular.forEach(vm.navItems, function (item) {
                    if (item.id == id) {
                        found = item;
                    }
                });

                return found;
            }

            function updateProject() {
                vm.valid = true;
                if (vm.project.name == "" || vm.project.name == undefined || vm.project.name == null) {
                    $rootScope.showErrorMessage("Project Name cannot be empty");
                    loadProject();
                }
                else {
                    vm.project.projectOwner = vm.project.projectOwnerObject.id;
                    ProjectService.updateProject(vm.project).then(
                        function (data) {
                            loadProject();
                            $rootScope.showSuccessMessage("Project Details updated successfully");
                        },
                        function (error) {
                            $rootScope.showErrorMessage("{0} Name already exists".format(vm.project.name));
                            loadProject();
                        });
                }
            }

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (projects) {
                        angular.forEach(projects, function (project) {
                            projectMap.put(project.name, project);
                        })
                    }
                )
            }

            function loadPersons() {
                CommonService.findAllPersons().then(
                    function (data) {
                        angular.forEach(data, function (person) {
                            if (person.objectType == 'PERSON') {
                                vm.persons.push(person);
                            }
                        });
                    })
            }

            function loadMailServers() {
                vm.mailServers = [];
                MailServerService.getAllMailServers().then(
                    function (success) {
                        vm.mailServers = success;
                    }, function (error) {

                    });
            }

            function showReceiverPassword() {
                var eyeIcon = document.getElementById("showPassword");
                var receiverPassword = document.getElementById("receiverPassword");
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye") {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                } else {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                }

                if (receiverPassword.attributes.type.nodeValue == "password") {
                    receiverPassword.attributes.type.nodeValue = "text";
                } else {
                    receiverPassword.attributes.type.nodeValue = "password";
                }
            }

            function showSenderPassword() {
                var eyeIcon = document.getElementById("showPassword1");
                var receiverPassword = document.getElementById("senderPassword");
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye") {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                } else {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                }

                if (receiverPassword.attributes.type.nodeValue == "password") {
                    receiverPassword.attributes.type.nodeValue = "text";
                } else {
                    receiverPassword.attributes.type.nodeValue = "password";
                }
            }

            function saveAs() {
                var options = {
                    title: 'Save As',
                    template: 'app/desktop/modules/pm/project/home/tabs/basic/newProjectCloneView.jsp',
                    controller: 'NewProjectCloneController as newProjectCloneVm',
                    resolve: 'app/desktop/modules/pm/project/home/tabs/basic/newProjectCloneController',
                    width: 600,
                    /*showMask: true,*/
                    data: {
                        projectPlan: vm.project
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.project.saveAs'}
                    ],
                    callback: function (data) {
                        $rootScope.hideSidePanel();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            $scope.onEventExampleClicked = function (event, pos, item) {
                alert('Click! ' + event.timeStamp + ' ' + pos.pageX + ' ' + pos.pageY);
            };

            $scope.onEventExampleHover = function (event, pos, item) {
                console.log('Hover! ' + event.timeStamp + ' ' + pos.pageX + ' ' + pos.pageY);
            };

            function loadIssuesCount() {
                IssueService.getIssuesCountByPriority(vm.project.id).then(
                    function (data) {
                        vm.piechart1 = true;

                        $timeout(function () {
                            var options = {
                                chart: {
                                    width: 380,
                                    type: "donut"
                                },
                                dataLabels: {
                                    enabled: false
                                },
                                series: [data.high, data.medium, data.low],
                                labels: ["High", "Medium", "Low"]
                            };

                            var chart = new ApexCharts(
                                document.querySelector("#pie"),
                                options);
                            chart.render();
                        }, 2000);
                    }
                );
                loadTasksCount();
            }

            function mediaCount() {
                ProjectService.getProjectMediaCount(vm.project.id).then(
                    function (data) {
                        vm.barChart1 = true;
                        $timeout(function () {
                            var options = {
                                chart: {
                                    height: 300,
                                    type: 'bar'
                                },
                                plotOptions: {
                                    bar: {
                                        horizontal: true
                                    }
                                },
                                dataLabels: {
                                    enabled: false
                                },
                                series: [{

                                    data: [data.images, data.videos, data.files]
                                }],
                                xaxis: {
                                    categories: ['Images', 'Videos', 'Files']
                                }
                            };

                            var chart = new ApexCharts(
                                document.querySelector("#mediaChart"),
                                options
                            );
                            chart.render();
                            vm.barChart2 = true;
                        }, 2000);
                    }
                );
            }

            function loadTasksCount() {
                TaskService.getTasksCount(vm.project.id).then(
                    function (data) {

                        $timeout(function () {
                            var options = {
                                chart: {
                                    height: 300,
                                    type: 'bar'
                                },
                                plotOptions: {
                                    bar: {
                                        horizontal: true
                                    }
                                },
                                dataLabels: {
                                    enabled: false
                                },
                                series: [{

                                    data: [data.newTasks, data.assignedTasks, data.inProgressTasks, data.finishedTasks]
                                }],
                                xaxis: {
                                    categories: ['New', 'Assigned', 'Inprogresses', 'Finished']
                                }
                            };

                            var chart = new ApexCharts(
                                document.querySelector("#chart"),
                                options
                            );
                            chart.render();
                            vm.barChart2 = true;
                        }, 2000);
                        mediaCount();
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.project.update', updateObjectMail);
                    loadProject();
                    loadProjects();
                    loadPersons();
                    loadMailServerSettings();
                    loadMailServers();
                    $rootScope.showComments('PROJECT', $stateParams.projectId);
                    $timeout(function () {
                        activateProjectTab('project.home');
                    }, 2000);
                }

            })();
        }
    }
);