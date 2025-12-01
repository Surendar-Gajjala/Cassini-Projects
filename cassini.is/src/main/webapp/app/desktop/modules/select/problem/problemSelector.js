/**
 * Created by swapna on 24/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/issue/issueService'
    ],

    function (module) {
        module.factory('ProblemSelector', ProblemSelector);

        function ProblemSelector(IssueService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling problem selector dialog");
                var options = {
                    title: 'Select Problem',
                    template: 'app/desktop/modules/select/problem/problemSelectionView.jsp',
                    controller: 'ProblemSelectionController as problemSelectVm',
                    resolve: 'app/desktop/modules/select/problem/problemSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.problem.selected'}
                    ],
                    callback: function (selectedProblem) {
                        callback(selectedProblem, selectedProblem.title);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                IssueService.getIssue(id).then(
                    function (data) {
                        attribute.refValueString = data.title;
                        attribute[attributeName] = data.title;
                    }
                );
            }
        }
    }
);