define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.person': {
                    url: '/person',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/person/personMainView.jsp',
                    controller: 'PersonMainController as personMainVm',
                    resolve: ['app/desktop/modules/person/personMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.person.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/person/all/allPersonsView.jsp',
                    controller: 'AllPersonsController as allPersonsVm',
                    resolve: ['app/desktop/modules/person/all/allPersonsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.person.details': {
                    url: '/details/:personId',
                    templateUrl: 'app/desktop/modules/person/details/personDetailsView.jsp',
                    controller: 'PersonDetailsController as personDetailsVm',
                    resolve: ['app/desktop/modules/person/details/personDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.person.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/person/new/newPersonView.jsp',
                    controller: 'NewPersonController as newPersonVm',
                    resolve: ['app/desktop/modules/person/new/newPersonController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.person.emergencyContact': {
                    url: '/emergencyContact',
                    templateUrl: 'app/desktop/modules/person/details/tabs/emergencyContact/personEmergencyContactView.jsp',
                    controller: 'PersonEmergencyContactController as personEmergencyContactVm',
                    resolve: ['app/desktop/modules/person/details/tabs/emergencyContact/personEmergencyContactController'],
                    css: cssConfig.getViewCss('app')
                },


            }
        };
    }
);