define([], function() {
        var viewsCss = {
                global: [{
                    href: 'app/assets/css/desktop.imports.css',
                    persist: true
                },{
                    href: 'app/assets/js/loading/treasure-overlay-spinner.min.css',
                    persist: true
                },{
                    href: 'app/assets/bower_components/split-pane/split-pane.css',
                    persist: true
                },{
                    href: 'app/assets/bower_components/jquery.easyui/themes/icon.css',
                    persist: true
                },{
                    href: 'app/assets/bower_components/jquery.easyui/themes/bootstrap/easyui.css',
                    persist: true
                },{
                    href: 'app/assets/bower_components/dropzone/dist/dropzone.css',
                    persist: true
                }],
                app: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                },{
                    href: 'app/assets/template/css/bootstrap-timepicker.min.css',
                    persist: true
                },{
                    href: 'app/assets/bower_components/angular-ui-select/dist/select.css',
                    persist: true
                },{
                    href: 'app/assets/bower_components/angular-xeditable/dist/css/xeditable.css',
                    persist: true
                },{
                    href: 'app/assets/css/jquery.orgchart.css',
                    persist: true
                },{
                    href: 'app/assets/js/rzslider/rzslider.css',
                    persist: true
                }],
                login: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                },{
                    href: 'app/assets/css/app/desktop/login.css',
                    persist: true
                }],
                wbs: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                }],
                bom: [{
                    href: 'app/assets/css/app/desktop/app.css',
                    persist: true
                },{
                    href: 'app/assets/bower_components/handsontable/dist/handsontable.full.min.css',
                    persist: true
                }]
            },
            getViewCss =  function(route) {
                var cssList = viewsCss.global;

                if(viewsCss[route] != null && route != null && route != 'global') {
                    cssList = cssList.concat(viewsCss[route]);
                }

                return cssList;
            };
        return {
            getViewCss: getViewCss
        };
    }
);