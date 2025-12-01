define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.shift': {
                    url: '/shift',
                    abstract: true,
                    templateUrl: 'app/phone/modules/shift/shiftMainView.jsp',
                    controller: 'ShiftMainController as shiftMainVm',
                    resolve: ['app/phone/modules/shift/shiftMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.shift.all': {
                    url: '/all',
                    templateUrl: 'app/phone/modules/shift/all/allShiftsView.jsp',
                    controller: 'AllShiftsController as allShiftsVm',
                    resolve: ['app/phone/modules/shift/all/allShiftsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.shift.details': {
                    url: '/details/:shiftId',
                    templateUrl: 'app/phone/modules/shift/details/shiftDetailsView.jsp',
                    controller: 'ShiftDetailsController as shiftDetailsVm',
                    resolve: ['app/phone/modules/shift/details/shiftDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.shift.new': {
                    url: '/new',
                    templateUrl: 'app/phone/modules/shift/new/newShiftView.jsp',
                    controller: 'NewShiftController newShiftVm',
                    resolve: ['app/phone/modules/shift/new/newShiftController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);