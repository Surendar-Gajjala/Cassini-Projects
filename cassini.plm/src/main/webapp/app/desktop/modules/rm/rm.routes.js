define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.rm': {
                    url: '/rm',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/rm/rmMainView.jsp',
                    controller: 'RMMainController as rmMainVm',
                    resolve: ['app/desktop/modules/rm/rmMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.glossary': {
                    url: '/glossary',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/rm/glossary/glossaryView.jsp',
                    controller: 'GlossaryController as glossaryVm',
                    resolve: ['app/desktop/modules/rm/glossary/glossaryController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.glossary.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/rm/glossary/all/glossarysView.jsp',
                    controller: 'GlossarysController as glossarysVm',
                    resolve: ['app/desktop/modules/rm/glossary/all/glossarysController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.glossary.new': {
                    url: '/glossary/new',
                    templateUrl: 'app/desktop/modules/rm/glossary/new/newGlossaryView.jsp',
                    controller: 'NewGlossaryController as newGlossaryVm',
                    resolve: ['app/desktop/modules/rm/glossary/new/newGlossaryController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.glossary.details': {
                    url: '/details/:glossaryId?tab',
                    templateUrl: 'app/desktop/modules/rm/glossary/details/glossaryDetailsView.jsp',
                    controller: 'GlossaryDetailsController as glossaryDetailsVm',
                    resolve: ['app/desktop/modules/rm/glossary/details/glossaryDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.glossary.basic': {
                    url: '/basic/:glossaryId',
                    templateUrl: 'app/desktop/modules/rm/glossary/details/tabs/basic/glossaryBasicView.jsp',
                    controller: 'GlossaryBasicController as glossaryBasicVm',
                    resolve: ['app/desktop/modules/rm/glossary/details/tabs/basic/glossaryBasicController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.specifications': {
                    url: '/specifications',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/rm/specification/specificationsView.jsp',
                    controller: 'SpecificationsController as specificationsVm',
                    resolve: ['app/desktop/modules/rm/specification/specificationsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.specifications.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/rm/specification/all/allSpecificationsView.jsp',
                    controller: 'AllSpecificationsController as allSpecificationsVm',
                    resolve: ['app/desktop/modules/rm/specification/all/allSpecificationsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.specifications.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/rm/specification/new/newSpecificationView.jsp',
                    controller: 'NewSpecificationController as newSpecificationVm',
                    resolve: ['app/desktop/modules/rm/specification/new/newSpecificationController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.specifications.details': {
                    url: '/details/:specId?tab',
                    templateUrl: 'app/desktop/modules/rm/specification/details/specDetailsView.jsp',
                    controller: 'SpecificationDetailsController as specDetailsVm',
                    resolve: ['app/desktop/modules/rm/specification/details/specDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.specifications.details.requirements.details': {
                    url: '/requirements/:requirementId',
                    templateUrl: 'app/desktop/modules/rm/specification/requirements/details/requirementDetailsView.jsp',
                    controller: 'RequirementDetailsController as requirementDetailsVm',
                    resolve: ['app/desktop/modules/rm/specification/requirements/details/requirementDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);