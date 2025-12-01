<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Machine Type :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        Select <span class="caret" style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 380px;height: 200px;">
                                            <machine-tree
                                                    on-select-type="newMachineVm.onSelectType"></machine-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMachineVm.newMachine.itemType.name" readonly>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">Machine Number : <span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newMachineVm.autoNumber()">Auto
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMachineVm.newMachine.itemNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Machine Name :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newMachineVm.newMachine.itemName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newMachineVm.newMachine.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Units :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newMachineVm.newMachine.units">
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newMachineVm.newMachineAttributes"></attributes-view>
                </form>

                <br>
                <h4 ng-if="newMachineVm.requiredAttributes.length > 0 || newMachineVm.attributes.length > 0"
                    class="section-title" style="color: black;">ATTRIBUTES
                </h4>
                <br>

                <div>
                    <form class="form-horizontal" ng-if="newMachineVm.newMachine.itemType != null">
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newMachineVm.requiredAttributes"></attributes-view>
                        <br>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newMachineVm.attributes"></attributes-view>
                    </form>
                </div>
                <br><br>
            </div>
        </div>
    </div>
</div>
