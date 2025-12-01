/**
 * Created by swapna on 24/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/select/person/personSelector',
        'app/desktop/modules/select/material/materialSelector',
        'app/desktop/modules/select/machine/machineSelector',
        'app/desktop/modules/select/manpower/manpowerSelector',
        'app/desktop/modules/select/project/projectSelector',
        'app/desktop/modules/select/task/taskSelector',
        'app/desktop/modules/select/site/siteSelector',
        'app/desktop/modules/select/store/storeSelector',
        'app/desktop/modules/select/supplier/supplierSelector',
        'app/desktop/modules/select/problem/problemSelector',
        'app/desktop/modules/select/receive/receiveSelector',
        'app/desktop/modules/select/issue/issueSelector',
        'app/desktop/modules/select/loan/loanSelector',
        'app/desktop/modules/select/stockReturn/stockReturnSelector',
        'app/desktop/modules/select/scrapRequest/scrapRequestSelector',
        'app/desktop/modules/select/customIndent/customIndentSelector',
        'app/desktop/modules/select/customPurchaseOrder/purchaseOrderSelector',
        'app/desktop/modules/select/customRequisition/customRequisitionSelector',
        'app/desktop/modules/select/customRoadChallan/customRoadChallanSelector',
        'app/desktop/modules/select/contractor/contractorSelector',
        'app/desktop/modules/select/workOrder/workOrderSelector'

    ],
    function (module) {
        module.controller('ObjectSelectionController', ObjectSelectionController);

        function ObjectSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $application,
                                           PersonSelector, MaterialSelector, MachineSelector, ManpowerSelector, ProjectSelector, TaskSelector,
                                           SiteSelector, StoreSelector, SupplierSelector, ProblemSelector, ReceiveSelector, IssueSelector,
                                           LoanSelector, StockReturnSelector, ScrapRequestSelector, CustomIndentSelector, PurchaseOrderSelector,
                                           CustomRequisitionSelector, CustomRoadChallanSelector, ContractorSelector, WorkOrderSelector) {

            var vm = this;
            vm.loadObjectSelectors = loadObjectSelectors;

            function loadObjectSelectors() {
                $application.registerObjectSelector('PERSON', PersonSelector);
                $application.registerObjectSelector('MATERIAL', MaterialSelector);
                $application.registerObjectSelector('MACHINE', MachineSelector);
                $application.registerObjectSelector('MANPOWER', ManpowerSelector);
                $application.registerObjectSelector('PROJECT', ProjectSelector);
                $application.registerObjectSelector('TASK', TaskSelector);
                $application.registerObjectSelector('SITE', SiteSelector);
                $application.registerObjectSelector('STORE', StoreSelector);
                $application.registerObjectSelector('SUPPLIER', SupplierSelector);
                $application.registerObjectSelector('PROBLEM', ProblemSelector);
                $application.registerObjectSelector('RECEIVE', ReceiveSelector);
                $application.registerObjectSelector('ISSUE', IssueSelector);
                $application.registerObjectSelector('LOAN', LoanSelector);
                $application.registerObjectSelector('STOCKRETURN', StockReturnSelector);
                $application.registerObjectSelector('SCRAPREQUEST', ScrapRequestSelector);
                $application.registerObjectSelector('CUSTOM_INDENT', CustomIndentSelector);
                $application.registerObjectSelector('CUSTOM_PURCHASEORDER', PurchaseOrderSelector);
                $application.registerObjectSelector('CUSTOM_REQUISITION', CustomRequisitionSelector);
                $application.registerObjectSelector('CUSTOM_ROADCHALAN', CustomRoadChallanSelector);
                $application.registerObjectSelector('CONTRACTOR', ContractorSelector);
                $application.registerObjectSelector('WORKORDER', WorkOrderSelector);
            }

            (function () {
                loadObjectSelectors();
            })();
        }
    }
)
;

