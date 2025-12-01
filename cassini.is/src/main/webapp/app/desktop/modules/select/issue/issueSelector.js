/**
 * Created by swapna on 27/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/topStockIssuedService'
    ],

    function (module) {
        module.factory('IssueSelector', IssueSelector);

        function IssueSelector(TopStockIssuedService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling issue selector dialog");
                var options = {
                    title: 'Select Issue',
                    template: 'app/desktop/modules/select/issue/issueSelectionView.jsp',
                    controller: 'IssueSelectionController as issueSelectVm',
                    resolve: 'app/desktop/modules/select/issue/issueSelectionController',
                    width: 550,
                    data: {},
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: 'Select', broadcast: 'app.stockIssue.selected'}
                    ],
                    callback: function (selectedIssue) {
                        callback(selectedIssue, selectedIssue.issueNumberSource);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                TopStockIssuedService.getTopStockIssue(0, id).then(
                    function (data) {
                        attribute.refValueString = data.issueNumberSource;
                        attribute[attributeName] = data.issueNumberSource;
                    }
                );
            }
        }
    }
);