<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>Resource</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="selectOperationResourcesVm.operationResource.resource"
                                       theme="bootstrap"
                                       style="width:100%"
                                       on-select="selectOperationResourcesVm.operationResource.resourceType = null;selectOperationResourcesVm.selectedOperationType = null">
                                <ui-select-match placeholder="Select">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="source in selectOperationResourcesVm.selectedTypes | filter: $select.search">
                                    <div>{{source}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>Resource Type</span>
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
                                        <div ng-show="selectOperationResourcesVm.operationResource.resource=='MATERIALS'"
                                             style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <material-type-tree
                                                    on-select-type="selectOperationResourcesVm.onSelectType"
                                                    object-type="MATERIALTYPE"></material-type-tree>
                                        </div>

                                        <div ng-show="selectOperationResourcesVm.operationResource.resource=='MANPOWER'"
                                             style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <manpower-type-tree
                                                    on-select-type="selectOperationResourcesVm.onSelectType"
                                                    object-type="MANPOWERTYPE"></manpower-type-tree>
                                        </div>

                                        <div ng-show="selectOperationResourcesVm.operationResource.resource=='TOOLS'"
                                             style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <tools-type-tree
                                                    on-select-type="selectOperationResourcesVm.onSelectType"
                                                    object-type="TOOLSTYPE"></tools-type-tree>
                                        </div>

                                        <div ng-show="selectOperationResourcesVm.operationResource.resource=='MACHINES'"
                                             style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <machines-type-tree
                                                    on-select-type="selectOperationResourcesVm.onSelectType"
                                                    object-type="MACHINESTYPE"></machines-type-tree>
                                        </div>

                                        <div ng-show="selectOperationResourcesVm.operationResource.resource=='INSTRUMENTS'"
                                             style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <instruments-type-tree
                                                    on-select-type="selectOperationResourcesVm.onSelectType"
                                                    object-type="INSTRUMENTSTYPE"></instruments-type-tree>
                                        </div>

                                        <div ng-show="selectOperationResourcesVm.operationResource.resource=='EQUIPMENTS'"
                                             style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <equipments-type-tree
                                                    on-select-type="selectOperationResourcesVm.onSelectType"
                                                    object-type="EQUIPMENTSTYPE"></equipments-type-tree>
                                        </div>
                                        <div ng-show="selectOperationResourcesVm.operationResource.resource=='JIGS_FIXTURES'"
                                             style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <manufacturing-type-tree
                                                    on-select-type="selectOperationResourcesVm.onSelectType"
                                                    object-type="JIFSFIXTURETYPE"></manufacturing-type-tree>
                                        </div>

                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="selectOperationResourcesVm.selectedOperationType.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>Quantity</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="number" class="form-control" placeholder="{{'ENTER_NUMBER' | translate}}"
                                   name="title" ng-model="selectOperationResourcesVm.operationResource.quantity">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="selectOperationResourcesVm.operationResource.description"></textarea>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

