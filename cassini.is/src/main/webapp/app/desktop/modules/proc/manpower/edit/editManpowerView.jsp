<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default min-width" ng-click="manpowerEditVm.back()">Back</button>
            <button class="btn btn-sm btn-success min-width" ng-click="manpowerEditVm.saveManpower()">Save</button>
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
                            <span>ManPower Type: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <span>{{manpowerEditVm.manpower.itemType.name}}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>ManPower Number: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <span>{{manpowerEditVm.manpower.itemNumber}}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>ManPower Name: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <span>{{manpowerEditVm.manpower.person.fullName}}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span> PhoneNo: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <a href="" editable-text="manpowerEditVm.manpower.person.phoneMobile"
                               onaftersave="manpowerEditVm.update()">
                                {{manpowerEditVm.manpower.person.phoneMobile || 'Click to enter email'}}</a>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span> Email: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <a href="" editable-text="manpowerEditVm.manpower.person.email"
                               onaftersave="manpowerEditVm.update()">
                                {{manpowerEditVm.manpower.person.email || 'Click to enter email'}}</a>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Description: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <a href="" editable-text="manpowerEditVm.manpower.description"
                               onaftersave="manpowerEditVm.update()">
                                {{manpowerEditVm.manpower.description || 'Click to enter description'}}</a>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Units: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <a href="" editable-text="manpowerEditVm.manpower.units"
                               onaftersave="manpowerEditVm.update()">
                                {{manpowerEditVm.manpower.units}}</a>
                        </div>
                    </div>
                </div>
            </div>


            <br>
            <h4 class="section-title" style="margin-left:170px;">Attributes</h4>
            <br>

            <div>
                <form class="form-horizontal" ng-if="manpowerEditVm.item.itemType != null">
                    <div class="form-group" ng-repeat="attribute in manpowerEditVm.attributes">
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
