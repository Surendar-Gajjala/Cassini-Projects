<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>QUALITY_DASHBOARD</span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;" <%--ng-init="qualityDashboardVm.initCharts()"--%>>
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
                background: linear-gradient(45deg, #ffa500, #ffc03c);
            }

            .bg-c-cyan {
                background: linear-gradient(45deg, #05cffc, #04ccf0);
            }

            .bg-c-purple {
                background: linear-gradient(45deg, #6036f9, #626ef7);
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
                padding: 8px 10px;
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
                margin: 5px;
                flex: 1
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


            .widget1-window-model.modal {
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

            .widget1-window-model .widget1-window-content {
                margin: auto;
                display: block;
                height: 94%;
                width: 97%;
                background-color: white;
                border-radius: 7px !important;
            }

            .widget1-window-header {
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



            .window1-header {
                font-weight: bold;
                font-size: 22px;
                /*position: absolute;*/
                display: inline-block;
                /*left: 44%;*/
                margin-top: 7px;
            }

            .window1-content {
                padding: 10px;
                overflow: auto;
                min-width: 100%;
                width: auto;
            }

            .window1-close {
                position: absolute;
                right: 25px;
                width: 38px;
                height: 38px;
                opacity: 0.3;
            }

            .window1-close:hover {
                opacity: 0.6;
                border-radius: 50%;
                background-color: #ddd;
            }

            .window1-close:before, .window1-close:after {
                position: absolute;
                top: 7px;
                left: 18px;
                content: ' ';
                height: 22px;
                width: 2px;
                background-color: #333;
            }

            .window1-close:before {
                transform: rotate(45deg) !important;
            }

            .window1-close:after {
                transform: rotate(-45deg) !important;
            }


        </style>
        <div class="home-side-panel">
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>INSPECTION_FAILED_PRODUCTS</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>PRODUCT_NAME</th>
                                <th style="width: 100px;" translate>INSPECTIONS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="qualityDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_FAILED_INSPECTIONS</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="qualityDashboardVm.loading == false && qualityDashboardVm.inspectionFailureProducts.length == 0">
                                <td colspan="10" translate>NO_FAILED_INSPECTIONS</td>
                            </tr>
                            <tr ng-repeat="product in qualityDashboardVm.inspectionFailureProducts"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.items.details({itemId: product.productId})"
                                       title="{{clickToShowDetails}}">{{product.itemName}}</a>
                                </td>
                                <td>
                                    {{product.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>INSPECTION_FAILED_MATERIALS</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>PART_NAME</th>
                                <th style="width: 100px;" translate>INSPECTIONS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="qualityDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>NO_IF_MATERIALS</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="qualityDashboardVm.loading == false && qualityDashboardVm.inspectionFailureMaterials.length == 0">
                                <td colspan="10" translate>NO_FAILED_M_INSPECTIONS</td>
                            </tr>
                            <tr ng-repeat="material in qualityDashboardVm.inspectionFailureMaterials"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.mfr.mfrparts.details({mfrId: material.mfrId, manufacturePartId: material.mfrPartId})"
                                       title="{{clickToShowDetails}}">{{material.partName}}</a>
                                </td>
                                <td>
                                    {{material.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>CUSTOMER_REPORTING_PROBLEMS</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>CUSTOMER_NAME</th>
                                <th style="width: 100px;" translate>PROBLEMS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="qualityDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>L_CUSTOMER_REPORTING_PROBLEMS</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="qualityDashboardVm.loading == false && qualityDashboardVm.customerReportingProblems.length == 0">
                                <td colspan="10" translate>NO_CUSTOMER_REPORTING_PROBLEMS</td>
                            </tr>
                            <tr ng-repeat="product in qualityDashboardVm.customerReportingProblems"
                                style="font-size: 14px;">
                                <td>{{product.customerName}}</td>
                                <td>
                                    {{product.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>TOP_PRODUCT_PROBLEMS</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>PRODUCT_NAME</th>
                                <th style="width: 100px;" translate>PROBLEMS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="qualityDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_PRODUCT_PROBLEMS</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="qualityDashboardVm.loading == false && qualityDashboardVm.productProblems.length == 0">
                                <td colspan="10" translate>NO_PRODUCT_PROBLEMS</td>
                            </tr>
                            <tr ng-repeat="product in qualityDashboardVm.productProblems"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.items.details({itemId: product.productId})"
                                       title="{{clickToShowDetails}}">{{product.itemName}}</a>
                                </td>
                                <td>
                                    {{product.count}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="sidepanel-widget">
                <div class="widget-header" style="border-bottom: 1px solid #ddd;">
                    <h5 translate>T_MANUFACTURER_FOR_NCR</h5>
                </div>
                <div class="widget-body">
                    <div class='responsive-table'
                         style="height: 100%;overflow:auto;width: 100%;position: relative;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr style="font-size: 14px;">
                                <th class="name-column3" translate>MANUFACTURER_NAME</th>
                                <th style="width: 100px;" translate>NCRS</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="qualityDashboardVm.loading == true">
                                <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>L_MFR_FOR_NCRS</span>
                           </span>
                                </td>
                            </tr>
                            <tr ng-if="qualityDashboardVm.loading == false && qualityDashboardVm.manufacturersForNCR.length == 0">
                                <td colspan="10" translate>NO_MFR_FOR_NCR</td>
                            </tr>
                            <tr ng-repeat="manufacturer in qualityDashboardVm.manufacturersForNCR"
                                style="font-size: 14px;">
                                <td>
                                    <a ui-sref="app.mfr.details({manufacturerId:manufacturer.mfrId})"
                                       title="{{clickToShowDetails}}">{{manufacturer.manufacturer}}
                                    </a>
                                </td>
                                <td>
                                    {{manufacturer.count}}
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
                                <h5>PRs
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{qualityTypeCardCounts.problemReports}}</span>
                                </h5>

                                <p>Implemented
                                    <span style="float: right;font-size: 16px;">{{qualityTypeCardCounts.implementedPrs}}</span>
                                </p>
                            </div>
                        </div>

                        <div class="card bg-c-pink order-card">
                            <div class="card-block">
                                <h5>NCRs
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{qualityTypeCardCounts.ncrs}}</span>
                                </h5>

                                <p>Implemented<span style="float: right;font-size: 16px;">{{qualityTypeCardCounts.implementedNcrs}}</span>
                                </p>
                            </div>
                        </div>

                        <div class="card bg-c-yellow order-card">
                            <div class="card-block">
                                <h5>QCRs
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{qualityTypeCardCounts.qcrs}}</span>
                                </h5>

                                <p>Implemented<span style="float: right;font-size: 16px;">{{qualityTypeCardCounts.implementedQcrs}}</span>
                                </p>
                            </div>
                        </div>
                        <div class="card bg-c-green order-card">
                            <div class="card-block">
                                <h5>Inspection Plans
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{qualityTypeCardCounts.inspectionPlans}}</span>
                                </h5>

                                <p>
                                    Approved<span style="float: right;font-size: 16px;">{{qualityTypeCardCounts.approvedInspectionPlans}}</span>
                                </p>
                            </div>
                        </div>
                        <div class="card bg-c-cyan order-card">                                                                            
                            <div class="card-block">
                                <h5>Supplier Audits
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{qualityTypeCardCounts.supplierAudits}}</span>
                                </h5>

                                <p>Approved<span style="float: right;font-size: 16px;">{{qualityTypeCardCounts.approvedSupplierAudits}}</span>
                                </p>
                            </div>
                        </div>         
                        <div class="card bg-c-orange order-card">
                            <div class="card-block">
                                <h5>Inspections
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{qualityTypeCardCounts.inspections}}</span>
                                </h5>

                                <p>
                                    Approved<span style="float: right;font-size: 16px;">{{qualityTypeCardCounts.approvedInspections}}</span>
                                </p>
                            </div>
                        </div>
                        <div class="card bg-c-purple order-card">
                            <div class="card-block">
                                <h5>PPAP
                                    <span style="margin-top: -5px;float: right;font-size: 24px;">{{qualityTypeCardCounts.ppap}}</span>
                                </h5>

                                <p>
                                    Approved<span style="float: right;font-size: 16px;">{{qualityTypeCardCounts.approvedPpap}}</span>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{inspectionPlanByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="inspectionPlanByStatus">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{inspectionByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="inspectionsByStatus">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="problemReportsBySource">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{problemReportByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="problemReportsByStatus">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="problemReportsBySeverity">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="problemReportsByFailure">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div id="problemReportsByDisposition">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{ncrByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="ncrByStatus">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="ncrsBySeverity">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div id="ncrsByFailure">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="ncrsByDisposition">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="qcrByType">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <h5>{{qcrByStatus}}</h5>
                    </div>
                    <div class="widget-body no-scroll">
                        <div id="qcrByStatus">

                        </div>
                    </div>
                </div>
                <div class="home-widget counts-chart-container">                          
                    <div class="widget-header">
                        <h5>Supplier Audit Status By Supplier</h5>
                    </div>
                    <div id="supplierAuditByStatus">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div id="ppapByStatus">

                    </div>
                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <i class="la la-expand" style="float: right;cursor: pointer;" title="Maximize widget"
                        ng-click="maximizeWidget('inspectionReports')"></i>
                        <h5>{{inspectionReports}}</h5>
                    </div>
                    <div id="inspectionReports">

                    </div>
                </div>
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <i class="la la-expand" style="float: right;cursor: pointer;" title="Maximize widget"
                        ng-click="maximizeWidget('supplierAuditByYear')"></i>
                        <h5>{{supplierAuditByYear}}</h5>
                    </div>
                    <div id="supplierAuditByYear">

                    </div>
                </div>
    

                <div class="home-widget counts-chart-container" style="border: none">

                </div>
                <div class="home-widget counts-chart-container" style="border: none">

                </div>
            </div>
            <div class="widgets-row">
                <div class="home-widget counts-chart-container">
                    <div class="widget-header">
                        <i class="la la-expand" style="float: right;cursor: pointer;" title="Maximize widget"
                           ng-click="maximizePPAPChecklistWidget('ppapChecklistReports')"></i>
                        <h5>{{ppapChecklistStatusTitle}}</h5>
                    </div>
                    <div id="ppapChecklistStatus">

                    </div>
                </div>

                <div class="home-widget counts-chart-container" style="border: none">

                </div>
                <div class="home-widget counts-chart-container" style="border: none">

                </div>
            </div>
        </div>
    </div>
    <div id="widget-window" class="widget-window-model modal">
        <div class="widget-window-content">
            <div class="widget-window-header">
                <span class="window-header" ng-if="maxType == 'inspectionReports'">Inspection Reports by Status</span>
                <span class="window-header" ng-if="maxType == 'supplierAuditByYear'">Supplier Audit By Year</span>
                <a href="" ng-click="hideWidgetWindow()" class="window-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div id="maximizeInpectionReports" class="window-content" style="padding: 0;overflow: hidden;"></div>
        </div>
    </div>
    <div id="widget1-window" class="widget1-window-model modal">
        <div class="widget1-window-content">
            <div class="widget1-window-header">
                <span class="window1-header" ng-if="maxType == 'ppapChecklistReports'">PPAP CheckList Reports</span>
                <a href="" ng-click="hideWidgetPPAPWindow()" class="window1-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div id="maximizeCheckListReports" class="window1-content" style="padding: 0;overflow: hidden;"></div>
        </div>
    </div>
</div>