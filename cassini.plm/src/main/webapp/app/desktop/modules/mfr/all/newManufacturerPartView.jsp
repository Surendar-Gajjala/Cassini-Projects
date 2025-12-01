<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MANUFACTURER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newManufacturerPartVm.newManufacturepart.manufacturer"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectManufacturer}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="manufacturer in newManufacturerPartVm.manufacturers | filter: $select.search">
                                    <div ng-bind="manufacturer.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MANUFACTURER_PART_TYPE</span>
                            <span class="asterisk">*</span>:
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span translate id="Select">SELECT</span>
                                        <span class="caret" style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <manufacturer-part-tree
                                                    on-select-type="newManufacturerPartVm.onSelectType"></manufacturer-part-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newManufacturerPartVm.newManufacturepart.mfrPartType.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PART_NUMBER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="type"
                                   placeholder="{{'ENTER_PART_NUMBER' | translate }}"
                                   ng-model="newManufacturerPartVm.newManufacturepart.partNumber">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PART_NAME</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="type"
                                   placeholder="{{'ENTER_PART_NAME' | translate }}"
                                   ng-model="newManufacturerPartVm.newManufacturepart.partName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                        <textarea name="description" rows="3" class="form-control" style="resize: none"
                                  placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate }}"
                                  ng-model="newManufacturerPartVm.newManufacturepart.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SERIALIZED</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="serialized" switch="none" checked=""
                                   ng-model="newManufacturerPartVm.newManufacturepart.serialized">
                            <label for="serialized" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>THUMBNAIL</span> :</label>

                        <div class="col-sm-7">
                            <input type="file" id="fileId" class="form-control" accept="image/*"
                                   ng-file-model="newManufacturerPartVm.newManufacturepart.imageFile">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newManufacturerPartVm.newManufacturepart.workflowDefinition"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.name}} [ Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow in newManufacturerPartVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newManufacturerPartVm.reqObjectAttributes.length > 0"
                                     attributes="newManufacturerPartVm.reqObjectAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newManufacturerPartVm.objectAttributes.length > 0"
                                     attributes="newManufacturerPartVm.objectAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newManufacturerPartVm.attributes.length > 0"
                                     attributes="newManufacturerPartVm.attributes"></attributes-view>
                </form>
            </div>
            <br><br>
        </div>
    </div>
</div>

