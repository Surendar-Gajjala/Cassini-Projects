<style scoped>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        position: absolute;
        overflow: auto;
        height: 100%;
    }

    /*.view-content .responsive-table table {
        table-layout: fixed;
    }*/

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        /*position: sticky;*/
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .bom-info-panel {
        display: none;
        z-index: 101;
        width: 300px;
        position: absolute;
        top: 0px;
        left: 0px;
        background-color: #fff;
        bottom: 0px;
        border: 1px solid #ddd;
        overflow-y: auto;
    }

    .bom-info-panel .info-panel-header {
        border-bottom: 1px solid #ddd;
        padding-left: 10px;
    }

    .bom-info-panel .info-panel-header h3 {
        line-height: 50px;
        margin: 0;
    }

    .info-panel-header .close {
        position: absolute;
        right: 10px;
        top: 10px;
        width: 32px;
        line-height: 50px;
        height: 50px;
        padding-top: 7px;
        margin-right: -5px;
        opacity: 0.3;

    }

    .info-panel-header .close:hover {
        opacity: 1;
    }

    .info-panel-header .close:before, .info-panel-header .close:after {
        position: absolute;
        left: 15px;
        content: ' ';
        height: 15px;
        width: 2px;
        background-color: #333;
    }

    .info-panel-header .close:before {
        transform: rotate(45deg);
    }

    .info-panel-header .close:after {
        transform: rotate(-45deg);
    }

    .info-panel-details {
        padding: 10px;
    }

</style>
<div class="view-container" fitcontent>

    <style scoped>
        .search-form {
            height: 30px;
            border-radius: 15px;
        }

        .sections-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 10; /* Sit on top */
            padding-top: 15px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .sections-model .modal-content {
            margin-left: auto;
            margin-right: auto;
            top: 111px;
            display: block;
            height: 70%;
            width: 40%;
        }

        .sections-model .report-content {
            margin-left: auto;
            margin-right: auto;
            top: 50px;
            display: block;
            height: 80%;
            width: 60%;
        }

        .sections-model .workCenter-content {
            margin-left: auto;
            margin-right: auto;
            margin-top: 50px;
            display: block;
            height: 80%;
            width: 80%;
        }

        .sidePanel.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 10; /* Sit on top */
            left: 0;
            top: 125px;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .sidePanel .right-side {
            margin-left: auto;
            display: block;
        }

        #freeTextSearchDirective {
            top: 7px !important;
            right: 0 !important;
        }
    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-xs" ng-click="bomVm.showBomInfoPanel()" title="Click to BOM Info">
                BOM Info
            </button>
            <button class="btn btn-xs" title="Print BOM" ng-if="bomVm.showAddItem" ng-click="showSectionsMenu()">
                <i class="fa fa-print" style="color: black;font-size: 18px;"></i>
            </button>
            <button class="btn btn-xs" title="Filter BOM Items by Work Center" ng-if="bomVm.showAddItem"
                    ng-click="showWorkCenterItems()">
                <i class="fa fa-filter" style="color: black;font-size: 18px;"></i>
            </button>

            <button class="btn btn-xs" ng-if="bomVm.showAddItem && hasPermission('permission.admin.all')"
                    ng-click="bomVm.synchronizeSelectedBom()">
                Synchronize BOM
            </button>
            <%--<button ng-if="bomVm.selectedBom.objectType == 'BOMINSTANCE'" class="btn btn-xs"
                    ng-click="bomVm.getInstanceRequestReport()" title="MBOM Request Report">
                <i class="fa flaticon-stamp13"></i>
            </button>--%>
        </div>
        <free-text-search ng-if="bomVm.selectedBom != null && bomVm.showAddItem" on-clear="resetBomSearch"
                          search-term="searchBomText"
                          on-search="searchBom"></free-text-search>
    </div>
    <div class="view-content no-padding" styl e="overflow-y: auto;padding: 10px;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="addType" ng-click="bomVm.addItemNumber()"></a></li>
                <li><a tabindex="-1" href="" id="deleteType" ng-click="bomVm.deleteItemNumber()"></a></li>
                <li><a tabindex="-1" href="" id="importBOM"></a></li>
            </ul>
        </div>

        <input type="file" style="display:none" id="fileUpload" name='file' custom-on-change="uploadFile"/>

        <div class="bom-info-panel" id="bomInfoPanel" style="padding: 0px">
            <div class="info-panel-header">
                <h3>BOM</h3>
                <a href="" ng-click="bomVm.showBomInfoPanel()"
                   class="close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="info-panel-details">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div>
                        <input type="search" class="form-control input-sm search-form" placeholder="Search"
                               ng-model="bomVm.searchValue" ng-change="bomVm.searchTree()">
                    </div>
                    <ul id="classificationMainTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
        </div>
        <div ng-include="'app/desktop/modules/bom/details/bomDetailsView.jsp'"
             ng-controller="BomDetailsController as bomDetailsVm"></div>


        <%-- <div class="split-pane fixed-left">
                 <div class="split-pane-component" style="width: 200px; padding: 10px;">

                 </div>
                 <div class="split-pane-divider" style="left: 200px !important;"></div>
                 <div class="split-pane-component split-right-pane noselect"
                      style="left: 200px;right: -10px !important;top: -10px !important;height: 103%;">

                 </div>
             </div>--%>

        <div id="report-view" class="sections-model modal">
            <div class="report-content">
                <div class="responsive-table"
                     style="width: 100%;overflow-y: auto;position: relative;background: white;">
                    <table class="table table-striped" id="printRequestReport">
                        <thead>
                        <tr>
                            <th class="threeHundred-column">Nomenclature</th>
                            <th>Type</th>
                            <th>BOM Quantity</th>
                            <th>Requested Quantity</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="bomItem in bomVm.requestReportItems">
                            <td class="threeHundred-column">
                                <span class="level{{bomItem.item.level}}" ng-if="bomItem.item.bomItemType != 'PART'">
                                    <i class="mr5 fa fa-caret-down"
                                       style="cursor: pointer; color: #909090;font-size: 18px;">
                                    </i>
                                    <span>{{bomItem.item.typeRef.name}}<span ng-if="bomItem.item.typeRef.versity"> ( VSPL )</span></span>
                                </span>
                                <span class="level{{bomItem.item.level}}" ng-if="bomItem.item.bomItemType == 'PART'">
                                    {{bomItem.item.item.itemMaster.itemName}}
                                </span>
                            </td>
                            <td>
                                <bom-group-type ng-if="bomItem.item.bomItemType != 'PART'"
                                                object="bomItem.item.typeRef"></bom-group-type>
                                <span>{{bomItem.item.item.itemMaster.parentType.name}}</span>
                            </td>
                            <td>
                                <span class="badge badge-primary"
                                      ng-if="bomItem.item.bomItemType == 'PART' && !bomItem.item.item.itemMaster.itemType.hasLots"
                                      style="font-size: 13px;">{{bomItem.item.quantity}}
                                </span>
                                <span class="badge badge-primary"
                                      ng-if="bomItem.item.bomItemType == 'PART' && bomItem.item.item.itemMaster.itemType.hasLots"
                                      style="font-size: 13px;">{{bomItem.item.fractionalQuantity}}
                                </span>
                            </td>
                            <td>
                                <span class="badge badge-success"
                                      ng-if="bomItem.item.bomItemType == 'PART' && !bomItem.item.item.itemMaster.itemType.hasLots"
                                      style="font-size: 13px;">{{bomItem.requested}}
                                </span>
                                <span class="badge badge-success"
                                      ng-if="bomItem.item.bomItemType == 'PART' && bomItem.item.item.itemMaster.itemType.hasLots"
                                      style="font-size: 13px;">{{bomItem.fractionalRequested}}
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div style="height: 10%;text-align: center;background: lightgrey;">
                    <button class="btn btn-sm btn-default" ng-click="bomVm.closeRequestReport()"
                            style="margin-top: 5px;">Close
                    </button>
                    <button class="btn btn-sm btn-primary" ng-click="bomVm.printRequestReport()"
                            style="margin-top: 5px;">Print
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
