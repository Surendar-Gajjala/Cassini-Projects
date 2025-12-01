<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="loanSelectVm.resetPage"
                          on-search="loanSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{loanSelectVm.loans.numberOfElements}} of
                                            {{loanSelectVm.loans.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{loanSelectVm.loans.totalElements != 0 ? loanSelectVm.loans.number+1:0}} of {{loanSelectVm.loans.totalPages}}</span>
                <a href="" ng-click="loanSelectVm.previousPage()"
                   ng-class="{'disabled': loanSelectVm.loans.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="loanSelectVm.nextPage()"
                   ng-class="{'disabled': loanSelectVm.loans.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th>Loan Number</th>
                <th>Status</th>
                <th>From Project</th>
                <th>To Project</th>
                <th>From Store</th>
                <th>To Store</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="loanSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span>Loading Loans...</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="loanSelectVm.loading == false && loanSelectVm.loans.content.length == 0">
                <td colspan="12" translate>No Loans</td>
            </tr>

            <tr ng-repeat="loan in loanSelectVm.loans.content | filter: search"
                ng-click="loan.checked = !loan.checked; loanSelectVm.radioChange(loan, $event)"
                ng-dblclick="loan.checked = !loan.checked; loanSelectVm.selectRadioChange(loan, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="loan.checked" name="loan" value="loan"
                           ng-click="loanSelectVm.radioChange(loan, $event)"
                           ng-dblclick="loanSelectVm.selectRadioChange(loan, $event)">
                </td>
                <td>{{loan.loanNumber}}</td>
                <td>
                          <span ng-if="loan.status == 'PENDING' "
                                style="background-color: #00C851;color: white;padding: 2px 8px;border-radius: 3px;">{{loan.status}}</span>
                        <span ng-if="loan.status == 'RETURNED' "
                              style="background-color: #ff4444;color: white;padding: 2px 8px;border-radius: 3px;">{{loan.status}}</span>
                </td>
                <td>{{loan.fromProjectObject.name}}</td>
                <td>{{loan.toProjectObject.name}}</td>
                <td>{{loan.fromStoreObject.storeName}}</td>
                <td>{{loan.toStoreObject.storeName}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>


