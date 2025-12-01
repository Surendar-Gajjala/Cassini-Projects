<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-5 control-label">
                            <span>Attribute Group</span> : </label>

                        <div class="col-sm-7" style="margin-top: 10px;">
                            <span>{{editAttributeGroupVm.attribute.attributeGroup}}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-5 control-label">
                            <span>New Attribute Group</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control" type="text" placeholder="Enter new group name"
                                   ng-model="editAttributeGroupVm.newGroupName"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
