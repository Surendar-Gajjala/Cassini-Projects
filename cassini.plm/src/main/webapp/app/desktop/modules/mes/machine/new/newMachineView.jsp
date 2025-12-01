<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MACHINE_TYPE</span>
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
                                                    on-select-type="newMachineVm.onSelectType"
                                                    object-type="MACHINETYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMachineVm.selectedMachineType.name" readonly>

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
                                            ng-click="newMachineVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMachineVm.newMachine.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKCENTER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMachineVm.newMachine.workCenter" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newMachineVm.selectWorkCenterTitle}}">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workCenter.id as workCenter in newMachineVm.workCenters | filter: $select.search">
                                    <div>{{workCenter.number}} - {{workCenter.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newMachineVm.newMachine.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newMachineVm.newMachine.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>IMAGE_TITLE</span> :</label>

                        <div class="col-sm-7">
                            <input id="imageId" type="file" class="form-control" accept="image/*"
                                   ng-file-model="newMachineVm.newMachine.imageFile">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUIRESMAINTENANCE</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="requiresMaintenance" switch="none" checked=""
                                   ng-model="newMachineVm.newMachine.requiresMaintenance">
                            <label for="requiresMaintenance" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div ng-show="newMachineVm.newMachine.requiresMaintenance">
                        <mes-asset asset="newMachineVm.newAsset"></mes-asset>
                    </div>
                    <mes-mfr-data update-type="'creation'" has-permission="true"
                                  manufacturer-data="newMachineVm.newMachine.manufacturerData"></mes-mfr-data>
                    <attributes-view show-objects="selectObjectValues" ng-if="newMachineVm.attributes.length > 0"
                                     attributes="newMachineVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
