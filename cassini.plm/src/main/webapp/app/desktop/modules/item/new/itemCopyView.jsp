<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_NUMBER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="itemCopyVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="itemCopyVm.newItem.itemNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_NAME</span><span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input class="form-control" type="text" style="resize: none"
                                   ng-model="itemCopyVm.newItem.itemName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="itemCopyVm.newItem.description"></textarea>
                        </div>
                    </div>

                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>
