<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default min-width" ng-click="machineEditVm.back()">Back</button>
            <button class="btn btn-sm btn-success min-width" ng-click="machineEditVm.saveMachineItem()">Save</button>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto; overflow-x: hidden">
        <div class="row">
            <h4 class="section-title" style="margin-left: 170px;">Basic Info</h4>
            <br>

            <div class="row row-eq-height" style="margin: 0;">
                <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Machine Name: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <a href="" editable-text="machineEditVm.machine.itemName"
                               onaftersave="machineEditVm.update()">
                                {{machineEditVm.machine.itemName}}</a>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Machine Number: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <span>{{machineEditVm.machine.itemNumber}}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Machine Type: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <span>{{machineEditVm.machine.itemType.name}}</span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Description: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <a href="" editable-text="machineEditVm.machine.description"
                               onaftersave="machineEditVm.update()">
                                {{machineEditVm.machine.description || 'Click to enter description'}}</a>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Units: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">

                            <a href="" editable-text="machineEditVm.machine.units"
                               onaftersave="machineEditVm.update()">
                                {{machineEditVm.machine.units}}</a>
                        </div>
                    </div>
                </div>
            </div>


            <br>
            <h4 class="section-title" style="margin-left:170px;">Attributes</h4>
            <br>

            <div>
                <form class="form-horizontal" ng-if="machineEditVm.machine.itemType != null">
                    <div class="form-group" ng-repeat="attribute in machineEditVm.attributes">
                        <label class="col-sm-4 control-label">{{attribute.attributeDef.name}}: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'STRING'"
                                   ng-model="attribute.stringValue">
                            <input type="number" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                                   ng-model="attribute.integerValue">
                            <input type="number" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                                   ng-model="attribute.doubleValue">
                            <input type="checkbox" class="form-control" name="title"
                                   style="width: 25px;margin-top: 12px;"
                                   ng-if="attribute.attributeDef.dataType == 'BOOLEAN'"
                                   ng-model="attribute.booleanValue">
                            <input type="text" class="form-control" name="title" date-picker
                                   ng-if="attribute.attributeDef.dataType == 'DATE'"
                                   ng-model="attribute.dateValue">
                        </div>
                    </div>
                </form>
            </div>
            <br><br>
        </div>
    </div>
</div>

<%--<div class="view-content no-padding" style="overflow-y: auto; overflow-x: hidden">
    <div class="row">
        <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-5 col-md-offset-3">
            <h4 class="section-title">Basic Info</h4>
            <br>

            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 text-right">Machine Name: </label>

                    <div class="col-sm-7">
                        <span>{{machineEditVm.machine.itemName}}</span>
                        &lt;%&ndash;<div class="input-group mb15">
                                <input type="text" class="form-control" name="title" ng-model="itemEditVm.item.itemType.name" disabled>&ndash;%&gt;
                                &lt;%&ndash;<div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button" disabled>
                                        Select <span class="caret" style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <classification-tree on-select-type="itemEditVm.onSelectType"></classification-tree>
                                        </div>
                                    </div>
                                </div>
                            </div>&ndash;%&gt;
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 text-right">Machine Number: </label>

                    <div class="col-sm-7">
                        <span>{{machineEditVm.machine.itemNumber}}</span>
                        &lt;%&ndash;<div class="input-group mb15">
                            <input type="text" class="form-control" name="title" ng-model="itemEditVm.item.itemNumber" disabled>
                            <div class="input-group-btn">
                                <button class="btn btn-default" type="button" style="width: 85px" disabled
                                        ng-click="itemEditVm.autoNumber()">Auto</button>
                            </div>
                        </div>&ndash;%&gt;
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 text-right">Machine Type: </label>

                    <div class="col-sm-7">
                        <span>{{machineEditVm.machine.itemType.name}}</span>
                        </div>
                    </div>


                        <div class="form-group">
                    <label class="col-sm-4 control-label">Description: </label>

                    <div class="col-sm-7">
                        <textarea class="form-control" rows="5" style="resize: none"
                                  ng-model="machineEditVm.machine.description"></textarea>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Units: </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title" ng-model="machineEditVm.machine.units">
                    </div>
                </div>
            </form>

--%>