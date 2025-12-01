define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/userTasksService',
        'app/desktop/modules/home/internal/newObjectController',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/problemReportService',
        'app/desktop/modules/home/widgets/conversations/conversationsWidget',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService',
        'app/shared/services/core/searchService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'

    ],
    function (module) {
        module.controller('HomeDashboardController', HomeDashboardController);

        function HomeDashboardController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate, $application,
                                         UserTasksService, CommentsService, ItemService, ECOService, ProblemReportService, SearchService, LoginService) {

            $rootScope.viewInfo.showDetails = false;

            var vm = this;
            var owner = null;

            vm.greeting = "";
            vm.dateString = "";
            vm.userTasksCount = 0;
            vm.conversationsCount = 0;
            $rootScope.savedSearchesCount = 0;

            var parsed = angular.element("<div></div>");
            $scope.saved = parsed.html($translate.instant("SAVED_SEARCH_TITLE")).html();
            var homeWidgetsUpdateMessage = parsed.html($translate.instant("HOME_WIDGETS_UPDATED_SUCCESS_MES")).html();

            function updateDate() {
                var options = {
                    weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
                    hour: '2-digit', minute: '2-digit', second: '2-digit'
                };
                var today = new Date();
                vm.dateString = today.toLocaleDateString("en-US", options);
            }

            function setGreeting() {
                var welcome;
                var date = new Date();
                var hour = date.getHours();

                if (hour < 12) {
                    welcome = "Good Morning";
                } else if (hour < 17) {
                    welcome = "Good Afternoon";
                } else {
                    welcome = "Good Evening";
                }

                vm.greeting = welcome + " " + $rootScope.personInfo.fullName;
            }

            function init() {
                $('body').addClass('dark-mode');
            }

            vm.gotoSettings = gotoSettings;
            function gotoSettings() {
                $state.go("app.settings");
            }

            vm.gotoClassification = gotoClassification;
            function gotoClassification() {
                $state.go("app.classification");
            }

            vm.gotoAdmin = gotoAdmin;
            function gotoAdmin() {
                $state.go("app.newadmin.users");
            }

            vm.showMyTasks = showMyTasks;
            function showMyTasks() {
                var options = {
                    title: "My Tasks",
                    template: 'app/desktop/modules/home/dashboard/details/tasks/tasksView.jsp',
                    controller: 'TasksController as userTasksVm',
                    resolve: 'app/desktop/modules/home/dashboard/details/tasks/tasksController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: "Close", broadcast: 'app.home.mytasks'}
                    ],
                    callback: function () {
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showConversations = showConversations;
            function showConversations() {
                var options = {
                    title: "Conversations",
                    template: 'app/desktop/modules/home/widgets/conversations/conversationsWidget.jsp',
                    controller: 'ConversationsController as conversationsVm',
                    resolve: 'app/desktop/modules/home/widgets/conversations/conversationsWidget',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: "Close", broadcast: 'app.home.conversations'}
                    ],
                    callback: function () {
                        loadConversationsCount();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadUserTasksCounts() {
                UserTasksService.getUserTaskCountsByStatus().then(
                    function (data) {
                        vm.userTasksCount = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadConversationsCount() {
                UserTasksService.getConversationCountByPerson($rootScope.loginPersonDetails.person.id).then(
                    function (data) {
                        vm.conversationsCount = data;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSavedSearchesCount() {
                SearchService.getSavedSearchesCount().then(
                    function (data) {
                        $rootScope.savedSearchesCount = data;

                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadObjectsCounts() {
                UserTasksService.getObjectCounts().then(
                    function (data) {
                        vm.widgetObjectCounts = data;
                        loadUserPreferences();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.getObjectCount = getObjectCount;
            function getObjectCount(widget) {
                if (widget.subType == "ecr") {
                    return vm.widgetObjectCounts.ecrCount;
                } else if (widget.subType == "dcr") {
                    return vm.widgetObjectCounts.dcrCount;
                } else if (widget.subType == "eco") {
                    return vm.widgetObjectCounts.ecoCount;
                } else if (widget.subType == "dco") {
                    return vm.widgetObjectCounts.dcoCount;
                } else if (widget.subType == "mco") {
                    return vm.widgetObjectCounts.mcoCount;
                } else if (widget.subType == "item") {
                    return vm.widgetObjectCounts.itemsCount;
                } else if (widget.subType == "manufacturerpart") {
                    return vm.widgetObjectCounts.mfrPartCount;
                } else if (widget.subType == "pgcspecification") {
                    return vm.widgetObjectCounts.specificationCount;
                } else if (widget.subType == "pgcdeclaration") {
                    return vm.widgetObjectCounts.declarationCount;
                } else if (widget.subType == "mroasset") {
                    return vm.widgetObjectCounts.assetCount;
                } else if (widget.subType == "mroworkrequest") {
                    return vm.widgetObjectCounts.workRequestCount;
                } else if (widget.subType == "mroworkorder") {
                    return vm.widgetObjectCounts.workOrderCount;
                } else if (widget.subType == "operation") {
                    return vm.widgetObjectCounts.operationCount;
                } else if (widget.subType == "productionorder") {
                    return 0;
                } else if (widget.subType == "mromaintenanceplan") {
                    return vm.widgetObjectCounts.maintenancePlanCount;
                } else if (widget.subType == "problemreport") {
                    return vm.widgetObjectCounts.problemReportCount;
                } else if (widget.subType == "qcr") {
                    return vm.widgetObjectCounts.qcrCount;
                } else if (widget.subType == "project") {
                    return vm.widgetObjectCounts.projectsCount;
                } else if (widget.subType == "requirementdocument") {
                    return vm.widgetObjectCounts.requirementDocumentsCount;
                } else if (widget.subType == "inspectionplan") {
                    return vm.widgetObjectCounts.inspectionPlanCount;
                } else if (widget.subType == "inspection") {
                    return vm.widgetObjectCounts.inspectionCount;
                } else if (widget.subType == "ncr") {
                    return vm.widgetObjectCounts.ncrCount;
                } else if (widget.subType == "manufacturer") {
                    return vm.widgetObjectCounts.mfrCount;
                } else if (widget.subType == "mfrsupplier") {
                    return vm.widgetObjectCounts.supplierCount;
                } else if (widget.subType == "plmworkflow") {
                    return vm.widgetObjectCounts.workflowCount;
                } else if (widget.subType == "pdmvault") {
                    return vm.widgetObjectCounts.vaultCount;
                } else if (widget.subType == "plant") {
                    return vm.widgetObjectCounts.plantCount;
                } else if (widget.subType == "workcenter") {
                    return vm.widgetObjectCounts.workCenterCount;
                } else if (widget.subType == "machine") {
                    return vm.widgetObjectCounts.machineCount;
                } else if (widget.subType == "equipment") {
                    return vm.widgetObjectCounts.equipmentCount;
                } else if (widget.subType == "instrument") {
                    return vm.widgetObjectCounts.instrumentCount;
                } else if (widget.subType == "tool") {
                    return vm.widgetObjectCounts.toolCount;
                } else if (widget.subType == "jigfixture") {
                    return vm.widgetObjectCounts.jigAndFixtureCount;
                } else if (widget.subType == "material") {
                    return vm.widgetObjectCounts.materialCount;
                } else if (widget.subType == "manpower") {
                    return vm.widgetObjectCounts.manPowerCount;
                } else if (widget.subType == "shift") {
                    return vm.widgetObjectCounts.shiftCount;
                } else if (widget.subType == "assemblyline") {
                    return vm.widgetObjectCounts.assemblyLineCount;
                } else if (widget.subType == "pgcsubstance") {
                    return vm.widgetObjectCounts.substanceCount;
                } else if (widget.subType == "mrometer") {
                    return vm.widgetObjectCounts.meterCount;
                } else if (widget.subType == "mrosparepart") {
                    return vm.widgetObjectCounts.sparePartCount;
                } else if (widget.subType == "ppap") {
                    return vm.widgetObjectCounts.ppapCount;
                } else if (widget.subType == "supplieraudit") {
                    return vm.widgetObjectCounts.supplierAuditCount;
                } else if (widget.subType == "mbom") {
                    return vm.widgetObjectCounts.mbomCount;
                } else if (widget.subType == "program") {
                    return vm.widgetObjectCounts.programCount;
                }
            }

            vm.getObjectIcon = getObjectIcon;
            function getObjectIcon(widget) {
                if (widget.subType == "ecr") {
                    return "la la-bell";
                } else if (widget.subType == "dcr") {
                    return "la la-edit";
                } else if (widget.subType == "eco") {
                    return "la la-pen";
                } else if (widget.subType == "dco") {
                    return "la-layer-group";
                } else if (widget.subType == "mco") {
                    return "la-hockey-puck";
                } else if (widget.subType == "item") {
                    return "la-cog";
                } else if (widget.subType == "manufacturerpart") {
                    return "la-shield-alt";
                } else if (widget.subType == "pgcspecification") {
                    return "la-book";
                } else if (widget.subType == "pgcdeclaration") {
                    return "la-pen-square";
                } else if (widget.subType == "mroasset") {
                    return "la-copy";
                } else if (widget.subType == "mroworkrequest") {
                    return "la-file-contract";
                } else if (widget.subType == "mroworkorder") {
                    return "la la-archive";
                } else if (widget.subType == "mbom") {
                    return "la-cog";
                } else if (widget.subType == "program") {
                    return "la-calendar";
                } else if (widget.subType == "operation") {
                    return "fa-bar-chart";
                } else if (widget.subType == "productionorder") {
                    return "la la-industry";
                } else if (widget.subType == "mromaintenanceplan") {
                    return "fa-gear";
                } else if (widget.subType == "problemreport") {
                    return "la-stream";
                } else if (widget.subType == "qcr") {
                    return "la la-database";
                } else if (widget.subType == "project") {
                    return "la-calendar";
                } else if (widget.subType == "requirementdocument") {
                    return "la-address-card";
                } else if (widget.subType == "inspectionplan") {
                    return "la la-warehouse";
                } else if (widget.subType == "inspection") {
                    return "la-award";
                } else if (widget.subType == "ncr") {
                    return "la la-school";
                } else if (widget.subType == "manufacturer") {
                    return "flaticon-office42";
                } else if (widget.subType == "mfrsupplier") {
                    return "fa-th";
                } else if (widget.subType == "plmworkflow") {
                    return "flaticon-plan2";
                } else if (widget.subType == "pdmvault") {
                    return "la-database";
                } else {
                    return "fa-gear";
                }
            }

            vm.checkLongNames = checkLongNames;
            function checkLongNames(widget) {
                if (widget.subType == 'mromaintenanceplan' || widget.subType == 'requirementdocument' || widget.subType == 'productionorder' || widget.subType == 'manufacturerpart') {
                    return widget.subType;
                } else {
                    return null;
                }
            }

            vm.showSavedSearches = showSavedSearches;
            function showSavedSearches() {
                var options = {
                    title: $scope.saved,
                    template: 'app/desktop/modules/item/showSavedSearches.jsp',
                    controller: 'SavedSearchesController as searchVm',
                    resolve: 'app/desktop/modules/item/savedSearchesController',
                    width: 700,
                    showMask: true,
                    data: {
                        type: "ALL",
                        dashboardCount: vm.savedSearchesCount
                    },
                    callback: function (result) {
                        showSavedResult(result);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function showSavedResult(savedSearch) {
                if (savedSearch.searchObjectType == 'ITEM') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $rootScope.allItemsLoad = false;
                    $state.go("app.items.all");
                } else if (savedSearch.searchObjectType == 'CHANGE') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $rootScope.allEcosLoad = false;
                    $state.go("app.changes.eco.all");
                } else if (savedSearch.searchObjectType == 'MANUFACTURER') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $state.go("app.mfr.all");
                } else if (savedSearch.searchObjectType == 'MANUFACTURERPART') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $state.go("app.mfr.mfrparts.all");
                }
            }

            vm.addWidgets = addWidgets;
            function addWidgets() {
                var options = {
                    title: "Add Widgets",
                    template: 'app/desktop/modules/home/widgets/widgetSidepanel.jsp',
                    controller: 'WidgetsController as userWidgetsVm',
                    resolve: 'app/desktop/modules/home/widgets/widgetSidepanelController.js',
                    width: 400,
                    showMask: true,
                    buttons: [
                        {text: "Update", broadcast: 'app.widget.new'}
                    ],
                    callback: function (result) {
                        vm.homeWidgets = [];
                        if (result != null && result.userWidgetJson != null && result.userWidgetJson != "") {
                            var userWidgets = JSON.parse(result.userWidgetJson);
                            sortWidgets(userWidgets);
                        }
                        $rootScope.hideSidePanel();
                        $rootScope.widgets = $rootScope.backupWidgets;
                        $rootScope.showSuccessMessage(homeWidgetsUpdateMessage);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.sortedWidgets = [];
            vm.beforeSortedWidgets = [];
            vm.newWidgets = [];
            vm.countWidgets = [];
            vm.sizeOfWidgets = [];
            function loadUserPreferences() {
                LoginService.getUserPreference($rootScope.loginPersonDetails.id).then(
                    function (data) {
                        vm.userPreference = data;
                        if (vm.userPreference.userWidgetJson != null && vm.userPreference.userWidgetJson != "") {
                            var widgets = JSON.parse(vm.userPreference.userWidgetJson);
                            sortWidgets(widgets);
                        } else {
                            if ($rootScope.widgets.length > 0) {
                                sortWidgets($rootScope.widgets);
                            }
                        }
                    }
                );
            }

            $rootScope.sortWidgets = sortWidgets;
            function sortWidgets(widgets) {
                vm.sortedWidgets = [];
                vm.newWidgets = [];
                vm.countWidgets = [];
                vm.homeWidgets = [];
                vm.sizeOfWidgets = [];
                vm.beforeSortedWidgets = [];
                var i = 0;
                angular.forEach(widgets, function (widget) {
                    vm.homeWidgets = [];
                    if (widget.privilege == 'view') {
                        var count = getObjectCount(widget);
                        if (count > 0) {
                            vm.sizeOfWidgets.push(widget);
                        }
                        else {
                            vm.countWidgets.push(widget);
                        }

                    }
                    if (widget.privilege == 'create') {
                        vm.newWidgets.push(widget);
                    }
                    i++;
                    if (widgets.length == i) {
                        vm.beforeSortedWidgets = vm.sizeOfWidgets.concat(vm.countWidgets);
                        vm.sortedWidgets = vm.beforeSortedWidgets.concat(vm.newWidgets);
                        loadHomeWidgets(vm.sortedWidgets);
                    }
                });
            }


            function loadHomeWidgets(userWidgets) {
                if (slideTimeout != null && slideTimeout != undefined) {
                    clearTimeout(slideTimeout);
                    slideIndex = 1;
                }
                vm.homeWidgets = [[]];
                var rows = Math.ceil(userWidgets.length / 12);
                var count = -1;
                for (var i = 0; i < rows; i++) {
                    vm.homeWidgets[i] = [];

                    for (var j = 0; j < 12; j++) {
                        count++;
                        if (userWidgets[count] !== null && userWidgets[count] !== undefined) {
                            vm.homeWidgets[i][j] = userWidgets[count];
                        }
                    }
                }
                $timeout(function () {
                    showSlides();
                }, 1000)
            }

            var slideIndex = 1;

            $scope.plusSlides = plusSlides;
            function plusSlides(n) {
                showSlides(slideIndex += n);
            }

            $scope.currentSlide = currentSlide;
            function currentSlide(n) {
                slideIndex = n;
                clearTimeout(slideTimeout);
                showSlides();
            }

            var slideTimeout;

            function showSlides() {
                var i;
                var slides = document.getElementsByClassName("mySlides");
                var dots = document.getElementsByClassName("slide-dot");
                for (i = 0; i < slides.length; i++) {
                    slides[i].style.display = "none";
                }
                for (i = 0; i < dots.length; i++) {
                    dots[i].className = dots[i].className.replace(" dot-active", "");
                }
                if (slideIndex > slides.length) {
                    slideIndex = 1
                }
                if (slides.length > 0) {
                    slides[slideIndex - 1].style.display = "block";
                    dots[slideIndex - 1].className += " dot-active";
                    /*slideIndex++;
                     if (vm.homeWidgets.length > 1) {
                     slideTimeout = setTimeout(showSlides, 10000);
                     }*/
                }
            }


            $rootScope.getDashBoardCounts = getDashBoardCounts;
            function getDashBoardCounts() {
                loadObjectsCounts();
                loadUserTasksCounts();
                loadConversationsCount();
                loadSavedSearchesCount();
            }

            (function () {
                init();
                setGreeting();
                updateDate();

                $timeout(function () {
                    $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                    if ($rootScope.localStorageLogin != null) {
                        owner = $rootScope.localStorageLogin.login.person.id;
                    }
                    $rootScope.getDashBoardCounts();
                });

                var intervalPromise = $interval(updateDate, 1000);

                $scope.$on("$destroy", function () {
                    $interval.cancel(intervalPromise);
                });
            })();

        }
    }
);