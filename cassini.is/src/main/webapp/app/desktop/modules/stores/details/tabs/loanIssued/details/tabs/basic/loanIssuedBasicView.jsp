<div ng-if="loanBasicVm.loading == true" style="padding: 30px;">
    <br/>
<span style="font-size: 15px;">
<img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading...
</span>
    <br/>
</div>
<div class="row row-eq-height" style="margin: 0;">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Loan Number: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{loanBasicVm.loan.loanNumber}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>From Project: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{loanBasicVm.loan.fromProjectName}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>To Project: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{loanBasicVm.loan.toProjectName}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>To Store: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{loanBasicVm.loan.toStore}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created By: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{loanBasicVm.loan.issuedBy}}</span>
            </div>
        </div>


        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created On: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{loanBasicVm.loan.createdDate}}</span>
            </div>
        </div>

        <attributes-details-view attribute-id="loanId" attribute-type="LOAN"
                                 has-permission="hasPermission('permission.loanIssued.edit')"></attributes-details-view>
    </div>
</div>