<style>
    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }

    #roadChallan-container #roadChallan-content {
        padding: 10px;
        position: relative;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    #roadChallan-container .table-footer {
        padding: 0 10px 0 10px;
        position: relative;
        bottom: 0;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
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
<div id="roadChallan-container" class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="roadChallanDetailsVm.back()">Back</button>
                <button class="min-width btn btn-sm btn-warning"
                        ng-if="roadChallanDetailsVm.addItem == true && storeRoadChallanItemsList.content.length > 0"
                        ng-click="roadChallanDetailsVm.printRoadChallan()">Print
                </button>
                <button class="min-width btn btn-sm btn-warning"
                        ng-if="roadChallanDetailsVm.addItem == true && storeRoadChallanItemsList.content.length > 0"
                        ng-click="roadChallanDetailsVm.showAttributes()">Show Attributes
                </button>
            </div>
            <h4 style="float: right;margin: 0;padding-right: 10px;">Store Details : {{title}}</h4>
        </div>
    </div>


    <div id="roadChallan-content" class="view-content no-padding" style="overflow-y: hidden;padding: 10px;">
        <div class="item-details-tabs">
            <uib-tabset active="roadChallanDetailsVm.activeTab">
                <uib-tab heading="{{roadChallanDetailsVm.tabs.basic.heading}}"
                         select="roadChallanDetailsVm.detailsTabActivated(roadChallanDetailsVm.tabs.basic.id)">
                    <div ng-include="roadChallanDetailsVm.tabs.basic.template"
                         ng-controller="RoadChallanBasicController as basicVm"></div>
                </uib-tab>
                <uib-tab heading="{{roadChallanDetailsVm.tabs.items.heading}}"
                         ng-if="hasPermission('permission.roadChallans.addRoadChalanItems') || hasPermission('permission.roadChallans.editRoadChalanItems')"
                         select="roadChallanDetailsVm.detailsTabActivated(roadChallanDetailsVm.tabs.items.id)">
                    <div ng-include="roadChallanDetailsVm.tabs.items.template"
                         ng-controller="RoadChallanItemsController as roadItemsVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
    <div class="table-footer" id="detailsFooter">
        <div class="col-md-12" style="margin-top: 5px;" ng-if="selectedRoadChallanDetailsTab != 'details.basic'">
            <div class="col-md-6">
        <span ng-if="selectedRoadChallanDetailsTab == 'details.items'" style="font-weight: 700">
                    Displaying {{storeRoadChallanItemsList.numberOfElements}} of {{storeRoadChallanItemsList.totalElements}}</span>
            </div>
            <div class="col-md-6 text-right">
        <span ng-if="selectedRoadChallanDetailsTab == 'details.items'">
                    <span class="mr10">Page {{storeRoadChallanItemsList.totalElements != 0 ? storeRoadChallanItemsList.number+1:0}} of {{storeRoadChallanItemsList.totalPages}}</span>
                        <a href="" ng-click="roadChallanDetailsVm.previousPage()"
                           ng-class="{'disabled': storeRoadChallanItemsList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="roadChallanDetailsVm.nextPage()"
                           ng-class="{'disabled': storeRoadChallanItemsList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
        </span>
            </div>
        </div>
    </div>
</div>
