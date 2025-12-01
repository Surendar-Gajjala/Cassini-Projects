<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MANUFACTURER_TYPE</span>
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
                                            <manufacturer-tree
                                                    on-select-type="newMfrVm.onSelectType"></manufacturer-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title" style=""
                                       ng-model="newMfrVm.newManufacture.mfrType.name" readonly>

                            </div>
                        </div>
                    </div>
                    <div class=form-group>
                        <label class="col-sm-4 control-label">
                            <span translate>MANUFACTURER_NAME</span>
                            <span class="asterisk">*</span>:
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_MANUFACTURER_NAME' | translate }}"
                                   ng-model="newMfrVm.newManufacture.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>:
                        </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="6" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate }}"
                                      ng-model="newMfrVm.newManufacture.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PHONE_NUMBER</span>
                            <%--<span class="asterisk">*</span>:--%> </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_PHONE_NUMBER' | translate }}"
                                   ng-model="newMfrVm.newManufacture.phoneNumber">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CONTACT_PERSON</span>
                            <%-- <span class="asterisk">*</span>--%>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_CONTACT_PERSON' | translate }}"
                                   ng-model="newMfrVm.newManufacture.contactPerson">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMfrVm.newManufacture.workflowDefinition" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflow}}">{{$select.selected.name}} [ Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices repeat="workflow in newMfrVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newMfrVm.reqObjectAttributes.length > 0"
                                     attributes="newMfrVm.reqObjectAttributes"></attributes-view>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newMfrVm.objectAttributes.length > 0"
                                     attributes="newMfrVm.objectAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newMfrVm.attributes.length > 0"
                                     attributes="newMfrVm.attributes"></attributes-view>
                </form>
            </div>
            <br><br>
        </div>
    </div>
</div>
