<style scoped>
    .item-number {
        display: inline-block;
    }

    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        color: #555 !important;
        cursor: default !important;
        background-color: transparent !important;
        border-color: #ddd #ddd transparent #ddd !important;
        border-bottom: 2px solid #2a6fa8 !important;
    }

    .item-rev {
        font-size: 16px;
        font-weight: normal;
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
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default min-width" ng-click="itemVm.back()">
                Back
            </button>
            <%--<div class="btn-group" ng-if="itemVm.tabs.bom.active">
                <div class="btn-group">
                    <button type="button" class="btn btn-sm btn-success min-width dropdown-toggle"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false"
                            ng-disabled="itemVm.lifeCycleStatus == 'RELEASED' || itemVm.lifeCycleStatus == 'OBSOLETE'">
                        Add Item <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a href="" ng-click="itemVm.broadcast('app.item.addbom')">Single</a></li>
                        <li><a href="" ng-click="itemVm.broadcast('app.item.bom.multiple')">Multiple</a></li>
                    </ul>
                </div>
            </div>--%>
            <button ng-if="itemVm.tabs.files.active"
                    class="btn btn-sm btn-success min-width" ng-click="itemVm.onAddItemFiles()">Add Files
            </button>
        </div>
    </div>

    <div class="view-content no-padding">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset>
                        <uib-tab heading="{{itemVm.tabs.basic.heading}}" active="itemVm.tabs.basic.active"
                                 select="itemVm.itemDetailsTabActivated(itemVm.tabs.basic.id)">
                            <div ng-include="itemVm.tabs.basic.template"
                                 ng-controller="ItemBasicInfoController as itemBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{itemVm.tabs.attributes.heading}}" active="itemVm.tabs.attributes.active"
                                 select="itemVm.itemDetailsTabActivated(itemVm.tabs.attributes.id)">
                            <div ng-include="itemVm.tabs.attributes.template"
                                 ng-controller="ItemAttributesController as itemAttributesVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{itemVm.tabs.bom.heading}}" active="itemVm.tabs.bom.active"
                                 select="itemVm.itemDetailsTabActivated(itemVm.tabs.bom.id)">
                            <div ng-include="itemVm.tabs.bom.template"
                                 ng-controller="ItemBomController as itemBomVm"></div>
                        </uib-tab>--%>
                        <uib-tab heading="{{itemVm.tabs.files.heading}}" active="itemVm.tabs.files.active"
                                 select="itemVm.itemDetailsTabActivated(itemVm.tabs.files.id)">
                            <div ng-include="itemVm.tabs.files.template"
                                 ng-controller="ItemFilesController as itemFilesVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
