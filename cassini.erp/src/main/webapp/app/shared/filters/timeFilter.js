define(['app/app.modules'], function(app)
{
    app.filter('time', function () {
        return function (text) {
            if (text) {
                var strArr = text.split(',');
                return strArr[1];
            }else {
                return '';
            };
        };
    });
});