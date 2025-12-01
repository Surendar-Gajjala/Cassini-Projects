<div ng-if="showNew">
    <div class="row">
        <div class="col-sm-12">
            <h4 class="section-title">New Report</h4>
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
    <h4 style="text-align:left" ng-if="loading == false">No field reports</h4>
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
                    <h4>
                        <i class="glyphicon glyphicon-user mr5"></i>{{report.customer.salesRep.firstName}}
                    </h4>
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