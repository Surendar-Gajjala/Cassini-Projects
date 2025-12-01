<style>
    #td {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
        text-align: center;
    }

    .table thead > tr > th {
        background-color: #fff;
        color: #000000;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">

        <label class="btn fileContainer" style="margin-bottom: 0px !important;padding-top: 5px;"
               title="Import test runs">Import
            <input type="file" id="file" value="file"  onchange="angular.element(this).scope().importTestRuns()" style="display: none">
        </label>
    </div>


    <div id="applicationView" class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Run Configuration</th>
                    <th>Scenario</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>

                <tr ng-if="testRunVm.testRuns.length == 0">
                    <td colspan="10" style="padding-left: 11px;">No Test runs</td>
                </tr>

                <tr ng-repeat="testRun in testRunVm.testRuns.content">
                    <td><a href="" ng-click="testRunVm.showRunDetails(testRun)">{{testRun.id}}</a>
                    </td>
                    <td><span>
                        {{testRun.testRunConfiguration.name}}</span>
                    </td>
                    <td>
                        {{testRun.testRunConfiguration.scenario.name}}
                    </td>
                    <td>
                        {{testRun.startTime}}
                    </td>
                    <td>{{testRun.finishTime}}</td>
                    <td><button
                                title="Delete Test run" class="btn btn-xs btn-danger"
                                ng-click="testRunVm.deleteTestRun(testRun.id)">
                        <i class="fa fa-trash"></i>
                    </button></td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="table-footer">
            <div>
                <div>
                    <h5>Displaying {{testRunVm.testRuns.numberOfElements}} of {{testRunVm.testRuns.totalElements}}</h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{testRunVm.testRuns.totalElements != 0 ? testRunVm.testRuns.number+1:0}} of {{testRunVm.testRuns.totalPages}}</span>
                    <a href="" ng-click="testRunVm.previousPage()"
                       ng-class="{'disabled': testRunVm.testRuns.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="testRunVm.nextPage()"
                       ng-class="{'disabled': testRunVm.testRuns.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>



    </div>
</div>
