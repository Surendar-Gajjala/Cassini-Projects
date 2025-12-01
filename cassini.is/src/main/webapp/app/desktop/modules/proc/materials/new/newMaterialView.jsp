<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Material Type :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        Select <span class="caret" style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu" style="width: 280px;">
                                        <div style="padding: 5px; border: 1px solid #FFF;
                                        height: auto; max-height:200px; overflow-x: auto">
                                            <material-tree
                                                    on-select-type="newMaterialVm.onSelectType"></material-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMaterialVm.newMaterial.itemType.name"
                                       readonly>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">Material Number : <span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">

                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newMaterialVm.autoNumber()">Auto
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMaterialVm.newMaterial.itemNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Material Name :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newMaterialVm.newMaterial.itemName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newMaterialVm.newMaterial.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Units :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newMaterialVm.newMaterial.units">
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newMaterialVm.newMaterialAttributes"></attributes-view>
                </form>

                <br>
                <h4 ng-if="newMaterialVm.requiredAttributes.length > 0 || newMaterialVm.attributes.length > 0"
                    class="section-title" style="color: black;">Attributes</h4>
                <br>

                <div>
                    <form class="form-horizontal" ng-if="newMaterialVm.newMaterial.itemType != null">
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newMaterialVm.requiredAttributes"></attributes-view>
                        <br>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newMaterialVm.attributes"></attributes-view>
                    </form>
                </div>
                <br><br><br>
            </div>
        </div>
    </div>
</div>
