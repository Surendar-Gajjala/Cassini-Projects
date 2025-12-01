define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.compliance': {
                    url: '/compliance',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/compliance/complianceMainView.jsp',
                    controller: 'ComplianceMainController as complianceMainVm',
                    resolve: ['app/desktop/modules/compliance/complianceMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.substance': {
                    url: '/substance',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/compliance/substances/substancesHomeView.jsp',
                    controller: 'SubstancesHomeController as substancesHomeVm',
                    resolve: ['app/desktop/modules/compliance/substances/substancesHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.substance.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/compliance/substances/all/allSubstancesView.jsp',
                    controller: 'AllSubstanceController as allSubstanceVm',
                    resolve: ['app/desktop/modules/compliance/substances/all/allSubstancesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.substance.details': {
                    url: '/:substanceId?tab',
                    templateUrl: 'app/desktop/modules/compliance/substances/details/substancesDetailsView.jsp',
                    controller: 'SubstancesDetailsController as substanceDetailsVm',
                    resolve: ['app/desktop/modules/compliance/substances/details/substancesDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.specification': {
                    url: '/specification',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/compliance/specifications/specificationHomeView.jsp',
                    controller: 'SpecificationHomeController as specificationsHomeVm',
                    resolve: ['app/desktop/modules/compliance/specifications/specificationHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.specification.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/compliance/specifications/all/allSpecificationsView.jsp',
                    controller: 'AllSpecificationController as allSpecificationVm',
                    resolve: ['app/desktop/modules/compliance/specifications/all/allSpecificationsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.specification.details': {
                    url: '/:specificationId?tab',
                    templateUrl: 'app/desktop/modules/compliance/specifications/details/specificationDetailsView.jsp',
                    controller: 'SpecificationDetailsController as specificationDetailsVm',
                    resolve: ['app/desktop/modules/compliance/specifications/details/specificationDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.declaration': {
                    url: '/declaration',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/compliance/declaration/declarationHomeView.jsp',
                    controller: 'DeclarationHomeController as declarationHomeVm',
                    resolve: ['app/desktop/modules/compliance/declaration/declarationHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.declaration.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/compliance/declaration/all/allDeclarationsView.jsp',
                    controller: 'AllDeclarationController as allDeclarationVm',
                    resolve: ['app/desktop/modules/compliance/declaration/all/allDeclarationsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.compliance.declaration.details': {
                    url: '/:declarationId?tab',
                    templateUrl: 'app/desktop/modules/compliance/declaration/details/declarationDetailsView.jsp',
                    controller: 'DeclarationDetailsController as declarationDetailsVm',
                    resolve: ['app/desktop/modules/compliance/declaration/details/declarationDetailsController'],
                    css: cssConfig.getViewCss('app')
                }

            }
        };
    }
);