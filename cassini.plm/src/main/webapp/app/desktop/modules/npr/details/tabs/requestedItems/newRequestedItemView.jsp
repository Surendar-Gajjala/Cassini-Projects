<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

        .open > .dropdown-toggle.btn {
            color: #555552 !important;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="" translate>BASIC_INFO</h4>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <classification-tree
                                                    on-select-type="newRequestedItemVm.onSelectType"></classification-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newRequestedItemVm.newNprItem.itemType.name" readonly>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_ITEMNAME' | translate}}"
                                   name="title" ng-model="newRequestedItemVm.newNprItem.itemName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" placeholder="{{'ENTER_ITEMDESCRIPTION' | translate}}"
                                      rows="3" style="resize: none"
                                      ng-model="newRequestedItemVm.newNprItem.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>UNITS</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newRequestedItemVm.newNprItem.units">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" placeholder="{{'ENTER_NOTES' | translate}}"
                                      rows="3" style="resize: none"
                                      ng-model="newRequestedItemVm.newNprItem.notes"></textarea>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newRequestedItemVm.newItemAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newRequestedItemVm.newItemRevisionAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newRequestedItemVm.newNprItem.itemType != null && newRequestedItemVm.attributes.length > 0"
                                     attributes="newRequestedItemVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
