define(['app/app.modules'], function(app)
{
    app.filter('convertDate', function () {
        return function (text) {
            if (text) {
                var parts = text.split("/");
                var dt = new Date(parseInt(parts[2], 10),
                    parseInt(parts[1], 10) - 1,
                    parseInt(parts[0], 10));

                return dt;
            }else {
                return '';
            };
        };
    });
});