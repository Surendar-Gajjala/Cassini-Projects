/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module'
    ],

    function (module) {
        module.factory('PersonSelector', PersonSelector);

        function PersonSelector() {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: $rootScope.selectPersonTitle,
                    template: 'app/desktop/modules/select/person/personSelectionView.jsp',
                    controller: 'PersonSelectionController as personSelectVm',
                    resolve: 'app/desktop/modules/select/person/personSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.person'}
                    ],
                    callback: function (selectedPerson) {
                        callback(selectedPerson.person, selectedPerson.person.fullName);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails() {

            }
        }
    }
);
