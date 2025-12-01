define(['app/app.modules'], function (app) {
    app.directive('includeReplace', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            require: 'ngInclude',
            restrict: 'A', /* optional */
            link: function (scope, el, attrs) {
                $compile(el.children())(scope);
                el.replaceWith(el.children());
            }
        };
    }]);

    app.directive('fixDropDown', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            restrict: 'A',
            link: function (scope, el, attrs) {
                $compile(el.children())(scope);

                $(el).mouseover(function(event) {
                    var position = $(el).offset();
                    var height = $(el).outerHeight();
                    $(el).find('ul').css({top: (position.top + height), left: position.left, position: 'fixed', margin: 0});
                    $(el).find('ul').css({display: 'block'});
                });

                $(el).mouseout(function(event) {
                    $(el).find('ul').css({display: 'none'});
                });

            }
        };
    }]);

    app.directive('viewToolbar', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            restrict: 'A',
            scope: {
                'toolbarData': '='
            },

            link: function (scope, el, attrs) {
                function setupToolbar() {
                    if(scope.toolbarData != null && scope.toolbarData != undefined) {
                        var tb = $(document.getElementById(scope.toolbarData.id));
                        if(tb != null && tb != undefined) {
                            tb.remove();
                            $(el).append(tb);
                            el.attr('ng-controller', scope.toolbarData.controller);
                        }
                        $compile(el.children())(scope);

                        $(el).find('.btn').addClass('btn-sm');
                    }
                    else {
                        $(el).empty();
                    }
                }
                scope.$watch('toolbarData', function() {
                    setupToolbar();
                });

                setupToolbar();
            }
        };
    }]);

    app.directive('vTabs', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            restrict: 'A',
            link: function (scope, el, attrs) {
                //$compile(el.children())(scope);

                $("div.bhoechie-tab-menu>div.list-group>a").click(function(e) {
                    e.preventDefault();
                    $(this).siblings('a.active').removeClass("active");
                    $(this).addClass("active");
                    var index = $(this).index();
                    $("div.bhoechie-tab>div.bhoechie-tab-content").removeClass("active");
                    $("div.bhoechie-tab>div.bhoechie-tab-content").eq(index).addClass("active");

                    adjustHeight();
                });

                function adjustHeight() {

                    $timeout(function() {
                        var count = $(".list-group-item").length;
                        var h = $(".list-group-item").outerHeight();

                        var menuHeight = count * h;
                        //var menuHeight = $("div.bhoechie-tab-menu").outerHeight();
                        var contentHeight = $("div.bhoechie-tab").outerHeight();

                        if(menuHeight > contentHeight) {
                            $("div.bhoechie-tab").height(menuHeight);
                        }
                        else if(contentHeight > menuHeight) {
                            //$("div.bhoechie-tab-menu").height(contentHeight-2);
                        }
                    }, 1000);
                }

                adjustHeight();
            }
        };
    }]);

    app.directive('contentpanel', ['$compile', '$timeout',
            function ($compile, $timeout) {
                return {
                    restrict: 'A',
                    link: function ($scope, elm, attr) {
                        $(window).resize(function() {
                            adjustContentPanelHeight()
                        });


                        function adjustContentPanelHeight() {
                            var height = $(window).height();
                            $(elm).height(height - 217);
                        }

                        $timeout(function() {
                            adjustContentPanelHeight();
                        }, 1000);

                        adjustContentPanelHeight();
                    }
                };
            }
        ]
    );

    app.directive('fitcontent', ['$compile', '$timeout',
            function ($compile, $timeout) {
                return {
                    restrict: 'A',
                    link: function ($scope, elm, attr) {
                        $(window).resize(function() {
                            adjustHeight()
                        });


                        function adjustHeight() {
                            var height = $(window).height();
                            $(elm).height(height-217-50);
                        }

                        $timeout(function() {
                            adjustHeight();
                        }, 1000);

                        adjustHeight();
                    }
                };
            }
        ]
    );

    app.directive('infiniteScroll', ['$compile', '$timeout',
            function ($compile, $timeout) {
                return {
                    restrict: 'A',
                    scope: {
                        loadMore: '&'
                    },
                    link: function (scope, elem, attrs){
                        var box = elem[0];
                        var lengthThreshold = 100;
                        var lastRemaining = 9999;

                        elem.bind('scroll', function(){
                            var remaining = box.scrollHeight - (box.clientHeight + box.scrollTop);

                            if (remaining < lengthThreshold && (remaining - lastRemaining) < 0) {
                                scope.$apply(scope.loadMore)
                            }
                            lastRemaining = remaining;
                        })
                    }
                }
            }
        ]
    );

    app.directive('datePicker',
        ['$parse',
            function($parse) {
                return {
                    restrict: 'A',
                    require: 'ngModel',
                    link: function (scope, elem, attrs, ctrl) {
                        var ngModel = $parse(attrs.ngModel);
                        $(elem).datepicker({
                            dateFormat: 'dd/mm/yy',
                            onSelect:function (dateText) {
                                scope.$apply(function(scope){
                                    // Change binded variable
                                    ngModel.assign(scope, dateText);
                                });
                            }
                        });
                    }
                };
            }
        ]
    );
});
