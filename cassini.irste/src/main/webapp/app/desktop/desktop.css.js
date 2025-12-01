define([], function () {
        var viewsCss = {
                global: [{
                    href: 'app/assets/css/desktop.imports.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/animate.css/animate.css',
                    persist: true
                }, {
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
                }],
                app: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/angular-ui-select/dist/select.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/bpmn-js/dist/assets/diagram-js.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/angular-xeditable/dist/css/xeditable.css',
                    persist: true
                }],
                login: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/cassini-platform/css/app/desktop/login.css',
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