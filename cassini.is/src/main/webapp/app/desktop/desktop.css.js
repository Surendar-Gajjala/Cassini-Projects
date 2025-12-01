define([], function () {
        var viewsCss = {
                global: [{
                    href: 'app/assets/css/desktop.imports.css',
                    persist: true
                }, {
                    href: 'app/assets/js/loading/treasure-overlay-spinner.min.css',
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
                    href: 'app/assets/bower_components/dropzone/dist/dropzone.css',
                    persist: true
                }],
                app: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/angular-ui-tab-scroll/angular-ui-tab-scroll.css',
                    persist: true
                }, {
                    href: 'app/assets/template/css/bootstrap-timepicker.min.css',
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
                }, {
                    href: 'app/assets/css/jquery.orgchart.css',
                    persist: true
                }, {
                    href: 'app/assets/js/orgChart/getorgchart.css',
                    persist: true
                }],
                login: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/cassini-platform/css/app/desktop/login.css',
                    persist: true
                }],
                wbs: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/js/gantt/skins/dhtmlxgantt_material.css?v=5.2.0',
                    persist: true
                }],
                bom: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/handsontable/dist/handsontable.full.min.css',
                    persist: true
                }],
                meeting: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }, {
                    href: 'app/assets/bower_components/ng-tags-input/ng-tags-input.min.css',
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