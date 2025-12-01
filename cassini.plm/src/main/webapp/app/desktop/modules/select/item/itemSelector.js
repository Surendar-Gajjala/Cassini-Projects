/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService'
    ],

    function (module) {
        module.factory('ItemSelector', ItemSelector);

        function ItemSelector(ItemService, $translate) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: $rootScope.selectItemTitle,
                    template: 'app/desktop/modules/select/item/itemSelectionView.jsp',
                    controller: 'ItemSelectionController as itemSelectVm',
                    resolve: 'app/desktop/modules/select/item/itemSelectionController',
                    width: 650,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.attributes.items.selector'}
                    ],
                    callback: function (selectedItem) {
                        callback(selectedItem, selectedItem.itemNumber);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                ItemService.getItem(id).then(
                    function (data) {
                        attribute.refValueString = data.itemNumber;
                    }
                );
            }
        }
    }
);
