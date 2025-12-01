<style>
    .meterstable td .add-req-btn {
        display: none;
    }

    .meterstable tbody tr td .la-times {
        font-size: 17px;
    }

    .meterstable tbody tr:hover td .add-req-btn {
        display: unset;
    }

    .thumbnail-container {
        border: 1px solid #ddd;
        height: 380px;
        width: 380px;
        position: absolute;
        right: 30px;
        background-color: #fff;
        z-index: 10 !important;
    }

    .medium-image {
        width: 378px;
        max-height: 378px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
    }

    .col-sm-5 .editable-textarea {
        width: 450px;
    }
</style>

<div ng-if="assetBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_ASSETS_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="assetBasicVm.loading == false">

    <div class="thumbnail-container">
        <div>
            <a ng-if="assetBasicVm.asset.imagePath != null && assetBasicVm.asset.imagePath != ''" href=""
               ng-click="assetBasicVm.showResourceImage(assetBasicVm.asset)"
               title="{{assetBasicVm.titleImage}}">
                <img class="medium-image" ng-src="{{assetBasicVm.asset.imagePath}}">
            </a>


            <div ng-if="assetBasicVm.asset.imagePath == null"
                 class="no-thumbnail">

            </div>

            <div id="item-thumbnail-basic{{assetBasicVm.assetId}}" class="item-thumbnail modal">
                <div class="item-thumbnail-content">
                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                        <div class="thumbnail-view" id="thumbnail-view-basic{{assetBasicVm.assetId}}">
                            <div id="thumbnail-image-basic{{assetBasicVm.assetId}}"
                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                <img ng-src="{{assetBasicVm.asset.imagePath}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{assetBasicVm.assetId}}"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assetBasicVm.asset.number}}</span>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>RESOURCE_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assetBasicVm.asset.resourceObject.type.name}}</span>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>RESOURCE</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href="" ng-click="assetBasicVm.showResource(assetBasicVm.asset)">
                <span ng-bind-html="assetBasicVm.asset.resourceObject.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ASSET_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assetBasicVm.asset.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('mroasset','edit')}"
             title="{{hasPermission('mroasset','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('mroasset','edit')}"
               onaftersave="assetBasicVm.updateAsset()"
               editable-text="assetBasicVm.asset.name">
                <span ng-bind-html="assetBasicVm.asset.name" title="{{assetBasicVm.asset.name}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-5 col-sm-5" ng-class="{'cursor-override': !hasPermission('mroasset','edit')}"
             title="{{hasPermission('mroasset','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('mroasset','edit')}"
               onaftersave="assetBasicVm.updateAsset()"
               editable-textarea="assetBasicVm.asset.description">
                <span ng-bind-html="(assetBasicVm.asset.description ) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{assetBasicVm.asset.description}}"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>METERED</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mroasset','edit')}"
             title="{{hasPermission('mroasset','edit') ? '' : noPermission}}">
            <span ng-if="assetBasicVm.asset.metered && !assetBasicVm.editMetered"
                  class="label label-outline bg-light-success" translate>YES</span>
            <span ng-if="!assetBasicVm.asset.metered && !assetBasicVm.editMetered"
                  class="label label-outline bg-light-danger" translate>NO</span>
            <span ng-if="!assetBasicVm.editMetered"
                  title="{{assetBasicVm.asset.meterObjects.length >0 ? cannotDeleteMeterValues : ''}}">
                <a href="" class="fa fa-pencil row-edit-btn"
                   ng-class="{'permission-text-disabled': assetBasicVm.asset.meterObjects.length >0 || !hasPermission('mroasset','edit')}"
                   ng-click="assetBasicVm.changeMeterFlag()" title="{{EDIT | translate}}"></a>
            </span>


            <div style="display: flex;" ng-if="assetBasicVm.editMetered">
                <ui-select ng-model="assetBasicVm.asset.metered" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="meter.value as meter in assetBasicVm.meteredFlags | filter: $select.search">
                        <div>{{meter.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="assetBasicVm.updateAsset()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="assetBasicVm.cancelMeterFlag()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="row" ng-if="assetBasicVm.asset.metered ==true">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>METERS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" style="height: 10em !important;">
            <div class="responsive-table" style="padding: 0;width: 50% !important;">
                <table class="table table-striped highlight-row meterstable">
                    <thead>
                    <tr>
                        <th style="width: 30px;">
                            <i class="la la-plus" title="{{'ADD_METERS' | translate}}"
                               ng-if="assetBasicVm.editMetered ==false"
                               style="cursor: pointer"
                               ng-click="assetBasicVm.addMeters()"></i>
                        </th>

                        <th style="width: 150px" translate>NAME</th>
                        <th style="width: 200px" translate>DESCRIPTION</th>
                        <th style="width: 150px" translate>Last Meter Reading</th>
                        <th style="width: 150px" translate>Last Reading Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="meter in assetBasicVm.asset.meterObjects">
                        <td ng-class="{'cursor-override': !hasPermission('mroasset','edit')}"
                            title="{{hasPermission('mroasset','edit') ? '' : noPermission}}">
                            <i ng-class="{'permission-text-disabled': !hasPermission('mroasset','edit')}"
                               class="la la-times add-req-btn" title="{{'REMOVE_METER' | translate}}"
                               style="cursor: pointer" ng-if="!assetBasicVm.asset.metered"
                               ng-click="assetBasicVm.removeAssetMeter(assetBasicVm.assetId,meter.id)"></i>
                        </td>
                        <td title="{{meter.name}}">
                            <span ng-bind-html="meter.name"></span>
                        </td>
                        <td title="{{meter.description}}">
                            <span ng-bind-html="meter.description"></span>
                        </td>
                        <td>{{meter.lastReadingValue}}</td>
                        <td>{{meter.lastReadingDate}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assetBasicVm.asset.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assetBasicVm.asset.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assetBasicVm.asset.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assetBasicVm.asset.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MROOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{asset.objectType}}"
                                   has-permission="true"
                                   object-type-id="asset.type.id"
                                   object-id="asset.id"></object-attribute-details-view>
</div>
