/**
 * Created by annap on 04/08/2016.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.accommodation': {
                    url: '/accommodation',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/accommodation/accommodationMainView.jsp',
                    controller: 'AccommodationMainController as accommodationMainVm',
                    resolve: ['app/desktop/modules/accommodation/accommodationMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.accommodation.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/accommodation/all/accommodationView.jsp',
                    controller: 'AccommodationController as accommodationVm',
                    resolve: ['app/desktop/modules/accommodation/all/accommodationController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.accommodation.details': {
                    url: '/:accommodationId',
                    templateUrl: 'app/desktop/modules/accommodation/details/accommodationDetailsView.jsp',
                    controller: 'AccommodationDetailsController as accommodationDetailsVm',
                    resolve: ['app/desktop/modules/accommodation/details/accommodationDetailsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.accommodation.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/accommodation/new/newAccommodationView.jsp',
                    controller: 'NewAccommodationController as newAccommodationVm',
                    resolve: ['app/desktop/modules/accommodation/new/newAccommodationController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.accommodation.suit': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/accommodation/all/suitView.jsp',
                    controller: 'SuitController as suiteVm',
                    resolve: ['app/desktop/modules/accommodation/all/suitController'],
                    css: cssConfig.getViewCss('app')
                }

            }
        };
    }
);