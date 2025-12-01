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
                    href: 'app/assets/bower_components/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.css',
                    persist: true
                }, {
                    href: 'app/assets/js/gantt/skins/dhtmlxgantt_material.css',
                    persist: true
                },
                {
                    href: 'app/assets/bower_components/dropzone/dist/dropzone.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/summernote/dist/summernote.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/bootstrap-sweetalert/dist/sweetalert.css',
                    persist: true
                }, {
                    href: 'app/assets/css/app/desktop/toggle-switch.css',
                    persist: true
                },
                {
                    href: 'app/assets/css/app/desktop/4mcrf.css',
                    persist: true
                },
                {
                    href: 'app/assets/css/app/desktop/spr.css',
                    persist: true
                },],
                app: [{
                    href: 'app/assets/css/app/desktop/app.css?hash=' + window.sessionId,
                    persist: true
                }, {
                    href: 'app/assets/bower_components/angular-ui-tab-scroll/angular-ui-tab-scroll.css',
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
                }, {
                    href: 'app/assets/js/select2.min.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/flag-icon-css/css/flag-icon.css',
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