<style>
    .gassPassWidth {
        display: run-in;
        word-wrap: break-word;
        width: 200px;
        white-space: normal !important;
        text-align: left;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        /*overflow-x: hidden;*/
        visibility: hidden;
        width: 600px;
        max-height: 300px;
        background-color: white;
        border: 1px solid lightgrey;
        color: black;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 10;
        top: 25%;
        /*bottom: 21px;*/
        /*left: auto;*/
        right: auto;
        opacity: 0;
        transition: opacity 1s;
        overflow-y: auto;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 101%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }
</style>
<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>S.no</th>
            <th>Package</th>
            <th>Gate Passes</th>
            <th>Inwards</th>
            <th>Item Instances</th>
            <th>Issues</th>
            <th>Missiles</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="itemReportVm.loading == true">
            <td colspan="25">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5">
                            <span>Loading Report...</span>
                </span>
            </td>
        </tr>

        <tr ng-if="itemReportVm.loading == false && itemReportVm.itemReport.length == 0">
            <td colspan="25">No Report</td>
        </tr>
        <tr ng-repeat="report in itemReportVm.itemReport">
            <td>{{$index + 1}}</td>
            <td>{{report.bomItem.namePath}}</td>
            <td>
                <div class="attributeTooltip">
                    <span class="badge badge-primary">{{report.gatePasses.length}}</span>

                    <div class="attributeTooltiptext">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th>Gate Pass Name</th>
                                <th>Gate Pass Number</th>
                                <th>Gate Pass Date</th>
                                <th>Created Date</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="gatePass in report.gatePasses">
                                <td class="gassPassWidth">{{gatePass.gatePass.name}}</td>
                                <td class="gassPassWidth">{{gatePass.gatePassNumber}}</td>
                                <td>{{gatePass.gatePassDate}}</td>
                                <td>{{gatePass.createdDate}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </td>
            <td>
                <span class="badge badge-info">{{report.inwards.length}}</span>
            </td>
            <td>
                <span class="badge badge-warning">{{report.itemInstances.length}}</span>
            </td>
            <td>
                <span class="badge badge-success">{{report.issueItems.length}}</span>
            </td>
            <td>
                <span class="badge badge-danger">{{report.missiles.length}}</span>
            </td>
        </tr>
        </tbody>
    </table>
</div>