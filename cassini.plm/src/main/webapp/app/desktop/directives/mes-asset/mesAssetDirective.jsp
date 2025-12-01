<div class="form-group" style="margin-top: 15px;">
    <label class="col-sm-4 control-label">
        <span translate>ASSET_TYPE</span>
        <span class="asterisk">*</span> : </label>

    <div class="col-sm-7">
        <div class="input-group mb15">
            <div class="input-group-btn" uib-dropdown>
                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                        type="button">
                    <span translate id="selectAssetType">SELECT</span> <span class="caret"
                                                        style="margin-left: 4px;"></span>
                </button>
                <div class="dropdown-menu" role="menu">
                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">

                        <mro-object-type-tree
                                on-select-type="onSelectAssetType"
                                object-type="ASSETTYPE"></mro-object-type-tree>
                    </div>
                </div>
            </div>
            <input type="text" class="form-control" name="title"
                   ng-model="asset.type.name" readonly>

        </div>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-4 control-label">
        <span translate>ASSET_NUMBER</span>
        <span class="asterisk">*</span> :
    </label>

    <div class="col-sm-7">
        <div class="input-group mb15">
            <div class="input-group-btn">
                <button class="btn btn-default" type="button" style="width: 85px"
                        ng-click="generateAssetAutoNumber()" translate>AUTO
                </button>
            </div>
            <input type="text" class="form-control" name="title"
                   ng-model="asset.number" readonly>
        </div>
    </div>
</div>


<div class="form-group">
    <label class="col-sm-4 control-label"><span translate>NAME</span>
        <span class="asterisk">*</span> : </label>

    <div class="col-sm-7">
        <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
               name="title" ng-model="asset.name">
    </div>
</div>
<div class="form-group">
    <label class="col-sm-4 control-label">
        <span translate>DESCRIPTION</span>: </label>

    <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="asset.description"></textarea>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-4 control-label">
        <span translate>METERED</span>
        :</label>

    <div class="col-sm-7">
        <input type="checkbox" id="isMetered" switch="none" checked=""
               ng-model="asset.metered" ng-change="changeMeter()">
        <label for="isMetered" data-on-label="Yes" data-off-label="No"></label>
    </div>

</div>
<div class="form-group" ng-if="asset.metered">
    <label class="col-sm-4 control-label">
        <span translate>METERS</span>
        <span class="asterisk">*</span>: </label>

    <div class="col-sm-7">
        <ui-select multiple ng-model="asset.meters" theme="bootstrap"
                   close-on-select="false" title="{{chooseMeterTitle}}" remove-selected="true">
            <ui-select-match placeholder="{{selectMeterTitle}}">{{$item.name}}
            </ui-select-match>
            <ui-select-choices
                    repeat="meter.id as meter in meters track by $index">
                <div ng-bind="meter.name"></div>
            </ui-select-choices>
        </ui-select>

    </div>
</div>