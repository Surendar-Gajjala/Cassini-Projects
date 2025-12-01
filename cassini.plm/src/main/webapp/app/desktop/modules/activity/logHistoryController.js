define(
    [
        'app/desktop/modules/activity/activity.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/activityStreamService'
    ],
    function (module) {
        module.controller('LogHistoryController', LogHistoryController);

        function LogHistoryController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, $translate, $window, $interval,
                                      LoginService, ActivityStreamService) {

            var vm = this;
            var parsed = angular.element("<div></div>")
            vm.downloadedName = parsed.html($translate.instant("ACTIVITY_STREAMS_DOWNLOAD_NAME")).html();
            $scope.activitiesTitle = parsed.html($translate.instant("ACTIVITYS")).html();

            vm.sessionPage = {
                page: 0,
                size: 20,
                sort: {
                    field: "loginTime",
                    order: "DESC"
                }
            };

            vm.logHistory = [];
            vm.persons = [];

            vm.loadAllActivityStream = loadAllActivityStream;

            function loadPersons() {
                LoginService.getAllLogins().then(
                    function (data) {
                        vm.persons = [];
                        vm.sessionPersons = [];
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                            vm.sessionPersons.push(login.person);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.pageable = {
                page: 0,
                size: 25,
                sort: {
                    field: "timestamp",
                    order: "DESC"
                }
            };
            $scope.activityStreamFilter = {
                objectId: '',
                date: null,
                user: '',
                type: null,
                objectIds: [],
                action: null
            };

            $scope.selectedDate = {
                date:null
            };

            vm.types = [];
            function loadAllActivityStream() {
                vm.loading = true;
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                $scope.activityStreamFilter.date = null;
                        if ($scope.selectedDate.date != undefined && $scope.selectedDate.date != "" && $scope.selectedDate.date !=null) {                    
                            var dateList = $scope.selectedDate.date.split($rootScope.applicationDateSelectFormatDivider);  
                            var date = null;                         
                            if($rootScope.applicationDateSelectFormat =="MM-DD-YYYY"){
                            
                            date = dateList[1] +"/"+dateList[0] +"/"+dateList[2];
                            
                            }else{
                            
                            date = dateList[0] +"/"+dateList[1] +"/"+dateList[2];
                            
                            }
                            
                            $scope.activityStreamFilter.date=date;
                        }
                ActivityStreamService.getDateWiseActivityStream(vm.pageable, $scope.activityStreamFilter).then(
                    function (data) {
                        vm.historyCount = 0;
                        vm.lastPage = data.last;
                        vm.activityStreams = data.histories;
                        loadData();
                        $rootScope.hideBusyIndicator();
                        $scope.loading = false;
                        $timeout(function () {
                            $('.log-filter-panel').width($('.log-timeline-container').outerWidth());
                            $('.tab-content').height($('#rightSidePanel').outerHeight() - 100);
                            $('.tab-pane').height($('#rightSidePanel').outerHeight() - 100);
                        }, 500)
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadData() {
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                var today = moment(new Date());
                vm.todayDate = today.format($rootScope.applicationDateSelectFormat);
                angular.forEach(vm.activityStreams, function (histories, key) {
                    vm.historyCount++;
                    angular.forEach(histories, function (history) {
                        var time = moment(history.timestamp, dateFormat);
                        var hours = time.format('HH:mm');

                        var timeString = hours;
                        var hour = +timeString.substr(0, 2);
                        var h = hour % 12 || 12;
                        var suffix = (hour < 12 || hour === 24) ? "am" : "pm";
                        history.time = h + timeString.substr(2, 3) + suffix;
                        if (history.summary != null) {
                            history.summary = $sce.trustAsHtml(history.summary.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                        }
                        if (history.sessionObject != null && history.sessionObject != "") {
                            time = moment(history.sessionObject.loginTime, dateFormat);
                            hours = time.format('HH:mm');

                            timeString = hours;
                            hour = +timeString.substr(0, 2);
                            h = hour % 12 || 12;
                            suffix = (hour < 12 || hour === 24) ? "am" : "pm";
                            history.sessionObject.time = h + timeString.substr(2, 3) + suffix;
                        }
                    })
                });
                $rootScope.hideBusyIndicator();
            }

            vm.loadMoreHistory = loadMoreHistory;
            $scope.loadMoreHistory = loadMoreHistory;
            function loadMoreHistory() {
                vm.pageable.page++;
                $scope.loading = true;
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                ActivityStreamService.getDateWiseActivityStream(vm.pageable, $scope.activityStreamFilter).then(
                    function (data) {
                        vm.lastPage = data.last;
                        vm.moreHistory = data.histories;
                        var today = moment(new Date());
                        vm.todayDate = today.format($rootScope.applicationDateSelectFormat);
                        angular.forEach(vm.moreHistory, function (histories, key) {

                            var history1 = vm.activityStreams[key];

                            angular.forEach(histories, function (history) {
                                var time = moment(history.timestamp, dateFormat);
                                var hours = time.format('HH:mm');

                                var timeString = hours;
                                var hour = +timeString.substr(0, 2);
                                var h = hour % 12 || 12;
                                var suffix = (hour < 12 || hour === 24) ? "am" : "pm";
                                history.time = h + timeString.substr(2, 3) + suffix;

                                if (history.summary != null) {
                                    history.summary = $sce.trustAsHtml(history.summary.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                }

                                if (history.sessionObject != null && history.sessionObject != "") {
                                    time = moment(history.sessionObject.loginTime, dateFormat);
                                    hours = time.format('HH:mm');

                                    timeString = hours;
                                    hour = +timeString.substr(0, 2);
                                    h = hour % 12 || 12;
                                    suffix = (hour < 12 || hour === 24) ? "am" : "pm";
                                    history.sessionObject.time = h + timeString.substr(2, 3) + suffix;
                                }
                                if (history1 != undefined) {
                                    history1.push(history);
                                }
                            })
                            if (history1 == undefined) {
                                vm.activityStreams[key] = histories;
                            }
                        })
                        $rootScope.hideBusyIndicator();
                        $scope.loading = false;

                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            vm.selectType = selectType;
            $scope.selectedType = null;
            function selectType(type) {
                if (type != null) {
                    $scope.selectedType = type;
                    $scope.activityStreamFilter.type = type;
                } else {
                    $scope.selectedType = null;
                    $scope.activityStreamFilter.type = null;
                }
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                vm.pageable.page = 0;
                loadAllActivityStream();
            }

            vm.selectUser = selectUser;
            $scope.selectedUser = null;
            function selectUser(user) {
                if (user != null) {
                    $scope.selectedUser = user;
                    $scope.activityStreamFilter.user = user.id;
                } else {
                    $scope.selectedUser = null;
                    $scope.activityStreamFilter.user = '';
                }
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                vm.pageable.page = 0;
                loadAllActivityStream();
            }

            vm.selectSessionUser = selectSessionUser;
            $scope.selectedSessionUser = null;
            function selectSessionUser(user) {
                if (user != null) {
                    $scope.selectedSessionUser = user;
                    $scope.sessionFilter.user = user.fullName;
                } else {
                    $scope.selectedSessionUser = null;
                    $scope.sessionFilter.user = null;
                }
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                vm.pageable.page = 0;
                loadSessions();
            }

            function watchDates() {
                $scope.$watch('selectedDate.date', function (newVal, oldVal) {
                    if (angular.equals(newVal, oldVal)) {

                    } else {
                        $scope.selectedDate.date = newVal;
                        vm.pageable.page = 0;
                        loadAllActivityStream();
                    }
                    $scope.$evalAsync();
                }, true);

                $scope.$watch('sessionFilter.loginTime', function (newVal, oldVal) {
                    if (angular.equals(newVal, oldVal)) {

                    } else {
                        $scope.sessionFilter.loginTime = newVal;
                        vm.pageable.page = 0;
                        loadSessions();
                    }
                    $scope.$evalAsync();
                }, true);
            }

            vm.active = 0;
            vm.tabs = {
                log: {
                    id: 'details.log',
                    heading: "Activity Stream",
                    template: 'app/desktop/modules/activity/activityStreamView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                session: {
                    id: 'details.sessions',
                    heading: 'Sessions',
                    template: 'app/desktop/modules/activity/sessionsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                }
            };

            vm.selectLogTab = selectLogTab;
            function selectLogTab(id) {
                if (id == "details.log") {
                    vm.pageable.page = 0;
                    loadAllActivityStream();
                } else {
                    loadSessions();
                }
            }

            $scope.sessionFilter = {
                loginTime: null,
                user: null
            };
            vm.loadSessions = loadSessions;
            function loadSessions() {
                vm.sessionPage.page = 0;
                vm.loading = true;
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                ActivityStreamService.getAllActivitySessions(vm.sessionPage, $scope.sessionFilter).then(
                    function (data) {
                        vm.lastPage = data.last;
                        vm.sessions = [];
                        if (data != "") {
                            vm.sessions = data.sessionDtoList;
                        }
                        var today = moment(new Date());
                        vm.todayDate = today.format($rootScope.applicationDateSelectFormat);
                        angular.forEach(vm.sessions, function (session) {
                            session.session.showLogs = false;
                            var login = moment(session.session.loginTime, dateFormat);
                            if (session.session.logoutTime != null && session.session.logoutTime != "") {
                                var logout = moment(session.session.logoutTime, dateFormat);
                                session.session.logout = logout.format('HH:mm');
                            }
                            session.session.loginDate = login.format($rootScope.applicationDateSelectFormat);
                            session.session.time = login.format('HH:mm');
                            angular.forEach(session.activityStreams, function (history) {
                                var time = moment(history.timestamp, dateFormat);
                                var hours = time.format('HH:mm');

                                var timeString = hours;
                                var hour = +timeString.substr(0, 2);
                                var h = hour % 12 || 12;
                                var suffix = (hour < 12 || hour === 24) ? "am" : "pm";
                                history.time = h + timeString.substr(2, 3) + suffix;
                            });

                            if (session.session.logoutTime != null && session.session.logoutTime != "") {
                                var login = moment(session.session.loginTime, dateFormat);
                                var logout = moment(session.session.logoutTime, dateFormat);
                                var totalSec = logout.diff(login, 'seconds');

                                var duration = moment().startOf('day')
                                    .seconds(totalSec)
                                    .format('H:mm');
                                session.duration = duration !== "0:00" ? duration : "0:01";
                            }
                            else {
                                session.duration = null;
                            }
                        });
                        if (vm.sessions.length > 0) {
                            vm.sessions[0].session.showLogs = true;
                            vm.selectedSession = vm.sessions[0];
                        }
                        $timeout(function () {
                            $('.session-filter-panel').width($('.session-timeline-container').outerWidth());
                            $('.tab-content').height($('#rightSidePanel').outerHeight() - 100);
                            $('.tab-pane').height($('#rightSidePanel').outerHeight() - 100);
                        }, 100);
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.loadMoreSessions = loadMoreSessions;
            function loadMoreSessions() {
                vm.sessionPage.page++;
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                ActivityStreamService.getAllActivitySessions(vm.sessionPage, $scope.sessionFilter).then(
                    function (data) {
                        vm.lastPage = data.last;
                        vm.moreSessions = data.sessionDtoList;
                        vm.types = data.types;
                        var today = moment(new Date());
                        vm.todayDate = today.format($rootScope.applicationDateSelectFormat);
                        angular.forEach(vm.moreSessions, function (session) {
                            session.session.showLogs = false;
                            var login = moment(session.session.loginTime, dateFormat);
                            if (session.session.logoutTime != null && session.session.logoutTime != "") {
                                var logout = moment(session.session.logoutTime, dateFormat);
                                session.session.logout = logout.format('HH:mm');
                            }
                            session.session.loginDate = login.format($rootScope.applicationDateSelectFormat);
                            session.session.time = login.format('HH:mm');
                            angular.forEach(session.activityStreams, function (history) {
                                var time = moment(history.timestamp, dateFormat);
                                var hours = time.format('HH:mm');

                                var timeString = hours;
                                var hour = +timeString.substr(0, 2);
                                var h = hour % 12 || 12;
                                var suffix = (hour < 12 || hour === 24) ? "am" : "pm";
                                history.time = h + timeString.substr(2, 3) + suffix;
                            })
                            vm.sessions.push(session);
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $scope.showSessionLogs = showSessionLogs;
            function showSessionLogs(session) {
                if (vm.selectedSession != null && vm.selectedSession.session.sessionId == session.session.sessionId) {
                    session.session.showLogs = false;
                    vm.selectedSession = null;
                } else {
                    if (vm.selectedSession != null) {
                        vm.selectedSession.session.showLogs = false;
                    }
                    session.session.showLogs = true;
                    vm.selectedSession = session;
                }
            }

            $rootScope.sessionPopover = {
                templateUrl: 'app/desktop/modules/item/details/tabs/timelineHistory/sessionPopover.jsp'
            };
            var dateFormat = 'DD/MM/YYYY, HH:mm';
            function loadObjectTypes() {
                vm.objectTypes = [];
                ActivityStreamService.getObjectTypes().then(
                    function (data) {
                        vm.objectTypes = [];
                        angular.forEach(data, function (objectType) {
                            if (vm.objectTypes.indexOf(objectType) == -1) {
                                vm.objectTypes.push(objectType);
                            }
                        })
                        if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                            dateFormat = 'DD MM YYYY, HH:mm';
                        } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                            dateFormat = 'DD.MM.YYYY, HH:mm';
                        } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                            dateFormat = 'MM-DD-YYYY, HH:mm';
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadPersons();
                loadObjectTypes();
                loadAllActivityStream();
                watchDates();

                /*$timeout(function() {
                 $('.tab-pane').scroll(function() {
                 var element = document.querySelector('#loadMore');
                 var position = element.getBoundingClientRect();

                 // checking whether fully visible
                 $timeout(function() {
                 if(position.top >= 0 && position.bottom <= window.innerHeight) {
                 //vm.loadMoreHistory();
                 }
                 }, 1000);
                 });
                 }, 1000);*/
            })();
        }
    }
);