<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>ITEMS_DASHBOARD</span>

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
                max-height: 350px;
                width: 100%;
                border: 1px solid #ddd;
                border-radius: 5px;
                margin-bottom: 20px;
                height: 350px;
            }

            .home-content-panel {
                position: absolute;
                top: 0;
                bottom: 0;
                left: 351px;
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
        <div class="home-side-panel">
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>TOP_PROBLEM_ITEMS</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th translate>ITEM_NAME</th>
                                <th style="width: 100px;text-align: center" translate>PROBLEMS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="itemDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_PROBLEM_ITEMS</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="itemDashboardVm.loading == false && itemDashboardVm.problemItems.length == 0">
                                <td colspan="10" translate>NO_PROBLEM_ITEMS</td>
                            </tr>
                            <tr ng-repeat="item in itemDashboardVm.problemItems"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.items.details({itemId: item.itemId})"
                                       title="{{clickToShowDetails}}">
                                        {{item.itemName}}</a>
                                </td>
                                <td style="text-align: center">
                                    {{item.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>TOP_PROBLEM_ITEMTYPES</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th translate>ITEM_TYPE</th>
                                <th style="width: 80px;text-align: center" translate>ITEMS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="itemDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_PROBLEM_ITEMTYPES</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="itemDashboardVm.loading == false && itemDashboardVm.problemItems.length == 0">
                                <td colspan="10" translate>NO_PROBLEM_ITEMTYPES</td>
                            </tr>
                            <tr ng-repeat="itemType in itemDashboardVm.problemItemTypes"
                                style="font-size: 14px;">
                                <td title="{{itemType.name}}">
                                    {{itemType.name}}
                                </td>
                                <td style="text-align: center">
                                    {{itemType.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>FREQUENTLY_CHANGING_ITEMTYPES</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th translate>ITEM_TYPE</th>
                                <th style="width: 80px;text-align: center" translate>CHANGES_TITLE</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="itemDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_F_C_ITEMTYPES</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="itemDashboardVm.loading == false && itemDashboardVm.changingItemTypes.length == 0">
                                <td colspan="10" translate>NO_F_C_ITEMTYPES</td>
                            </tr>
                            <tr ng-repeat="itemType in itemDashboardVm.changingItemTypes"
                                style="font-size: 14px;">
                                <td title="{{itemType.name}}">
                                    {{itemType.name}}
                                </td>
                                <td style="text-align: center">
                                    {{itemType.count}}
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
                <div class="container" style="margin: 0;padding: 0;">
                    <div class="data-card-row">
                        <div class="card bg-c-blue order-card">
                            <div class="card-block">
                                <h5>Products
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{itemClassCardCounts.products}}</span>
                                </h5>
                            </div>
                        </div>

                        <div class="card bg-c-pink order-card">
                            <div class="card-block">
                                <h5>Assemblies
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{itemClassCardCounts.assemblies}}</span>
                                </h5>
                            </div>
                        </div>

                        <div class="card bg-c-purple order-card">
                            <div class="card-block">
                                <h5>Parts
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{itemClassCardCounts.parts}}</span>
                                </h5>
                            </div>
                        </div>
                        <div class="card bg-c-orange order-card">
                            <div class="card-block">
                                <h5>Documents
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{itemClassCardCounts.documents}}</span>
                                </h5>
                            </div>
                        </div>
                        <div class="card bg-c-green order-card">
                            <div class="card-block">
                                <h5>Others
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{itemClassCardCounts.others}}</span>
                                </h5>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{productsByStatus}}</h5>
                    </div>
                    <div id="productItemsByStatusCounts">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{assembliesByStatus}}</h5>
                    </div>
                    <div id="assemblyItemsByStatusCounts">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{partsByStatus}}</h5>
                    </div>
                    <div id="partItemsByStatusCounts">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{documentsByStatus}}</h5>
                    </div>
                    <div id="documentItemsByStatusCounts">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{othersByStatus}}</h5>
                    </div>
                    <div id="otherItemsByStatusCounts">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="configurationCounts">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div id="productItemsByLifecycleCounts">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="assemblyItemsByLifecycleCounts">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="partItemsByLifecycleCounts">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div id="documentItemsByLifecycleCounts">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="otherItemsByLifecycleCounts">

                    </div>
                </div>
                <div class="home-widget counts-chart-container" style="border: none">

                </div>
            </div>
        </div>
    </div>
</div>