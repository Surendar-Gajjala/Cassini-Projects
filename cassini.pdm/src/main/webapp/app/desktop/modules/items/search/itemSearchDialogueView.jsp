<div style="padding: 20px;">
    <div>
        <form class="form-horizontal col-sm-10 col-offset-1">
            <div class="form-group">
                <label class="col-sm-3 control-label">Item Type: </label>

                <div class="col-sm-9">
                    <div class="input-group mb15">
                        <input type="text" class="form-control" name="title"
                               ng-model="itemSearchDialogueVm.selectedType.name" readonly>

                        <div class="input-group-btn" uib-dropdown>
                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                Select <span class="caret" style="margin-left: 4px;"></span>
                            </button>
                            <div class="dropdown-menu" role="menu">
                                <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                    <classification-tree
                                            on-select-type="itemSearchDialogueVm.onSelectType"></classification-tree>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Item Number: </label>

                <div class="col-sm-9">
                    <input type="text" class="form-control" name="title"
                           ng-model="itemSearchDialogueVm.filters.itemNumber"
                           ng-enter="itemSearchDialogueVm.search()">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Description: </label>

                <div class="col-sm-9">
                    <input type="text" class="form-control" name="title"
                           ng-model="itemSearchDialogueVm.filters.description"
                           ng-enter="itemSearchDialogueVm.search()">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Revision: </label>

                <div class="col-sm-9">
                    <input type="text" class="form-control" name="title"
                           ng-model="itemSearchDialogueVm.filters.revision"
                           ng-enter="itemSearchDialogueVm.search()">
                </div>
            </div>
        </form>
    </div>
    <br>
    <br>
</div>

