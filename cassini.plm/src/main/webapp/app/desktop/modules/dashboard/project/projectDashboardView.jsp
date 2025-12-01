<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>PROJECTS_DASHBOARD</span>

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
                width: 400px;
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
                left: 0;
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

            .widget-window-model.modal {
                display: none; /* Hidden by default */
                position: fixed; /* Stay in place */
                padding-top: 20px; /* Location of the box */
                left: 0;
                top: 0;
                width: 100%; /* Full width */
                height: 100%; /* Full height */
                overflow: auto; /* Enable scroll if needed */
                background-color: rgb(0, 0, 0); /* Fallback color */
                background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
            }

            .widget-window-model .widget-window-content {
                margin: auto;
                display: block;
                height: 94%;
                width: 97%;
                background-color: white;
                border-radius: 7px !important;
            }

            .widget-window-header {
                padding: 5px;
                text-align: center;
                border-bottom: 1px solid lightgrey;
                height: 50px;
            }

            .window-header {
                font-weight: bold;
                font-size: 22px;
                /*position: absolute;*/
                display: inline-block;
                /*left: 44%;*/
                margin-top: 7px;
            }

            .window-content {
                padding: 10px;
                overflow: auto;
                min-width: 100%;
                width: auto;
            }

            .window-close {
                position: absolute;
                right: 25px;
                width: 38px;
                height: 38px;
                opacity: 0.3;
            }

            .window-close:hover {
                opacity: 0.6;
                border-radius: 50%;
                background-color: #ddd;
            }

            .window-close:before, .window-close:after {
                position: absolute;
                top: 7px;
                left: 18px;
                content: ' ';
                height: 22px;
                width: 2px;
                background-color: #333;
            }

            .window-close:before {
                transform: rotate(45deg) !important;
            }

            .window-close:after {
                transform: rotate(-45deg) !important;
            }

            .center {
                text-align: center;

            }
        </style>
        <div class="home-content-panel">
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5 translate>PROJECT_BY_STATUS</h5>
                    </div>
                    <div id="projectByStatus">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5 translate>ACTIVITY_BY_STATUS</h5>
                    </div>
                    <div id="activityByStatus">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5 translate>TASK_BY_STATUS</h5>
                    </div>
                    <div id="taskByStatus">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <i class="la la-expand" style="float: right;cursor: pointer;" title="Maximize widget"
                           ng-click="maximizeWidget('openTask')"></i>
                        <h5 translate>OPEN_TASKS</h5>
                    </div>
                    <div id="openTasks">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5 translate>REQUIREMENT_BY_TYPE</h5>
                    </div>
                    <div id="reqByType">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5 translate>REQUIREMENT_BY_STATUS</h5>
                    </div>
                    <div id="reqByStatus">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5 translate>DELIVERABLE_BY_STATUS</h5>
                    </div>
                    <div id="projectDeliverableByStatus">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <i class="la la-expand" style="float: right;cursor: pointer;" title="Maximize widget"
                           ng-click="maximizeWidget('projectActivity')"></i>
                        <h5 translate>PROJECT_ACTIVITY_STATUS</h5>
                    </div>


                    <div id="projectActivityStatus">

                    </div>
                    <div style="font-size: 11px;" class="center">
                        Total no of projects :
                        <span> {{projectDashboardVm.totalProjectsCounts}}</span><br>
                        Total no of activities :
                        <span> {{projectDashboardVm.totalActivityStatusCounts}}</span></div>
                </div>
                <%-- <div class="home-widget counts-chart-container">
                     <div class="widget-header">
                         <h5>Activity Deliverable by Status</h5>
                     </div>
                     <div id="activityDeliverableByStatus">

                     </div>
                 </div>
                 <div class="home-widget counts-chart-container">
                     <div class="widget-header">
                         <h5 translate>DELIVERABLE_BY_STATUS</h5>
                     </div>
                     <div id="taskDeliverableByStatus">

                     </div>
                 </div>--%>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <i class="la la-expand" style="float: right;cursor: pointer;" title="Maximize widget"
                           ng-click="maximizeWidget('projectStatusByProgram')"></i>
                        <h5 translate>Project Status By Program</h5>
                    </div>
                    <div id="projectStatusByProgram">

                    </div>
                </div>

            </div>
            <div class="widgets-row">

                <div class="home-widget counts-chart-container" style="height: 350px !important;">

                    <div class="widget-header">
                        <i class="la la-expand" style="float: right;cursor: pointer;" title="Maximize widget"
                           ng-click="maximizeWidget('graph-container')"></i>

                        <h5 translate>Program Management</h5>
                    </div>
                    <div id="graph-container">

                    </div>
                </div>

                <div>


                </div>


                <div>

                </div>

            </div>
        </div>
    </div>

    <div id="widget-window" class="widget-window-model modal">
        <div class="widget-window-content">
            <div class="widget-window-header">
                <span class="window-header" ng-if="maxType == 'openTask'">Open Tasks</span>
                <span class="window-header" ng-if="maxType == 'projectActivity'">Project Activity Status</span>
                <span class="window-header" ng-if="maxType == 'projectStatusByProgram'">Project Status By Program</span>
                <span class="window-header" ng-if="maxType == 'graph-container'">Program Management</span>
                <a href="" ng-click="hideWidgetWindow()" class="window-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="window-content" style="padding: 0;overflow: hidden;">

                <div id="maximizeOpenTasks"></div>
                <div id=" " class="center" ng-if="maxType == 'projectActivity'">

                    <b>Total no of projects :
                        <span> {{projectDashboardVm.totalProjectsCounts}}</span><br>
                        Total no of activities :
                        <span> {{projectDashboardVm.totalActivityStatusCounts}}</span></b>
                </div>

            </div>

        </div>
    </div>
</div>