/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService'
    ],

    function (module) {
        module.factory('ItemRevisionSelector', ItemRevisionSelector);

        function ItemRevisionSelector(ItemService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: $rootScope.selectItemRevisionTitle,
                    template: 'app/desktop/modules/select/itemRevision/itemRevisionSelection.jsp',
                    controller: 'ItemRevisionSelectionController as itemRevSelectVm',
                    resolve: 'app/desktop/modules/select/itemRevision/itemRevisionSelectionController',
                    width: 650,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.itemRevision'}
                    ],
                    callback: function (selectedItem) {
                        callback(selectedItem, selectedItem.itemMasterObject.itemNumber + "(" + selectedItem.revision + ")");
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                ItemService.getItemRevision(id).then(
                    function (data) {
                        attribute.refValueString = data.itemNumber;
                    }
                );
            }
        }
    }
);
