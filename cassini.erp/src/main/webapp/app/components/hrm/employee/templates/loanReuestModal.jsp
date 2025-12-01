<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">Loan Request</h3>
</div>
<div class="modal-body styled-panel" style="margin: 20px;">
<form class="form" angular-validator-submit="loanRequest()" name="loanForm" novalidate angular-validator>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label">Amount</label>
                    <input type="text" class="form-control" ng-model="loan.amount" name ="amount"
                           required-message="constants.REQUIRED" validate-on="dirty" required/>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label">Loan Type</label>
                    <select class="form-control" ng-model="loan.type" data-placeholder="Select" ng-options="loanType as loanType.name for loanType in loanTypesList"
                            name ="loantype" required-message="constants.REQUIRED" validate-on="dirty" required>
                        <option value="">-Select-</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label">Terms</label>
                    <input type="text" class="form-control" ng-model="loan.term" name ="terms"
                           required-message="constants.REQUIRED" validate-on="dirty" required/>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label">Reason For Loan</label>
                    <textarea class="form-control" rows="3" ng-model="loan.reason"></textarea>
                </div>

            </div>

        </div>
    </form>
</div>


<div class="modal-footer text-right" style="background-color: #F9F9F9;">
    <button class="btn btn-link" data-ng-click="close();">Cancel</button>
    <button class="btn btn-primary" data-ng-click="validateLoan(loanForm);">Submit</button>
</div>
