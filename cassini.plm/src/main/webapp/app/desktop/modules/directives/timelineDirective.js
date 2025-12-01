define(
    [
        'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/activityStreamService'
    ],

    function (module) {
        module.directive('timelineView', TimelineDirectiveController);

        function TimelineDirectiveController($rootScope, $sce, $translate, LoginService, ActivityStreamService) {
            return {
                templateUrl: 'app/desktop/modules/directives/timelineDirectiveView.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'objectType': '@',
                    'objectId': '=',
                    'objectIds': '=',
                    'hasPermission': '='
                },
                link: function ($scope, $elem, attrs) {

                    var parsed = angular.element("<div></div>");
                    $scope.downloadedName = parsed.html($translate.instant("ACTIVITY_STREAMS_DOWNLOAD_NAME")).html();
                    $scope.selectDate = parsed.html($translate.instant("SELECT_DATE")).html();
                    $scope.selectUser = parsed.html($translate.instant("SELECT_USER")).html();
                    var attributeTitle = parsed.html($translate.instant("ATTRIBUTE")).html();
                    var propertyTitle = parsed.html($translate.instant("PROPERTY")).html();
                    $scope.loadActivityStream = loadActivityStream;
                    $scope.types = [
                        {label: attributeTitle, value: "Attribute"},
                        {label: propertyTitle, value: "Property"}
                    ];

                    $scope.persons = [];
                    function loadPersons() {
                        $scope.persons = [];
                        LoginService.getAllLogins().then(
                            function (data) {
                                angular.forEach(data, function (login) {
                                    if (login.isActive == true) {
                                        $scope.persons.push(login.person);
                                    }
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }

                    $scope.loading = true;
                    var pageable = {
                        page: 0,
                        size: 25,
                        sort: {
                            field: "timestamp",
                            order: "DESC"
                        }
                    };
                    $scope.activityStreamFilter = {
                        objectId: $scope.objectId,
                        date: null,
                        user: '',
                        objectIds: [],
                        action: null
                    };


                    $scope.selectedType = null;
                    $scope.selectType = selectType;
                    function selectType(type) {
                        if (type != null) {
                            $scope.selectedType = type;
                            $scope.activityStreamFilter.type = type.value;
                        } else {
                            $scope.selectedType = null;
                            $scope.activityStreamFilter.type = null;
                        }
                    }

                    $scope.selectedUser = null;
                    $scope.selectUser = selectUser;
                    function selectUser(user) {
                        if (user != null) {
                            $scope.selectedUser = user;
                            $scope.activityStreamFilter.user = user.id;
                        } else {
                            $scope.selectedUser = null;
                            $scope.activityStreamFilter.user = '';
                        }
                        $rootScope.showBusyIndicator($('.view-content'));
                        pageable.page = 0;
                        loadActivityStream();
                    }

                    $scope.selectedAction = null;
                    $scope.selectAction = selectAction;
                    function selectAction(action) {
                        if (action != null) {
                            $scope.selectedAction = action;
                            var converter = objectTypeConverterMap.get($scope.objectType);
                            $scope.activityStreamFilter.action = converter + "" + action.value;
                        } else {
                            $scope.selectedAction = null;
                            $scope.activityStreamFilter.action = null;
                        }
                        $rootScope.showBusyIndicator($('.view-content'));
                        pageable.page = 0;
                        loadActivityStream();
                    }

                    function watchDates() {
                        $scope.$watch('selectedDate.date', function (newVal, oldVal) {
                            if (angular.equals(newVal, oldVal)) {

                            } else {
                                $scope.selectedDate.date = newVal;
                                $rootScope.showBusyIndicator($('.view-content'));
                                pageable.page = 0;
                                loadActivityStream();
                            }
                            $scope.$evalAsync();
                        }, true);
                    }

                    $scope.objectHistory = {};
                    $scope.lastPage = true;
                    $scope.todayDate = null;
                    $scope.historyCount = 0;
                    $scope.selectedDate = {
                        date:null
                    };

                    function loadActivityStream() {
                        $rootScope.showBusyIndicator($('.view-content'));
                        if ($scope.objectId != null && $scope.objectId != "" && $scope.objectId != undefined) {
                            $scope.activityStreamFilter.objectId = $scope.objectId;
                        }
                        if ($scope.objectIds != undefined && $scope.objectIds.length > 0) {
                            $scope.activityStreamFilter.objectIds = $scope.objectIds;
                        }
                        if ($scope.objectId == undefined || $scope.objectId == "") {
                            $scope.objectId = 0;
                            $scope.activityStreamFilter.objectId = '';
                        }
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
                        ActivityStreamService.getDatewiseActivityStreamByObjectId($scope.objectId, pageable, $scope.activityStreamFilter).then(
                            function (data) {
                                $scope.historyCount = 0;
                                $scope.lastPage = data.last;
                                $scope.objectHistory = data.histories;
                                loadData();
                                $scope.loading = false;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    function loadData() {
                        $rootScope.showBusyIndicator();
                        var today = moment(new Date());
                        $scope.todayDate = today.format($rootScope.applicationDateSelectFormat);
                        angular.forEach($scope.objectHistory, function (histories, key) {
                            $scope.historyCount++;
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

                    $scope.sessionPopover = {
                        templateUrl: 'app/desktop/modules/item/details/tabs/timelineHistory/sessionPopover.jsp'
                    };

                    $scope.loadMoreHistory = loadMoreHistory;
                    function loadMoreHistory() {
                        pageable.page++;
                        $scope.loading = true;
                        $rootScope.showBusyIndicator($('.view-content'));
                        if ($scope.objectId == undefined || $scope.objectId == "") {
                            $scope.objectId = 0;
                        }
                        ActivityStreamService.getDatewiseActivityStreamByObjectId($scope.objectId, pageable, $scope.activityStreamFilter).then(
                            function (data) {
                                $scope.lastPage = data.last;
                                $scope.moreHistory = data.histories;
                                var today = moment(new Date());
                                $scope.todayDate = today.format($rootScope.applicationDateSelectFormat);
                                angular.forEach($scope.moreHistory, function (histories, key) {

                                    var history1 = $scope.objectHistory[key];

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
                                        $scope.objectHistory[key] = histories;
                                    }
                                });
                                $scope.loading = false;
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    function resizeView() {
                        var tabPane = $('.tab-pane').outerHeight();
                        $('.timeline-container').height(tabPane);
                        var hasToolbar = $('.view-container').find('.view-toolbar').length > 0;
                        var viewTitleContainer = $('#viewTitleContainer').outerHeight();
                        if (hasToolbar && viewTitleContainer != null) {
                            $('.timeline-filter-panel').css('top', 209 + 'px');
                        } else if (hasToolbar && viewTitleContainer == null) {
                            $('.timeline-filter-panel').css('top', 159 + 'px');
                        } else {
                            $('.timeline-filter-panel').css('top', 113 + 'px');
                        }
                        $('.timeline-container .timeline-filter-panel').height(tabPane - 40);
                    }

                    var actionNameMap = new Hashtable();
                    actionNameMap.put("create", "Create");
                    actionNameMap.put("update", "Update");
                    actionNameMap.put("files", "Files");
                    actionNameMap.put("workflow", "Workflow");
                    actionNameMap.put("teams", "Team");
                    actionNameMap.put("deliverables", "Deliverables");
                    actionNameMap.put("referenceitems", "Reference Items");
                    actionNameMap.put("phases", "Phases");
                    actionNameMap.put("activities", "Activities");
                    actionNameMap.put("tasks", "Tasks");
                    actionNameMap.put("milestones", "Milestones");
                    actionNameMap.put("subscribe", "Subscribe");
                    actionNameMap.put("unsubscribe", "Unsubscribe");
                    actionNameMap.put("reqdocuments", "Requirements");
                    actionNameMap.put("resources", "Resources");
                    actionNameMap.put("projects", "Projects");
                    actionNameMap.put("route", "Route");
                    actionNameMap.put("revision", "Revision");
                    actionNameMap.put("part", "Part");
                    actionNameMap.put("parts", "Parts");
                    actionNameMap.put("contact", "Contact");
                    actionNameMap.put("inspectionreport","Inspection Report");
                    actionNameMap.put("spareparts","Spare Parts");
                    actionNameMap.put("operations","Operations");
                    actionNameMap.put("workCenters","WorkCenters");
                    actionNameMap.put("persons","Persons");
                    actionNameMap.put("resource","Resource");
                    actionNameMap.put("bom", "BOM");
                    actionNameMap.put("checklist", "Checklist");
                    actionNameMap.put("relatedItems", "Related Items");
                    actionNameMap.put("affectedItems", "Affected Items");
                    actionNameMap.put("requestedItem", "Requested Item");
                    actionNameMap.put("plan", "Plan");
                    actionNameMap.put("problemItems", "Problem Items");
                    actionNameMap.put("capa", "Capa");
                    actionNameMap.put("sources", "Sources");
                    actionNameMap.put("lifeCyclePhase", "LifeCyclePhase");
                    actionNameMap.put("release", "Released");
                    actionNameMap.put("incorporate", "Incorporate");
                    actionNameMap.put("unincorporate", "Unincorporate");
                    actionNameMap.put("specifications", "Specification");



                    var objectTypeConverterMap = new Hashtable();
                    objectTypeConverterMap.put("PROJECT", "plm.project.");
                    objectTypeConverterMap.put("PROGRAM", "plm.program.");
                    objectTypeConverterMap.put("PLANT", "plm.mes.plant.");
                    objectTypeConverterMap.put("ASSEMBLYLINE", "plm.mes.assemblyline.");
                    objectTypeConverterMap.put("MACHINE", "plm.mes.machine.");
                    objectTypeConverterMap.put("EQUIPMENT", "plm.mes.equipment.");
                    objectTypeConverterMap.put("SHIFT", "plm.mes.shift.");
                    objectTypeConverterMap.put("MATERIAL", "plm.mes.material.");
                    objectTypeConverterMap.put("JIGFIXTURE", "plm.mes.jigfixture.");
                    objectTypeConverterMap.put("TOOL", "plm.mes.tool.");
                    objectTypeConverterMap.put("OPERATION", "plm.mes.operation.");
                    objectTypeConverterMap.put("MANPOWER", "plm.mes.manpower.");
                    objectTypeConverterMap.put("INSTRUMENT", "plm.mes.instrument.");
                    objectTypeConverterMap.put("WORKCENTER", "plm.mes.workcenter.");
                    objectTypeConverterMap.put("MBOM", "plm.mes.mbom.");
                    objectTypeConverterMap.put("BOP", "plm.mes.bop.");
                    objectTypeConverterMap.put("MANUFACTURER", "plm.oem.manufacturer.");
                    objectTypeConverterMap.put("MANUFACTURERPART", "plm.oem.manufacturerPart.");
                    objectTypeConverterMap.put("SUPPLIER", "plm.oem.supplier.");
                    objectTypeConverterMap.put("ASSET", "plm.mro.mroasset.");
                    objectTypeConverterMap.put("METER", "plm.mro.mrometer.");
                    objectTypeConverterMap.put("SPAREPART", "plm.mro.mrosparepart.");
                    objectTypeConverterMap.put("MAINTENANCEPLAN", "plm.mro.mromaintenanceplan.");
                    objectTypeConverterMap.put("WORKORDER", "plm.mro.mroworkorder.");
                    objectTypeConverterMap.put("WORKREQUEST", "plm.mro.mroworkrequest.");
                    objectTypeConverterMap.put("PPAP", "plm.quality.ppap.");
                    objectTypeConverterMap.put("SUPPLIERAUDIT", "plm.quality.supplierAudit.");
                    objectTypeConverterMap.put("QCR", "plm.quality.qcr.");
                    objectTypeConverterMap.put("NCR", "plm.quality.ncr.");
                    objectTypeConverterMap.put("PROBLEMREPORT","plm.quality.problemReport.");
                    objectTypeConverterMap.put("INSPECTION", "plm.quality.inspection.");
                    objectTypeConverterMap.put("INSPECTIONPLAN", "plm.quality.plan.");
                    objectTypeConverterMap.put("ECR", "plm.change.ecr.");
                    objectTypeConverterMap.put("ECO", "plm.change.eco.");
                    objectTypeConverterMap.put("DCR", "plm.change.dcr.");
                    objectTypeConverterMap.put("DCO", "plm.change.dco.");
                    objectTypeConverterMap.put("MCO", "plm.change.mco.");
                    objectTypeConverterMap.put("VARIANCE", "plm.change.variance.");
                    objectTypeConverterMap.put("ITEM", "plm.items.");
                    objectTypeConverterMap.put("PLMNPR", "plm.npr.");


                    var dateFormat = 'DD/MM/YYYY, HH:mm';

                    function loadObjectTypeActions() {
                        if ($scope.objectType == "ITEM"){
                        ActivityStreamService.getObjectTypeActionsIds($scope.objectIds, $scope.objectType).then(
                            function (data) {
                                $scope.objectTypeActions = [];
                                angular.forEach(data, function (action) {
                                    $scope.objectTypeActions.push({name: actionNameMap.get(action), value: action});
                                });
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'DD MM YYYY, HH:mm';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'DD.MM.YYYY, HH:mm';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'MM-DD-YYYY, HH:mm';
                                }
                            }
                        )
                    }else{
                        ActivityStreamService.getObjectTypeActions($scope.objectId, $scope.objectType).then(
                            function (data) {
                                $scope.objectTypeActions = [];
                                angular.forEach(data, function (action) {
                                    $scope.objectTypeActions.push({name: actionNameMap.get(action), value: action});
                                });
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'DD MM YYYY, HH:mm';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'DD.MM.YYYY, HH:mm';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'MM-DD-YYYY, HH:mm';
                                }
                            }
                        )
                    }
                }

                    (function () {
                        $scope.$on('app.object.timeline', function (event, data) {
                            pageable.page = 0;
                            $(window).resize(resizeView);
                            loadActivityStream();
                            loadObjectTypeActions();
                            loadPersons();
                            watchDates();
                            resizeView();
                        })
                    })();
                }
            }
        }
    }
);
