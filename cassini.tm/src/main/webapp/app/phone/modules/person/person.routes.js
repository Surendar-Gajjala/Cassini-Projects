define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.person': {
                    url: '/person',
                    abstract: true,
                    templateUrl: 'app/phone/modules/person/personMainView.jsp',
                    controller: 'PersonMainController as personMainVm',
                    resolve: ['app/phone/modules/person/personMainController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.person.all': {
                    url: '/all',
                    templateUrl: 'app/phone/modules/person/all/allPersonsView.jsp',
                    controller: 'AllPersonsController as allPersonsVm',
                    resolve: ['app/phone/modules/person/all/allPersonsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.person.new': {
                    url: '/new',
                    templateUrl: 'app/phone/modules/person/new/NewPersonDialogueView.jsp',
                    controller: 'NewPersonDialogueController newPersonDialogueVm',
                    resolve: ['app/phone/modules/person/new/newPersonDialogueController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.person.details': {
                    url: '/details/:personId',
                    templateUrl: 'app/phone/modules/person/details/personDetailsView.jsp',
                    controller: 'PersonDetailsController as personDetailsVm',
                    resolve: ['app/phone/modules/person/details/personDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
