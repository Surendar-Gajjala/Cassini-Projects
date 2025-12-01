<style>
    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
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
        background-color: #fff;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="manpowerDetailsVm.back()">Back</button>
            </div>
            <h4 style="float: right;margin: 0;padding-right: 10px;">{{viewInfo.title}}</h4>
        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="manpowerDetailsVm.activeTab">
                        <uib-tab heading="{{manpowerDetailsVm.tabs.basic.heading}}"
                                 select="manpowerDetailsVm.manpowerDetailsTabActivated(manpowerDetailsVm.tabs.basic.id)">
                            <div ng-include="manpowerDetailsVm.tabs.basic.template"
                                 ng-controller="ManpowerBasicDetailsController as manpowerBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{manpowerDetailsVm.tabs.attributes.heading}}"
                                 select="manpowerDetailsVm.manpowerDetailsTabActivated(manpowerDetailsVm.tabs.attributes.id)">
                            <div ng-include="manpowerDetailsVm.tabs.attributes.template"
                                 ng-controller="ManpowerAttributesController as manpowerAttributeVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
