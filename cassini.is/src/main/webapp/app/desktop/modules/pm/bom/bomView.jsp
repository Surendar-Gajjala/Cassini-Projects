<style scoped>

    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="row" style="margin: 0">
            <div class="col-sm-6">
                <div class="btn-group">
                    <%--  <button class="min-width btn btn-sm btn-default" ng-click="bomVm.back()">Back</button>--%>
                    <button class="min-width btn btn-sm btn-success"
                            ng-click="bomVm.saveBomGroups()"
                            ng-disabled="selectedProject.locked == true || bomVm.dbHide == false || !(hasPermission('permission.bom.create') && hasPermission('permission.bom.edit') || login.person.isProjectOwner)">
                        {{ running ? 'Please wait...' : 'Save' }}
                    </button>
                    <button class="min-width btn btn-sm btn-info" ng-if="bomVm.bomGroups.length >=1"
                            ng-click="bomVm.select()"
                            ng-disabled="(selectedProject.locked == true) || !(hasPermission('permission.bom.create') || login.person.isProjectOwner)">
                        Select Items
                    </button>
                    <button class="btn min-width btn-sm" height="30px" ng-click="exportinputParams()"
                            ng-disabled="(selectedProject.locked == true) || !(hasPermission('permission.bom.import') || login.person.isProjectOwner)">
                        Export
                    </button>

                    <input type="file" id="file" value="file" multiple
                           ng-disabled="(selectedProject.locked == true) || !(hasPermission('permission.bom.import') || login.person.isProjectOwner)"
                           title="Click to Import bomItems" style="display:none;"/>
                    <label for="file" class="btn btn-sm min-width"
                           ng-disabled="(selectedProject.locked == true) || !(hasPermission('permission.bom.import') || login.person.isProjectOwner)">
                        Import
                    </label>


                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            ng-if="hasPermission('permission.admin.all') || specPermission.importPermission == true"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="mr5">Import</span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" style="margin-left: 321px;">
                        <li onclick="$('#fileExcel').click()"><a href="">Excel</a></li>
                        <li onclick="$('#filePdf').click();"><a href="">Pdf</a></li>
                    </ul>


                    <input type="file" id="fileExcel" value="file"
                           onchange="angular.element(this).scope().importExcel()"
                           style="display: none">

                    <input type="file" id="filePdf" value="file"
                           onchange="angular.element(this).scope().importPdf()"
                           style="display: none">

                    <button type="button" ng-if="bomVm.pdfFile != null && bomVm.pdfFile != ''"
                            class="btn btn-sm btn-info"
                            ng-click="bomVm.downloadPdf(bomVm.pdfFile)">
                        DownloadPDF
                    </button>

                    <button type="button" ng-if="bomVm.pdfFile != null && bomVm.pdfFile != ''"
                            class="btn btn-sm btn-info"
                            ng-click="bomVm.filePreview(bomVm.pdfFile)">
                        PDFPreview
                    </button>


                </div>
            </div>
        </div>
        <free-text-search on-clear="bomVm.resetPage"
                          on-search="bomVm.freeTextSearch"></free-text-search>
    </div>

    <div id="view-content" class="view-content no-padding" style="padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="text-align: center; width:200px">Item Number</th>
                    <th style="text-align: left; width:200px">Item Name</th>
                    <th style="text-align: left; width:200px">Item Description</th>
                    <th style="text-align: left; width:150px">Item Type</th>
                    <th style="text-align: center; width:150px">Units</th>
                    <th style="text-align: center; width:150px"> BOQ Qty</th>
                    <th style="text-align: center; width:150px">Received Qty</th>
                    <th style="text-align: center; width:150px">Issued Qty</th>
                    <th style="text-align: center; width:150px">Returned Qty</th>
                    <th style="text-align: center; width:150px">Balance qty to be supplied</th>
                    <th style="text-align: center; width:150px">Balance qty to be issued</th>
                    <th style="text-align: center; width:150px">Inventory</th>
                    <th style="text-align: left; width:200px">Notes</th>
                    <th style="text-align: center; min-width:100px">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="bomVm.loading == true || (bomVm.currentBomGroup.bomItems.length == undefined && bomVm.currentBomGroup != undefined)">
                    <td colspan="15">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Items...
                        </span>
                    </td>
                </tr>

                <tr ng-if="bomVm.loading == false && bomVm.currentBomGroup.bomItems.length == 0">
                    <td colspan="15" style="text-align: center">No BOQ Items</td>
                </tr>

                <tr ng-repeat="bomItem in bomVm.currentBomGroup.bomItems">
                    <td style="text-align: center; width:200px;!important;">{{bomItem.itemNumber}}</td>
                    <td title="{{bomItem.itemName}}">{{bomItem.itemName | limitTo: 15}}{{bomItem.itemName.length > 15 ?
                        '...' : ''}}
                    </td>
                    <td title="{{bomItem.description}}">
                        {{bomItem.description | limitTo: 20}}{{bomItem.description.length > 20 ? '...' : ''}}
                    </td>
                    <td style="text-align: left; width:150px; !important">{{bomItem.resourceTypeName}}</td>
                    <td style="text-align: center" title="{{bomItem.units}}">{{bomItem.units | limitTo:
                        15}}{{bomItem.units.length > 15 ? '...' : ''}}
                    </td>
                    <td style="text-align: center; width:150px;!important;">
                       <span ng-if="(bomItem.editMode == false && bomItem.edit == false)"
                             style="text-align: center; width: 150px;"><span
                               ng-bind-html="bomItem.quantity | number"></span></span>
                        <input ng-if="(bomItem.editMode == true || bomItem.edit == true)" type="number"
                               class="form-control input-sm" style="text-align: center"
                               ng-model="bomItem.newQuantity">
                    </td>
                    <td style="text-align: center; width:150px; !important">{{bomItem.receivedQty | number}}</td>
                    <td style="text-align: center; width:150px; !important">{{bomItem.issuedQty | number}}</td>
                    <td style="text-align: center; width:150px; !important">{{bomItem.returnedQty | number}}</td>
                    <td style="text-align: center; width:150px; !important">{{bomItem.totalBoqQuantity -
                        bomItem.receivedQty |
                        number}}
                    </td>
                    <td style="text-align: center; width:150px; !important">{{(bomItem.totalBoqQuantity +
                        bomItem.returnedQty) - bomItem.issuedQty |
                        number}}
                    </td>
                    <td style="text-align: center; width:150px; !important">{{bomItem.inventory}}</td>

                    <td title="{{bomItem.notes}}">
              <span ng-if="(bomItem.editMode == false && bomItem.edit == false)"
                    style="text-align: left; width: 200px;"><span>{{bomItem.notes | limitTo: 15}}{{bomItem.notes.length > 15 ? '...' : ''}}</span></span>
                        <input ng-if="(bomItem.editMode == true || bomItem.edit == true)" type="text"
                               class="form-control input-sm" style="text-align: left"
                               ng-model="bomItem.newNotes ">
                    </td>

                    <td class="text-center">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                                          ng-if="bomItem.showValues == true || bomItem.editMode == true">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="bomVm.applyChanges(bomItem)">
                                            <a class="dropdown-item" title="Save" type="button"
                                               ng-if="(bomItem.editMode == true)">
                                                <span style="padding-left: 3px;">Save</span>
                                            </a></li>
                                        <li ng-click="bomVm.cancelChanges(bomItem)">
                                            <a class="dropdown-item" type="button"
                                               ng-if="(bomItem.editMode == true)"
                                               title="Cancel Changes">

                                                <span style="padding-left: 3px;">Cancel</span>
                                            </a>
                                        </li>
                                        <li ng-click="bomVm.editBomItem(bomItem)"
                                            ng-if="(bomItem.showValues == true)">
                                            <a title="Edit BOQ Item" type="button"
                                               class="dropdown-item"
                                               ng-disabled="selectedProject.locked == true || !(hasPermission('permission.bom.edit') || login.person.isProjectOwner)">
                                                <span style="padding-left: 3px;">Edit</span>
                                            </a>
                                        </li>
                                        <li ng-click="bomVm.deleteBomItem(bomItem)"
                                            ng-if="(bomItem.showValues == true)">
                                            <a title="Remove BOQ Item"
                                               type="button"
                                               class="dropdown-item"
                                               ng-disabled="selectedProject.locked == true || !(hasPermission('permission.bom.edit') || login.person.isProjectOwner)">
                                                <span style="padding-left: 3px;">Remove</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                    </td>


                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer" dnd-list="bomVm.bomGroups" style="background-color: #E4E7EA; border: 1px solid #CCC;
                    padding-top: 2px;padding-left: 10px;padding-bottom:2px;display: flex;overflow-x: auto;overflow-y: auto;">
            <a class="btn btn-sm mr5"
               ng-class="{'btn-lightblue': group.active == true, 'btn-white': group.active == false}"
               ng-repeat="group in bomVm.bomGroups"
               dnd-draggable="group"
               dnd-effect-allowed="move"
               ng-click="bomVm.showBomGroup(group)"
               ng-disabled="selectedProject.locked == true || !(hasPermission('permission.bom.edit') || login.person.isProjectOwner)">
            <span class="mr10" ng-if="group.editName == false"
                  ng-dblclick="bomVm.editGroupName(group)"
                  ng-click="bomVm.showBomGroup(group)">{{group.name}}</span>
                <input id="editName{{group.id}}"
                       type="text"
                       style="height: 25px;width: 100px;"
                       ng-if="(group.editName == true)"
                       ng-model="group.name"
                       ng-enter="group.editName = false;bomVm.updateBomGroup(group)">
            <span title="Remove BOQ Group" ng-click="bomVm.deleteBomGroup(group)"
                  ng-if="hasPermission('permission.bom.edit') || login.person.isProjectOwner"
                    ><i class="fa fa-times"></i></span>
            </a>

            <a title="New BOQ Group" class="btn btn-sm btn-success"
               ng-click="bomVm.addBomGroup()"
               ng-if="(selectedProject.locked == false) && (hasPermission('permission.bom.create') || login.person.isProjectOwner)">
                <i class="fa fa-plus"></i>
            </a>
        </div>
    </div>
</div>
