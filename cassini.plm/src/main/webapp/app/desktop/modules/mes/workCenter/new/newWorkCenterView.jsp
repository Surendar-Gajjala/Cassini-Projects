<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANT</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkCenterVm.workCenter.plant" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newWorkCenterVm.selectPlantTitle}}">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="plant.id as plant in newWorkCenterVm.plants | filter: $select.search">
                                    <div>{{plant.number}} - {{plant.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSEMBLY_LINE</span></label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkCenterVm.workCenter.assemblyLine" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newWorkCenterVm.selectAssemblyLineTitle}}">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="assemblyLine.id as assemblyLine in newWorkCenterVm.assemblyLines | filter: $select.search">
                                    <div>{{assemblyLine.number}} - {{assemblyLine.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKCENTER_TYPE_TITLE</span>
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
                                            <manufacturing-type-tree
                                                    on-select-type="newWorkCenterVm.onSelectType"
                                                    object-type="WORKCENTERTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newWorkCenterVm.workCenter.type.name" readonly>

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
                                            ng-click="newWorkCenterVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newWorkCenterVm.workCenter.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name"
                                   placeholder="{{'ENTER_NAME' | translate}}"
                                   ng-model="newWorkCenterVm.workCenter.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newWorkCenterVm.workCenter.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>LOCATION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_LOCATION' | translate}}"
                                      ng-model="newWorkCenterVm.workCenter.location"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUIRESMAINTENANCE</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="requiresMaintenance" switch="none" checked=""
                                   ng-model="newWorkCenterVm.workCenter.requiresMaintenance">
                            <label for="requiresMaintenance" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div ng-show="newWorkCenterVm.workCenter.requiresMaintenance">
                        <mes-asset asset="newWorkCenterVm.newAsset"></mes-asset>
                    </div>
                    <attributes-view show-objects="selectObjectValues" ng-if="newWorkCenterVm.attributes.length > 0"
                                     attributes="newWorkCenterVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
