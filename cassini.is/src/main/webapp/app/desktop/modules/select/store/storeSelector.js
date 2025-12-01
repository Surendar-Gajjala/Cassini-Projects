/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/topStoreService'
    ],

    function (module) {
        module.factory('StoreSelector', StoreSelector);

        function StoreSelector(TopStoreService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling store selector dialog");
                var options = {
                    title: 'Select Store',
                    template: 'app/desktop/modules/select/store/storeSelectionView.jsp',
                    controller: 'StoreSelectionController as storeSelectVm',
                    resolve: 'app/desktop/modules/select/store/storeSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.store.selected'}
                    ],
                    callback: function (selectedStore) {
                        callback(selectedStore, selectedStore.storeName);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                TopStoreService.getTopStore(id).then(
                    function (data) {
                        attribute.refValueString = data.storeName;
                        attribute[attributeName] = data.storeName;
                    }
                );
            }
        }
    }
);