<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                  <form class="form-horizontal">

                    <div class="form-group">

                        <label class="col-sm-3 control-label">Name <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">

                        <input type="text" class="form-control" name="title"
                                       ng-model="newVaultVm.newVault.name">

                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Description <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newVaultVm.newVault.description"></textarea>
                        </div>
                    </div>

                </form>

                <br><br>
            </div>
        </div>
    </div>
</div>
