/**
 * Created by swapna on 27/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/loanService'
    ],

    function (module) {
        module.factory('LoanSelector', LoanSelector);

        function LoanSelector(LoanService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling loan selector dialog");
                var options = {
                    title: 'Select Loan',
                    template: 'app/desktop/modules/select/loan/loanSelectionView.jsp',
                    controller: 'LoanSelectionController as loanSelectVm',
                    resolve: 'app/desktop/modules/select/loan/loanSelectionController',
                    width: 650,
                    data: {},
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: 'Select', broadcast: 'app.loan.selected'}
                    ],
                    callback: function (selectedLoan) {
                        callback(selectedLoan, selectedLoan.loanNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                LoanService.getLoanById(null, id).then(
                    function (data) {
                        attribute.refValueString = data.loanNumber;
                        attribute[attributeName] = data.loanNumber;
                    }
                );
            }
        }
    }
);