define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],

    function (module) {
        module.factory('PersonSelector', PersonSelector);

        function PersonSelector(CommonService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                if ($rootScope.objectAttributeValue == undefined) {
                    $rootScope.objectAttributeValue = '';
                }
                var options = {
                    title: 'Select Person',
                    template: 'app/assets/bower_components/cassini-platform/app/desktop/modules/select/person/personSelectionView.jsp',
                    controller: 'PersonSelectionController as personSelectVm',
                    resolve: 'app/assets/bower_components/cassini-platform/app/desktop/modules/select/person/personSelectionController.js',
                    width: 550,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: 'Select', broadcast: 'app.person.selected'}
                    ],
                    callback: function (selectedPerson) {
                        callback(selectedPerson, selectedPerson.fullName);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                CommonService.getPerson(id).then(
                    function (data) {
                        attribute.refValueString = data.fullName;
                        attribute[attributeName] = data.fullName;
                    }
                )
            }
        }
    }
);