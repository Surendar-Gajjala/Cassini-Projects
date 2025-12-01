<div class="view-container" fitcontent>
    <style>
        .item-details-tabs .tab-content {
            padding: 0 !important;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

        .tab-content {
            padding: 0px !important;
        }

        .tab-content .tab-pane {
            overflow: auto !important;
        }

        .tab-pane {
            position: relative;
        }

        .tab-content .tab-pane .responsive-table {
            height: 100%;
            position: absolute;
            overflow: auto !important;
            padding: 5px;
        }

        .tab-content .tab-pane .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px !important;
            z-index: 5;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        .item-rev {
            font-size: 16px;
            font-weight: normal;
        }

        .item-number {
            display: inline-block;
        }

        .req-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 100px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .req-model .reqModel-content {
            margin: auto;
            display: block;
            height: 235px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        #req-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        #req-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }

        .req-header {
            font-weight: bold;
            font-size: 22px;
        }

        #req-footer {
            border-top: 1px solid lightgrey;
            padding: 5px;
            text-align: right;
            height: 40px;
            width: 100%;
            background-color: #edeeef;
            border-bottom-left-radius: 7px;
            border-bottom-right-radius: 7px;
        }

        .center {
            display: block;
            margin-left: auto;
            margin-right: auto;
            margin-top: 4%;
            width: 300px;
        }

    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="reqTemplateDetailsVm.showAllRequirements()"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="reqTemplateDetailsVm.active">
                        <uib-tab heading="{{reqTemplateDetailsVm.tabs.basic.heading}}"
                                 active="reqTemplateDetailsVm.tabs.basic.active"
                                 select="reqTemplateDetailsVm.tabActivated(reqTemplateDetailsVm.tabs.basic.id)">
                            <div ng-include="reqTemplateDetailsVm.tabs.basic.template"
                                 ng-controller="ReqTemplateBasicInfoController as reqTemplateBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="reviewer" heading="Reviewers"
                                 active="reqTemplateDetailsVm.tabs.reviewers.active"
                                 select="reqTemplateDetailsVm.tabActivated(reqTemplateDetailsVm.tabs.reviewers.id)">
                            <div ng-include="reqTemplateDetailsVm.tabs.reviewers.template"
                                 ng-controller="ReqTemplateReviewersController as reqTemplateReviewersVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="reqTemplateDetailsVm.tabs" custom-tabs="reqTemplateDetailsVm.customTabs"
                                     object-value="reqTemplateDetailsVm.reqTemplate" tab-id="reqTemplateDetailsVm.tabId" active="reqTemplateDetailsVm.active"></plugin-tabs>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>