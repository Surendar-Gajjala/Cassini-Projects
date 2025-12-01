define(['app/app.modules'], function(app)
{
    app.directive('datePicker',
        ['$parse',
            function($parse)
            {
                return {
                    restrict: 'A',
                    link: function (scope, elem, attrs, ctrl) {
                        var ngModel = $parse(attrs.ngModel);
                        $(elem).datepicker({
                            dateFormat: 'dd/mm/yy',
                            onSelect:function (dateText, inst) {
                                scope.$apply(function(scope){
                                    // Change binded variable
                                    ngModel.assign(scope, dateText);
                                });
                            }
                        });
                    }
                };
            }
        ])
});

