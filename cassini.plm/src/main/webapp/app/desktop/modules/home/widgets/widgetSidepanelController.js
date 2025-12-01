define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('WidgetsController', WidgetsController);

        function WidgetsController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $translate,
                                   CommonService, LoginService) {

            var vm = this;

            vm.loading = true;
            vm.userPreference = null;
            vm.widgetJson = [];
            vm.jsonArray = [];
            vm.selectAll = false;
            function loadUserPreferences() {
                LoginService.getUserPreference($rootScope.loginPersonDetails.id).then(
                    function (data) {
                        vm.userPreference = data;
                        if (vm.userPreference.userWidgetJson != null && vm.userPreference.userWidgetJson != "") {
                            vm.jsonArray = JSON.parse(vm.userPreference.userWidgetJson);
                            vm.selectAll = $rootScope.widgets.length == vm.jsonArray.length;
                            if ($rootScope.widgets.length > 0) {
                                angular.forEach($rootScope.widgets, function (widget) {
                                    angular.forEach(vm.jsonArray, function (json) {
                                        if (json.name === widget.name) {
                                            widget.checked = true;

                                        }
                                    })
                                })
                            }
                        } else if ($rootScope.widgets.length > 0) {
                            angular.forEach($rootScope.widgets, function (widget) {
                                widget.checked = true;
                                selectWidget(widget);
                            })
                        }


                    }
                );
            }

            vm.selectAllWidget = selectAllWidget;
            function selectAllWidget() {
                if (vm.selectAll) {
                    angular.forEach($rootScope.widgets, function (widget) {
                        widget.checked = true;
                    });
                    vm.jsonArray = angular.copy($rootScope.widgets);
                } else {
                    angular.forEach($rootScope.widgets, function (widget) {
                        widget.checked = false;
                    });
                    vm.jsonArray = [];
                }
            }

            vm.selectWidget = selectWidget;
            function selectWidget(widget) {
                if (vm.jsonArray.length > 0) {
                    var index = vm.jsonArray.map(function (r) {
                        return r.id;
                    }).indexOf(widget.id);
                    if (index >= 0) {
                        vm.jsonArray.splice(index, 1);
                    }
                    else {
                        var obj = new Object();
                        obj.id = widget.id;
                        obj.name = widget.name;
                        obj.sequence = widget.id;
                        obj.privilege = widget.privilege;
                        obj.subType = widget.subType;
                        obj.isSelected = true;
                        vm.jsonArray.push(obj);
                    }
                }
                else {
                    var obj = new Object();
                    obj.id = widget.id;
                    obj.name = widget.name;
                    obj.sequence = widget.id;
                    obj.privilege = widget.privilege;
                    obj.subType = widget.subType;
                    obj.isSelected = true;
                    vm.jsonArray.push(obj);
                }
                vm.selectAll = $rootScope.widgets.length == vm.jsonArray.length;
            }


            function updatePreference() {
                $rootScope.showBusyIndicator();
                vm.userPreference.userWidgetJson = JSON.stringify(vm.jsonArray);
                LoginService.updateUserPreference(vm.userPreference).then(
                    function (data) {
                        $rootScope.$off('app.widget.new', updatePreference);
                        $rootScope.hideBusyIndicator();
                        $scope.callback(data);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

            }


            (function () {
                loadUserPreferences();
                $rootScope.$on('app.widget.new', updatePreference);
            })();
        }
    }
);