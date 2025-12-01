define([
        'app/desktop/modules/home/home.module'
    ],
    function (module) {
        module.controller('SelectWidgetsController', SelectWidgetsController);

        function SelectWidgetsController($scope, $rootScope, $timeout, $translate, $state, $stateParams, $cookies, $window) {

            var vm = this;

            $scope.check = false;
            vm.selectCheck = selectCheck;
            vm.widgets = $scope.data.widgets;
            vm.selectedWidgets = [];

            function selectCheck(widget) {
                if (widget.checked) {
                    vm.selectedWidgets.push(widget);
                } else {
                    angular.forEach(vm.selectedWidgets, function (selected) {
                        if (widget.priority == selected.priority) {
                            var index = vm.selectedWidgets.indexOf(selected);
                            vm.selectedWidgets.splice(index, 1);
                        }
                    })
                }
            }

            function onOk() {
                $rootScope.hideSidePanel();
                angular.forEach(vm.widgets, function (widget) {
                    angular.forEach(vm.selectedWidgets, function (selectWidget) {
                        if (widget.name == selectWidget.name) {
                            selectWidget.priority = widget.priority;
                        }
                    })

                });
                $timeout(function () {
                    $scope.callback(vm.selectedWidgets);
                }, 1000)
            }

            var parsed = angular.element("<div></div>");
            var latestRevisionUpdateItems = $translate.instant("LATEST_REVISION_UPDATE_ITEMS");
            var savedSearchItems = $translate.instant("SAVED_SEARCHES");
            var mostUsedMfrParts = parsed.html($translate.instant("MOST_USED_MFR_PARTS")).html();
            var releasedEcos = $translate.instant("RELEASED_ECO");
            var myTasks = $translate.instant("MY_TASKS");

            function loadWidgets() {
                angular.forEach(vm.widgets, function (widget) {
                    if (widget.name == myTasks) {
                        widget.permission = $rootScope.hasPermission('project','all');
                    } else if (widget.name == latestRevisionUpdateItems) {
                        widget.permission = $rootScope.hasPermission('item','all');
                    } else if (widget.name == mostUsedMfrParts) {
                        widget.permission = $rootScope.hasPermission('manufacturer','all');
                    } else if (widget.name == releasedEcos) {
                        widget.permission = $rootScope.hasPermission('change.eco','all');
                    }
                });
                $timeout(function () {
                    angular.forEach(vm.widgets, function (widget) {
                        widget.checked = false;
                        angular.forEach(vm.selectedWidgets, function (selWidget) {
                            if (widget.priority == selWidget.priority) {
                                widget.checked = true;
                            }
                        })
                    })
                }, 200);
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

            vm.updateWidgetSeq = updateWidgetSeq;
            function updateWidgetSeq(actualRow, targetObject) {
                var actualPriority = actualRow.priority;
                var targetPriority = targetObject.priority;
                if (actualRow.priority > targetObject.priority) {
                    angular.forEach(vm.widgets, function (widget) {
                        if (widget.priority == actualRow.priority || widget.priority == targetObject.priority) {

                        } else if (targetObject.priority < widget.priority && actualRow.priority > widget.priority) {
                            widget.priority = widget.priority + 1;
                        }
                    });
                    actualRow.priority = targetPriority;
                    targetObject.priority = targetPriority + 1;
                    $timeout(function () {
                        vm.widgets.sort(dynamicSort('priority'));
                        $window.localStorage.setItem("plm_widgets", JSON.stringify(vm.widgets));
                        loadWidgets();
                    }, 1000)
                } else {
                    angular.forEach(vm.widgets, function (widget) {
                        if (widget.priority == actualRow.priority || widget.priority == targetObject.priority) {

                        } else if (targetObject.priority > widget.priority && actualRow.priority < widget.priority) {
                            widget.priority = widget.priority - 1;
                        }
                    });
                    actualRow.priority = targetPriority;
                    targetObject.priority = targetPriority - 1;
                    $timeout(function () {
                        vm.widgets.sort(dynamicSort('priority'));
                        $window.localStorage.setItem("plm_widgets", JSON.stringify(vm.widgets));
                        loadWidgets();
                    }, 1000)
                }
                $scope.$evalAsync();

            }

            (function () {

                //if ($application.homeLoaded == true) {
                    if ($scope.data.selectedWidgets != null || $scope.data.selectedWidgets != undefined) {
                        vm.selectedWidgets = $scope.data.selectedWidgets;
                        loadWidgets();
                    }
                    $rootScope.$on("add.select.widgets", onOk);
                //}
            })();
        }
    }
);
