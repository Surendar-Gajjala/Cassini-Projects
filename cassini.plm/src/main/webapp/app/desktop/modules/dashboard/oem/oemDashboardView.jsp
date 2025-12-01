<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>OEM_DASHBOARD</span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">
        <style scoped>
            .view-content {
                position: relative;
            }
            .home-side-panel {
                position: absolute;
                top: 0;
                bottom: 0;
                left: 0;
                width: 350px;
                border-right: 1px solid #ddd;
                overflow-y: auto;
                padding: 20px;
            }

            .home-side-panel .sidepanel-widget {
                height: 350px;
                width: 100%;
                border: 1px solid #ddd;
                border-radius: 5px;
                margin-bottom: 20px;
            }

            .home-content-panel {
                position: absolute;
                top: 0;
                bottom: 0;
                left: 355px;
                right: 0;
                overflow-y: auto;
                padding: 10px;
            }

            .counts-chart-container {
                width: 500px;
                height: 300px;
                border: 1px solid #ddd;
                border-radius: 5px;
                padding: 10px;
            }

            .widgets-row {
                display: flex;
                justify-content: center;
            }

            .widgets-row > div {
                flex: 1;
                margin: 10px;
            }

            .home-widget {
                border: 1px solid #ddd;
                border-radius: 5px;
            }

            .second-row-second-col {
                display: flex;
                flex-direction: column;
            }

            .second-row-second-col > div {
                flex: 1;
            }

            .second-row-second-col > div:first-child {
                margin-bottom: 20px;
            }

            .widget-header {
                height: 40px;
                /*border-bottom: 1px solid #ddd;*/
                padding-left: 10px;
            }

            .widget-header > h5 {
                margin: 0;
                line-height: 40px;
            }

            .widget-body {
                overflow-y: auto;
                height: calc(100% - 40px);
            }

            .apexcharts-canvas .apexcharts-svg text {
                font-family: inherit !important;
            }

            .no-scroll {
                overflow: hidden;
            }

        </style>
        <div class="home-side-panel">
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>TOP_MANUFACTURER</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>MANUFACTURER</th>
                                <th style="width: 100px;" translate>PARTS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="oemDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_TOP_MANUFACTURER</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="oemDashboardVm.loading == false && oemDashboardVm.topMfrParts.length == 0">
                                <td colspan="10" translate>NO_MANUFACTURERS</td>
                            </tr>
                            <tr ng-repeat="mfr in oemDashboardVm.topMfrParts"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.mfr.details({manufacturerId:mfr.id})"
                                       title="{{clickToShowDetails}}">{{mfr.mfr}}</a>
                                </td>
                                <td>
                                    {{mfr.parts}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>TOP_PROBLEM_PARTS</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>PART_NUMBER</th>
                                <th style="width: 100px;" translate>PART_NAME</th>
                                <th style="width: 100px;" translate>PROBLEMS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="oemDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_PROBLEM_PARTS</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="oemDashboardVm.loading == false && oemDashboardVm.topProblemParts.length == 0">
                                <td colspan="10" translate>NO_PROBLEM_PARTS</td>
                            </tr>
                            <tr ng-repeat="problemPart in oemDashboardVm.topProblemParts"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.mfr.mfrparts.details({mfrId: problemPart.mfrId, manufacturePartId: problemPart.partId})"
                                       title="{{clickToShowDetails}}">{{problemPart.partNumber}}</a>
                                </td>
                                <td>{{problemPart.partName}}</td>
                                <td>
                                    {{problemPart.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>TOP_PROBLEM_MANUFACTURER</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>MANUFACTURER</th>
                                <th style="width: 100px;" translate>PROBLEMS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="oemDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_PROBLEM_MANUFACTURER</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="oemDashboardVm.loading == false && oemDashboardVm.topProblemMfrs.length == 0">
                                <td colspan="10" translate>NO_PROBLEM_MANUFACTURER</td>
                            </tr>
                            <tr ng-repeat="problemMfr in oemDashboardVm.topProblemMfrs"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.mfr.details({manufacturerId: problemMfr.mfrId})"
                                       title="{{clickToShowDetails}}">{{problemMfr.name}}</a>
                                </td>
                                <td>
                                    {{problemMfr.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>TOP_RECURRING_PARTS</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>PART_NUMBER</th>
                                <th style="width: 100px;" translate>RECURRING</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="oemDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_RECURRING_PARTS</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="oemDashboardVm.loading == false && oemDashboardVm.topRecurringParts.length == 0">
                                <td colspan="10" translate>NO_RECURRING_PARTS</td>
                            </tr>
                            <tr ng-repeat="recurringPart in oemDashboardVm.topRecurringParts"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.mfr.mfrparts.details({mfrId: recurringPart.mfrId, manufacturePartId: recurringPart.partId})"
                                       title="{{clickToShowDetails}}">{{recurringPart.partNumber}}
                                    </a>
                                </td>
                                <td>
                                    {{recurringPart.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="home-content-panel">
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5 translate>MANUFACTURER_PARTS_BY_STATUS</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="mfrPartByStatus">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5 translate>MANUFACTURER_BY_STATUS</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="mfrByStatus">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>