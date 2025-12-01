<div>
    <style scoped>
        .ui-select-multiple.ui-select-bootstrap .ui-select-match-item > span {
        }

        .ui-select-multiple.ui-select-bootstrap .ui-select-match .close {
            font-size: 21px !important;
            line-height: 18px !important;
            opacity: 0.8;
            font-weight: 400;
        }
    </style>
    <div ng-show="shareVm.objectType == 'ITEM'">
        <div ng-if="shareVm.selectionType == 'itemSelection'" class='responsive-table' style="padding: 10px;">
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th translate>ITEM_NUMBER</th>
                    <th translate>ITEM_TYPE</th>
                    <th translate>SHARED_TO</th>
                    <th translate>SHARE_TYPE</th>
                    <th translate>PERMISSIONS</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shareVm.itemSharedPersons.length == 0">
                    <td colspan="10"><span translate>NO_SHARED</span></td>
                </tr>
                <tr ng-repeat="share in shareVm.itemSharedPersons">
                    <td>{{share.objectId.itemNumber}}</td>
                    <td>{{share.objectId.itemType.name}}</td>
                    <td>{{share.sharedTo}}</td>
                    <td>{{share.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': share.permission === 'READ','label-success': share.permission === 'WRITE'}">
                            {{share.permission}}
                        </div>
                    </td>
                    <td>
                        <button title="Delete this shared person" class="btn btn-xs btn-danger"
                                ng-click="shareVm.deleteShare(share)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>

        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>ITEM_NUMBER</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.item != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.itemNumber" readonly>

                                    <input ng-if="shareVm.items != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.selectedItems" readonly>

                                </div>
                            </div>
                            <div class="form-group" ng-if="shareVm.item != null">
                                <label class="col-sm-4 control-label">
                                    <span translate>ITEM_NAME</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.itemName" readonly>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>SHARE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.shareType" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPersonType}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="shareType as shareType in shareVm.shareTypes track by $index">
                                            <div ng-bind="shareType | highlight: $select.search"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>USER_GROUP</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'PERSON'">
                                    <%--<ui-select ng-model="shareVm.shareObject.sharedTo" theme="bootstrap"
                                        style="width:100%" ng-show="shareVm.users.length > 0">
                                        <ui-select-match placeholder="{{shareVm.selectUserMsg}}">
                                            {{$select.selected.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user.person.id as user in shareVm.users">
                                            <div ng-bind="user.person.firstName | highlight: $select.search"></div>
                                        </ui-select-choices>
                                        </ui-select>--%>


                                    <ui-select ng-show="shareVm.users.length > 0" multiple
                                               ng-model="shareVm.sharedToObjects" theme="bootstrap" checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareVm.selectUserType}}">
                                            {{$item.person.fullName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareVm.users">
                                            <div ng-bind="user.person.fullName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'GROUP'">
                                    <ui-select ng-model="shareVm.sharedToGroupObject" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectGroupMsg}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="group in shareVm.groups">
                                            <div ng-bind="group.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPermission}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareVm.processing" ng-cloak>
                                <span translate>ITEM_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div ng-show="shareVm.objectType == 'MANUFACTURER'">
        <div ng-if="shareVm.selectionType == 'itemSelection'" class='responsive-table' style="padding: 10px;">
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th translate>MANUFACTURER_NAME</th>
                    <th translate>MANUFACTURER_TYPE</th>
                    <th translate>SHARED_TO</th>
                    <th translate>SHARE_TYPE</th>
                    <th translate>PERMISSIONS</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shareVm.itemSharedPersons.length == 0">
                    <td colspan="10"><span translate>NO_SHARED</span></td>
                </tr>
                <tr ng-repeat="share in shareVm.itemSharedPersons">
                    <td>{{share.objectId.name}}</td>
                    <td>{{share.objectId.mfrType.name}}</td>
                    <td>{{share.sharedTo}}</td>
                    <td>{{share.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': share.permission === 'READ','label-success': share.permission === 'WRITE'}">
                            {{share.permission}}
                        </div>
                    </td>
                    <td>
                        <button title="Delete this shared person" class="btn btn-xs btn-danger"
                                ng-click="shareVm.deleteShare(share)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>

        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>MANUFACTURER_NAME</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.item != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.name" readonly>

                                    <input ng-if="shareVm.items != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.selectedMfrs" readonly>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>SHARE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.shareType" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPersonType}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="shareType as shareType in shareVm.shareTypes track by $index">
                                            <div ng-bind="shareType | highlight: $select.search"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>USER_GROUP</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'PERSON'">
                                    <%--<ui-select ng-model="shareVm.shareObject.sharedTo" theme="bootstrap"
                                        style="width:100%" ng-show="shareVm.users.length > 0">
                                        <ui-select-match placeholder="{{shareVm.selectUserMsg}}">
                                            {{$select.selected.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user.person.id as user in shareVm.users">
                                            <div ng-bind="user.person.firstName | highlight: $select.search"></div>
                                        </ui-select-choices>
                                        </ui-select>--%>


                                    <ui-select ng-show="shareVm.users.length > 0" multiple
                                               ng-model="shareVm.sharedToObjects" theme="bootstrap" checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareVm.selectUserType}}">
                                            {{$item.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareVm.users">
                                            <div ng-bind="user.person.firstName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'GROUP'">
                                    <ui-select ng-model="shareVm.sharedToGroupObject" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectGroupMsg}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="group in shareVm.groups">
                                            <div ng-bind="group.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPermission}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareVm.processing" ng-cloak>
                                <span translate>MFR_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div ng-show="shareVm.objectType == 'MANUFACTURERPART'">
        <div ng-if="shareVm.selectionType == 'itemSelection'" class='responsive-table' style="padding: 10px;">
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th style="width: 200px;" translate>PART_NUMBER</th>
                    <th translate>PART_NAME</th>
                    <th translate>SHARED_TO</th>
                    <th translate>SHARE_TYPE</th>
                    <th translate>PERMISSIONS</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shareVm.itemSharedPersons.length == 0">
                    <td colspan="10"><span translate>NO_SHARED</span></td>
                </tr>
                <tr ng-repeat="share in shareVm.itemSharedPersons">
                    <td>{{share.objectId.partNumber}}</td>
                    <td>{{share.objectId.partName}}</td>
                    <td>{{share.sharedTo}}</td>
                    <td>{{share.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': share.permission === 'READ','label-success': share.permission === 'WRITE'}">
                            {{share.permission}}
                        </div>
                    </td>
                    <td>
                        <button title="Delete this shared person" class="btn btn-xs btn-danger"
                                ng-click="shareVm.deleteShare(share)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>

        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PART_NUMBER</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.item != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.partNumber" readonly>

                                    <input ng-if="shareVm.items != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.selectedMfrParts" readonly>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>SHARE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.shareType" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPersonType}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="shareType as shareType in shareVm.shareTypes track by $index">
                                            <div ng-bind="shareType | highlight: $select.search"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>USER_GROUP</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'PERSON'">

                                    <ui-select ng-show="shareVm.users.length > 0" multiple
                                               ng-model="shareVm.sharedToObjects" theme="bootstrap" checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareVm.selectUserType}}">
                                            {{$item.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareVm.users">
                                            <div ng-bind="user.person.firstName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'GROUP'">
                                    <ui-select ng-model="shareVm.sharedToGroupObject" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectGroupMsg}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="group in shareVm.groups">
                                            <div ng-bind="group.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPermission}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareVm.processing" ng-cloak>
                                <span translate>MFR_PART_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div ng-show="shareVm.objectType == 'MFRSUPPLIER'">
        <div ng-if="shareVm.selectionType == 'itemSelection'" class='responsive-table' style="padding: 10px;">
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th translate>NAME</th>
                    <th translate>SHARED_TO</th>
                    <th translate>SHARE_TYPE</th>
                    <th translate>PERMISSIONS</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shareVm.itemSharedPersons.length == 0">
                    <td colspan="10"><span translate>NO_SHARED</span></td>
                </tr>
                <tr ng-repeat="share in shareVm.itemSharedPersons">
                    <td>{{share.objectId.name}}</td>
                    <td>{{share.sharedTo}}</td>
                    <td>{{share.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': share.permission === 'READ','label-success': share.permission === 'WRITE'}">
                            {{share.permission}}
                        </div>
                    </td>
                    <td>
                        <button title="Delete this shared person" class="btn btn-xs btn-danger"
                                ng-click="shareVm.deleteShare(share)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>

        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>NAME</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.item != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.name" readonly>

                                    <input ng-if="shareVm.items != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.selectedSuppliers" readonly>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>SHARE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.shareType" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPersonType}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="shareType as shareType in shareVm.shareTypes track by $index">
                                            <div ng-bind="shareType | highlight: $select.search"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>USER_GROUP</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'PERSON'">

                                    <ui-select ng-show="shareVm.users.length > 0" multiple
                                               ng-model="shareVm.sharedToObjects" theme="bootstrap" checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareVm.selectUserType}}">
                                            {{$item.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareVm.users">
                                            <div ng-bind="user.person.firstName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'GROUP'">
                                    <ui-select ng-model="shareVm.sharedToGroupObject" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectGroupMsg}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="group in shareVm.groups">
                                            <div ng-bind="group.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPermission}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareVm.processing" ng-cloak>
                                <span translate>SUPPLIER_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div ng-show="shareVm.objectType == 'CUSTOMOBJECT'">
        <div ng-if="shareVm.selectionType == 'itemSelection'" class='responsive-table' style="padding: 10px;">
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th translate>NAME</th>
                    <th translate>SHARED_TO</th>
                    <th translate>SHARE_TYPE</th>
                    <th translate>PERMISSIONS</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shareVm.itemSharedPersons.length == 0">
                    <td colspan="10"><span translate>NO_SHARED</span></td>
                </tr>
                <tr ng-repeat="share in shareVm.itemSharedPersons">
                    <td>{{share.objectId.name}}</td>
                    <td>{{share.sharedTo}}</td>
                    <td>{{share.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': share.permission === 'READ','label-success': share.permission === 'WRITE'}">
                            {{share.permission}}
                        </div>
                    </td>
                    <td>
                        <button title="Delete this shared person" class="btn btn-xs btn-danger"
                                ng-click="shareVm.deleteShare(share)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>

        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>NAME</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.item != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.name" readonly>

                                    <input ng-if="shareVm.items != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.selectedCustomObjects" readonly>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>SHARE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.shareType" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPersonType}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="shareType as shareType in shareVm.shareTypes track by $index">
                                            <div ng-bind="shareType | highlight: $select.search"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>USER_GROUP</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'PERSON'">

                                    <ui-select ng-show="shareVm.users.length > 0" multiple
                                               ng-model="shareVm.sharedToObjects" theme="bootstrap" checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareVm.selectUserType}}">
                                            {{$item.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareVm.users">
                                            <div ng-bind="user.person.firstName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'GROUP'">
                                    <ui-select ng-model="shareVm.sharedToGroupObject" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectGroupMsg}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="group in shareVm.groups">
                                            <div ng-bind="group.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPermission}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareVm.processing" ng-cloak>
                                <span translate>CUSTOM_OBJECT_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div ng-show="shareVm.objectType == 'DOCUMENT'">
        <div ng-if="shareVm.selectionType == 'itemSelection'" class='responsive-table' style="padding: 10px;">
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th translate>NAME</th>
                    <th translate>SHARED_TO</th>
                    <th translate>SHARE_TYPE</th>
                    <th translate>PERMISSIONS</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shareVm.itemSharedPersons.length == 0">
                    <td colspan="10"><span translate>NO_SHARED</span></td>
                </tr>
                <tr ng-repeat="share in shareVm.itemSharedPersons">
                    <td>{{share.objectId.name}}</td>
                    <td>{{share.sharedTo}}</td>
                    <td>{{share.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': share.permission === 'READ','label-success': share.permission === 'WRITE'}">
                            {{share.permission}}
                        </div>
                    </td>
                    <td>
                        <button title="Delete this shared person" class="btn btn-xs btn-danger"
                                ng-click="shareVm.deleteShare(share)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>

        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>NAME</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.item != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.name" readonly>

                                    <input ng-if="shareVm.items != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.selectedSuppliers" readonly>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>SHARE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.shareType" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPersonType}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="shareType as shareType in shareVm.shareTypes track by $index">
                                            <div ng-bind="shareType | highlight: $select.search"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>USER_GROUP</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'PERSON'">

                                    <ui-select ng-show="shareVm.users.length > 0" multiple
                                               ng-model="shareVm.sharedToObjects" theme="bootstrap" checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareVm.selectUserType}}">
                                            {{$item.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareVm.users">
                                            <div ng-bind="user.person.firstName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'GROUP'">
                                    <ui-select ng-model="shareVm.sharedToGroupObject" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectGroupMsg}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="group in shareVm.groups">
                                            <div ng-bind="group.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPermission}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareVm.processing" ng-cloak>
                                <span translate>FILES_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div ng-show="shareVm.objectType == 'PROJECT' || shareVm.objectType == 'PROJECTACTIVITY' || shareVm.objectType == 'PROJECTTASK'">
        <div ng-if="shareVm.selectionType == 'itemSelection'" class='responsive-table' style="padding: 10px;">
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th translate>Project Name</th>
                    <th translate>SHARED_TO</th>
                    <th translate>SHARE_TYPE</th>
                    <th translate>PERMISSIONS</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shareVm.itemSharedPersons.length == 0">
                    <td colspan="10"><span translate>NO_SHARED</span></td>
                </tr>
                <tr ng-repeat="share in shareVm.itemSharedPersons">
                    <td>{{share.objectId.name}}</td>
                    <td>{{share.sharedTo}}</td>
                    <td>{{share.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': share.permission === 'READ','label-success': share.permission === 'WRITE'}">
                            {{share.permission}}
                        </div>
                    </td>
                    <td>
                        <button title="Delete this shared person" class="btn btn-xs btn-danger"
                                ng-click="shareVm.deleteShare(share)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>

        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group" ng-if="shareVm.selectionType == 'projectMultipleSelection'">
                                <label class="col-sm-4 control-label">
                                    <span translate>PROJECT_NUMBER</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.items != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.selectedItems" readonly>
                                </div>
                            </div>
                            <div class="form-group" ng-if="shareVm.selectionType != 'projectMultipleSelection'">
                                <label class="col-sm-4 control-label">
                                    <span translate>PROJECT_NAME</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.item != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.name" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>SHARE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.shareType" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPersonType}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="shareType as shareType in shareVm.shareTypes track by $index">
                                            <div ng-bind="shareType | highlight: $select.search"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>USER_GROUP</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'PERSON'">

                                    <ui-select ng-show="shareVm.users.length > 0" multiple
                                               ng-model="shareVm.sharedToObjects" theme="bootstrap" checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareVm.selectUserType}}">
                                            {{$item.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareVm.users">
                                            <div ng-bind="user.person.firstName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'GROUP'">
                                    <ui-select ng-model="shareVm.sharedToGroupObject" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectGroupMsg}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="group in shareVm.groups">
                                            <div ng-bind="group.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPermission}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareVm.processing" ng-cloak>
                                <span translate>PROJECT_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div ng-show="shareVm.objectType == 'PROGRAM' ">
        <div ng-if="shareVm.selectionType == 'itemSelection'" class='responsive-table' style="padding: 10px;">
            <table class='table table-striped'>
                <thead>
                <tr>
                    <th translate>Program Name</th>
                    <th translate>SHARED_TO</th>
                    <th translate>SHARE_TYPE</th>
                    <th translate>PERMISSIONS</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shareVm.itemSharedPersons.length == 0">
                    <td colspan="10"><span translate>NO_SHARED</span></td>
                </tr>
                <tr ng-repeat="share in shareVm.itemSharedPersons">
                    <td>{{share.objectId.name}}</td>
                    <td>{{share.sharedTo}}</td>
                    <td>{{share.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': share.permission === 'READ','label-success': share.permission === 'WRITE'}">
                            {{share.permission}}
                        </div>
                    </td>
                    <td>
                        <button title="Delete this shared person" class="btn btn-xs btn-danger"
                                ng-click="shareVm.deleteShare(share)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>

        <div style="position: relative;">
            <div style="padding: 20px;">
                <div class="row" style="margin: 0;">
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group" ng-if="shareVm.selectionType == 'programMultipleSelection'">
                                <label class="col-sm-4 control-label">
                                    <span translate>PROGRAM_NUMBER</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.items != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.selectedItems" readonly>
                                </div>
                            </div>
                            <div class="form-group" ng-if="shareVm.selectionType != 'programMultipleSelection'">
                                <label class="col-sm-4 control-label">
                                    <span translate>PROGRAM_NAME</span>
                                    <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <input ng-if="shareVm.item != null" type="text" class="form-control" name="title"
                                           ng-model="shareVm.item.name" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>SHARE_TYPE</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.shareType" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPersonType}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="shareType as shareType in shareVm.shareTypes track by $index">
                                            <div ng-bind="shareType | highlight: $select.search"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>USER_GROUP</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'PERSON'">

                                    <ui-select ng-show="shareVm.users.length > 0" multiple
                                               ng-model="shareVm.sharedToObjects" theme="bootstrap" checkboxes="true"
                                               close-on-select="false" title="" remove-selected="true">
                                        <ui-select-match placeholder="{{shareVm.selectUserType}}">
                                            {{$item.person.firstName}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="user in shareVm.users">
                                            <div ng-bind="user.person.firstName"></div>
                                        </ui-select-choices>
                                    </ui-select>

                                    <div ng-show="shareVm.users.length == 0">
                                        <div class="col-sm-9" style="margin-left: -11px;width: 107%;">
                                            <input type="text" class="form-control" name="title"
                                                   placeholder="{{shareVm.noExternalUsersMsg}}" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-7" ng-if="shareVm.shareObject.shareType == 'GROUP'">
                                    <ui-select ng-model="shareVm.sharedToGroupObject" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectGroupMsg}}">
                                            {{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices repeat="group in shareVm.groups">
                                            <div ng-bind="group.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <span translate>PERMISSIONS</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="shareVm.shareObject.permission" theme="bootstrap"
                                               style="width:100%">
                                        <ui-select-match placeholder="{{shareVm.selectPermission}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices repeat=" permission in shareVm.permissions">
                                            <div ng-bind="permission"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                            <br>

                            <div ng-show="shareVm.processing" ng-cloak>
                                <span translate>PROGRAM_SHARE_MESSAGE</span>

                                <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                                <br>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


</div>