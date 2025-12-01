<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>CHANGES_DASHBOARD</span>

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

            .bg-c-pink {
                background: linear-gradient(45deg, #FF5370, #ff869a);
            }

            .bg-c-green {
                background: linear-gradient(45deg, #247565, #25c1a2);
            }

            .bg-c-orange {
                background: linear-gradient(to right, #ff512f, #f09819);
            }

            .bg-c-vanusa {
                background: linear-gradient(to right, #89216B, #DA4453); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
            }

            .bg-c-skyline {
                background: linear-gradient(to right, #2B32B2, #1488CC); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
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

            .data-card-row > .card:first-child {
                margin-left: 0;
            }

            .data-card-row > .card:last-child {
                margin-right: 0;
            }

        </style>
        <div class="home-content-panel">
            <div class="widgets-row">
                <div class="data-card-row">
                    <div class="card bg-c-green order-card">
                        <div class="card-block">
                            <h5>ECRs
                                <span style="margin-top: -5px;float: right;font-size: 24px;">{{changeTypeCardCounts.ecrs}}</span>
                            </h5>
                        </div>
                    </div>
                    <div class="card bg-c-vanusa order-card">
                        <div class="card-block">
                            <h5>ECOs
                                <span style="margin-top: -5px;float: right;font-size: 24px;">{{changeTypeCardCounts.ecos}}</span>
                            </h5>
                        </div>
                    </div>
                    <div class="card bg-c-blue order-card">
                        <div class="card-block">
                            <h5>DCRs
                                <span style="margin-top: -5px;float: right;font-size: 24px;">{{changeTypeCardCounts.dcrs}}</span>
                            </h5>
                        </div>
                    </div>
                    <div class="card bg-c-pink order-card">
                        <div class="card-block">
                            <h5>DCOs
                                <span style="margin-top: -5px;float: right;font-size: 24px;">{{changeTypeCardCounts.dcos}}</span>
                            </h5>
                        </div>
                    </div>
                    <div class="card bg-c-yellow order-card">
                        <div class="card-block">
                            <h5>MCOs
                                <span style="margin-top: -5px;float: right;font-size: 24px;">{{changeTypeCardCounts.mcos}}</span>
                            </h5>
                        </div>
                    </div>
                    <div class="card bg-c-skyline order-card">
                        <div class="card-block">
                            <h5>Deviations
                                <span style="margin-top: -5px;float: right;font-size: 24px;">{{changeTypeCardCounts.deviations}}</span>
                            </h5>
                        </div>
                    </div>
                    <div class="card bg-c-orange order-card">
                        <div class="card-block">
                            <h5>Waivers
                                <span style="margin-top: -5px;float: right;font-size: 24px;">{{changeTypeCardCounts.waivers}}</span>
                            </h5>
                        </div>
                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{ecrsByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="ecrReports">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{dcrsByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="dcrReports">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-body no-scroll">
                        <div id="ecoReports">

                        </div>
                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-body no-scroll">
                        <div id="dcoReports">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-body no-scroll">
                        <div id="mcoReports">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="deviationReports">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div id="waiverReports">

                    </div>
                </div>
                <div class="home-widget counts-chart-container" style="border: none">

                </div>
                <div class="home-widget counts-chart-container" style="border: none">

                </div>
            </div>
        </div>
    </div>
</div>