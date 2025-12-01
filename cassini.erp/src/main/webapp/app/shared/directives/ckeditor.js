define(['app/app.modules'], function (app) {
    app.directive('ckEditor', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            require: '?ngModel',
            link: function ($scope, elm, attr, ngModel) {

                var ck = CKEDITOR.replace(elm[0], {
                    removeButtons: 'About,Source,Maximize'
                });

                ck.on('pasteState', function () {
                    $scope.$apply(function () {
                        ngModel.$setViewValue(ck.getData());
                    });
                });

                ngModel.$render = function (value) {
                    ck.setData(ngModel.$modelValue);
                };
            }
        };
    }]);
});
