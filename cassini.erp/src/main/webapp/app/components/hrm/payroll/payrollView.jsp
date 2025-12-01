<script type="text/ng-template" id="employeePayEdit-view-tb">
    <div class="row" ng-show="currentViewName == 'employee-pay-edit'">
        <div class="col-md-12 mb20">
            <button class="btn btn-sm btn-primary mr10" data-ng-click="navigate();"><i
                    class="fa fa-chevron-left mr10"></i> Back
            </button>
            <button class="btn btn-sm btn-success" data-ng-click="runNewPayroll();">Run Payroll</button>
        </div>
    </div>
</script>

<div ng-include="currentTemplate"></div>