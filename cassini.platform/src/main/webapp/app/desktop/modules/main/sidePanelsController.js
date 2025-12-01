define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module'
    ],
    function (module) {
        module.controller('SidePanelsController', SidePanelsController);

        function SidePanelsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            var vm = this;

            vm.rightSidePanelOptions = {
                title: "",
                visible: false,
                buttons: [],
                showBusy: false
            };
            vm.leftSidePanelOptions = {
                title: "",
                visible: false,
                buttons: [],
                showBusy: false
            };

            vm.broadcastButtonCallback = broadcastButtonCallback;
            $rootScope.showSidePanel = showSidePanel;
            $rootScope.hideSidePanel = vm.hideSidePanel = hideSidePanel;

            function clearBroadcastListeners(buttons) {
                angular.forEach(buttons, function (button) {
                    $rootScope.$$listeners[button.broadcast] = [];
                });
            }

            function showSidePanel(options) {
                $timeout(function () {
                    if (options.side == null || options.side == undefined || options.side == 'right') {
                        if (vm.rightSidePanelOptions.visible != undefined && !vm.rightSidePanelOptions.visible) {
                            clearBroadcastListeners(vm.rightSidePanelOptions.buttons);
                            showRightSidePanel(options);
                        }
                    }
                    else if (options.side == null || options.side == undefined || options.side == 'left') {
                        if (vm.leftSidePanelOptions.visible != undefined && !vm.leftSidePanelOptions.visible) {
                            clearBroadcastListeners(vm.leftSidePanelOptions.buttons);
                            showLeftSidePanel(options);
                        }
                    }
                }, 400);
            }

            vm.existedRightPanel = false;
            vm.existedLeftPanel = false;
            function showRightSidePanel(options) {
                vm.existedRightPanel = !vm.existedRightPanel;
                if (vm.existedRightPanel) {
                    require([options.resolve], function () {
                        vm.rightSidePanelOptions = options;
                        if (options.showMask != null && options.showMask != undefined &&
                            options.showMask == true) {
                            $('#contentpanel-mask').show();
                        }

                        var html = "<div ng-include=\"'" + options.template + "'\"";
                        html += "ng-controller='" + options.controller + "'></div>";

                        var h = 50 + $('#viewTitleContainer').outerHeight();
                        $("#rightSidePanel").css({top: h + 'px'});

                        $('#rightSidePanelContent').empty();
                        $('#rightSidePanelContent').append(html);


                        var el = angular.element($("#rightSidePanelContent"));
                        var $localScope = el.scope().$new();
                        $localScope.data = options.data;
                        $localScope.callback = options.callback;
                        $localScope.showBusy = function (flag) {
                            vm.rightSidePanelOptions.showBusy = flag;
                        };
                        var $injector = el.injector();

                        $injector.invoke(function ($compile) {
                            $compile(el)($localScope);
                        });

                        if (options.width == null && options.width == undefined) {
                            options.width = 500;
                        }

                        var slider_width = $('#rightSidePanel').width();

                        if (!vm.rightSidePanelOptions.visible || options.width != slider_width) {
                            $('#rightSidePanel').width(options.width);
                            $('#rightSidePanel').css({'margin-right': "-" + options.width-1 + "px"});
                            $('#rightSidePanel').animate({"margin-right": '+=' + options.width}, 'slow', function () {
                                $('#rightSidePanelButtonsPanel').css({'width': options.width + 'px'});
                                if (options.buttons != undefined && options.buttons.length > 0) {
                                    $('#rightSidePanelButtonsPanel').css({'width': options.width + 'px'});
                                    if (options.buttons != undefined && options.buttons.length == 2) {
                                        $('#rightSidePanelButtonsPanel').css({'display': 'inline-flex'});
                                    } else {
                                        //$('#rightSidePanelButtonsPanel').css({'display': 'inline'});
                                    }
                                    $('#rightSidePanelButtonsPanel').show();
                                } else {
                                    //$('#rightSidePanelButtonsPanel').hide();
                                }
                            });
                            var height = $('#rightSidePanel').outerHeight();
                            if (options.buttons != undefined && options.buttons.length > 0) {
                                $('#rightSidePanelContent').height(height - 100);
                            } else {
                                $('#rightSidePanelContent').height(height - 50);
                            }
                            vm.rightSidePanelOptions.visible = true;

                        }
                        $scope.$apply();
                        $("#rightSidePanelContent").hide().fadeIn('fast');
                    });
                }
            }

            function showLeftSidePanel(options) {
                vm.existedLeftPanel = !vm.existedLeftPanel;
                if (vm.existedLeftPanel) {
                    require([options.resolve], function () {
                        vm.leftSidePanelOptions = options;
                        if (options.showMask != null && options.showMask != undefined &&
                            options.showMask == true) {
                            $('#contentpanel-mask').show();
                        }

                        var html = "<div ng-include=\"'" + options.template + "'\"";
                        html += "ng-controller='" + options.controller + "'></div>";

                        var h = 50 + $('#viewTitleContainer').outerHeight();
                        $("#leftSidePanel").css({top: h + 'px'});

                        $('#leftSidePanelContent').empty();
                        $('#leftSidePanelContent').append(html);

                        var el = angular.element($("#leftSidePanelContent"));
                        var $localScope = el.scope().$new();
                        $localScope.data = options.data;
                        $localScope.callback = options.callback;
                        $localScope.showBusy = function (flag) {
                            vm.leftSidePanelOptions.showBusy = flag;
                        };
                        var $injector = el.injector();

                        $injector.invoke(function ($compile) {
                            $compile(el)($localScope);
                        });

                        if (options.width == null && options.width == undefined) {
                            options.width = 500;
                        }

                        var slider_width = $('#leftSidePanel').width();

                        if (!vm.leftSidePanelOptions.visible || options.width != slider_width) {
                            $('#leftSidePanel').width(options.width);
                            $('#leftSidePanel').css({'margin-left': "-" + options.width-1 + "px"});
                            $('#leftSidePanel').animate({"margin-left": '+=' + options.width}, 'slow', function () {
                                $('#leftSidePanelButtonsPanel').css({'width': options.width + 'px'});
                                if (options.buttons != undefined && options.buttons.length > 0) {
                                    if (options.buttons != undefined && options.buttons.length == 2) {
                                        $('#leftSidePanelButtonsPanel').css({'display': 'inline-flex'});
                                    } else {
                                        //$('#leftSidePanelButtonsPanel').css({'display': 'inline'});
                                    }
                                    $('#leftSidePanelButtonsPanel').show();
                                } else {
                                    //$('#leftSidePanelButtonsPanel').hide();
                                }
                            });
                            vm.leftSidePanelOptions.visible = true;
                            var height = $('#leftSidePanel').outerHeight();
                            if (options.buttons != undefined && options.buttons.length > 0) {
                                $('#leftSidePanelContent').height(height - 100);
                            } else {
                                $('#leftSidePanelContent').height(height - 50);
                            }
                        }
                        $scope.$apply();
                        $("#leftSidePanelContent").hide().fadeIn('fast');
                    });
                }
            }

            function hideSidePanel(side) {
                if (side == null || side == undefined || side == 'right') {
                    vm.existedRightPanel = false;
                    hideRightSidePanel();
                }
                else if (side == 'left') {
                    vm.existedLeftPanel = false;
                    hideLeftSidePanel();
                }
            }

            function hideRightSidePanel() {
                if (vm.rightSidePanelOptions.visible) {
                    $rootScope.$broadcast('app.rightside.panel.closing');
                    var h = 50 + $('#viewTitleContainer').outerHeight();
                    $("#rightSidePanel").css({top: h + 'px'});

                    var slider_width = $('#rightSidePanel').width();
                    $('#rightSidePanel').animate({"margin-right": '-=' + slider_width}, 'slow',
                        function () {
                            if (vm.rightSidePanelOptions.showMask != null &&
                                vm.rightSidePanelOptions.showMask != undefined &&
                                vm.rightSidePanelOptions.showMask == true) {
                                $('#contentpanel-mask').hide();
                            }
                        }
                    );
                    vm.rightSidePanelOptions.visible = false;
                    //$('#rightSidePanelButtonsPanel').hide();
                }
            }

            function hideLeftSidePanel() {
                if (vm.leftSidePanelOptions.visible) {
                    $rootScope.$broadcast('app.leftside.panel.closing');
                    var h = 50 + $('#viewTitleContainer').outerHeight();
                    $("#leftSidePanel").css({top: h + 'px'});

                    var slider_width = $('#leftSidePanel').width();
                    $('#leftSidePanel').animate({"margin-left": '-=' + slider_width}, 'slow',
                        function () {
                            if (vm.leftSidePanelOptions.showMask != null &&
                                vm.leftSidePanelOptions.showMask != undefined &&
                                vm.leftSidePanelOptions.showMask == true) {
                                $('#contentpanel-mask').hide();
                            }
                        }
                    );
                    vm.leftSidePanelOptions.visible = false;
                    //$('#leftSidePanelButtonsPanel').hide();
                }
            }

            function broadcastButtonCallback(button) {
                $rootScope.$broadcast(button.broadcast);
            }

            (function () {
                $rootScope.$on('$stateChangeStart',
                    function (event, toState, toParams, fromState, fromParams) {
                        hideSidePanel();
                        $timeout(function () {
                            hideSidePanel();
                        }, 500);
                    }
                );

                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        hideSidePanel('right');
                        hideSidePanel('left');
                    }
                });
            })();
        }
    }
);