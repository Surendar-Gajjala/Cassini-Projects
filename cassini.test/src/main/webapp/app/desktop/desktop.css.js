/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
define([], function () {
        var viewsCss = {
                global: [{
                    href: 'app/assets/bower_components/split-pane/split-pane.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/jquery.easyui/themes/icon.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/jquery.easyui/themes/bootstrap/easyui.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/dropzone/dist/dropzone.css',
                    persist: true
                }, {
                    href: 'app/assets/css/jquery.orgchart.css',
                    persist: true
                },{
                    href: 'app/assets/bower_components/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.css',
                    persist: true
                }, {
                    href: 'app/assets/css/desktop.imports.css',
                    persist: true
                }],
                app: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/angular-ui-select/dist/select.css',
                    persist: true
                }],
                login: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/css/app/desktop/login.css',
                    persist: true
                }]
            },
            getViewCss = function (route) {
                var cssList = viewsCss.global;

                if (viewsCss[route] != null && route != null && route != 'global') {
                    cssList = cssList.concat(viewsCss[route]);
                }

                return cssList;
            };
        return {
            getViewCss: getViewCss
        };
    }
);