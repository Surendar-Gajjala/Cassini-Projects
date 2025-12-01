/**
 * Created by swapna on 27/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/stockReturnService'
    ],

    function (module) {
        module.factory('StockReturnSelector', StockReturnSelector);

        function StockReturnSelector(StockReturnService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling stockReturn selector dialog");
                var options = {
                    title: 'Select StockReturn',
                    template: 'app/desktop/modules/select/stockReturn/stockReturnSelectionView.jsp',
                    controller: 'StockReturnSelectionController as stockReturnSelectVm',
                    resolve: 'app/desktop/modules/select/stockReturn/stockReturnSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.stockReturn.selected'}
                    ],
                    callback: function (selectedStockReturn) {
                        callback(selectedStockReturn, selectedStockReturn.returnNumberSource);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                StockReturnService.getStockReturn(null, id).then(
                    function (data) {
                        attribute.refValueString = data.returnNumberSource;
                        attribute[attributeName] = data.returnNumberSource;
                    }
                );
            }
        }
    }
);