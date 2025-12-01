/**
 * Created by swapna on 28/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/roadChallanService'
    ],

    function (module) {
        module.factory('CustomRoadChallanSelector', CustomRoadChallanSelector);

        function CustomRoadChallanSelector(RoadChallanService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling custom RoadChallan selector dialog");
                var options = {
                    title: 'Select Custom RoadChallan',
                    template: 'app/desktop/modules/select/customRoadChallan/customRoadChallanSelectionView.jsp',
                    controller: 'CustomRoadChallanSelectionController as customRoadChallanSelectVm',
                    resolve: 'app/desktop/modules/select/customRoadChallan/customRoadChallanSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.customRoadChallan.selected'}
                    ],
                    callback: function (selectedCustomRoadChallan) {
                        callback(selectedCustomRoadChallan, selectedCustomRoadChallan.chalanNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                RoadChallanService.getRoadChallan(null, id).then(
                    function (data) {
                        attribute.refValueString = data.chalanNumber;
                        attribute[attributeName] = data.chalanNumber;
                    }
                );
            }
        }
    }
);