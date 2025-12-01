define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.pdm': {
                    url: '/pdm',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pdm/pdmMainView.jsp',
                    controller: 'PDMMainController as pdmMainVm',
                    resolve: ['app/desktop/modules/pdm/pdmMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.vaults': {
                    url: '/vaults',
                    templateUrl: 'app/desktop/modules/pdm/vaults/all/allVaultsView.jsp',
                    controller: 'AllVaultsController as allVaultsVm',
                    resolve: ['app/desktop/modules/pdm/vaults/all/allVaultsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.vaultdetails': {
                    url: '/vaultdetails/:vaultId?path',
                    templateUrl: 'app/desktop/modules/pdm/vaults/details/vaultDetailsView.jsp',
                    controller: 'VaultDetailsController as vaultDetailsVm',
                    resolve: ['app/desktop/modules/pdm/vaults/details/vaultDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.assemblies': {
                    url: '/assemblies',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pdm/assemblies/assembliesView.jsp',
                    controller: 'AssembliesController as assembliesVm',
                    resolve: ['app/desktop/modules/pdm/assemblies/assembliesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.assemblies.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pdm/assemblies/all/allAssembliesView.jsp',
                    controller: 'AllAssembliesController as allAssembliesVm',
                    resolve: ['app/desktop/modules/pdm/assemblies/all/allAssembliesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.assemblies.details': {
                    url: '/details/:assemblyId',
                    templateUrl: 'app/desktop/modules/pdm/assemblies/details/assemblyDetailsView.jsp',
                    controller: 'AssemblyDetailsController as assemblyDetailsVm',
                    resolve: ['app/desktop/modules/pdm/assemblies/details/assemblyDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.parts': {
                    url: '/parts',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pdm/parts/partsView.jsp',
                    controller: 'PartsController as partsVm',
                    resolve: ['app/desktop/modules/pdm/parts/partsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.parts.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pdm/parts/all/allPartsView.jsp',
                    controller: 'AllPartsController as allPartsVm',
                    resolve: ['app/desktop/modules/pdm/parts/all/allPartsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.parts.details': {
                    url: '/details/:partId',
                    templateUrl: 'app/desktop/modules/pdm/parts/details/partDetailsView.jsp',
                    controller: 'PartDetailsController as partDetailsVm',
                    resolve: ['app/desktop/modules/pdm/parts/details/partDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.drawings': {
                    url: '/drawings',
                    templateUrl: 'app/desktop/modules/pdm/drawings/all/allDrawingsView.jsp',
                    controller: 'AllDrawingsController as allDrawingsVm',
                    resolve: ['app/desktop/modules/pdm/drawings/all/allDrawingsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pdm.files': {
                    url: '/files/:fileType',
                    templateUrl: 'app/desktop/modules/pdm/files/pdmFilesView.jsp',
                    controller: 'PDMFilesController as pdmFilesVm',
                    resolve: ['app/desktop/modules/pdm/files/pdmFilesController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);