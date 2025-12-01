define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.irsteLogin': {
                    url: '/login?expired',
                    templateUrl: 'app/desktop/modules/irsteLogin/irsteloginView.jsp',
                    controller: 'IrsteLoginController as irsteLoginVm',
                    resolve: ['app/desktop/modules/irsteLogin/irsteLoginController'],
                    css: cssConfig.getViewCss('login')
                }
            }
        };
    }
);