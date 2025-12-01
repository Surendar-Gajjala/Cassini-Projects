/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
define([
        'app/desktop/modules/home/home.module'
    ],
    function (module) {
        module.controller('SelectWidgetsController', SelectWidgetsController);

        function SelectWidgetsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

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
                $scope.callback(vm.selectedWidgets);
            }

            function loadWidgets() {
                angular.forEach(vm.widgets, function (widget) {
                    widget.checked = false;
                    angular.forEach(vm.selectedWidgets, function (selWidget) {
                        if (widget.priority == selWidget.priority) {
                            widget.checked = true;
                        }
                    })
                })
            }

            (function () {

                if ($application.homeLoaded == true) {
                    if ($scope.data.selectedWidgets != null || $scope.data.selectedWidgets != undefined) {
                        vm.selectedWidgets = $scope.data.selectedWidgets;
                        loadWidgets();
                    }
                    $rootScope.$on("add.select.widgets", onOk);
                }
            })();
        }
    }
);
