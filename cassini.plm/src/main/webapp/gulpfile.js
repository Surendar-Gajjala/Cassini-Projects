var gulp = require('gulp');
var bower = require('gulp-bower');

var paths = {
    bower: "./bower_components/",
    base: "app/assets/bower_components/"
};

gulp.task('bower', function() {
    return bower()
});

gulp.task('copy', ['bower'], function() {
    var bower = {
        "requirejs" : "requirejs/require.js",
        "angular" : "angular/angular.min.js",
        "angular-ui-router" : "angular-ui-router/release/angular-ui-router.min.js",
        "angular-cookies" : "angular-cookies/angular-cookies.min.js",
        "angular-bootstrap" : "angular-bootstrap/ui-bootstrap-tpls.min.js",
        "angular-css": "angular-css/angular-css.min.js",
        "angular-ui-select" : ["angular-ui-select/dist/select.min.js","angular-ui-select/dist/select.css"],
        "angular-animate" : "angular-animate/angular-animate.min.js",
        "angular-xeditable" : ["angular-xeditable/dist/js/xeditable.min.js","angular-xeditable/dist/css/xeditable.css"],
        "angular-validator" : "angular-validator/dist/angular-validator.min.js",
        "angular-flot" : "angular-flot/angular-flot.js",
        "angular-morris-chart" : "angular-morris-chart/src/angular-morris-chart.min.js",
        "angular-ui-ace" : "angular-ui-ace/ui-ace.min.js",
        "angular-daterangepicker" : "angular-daterangepicker/js/angular-daterangepicker.min.js",
        "angular-calendar" : "angular-calendar/dist/calendar.min.js",
        "jquery" : "jquery/dist/jquery.min.js",
        "jquery.easyui" : ["jquery.easyui/themes/icon.css","jquery.easyui/themes/bootstrap/easyui.css","jquery.easyui/jquery.easyui.min.js"],
        "jquery-migrate" : "jquery-migrate/jquery-migrate.min.js",
        "jquery-ui" : "jquery-ui/jquery-ui.min.js",
        "jquery-toggles" : "jquery-toggles/**/*.{js,map,css,ttf,svg,woff,eot}",
        "jquery-cookie" : "jquery-cookie/jquery.cookie.js",
        "jquery-maskedinput" : "jquery-maskedinput/dist/jquery.maskedinput.min.js",
        "jquery.validate" : "jquery.validate/dist/jquery.validate.min.js",
        "bootstrap": "bootstrap/dist/**/*.{js,map,css,ttf,svg,woff,woff2,eot}",
        "bootstrap3-wysihtml5":["bootstrap3-wysihtml5/dist/bootstrap-wysihtml5-0.0.2.min.js","bootstrap3-wysihtml5/dist/bootstrap-wysihtml5-0.0.2.css"],
        "retina.js":"retina.js/dist/retina.min.js",
        "modernizr": "modernizr/lib/cli.js",
        "mobile-detect":"mobile-detect/mobile-detect.min.js",
        "sugar" : "sugar/release/sugar.min.js",
        "moment":"moment/moment.js",
        "moment-timezone":"moment-timezone/builds/moment-timezone-with-data.min.js",
        "select2" : ["select2/dist/js/select2.min.js","select2/dist/css/select2.min.css"],
        "jquery.tagsinput":"jquery.tagsinput/src/jquery.tagsinput.js",
        "wysihtml5":"wysihtml5/dist/wysihtml5-0.2.0.min.js",
        "morris.js" :  "morris.js/morris.min.js",
        "respond":"respond/dest/respond.min.js",
        "weather-icons": "weather-icons/**/*.{js,map,css,ttf,svg,woff,eot}",
        "fullcalendar":["fullcalendar/dist/fullcalendar.min.js","fullcalendar/dist/fullcalendar.css"],
        "html5shiv":"html5shiv/dist/html5shiv.min.js",
        "split-pane":["split-pane/split-pane.js","split-pane/split-pane.css"],
        "dropzone":["dropzone/dist/dropzone.js","dropzone/dist/dropzone.css"],
        "handsontable":"handsontable/dist/handsontable.full.min.js",
        "font-awesome": "font-awesome/**/*.{js,map,css,ttf,svg,woff,woff2,eot}",
        "roboto-fontface": "roboto-fontface/**/*.{css,ttf,svg,woff,eot}",
        "animate.css":"animate.css/animate.min.css",
        "bootstrap-daterangepicker":["bootstrap-daterangepicker/daterangepicker.css","bootstrap-daterangepicker/daterangepicker.js"],
        "cassini-platform": "cassini-platform/**/*"
    };

    for(var destination in bower) {
        console.log("src:::" + bower[destination]);
        console.log("dest:::" + destination);

        if(Array.isArray(bower[destination])) {
            var temp = [];

            for( var childDestination in bower[destination]) {
                temp.push(paths.bower + bower[destination][childDestination]);
            }
            gulp.src(temp)
                .pipe(gulp.dest(paths.base + destination));
        }else {
            gulp.src(paths.bower + bower[destination])
                .pipe(gulp.dest(paths.base + destination));
        }
    }
});

gulp.task('default', ['copy']);