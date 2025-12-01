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
                <button class="btn btn-sm btn-default min-width" ng-click="siteVm.back()">Back</button>
            </div>
            <h4 style="text-align:right;float: right;margin: 0;padding-right: 10px;vertical-align: middle;width: 500px;max-width:500px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">
                Site Details : {{viewInfo.title}}</h4>
        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset>
                        <uib-tab heading="{{siteVm.tabs.basic.heading}}" active="siteVm.tabs.basic.active"
                                 select="siteVm.siteDetailsTabActivated(siteVm.tabs.basic.id)">
                            <div ng-include="siteVm.tabs.basic.template"
                                 ng-controller="SiteBasicDetailsController as siteBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="tasks"
                                 heading="{{siteVm.tabs.tasks.heading}}" active="siteVm.tabs.tasks.active"
                                 ng-show="hasPermission('permission.sites.tasks')"
                                 select="siteVm.siteDetailsTabActivated(siteVm.tabs.tasks.id)">
                            <div ng-include="siteVm.tabs.tasks.template"
                                 ng-controller="SiteTasksDetailsController as siteTasksVm"></div>
                        </uib-tab>
                        <uib-tab id="resources"
                                 heading="{{siteVm.tabs.resources.heading}}" active="siteVm.tabs.resources.active"
                                 ng-show="hasPermission('permission.sites.resources')"
                                 select="siteVm.siteDetailsTabActivated(siteVm.tabs.resources.id)">
                            <div ng-include="siteVm.tabs.resources.template"
                                 ng-controller="SiteResourcesController as siteResourcesVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
