define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.accomm': {
                    url: '/accomm',
                    abstract: true,
                    templateUrl: 'app/phone/modules/accomm/accommMainView.jsp',
                    controller: 'AccommodationMainController as accommMainVm',
                    resolve: ['app/phone/modules/accomm/accommMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.accomm.all': {
                    url: '/all',
                    templateUrl: 'app/phone/modules/accomm/all/allAccommsView.jsp',
                    controller: 'AllAccommodationsController as allAccommsVm',
                    resolve: ['app/phone/modules/accomm/all/allAccommsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.accomm.details': {
                    url: '/details/:deptId',
                    templateUrl: 'app/phone/modules/accomm/details/accommDetailsView.jsp',
                    controller: 'AccommodationDetailsController as accommDetailsVm',
                    resolve: ['app/phone/modules/accomm/details/accommDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.accomm.new': {
                    url: '/new',
                    templateUrl: 'app/phone/modules/accomm/new/newAccommView.jsp',
                    controller: 'NewAccommodationController newAccommVm',
                    resolve: ['app/phone/modules/accomm/new/newAccommController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);