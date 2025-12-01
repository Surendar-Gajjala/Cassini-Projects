/**
 * Created by swapna on 28/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/scrapService'
    ],

    function (module) {
        module.factory('ScrapRequestSelector', ScrapRequestSelector);

        function ScrapRequestSelector(ScrapService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling scrapRequest selector dialog");
                var options = {
                    title: 'Select ScrapRequest',
                    template: 'app/desktop/modules/select/scrapRequest/scrapRequestSelectionView.jsp',
                    controller: 'ScrapRequestSelectionController as scrapRequestSelectVm',
                    resolve: 'app/desktop/modules/select/scrapRequest/scrapRequestSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.scrapRequest.selected'}
                    ],
                    callback: function (selectedScrapRequest) {
                        callback(selectedScrapRequest, selectedScrapRequest.scrapNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                ScrapService.get(id).then(
                    function (data) {
                        attribute.refValueString = data.scrapNumber;
                        attribute[attributeName] = data.scrapNumber;
                    }
                );
            }
        }
    }
);