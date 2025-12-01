/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/topStockReceivedService'
    ],

    function (module) {
        module.factory('ReceiveSelector', ReceiveSelector);

        function ReceiveSelector(TopStockReceivedService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling receive selector dialog");
                var options = {
                    title: 'Select Receive',
                    template: 'app/desktop/modules/select/receive/receiveSelectionView.jsp',
                    controller: 'ReceiveSelectionController as receiveSelectVm',
                    resolve: 'app/desktop/modules/select/receive/receiveSelectionController',
                    width: 600,
                    data: {},
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: 'Select', broadcast: 'app.receive.selected'}
                    ],
                    callback: function (selectedReceive) {
                        callback(selectedReceive, selectedReceive.receiveNumberSource);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                TopStockReceivedService.getStockReceive(0, id).then(
                    function (data) {
                        attribute.refValueString = data.receiveNumberSource;
                        attribute[attributeName] = data.receiveNumberSource;
                    }
                );
            }
        }
    }
);