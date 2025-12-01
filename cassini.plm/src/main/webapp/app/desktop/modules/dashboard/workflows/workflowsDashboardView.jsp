<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>WORKFLOW_DASHBOARD</span>

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

            .order-card {
                color: #fff;
            }

            .bg-c-blue {
                background: linear-gradient(45deg, #4099ff, #73b4ff);
            }

            .bg-c-yellow {
                background: linear-gradient(45deg, #FFB64D, #ffcb80);
            }

            .bg-c-slate {
                background: linear-gradient(45deg, #386698, #4097d6);
            }

            .bg-c-pink {
                background: linear-gradient(45deg, #FF5370, #ff869a);
            }

            .bg-c-green {
                background: linear-gradient(45deg, #247565, #25c1a2);
            }

            .bg-c-orange {
                background: linear-gradient(45deg, #ffa500, #ffc03c);
            }

            .bg-c-purple {
                background: linear-gradient(45deg, #7e207d, #d640a9);
            }

            .bg-c-teal {
                background: linear-gradient(to right, #ff512f, #f09819);
            }

            .bg-c-peru {
                background: linear-gradient(to right, #94716B, #B79891); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

            }

            .card {
                border-radius: 5px;
                -webkit-box-shadow: 0 1px 2.94px 0.06px rgba(4, 26, 55, 0.16);
                box-shadow: 0 1px 2.94px 0.06px rgba(4, 26, 55, 0.16);
                border: none;
                -webkit-transition: all 0.3s ease-in-out;
                transition: all 0.3s ease-in-out;
            }

            .card .card-block {
                padding: 10px 20px;
            }

            .order-card i {
                font-size: 26px;
            }

            .data-card-row {
                display: flex;
                flex-direction: row;
                align-items: stretch;
            }

            .data-card-row > .card {
                margin: 10px;
                flex: 1
            }

        </style>
        <div class="home-content-panel">
            <div class="widgets-row">
                <div class="container" style="margin: 0;padding: 0;">
                    <div class="data-card-row">
                        <div class="card bg-c-blue order-card">
                            <div class="card-block">
                                <h5>Items
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{workflowTypeCard.items}}</span>
                                </h5>
                            </div>
                        </div>

                        <div class="card bg-c-pink order-card">
                            <div class="card-block">
                                <h5>Changes
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{workflowTypeCard.changes}}</span>
                                </h5>
                            </div>
                        </div>

                        <div class="card bg-c-purple order-card">
                            <div class="card-block">
                                <h5>Quality
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{workflowTypeCard.quality}}</span>
                                </h5>
                            </div>
                        </div>
                        <div class="card bg-c-orange order-card">
                            <div class="card-block">
                                <h5>Manufacturers
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{workflowTypeCard.mfr}}</span>
                                </h5>
                            </div>
                        </div>
                        <div class="card bg-c-green order-card">
                            <div class="card-block">
                                <h5>MFR Parts
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{workflowTypeCard.mfrParts}}</span>
                                </h5>
                            </div>
                        </div>
                        <div class="card bg-c-peru order-card">
                            <div class="card-block">
                                <h5>Projects
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{workflowTypeCard.projects}}</span>
                                </h5>
                            </div>
                        </div>
                        <div class="card bg-c-teal order-card">
                            <div class="card-block">
                                <h5>Work Orders
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{workflowTypeCard.workOrder}}</span>
                                </h5>
                            </div>
                        </div>
                        <div class="card bg-c-teal order-card">
                            <div class="card-block">
                                <h5>NPR
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{workflowTypeCard.npr}}</span>
                                </h5>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div id="workflowByStatus">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{itemWfByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="ItemWorkflowByStatus">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{changeWfByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="ChangeWorkflowByStatus">

                        </div>
                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{qualityWfByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="QualityWorkflowByStatus">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="ManufacturerWorkflowByStatus">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{mfrPartWfByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="ManufacturerPartWorkflowByStatus">

                        </div>
                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div id="ProjectWorkflowByStatus">

                    </div>

                </div>
                <div class="home-widget counts-chart-container">
                    <div id="NPRWorkflowByStatus">

                    </div>
                </div>

                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{workOrderWfByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="WorkOrderWorkflowByStatus">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>