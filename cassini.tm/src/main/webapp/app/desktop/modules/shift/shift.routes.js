define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.shift': {
                    url: '/shift',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/shift/shiftMainView.jsp',
                    controller: 'ShiftMainController as shiftMainVm',
                    resolve: ['app/desktop/modules/shift/shiftMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.shift.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/shift/all/allShiftsView.jsp',
                    controller: 'AllShiftsController as allShiftsVm',
                    resolve: ['app/desktop/modules/shift/all/allShiftsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.shift.details': {
                    url: '/details/:shiftId',
                    templateUrl: 'app/desktop/modules/shift/details/shiftDetailsView.jsp',
                    controller: 'ShiftDetailsController as shiftDetailsVm',
                    resolve: ['app/desktop/modules/shift/details/shiftDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.shift.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/shift/new/newShiftView.jsp',
                    controller: 'NewShiftController newShiftVm',
                    resolve: ['app/desktop/modules/shift/new/newShiftController'],
                    css: cssConfig.getViewCss('app')
                },

            }
        };
    }
);