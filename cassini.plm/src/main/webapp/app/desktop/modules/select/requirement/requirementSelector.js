/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/req/req.module',
        'app/shared/services/core/requirementService'
    ],

    function (module) {
        module.factory('RequirementSelector', RequirementSelector);

        function RequirementSelector(RequirementService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: $rootScope.selectReqTitle,
                    template: 'app/desktop/modules/select/requirement/requirementSelectionView.jsp',
                    controller: 'RequirementSelectionController as requirementSelectVm',
                    resolve: 'app/desktop/modules/select/requirement/requirementSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.req'}
                    ],
                    callback: function (selectedItem) {
                        callback(selectedItem, selectedItem.number);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                RequirementService.getRequirement(id).then(
                    function (data) {
                        attribute.refValueString = data.number;
                    }
                );
            }
        }
    }
);
