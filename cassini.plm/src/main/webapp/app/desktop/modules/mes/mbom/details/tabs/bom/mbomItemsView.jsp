<div class="mbom-items-view" style="display: flex;width: 100%;">
    <style scoped>
        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        .validate-bom-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .validate-bom-model .validate-bom-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 60%;
            background-color: white;
            border-radius: 7px !important;
        }

        .validate-bom-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .validate-bom-footer {
            border-bottom: 1px solid lightgrey;
            padding: 5px;
            text-align: center;
            height: 50px;
            width: 100%;
            display: flex;
        }

        .bom-header {
            font-weight: bold;
            font-size: 22px;
            /*position: absolute;*/
            display: inline-block;
            /*left: 44%;*/
            margin-top: 7px;
        }

        .bom-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
        }

        .bom-close {
            position: relative;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .bom-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .bom-close:before, .bom-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .bom-close:before {
            transform: rotate(45deg) !important;
        }

        .bom-close:after {
            transform: rotate(-45deg) !important;
        }

        #configuration-error {
            display: none;
            padding: 11px !important;
            margin-bottom: 0 !important;
            width: 100%;
            height: 50px;
            margin-top: -50px;
            float: left;
            position: relative;
        }

        .config-close-btn {
            position: absolute;
            right: 40px;
            top: 7px;
            width: 32px;
            height: 32px;
            opacity: 0.3;
        }

        .config-close-btn:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close-btn:before, .config-close-btn:after {
            position: absolute;
            top: 9px;
            left: 15px;
            content: ' ';
            height: 15px;
            width: 2px;
            background-color: #333;
        }

        .config-close-btn:before {
            transform: rotate(45deg) !important;
        }

        .config-close-btn:after {
            transform: rotate(-45deg) !important;
        }

        .ml9 {
            margin-left: 9px;
        }

        .tab-content .tab-pane {
            overflow: hidden;
        }

        tr.ebom-row {
            background: lightpink; /* fallback for old browsers */
        }

        tr.ebom-row td {
            background: lightpink !important;
        }

        tr.hand-cursor {
            cursor: pointer;
        }

        tr.disable-cursor {
            cursor: not-allowed !important;
        }
    </style>
    <div class="mbom-items-left" style="width: 50%;border-right: 1px solid lightgrey;">
        <div style="height: 25px;"><h4 style="text-align: center;margin: 0">EBOM</h4></div>

        <div class='responsive-table'>
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th style="width:1px !important;white-space: nowrap;text-align: left;" translate>ITEM_NUMBER</th>
                    <th translate>ITEM_TYPE</th>
                    <th translate>ITEM_NAME</th>
                    <th class="col-width-75" style="text-align: center" translate>REVISION</th>
                    <th class="col-width-100" style="text-align: center" translate>MAKE_BUY</th>
                    <th class="col-width-100" style="text-align: center" translate>MFR_PART</th>
                    <th class="col-width-75" style="text-align: center" translate>QUANTITY</th>
                    <th class="col-width-75" style="text-align: center" translate>CONSUMED</th>
                    <th style="width: 20px;"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="mbomItemsVm.loading == true">
                    <td colspan="25"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_ITEMS</span>
                    </td>
                </tr>
                <tr ng-if="mbomItemsVm.loading == false && mbomItemsVm.bomItems.length == 0">
                    <td colspan="25"><span translate>NO_ITEMS</span></td>
                </tr>
                <tr id="{{$index}}" ng-repeat="bomItem in mbomItemsVm.bomItems"
                    ng-class="{'ebom-row':bomItem.selected,'hand-cursor':bomItem.quantity - bomItem.consumedQty > 0,'disable-cursor':bomItem.quantity - bomItem.consumedQty == 0}"
                    dragbomitemtombom bom-items="mbomItemsVm.bomItems" m-bom-items="mbomItemsVm.mBomItems"
                    mbom="mbomRevision" selected-items="mbomItemsVm.selectedItemsToDrag"
                    create-m-bom-item="mbomItemsVm.createMBomItem" drag-name="'bomItem'" drop-name="'bomItem'"
                    ng-click="mbomItemsVm.selectBomItem(bomItem)">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <span class="level{{bomItem.level}}">
                            <i ng-if="bomItem.hasBom && bomItem.editMode == false" class="fa"
                               style="cursor: pointer;margin-right: 0;"
                               ng-class="{'fa-caret-right': (bomItem.expanded == false || bomItem.expanded == null || bomItem.expanded == undefined),
                                          'fa-caret-down': bomItem.expanded == true}"
                               ng-click="mbomItemsVm.toggleItemNode(bomItem)"></i>
                            <span ng-class="{'ml9': bomItem.hasBom == false}">{{bomItem.itemNumber}}</span>
                        </span>
                    </td>
                    <td style="word-wrap: break-word;white-space: normal !important;">{{bomItem.itemTypeName}}</td>
                    <td class="col-width-250">{{bomItem.itemName}}</td>
                    <td class="col-width-75" style="text-align: center">{{bomItem.revision}}</td>
                    <td class="col-width-100" style="text-align: center">
                        <span class="label label-outline bg-light-primary"
                              ng-if="bomItem.makeOrBuy == 'MAKE'">{{bomItem.makeOrBuy}}</span>
                        <span class="label label-outline bg-light-danger"
                              ng-if="bomItem.makeOrBuy == 'BUY'">{{bomItem.makeOrBuy}}</span>
                    </td>
                    <td class="col-width-100" style="text-align: center">
                        <a href=""
                           ui-sref="app.mfr.mfrparts.details({mfrId: bomItem.mfrId, manufacturePartId: bomItem.mfrPartId,tab:'details.basic'})"
                           title="Click to show details">{{bomItem.mfrPartNumber}}</a>
                    </td>
                    <td class="col-width-75" style="text-align: center">{{bomItem.quantity}}</td>
                    <td class="col-width-75" style="text-align: center">{{bomItem.consumedQty}}</td>
                    <td>
                        <i class="fa fa-circle" title="" style="color: red" ng-if="bomItem.consumedQty == 0"></i>
                        <i class="fa fa-warning" title="Partially consumed" style="color: orange"
                           ng-if="bomItem.consumedQty > 0 && bomItem.quantity > bomItem.consumedQty"></i>
                        <i class="fa fa-check" title="Total consumed" ng-if="bomItem.quantity == bomItem.consumedQty"
                           style="color: green"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="mbom-items-right" style="width: 50%;margin-left: 50%;">
        <div style="height: 25px;"><h4 style="text-align: center;margin: 0">MBOM</h4></div>
        <div style="font-style: italic; text-align: center;width: 100%;padding: 5px;" id="mbom-view"
             ng-if="!mbomRevision.released && !mbomRevision.rejected"
             dragbomitemtombom bom-items="mbomItemsVm.bomItems"
             selected-items="mbomItemsVm.selectedItemsToDrag"
             m-bom-items="mbomItemsVm.mBomItems" mbom="mbomRevision"
             create-m-bom-item="mbomItemsVm.createMBomItem" drag-name="'bomItem'" drop-name="'bomItem'">
            {{mbomItemsVm.dragAndDropItems}}
        </div>
        <div class='responsive-table'>
            <table class='table table-striped'>
                <thead>
                <tr id="mbomHeader" dragbomitemtombom bom-items="mbomItemsVm.bomItems"
                    m-bom-items="mbomItemsVm.mBomItems" mbom="mbomRevision"
                    selected-items="mbomItemsVm.selectedItemsToDrag"
                    create-m-bom-item="mbomItemsVm.createMBomItem" drag-name="'bomItem'" drop-name="'bomItem'">
                    <th style="width: 20px;">
                        <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                              ng-if="!mbomRevision.released && !mbomRevision.rejected">
                            <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                               style="padding-left: 3px;"></i>
                            <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                                <li>
                                    <a href="" ng-click="mbomItemsVm.addPhantomItem(null)"
                                       translate>NEW_PHANTOM</a>
                                </li>
                            </ul>
                        </span>
                    </th>
                    <th style="width:1px !important;white-space: nowrap;text-align: left;" translate>ITEM_NUMBER</th>
                    <th translate>ITEM_TYPE</th>
                    <th translate>ITEM_NAME</th>
                    <th class="col-width-75" style="text-align: center" translate>REVISION</th>
                    <th class="col-width-75" style="text-align: center" translate>QUANTITY</th>
                    <th class="col-width-100" style="text-align: center" translate>MAKE_BUY</th>
                    <th class="col-width-100" translate>MFR_PART</th>
                    <th class="col-width-75 sticky-col sticky-actions-col"
                        ng-if="!mbomRevision.released && !mbomRevision.rejected" style="text-align: center" translate>
                        ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="mbomItemsVm.loading == true">
                    <td colspan="25"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_ITEMS</span>
                    </td>
                </tr>
                <tr ng-if="mbomItemsVm.loading == false && mbomItemsVm.mBomItems.length == 0" id="no-items-row"
                    dragbomitemtombom bom-items="mbomItemsVm.bomItems" m-bom-items="mbomItemsVm.mBomItems"
                    selected-items="mbomItemsVm.selectedItemsToDrag"
                    create-m-bom-item="mbomItemsVm.createMBomItem" mbom="mbomRevision">
                    <td colspan="25"><span translate>NO_ITEMS</span></td>
                </tr>
                <tr id="{{$index}}" ng-repeat="mBomItem in mbomItemsVm.mBomItems"
                    dragbomitemtombom bom-items="mbomItemsVm.bomItems" m-bom-items="mbomItemsVm.mBomItems"
                    selected-items="mbomItemsVm.selectedItemsToDrag"
                    create-m-bom-item="mbomItemsVm.createMBomItem" drag-name="'mbomItem'" drop-name="'mbomItem'"
                    mbom="mbomRevision">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                              ng-if="(mBomItem.type == 'PHANTOM' || mBomItem.hasBom) && !mbomItem.editMode && !mbomRevision.released && !mbomRevision.rejected">
                            <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                               style="padding-left: 3px;"></i>
                            <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                                <li>
                                    <a href="" ng-click="mbomItemsVm.addPhantomItem(mBomItem)"
                                       translate>NEW_PHANTOM</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <span class="level{{mBomItem.level}}">
                            <i ng-if="mBomItem.hasBom && mBomItem.editMode == false" class="fa"
                               style="cursor: pointer;margin-right: 0;"
                               ng-class="{'fa-caret-right': (mBomItem.expanded == false || mBomItem.expanded == null || mBomItem.expanded == undefined),
                                      'fa-caret-down': mBomItem.expanded == true}"
                               ng-click="mbomItemsVm.toggleMBOMItemNode(mBomItem)"></i>
                            <span ng-if="mBomItem.type == 'NORMAL'" ng-class="{'ml9': mBomItem.hasBom == false}">{{mBomItem.itemNumber}}</span>
                            <span ng-if="mBomItem.type == 'PHANTOM'" ng-class="{'ml9': mBomItem.hasBom == false}">{{mBomItem.phantomNumber}}</span>
                        </span>
                    </td>
                    <td style="word-wrap: break-word;white-space: normal !important;">
                        <span ng-if="mBomItem.type == 'NORMAL'">{{mBomItem.itemTypeName}}</span>
                        <span ng-if="mBomItem.type == 'PHANTOM'">Phantom</span>
                    </td>
                    <td class="col-width-250">
                        <span ng-if="mBomItem.type == 'NORMAL'">{{mBomItem.itemName}}</span>
                        <span ng-if="mBomItem.type == 'PHANTOM' && !mBomItem.editMode">{{mBomItem.phantomName}}</span>
                        <input type="text" ng-if="mBomItem.editMode && mBomItem.type == 'PHANTOM'"
                               ng-model="mBomItem.phantomName" class="form-control" placeholder="Enter name"/>
                    </td>
                    <td class="col-width-75" style="text-align: center">{{mBomItem.revision}}</td>
                    <td class="col-width-75" style="text-align: center">
                        <span ng-if="mBomItem.type == 'PHANTOM'">{{mBomItem.quantity}}</span>
                        <span ng-if="!mBomItem.editMode && mBomItem.type == 'NORMAL'">{{mBomItem.quantity}}</span>
                        <input ng-pattern="/^[1-9]{1}[0-9]*$/" style="width: 70px;margin: 0;text-align: center"
                               type="number" ng-if="mBomItem.editMode && mBomItem.type == 'NORMAL'"
                               class="form-control input-sm" name="quantity"
                               ng-model="mBomItem.quantity">
                    </td>
                    <td class="col-width-100" style="text-align: center">
                        <span class="label label-outline bg-light-primary"
                              ng-if="mBomItem.makeOrBuy == 'MAKE'">{{mBomItem.makeOrBuy}}</span>
                        <span class="label label-outline bg-light-danger"
                              ng-if="mBomItem.makeOrBuy == 'BUY'">{{mBomItem.makeOrBuy}}</span>
                    </td>
                    <td class="col-width-100">
                        <a href=""
                           ui-sref="app.mfr.mfrparts.details({mfrId: mBomItem.mfrId, manufacturePartId: mBomItem.mfrPartId,tab:'details.basic'})"
                           title="Click to show details">{{mBomItem.mfrPartNumber}}</a>
                        <i class="la la-pencil"
                           ng-if="mBomItem.makeOrBuy == 'BUY' && !mbomRevision.released && !mbomRevision.rejected"
                           style="cursor: pointer;padding-left: 5px;"
                           title="Add Manufacturer part"></i>
                    </td>
                    <td class="col-width-100 actions-col sticky-col sticky-actions-col" style="text-align: center;"
                        ng-if="!mbomRevision.released && !mbomRevision.rejected">
                        <span class="btn-group" ng-if="mBomItem.editMode == true" style="margin: 0">
                            <i title="{{'SAVE' | translate}}" ng-if="mBomItem.isNew" style="cursor: pointer"
                               ng-click="mbomItemsVm.save(mBomItem)"
                               class="la la-check">
                            </i>
                           <i title="{{'SAVE' | translate}}" style="cursor: pointer" ng-if="!mBomItem.isNew"
                              ng-click="mbomItemsVm.updateMBOMItem(mBomItem)"
                              class="la la-check">
                           </i>
                            <i title="{{'CANCEL' | translate}}" style="cursor: pointer"
                               ng-click="mbomItemsVm.cancelChanges(mBomItem)"
                               class="la la-times">
                            </i>
                        </span>
                        <span class="row-menu" uib-dropdown dropdown-append-to-body
                              ng-if="mBomItem.editMode == false" style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li style="cursor: pointer" ng-click="mbomItemsVm.editMBOMItem(mBomItem)">
                                    <a translate>EDIT</a>
                                </li>
                                <li ng-click="mbomItemsVm.deleteMBOMItem(mBomItem)" style="cursor: pointer">
                                    <a translate>REMOVE</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div id="validate-bom" class="validate-bom-model modal">
        <div class="validate-bom-content">
            <div class="validate-bom-header">
                <span class="bom-header">EBOM Validate</span>
                <a href="" ng-click="mbomItemsVm.hideValidateBom()" class="bom-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div id="configuration-error" class="alert {{notificationBackground}} animated">
                <span style="margin-right: 10px;"><i class="fa {{notificationClass}}"></i></span>
                <a href="" class="config-close-btn" ng-click="closeErrorNotification()"></a>
                {{message}}
            </div>
            <div class="bom-content" style="padding: 10px;overflow: auto;">
                <table class='table table-striped highlight-row'>
                    <thead>
                    <tr>
                        <th style="width:1px !important;white-space: nowrap;text-align: left;" translate>ITEM_NUMBER
                        </th>
                        <th class="col-width-200" translate>ITEM_TYPE</th>
                        <th class="col-width-300" translate>ITEM_NAME</th>
                        <th class="col-width-75" style="text-align: center" translate>REVISION</th>
                        <th class="col-width-100" style="text-align: center" translate>MAKE_BUY</th>
                        <th class="col-width-100" style="text-align: center" translate>MFR_PART</th>
                        <th class="col-width-75" style="text-align: center" translate>TOTAL_QUANTITY</th>
                        <th class="col-width-75" style="text-align: center" translate>CONSUMED</th>
                        <td style="width: 20px;"></td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in mbomItemsVm.validatedItems">
                        <td style="width:1px !important;white-space: nowrap;text-align: left;">{{item.itemNumber}}</td>
                        <td class="col-width-200">{{item.itemTypeName}}</td>
                        <td class="col-width-300">{{item.itemName}}</td>
                        <td class="col-width-75" style="text-align: center"><span>{{item.revision}}</span></td>
                        <td class="col-width-100" style="text-align: center">
                        <span class="label label-outline bg-light-primary"
                              ng-if="item.makeOrBuy == 'MAKE'">{{item.makeOrBuy}}</span>
                        <span class="label label-outline bg-light-danger"
                              ng-if="item.makeOrBuy == 'BUY'">{{item.makeOrBuy}}</span>
                        </td>
                        <td class="col-width-100" style="text-align: center">
                            {{item.mfrPartNumber}}
                        </td>
                        <td class="col-width-75" style="text-align: center">
                            <span>{{item.totalQuantity}}</span>
                        </td>
                        <td class="col-width-75" style="text-align: center">
                            {{item.consumedQty}}
                        </td>
                        <td>
                            <i class="fa fa-circle" title="" style="color: red" ng-if="item.consumedQty == 0"></i>
                            <i class="fa fa-warning" title="Partially consumed" style="color: orange"
                               ng-if="item.consumedQty > 0 && item.totalQuantity > item.consumedQty"></i>
                            <i class="fa fa-check" title="Total consumed" ng-if="item.totalQuantity == item.consumedQty"
                               style="color: green"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>