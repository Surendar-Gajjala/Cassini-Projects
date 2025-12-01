<div class="view-container" fitcontent>
    <style scoped>
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
        }

        .tab-content .tab-pane .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px !important;
            z-index: 5;
            background-color: #fff;
        }

    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default min-width" ng-click="itemDetailsVm.back()">
                Back
            </button>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="itemDetailsVm.active">
                        <uib-tab heading="{{itemDetailsVm.tabs.basic.heading}}"
                                 active="itemDetailsVm.tabs.basic.active"
                                 select="itemDetailsVm.itemDetailsTabActivated(itemDetailsVm.tabs.basic.id)">
                            <div ng-include="itemDetailsVm.tabs.basic.template"
                                 ng-controller="ItemBasicInfoController as itemBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{itemDetailsVm.tabs.attributes.heading}}"
                                 active="itemDetailsVm.tabs.attributes.active"
                                 select="itemDetailsVm.itemDetailsTabActivated(itemDetailsVm.tabs.attributes.id)">
                            <div ng-include="itemDetailsVm.tabs.attributes.template"
                                 ng-controller="ItemAttributesController as itemAttributesVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{itemDetailsVm.tabs.files.heading}}"
                                 active="itemDetailsVm.tabs.files.active"
                                 select="itemDetailsVm.itemDetailsTabActivated(itemDetailsVm.tabs.files.id)">
                            <div ng-include="itemDetailsVm.tabs.files.template"
                                 ng-controller="ItemFilesController as itemFilesVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{itemDetailsVm.tabs.instances.heading}}"
                                 active="itemDetailsVm.tabs.instances.active"
                                 select="itemDetailsVm.itemDetailsTabActivated(itemDetailsVm.tabs.instances.id)">
                            <div ng-include="itemDetailsVm.tabs.instances.template"
                                 ng-controller="ItemInstanceController as itemInstanceVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{itemDetailsVm.tabs.report.heading}}"
                                 active="itemDetailsVm.tabs.report.active"
                                 select="itemDetailsVm.itemDetailsTabActivated(itemDetailsVm.tabs.report.id)">
                            <div ng-include="itemDetailsVm.tabs.report.template"
                                 ng-controller="ItemReportController as itemReportVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>