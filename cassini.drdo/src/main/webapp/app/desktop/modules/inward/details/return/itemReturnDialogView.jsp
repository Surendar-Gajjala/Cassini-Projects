<div style="padding: 10px;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Reason <span class="asterisk">*</span> </span>: </label>

                        <div class="col-sm-7" ng-show="itemReturnDialogVm.mode != 'P_ACCEPTALL'">
                             <textarea class="form-control" rows="5" style="resize: none"
                                       ng-model="itemReturnDialogVm.inwardItemInstance.item.reason">
                             </textarea>
                        </div>

                        <div class="col-sm-7" ng-show="itemReturnDialogVm.mode == 'P_ACCEPTALL'">
                             <textarea class="form-control" rows="5" style="resize: none"
                                       ng-model="itemReturnDialogVm.provisionalAcceptReason">
                             </textarea>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>