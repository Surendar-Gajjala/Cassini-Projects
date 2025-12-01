define(
    [
        'app/desktop/desktop.app',
        'jquery-ui'
    ],

    function (module) {
        module.directive('includeReplace', ['$compile', '$translate', '$timeout', function ($compile, $timeout, $translate) {
            return {
                require: 'ngInclude',
                restrict: 'A',
                link: function (scope, el, attrs) {
                    $compile(el.children())(scope);
                    el.replaceWith(el.children());
                }
            };
        }]);

        module.directive('iframeOnload', [function () {
                return {
                    scope: {
                        callBack: '&iframeOnload'
                    },
                    link: function (scope, element, attrs) {
                        element.on('load', function () {
                            return scope.callBack();
                        });
                    }
                }
            }]
        );

        module.directive('ngFileModel', ['$parse', function ($parse) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var model = $parse(attrs.ngFileModel);
                    var isMultiple = attrs.multiple;
                    var modelSetter = model.assign;
                    element.bind('change', function () {
                        var values = [];
                        angular.forEach(element[0].files, function (item) {
                            var value = item;
                            values.push(value);
                        });
                        scope.$apply(function () {
                            if (isMultiple) {
                                modelSetter(scope, values);
                            } else {
                                modelSetter(scope, values[0]);
                            }
                        });
                    });
                }
            };
        }]);

        module.directive('contentpanel', ['$compile', '$timeout',
                function ($compile, $timeout) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustContentPanelHeight()
                            });

                            function adjustContentPanelHeight() {
                                var height = $(window).height();
                                $(elm).height(height - 217);
                            }

                            $timeout(function () {
                                adjustContentPanelHeight();
                            }, 1000);

                            adjustContentPanelHeight();
                        }
                    };
                }
            ]
        );

        module.directive('projectHeaderBar', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                restrict: 'A',
                link: function (scope, el, attrs) {
                    $(el).find('.topmenu-item-link').bind('click', function () {
                        $(el).find('.topmenu-item-link').removeClass('active');
                        //$(this).addClass('active');
                    });
                }
            };
        }]);


        module.directive('datePicker',
            ['$parse', '$rootScope',
                function ($parse, $rootScope) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var createDate = null;
                            var workingDays = null;
                            var dateFormat = 'dd/mm/yy';
                            if ($rootScope.applicationDateFormat != undefined && $rootScope.applicationDateFormat != null) {
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'dd mm yy';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'dd.mm.yy';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'mm-dd-yy';
                                }
                            }
                            if ($rootScope.workingDays == 5) workingDays = $rootScope.noWeekEndsOrHolidays;
                            else if ($rootScope.workingDays == 6) workingDays = $rootScope.noSundayOrHolidays;
                            else if ($rootScope.workingDays == 7) workingDays = $rootScope.noHolidays;
                            var ngModel = $parse(attrs.ngModel);
                            if (attrs["minDate"] != null && attrs["minDate"] != "" && attrs["minDate"] != undefined) {
                                var splitDate = attrs["minDate"].split(",");
                                createDate = new Date(splitDate[0].toString().split("/").reverse().join("-"));
                            }
                            $(elem).datepicker({
                                dateFormat: dateFormat,
                                minDate: createDate,
                                changeMonth: true,
                                changeYear: true,
                                beforeShowDay: workingDays,
                                onSelect: function (dateText) {
                                    scope.$apply(function (scope) {
                                        ngModel.assign(scope, dateText);
                                    });
                                }
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );

        module.directive('datePickerEdit',
            ['$parse', '$rootScope',
                function ($parse, $rootScope) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var workingDays = null;
                            var dateFormat = 'dd/mm/yy';
                            if ($rootScope.applicationDateFormat != undefined && $rootScope.applicationDateFormat != null) {
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'dd mm yy';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'dd.mm.yy';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'mm-dd-yy';
                                }
                            }
                            if ($rootScope.workingDays == 5) workingDays = $rootScope.noWeekEndsOrHolidays;
                            else if ($rootScope.workingDays == 6) workingDays = $rootScope.noSundayOrHolidays;
                            else if ($rootScope.workingDays == 7) workingDays = $rootScope.noHolidays;
                            $(elem).datepicker({
                                dateFormat: dateFormat,
                                changeMonth: true,
                                changeYear: true,
                                beforeShowDay: workingDays,
                                onSelect: function (dateText) {
                                    scope.$apply(function (scope) {
                                        ngModel.assign(scope, dateText);
                                    });
                                }
                            });
                        }
                    };
                }
            ]
        );

        module.directive('inwardDatePicker',
            ['$parse', '$rootScope',
                function ($parse, $rootScope) {
                    var today = new Date();
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var workingDays = null;
                            var dateFormat = 'dd/mm/yy';
                            if ($rootScope.applicationDateFormat != undefined && $rootScope.applicationDateFormat != null) {
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'dd mm yy';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'dd.mm.yy';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'mm-dd-yy';
                                }
                            }
                            if ($rootScope.workingDays == 5) workingDays = $rootScope.noWeekEndsOrHolidays;
                            else if ($rootScope.workingDays == 6) workingDays = $rootScope.noSundayOrHolidays;
                            else if ($rootScope.workingDays == 7) workingDays = $rootScope.noHolidays;
                            $(elem).datepicker({
                                autoclose: true,
                                endDate: "today",
                                maxDate: today,
                                dateFormat: dateFormat,
                                changeMonth: true,
                                changeYear: true,
                                beforeShowDay: workingDays,
                                onSelect: function (dateText) {
                                    scope.$apply(function (scope) {
                                        ngModel.assign(scope, dateText);
                                    });
                                }
                            });
                        }
                    };
                }
            ]
        );

        module.directive('expiryDatePicker',
            ['$parse', '$rootScope',
                function ($parse, $rootScope) {
                    var today = new Date();
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var workingDays = null;
                            var dateFormat = 'dd/mm/yy';
                            if ($rootScope.applicationDateFormat != undefined && $rootScope.applicationDateFormat != null) {
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'dd mm yy';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'dd.mm.yy';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'mm-dd-yy';
                                }
                            }
                            if ($rootScope.workingDays == 5) workingDays = $rootScope.noWeekEndsOrHolidays;
                            else if ($rootScope.workingDays == 6) workingDays = $rootScope.noSundayOrHolidays;
                            else if ($rootScope.workingDays == 7) workingDays = $rootScope.noHolidays;
                            $(elem).datepicker({
                                autoclose: true,
                                minDate: 'dateToday',
                                dateFormat: dateFormat,
                                changeMonth: true,
                                changeYear: true,
                                beforeShowDay: workingDays,
                                onSelect: function (dateText) {
                                    scope.$apply(function (scope) {
                                        ngModel.assign(scope, dateText);
                                    });
                                }
                            });
                        }
                    };
                }
            ]
        );

        module.directive('startDatePicker',
            ['$parse', '$rootScope',
                function ($parse, $rootScope) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var workingDays = null;
                            var dateFormat = 'dd/mm/yy';
                            if ($rootScope.applicationDateFormat != undefined && $rootScope.applicationDateFormat != null) {
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'dd mm yy';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'dd.mm.yy';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'mm-dd-yy';
                                }
                            }
                            if ($rootScope.workingDays == 5) workingDays = $rootScope.noWeekEndsOrHolidays;
                            else if ($rootScope.workingDays == 6) workingDays = $rootScope.noSundayOrHolidays;
                            else if ($rootScope.workingDays == 7) workingDays = $rootScope.noHolidays;
                            $(elem).datepicker({
                                dateFormat: dateFormat,
                                minDate: 'dateToday',
                                changeMonth: true,
                                changeYear: true,
                                beforeShowDay: workingDays,
                                onSelect: function (dateText) {
                                    var dt2 = $('#plannedStartDate');
                                    var startDate = $(this).datepicker('getDate');
                                    var minDate = $(this).datepicker('getDate');
                                    dt2.datepicker('setDate', minDate);
                                    startDate.setDate(startDate.getDate());
                                    dt2.datepicker('option', 'minDate', minDate);
                                    dt2.datepicker('option', 'minDate', startDate);
                                    scope.$apply(function (scope) {
                                        ngModel.assign(scope, dateText);
                                    });
                                }
                            });
                        }
                    };
                }
            ]
        );

        module.directive('startFinishDatePicker',
            ['$parse', '$rootScope',
                function ($parse, $rootScope) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var workingDays = null;
                            var dateFormat = 'dd/mm/yy';
                            if ($rootScope.applicationDateFormat != undefined && $rootScope.applicationDateFormat != null) {
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'dd mm yy';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'dd.mm.yy';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'mm-dd-yy';
                                }
                            }
                            if ($rootScope.workingDays == 5) workingDays = $rootScope.noWeekEndsOrHolidays;
                            else if ($rootScope.workingDays == 6) workingDays = $rootScope.noSundayOrHolidays;
                            else if ($rootScope.workingDays == 7) workingDays = $rootScope.noHolidays;
                            $(elem).datepicker({
                                dateFormat: dateFormat,
                                minDate: 'dateToday',
                                changeMonth: true,
                                changeYear: true,
                                beforeShowDay: workingDays,
                                onSelect: function (dateText) {
                                    var sDate = $('#plannedStartDate');
                                    var fDate = $('#plannedFinishDate');
                                    var startDate = sDate.datepicker('getDate');
                                    fDate.datepicker('option', 'minDate', startDate);
                                    scope.$apply(function (scope) {
                                        ngModel.assign(scope, dateText);
                                    });
                                }
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );


        module.directive('finishDatePicker',
            ['$parse', '$rootScope',
                function ($parse, $rootScope) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var workingDays = null;
                            var dateFormat = 'dd/mm/yy';
                            if ($rootScope.applicationDateFormat != undefined && $rootScope.applicationDateFormat != null) {
                                if ($rootScope.applicationDateFormat == "dd MM yyyy HH:mm:ss") {
                                    dateFormat = 'dd mm yy';
                                } else if ($rootScope.applicationDateFormat == "dd.MM.yyyy, HH:mm:ss") {
                                    dateFormat = 'dd.mm.yy';
                                } else if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                                    dateFormat = 'mm-dd-yy';
                                }
                            }
                            if ($rootScope.workingDays == 5) workingDays = $rootScope.noWeekEndsOrHolidays;
                            else if ($rootScope.workingDays == 6) workingDays = $rootScope.noSundayOrHolidays;
                            else if ($rootScope.workingDays == 7) workingDays = $rootScope.noHolidays;
                            $(elem).datepicker({
                                dateFormat: dateFormat,
                                changeMonth: true,
                                changeYear: true,
                                beforeShowDay: workingDays,
                                onSelect: function (dateText) {
                                    var sDate = $('#plannedStartDate');
                                    var fDate = $('#plannedFinishDate');
                                    var startDate = sDate.datepicker('getDate');
                                    fDate.datepicker('option', startDate);
                                    scope.$apply(function (scope) {
                                        ngModel.assign(scope, dateText);
                                    });
                                }
                            });
                        }
                    };
                }
            ]
        );

        module.directive('fitcontentpanel', ['$compile', '$timeout',
                function ($compile, $timeout) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustHeight()
                            });


                            function adjustHeight() {
                                var height = $("#contentpanel").height();
                                $(elm).height(height - 5);
                            }

                            $timeout(function () {
                                adjustHeight();
                            }, 1000);

                            adjustHeight();
                        }
                    };
                }
            ]
        );

        module.directive('ngEnter', function () {
            return function (scope, element, attrs) {
                element.bind("keydown keypress", function (event) {
                    if (event.which === 13) {
                        scope.$apply(function () {
                            scope.$eval(attrs.ngEnter);
                        });

                        event.preventDefault();
                    }
                });
            };
        });


        module.directive('btSmall', function () {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var elements = angular.element(element).find(attrs.btSmall);
                    Array.prototype.forEach.call(elements, function (element) {
                        var aE = angular.element(element);
                        if (element.nodeName === 'input') {
                            aE.addClass('input-sm');
                        } else if (aE.hasClass('btn')) {
                            aE.addClass('btn-sm');
                        }
                    });
                }
            }
        });

        module.directive('imageonload', function () {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    element.bind('load', function () {
                        //call the function that was passed
                        scope.$apply(attrs.imageonload);
                    });
                }
            };
        })

        module.directive('sidepanelcontent', ['$compile', '$timeout',
                function ($compile, $timeout) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustHeight()
                            });


                            function adjustHeight() {
                                var hasRightButtonsPanel = $(elm).find('#rightSidePanelButtonsPanel').length > 0;
                                var hasLeftButtonsPanel = $(elm).find('#leftSidePanelButtonsPanel').length > 0;
                                if (hasRightButtonsPanel) {
                                    var rightHeight = $("#rightSidePanel").outerHeight();
                                    $(elm).find('#rightSidePanelContent').height(rightHeight - 100);
                                } else {
                                    var rightHeight = $("#rightSidePanel").outerHeight();
                                    $(elm).find('#rightSidePanelContent').height(rightHeight - 50);
                                }

                                if (hasLeftButtonsPanel) {
                                    var leftHeight = $("#leftSidePanel").outerHeight();
                                    $(elm).find('#leftSidePanelContent').height(leftHeight - 100);
                                } else {
                                    var leftHeight = $("#leftSidePanel").outerHeight();
                                    $(elm).find('#leftSidePanelContent').height(leftHeight - 50);
                                }
                            }

                            $timeout(function () {
                                adjustHeight();
                            }, 1000);
                        }
                    };
                }
            ]
        );

        module.directive('irstecontentpanel', ['$compile', '$timeout',
                function ($compile, $timeout) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustContentPanelHeight()
                            });

                            function adjustContentPanelHeight() {
                                var height = $(window).height();
                                var imageHeight = $("#irsteHeaderImage").outerHeight();
                                var headerHeight = $("#headerbar").outerHeight();
                                $(elm).height(height - (imageHeight + headerHeight));
                            }

                            $timeout(function () {
                                adjustContentPanelHeight();
                            }, 1000);

                            adjustContentPanelHeight();
                        }
                    };
                }
            ]
        );

        module.directive('applicationfitcontent', ['$compile', '$timeout',
                function ($compile, $timeout) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustHeight()
                            });


                            function adjustHeight() {
                                var hasToolbar = $(elm).find('.view-toolbar').length > 0;
                                var height = $("#contentpanel").outerHeight();
                                if (hasToolbar) {
                                    $(elm).find('.view-content').height(height - 53);
                                } else {
                                    $(elm).find('.view-content').height(height - 4);
                                }

                            }

                            $timeout(function () {
                                adjustHeight();
                            });
                        }
                    };
                }
            ]
        );

        module.directive('applicationcontent', ['$compile', '$timeout',
                function ($compile, $timeout) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustHeight()
                            });


                            function adjustHeight() {
                                var height = $("#contentpanel").outerHeight();

                                //$(elm).height(height-5);

                                var imageHeight = $("#irsteHeaderImage").outerHeight();
                                var headerHeight = $("#headerbar").outerHeight();

                                $(elm).find('.application-wizard').height(height - (imageHeight + headerHeight));

                            }

                            $timeout(function () {
                                adjustHeight();
                            });
                        }
                    };
                }
            ]
        );

        module.directive('dateTimePicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                pickTime: false,
                                minView: 2,
                                format: 'dd/mm/yyyy',
                                autoclose: true,
                                //pickerPosition: "top-left",
                                CustomFormat: 'dd/mm/yy'
                            }).on('changeDate', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev = ev.format('DD/MM/YYYY');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );

        module.directive('irsteDatePicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                pickTime: false,
                                minView: 2,
                                format: 'dd/mm/yyyy',
                                autoclose: true,
                                pickerPosition: "top-left",
                                CustomFormat: 'dd/mm/yy',
                                endDate: new Date()
                            }).on('changeDate', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev = ev.format('DD/MM/YYYY');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );

        module.directive('futureDatePicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                pickTime: false,
                                minView: 2,
                                format: 'dd/mm/yyyy',
                                autoclose: true,
                                pickerPosition: "top-left",
                                CustomFormat: 'dd/mm/yy',
                                startDate: new Date(),
                                changeMonth: true,
                                changeYear: true,
                            }).on('changeDate', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev = ev.format('DD/MM/YYYY');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );

        module.directive('dobPicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                pickTime: false,
                                minView: 2,
                                startView: 'decade',
                                format: 'dd/mm/yyyy',
                                autoclose: true,
                                pickerPosition: "top-left",
                                CustomFormat: 'dd/mm/yy',
                                endDate: new Date(),
                                changeMonth: true,
                                changeYear: true,
                            }).on('changeDate', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev = ev.format('DD/MM/YYYY');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );

        module.directive('yearPicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                format: "yyyy",
                                startView: 'decade',
                                minView: 'decade',
                                viewSelect: 'decade',
                                autoclose: true,
                                pickerPosition: "top-left",
                            }).on('changeYear', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev = ev.format('YYYY');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);

                        }
                    };
                }
            ]
        );

        module.directive('monthPicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                format: "mm/yyyy",
                                startView: 3,
                                minView: 3,
                                autoclose: true,
                                //pickerPosition: "top-left",
                                endDate: new Date()
                            }).on('changeMonth', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev = ev.format('MM/YYYY');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);

                        }
                    };
                }
            ]
        );

        module.directive('passingYearPicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                format: "yyyy",
                                startView: 'decade',
                                minView: 'decade',
                                viewSelect: 'decade',
                                autoclose: true,
                                pickerPosition: "top-left",
                                endDate: new Date()
                            }).on('changeYear', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev = ev.format('YYYY');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);

                        }
                    };
                }
            ]
        );

        module.directive('dateHourTimePicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                format: 'dd/mm/yyyy hh:ii',
                                autoclose: true,
                                pickerPosition: "top-left",
                                linkFormat: "dd/mm/yyyy hh:ii"
                            }).on('changeDate', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev = ev.format('DD/MM/YYYY, HH:mm:ss');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );

        module.directive('timePicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var position = "top-left";
                            if (attrs.name != undefined) {
                                if (attrs.name == "timeValidation") {
                                    position = "bottom-left";
                                }
                            }
                            $(elem).datetimepicker({
                                format: 'hh:ii:00',
                                startView: "day",
                                minView: 'hour',
                                maxView: 'day',
                                viewSelect: 'hour',
                                title: "Select Time",
                                autoclose: true,
                                pickerPosition: position,
                                linkFormat: "hh:ii:00"
                            }).on('changeDate', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev.second(0);
                                    ev = ev.format('HH:mm:ss');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );

        module.directive('timePickers',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            $(elem).datetimepicker({
                                format: 'dd/mm/yyyy hh:ii:ss',
                                autoclose: true,
                                pickerPosition: "top-left",
                                linkFormat: "dd/mm/yyyy hh:ii:ss"
                            }).on('changeDate', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev.second(0);
                                    ev = ev.format('DD/MM/YYYY, HH:mm:ss');
                                    ngModel.assign(scope, ev);
                                });
                            });
                        }
                    };
                }
            ]
        );

        /* -------  Directive to Allow double Type Values -------*/

        module.directive('validNumber', function () {
            return {
                require: '?ngModel',
                link: function (scope, element, attrs, ngModelCtrl) {
                    if (!ngModelCtrl) {
                        return;
                    }

                    ngModelCtrl.$parsers.push(function (val) {
                        if (angular.isUndefined(val)) {
                            var val = '';
                        }
                        var clean = val.replace(/[^0-9\.]/g, '');
                        var decimalCheck = clean.split('.');

                        if (!angular.isUndefined(decimalCheck[1])) {
                            decimalCheck[1] = decimalCheck[1].slice(0, 6);
                            clean = decimalCheck[0] + '.' + decimalCheck[1];
                        }

                        if (val !== clean) {
                            ngModelCtrl.$setViewValue(clean);
                            ngModelCtrl.$render();
                        }
                        return clean;
                    });

                    element.bind('keypress', function (event) {
                        if (event.keyCode === 32) {
                            event.preventDefault();
                        }
                    });
                }
            };
        });


        module.directive('validDoubleNumber', function () {
            return {
                require: '?ngModel',
                link: function (scope, element, attrs, ngModelCtrl) {
                    if (!ngModelCtrl) {
                        return;
                    }

                    ngModelCtrl.$parsers.push(function (val) {
                        if (angular.isUndefined(val)) {
                            var val = '';
                        }
                        var clean = val.replace(/[^0-9\.]/g, '');
                        var decimalCheck = clean.split('.');

                        if (!angular.isUndefined(decimalCheck[1])) {
                            clean = decimalCheck[0] + '.' + decimalCheck[1];
                        }
                        if (val !== clean) {
                            ngModelCtrl.$setViewValue(clean);
                            ngModelCtrl.$render();
                        }
                        return clean;
                    });

                    element.bind('keypress', function (event) {
                        if (event.keyCode === 32) {
                            event.preventDefault();
                        }
                    });
                }
            };
        });


        /*------ Allow to Integer values only ------*/

        module.directive('numbersOnly', function () {
            return {
                require: '?ngModel',
                link: function (scope, element, attrs, ngModelCtrl) {
                    if (!ngModelCtrl) {
                        return;
                    }

                    ngModelCtrl.$parsers.push(function (val) {
                        var clean = val.replace(/[^0-9]+/g, '');
                        if (val !== clean) {
                            ngModelCtrl.$setViewValue(clean);
                            ngModelCtrl.$render();
                        }
                        return clean;
                    });

                    element.bind('keypress', function (event) {
                        if (event.keyCode === 32) {
                            event.preventDefault();
                        }
                    });
                }
            };
        });

        module.directive('customOnChange', function () {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var onChangeHandler = scope.$eval(attrs.customOnChange);
                    element.on('change', onChangeHandler);
                    element.on('$destroy', function () {
                        element.off();
                    });

                }
            };
        });

        module.directive('shiftTimePicker',
            ['$parse',
                function ($parse) {
                    return {
                        restrict: 'A',
                        require: 'ngModel',
                        link: function (scope, elem, attrs, ctrl) {
                            var ngModel = $parse(attrs.ngModel);
                            var position = "top-left";
                            if (attrs.name != undefined) {
                                if (attrs.name == "timeValidation") {
                                    position = "bottom-left";
                                }
                            }
                            $(elem).datetimepicker({
                                format: 'hh:ii',
                                startView: "day",
                                minView: 'hour',
                                maxView: 'day',
                                viewSelect: 'hour',
                                title: "Select Time",
                                autoclose: true,
                                pickerPosition: position,
                                linkFormat: "hh:ii:00"
                            }).on('changeDate', function (ev) {
                                scope.$apply(function (scope) {
                                    ev = moment(ev.date);
                                    ev.second(0);
                                    ev = ev.format('HH:mm');
                                    ngModel.assign(scope, ev);
                                });
                            }).attr('readonly', true);
                        }
                    };
                }
            ]
        );

        module.directive("contenteditable", function () {
            return {
                restrict: "A",
                require: "ngModel",
                link: function (scope, element, attrs, ngModel) {

                    if (ngModel != null && ngModel !== undefined) {
                        function read() {
                            ngModel.$setViewValue(element.html());
                        }

                        ngModel.$render = function () {
                            element.html(ngModel.$viewValue || "");
                        };

                        element.bind("blur keyup change", function () {
                            scope.$apply(read);
                            return true;
                        });
                    }
                }
            };
        });

        module.factory('$i18n', ['$translate', function ($translate) {
            return {
                getValue: function (key) {
                    var parsed = angular.element("<div></div>");
                    return parsed.html($translate.instant(key)).html();
                }
            };
        }]);
    }
);
