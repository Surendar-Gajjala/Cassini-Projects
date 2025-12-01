define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/desktop/modules/home/widgets/folders/foldersWidgetController',
        'app/desktop/modules/home/widgets/items/latestRevUpdateItemsController',
        'app/desktop/modules/home/widgets/savedSearch/savedSearchController',
        'app/desktop/modules/home/widgets/mfrparts/mostusedMfrPartsWidController',
        'app/desktop/modules/home/widgets/ecos/ecosWidgetController',
        'app/desktop/modules/home/widgets/tasks/myActivityTasksController',
        'app/desktop/modules/home/widgets/recentlyVisited/recentlyVisitedController',
        'app/desktop/modules/home/widgets/requirements/requirementsController',
        'app/desktop/modules/home/widgets/subscribes/subscribesWidgetController',
        'app/desktop/modules/home/widgets/workflows/workflowWidgetController',
        'app/desktop/modules/home/external/externalUserController'

    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate) {

            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = $translate.instant("HOME_TITLE");
            $rootScope.viewInfo.showDetails = true;



            var vm = this;


            vm.addAndRemoveWidgets = addAndRemoveWidgets;
            vm.selectedWidgets = [];

            var parsed = angular.element("<div></div>");

            var latestRevisionUpdateItems = $translate.instant("LATEST_REVISION_UPDATE_ITEMS");
            var savedSearchItems = parsed.html($translate.instant("SAVED_SEARCHES")).html();
            var mostUsedMfrParts = parsed.html($translate.instant("MOST_USED_MFR_PARTS")).html();
            var releasedEcos = parsed.html($translate.instant("PENDING_ECO")).html();
            var myTasks = parsed.html($translate.instant("MY_TASKS")).html();
            var recentlyVisited = parsed.html($translate.instant("RECENTLY_VISITED")).html();
            var widgets = $translate.instant("WIDGETS");
            var add = parsed.html($translate.instant("UPDATE")).html();
            var selectedWidgetsSuccessMessage = parsed.html($translate.instant("SELECTED_WIDGETS_SUCCESS_MESSAGE")).html();
            var myRequirements = parsed.html($translate.instant("MY_REQUIREMENTS")).html();
            var mySubscribes = parsed.html($translate.instant("MY_SUBSCRIBES")).html();
            var overViewRequirements = $translate.instant("OVERVIEW_REQUIREMENTS");

            var folders = $translate.instant("FOLDERS_TITLE");

            vm.showFoldersTree = showFoldersTree;

            function showFoldersTree() {
                var options = {
                    side: 'left',
                    title: folders,
                    template: 'app/desktop/modules/home/widgets/folders/foldersWidgetView.jsp',
                    controller: 'FoldersWidgetController as foldersVm',
                    resolve: 'app/desktop/modules/home/widgets/folders/foldersWidgetController',
                    width: 300,
                    callback: function () {

                    }
                };
                $rootScope.showSidePanel(options);
            }


            vm.widgets = [
                {
                    id: "widgets.myTasks",
                    name: myTasks,
                    permission: true,
                    priority: 1,
                    source: "app/desktop/modules/home/widgets/tasks/myActivityTasksView.jsp",
                    controller: "MyActivityTasksController as myActivityTasksVm"
                },
                {
                    id: "widgets.recentlyVisited",
                    name: recentlyVisited,
                    permission: true,
                    priority: 2,
                    source: "app/desktop/modules/home/widgets/recentlyVisited/recentlyVisitedWidget.jsp",
                    controller: "RecentlyVisitedController as recentlyVisitedVm"
                },
                {
                    id: "widgets.savedSearchItems",
                    name: savedSearchItems,
                    permission: true,
                    priority: 3,
                    source: "app/desktop/modules/home/widgets/savedSearch/savedSearchWidget.jsp",
                    controller: "SavedSearchController as savedSearchWidgetVm"
                },
                {
                    id: "widgets.releasedEcos",
                    name: releasedEcos,
                    permission: true,
                    priority: 4,
                    source: "app/desktop/modules/home/widgets/ecos/ecosWidget.jsp",
                    controller: "EcoWidgetController as ecoWigVm"
                },

                {
                    id: "widgets.myRequirements",
                    name: myRequirements,
                    permission: true,
                    priority: 5,
                    source: "app/desktop/modules/home/widgets/requirements/requirementsWidget.jsp",
                    controller: "RequirementsController as requirementsWidgetVm"
                }
                ,
                {
                    id: "widgets.mySubscribes",
                    name: mySubscribes,
                    permission: true,
                    priority: 6,
                    source: "app/desktop/modules/home/widgets/subscribes/subscribesWidget.jsp",
                    controller: "SubscribesController as subscribesWidgetVm"
                }

            ];

            function addAndRemoveWidgets() {
                var options = {
                    title: widgets,
                    template: 'app/desktop/modules/home/selectWidgets.jsp',
                    controller: 'SelectWidgetsController as selectWidgetVm',
                    resolve: 'app/desktop/modules/home/selectWidgetsController.js',
                    width: 500,
                    data: {
                        widgets: vm.widgets,
                        selectedWidgets: vm.selectedWidgets
                    },
                    buttons: [
                        {text: add, broadcast: 'add.select.widgets'}
                    ],
                    callback: function (result) {
                        vm.selectedWidgets = result;
                        $window.localStorage.setItem("plm_widgets", JSON.stringify(vm.selectedWidgets));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedWidgetsSuccessMessage);
                        }
                        addWidgets();
                    }
                };

                $rootScope.showSidePanel(options);
            }



            function addWidgets() {
                var widgetsHtml = "";
                var i = 0;
                var mostUsedMfrParts = parsed.html($translate.instant("MOST_USED_MFR_PARTS")).html();
                vm.selectedWidgets.sort(dynamicSort("priority"));
                angular.forEach(vm.selectedWidgets, function (widget) {

                    if ((widget.id === 'widgets.myTasks') && ($rootScope.hasPermission('project','view') || $rootScope.hasPermission('project','all') || $rootScope.hasPermission('admin','all'))) {
                        i++;
                        if (i < 3) {
                            widgetsHtml += "<div class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else if (i < 5) {
                            widgetsHtml += "<div style = 'top:30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else {
                            widgetsHtml += "<div style = 'top:60px;padding-bottom: 30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                    }
                    else if (widget.id === 'widgets.recentlyVisited') {
                        i++;
                        if (i < 3) {
                            widgetsHtml += "<div class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else if (i < 5) {
                            widgetsHtml += "<div style = 'top:30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else {
                            widgetsHtml += "<div style = 'top:60px;padding-bottom: 30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                    } else if (widget.id === 'widgets.savedSearches') {
                        i++;
                        if (i < 3) {
                            widgetsHtml += "<div class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else if (i < 5) {
                            widgetsHtml += "<div style = 'top:30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else {
                            widgetsHtml += "<div style = 'top:60px;padding-bottom: 30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                    }
                    else if ((widget.id === 'widgets.pendingEcos') && ($rootScope.hasPermission('change','eco','view') || $rootScope.hasPermission('change','eco','all'))) {
                        i++;
                        if (i < 3) {
                            widgetsHtml += "<div class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else if (i < 5) {
                            widgetsHtml += "<div style = 'top:30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else {
                            widgetsHtml += "<div style = 'top:60px;padding-bottom: 30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                    } else if (widget.id === 'widgets.myRequirements') {
                        i++;
                        if (i < 3) {
                            widgetsHtml += "<div class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else if (i < 5) {
                            widgetsHtml += "<div style = 'top:30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else {
                            widgetsHtml += "<div style = 'top:60px;padding-bottom: 30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                    }

                    else if (widget.id === 'widgets.mySubscribes') {
                        i++;
                        if (i < 3) {
                            widgetsHtml += "<div class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else if (i < 5) {
                            widgetsHtml += "<div style = 'top:30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                        else {
                            widgetsHtml += "<div style = 'top:60px;padding-bottom: 30px;' class='col-md-6' ng-controller='" + widget.controller + "'>";
                            widgetsHtml += "<div ng-include=\"'" + widget.source + "'\"></div></div>";
                        }
                    }

                });

                $timeout(function () {
                    var widgetElement = angular.element($("#addWidgetId"));
                    widgetElement.empty();
                    widgetElement.append(widgetsHtml);

                    var el = angular.element($("#addWidgetId"));
                    if (el.length > 0) {
                        var $localScope = el.scope();
                        var $injector = el.injector();

                        $injector.invoke(function ($compile) {
                            $compile(el)($localScope);
                        });
                    }

                }, 500)
            }


            function loadWidgets() {
                if (validateJSON()) {
                    vm.selectedWidgets = JSON.parse($window.localStorage.getItem("plm_widgets"));
                    if (vm.selectedWidgets == null) {
                        $window.localStorage.setItem("plm_widgets", JSON.stringify(vm.widgets));
                        vm.selectedWidgets = JSON.parse($window.localStorage.getItem("plm_widgets"));
                        addWidgets();
                    } else {
                        var recentlyVisitedExist = false;
                        angular.forEach(vm.selectedWidgets, function (widget) {
                            if (widget.priority == 2) {
                                recentlyVisitedExist = true;
                            }
                        });

                        if (!recentlyVisitedExist) {
                            vm.selectedWidgets.unshift({
                                name: recentlyVisited,
                                permission: true,
                                priority: 2,
                                source: "app/desktop/modules/home/widgets/recentlyVisited/recentlyVisitedWidget.jsp",
                                controller: "RecentlyVisitedController as recentlyVisitedVm"
                            });
                            addWidgets();
                        } else {
                            addWidgets();
                        }
                    }

                } else {
                    var recentlyVisitedExist = false;
                    angular.forEach(vm.selectedWidgets, function (widget) {
                        if (widget.priority == 2) {
                            recentlyVisitedExist = true;
                        }
                    });

                    if (!recentlyVisitedExist) {
                        vm.selectedWidgets.unshift({
                            name: recentlyVisited,
                            permission: true,
                            priority: 2,
                            source: "app/desktop/modules/home/widgets/recentlyVisited/recentlyVisitedWidget.jsp",
                            controller: "RecentlyVisitedController as recentlyVisitedVm"
                        });
                        addWidgets();
                    } else {
                        addWidgets();
                    }

                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("plm_widgets"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function dynamicSort(property) {
                var sortOrder = 1;
                if (property[0] === "-") {
                    sortOrder = -1;
                    property = property.substr(1);
                }
                return function (a, b) {
                    /* next line works with strings and numbers,
                     * and you may want to customize it to your needs
                     */
                    var result = (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0;
                    return result * sortOrder;
                }
            }


            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $timeout(function () {
                        loadWidgets();
                    }, 500);
                });

                $scope.$on('app.widgets.add', function (event, data) {
                    addAndRemoveWidgets();
                });
            })();
        }
    }
)
;