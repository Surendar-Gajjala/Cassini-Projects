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

    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.req.document.template.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="reqDocTemplateDetailsVm.active">
                        <uib-tab heading="{{reqDocTemplateDetailsVm.tabs.basic.heading}}"
                                 active="reqDocTemplateDetailsVm.tabs.basic.active"
                                 select="reqDocTemplateDetailsVm.tabActivated(reqDocTemplateDetailsVm.tabs.basic.id)">
                            <div ng-include="reqDocTemplateDetailsVm.tabs.basic.template"
                                 ng-controller="ReqDocTemplateBasicController as reqDocTemplateBasicVm"></div>
                        </uib-tab>

                        <uib-tab id="reviewers" heading="{{reqDocTemplateDetailsVm.tabs.reviewers.heading}}"
                                 active="reqDocTemplateDetailsVm.tabs.reviewers.active"
                                 select="reqDocTemplateDetailsVm.tabActivated(reqDocTemplateDetailsVm.tabs.reviewers.id)">
                            <div ng-include="reqDocTemplateDetailsVm.tabs.reviewers.template"
                                 ng-controller="ReqDocTemplateReviewerController as reqDocTemplateReviewerVm"></div>
                        </uib-tab>
                        <uib-tab id="reqs" heading="{{reqDocTemplateDetailsVm.tabs.requirements.heading}}"
                                 active="reqDocTemplateDetailsVm.tabs.requirements.active"
                                 select="reqDocTemplateDetailsVm.tabActivated(reqDocTemplateDetailsVm.tabs.requirements.id)">
                            <div ng-include="reqDocTemplateDetailsVm.tabs.requirements.template"
                                 ng-controller="ReqDocTemplateRequirementController as reqDocTemplateRequirementVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="reqDocTemplateDetailsVm.tabs" custom-tabs="reqDocTemplateDetailsVm.customTabs"
                                     object-value="reqDocTemplateDetailsVm.reqDocTemplate" tab-id="reqDocTemplateDetailsVm.tabId" active="reqDocTemplateDetailsVm.active"></plugin-tabs>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>




