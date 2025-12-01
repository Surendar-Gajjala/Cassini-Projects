<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>RESOURCE_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="Select" translate>SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">

                                            <manufacturing-resource-type-tree resource-type="true"
                                                                              on-select-type="newAssetVm.onSelectType"></manufacturing-resource-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newAssetVm.type.name" readonly>

                            </div>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>RESOURCE</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select theme="bootstrap" ng-model="newAssetVm.resourceName"
                                       on-select="newAssetVm.selectResource($item)"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectResource}}">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="resource.name as resource in newAssetVm.resources | filter: $select.search">
                                    <div>{{resource.number}} - {{resource.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSET_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="selectAssert" translate>SELECT</span> <span class="caret"
                                                                                              style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">

                                            <manufacturing-type-tree
                                                    on-select-type="newAssetVm.onSelectAssetType" type="MESOBJECTTYPE"
                                                    object-type="ASSETTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newAssetVm.assetType.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newAssetVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newAssetVm.newAsset.number">
                            </div>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newAssetVm.newAsset.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newAssetVm.newAsset.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>METERED</span>
                            :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isMetered" switch="none" checked=""
                                   ng-model="newAssetVm.newAsset.metered" ng-change="newAssetVm.getMeters()">
                            <label for="isMetered" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>


                    <div class="form-group" ng-if="newAssetVm.newAsset.metered">
                        <label class="col-sm-4 control-label">
                            <span translate>METERS</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select multiple ng-model="newAssetVm.meterIds" theme="bootstrap"
                                       close-on-select="false" title="Choose a Meter" remove-selected="true">
                                <ui-select-match placeholder="{{selectMeter}}">{{$item.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="meter.id as meter in newAssetVm.meters track by $index">
                                    <div ng-bind="meter.name"></div>
                                </ui-select-choices>
                            </ui-select>

                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues" ng-if="newAssetVm.attributes.length > 0"
                                     attributes="newAssetVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
