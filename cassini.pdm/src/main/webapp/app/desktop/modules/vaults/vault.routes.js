define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.vaults': {
                    url: '/vaults',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/vaults/vaultView.jsp',
                    controller: 'VaultController as vaultVm',
                    resolve: ['app/desktop/modules/vaults/vaultController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.vaults.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/vaults/all/allVaultsView.jsp',
                    controller: 'AllVaultsController as vaultsVm',
                    resolve: ['app/desktop/modules/vaults/all/allVaultsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.vaults.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/vaults/new/newVaultView.jsp',
                    controller: 'NewVaultController as newVaultVm',
                    resolve: ['app/desktop/modules/vaults/new/newVaultController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.vaults.details': {
                    url: '/details/:vaultId/:vaultName',
                    templateUrl: 'app/desktop/modules/vaults/details/vaultDetailsView.jsp',
                    controller: 'VaultDetailsController as vaultVm',
                    resolve: ['app/desktop/modules/vaults/details/vaultDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);