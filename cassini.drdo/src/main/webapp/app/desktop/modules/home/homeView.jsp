<style scoped>
    #td {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
        text-align: left;
    }

    .description {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal !important;
        text-align: left;
    }

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

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .widget {
        padding-right: 25px !important;
        cursor: pointer;
    }

    a.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #121C25;
        font-size: 16px !important;
    }

    .heading {
        margin-bottom: 5px !important;
        margin-top: 8px !important;
        color: black;
        padding: 7px 10px;
        font-size: 20px;
        font-weight: bold;
    }

    .inward-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .inward-notification .badge1 {
        position: absolute;
        top: -10px;
        right: -13px;
        padding: 0px 6px;
        border-radius: 50%;
        background-color: #34398b;
        color: white;
        font-size: 14px;
    }

    .gatePass-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .gatePass-notification .badge1 {
        position: absolute;
        top: -10px;
        right: -13px;
        padding: 0px 6px;
        border-radius: 50%;
        background-color: #22a3e6;
        color: black;
        font-size: 14px;
    }

    .request-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .request-notification .badge1 {
        position: absolute;
        top: -10px;
        right: -13px;
        padding: 0px 6px;
        border-radius: 50%;
        background-color: orange;
        color: white;
        font-size: 14px;
    }

    .issue-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .issue-notification .badge1 {
        position: absolute;
        top: -10px;
        right: -13px;
        padding: 0px 6px;
        border-radius: 50%;
        background-color: green;
        color: white;
        font-size: 14px;
    }

    .return-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .return-notification .badge1 {
        position: absolute;
        top: -10px;
        right: -13px;
        padding: 0px 6px;
        border-radius: 50%;
        background-color: red;
        color: white;
        font-size: 14px;
    }

    .failure-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .failure-notification .badge1 {
        position: absolute;
        top: -10px;
        right: -13px;
        padding: 0px 6px;
        border-radius: 50%;
        background-color: darkred;
        color: white;
        font-size: 14px;
    }

    .new-gatePass {
        font-weight: 600;
    }

    .notification.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        /*  z-index: 1; /!* Sit on top *!/*/
        padding-top: 50px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    /* Modal Content (image) */
    .notification .modal-content {
        margin: auto;
        display: block;
        height: auto;
        width: 50%;
        overflow-y: auto;
        /*max-width: 70%;*/
    }

    .sections-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 10; /* Sit on top */
        left: 0;
        top: 50px;
        overflow: hidden; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .sections-model .modal-content {
        margin-left: auto;
        display: block;
        border-radius: 0px;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar" style="padding-top: 0px;padding-left: 0px;">
        <div style="width: 100%;display: inline-flex;">
            <div style="padding-right:0px;border-right: 1px solid grey;margin-top: 1px;width: 11%;">
                <p class="heading" style="padding: 0 10px;">Notifications</p>
            </div>
            <div style="margin-top: 12px;display: inline-flex;padding-left: 10px;padding-right: 0px;width: 80%;">
                <div class="widget"
                     ng-if="hasPermission('permission.admin.all') || hasPermission('permission.gatePass.view')">
                    <a class="gatePass-notification" ng-class="{'activeView':homeVm.notificationView == 'GatePass'}"
                       ng-click='homeVm.changeView("GatePass")'>
                        <span class="badge1" ng-if="homeVm.notificationView != 'GatePass'">{{homeVm.notifications.gatePasses}}</span>
                        Gate Passes</a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.view')">
                    <a class="inward-notification" ng-class="{'activeView':homeVm.notificationView == 'Inward'}"
                       ng-click='homeVm.changeView("Inward")'>
                        <span ng-if="homeVm.notificationView != 'Inward'"
                              class="badge1">{{homeVm.notifications.inwards}}</span>
                        Inwards
                    </a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.SSQAGApprove') || hasPermission('permission.inward.storeApprove')">
                    <a class="inward-notification" ng-class="{'activeView':homeVm.notificationView == 'InwardItems'}"
                       ng-click='homeVm.changeView("InwardItems")'>
                        <span ng-if="homeVm.notificationView != 'InwardItems'"
                              class="badge1">{{homeVm.notifications.inwardItems}}</span>
                        Inward Items
                    </a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.storeApprove') || hasPermission('permission.requests.casApprove')
                            || hasPermission('permission.requests.bdlApprove') || hasPermission('permission.requests.new')">
                    <a class="request-notification" ng-class="{'activeView':homeVm.notificationView == 'Request'}"
                       ng-click='homeVm.changeView("Request")'>
                        <span ng-if="homeVm.notificationView != 'Request'"
                              class="badge1">{{homeVm.notifications.requests}}</span>
                        Requests</a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.storeApprove') || hasPermission('permission.requests.casApprove') || hasPermission('permission.requests.new') || hasPermission('permission.bdlPcc.receive')">
                    <a class="issue-notification" ng-class="{'activeView':homeVm.notificationView == 'Issue'}"
                       ng-click='homeVm.changeView("Issue")'>
                        <span ng-if="homeVm.notificationView != 'Issue'"
                              class="badge1">{{homeVm.notifications.issues}}</span>
                        Issues</a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.return.view')">
                    <a class="return-notification" ng-class="{'activeView':homeVm.notificationView == 'Return'}"
                       ng-click="homeVm.changeView('Return')">
                        <span ng-if="homeVm.notificationView != 'Return'"
                              class="badge1">{{homeVm.notifications.returns}}</span>
                        Rejects</a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.failure.view')">
                    <a class="failure-notification" ng-class="{'activeView':homeVm.notificationView == 'Failure'}"
                       ng-click="homeVm.changeView('Failure')">
                        <span ng-if="homeVm.notificationView != 'Failure'"
                              class="badge1">{{homeVm.notifications.failures}}</span>
                        Failures</a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.failure.view')">
                    <a class="failure-notification"
                       ng-class="{'activeView':homeVm.notificationView == 'FailureProcess'}"
                       ng-click="homeVm.changeView('FailureProcess')">
                        <span ng-if="homeVm.notificationView != 'FailureProcess'"
                              class="badge1">{{homeVm.notifications.failureProcesses}}</span>
                        Failure Processes</a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.dispatch.view')">
                    <a class="request-notification" ng-class="{'activeView':homeVm.notificationView == 'Dispatch'}"
                       ng-click="homeVm.changeView('Dispatch')">
                        <span class="badge1" ng-if="homeVm.notificationView != 'Dispatch'">{{homeVm.notifications.dispatches}}</span>
                        Dispatches</a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.requests.casManager') || hasPermission('permission.inward.storeApprove')">
                    <a class="inward-notification" ng-class="{'activeView':homeVm.notificationView == 'ToExpireItems'}"
                       ng-click="homeVm.changeView('ToExpireItems')">
                        <span class="badge1" ng-if="homeVm.notificationView != 'ToExpireItems'">{{homeVm.notifications.toExpire}}</span>
                        To Expire</a>
                </div>
                <div class="widget"
                     ng-if="hasPermission('permission.requests.casManager') || hasPermission('permission.inward.storeApprove')">
                    <a class="inward-notification" ng-class="{'activeView':homeVm.notificationView == 'ExpiryItems'}"
                       ng-click="homeVm.changeView('ExpiryItems')">
                        <span class="badge1" ng-if="homeVm.notificationView != 'ExpiryItems'">{{homeVm.notifications.expiryItems}}</span>
                        Expired Items</a>
                </div>
            </div>
            <div style="text-align: right;margin-top: 4px;padding-left: 0px;width: 9%;">
                <button class="btn btn-xs" ng-click="homeVm.showNewInward()"
                        ng-show="homeVm.notificationView == 'Inward' && hasPermission('permission.inward.new')">
                    New Inward
                </button>
                <button class="btn btn-xs" ng-click="homeVm.showNewRequest()"
                        ng-show="homeVm.notificationView == 'Request' && hasPermission('permission.requests.new')">
                    New Request
                </button>
                <button class="btn btn-xs" ng-click="homeVm.showNewIssue()"
                        ng-show="homeVm.notificationView == 'Issue' && hasPermission('permission.issued.new')">New Issue
                </button>
                <button class="btn btn-xs" ng-click="homeVm.showNewGatePass()"
                        ng-show="homeVm.notificationView == 'GatePass' && hasPermission('permission.gatePass.new')">New
                    GatePass
                </button>
                <button class="btn btn-xs" ng-click="homeVm.showNewDispatch()"
                        ng-show="homeVm.notificationView == 'Dispatch' && hasPermission('permission.dispatch.new')">
                    New Dispatch
                </button>
            </div>
        </div>
    </div>

    <%----------------------------------------   Gate Pass Notifications ----------------------------------------------%>

    <div class="view-content" ng-show="homeVm.showGatePassView" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="twentyPercent-column">Gate Pass Name</th>
                    <th class="twentyPercent-column">Gate Pass Number</th>
                    <th class="tenPercent-column">Gate Pass Date</th>
                    <th class="twentyPercent-column">Created By</th>
                    <th class="twentyPercent-column">Created Date</th>
                    <th class="tenPercent-column">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading GatePass...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.gatePasses.content.length == 0">
                    <td colspan="25">No GatePass</td>
                </tr>

                <tr ng-repeat="gatePass in homeVm.gatePasses.content"
                    ng-class="{'new-gatePass':gatePass.inwards == 0}">
                    <td class="twentyPercent-column">
                        <a href="" ng-click="homeVm.downloadGatePass(gatePass)"
                           title="Click to download Gate Pass">{{gatePass.gatePass.name}}
                            <span ng-if="gatePass.showNew && gatePass.inwards == 0"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                        </a>
                    </td>
                    <td class="twentyPercent-column">{{gatePass.gatePassNumber}}</td>
                    <td class="tenPercent-column">{{gatePass.gatePassDate}}</td>
                    <td class="twentyPercent-column">{{gatePass.createdByObject.fullName}}</td>
                    <td class="twentyPercent-column">{{gatePass.createdDate}}</td>
                    <td class="tenPercent-column">

                        <button class="btn btn-xs btn-primary" ng-click="homeVm.showGatePassItems(gatePass)">
                            <i class="fa fa-list" title="Click to show Details"></i>
                        </button>
                        <button class="btn btn-xs btn-danger" ng-click="homeVm.deleteGatePass(gatePass)"
                                ng-disabled="!hasPermission('permission.gatePass.delete')">
                            <i class="fa fa-trash" title="Delete"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.gatePasses.totalElements ==0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.gatePasses.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.gatePasses.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.gatePasses.last == true">{{homeVm.gatePasses.totalElements}}</span>


                        <span> of </span>{{homeVm.gatePasses.totalElements}}
                    </h5>
                    <%--<h5>
                        Displaying {{homeVm.gatePasses.numberOfElements}} of {{homeVm.gatePasses.totalElements}}
                    </h5>--%>
                </div>

                <div class="text-right">
                     <span>
                        <span class="mr10">Page {{homeVm.gatePasses.totalElements != 0 ? homeVm.gatePasses.number+1:0}} of {{homeVm.gatePasses.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.gatePasses.first}">
                            <i class="fa fa-arrow-circle-left mr10"></i>
                        </a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.gatePasses.last}">
                            <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <div class="view-content" ng-show="homeVm.showInwardView" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Inward Number</th>
                    <th>BOM</th>
                    <th>Status</th>
                    <th>Gate Pass</th>
                    <th>Supplier</th>
                    <th class="oneFifty-column">Notes</th>
                    <th>Created By</th>
                    <th>Modified Date</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Inwards...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.inwards.content.length == 0">
                    <td colspan="25">No Inwards</td>
                </tr>

                <tr ng-repeat="inward in homeVm.inwards.content"
                    ng-class="{'new-gatePass':!inward.itemsExist}">
                    <td>
                        <a href="" ng-click="homeVm.showInwardDetails(inward)"
                           title="Click to show details">
                            {{inward.number}}
                            <span ng-if="inward.showNew && !inward.itemsExist"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                            <span ng-if="inward.showNewBadge"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                        </a>
                    </td>
                    <td>{{inward.bom.item.itemMaster.itemName}}</td>
                    <td>

                        <span ng-if="inward.status == 'SSQAG'" class="badge badge-success" style="font-size: 13px;">
                            <a href="" ng-click="homeVm.showInwardDetails(inward)" title="Click to show details"
                               style="color: white;text-decoration: none;">
                                <i ng-if="inward.underReview" class="fa fa-eye" style="padding: 0 3px;color: black;"
                                   title="Inward has under review Items"></i>
                                <i ng-if="inward.provisionalAcceptItems" class="fa fa-shield"
                                   title="Inward has Provisionally Accepted Items"
                                   style="padding: 0 3px;color: orangered;"></i>{{inward.status}}
                            </a>
                        </span>
                        <inward-status ng-if="inward.status != 'SSQAG'" inward="inward"></inward-status>
                    </td>
                    <td>
                        <a href="" ng-click="homeVm.downloadGatePass(inward.gatePass)"
                           title="Click to download Gate Pass">{{inward.gatePass.gatePass.name}}
                        </a>
                    </td>
                    <td>{{inward.supplier.supplierName}}</td>
                    <td class="oneFifty-column">{{inward.notes}}</td>
                    <td>{{inward.createdByObject.fullName}}</td>
                    <td>{{inward.modifiedDate}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>
                        Displaying {{homeVm.inwards.numberOfElements}} of {{homeVm.inwards.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.inwards.totalElements ==0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.inwards.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.inwards.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.inwards.last == true">{{homeVm.inwards.totalElements}}</span>


                        <span> of </span>{{homeVm.inwards.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.inwards.totalElements != 0 ? homeVm.inwards.number+1:0}} of {{homeVm.inwards.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.inwards.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.inwards.last}"><i class="fa fa-arrow-circle-right"></i></a>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <%----------------------------------------   INWARD ITEMS Notifications ----------------------------------------------%>

    <div class="view-content" ng-show="homeVm.showInwardItemsView" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Inward Number</th>
                    <th>BOM</th>
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th style="width: 150px;">UPN</th>
                    <th style="width: 150px;">Location</th>
                    <th>Modified Date</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading InwardItems...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.inwardItems.content.length == 0">
                    <td colspan="25">No Inward Items</td>
                </tr>

                <tr ng-repeat="inwardItemInstance in homeVm.inwardItems.content">
                    <td>
                        <a href="" ng-click="homeVm.showInwardDetails(inwardItemInstance.inward)"
                           title="Click to show details">
                            {{inwardItemInstance.inward.number}}
                        </a>
                    </td>
                    <td>{{inwardItemInstance.inward.bom.item.itemMaster.itemName}}</td>
                    <td>
                        {{inwardItemInstance.item.item.itemMaster.itemName}}
                    </td>
                    <td>
                        {{inwardItemInstance.item.item.itemMaster.parentType.name}}
                    </td>
                    <td>
                        <item-instance-status object="inwardItemInstance.item" title="Click to show details"
                                              ng-click="homeVm.showInwardDetails(inwardItemInstance.inward)">
                        </item-instance-status>
                    </td>
                    <td style="width: 150px;">
                        <span style="font-size: 13px;" class="badge badge-success"
                              ng-if="inwardItemInstance.item.upnNumber != null">
                            <a href="" title="Click to history" style="color:white !important;"
                               ng-if="!inwardItemInstance.item.item.itemMaster.itemType.hasLots"
                               ng-click="showUpnHistory(inwardItemInstance.item,'right')">{{inwardItemInstance.item.upnNumber}}
                            </a>
                            <a href="" title="Click to history" style="color:white !important;"
                               ng-if="inwardItemInstance.item.item.itemMaster.itemType.hasLots"
                               ng-click="showLotUpnHistory(inwardItemInstance.item,'INWARD')">{{inwardItemInstance.item.upnNumber}}
                            </a>
                        </span>
                    </td>
                    <td style="width: 150px;">{{inwardItemInstance.item.storage.name}}</td>
                    <td>{{inwardItemInstance.item.modifiedDate}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>
                        Displaying {{homeVm.inwardItems.numberOfElements}} of
                        {{homeVm.inwardItems.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.inwardItems.totalElements ==0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.inwardItems.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.inwardItems.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.inwardItems.last == true">{{homeVm.inwardItems.totalElements}}</span>


                        <span> of </span>{{homeVm.inwardItems.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.inwardItems.totalElements != 0 ? homeVm.inwardItems.number+1:0}} of {{homeVm.inwardItems.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.inwardItems.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.inwardItems.last}"><i class="fa fa-arrow-circle-right"></i></a>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <%----------------------------------------   REQUEST Notifications ----------------------------------------------%>

    <div class="view-content" ng-show="homeVm.showRequestView" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Request Number</th>
                    <th>Instance</th>
                    <th>Status</th>
                    <th>Requested By</th>
                    <th>Requested Date</th>
                    <th>Notes</th>
                    <th style="width: 70px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Requests...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.requests.content.length == 0">
                    <td colspan="25">No Requests</td>
                </tr>

                <tr ng-repeat="request in homeVm.requests.content">
                    <td>
                        <a href="" ng-click="homeVm.showRequestDetails(request)"
                           title="Click to show details">
                            <span ng-bind-html="request.reqNumber | highlightText: freeTextQuery"></span>
                            <span ng-if="request.newRequest"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                        </a>
                    </td>
                    <td>
                        <span ng-bind-html="request.bomInstance.item.instanceName | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <request-status request="request" title="{{request.reason}}"></request-status>
                    </td>
                    <td>
                        <span ng-bind-html="request.requestedBy.fullName | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="request.requestedDate | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{request.notes}}</td>
                    <td style="width: 70px">
                        <button title="Delete Request"
                                class="btn btn-xs btn-danger"
                                ng-click="homeVm.deleteRequest(request)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%-- <h5>
                         Displaying {{homeVm.requests.numberOfElements}} of
                         {{homeVm.requests.totalElements}}
                     </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.requests.totalElements ==0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.requests.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.requests.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.requests.last == true">{{homeVm.requests.totalElements}}</span>


                        <span> of </span>{{homeVm.requests.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.requests.totalElements != 0 ? homeVm.requests.number+1:0}} of {{homeVm.requests.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.requests.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.requests.last}"><i class="fa fa-arrow-circle-right"></i></a>
                    </span>
                </div>
            </div>
        </div>
    </div>


    <%----------------------------------------   ISSUE Notifications ----------------------------------------------%>

    <div class="view-content" ng-show="homeVm.showIssuesView" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Issue Number</th>
                    <th>Instance</th>
                    <th>Status</th>
                    <th>Last Updated By</th>
                    <th>Last Updated</th>
                    <th>Request Number</th>
                    <th>Requested By</th>
                    <th>Notes</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Issues...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.issues.content.length == 0">
                    <td colspan="25">No Issues</td>
                </tr>

                <tr ng-repeat="issue in homeVm.issues.content">
                    <td>
                        <a href="" ng-click="homeVm.showIssueDetails(issue)"
                           title="Click to show details">
                            {{issue.number}}
                        </a>
                    </td>
                    <td>{{issue.bomInstance.item.instanceName}}</td>
                    <td>
                        <span class="badge badge-warning"
                              ng-if="issue.status == 'BDL_QC' || issue.status == 'BDL_PPC' || issue.status == 'VERSITY_QC' || issue.status == 'VERSITY_PPC'"
                              style="font-size: 13px;">
                            <i ng-if="issue.provisionalApprove" class="fa fa-shield"
                               style="padding: 0 3px;color: white;font-size: 14px"
                               title="Issue has Provisionally Approved Items"></i>{{issue.status}}
                        </span>
                        <span class="badge badge-danger"
                              ng-if="issue.status == 'REJECTED' || issue.status == 'PARTIALLY_REJECTED'"
                              style="font-size: 13px;">
                            <i ng-if="issue.provisionalApprove" class="fa fa-shield"
                               style="padding: 0 3px;color: white;font-size: 14px"
                               title="Issue has Provisionally Approved Items"></i>{{issue.status}}
                        </span>
                        <span class="badge badge-primary"
                              ng-if="issue.status == 'PARTIALLY_APPROVED' || issue.status == 'PARTIALLY_RECEIVED'"
                              style="font-size: 13px;">
                            <i ng-if="issue.provisionalApprove" class="fa fa-shield"
                               style="padding: 0 3px;color: white;font-size: 14px"
                               title="Issue has Provisionally Approved Items"></i>{{issue.status}}
                        </span>
                        <span class="badge badge-success"
                              ng-if="issue.status == 'RECEIVED' || issue.status == 'APPROVED'" style="font-size: 13px;">
                            <i ng-if="issue.provisionalApprove" class="fa fa-shield"
                               style="padding: 0 3px;color: white;font-size: 14px"
                               title="Issue has Provisionally Approved Items"></i>{{issue.status}}
                        </span>
                        <span class="badge badge-secondary"
                              ng-if="issue.status == 'ITEM_RESET'" style="font-size: 13px;">
                            {{issue.status}}
                        </span>
                        <%--<issue-status issue="issue"></issue-status>--%>
                    </td>
                    <td>{{issue.modifiedByObject.fullName}}</td>
                    <td>{{issue.modifiedDate}}</td>
                    <td>{{issue.request.reqNumber}}</td>
                    <td>{{issue.request.requestedBy.fullName}}</td>
                    <td>{{issue.notes}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>
                        Displaying {{homeVm.issues.numberOfElements}} of
                        {{homeVm.issues.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.issues.totalElements == 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.issues.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.issues.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.issues.last == true">{{homeVm.issues.totalElements}}</span>


                        <span> of </span>{{homeVm.issues.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.issues.totalElements != 0 ? homeVm.issues.number+1:0}} of {{homeVm.issues.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.issues.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.issues.last}"><i class="fa fa-arrow-circle-right"></i></a>
                    </span>
                </div>
            </div>
        </div>
    </div>


    <%----------------------------------------   RETURN Notifications ----------------------------------------------%>

    <div class="view-content" ng-show="homeVm.showReturnView" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th>UPN</th>
                    <th>Serial Number</th>
                    <th class="threeHundred-column">Location</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Return Items...
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.returnItems.content.length == 0">
                    <td colspan="7">No Return Items</td>
                </tr>
                <tr ng-repeat="returnItem in homeVm.returnItems.content">
                    <td>
                        {{returnItem.item.itemMaster.itemName}}
                        {{returnItem.item.partSpec.specName}}
                    </td>
                    <td>{{returnItem.item.itemMaster.parentType.name}}</td>
                    <td>
                        <item-instance-status object="returnItem"></item-instance-status>
                    </td>
                    <td>
                        <span class="badge badge-danger" style="font-size: 13px;">
                            <a href="" style="color: white !important;" title="Click to history"
                               ng-click="showUpnHistory(returnItem,'right')">{{returnItem.upnNumber}}</a>
                        </span>
                    </td>
                    <td>
                        <span ng-if="!returnItem.item.itemMaster.itemType.hasLots"><%--{{returnItem.manufacturer.mfrCode}} - --%></span>{{returnItem.oemNumber}}
                    </td>
                    <td class="threeHundred-column">{{returnItem.storagePath}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>
                        Displaying {{homeVm.returnItems.numberOfElements}} of
                        {{homeVm.returnItems.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.returnItems.totalElements == 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.returnItems.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.returnItems.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.returnItems.last == true">{{homeVm.returnItems.totalElements}}</span>


                        <span> of </span>{{homeVm.returnItems.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.returnItems.totalElements != 0 ? homeVm.returnItems.number+1:0}} of {{homeVm.returnItems.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.returnItems.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.returnItems.last}"><i class="fa fa-arrow-circle-right"></i></a>
                    </span>
                </div>
            </div>
        </div>
    </div>


    <%----------------------------------------   FAILURE Notifications ----------------------------------------------%>

    <div class="view-content" ng-show="homeVm.showFailureView" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th>UPN</th>
                    <th>Serial Number</th>
                    <th class="threeHundred-column">Location</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Failure Items...
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.failureItems.content.length == 0">
                    <td colspan="7">No Failure Items</td>
                </tr>
                <tr ng-repeat="failureItem in homeVm.failureItems.content">
                    <td>
                        {{failureItem.item.itemMaster.itemName}}
                        {{failureItem.item.partSpec.specName}}
                    </td>
                    <td>{{failureItem.item.itemMaster.parentType.name}}</td>
                    <td>
                        <item-instance-status object="failureItem"></item-instance-status>
                    </td>
                    <td><span class="badge badge-danger" style="font-size: 13px;">
                             <a href="" style="color: white !important;" title="Click to history"
                                ng-click="showUpnHistory(failureItem,'right')">{{failureItem.upnNumber}}</a>
                        </span>
                    </td>
                    <td>
                        <span ng-if="!failureItem.item.itemMaster.itemType.hasLots"><%--{{failureItem.manufacturer.mfrCode}} - --%></span>{{failureItem.oemNumber}}
                    </td>
                    <td class="threeHundred-column">{{failureItem.storagePath}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>
                        Displaying {{homeVm.failureItems.numberOfElements}} of
                        {{homeVm.failureItems.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.failureItems.totalElements == 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.failureItems.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.failureItems.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.failureItems.last == true">{{homeVm.failureItems.totalElements}}</span>


                        <span> of </span>{{homeVm.failureItems.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.failureItems.totalElements != 0 ? homeVm.failureItems.number+1:0}} of {{homeVm.failureItems.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.failureItems.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.failureItems.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <%----------------------------------------   FAILURE PROCESS Notifications ----------------------------------------------%>

    <div class="view-content" ng-show='homeVm.showFailureProcessView' style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th>UPN</th>
                    <th>Serial Number</th>
                    <th class="threeHundred-column">Actions</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Failure Process Items...
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.failureProcessItems.content.length == 0">
                    <td colspan="7">No Failure Items</td>
                </tr>
                <tr ng-repeat="failureItem in homeVm.failureProcessItems.content">
                    <td>
                        {{failureItem.item.itemMaster.itemName}}
                        {{failureItem.item.partSpec.specName}}
                    </td>
                    <td>{{failureItem.item.itemMaster.parentType.name}}</td>
                    <td><span class="badge badge-danger" style="font-size: 13px;">
                             <a href="" style="color: white !important;" title="Click to history"
                                ng-click="showUpnHistory(failureItem,'right')">{{failureItem.upnNumber}}</a>
                        </span>
                    </td>
                    <td>
                        <span ng-if="!failureItem.item.itemMaster.itemType.hasLots"><%--{{failureItem.manufacturer.mfrCode}} - --%></span>{{failureItem.oemNumber}}
                    </td>
                    <td ng-if="!failureItem.item.itemMaster.itemType.hasLots">
                        <button title="Show failure report for Part"
                                class="btn btn-xs" style="background-color: #cf34c3"
                                ng-click="homeVm.showFailureListForPart(failureItem)">
                            <i class="fa fa-list"></i>
                        </button>
                    </td>
                    <td ng-if="failureItem.item.itemMaster.itemType.hasLots">
                            <span>
                                <a title="Click to show Failure Lots" style="font-size: 14px;"
                                   class="badge badge-success"
                                   uib-popover-template="homeVm.failedLotsPopover.templateUrl"
                                   popover-append-to-body="true"
                                   popover-popup-delay="50"
                                   popover-placement="top-right"
                                   popover-trigger="'outsideClick'">{{failureItem.lotInstanceList.length}}
                                </a>
                            </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5 ng-if="homeVm.showFailureProcessView">
                        Displaying
                        {{homeVm.failureProcessItems.numberOfElements}} of
                        {{homeVm.failureProcessItems.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.failureProcessItems.totalElements == 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.failureProcessItems.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.failureProcessItems.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.failureProcessItems.last == true">{{homeVm.failureProcessItems.totalElements}}</span>


                        <span> of </span>{{homeVm.failureProcessItems.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.dispatches.totalElements != 0 ? homeVm.dispatches.number+1:0}} of {{homeVm.dispatches.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.dispatches.first}">
                            <i class="fa fa-arrow-circle-left mr10"></i>
                        </a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.dispatches.last}">
                            <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </span>
                </div>
            </div>
        </div>
    </div>
    <%----------------------------------------   DISPATCH Notifications ----------------------------------------------%>
    <div class="view-content no-padding" style="padding: 10px;overflow-y: auto;"
         ng-show="homeVm.showDispatchView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Dispatch Number</th>
                    <th>BOM</th>
                    <th>Status</th>
                    <th>Gate Pass Number</th>
                    <th>Dispatch Type</th>
                    <th>Dispatch Date</th>
                    <th>Created By</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading dispatches...
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.dispatches.content.length == 0">
                    <td colspan="7">No dispatches</td>
                </tr>
                <tr ng-repeat="dispatch in homeVm.dispatches.content">
                    <td>
                        <a href=""
                           ng-click="homeVm.showDispatchDetails(dispatch)">{{dispatch.number}}</a>
                    </td>
                    <td>{{dispatch.bom.item.itemMaster.itemName}}</td>
                    <td>{{dispatch.gatePassNumber}}</td>
                    <td>
                            <span ng-if="dispatch.status == 'NEW'"
                                  class="badge badge-primary">{{dispatch.status}}</span>
                        <span ng-if="dispatch.status == 'DISPATCHED'"
                              class="badge badge-success">{{dispatch.status}}</span>
                    </td>
                    <td>
                        <span ng-if="dispatch.type == 'REJECTED'"
                              class="badge badge-danger">{{dispatch.type}}</span>
                        <span ng-if="dispatch.type == 'FAILURE'"
                              class="badge badge-warning">{{dispatch.type}}</span>
                         <span ng-if="dispatch.type == 'FABRICATION'"
                               class="badge badge-success">{{dispatch.type}}</span>
                    </td>
                    <td>{{dispatch.dispatchDate}}</td>
                    <td>{{dispatch.createdByObject.fullName}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5 ng-if="homeVm.notificationView == 'Dispatch'">
                        Displaying
                        {{homeVm.dispatches.numberOfElements}} of
                        {{homeVm.dispatches.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.dispatches.totalElements == 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.dispatches.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.dispatches.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.dispatches.last == true">{{homeVm.dispatches.totalElements}}</span>


                        <span> of </span>{{homeVm.dispatches.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.dispatches.totalElements != 0 ? homeVm.dispatches.number+1:0}} of {{homeVm.dispatches.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.dispatches.first}">
                            <i class="fa fa-arrow-circle-left mr10"></i>
                        </a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.dispatches.last}">
                            <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <%----------------------------------------   EXPIRY ITEMS Notifications ----------------------------------------------%>


    <div class="view-content" ng-show="homeVm.showExpiryItemsView" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th style="width: 150px;">UPN</th>
                    <th>Serial Number</th>
                    <th>Storage</th>
                    <th>Expiry Date</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Expiry Items...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.expiredItems.content.length == 0">
                    <td colspan="25">No Expired Items</td>
                </tr>

                <tr ng-repeat="expiredItem in homeVm.expiredItems.content">
                    <td>
                        {{expiredItem.item.itemMaster.itemName}}
                        {{expiredItem.item.partSpec.specName}}
                    </td>
                    <td>{{expiredItem.item.itemMaster.parentType.name}}</td>
                    <td>
                        <item-instance-status object="expiredItem"></item-instance-status>
                    </td>
                    <td><span class="badge badge-danger" style="font-size: 13px;">
                             <a href="" style="color: white !important;" title="Click to history"
                                ng-click="showUpnHistory(expiredItem,'right')">{{expiredItem.upnNumber}}</a>
                        </span>
                    </td>
                    <td>
                        <span ng-if="!expiredItem.item.itemMaster.itemType.hasLots"><%--{{expiredItem.manufacturer.mfrCode}} - --%></span>{{expiredItem.oemNumber}}
                    </td>
                    <td class="threeHundred-column">{{expiredItem.storagePath}}</td>
                    <td>{{expiredItem.expiryDate}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>
                        Displaying {{homeVm.expiredItems.numberOfElements}} of
                        {{homeVm.expiredItems.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.expiredItems.totalElements == 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.expiredItems.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.expiredItems.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.expiredItems.last == true">{{homeVm.expiredItems.totalElements}}</span>


                        <span> of </span>{{homeVm.expiredItems.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.expiredItems.totalElements != 0 ? homeVm.expiredItems.number+1:0}} of {{homeVm.expiredItems.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.expiredItems.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.expiredItems.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </span>
                </div>
            </div>
        </div>
    </div>


    <div class="view-content" ng-show="homeVm.showToExpireItems" style="padding: 0px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th style="width: 150px;">UPN</th>
                    <th>Serial Number</th>
                    <th>Storage</th>
                    <th>Expiry Date</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="homeVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Expiry Items...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="homeVm.loading == false && homeVm.toExpireItems.content.length == 0">
                    <td colspan="25">No Expired Items</td>
                </tr>

                <tr ng-repeat="expiredItem in homeVm.toExpireItems.content">
                    <td>
                        {{expiredItem.item.itemMaster.itemName}}
                        {{expiredItem.item.partSpec.specName}}
                    </td>
                    <td>{{expiredItem.item.itemMaster.parentType.name}}</td>
                    <td>
                        <item-instance-status object="expiredItem"></item-instance-status>
                    </td>
                    <td><span class="badge badge-danger" style="font-size: 13px;">
                             <a href="" style="color: white !important;" title="Click to history"
                                ng-click="showUpnHistory(expiredItem,'right')">{{expiredItem.upnNumber}}</a>
                        </span>
                    </td>
                    <td>
                        <span ng-if="!expiredItem.item.itemMaster.itemType.hasLots"><%--{{expiredItem.manufacturer.mfrCode}} - --%></span>{{expiredItem.oemNumber}}
                    </td>
                    <td class="threeHundred-column">{{expiredItem.storagePath}}</td>
                    <td>{{expiredItem.expiryDate}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>
                        Displaying {{homeVm.toExpireItems.numberOfElements}} of
                        {{homeVm.toExpireItems.totalElements}}
                    </h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="homeVm.toExpireItems.totalElements == 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)}}
                        </span>
                        <span ng-if="homeVm.toExpireItems.totalElements > 0">
                            {{(homeVm.pageable.page*homeVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="homeVm.toExpireItems.last == false">{{((homeVm.pageable.page+1)*homeVm.pageable.size)}}</span>
                        <span ng-if="homeVm.toExpireItems.last == true">{{homeVm.toExpireItems.totalElements}}</span>


                        <span> of </span>{{homeVm.toExpireItems.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{homeVm.expiredItems.totalElements != 0 ? homeVm.expiredItems.number+1:0}} of {{homeVm.expiredItems.totalPages}}</span>
                        <a href="" ng-click="homeVm.previousPage()"
                           ng-class="{'disabled': homeVm.expiredItems.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="homeVm.nextPage()"
                           ng-class="{'disabled': homeVm.expiredItems.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <%--<div id="updatesPanel" class="sections-model modal">
        <div id="updates-content" class="modal-content">
            <div style="height: 50px;width: 100%;text-align: center;padding: 10px;border-bottom: 1px solid lightgrey;">
                <span class="text-center text-primary" style="font-size: 22px;">Updates</span>
                <span class="fa fa-times" ng-click="unreadMessages()" title="Click to Close"
                      style="float: right;cursor: pointer;font-size: 30px;"></span>
            </div>
            <div id="updates" class="updates-content" style="overflow-y: auto;">
                <div style="padding: 10px;">
                    <button class="btn btn-xs btn-danger" ng-click="deleteUpdates()">Clear</button>
                </div>
                <hr style="margin: 0;">
                <div ng-repeat="update in homeVm.drdoUpdates"
                     style="padding: 10px 15px;border-bottom: 1px solid lightgrey;">
                    <h5 style="margin:0;" ng-if="!update.read">{{update.message}}</h5>

                    <p style="margin:0;" ng-if="update.read">{{update.message}}</p>

                    <p style="text-align: right;margin: 0">{{update.date}}</p>
                </div>
            </div>
        </div>
    </div>--%>


    <%--<div id="notificationModal" class="sections-model modal">
        <div id="modal-content" class="modal-content">

            <div style="height: 50px;width: 100%;text-align: center;padding: 10px;border-bottom: 1px solid lightgrey;">
                <span class="text-center text-primary" style="font-size: 22px;">Updates</span>
                <span class="fa fa-times" ng-click="homeVm.deleteUpdates()" title="Click to Close"
                      style="float: right;cursor: pointer;font-size: 30px;"></span>
            </div>

            <div class="updates-content" style="overflow-y: auto;">
                <div ng-repeat="update in homeVm.drdoUpdates"
                     style="padding: 10px 15px;border-bottom: 1px solid lightgrey;">
                    <h5 style="margin:0;">{{update.message}}</h5>

                    <p style="text-align: right;margin: 0">{{update.date}}</p>
                </div>
            </div>
        </div>
    </div>--%>
</div>