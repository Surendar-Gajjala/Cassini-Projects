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
                <button class="btn btn-sm btn-default min-width" ng-click="indentDetailsVm.back()">Back</button>
                <button ng-if="indentDetailsVm.addItem == true && indent.status != 'APPROVED'"
                        class="min-width btn btn-sm btn-warning"
                        ng-disabled="hasPermission('permission.indents.addIndentItems') == false"
                        ng-click="indentDetailsVm.addItems()">Add Items
                </button>
                <button ng-if="groupedItems.length > 0 && indent.status != 'APPROVED' && !indentDetailsVm.showApproveButton && hasPermission('permission.indents.editIndentItems')"
                        class="min-width btn btn-sm btn-default"
                        ng-click="indentDetailsVm.updateIndent()">Update
                </button>
                <button ng-if="indentDetailsVm.addItem == true" class="min-width btn btn-sm btn-warning"
                        ng-click="indentDetailsVm.printIndentChallan()">Print
                </button>
                <button ng-if="groupedItems.length > 0 && indent.status != 'APPROVED' && indentDetailsVm.showApproveButton"
                        class="min-width btn btn-sm btn-default"
                        ng-disabled="hasPermission('permission.indents.approveIndent') == false"
                        ng-click="indentDetailsVm.approveIndent()">Approve
                </button>
            </div>
            <h4 style="float: right;margin: 0;padding-right: 10px;">Store Details : {{title}}</h4>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="indentDetailsVm.activeTab">
                        <uib-tab heading="{{indentDetailsVm.tabs.basic.heading}}"
                                 select="indentDetailsVm.detailsTabActivated(indentDetailsVm.tabs.basic.id)">
                            <div ng-include="indentDetailsVm.tabs.basic.template"
                                 ng-controller="IndentBasicDetailsController as indentBasicDetailsVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{indentDetailsVm.tabs.items.heading}}" ng-if="hasPermission('permission.indents.viewIndentItems') || hasPermission('permission.indents.addIndentItems') ||
                                                                                hasPermission('permission.indents.removeIndentItems') || hasPermission('permission.indents.editIndentItems')"
                                 select="indentDetailsVm.detailsTabActivated(indentDetailsVm.tabs.items.id)">
                            <div ng-include="indentDetailsVm.tabs.items.template"
                                 ng-controller="IndentRequisitionItemsController as indentRequisitionItemsVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
