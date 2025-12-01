<div class="row" ng-if="showNew == false">
    <div class="col-sm-12" style="text-align:center; margin-bottom:20px">
        <button class="btn btn-success" ng-click="addNew();">New Report</button>
    </div>
</div>
<div ng-if="showNew">
    <div class="row">
        <div class="col-md-10 col-md-offset-1 col-lg-6 col-lg-offset-3">
            <h3 class="text-center">New Field Report</h3>
            <br/>
            <h4 class="section-title">Customer</h4>
            <div ng-show="newFieldReport.customer != null">
                <h5>Name: <span class="text-success">{{newFieldReport.customer.name}}</span></h5>
                <h5>Region: <span class="text-success">{{newFieldReport.customer.salesRegion.name}}</span></h5>
                <h5>District: <span class="text-success">{{newFieldReport.customer.salesRegion.district}}</span></h5>
            </div>
            <div ng-if="newFieldReport.customer == null" style="margin-bottom: 10px; color: red;">
                <%--<span>Select a customer</span>--%>
                    <button class="btn btn-sm btn-default" ng-click="showCustomerSelectionDialog()">{{selectText}}</button>&nbsp;&nbsp;
                    <button class="btn btn-sm btn-success" ng-hide="orderCreated"
                            ng-click="newCustomer()">Create</button>
            </div>

            <br/><br/><br/>
            <h4 class="section-title">Notes</h4>
            <textarea class="form-control" rows="10" ng-model="newFieldReport.notes" ck-editor></textarea>
        </div>
    </div>
    <br/>
    <div class="row">
        <div class="col-md-6 col-md-offset-3" style="text-align: center">
            <button class="btn btn-default mr10" ng-click="cancel()">Cancel</button>
            <button class="btn btn-primary" ng-click="createNew()">Submit</button>
        </div>
        <br/>
    </div>
</div>

<div ng-if="showNew == false && fieldReports.length == 0" style="padding: 20px">
    <h4 style="text-align:center" ng-if="loading == false">No field reports</h4>
    <div style="text-align:center" ng-if="loading == true">
        <span style="font-size: 18px;">
            <img src="app/assets/images/loaders/loader6.gif" class="mr5">Loading reports...
        </span>
    </div>
</div>


<div ng-if="showNew == false && fieldReports.length > 0">
    <ul class="timeline">
        <li class="customer-report" ng-repeat="report in fieldReports" ng-class="{'timeline-inverted': $index % 2 != 0}">
            <div class="timeline-badge info"><i class="glyphicon glyphicon-list"></i></div>
            <div class="timeline-panel">
                <div class="toggle-button" ng-click="toggleNotes(report)">
                    <span title="Show Notes" ng-if="report.showNotes == false"><i class="fa fa-plus-circle"></i></span>
                    <span title="Hide Notes" ng-if="report.showNotes == true"><i class="fa fa-minus-circle"></i></span>
                </div>
                <div class="timeline-heading">
                    <h4 class="timeline-title">
                        <a href="" ng-click="showCustomerDetails(report.customer)">{{report.customer.name}}</a>
                    </h4>
                    <h5>
                        <span><i class="glyphicon glyphicon-map-marker"></i></span>
                        <span class="mr20">Region: {{report.customer.salesRegion.name}}</span>
                        <span class="mr20">District: {{report.customer.salesRegion.district}}</span>
                        <span>State: {{report.customer.salesRegion.state.name}}</span>
                    </h5>
                    <p>
                        <small class="text-muted">
                            <i class="glyphicon glyphicon-time mr5"></i> {{report.timestamp}}
                        </small>
                    </p>
                </div>
                <div ng-show="report.showNotes == true" class="timeline-body" style="border-top: 1px dotted;padding-top: 20px;">
                    <div class="report-body" ng-bind-html="report.notes"></div>
                </div>
            </div>
        </li>
    </ul>
    <div class="text-center" ng-if="pagedFieldReports.last == false">
        <button class="btn btn-sm btn-default" style="width: 200px" ng-click="loadMore()">Load More</button>
    </div>
</div>