<div class="row" style="padding: 20px;">
    <div class="col-sm-10">
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span translate>ITEM_ALL_ITEM_TYPE</span> <span> : </span>
                </label>

                <div class="col-sm-8">
                    <div class="input-group mb15">
                        <div class="input-group-btn" uib-dropdown>
                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                            </button>
                            <div class="dropdown-menu" role="menu">
                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;width: 230px;">
                                    <classification-tree
                                            on-select-type="itemSearchDialogueVm.onSelectType"></classification-tree>
                                </div>
                            </div>
                        </div>
                        <input type="text" class="form-control" name="title"
                               ng-model="itemSearchDialogueVm.selectedType.name" readonly>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label"><span
                        translate>ITEM_ALL_ITEM_NUMBER </span><span> : </span></label>

                <div class="col-sm-8">
                    <input type="text" class="form-control" name="title"
                           ng-model="itemSearchDialogueVm.filters.itemNumber"
                           ng-enter="itemSearchDialogueVm.search()">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span translate>ITEM_ALL_ITEM_NAME</span> <span> : </span>
                </label>

                <div class="col-sm-8">
                    <input type="text" class="form-control" name="title"
                           ng-model="itemSearchDialogueVm.filters.itemName"
                           ng-enter="itemSearchDialogueVm.search()">
                </div>
            </div>
        </form>
    </div>
    <br>
    <br>
</div>

